package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.AuthorityModuleOperations;
import com.yougou.logistics.city.manager.AuthorityModuleOperationsManager;

@Controller
@RequestMapping("/authorityModuleOperationsController")
public class AuthorityModuleOperationsController extends BaseCrudController<AuthorityModuleOperations> {
    @Resource
    private AuthorityModuleOperationsManager authorityModuleOperationsManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authorityModuleOperations/",authorityModuleOperationsManager);
    }
}