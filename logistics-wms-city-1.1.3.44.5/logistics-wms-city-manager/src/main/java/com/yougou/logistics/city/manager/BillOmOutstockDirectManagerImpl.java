package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmOutstockDirectForQuery;
import com.yougou.logistics.city.service.BillOmOutstockDirectService;
import com.yougou.logistics.city.service.SystemUserService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-09 11:09:10
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
@Service("billOmOutstockDirectManager")
class BillOmOutstockDirectManagerImpl extends BaseCrudManagerImpl implements BillOmOutstockDirectManager {
    @Resource
    private BillOmOutstockDirectService billOmOutstockDirectService;
    
    @Resource
    private SystemUserService systemUserService;

    @Override
    public BaseCrudService init() {
        return billOmOutstockDirectService;
    }
    
    

	@Override
	public int findOutstockDirectByStoreCount(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockDirectService.findOutstockDirectByStoreCount(vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}



	@Override
	public List<BillOmOutstockDirect> findOutstockDirectByStoreByPage(SimplePage page, String orderByField,
			String orderBy, BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDirectService.findOutstockDirectByStoreByPage(page, orderByField, orderBy, vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}



	@Override
	public SumUtilMap<String, Object> selectOutstockDirectByStoreSumQty(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockDirectService.selectOutstockDirectByStoreSumQty(vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}



	@Override
	public int findOutstockDirectByAreaCount(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockDirectService.findOutstockDirectByAreaCount(vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}



	@Override
	public List<BillOmOutstockDirect> findOutstockDirectByAreaByPage(SimplePage page, String orderByField,
			String orderBy, BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDirectService.findOutstockDirectByAreaByPage(page, orderByField, orderBy, vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}



	@Override
	public SumUtilMap<String, Object> selectOutstockDirectByAreaSumQty(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDirectService.selectOutstockDirectByAreaSumQty(vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}



	@Override
	public List<BillOmOutstockDirect> queryLocateNoByExpType(String expType,String locno, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			BillOmOutstockDirectForQuery vo = new  BillOmOutstockDirectForQuery();
			vo.setExpType(expType);
			vo.setLocno(locno);
			return billOmOutstockDirectService.queryLocateNoByExpType(vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryOperateTypeByParam(
			BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDirectService.queryOperateTypeByParam(vo, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryAreaInfoByParam(
			BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams, String orderByField, String orderBy) throws ManagerException {
		try{
			return billOmOutstockDirectService.queryAreaInfoByParam(vo, authorityParams, orderByField, orderBy);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryStoreInfoByParam(
			BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams, String orderByField, String orderBy) throws ManagerException {
		try{
			return billOmOutstockDirectService.queryStoreInfoByParam(vo, authorityParams, orderByField, orderBy);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryOutstockDirectByStore(
			BillOmOutstockDirectForQuery vo) throws ManagerException {
		try{
			return billOmOutstockDirectService.queryOutstockDirectByStore(vo);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryOutstockDirectByArea(
			BillOmOutstockDirectForQuery vo) throws ManagerException {
		try{
			return billOmOutstockDirectService.queryOutstockDirectByArea(vo);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<SystemUser> getUserListByRoleId(SystemUser systemUser)
			throws ManagerException {
		try{
			return systemUserService.querySystemUserListByRoleId(systemUser);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public void procOmOutStockDirect(BillOmOutstockDirectForQuery vo)
			throws ManagerException {
		try{
			 if(StringUtils.isNotBlank(vo.getLocno()) && StringUtils.isNotBlank(vo.getLocateNo()) && StringUtils.isNotBlank(vo.getBatchNo())
				  &&StringUtils.isNotBlank(vo.getOperateType()) && StringUtils.isNotBlank(vo.getExpType()) && StringUtils.isNotBlank(vo.getSortType()) 
				  && CommonUtil.hasValue(vo.getDlist())){
				 billOmOutstockDirectService.procOmOutStockDirect(vo);
			 }else{
				 throw new ManagerException("参数非法！");
			 }
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void procOmPlanOutStockDirect(BillOmOutstockDirectForQuery vo) throws ManagerException {
		try{
			 if(StringUtils.isNotBlank(vo.getLocno()) 
				&& StringUtils.isNotBlank(vo.getWareNo()) 
				&& StringUtils.isNotBlank(vo.getAreaNo())
				&& StringUtils.isNotBlank(vo.getOutstockPeople()) 
				&& StringUtils.isNotBlank(vo.getCreator()) 
				&& StringUtils.isNotBlank(vo.getCellType())){
				 billOmOutstockDirectService.procOmPlanOutStockDirect(vo);
			 }else{
				 throw new ManagerException("参数非法！");
			 }
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findOutstockDirectCount(BillOmOutstockDirect billOmOutstockDirect, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDirectService.findOutstockDirectCount(billOmOutstockDirect, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> findOutstockDirectByPage(SimplePage page,
			BillOmOutstockDirect billOmOutstockDirect, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDirectService.findOutstockDirectByPage(page, billOmOutstockDirect, authorityParams);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
	@Override
	public SumUtilMap<String, Object> selectSumQty(BillOmOutstockDirect billOmOutstockDirect, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmOutstockDirectService.selectSumQty(billOmOutstockDirect, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public int findHmPlanCmDefareaCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException{
		try {
			return billOmOutstockDirectService.findHmPlanCmDefareaCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<CmDefarea> findHmPlanCmDefareaByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmOutstockDirectService.findHmPlanCmDefareaByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	@Override
	public List<BillOmOutstockDirect> queryLocateNoByMore(String expType,
			String locno, String locateNo, String expNo, String poNo,String brandNo, String sysNo, AuthorityParams authorityParams)
			throws ManagerException {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("expType", expType);
		map.put("locno", locno);
		map.put("locateNo", locateNo);
		map.put("expNo", expNo);
		map.put("poNo", poNo);
		map.put("brandNo", brandNo);
		map.put("sysNo", sysNo);
		try {
			return billOmOutstockDirectService.queryLocateNoByMore(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}