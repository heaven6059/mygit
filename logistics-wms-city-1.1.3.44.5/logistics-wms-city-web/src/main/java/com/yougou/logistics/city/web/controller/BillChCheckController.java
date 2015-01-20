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
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.enums.ResultMessageEnums;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillChCheckDirectManager;
import com.yougou.logistics.city.manager.BillChCheckDtlManager;
import com.yougou.logistics.city.manager.BillChCheckManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 出盘发单
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
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
@RequestMapping("/bill_ch_check")
@ModuleVerify("25120030")
public class BillChCheckController extends BaseCrudController<BillChCheck> {
	@Log
	private Logger log;
	@Resource
	private BillChCheckManager billChCheckManager;

	@Resource
	private BillChCheckDirectManager billChCheckDirectManager;

	@Resource
	private BillChCheckDtlManager billChCheckDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billchcheck/", billChCheckManager);
	}

	/**
	 * 初盘发单
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/check_list")
	public ModelAndView check_list() throws Exception {
		return new ModelAndView("billchcheck/check_list");
	}

	/**
	 * 查询定位明细中的品牌
	 * 
	 * @param req
	 * @param dto
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findBrandInDirect")
	@ResponseBody
	public Map<String, Object> findBrandInDirect(HttpServletRequest req, BillChCheckDirectDto dto)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			List<Brand> list = billChCheckDirectManager.findBrandInDirect(dto);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 查询定位表信息
	 * 
	 * @param req
	 * @param dto
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findDirect4check")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, BillChCheckDirectDto dto) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String orderby = null;
			String sort = req.getParameter("sort");
			if(CommonUtil.hasValue(sort)){
				orderby = sort;
				String order = req.getParameter("order");
				if(CommonUtil.hasValue(order)){
					orderby += " " + order;
				}
			}
			int total = billChCheckDirectManager.findDirectCount(dto);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDirectDto> list = billChCheckDirectManager.findDirectList(dto, orderby, page);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/selectAllCellCountAndStockCount")
	@ResponseBody
	public Map<String, Object> selectAllCellCountAndStockCount(HttpServletRequest req, BillChCheckDirectDto dto)
			throws ManagerException {
		try {
			Map<String, Object> map = billChCheckDirectManager.selectAllCellCountAndStockCount(dto);
			map.put("result", ResultMessageEnums.SUCCESS.getMessage());
			return map;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result", "error");
			obj.put("msg", "获取储位数，通道数失败");
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result", "error");
			obj.put("msg", "系统异常，请联系管理员");
			return obj;
		}
	}

	/**
	 * 生成盘点单
	 * 
	 * @param req
	 * @param dto
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/createBillChCheck")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> createBillChCheck(HttpServletRequest req, BillChCheckDirectDto dto, String checkDate,
			Integer stockCount, Integer cellCount) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			long curDate = System.currentTimeMillis();
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			dto.setLocno(user.getLocNo());
			billChCheckManager.createBillChCheck(dto, checkDate, stockCount, cellCount, user.getLoginName(),user.getUsername());
			obj.put("result", "success");
			/*
			 * System.out.println("消耗时间：" + (System.currentTimeMillis() -
			 * curDate) / 1000 + "秒");
			 */
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("errorCode", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("errorCode", "生成任务失败");
		}
		return obj;
	}

	@RequestMapping(value = "/main_list.json")
	@ResponseBody
	public Map<String, Object> MainList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billChCheckManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDto> list = this.billChCheckManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			
			
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			for (BillChCheckDto checkDto : list) {
				totalItemQty = totalItemQty.add(checkDto.getItemQty()==null?new BigDecimal(0):checkDto.getItemQty());
			}
			
			footer.put("checkNo", "小计");
			footer.put("itemQty", totalItemQty);
			footerList.add(footer);
			
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billChCheckManager.selectChCheckPlanSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("check_no", "合计");
			} else {
				sumFoot.put("checkNo", "合计");
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

	/**
	 * 批量分配盘点人员
	 * 
	 * @param req
	 * @param dto
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/distributionAssignNoBatch")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> distributionAssignNoBatch(HttpServletRequest req, String checkNos, String assignNo) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
			Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billChCheckManager.distributionAssignNoBatch(user.getLocNo(), checkNos, assignNo,Integer.parseInt(systemId.toString()), Integer.parseInt(areaSystemId.toString()));
			obj.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", "系统异常，请联系管理员");
		}
		return obj;
	}

	/**
	 * 出盘回单主档信息
	 * 
	 * @param req
	 * @param dto
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectChCheck")
	@ResponseBody
	public Map<String, Object> selectChCheck(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = billChCheckManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDto> list = billChCheckManager.selectChCheck(params, page, authorityParams);
			
			
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalRealCount = new BigDecimal(0);
			BigDecimal totalDifferlCount = new BigDecimal(0);
			for (BillChCheckDto billOmRecheck : list) {
				totalRealQty = totalRealQty.add(BigDecimal.valueOf(billOmRecheck.getCellCount()==null?0:billOmRecheck.getCellCount()));
				totalRecheckQty = totalRecheckQty.add(BigDecimal.valueOf(billOmRecheck.getItemCount()==null?0:billOmRecheck.getItemCount()));
				totalRealCount = totalRealCount.add(BigDecimal.valueOf(billOmRecheck.getRealCount()==null?0:billOmRecheck.getRealCount()));
				totalDifferlCount = totalDifferlCount.add(billOmRecheck.getDifferlCount());
			}
			
			footer.put("planNo", "小计");
			footer.put("cellCount", totalRealQty);
			footer.put("itemCount", totalRecheckQty);
			footer.put("realCount", totalRealCount);
			footer.put("differlCount", totalDifferlCount);
			footerList.add(footer);
			
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot =billChCheckManager.selectChCheckSumQty(params, authorityParams); //billOmRecheckManager.selectOutstockRecheckSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("cell_Count", 0);
					sumFoot.put("item_Count", 0);
					sumFoot.put("real_Count", 0);
					sumFoot.put("differl_Count", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("plan_No", "合计");
				footerList.add(sumFoot);
			} else {
				sumFoot.put("plan_No", "合计");
			}
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			obj.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 查询盘点单下的储位
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */

	@RequestMapping(value = "/selectCellNo")
	@ResponseBody
	public Map<String, Object> selectCellNo(HttpServletRequest req, BillChCheck check) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			check.setLocno(user.getLocNo());
			List<BillChCheckDtl> list = billChCheckDtlManager.selectCellNo(check);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 查询盘点计划明细中的储位
	 * @param req
	 * @param planNo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findCellNobyPlan")
	@ResponseBody
	public List<BillChCheckDtl> findCellNobyPlan(HttpServletRequest req, String planNo) throws ManagerException {
		List<BillChCheckDtl> list=new ArrayList<BillChCheckDtl>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			list= billChCheckDtlManager.findCellNobyPlan(planNo,user.getLocNo());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 初盘回单审核
	 * 
	 * @param req
	 * @param check
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/chCheck")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> chCheck(HttpServletRequest req, String checkNos) throws ManagerException {
		HttpSession session = req.getSession();
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			billChCheckManager.chCheck(checkNos, user.getLocNo(), user.getLoginName(),user.getUsername());
			obj.put("result", ResultMessageEnums.SUCCESS.getMessage());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", "系统异常，请联系管理员");
		}
		return obj;
	}

	/**
	 * 复盘发单
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/recheck_list")
	public ModelAndView recheck_list() throws Exception {
		return new ModelAndView("billchcheck/recheck_list");
	}

	/**
	 * 复盘发单
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectReChCheck")
	@ResponseBody
	public Map<String, Object> selectReChCheck(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map params = builderParams(req, model);
			int total = billChCheckManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDto> list = billChCheckManager.selectChCheck(params, page, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", "系统异常，请联系管理员");
		}
		return obj;
	}

	/**
	 * 盘点回单
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/receipt_list")
	public ModelAndView receipt_list() throws Exception {
		return new ModelAndView("billchcheck/receipt_list");
	}

	/**
	 * 复盘回单
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/re_receipt_list")
	public ModelAndView re_receipt_list() throws Exception {
		return new ModelAndView("billchcheck/re_receipt_list");
	}
	
	/**
	 * 判断时候已经开始盘点(即盘点明细中check_qty是否有存在大于0的记录)
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/hasBegunCheck")
	@ResponseBody
	public Map<String, Object> hasBegunCheck(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String checkNos = req.getParameter("checkNos");
			String locno = req.getParameter("locno");
			if(CommonUtil.hasValue(checkNos) && CommonUtil.hasValue(locno)){
				String result = billChCheckManager.hasBegunCheck(locno, checkNos);
				if(CommonUtil.hasValue(result)){
					obj.put("result", ResultMessageEnums.SUCCESS.getMessage());
					obj.put("msg", result+"你是否确认继续还原？");
				}
			}else{
				obj.put("result", ResultMessageEnums.ERROR.getMessage());
				if(!CommonUtil.hasValue(locno)){
					obj.put("msg", "缺少仓库编码");
				}else{
					obj.put("msg", "缺少盘点单号");
				}
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", "系统异常，请联系管理员");
		}
		return obj;
	}
	
	/**
	 * 还原盘点单
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/restoreCheck")
	@ResponseBody
	public Map<String, Object> restoreCheck(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String values = req.getParameter("values");
			String locno = req.getParameter("locno");
			if(CommonUtil.hasValue(values) && CommonUtil.hasValue(locno)){
				billChCheckManager.restoreCheck(locno, values);
				
			}else{
				obj.put("result", ResultMessageEnums.ERROR.getMessage());
				if(!CommonUtil.hasValue(locno)){
					obj.put("msg", "缺少仓库编码");
				}else{
					obj.put("msg", "缺少盘点单号");
				}
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", "系统异常，请联系管理员");
		}
		return obj;
	}
}