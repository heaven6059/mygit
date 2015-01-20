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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillChDifferent;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillChDifferentManager;

@Controller
@RequestMapping("/bill_ch_different")
public class BillChDifferentController extends BaseCrudController<BillChDifferent> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillChDifferentManager billChDifferentManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billchdifferent/",billChDifferentManager);
    }
    
    
    @RequestMapping(value = "/createDiff")
	@ResponseBody
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
			log.error("=======盘点计划单生产差异时异常：" + e.getMessage(), e);
			result = e.getMessage();
			/*if(result != null && result.toString().indexOf("计划单") < 0){
				result = "系统异常";
			}*/
		}
    	map.put("result", result);
    	return map;
	}

}