package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.ConContentMoveService;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 07 11:21:04 CST 2014
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
@Service("conContentMoveManager")
class ConContentMoveManagerImpl extends BaseCrudManagerImpl implements
	ConContentMoveManager {
    @Resource
    private ConContentMoveService conContentMoveService;

    @Override
    public BaseCrudService init() {
	return conContentMoveService;
    }

    @Override
    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
	return conContentMoveService.selectSumQty(map, authorityParams);
    }

}