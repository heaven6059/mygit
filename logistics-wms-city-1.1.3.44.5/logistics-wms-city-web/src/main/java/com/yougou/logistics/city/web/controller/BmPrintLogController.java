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

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmPrintLog;
import com.yougou.logistics.city.dal.mapper.BmPrintLogMapper;
import com.yougou.logistics.city.manager.BmPrintLogManager;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 07 17:41:16 CST 2014
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
@RequestMapping("/bm_print_log")
public class BmPrintLogController extends BaseCrudController<BmPrintLog> {
	@Log
	private Logger log;
	
	@Resource
    private BmPrintLogManager bmPrintLogManager;

    @Resource
    private BmPrintLogMapper bmPrintLogMapper;

    @Override
    public CrudInfo init() {
	return new CrudInfo("bmPrintLog/", bmPrintLogManager);
    }

    @RequestMapping(value = "/getList.json")
    @ResponseBody
    public Map<String, Object> queryList(HttpServletRequest req, Model model)
	    throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
				: Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
				: Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
				: String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
				: String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.bmPrintLogManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BmPrintLog> list = this.bmPrintLogManager.findByPage(page,
				sortColumn, sortOrder, params);
	
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalQty = new BigDecimal(0);
			for (BmPrintLog dtl : list) {
			    totalQty = totalQty.add(new BigDecimal(dtl.getGetQty()));
			}
			footer.put("startSerial", "小计");
			footer.put("getQty", totalQty);
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			footerList.add(footer);
			if (pageNo == 1) {
			    int sumQty = bmPrintLogMapper.selectSumQty(params);
			    sumFoot.put("getQty", sumQty);
			    sumFoot.put("isSelectSum", true);
			}
			sumFoot.put("startSerial", "合计");
			footerList.add(sumFoot);
	
			
			obj.put("footer", footerList);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
    }
}