package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.BillOmCheckWeightMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * 称重service实现
 * 
 * @author qin.dy
 * @date 2013-9-29 下午9:35:50
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmCheckWeightService")
class BillOmCheckWeightServiceImpl extends BaseCrudServiceImpl implements BillOmCheckWeightService {
    @Resource
    private BillOmCheckWeightMapper billOmCheckWeightMapper;

    @Override
    public BaseCrudMapper init() {
        return billOmCheckWeightMapper;
    }
}