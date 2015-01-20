package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.manager.ConLabelDtlManager;

@Controller
@RequestMapping("/con_label_dtl")
public class ConLabelDtlController extends BaseCrudController<ConLabelDtl> {
	
	 @Resource
	 private ConLabelDtlManager conLabelDtlManager;

    
    @Override
    public CrudInfo init() {
        return new CrudInfo("conlabeldtl/",conLabelDtlManager);
    }
    
    
    
}