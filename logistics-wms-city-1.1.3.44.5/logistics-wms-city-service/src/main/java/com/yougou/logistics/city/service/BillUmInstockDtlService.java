package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-17 20:35:58
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public interface BillUmInstockDtlService extends BaseCrudService {
    public long findMaxInstockId(Map<String, Object> params)
	    throws ServiceException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams);
    /**
     * 按计划保存明细(将实际上架储位和实际上架数量设为预上架储位和计划数量)
     * @param params
     * @return
     * @throws ServiceException
     */
    public int planSave(Map<String, Object> params)throws ServiceException;
}