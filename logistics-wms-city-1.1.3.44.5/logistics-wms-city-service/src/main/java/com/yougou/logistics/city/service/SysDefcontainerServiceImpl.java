package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.SysDefcontainerMapper;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 容器资料service实现
 * 
 * @author qin.dy
 * @date 2013-9-22 下午3:03:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("sysDefcontainerService")
class SysDefcontainerServiceImpl extends BaseCrudServiceImpl implements SysDefcontainerService {
    @Resource
    private SysDefcontainerMapper sysDefcontainerMapper;

    @Override
    public BaseCrudMapper init() {
        return sysDefcontainerMapper;
    }
}