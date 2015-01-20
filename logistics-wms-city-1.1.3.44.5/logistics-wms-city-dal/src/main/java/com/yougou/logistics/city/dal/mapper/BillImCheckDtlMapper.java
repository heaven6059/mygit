package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.SizeComposeDTO;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BillImCheckDtlKey;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 收货验收单详情mapper
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:13:53
 * @version 0.1.0
 * @copyright yougou.com
 */
public interface BillImCheckDtlMapper extends BaseCrudMapper {

	public Integer selectMaxRowId(BillImCheckDtlKey billImCheckDtlKey);

	public int selectCountMx(SizeComposeDTO dtoParam);

	public List<SizeComposeDTO> queryBillImCheckDTOGroupBy(@Param("page") SimplePage page,
			@Param("dto") SizeComposeDTO dtoParam);

	public List<SizeComposeDTO> queryBillImCheckDTOBExpNo(SizeComposeDTO dtoParam);

	/**
	 * 查询不重复sizecode
	 * 
	 * @return
	 */
	public List<SizeInfo> getSizeCodeByGroup();

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

	/**
	 * 通过入库单号和商品编码得到明细信息
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckDtl(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);

	/**
	 * 查询是否有重复的验收明细
	 * 
	 * @param billImCheckDtl
	 * @return
	 */
	public List<BillImCheckDtl> selectCheckDtlGroupByCount(BillImCheckDtl billImCheckDtl);
	
	
	/**
	 * 根据收货单号查询组装验收单的插入信息
	 * @param map
	 * @param authorityParams
	 * @return
	 */
	public List<BillImCheckDtl> selectCheckDetailByReciptNo(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	
	/**
	 * 批量插入验收单明细
	 * @param lstCheckDtl
	 * @throws DaoException
	 */
	public void  insertBathCheckDtl(List<BillImCheckDtl> lstCheckDtl)throws DaoException;

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);
	
	public SumUtilMap<String, Object> selectDtlSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model,
			@Param("authorityParams") AuthorityParams authorityParams);
	
	/**
	 * 验证验收单明细的有差异数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int selectCheckDtlDiffCount(BillImCheckDtl billImCheckDtl) throws DaoException;
	
	/**
	 * 查询验收明细差异数量
	 * @param billImCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectCheckDtlDiffQty(BillImCheckDtl billImCheckDtl) throws DaoException;
	
	/**
	 * 验收单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillImCheckDtl> selectBillImCheckDtlByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 验收单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int selectBillImCheckDtlCount(@Param("params") Map<String, Object> params,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询箱号数据
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public List<BillImCheckDtl> selectGroupByBox(@Param("params") Map<String, Object> params)throws DaoException;

	/**
	 * 查询是否存在板号
	 * @param billImCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectCheckDtlPanNum (BillImCheckDtl billImCheckDtl) throws DaoException;
	
}