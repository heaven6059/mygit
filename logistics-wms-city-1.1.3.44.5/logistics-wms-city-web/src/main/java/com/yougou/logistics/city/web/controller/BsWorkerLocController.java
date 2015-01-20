package com.yougou.logistics.city.web.controller;

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

import com.yougou.logistics.base.common.annotation.IgnoredInterceptors;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmDefloc;
import com.yougou.logistics.city.common.model.BsWorkerLoc;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BsWorkerLocManager;

/*
 * 仓别与用户关系维护
 * @author luo.hl
 * @date  Mon Sep 23 10:25:26 CST 2013
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
@RequestMapping("/bs_worker_loc")
public class BsWorkerLocController extends BaseCrudController<BsWorkerLoc> {
	@Resource
	private BsWorkerLocManager bsWorkerLocManager;
	@Log
	private Logger log;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bsworkerloc/", bsWorkerLocManager);
	}

	@RequestMapping(value = "/findWorkerLoc/list")
	@ResponseBody
	public List<BsWorkerLoc> findWorkerLoc(String workerNo) {
		List<BsWorkerLoc> workerLocList = new ArrayList<BsWorkerLoc>();
		if (StringUtils.isBlank(workerNo)) {
			return workerLocList;
		}
		try {
			Map<String, Object> mapParaMap = new HashMap<String, Object>();
			mapParaMap.put("workerNo", workerNo);
			workerLocList = bsWorkerLocManager.findByBiz(null, mapParaMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return workerLocList;
	}

	@RequestMapping(value = "/saveWorkerLoc")
	@ResponseBody
	public String saveWorkerLoc(String paramStr, HttpServletRequest req) {
		if (StringUtils.isBlank(paramStr)) {
			return "fail";
		}
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bsWorkerLocManager.saveWorkerLoc(paramStr, user.getLoginName());
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@IgnoredInterceptors
	@RequestMapping(value = "/findLocByWorkerNo")
	@ResponseBody
	public Map<String, Object> findLocByWorkerNo(HttpServletRequest req, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			Map<String, Object> params = builderParams(req, model);
			List<BmDefloc> list = bsWorkerLocManager.findLocByWorkerNo(params);
			map.put("list", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}

}