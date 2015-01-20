package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BmDefreportMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bmDefreportService")
class BmDefreportServiceImpl extends BaseCrudServiceImpl implements BmDefreportService {
    @Resource
    private BmDefreportMapper bmDefreportMapper;

    @Override
    public BaseCrudMapper init() {
        return bmDefreportMapper;
    }
}