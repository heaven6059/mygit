package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportKey;
import com.yougou.logistics.city.common.model.Supplier;
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
public interface BillImImportService extends BaseCrudService {

    /**
     * 删除预到货通知单
     * 
     * @param locnoStrs
     * @return
     * @throws ManagerException
     */
    public int deleteImImport(BillImImportKey billImImportKey)
	    throws ServiceException;
    
    /**
     * 删除预到货通知单
     * @param billImImport
     * @return
     * @throws ServiceException
     */
    public int deleteByPrimarayKeyForModel(BillImImport billImImport)
    	    throws ServiceException;

    public int findImpoertNoCount(Map map,AuthorityParams authorityParams) throws ServiceException;

    public List<?> findImportNoByPage(SimplePage page, @Param("params") Map map,AuthorityParams authorityParams)
	    throws ServiceException;

    /**
     * 查询已经审核的通知单的供应商
     * 
     * @param imp
     * @return
     * @throws ServiceException
     */
    public List<Supplier> findSupplierNo(BillImImport imp,AuthorityParams authorityParams)
	    throws ServiceException;

    public BillImImport selectByPrimaryKey(BillImImport billImImport)
	    throws ServiceException;

    /**
     * 是否可以手动关闭(查询收货单)
     * 
     * @param billImImport
     * @return
     * @throws ServiceException
     */
    public boolean isCanOverFlocByReceipt(BillImImport billImImport)
	    throws ServiceException;

    /**
     * 是否可以手动关闭(查询分货单)
     * 
     * @param billImImport
     * @return
     * @throws ServiceException
     */
    public boolean isCanOverFlocByDivide(BillImImport billImImport)
	    throws ServiceException;

    /**
     * 是否可以手动关闭(查询验收单)
     * 
     * @param billImImport
     * @return
     * @throws ServiceException
     */
    public boolean isCanOverFlocByCheck(BillImImport billImImport)
	    throws ServiceException;

    public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
}