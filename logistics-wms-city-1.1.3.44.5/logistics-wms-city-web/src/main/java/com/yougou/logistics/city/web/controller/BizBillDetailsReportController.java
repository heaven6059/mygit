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
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BizBillDetailsReport;
import com.yougou.logistics.city.common.model.ItemCellContent;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BizBillDetailsReportManager;
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
@RequestMapping("/bizbilldetailsreport")
@ModuleVerify("25130170")
public class BizBillDetailsReportController extends BaseCrudController<BizBillDetailsReport> {

	@Log
	private Logger log;
	
	@Resource
	private BizBillDetailsReportManager bizBillDetailsReportManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("bizbilldetailsreport/",bizBillDetailsReportManager);
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
			
			int total = bizBillDetailsReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BizBillDetailsReport> list = bizBillDetailsReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();

			BigDecimal totalPlanQty = new BigDecimal(0);//计划数
			BigDecimal totalRealQty = new BigDecimal(0);//实数
			
			
			for(BizBillDetailsReport report:list){
				totalPlanQty = totalPlanQty.add(report.getPlanQty()==null?new BigDecimal(0):report.getPlanQty());
				totalRealQty = totalRealQty.add(report.getRealQty()==null?new BigDecimal(0):report.getRealQty());
			}
			footer.put("createtm", "小计");
			footer.put("planQty", totalPlanQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = bizBillDetailsReportManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					sumFoot2.put("planQty", sumFoot1.get("planqty"));
					sumFoot2.put("realQty", sumFoot1.get("realqty"));		
					sumFoot2.put("isselectsum", true);
				}else{
					sumFoot2.put("planQty", 0);
					sumFoot2.put("realQty", 0);	
					sumFoot2.put("isselectsum", true);
				}
			}
			sumFoot2.put("createtm", "合计");
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
			Map<String, Object> builderParams=builderParams(req, model);	
			Map<String, Object> params = new HashMap<String, Object>(builderParams);			
			//参数处理
			this.paramsHandle(params);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			int total = bizBillDetailsReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(1, total, (int) total);
			List<BizBillDetailsReport> list = bizBillDetailsReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			
	        String exportColumns=StringUtils.isEmpty(req.getParameter("exportColumns")) ? "" : req
					.getParameter("exportColumns");
	        String fileName=(String)params.get( "fileName");
//	        int firstRowNum=Integer.valueOf(params.get( "firstRowNum").toString());
//	        int diff = firstRowNum - 10;
//	        ObjectMapper mapper = new ObjectMapper();
//	        if (StringUtils.isNotEmpty(exportColumns)){
//	        	 exportColumns=exportColumns.replace( "[" ,"" );
//	             exportColumns=exportColumns.replace( "]" ,"" );
//	             exportColumns= "[" +exportColumns+"]";
//	             List<JqueryDataGrid> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<JqueryDataGrid>>() {});
//	             int [][] xys = new int [31+diff][4];
//	             int [] firstXY = {0,0,1,0};
//	             xys[0] = firstXY;
//	             for(int idx=0;idx<diff;idx++){
//	            	 int [] tem = {0,1+idx,1,1+idx};
//	            	 xys[1+idx] = tem;
//	             }
//	             int [][] endXY = {
//	            		 {0,1+diff,1,1+diff},
//	            		 {0,2+diff,0,5+diff},
//	            		 {0,6+diff,0,9+diff},
//	            		 {0,10+diff,0,11+diff},
//	            		 {0,12+diff,0,12+diff},
//	            		 {0,13+diff,0,13+diff},
//	            		 {0,14+diff,0,16+diff},
//	            		 {0,17+diff,0,19+diff},
//	            		 {0,20+diff,0,22+diff},
//	            		 {1,2+diff,1,2+diff},
//	            		 {1,3+diff,1,3+diff},
//	            		 {1,4+diff,1,4+diff},
//	            		 {1,5+diff,1,5+diff},
//	            		 {1,6+diff,1,6+diff},
//	            		 {1,7+diff,1,7+diff},
//	            		 {1,8+diff,1,8+diff},
//	            		 {1,9+diff,1,9+diff},
//	            		 {1,10+diff,1,10+diff},
//	            		 {1,11+diff,1,11+diff},
//	            		 {1,12+diff,1,12+diff},
//	            		 {1,13+diff,1,13+diff},
//	            		 {1,14+diff,1,14+diff},
//	            		 {1,15+diff,1,15+diff},
//	            		 {1,16+diff,1,16+diff},
//	            		 {1,17+diff,1,17+diff},
//	            		 {1,18+diff,1,18+diff},
//	            		 {1,19+diff,1,19+diff},
//	            		 {1,20+diff,1,20+diff},
//	            		 {1,21+diff,1,21+diff},
//	            		 {1,22+diff,1,22+diff}
//	             };
//	             for(int idx=0;idx<endXY.length;idx++){
//	            	 xys[idx+1+diff] = endXY[idx];
//	             }
//	             
//	             int [] sumColIdxs = {1+diff,2+diff,3+diff,4+diff,5+diff,6+diff,7+diff,8+diff,9+diff,10+diff,11+diff,12+diff,13+diff,14+diff,15+diff,16+diff,17+diff,18+diff,19+diff,20+diff,21+diff,22+diff};
//	             
//	             Map<JqueryDataGrid, int[]> tabColMap = new HashMap<JqueryDataGrid, int[]>();
//	             int idx = 0;
//	             for(JqueryDataGrid jdg:ColumnsList){
//	            	 tabColMap.put(jdg, xys[idx]);
//	            	 idx++;
//	             }
//	             HSSFWorkbook wb = ExcelUtils.get4XY(fileName, tabColMap, list, true, sumColIdxs);
//	             response.setContentType("application/vnd.ms-excel");
//	 			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
//	 			response.setHeader("Pragma", "no-cache");
//	 			wb.write(response.getOutputStream());
//	 			response.getOutputStream().flush();
//	 			response.getOutputStream().close();
//	        }
			ObjectMapper mapper = new ObjectMapper();
			exportColumns = exportColumns.replace("[", "");
			exportColumns = exportColumns.replace("]", "");
			exportColumns = "[" + exportColumns + "]";

			// 字段名列表
			List<Map> ColumnsList = mapper.readValue(exportColumns,new TypeReference<List<Map>>(){});

			List<Map> listArrayList = new ArrayList<Map>();
			Map<String,Object> totalMap = new HashMap<String,Object>();
		    BigDecimal planQtyTotal = new BigDecimal(0);
		    BigDecimal realQtyTotal = new BigDecimal(0);
			if (list != null && list.size() > 0) {
				for (BizBillDetailsReport vo : list) {
					Map map = new HashMap();
					planQtyTotal = planQtyTotal.add(vo.getPlanQty());
					realQtyTotal = realQtyTotal.add(vo.getRealQty());
					BeanUtilsCommon.object2MapWithoutNull(vo, map);
					map.put("createtm", vo.getCreatetm());
					if(vo.getEdittm() != null){
						map.put("edittm", vo.getEdittm());
					}
					listArrayList.add(map);
				}
				//合计
				BizBillDetailsReport tempVo = new BizBillDetailsReport();
				tempVo.setPlanQty(planQtyTotal);
				tempVo.setRealQty(realQtyTotal);
				BeanUtilsCommon.object2MapWithoutNull(tempVo,totalMap);
				totalMap.put("createtm", "合计");
				listArrayList.add(totalMap);
				HSSFExport.commonExportData(
						StringUtils.isNotEmpty(fileName) ? fileName : "导出信息",
						ColumnsList, listArrayList, response);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}
}
