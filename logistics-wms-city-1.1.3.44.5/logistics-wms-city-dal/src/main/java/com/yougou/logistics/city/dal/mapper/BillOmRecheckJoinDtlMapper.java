package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto2;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货交接单明细mapper
 * 
 * @author luohl
 * @date 2013-10-11 上午11:19:37
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmRecheckJoinDtlMapper extends BaseCrudMapper {
	/**
	 * 查询单号
	 * @param map
	 * @return
	 * @throws DaoException
	 */

	public int selectRecheckNoCountByConLabel(@Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<?> selectRecheckNoByConLabel(@Param("page") SimplePage page, @Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public int selectRecheckNoCountByItemNo(@Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<?> selectRecheckNoByItemNo(@Param("page") SimplePage page, @Param("params") Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 查询未交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int selectNoReCheckCount(@Param("params")Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询未交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<?> selectNoReCheck(@Param("params")Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询已交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int selectReCheckedCount(@Param("params")Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 查询已交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckJoinDto> selectReChecked(@Param("params")Map<?, ?> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 尺码横排，查询商品信息
	 * @param dtl
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckJoinDto2> selectItemDetail(@Param("dtl") BillOmRecheckJoinDto dtl) throws DaoException;

	/**
	 * 根据箱号查询单号
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public BillOmRecheckDtl selectReCheckNoByLabelNo(Map<?, ?> map) throws DaoException;

	/**
	 * 根据单号，状态查询记录数
	 * @param map
	 * @return
	 * @throws DaoException
	 */

	public int selectReCheckCountByStataus(Map<?, ?> map) throws DaoException;

	/**
	 * 更具箱号更新状态
	 * @param map
	 * @throws DaoException
	 */
	public void updateStatusByLabelNo(Map<?, ?> map) throws DaoException;
	
	
	public void checkStatus(Map<String, String> map) throws DaoException;
	
	public SumUtilMap<String, Object> selectNoReCheckSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public SumUtilMap<String, Object> selectNoReCheckedSumQty(@Param("params")Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
}