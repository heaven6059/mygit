package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.api.dto.BillStatusLogDto;
import com.yougou.logistics.city.common.model.BillStatusLog;

public interface BillStatusLogMapper extends BaseCrudMapper {
	/**
	 * 根据预到货通知单分销合同号(s_po_no)获取状态日志信息
	 * @param poNo
	 * @return
	 */
	public List<BillStatusLogDto> selectBillStatusLogByPoNo(@Param("poNo")String poNo);
	/**
	 * 根据发货通知单来源单号(sourceexp_no)获取状态日志信息
	 * @param sourceExpNo
	 * @return
	 */
	public List<BillStatusLogDto> selectBillStatusLogBySourceExpNo(@Param("sourceExpNo")String sourceExpNo);

	/**
	 * 插入状态记录
	 * @param params
	 * @throws DaoException
	 */
	public void procInsertBillStatusLog(Map<String, Object> params) throws DaoException;
	/**
	 * 关联预到货通知单查询状态日志信息数量
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 */
	public int selectCountByIm(@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	/**
	 * 关联预到货通知单查询状态日志信息
	 * @param page
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillStatusLog> selectPageByIm(@Param("page") SimplePage page,@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	/**
	 * 关联发货通知单查询状态日志信息数量
	 * @param params
	 * @return
	 */
	public int selectCountByOm(@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	/**
	 * 关联发货通知单查询状态日志信息
	 * @param page
	 * @param params
	 * @return
	 */
	public List<BillStatusLog> selectPageByOm(@Param("page") SimplePage page,@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	/**
	 * 关联店退仓单查询状态日志信息数量
	 * @param params
	 * @return
	 */
	public int selectCountByUm(@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	/**
	 * 关联店退仓单查询状态日志信息
	 * @param page
	 * @param params
	 * @return
	 */
	public List<BillStatusLog> selectPageByUm(@Param("page") SimplePage page,@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
}