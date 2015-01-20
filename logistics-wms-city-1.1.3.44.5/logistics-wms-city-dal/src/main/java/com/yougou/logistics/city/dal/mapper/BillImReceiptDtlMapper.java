/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillImReceiptDtlMapper
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
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillImReceiptDtlMapper extends BaseCrudMapper {
	public int deleteByPrimarayKeyForReceiptNo(Object obj) throws DaoException;

	public Integer selectMaxRowId(Object obj) throws DaoException;

	public int selectDetailCount(@Param("params") Map<?, ?> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int selectPrepareDivideDetailCount(@Param("params") Map<?, ?> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int updateStatusByImportNo(BillImReceiptDtl dtl) throws DaoException;

	public List<BillImImportDtlDto> selectDetail(@Param("page") SimplePage page, @Param("params") Map<?, ?> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImReceiptDtl> selectPrepareDivideDetail(@Param("page") SimplePage page,
			@Param("params") Map<?, ?> map, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public List<BillImImportDtlDto> selectDetailAll(@Param("params") Map<?, ?> map) throws DaoException;

	public List<String> selectBoxNoByImportNo(String importNo) throws DaoException;

	/**
	 * 查询收货单下的所有通知单
	 * 
	 * @param dtl
	 * @return
	 */
	public List<BillImReceiptDtl> selectImportNoByReceiptNo(@Param("dtl") BillImReceiptDtl dtl) throws DaoException;

	/**
	 * 查询收货单下所有明细
	 * 
	 * @param dtl
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> selectAllDetailByReciptNo(@Param("dtl") BillImReceiptDtl dtl,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询明细中没有的商品数量
	 * @param dtl
	 * @param authorityParams TODO
	 * 
	 * @return
	 * @throws DaoException
	 */
	public int selectItemNotInReceiptCount(@Param("item") Item item, @Param("dtl") BillImReceiptDtl dtl,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询明细中没有的商品
	 * @param dtl
	 * @param authorityParams TODO
	 * 
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> selectItemNotInReceipt(@Param("item") Item item, @Param("dtl") BillImReceiptDtl dtl,
			@Param("page") SimplePage page, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public int selectDtlByItemNoAndSizeNo(@Param("dtl") BillImReceiptDtl dtl) throws DaoException;

	/**
	 * 查询收货单明细
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws DaoException
	 */

	public List<BillImReceiptDtl> selectItemDetail(@Param("params") Map<String, Object> map,
			@Param("page") SimplePage page, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public int selectItemDetailCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImReceiptDtl> selectItemDetail4Prepare(@Param("params") Map<String, Object> map,
			@Param("page") SimplePage page, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public int selectItemDetail4PrepareCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 通过商品编码查询收货明细，取第一条记录
	 * 
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public BillImReceiptDtl selectDtlByItemNo(@Param("dtl") BillImReceiptDtl dtl) throws DaoException;

	/**
	 * 查询收货单明细可选的箱号,验收明细选择箱号
	 * 
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> selectBillImReceiptDtlBox(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 预分货增加明细查询箱库存
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> selectContentBox4Divide(@Param("params") Map<String, Object> map) throws DaoException;

	/**
	 * 查询收货单对应的品牌库
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public String selectSysNo(@Param("params") Map<String, String> map) throws DaoException;
	/**
	 * 查询收货单对应的品牌库(多品牌)
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectSysNoList(@Param("params") Map<String, Object> map) throws DaoException;
	/**
	 * 查询需要导出的明细(尺码横排)
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceiptDtl> select4Export(@Param("params") Map<String, Object> map) throws DaoException;
	/**
	 *  查询明细中商品的尺码类别 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<String> selectItemSizeKind(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public int selectItemDetailByGroupCount(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImReceiptDtl> selectItemDetailByGroup(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams, @Param("page") SimplePage page)
			throws DaoException;

	public List<BillImReceiptDtl> selectDetailBySizeNo(@Param("params") Map<String, Object> map) throws DaoException;

	public int selectBoxQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<BillImReceiptDtl> selectBoxPan4ReceiptDtl(@Param("params") Map<String, Object> map) throws DaoException;

	public int selectPanIsExist(BillImReceiptDtl receiptDtl) throws DaoException;
	
	public int selectPanIsExistLock(BillImReceiptDtl receiptDtl) throws DaoException;
	
	public int updatePanNoBy(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
	 * 根据板号查询箱号
	 * @param billImCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public BillImReceiptDtl selectReceiptDtlPanByBox (BillImReceiptDtl billImReceiptDtl) throws DaoException;

	/**
	 * 更新箱状态为22,预分货单
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer batchUpdateBoxStatus4Prepare(@Param("params") Map<String, Object> map) throws DaoException;
}