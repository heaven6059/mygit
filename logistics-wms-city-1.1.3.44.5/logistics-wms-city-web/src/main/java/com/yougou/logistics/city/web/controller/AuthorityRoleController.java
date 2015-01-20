package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.AuthorityRole;
import com.yougou.logistics.city.manager.AuthorityRoleManager;

/*
 * 用户权限表
 * @author su.yq
 * @date  Mon Nov 04 12:15:10 CST 2013
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
@RequestMapping("/authority_role")
public class AuthorityRoleController extends BaseCrudController<AuthorityRole> {
    @Resource
    private AuthorityRoleManager authorityRoleManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authorityRole/",authorityRoleManager);
    }
}