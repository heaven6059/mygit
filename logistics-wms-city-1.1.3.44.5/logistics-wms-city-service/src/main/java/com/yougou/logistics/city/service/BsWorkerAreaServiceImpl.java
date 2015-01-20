package com.yougou.logistics.city.service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.BsWorkerAreaMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Sep 25 14:31:21 CST 2013
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
@Service("bsWorkerAreaService")
class BsWorkerAreaServiceImpl extends BaseCrudServiceImpl implements BsWorkerAreaService {
    @Resource
    private BsWorkerAreaMapper bsWorkerAreaMapper;

    @Override
    public BaseCrudMapper init() {
        return bsWorkerAreaMapper;
    }
}