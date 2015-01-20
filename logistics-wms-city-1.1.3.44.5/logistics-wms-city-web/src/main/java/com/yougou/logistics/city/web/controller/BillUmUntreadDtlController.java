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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.BillUmUntreadStatusEnums;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmUntreadDtlManager;
import com.yougou.logistics.city.manager.BillUmUntreadManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
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
@RequestMapping("/bill_um_untread_dtl")
public class BillUmUntreadDtlController extends BaseCrudController<BillUmUntreadDtl> {
	@Log
	private Logger log;
	@Resource
	private BillUmUntreadDtlManager billUmUntreadDtlManager;
	
	@Resource
	private BillUmUntreadManager billUmUntreadManager;
	@Override
	public CrudInfo init() {
		return new CrudInfo("billUmUntreadDtl/", billUmUntreadDtlManager);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDetail")
	@ResponseBody
	public Map<String, Object> saveDetail(HttpServletRequest req, BillUmUntread untread, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			untread.setLocno(user.getLocNo());
			untread.setCreator(user.getLoginName());
			untread.setEditor(user.getLoginName());
			untread.setEditorName(user.getUsername());
			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			inserted = URLDecoder.decode(inserted, "UTF-8");
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			updated = URLDecoder.decode(updated, "UTF-8");
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			deleted = URLDecoder.decode(deleted, "UTF-8");

			List<ConBoxDtl> insertList = new ArrayList<ConBoxDtl>();
			List<ConBoxDtl> updateList = new ArrayList<ConBoxDtl>();
			List<ConBoxDtl> deleteList = new ArrayList<ConBoxDtl>();
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
			//增加判断主表中的数据状态是否是建单状态
			BillUmUntreadKey billUmUntreadKey = new BillUmUntreadKey();
			billUmUntreadKey.setLocno(untread.getLocno());
			billUmUntreadKey.setOwnerNo(untread.getOwnerNo());
			billUmUntreadKey.setUntreadNo(untread.getUntreadNo());
			int isExits =  billUmUntreadManager.judgeObjIsExist(billUmUntreadKey,BillUmUntreadStatusEnums.STATUS10.getStatus());
			if (0 == isExits){
				resultMap.put("msg", "单据:"+untread.getUntreadNo()+"已删除或状态已改变!");
				return resultMap;
			}
			billUmUntreadDtlManager.saveUntreadDtl(insertList, updateList, deleteList, untread);
			resultMap.put("result", ResultEnums.SUCCESS.getResultMsg());
			return resultMap;
		} catch (ManagerException e) {
			resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			resultMap.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
			return resultMap;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			resultMap.put("msg", "保存失败");
			return resultMap;
		}
	}

	@SuppressWarnings("rawtypes")
	private List<ConBoxDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<ConBoxDtl> tl = new ArrayList<ConBoxDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ConBoxDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), ConBoxDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/listByBox.json")
	@ResponseBody
	public Map<String, Object> queryListByBox(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billUmUntreadDtlManager.findCountByBox(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmUntreadDtl> list = this.billUmUntreadDtlManager.findByPageByBox(page, params);

			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	// 按箱查询明细
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/select4Box")
	@ResponseBody
	public Map<String, Object> select4Box(HttpServletRequest req, Model model, Store store) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map params = builderParams(req, model);
			int total = billUmUntreadDtlManager.select4BoxCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<ConBoxDtl> list = billUmUntreadDtlManager.select4Box(params, page, null);

			obj.put("total", total);
			obj.put("rows", list);
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
			int total = billUmUntreadDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmUntreadDtl> list = billUmUntreadDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);

			obj.put("total", total);
			obj.put("rows", list);
			//		Map<String, Object> obj = super.queryList(req, model);
			//		List<BillUmUntreadDtl> list = CommonUtil.getRowsByListJson(obj,BillUmUntreadDtl.class);
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalReceiptQty = new BigDecimal(0);
			BigDecimal totalCheckQty = new BigDecimal(0);
			for (BillUmUntreadDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalReceiptQty = totalReceiptQty.add(dtl.getReceiptQty());
				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("itemNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("receiptQty", totalReceiptQty);
			footer.put("checkQty", totalCheckQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billUmUntreadDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot != null) {
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