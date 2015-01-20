package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.BillChDifferent;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillChDifferentManager;

/*
 * 
 * @author jiang.ys
 * @date  Mon Nov 04 14:14:53 CST 2013
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
@RequestMapping("/bill_ch_plan_confirm")
@ModuleVerify("25120070")
public class BillChPlanConfirmController {
    
	@Log
	private Logger log;
	
	@Resource
    private BillChDifferentManager billChDifferentManager;
    @RequestMapping(value = "/list")
	@InitpowerInterceptors
	public String list() {
		return "billchplanconfirm/list";
	}
    
    @RequestMapping(value = "/createDiff")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public Object createDiff(BillChDifferent billChDifferent ,HttpServletRequest req){
		Map<String, Object> map = new HashMap<String, Object>();
    	Object result = null;
    	try {
    		HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    		String planStatus = req.getParameter("planStatus");
    		if(!"25".equals(planStatus)){//正式代码
    			//if(!"15".equals(planStatus)){
    			result = "只能选择【初盘/复盘】状态的计划单";
    		}else{
    			System.out.println(billChDifferent);
    			billChDifferentManager.addDiff(billChDifferent,user);
        		result = "success";
    		}
		} catch (Exception e) { 
			log.error("=======盘点计划单确认时异常：" + e.getMessage(), e);
			result = e.getMessage();
//			if(result != null && result.toString().indexOf("计划单") < 0){
//				result = "系统异常";
//			}
		}
    	map.put("result", result);
    	return map;
	}
}