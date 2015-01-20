/*
 * 类名 com.yougou.logistics.city.dal.database.BillOmLocateMapper
 * @author su.yq
 * @date  Mon Nov 04 14:35:57 CST 2013
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillOmLocateMapper extends BaseCrudMapper {

	public int selectBillOmLocateCount(@Param("params") BillOmLocate billOmLocate,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillOmLocate> selectBillOmLocateByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") BillOmLocate billOmLocate, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 出货续调调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	public void procBillOmExpContinueDispatchQuery(Map<String, String> map) throws DaoException;
	
	/**
	 * 验证波次下面是否存在未完结的复核单核拣货单
	 * @param billOmLocate
	 * @return
	 * @throws DaoException
	 */
	public int checkBillOmLocateOver(@Param("params") BillOmLocate billOmLocate) throws DaoException;
	
	/**
	 * 验证波次下全部确认的拣货单，是否有新建的复核单
	 * @param billOmLocate
	 * @return
	 * @throws DaoException
	 */
	public int checkBillOmLocateOverNo(@Param("params") BillOmLocate billOmLocate) throws DaoException;
	
	/**
	 * 验证波次下面是否存在未完结的复核单核拣货单
	 * @param billOmLocate
	 * @return
	 * @throws DaoException
	 */
	public int checkBillOmLocateRecovery(@Param("params") BillOmLocate billOmLocate) throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") BillOmLocate billOmLocate,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询是否存在手工关闭的波茨
	 * @param maps
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmLocate> selectOmLocateByExpNo(@Param("params") Map<String, Object> maps) throws DaoException;
	
}