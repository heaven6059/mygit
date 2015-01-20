package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillImImportDtlSizeKind;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-25 10:24:56
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public interface BillImImportDtlManager extends BaseCrudManager {

    public <ModelType> Map<String, Object> addImImportDetail(String locno,
	    String ownerNo, String importNo,
	    Map<CommonOperatorEnum, List<ModelType>> params, String loginName,String username)
	    throws ManagerException;

    public Map<String, Object> selectBoxNoByDetailPageCount(int page,
	    int pageSize, String orderByField, String orderBy,
	    Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException;

    public Map<String, Object> findDetailByImportNo(int page, int pageSize,
	    String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
	    throws ManagerException;

    public int selectCountMx(BillImImportDtlSizeKind dto,AuthorityParams authorityParams)
	    throws ManagerException;

    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListByImportNo(
	    BillImImportDtlSizeKind dto) throws ManagerException;

    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListGroupBy(
	    SimplePage page, BillImImportDtlSizeKind dto,AuthorityParams authorityParams)
	    throws ManagerException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams);
}