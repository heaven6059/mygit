package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.model.SystemUser;
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
public interface BillImImportManager extends BaseCrudManager {

    /**
     * 删除预到货通知单
     * 
     * @param locnoStrs
     * @return
     * @throws ManagerException
     */
    public boolean deleteImImport(String noStrs,AuthorityParams authorityParams) throws ManagerException;

    /**
     * 审核预到货通知单
     * 
     * @param importNo
     * @param locno
     * @param ownerNo
     * @return
     * @throws ManagerException
     */
    public Map<String, Object> auditImImport(String noStrs,SystemUser user,AuthorityParams authorityParams) throws ManagerException;
    
    /**
     * 修改预到货通知单
     * @param billImImport
     * @param user
     * @param authorityParams
     * @return
     * @throws ManagerException
     */
    public boolean updateImImport(BillImImport billImImport,SystemUser user, AuthorityParams authorityParams) throws ManagerException;

    /**
     * 新增预到货通知单
     * 
     * @param billImImport
     * @return
     * @throws ManagerException
     */
    public Map<String, Object> addBillImImport(BillImImport billImImport)
	    throws ManagerException;

    public int findImpoertNoCount(Map map,AuthorityParams authorityParams) throws ManagerException;

    public List<?> findImportNoByPage(SimplePage page, Map map,AuthorityParams authorityParams)
	    throws ManagerException;

    public Map<String, Object> selectIsReceiptByImportNo(String ImportNo)
	    throws ManagerException;

    public Map<String, Object> overImImport(String importNo, String locno,
	    String ownerNo) throws ManagerException;

    public List<Supplier> findSupplierNo(BillImImport imp,AuthorityParams authorityParams)
	    throws ManagerException;

    public int overFlocByDif(String keyStr, String locno, String ownerNo,
	    String auditor,String auditorName,AuthorityParams authorityParams) throws ManagerException;

    public void bathUpload(String locno,String ownerNo, String importNoStr,AuthorityParams authorityParams)
	    throws ManagerException;

    public Map<String, Object> findSumQty(Map<String, Object> params,AuthorityParams authorityParams);
    
    /**
     * 批量收货
     * @param listIm
     * @param user
     * @return
     * @throws ServiceException
     */
    public boolean toBillimReceipt(List<BillImImport> listIm,SystemUser user )throws ManagerException;
}