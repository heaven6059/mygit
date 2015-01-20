package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmExp;

/**
 * 出库订单
 * @author zuo.sw
 * @date  2013-09-29 16:50:42
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
public interface BillOmExpMapper extends BaseCrudMapper {

	/**
	 * 出库调度总页数
	 * @param billOmLocate
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectBillOmExpDispatchCount(@Param("params") BillOmExp billOmExp,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 出库调度信息列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billOmExp
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmExp> selectBillOmExpDispatchByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") BillOmExp billOmExp, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	/**
	 * 出库调度调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procBillOmExpDispatchQuery(Map<String, String> map) throws DaoException;
	
	/**
	 * 出库调度试图汇总数据
	 * @param billOmExp
	 * @return
	 */
	public BillOmExp selectBillOmExpViewTotalQty(BillOmExp billOmExp) throws DaoException;
	
	/**
	 * 复核 查询出库单 更新状态
	 * @param billOmExp
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmExp> selectBillOmExpUpdateStatus(BillOmExp billOmExp) throws DaoException;
	
	public int selectCountExp(@Param("params") BillOmExp params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public List<BillOmExp> selectByPageExp(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params") BillOmExp params,@Param("authorityParams") AuthorityParams authorityParams);
	
	
	public int selectCountExpPre(@Param("params") BillOmExp params,@Param("authorityParams") AuthorityParams authorityParams);
	
	public List<BillOmExp> selectByPageExpPre(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params") BillOmExp params,@Param("authorityParams") AuthorityParams authorityParams);
	

	/**
	 * 查询待定位的发货通知单中，是否存在不同的品质和属性
	 * @param locno
	 * @param expNos
	 * @return
	 */
	public int selectIsDiffAttribute(@Param("locno") String locno,@Param("expNos") String[] expNos);
	
	/**
	 * 查询待定位的发货通知单中，是否存在不同的品质和属性
	 * @param locno
	 * @param expNos
	 * @return
	 */
	public int selectIsExpSysNo(@Param("locno") String locno,@Param("expNos") String[] expNos);
	
	/**
	 * 修改发货通知单状态
	 * @param billOmExp
	 * @return
	 * @throws DaoException
	 */
	public int updateOmExpStatusByExpNo(BillOmExp billOmExp) throws DaoException;
	
	/**
	 * 插入拆分单据
	 * @param billOmExp
	 * @return
	 * @throws DaoException
	 */
	public int insertSplitByExp (BillOmExp billOmExp) throws DaoException;
	
	public Map<String, Object> selectSumQty(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams);

}