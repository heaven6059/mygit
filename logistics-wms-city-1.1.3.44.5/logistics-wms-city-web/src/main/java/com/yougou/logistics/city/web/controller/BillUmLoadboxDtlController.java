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

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillUmLoadboxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmLoadboxDtlManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
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
@RequestMapping("/bill_um_loadbox_dtl")
public class BillUmLoadboxDtlController extends BaseCrudController<BillUmLoadboxDtl> {
	@Resource
	private BillUmLoadboxDtlManager billUmLoadboxDtlManager;

	@Log
	Logger log;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billUmLoadboxDtl/", billUmLoadboxDtlManager);
	}

	@RequestMapping(value = "/dtllist.json")
	@ResponseBody
	public Map<String, Object> dtlList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = billUmLoadboxDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmLoadboxDtl> list = billUmLoadboxDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);

			for (BillUmLoadboxDtl temp : list) {
				FooterUtil.setFooterMap("itemQty", temp.getItemQty(), footerMap);
				FooterUtil.setFooterMap("boxingQty", temp.getBoxingQty(), footerMap);
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billUmLoadboxDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("boxing_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}