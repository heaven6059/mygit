package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-15 14:36:28
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
public interface BillUmDirectService extends BaseCrudService {

    public int selectCount4Direct(Map map) throws ServiceException;

    public List<BillUmDirect> selectByPage4Direct(SimplePage page, Map map)
	    throws ServiceException;

    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map);
}