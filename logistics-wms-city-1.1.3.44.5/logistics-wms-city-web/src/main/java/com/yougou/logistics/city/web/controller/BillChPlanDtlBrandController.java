package com.yougou.logistics.city.web.controller;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillChPlanDtlBrand;
import com.yougou.logistics.city.manager.BillChPlanDtlBrandManager;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-11-12 14:17:19
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
@RequestMapping("/bill_ch_plan_dtl_brand")
public class BillChPlanDtlBrandController extends BaseCrudController<BillChPlanDtlBrand> {
    @Resource
    private BillChPlanDtlBrandManager billChPlanDtlBrandManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bill_ch_plan_dtl_brand/",billChPlanDtlBrandManager);
    }
}