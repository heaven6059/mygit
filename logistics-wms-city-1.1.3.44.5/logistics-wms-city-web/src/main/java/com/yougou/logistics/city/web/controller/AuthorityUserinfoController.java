package com.yougou.logistics.city.web.controller;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.manager.AuthorityUserinfoManager;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Jun 30 17:31:36 CST 2014
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
@RequestMapping("/authority_userinfo")
public class AuthorityUserinfoController extends BaseCrudController<AuthorityUserinfo> {
    @Resource
    private AuthorityUserinfoManager authorityUserinfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authorityUserinfo/",authorityUserinfoManager);
    }
}