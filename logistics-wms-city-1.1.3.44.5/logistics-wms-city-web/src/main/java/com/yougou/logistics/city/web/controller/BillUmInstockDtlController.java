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
import com.yougou.logistics.city.common.model.BillUmInstockDtl;
import com.yougou.logistics.city.common.model.BillUmInstockKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmInstockDtlManager;
import com.yougou.logistics.city.manager.BillUmInstockManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Oct 14 19:59:56 CST 2013
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
@RequestMapping("/bill_um_instock_dtl")
public class BillUmInstockDtlController extends BaseCrudController<BillUmInstockDtl> {
	@Log
	private Logger log;
	@Resource
	private BillUmInstockDtlManager billUmInstockDtlManager;
	@Resource
	private BillUmInstockManager billUmInstockManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billuminstockdtl/", billUmInstockDtlManager);
	}

	@RequestMapping(value = "/plan_save")
	@ResponseBody
	public Object planSave(HttpServletRequest req,BillUmInstockDtl billUmInstockDtl) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String locno = billUmInstockDtl.getLocno();
			String instockNo = billUmInstockDtl.getInstockNo();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(instockNo)) {
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
				billUmInstockDtlManager.planSave(locno, instockNo, user);
				result = "success";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/single_save")
	@ResponseBody
	public Object singleSave(HttpServletRequest req,BillUmInstockDtl billUmInstockDtl) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String locno = billUmInstockDtl.getLocno();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			String instockNo = billUmInstockDtl.getInstockNo();
			Long instockId = billUmInstockDtl.getInstockId();
			String itemNo = billUmInstockDtl.getItemNo();
			String sizeNo = billUmInstockDtl.getSizeNo();
			String boxNo = billUmInstockDtl.getBoxNo();
			String sourceNo = billUmInstockDtl.getSourceNo();
			String cellNo = billUmInstockDtl.getCellNo();
			Long cellId = billUmInstockDtl.getCellId();
			Long destCellId = billUmInstockDtl.getDestCellId();
			String destCellNo = billUmInstockDtl.getDestCellNo();
			String realCellNo = billUmInstockDtl.getRealCellNo();
			BigDecimal realQty = billUmInstockDtl.getRealQty();
			if (StringUtils.isBlank(locno)||
					StringUtils.isBlank(ownerNo)||
					StringUtils.isBlank(instockNo)||
					instockId == null || instockId == 0||
					StringUtils.isBlank(itemNo)||
					StringUtils.isBlank(sizeNo)||
					StringUtils.isBlank(boxNo)||
					StringUtils.isBlank(sourceNo)||
					StringUtils.isBlank(cellNo)||
					cellId == null || cellId == 0||
					destCellId == null || destCellId == 0||
					StringUtils.isBlank(destCellNo)||
					StringUtils.isBlank(realCellNo)||
					realQty == null || realQty.intValue() < 0) {
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
				billUmInstockDtl.setInstockWorker(user.getLoginName());
				billUmInstockDtl.setInstockName(user.getUsername());
				billUmInstockDtlManager.singleSave(billUmInstockDtl);
				result = "success";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/single_split")
	@ResponseBody
	public Object singleSplit(HttpServletRequest req,BillUmInstockDtl billUmInstockDtl) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String locno = billUmInstockDtl.getLocno();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			String instockNo = billUmInstockDtl.getInstockNo();
			Long instockId = billUmInstockDtl.getInstockId();
			String itemNo = billUmInstockDtl.getItemNo();
			String sizeNo = billUmInstockDtl.getSizeNo();
			String boxNo = billUmInstockDtl.getBoxNo();
			String sourceNo = billUmInstockDtl.getSourceNo();
			String cellNo = billUmInstockDtl.getCellNo();
			Long cellId = billUmInstockDtl.getCellId();
			Long destCellId = billUmInstockDtl.getDestCellId();
			String destCellNo = billUmInstockDtl.getDestCellNo();
			String realCellNo = billUmInstockDtl.getRealCellNo();
			BigDecimal realQty = billUmInstockDtl.getRealQty();
			if (StringUtils.isBlank(locno)||
					StringUtils.isBlank(ownerNo)||
					StringUtils.isBlank(instockNo)||
					instockId == null || instockId == 0||
					StringUtils.isBlank(itemNo)||
					StringUtils.isBlank(sizeNo)||
					StringUtils.isBlank(boxNo)||
					StringUtils.isBlank(sourceNo)||
					StringUtils.isBlank(cellNo)||
					cellId == null || cellId == 0||
					destCellId == null || destCellId == 0||
					StringUtils.isBlank(destCellNo)||
					StringUtils.isBlank(realCellNo)||
					realQty == null || realQty.intValue() < 0) {
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
				billUmInstockDtl.setInstockWorker(user.getLoginName());
				billUmInstockDtl.setInstockName(user.getUsername());
				billUmInstockDtlManager.singleSplit(billUmInstockDtl);
				result = "success";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/single_delete")
	@ResponseBody
	public Object singleDelete(HttpServletRequest req,BillUmInstockDtl billUmInstockDtl) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			String locno = billUmInstockDtl.getLocno();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			String instockNo = billUmInstockDtl.getInstockNo();
			Long instockId = billUmInstockDtl.getInstockId();
			if (StringUtils.isBlank(locno)||
					StringUtils.isBlank(ownerNo)||
					StringUtils.isBlank(instockNo)||
					instockId == null || instockId == 0) {
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
				billUmInstockDtlManager.singleDelete(billUmInstockDtl);
				result = "success";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/saveDtl")
	@ResponseBody
	public Object saveDtl(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			String locno = req.getParameter("locno");
			String instockNo = req.getParameter("instockNo");
			String ownerNo = req.getParameter("ownerNo");
			String instockWorker = req.getParameter("instockWorker");
			String instockDtlStr = req.getParameter("instockDtlStr");
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(instockNo) || StringUtils.isBlank(ownerNo)
					|| StringUtils.isBlank(instockWorker)) {
				result = "缺少参数";
			} else {
				billUmInstockDtlManager.saveDtl(locno, instockNo, ownerNo, instockWorker, instockDtlStr);
				result = "success";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}

	@RequestMapping(value = "/splitDtl")
	@ResponseBody
	public Object splitDtl(BillUmInstockDtl billUmInstockDtl, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			String locno = billUmInstockDtl.getLocno();
			String instockNo = billUmInstockDtl.getInstockNo();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			Long instockId = billUmInstockDtl.getInstockId();
			BigDecimal realQtyIn = new BigDecimal(req.getParameter("realQtyIn"));
			BigDecimal newRealQty = new BigDecimal(req.getParameter("newRealQty"));
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(instockNo) || StringUtils.isBlank(ownerNo)
					|| instockId == null || instockId == 0) {
				result = "缺少参数";
			} else {
				BillUmInstockDtl newBillUmInstockDtl = billUmInstockDtlManager.splitDtl(billUmInstockDtl, realQtyIn,
						newRealQty);
				result = "success";
				map.put("newBillUmInstockDtl", newBillUmInstockDtl);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}

	@RequestMapping(value = "/deleteDtl")
	@ResponseBody
	public Object deleteDtl(BillUmInstockDtl billUmInstockDtl, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "success";
		try {
			String locno = billUmInstockDtl.getLocno();
			String instockNo = billUmInstockDtl.getInstockNo();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			String itemNo = billUmInstockDtl.getItemNo();
			String sizeNo = billUmInstockDtl.getSizeNo();
			String boxNo = billUmInstockDtl.getBoxNo();
			Long instockId = billUmInstockDtl.getInstockId();
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(instockNo) || StringUtils.isBlank(ownerNo)
					|| StringUtils.isBlank(itemNo) || StringUtils.isBlank(sizeNo) || StringUtils.isBlank(boxNo)
					|| instockId == null || instockId == 0) {
				result = "缺少参数";
			} else {
				BigDecimal newRealQty = billUmInstockDtlManager.deleteDtl(billUmInstockDtl);
				if (newRealQty != null) {
					map.put("resultRealQty", newRealQty);
				}
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
		}
		map.put("result", result);
		return map;
	}

	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billUmInstockDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmInstockDtl> list = this.billUmInstockDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal instockedQty = new BigDecimal(0);
			for (BillUmInstockDtl dtl : list) {
				/*if (StringUtils.isEmpty(dtl.getRealCellNo())) {
					dtl.setRealCellNo(dtl.getDestCellNo());
				}
				if (dtl.getRealQty() == null || dtl.getRealQty().compareTo(new BigDecimal(0)) == 0) {
					dtl.setRealQty(dtl.getItemQty());
				}*/
				totalItemQty = totalItemQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty() == null ? new BigDecimal(0) : dtl.getRealQty());
				instockedQty = instockedQty.add(dtl.getInstockedQty() == null ? new BigDecimal(0) : dtl.getInstockedQty());
			}
			footer.put("itemNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footer.put("instockedQty", instockedQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billUmInstockDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
					sumFoot.put("instocked_qty", 0);
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

	@RequestMapping(value = "/printDetail")
	@ResponseBody
	public Object printDetail(HttpServletRequest req) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String locno = StringUtils.isEmpty(req.getParameter("locno")) ? "" : String.valueOf(req.getParameter("locno"));
		String keys = StringUtils.isEmpty(req.getParameter("keys")) ? "" : String.valueOf(req.getParameter("keys"));
		try {
			List<String> htmlList = billUmInstockDtlManager.printDetail(locno, keys);
			if (htmlList == null || htmlList.size() == 0) {
				obj.put("msg", "没有需要打印的数据");
			} else {
				obj.put("result", "success");
				obj.put("data", htmlList);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}