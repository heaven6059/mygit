package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 07 17:41:16 CST 2014
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
public interface BmPrintLogManager extends BaseCrudManager {
    public List<String> getLabelPrefix(SystemUser user, int qty, String printType, String storeName)
	    throws ManagerException;
    /**
     * 获取完整模板的标签
     * @param user
     * @param printType
     * @param dataStr storeNo_storeName_qty_bufferName
     * @return
     * @throws ManagerException
     */
    public Object getLabel4Full(SystemUser user,String printType,String dataStr)throws ManagerException;
}