package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BmPrintLog;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BmPrintLogManager;
import com.yougou.logistics.city.web.utils.StringUtils;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:21:11 CST 2014
 * @version 1.0.6
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
@RequestMapping("/bill_um_labelprint")
@ModuleVerify("25060110")
public class BillUmLabelPrintController extends BaseCrudController<BmPrintLog> {

	@Log
	private Logger log;

	@Resource
	private BmPrintLogManager bmPrintLogManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billumlabelprint/", bmPrintLogManager);
	}

	/**
	 * 获取标签前缀
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/getLabelPrefix")
	@ResponseBody
	public Map<String, Object> getLabelPrefix(HttpServletRequest req, HttpSession session) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String qty = req.getParameter("qty");
			String printType = req.getParameter("printType");
			String storeName = req.getParameter("storeName");
			if (!StringUtils.isNumber(qty)) {
				obj.put("result", ResultEnums.FAIL);
				obj.put("msg", "参数错误");
				return obj;
			}
			int num = Integer.parseInt(qty);
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			List<String> labelNoList = bmPrintLogManager.getLabelPrefix(user, num, printType, storeName);
			obj.put("result", ResultEnums.SUCCESS.getResultMsg());
			obj.put("labelNos", labelNoList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL);
			obj.put("msg", "获取标签失败");
		}
		return obj;
	}
	@RequestMapping(value = "/getLabel4Full")
	@ResponseBody
	public Object getLabel4Full(HttpServletRequest req, HttpSession session) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String dtStr = req.getParameter("dtStr");
			String printType = req.getParameter("printType");
			if (!CommonUtil.hasValue(dtStr) || !CommonUtil.hasValue(printType)) {
				obj.put("result", ResultEnums.FAIL);
				obj.put("msg", "缺少参数,获取标签失败!");
				return obj;
			}
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			return bmPrintLogManager.getLabel4Full(user, printType, dtStr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL);
			obj.put("msg", "获取标签失败");
		}
		return obj;
	}
}