package com.yougou.logistics.city.dal.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2014-01-17 22:26:59
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
public interface BillUmInstockMapper extends BaseCrudMapper {

    /**
     * 退仓上架单合计
     * @param params
     * @param authorityParams
     * @return
     */
    Map<String, Object> selectSumQty(@Param("params")Map<String, Object> params,
            @Param("authorityParams")AuthorityParams authorityParams)throws DaoException;
}