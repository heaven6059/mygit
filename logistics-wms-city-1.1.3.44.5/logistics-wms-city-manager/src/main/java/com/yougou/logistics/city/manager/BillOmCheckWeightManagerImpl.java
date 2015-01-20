package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.BillOmCheckWeightService;

/**
 * 
 * 称重manager实现
 * 
 * @author qin.dy
 * @date 2013-9-29 下午9:35:03
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmCheckWeightManager")
class BillOmCheckWeightManagerImpl extends BaseCrudManagerImpl implements BillOmCheckWeightManager {
    @Resource
    private BillOmCheckWeightService billOmCheckWeightService;

    @Override
    public BaseCrudService init() {
        return billOmCheckWeightService;
    }
}