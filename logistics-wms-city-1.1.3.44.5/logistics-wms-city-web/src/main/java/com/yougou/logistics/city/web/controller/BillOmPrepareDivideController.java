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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmPrepareDivideManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@RequestMapping("/bill_om_preparedivide")
@ModuleVerify("25040090")
public class BillOmPrepareDivideController extends BaseCrudController<BillImReceipt> {
	@Log
	private Logger log;
	@Resource
	private BillOmPrepareDivideManager billOmPrepareDivideManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billompreparedivide/", billOmPrepareDivideManager);
	}


	@RequestMapping(value = "/findMainRecipt")
	@ResponseBody
	public Map<String, Object> findMainRecipt(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String,Object> map = builderParams(req, model);
			Map<String,Object> reciptMap=new HashMap<String,Object>();
			reciptMap.putAll(map);
			reciptMap.put("businessType", "1");
			
			int total = billOmPrepareDivideManager.findMainReciptCount(reciptMap,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceipt> list = billOmPrepareDivideManager.findMainRecipt(page, reciptMap,authorityParams);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/addMain")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> addMain(BillImReceipt billImReceipt, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			Date date = new Date();
			billImReceipt.setCreatetm(date);
			billImReceipt.setRecivedate(date);
			billImReceipt.setCreator(user.getLoginName());
			billImReceipt.setCreatorName(user.getUsername());
			billImReceipt.setEditor(user.getLoginName());
			billImReceipt.setEditorName(user.getUsername());
			billImReceipt.setLocno(user.getLocNo());
			billOmPrepareDivideManager.saveMain(billImReceipt);
			map.put("result", "success");
			map.put("data", billImReceipt);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", "fail");
		}
		return map;
	}

	@RequestMapping(value = "/editMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> editMain(BillImReceipt billImReceipt, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImReceipt.setEditor(user.getLoginName());
			billImReceipt.setEdittm(new Date());
			billImReceipt.setEditorName(user.getUsername());
			billImReceipt.setLocno(user.getLocNo());
			billOmPrepareDivideManager.saveMain(billImReceipt);
			map.put("result", "success");
			map.put("data", billImReceipt);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", "fail");
			map.put("msg", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/auditMain")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditMain(String keyStr, String locNo, String ownerNo, HttpServletRequest req) {
		Map obj = new HashMap();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if (billOmPrepareDivideManager.auditBatch(keyStr, locNo, ownerNo, user) > 0) {
				obj.put("result", "success");
			} else {
				obj.put("result", "fail");
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("msg", e.getMessage());
			obj.put("result", "fail");
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("msg", "审核失败。");
			obj.put("result", "fail");
		}
		return obj;
	}


	@RequestMapping(value = "/edit")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> edit(BillImReceipt billImReceipt, HttpServletRequest req) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			insertedList = URLDecoder.decode(insertedList, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillImReceiptDtl> insertList = new ArrayList<BillImReceiptDtl>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference(mapper, list);
			}
			//删除的行
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			deletedList = URLDecoder.decode(deletedList, "UTF-8");
			List<BillImReceiptDtl> deletedItemLst = new ArrayList<BillImReceiptDtl>();
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
				});
				deletedItemLst = convertListWithTypeReference(mapper, list);
			}

			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImReceipt.setEdittm(new Date());
			billImReceipt.setLocno(user.getLocNo());
			billImReceipt.setEditor(user.getLoginName());
			resultMap = billOmPrepareDivideManager.update(billImReceipt, insertList, deletedItemLst);
			resultMap.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put("result", "fail");
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}

	@RequestMapping(value = "/deleteReceipt")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteReceipt(String keyStr, String locNo, String ownerNo, HttpServletRequest req) {
		Map obj = new HashMap();
		try {
			if (billOmPrepareDivideManager.deleteBatch(keyStr, locNo, ownerNo) > 0) {
				obj.put("result", "success");
			} else {
				obj.put("result", "fail");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}

	private List<BillImReceiptDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillImReceiptDtl> tl = new ArrayList<BillImReceiptDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillImReceiptDtl type = mapper
					.readValue(mapper.writeValueAsString(list.get(i)), BillImReceiptDtl.class);
			tl.add(type);
		}
		return tl;
	}

	
	@RequestMapping(value = "/findBoxNo4Divide")
	@ResponseBody
	public Map<String, Object> findBoxNo4Divide(HttpServletRequest req, Model model) throws ManagerException {
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
			Map<String, Object> p = builderParams(req, model);
			Map<String,Object> params = new HashMap<String,Object>(p);
			//仓区多选时
			List<String> wareNoList = new ArrayList<String>();
			String wareNotemp = (String)params.get("wareNo");
			if(StringUtils.isNotBlank(wareNotemp) && !"null".equals(wareNotemp))
			{
				String  [] areaNos = wareNotemp.split(",");
				for (String string : areaNos) {
					wareNoList.add(string);
				}
				params.put("wareNo", wareNoList);
			}
			//库区多选时
			List<String> areaList = new ArrayList<String>();
			String areatemp = (String)params.get("areaNo");
			if( StringUtils.isNotBlank(areatemp)&&!"null".equals(areatemp))
			{
				String[] areaNos = areatemp.split(",");
				params.remove("wareNo");
				for (String areaNo : areaNos) {
					areaList.add(areaNo);
					
				}
				params.put("areaNo", areaList);
			}
			
			//发货通知单号
			String expNos = (String)params.get("expNos");
			if( StringUtils.isNotBlank(expNos) && !"null".equals(expNos))
			{
				String[] receiptNos=expNos.split(",");
				expNos = "";
				for(int i = 0; i < receiptNos.length; i++) {
					expNos += "'" + receiptNos[i] + "',";
				}
				expNos = expNos.substring(0, expNos.length()-1);
				params.put("expNos", expNos);
			}
			
			this.paramsHandle(params);

			int total = billOmPrepareDivideManager.findBoxNo4DivideCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List list = billOmPrepareDivideManager.findBoxNo4Divide(page, params,authorityParams);

			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	/**
	 * 参数处理
	 * @param params
	 */
	private void paramsHandle(Map<String, Object> params) {

		//大类三
		String cateThree = (String) params.get("cateThree");
		String[] cateThreeValues = null;
		if (StringUtils.isNotEmpty(cateThree)) {
			cateThreeValues = cateThree.split(",");
			params.put("cateCode", cateThreeValues);
		}else{
			//大类二
			String cateTwo = (String) params.get("cateTwo");
			String[] cateTwoValues = null;
			if (StringUtils.isNotEmpty(cateTwo)) {
				cateTwoValues = cateTwo.split(",");
				params.put("cateCode",cateTwoValues);	
			}else{
				//大类一
				String cateOne = (String) params.get("cateOne");
				String[] cateOneValues = null;
				if (StringUtils.isNotEmpty(cateOne)) {
					cateOneValues = cateOne.split(",");
				}
				params.put("cateCode", cateOneValues);	
			}
		}
		
	}

	
	//手工关闭预分货单
	@RequestMapping(value = "/overPrepareDivide")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> overPrepareDivide(BillImReceipt billImReceipt, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillImReceipt> receiptList = new ArrayList<BillImReceipt>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				receiptList = convertListWithTypeReference1(mapper, list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmPrepareDivideManager.overPrepareDivide(receiptList, user.getLoginName());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "关闭保存失败,请联系管理员!");
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private List<BillImReceipt> convertListWithTypeReference1(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillImReceipt> tl = new ArrayList<BillImReceipt>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillImReceipt type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillImReceipt.class);
			tl.add(type);
		}
		return tl;
	}
}