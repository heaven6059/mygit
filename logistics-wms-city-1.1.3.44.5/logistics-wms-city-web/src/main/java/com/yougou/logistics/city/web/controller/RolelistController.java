package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.Rolelist;
import com.yougou.logistics.city.manager.RolelistManager;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-10 18:18:26
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
@RequestMapping("/rolelist")
public class RolelistController extends BaseCrudController<Rolelist> {
    @Resource
    private RolelistManager rolelistManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("rolelist/",rolelistManager);
    }
}