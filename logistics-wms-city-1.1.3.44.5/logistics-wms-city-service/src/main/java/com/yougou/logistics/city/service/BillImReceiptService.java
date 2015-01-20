package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportDtl;
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
public interface BillImReceiptService extends BaseCrudService {

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
	    BillImReceipt receipt, AuthorityParams authorityParams) throws ServiceException;

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
    public int selectCountMx(BillImReceipt receipt, AuthorityParams authorityParams) throws ServiceException;

    /**
     * 保存主表信息
     * 
     * @param receipt
     * @param insertDetail
     * @throws ServiceException
     */
    public void saveMain(BillImReceipt receipt) throws ServiceException;

    /**
     * 新增
     * 
     * @param receipt
     * @param detailMap
     */
    public void save(BillImReceipt receipt,
	    List<BillImImportDtlDto> insertDetail) throws ServiceException;

    /**
     * 修改
     * 
     * @param receipt
     * @param detailMap
     * @return TODO
     */
    public Map<String, Object> update(BillImReceipt receipt,
	    List<BillImImportDtlDto> insert, List<BillImImportDtlDto> update,
	    List<BillImImportDtlDto> del,SystemUser user) throws ServiceException;

    // 删除明细
    public void delDtal(BillImReceipt receipt, BillImImportDtl dtl)
	    throws ServiceException;

    public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

    public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams)
	    throws ServiceException;

    public List<BillImReceipt> findMainReciptSum(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams)
    	    throws ServiceException;
    
    public String selectIsReceiptByImportNo(String ImportNo)
	    throws ServiceException;

    /**
     * 查询未验货的收货单
     * 
     * @param page
     * @param map
     * @param authorityParams TODO
     * @return
     * @throws ServiceException
     */
    public List<BillImReceipt> findReciptNoChecked(
	    @Param("page") SimplePage page, @Param("params") Map<?, ?> map, AuthorityParams authorityParams)
	    throws ServiceException;

    public int findReciptNoCheckedCount(@Param("page") SimplePage page,
	    @Param("params") Map<?, ?> map, AuthorityParams authorityParams) throws ServiceException;

    /**
     * 确认收货
     * 
     * @param receipt
     */
    public int auditReceipt(BillImReceipt receipt) throws ServiceException;

    public int selectCount4Direct(Map map) throws ServiceException;

    public List<BillImImport> selectByPage4Direct(SimplePage page, Map map)
	    throws ServiceException;

    public List<BillImImportDtlDto> selectBatchSelectBox(
	    List<String> importNoList, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException;

    /**
     * 打印收货单
     * @param map
     * @param authorityParams
     * @param brand 
     * @return
     * @throws ServiceException
     */
	public List<BillImReceipt> findImReceiptPrint(Map<?, ?> map,
			AuthorityParams authorityParams)throws ServiceException;
	
	public void addDtal(BillImReceipt receipt, BillImImportDtl dtl) throws ServiceException;
	
	public void addReceiptDtl(BillImReceipt receipt,List<BillImImportDtl> list,String quality,int rowId)  throws ServiceException ;

}