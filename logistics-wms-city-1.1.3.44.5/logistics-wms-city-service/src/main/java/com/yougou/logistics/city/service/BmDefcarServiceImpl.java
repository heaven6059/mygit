package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.BmDefcarMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * 车辆管理service实现
 * 
 * @author qin.dy
 * @date 2013-9-23 下午7:07:29
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bmDefcarService")
class BmDefcarServiceImpl extends BaseCrudServiceImpl implements BmDefcarService {
    @Resource
    private BmDefcarMapper bmDefcarMapper;

    @Override
    public BaseCrudMapper init() {
        return bmDefcarMapper;
    }
}