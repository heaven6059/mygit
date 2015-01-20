package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ItemTypeinfo;
import com.yougou.logistics.city.manager.ItemTypeinfoManager;

@Controller
@RequestMapping("/item_typeinfo")
public class ItemTypeinfoController extends BaseCrudController<ItemTypeinfo> {
    @Resource
    private ItemTypeinfoManager itemTypeinfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("item_typeinfo/",itemTypeinfoManager);
    }
}