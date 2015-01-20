package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmCheckTaskDtl;
import com.yougou.logistics.city.common.model.BillUmCheckTaskKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillUmCheckTaskDtlManager;
import com.yougou.logistics.city.manager.BillUmCheckTaskManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
@RequestMapping("/bill_um_check_task_dtl")
public class BillUmCheckTaskDtlController extends BaseCrudController<BillUmCheckTaskDtl> {

	private static final String STATUS10 = "10";
	
	@Log
	private Logger log;
	
	@Resource
	private BillUmCheckTaskDtlManager billUmCheckTaskDtlManager;

	@Resource
	private BillUmCheckTaskManager billUmCheckTaskManager;
	@Override
	public CrudInfo init() {
		return new CrudInfo("billUmCheckTaskDtl/", billUmCheckTaskDtlManager);
	}
	
	/**
	 * 查询明细
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
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
			int total = billUmCheckTaskDtlManager.findCount(params, authorityParams,DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmCheckTaskDtl> list = billUmCheckTaskDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalCheckQty = new BigDecimal(0);
			int totalDiffQty = 0;
			for (BillUmCheckTaskDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalCheckQty = totalCheckQty.add(dtl.getCheckQty());
				totalDiffQty += Math.abs(dtl.getCheckQty().intValue()-dtl.getItemQty().intValue());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("untreadNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("checkQty", totalCheckQty);
			footer.put("difQty", totalDiffQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billUmCheckTaskDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					String diffQty = "0";
					if (sumFoot1.get("difqty") != null) {
						diffQty = sumFoot1.get("difqty").toString();
					}
					String itemQty = "0";
					if (sumFoot1.get("itemQty") != null) {
						itemQty = sumFoot1.get("itemQty").toString();
					}
					String checkQty = "0";
					if (sumFoot1.get("checkQty") != null) {
						checkQty = sumFoot1.get("checkQty").toString();
					}
					sumFoot.put("itemQty", itemQty);
					sumFoot.put("checkQty", checkQty);
					sumFoot.put("difQty", diffQty);
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("untreadNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 添加差异商品
	 * @param req
	 * @param untread
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findUntreadNo4CheckTaskDtl.json")
	@ResponseBody
	public Map<String, Object> findUntreadNo4CheckTaskDtl(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			/*ownerNo=BL, checkTaskNo=006UT14092600003, locno=006*/
			BillUmCheckTaskKey billUmCheckTaskKey = new BillUmCheckTaskKey();
			billUmCheckTaskKey.setCheckTaskNo(params.get("checkTaskNo").toString());
			billUmCheckTaskKey.setLocno(params.get("locno").toString());
			billUmCheckTaskKey.setOwnerNo(params.get("ownerNo").toString());
			
			BillUmCheckTask billUmCheckTask = (BillUmCheckTask) billUmCheckTaskManager.findById(billUmCheckTaskKey);
			if (null == billUmCheckTask){
				result.put("flag", "notExits");
				result.put("msg", "单据: " + params.get("checkTaskNo") +"不存在");
				return result;
			}
			if(!STATUS10.equals(billUmCheckTask.getStatus())){
				result.put("flag", "notExits");
				result.put("msg", "单据: " + params.get("checkTaskNo") +"状态发生了变化，不能进行当前操作");
				return result;
			}
			List<BillUmCheckTaskDtl> list = billUmCheckTaskDtlManager.findUntreadNo4CheckTaskDtl(params);
			result.put("rows", list);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("rows", "");
			return result;
		}
	}
	
	/**
	 * 查询商品
	 * @param req
	 * @param item
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findItem4CheckTask.json")
	@ResponseBody
	public Map<String, Object> findItem4CheckTask(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			Map<String, Object> params = builderParams(req, model);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int total = billUmCheckTaskDtlManager.findItemCount4CheckTask(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Item> list = billUmCheckTaskDtlManager.findItem4CheckTask(params, page, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	// 新增明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveUmCheckTaskDtl")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveUmCheckTaskDtl(BillUmCheckTask check, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			check.setCreatetm(new Date());
			check.setCreator(user.getLoginName());
			check.setCreatorName(user.getUsername());
			check.setLocno(user.getLocNo());
			check.setEditor(user.getLoginName());
			check.setEditorName(user.getUsername());
			check.setEdittm(new Date());

			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");

			List<BillUmCheckTaskDtl> insertList = new ArrayList<BillUmCheckTaskDtl>();
			List<BillUmCheckTaskDtl> updateList = new ArrayList<BillUmCheckTaskDtl>();
			List<BillUmCheckTaskDtl> deleteList = new ArrayList<BillUmCheckTaskDtl>();
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
			
			billUmCheckTaskDtlManager.saveUmCheckTaskDtl(insertList, updateList, deleteList, check);
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
	
	
	/**
     * 按计划保存
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/saveCheckQty4itemQty")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> saveCheckQty4itemQty(HttpServletRequest req,Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> params = builderParams(req, model);
			BillUmCheckTaskKey billUmCheckTaskKey = new BillUmCheckTaskKey();
			billUmCheckTaskKey.setOwnerNo(params.get("ownerNo").toString());
			billUmCheckTaskKey.setLocno(params.get("locno").toString());
			billUmCheckTaskKey.setCheckTaskNo(params.get("checkTaskNo").toString());
			BillUmCheckTask billUmCheckTask = (BillUmCheckTask) billUmCheckTaskManager.findById(billUmCheckTaskKey);
			if(billUmCheckTask==null){
				map.put("result","notExits" );
				map.put("msg", "单据 :"+params.get("checkTaskNo")+"不存在");
				return map;
			}
			if(!STATUS10.equals(billUmCheckTask.getStatus())){
				map.put("result","notExits" );
				map.put("msg", "单据 :"+params.get("checkTaskNo")+"状态发生变化,不能进行当前操作");
				return map;
			}
			billUmCheckTaskDtlManager.saveCheckQty4itemQty(params);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
    
    /**
     * 按单删除
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/delUntreadByCheckTask")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> delUntreadByCheckTask(HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BillUmCheckTaskDtl> taskList = new ArrayList<BillUmCheckTaskDtl>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				taskList = convertListWithTypeReference(mapper, list);
			}
			BillUmCheckTaskKey billUmCheckTaskKey = new BillUmCheckTaskKey();
			billUmCheckTaskKey.setCheckTaskNo(taskList.get(0).getCheckTaskNo());
			billUmCheckTaskKey.setLocno(taskList.get(0).getLocno());
			billUmCheckTaskKey.setOwnerNo(taskList.get(0).getOwnerNo());
			BillUmCheckTask billUmCheckTask = (BillUmCheckTask) billUmCheckTaskManager.findById(billUmCheckTaskKey);
			if(billUmCheckTask==null){
				map.put("flag","notExits" );
				map.put("msgs", "单据 :"+taskList.get(0).getCheckTaskNo()+"不存在");
				return map;
			}
			if(!STATUS10.equals(billUmCheckTask.getStatus())){
				map.put("flag","notExits" );
				map.put("msgs", "单据 :"+taskList.get(0).getCheckTaskNo()+"状态发生变化,不能进行当前操作");
				return map;
			}
			billUmCheckTaskDtlManager.delUntreadByCheckTask(taskList);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
    
    /**
     * 商品置0
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/updateCheckQtyToZero")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> updateCheckQtyToZero(HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BillUmCheckTaskDtl> taskList = new ArrayList<BillUmCheckTaskDtl>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				taskList = convertListWithTypeReference(mapper, list);
			}
			BillUmCheckTaskKey billUmCheckTaskKey = new BillUmCheckTaskKey();
			billUmCheckTaskKey.setOwnerNo(taskList.get(0).getOwnerNo());
			billUmCheckTaskKey.setLocno(taskList.get(0).getLocno());
			billUmCheckTaskKey.setCheckTaskNo(taskList.get(0).getCheckTaskNo());
			BillUmCheckTask billUmCheckTask = (BillUmCheckTask) billUmCheckTaskManager.findById(billUmCheckTaskKey);
			if(billUmCheckTask==null){
				map.put("result","notExits" );
				map.put("msg", "单据: "+taskList.get(0).getCheckTaskNo()+"不存在");
				return map;
			}
			if(!STATUS10.equals(billUmCheckTask.getStatus())){
				map.put("result","notExits" );
				map.put("msg", "单据: "+taskList.get(0).getCheckTaskNo()+"状态发生变化,不能进行当前操作");
				return map;
			}
			
			billUmCheckTaskDtlManager.updateCheckQtyToZero(taskList);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	private List<BillUmCheckTaskDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmCheckTaskDtl> tl = new ArrayList<BillUmCheckTaskDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmCheckTaskDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillUmCheckTaskDtl.class);
			tl.add(type);
		}
		return tl;
	}
	
}