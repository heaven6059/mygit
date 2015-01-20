package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ZoneInfo;
import com.yougou.logistics.city.manager.ZoneInfoManager;

/*
 * 区域管理
 * @author su.yq
 * @date  Tue Oct 29 14:22:36 CST 2013
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
@Controller
@RequestMapping("/zone_info")
@ModuleVerify("25030110")
public class ZoneInfoController extends BaseCrudController<ZoneInfo> {
    @Resource
    private ZoneInfoManager zoneInfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("zoneinfo/",zoneInfoManager);
    }
}