package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.SizeInfo;

public interface SizeInfoService extends BaseCrudService {

    public List<SizeInfo> findSizeInfoBySizeNo(SizeInfo sizeInfo,AuthorityParams authorityParams)
	    throws ServiceException;

    public List<SizeInfo> selectSizeInfoBySizeNoList(List<String> list,
	    String sysNo,AuthorityParams authorityParams) throws ServiceException;
    
    /**
     * 根据syso_no,size_kind查询size_code 默认hcol_no升序
     * @param params
     * @return
     */
    public List<String> findSizeCodeBySysAndKind(@Param("params")Map<String, Object> params);
}