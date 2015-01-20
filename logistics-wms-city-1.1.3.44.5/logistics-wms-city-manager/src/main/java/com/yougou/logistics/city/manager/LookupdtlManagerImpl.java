package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.Lookupdtl;
import com.yougou.logistics.city.service.LookupdtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("lookupdtlManager")
class LookupdtlManagerImpl extends BaseCrudManagerImpl implements LookupdtlManager {
    @Resource
    private LookupdtlService lookupdtlService;

    @Override
    public BaseCrudService init() {
        return lookupdtlService;
    }

	@Override
	public List<Lookupdtl> selectOutstockDirectExpType(String  lookupcode,String locno)
			throws ManagerException {
		try{
			Lookupdtl lookupDtl = new Lookupdtl();
			lookupDtl.setLookupcode(lookupcode);
			lookupDtl.setLocno(locno);
			return  lookupdtlService.selectOutstockDirectExpType(lookupDtl);
		}catch(Exception e){
			throw new ManagerException(e);
		}
		
	}

	@Override
	public List<Lookupdtl> selectLookupdtlBySysNo(String lookupcode,
			String sysNo) throws ManagerException {
		try{
			Lookupdtl lookupDtl = new Lookupdtl();
			lookupDtl.setLookupcode(lookupcode);
			lookupDtl.setSysNo(sysNo);
			return  lookupdtlService.selectLookupdtlBySysNo(lookupDtl);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public Map<String, Object> selectLookupdtlByCode(Map<String, Object> params)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			List<Lookupdtl>  lst = lookupdtlService.selectLookupdtlByCode(params);
			for(Lookupdtl  lookupdtl : lst){
				if(!obj.containsKey(lookupdtl.getItemval())){
					obj.put(lookupdtl.getItemval(), lookupdtl.getItemname());
				}
			}
			return  obj;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}
}