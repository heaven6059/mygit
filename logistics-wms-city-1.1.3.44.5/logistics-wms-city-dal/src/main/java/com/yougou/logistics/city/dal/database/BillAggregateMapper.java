package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.api.dto.BillUnusualDto;

/**
 * TODO: 业务单据汇总查询
 * 
 * @author ye.kl
 * @date 2014-1-23 下午7:48:58
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillAggregateMapper extends BaseCrudMapper {

	/**
	 * 查询存在异常的单据商品数量
	 * @param params
	 * @param page
	 * @return
	 */
	public List<BillUnusualDto> selectBillUnusualByPage(@Param("params") Map<String, Object> params,@Param("page") SimplePage page);
	
	/**
	 * 查询存在异常的单据记录数
	 * @param params
	 * @return
	 */
	public int selectBillUnusualCount(@Param("params") Map<String, Object> params);
	
	/**
	 * 查询存在异常的单据商品数量
	 * @param params
	 * @return
	 */
	public List<BillUnusualDto> selectBillUnusual(@Param("params") Map<String, Object> params);
	
	
}
