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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.DateConContentReport;
import com.yougou.logistics.city.manager.DateConContentReportManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-2-22 下午7:03:22
 * @version 0.1.0 
 * @copyright yougou.com 
 */

@Controller
@RequestMapping("/datecontentreport")
@ModuleVerify("25130080")
public class DateConContentReportController extends BaseCrudController<DateConContentReport> {
	
	@Log
	private Logger log;
	
	@Resource
	private DateConContentReportManager dateConContentReportManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("datecontentreport/", dateConContentReportManager);
	}
	
	
	@RequestMapping(value = "/listReport.json")
	@ResponseBody
	public  Map<String, Object> queryListReport(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = dateConContentReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<DateConContentReport> list = dateConContentReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			
			BigDecimal totalJhrkQty = new BigDecimal(0);
			BigDecimal totalTcrkQty = new BigDecimal(0);
			BigDecimal totalTcckQty = new BigDecimal(0);
			BigDecimal totalFhckQty = new BigDecimal(0);
			BigDecimal totalQtrkQty = new BigDecimal(0);
			BigDecimal totalBsckQty = new BigDecimal(0);
			BigDecimal totalPyrkQty = new BigDecimal(0);
			BigDecimal totalKpckQty = new BigDecimal(0);
			
			for(DateConContentReport report:list){
				totalJhrkQty = totalJhrkQty.add(report.getJhrkQty()==null?new BigDecimal(0):report.getJhrkQty());
				totalTcrkQty = totalTcrkQty.add(report.getTcrkQty()==null?new BigDecimal(0):report.getTcrkQty());
				totalTcckQty = totalTcckQty.add(report.getTcckQty()==null?new BigDecimal(0):report.getTcckQty());
				totalFhckQty = totalFhckQty.add(report.getFhckQty()==null?new BigDecimal(0):report.getFhckQty());
				totalQtrkQty = totalQtrkQty.add(report.getQtrkQty()==null?new BigDecimal(0):report.getQtrkQty());
				totalBsckQty = totalBsckQty.add(report.getBsckQty()==null?new BigDecimal(0):report.getBsckQty());
				totalPyrkQty = totalPyrkQty.add(report.getPyrkQty()==null?new BigDecimal(0):report.getPyrkQty());
				totalKpckQty = totalKpckQty.add(report.getKpckQty()==null?new BigDecimal(0):report.getKpckQty());
			}
			
			footer.put("audittmStr", "小计");
			footer.put("jhrkQty", totalJhrkQty);
			footer.put("tcrkQty", totalTcrkQty);
			footer.put("tcckQty", totalTcckQty);
			footer.put("fhckQty", totalFhckQty);
			footer.put("qtrkQty", totalQtrkQty);
			footer.put("bsckQty", totalBsckQty);
			footer.put("pyrkQty", totalPyrkQty);
			footer.put("kpckQty", totalKpckQty);
			footerList.add(footer);
			
			// 合计
					Map<String, Object> sumFoot1 = new HashMap<String, Object>();
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					if (pageNo == 1) {
						sumFoot1 = dateConContentReportManager.selectSumQty(params, authorityParams);
						if (sumFoot1 != null) {
							sumFoot2.put("jhrkQty", sumFoot1.get("jhrkQty"));
							sumFoot2.put("tcrkQty", sumFoot1.get("tcrkQty"));
							sumFoot2.put("tcckQty", sumFoot1.get("tcckQty"));
							sumFoot2.put("fhckQty", sumFoot1.get("fhckQty"));
							sumFoot2.put("qtrkQty", sumFoot1.get("qtrkQty"));
							sumFoot2.put("bsckQty", sumFoot1.get("bsckQty"));
							sumFoot2.put("pyrkQty", sumFoot1.get("pyrkQty"));
							sumFoot2.put("kpckQty", sumFoot1.get("kpckQty"));
							sumFoot2.put("isselectsum", true);
						}
					}
					sumFoot2.put("audittmStr", "合计");
					footerList.add(sumFoot2);
					
			obj.put("footer", footerList);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
}
