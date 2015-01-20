package com.yougou.logistics.city.service;

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
import com.yougou.logistics.city.common.enums.BillUmUntreadMmDtlStatusEnums;
import com.yougou.logistics.city.common.enums.BillUmUntreadMmStatusEnums;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.model.BillUmUntreadMmDtl;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.dal.mapper.BillUmDirectMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMmDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMmMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:33:10 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmUntreadMmService")
class BillUmUntreadMmServiceImpl extends BaseCrudServiceImpl implements BillUmUntreadMmService {
	@Resource
	private BillUmUntreadMmMapper billUmUntreadMmMapper;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private BillUmUntreadMmDtlMapper billUmUntreadMmDtlMapper;
	@Resource
	private BillUmDirectMapper billUmDirectMapper;

	private static final String CELLNON = "N";

	@Override
	public BaseCrudMapper init() {
		return billUmUntreadMmMapper;
	}

	public void saveMain(BillUmUntreadMm untreadMm) throws ServiceException {
		try {
			//新增
			if (StringUtils.isEmpty(untreadMm.getUntreadMmNo())) {
				String untreadMmNo = procCommonService
						.procGetSheetNo(untreadMm.getLocno(), CNumPre.UM_S_UNTREAD_NO_PRE);
				untreadMm.setUntreadMmNo(untreadMmNo);
				untreadMm.setStatus(BillUmUntreadMmStatusEnums.STATUS10.getStatus());
				billUmUntreadMmMapper.insertSelective(untreadMm);
			} else {//修改
				billUmUntreadMmMapper.updateByPrimaryKeySelective(untreadMm);
			}
		} catch (ServiceException e) {
			throw e;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteUntreadMm(String keyStr, String locno) throws ServiceException {
		try {
			BillUmUntreadMm mm = null;
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					mm = new BillUmUntreadMm();
					mm.setLocno(locno);
					mm.setUntreadMmNo(subKyes[0]);
					mm.setOwnerNo(subKyes[1]);
					billUmUntreadMmMapper.deleteByPrimarayKeyForModel(mm);
					//删除明细
					billUmUntreadMmDtlMapper.deleteAllDetail(mm);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void auditUntreadMm(String keyStr, String locno, String oper) throws ServiceException {

		try {
			BillUmUntreadMm mm = null;
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					mm = new BillUmUntreadMm();
					mm.setLocno(locno);
					mm.setUntreadMmNo(subKyes[0]);
					mm.setOwnerNo(subKyes[1]);
					mm.setStatus(BillUmUntreadMmStatusEnums.STATUS11.getStatus());
					mm.setAuditor(oper);
					mm.setAudittm(new Date());
					mm.setSourceStatus(BillUmUntreadMmDtlStatusEnums.STATUS10.getStatus());
					int count = billUmUntreadMmMapper.updateByPrimaryKeySelective(mm);
					if(count==0){
						throw new ServiceException("单据【"+subKyes[0]+"】已经审核，不能再审核！");
					}
					//更新明细状态
					mm.setStatus(BillUmUntreadMmDtlStatusEnums.STATUS13.getStatus());
					billUmUntreadMmDtlMapper.updateAllDetail(mm);
					//写定位信息
					List<BillUmUntreadMmDtl> dtlList = billUmUntreadMmDtlMapper.selectDetail4Direct(mm);
					for (BillUmUntreadMmDtl mmDtl : dtlList) {
						//查询最大定位rowid
						Integer maxRowId = billUmDirectMapper.selectMaxRowId(mm);
						BillUmDirect direct = new BillUmDirect();
						direct.setLocno(locno);
						direct.setUntreadMmNo(mm.getUntreadMmNo());
						direct.setRowId(maxRowId + 1);
						direct.setSizeNo(mmDtl.getSizeNo());
						direct.setItemNo(mmDtl.getItemNo());
						direct.setEstQty(mmDtl.getItemQty());
						direct.setCellNo(CELLNON);
						direct.setCreatetm(new Date());
						billUmDirectMapper.insertSelective(direct);
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}