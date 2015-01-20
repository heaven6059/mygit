package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmDivideService;
import com.yougou.logistics.city.service.BillOmRecheckService;

/**
 * 
 * 分货复核单manage实现
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:20:20
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmRecheckManager")
class BillOmRecheckManagerImpl extends BaseCrudManagerImpl implements BillOmRecheckManager {
    @Resource
    private BillOmRecheckService billOmRecheckService;
    
    @Resource
    private BillOmDivideService billOmDivideService;
    
    @Override
    public BaseCrudService init() {
        return billOmRecheckService;
    }

	@Override
	public List<BillOmDivide> selectDivideCollectByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDivideService.selectDivideCollectByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectDivideCollectCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDivideService.selectDivideCollectCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Store> selectStoreByParam(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDivideService.selectStoreByParam(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmDivideDtl> queryRecheckBoxItem(Map<String, Object> params) throws ManagerException {
		try {
			return billOmDivideService.queryRecheckBoxItem(params);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void packageBox(List<BillOmDivideDtl> dtlLst, BillOmRecheck billOmRecheck, String boxNo)
			throws ManagerException {
		try {
			billOmRecheckService.packageBox(dtlLst,billOmRecheck,boxNo);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
	}

	@Override
	public void check(String ids, SystemUser user,String checkUser) throws ManagerException {
		try {
//			if(StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(checkUser)){
//				String[] idArr = ids.split(",");
//				for(String id : idArr){
//					String[] tmp = id.split("-");
//					if(tmp.length==2){
//						BillOmRecheck recheck = new BillOmRecheck();
//						recheck.setRecheckNo(tmp[0]);
//						recheck.setLocno(tmp[1]);
//						recheck.setCreator(user);
//						billOmRecheckService.check(recheck);
//					}
//				}
//			}
			billOmRecheckService.check(ids,user,checkUser);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void deleteBillOmRecheck(List<BillOmRecheck> listOmRechecks) throws ManagerException {
		try {
			billOmRecheckService.deleteBillOmRecheck(listOmRechecks);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void packageBoxOutstock(List<BillOmOutstockDtlDto> dtlLst, BillOmRecheck billOmRecheck, String boxNo)
			throws ManagerException {
		try {
			billOmRecheckService.packageBoxOutstock(dtlLst,billOmRecheck,boxNo);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void checkOutStock(String ids, SystemUser user, String checkUser) throws ManagerException {
		try {
			billOmRecheckService.checkOutStock(ids, user, checkUser);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void deleteBillOmOutStockRecheck(List<BillOmRecheck> listOmRechecks) throws ManagerException {
		try {
			billOmRecheckService.deleteBillOmOutStockRecheck(listOmRechecks);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void packageBoxRf(BillOmRecheck billOmRecheck) throws ManagerException {
		try {
			billOmRecheckService.packageBoxRf(billOmRecheck);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void packageBoxOutstockRf(BillOmRecheck billOmRecheck) throws ManagerException {
		try {
			billOmRecheckService.packageBoxOutstockRf(billOmRecheck);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findRecheckBoxItemCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckService.findRecheckBoxItemCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillOmDivideDtl> findRecheckBoxItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckService.findRecheckBoxItemByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckService.selectSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectRecheckSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmRecheckService.selectRecheckSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectOutstockRecheckSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmRecheckService.selectOutstockRecheckSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findOutstockRecheckCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmRecheckService.findOutstockRecheckCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillOmRecheck> findOutstockRecheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmRecheckService.findOutstockRecheckByPage(page, orderByField, orderBy, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findCount4Source(Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmRecheckService.findCount4Source(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillOmRecheck> findByPage4Source(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmRecheckService.findByPage4Source(page, orderByField, orderBy, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
}