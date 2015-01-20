package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillChRequestDtl;

public interface BillChRequestDtlManager extends BaseCrudManager {
	public int findCountForJoinItem(Map<String,Object> params)throws ManagerException;
	public  List<BillChRequestDtl> findForJoinItemByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ManagerException;
}