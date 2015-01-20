package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2014-01-17 20:35:58
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
public interface BillUmInstockService extends BaseCrudService {
    /**
     * 退仓上架单合计
     * @param params
     * @param authorityParams
     * @return
     */
    Map<String, Object> selectSumQty(Map<String, Object> params,
            AuthorityParams authorityParams) throws ServiceException;
}