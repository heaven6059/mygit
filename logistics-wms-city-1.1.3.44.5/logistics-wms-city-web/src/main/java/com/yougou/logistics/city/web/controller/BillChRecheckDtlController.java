package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.enums.BillChCheckStautsEnums;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.enums.ResultMessageEnums;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtlDto;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;
import com.yougou.logistics.city.dal.mapper.BillChRecheckDtlMapper;
import com.yougou.logistics.city.manager.BillChRecheckDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Dec 17 18:31:03 CST 2013
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
@RequestMapping("/bill_ch_recheck_dtl")
@ModuleVerify("25120040")
public class BillChRecheckDtlController extends BaseCrudController<BillChRecheckDtl> {
	@Resource
	private BillChRecheckDtlManager billChRecheckDtlManager;
	@Resource
	private BillChRecheckDtlMapper billChRecheckDtlMapper;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billchrecheckdtl/", billChRecheckDtlManager);
	}

	@Log
	Logger log;

	/**
	 * 复盘发单
	 * 
	 * @param req
	 * @param checkedRows
	 * @param reCheckWorker
	 * @return
	 * @throws ManagerException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveReCheckDtl")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveReCheckDtl(HttpServletRequest req, String checkedRows, String reCheckWorker)
			throws ManagerException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
			Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
			String dataStr = URLDecoder.decode(checkedRows, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillChCheckDtl> dataList = new ArrayList<BillChCheckDtl>();
			if (StringUtils.isNotEmpty(dataStr)) {
				List<Map> list = mapper.readValue(dataStr, new TypeReference<List<Map>>() {
				});
				dataList = convertListWithTypeReference2(mapper, list);
			}
			billChRecheckDtlManager.createChReCheckDtl(dataList, reCheckWorker,Integer.parseInt(systemId.toString()), Integer.parseInt(areaSystemId.toString()));
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillChCheckDtl> convertListWithTypeReference2(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillChCheckDtl> tl = new ArrayList<BillChCheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillChCheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillChCheckDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/selectReCheck")
	@ResponseBody
	public Map<String, Object> selectChCheck(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String,Object> params = builderParams(req, model);
			int total = billChRecheckDtlManager.selectReCheckCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChRecheckDtlDto> list = billChRecheckDtlManager.selectReCheck(params, page, authorityParams);
			
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalRealCount = new BigDecimal(0);
			BigDecimal totalDifferlCount = new BigDecimal(0);
			for (BillChRecheckDtlDto billChRecheckDtlDto : list) {
				totalRealQty = totalRealQty.add(BigDecimal.valueOf(billChRecheckDtlDto.getCellCount()));
				totalRecheckQty = totalRecheckQty.add(BigDecimal.valueOf(billChRecheckDtlDto.getItemCount()));
				totalRealCount = totalRealCount.add(BigDecimal.valueOf(billChRecheckDtlDto.getRealCount()));
				totalDifferlCount = totalDifferlCount.add(billChRecheckDtlDto.getDifferlCount());
			}
			
			footer.put("planNo", "小计");
			footer.put("cellCount", totalRealQty);
			footer.put("itemCount", totalRecheckQty);
			footer.put("realCount", totalRealCount);
			footer.put("differlCount", totalDifferlCount);
			footerList.add(footer);
			
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot =billChRecheckDtlManager.selectChReCheckSumQty(params, authorityParams); 
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

	@RequestMapping(value = "/selectCellNo")
	@ResponseBody
	public Map<String, Object> selectCellNo(HttpServletRequest req, BillChRecheckDtl check) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			check.setLocno(user.getLocNo());
			List<BillChRecheckDtl> list = billChRecheckDtlManager.selectCellNo(check);

			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return obj;
	}

	/**
	 * 更新明细
	 * 
	 * @param req
	 * @param allData
	 * @param checkNo
	 * @return
	 * @throws ManagerException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	@RequestMapping(value = "/updateReCheckDtl")
	@ResponseBody
	public Map<String, Object> updateReCheckDtl(HttpServletRequest req, String inserted, String deleted,
			String updated, String checkNo, String recheckNo) throws ManagerException, JsonParseException,
			JsonMappingException, IOException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String insertedStr = URLDecoder.decode(inserted, "UTF-8");
			String deletedStr = URLDecoder.decode(deleted, "UTF-8");
			String updatedStr = URLDecoder.decode(updated, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillChRecheckDtl> insertList = new ArrayList<BillChRecheckDtl>();
			List<BillChRecheckDtl> deleteList = new ArrayList<BillChRecheckDtl>();
			List<BillChRecheckDtl> updatedList = new ArrayList<BillChRecheckDtl>();
			if (StringUtils.isNotEmpty(insertedStr)) {
				List<Map> list = mapper.readValue(insertedStr, new TypeReference<List<Map>>() {
				});
				insertList = convertReListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(deletedStr)) {
				List<Map> list = mapper.readValue(deletedStr, new TypeReference<List<Map>>() {
				});
				deleteList = convertReListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(updatedStr)) {
				List<Map> list = mapper.readValue(updatedStr, new TypeReference<List<Map>>() {
				});
				updatedList = convertReListWithTypeReference(mapper, list);
			}
			ResultVo resultVo = billChRecheckDtlManager.updateReCheckDtl(insertList, updatedList, deleteList,
					recheckNo, checkNo, user.getLocNo());

			if (resultVo.isSuccess()) {
				obj.put("result", ResultMessageEnums.SUCCESS.getMessage());
			} else {
				obj.put("result", ResultMessageEnums.ERROR.getMessage());
				obj.put("msg", resultVo.getErrorMessage());
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", "保存失败，请联系管理员");
		}
		return obj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillChRecheckDtl> convertReListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillChRecheckDtl> tl = new ArrayList<BillChRecheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillChRecheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillChRecheckDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/chReCheckAudit")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> chReCheckAudit(HttpServletRequest req, String dtls) throws ManagerException {
		HttpSession session = req.getSession();
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			billChRecheckDtlManager.chReCheckAudit(dtls, user.getLocNo());
			obj.put("result", ResultMessageEnums.SUCCESS.getMessage());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultMessageEnums.ERROR.getMessage());
		}
		return obj;
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
			int total = this.billChRecheckDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChRecheckDtl> list = this.billChRecheckDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalDiffQty = new BigDecimal(0);
			for (BillChRecheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
				totalDiffQty = totalDiffQty.add(dtl.getDiffQty());
			}
			footer.put("cellNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("recheckQty", totalRecheckQty);
			footer.put("diffQty", totalDiffQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billChRecheckDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("check_qty", 0);
					sumFoot.put("recheck_qty", 0);
					sumFoot.put("diff_Qty", 0);
					sumFoot.put("recheck_Diff_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("cell_no", "合计");
			} else {
				sumFoot.put("cellNo", "合计");
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

	/**
	 * 按计划保存
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/saveByPlan")
	@ResponseBody
	public Map<String, Object> saveByPlan(HttpServletRequest req, Model model, BillChRecheckDtl chCheck)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			this.billChRecheckDtlManager.saveByPlan(chCheck);
			obj.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL.getResultMsg());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL.getResultMsg());
			obj.put("msg", "系统异常,请联系管理员");
		}
		return obj;
	}

	/**
	 * 实盘置零
	 * 
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/resetPlan")
	@ResponseBody
	public Map<String, Object> resetPlan(HttpServletRequest req, Model model, BillChRecheckDtl chCheck)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			this.billChRecheckDtlManager.resetPlan(chCheck);
			obj.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL.getResultMsg());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL.getResultMsg());
			obj.put("msg", "系统异常,请联系管理员");
		}
		return obj;
	}

	/**
	 * 修改主档信息
	 * @param req
	 * @param check
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/saveMain")
	@ResponseBody
	public Map<String, Object> saveMain(HttpServletRequest req, BillChRecheckDtl check) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			check.setSourceStatus(BillChCheckStautsEnums.STATUS10.getStatus());
			billChRecheckDtlManager.modifyById(check);
			obj.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "系统异常");
		}
		return obj;
	}
}