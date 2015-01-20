package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDeliverExport;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 
 * 
 * @author jiang.ys
 * @date 2013-10-12 下午3:23:31
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDeliverExportMapper extends BaseCrudMapper {
	
	 public List<Map<String, Object>> selectDeliverDtlSize(@Param("params")Map<String,Object> params);
	 public List<Map<String, Object>> selectDeliverDtlSizeNum(@Param("params")Map<String,Object> params, @Param("authorityParams")AuthorityParams authorityParams);
	 /**
	  * 获取size_kind
	  * @param model
	  * @param authorityParams
	  * @return
	  */
	 public List<String> selectAllDtlSizeKind(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams);
	 
	 public int selectExportCount(@Param("params") BillOmDeliverExport model, @Param("authorityParams") AuthorityParams authorityParams);
	 public List<BillOmDeliverExport> selectBillOmDeliverExportByGroup(@Param("params") BillOmDeliverExport model,
				@Param("authorityParams") AuthorityParams authorityParams);
	 
	 public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws ServiceException;
}