package com.yougou.logistics.city.web.controller;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ConContentHistory;
import com.yougou.logistics.city.manager.ConContentHistoryManager;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请写出类的用途 
 * @author zo
 * @date  2014-06-18 10:38:59
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
@RequestMapping("/con_content_history")
public class ConContentHistoryController extends BaseCrudController<ConContentHistory> {
    @Resource
    private ConContentHistoryManager conContentHistoryManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("con_content_history/",conContentHistoryManager);
    }
}