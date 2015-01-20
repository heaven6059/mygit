package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillChRequestDtl;

public interface BillChRequestDtlService extends BaseCrudService {
	public int findCountForJoinItem(Map<String,Object> params)throws ServiceException;
	public  List<BillChRequestDtl> findForJoinItemByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ServiceException;
}