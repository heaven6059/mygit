package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.AuthorityOperations;
import com.yougou.logistics.city.manager.AuthorityOperationsManager;

@Controller
@RequestMapping("/authority_operations")
public class AuthorityOperationsController extends BaseCrudController<AuthorityOperations> {
    @Resource
    private AuthorityOperationsManager authorityOperationsManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authorityOperations/",authorityOperationsManager);
    }
}