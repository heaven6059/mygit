package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillStoreRule;
import com.yougou.logistics.city.manager.BillStoreRuleManager;

@Controller
@RequestMapping("/bill_store_rule")
@ModuleVerify("25040020")
public class BillStoreRuleController extends BaseCrudController<BillStoreRule> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillStoreRuleManager billStoreRuleManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billstorerule/",billStoreRuleManager);
    }
    
    @RequestMapping(value = "/deleteStoreRule")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteStoreRule(String locno, String keyStr, String tempStr, HttpServletRequest req) {
		try {
			if (billStoreRuleManager.deleteStoreRuleBatch(locno, keyStr, tempStr) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
}