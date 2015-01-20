package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillAccControl;
import com.yougou.logistics.city.manager.BillAccControlManager;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 07 16:10:55 CST 2014
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
@RequestMapping("/bill_acc_control")
public class BillAccControlController extends BaseCrudController<BillAccControl> {
	
	@Log
    private Logger log;
	
    @Resource
    private BillAccControlManager billAccControlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billAccControl/",billAccControlManager);
    }
    
    @RequestMapping(value = "/listBillAccControlGroupByBillName")
	@ResponseBody
	public List<BillAccControl> listBillAccControlGroupByBillName(HttpServletRequest req,Model model)throws ManagerException{
    	List<BillAccControl> list = new ArrayList<BillAccControl>();
    	try{
    		Map<String,Object> params=builderParams(req, model);
    		list = this.billAccControlManager.findBillAccControlGroupByBillName(params);
    		return list;
    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
}