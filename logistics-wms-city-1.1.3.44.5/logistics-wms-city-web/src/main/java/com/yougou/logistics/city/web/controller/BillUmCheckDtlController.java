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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmCheckDtlManager;
import com.yougou.logistics.city.manager.BillUmUntreadDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 11 14:40:26 CST 2013
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
@RequestMapping("/bill_um_check_dtl")
public class BillUmCheckDtlController<ModelType> extends BaseCrudController<BillUmCheckDtl> {

	@Log
	private Logger log;

	@Resource
	private BillUmCheckDtlManager billUmCheckDtlManager;

	@Resource
	private BillUmUntreadDtlManager billUmUntreadDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billUmCheckDtl/", billUmCheckDtlManager);
	}

	/**
	 * 查询单明细列表（带分页）
	 */
	@RequestMapping(value = "/listBillUmCheckDtl.json")
	@ResponseBody
	public Map<String, Object> listBillUmCheckDtl(HttpServletRequest req, Model model)
			throws ManagerException {
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
			int total = billUmCheckDtlManager.findBillUmCheckDtlCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmCheckDtl> list = billUmCheckDtlManager.findBillUmCheckDtlByPage(page, sortColumn, sortOrder, params,
					authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		} catch (Exception e) {
			log.error("===========查询退仓验收单明细列表（带分页）时异常：" + e.getMessage(), e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}

	/**
	 * 新增和删除预到货通知单明细
	 */
	@RequestMapping(value = "/saveBillUmCheck")
	@ResponseBody
	public Map<String, Object> saveBillUmCheck(BillUmCheck billUmCheck, HttpServletRequest req)
			throws JsonParseException, JsonMappingException, IOException, ManagerException {
		return null;
		//		Map<String, Object> result = new HashMap<String, Object>();
		/*try {

			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");

			ObjectMapper mapper = new ObjectMapper();
			Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
			if (StringUtils.isNotBlank(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
				});
				List<ModelType> oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.DELETED, oList);
			}
			if (StringUtils.isNotBlank(upadtedList)) {
				List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>() {
				});
				List<ModelType> oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.UPDATED, oList);
			}
			if (StringUtils.isNotBlank(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				List<ModelType> oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.INSERTED, oList);
			}
			result = billUmCheckDtlManager.addBillUmCheck(billUmCheck, params);
			return result;
		} catch (Exception e) {
			log.error("===========新增，修改，删除退仓验收单明细时异常：" + e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "保存失败，请联系管理员");
			return result;
		}*/
	}

	/*@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<ModelType> tl = new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}*/

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDetail")
	@ResponseBody
	public Map<String, Object> saveDetail(HttpServletRequest req, BillUmCheck check, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Date d = new Date();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			check.setLocno(user.getLocNo());
			check.setCreator(user.getLoginName());
			check.setCreatorName(user.getUsername());
			check.setCreatetm(d);
			check.setEditor(user.getLoginName());
			check.setEditorName(user.getUsername());
			check.setEdittm(d);
			check.setEdittm(new Date());
			check.setEditor(user.getLoginName());
			check.setEditorName(user.getUsername());
			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			inserted = URLDecoder.decode(inserted, "UTF-8");
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			updated = URLDecoder.decode(updated, "UTF-8");
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			deleted = URLDecoder.decode(deleted, "UTF-8");

			List<BillUmCheckDtl> insertList = new ArrayList<BillUmCheckDtl>();
			List<BillUmCheckDtl> updateList = new ArrayList<BillUmCheckDtl>();
			List<BillUmCheckDtl> deleteList = new ArrayList<BillUmCheckDtl>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(inserted)) {
				List<Map> list = mapper.readValue(inserted, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(updated)) {
				List<Map> list = mapper.readValue(updated, new TypeReference<List<Map>>() {
				});
				updateList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference(mapper, list);
			}
			resultMap = billUmCheckDtlManager.saveCheckDtl(insertList, updateList, deleteList, check);
			resultMap.put("result", ResultEnums.SUCCESS.getResultMsg());
			return resultMap;
		} catch (Exception e) {
			resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			resultMap.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
			return resultMap;
		}
	}

	@SuppressWarnings("rawtypes")
	private List<BillUmCheckDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmCheckDtl> tl = new ArrayList<BillUmCheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmCheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillUmCheckDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/selectAllBox.json")
	@ResponseBody
	public Map<String, Object> selectAllBox(HttpServletRequest req, BillUmUntread untread) throws ManagerException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List<BillUmUntreadDtl> list = billUmUntreadDtlManager.selectAllBox(untread, authorityParams);
			result.put("data", list);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("data", "");
			return result;
		}
	}

	@RequestMapping(value = "/selectItem.json")
	@ResponseBody
	public Map<String, Object> selectItem(HttpServletRequest req, Item item) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			//			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
			//					.getParameter("sort"));
			//			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
			//					.getParameter("order"));
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			if(item.getItemNo() != null) {
				item.setItemNo(item.getItemNo().toUpperCase());
			}
			if(item.getBarcode() != null) {
				item.setBarcode(item.getBarcode().toUpperCase());
			}
			int total = billUmCheckDtlManager.selectItemCount4Check(item, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Item> list = billUmCheckDtlManager.selectItem4Check(item, page, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
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
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billUmCheckDtlManager.findBillUmCheckDtlCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmCheckDtl> list = billUmCheckDtlManager.findBillUmCheckDtlByPage(page, sortColumn, sortOrder, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);

			//		Map<String, Object> obj = super.queryList(req, model);
			//		List<BillUmCheckDtl> list = CommonUtil.getRowsByListJson(obj, BillUmCheckDtl.class);

//			List<Object> footerList = new ArrayList<Object>();
//			//当页小计
//			BigDecimal totalItemQty = new BigDecimal(0);
//			BigDecimal totalCheckQty = new BigDecimal(0);
//			BigDecimal totalDiffQty = new BigDecimal(0);
//			BigDecimal totalRecheckQty = new BigDecimal(0);
//			for (BillUmCheckDtl dtl : list) {
//				totalItemQty = totalItemQty.add(dtl.getItemQty());
//				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
//				totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
//				totalDiffQty = totalDiffQty.add(dtl.getCheckQty().subtract(dtl.getItemQty()));
//			}
//			Map<String, Object> footer = new HashMap<String, Object>();
//			footer.put("itemNo", "小计");
//			footer.put("itemQty", totalItemQty);
//			footer.put("checkQty", totalCheckQty);
//			footer.put("recheckQty", totalRecheckQty);
//			footer.put("difQty", Math.abs(totalDiffQty.intValue()));
//			footerList.add(footer);
//			// 合计
//			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
//			Map<String, Object> sumFoot = new HashMap<String, Object>();
//			if (pageNo == 1) {
//				sumFoot1 = billUmCheckDtlManager.selectSumQty(params, authorityParams);
//				if (sumFoot1 != null) {
//					String diffQty = "0";
//					if (sumFoot1.get("difqty") != null) {
//						diffQty = sumFoot1.get("difqty").toString();
//					}
//					String itemQty = "0";
//					if (sumFoot1.get("itemQty") != null) {
//						itemQty = sumFoot1.get("itemQty").toString();
//					}
//					String checkQty = "0";
//					if (sumFoot1.get("checkQty") != null) {
//						checkQty = sumFoot1.get("checkQty").toString();
//					}
//					String recheckQty = "0";
//					if (sumFoot1.get("recheckQty") != null) {
//						recheckQty = sumFoot1.get("recheckQty").toString();
//					}
//					sumFoot.put("itemQty", itemQty);
//					sumFoot.put("checkQty", checkQty);
//					sumFoot.put("recheckQty", recheckQty);
//					sumFoot.put("difQty", diffQty);
//					sumFoot.put("isselectsum", true);
//				}
//			}
//			sumFoot.put("itemNo", "合计");
//			footerList.add(sumFoot);
//			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	@RequestMapping(value = "/findDtlSumQty.json")
	@ResponseBody
	public Map<String, Object> findDtlSumQty(HttpServletRequest req, Model model)throws ManagerException {
		Map<String, Object> sumFoot = new HashMap<String, Object>();
		String num = "0";
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			Map<String, Object> sumFoot1 = billUmCheckDtlManager.selectSumQty(params, authorityParams);
			if (sumFoot1 != null) {
				String diffQty = num;
				if (sumFoot1.get("difqty") != null) {
					diffQty = sumFoot1.get("difqty").toString();
				}
				String itemQty = num;
				if (sumFoot1.get("itemQty") != null) {
					itemQty = sumFoot1.get("itemQty").toString();
				}
				String checkQty = num;
				if (sumFoot1.get("checkQty") != null) {
					checkQty = sumFoot1.get("checkQty").toString();
				}
				String recheckQty = num;
				if (sumFoot1.get("recheckQty") != null) {
					recheckQty = sumFoot1.get("recheckQty").toString();
				}
				sumFoot.put("itemQty", itemQty);
				sumFoot.put("checkQty", checkQty);
				sumFoot.put("recheckQty", recheckQty);
				sumFoot.put("difQty", diffQty);
			}
		} catch (Exception e) {
			sumFoot.put("itemQty", num);
			sumFoot.put("checkQty", num);
			sumFoot.put("recheckQty", num);
			sumFoot.put("difQty", num);
			log.error(e.getMessage(), e);
		}
		return sumFoot;
	}
	
	@RequestMapping(value = "/listByPage.json")
	@ResponseBody
	public Map<String, Object> listByPage(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billUmCheckDtlManager.findCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmCheckDtl> list = billUmCheckDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams);
			
			obj.put("total", total);
			obj.put("rows", list);

			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalNoRecheckQty = new BigDecimal(0);
			for (BillUmCheckDtl dtl : list) {
				totalNoRecheckQty = totalNoRecheckQty.add(dtl.getNoRecheckQty());
			}
			
			//当页小计
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("itemNo", "小计");
			footer.put("noRecheckQty", totalNoRecheckQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billUmCheckDtlManager.selectPageSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					String noRecheckQty = "0";
					if (sumFoot1.get("noRecheckQty") != null) {
						noRecheckQty = sumFoot1.get("noRecheckQty").toString();
					}
					sumFoot.put("noRecheckQty", noRecheckQty);
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("itemNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}