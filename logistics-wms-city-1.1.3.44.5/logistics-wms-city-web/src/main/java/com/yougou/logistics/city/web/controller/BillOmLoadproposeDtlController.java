package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmLoadproposeDtl;
import com.yougou.logistics.city.manager.BillOmLoadproposeDtlManager;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-11 18:02:33
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
@RequestMapping("/bill_om_loadpropose_dtl")
public class BillOmLoadproposeDtlController extends BaseCrudController<BillOmLoadproposeDtl> {
    @Resource
    private BillOmLoadproposeDtlManager billOmLoadproposeDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bill_om_loadpropose_dtl/",billOmLoadproposeDtlManager);
    }
}