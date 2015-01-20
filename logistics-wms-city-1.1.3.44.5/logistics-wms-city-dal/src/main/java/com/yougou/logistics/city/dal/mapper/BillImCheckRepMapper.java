package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 入库查询mapper
 * @author chen.yl1
 *
 */
public interface BillImCheckRepMapper extends BaseCrudMapper {

	/**
	 * 查询不重复sizecode
	 * 
	 * @return
	 */
	public List<SizeInfo> getSizeCodeByGroup(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	
	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	
	/**
	 * 统计总数
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public int getCount(@Param("params") BillCheckImRep model, @Param("authorityParams") AuthorityParams authorityParams);
	
	/**
	 * 通过groupby得到商品编码和入库单号
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckByGroup(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckExportByGroup(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	
	/**
	 * 通过入库单号和商品编码得到明细信息
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckDtlIm(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckDtlUm(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillCheckImRep> getBillImCheckDtlOtm(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	public SumUtilMap<String, Object> selectSumQty(@Param("params") BillCheckImRep model, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

}