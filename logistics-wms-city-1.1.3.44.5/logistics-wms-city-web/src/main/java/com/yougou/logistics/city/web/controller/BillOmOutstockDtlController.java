package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillOmOutstockDtlManager;
import com.yougou.logistics.city.manager.BillOmOutstockManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:13 CST 2013
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
@RequestMapping("/bill_om_outstock_dtl")
@ModuleVerify("25100020")
public class BillOmOutstockDtlController extends BaseCrudController<BillOmOutstockDtl> {

	@Log
	private Logger log;
	
	@Resource
	private BillOmOutstockManager billOmOutstockManager;
	
	@Resource
	private BillOmOutstockDtlManager billOmOutstockDtlManager;
	
	@Resource
	private SizeInfoManager sizeInfoManager;

	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private final static String STATUS13 = "13";

	@Override
	public CrudInfo init() {
		return new CrudInfo("billomoutstockdtl/", billOmOutstockDtlManager);
	}
	
	
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmOutstockDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherin> list = this.billOmOutstockDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}

	@RequestMapping(value = "/dlist.json")
	@ResponseBody
	public Map<String, Object> queryList1(HttpServletRequest req, Model model) {
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
			int total = billOmOutstockDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDtlDto> list = billOmOutstockDtlManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);

			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("sCellNo", "汇总");
			footerList.add(footerMap);
			for (BillOmOutstockDtlDto temp : list) {
				try {
					FooterUtil.setFooterMap("itemQty", temp.getItemQty(), footerMap);
					FooterUtil.setFooterMap("realQty", temp.getRealQty(), footerMap);
					FooterUtil.setFooterMap("diffQty", temp.getDiffQty(), footerMap);
					FooterUtil.setFooterMap("outstockedQty", temp.getOutstockedQty(), footerMap);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmOutstockDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
					sumFoot.put("diff_qty", 0);
					sumFoot.put("outstocked_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("s_Cell_No", "合计");
			} else {
				sumFoot.put("sCellNo", "合计");
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

	@RequestMapping(value = "/do_dtlexport")
	public void doExport(BillOmOutstockDtl modelType, HttpServletRequest req, Model model, HttpServletResponse response) {
		try {
			Map<String, Object> params = builderParams(req, model);
			String exportColumns = (String) params.get("exportColumns");
			String fileName = (String) params.get("fileName");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(exportColumns)) {

				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";

				// 字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns, new TypeReference<List<Map>>() {
				});

				// List<ModelType> list= this .manager .findByBiz(modelType,
				// params);
				int total = billOmOutstockDtlManager.findCount(params);
				SimplePage page = new SimplePage(1, total, (int) total);
				List<BillOmOutstockDtl> list = billOmOutstockDtlManager.findByPage(page, "", "", params);
				List<Map> listArrayList = new ArrayList<Map>();
				if (list != null && list.size() > 0) {
					for (BillOmOutstockDtl vo : list) {
						String showAssignName = vo.getOutstockName();
						String showAssignNameCh = vo.getOutstockNameCh();
						if (StringUtils.isEmpty(vo.getOutstockName())) {
							showAssignName = vo.getAssignName();
							showAssignNameCh = vo.getAssignNameCh();
						}
						vo.setShowAssignName(showAssignName);
						vo.setShowAssignNameCh(showAssignNameCh);
						Map map = new HashMap();
						BeanUtilsCommon.object2MapWithoutNull(vo, map);
						listArrayList.add(map);

					}
					HSSFExport.commonExportData(StringUtils.isNotEmpty(fileName) ? fileName : "导出信息", ColumnsList,
							listArrayList, response);
				}
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 移库回单修改
	 * 
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/editDetail")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> editDetail(HttpServletRequest req,BillOmOutstock billOmOutstock){
		Map<String, Object> result = new HashMap<String, Object>();
		List<BillOmOutstockDtl> oList = new ArrayList<BillOmOutstockDtl>();
		try {
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				oList = convertListWithTypeReference(mapper, list);
			}

			// 获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmOutstock.setLocno(user.getLocNo());
			billOmOutstockDtlManager.editDetail(oList,billOmOutstock, user);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getCause().getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "确认失败！");
		}
		return result;
	}

	/**
	 * 审核
	 * 
	 * @param locno
	 * @param outstockNo
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/omPlanOutStockAudit")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> omPlanOutStockAudit(String outstockNo, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmOutstockDtlManager.omPlanOutStockAudit(outstockNo, user);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private List<BillOmOutstockDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmOutstockDtl> tl = new ArrayList<BillOmOutstockDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmOutstockDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmOutstockDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/printDetail")
	@ResponseBody
	public Map<String, Object> printDetail(HttpServletRequest req, HttpSession session, String keys) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("result", "success");
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			if (StringUtils.isEmpty(keys)) {
				result.put("result", "error");
				result.put("msg", "参数错误");
			}
			String[] keystr = keys.split(",");
			for (String key : keystr) {
				BillOmOutstockDtl dtl = new BillOmOutstockDtl();
				dtl.setLocno(user.getLocNo());
				dtl.setOutstockNo(key);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				// 查询客户
				List<BillOmOutstockDtl> storeNoList = this.billOmOutstockDtlManager.findStoreNo(dtl);
				resultMap.put("storeNoList", storeNoList);
				int itemCount = 0;
				if (storeNoList != null && storeNoList.size() == 1 && storeNoList.get(0) != null) { // 是同一个客户
					List<BillOmOutstockDtl> detailList = this.billOmOutstockDtlManager.findStockDtl(dtl);
					for (BillOmOutstockDtl d : detailList) {
						if (d.getItemQty() != null) {
							itemCount = itemCount + d.getItemQty().intValue();
						}
					}
					resultMap.put("rows", detailList);
					resultMap.put("haveStore", true);
				} else {
					List<BillOmOutstockDtl> detailList = this.billOmOutstockDtlManager.findStockDtlNoStoreNo(dtl);
					for (BillOmOutstockDtl d : detailList) {
						if (d.getItemQty() != null) {
							itemCount = itemCount + d.getItemQty().intValue();
						}
					}
					resultMap.put("rows", detailList);
					resultMap.put("haveStore", false);
				}
				resultMap.put("itemCount", itemCount);
				resultMap.put("printTime", format.format(new Date()));
				resultMap.put("outstockNo", key);
				resultList.add(resultMap);
			}
			result.put("data", resultList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		}
		return result;
	}

	@RequestMapping(value = "/printDetail4Area")
	@ResponseBody
	public Map<String, Object> printDetail4Area(HttpServletRequest req, HttpSession session, String keys) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		try {
			List<Map<String, Object>> list = this.billOmOutstockDtlManager.getPrintInf4AreaCut(user.getLocNo(), keys,
					user.getLoginName());
			result.put("data", list);

		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		}
		return result;
	}

	@RequestMapping(value = "/printByArea")
	@ResponseBody
	public Map<String, Object> printByArea(HttpServletRequest req, HttpSession session, String keys) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String result = "";
			String locno = req.getParameter("locno");
			Object u = session.getAttribute(PublicContains.SESSION_USER);
			SystemUser user = null;
			if (u != null) {
				user = (SystemUser) u;
			}
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(keys)) {
				result = "缺少参数";
				obj.put("result", result);
			} else {
				obj = billOmOutstockDtlManager.printByArea(locno, keys, user);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		}
		return obj;
	}

	@RequestMapping(value = "/printByStore")
	@ResponseBody
	public Map<String, Object> printByStore(HttpServletRequest req, HttpSession session, String keys) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String result = "";
			String locno = req.getParameter("locno");
			Object u = session.getAttribute(PublicContains.SESSION_USER);
			SystemUser user = null;
			if (u != null) {
				user = (SystemUser) u;
			}
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(keys)) {
				result = "缺少参数";
				obj.put("result", result);
			} else {
				obj = billOmOutstockDtlManager.printByStore(locno, keys, user);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		}
		return obj;
	}

	@RequestMapping(value = "/getListByPage.json")
	@ResponseBody
	public Map<String, Object> getListByPage(HttpServletRequest req, Model model,
			BillOmOutstockDirect billOmOutstockDirect) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map param = this.builderParams(req, model);
			int total = this.billOmOutstockDtlManager.findCount(param, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDtl> list = billOmOutstockDtlManager.findByPage(page, "", "", param, authorityParams, DataAccessRuleEnum.BRAND);
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("ownerName", "小计");
			footerList.add(footerMap);
			for (BillOmOutstockDtl temp : list) {
				this.setFooterMap("itemQty", temp.getItemQty(), footerMap);
				this.setFooterMap("realQty", temp.getRealQty(), footerMap);
				this.setFooterMap("instockQty", temp.getInstockQty(), footerMap);
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmOutstockDtlManager.selectSumQty(param, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
					sumFoot.put("instock_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("owner_name", "合计");
			} else {
				sumFoot.put("ownerName", "合计");
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
	
	
	/**
	 * 按计划保存
	 * @param req
	 * @param outstock
	 * @return
	 */
	@RequestMapping(value = "/saveByPlan")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> saveByPlan(HttpServletRequest req, BillOmOutstock outstock) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			outstock.setLocno(user.getLocNo());
			BillOmOutstock outstockResult = billOmOutstockManager.findById(outstock);
			if (STATUS13.equals(outstockResult.getStatus())) {
				obj.put("result", "error");
				obj.put("msg", "上架完成的单据不能修改");
				return obj;
			}
			billOmOutstockDtlManager.saveByPlan(outstock,user);
			obj.put("result", "success");
		} catch (ManagerException e) {
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			obj.put("result", "error");
			obj.put("msg", "系统异常");
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 验证是否存在有实际数量的值
	 * @param req
	 * @param outstock
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectCheckDtlRealQty")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectCheckDtlRealQty(HttpServletRequest req,
			BillOmOutstock outstock) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		int total = 0;
		try {
			total = this.billOmOutstockDtlManager.selectCheckDtlRealQty(outstock);
			obj.put("result", total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
//	/**
//     * 转库存锁定
//     * @param req
//     * @param BillConStorelock
//     * @throws JsonParseException
//     * @throws JsonMappingException
//     * @throws IOException
//     * @throws ManagerException
//     */
//	@RequestMapping(value = "/toStoreLock")
//	@ResponseBody
//	@OperationVerify(OperationVerifyEnum.VERIFY)
//	public ResponseEntity<Map<String, Object>> toStoreLock(HttpServletRequest req,BillOmOutstockDtl outstockDtl) throws JsonParseException, JsonMappingException, IOException,
//			ManagerException {
//		Map<String, Object> flag = new HashMap<String, Object>();
//		try {
//			HttpSession session = req.getSession();
//			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
//			billOmOutstockDtlManager.toStoreLock(outstockDtl.getLocno(),outstockDtl.getOutstockNo(),user.getLoginName());
//			flag.put("result", "success");
//			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
//		} catch (Exception e) {
//			flag.put("result", "fail");
//			flag.put("msg", e.getMessage());
//   			log.error("转库存锁定异常："+e.getMessage(),e);
//			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
//		}
//	}

//	/**
//     * 转库存锁定
//     * @param req
//     * @param BillConStorelock
//     * @throws JsonParseException
//     * @throws JsonMappingException
//     * @throws IOException
//     * @throws ManagerException
//     */
//	@RequestMapping(value = "/checkIsShowToStoreLock")
//	@ResponseBody
//	@OperationVerify(OperationVerifyEnum.VERIFY)
//	public ResponseEntity<Map<String, Object>> checkIsShowToStoreLock(HttpServletRequest req,BillOmOutstock outstock) throws JsonParseException, JsonMappingException, IOException,
//			ManagerException {
//		Map<String, Object> flag = new HashMap<String, Object>();
//		try {
//			String r = "0";
//			BillOmOutstock billOmOutstock = billOmOutstockManager.findById(outstock);
//			if(billOmOutstock != null){
//				BillWmPlan wmPlan = new BillWmPlan();
//				wmPlan.setLocno(outstock.getLocno());
//				wmPlan.setOwnerNo(OWNERNOBL);
//				wmPlan.setPlanNo(billOmOutstock.getSourceNo());
//				BillWmPlan billWmPlan = billWmPlanManager.findById(wmPlan);
//				if(billWmPlan != null){
//					if (BillWmPlanStatusEnums.STATUS20.getStatus().equals(billWmPlan.getStatus())
//							&& "90".equals(billOmOutstock.getStatus())) {
//						r = "1";
//					}
//				}
//			}
//			flag.put("result", r);
//			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
//		} catch (Exception e) {
//			flag.put("result", "0");
//   			log.error("查询验证是否转库存锁定异常："+e.getMessage(),e);
//			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
//		}
//	}
	
	@RequestMapping(value = "/getDetailBoxQty")
	@ResponseBody
	public Map<String, Object> getDetailBoxQty(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			int boxQty = billOmOutstockDtlManager.selectBoxQty(params, authorityParams);
			obj.put("boxQty", boxQty);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(value = "/initHead")
	@ResponseBody
	public Map<String, Object> initHead(HttpServletRequest req, HttpSession session, Model model)
			throws ManagerException {
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		Map<String, Object> map = new HashMap<String, Object>();

		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");

		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		Map param = this.builderParams(req, model);
		List<String> sizeKindList = billOmOutstockDtlManager.selectItemSizeKind(param, authorityParams);
		if (sizeKindList == null || sizeKindList.size() == 0) {
			return map;
		}
		List<String> sysList = billOmOutstockDtlManager.selectSysNo(param);
		if(sysList == null || sysList.size() != 1) {
			return map;
		}
		String sysNo = sysList.get(0);
		Map header = getBrandList(preColNames, endColNames, sizeTypeFiledName, sysNo, sizeKindList);
		map.put("header", header);
		return map;
	}
	
	@RequestMapping(value = "/selectItemDetail4SizeHorizontal")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectItemDetail4SizeHorizontal(HttpServletRequest req, Model model,
			HttpSession session) throws ManagerException {
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		int total = 0;
		// 返回参数列表
		List<BillOmOutstockDtl> billCheckImRepList = new ArrayList<BillOmOutstockDtl>();
		//返回汇总列表
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
//		Map<String, Object> footer1Map = new HashMap<String, Object>();
		footerMap.put("sCellNo", "小计");
		footerList.add(footerMap);
		Map param = this.builderParams(req, model);
		List<String> sysList = billOmOutstockDtlManager.selectSysNo(param);
		String sysNo = sysList.get(0);
		// 返回 Map集合
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			if(sysList == null || sysList.size() != 1) {
			} else {
				//** 统计总数 **/
				total = billOmOutstockDtlManager.selectItemDetailByGroupCount(param, authorityParams);
				/** 分组计算拿到订单编号和商品编号 **/
				SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
				List<BillOmOutstockDtl> tempList = billOmOutstockDtlManager.selectItemDetailByGroup(param, authorityParams, page);
				for (int z = 0; z < tempList.size(); z++) {
					BigDecimal allCounts = new BigDecimal(0);
					
					Map<String, Object> param2 = new HashMap<String, Object>();
					param2.put("locno", tempList.get(z).getLocno());
					param2.put("outstockNo", tempList.get(z).getOutstockNo());
					param2.put("itemNo", tempList.get(z).getItemNo());
					param2.put("scanLabelNo", tempList.get(z).getScanLabelNo());
					param2.put("sCellNo", tempList.get(z).getsCellNo());
					param2.put("storeNo", tempList.get(z).getStoreNo());

					List<BillOmOutstockDtl> dtlList = billOmOutstockDtlManager.selectDetailBySizeNo(param2);
					BillOmOutstockDtl dtl = tempList.get(z);

					SizeInfo sizeInfoParamInfo = new SizeInfo();
					Map<String, Object> mapParaMap = new HashMap<String, Object>();
					mapParaMap.put("sysNo", sysNo);
					mapParaMap.put("sizeKind", dtl.getSizeKind());
					List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
//					Map<String, BigDecimal> maps = new HashMap<String, BigDecimal>();
					for (int k = 0; k < dtlList.size(); k++) {
						for (int i = 0; i < sizeInfoList.size(); i++) {
							BillOmOutstockDtl temp = dtlList.get(k);
							SizeInfo tempSizeInfo = sizeInfoList.get(i);
							// 匹配尺码
							if (temp.getSizeNo().equals(tempSizeInfo.getSizeNo())) { // 相对
								BigDecimal a = new BigDecimal(0);
								if (null != temp.getItemQty()) {
									a = temp.getItemQty();
								}
								Object[] arg = new Object[] { a.toString() };
								String filedName = "setV" + (i + 1);
								CommonUtil.invokeMethod(dtl, filedName, arg);
								allCounts = allCounts.add(a);
								FooterUtil.setFooterMap("v" + (i + 1), temp.getItemQty(), footerMap);
								FooterUtil.setFooterMap("allCount", temp.getItemQty(), footerMap);
								break;
							}
						}
					}
					dtl.setAllCount(allCounts);
					billCheckImRepList.add(dtl);
				}
			}
			obj.put("total", total);
			obj.put("rows", billCheckImRepList);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			new ManagerException(e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	// 获取尺码横排头部信息
		private Map getBrandList(String preColNames, String endColNames, String sizeTypeFiledName, String sysNo,
				List<String> list) throws ManagerException {
			HashMap returnMap = new HashMap();

			LinkedList returnList = new LinkedList();
			ObjectMapper mapper = new ObjectMapper();
			List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
			List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();

			try {
				if (StringUtils.isNotEmpty(preColNames)) {
					preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
					});
				}

				if (StringUtils.isNotEmpty(endColNames)) {
					endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
					});
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new ManagerException(e);
			}

			// 尺码类型 A-ArrayList<SizeInfo>
			LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
			/*
			 * Map<String, Object> tempParams = new HashMap<String, Object>();
			 * tempParams.put("sysNo", sysNo); tempParams.put("preColNames",
			 * preColNames); tempParams.put("endColNames", endColNames);
			 * tempParams.put("sizeTypeFiledName", sizeTypeFiledName);
			 */

			// 查询尺码对应的尺码类别
			List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(list, sysNo, null);
			/*
			 * List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null,
			 * tempParams);
			 */
			if (sizeTypeList != null && sizeTypeList.size() > 0) {
				for (SizeInfo vo : sizeTypeList) {
					String sizeTypeName = vo.getSizeKind();
					if (sizeTypeMap.containsKey(sizeTypeName)) {
						ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
						listA.add(vo.getSizeName()); // ===========
						sizeTypeMap.put(sizeTypeName, listA);
					} else {
						ArrayList listA = new ArrayList();
						listA.add(vo.getSizeName());
						sizeTypeMap.put(sizeTypeName, listA);
					}
				}
			}

			int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
			if (sizeTypeMap != null) {
				java.util.Iterator it = sizeTypeMap.entrySet().iterator();
				while (it.hasNext()) {
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					List tempList = (List) entry.getValue();
					if (maxSortCount < tempList.size()) {
						maxSortCount = tempList.size();
					}
				}
			}

			// ====开始处理====
			Editor defaultEditor = new Editor();
			defaultEditor.setType("validatebox");
			if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
				int typeSizeV = sizeTypeMap.size(); // 公共的类别数
				if (typeSizeV > 1) { // 多于一个尺码类别
					int rowspan = typeSizeV - 2; // 合并的行
					int preColNamesV = preColNamesList.size(); // 前
					int endColNamesV = endColNamesList.size(); // 后
					if (rowspan >= 0 && rowspan != -1) {
						// ①处理合并表头
						if (rowspan > 0) { // 大于2个的时候
							for (int i = 0; i < rowspan; i++) {
								LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
								// 1.前面合并
								if (i == 0) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setTitle("");
									v.setWidth(80);
									v.setEditor(defaultEditor);
									v.setRowspan(rowspan);
									v.setColspan(preColNamesV);
									v.setAlign("left");
									colList.add(v);
								}
								// 2.显示尺码
								int k = 0;
								for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
									if (k == i) {
										int diffCols = maxSortCount - (en.getValue().size());
										JqueryDataGrid v = new JqueryDataGrid();
										v.setTitle(en.getKey());
										v.setWidth(50);
										v.setEditor(defaultEditor);
										v.setAlign("left");
										colList.add(v);

										for (int p = 0; p < en.getValue().size(); p++) {
											JqueryDataGrid v1 = new JqueryDataGrid();
											v1.setTitle(en.getValue().get(p).toString());
											v1.setWidth(50);
											v1.setEditor(defaultEditor);
											v1.setAlign("left");
											colList.add(v1);
										}
										if (diffCols > 0) {
											for (int m = 1; m <= diffCols; m++) {
												JqueryDataGrid v1 = new JqueryDataGrid();
												v1.setTitle("");
												v1.setWidth(50);
												v1.setEditor(defaultEditor);
												v1.setAlign("left");
												colList.add(v1);
											}
										}

										break;
									}
									k = k + 1;
								}
								// 3.合并后头
								if (i == 0) {
									if (endColNamesV > 0) {
										JqueryDataGrid v = new JqueryDataGrid();
										v.setTitle("");
										v.setWidth(80);
										v.setEditor(defaultEditor);
										v.setRowspan(rowspan);
										v.setColspan(endColNamesV);
										v.setAlign("left");
										colList.add(v);
									}
								}
								returnList.add(colList);
							}
						}

						// ②处理业务表头
						for (int ii = 1; ii >= 0; ii--) {
							LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
							// 1.业务头
							if (ii == 1) {
								if (preColNamesList.size() > 0) {
									for (JqueryDataGrid col : preColNamesList) {
										JqueryDataGrid v = new JqueryDataGrid();
										v.setField(col.getField());
										v.setTitle(col.getTitle());
										v.setWidth(SysConstans.WIDTH_80);
										if (col.getWidth() != 0) {
											v.setWidth(col.getWidth());
										}

										v.setEditor(defaultEditor);
										if (col.getEditor() != null) {
											if (!CommonUtil.hasValue(col.getEditor().getType())) {
												col.getEditor().setType(defaultEditor.getType());
											}
											v.setEditor(col.getEditor());
										}

										v.setRowspan(2);
										v.setAlign("left");
										colList.add(v);
									}
								}
							}
							// 2.尺码
							int k2 = 0;
							java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
							while (it2.hasNext()) {
								java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
										.next();

								if ((sizeTypeMap.size() - 1 - ii) == k2) {

									int diffCols2 = maxSortCount - (entry.getValue().size());
									// <#-- 这里做判断的原因是为了防止重复写 field: ,上面是List循环的 -->
									JqueryDataGrid v = new JqueryDataGrid();
									v.setTitle(entry.getKey());
									v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
									v.setEditor(defaultEditor);
									v.setAlign("left");
									if (ii == 1) {
										v.setField(sizeTypeFiledName);
										v.setAlign("center");
									}
									colList.add(v);

									for (int p = 0; p < entry.getValue().size(); p++) {
										JqueryDataGrid v1 = new JqueryDataGrid();
										v1.setTitle(entry.getValue().get(p).toString());
										v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
										v1.setEditor(defaultEditor);
										v1.setAlign("left");
										if (ii == 1) {
											v1.setField("v" + (p + 1));
											v1.setAlign("center");
										}
										colList.add(v1);
									}

									if (diffCols2 > 0) {
										for (int m = 1; m <= diffCols2; m++) {
											JqueryDataGrid v1 = new JqueryDataGrid();
											v1.setTitle("");
											v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
											v1.setEditor(defaultEditor);
											v1.setAlign("left");
											if (ii == 1) {
												v1.setField("v" + (entry.getValue().size() + m));
												v1.setAlign("center");
											}
											colList.add(v1);
										}
									}

									break;
								}
								k2 = k2 + 1;
							}
							// 3.
							if (ii == 1) {
								if (endColNamesList.size() > 0) {
									for (JqueryDataGrid col : endColNamesList) {
										JqueryDataGrid v = new JqueryDataGrid();
										v.setField(col.getField());
										v.setTitle(col.getTitle());
										v.setWidth(SysConstans.WIDTH_80);
										if (col.getWidth() != 0) {
											v.setWidth(col.getWidth());
										}
										v.setEditor(defaultEditor);
										if (col.getEditor() != null) {
											if (!CommonUtil.hasValue(col.getEditor().getType())) {
												col.getEditor().setType(defaultEditor.getType());
											}
											v.setEditor(col.getEditor());
										}
										v.setRowspan(2);
										v.setAlign("left");
										colList.add(v);
									}
								}
							}

							returnList.add(colList);

						}
					}
				} else if (typeSizeV == 1) {// 只有一个尺码类别
					LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
					// 1.业务头 标题栏
					if (preColNamesList.size() > 0) {
						for (JqueryDataGrid col : preColNamesList) {
							JqueryDataGrid v = new JqueryDataGrid();
							v.setField(col.getField());
							v.setTitle(col.getTitle());
							v.setWidth(SysConstans.WIDTH_80);
							if (col.getWidth() != 0) {
								v.setWidth(col.getWidth());
							}

							v.setEditor(defaultEditor);
							if (col.getEditor() != null) {
								if (!CommonUtil.hasValue(col.getEditor().getType())) {
									col.getEditor().setType(defaultEditor.getType());
								}
								v.setEditor(col.getEditor());
							}

							v.setRowspan(1);
							v.setAlign("left");
							colList.add(v);
						}
					}
					// 尺码横排栏
					java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
					while (it2.hasNext()) {
						java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2.next();
						// 尺码类别
						JqueryDataGrid v = new JqueryDataGrid();
						v.setTitle(entry.getKey());
						v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
						v.setEditor(defaultEditor);
						v.setAlign("left");
						v.setField(sizeTypeFiledName);
						v.setAlign("center");
						colList.add(v);

						for (int p = 0; p < entry.getValue().size(); p++) {
							JqueryDataGrid v1 = new JqueryDataGrid();
							v1.setTitle(entry.getValue().get(p).toString());
							v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
							v1.setEditor(defaultEditor);
							v1.setAlign("left");
							v1.setField("v" + (p + 1));
							v1.setAlign("center");
							colList.add(v1);
						}
					}
					// 合计信息
					if (endColNamesList.size() > 0) {
						for (JqueryDataGrid col : endColNamesList) {
							JqueryDataGrid vend = new JqueryDataGrid();
							vend.setField(col.getField());
							vend.setTitle(col.getTitle());
							vend.setWidth(SysConstans.WIDTH_80);
							if (col.getWidth() != 0) {
								vend.setWidth(col.getWidth());
							}
							vend.setEditor(defaultEditor);
							if (col.getEditor() != null) {
								if (!CommonUtil.hasValue(col.getEditor().getType())) {
									col.getEditor().setType(defaultEditor.getType());
								}
								vend.setEditor(col.getEditor());
							}
							vend.setRowspan(1);
							vend.setAlign("left");
							colList.add(vend);
						}
					}
					returnList.add(colList);
				}
			}

			// ====开始处理====

			returnMap.put("returnCols", returnList);
			returnMap.put("maxType", maxSortCount);
			returnMap.put("startType", preColNamesList.size() + 1);
			return returnMap;
		}
		
		@RequestMapping(value = "/do_export4Detail")
		public void doExport(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info)
				throws ManagerException {
			try {
				AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
				Map params = this.builderParams(req, model);
				List<String> sizeKindList = billOmOutstockDtlManager.selectItemSizeKind(params, authorityParams);
				if (sizeKindList == null || sizeKindList.size() == 0) {
					return;
				}
				List<String> sysList = billOmOutstockDtlManager.selectSysNo(params);
				if(sysList == null || sysList.size() != 1) {
					return;
				}
				String sysNo = sysList.get(0);
				
				HashMap returnMap = new HashMap();

				LinkedList returnList = new LinkedList();

				String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
						.getParameter("preColNames");
				String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
						.getParameter("endColNames");

				String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
						.getParameter("sizeTypeFiledName");

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

				LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
				List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(sizeKindList, sysNo, null);

				if (sizeTypeList != null && sizeTypeList.size() > 0) {
					for (SizeInfo vo : sizeTypeList) {
						String sizeTypeName = vo.getSizeKind();
						if (sizeTypeMap.containsKey(sizeTypeName)) {
							ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
							listA.add(vo.getSizeName()); //===========
							sizeTypeMap.put(sizeTypeName, listA);
						} else {
							ArrayList listA = new ArrayList();
							listA.add(vo.getSizeName());
							sizeTypeMap.put(sizeTypeName, listA);
						}
					}
				}

				int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
				if (sizeTypeMap != null) {
					java.util.Iterator it = sizeTypeMap.entrySet().iterator();
					while (it.hasNext()) {
						java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
						List tempList = (List) entry.getValue();
						if (maxSortCount < tempList.size()) {
							maxSortCount = tempList.size();
						}
					}
				}

				String fileName = (String) params.get("fileName");

				String dataRow = StringUtils.isEmpty(req.getParameter("dataRow")) ? "" : req.getParameter("dataRow");
				List<Map> dataRowList = new ArrayList<Map>();
				if (StringUtils.isNotEmpty(dataRow)) {
					dataRowList = mapper.readValue(dataRow, new TypeReference<List<Map>>() {
					});
				}

				response.setContentType("application/vnd.ms-excel");

				String fileName2 = new String(fileName.getBytes("gb2312"), "iso-8859-1");
				//文件名
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName2 + ".xls");
				response.setHeader("Pragma", "no-cache");
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet1 = wb.createSheet();
				//HSSFSheet  sheet2=wb.createSheet();
				//wb.setSheetName(1,"魏海金",HSSFWorkbook.ENCODING_UTF_16);
				//sheet名字
				wb.setSheetName(0, fileName);
				sheet1.setDefaultRowHeightInPoints(20);
				sheet1.setDefaultColumnWidth((short) 18);
				//设置页脚
				HSSFFooter footer = sheet1.getFooter();
				footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
				//设置样式 表头
				HSSFCellStyle style1 = wb.createCellStyle();
				style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont font1 = wb.createFont();
				font1.setFontHeightInPoints((short) 13);
				font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style1.setFont(font1);
				//设置样式 表头
				HSSFCellStyle style2 = wb.createCellStyle();
				style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				style2.setWrapText(true);

				//设置样式 表头
				HSSFCellStyle style3 = wb.createCellStyle();
				style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				HSSFFont font3 = wb.createFont();
				font3.setFontHeightInPoints((short) 18);
				font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style3.setFont(font3);
				style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
				HSSFRow row0 = sheet1.createRow(0);
				row0.setHeightInPoints(35);

				//第一行 提示长
				HSSFCell cell0 = row0.createCell((short) 0);
				cell0.setCellValue(fileName.toString());
				cell0.setCellStyle(style3);

				//真正开始============================================================================
				if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
					int typeSizeV = sizeTypeMap.size(); // 公共的类别数
					if (typeSizeV > 1) {
						int rowspan = typeSizeV - 2; // 合并的行========================
						int preColNamesV = preColNamesList.size(); // 前
						int endColNamesV = endColNamesList.size(); //后

						//合并
						Region rg1 = new Region(0, (short) 0, 0, (short) (maxSortCount + preColNamesV + endColNamesV));
						sheet1.addMergedRegion(rg1);

						if (rowspan >= 0 && rowspan != -1) {
							//①处理合并表头
							if (rowspan > 0) { // 大于2个的时候
								for (int i = 0; i < rowspan; i++) {
									int row = i + 1;
									HSSFRow row1 = sheet1.createRow(row);
									row1.setHeightInPoints(20);
									//1.前面合并
									if (i == 0) {
										HSSFCell cell1_0 = row1.createCell((short) 0);
										cell1_0.setCellValue("");
										Region rg11_cell1_0 = new Region(row, (short) 0, rowspan,
												(short) (preColNamesV - 1));
										sheet1.addMergedRegion(rg11_cell1_0);

									}

									//2.显示尺码
									int k = 0;
									for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
										if (k == i) {
											int diffCols = maxSortCount - (en.getValue().size());
											sheet1.autoSizeColumn((short) (preColNamesV));
											HSSFCell cell1_A = row1.createCell((short) preColNamesV);
											cell1_A.setCellValue(en.getKey() + "   ");

											for (int p = 0; p < en.getValue().size(); p++) {
												sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
												HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
												cell1_AG.setCellValue(en.getValue().get(p).toString());
											}

											if (diffCols > 0) {
												for (int m = 1; m <= diffCols; m++) {
													HSSFCell cell1_AGG = row1.createCell((short) (preColNamesV
															+ en.getValue().size() + m));
													cell1_AGG.setCellValue("      ");
												}
											}

											break;
										}
										k = k + 1;
									}

									//3.合并后头
									if (i == 0) {
										if (endColNamesV > 0) {
											HSSFCell cell1_E = row1.createCell((short) (preColNamesV + maxSortCount + 1));
											cell1_E.setCellValue("");
											Region rg11_cell1_0 = new Region(row,
													(short) (preColNamesV + maxSortCount + 1), rowspan,
													(short) (preColNamesV + maxSortCount + endColNamesV));
											sheet1.addMergedRegion(rg11_cell1_0);
										}
									}
								}
							}

							int mm = 1;
							//②处理业务表头
							for (int ii = 1; ii >= 0; ii--) {
								HSSFRow row1 = sheet1.createRow(rowspan + mm);
								row1.setHeightInPoints(20);
								//1.业务头
								if (ii == 1) {
									if (preColNamesList.size() > 0) {
										int nn = 0;
										for (JqueryDataGrid col : preColNamesList) {
											HSSFCell cell1_0 = row1.createCell((short) nn);
											cell1_0.setCellValue(col.getTitle());
											Region rg11_cell1_0 = new Region(rowspan + mm, (short) (nn), rowspan + mm + 1,
													(short) (nn));
											sheet1.addMergedRegion(rg11_cell1_0);
											sheet1.autoSizeColumn((short) (nn));
											nn = nn + 1;
										}
									}
								}

								//2.尺码	
								int k2 = 0;
								java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
								while (it2.hasNext()) {
									java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
											.next();

									if ((sizeTypeMap.size() - 1 - ii) == k2) {
										int diffCols2 = maxSortCount - (entry.getValue().size());

										HSSFCell cell1_A = row1.createCell((short) preColNamesV);
										sheet1.autoSizeColumn((short) (preColNamesV));
										cell1_A.setCellValue(entry.getKey() + "   ");

										for (int p = 0; p < entry.getValue().size(); p++) {
											sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
											HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
											cell1_AG.setCellValue(entry.getValue().get(p).toString());
										}
										if (diffCols2 > 0) {
											for (int m = 1; m <= diffCols2; m++) {
												HSSFCell cell1_AGG = row1.createCell((short) (preColNamesV
														+ entry.getValue().size() + m));
												cell1_AGG.setCellValue("      ");
											}
										}
										break;
									}
									k2 = k2 + 1;
								}

								//3.
								if (ii == 1) {
									if (endColNamesList.size() > 0) {
										int nn = 0;
										for (JqueryDataGrid col : endColNamesList) {
											HSSFCell cell1_0 = row1
													.createCell((short) (preColNamesV + maxSortCount + 1 + nn));
											cell1_0.setCellValue(col.getTitle());
											Region rg11_cell1_0 = new Region(rowspan + mm, (short) (preColNamesV
													+ maxSortCount + 1 + nn), rowspan + mm + 1, (short) (preColNamesV
													+ maxSortCount + 1 + nn));
											sheet1.addMergedRegion(rg11_cell1_0);
											sheet1.autoSizeColumn((short) (preColNamesV + maxSortCount + 1 + nn));
											nn = nn + 1;
										}
									}
								}

								mm = mm + 1;
							}
						}
					} else if (typeSizeV == 1) {
						int preColNamesV = preColNamesList.size(); // 前
						int endColNamesV = endColNamesList.size(); //后
						// 1.业务头 标题栏
						int start = 0;
						HSSFRow row1 = sheet1.createRow(0);
						if (preColNamesList.size() > 0) {

							for (JqueryDataGrid col : preColNamesList) {
								HSSFCell cell1_0 = row1.createCell((short) start);
								cell1_0.setCellValue(col.getTitle());
								if (sizeTypeMap.size() > 1) {
									Region rg11_cell1_0 = new Region(0, (short) start, 1, (short) start);
									sheet1.addMergedRegion(rg11_cell1_0);
								}
								start++;
							}
						}
						// 尺码横排栏
						//尺码横排最大的单元格个数
						int sizeNoNum = 0;
						java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
						while (it2.hasNext()) {
							java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
									.next();
							int diffCols2 = maxSortCount - (entry.getValue().size());

							HSSFCell cell1_A = row1.createCell((short) preColNamesV);
							sheet1.autoSizeColumn((short) (preColNamesV));
							cell1_A.setCellValue(entry.getKey() + "   ");

							for (int p = 0; p < entry.getValue().size(); p++) {
								sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
								HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
								cell1_AG.setCellValue(entry.getValue().get(p).toString());
							}
							if (diffCols2 > 0) {
								for (int m = 1; m <= diffCols2; m++) {
									HSSFCell cell1_AGG = row1
											.createCell((short) (preColNamesV + entry.getValue().size() + m));
									cell1_AGG.setCellValue("      ");
								}
							}

							if (entry.getValue().size() >= sizeNoNum) {
								sizeNoNum = entry.getValue().size();
							}
						}
						// 合计信息
						start = start + sizeNoNum + 1;
						if (endColNamesList.size() > 0) {
							row1.setHeightInPoints(20);
							for (JqueryDataGrid col : endColNamesList) {
								HSSFCell cell1_0 = row1.createCell((short) start);
								cell1_0.setCellValue(col.getTitle());
								if (sizeTypeMap.size() > 1) {
									Region rg11_cell1_0 = new Region(0, (short) start, 1, (short) start);
									sheet1.addMergedRegion(rg11_cell1_0);
								}
								start = start + 1;
							}
						}
					}
					if (dataRowList != null && dataRowList.size() > 0) {
						Map<String, Integer> sumMap = new HashMap<String, Integer>();
						int sumAllCount = 0;
						for (int v = 0; v < dataRowList.size(); v++) {
							Map map = dataRowList.get(v);
							sumAllCount = Integer.parseInt(map.get("allCount").toString()) + sumAllCount;
							int startRow = 0;
							if (sizeTypeMap.size() == 1) {
								startRow = typeSizeV;
							} else {
								startRow = typeSizeV + 1;
							}
							HSSFRow rowD = sheet1.createRow(startRow + v);
							rowD.setHeightInPoints(20);

							if (preColNamesList.size() > 0) {
								for (int m = 0; m < preColNamesList.size(); m++) {
									HSSFCell cell1_0 = rowD.createCell((short) m);
									JqueryDataGrid col = preColNamesList.get(m);
									String colV = map.get(col.getField()) != null ? String.valueOf(map.get(col.getField()))
											: "";
									cell1_0.setCellValue(colV);
								}
							}

							HSSFCell cell1_00 = rowD.createCell((short) preColNamesList.size());
							String colV = map.get(sizeTypeFiledName) != null ? String.valueOf(map.get(sizeTypeFiledName))
									: "";
							cell1_00.setCellValue(colV);

							for (int vv = 0; vv < maxSortCount; vv++) {
								HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + vv));
								String vX = "v" + (vv + 1);
								String colVVV = map.get(vX) != null ? String.valueOf(map.get(vX)) : "";
								cell1_000.setCellValue(colVVV);
								String sumVx = "S" + vX;
								if (sumMap.get(sumVx) == null) {
									if ("".equals(colVVV)) {
										sumMap.put(sumVx, 0);
									} else {
										sumMap.put(sumVx, Integer.parseInt(colVVV));
									}
								} else {
									if (!"".equals(colVVV)) {
										sumMap.put(sumVx, sumMap.get(sumVx) + Integer.parseInt(colVVV));
									}
								}
							}

							if (endColNamesList.size() > 0) {
								for (int m = 0; m < endColNamesList.size(); m++) {
									HSSFCell cell1_0000 = rowD.createCell((short) (preColNamesList.size() + 1
											+ maxSortCount + m));
									JqueryDataGrid coll = endColNamesList.get(m);
									String colVVVV = map.get(coll.getField()) != null ? String.valueOf(map.get(coll
											.getField())) : "0";
									cell1_0000.setCellValue(colVVVV);
								}
							}

						}
						//增加合计
						HSSFRow rowD = sheet1.createRow(sheet1.getLastRowNum() + 1);
						rowD.setHeightInPoints(20);
						HSSFCell cell1_0 = rowD.createCell(0);
						cell1_0.setCellValue("小计");
						for (int vv = 0; vv < maxSortCount; vv++) {
							HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + vv));
							String vX = "Sv" + (vv + 1);
							Integer colVVV = sumMap.get(vX) != null ? sumMap.get(vX) : 0;
							String stringValue = colVVV == 0 ? "" : String.valueOf(colVVV);
							cell1_000.setCellValue(stringValue);
						}
						HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + maxSortCount));
						cell1_000.setCellValue(String.valueOf(sumAllCount));
					}
				}
				wb.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		}
	
}