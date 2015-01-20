package com.yougou.logistics.city.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Oct 14 14:47:37 CST 2013
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
@Service("billOmOutstockService")
class BillOmOutstockServiceImpl extends BaseCrudServiceImpl implements BillOmOutstockService {

	@Resource
	private BillOmOutstockMapper billOmOutstockMapper;
	
	private static final String STATUS90 = "90";
	private static final String STATUS10 = "10";

	@Override
	public BaseCrudMapper init() {
		return billOmOutstockMapper;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findMoveStockCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockMapper.selectMoveStockCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstock> findMoveStockByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockMapper.selectMoveStockByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void queryBill(List<BillOmOutstock> listOutstocks, SystemUser user) throws ServiceException {
		if(!CommonUtil.hasValue(listOutstocks)){
			throw new ServiceException("参数非法");
		}
		Date date = new Date();
		String auditor = user.getLoginName();
		String auditorname = user.getUsername();
		for (BillOmOutstock b : listOutstocks) {
			b.setStatus(STATUS90);
			b.setUpdStatus(STATUS10);
			b.setAuditor(auditor);
			b.setAuditorname(auditorname);
			b.setAudittm(date);
			b.setEditor(auditor);
			b.setEditorname(auditorname);
			b.setEdittm(date);
			int count = billOmOutstockMapper.updateByPrimaryKeySelective(b);
			if(count < 1){
				//throw new ServiceException(b.getOutstockNo()+"更新状态移库完成失败!");
				throw new ServiceException("单据"+b.getOutstockNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
			}
		}
	}

	@Override
	public SumUtilMap<String, Object> selectImmediateMoveSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmOutstockMapper.selectImmediateMoveSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billOmOutstockMapper.selectSumQty(params, authorityParams);
	}
	
}