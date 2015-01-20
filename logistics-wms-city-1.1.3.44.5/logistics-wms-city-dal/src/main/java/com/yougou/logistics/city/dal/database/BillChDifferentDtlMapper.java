package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillChDifferentDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillChDifferentDtlMapper extends BaseCrudMapper {
    /**
     * 批量插入差异信息
     * 
     * @param list
     */
    public void batchInsertDtl(List<BillChDifferentDtl> list);

    /**
     * 汇总数量
     * 
     * @param map
     * @param authorityParams TODO
     * @return
     */
    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map, @Param("authorityParams")AuthorityParams authorityParams);
}