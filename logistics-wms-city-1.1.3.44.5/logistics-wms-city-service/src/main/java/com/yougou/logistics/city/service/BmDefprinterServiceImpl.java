package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.BmDefprinterMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * 打印机service实现
 * 
 * @author qin.dy
 * @date 2013-11-1 下午2:38:14
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bmDefprinterService")
class BmDefprinterServiceImpl extends BaseCrudServiceImpl implements BmDefprinterService {
    @Resource
    private BmDefprinterMapper bmDefprinterMapper;

    @Override
    public BaseCrudMapper init() {
        return bmDefprinterMapper;
    }
}