package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;

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
public interface BillImReceiptDtlManager extends BaseCrudManager {
	/**
	 * 详情数量
	 * 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findDetailCount(Map<?, ?> map, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 详情
	 * 
	 * @param page
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillImImportDtlDto> findDetail(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams)
			throws ManagerException;

	public List<BillImReceiptDtl> findAllDetailByReciptNo(BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ManagerException;

	public int findItemNotInReceiptCount(Item item, BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ManagerException;

	public List<BillImReceiptDtl> findItemNotInReceipt(Item item, BillImReceiptDtl dtl, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException;

	public int findDtlByItemNoAndSizeNo(BillImReceiptDtl dtl) throws ManagerException;

	public List<BillImReceiptDtl> selectItemDetail(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ManagerException;

	public int selectItemDetailCount(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询收货单明细可选的箱号,验收明细选择箱号
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> findBillImReceiptDtlBox(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

	public String selectSysNo(Map<String, String> map) throws ManagerException;

	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException;

	public List<BillImReceiptDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ManagerException;

	public List<BillImReceiptDtl> selectDetailBySizeNo(Map<String, Object> map) throws ManagerException;

	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;
	/**
	 * 查询收货单对应的品牌库(多品牌)
	 * @param map
	 * @return
	 * @throws ManagerException
	 */
	public List<String> findSysNoList(Map<String, Object> map) throws ManagerException;
	/**
	 * 根据品牌库获取明细（导出）
	 * @param locno
	 * @param receiptNo
	 * @param sysNo
	 * @return
	 * @throws ManagerException
	 */
	public List<BillImReceiptDtl> findDataBySys(String locno,String receiptNo,String sysNo,List<BillImReceiptDtl> dtls) throws ManagerException;
	/**
	 * 查询需要导出的明细(尺码横排)
	 * @param map
	 * @return
	 * @throws ManagerException
	 */
	public List<BillImReceiptDtl> find4Export(@Param("params") Map<String, Object> map) throws ManagerException;
}