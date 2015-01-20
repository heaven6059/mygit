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
import com.yougou.logistics.city.common.dto.InoutDiffReportDto;
import com.yougou.logistics.city.common.model.SplitDepotDateSumReport;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.InoutDiffReportManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-23 上午10:13:45
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/inoutdiffreport")
@ModuleVerify("25130160")
public class InoutDiffReportController extends BaseCrudController<InoutDiffReportDto> {

	@Log
	private Logger log;
	
	@Resource
	private InoutDiffReportManager inoutDiffReportManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("inoutdiffreport/",inoutDiffReportManager);
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
			Map<String, Object> builderParams=builderParams(req, model);			
			Map<String, Object> params = new HashMap<String, Object>(builderParams);
			
			//参数处理
			this.paramsHandle(params);
			
			int total = inoutDiffReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<InoutDiffReportDto> list = inoutDiffReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();

			BigDecimal totalItemQty = new BigDecimal(0);//计划数量
			BigDecimal totalReceiptQty = new BigDecimal(0);//数量(收货/拣货)
			BigDecimal totalCheckQty = new BigDecimal(0);//数量（验收/复核）
			BigDecimal totalDiffQty = new BigDecimal(0);//差异数量
						
			for(InoutDiffReportDto report:list){
				totalItemQty = totalItemQty.add(report.getItemQty()==null?new BigDecimal(0):report.getItemQty());
				totalReceiptQty = totalReceiptQty.add(report.getReceiptQty()==null?new BigDecimal(0):report.getReceiptQty());
				totalCheckQty = totalCheckQty.add(report.getCheckQty()==null?new BigDecimal(0):report.getCheckQty());
				totalDiffQty = totalDiffQty.add(report.getDiffQty()==null?new BigDecimal(0):report.getDiffQty());				
			}
			
			footer.put("poDate", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("receiptQty", totalReceiptQty);
			footer.put("checkQty", totalCheckQty);
			footer.put("diffQty", totalDiffQty);
			
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = inoutDiffReportManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					sumFoot2.put("itemQty", sumFoot1.get("itemqty"));
					sumFoot2.put("receiptQty", sumFoot1.get("receiptqty"));
					sumFoot2.put("checkQty", sumFoot1.get("checkqty"));
					sumFoot2.put("diffQty", sumFoot1.get("diffqty"));					
					sumFoot2.put("isselectsum", true);
				}else{
					sumFoot2.put("itemQty", 0);
					sumFoot2.put("receiptQty", 0);
					sumFoot2.put("checkQty", 0);
					sumFoot2.put("diffQty", 0);					
					sumFoot2.put("isselectsum", true);
				}
			}
			sumFoot2.put("poDate", "合计");
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
		//品牌
		String brandNo = (String) params.get("brandNo");
		String[] brandNoValues = null;
		if (StringUtils.isNotEmpty(brandNo)) {
			brandNoValues = brandNo.split(",");
		}
		params.put("brandNoValues", brandNoValues);
		
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
		

		//品质
		String quality = (String) params.get("quality");
		String[] qualityValues = null;
		if (StringUtils.isNotEmpty(quality)) {
			qualityValues = quality.split(",");
		}
		params.put("qualityValues", qualityValues);
		
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

	
	@RequestMapping(value = "/doExport")
	public void export(HttpServletRequest req,Model model,HttpServletResponse response){
		try {
			Map<String,Object> params=builderParams(req, model);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			SimplePage page = new SimplePage(0, 0, (int) 0);
			List<SplitDepotDateSumReport> list = inoutDiffReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			
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
	            			    {0,1,1,1},
            		 		    {0,2,1,2},
	            		 		{0,3,1,3},
	            		 		{0,4,1,4},
	            		 		{0,5,1,5},
	            		 		{0,6,1,6},
	            		 		{0,7,1,7},
	            		 		{0,8,1,8},
	            		 		{0,9,1,9},
	            		 		{0,10,1,10},
	            		 		{0,11,1,11},
	            		 		{0,12,1,12},
	            		 		{0,13,1,13},
	            		 		{0,14,1,14},
	            		 		{0,15,1,15},
	            		 		{0,16,1,16},
	            		 		{0,17,1,17},
	            		 		{0,18,1,18},
	            		 		{0,19,1,19},
         		 		        {0,20,1,20}
	            		 	};
	             int [] sumColIdxs = {15,16,17,18};
	             
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
}
