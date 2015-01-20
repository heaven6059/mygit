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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillUmInstock;
import com.yougou.logistics.city.common.model.BillUmInstockKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmInstockManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 退仓上架任务
 * @author zuo.sw
 * @date  2014-01-17 20:35:58
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
@RequestMapping("/bill_um_instock")
public class BillUmInstockController extends BaseCrudController<BillUmInstock> {
	@Log
	private Logger log;
	@Resource
	private BillUmInstockManager billUmInstockManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billuminstock/", billUmInstockManager);
	}

	@RequestMapping(value = "/check")
	@ResponseBody
	public Object check(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			String locno = req.getParameter("locno");
			String instockNo = req.getParameter("instockNo");
			String ownerNo = req.getParameter("ownerNo");

			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);

			if (StringUtils.isBlank(locno) || StringUtils.isBlank(instockNo) || StringUtils.isBlank(ownerNo)) {
				result = "缺少参数";
			} else {
				BillUmInstockKey billUmInstockKey = new BillUmInstockKey();
				billUmInstockKey.setInstockNo(instockNo);
				billUmInstockKey.setLocno(locno);
				billUmInstockKey.setOwnerNo(ownerNo);
				BillUmInstock billUmInstock = (BillUmInstock) billUmInstockManager.findById(billUmInstockKey);
				if(!"10".equals(billUmInstock.getStatus())){
					result="单据:" +instockNo +"状态已改变" ;
					map.put("result", result);
					return map;
				}
				
				billUmInstockManager.check(locno, instockNo, ownerNo, user);
				result = "success";
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result = e.getMessage();
			if(StringUtils.isBlank(result)){
				result = "系统异常";
			}
		} catch (Exception e){
			log.error(e.getMessage(), e);
			result = e.getMessage();
			if(StringUtils.isBlank(result)){
				result = "系统异常";
			}
		}
		map.put("result", result);
		return map;
	}

	@RequestMapping(value = "/json4List.json")
	@ResponseBody
	public Object getList4Json(HttpServletRequest req, Model model) {
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
			int total = this.billUmInstockManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmInstock> list = this.billUmInstockManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			//小计
            Map<String, Object> footer = new HashMap<String, Object>();
            List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
            for(BillUmInstock billUmInstock : list){
                totalItemQty = totalItemQty.add(billUmInstock.getItemQty());
                totalRealQty = totalRealQty.add(billUmInstock.getRealQty());
            }
            footer.put("instockNo", "小计");
            footer.put("itemQty", totalItemQty);
            footer.put("realQty", totalRealQty);
            footerList.add(footer);
            //合计
            Map<String, Object> sumFoot = new HashMap<String, Object>();
            if (pageNo == 1) {
                sumFoot = this.billUmInstockManager.selectSumQty(params, authorityParams);
                if (sumFoot == null) {
                    sumFoot = new SumUtilMap<String, Object>();
                    sumFoot.put("item_qty", 0);
                    sumFoot.put("real_qty", 0);
                }
                sumFoot.put("isselectsum", true);
                sumFoot.put("instock_No", "合计");
            } else {
                sumFoot.put("instockNo", "合计");
            }
            footerList.add(sumFoot);
			obj.put("total", total);
			obj.put("footer", footerList);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return obj;
	}
}