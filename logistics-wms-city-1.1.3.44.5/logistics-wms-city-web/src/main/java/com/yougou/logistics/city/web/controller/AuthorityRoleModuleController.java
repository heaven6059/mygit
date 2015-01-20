package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.AuthorityRoleModule;
import com.yougou.logistics.city.manager.AuthorityRoleModuleManager;

@Controller
@RequestMapping("/authorityRoleModuleController")
public class AuthorityRoleModuleController extends BaseCrudController<AuthorityRoleModule> {
    @Resource
    private AuthorityRoleModuleManager authorityRoleModuleManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authorityRoleModule/",authorityRoleModuleManager);
    }
}