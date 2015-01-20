package com.yougou.logistics.city.web.controller;

import java.io.IOException;
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
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmCheckDtlManager;
import com.yougou.logistics.city.manager.BillUmCheckManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 退仓验收
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
@RequestMapping("/bill_um_check")
@ModuleVerify("25060020")
public class BillUmCheckController extends BaseCrudController<BillUmCheck> {

	@Log
	private Logger log;

	@Resource
	private BillUmCheckManager billUmCheckManager;

	@Resource
	private BillUmCheckDtlManager billUmCheckDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billumcheck/", billUmCheckManager);
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
			int total = this.billUmCheckManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmCheck> list = this.billUmCheckManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			
//			Map<String, Object> footer = new HashMap<String, Object>();
//			List<Object> footerList = new ArrayList<Object>();
//			BigDecimal totalItemQty = new BigDecimal(0);
//			BigDecimal totalRealQty = new BigDecimal(0);
//			for (BillUmCheck billUmCheck : list) {
//				Map<String, Object> mapObj = new HashMap<String, Object>();
//				mapObj.put("locno", billUmCheck.getLocno());
//				mapObj.put("untreadNo", billUmCheck.getUntreadNo());
//				mapObj.put("checkNo", billUmCheck.getCheckNo());
//				Map<String, Object> sumFoot = billUmCheckManager.selectUntreadJoinCheckDtlSumQty(mapObj, authorityParams);
//				BigDecimal itemQty = (BigDecimal) sumFoot.get("itemQty");
//				BigDecimal realQty = (BigDecimal)sumFoot.get("realQty");
//				totalItemQty = totalItemQty.add(itemQty);
//				totalRealQty = totalRealQty.add(realQty);
//				billUmCheck.setItemQty(itemQty);
//				billUmCheck.setRealQty(realQty);
//			}
//			footer.put("checkNo", "小计");
//			footer.put("itemQty", totalItemQty);
//			footer.put("realQty", totalRealQty);
//			footerList.add(footer);
//			
//			// 合计
//			Map<String, Object> sumFoot = new HashMap<String, Object>();
//			if (pageNo == 1) {
//				sumFoot = billUmCheckManager.selectUntreadJoinCheckSumQty(params, authorityParams);
//				if (sumFoot == null) {
//					sumFoot = new SumUtilMap<String, Object>();
//					sumFoot.put("item_qty", 0);
//					sumFoot.put("real_qty", 0);
//				}
//				sumFoot.put("isselectsum", true);
//				sumFoot.put("check_no", "合计");
//			} else {
//				sumFoot.put("checkNo", "合计");
//			}
//			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", list);
//			obj.put("footer", footerList);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
	@RequestMapping(value = "/findSumQty.json")
	@ResponseBody
	public Map<String, Object> findSumQty(HttpServletRequest req, Model model)throws ManagerException {
		Map<String, Object> sumFoot = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			sumFoot = billUmCheckManager.selectUntreadJoinCheckSumQty(params, authorityParams);
			if (sumFoot == null) {
				sumFoot = new SumUtilMap<String, Object>();
				sumFoot.put("item_qty", 0);
				sumFoot.put("real_qty", 0);
			}
		} catch (Exception e) {
			sumFoot = new SumUtilMap<String, Object>();
			sumFoot.put("item_qty", 0);
			sumFoot.put("real_qty", 0);
			log.error(e.getMessage(), e);
		}
		return sumFoot;
	}
	// 新增主表
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addMain")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> addMain(BillUmCheck check, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
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
				insertList = convertListWithTypeReference2(mapper, list);
			}
			if (StringUtils.isNotEmpty(updated)) {
				List<Map> list = mapper.readValue(updated, new TypeReference<List<Map>>() {
				});
				updateList = convertListWithTypeReference2(mapper, list);
			}
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference2(mapper, list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			check.setLocno(user.getLocNo());
			check.setCreatetm(new Date());
			check.setCreator(user.getLoginName());
			check.setCreatorName(user.getUsername());
			check.setEdittm(new Date());
			check.setEditor(user.getLoginName());
			check.setEditorName(user.getUsername());
			
			billUmCheckManager.saveMain(check, insertList, updateList, deleteList);

			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("checkNo", check.getCheckNo());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "保存失败,请联系管理员!");
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private List<BillUmCheckDtl> convertListWithTypeReference2(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmCheckDtl> tl = new ArrayList<BillUmCheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmCheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillUmCheckDtl.class);
			tl.add(type);
		}
		return tl;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Store> convertListWithTypeReferenceStore(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<Store> tl = new ArrayList<Store>(list.size());
		for (int i = 0; i < list.size(); i++) {
			Store type = mapper.readValue(mapper.writeValueAsString(list.get(i)), Store.class);
			tl.add(type);
		}
		return tl;
	}

	// 修改主表
	@RequestMapping(value = "/editMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> editMain(BillUmCheck check, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			check.setLocno(user.getLocNo());
			check.setEdittm(new Date());
			check.setEditor(user.getLoginName());
			check.setEditorName(user.getUsername());
			
			billUmCheckManager.saveMain(check, null, null, null);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "保存失败,请联系管理员!");
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteBillUmCheck")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public String deleteBillUmCheck(String keyStr, HttpServletRequest req) {
		try {
			List<BillUmCheck> listBillUmChecks = new ArrayList<BillUmCheck>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(keyStr)) {
				List<Map> list = mapper.readValue(keyStr, new TypeReference<List<Map>>() {
				});
				listBillUmChecks = convertListWithTypeReference(mapper, list);
			}

			if (billUmCheckManager.deleteBillUmCheck(listBillUmChecks) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	// 审核
	@RequestMapping(value = "/auditCheck")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditUntreadMm(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmCheckManager.auditCheck(keyStr, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("msg", e.getMessage());
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private List<BillUmCheck> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmCheck> tl = new ArrayList<BillUmCheck>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmCheck type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillUmCheck.class);
			tl.add(type);
		}
		return tl;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selectUmNoForInstock")
	@ResponseBody
	public Map<String, Object> selectUmNoForInstock(HttpServletRequest req, Model model) throws ManagerException {
		Map obj = new HashMap();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			//			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
			//					: String.valueOf(req.getParameter("sort"));
			//			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
			//					: String.valueOf(req.getParameter("order"));
			Map params = builderParams(req, model);

			int total = billUmCheckManager.selectCountUmNoForInstock(params);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillUmCheck> list = billUmCheckManager.selectByPageUmNoForInstock(page, params);
			if (null == list || list.isEmpty() || list.size() == 0) {
				list = new ArrayList<BillUmCheck>(0);
			}

			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selectCheckNoForInstock")
	@ResponseBody
	public Map<String, Object> selectCheckNoForInstock(HttpServletRequest req, Model model) throws ManagerException {
		Map obj = new HashMap();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			//		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
			//				: String.valueOf(req.getParameter("sort"));
			//		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
			//				: String.valueOf(req.getParameter("order"));
			Map params = builderParams(req, model);

			int total = billUmCheckManager.selectCountCheckNoForInstock(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillUmCheck> list = billUmCheckManager.selectByPageCheckNoForInstock(page, params, authorityParams);
			if (null == list || list.isEmpty() || list.size() == 0) {
				list = new ArrayList<BillUmCheck>(0);
			}
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/validateIsDiff")
	@ResponseBody
	public String validateIsDiff(String checkNoStr, String locno, String untreadMmNo, HttpServletRequest req)
			throws ManagerException {
		try {
			Map params = new HashMap();
			params.put("checkNoStr", checkNoStr);
			params.put("locno", locno);
			params.put("untreadMmNo", untreadMmNo);
			if (billUmCheckDtlManager.selectCountForDiff(params) > 0) {
				return "success";
			} else {
				return "none";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
	
	/**
	 * 校验退仓验收单的合法性-上架定位
	 * @param checkNoStr
	 * @param locno
	 * @param ownerNo
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/validateIsEnable")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> validateIsEnable(String checkNoStr, String locno, String ownerNo,HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = billUmCheckManager.validateIsEnable(checkNoStr,locno,ownerNo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("flag", "error");
			map.put("resultMsg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/selectDiffDirect")
	@ResponseBody
	public Map<String, Object> selectDiffDirect(HttpServletRequest req, Model model) throws ManagerException {
		Map obj = new HashMap();

		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			//		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
			//				: String.valueOf(req.getParameter("sort"));
			//		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
			//				: String.valueOf(req.getParameter("order"));
			Map params = builderParams(req, model);

			int total = billUmCheckDtlManager.selectCountForDiff(params);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillUmCheckDtl> list = billUmCheckDtlManager.selectByPageForDiff(page, params);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 退仓验收单批量转货操作
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/transferCargo")
    @ResponseBody    
    @OperationVerify(OperationVerifyEnum.VERIFY)
    public Map<String, Object> transferCargo(HttpServletRequest req,HttpSession session,Model model) throws Exception {
		SystemUser currentUser = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    	Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = req.getParameterMap();
    	if(!(null!=params && params.size()>0)){
    		map.put("result", "fail");
    		map.put("status", 1);
    		log.error("接收的参数为空");
    	}else{
    		try {
    			Map<String, Object> paramMap= builderParams(req, model);
    			map=billUmCheckManager.transferCargo(paramMap,currentUser);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				map.put("result", "error");
				map.put("msg", e.getMessage());
			}
    	}
    	return map;
    }
	
	/**
	 * 退仓验收属性转换操作
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/propertyChange")
    @ResponseBody    
    @OperationVerify(OperationVerifyEnum.VERIFY)
    public Map<String, Object> propertyChange(HttpServletRequest req,HttpSession session,Model model) throws Exception {
		SystemUser currentUser = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    	Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = req.getParameterMap();
    	if(!(null!=params && params.size()>0)){
    		map.put("result", "fail");
    		map.put("status", 1);
    		log.error("接收的参数为空");
    	}else{
    		try {
    			Map<String, Object> paramMap= builderParams(req, model);
    			map=billUmCheckManager.prpertyChange(paramMap, currentUser);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				map.put("result", "error");
				map.put("msg", e.getMessage());
			}
    	}
    	return map;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findBillUmCheckByPage")
	@ResponseBody
	public Map<String, Object> findBillUmCheckByPage(HttpServletRequest req, Model model) throws ManagerException {
		Map obj = new HashMap();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map params = builderParams(req, model);
			int total = billUmCheckManager.findBillUmCheckCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillUmCheck> list = billUmCheckManager.findBillUmCheckByPage(page, sortColumn, sortOrder, params, authorityParams);
			if (null == list || list.isEmpty() || list.size() == 0) {
				list = new ArrayList<BillUmCheck>(0);
			}
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	// 门店转货
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/toStoreConvert")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> toStoreConvert(BillUmCheck check, HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//check.setCreator(user.getLoginName());
			//check.setLocno(user.getLocNo());
			
			//验收单
			String checkDatas = StringUtils.isEmpty(req.getParameter("checkDatas")) ? "" : req.getParameter("checkDatas");
			List<BillUmCheck> checkList = new ArrayList<BillUmCheck>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(checkDatas)) {
				List<Map> list = mapper.readValue(checkDatas, new TypeReference<List<Map>>() {
				});
				checkList = convertListWithTypeReference(mapper, list);
			}

			//选择的客户
			String storeDatas = StringUtils.isEmpty(req.getParameter("storeDatas")) ? "" : req.getParameter("storeDatas");
			List<Store> storeList = new ArrayList<Store>();
			if (StringUtils.isNotEmpty(storeDatas)) {
				List<Map> list = mapper.readValue(storeDatas, new TypeReference<List<Map>>() {
				});
				storeList = convertListWithTypeReferenceStore(mapper, list);
			}
			
			billUmCheckManager.toStoreConvertRecheck(checkList, storeList, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
		
}