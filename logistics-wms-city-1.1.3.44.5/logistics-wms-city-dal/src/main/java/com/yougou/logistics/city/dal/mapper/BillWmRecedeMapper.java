package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillWmRecede;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public interface BillWmRecedeMapper extends BaseCrudMapper {

	/**
	 * 退厂调度总页数
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectBillWmRecedeDispatchCount(@Param("params") BillWmRecede billWmRecede,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 退厂调度信息列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmRecede> selectBillWmRecedeDispatchByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") BillWmRecede billWmRecede, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	/**
	 * 退厂通知单定位
	 * @param maps
	 * @throws DaoException
	 */
	public void procBillWmRecedeLocateQuery(Map<String, String> maps) throws DaoException;

	
	/**
	 * 退厂任务分派总页数
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectBillWmRecedeJoinDirectCount(@Param("params") BillWmRecede billWmRecede,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	/**
	 * 查询退厂任务分派下架指示表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmRecede> selectBillWmRecedeJoinDirectByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") BillWmRecede billWmRecede, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	
	/**
	 * 退仓复核退厂调度汇总查询总页数
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int selectBillWmRecedeGroupCount(@Param("params") BillWmRecede billWmRecede,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	/**
	 * 退仓复核退厂调度信息列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmRecede> selectBillWmRecedeGroupByPage(@Param("page") SimplePage page,
			@Param("orderByField") String orderByField, @Param("orderBy") String orderBy,
			@Param("params") BillWmRecede billWmRecede, @Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;
	
	/**
	 * 更新退厂通知单状态为复核状态
	 * @return
	 */
	public int updateRecedeStatus4Recheck(BillWmRecede billWmRecede) throws DaoException;
	
	/**
	 * 更新退厂通知单状态为拣货状态
	 * @return
	 */
	public int updateRecedeStatus4Outstock(BillWmRecede billWmRecede) throws DaoException;
	
}