package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillUmInstockDirect;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmInstockDirectManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 退仓上架任务
 * @author zuo.sw
 * @date  Mon Nov 18 12:08:45 CST 2013
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
@RequestMapping("/bill_um_instock_direct")
@ModuleVerify("25060030")
public class BillUmInstockDirectController extends BaseCrudController<BillUmInstockDirect> {

	@Log
	private Logger log;

	@Resource
	private BillUmInstockDirectManager billUmInstockDirectManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billuminstockdirect/", billUmInstockDirectManager);
	}

	@RequestMapping(value = "/list_direct.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billUmInstockDirectManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmInstockDirect> list = billUmInstockDirectManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);

			obj.put("total", total);
			obj.put("rows", list);

			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalEstQty = new BigDecimal(0);
			for (BillUmInstockDirect dtl : list) {
				totalEstQty = totalEstQty.add(dtl.getEstQty());
			}
			footer.put("sourceNo", "小计");
			footer.put("estQty", totalEstQty);
			footerList.add(footer);
			obj.put("footer", footerList);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billUmInstockDirectManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					sumFoot2.put("estQty", sumFoot1.get("estQty"));
					sumFoot2.put("isselectsum", true);
					sumFoot1 = sumFoot2;
				}
			}
			sumFoot1.put("sourceNo", "合计");
			footerList.add(sumFoot1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/createTask")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> createTask(String locno, String ownerNo, String untreadMmNo, String strCheckNoList,
			HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmInstockDirectManager.createTask(locno, ownerNo, untreadMmNo, strCheckNoList, user.getLoginName());
			result.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/sendOrder")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> sendOrder(String locno, String ownerNo, String sender, String rowIdList,
			HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmInstockDirectManager.sendOrder(locno, ownerNo, sender, rowIdList, user.getLoginName());
			result.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	
	@RequestMapping(value = "/akeySendOrder")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> akeySendOrder(BillUmInstockDirect instockDirect,HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			instockDirect.setWorkUser(user.getLoginName());
			billUmInstockDirectManager.akeySendOrder(instockDirect);
			result.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	
}