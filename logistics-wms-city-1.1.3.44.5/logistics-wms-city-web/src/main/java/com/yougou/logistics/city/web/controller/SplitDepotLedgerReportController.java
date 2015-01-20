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
import com.yougou.logistics.city.common.model.SplitDepotLedgerReport;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.SplitDepotLedgerReportManager;
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
@RequestMapping("/splitdepotledgerreport")
@ModuleVerify("25130130")
public class SplitDepotLedgerReportController extends BaseCrudController<SplitDepotLedgerReport> {

	@Log
	private Logger log;
	
	@Resource
	private SplitDepotLedgerReportManager splitDepotLedgerReportManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("splitdepotledgerreport/",splitDepotLedgerReportManager);
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
			
			int total = splitDepotLedgerReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<SplitDepotLedgerReport> list = splitDepotLedgerReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();

			BigDecimal totalLastIssueQty = new BigDecimal(0);//上期库存
			BigDecimal totalThisIssueQty = new BigDecimal(0);//本期库存
			
			BigDecimal totalRkCrkQty = new BigDecimal(0);//厂入库数量
			BigDecimal totalRkCyrQty = new BigDecimal(0);//仓移入数量
			BigDecimal totalRkDthQty = new BigDecimal(0);//店退货数量
			BigDecimal totalRkQtrkQty = new BigDecimal(0);//其他入库数量
			BigDecimal totalCkCcdQty = new BigDecimal(0);//仓出店数量
			BigDecimal totalCkCycQty = new BigDecimal(0);//仓移出数量
			BigDecimal totalCkQtckQty = new BigDecimal(0);//其他出库数量
			BigDecimal totalCkKbmzhQty = new BigDecimal(0);//跨部门转货数量
			BigDecimal totalPdPyQty = new BigDecimal(0);//盘盈数量
			BigDecimal totalPdPkQty = new BigDecimal(0);//盘亏数量
			BigDecimal totalTzKctzQty = new BigDecimal(0);//库存调整
			BigDecimal totalTzKctzTypeQty = new BigDecimal(0);//库存品质调整
			
			BigDecimal totalYdhCrQty = new BigDecimal(0);//预到货未收-厂入
			BigDecimal totalYdhCyrQty = new BigDecimal(0);//预到货未收-仓移入
			BigDecimal totalYdhDtcQty = new BigDecimal(0);//预到货未收-店退仓
			BigDecimal totalYswyCrQty = new BigDecimal(0);//已收未验-厂入
			BigDecimal totalYswyCyrQty = new BigDecimal(0);//已收未验-仓移入
			BigDecimal totalYswyDtcQty = new BigDecimal(0);//已收未验-店退仓
			BigDecimal totalYshCrQty = new BigDecimal(0);//已收货数-厂入
			BigDecimal totalYshCyrQty = new BigDecimal(0);//已收货数-仓移入
			BigDecimal totalYshDtcQty = new BigDecimal(0);//已收货数-店退仓
			
			
			for(SplitDepotLedgerReport report:list){
				totalLastIssueQty = totalLastIssueQty.add(report.getLastIssueQty()==null?new BigDecimal(0):report.getLastIssueQty());
				totalThisIssueQty = totalThisIssueQty.add(report.getThisIssueQty()==null?new BigDecimal(0):report.getThisIssueQty());
				
				totalRkCrkQty = totalRkCrkQty.add(report.getRkCrkQty()==null?new BigDecimal(0):report.getRkCrkQty());
				totalRkCyrQty = totalRkCyrQty.add(report.getRkCyrQty()==null?new BigDecimal(0):report.getRkCyrQty());
				totalRkDthQty = totalRkDthQty.add(report.getRkDthQty()==null?new BigDecimal(0):report.getRkDthQty());
				totalRkQtrkQty = totalRkQtrkQty.add(report.getRkQtrkQty()==null?new BigDecimal(0):report.getRkQtrkQty());
				totalCkCcdQty = totalCkCcdQty.add(report.getCkCcdQty()==null?new BigDecimal(0):report.getCkCcdQty());
				totalCkCycQty = totalCkCycQty.add(report.getCkCycQty()==null?new BigDecimal(0):report.getCkCycQty());
				totalCkQtckQty = totalCkQtckQty.add(report.getCkQtckQty()==null?new BigDecimal(0):report.getCkQtckQty());
				totalCkKbmzhQty = totalCkKbmzhQty.add(report.getCkKbmzhQty()==null?new BigDecimal(0):report.getCkKbmzhQty());
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
				totalYshCrQty = totalYshCrQty.add(report.getYshCrQty()==null?new BigDecimal(0):report.getYshCrQty());
				totalYshCyrQty = totalYshCyrQty.add(report.getYshCyrQty()==null?new BigDecimal(0):report.getYshCyrQty());
				totalYshDtcQty = totalYshDtcQty.add(report.getYshDtcQty()==null?new BigDecimal(0):report.getYshDtcQty());
			}
			
			footer.put("locName", "小计");
			footer.put("lastIssueQty", totalLastIssueQty);
			footer.put("thisIssueQty", totalThisIssueQty);
			
			footer.put("rkCrkQty", totalRkCrkQty);
			footer.put("rkCyrQty", totalRkCyrQty);
			footer.put("rkDthQty", totalRkDthQty);
			footer.put("rkQtrkQty", totalRkQtrkQty);
			footer.put("ckCcdQty", totalCkCcdQty);
			footer.put("ckCycQty", totalCkCycQty);
			footer.put("ckQtckQty", totalCkQtckQty);
			footer.put("ckKbmzhQty", totalCkKbmzhQty);
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
			footer.put("yshCrQty", totalYshCrQty);
			footer.put("yshCyrQty", totalYshCyrQty);
			footer.put("yshDtcQty", totalYshDtcQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = splitDepotLedgerReportManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					sumFoot2.put("lastIssueQty", sumFoot1.get("lastissueqty"));
					sumFoot2.put("thisIssueQty", sumFoot1.get("thisissueqty"));					
					sumFoot2.put("rkCrkQty", sumFoot1.get("rkCrkQty"));
					sumFoot2.put("rkCyrQty", sumFoot1.get("rkCyrQty"));
					sumFoot2.put("rkDthQty", sumFoot1.get("rkDthQty"));
					sumFoot2.put("rkQtrkQty", sumFoot1.get("rkQtrkQty"));
					sumFoot2.put("ckCcdQty", sumFoot1.get("ckCcdQty"));
					sumFoot2.put("ckCycQty", sumFoot1.get("ckCycQty"));
					sumFoot2.put("ckQtckQty", sumFoot1.get("ckQtckQty"));
					sumFoot2.put("ckKbmzhQty", sumFoot1.get("ckKbmzhQty"));
					sumFoot2.put("pdPyQty", sumFoot1.get("pdPyQty"));
					sumFoot2.put("pdPkQty", sumFoot1.get("pdPkQty"));
					sumFoot2.put("tzKctzQty", sumFoot1.get("tzKctzQty"));
					sumFoot2.put("tzKctzTypeQty", sumFoot1.get("tzKctzTypeQty"));
					sumFoot2.put("ydhCrQty", sumFoot1.get("ydhCrQty"));
					sumFoot2.put("ydhCyrQty", sumFoot1.get("ydhCyrQty"));
					sumFoot2.put("ydhDtcQty", sumFoot1.get("ydhDtcQty"));
					sumFoot2.put("yswyCrQty", sumFoot1.get("yswyCrQty"));
					sumFoot2.put("yswyCyrQty", sumFoot1.get("yswyCyrQty"));
					sumFoot2.put("yswyDtcQty", sumFoot1.get("yswyDtcQty"));
					sumFoot2.put("yshCrQty", sumFoot1.get("yshCrQty"));
					sumFoot2.put("yshCyrQty", sumFoot1.get("yshCyrQty"));
					sumFoot2.put("yshDtcQty", sumFoot1.get("yshDtcQty"));
					sumFoot2.put("isselectsum", true);
				}else{
					sumFoot2.put("lastIssueQty", 0);
					sumFoot2.put("thisIssueQty", 0);					
					sumFoot2.put("rkCrkQty", 0);	
					sumFoot2.put("rkCyrQty", 0);	
					sumFoot2.put("rkDthQty", 0);	
					sumFoot2.put("rkQtrkQty", 0);	
					sumFoot2.put("ckCcdQty", 0);	
					sumFoot2.put("ckCycQty", 0);	
					sumFoot2.put("ckQtckQty", 0);
					sumFoot2.put("ckKbmzhQty", 0);	
					sumFoot2.put("pdPyQty", 0);	
					sumFoot2.put("pdPkQty", 0);	
					sumFoot2.put("tzKctzQty", 0);
					sumFoot2.put("tzKctzTypeQty", 0);
					sumFoot2.put("ydhCrQty", 0);	
					sumFoot2.put("ydhCyrQty", 0);	
					sumFoot2.put("ydhDtcQty", 0);	
					sumFoot2.put("yswyCrQty", 0);	
					sumFoot2.put("yswyCyrQty", 0);	
					sumFoot2.put("yswyDtcQty", 0);	
					sumFoot2.put("yshCrQty", 0);	
					sumFoot2.put("yshCyrQty", 0);	
					sumFoot2.put("yshDtcQty", 0);	
					sumFoot2.put("isselectsum", true);
				}
			}
			sumFoot2.put("locName", "合计");
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
			Map<String, Object> builderParams=builderParams(req, model);	
			Map<String, Object> params = new HashMap<String, Object>(builderParams);			
			//参数处理
			this.paramsHandle(params);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			int total = splitDepotLedgerReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(1, total, (int) total);
			List<SplitDepotLedgerReport> list = splitDepotLedgerReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			
	        String exportColumns=StringUtils.isEmpty(req.getParameter("exportColumns")) ? "" : req
					.getParameter("exportColumns");
	        String fileName=(String)params.get( "fileName");
	        int firstRowNum=Integer.valueOf(params.get( "firstRowNum").toString());
	        int diff = firstRowNum - 10;
	        ObjectMapper mapper = new ObjectMapper();
	        if (StringUtils.isNotEmpty(exportColumns)){
	        	 exportColumns=exportColumns.replace( "[" ,"" );
	             exportColumns=exportColumns.replace( "]" ,"" );
	             exportColumns= "[" +exportColumns+"]";
	             List<JqueryDataGrid> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<JqueryDataGrid>>() {});
	             int [][] xys = new int [32+diff][4];
	             int [] firstXY = {0,0,1,0};
	             xys[0] = firstXY;
	             for(int idx=0;idx<diff;idx++){
	            	 int [] tem = {0,1+idx,1,1+idx};
	            	 xys[1+idx] = tem;
	             }
	             int [][] endXY = {
	            		 {0,1+diff,1,1+diff},
	            		 {0,2+diff,0,5+diff},
	            		 {0,6+diff,0,9+diff},
	            		 {0,10+diff,0,11+diff},
	            		 {0,12+diff,0,12+diff},
	            		 {0,13+diff,0,14+diff},
	            		 {0,15+diff,0,17+diff},
	            		 {0,18+diff,0,20+diff},
	            		 {0,21+diff,0,23+diff},
	            		 {1,2+diff,1,2+diff},
	            		 {1,3+diff,1,3+diff},
	            		 {1,4+diff,1,4+diff},
	            		 {1,5+diff,1,5+diff},
	            		 {1,6+diff,1,6+diff},
	            		 {1,7+diff,1,7+diff},
	            		 {1,8+diff,1,8+diff},
	            		 {1,9+diff,1,9+diff},
	            		 {1,10+diff,1,10+diff},
	            		 {1,11+diff,1,11+diff},
	            		 {1,12+diff,1,12+diff},
	            		 {1,13+diff,1,13+diff},
	            		 {1,14+diff,1,14+diff},
	            		 {1,15+diff,1,15+diff},
	            		 {1,16+diff,1,16+diff},
	            		 {1,17+diff,1,17+diff},
	            		 {1,18+diff,1,18+diff},
	            		 {1,19+diff,1,19+diff},
	            		 {1,20+diff,1,20+diff},
	            		 {1,21+diff,1,21+diff},
	            		 {1,22+diff,1,22+diff},
	            		 {1,23+diff,1,23+diff}
	             };
	             for(int idx=0;idx<endXY.length;idx++){
	            	 xys[idx+1+diff] = endXY[idx];
	             }
	             
	             int [] sumColIdxs = {1+diff,2+diff,3+diff,4+diff,5+diff,6+diff,7+diff,8+diff,9+diff,10+diff,11+diff,12+diff,13+diff,14+diff,15+diff,16+diff,17+diff,18+diff,19+diff,20+diff,21+diff,22+diff,23+diff};
	             
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
