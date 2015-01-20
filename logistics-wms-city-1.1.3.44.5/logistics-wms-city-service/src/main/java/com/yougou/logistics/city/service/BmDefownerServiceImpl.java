package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BmDefownerMapper;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * 委托业主service实现
 * 
 * @author qin.dy
 * @date 2013-9-22 下午2:03:26
 * @copyright yougou.com
 */
@Service("bmDefownerService")
class BmDefownerServiceImpl extends BaseCrudServiceImpl implements BmDefownerService {
    @Resource
    private BmDefownerMapper bmDefownerMapper;

    @Override
    public BaseCrudMapper init() {
        return bmDefownerMapper;
    }
}