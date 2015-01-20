package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
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
import com.yougou.logistics.city.common.model.StaffPerformanceReport;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.StaffPerformanceReportManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-7-1 上午10:05:25
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/staffperformancereport")
@ModuleVerify("25130140")
public class StaffPerformanceReportController extends BaseCrudController<StaffPerformanceReport> {

	@Log
	private Logger log;

	@Resource
	private StaffPerformanceReportManager staffPerformanceReportManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("staffperformancereport/", staffPerformanceReportManager);
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
			Map<String, Object> builderParams = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>(builderParams);
			this.paramsHandle(params);//参数处理
			int total = staffPerformanceReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<StaffPerformanceReport> list = staffPerformanceReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalDhshQty = new BigDecimal(0);//到货收货
			BigDecimal totalShysQty = new BigDecimal(0);//收货验收
			BigDecimal totalFhfhQty = new BigDecimal(0);//分货复核
			BigDecimal totalJhlQty = new BigDecimal(0);//拣货量
			BigDecimal totalFhdbQty = new BigDecimal(0);//复核打包
			BigDecimal totalThshQty = new BigDecimal(0);//退货收货
			BigDecimal totalThysQty = new BigDecimal(0);//退货验收
			BigDecimal totalQtrkQty = new BigDecimal(0);//其他入库
			BigDecimal totalQtckQty = new BigDecimal(0);//其他出库
			BigDecimal totalRksjQty = new BigDecimal(0);//入库上架
			BigDecimal totalTcsjQty = new BigDecimal(0);//退仓上架
			BigDecimal totalJsykQty = new BigDecimal(0);//即时移库
			BigDecimal totalCpQty = new BigDecimal(0);//初盘
			BigDecimal totalFpQty = new BigDecimal(0);//复盘
			BigDecimal sumQty = new BigDecimal(0);//总量
			
			for(StaffPerformanceReport report:list){
				totalDhshQty = totalDhshQty.add(report.getDhshQty()==null?new BigDecimal(0):report.getDhshQty());
				totalShysQty = totalShysQty.add(report.getShysQty()==null?new BigDecimal(0):report.getShysQty());
				totalFhfhQty = totalFhfhQty.add(report.getFhfhQty()==null?new BigDecimal(0):report.getFhfhQty());
				totalJhlQty = totalJhlQty.add(report.getJhlQty()==null?new BigDecimal(0):report.getJhlQty());
				totalFhdbQty = totalFhdbQty.add(report.getFhdbQty()==null?new BigDecimal(0):report.getFhdbQty());
				totalThshQty = totalThshQty.add(report.getThshQty()==null?new BigDecimal(0):report.getThshQty());
				totalThysQty = totalThysQty.add(report.getThysQty()==null?new BigDecimal(0):report.getThysQty());
				totalQtrkQty = totalQtrkQty.add(report.getQtrkQty()==null?new BigDecimal(0):report.getQtrkQty());
				totalQtckQty = totalQtckQty.add(report.getQtckQty()==null?new BigDecimal(0):report.getQtckQty());
				totalRksjQty = totalRksjQty.add(report.getRksjQty()==null?new BigDecimal(0):report.getRksjQty());
				totalTcsjQty = totalTcsjQty.add(report.getTcsjQty()==null?new BigDecimal(0):report.getTcsjQty());
				totalJsykQty = totalJsykQty.add(report.getJsykQty()==null?new BigDecimal(0):report.getJsykQty());
				totalCpQty = totalCpQty.add(report.getCpQty()==null?new BigDecimal(0):report.getCpQty());
				totalFpQty = totalFpQty.add(report.getFpQty()==null?new BigDecimal(0):report.getFpQty());
				sumQty = sumQty.add(report.getTotalQty()==null?new BigDecimal(0):report.getTotalQty());
				report.setShowDhshQty(report.getDhshQty()+"("+report.getBoxNum()+"箱)");
			}
			
			footer.put("loginName", "小计");
			footer.put("showDhshQty", totalDhshQty);
			footer.put("shysQty", totalShysQty);
			footer.put("fhfhQty", totalFhfhQty);
			footer.put("jhlQty", totalJhlQty);
			footer.put("fhdbQty", totalFhdbQty);
			footer.put("thshQty", totalThshQty);
			footer.put("thysQty", totalThysQty);
			footer.put("qtrkQty", totalQtrkQty);
			footer.put("qtckQty", totalQtckQty);
			footer.put("rksjQty", totalRksjQty);
			footer.put("tcsjQty", totalTcsjQty);
			footer.put("jsykQty", totalJsykQty);
			footer.put("cpQty", totalCpQty);
			footer.put("fpQty", totalFpQty);
			footer.put("totalQty", sumQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = staffPerformanceReportManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					sumFoot2.put("showDhshQty", sumFoot1.get("dhshQty"));
					sumFoot2.put("dhshQty", sumFoot1.get("dhshQty"));
					sumFoot2.put("shysQty", sumFoot1.get("shysQty"));
					sumFoot2.put("fhfhQty", sumFoot1.get("fhfhQty"));
					sumFoot2.put("jhlQty", sumFoot1.get("jhlQty"));
					sumFoot2.put("fhdbQty", sumFoot1.get("fhdbQty"));
					sumFoot2.put("thshQty", sumFoot1.get("thshQty"));
					sumFoot2.put("thysQty", sumFoot1.get("thysQty"));
					sumFoot2.put("pdPyQty", sumFoot1.get("pdPyQty"));
					sumFoot2.put("qtrkQty", sumFoot1.get("qtrkQty"));
					sumFoot2.put("qtckQty", sumFoot1.get("qtckQty"));
					sumFoot2.put("rksjQty", sumFoot1.get("rksjQty"));
					sumFoot2.put("tcsjQty", sumFoot1.get("tcsjQty"));
					sumFoot2.put("jsykQty", sumFoot1.get("jsykQty"));
					sumFoot2.put("cpQty", sumFoot1.get("cpQty"));
					sumFoot2.put("fpQty", sumFoot1.get("fpQty"));
					sumFoot2.put("totalQty", sumFoot1.get("totalQty"));
					sumFoot2.put("isselectsum", true);
				}
			}
			sumFoot2.put("loginName", "合计");
			footerList.add(sumFoot2);
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
			Map<String, Object> builderParams=builderParams(req, model);	
			Map<String, Object> params = new HashMap<String, Object>(builderParams);			
			//参数处理
			this.paramsHandle(params);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			int total = staffPerformanceReportManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(1, total, (int) total);
			List<StaffPerformanceReport> list = staffPerformanceReportManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			for(StaffPerformanceReport report:list){
				report.setShowDhshQty(report.getDhshQty().toString());
			}
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
	             int [][] xys = new int [24+diff][4];
	             int [] firstXY = {0,0,1,0};
	             xys[0] = firstXY;
	             for(int idx=0;idx<diff;idx++){
	            	 int [] tem = {0,1+idx,1,1+idx};
	            	 xys[1+idx] = tem;
	             }
	             int [][] endXY = {
	         	 		 {0,1+diff,1,1+diff},
	         	 		 {0,2+diff,0,3+diff},
	         	 		 {0,4+diff,0,4+diff},
	         	 		 {0,5+diff,0,6+diff},
	         	 		 {0,7+diff,0,8+diff},
	         	 		 {0,9+diff,0,10+diff},
	         	 		 {0,11+diff,0,13+diff},
	         	 		 {0,14+diff,0,15+diff},
	         	 		 {0,16+diff,1,16+diff},
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
	         	 		 {1,15+diff,1,15+diff}
	             };
	             for(int idx=0;idx<endXY.length;idx++){
	            	 xys[idx+1+diff] = endXY[idx];
	             }
	             
	             int [] sumColIdxs = {2+diff,3+diff,4+diff,5+diff,6+diff,7+diff,8+diff,9+diff,10+diff,11+diff,12+diff,13+diff,14+diff,15+diff,16+diff};
	             
	             Map<JqueryDataGrid, int[]> tabColMap = new HashMap<JqueryDataGrid, int[]>();
	             int idx = 0;
	             for(JqueryDataGrid jdg:ColumnsList){
	            	 tabColMap.put(jdg, xys[idx]);
	            	 idx++;
	             }
				HSSFWorkbook wb = getStaff4XY(fileName, tabColMap, list, true, sumColIdxs);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
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
	 * 生成表头跨行跨列的Excel
	 * @param fileName Excel标题名
	 * @param tabColMap 表头与合并行列坐标映射(坐标为rowFrom,colFrom,rowTo,colTo)
	 * @param data 数据集合
	 * @param needSum 是否需要总计
	 * @param sumColIdxs 需要总计的列序号(首列序号为0)
	 * @return
	 * @throws Exception
	 */
	public static <T> HSSFWorkbook getStaff4XY(String fileName,Map<JqueryDataGrid, int[]> tabColMap,List<T> data,boolean needSum,int [] sumColIdxs) throws Exception{
		
		Map<String, Integer> getterIdxMap = new HashMap<String, Integer>();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet1 = wb.createSheet();
		//sheet名字
		wb.setSheetName(0, fileName);
		sheet1.setDefaultRowHeightInPoints(20);
		HSSFRow row0 = sheet1.createRow(0);
		row0.setHeightInPoints(35);

		//设置样式 表头
		HSSFCellStyle style1=wb.createCellStyle(); 
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font1=wb.createFont();
		font1.setFontHeightInPoints((short)11);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		//总计样式
		HSSFCellStyle style2=wb.createCellStyle(); 
		HSSFFont font2=wb.createFont();
		font2.setFontHeightInPoints((short)11);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style2.setFont(font2);
		//标题样式
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 18);
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font3);
		style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		//第一行 提示长
		HSSFCell cell0 = row0.createCell((short) 0);
		cell0.setCellValue(fileName.toString());
		cell0.setCellStyle(style3);
		
		int maxColIdx = 0;//最大列序号(从0起)
		int firstRowIdx = 0;//首行数据序号(表头的下一行)
		int cateNum = 2;//分类级别数
		
		for(Entry<JqueryDataGrid, int[]> m:tabColMap.entrySet()){
			int [] xy = m.getValue();
			HSSFRow row = sheet1.getRow(xy[0]+1);
			if(row == null){
				row = sheet1.createRow(xy[0]+1);
			}
			if(maxColIdx < xy[3]){
				maxColIdx = xy[3];
			}
			if(firstRowIdx < xy[2]){
				firstRowIdx = xy[2];
			}
			String getter = m.getKey().getField();
			if(!StringUtils.isBlank(getter)){
				getter = "get"+(getter.charAt(0)+"").toUpperCase()+getter.substring(1);
				getterIdxMap.put(getter, xy[3]);
				
				if(getter.equals("getCateOneName")){
					cateNum++;
				}
				if(getter.equals("getCateTwoName")){
					cateNum++;
				}
				if(getter.equals("getCateThreeName")){
					cateNum++;
				}
			}
			HSSFCell cell = row.createCell((short) xy[1]);
			cell.setCellValue(m.getKey().getTitle());
			cell.setCellStyle(style1);
			Region rg = new Region(xy[0]+1, (short) xy[1], xy[2]+1, (short) xy[3]);
			sheet1.addMergedRegion(rg);
		}
		firstRowIdx += 2;
		//合并标题行
		Region rg = new Region(0, (short) 0, 0, (short) maxColIdx);
		sheet1.addMergedRegion(rg);
		//填充数据
		if(CommonUtil.hasValue(data)){
			int dataRowIdx = firstRowIdx;
			int colIdx = 0;
			String getter = null;
			Object result = null;
			Object [] arg = new Object[]{};
			Map<Integer, BigDecimal> sumMap = new HashMap<Integer, BigDecimal>();
			if(needSum && sumColIdxs != null){
				for(int sc:sumColIdxs){
					sumMap.put(sc, new BigDecimal(0));
				}
			}
			
			
			
			for(T t:data){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				for(Entry<String, Integer> m:getterIdxMap.entrySet()){
					colIdx = m.getValue();
					getter = m.getKey();
					result = CommonUtil.invokeMethod(t, getter, arg);
					if(result == null){
						continue;
					}
					if(needSum && sumMap.get(colIdx) != null){
						sumMap.put(colIdx, sumMap.get(colIdx).add(new BigDecimal(result.toString())));
					}
					HSSFCell cell = row.createCell((short)colIdx);
					
					
					if(colIdx == cateNum){
						String box = "("+CommonUtil.invokeMethod(t, "getBoxNum", arg)+"箱)";
						cell.setCellValue(result.toString()+box);
					}else{
						cell.setCellValue(result.toString());
					}
					
				}
				dataRowIdx++;
			}
			if(needSum){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				for(Entry<Integer, BigDecimal> m:sumMap.entrySet()){
					colIdx = m.getKey();
					HSSFCell cell = row.createCell((short)colIdx);
					cell.setCellValue(m.getValue().toString());
					cell.setCellStyle(style2);
				}
				HSSFCell cell = row.createCell((short)0);
				cell.setCellValue("总计");
				cell.setCellStyle(style2);
			}
		}
		return wb;
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
	}
	
}
