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
import com.yougou.logistics.city.common.model.BillWmRequestDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillWmRequestDtlManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 17:59:52 CST 2014
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
@RequestMapping("/bill_wm_request_dtl")
public class BillWmRequestDtlController extends BaseCrudController<BillWmRequestDtl> {
	@Resource
	private BillWmRequestDtlManager billWmRequestDtlManager;
	@Log
	private Logger log;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billWmRequestDtl/", billWmRequestDtlManager);
	}

	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> dtlList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billWmRequestDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmRequestDtl> list = this.billWmRequestDtlManager.findByPage(page, null, null, params,
					authorityParams, DataAccessRuleEnum.BRAND);

			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);

			for (BillWmRequestDtl temp : list) {

				FooterUtil.setFooterMap("itemQty", temp.getItemQty(), footerMap);
				FooterUtil.setFooterMap("packQty", temp.getPackQty(), footerMap);

			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billWmRequestDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_Qty", 0);
					sumFoot.put("pack_Qty", 0);
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
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}