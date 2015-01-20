package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillChRequestDtl;
import com.yougou.logistics.city.manager.BillChRequestDtlManager;

@Controller
@RequestMapping("/bill_ch_request_dtl")
public class BillChRequestDtlController extends BaseCrudController<BillChRequestDtl> {
    @Resource
    private BillChRequestDtlManager billChRequestDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billchrequestdtl/",billChRequestDtlManager);
    }
    @RequestMapping(value = "/listForJoinItem.json")
	@ResponseBody
    public Map<String, Object> queryListForJoinItem(HttpServletRequest req, Model model)throws ManagerException {
    	int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = this.billChRequestDtlManager.findCountForJoinItem(params);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<BillChRequestDtl> list = this.billChRequestDtlManager.findForJoinItemByPage(page, sortColumn, sortOrder, params);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		return obj;
    }
}