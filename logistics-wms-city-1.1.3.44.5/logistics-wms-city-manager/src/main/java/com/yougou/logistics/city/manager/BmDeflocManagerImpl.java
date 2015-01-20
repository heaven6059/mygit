package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BmDeflocService;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("bmDeflocManager")
class BmDeflocManagerImpl extends BaseCrudManagerImpl implements BmDeflocManager {
    @Resource
    private BmDeflocService bmDeflocService;
    
	@Resource
	private BsWorkerLocManager bsWorkerLocManager;

    @Override
    public BaseCrudService init() {
        return bmDeflocService;
    }
    
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteFefloc(String locnoStrs) throws ManagerException {
		int a = 0 ;
		if(StringUtils.isNotBlank(locnoStrs)){
			String [] strs = locnoStrs.split(",");
			for(String obj : strs){
				try{
					a += bmDeflocService.deleteFefloc(String.valueOf(obj).trim());
				}catch (Exception e) {
		            throw new ManagerException(e);
		        }
			}
		}
		return a;
	}
	
	@Override
	public boolean findIsLocUser(String locnoStrs) throws ManagerException {
		int a = 0 ;
		if(StringUtils.isNotBlank(locnoStrs)){
			String [] strs = locnoStrs.split(",");
			for(String obj : strs){
				Map<String,Object>  parMap = new  HashMap<String,Object>(0);
				try{
					parMap.put("locNo", String.valueOf(obj).trim());
					int c = bsWorkerLocManager.findCount(parMap);
					if(c > 0){
						a++;
					}
				}catch (Exception e) {
		            throw new ManagerException(e);
		        }
			}
		}
		return (a > 0) ? false:true;
	}
}