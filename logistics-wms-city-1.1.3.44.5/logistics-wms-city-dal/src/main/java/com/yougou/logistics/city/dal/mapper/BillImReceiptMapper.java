/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillImReceiptMapper
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
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImReceipt;

public interface BillImReceiptMapper extends BaseCrudMapper {

	public List<BillImReceipt> selectReceiptByPage(@Param("page") SimplePage page,
			@Param("receipt") BillImReceipt receipt, @Param("authorityParams")AuthorityParams authorityParams);

	public int selectCountMx(@Param("receipt") BillImReceipt receipt, @Param("authorityParams")AuthorityParams authorityParams);

	public int selectMainReciptCount(@Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImReceipt> selectMainRecipt(@Param("page") SimplePage page, @Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public List<BillImReceipt> selectMainReciptSum(@Param("page") SimplePage page, @Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	public String selectIsReceiptByImportNo(String ImportNo) throws DaoException;

	/**
	 * 查询未验收的收货单
	 * @param page
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillImReceipt> selectReciptNoChecked(@Param("page") SimplePage page, @Param("params") Map<?, ?> map, @Param("authorityParams")AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 查询未验收的收货单数量
	 * @param page
	 * @param map
	 * @param authorityParams TODO
	 * @return
	 * @throws DaoException
	 */
	public int selectReciptNoCheckedCount(@Param("page") SimplePage page, @Param("params") Map<?, ?> map, @Param("authorityParams")AuthorityParams authorityParams)
			throws DaoException;

	public int selectCount4Direct(@Param("params") Map map) throws DaoException;

	public List<BillImImport> selectByPage4Direct(@Param("page") SimplePage page, @Param("params") Map map)
			throws DaoException;
	public List<BillImImportDtlDto> selectBatchSelectBox(@Param("importNoList")List<String> importNoList,@Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	
	public int selectBoxNo4DivideCount(@Param("params") Map map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<?> selectBoxNo4Divide(@Param("page") SimplePage page, @Param("params") Map map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImReceipt> selectImReceiptPrint(@Param("params")Map<?, ?> map,
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 关闭预分货单
	 * @param params
	 * @throws DaoException
	 */
	public void procOmCancelPreparedivide(Map<String, String> params) throws DaoException;
}