package com.yougou.logistics.city.web.controller;

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
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.manager.BillWmOutstockManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
@RequestMapping("/bill_wm_outstock")
@ModuleVerify("25090010")
public class BillWmOutstockController extends BaseCrudController<BillWmOutstock> {
	
    @Log
    private Logger log;
    
	@Resource
	private BillWmOutstockManager billWmOutstockManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billwmoutstock/", billWmOutstockManager);
	}
	
	@RequestMapping(value = "/list_d.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String brandNo = "";
			String sysNo = "";
			String assignName = "";
			if(paramsAll.get("brandNo") != null) {
				brandNo = paramsAll.get("brandNo").toString();
			}
			if(paramsAll.get("sysNo") != null) {
				sysNo = paramsAll.get("sysNo").toString();
			}
			if(paramsAll.get("assignName") != null) {
				assignName = paramsAll.get("assignName").toString();
			}
			if(!brandNo.equals("") || !sysNo.equals("") || !assignName.equals("")) {
				params.put("joinIn", "true");
			}
			
			int total = billWmOutstockManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmOutstock> list = billWmOutstockManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    /**
     * 拣货确认
     * @param locno
     * @param ownerNo
     * @param outstockNo
     * @param editor 当前登录用户
     * @param keyStr
     * @return
     */
	@RequestMapping(value = "checkOutstock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public Map<String, Object> checkOutstock(String locno, String ownerNo, String outstockNo, String editor,
			String keyStr,String userChName) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			billWmOutstockManager.checkOutstock(locno, outstockNo, editor, keyStr, ownerNo,userChName);
			obj.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(),e);
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}
}