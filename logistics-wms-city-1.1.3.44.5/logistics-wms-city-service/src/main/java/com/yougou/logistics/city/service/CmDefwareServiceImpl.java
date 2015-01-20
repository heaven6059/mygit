package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.CmDefwareMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 
 * 仓区service实现
 * 
 * @author qin.dy
 * @date 2013-9-25 下午3:40:25
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefwareService")
class CmDefwareServiceImpl extends BaseCrudServiceImpl implements CmDefwareService {
    @Resource
    private CmDefwareMapper cmDefwareMapper;

    @Override
    public BaseCrudMapper init() {
        return cmDefwareMapper;
    }
}