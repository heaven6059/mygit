package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.SysDefcontainer;
import com.yougou.logistics.city.service.SysDefcontainerService;

/**
 * 容器资料manager实现
 * 
 * @author qin.dy
 * @date 2013-9-22 下午3:03:15
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("sysDefcontainerManager")
class SysDefcontainerManagerImpl extends BaseCrudManagerImpl implements SysDefcontainerManager {
    @Resource
    private SysDefcontainerService sysDefcontainerService;

    @Override
    public BaseCrudService init() {
        return sysDefcontainerService;
    }
    @Override
   	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
   	public int deleteFefloc(String locnoStrs) throws ManagerException {
   		int a = 0 ;
   		if(StringUtils.isNotBlank(locnoStrs)){
   			String [] strs = locnoStrs.split(",");
   			for(String str : strs){
   				String[] id = str.split("-");
   				if(id!=null && id.length==3){
   					SysDefcontainer key = new SysDefcontainer();
   					key.setLocno(id[0]);
   					key.setContainerType(id[1]);
   					key.setUseType(id[2]);
   					try{
   						a += sysDefcontainerService.deleteById(key);
   					}catch (Exception e) {
   			            throw new ManagerException(e);
   			        }
   				}
   				
   			}
   		}
   		return a;
   	}
}