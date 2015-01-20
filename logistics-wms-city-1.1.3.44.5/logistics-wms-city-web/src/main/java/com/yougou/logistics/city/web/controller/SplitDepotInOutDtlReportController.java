package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.model.SplitDepotInOutDtlReport;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.SplitDepotInOutDtlReportManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-16 下午2:19:05
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/splitdepotinoutreport")
@ModuleVerify("25130110")
public class SplitDepotInOutDtlReportController extends BaseCrudController<SplitDepotInOutDtlReport> {
	
	@Log
	private Logger log;
	
	@Resource
	private SplitDepotInOutDtlReportManager splitDepotInOutDtlReportManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("splitdepotinoutreport/",splitDepotInOutDtlReportManager);
	}
	
	
	@RequestMapping(value = "/listReport.json")
	@ResponseBody
	public  Map<String, Object> queryListReport(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
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
			
			this.paramsHandle(params);
			
//			//季节
//			String season = (String)params.get("season");
//			String [] seasonValues = null;
//			if(StringUtils.isNotEmpty(season)){
//				seasonValues = season.split(",");
//			}
//			params.put("seasonValues",seasonValues);
//			//性别
//			String gender = (String) params.get("gender");
//			String [] genderValues = null;
//			if(StringUtils.isNotEmpty(gender)){
//				genderValues = gender.split(",");
//			}
//			params.put("genderValues",genderValues);
			
			
			
			
			int total = splitDepotInOutDtlReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<SplitDepotInOutDtlReport> list = splitDepotInOutDtlReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			
			BigDecimal totalQty = new BigDecimal(0);//库存数量
			BigDecimal totalCrkQty = new BigDecimal(0);//仓入库数量
			BigDecimal totalCyrQty = new BigDecimal(0);//仓移入数量
			BigDecimal totalDthQty = new BigDecimal(0);//店退货数量
			BigDecimal totalQtrkQty = new BigDecimal(0);//其他入库数量
			BigDecimal totalRkCytzQty = new BigDecimal(0);//入库差异调整数量
			BigDecimal totalCcdQty = new BigDecimal(0);//仓出店数量
			BigDecimal totalCycQty = new BigDecimal(0);//仓移出数量
			BigDecimal totalQtckQty = new BigDecimal(0);//其他出库数量
			BigDecimal totalKbmzhQty = new BigDecimal(0);//跨部门转货数量
			BigDecimal totalCkCytzQty = new BigDecimal(0);//出库差异调整数量
			BigDecimal totalPyQty = new BigDecimal(0);//盘盈数量
			BigDecimal totalPkQty = new BigDecimal(0);//盘亏数量
			BigDecimal totalThisIssueQty = new BigDecimal(0);//本期库存
			
			for(SplitDepotInOutDtlReport report:list){
				totalQty = totalQty.add(report.getQty()==null?new BigDecimal(0):report.getQty());
				totalCrkQty = totalCrkQty.add(report.getCrkQty()==null?new BigDecimal(0):report.getCrkQty());
				totalCyrQty = totalCyrQty.add(report.getCyrQty()==null?new BigDecimal(0):report.getCyrQty());
				totalDthQty = totalDthQty.add(report.getDthQty()==null?new BigDecimal(0):report.getDthQty());
				totalQtrkQty = totalQtrkQty.add(report.getQtrkQty()==null?new BigDecimal(0):report.getQtrkQty());
				totalRkCytzQty = totalRkCytzQty.add(report.getRkCytzQty()==null?new BigDecimal(0):report.getRkCytzQty());
				totalCcdQty = totalCcdQty.add(report.getCcdQty()==null?new BigDecimal(0):report.getCcdQty());
				totalCycQty = totalCycQty.add(report.getCycQty()==null?new BigDecimal(0):report.getCycQty());
				totalQtckQty = totalQtckQty.add(report.getQtckQty()==null?new BigDecimal(0):report.getQtckQty());
				totalKbmzhQty = totalKbmzhQty.add(report.getKbmzhQty()==null?new BigDecimal(0):report.getKbmzhQty());
				totalCkCytzQty = totalCkCytzQty.add(report.getCkCytzQty()==null?new BigDecimal(0):report.getCkCytzQty());
				totalPyQty = totalPyQty.add(report.getPyQty()==null?new BigDecimal(0):report.getPyQty());
				totalPkQty = totalPkQty.add(report.getPkQty()==null?new BigDecimal(0):report.getPkQty());
				totalThisIssueQty = totalThisIssueQty.add(report.getThisIssueQty()==null?new BigDecimal(0):report.getThisIssueQty());
				//report.setPkQty(new BigDecimal(0).subtract(report.getPkQty()));//盘亏显示负数
				
				//只显示盘盈或者盘亏
//				int py = report.getPyQty().intValue();
//				int pk = report.getPkQty().intValue();
//				int pdValue = pk+py;
//				if(pdValue > 0){
//					report.setPyQty(new BigDecimal(pdValue));
//					report.setPkQty(new BigDecimal(0));
//				}
//				if(pdValue < 0){
//					report.setPyQty(new BigDecimal(0));
//					report.setPkQty(new BigDecimal(pdValue));
//				}
			}
			
			//只显示盘盈或者盘亏
//			int py = totalPyQty.intValue();
//			int pk = totalPkQty.intValue();
//			int pdValue = pk+py;
//			if(pdValue > 0){
//				totalPyQty = new BigDecimal(pdValue);
//				totalPkQty = new BigDecimal(0);
//			}
//			if(pdValue < 0){
//				totalPyQty = new BigDecimal(0); 
//				totalPkQty = new BigDecimal(pdValue);
//			}
			
			footer.put("itemNo", "小计");
			footer.put("qty", totalQty);
			footer.put("crkQty", totalCrkQty);
			footer.put("cyrQty", totalCyrQty);
			footer.put("dthQty", totalDthQty);
			footer.put("qtrkQty", totalQtrkQty);
			footer.put("rkCytzQty", totalRkCytzQty);
			footer.put("ccdQty", totalCcdQty);
			footer.put("cycQty", totalCycQty);
			footer.put("qtckQty", totalQtckQty);
			footer.put("kbmzhQty", totalKbmzhQty);
			footer.put("ckCytzQty", totalCkCytzQty);
			footer.put("pyQty", totalPyQty);
			footer.put("pkQty", totalPkQty);
			footer.put("thisIssueQty", totalThisIssueQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = splitDepotInOutDtlReportManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					
					String pyNum = sumFoot1.get("pyQty").toString();
					String pkNum = sumFoot1.get("pkQty").toString();
					BigDecimal bPyNum = new BigDecimal(pyNum);
					BigDecimal bPkNum = new BigDecimal(pkNum);
					int pyQty = bPyNum.intValue();
					int pkQty = bPkNum.intValue();
//					int pdV = bPkNum.intValue()+bPyNum.intValue();
//					if(pdV > 0){
//						pyQty = pdV;
//						pkQty = 0;
//					}
//					if(pdV < 0){
//						pyQty = 0;
//						pkQty = pdV;
//					}
					
					sumFoot2.put("qty", sumFoot1.get("qty"));
					sumFoot2.put("crkQty", sumFoot1.get("crkQty"));
					sumFoot2.put("cyrQty", sumFoot1.get("cyrQty"));
					sumFoot2.put("dthQty", sumFoot1.get("dthQty"));
					sumFoot2.put("qtrkQty", sumFoot1.get("qtrkQty"));
					sumFoot2.put("rkCytzQty", sumFoot1.get("rkCytzQty"));
					sumFoot2.put("ccdQty", sumFoot1.get("ccdQty"));
					sumFoot2.put("cycQty", sumFoot1.get("cycQty"));
					sumFoot2.put("qtckQty", sumFoot1.get("qtckQty"));
					sumFoot2.put("kbmzhQty", sumFoot1.get("kbmzhQty"));
					sumFoot2.put("ckCytzQty", sumFoot1.get("ckCytzQty"));
					sumFoot2.put("pyQty", pyQty);
					sumFoot2.put("pkQty", pkQty);
					sumFoot2.put("thisIssueQty", sumFoot1.get("thisIssueQty"));
					sumFoot2.put("isselectsum", true);
				}
			}
			sumFoot2.put("itemNo", "合计");
			footerList.add(sumFoot2);

			obj.put("footer", footerList);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
   	 * 参数处理
   	 * @param params
   	 */
   	private void paramsHandle(Map<String, Object> params) {
   		
   		//大类一
   		String cateOne = (String) params.get("cateOne");
   		String[] cateOneValues = null;
   		if (StringUtils.isNotEmpty(cateOne)) {
   			cateOneValues = cateOne.split(",");
   		}
   		params.put("cateOneValues", cateOneValues);		
   		
   		//大类二
   		String cateTwo = (String) params.get("cateTwo");
   		String[] cateTwoValues = null;
   		if (StringUtils.isNotEmpty(cateTwo)) {
   			cateTwoValues = cateTwo.split(",");
   		}
   		params.put("cateTwoValues", cateTwoValues);		
   		
   		//大类三
   		String cateThree = (String) params.get("cateThree");
   		String[] cateThreeValues = null;
   		if (StringUtils.isNotEmpty(cateThree)) {
   			cateThreeValues = cateThree.split(",");
   		}
   		params.put("cateThreeValues", cateThreeValues);	
   		
   		//季节
		String season = (String)params.get("season");
		String [] seasonValues = null;
		if(StringUtils.isNotEmpty(season)){
			seasonValues = season.split(",");
		}
		params.put("seasonValues",seasonValues);
		//性别
		String gender = (String) params.get("gender");
		String [] genderValues = null;
		if(StringUtils.isNotEmpty(gender)){
			genderValues = gender.split(",");
		}
		params.put("genderValues",genderValues);
   	}
	
	@RequestMapping(value = "/doExport")
	public void export(HttpServletRequest req,Model model,HttpServletResponse response){
		try {
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
			
			this.paramsHandle(params);
			
//			//季节
//			String season = (String)params.get("season");
//			String [] seasonValues = null;
//			if(StringUtils.isNotEmpty(season)){
//				seasonValues = season.split(",");
//			}
//			params.put("seasonValues",seasonValues);
//			//性别
//			String gender = (String) params.get("gender");
//			String [] genderValues = null;
//			if(StringUtils.isNotEmpty(gender)){
//				genderValues = gender.split(",");
//			}
//			params.put("genderValues",genderValues);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
//			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10000 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));

			int total = splitDepotInOutDtlReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, total, (int) total);
			List<SplitDepotDateSumReport> list = splitDepotInOutDtlReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
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
	             int [][] xys = {
	            		 		{0,0,1,0},
	            		 		{0,1,1,1},
	            		 		{0,2,1,2},
	            		 		{0,3,1,3},
	            		 		{0,4,1,4},
	            		 		{0,5,1,5},
	            		 		{0,6,1,6},
	            		 		{0,7,1,7},
	            		 		{0,8,0,12},
	            		 		{0,13,0,17},
	            		 		{0,18,0,19},
	            		 		
	            		 		{1,8,1,8},
	            		 		{1,9,1,9},
	            		 		{1,10,1,10},
	            		 		{1,11,1,11},
	            		 		{1,12,1,12},
	            		 		{1,13,1,13},
	            		 		{1,14,1,14},
	            		 		{1,15,1,15},
	            		 		{1,16,1,16},
	            		 		{1,17,1,17},
	            		 		{1,18,1,18},
	            		 		{1,19,1,19}
	            		 	};
	             int [] sumColIdxs = {6,7,8,9,10,11,12,13,14,15,16,17,18,19};
	             
	             Map<JqueryDataGrid, int[]> tabColMap = new HashMap<JqueryDataGrid, int[]>();
	             int idx = 0;
	             for(JqueryDataGrid jdg:ColumnsList){
	            	 tabColMap.put(jdg, xys[idx]);
	            	 idx++;
	             }
	             HSSFWorkbook wb = ExcelUtils.get4XY(fileName, tabColMap, list, true, sumColIdxs);
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
	
//	/**
//	 * 查询基础核对报表数据
//	 * @param req
//	 * @param model
//	 * @param res
//	 * @return
//	 * @throws ManagerException
//	 */
//	@RequestMapping(value = "/getReportTitle")
//	@ResponseBody
//	public ResponseEntity<HashMap> getReportTitle(HttpServletRequest req, Model model, HttpServletResponse res)
//			throws ManagerException {
//		
//		HashMap returnMap = new HashMap();
//		LinkedList returnList = new LinkedList();
//		LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
//		
//		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req.getParameter("preColNames");
//		//String rkColNames = StringUtils.isEmpty(req.getParameter("rkColNames")) ? "" : req.getParameter("rkColNames");
//		//String ckColNames = StringUtils.isEmpty(req.getParameter("ckColNames")) ? "" : req.getParameter("ckColNames");
//		//String pdColNames = StringUtils.isEmpty(req.getParameter("pdColNames")) ? "" : req.getParameter("pdColNames");
//		
//		List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
//		ObjectMapper mapper = new ObjectMapper();
//		try{
//			if (StringUtils.isNotEmpty(preColNames)) {
//				preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
//				});
//			}
//		}catch (Exception e) {
//			throw new ManagerException(e);
//		}
//		
//		for (JqueryDataGrid j : preColNamesList) {
//			//声明集合变量
//			//====开始处理表头====
//			Editor defaultEditor = new Editor();
//			defaultEditor.setType("validatebox");
//			//单据名称列
//			JqueryDataGrid jqueryDataGrid = new JqueryDataGrid();
//			jqueryDataGrid.setTitle(j.getTitle());
//			jqueryDataGrid.setWidth(120);
//			jqueryDataGrid.setEditor(defaultEditor);
//			jqueryDataGrid.setRowspan(2);
//			jqueryDataGrid.setAlign("left");
//			jqueryDataGrid.setField(j.getField());
//			colList.add(jqueryDataGrid);
//		}
//		
//		returnList.add(colList);
//		returnMap.put("returnCols", returnList);
//		return new ResponseEntity<HashMap>(returnMap, HttpStatus.OK);
//	}
}
