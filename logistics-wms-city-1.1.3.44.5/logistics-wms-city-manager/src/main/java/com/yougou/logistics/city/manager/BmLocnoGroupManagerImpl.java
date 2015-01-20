package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmLocnoGroup;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.service.BmLocnoGroupService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;


/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-11-07 10:46:51
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
@Service("bmLocnoGroupManager")
class BmLocnoGroupManagerImpl extends BaseCrudManagerImpl implements BmLocnoGroupManager {
    @Resource
    private BmLocnoGroupService bmLocnoGroupService;

    @Override
    public BaseCrudService init() {
        return bmLocnoGroupService;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public Map<String, Object> addLocnoGroup(BmLocnoGroup bmLocnoGroup)throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//是否有重复记录
			Map<String, Object> objParams = new HashMap<String, Object>();
			objParams.put("sysNo", bmLocnoGroup.getSysNo());
			objParams.put("locno", bmLocnoGroup.getLocno());
			//objParams.put("groupCode", bmLocnoGroup.getGroupCode());
			objParams.put("businessType", bmLocnoGroup.getBusinessType());
			int a = bmLocnoGroupService.findCount(objParams);
			if(a > 0){
				throw new ManagerException("当前品牌库下业务类型的仓库组别已经存在，不能重复添加！");
			}
			bmLocnoGroup.setCreatetm(DateUtil.getCurrentDateTime());
			bmLocnoGroup.setEdittm(DateUtil.getCurrentDateTime());
			if(StringUtils.isBlank(bmLocnoGroup.getOwnerNo())){
				bmLocnoGroup.setOwnerNo("BL");
			}
		    int u = bmLocnoGroupService.add(bmLocnoGroup);
		    if (u < 1) {
		    	throw new ManagerException("新增时未更新到记录！");
		    }
		    obj.put("flag", "success");
		    obj.put("locno", bmLocnoGroup.getLocno());
		} catch (Exception e) {
		    throw new ManagerException(e.getMessage());
		}
		return obj;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public Map<String, Object> modifyLocnoGroup(BmLocnoGroup bmLocnoGroup)throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//是否有重复记录
			bmLocnoGroup.setEdittm(DateUtil.getCurrentDateTime());
			if(StringUtils.isBlank(bmLocnoGroup.getOwnerNo())){
				bmLocnoGroup.setOwnerNo("BL");
			}
		    int u = bmLocnoGroupService.modifyById(bmLocnoGroup);
			if(u != 1){
				throw new ManagerException("未更新到任何数据！");
			}
		    obj.put("flag", "success");
		} catch (Exception e) {
		    throw new ManagerException(e.getMessage());
		}
		return obj;
    }
    

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> deleteLocnoGroup(String locnogroupStrs) throws ManagerException{
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			if(StringUtils.isNotBlank(locnogroupStrs)){
				String [] strs = locnogroupStrs.split(",");
				BmLocnoGroup bmLocnoGroup = new  BmLocnoGroup();
				for(String objEnt : strs){
					String [] bmLocnoGroups = objEnt.split("#");
					bmLocnoGroup.setLocno(bmLocnoGroups[0]);
					bmLocnoGroup.setSysNo(bmLocnoGroups[1]);
					bmLocnoGroup.setGroupCode(bmLocnoGroups[2]);
					bmLocnoGroup.setBusinessType(bmLocnoGroups[3]);
					if(StringUtils.isBlank(bmLocnoGroup.getOwnerNo())){
						bmLocnoGroup.setOwnerNo("BL");
					}
				    int a =  bmLocnoGroupService.deleteById(bmLocnoGroup);
				    if(a != 1){
				    	throw new ManagerException("未删除到任何数据！");
				    }
				}
			}
			objMap.put("flag", "success");
		} catch (Exception e) {
		    throw new ManagerException(e.getMessage());
		}
		return objMap;
	}
}