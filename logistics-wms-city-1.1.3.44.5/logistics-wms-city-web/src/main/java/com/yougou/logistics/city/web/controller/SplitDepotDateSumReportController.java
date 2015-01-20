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
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.SplitDepotDateSumReportManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-18 下午1:46:41
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/splitdepotdatesumreport")
@ModuleVerify("25130150")
public class SplitDepotDateSumReportController extends BaseCrudController<SplitDepotDateSumReport> {

	@Log
	private Logger log;
	
	@Resource
	private SplitDepotDateSumReportManager depotDateSumReportManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("splitdepotdatesumreport/",depotDateSumReportManager);
	}
	
	@RequestMapping(value = "/listReport.json")
	@ResponseBody
	public  Map<String, Object> queryListReport(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			//int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			//int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			
			//参数处理
			this.paramsHandle(params);
			
			//int total = depotDateSumReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(0, 0, (int) 0);
			//List<SplitDepotDateSumReport> list = depotDateSumReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			List<SplitDepotDateSumReport> list = depotDateSumReportManager.findSplitDepotDateSumReportList(params, authorityParams);
			//obj.put("total", total);
			obj.put("rows", list);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			
			BigDecimal totalQty = new BigDecimal(0);//上期库存数量
			BigDecimal totalThisIssueQty = new BigDecimal(0);//本期库存数量
			BigDecimal totalRkCrkQty = new BigDecimal(0);//厂入库数量
			BigDecimal totalRkCyrQty = new BigDecimal(0);//仓移入数量
			BigDecimal totalRkDthQty = new BigDecimal(0);//店退货数量
			BigDecimal totalRkQtrkQty = new BigDecimal(0);//其他入库数量
			BigDecimal totalRkCytzQty = new BigDecimal(0);//入库差异调整数量
			BigDecimal totalCkCcdQty = new BigDecimal(0);//仓出店数量
			BigDecimal totalCkCycQty = new BigDecimal(0);//仓移出数量
			BigDecimal totalCkQtckQty = new BigDecimal(0);//其他出库数量
			BigDecimal totalCkKbmzhQty = new BigDecimal(0);//跨部门转货数量
			BigDecimal totalCkCytzQty = new BigDecimal(0);//出库差异调整数量
			BigDecimal totalPdPyQty = new BigDecimal(0);//盘盈数量
			BigDecimal totalPdPkQty = new BigDecimal(0);//盘亏数量
			BigDecimal totalTzKctzQty = new BigDecimal(0);//库存品质调整
			BigDecimal totalTzKctzTypeQty = new BigDecimal(0);//库存属性调整
			
			BigDecimal totalYdhCrQty = new BigDecimal(0);//预到货未收-厂入
			BigDecimal totalYdhCrBoxQty = new BigDecimal(0);//预到货未收-厂入箱数
			BigDecimal totalYdhCyrQty = new BigDecimal(0);//预到货未收-仓移入
			BigDecimal totalYdhCyrBoxQty = new BigDecimal(0);//预到货未收-仓移入箱数
			BigDecimal totalYdhDtcQty = new BigDecimal(0);//预到货未收-店退仓
			BigDecimal totalYdhDtcBoxQty = new BigDecimal(0);//预到货未收-店退仓箱数
			BigDecimal totalYswyCrQty = new BigDecimal(0);//已收未验-厂入
			BigDecimal totalYswyCrBoxQty = new BigDecimal(0);//已收未验-厂入箱数
			BigDecimal totalYswyCyrQty = new BigDecimal(0);//已收未验-仓移入
			BigDecimal totalYswyCyrBoxQty = new BigDecimal(0);//已收未验-仓移入箱数
			BigDecimal totalYswyDtcQty = new BigDecimal(0);//已收未验-店退仓
			BigDecimal totalYswyDtcBoxQty = new BigDecimal(0);//已收未验-店退仓箱数
			BigDecimal totalYshCrQty = new BigDecimal(0);//已收货数-厂入
			BigDecimal totalYshCyrQty = new BigDecimal(0);//已收货数-仓移入
			BigDecimal totalYshDtcQty = new BigDecimal(0);//已收货数-店退仓
			BigDecimal totalYshCrBoxQty = new BigDecimal(0);//已收货数-厂入箱数
			BigDecimal totalYshCyrBoxQty = new BigDecimal(0);//已收货数-仓移入箱数
			BigDecimal totalYshDtcBoxQty = new BigDecimal(0);//已收货数-店退仓箱数
			
			
			for(SplitDepotDateSumReport report:list){
				
				//BigDecimal ckCytzQty = (report.getCkCytzQty()==null?new BigDecimal(0):report.getCkCytzQty()).abs();
				totalQty = totalQty.add(report.getQty()==null?new BigDecimal(0):report.getQty());
				totalThisIssueQty = totalThisIssueQty.add(report.getThisIssueQty()==null?new BigDecimal(0):report.getThisIssueQty());
				totalRkCrkQty = totalRkCrkQty.add(report.getRkCrkQty()==null?new BigDecimal(0):report.getRkCrkQty());
				totalRkCyrQty = totalRkCyrQty.add(report.getRkCyrQty()==null?new BigDecimal(0):report.getRkCyrQty());
				totalRkDthQty = totalRkDthQty.add(report.getRkDthQty()==null?new BigDecimal(0):report.getRkDthQty());
				totalRkQtrkQty = totalRkQtrkQty.add(report.getRkQtrkQty()==null?new BigDecimal(0):report.getRkQtrkQty());
				totalRkCytzQty = totalRkCytzQty.add(report.getRkCytzQty()==null?new BigDecimal(0):report.getRkCytzQty());
				totalCkCcdQty = totalCkCcdQty.add(report.getCkCcdQty()==null?new BigDecimal(0):report.getCkCcdQty());
				totalCkCycQty = totalCkCycQty.add(report.getCkCycQty()==null?new BigDecimal(0):report.getCkCycQty());
				totalCkQtckQty = totalCkQtckQty.add(report.getCkQtckQty()==null?new BigDecimal(0):report.getCkQtckQty());
				totalCkKbmzhQty = totalCkKbmzhQty.add(report.getCkKbmzhQty()==null?new BigDecimal(0):report.getCkKbmzhQty());
				totalCkCytzQty = totalCkCytzQty.add(report.getCkCytzQty()==null?new BigDecimal(0):report.getCkCytzQty());
				totalPdPyQty = totalPdPyQty.add(report.getPdPyQty()==null?new BigDecimal(0):report.getPdPyQty());
				totalPdPkQty = totalPdPkQty.add(report.getPdPkQty()==null?new BigDecimal(0):report.getPdPkQty());
				totalTzKctzQty = totalTzKctzQty.add(report.getTzKctzQty()==null?new BigDecimal(0):report.getTzKctzQty());
				totalTzKctzTypeQty = totalTzKctzTypeQty.add(report.getTzKctzTypeQty()==null?new BigDecimal(0):report.getTzKctzTypeQty());
				
				totalYdhCrQty = totalYdhCrQty.add(report.getYdhCrQty()==null?new BigDecimal(0):report.getYdhCrQty());
				totalYdhCyrQty = totalYdhCyrQty.add(report.getYdhCyrQty()==null?new BigDecimal(0):report.getYdhCyrQty());
				totalYdhDtcQty = totalYdhDtcQty.add(report.getYdhDtcQty()==null?new BigDecimal(0):report.getYdhDtcQty());
				totalYswyCrQty = totalYswyCrQty.add(report.getYswyCrQty()==null?new BigDecimal(0):report.getYswyCrQty());
				totalYswyCyrQty = totalYswyCyrQty.add(report.getYswyCyrQty()==null?new BigDecimal(0):report.getYswyCyrQty());
				totalYswyDtcQty = totalYswyDtcQty.add(report.getYswyDtcQty()==null?new BigDecimal(0):report.getYswyDtcQty());
				
				totalYdhCrBoxQty = totalYdhCrBoxQty.add(report.getYdhCrBoxQty()==null?new BigDecimal(0):report.getYdhCrBoxQty());
				totalYdhCyrBoxQty = totalYdhCyrBoxQty.add(report.getYdhCyrBoxQty()==null?new BigDecimal(0):report.getYdhCyrBoxQty());
				totalYdhDtcBoxQty = totalYdhDtcBoxQty.add(report.getYdhDtcBoxQty()==null?new BigDecimal(0):report.getYdhDtcBoxQty());
				totalYswyCrBoxQty = totalYswyCrBoxQty.add(report.getYswyCrBoxQty()==null?new BigDecimal(0):report.getYswyCrBoxQty());
				totalYswyCyrBoxQty = totalYswyCyrBoxQty.add(report.getYswyCyrBoxQty()==null?new BigDecimal(0):report.getYswyCyrBoxQty());
				totalYswyDtcBoxQty = totalYswyDtcBoxQty.add(report.getYswyDtcBoxQty()==null?new BigDecimal(0):report.getYswyDtcBoxQty());
				
				totalYshCrQty = totalYshCrQty.add(report.getYshCrQty()==null?new BigDecimal(0):report.getYshCrQty());
				totalYshCyrQty = totalYshCyrQty.add(report.getYshCyrQty()==null?new BigDecimal(0):report.getYshCyrQty());
				totalYshDtcQty = totalYshDtcQty.add(report.getYshDtcQty()==null?new BigDecimal(0):report.getYshDtcQty());
				totalYshCrBoxQty = totalYshCrBoxQty.add(report.getYshCrBoxQty()==null?new BigDecimal(0):report.getYshCrBoxQty());
				totalYshCyrBoxQty = totalYshCyrBoxQty.add(report.getYshCyrBoxQty()==null?new BigDecimal(0):report.getYshCyrBoxQty());
				totalYshDtcBoxQty = totalYshDtcBoxQty.add(report.getYshDtcBoxQty()==null?new BigDecimal(0):report.getYshDtcBoxQty());
				
				//出库差异调整显示正数
				//report.setCkCytzQty(ckCytzQty);
				
				//只显示盘盈或者盘亏
//				int py = report.getPdPyQty().intValue();
//				int pk = report.getPdPkQty().intValue();
//				int pdValue = pk+py;
//				if(pdValue > 0){
//					report.setPdPyQty(new BigDecimal(pdValue));
//					report.setPdPkQty(new BigDecimal(0));
//				}
//				if(pdValue < 0){
//					report.setPdPyQty(new BigDecimal(0));
//					report.setPdPkQty(new BigDecimal(pdValue));
//				}
			}
			
//			//只显示盘盈或者盘亏
//			int py = totalPdPyQty.intValue();
//			int pk = totalPdPkQty.intValue();
//			int pdValue = pk+py;
//			if(pdValue > 0){
//				totalPdPyQty = new BigDecimal(pdValue);
//				totalPdPkQty = new BigDecimal(0);
//			}
//			if(pdValue < 0){
//				totalPdPyQty = new BigDecimal(0); 
//				totalPdPkQty = new BigDecimal(pdValue);
//			}
			
			footer.put("selectDate", "合计");
			footer.put("qty", totalQty);
			footer.put("thisIssueQty", totalThisIssueQty);
			footer.put("rkCrkQty", totalRkCrkQty);
			footer.put("rkCyrQty", totalRkCyrQty);
			footer.put("rkDthQty", totalRkDthQty);
			footer.put("rkQtrkQty", totalRkQtrkQty);
			footer.put("rkCytzQty", totalRkCytzQty);
			footer.put("ckCcdQty", totalCkCcdQty);
			footer.put("ckCycQty", totalCkCycQty);
			footer.put("ckQtckQty", totalCkQtckQty);
			footer.put("ckKbmzhQty", totalCkKbmzhQty);
			footer.put("ckCytzQty", totalCkCytzQty);
			footer.put("pdPyQty", totalPdPyQty);
			footer.put("pdPkQty", totalPdPkQty);
			footer.put("tzKctzQty", totalTzKctzQty);
			footer.put("tzKctzTypeQty", totalTzKctzTypeQty);
			footer.put("ydhCrQty", totalYdhCrQty);
			footer.put("ydhCyrQty", totalYdhCyrQty);
			footer.put("ydhDtcQty", totalYdhDtcQty);
			footer.put("yswyCrQty", totalYswyCrQty);
			footer.put("yswyCyrQty", totalYswyCyrQty);
			footer.put("yswyDtcQty", totalYswyDtcQty);
			footer.put("ydhCrBoxQty", totalYdhCrBoxQty);
			footer.put("ydhCyrBoxQty", totalYdhCyrBoxQty);
			footer.put("ydhDtcBoxQty", totalYdhDtcBoxQty);
			footer.put("yswyCrBoxQty", totalYswyCrBoxQty);
			footer.put("yswyCyrBoxQty", totalYswyCyrBoxQty);
			footer.put("yswyDtcBoxQty", totalYswyDtcBoxQty);
			footer.put("yshCrQty", totalYshCrQty);
			footer.put("yshCyrQty", totalYshCyrQty);
			footer.put("yshDtcQty", totalYshDtcQty);
			footer.put("yshCrBoxQty", totalYshCrBoxQty);
			footer.put("yshCyrBoxQty", totalYshCyrBoxQty);
			footer.put("yshDtcBoxQty", totalYshDtcBoxQty);
			footerList.add(footer);

			// 合计
//			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
//			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
//			if (pageNo == 1) {
//				sumFoot1 = depotDateSumReportManager.selectSumQty(params, authorityParams);
//				if (sumFoot1 != null) {
//					String pkNum = sumFoot1.get("pdPkQty").toString();
//					BigDecimal bPkNum = new BigDecimal(pkNum);
//					
//					sumFoot2.put("qty", sumFoot1.get("qty"));
//					sumFoot2.put("rkCrkQty", sumFoot1.get("rkCrkQty"));
//					sumFoot2.put("rkCyrQty", sumFoot1.get("rkCyrQty"));
//					sumFoot2.put("rkDthQty", sumFoot1.get("rkDthQty"));
//					sumFoot2.put("rkQtrkQty", sumFoot1.get("rkQtrkQty"));
//					sumFoot2.put("ckCcdQty", sumFoot1.get("ckCcdQty"));
//					sumFoot2.put("ckCycQty", sumFoot1.get("ckCycQty"));
//					sumFoot2.put("ckQtckQty", sumFoot1.get("ckQtckQty"));
//					sumFoot2.put("pdPyQty", sumFoot1.get("pdPyQty"));
//					sumFoot2.put("pdPkQty", new BigDecimal(0).subtract(bPkNum));
//					sumFoot2.put("tzKctzQty", sumFoot1.get("tzKctzQty"));
//					sumFoot2.put("ydhCrQty", sumFoot1.get("ydhCrQty"));
//					sumFoot2.put("ydhCyrQty", sumFoot1.get("ydhCyrQty"));
//					sumFoot2.put("ydhDtcQty", sumFoot1.get("ydhDtcQty"));
//					sumFoot2.put("yswyCrQty", sumFoot1.get("yswyCrQty"));
//					sumFoot2.put("yswyCyrQty", sumFoot1.get("yswyCyrQty"));
//					sumFoot2.put("yswyDtcQty", sumFoot1.get("yswyDtcQty"));
//					sumFoot2.put("yshCrQty", sumFoot1.get("yshCrQty"));
//					sumFoot2.put("yshCyrQty", sumFoot1.get("yshCyrQty"));
//					sumFoot2.put("yshDtcQty", sumFoot1.get("yshDtcQty"));
//					sumFoot2.put("isselectsum", true);
//				}
//			}
//			sumFoot2.put("selectDate", "合计");
//			footerList.add(sumFoot2);
			obj.put("footer", footerList);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	@RequestMapping(value = "/doExport")
	public void export(HttpServletRequest req,Model model,HttpServletResponse response){
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			SimplePage page = new SimplePage(0, 0, (int) 0);
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			//参数处理
			this.paramsHandle(params);
			
			//List<SplitDepotDateSumReport> list = depotDateSumReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			List<SplitDepotDateSumReport> list = depotDateSumReportManager.findSplitDepotDateSumReportList(params, authorityParams);
			
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
	            		 		{0,3,0,7},
	            		 		{0,8,0,12},
	            		 		{0,13,0,14},
	            		 		{0,15,0,16},
	            		 		{0,17,0,22},
	            		 		{0,23,0,28},
	            		 		{0,29,0,34},
	            		 		
	            		 		{1,3,1,3},
	            		 		{1,4,1,4},
	            		 		{1,5,1,5},
	            		 		{1,6,1,6},
	            		 		{1,7,1,7},
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
	            		 		{1,19,1,19},
	            		 		{1,20,1,20},
	            		 		{1,21,1,21},
	            		 		{1,22,1,22},
	            		 		{1,23,1,23},
	            		 		{1,24,1,24},
	            		 		{1,25,1,25},
	            		 		{1,26,1,26},
	            		 		{1,27,1,27},
	            		 		{1,28,1,28},
	            		 		{1,29,1,29},
	            		 		{1,30,1,30},
	            		 		{1,31,1,31},
	            		 		{1,32,1,32},
	            		 		{1,33,1,33},
	            		 		{1,34,1,34}
	            		 	};
	             
	             int [] sumColIdxs = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34};
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
	 /**
   	 * 参数处理
   	 * @param params
   	 */
   	private void paramsHandle(Map<String, Object> params) {;
   		
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
		// 季节
		String season = (String) params.get("season");
		String[] seasonValues = null;
		if (StringUtils.isNotEmpty(season)) {
			seasonValues = season.split(",");
		}
		params.put("seasonValues", seasonValues);
		// 性别
		String gender = (String) params.get("gender");
		String[] genderValues = null;
		if (StringUtils.isNotEmpty(gender)) {
			genderValues = gender.split(",");
		}
		params.put("genderValues", genderValues);
   	}
}
