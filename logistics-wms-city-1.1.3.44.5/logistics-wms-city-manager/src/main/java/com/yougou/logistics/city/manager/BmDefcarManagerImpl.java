package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefcarKey;
import com.yougou.logistics.city.service.BmDefcarService;

/**
 * 
 * 车辆管理manager实现
 * 
 * @author qin.dy
 * @date 2013-9-23 下午7:06:47
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bmDefcarManager")
class BmDefcarManagerImpl extends BaseCrudManagerImpl implements BmDefcarManager {
    @Resource
    private BmDefcarService bmDefcarService;

    @Override
    public BaseCrudService init() {
        return bmDefcarService;
    }
    
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteFefloc(String locnoStrs) throws ManagerException {
		int a = 0 ;
		if(StringUtils.isNotBlank(locnoStrs)){
			String [] strs = locnoStrs.split(",");
			for(String str : strs){
				String[] id = str.split("-");
				if(id!=null && id.length==2){
					BmDefcarKey key = new BmDefcarKey();
					key.setLocno(id[0]);
					key.setCarNo(id[1]);
					try{
						a += bmDefcarService.deleteById(key);
					}catch (Exception e) {
			            throw new ManagerException(e);
			        }
				}
				
			}
		}
		return a;
	}
}