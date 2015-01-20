package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillSmWasteDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
@RequestMapping("/bill_sm_waste_dtl")
public class BillSmWasteDtlController<ModelType> extends BaseCrudController<BillSmWasteDtl> {

	@Log
	private Logger log;
	@Resource
	private SizeInfoManager sizeInfoManager;
	@Resource
	private BillSmWasteDtlManager billSmWasteDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billsmwastedtl/", billSmWasteDtlManager);
	}

	/**
	 * 明细查询
	 */
	@RequestMapping(value = "/dtlList.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) {
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
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			
			int total = this.billSmWasteDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmWasteDtl> list = this.billSmWasteDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/dtl_List.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model) {
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
			int total = this.billSmWasteDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmWasteDtl> list = this.billSmWasteDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);

			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalWasteQty = new BigDecimal(0);
			for (BillSmWasteDtl dtl : list) {
				totalWasteQty = totalWasteQty.add(dtl.getWasteQty());
			}
			footer.put("cellNo", "小计");
			footer.put("wasteQty", totalWasteQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billSmWasteDtlManager.selectSumQty(params,authorityParams);
				if (sumFoot1 != null) {
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					sumFoot2.put("wasteQty", sumFoot1.get("wasteQty"));
					sumFoot2.put("isselectsum", true);
					sumFoot1 = sumFoot2;
				}
			}
			sumFoot1.put("cellNo", "合计");
			footerList.add(sumFoot1);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 查询库存表
	 */
	@RequestMapping(value = "/get_Content")
	@ResponseBody
	public Map<String, Object> getBiz(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String itemNo = "";
			if(paramsAll.get("itemNo") != null) {
				itemNo = paramsAll.get("itemNo").toString().toUpperCase();
				params.put("itemNo", itemNo);
			}
			String barcode = "";
			if(paramsAll.get("barcode") != null) {
				barcode = paramsAll.get("barcode").toString().toUpperCase();
				params.put("barcode", barcode);
			}
			int total = this.billSmWasteDtlManager.selectContentCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmWasteDtl> list = this.billSmWasteDtlManager.selectContent(page, sortColumn, sortOrder, params,authorityParams);
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
	 * 新增和删除预到货通知单明细
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addSmWasteDtl")
	public ResponseEntity<Map<String, Object>> addDtl(HttpServletRequest req, BillSmWaste billSmWaste){
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			if (null != billSmWaste) {
				String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req
						.getParameter("deleted");
				String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req
						.getParameter("updated");
				String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req
						.getParameter("inserted");

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
				flag = billSmWasteDtlManager.addSmWasteDtl(billSmWaste, params, authorityParams);
			}
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);

		} catch (ManagerException e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}
	/**
	 * 按库存数量保存出库数量
	 * @param req
	 * @param billSmWaste
	 * @return
	 */
	@RequestMapping(value = "/saveShipment")
	public ResponseEntity<Map<String, Object>> saveShipment(HttpServletRequest req, BillSmWaste billSmWaste){
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			if (null != billSmWaste) {
				String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req
						.getParameter("updated");
				

				ObjectMapper mapper = new ObjectMapper();
				Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
				
				if (StringUtils.isNotBlank(upadtedList)) {
					List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.UPDATED, oList);
				}
				
				flag = billSmWasteDtlManager.addShipmentDtl(billSmWaste, params, authorityParams);
			}
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);

		} catch (ManagerException e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
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
	}

	
	@RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 FileUtils.downloadTemple(session, response, "wasteTemplate.xls");
	}
	
	@RequestMapping("/iframe")
	public ModelAndView iframe(String wasteNo,String ownerNo) throws Exception {
		ModelAndView mode = new ModelAndView("billsmwaste/iframe");
		mode.addObject("wasteNo", wasteNo);
		mode.addObject("ownerNo", ownerNo);
		return mode;
	}
	@RequestMapping(value = "/importExcel")
	public ModelAndView upLoad(HttpServletRequest request,Model model,String wasteNo,String ownerNo) {
		ModelAndView mode = new ModelAndView("billsmwaste/iframe");
		mode.addObject("wasteNo", wasteNo);
		mode.addObject("ownerNo", ownerNo);
		SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
	    try{
	    	String [] colNames = {"itemNo","sizeNo","itemType","quality","cellNo","wasteQty"};
	    	boolean [] mustArray = {true,true,true,true,true,true};
			List<BillSmWasteDtl> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, null, BillSmWasteDtl.class);
			 if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 
			 //this.billSmOtherinDtlManager.excelImportData(list, user.getLocNo(), otherinNo,ownerNo,authorityParams);
			 billSmWasteDtlManager.importDtl(list, wasteNo, ownerNo, user);
			 mode.addObject("result", ResultEnums.SUCCESS.getResultMsg());
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mode.addObject("result", ResultEnums.FAIL.getResultMsg());
			mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
		}
		return mode;
	}
	
	@RequestMapping(value = "/dtlExport")
	public void export(BillSmWasteDtl modelType, HttpServletRequest req, Model model, HttpServletResponse response)throws ManagerException{
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req.getParameter("endColNames");
		String fileName = StringUtils.isEmpty(req.getParameter("fileName")) ? "" : req.getParameter("fileName");
		String locno = StringUtils.isEmpty(req.getParameter("locno")) ? "" : req.getParameter("locno");
		String ownerNo = StringUtils.isEmpty(req.getParameter("ownerNo")) ? "" : req.getParameter("ownerNo");
		String wasteNo = StringUtils.isEmpty(req.getParameter("wasteNo")) ? "" : req.getParameter("wasteNo");
		
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
			BillSmWasteDtl dtl = new BillSmWasteDtl();
			dtl.setLocno(locno);
			dtl.setOwnerNo(ownerNo);
			dtl.setWasteNo(wasteNo);
			
			List<BillSmWasteDtl> sysNoList = billSmWasteDtlManager.findDtlSysNo(dtl, authorityParams);
			HSSFWorkbook wb = null;
			if(sysNoList != null && sysNoList.size() > 0) {
				for(int i=0; i < sysNoList.size(); i++){
					BillSmWasteDtl dd = sysNoList.get(i);
					String sysNoStr = dd.getSysNoStr();
					dtl.setSysNo(dd.getSysNo());
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
					obj = billSmWasteDtlManager.findDtlSysNoByPage(dtl, authorityParams);
					List<BillSmWasteDtl> data = (List<BillSmWasteDtl>) obj.get("rows");
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
	
	
	//整箱添加箱容器
	@RequestMapping(value = "/saveWasteBoxContainer")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveWasteBoxContainer(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillSmWasteDtl> boxList = new ArrayList<BillSmWasteDtl>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				boxList = convertListWithTypeReference(mapper, list);
			}
			billSmWasteDtlManager.saveWasteBoxContainer(boxList,user.getLoginName(),user.getUsername());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "添加失败,请联系管理员!");
		}
		return map;
	}
		
}