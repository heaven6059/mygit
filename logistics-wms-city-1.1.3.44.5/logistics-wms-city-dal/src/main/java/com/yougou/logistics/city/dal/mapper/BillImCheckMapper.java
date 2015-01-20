package com.yougou.logistics.city.dal.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillImCheck;

/**
 * 
 * 收货验收单mapper
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:13:36
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillImCheckMapper extends BaseCrudMapper {

	public String getCellNo(@Param("locno") String locno);

	public String getCellId();

	public void writeConContentMove(@Param("checkNo") String checkNo, @Param("cellNo") String cellNo,
			@Param("checkQty") BigDecimal checkQty, @Param("cellId") String cellId, @Param("locno") String locno,
			@Param("ownerNo") String ownerNo);

	public void writeConContent(@Param("cellId") String cellId, @Param("checkQty") BigDecimal checkQty,
			@Param("cellNo") String cellNo, @Param("locno") String locno, @Param("ownerNo") String ownerNo);

	public void updateConBox(@Param("locno") String locno, @Param("boxNo") String boxNo,
			@Param("importNo") String importNo);

	public int findCheckForDirectCount(@Param("params") Map map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

	public List<BillImCheck> selectByPageForDirect(@Param("page") SimplePage page, @Param("params") Map map,
			@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public Map<String, Object> selectSumQty(@Param("params") Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams);
}