package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.manager.SupplierManager;

@Controller
@RequestMapping("/carriers")
public class CarriersController extends BaseCrudController<Supplier> {
    @Resource
    private SupplierManager supplierManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("carriers/",supplierManager);
    }
}