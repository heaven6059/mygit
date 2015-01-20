package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.ColorInfoService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Oct 29 11:44:54 CST 2013
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
@Service("colorInfoManager")
class ColorInfoManagerImpl extends BaseCrudManagerImpl implements ColorInfoManager {
    @Resource
    private ColorInfoService colorInfoService;

    @Override
    public BaseCrudService init() {
        return colorInfoService;
    }
}