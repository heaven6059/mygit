package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.util.CollectionUtils;
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
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto2;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillImInstockDtlManager;
import com.yougou.logistics.city.manager.BillImInstockManager;
import com.yougou.logistics.city.manager.CmDefcellManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 30 16:23:58 CST 2013
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
@RequestMapping("/bill_im_instock_dtl")
@ModuleVerify("25070060")
public class BillImInstockDtlController extends BaseCrudController<BillImInstockDtlDto> {
	@Log
	private Logger log;

	@Resource
	private BillImInstockDtlManager billImInstockDtlManager;

	@Resource
	private CmDefcellManager cmDefcellManager;

	@Resource
	private SizeInfoManager sizeInfoManager;

	@Resource
	private BillImInstockManager billImInstockManager;

	private static final String STAUTS0 = "0";

	private static final String STATUS10 = "10";

	private static final String STATUS13 = "13";

	@Override
	public CrudInfo init() {
		return new CrudInfo("billiminstockdtl/", billImInstockDtlManager);
	}

	@RequestMapping(value = "/findDetail")
	@ResponseBody
	public Map<String, Object> findDetail(BillImInstockDtl dtl, HttpServletRequest req) throws ManagerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<BillImInstockDtlDto2> returnDtoList = new ArrayList<BillImInstockDtlDto2>();
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			int total = billImInstockDtlManager.findItemCountBroupByItemNo(dtl);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillImInstockDtl> list = billImInstockDtlManager.findItemGroupByItemNo(page, dtl);

			for (BillImInstockDtl gvo : list) {
				gvo.setLocno(dtl.getLocno());
				if (CommonUtil.hasValue(gvo.getItemNo())) {
					List<BillImInstockDtlDto2> listTempMxList = billImInstockDtlManager.findDetailByParams(gvo);
					BigDecimal allCounts = new BigDecimal(0);
					if (CollectionUtils.isEmpty(listTempMxList)) {
						continue;
					}

					BillImInstockDtlDto2 dto = listTempMxList.get(0);
					Map<String, Object> mapParaMap = new HashMap<String, Object>();
					mapParaMap.put("sizeKind", dto.getSizeKind());
					mapParaMap.put("sysNo", dto.getSysNo());
					List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(null, mapParaMap);
					for (BillImInstockDtlDto2 temp : listTempMxList) {
						for (int i = 0; i < sizeInfoList.size(); i++) {
							SizeInfo sizeInfo = sizeInfoList.get(i);
							if (temp.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对
								Object[] arg = new Object[] { temp.getItemQty().toString() };
								String filedName = "setV" + (i + 1);
								CommonUtil.invokeMethod(dto, filedName, arg);
								allCounts = allCounts.add(temp.getItemQty());
								break;
							}
						}
					}
					dto.setAllCount(allCounts);
					dto.setAllCost(new BigDecimal(0));
					returnDtoList.add(dto);
				}
			}
			resultMap.put("total", total);
			resultMap.put("rows", returnDtoList);
			return resultMap;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}
	}

	@RequestMapping(value = "/findSysNo")
	@ResponseBody
	public Map<String, Object> findSysNo(HttpServletRequest req, BillImInstockDtlDto dto) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String sysNo = billImInstockDtlManager.findSysNo(dto);
			obj.put("sysNo", sysNo);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", "获取品牌库失败");
		}
		return obj;
	}

	@RequestMapping(value = "/getById")
	@ResponseBody
	public Map<String, Object> getById(HttpServletRequest req, BillImInstockDtlDto dto) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			BillImInstockDtl dtl = this.billImInstockDtlManager.findById(dto);
			obj.put("resultData", dtl);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
		}
		return obj;
	}

	@RequestMapping(value = "/updateById")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> updateById(HttpServletRequest req, BillImInstock instock, BillImInstockDtlDto dto) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			/*
			 * Map<String, Object> parmMap = new HashMap<String, Object>();
			 * parmMap.put("cellStatus", STAUTS0); parmMap.put("checkStatus",
			 * STAUTS0); parmMap.put("cellNo", dto.getRealCellNo()); int count =
			 * this.cmDefcellManager.findCount(parmMap); if (count <= 0) {
			 * obj.put("result", "error"); obj.put("msg", "实际储位不存在"); return
			 * obj; } this.billImInstockDtlManager.modifyById(dto);
			 */
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			instock.setLocno(user.getLocNo());
			BillImInstock instockResult = this.billImInstockManager.findById(instock);
			if (STATUS13.equals(instockResult.getStatus())) {
				obj.put("result", "error");
				obj.put("msg", "上架完成的单据不能修改");
				return obj;
			}
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			insertedList = URLDecoder.decode(insertedList, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillImInstockDtlDto> itemLst = new ArrayList<BillImInstockDtlDto>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				itemLst = convertListWithTypeReference(mapper, list);
			}
			billImInstockDtlManager.saveBatch(itemLst);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}

	private List<BillImInstockDtlDto> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillImInstockDtlDto> tl = new ArrayList<BillImInstockDtlDto>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillImInstockDtlDto type = mapper.readValue(mapper.writeValueAsString(list.get(i)),
					BillImInstockDtlDto.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/deleteById")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteById(HttpServletRequest req, BillImInstockDtlDto dto) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			
			// 查询主档的状态，只有新建的状态才能删除
//			BillImInstock param = new BillImInstock();
//			param.setInstockNo(dto.getInstockNo());
//			param.setLocno(dto.getLocno());
//			param.setOwnerNo(dto.getOwnerNo());
//			BillImInstock instock = this.billImInstockManager.findById(param);
//			if (STATUS13.equals(instock.getStatus())) {
//				obj.put("result", "error");
//				obj.put("msg", "上架完成的单据不能删除");
//				return obj;
//			}
//			BillImInstockDtl dtl = this.billImInstockDtlManager.findById(dto);
//			if (dtl.getItemQty().intValue() == 0) {
//				this.billImInstockDtlManager.deleteById(dto);
//			} else {
//				obj.put("result", "error");
//				obj.put("msg", "源数据不能删除");
//				return obj;
//			}
			
			billImInstockDtlManager.deleteSingle(dto);
			
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}

	/**
	 * 单行更新明细
	 * 
	 * @param req
	 * @param instock
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/saveSingle")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> saveSingle(HttpServletRequest req, BillImInstock instock, BillImInstockDtl dtl) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			dtl.setLocno(user.getLocNo());
			dtl.setEditor(user.getLoginName());
		    dtl.setEditorName(user.getUsername());
			dtl.setEdittm(new Date());
			billImInstockDtlManager.saveSingle(dtl);
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

	@RequestMapping(value = "/saveByPlan")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> saveByPlan(HttpServletRequest req, BillImInstock instock) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			instock.setLocno(user.getLocNo());
			instock.setEditor(user.getLoginName());
			instock.setEditorName(user.getUsername());//修改人中文名称
			instock.setEdittm(new Date());
			billImInstockDtlManager.saveByPlan(instock);
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

	@RequestMapping(value = "/splitById")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> splitById(HttpServletRequest req, BillImInstock instock, BillImInstockDtlDto dtl) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			dtl.setLocno(user.getLocNo());
			dtl.setEditor(user.getLoginName());
			dtl.setEditorName(user.getUsername());
			dtl.setEdittm(new Date());
			BillImInstockDtlDto dtlResult = this.billImInstockDtlManager.splitById(dtl);
			obj.put("dtl", dtlResult);
			obj.put("result", "success");
		} catch (ManagerException e) {
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			obj.put("result", "error");
			obj.put("msg", "拆封失败，请联系管理员");
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/dtlList.json")
	@ResponseBody
	public Map<String, Object> getList4Json(BillImCheckDtl billImCheckDtl, HttpServletRequest req, Model model)
			throws ManagerException {
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
			Map params = builderParams(req, model);
			int total = this.billImInstockDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillImInstockDtl> list = this.billImInstockDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/getByPage.json")
	@ResponseBody
	public Map<String, Object> getByPage(BillImCheckDtl billImCheckDtl, HttpServletRequest req, Model model)
			throws ManagerException {
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
			Map params = builderParams(req, model);
			int total = this.billImInstockDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillImInstockDtl> list = this.billImInstockDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillImInstockDtl temp : list) {
				this.setFooterMap("itemQty", temp.getItemQty(), footerMap);
				this.setFooterMap("realQty", temp.getRealQty(), footerMap);
				this.setFooterMap("instockedQty", temp.getInstockedQty(), footerMap);
			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImInstockDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_Qty", 0);
					sumFoot.put("real_Qty", 0);
					sumFoot.put("instocked_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", Integer.valueOf(total));
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
			//AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List<String> htmlList = billImInstockDtlManager.printDetail(locno, keys);
			if (htmlList != null && htmlList.size() != 0) {
				obj.put("result", "success");
				obj.put("data", htmlList);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
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
}