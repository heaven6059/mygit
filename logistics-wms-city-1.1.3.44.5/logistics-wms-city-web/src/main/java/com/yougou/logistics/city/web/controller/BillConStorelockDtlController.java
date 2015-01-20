package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillConStoreLockQuery;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillConStorelockDtlManager;
import com.yougou.logistics.city.manager.CmDefcellManager;
import com.yougou.logistics.city.manager.ItemManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

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

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
@RequestMapping("/bill_con_storelock_dtl")
public class BillConStorelockDtlController extends BaseCrudController<BillConStorelockDtl> {
	@Log
	private Logger log;
    @Resource
    private BillConStorelockDtlManager billConStorelockDtlManager;
    
    @Resource
    private CmDefcellManager cmDefcellManager;
    
    @Resource
    private SizeInfoManager  sizeInfoManager;
    
    @Resource 
    private ItemManager  itemManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billConStorelockDtl/",billConStorelockDtlManager);
    }
    
    /**
     * 查询库存
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value = "/findConContentGroupByPage.json")
	@ResponseBody    
	public  Map<String, Object> findConContentGroupByPage(HttpServletRequest req, Model model) throws ManagerException {
    	try{
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String regex = ",";
			CommonUtil.paramSplit("cateOne", "cateOneValues", params, regex);
			CommonUtil.paramSplit("cateTwo", "cateTwoValues", params, regex);
			CommonUtil.paramSplit("cateThree", "cateThreeValues", params, regex);
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
			//性别
			String season = (String)req.getParameter("seasonStr");
			String [] seasonValues = null;
			if(StringUtils.isNotBlank(season)){
				seasonValues = season.split(",");
			}
			if(seasonValues != null) {
				params.put("seasonValues", seasonValues);
			}
			//季节
			String gender = (String)req.getParameter("genderStr");
			String [] genderValues = null;
			if(StringUtils.isNotBlank(gender)){
				genderValues = gender.split(",");
			}
			if(genderValues != null) {
				params.put("genderValues", genderValues);
			}
    		int total = this.billConStorelockDtlManager.findConContentGroupByCount(params, authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillConStorelockDtl> list = this.billConStorelockDtlManager.findConContentGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
    		Map<String, Object> obj = new HashMap<String, Object>();
    		obj.put("total", total);
    		obj.put("rows", list);
    		return obj; 
    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}  
    
    /**
     * 查询明细
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value = "/findStorelockGroupByPage.json")
	@ResponseBody    
	public  Map<String, Object> findStorelockGroupByPage(HttpServletRequest req, Model model) throws ManagerException {
    	try{
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = this.billConStorelockDtlManager.findStorelockGroupByCount(params, authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillConStorelockDtl> list = this.billConStorelockDtlManager.findStorelockGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
    		Map<String, Object> obj = new HashMap<String, Object>();
    		obj.put("total", total);
    		obj.put("rows", list);
    		
    		// 汇总
    		List<Object> footerList = new ArrayList<Object>();
    		Map<String, Object> footer = new HashMap<String, Object>();
    		BigDecimal totalQty = new BigDecimal(0);
    		BigDecimal totalGoodContentQty = new BigDecimal(0);
    		for (BillConStorelockDtl dtl : list) {
    			totalQty = totalQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
    			totalGoodContentQty = totalGoodContentQty.add(dtl.getGoodContentQty() == null ? new BigDecimal(0) : dtl.getGoodContentQty());
    		}
    		footer.put("cellNo", "小计");
    		footer.put("itemQty", totalQty);
    		footer.put("goodContentQty", totalGoodContentQty);
    		footerList.add(footer);
    		
    		// 合计
    		Map<String, Object> sumFoot = new HashMap<String, Object>();
    		if (pageNo == 1) {
    			sumFoot = billConStorelockDtlManager.selectSumQty(params, authorityParams);
    			if (sumFoot == null) {
    				sumFoot = new SumUtilMap<String, Object>();
    				sumFoot.put("good_content_qty", 0);
    				sumFoot.put("item_qty", 0);
    			}
    			sumFoot.put("isselectsum", true);
    			sumFoot.put("cell_no", "合计");
    		} else {
    			sumFoot.put("cellNo", "合计");
    		}
    		footerList.add(sumFoot);
    		obj.put("footer", footerList);
    		return obj;
    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    	return null;
	} 
    
    
    /**
     * 新增明细
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveStorelockDtl")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveStorelockDtl(BillConStoreLockQuery lockQuery,HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			List<BillConStorelockDtl> listConStoreLocks = new ArrayList<BillConStorelockDtl>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
				listConStoreLocks=convertListWithTypeReference(mapper,list);
			}
			lockQuery.setInsertList(listConStoreLocks);
			billConStorelockDtlManager.saveStorelockDtl(lockQuery);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	
	/**
     * 删除明细
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delStorelockDtl")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delStorelockDtl(BillConStoreLockQuery lockQuery,HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			List<BillConStorelockDtl> listConStoreLocks = new ArrayList<BillConStorelockDtl>();
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
				listConStoreLocks=convertListWithTypeReference(mapper,list);
			}
			lockQuery.setInsertList(listConStoreLocks);
			billConStorelockDtlManager.delStorelockDtl(lockQuery);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillConStorelockDtl> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillConStorelockDtl> tl=new ArrayList<BillConStorelockDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillConStorelockDtl type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillConStorelockDtl.class);
			tl.add(type);
		}
		return tl;
	}
	
	/**
	 * 导出库存冻结明细
	 * 
	 * @param req
	 * @param model
	 * @param response
	 */
	@RequestMapping(value = "/doExport")
	@ResponseBody
	public void export(HttpServletRequest req,Model model,HttpServletResponse response){
		try {
			Map<String,Object> params=builderParams(req, model);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			SimplePage page = new SimplePage(0, 0, (int) 0);
			List<BillConStorelockDtl> list = billConStorelockDtlManager.findStorelockGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
			
			StringBuffer headData = new StringBuffer();
			String ownerName = ((String)params.get("ownerName")).split("→")[1];
			String storelockTypeName = ((String)params.get("storelockTypeName")).split("→")[1];
			String sourceTypeName = ((String)params.get("sourceTypeText")).split("→")[1];
			headData.append("单号：").append(params.get("storelockNo")).append("          ");
			headData.append("货主：").append(ownerName).append("          ");
			headData.append("锁定类型：").append(storelockTypeName).append("          ");
			headData.append("客户：").append(sourceTypeName).append("                    ");
			
	        String exportColumns=StringUtils.isEmpty(req.getParameter("exportColumns")) ? "" : req
					.getParameter("exportColumns");
	        String fileName=(String)params.get( "fileName");
	        ObjectMapper mapper = new ObjectMapper();
	        if (StringUtils.isNotEmpty(exportColumns)){
	        	 exportColumns=exportColumns.replace( "[" ,"" );
	             exportColumns=exportColumns.replace( "]" ,"" );
	             exportColumns= "[" +exportColumns+"]";
	             List<JqueryDataGrid> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<JqueryDataGrid>>() {
	 			});
	            
	            HSSFWorkbook wb = ExcelUtils.getDtlExcel(fileName, headData.toString(),ColumnsList, list);
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
	
	 @RequestMapping("/downloadTemple")
		public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
			 FileUtils.downloadTemple(session, response, "storelockTemplate.xls");
		}
	 
	 @RequestMapping(value = "/iframe")
		public ModelAndView iframe(HttpServletRequest req) throws Exception {
			ModelAndView mode = new ModelAndView("billconstorelock/iframe");
			return mode;
		}
	 
	 @RequestMapping(value = "/importExcel")
		public ModelAndView upLoad(HttpServletRequest request,Model model) {
			ModelAndView mode = new ModelAndView("billconstorelock/iframe");
			SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
			
		    try{
		    	String [] colNames = {"cellNo","itemNo","sizeNo","itemQty"};
		    	String [] keyNames= {"cellNo","itemNo","sizeNo","itemQty"};
		    	boolean [] mustArray = {true,true,true,true};
		    	
		    	AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(request);
				Map<String, Object> paramsAll = builderParams(request, model);
				Map<String, Object> params = new HashMap<String, Object>();
				params.putAll(paramsAll);
				
				List<BillConStorelockDtl> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, keyNames, BillConStorelockDtl.class);
				 if(list.size()==0){
				    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
					mode.addObject("msg","导入的文件没有数据");
					return mode;
				 }
				 
				Map<String ,Object> map = billConStorelockDtlManager.importStorelockDtlExcel(list, authorityParams, params);
				
				 mode.addObject("result",map.get("result"));
				 mode.addObject("msg",map.get("msg"));
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
			}
			return mode;
		}
	
}