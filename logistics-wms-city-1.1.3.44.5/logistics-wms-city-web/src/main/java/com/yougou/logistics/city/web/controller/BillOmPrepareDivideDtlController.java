package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
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

import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmPrepareDivideDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@RequestMapping("/bill_om_preparedivide_dtl")
public class BillOmPrepareDivideDtlController extends BaseCrudController<BillImReceiptDtl> {
	@Log
	private Logger log;

	@Resource
	private BillOmPrepareDivideDtlManager billOmPrepareDivideDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billompreparedividedtl/", billOmPrepareDivideDtlManager);
	}

	@RequestMapping(value = "/findPrepareDivideDetail")
	@ResponseBody
	public Map<String, Object> findPrepareDivideDetail(HttpServletRequest req, Model model) {
		Map<String, Object> obj = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map paramMap = this.builderParams(req, model);
			int total = billOmPrepareDivideDtlManager.findPrepareDivideDetailCount(paramMap,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceiptDtl> list = billOmPrepareDivideDtlManager.findPrepareDivideDetail(page, paramMap,authorityParams);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/selectItemDetail")
	@ResponseBody
	public Map<String, Object> selectItemDetail(HttpServletRequest req, Model model) {
		Map<String, Object> obj = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map paramMap = this.builderParams(req, model);
			int total = billOmPrepareDivideDtlManager.selectItemDetail4PrepareCount(paramMap,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceiptDtl> list = billOmPrepareDivideDtlManager.selectItemDetail4Prepare(paramMap, page,authorityParams);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillImReceiptDtl temp : list) {
				this.setFooterMap("receiptQty", temp.getReceiptQty(), footerMap);
				this.setFooterMap("checkQty", temp.getCheckQty(), footerMap);
				this.setFooterMap("divideQty", temp.getDivideQty(), footerMap);
			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmPrepareDivideDtlManager.selectSumQty(paramMap,authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("receipt_Qty", 0);
					sumFoot.put("check_Qty", 0);
					sumFoot.put("divide_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}

}