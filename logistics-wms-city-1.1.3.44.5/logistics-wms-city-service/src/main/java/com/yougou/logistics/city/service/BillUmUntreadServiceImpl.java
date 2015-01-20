package com.yougou.logistics.city.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.BillUmUntreadDtlStatusEnums;
import com.yougou.logistics.city.common.enums.BillUmUntreadStatusEnums;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
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
@Service("billUmUntreadService")
class BillUmUntreadServiceImpl extends BaseCrudServiceImpl implements BillUmUntreadService {
	@Resource
	private BillUmUntreadMapper billUmUntreadMapper;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private BillUmUntreadDtlMapper billUmUntreadDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billUmUntreadMapper;
	}

	public void saveMain(BillUmUntread untreadMm, SystemUser user) throws ServiceException {
		try {
			Date date = new Date();
			untreadMm.setLocno(user.getLocNo());

			
			
			//新增
			if (StringUtils.isEmpty(untreadMm.getUntreadNo())) {
				
				
				String untreadNo = procCommonService.procGetSheetNo(untreadMm.getLocno(), CNumPre.UM_UNTREAD_NO_PRE);
				untreadMm.setUntreadNo(untreadNo);
				untreadMm.setStatus(BillUmUntreadStatusEnums.STATUS10.getStatus());
				
				untreadMm.setCreatetm(date);
				untreadMm.setCreator(user.getLoginName());
				untreadMm.setCreatorName(user.getUsername());
				untreadMm.setEdittm(date);
				untreadMm.setEditor(user.getLoginName());
				untreadMm.setEditorName(user.getUsername());
				billUmUntreadMapper.insertSelective(untreadMm);
			} else {//修改
				BillUmUntreadKey billUmUntreadKey = new BillUmUntreadKey();
				billUmUntreadKey.setLocno(untreadMm.getLocno());
				billUmUntreadKey.setOwnerNo(untreadMm.getOwnerNo());
				billUmUntreadKey.setUntreadNo(untreadMm.getUntreadNo());
				int isExits = billUmUntreadMapper.judgeObjIsExist(billUmUntreadKey, BillUmUntreadStatusEnums.STATUS10.getStatus());
				//数据异常
				if(0 == isExits){
					throw new ServiceException("单据:" +untreadMm.getUntreadNo() +"已删除或者状态发生改变！");
				}else{
					untreadMm.setEdittm(date);
					untreadMm.setEditor(user.getLoginName());
					untreadMm.setEditorName(user.getUsername());
					billUmUntreadMapper.updateByPrimaryKeySelective(untreadMm);
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteUntread(String keyStr, String locno) throws ServiceException {
		try {
			BillUmUntread mm = null;
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					mm = new BillUmUntread();
					mm.setLocno(locno);
					mm.setUntreadNo(subKyes[0]);
					mm.setOwnerNo(subKyes[1]);
					int count = billUmUntreadMapper.judgeObjIsExist(mm, BillUmUntreadStatusEnums.STATUS10.getStatus());
					if (count==0){
						throw new ServiceException("单据 :" +mm.getUntreadNo() +"已删除或者状态发生改变！");
					}
					else{
						billUmUntreadMapper.deleteByPrimarayKeyForModel(mm);
						//删除明细
						billUmUntreadDtlMapper.deleteAllDetail(mm);
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void auditUntread(String keyStr, SystemUser user) throws ServiceException {

		try {
			Date date = new Date();
			String locno = user.getLocNo();
			String oper = user.getLoginName();
			String auditorName = user.getUsername();
			BillUmUntread mm = null;
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					mm = new BillUmUntread();
					mm.setLocno(locno);
					mm.setUntreadNo(subKyes[0]);
					mm.setOwnerNo(subKyes[1]);
					mm.setStatus(BillUmUntreadStatusEnums.STATUS11.getStatus());
					mm.setAuditor(oper);
					mm.setAudittm(date);
					mm.setAuditorName(auditorName);
					mm.setSourceStatus(BillUmUntreadStatusEnums.STATUS10.getStatus());
					int count = billUmUntreadMapper.updateByPrimaryKeySelective(mm);
					if(count==0){
						 throw new ServiceException("单据【"+subKyes[0]+"】已经审核，不能再审核！");
					}
					//更新明细状态
					mm.setStatus(BillUmUntreadDtlStatusEnums.STATUS13.getStatus());
					billUmUntreadDtlMapper.updateAllDetail(mm);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillUmUntread> findUntread2CheckTask(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, List<BillUmUntread> list, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billUmUntreadMapper.selectUntread2CheckTask(page, orderByField, orderBy, params, list, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int batchUpdateUntreadStatus(Map<String, Object> params,List<BillUmUntread> list)throws ServiceException{
		try {
			return billUmUntreadMapper.batchUpdateUntreadStatus(params, list);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

    @Override
    public Map<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
        try {
            return billUmUntreadMapper.selectSumQty(params, authorityParams);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

	@Override
	public int judgeObjIsExist(BillUmUntreadKey billUmUntreadKey, String status)
			throws ServiceException {
		return billUmUntreadMapper.judgeObjIsExist(billUmUntreadKey,status);
	}
}