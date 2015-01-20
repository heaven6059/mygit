package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
public interface BillImReceiptManager extends BaseCrudManager {

    /**
     * 
     * 查询与出库单相关联的收货单分页service
     * 
     * @author su.yq
     * @date 2013-10-10 下午08:56:11
     * @version 0.1.0
     * @copyright yougou.com
     * @param authorityParams TODO
     */
    public List<BillImReceipt> findReceiptByPage(SimplePage page,
	    BillImReceipt receipt, AuthorityParams authorityParams) throws ManagerException;

    /**
     * 
     * 查询与出库单相关联的收货单分页总数service
     * 
     * @author su.yq
     * @date 2013-10-10 下午08:56:11
     * @version 0.1.0
     * @copyright yougou.com
     * @param authorityParams TODO
     */
    public int selectCountMx(BillImReceipt receipt, AuthorityParams authorityParams) throws ManagerException;

    /**
     * 保存主表
     * 
     * @param receipt
     * @throws ManagerException
     */
    public void saveMain(BillImReceipt receipt) throws ManagerException;

    /**
     * 新增
     * 
     * @param receipt
     * @param insertDetail
     * @throws ServiceException
     */
    public void save(BillImReceipt receipt,
	    List<BillImImportDtlDto> insertDetail) throws ManagerException;

    /**
     * 修改
     * 
     * @param receipt
     * @param insert
     * @param update
     * @param del
     * @return TODO
     * @throws ServiceException
     */
    public Map<String, Object> update(BillImReceipt receipt,
	    List<BillImImportDtlDto> insert, List<BillImImportDtlDto> update,
	    List<BillImImportDtlDto> del,SystemUser user) throws ManagerException;

    /**
     * 批量审核
     * 
     * @param keyStr
     * @param locno
     * @param ownerNo
     * @param newParam
     *            TODO
     * @return
     * @throws ManagerException
     */
    public int auditBatch(String keyStr, String locno, String ownerNo,SystemUser user) throws ManagerException;

    /**
     * 删除
     * 
     * @param keyStr
     * @return
     * @throws ManagerException
     */
    public int deleteBatch(String keyStr, String locno, String ownerNo, SystemUser user)
	    throws ManagerException;

    /**
     * 收货单查询
     * 
     * @param map
     * @return
     * @throws ServiceException
     */
    public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

    public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams)
	    throws ManagerException;

    public List<BillImReceipt> findMainReciptSum(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams)
    	    throws ManagerException;
    
    public List<BillImReceipt> findReciptNoChecked(
	    @Param("page") SimplePage page, @Param("params") Map<?, ?> map, AuthorityParams authorityParams)
	    throws ManagerException;

    public int findReciptNoCheckedCount(@Param("page") SimplePage page,
	    @Param("params") Map<?, ?> map, AuthorityParams authorityParams) throws ManagerException;

    public int selectCount4Direct(Map map) throws ManagerException;

    public List<BillImImport> selectByPage4Direct(SimplePage page, Map map)
	    throws ManagerException;

    public List<BillImImportDtlDto> findBatchSelectBox(
	    List<String> importNoList, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

    /**
     * 打印收货单
     * @param map
     * @param authorityParams
     * @param brand 
     * @return
     * @throws ManagerException
     */
	public List<BillImReceipt> findImReceiptPrint(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;
}