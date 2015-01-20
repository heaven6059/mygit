package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.enums.ResultMessageEnums;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.dal.mapper.BillChCheckDtlMapper;
import com.yougou.logistics.city.manager.BillChCheckDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 初盘出单明细
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
@RequestMapping("/bill_ch_check_dtl")
public class BillChCheckDtlController extends BaseCrudController<BillChCheckDtl> {
	@Resource
	private BillChCheckDtlManager billChCheckDtlManager;
	@Resource
	private BillChCheckDtlMapper billChCheckDtlMapper;
	@Resource
	private SizeInfoManager sizeInfoManager;
	@Override
	public CrudInfo init() {
		return new CrudInfo("billchcheckdtl/", billChCheckDtlManager);
	}

	@Log
	Logger log;

	@RequestMapping(value = "/edit_list.json")
	@ResponseBody
	public Map<String, Object> editList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billChCheckDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = this.billChCheckDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return obj;
	}

	/**
	 * 保存明细
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
	@RequestMapping(value = "/saveCheckDtl")
	@ResponseBody
	public Map<String, Object> saveCheckDtl(HttpServletRequest req, String inserted, String deleted, String updated,
			String checkNo) throws ManagerException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);

			String insertedStr = URLDecoder.decode(inserted, "UTF-8");
			String deletedStr = URLDecoder.decode(deleted, "UTF-8");
			String updatedStr = URLDecoder.decode(updated, "UTF-8");
			List<BillChCheckDtl> insertList = new ArrayList<BillChCheckDtl>();
			List<BillChCheckDtl> deleteList = new ArrayList<BillChCheckDtl>();
			List<BillChCheckDtl> updatedList = new ArrayList<BillChCheckDtl>();

			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(insertedStr)) {
				List<Map> list = mapper.readValue(insertedStr, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference2(mapper, list);
			}
			if (StringUtils.isNotEmpty(deletedStr)) {
				List<Map> list = mapper.readValue(deletedStr, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference2(mapper, list);
			}
			if (StringUtils.isNotEmpty(updatedStr)) {
				List<Map> list = mapper.readValue(updatedStr, new TypeReference<List<Map>>() {
				});
				updatedList = convertListWithTypeReference2(mapper, list);
			}

			ResultVo resultVo = billChCheckDtlManager.saveCheckDtl(insertList, updatedList, deleteList, checkNo,
					user.getLocNo(), user.getLoginName());
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
	private List<BillChCheckDtl> convertListWithTypeReference2(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillChCheckDtl> tl = new ArrayList<BillChCheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillChCheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillChCheckDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/selectItem4ChCheck")
	@ResponseBody
	public Map<String, Object> selectItem4ChCheck(HttpServletRequest req, BillChCheckDtl dtl) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			int total = billChCheckDtlManager.selectItem4ChCheckCount(dtl, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = billChCheckDtlManager.selectItem4ChCheck(page, dtl, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return obj;
	}
	
	
	/**
	 * 查找盘点计划单内的商品
	 * @param req
	 * @param dtl
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findItemByPlan")
	@ResponseBody
	public Map<String, Object> findItemByPlan(HttpServletRequest req, BillChCheckDtl dtl) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			if(dtl.getItemNo() != null) {
				dtl.setItemNo(dtl.getItemNo().toUpperCase());
			}
			if(dtl.getBarcode() != null) {
				dtl.setBarcode(dtl.getBarcode().toUpperCase());
			}
			int total = billChCheckDtlManager.findItemByPlanCount(dtl, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = billChCheckDtlManager.findItemByPlan(page, dtl, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return obj;
	}

	/**
	 * 查询商品明细（商品盘、限制品牌）
	 * 
	 * @param req
	 * @param dtl
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findBrandLimitItem")
	@ResponseBody
	public Map<String, Object> findBrandLimitItem(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = billChCheckDtlManager.selectBrandLimitItemCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = billChCheckDtlManager.selectBrandLimitItem(page, params);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		return obj;
	}

	/**
	 * 复盘发单信息汇总
	 * 
	 * @param req
	 * @param dtl
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectReChCheck")
	@ResponseBody
	public Map<String, Object> selectReChCheck(HttpServletRequest req, BillChCheckDtl dtl, Model model)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map map = this.builderParams(req, model);
			int total = billChCheckDtlManager.selectReChCheckCount(map, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDto> list = billChCheckDtlManager.selectReChCheck(map, page, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return obj;
	}

	/**
	 * 查询复盘单下的储位
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
	 * 打印
	 * 
	 * @param req
	 * @param dtl
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/printBatch")
	@ResponseBody
	public Map<String, Object> queryAllDetal(HttpServletRequest req, Model model, String key) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> result = this.billChCheckDtlManager.printBatch(key);
			obj.put("data", result);
			obj.put("curDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			obj.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", ResultEnums.FAIL.getResultMsg());
			obj.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("msg", "系统异常");
			obj.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return obj;
	}

	@RequestMapping(value = "/listByPlanNo.json")
	@ResponseBody
	public Map<String, Object> queryListByPlanNo(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billChCheckDtlManager.findCountByPlanNo(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = this.billChCheckDtlManager.findByPageByPlanNo(page, params);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}

		return obj;
	}

	@RequestMapping(value = "/dtl_listByPlanNo.json")
	@ResponseBody
	public Map<String, Object> queryListByPlanNoDtl(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billChCheckDtlManager.findCountByPlanNo(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = this.billChCheckDtlManager.findByPageByPlanNo(page, params);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalCheckQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			for (BillChCheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
				totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
			}
			footer.put("checkNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("checkQty", totalCheckQty);
			footer.put("recheckQty", totalRecheckQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billChCheckDtlMapper.selectSumQty4Plan(params);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_Qty", 0);
					sumFoot.put("check_Qty", 0);
					sumFoot.put("recheck_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("check_No", "合计");
			} else {
				sumFoot.put("checkNo", "合计");
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
			int total = this.billChCheckDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = this.billChCheckDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalCheckQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalDiffQty = new BigDecimal(0);
			BigDecimal recheckDiffQty = new BigDecimal(0);
			for (BillChCheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
				if (dtl.getRecheckQty() == null) {
					totalRecheckQty = totalRecheckQty.add(new BigDecimal(0));
				} else {
					totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
				}

				totalDiffQty = totalDiffQty.add(dtl.getDiffQty());
				if (dtl.getRecheckDiffQty() == null) {
					recheckDiffQty = recheckDiffQty.add(new BigDecimal(0));
				} else {
					recheckDiffQty = recheckDiffQty.add(dtl.getRecheckDiffQty());
				}

			}
			footer.put("cellNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("checkQty", totalCheckQty);
			footer.put("recheckQty", totalRecheckQty);
			footer.put("diffQty", totalDiffQty);
			footer.put("recheckDiffQty", recheckDiffQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				if(pageSize >= total){
					sumFoot.putAll(footer);
					sumFoot.put("cellNo", "合计");
				}else{
					sumFoot = billChCheckDtlManager.selectSumQty(params, authorityParams);
					if (sumFoot == null) {
						sumFoot = new SumUtilMap<String, Object>();
						sumFoot.put("item_qty", 0);
						sumFoot.put("check_qty", 0);
						sumFoot.put("recheck_qty", 0);
						sumFoot.put("diff_Qty", 0);
						sumFoot.put("recheck_Diff_Qty", 0);
					}
					sumFoot.put("cell_no", "合计");
				}
				sumFoot.put("isselectsum", true);
				
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

	@RequestMapping(value = "/re_dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryReDtlList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billChCheckDtlManager.findByPage4CellCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillChCheckDtl> list = this.billChCheckDtlManager.findByPage4Cell(page, sortColumn, sortOrder, params,
					authorityParams);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalCheckQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalDiffQty = new BigDecimal(0);
			BigDecimal recheckDiffQty = new BigDecimal(0);
			for (BillChCheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
				if (dtl.getRecheckQty() == null) {
					totalRecheckQty = totalRecheckQty.add(new BigDecimal(0));
				} else {
					totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
				}

				totalDiffQty = totalDiffQty.add(dtl.getDiffQty());
				if (dtl.getRecheckDiffQty() == null) {
					recheckDiffQty = recheckDiffQty.add(new BigDecimal(0));
				} else {
					recheckDiffQty = recheckDiffQty.add(dtl.getRecheckDiffQty());
				}

			}
			footer.put("cellNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("checkQty", totalCheckQty);
			footer.put("recheckQty", totalRecheckQty);
			footer.put("diffQty", totalDiffQty);
			footer.put("recheckDiffQty", recheckDiffQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billChCheckDtlManager.selectSumQty4Cell(params, null);
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
	public Map<String, Object> saveByPlan(HttpServletRequest req, Model model, BillChCheck chCheck)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			this.billChCheckDtlManager.saveByPlan(chCheck);
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
	public Map<String, Object> resetPlan(HttpServletRequest req, Model model, BillChCheck chCheck)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			this.billChCheckDtlManager.resetPlan(chCheck);
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

	@RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 FileUtils.downloadTemple(session, response, "receiptTemplate.xls");
	}
	@RequestMapping("/iframe4receipt")
	public ModelAndView iframe(String wasteNo,String checkNo) throws Exception {
		ModelAndView mode = new ModelAndView("billchcheck/iframe4receipt");
		mode.addObject("checkNo", checkNo);
		return mode;
	}
	
	@RequestMapping(value = "/importExcel")
	public ModelAndView upLoad(HttpServletRequest request,Model model,String checkNo) {
		ModelAndView mode = new ModelAndView("billchcheck/iframe4receipt");
		mode.addObject("checkNo", checkNo);
		SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
	    try{
	    	String [] colNames = {"cellNo","itemNo","sizeNo","checkQty"};
	    	boolean [] mustArray = {true,true,true,true};
			List<BillChCheckDtl> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, new String [] {"cellNo","itemNo","sizeNo"}, BillChCheckDtl.class);
			 if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(request);
			 Map<String, Object> params = new HashMap<String, Object>();
			 params.put("locno", user.getLocNo());
			 params.put("checkNo", checkNo);
			 params.put("itemNoIsNot", "N");
			 int total = this.billChCheckDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(1, total, (int) total);
			List<BillChCheckDtl> existList = this.billChCheckDtlManager.findByPage(page, null, null, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			List<BillChCheckDtl> insertList = new ArrayList<BillChCheckDtl>();
			List<BillChCheckDtl> updatedList = new ArrayList<BillChCheckDtl>();
			if(!CommonUtil.hasValue(existList)){
				insertList = list;
			}else{
				Map<String, BillChCheckDtl> existMap = new HashMap<String, BillChCheckDtl>();
				for(BillChCheckDtl d:existList){
					existMap.put(d.getCellNo()+d.getItemNo()+d.getSizeNo(), d);
				}
				for(BillChCheckDtl d:list){
					String key = new StringBuilder().append(d.getCellNo()).append(d.getItemNo()).append(d.getSizeNo()).toString();
					if(existMap.get(key) != null){
						BillChCheckDtl temp = existMap.get(key);
						temp.setCheckQty(d.getCheckQty());
						updatedList.add(temp);
					}else{
						d.setCheckWorker(user.getLoginName());//导入操作 写入当前盘点人员
						insertList.add(d);
					}
				}
			}
			ResultVo resultVo = billChCheckDtlManager.saveCheckDtl(insertList, updatedList, null, checkNo,
					user.getLocNo(), user.getLoginName());
			if (resultVo.isSuccess()) {
				mode.addObject("result", ResultEnums.SUCCESS.getResultMsg());
			} else {
				mode.addObject("result", ResultMessageEnums.ERROR.getMessage());
				mode.addObject("msg", resultVo.getErrorMessage());
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mode.addObject("result", ResultEnums.FAIL.getResultMsg());
			mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
		}
		return mode;
	}
	/**
	 * 初盘回单明细 汇总导出
	 * @param modelType
	 * @param req
	 * @param model
	 * @param response
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/dtlExport")
	public void export(BillChCheckDtl modelType, HttpServletRequest req, Model model, HttpServletResponse response)throws ManagerException{
		/*导出信息*/
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req.getParameter("preColNames");
		/*合计*/
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req.getParameter("endColNames");
		String fileName = StringUtils.isEmpty(req.getParameter("fileName")) ? "" : req.getParameter("fileName");
		String locno = StringUtils.isEmpty(req.getParameter("locno")) ? "" : req.getParameter("locno");
		/*盘点单号*/
		String checkNo = StringUtils.isEmpty(req.getParameter("checkNo")) ? "" : req.getParameter("checkNo");
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			ObjectMapper mapper = new ObjectMapper();
			List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
			List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();
			if (StringUtils.isNotEmpty(preColNames)) {
				preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}
			if (StringUtils.isNotEmpty(endColNames)) {
				endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}
			BillChCheckDtl billChCheckDtl = new BillChCheckDtl();
			billChCheckDtl.setLocno(locno);
			billChCheckDtl.setCheckNo(checkNo);
			/*尺码横排  查询商品所属的品牌*/
			List<BillChCheckDtl> sysNoList = billChCheckDtlManager.findDtlSysNo(billChCheckDtl,authorityParams);
			HSSFWorkbook wb = null;
			if(sysNoList != null && sysNoList.size() > 0){
				for(int i=0; i < sysNoList.size(); i++){
					BillChCheckDtl dd = sysNoList.get(i);
					String sysNoStr = dd.getSysNoStr();
					billChCheckDtl.setSysNo(dd.getSysNo());
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("sysNo", dd.getSysNo());
					List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null, params);
					Map<String,List<String>> sizeTypeMap = new TreeMap<String, List<String>>();
					for(SizeInfo si : sizeTypeList){
						String sizeKind = si.getSizeKind();
						if(sizeTypeMap.get(sizeKind) == null){
							List<String> sizeCodeByKind = new ArrayList<String>();
							sizeCodeByKind.add(si.getSizeCode());
							sizeTypeMap.put(sizeKind, sizeCodeByKind);
						}else{
							sizeTypeMap.get(sizeKind).add(si.getSizeCode());
						}
					}
					Map<String,Object> obj=new HashMap<String,Object>();
					obj = billChCheckDtlManager.findDtlSysNoByPage(billChCheckDtl, authorityParams);
					@SuppressWarnings("unchecked")
					List<BillChCheckDtl> data =  (List<BillChCheckDtl>) obj.get("rows");
					wb = ExcelUtils.getSheet(preColNamesList, sizeTypeMap, endColNamesList, fileName, data, true, i, sysNoStr, wb);	
				}
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
				response.setHeader("Pragma", "no-cache");
				wb.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}