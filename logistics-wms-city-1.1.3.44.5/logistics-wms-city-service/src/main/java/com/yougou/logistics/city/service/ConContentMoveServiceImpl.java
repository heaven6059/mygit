package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.ConContentMove;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.ConContentMoveMapper;

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
@Service("conContentMoveService")
class ConContentMoveServiceImpl extends BaseCrudServiceImpl implements
	ConContentMoveService {
    @Resource
    private ConContentMoveMapper conContentMoveMapper;

    @Override
    public BaseCrudMapper init() {
	return conContentMoveMapper;
    }

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
	return conContentMoveMapper.selectSumQty(map, authorityParams);
    }

    @Override
    public List<ConContentMove> selectConContentMoveBoxNo(
	    Map<String, Object> param) {
	return conContentMoveMapper.selectConContentMoveBoxNo(param);
    }

}