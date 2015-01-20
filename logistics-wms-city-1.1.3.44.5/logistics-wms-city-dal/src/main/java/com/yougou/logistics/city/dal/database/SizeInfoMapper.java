package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.SizeInfo;

public interface SizeInfoMapper extends BaseCrudMapper {

    public List<SizeInfo> selectSizeInfoBySizeNo(@Param("sizeInfo")SizeInfo sizeInfo,@Param("authorityParams")AuthorityParams authorityParams);

    public List<SizeInfo> selectSizeInfoBySizeNoList(
	    @Param("list") List<String> list, @Param("sysNo") String sysNo, @Param("authorityParams")AuthorityParams authorityParams)
	    throws DaoException;
    /**
     * 根据syso_no,size_kind查询size_code 默认hcol_no升序
     * @param params
     * @return
     */
    public List<String> selectSizeCodeBySysAndKind(@Param("params")Map<String, Object> params);
}