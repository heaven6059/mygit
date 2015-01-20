package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BmDefownerService;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 委托业主manager实现
 * 
 * @author qin.dy
 * @date 2013-9-22 下午2:00:32
 * @copyright yougou.com
 */
@Service("bmDefownerManager")
class BmDefownerManagerImpl extends BaseCrudManagerImpl implements BmDefownerManager {
    @Resource
    private BmDefownerService bmDefownerService;

    @Override
    public BaseCrudService init() {
        return bmDefownerService;
    }
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteFefloc(String locnoStrs) throws ManagerException {
		int a = 0 ;
		if(StringUtils.isNotBlank(locnoStrs)){
			String [] strs = locnoStrs.split(",");
			for(String obj : strs){
				try{
					a += bmDefownerService.deleteById(obj);
				}catch (Exception e) {
		            throw new ManagerException(e);
		        }
			}
		}
		return a;
	}
}