package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImReceipt;
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
public interface BillImReceiptDtlService extends BaseCrudService {
	public int deleteByPrimarayKeyForReceiptNo(Object obj) throws ServiceException;

	/**
	 * 详情数量
	 * 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findDetailCount(Map<?, ?> map, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 详情
	 * 
	 * @param page
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillImImportDtlDto> findDetail(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams)
			throws ServiceException;

	public List<String> selectBoxNoByImportNo(String importNo) throws ServiceException;

	public List<BillImImportDtlDto> findDetailAll(Map<?, ?> map) throws ServiceException;

	public int updateStatusByImportNo(BillImReceiptDtl dtl) throws ServiceException;

	/***
	 * 查询收货单下所有明细
	 * 
	 * @param dtl
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillImReceiptDtl> findAllDetailByReciptNo(BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ServiceException;

	public int findItemNotInReceiptCount(Item item, BillImReceiptDtl dtl, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 查询明细中没有的商品
	 * @param dtl
	 * @param authorityParams TODO
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<BillImReceiptDtl> findItemNotInReceipt(Item item, BillImReceiptDtl dtl, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException;

	public int findDtlByItemNoAndSizeNo(BillImReceiptDtl dtl) throws ServiceException;

	public List<BillImReceiptDtl> selectItemDetail(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException;

	public int selectItemDetailCount(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	public BillImReceiptDtl selectDtlByItemNo(BillImReceiptDtl dtl) throws ServiceException;

	/**
	 * 查询收货单明细可选的箱号,验收明细选择箱号
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> findBillImReceiptDtlBox(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

	public String selectSysNo(Map<String, String> map) throws ServiceException;
	/**
	 * 查询收货单对应的品牌库(多品牌)
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public List<String> findSysNoList(Map<String, Object> map) throws ServiceException;

	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException;

	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException;

	public List<BillImReceiptDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ServiceException;

	public List<BillImReceiptDtl> selectDetailBySizeNo(Map<String, Object> map) throws ServiceException;

	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

	public BillImReceiptDtl selectReceiptDtlPanByBox (BillImReceiptDtl billImReceiptDtl) throws ServiceException;

	public Integer selectMaxRowId(BillImReceipt billImReceipt) throws ServiceException;
	/**
	 * 根据品牌库获取明细
	 * @param locno
	 * @param receiptNo
	 * @param sysNo
	 * @return
	 * @throws ServiceException
	 */
	public List<BillImReceiptDtl> findDataBySys(String locno, String receiptNo,String sysNo,List<BillImReceiptDtl> dtls) throws ServiceException;
	/**
	 * 查询需要导出的明细(尺码横排)
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public List<BillImReceiptDtl> find4Export(@Param("params") Map<String, Object> map) throws ServiceException;
}