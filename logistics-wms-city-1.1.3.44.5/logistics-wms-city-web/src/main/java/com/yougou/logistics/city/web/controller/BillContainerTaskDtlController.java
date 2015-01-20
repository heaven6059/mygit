package com.yougou.logistics.city.web.controller;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;
import com.yougou.logistics.city.manager.BillContainerTaskDtlManager;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
@RequestMapping("/bill_container_task_dtl")
public class BillContainerTaskDtlController extends BaseCrudController<BillContainerTaskDtl> {
    @Resource
    private BillContainerTaskDtlManager billContainerTaskDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bill_container_task_dtl/",billContainerTaskDtlManager);
    }
}