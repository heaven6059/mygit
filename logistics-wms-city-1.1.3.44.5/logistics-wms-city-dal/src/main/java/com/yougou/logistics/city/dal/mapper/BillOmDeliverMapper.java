package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.BillOmDeliverKey;

/**
 * 
 * 装车单mapper
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:23:10
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverMapper extends BaseCrudMapper {
	
	public int deleteByDeliverDtl(BillOmDeliverKey key) throws DaoException;
	
	public void checkBillOmDeliver(Map<String, String> map) throws DaoException;
	
	public List<BillOmDeliver> selectLoadproposeDtl(BillOmDeliver billOmDeliver) throws DaoException;
	
	public int loadproposeDtlCount(BillOmDeliver billOmDeliver) throws DaoException;
	
	public int updateLoadproposeDtl(BillOmDeliver billOmDeliver) throws DaoException;
	
	public int updateLoadpropose(BillOmDeliver billOmDeliver) throws DaoException;
	
	public int updateBillOmDeliverDtl(BillOmDeliver billOmDeliver) throws DaoException;
	
	public List<BillOmDeliver> selectBillOmDeliverSum(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams)
			throws DaoException;

	public List<BillOmDeliver> selectPrintOmDeliverList(@Param("orderByField")String sortColumn,
			@Param("orderBy")String sortOrder, @Param("params")Map<String, Object> params,
			@Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
}