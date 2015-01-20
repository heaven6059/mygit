package com.yougou.logistics.city.manager;

import java.util.List;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SizeInfo;

public interface SizeInfoManager extends BaseCrudManager {
    public List<SizeInfo> selectSizeInfoBySizeNoList(List<String> sizeKindList,
	    String sysNo,AuthorityParams authorityParams) throws ManagerException;
    
    public List<SizeInfo> findSizeInfoBySizeNo (SizeInfo sizeInfo,AuthorityParams authorityParams) throws ManagerException;
}