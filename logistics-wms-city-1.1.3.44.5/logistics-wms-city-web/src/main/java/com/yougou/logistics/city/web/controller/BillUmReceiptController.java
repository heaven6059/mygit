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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillUmReceipt;
import com.yougou.logistics.city.common.model.BillUmReceiptKey;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmReceiptManager;
import com.yougou.logistics.city.manager.BillUmUntreadManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 退仓收货
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
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
@RequestMapping("/bill_um_receipt")
@ModuleVerify("25060010")
public class BillUmReceiptController extends BaseCrudController<BillUmReceipt> {
	@Log
	private Logger log;
    @Resource
    private BillUmReceiptManager billUmReceiptManager;
    
    @Resource
    private BillUmUntreadManager billUmUntreadManager;
    private static final String STATUS10 = "10";
    private static final String STATUS11 = "11";
    @Override
    public CrudInfo init() {
        return new CrudInfo("billumreceipt/",billUmReceiptManager);
    }
    
    //新增主表
  	@RequestMapping(value = "/saveMainInfo")
  	//@OperationVerify(OperationVerifyEnum.ADD)
  	@ResponseBody
  	public Map<String, Object> addMain(BillUmReceipt billUmReceipt, HttpServletRequest req) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			BillUmUntreadKey billUmUntreadkey = new BillUmUntreadKey();
  			billUmUntreadkey.setLocno(billUmReceipt.getLocno());
  			billUmUntreadkey.setOwnerNo(billUmReceipt.getOwnerNo());
  			billUmUntreadkey.setUntreadNo(billUmReceipt.getUntreadNo());
  			BillUmUntread billUmUntread = (BillUmUntread) billUmUntreadManager.findById(billUmUntreadkey);
  			if(null == billUmUntread ||!STATUS11.equals(billUmUntread.getStatus()) ){
  				map.put("flag", "notEXits");
  				map.put("result", "单据 :" + billUmUntread.getUntreadNo() +"已删除或者状态已改变！");
  				return map;
  			}
  			
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
  			String receiptNo = billUmReceiptManager.addMain(billUmReceipt, user);
  			result = "success";
  			map.put("receiptNo", receiptNo);
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  			result = "系统异常,请联系管理员!";
  		}
  		map.put("result", result);
  		return map;
  	}
  	/**
  	 * 删除
  	 * @param billUmReceipt
  	 * @param req
  	 * @return
  	 */
  	@RequestMapping(value = "/delBillUmReceipt")
  	@ResponseBody
  	public Map<String, Object> delBillUmReceipt(HttpServletRequest req) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			String deleted = req.getParameter("deleted");
  			//System.out.println(deleted);
  			billUmReceiptManager.deleteReceipt(deleted);
  			result = "success";
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  			map.put("flag", "error");
  			map.put("msg", e.getMessage());
  			result = "系统异常,请联系管理员!";
  		}
  		map.put("result", result);
  		return map;
  	}
  	/**
  	 * 修改
  	 * @param billUmReceipt
  	 * @param req
  	 * @return
  	 */
  	@RequestMapping(value = "/modifyReceipt")
  	@ResponseBody
  	public Map<String, Object> modifyReceipt(BillUmReceipt billUmReceipt, HttpServletRequest req) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmReceipt.setEditorName(user.getUsername());
			
  			BillUmReceiptKey billUmReceiptKey = new BillUmReceiptKey();
			billUmReceiptKey.setLocno(billUmReceipt.getLocno());
			billUmReceiptKey.setOwnerNo(billUmReceipt.getOwnerNo());
			billUmReceiptKey.setReceiptNo(billUmReceipt.getReceiptNo());
			BillUmReceipt bReceipt =(BillUmReceipt) billUmReceiptManager.findById(billUmReceiptKey);
				if (null == bReceipt ||!STATUS10.equals(bReceipt.getStatus())){
					result = "notExist";
					map.put("result", "单据:"+billUmReceipt.getReceiptNo() +"已删除或者状态发生改变！");
					return map;
				}
  			
  			int a = billUmReceiptManager.modifyById(billUmReceipt);
  			if(a < 1){
  				result = "修改异常,请联系管理员!";
  			}else{
  				result = "success";
  			}
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  			result = "系统异常,请联系管理员!";
  		}
  		map.put("result", result);
  		return map;
  	}
  	/**
  	 * 验证店退仓单是否已经生成退仓收货单
  	 * @param untreadMm
  	 * @param req
  	 * @return
  	 */
  	@RequestMapping(value = "/checkUntread")
  	@ResponseBody
  	public Map<String, Object> checkUntread(BillUmReceipt billUmReceipt) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			String locno = billUmReceipt.getLocno();
  			String untreadNo = billUmReceipt.getUntreadNo();
  			String ownerNo = billUmReceipt.getOwnerNo();
  			if(StringUtils.isBlank(locno)||
  					StringUtils.isBlank(untreadNo)||
  					StringUtils.isBlank(ownerNo)){
  				result = "此记录不合法,请重新选择!";
  			}else{
  				Map<String, Object> params = new HashMap<String, Object>();
  				params.put("locno", locno);
  				params.put("untreadNo", untreadNo);
  				params.put("ownerNo", ownerNo);
  				int num = billUmReceiptManager.findCount(params);
  				if(num < 1){
  					result = "success";
  				}else{
  					result = "店退仓单【"+untreadNo+"】已经生成退仓收货单,请重新选择!";
  				}
  			}
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  			result = "系统异常,请联系管理员!";
  		}
  		map.put("result", result);
  		return map;
  	}
  	/**
  	 * 
  	 * @param req
  	 * @return
  	 */
  	@RequestMapping(value = "/batchCreate")
  	@ResponseBody
  	public Map<String, Object> batchCreate(HttpServletRequest req) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
  			String locnoStr = req.getParameter("locnoStr");
  			String untreadNoStr = req.getParameter("untreadNoStr");
  			String ownerNoStr = req.getParameter("ownerNoStr");
  			if(StringUtils.isBlank(locnoStr)||
  					StringUtils.isBlank(untreadNoStr)||
  					StringUtils.isBlank(ownerNoStr)){
  				result = "此记录不合法,请重新选择!";
  			}else{
  				billUmReceiptManager.batchCreate(locnoStr, untreadNoStr, ownerNoStr, user);
  				result = "success";
  			}
  		}catch (ManagerException e) {
  			log.error(e.getMessage(), e);
  			result = e.getMessage();
  		}catch (Exception e) {
  			log.error(e.getMessage(), e);
  			result = "系统异常,请联系管理员!";
  		}
  		map.put("result", result);
  		return map;
  	}
  	/**
  	 * 审核
  	 * @param untreadMm
  	 * @param req
  	 * @return
  	 */
  	@RequestMapping(value = "/check")
  	@ResponseBody
  	public Map<String, Object> check(BillUmReceipt billUmReceipt, HttpServletRequest req) {
  		Map<String, Object> map = new HashMap<String, Object>();
  		String result = "";
  		try {
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
  			String locno = billUmReceipt.getLocno();
  			String keys = billUmReceipt.getKeys();
  			if(StringUtils.isBlank(locno)||
  					StringUtils.isBlank(keys)){
  				result = "此记录不合法,请重新选择!";
  			}else{
  				billUmReceiptManager.check(billUmReceipt, user);
  				result = "success";
  			}
  		} catch (ManagerException e) {
  			log.error(e.getMessage(), e);
  			result = e.getMessage();
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  			result = "系统异常";
  		}
  		map.put("result", result);
  		return map;
  	}
  	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billUmReceiptManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmReceipt> list = this.billUmReceiptManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			
			
			//小计
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillUmReceipt billUmReceipt : list) {
				totalRealQty = totalRealQty.add(billUmReceipt.getRealQty());
			}
			footer.put("receiptNo", "小计");
			footer.put("realQty", totalRealQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billUmReceiptManager.selectUmReceiptSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("real_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("receipt_no", "合计");
			} else {
				sumFoot.put("receiptNo", "合计");
			}
			footerList.add(sumFoot);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
}