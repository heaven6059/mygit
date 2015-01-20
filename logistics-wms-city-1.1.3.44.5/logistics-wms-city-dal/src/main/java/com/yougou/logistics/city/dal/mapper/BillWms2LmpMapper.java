package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.api.dto.Bill4WmsDto;
import com.yougou.logistics.city.common.api.dto.ItemManageResultDto;

public interface BillWms2LmpMapper extends BaseCrudMapper {
	public List<Bill4WmsDto> getBill4Wms(@Param("params") Map<String, Object> map,
			@Param("list") List<String> billType, @Param("page") SimplePage page);
	
	/**
	 * LMP货管库存查询报表-查询总数据条数
	 * @param params
	 * @return
	 */
	public int selectItemManageContentCount(@Param("params") Map<String, Object> params);
	/**
	 * LMP货管库存查询报表-分页查询
	 * @param map
	 * @param page
	 * @return
	 */
	public List<ItemManageResultDto> selectItemManageContentByPage(@Param("params") Map<String, Object> params, @Param("page") SimplePage page);
	/**
	 * LMP货管库存查询报表-合计
	 * @param params
	 * @return
	 */
	public int selectItemManageContentNum(@Param("params") Map<String, Object> params);
}
