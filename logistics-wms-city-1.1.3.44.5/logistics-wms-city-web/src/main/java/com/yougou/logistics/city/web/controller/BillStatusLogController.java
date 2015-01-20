package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillStatusLog;
import com.yougou.logistics.city.manager.BillStatusLogManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/bill_status_log")
@ModuleVerify("25130030")
public class BillStatusLogController extends BaseCrudController<BillStatusLog> {
    @Resource
    private BillStatusLogManager billStatusLogManager;

    @Log
    private Logger log;
    @Override
    public CrudInfo init() {
        return new CrudInfo("billstatuslog/",billStatusLogManager);
    }
    
    @RequestMapping(value = "/list_any.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		//String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		//String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		Object billType = params.get("billType");
    		int total = 0;
    		List<BillStatusLog> list = new ArrayList<BillStatusLog>();
    		SimplePage page = null;
    		if(billType == null){
    			
    		}else if(billType.toString().equals("IM")){
    			total = this.billStatusLogManager.findCountByIm(params, authorityParams, DataAccessRuleEnum.BRAND);
    			page = new SimplePage(pageNo, pageSize, (int) total);
    			list = this.billStatusLogManager.findPageByIm(page, params, authorityParams, DataAccessRuleEnum.BRAND);
    		}else if(billType.toString().equals("OM")){
    			total = this.billStatusLogManager.findCountByOm(params, authorityParams, DataAccessRuleEnum.BRAND);
    			page = new SimplePage(pageNo, pageSize, (int) total);
    			list = this.billStatusLogManager.findPageByOm(page, params, authorityParams, DataAccessRuleEnum.BRAND);
    		}else if(billType.toString().equals("UM")){
    			total = this.billStatusLogManager.findCountByUm(params, authorityParams, DataAccessRuleEnum.BRAND);
    			page = new SimplePage(pageNo, pageSize, (int) total);
    			list = this.billStatusLogManager.findPageByUm(page, params, authorityParams, DataAccessRuleEnum.BRAND);
    		}
    		obj.put("total", total);
    		obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return obj;
	}
}