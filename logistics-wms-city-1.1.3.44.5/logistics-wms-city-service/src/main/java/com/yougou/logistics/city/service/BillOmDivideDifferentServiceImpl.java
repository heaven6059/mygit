package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BillOmDivideDifferentMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
@Service("billOmDivideDifferentService")
class BillOmDivideDifferentServiceImpl extends BaseCrudServiceImpl implements BillOmDivideDifferentService {
    @Resource
    private BillOmDivideDifferentMapper billOmDivideDifferentMapper;

    @Override
    public BaseCrudMapper init() {
        return billOmDivideDifferentMapper;
    }
}