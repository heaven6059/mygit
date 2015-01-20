package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BmCircleService;
import com.yougou.logistics.city.service.StoreService;

@Service("bmCircleManager")
class BmCircleManagerImpl extends BaseCrudManagerImpl implements BmCircleManager {
    @Resource
    private BmCircleService bmCircleService;
    
    @Resource
    private StoreService storeService;

    @Override
    public BaseCrudService init() {
        return bmCircleService;
    }

	@Override
	public boolean findIsStore(String circleNoStrs) throws ManagerException {
		int a = 0 ;
		if(StringUtils.isNotBlank(circleNoStrs)){
			String [] strs = circleNoStrs.split(",");
			for(String obj : strs){
				Map<String,Object>  parMap = new  HashMap<String,Object>(0);
				try{
					parMap.put("circle", String.valueOf(obj).trim());
					int c = storeService.findCount(parMap);
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteCircle(String circleNoStrs) throws ManagerException {
		int a = 0 ;
		if(StringUtils.isNotBlank(circleNoStrs)){
			String [] strs = circleNoStrs.split(",");
			for(String obj : strs){
				try{
					a += bmCircleService.deleteCircle(String.valueOf(obj).trim());
				}catch (Exception e) {
		            throw new ManagerException(e);
		        }
			}
		}
		return a;
	}
}