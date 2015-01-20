package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ProcCommonService;

/**
 * 容器资料manager实现
 * 
 * @author qin.dy
 * @date 2013-9-22 下午3:03:15
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bmContainerManager")
class BmContainerManagerImpl extends BaseCrudManagerImpl implements BmContainerManager {
    @Resource
    private BmContainerService bmContainerService;
    @Resource
    private ProcCommonService procCommonService;
    
    @Override
    public BaseCrudService init() {
        return bmContainerService;
    }
    @Override
   	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
   	public int deleteFefloc(String locnoStrs,SystemUser user) throws ManagerException {
   		int a = 0 ;
   		if(StringUtils.isNotBlank(locnoStrs)){
   			String [] strs = locnoStrs.split(",");
   			for(String str : strs){
   				String[] id = str.split("-");
   				if(id!=null && id.length==2){
   					BmContainer key = new BmContainer();
   					key.setLocno(id[0]);
   					key.setConNo(id[1]);
   					key.setIsDeleted("1");
   					key.setEditor(user.getLoginName());
   					key.setEdittm(new Date());
   					try{
//   						a += bmContainerService.deleteById(key);
	   						bmContainerService.modifyById(key);
	   						a=1;
   					}catch (Exception e) {
   			            throw new ManagerException(e);
   			        }
   				}
   				
   			}
   		}
   		return a;
   	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public void addEntity(BmContainer bmContainer)throws ManagerException{
    	try {
			String conType = bmContainer.getType();
			String containerMetrial = "";
			if (conType.equals("C"))
				containerMetrial = "21";
			else
				containerMetrial = "31";
    		String locno=bmContainer.getLocno();
    		String userId=bmContainer.getCreator();
			Map<String, String>  tmp=procCommonService.procGetContainerNoBase(locno,conType,userId,"T","1","0",containerMetrial);
			String conNo = tmp.get("strOutLabelNo");
			bmContainer.setConNo(conNo);
			bmContainerService.add(bmContainer);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
    }
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public int batchUpdate(List<BmContainer> bmContainer)throws ManagerException{
    	try {
    		return bmContainerService.batchUpdate(bmContainer);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
    }
	public boolean checkBmContainerStatus(BmContainer bmContainer)throws ManagerException{
		try {
			return bmContainerService.checkBmContainerStatus(bmContainer);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}