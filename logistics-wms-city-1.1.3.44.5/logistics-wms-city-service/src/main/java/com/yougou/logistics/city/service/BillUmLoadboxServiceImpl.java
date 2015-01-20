package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.BillUmCheckStatusEnums;
import com.yougou.logistics.city.common.enums.BillUmUntreadDtlStatusEnums;
import com.yougou.logistics.city.common.enums.BillUmUntreadMmStatusEnums;
import com.yougou.logistics.city.common.enums.BillUmUntreadStatusEnums;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmLoadbox;
import com.yougou.logistics.city.common.model.BillUmLoadboxDtl;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.dal.mapper.BillUmCheckMapper;
import com.yougou.logistics.city.dal.mapper.BillUmLoadboxDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmLoadboxMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMmMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmLoadboxService")
class BillUmLoadboxServiceImpl extends BaseCrudServiceImpl implements BillUmLoadboxService {
	@Resource
	private BillUmLoadboxMapper billUmLoadboxMapper;

	@Resource
	private BillUmLoadboxDtlMapper billUmLoadboxDtlMapper;

	@Resource
	private BillUmUntreadMmMapper billUmUntreadMmMapper;

	@Resource
	private BillUmCheckMapper billUmCheckMapper;

	@Resource
	private ProcCommonService procCommonService;

	@Override
	public BaseCrudMapper init() {
		return billUmLoadboxMapper;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void createLoadBox(String strKey, BillUmUntreadMm untreadMm, SystemUser user) throws ServiceException {

		try {
			Date date = new Date();
			
			if (StringUtils.isNotEmpty(strKey)) {
				String[] strKeys = strKey.split(",");
				//生成主档信息
				BillUmLoadbox box = new BillUmLoadbox();
				box.setLocno(untreadMm.getLocno());
				box.setOwnerNo(untreadMm.getOwnerNo());
				String loadboxNo = procCommonService.procGetSheetNo(untreadMm.getLocno(), CNumPre.UM_LOADBOX_NO_PRE);
				box.setLoadboxNo(loadboxNo);
				box.setStatus(BillUmUntreadMmStatusEnums.STATUS10.getStatus());
				box.setItemType(untreadMm.getUntreadType());
				box.setCreator(user.getLoginName());
				box.setCreatorName(user.getUsername());
				box.setCreatetm(date);
				billUmLoadboxMapper.insertSelective(box);
				List<BillUmCheck> list = new ArrayList<BillUmCheck>();
				for (String str : strKeys) {
					BillUmCheck check = new BillUmCheck();
					check.setCheckNo(str);
					check.setLocno(untreadMm.getLocno());
					check.setOwnerNo(untreadMm.getOwnerNo());
					check.setLoadboxNo(loadboxNo);
					//退仓装箱时 检查退仓验收状态 防止与分配上架退仓任务重复
					check.setCheckFlagStatus("Y");
					list.add(check);
					check.setStatus(BillUmCheckStatusEnums.STATUS20.getStatus());
					//回写验收单的状态和装箱单号
					int count = billUmCheckMapper.updateByPrimaryKeySelective(check);
					if(count==0){
						throw new ServiceException("验收单【"+str+"】已经装箱!");
					}
				}
				List<BillUmLoadboxDtl> boxList = billUmLoadboxDtlMapper.selectLoadBoxTask(untreadMm, list);
				Integer maxRowId = billUmLoadboxDtlMapper.selectMaxRowId(box);
				for (BillUmLoadboxDtl dtl : boxList) {
					dtl.setStatus(BillUmUntreadDtlStatusEnums.STATUS10.getStatus());
					dtl.setLocno(untreadMm.getLocno());
					dtl.setLoadboxNo(loadboxNo);
					dtl.setOwnerNo(untreadMm.getOwnerNo());
					dtl.setRowId(++maxRowId);
					billUmLoadboxDtlMapper.insertSelective(dtl);
				}
				
			} else {
				throw new ServiceException("参数为空！");
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void auditLoadBox(String keyStr, String locno, String oper) throws ServiceException {
		try {
			BillUmLoadbox mm = null;
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					mm = new BillUmLoadbox();
					mm.setLocno(locno);
					mm.setLoadboxNo(subKyes[0]);
					mm.setOwnerNo(subKyes[1]);
					mm.setStatus(BillUmUntreadStatusEnums.STATUS11.getStatus());
					mm.setAuditor(oper);
					mm.setAudittm(new Date());
					billUmLoadboxMapper.updateByPrimaryKeySelective(mm);
					//更新明细状态
					mm.setStatus(BillUmUntreadDtlStatusEnums.STATUS13.getStatus());
					billUmLoadboxMapper.updateAllDetail(mm);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String selectQuaByLoadBoxNo(String loadboxNo)
			throws ServiceException {
		try {
			return  billUmLoadboxMapper.selectQuaByLoadBoxNo(loadboxNo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}