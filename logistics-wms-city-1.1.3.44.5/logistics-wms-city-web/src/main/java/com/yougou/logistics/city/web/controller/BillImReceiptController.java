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
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtil;
import com.yougou.logistics.city.manager.BillImReceiptManager;
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
@RequestMapping("/bill_im_receipt")
@ModuleVerify("25070070")
public class BillImReceiptController extends BaseCrudController<BillImReceipt> {
	@Log
	private Logger log;
	@Resource
	private BillImReceiptManager billImReceiptManager;
	
	@Override
	public CrudInfo init() {
		return new CrudInfo("billimreceipt/", billImReceiptManager);
	}

	private static final String RESULTOK = "success";
	private static final String RESULTFAIL = "fail";

	@RequestMapping(value = "/findReceiptByPage.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, BillImReceipt billImReceipt){
		Map<String, Object> obj=null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			int total = billImReceiptManager.selectCountMx(billImReceipt, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceipt> list = billImReceiptManager.findReceiptByPage(page, billImReceipt, authorityParams);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/findMainRecipt")
	@ResponseBody
	public Map<String, Object> findMainRecipt(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map<?, ?> map = builderParams(req, model);
			int total = billImReceiptManager.findMainReciptCount(map,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceipt> list = billImReceiptManager.findMainRecipt(page, map,authorityParams);
			//********************小计、合计S********************
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("receiptNo", "小计");
			footerList.add(footerMap);
			for(BillImReceipt br:list){
				SumUtil.setFooterMap("receiptqty", br.getReceiptqty(), footerMap);
				SumUtil.setFooterMap("boxqty", br.getBoxqty(), footerMap);
			}
			if(pageNo == 1){
				Map<String, Object> sumFoot = new HashMap<String, Object>();
				if(pageSize >= total){
					sumFoot.putAll(footerMap);
				}else{
					List<BillImReceipt> sumList = billImReceiptManager.findMainReciptSum(page, map,authorityParams);
					for(BillImReceipt br:sumList){
						SumUtil.setFooterMap("receiptqty", br.getReceiptqty(), sumFoot);
						SumUtil.setFooterMap("boxqty", br.getBoxqty(), sumFoot);
					}
				}
				sumFoot.put("receiptNo", "合计");
				footerList.add(sumFoot);
			}
			obj.put("footer", footerList);
			//********************小计、合计S********************
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
		Date date = new Date();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImReceipt.setCreator(user.getLoginName());
			billImReceipt.setCreatorName(user.getUsername());
			billImReceipt.setCreatetm(date);
			billImReceipt.setEditor(user.getLoginName());
			billImReceipt.setEditorName(user.getUsername());
			billImReceipt.setEdittm(date);
			billImReceipt.setRecivedate(date);
			billImReceipt.setLocno(user.getLocNo());
			billImReceiptManager.saveMain(billImReceipt);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = formatter.format(billImReceipt.getCreatetm());
			
			map.put("createData", formattedDate);
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
		Date date = new Date();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImReceipt.setEditor(user.getLoginName());
			billImReceipt.setEditorName(user.getUsername());
			billImReceipt.setEdittm(date);
			billImReceipt.setLocno(user.getLocNo());
			
			billImReceiptManager.saveMain(billImReceipt);
			map.put("result", "success");
			map.put("data", billImReceipt);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", "fail");
			map.put("msg", e.getMessage());
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/auditMain")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditMain(String keyStr, String locNo, String ownerNo, HttpServletRequest req) {
		Map obj = new HashMap();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if (billImReceiptManager.auditBatch(keyStr, locNo, ownerNo, user) > 0) {
				obj.put("result", "success");
			} else {
				obj.put("result", "fail");
				obj.put("msg", "审核失败,请联系管理员!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}

	@RequestMapping(value = "/addSub")
	@ResponseBody
	public String add(BillImReceipt billImReceipt, HttpServletRequest req) throws ManagerException, JsonParseException,
			JsonMappingException, IOException {
		//billUmUntreadManager.add(billUmUntread);
		try {
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			insertedList = URLDecoder.decode(insertedList, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillImImportDtlDto> itemLst = new ArrayList<BillImImportDtlDto>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				itemLst = convertListWithTypeReference(mapper, list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImReceipt.setCreatetm(new Date());
			billImReceipt.setCreator(user.getLoginName());
			billImReceiptManager.save(billImReceipt, itemLst);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return RESULTFAIL;
		}
		return RESULTOK;
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
			List<BillImImportDtlDto> insertList = new ArrayList<BillImImportDtlDto>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference(mapper, list);
			}
			//修改的行
			String updatedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			updatedList = URLDecoder.decode(updatedList, "UTF-8");
			List<BillImImportDtlDto> updatedItemLst = new ArrayList<BillImImportDtlDto>();
			if (StringUtils.isNotEmpty(updatedList)) {
				List<Map> list = mapper.readValue(updatedList, new TypeReference<List<Map>>() {
				});
				updatedItemLst = convertListWithTypeReference(mapper, list);
			}
			//删除的行
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			deletedList = URLDecoder.decode(deletedList, "UTF-8");
			List<BillImImportDtlDto> deletedItemLst = new ArrayList<BillImImportDtlDto>();
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
			billImReceipt.setEditorName(user.getUsername());
			resultMap = billImReceiptManager.update(billImReceipt, insertList, updatedItemLst, deletedItemLst,user);
			resultMap.put("result", "success");
		}catch (ManagerException e) {
			log.error("=======================收货单保存明细操作异常："+e.getMessage(), e);
			resultMap.put("result", "fail");
			resultMap.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error("=======================收货单保存明细异常："+e.getMessage(), e);
			resultMap.put("result", "fail");
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}

	@RequestMapping(value = "/deleteReceipt")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteReceipt(String keyStr, String locNo, String ownerNo, HttpServletRequest req) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if (billImReceiptManager.deleteBatch(keyStr, locNo, ownerNo, user) > 0) {
				obj.put("result", "success");
			} else {
				obj.put("result", "fail");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
			obj.put("msg",e.getMessage());
		}
		return obj;
	}

	private List<BillImImportDtlDto> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillImImportDtlDto> tl = new ArrayList<BillImImportDtlDto>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillImImportDtlDto type = mapper
					.readValue(mapper.writeValueAsString(list.get(i)), BillImImportDtlDto.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/selectImport4Direct")
	@ResponseBody
	public Map<String, Object> selectImport4Direct(HttpServletRequest req, Model model) throws ManagerException {
		Map obj=null;
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map params = builderParams(req, model);
	
			int total = billImReceiptManager.selectCount4Direct(params);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillImImport> list = billImReceiptManager.selectByPage4Direct(page, params);
			obj = new HashMap();
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	
	@RequestMapping(value = "/findBatchSelectBox")
	@ResponseBody
	public Map<String, Object> findBatchSelectBox(String[] importNos, String locNo, String ownerNo, HttpServletRequest req) {
		Map obj = new HashMap();		
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List<String> importNoList =new ArrayList<String>();	
			for(String importNo:importNos){	
				importNoList.add(importNo);				
			}
			
			Map map = new HashMap();
			map.put("locNo", locNo);
			map.put("ownerNo", ownerNo);
			List<BillImImportDtlDto> list =billImReceiptManager.findBatchSelectBox(importNoList, map,authorityParams);
			obj.put("list", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
		}
		return obj;
	}
	@RequestMapping(value = "/print")
	@ResponseBody
	public Map<String, Object> print(HttpServletRequest req,Model model) throws ManagerException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<?, ?> map = builderParams(req, model);
			List<BillImReceipt> list = billImReceiptManager.findImReceiptPrint(map,authorityParams);
			StringBuffer receiptNo = new StringBuffer("单号");
			for(BillImReceipt billImReceipt : list){
				BigDecimal receiptqty = billImReceipt.getReceiptqty();
				if( receiptqty == null || receiptqty.intValue() == 0){
					receiptNo.append("[").append(billImReceipt.getReceiptNo()).append("]");
				}
			}
			if(!receiptNo.toString().equals("单号")){
				receiptNo.append("不存在明细,不允许打印!");
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", receiptNo.toString());
			}else{
				result.put("rows", list);
				result.put("result", ResultEnums.SUCCESS.getResultMsg());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		}
		return result;
	}
}