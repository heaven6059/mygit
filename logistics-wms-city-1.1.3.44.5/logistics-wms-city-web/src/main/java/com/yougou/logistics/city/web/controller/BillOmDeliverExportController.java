package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
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
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.model.BillOmDeliverExport;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillOmDeliverExportManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 发货导出
 * 
 * @author jiang.ys
 * @date 2013-10-12 下午3:28:44
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_deliver_export")
@ModuleVerify("25130040")
public class BillOmDeliverExportController extends BaseCrudController<BillOmDeliverExport> {
	@Log
    private Logger log;
    
    @Resource
    private BillOmDeliverExportManager billOmDeliverExportManager;
    
    @Resource
	private SizeInfoManager sizeInfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billomdeliverexport/",billOmDeliverExportManager);
    }
    
    @RequestMapping(value = "/get_size")
	@ResponseBody
	public  Object querySize(HttpServletRequest req, Model model){
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	try {
    		Map<String,Object> params = new HashMap<String, Object>();
        	params.put("locno", req.getParameter("locno"));
        	list = billOmDeliverExportManager.findDeliverDtlSize(params);
        	if(list != null){
        		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
        		Map<String, Object> temp;
        		for(Map<String, Object> map : list){
        			temp = new HashMap<String, Object>();
        			temp.put("sizeCode", map.get("SIZE_CODE"));
        			temp.put("sizeNo", map.get("SIZE_NO"));
        			result.add(temp);
        		}
        		return result;
        	}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
    @RequestMapping(value = "/initHead")
	@ResponseBody
	public Map<String, Object> initHead(HttpServletRequest req, HttpSession session, Model model)
			throws ManagerException {
    	
	   	Map<String, Object> builderParams=builderParams(req, model);	
	   	Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(builderParams);
		String itemNo = "";
		if(builderParams.get("itemNo") != null) {
			itemNo = builderParams.get("itemNo").toString().toUpperCase();
			params.put("itemNo", itemNo);
		}
		String barcode = "";
		if(builderParams.get("barcode") != null) {
			barcode = builderParams.get("barcode").toString().toUpperCase();
			params.put("barcode", barcode);
		}
		//性别
		String [] seasonValues = null;
		if(builderParams.get("seasonName") != null){
			String season = builderParams.get("seasonName").toString();
			seasonValues = season.split(",");
		}
		if(seasonValues != null) {
			params.put("seasonValues", seasonValues);
		}
		//季节
		String [] genderValues = null;
		if(builderParams.get("genderName") != null){
			String gender = (String)req.getParameter("genderName");
			genderValues = gender.split(",");
		}
		if(genderValues != null) {
			params.put("genderValues", genderValues);
		}
		     //参数处理
		this.paramsHandle(params);
    	
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		Map<String, Object> map = new HashMap<String, Object>();

		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");

		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		List<String> sizeKindList = billOmDeliverExportManager.findAllDtlSizeKind(params, authorityParams);
		if (sizeKindList == null || sizeKindList.size() == 0) {
			return map;
		}
		Map header = getBrandList(preColNames, endColNames, sizeTypeFiledName, params.get("sysNo").toString(), sizeKindList);
		map.put("header", header);
		return map;
	}
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model){
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil
					.getAuthorityParams(req);
    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		
    	  	Map<String, Object> builderParams=builderParams(req, model);
    	   	Map<String, Object> params = new HashMap<String, Object>();
    		params.putAll(builderParams);
    		String itemNo = "";
    		if(builderParams.get("itemNo") != null) {
    			itemNo = builderParams.get("itemNo").toString().toUpperCase();
    			params.put("itemNo", itemNo);
    		}
    		String barcode = "";
    		if(builderParams.get("barcode") != null) {
    			barcode = builderParams.get("barcode").toString().toUpperCase();
    			params.put("barcode", barcode);
    		}	
    		//性别
    		String [] seasonValues = null;
    		if(builderParams.get("seasonName") != null){
    			String season = builderParams.get("seasonName").toString();
    			seasonValues = season.split(",");
    		}
    		if(seasonValues != null) {
    			params.put("seasonValues", seasonValues);
    		}
    		//季节
    		String [] genderValues = null;
    		if(builderParams.get("genderName") != null){
    			String gender = (String)req.getParameter("genderName");
    			genderValues = gender.split(",");
    		}
    		if(genderValues != null) {
    			params.put("genderValues", genderValues);
    		}
    		     //参数处理
    		this.paramsHandle(params);
    		
    		//返回汇总列表
    		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
//    		Map<String, Object> footerMap = new HashMap<String, Object>();
    		Map<String, Object> footer1Map = new HashMap<String, Object>();
//    		footerMap.put("brandName", "小计");
//    		footerList.add(footerMap);
    				
    		int total = this.billOmDeliverExportManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillOmDeliverExport> list = this.billOmDeliverExportManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
    		Map<String, List<SizeInfo>> sizeKindListMap = new HashMap<String, List<SizeInfo>>();
    		List<SizeInfo> sizeKindList = null;
    		String sizeKind = null;
    		String sizeCode = null;
    		String sizeV = null;
    		if(list != null){
    			List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
    			Map<String, Object> temp;
    			List<Map<String, Object>> sizeNum = null;
    			for(BillOmDeliverExport vo:list){
    				
    				temp = new HashMap<String, Object>();
    				temp.put("locno", vo.getLocno());
    				temp.put("deliverNo", vo.getDeliverNo());
    				temp.put("brandName", vo.getBrandName());
    				temp.put("barcode", vo.getBarcode());
    				temp.put("storeNo", vo.getStoreNo());
    				temp.put("storeName", vo.getStoreName());
    				temp.put("itemNo", vo.getItemNo());
    				temp.put("itemName", vo.getItemName());
    				temp.put("years", vo.getYears());
    				temp.put("season", vo.getSeason());
    				temp.put("gender", vo.getGender());
    				temp.put("cateNo", vo.getCateNo());
    				temp.put("yearsName", vo.getYearsName());
    				temp.put("seasonName", vo.getSeasonName());
    				temp.put("genderName", vo.getGenderName());
    				temp.put("cateName", vo.getCateName());
    				temp.put("total", vo.getTotal());
    				temp.put("expDate", format.format(vo.getExpDate()));
    				temp.put("expNo", vo.getExpNo());
    				sizeNum = billOmDeliverExportManager.findDeliverDtlSizeNum(temp, authorityParams);
    				if(sizeNum != null){
    					for(Map<String, Object> sn:sizeNum){
    						sizeKind = sn.get("SIZE_KIND").toString();
    						sizeCode = sn.get("SIZE_CODE").toString();
    						sizeKindList = sizeKindListMap.get(sizeKind);
    						if(sizeKindList == null){    							
    							List<String> kind =new ArrayList<String>();
    							kind.add(sizeKind);
    							sizeKindList=this.sizeInfoManager.selectSizeInfoBySizeNoList(kind, params.get("sysNo").toString(),null);
    							sizeKindListMap.put(sizeKind, sizeKindList);
    						}
    						temp.put("sizeKind", sizeKind);
    						for(int idx=0;idx<sizeKindList.size();idx++){
    							if(sizeKindList.get(idx).getSizeCode().equals(sizeCode)){
    								sizeV = "v"+(idx+1);
    								temp.put(sizeV, sn.get("QTY"));
    								break;
    							}
    						}

    						////////////////////
//    						try {
//    							FooterUtil.setFooterMap(sizeV, new BigDecimal(sn.get("QTY").toString()), footerMap);
//    							FooterUtil.setFooterMap("total", new BigDecimal(sn.get("QTY").toString()), footerMap);
//    						} catch (Exception e) {
//    							log.error(e.getMessage(), e);
//    						}
    						////////////////////
    					}
    				}
    				rows.add(temp);
    			}
    			if(pageNo == 1){
//    				footer1Map.putAll(footerMap);//1.将小计直接设值到汇总
    				footer1Map.put("brandName", "汇总");
    				footer1Map.put("isselectsum", true);
    				footerList.add(footer1Map);
    				
    				Map<String, Object> sumMap = billOmDeliverExportManager.selectSumQty(params, authorityParams);
    				footer1Map.put("total", sumMap.get("total"));		
					
//    				if(total > pageSize){
//    					while((pageNo * pageSize) < total){//2.批量处理(以防数据过大),从第二页开始
//    						pageNo++;
//    						page = new SimplePage(pageNo, pageSize, (int) total);
//    						list = this.billOmDeliverExportManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
//    						for(BillOmDeliverExport vo:list){
//    							temp = new HashMap<String, Object>();
//    							temp.put("locno", vo.getLocno());
//    							temp.put("deliverNo", vo.getDeliverNo());
//    							temp.put("brandName", vo.getBrandName());
//    							temp.put("barcode", vo.getBarcode());
//    							temp.put("storeNo", vo.getStoreNo());
//    							temp.put("storeName", vo.getStoreName());
//    							temp.put("itemNo", vo.getItemNo());
//    							temp.put("itemName", vo.getItemName());
//    							temp.put("years", vo.getYears());
//    		    				temp.put("season", vo.getSeason());
//    		    				temp.put("gender", vo.getGender());
//    		    				temp.put("cateNo", vo.getCateNo());
//    		    				temp.put("yearsName", vo.getYearsName());
//    		    				temp.put("seasonName", vo.getSeasonName());
//    		    				temp.put("genderName", vo.getGenderName());
//    		    				temp.put("cateName", vo.getCateName());
//    							temp.put("total", vo.getTotal());
//    							temp.put("expDate", format.format(vo.getExpDate()));
//    							temp.put("expNo", vo.getExpNo());
//    							sizeNum = billOmDeliverExportManager.findDeliverDtlSizeNum(temp, authorityParams);
//    							if(sizeNum != null){
//    								for(Map<String, Object> sn:sizeNum){
//    									sizeKind = sn.get("SIZE_KIND").toString();
//    		    						sizeCode = sn.get("SIZE_CODE").toString();
//    		    						sizeKindList = sizeKindListMap.get(sizeKind);
//    		    						if(sizeKindList == null){
//    		    							List<String> kind =new ArrayList<String>();
//    		    							kind.add(sizeKind);
//    		    							sizeKindList=this.sizeInfoManager.selectSizeInfoBySizeNoList(kind, params.get("sysNo").toString(),null);
//    		    							sizeKindListMap.put(sizeKind, sizeKindList);
//    		    						}
//    		    						for(int idx=0;idx<sizeKindList.size();idx++){
//    		    							if(sizeKindList.get(idx).getSizeCode().equals(sizeCode)){
//    		    								sizeV = "v"+(idx+1);
//    		    								temp.put(sizeV, sn.get("QTY"));
//    		    								break;
//    		    							}
//    		    						}
//    		    						temp.put("sizeKind", sizeKind);
//    		    						////////////////////
//    		    						try {
//    		    							FooterUtil.setFooterMap(sizeV, new BigDecimal(sn.get("QTY").toString()), footer1Map);
//    		    							FooterUtil.setFooterMap("total", new BigDecimal(sn.get("QTY").toString()), footer1Map);
//    		    						} catch (Exception e) {
//    		    							log.error(e.getMessage(), e);
//    		    						}
//    		    						////////////////////
//    								}
//    							}
//    						}
//    					}
//    				}
    			}
    			obj.put("total", total);
    			obj.put("rows", rows);
    			obj.put("footer", footerList);
//    			return obj;
    		} else {
        		obj.put("total", total);
        		obj.put("rows", list);
        		obj.put("footer", footerList);
    		}

		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

    @SuppressWarnings({ "rawtypes", "deprecation", "unused", "unchecked" })
	@RequestMapping(value = "/do_export4Detail")
	public void doExportDetail(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info)
			throws ManagerException {
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map params = this.builderParams(req, model);
			
			// 设置查询参数
			BillOmDeliverExport rep = new BillOmDeliverExport();
			
			//大类一
			if (params.get("cateOne").toString() != null && params.get("cateOne").toString() != "") {
				String cateOne = params.get("cateOne").toString();
				String[] cateOneValues = null;
				if (StringUtils.isNotEmpty(cateOne)) {
					cateOneValues = cateOne.split(",");
				}
				rep.setCateOneValues(cateOneValues);
			}
			
			//大类二
			if (params.get("cateTwo").toString() != null && params.get("cateTwo").toString() != "") {
				String cateTwo = params.get("cateTwo").toString();
				String[] cateTwoValues = null;
				if (StringUtils.isNotEmpty(cateTwo)) {
					cateTwoValues = cateTwo.split(",");
				}
				rep.setCateTwoValues(cateTwoValues);
			}
			
			//大类三
			if (params.get("cateThree").toString() != null && params.get("cateThree").toString() != "") {
				String cateThree = params.get("cateThree").toString();
				String[] cateThreeValues = null;
				if (StringUtils.isNotEmpty(cateThree)) {
					cateThreeValues = cateThree.split(",");
				}
				rep.setCateThreeValues(cateThreeValues);
			}
			
			if (params.get("locno") != null && params.get("locno").toString() != "") {
				rep.setLocno(params.get("locno").toString());
			}
			if (params.get("sysNo") != null && params.get("sysNo").toString() != "") {
				rep.setSysNo(params.get("sysNo").toString());
			}
			if (params.get("brandNo") != null && params.get("brandNo").toString() != "") {
				rep.setBrandNo(params.get("brandNo").toString());
			}
			if (params.get("startExpDate") != null && params.get("startExpDate").toString() != "") {
				rep.setStartExpDate(params.get("startExpDate").toString());
			}
			if (params.get("endExpDate") != null && params.get("endExpDate").toString() != "") {
				rep.setEndExpDate(params.get("endExpDate").toString());
			}
			if (params.get("storeNo") != null && params.get("storeNo").toString() != "") {
				rep.setStoreNo(params.get("storeNo").toString());
			}
			if (params.get("itemNo") != null && params.get("itemNo").toString() != "") {
				rep.setItemNo(params.get("itemNo").toString().toUpperCase());
			}
			if (params.get("itemName") != null && params.get("itemName").toString() != "") {
				rep.setItemName(params.get("itemName").toString());
			}
			if (params.get("barcode") != null && params.get("barcode").toString() != "") {
				rep.setBarcode(params.get("barcode").toString());
			}
			if (params.get("expNo") != null && params.get("expNo").toString() != "") {
				rep.setExpNo(params.get("expNo").toString());
			}
			if (params.get("yearsName") != null && params.get("yearsName").toString() != "") {
				rep.setYearsName(params.get("yearsName").toString());
			}
			if (params.get("seasonName") != null && params.get("seasonName").toString() != "") {
				rep.setSeasonName(params.get("seasonName").toString());
			}
			if (params.get("genderName") != null && params.get("genderName").toString() != "") {
				rep.setGenderName(params.get("genderName").toString());
			}
			
			if(rep != null) {
				List<String> sizeKindList = billOmDeliverExportManager.findAllDtlSizeKind(params, authorityParams);
				if (sizeKindList == null || sizeKindList.size() == 0) {
					return;
				}
				String sysNo = rep.getSysNo();
//				
				HashMap returnMap = new HashMap();
//
				LinkedList returnList = new LinkedList();
//
				String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
						.getParameter("preColNames");
				String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
						.getParameter("endColNames");

				String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
						.getParameter("sizeTypeFiledName");

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

				LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
				List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(sizeKindList, sysNo, null);

				if (sizeTypeList != null && sizeTypeList.size() > 0) {
					for (SizeInfo vo : sizeTypeList) {
						String sizeTypeName = vo.getSizeKind();
						if (sizeTypeMap.containsKey(sizeTypeName)) {
							ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
							listA.add(vo.getSizeName()); //===========
							sizeTypeMap.put(sizeTypeName, listA);
						} else {
							ArrayList listA = new ArrayList();
							listA.add(vo.getSizeName());
							sizeTypeMap.put(sizeTypeName, listA);
						}
					}
				}

				int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
				if (sizeTypeMap != null) {
					java.util.Iterator it = sizeTypeMap.entrySet().iterator();
					while (it.hasNext()) {
						java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
						List tempList = (List) entry.getValue();
						if (maxSortCount < tempList.size()) {
							maxSortCount = tempList.size();
						}
					}
				}

				String fileName = (String) params.get("fileName");

				String dataRow = StringUtils.isEmpty(req.getParameter("dataRow")) ? "" : req.getParameter("dataRow");
				List<Map> dataRowList = new ArrayList<Map>();
				if (StringUtils.isNotEmpty(dataRow)) {
					dataRowList = mapper.readValue(dataRow, new TypeReference<List<Map>>() {
					});
				}

				response.setContentType("application/vnd.ms-excel");

				String fileName2 = new String(fileName.getBytes("gb2312"), "iso-8859-1");
				//文件名
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName2 + ".xls");
				response.setHeader("Pragma", "no-cache");
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet1 = wb.createSheet();
				//HSSFSheet  sheet2=wb.createSheet();
				//wb.setSheetName(1,"魏海金",HSSFWorkbook.ENCODING_UTF_16);
				//sheet名字
				wb.setSheetName(0, fileName);
				sheet1.setDefaultRowHeightInPoints(20);
				sheet1.setDefaultColumnWidth((short) 18);
				//设置页脚
				HSSFFooter footer = sheet1.getFooter();
				footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
				//设置样式 表头
				HSSFCellStyle style1 = wb.createCellStyle();
				style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont font1 = wb.createFont();
				font1.setFontHeightInPoints((short) 13);
				font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style1.setFont(font1);
				//设置样式 表头
				HSSFCellStyle style2 = wb.createCellStyle();
				style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				style2.setWrapText(true);

				//设置样式 表头
				HSSFCellStyle style3 = wb.createCellStyle();
				style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				HSSFFont font3 = wb.createFont();
				font3.setFontHeightInPoints((short) 18);
				font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style3.setFont(font3);
				style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
				HSSFRow row0 = sheet1.createRow(0);
				row0.setHeightInPoints(35);

				//第一行 提示长
				HSSFCell cell0 = row0.createCell((short) 0);
				cell0.setCellValue(fileName.toString());
				cell0.setCellStyle(style3);

				//真正开始============================================================================
				if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
					int typeSizeV = sizeTypeMap.size(); // 公共的类别数
					if (typeSizeV > 1) {
						int rowspan = typeSizeV - 2; // 合并的行========================
						int preColNamesV = preColNamesList.size(); // 前
						int endColNamesV = endColNamesList.size(); //后

						//合并
						Region rg1 = new Region(0, (short) 0, 0, (short) (maxSortCount + preColNamesV + endColNamesV));
						sheet1.addMergedRegion(rg1);

						if (rowspan >= 0 && rowspan != -1) {
							//①处理合并表头
							if (rowspan > 0) { // 大于2个的时候
								for (int i = 0; i < rowspan; i++) {
									int row = i + 1;
									HSSFRow row1 = sheet1.createRow(row);
									row1.setHeightInPoints(20);
									//1.前面合并
									if (i == 0) {
										HSSFCell cell1_0 = row1.createCell((short) 0);
										cell1_0.setCellValue("");
										Region rg11_cell1_0 = new Region(row, (short) 0, rowspan,
												(short) (preColNamesV - 1));
										sheet1.addMergedRegion(rg11_cell1_0);

									}

									//2.显示尺码
									int k = 0;
									for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
										if (k == i) {
											int diffCols = maxSortCount - (en.getValue().size());
											sheet1.autoSizeColumn((short) (preColNamesV));
											HSSFCell cell1_A = row1.createCell((short) preColNamesV);
											cell1_A.setCellValue(en.getKey() + "   ");

											for (int p = 0; p < en.getValue().size(); p++) {
												sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
												HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
												cell1_AG.setCellValue(en.getValue().get(p).toString());
											}

											if (diffCols > 0) {
												for (int m = 1; m <= diffCols; m++) {
													HSSFCell cell1_AGG = row1.createCell((short) (preColNamesV
															+ en.getValue().size() + m));
													cell1_AGG.setCellValue("      ");
												}
											}

											break;
										}
										k = k + 1;
									}

									//3.合并后头
									if (i == 0) {
										if (endColNamesV > 0) {
											HSSFCell cell1_E = row1.createCell((short) (preColNamesV + maxSortCount + 1));
											cell1_E.setCellValue("");
											Region rg11_cell1_0 = new Region(row,
													(short) (preColNamesV + maxSortCount + 1), rowspan,
													(short) (preColNamesV + maxSortCount + endColNamesV));
											sheet1.addMergedRegion(rg11_cell1_0);
										}
									}
								}
							}

							int mm = 1;
							//②处理业务表头
							for (int ii = 1; ii >= 0; ii--) {
								HSSFRow row1 = sheet1.createRow(rowspan + mm);
								row1.setHeightInPoints(20);
								//1.业务头
								if (ii == 1) {
									if (preColNamesList.size() > 0) {
										int nn = 0;
										for (JqueryDataGrid col : preColNamesList) {
											HSSFCell cell1_0 = row1.createCell((short) nn);
											cell1_0.setCellValue(col.getTitle());
											Region rg11_cell1_0 = new Region(rowspan + mm, (short) (nn), rowspan + mm + 1,
													(short) (nn));
											sheet1.addMergedRegion(rg11_cell1_0);
											sheet1.autoSizeColumn((short) (nn));
											nn = nn + 1;
										}
									}
								}

								//2.尺码	
								int k2 = 0;
								java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
								while (it2.hasNext()) {
									java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
											.next();

									if ((sizeTypeMap.size() - 1 - ii) == k2) {
										int diffCols2 = maxSortCount - (entry.getValue().size());

										HSSFCell cell1_A = row1.createCell((short) preColNamesV);
										sheet1.autoSizeColumn((short) (preColNamesV));
										cell1_A.setCellValue(entry.getKey() + "   ");

										for (int p = 0; p < entry.getValue().size(); p++) {
											sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
											HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
											cell1_AG.setCellValue(entry.getValue().get(p).toString());
										}
										if (diffCols2 > 0) {
											for (int m = 1; m <= diffCols2; m++) {
												HSSFCell cell1_AGG = row1.createCell((short) (preColNamesV
														+ entry.getValue().size() + m));
												cell1_AGG.setCellValue("      ");
											}
										}
										break;
									}
									k2 = k2 + 1;
								}

								//3.
								if (ii == 1) {
									if (endColNamesList.size() > 0) {
										int nn = 0;
										for (JqueryDataGrid col : endColNamesList) {
											HSSFCell cell1_0 = row1
													.createCell((short) (preColNamesV + maxSortCount + 1 + nn));
											cell1_0.setCellValue(col.getTitle());
											Region rg11_cell1_0 = new Region(rowspan + mm, (short) (preColNamesV
													+ maxSortCount + 1 + nn), rowspan + mm + 1, (short) (preColNamesV
													+ maxSortCount + 1 + nn));
											sheet1.addMergedRegion(rg11_cell1_0);
											sheet1.autoSizeColumn((short) (preColNamesV + maxSortCount + 1 + nn));
											nn = nn + 1;
										}
									}
								}

								mm = mm + 1;
							}
						}
					} else if (typeSizeV == 1) {
						int preColNamesV = preColNamesList.size(); // 前
						int endColNamesV = endColNamesList.size(); //后
						// 1.业务头 标题栏
						int start = 0;
						HSSFRow row1 = sheet1.createRow(0);
						if (preColNamesList.size() > 0) {

							for (JqueryDataGrid col : preColNamesList) {
								HSSFCell cell1_0 = row1.createCell((short) start);
								cell1_0.setCellValue(col.getTitle());
								if (sizeTypeMap.size() > 1) {
									Region rg11_cell1_0 = new Region(0, (short) start, 1, (short) start);
									sheet1.addMergedRegion(rg11_cell1_0);
								}
								start++;
							}
						}
						// 尺码横排栏
						//尺码横排最大的单元格个数
						int sizeNoNum = 0;
						java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
						while (it2.hasNext()) {
							java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
									.next();
							int diffCols2 = maxSortCount - (entry.getValue().size());

							HSSFCell cell1_A = row1.createCell((short) preColNamesV);
							sheet1.autoSizeColumn((short) (preColNamesV));
							cell1_A.setCellValue(entry.getKey() + "   ");

							for (int p = 0; p < entry.getValue().size(); p++) {
								sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
								HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
								cell1_AG.setCellValue(entry.getValue().get(p).toString());
							}
							if (diffCols2 > 0) {
								for (int m = 1; m <= diffCols2; m++) {
									HSSFCell cell1_AGG = row1
											.createCell((short) (preColNamesV + entry.getValue().size() + m));
									cell1_AGG.setCellValue("      ");
								}
							}

							if (entry.getValue().size() >= sizeNoNum) {
								sizeNoNum = entry.getValue().size();
							}
						}
						// 合计信息
						start = start + sizeNoNum + 1;
						if (endColNamesList.size() > 0) {
							row1.setHeightInPoints(20);
							for (JqueryDataGrid col : endColNamesList) {
								HSSFCell cell1_0 = row1.createCell((short) start);
								cell1_0.setCellValue(col.getTitle());
								if (sizeTypeMap.size() > 1) {
									Region rg11_cell1_0 = new Region(0, (short) start, 1, (short) start);
									sheet1.addMergedRegion(rg11_cell1_0);
								}
								start = start + 1;
							}
						}
					}
					if (dataRowList != null && dataRowList.size() > 0) {
						Map<String, Integer> sumMap = new HashMap<String, Integer>();
						int sumAllCount = 0;
						for (int v = 0; v < dataRowList.size(); v++) {
							Map map = dataRowList.get(v);
							if(map.get("total") != null) {
								sumAllCount = Integer.parseInt(map.get("total").toString()) + sumAllCount;
							}
							int startRow = 0;
							if (sizeTypeMap.size() == 1) {
								startRow = typeSizeV;
							} else {
								startRow = typeSizeV + 1;
							}
							HSSFRow rowD = sheet1.createRow(startRow + v);
							rowD.setHeightInPoints(20);

							if (preColNamesList.size() > 0) {
								for (int m = 0; m < preColNamesList.size(); m++) {
									HSSFCell cell1_0 = rowD.createCell((short) m);
									JqueryDataGrid col = preColNamesList.get(m);
									String colV = map.get(col.getField()) != null ? String.valueOf(map.get(col.getField()))
											: "";
									cell1_0.setCellValue(colV);
								}
							}

							HSSFCell cell1_00 = rowD.createCell((short) preColNamesList.size());
							String colV = map.get(sizeTypeFiledName) != null ? String.valueOf(map.get(sizeTypeFiledName))
									: "";
							cell1_00.setCellValue(colV);

							for (int vv = 0; vv < maxSortCount; vv++) {
								HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + vv));
								String vX = "v" + (vv + 1);
								String colVVV = map.get(vX) != null ? String.valueOf(map.get(vX)) : "";
								cell1_000.setCellValue(colVVV);
								String sumVx = "S" + vX;
								if (sumMap.get(sumVx) == null) {
									if ("".equals(colVVV)) {
										sumMap.put(sumVx, 0);
									} else {
										sumMap.put(sumVx, Integer.parseInt(colVVV));
									}
								} else {
									if (!"".equals(colVVV)) {
										sumMap.put(sumVx, sumMap.get(sumVx) + Integer.parseInt(colVVV));
									}
								}
							}

							if (endColNamesList.size() > 0) {
								for (int m = 0; m < endColNamesList.size(); m++) {
									HSSFCell cell1_0000 = rowD.createCell((short) (preColNamesList.size() + 1
											+ maxSortCount + m));
									JqueryDataGrid coll = endColNamesList.get(m);
									String colVVVV = map.get(coll.getField()) != null ? String.valueOf(map.get(coll
											.getField())) : "0";
									cell1_0000.setCellValue(colVVVV);
								}
							}

						}
						//增加合计
						HSSFRow rowD = sheet1.createRow(sheet1.getLastRowNum() + 1);
						rowD.setHeightInPoints(20);
						HSSFCell cell1_0 = rowD.createCell(0);
						cell1_0.setCellValue("小计");
						for (int vv = 0; vv < maxSortCount; vv++) {
							HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + vv));
							String vX = "Sv" + (vv + 1);
							Integer colVVV = sumMap.get(vX) != null ? sumMap.get(vX) : 0;
							String stringValue = colVVV == 0 ? "" : String.valueOf(colVVV);
							cell1_000.setCellValue(stringValue);
						}
						HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + maxSortCount));
						cell1_000.setCellValue(String.valueOf(sumAllCount));
					}
				}
				wb.write(response.getOutputStream());
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
    
//	@RequestMapping(value = "/doexport")
//	@OperationVerify(OperationVerifyEnum.EXPORT)
//	public void do_Export(HttpServletRequest req, Model model, HttpServletResponse response) {
//		try {
//			Map<String, Object> builderParams=builderParams(req, model);	
//		   	Map<String, Object> params = new HashMap<String, Object>();
//			params.putAll(builderParams);
//			String itemNo = "";
//			if(builderParams.get("itemNo") != null) {
//				itemNo = builderParams.get("itemNo").toString().toUpperCase();
//				params.put("itemNo", itemNo);
//			}
//			String barcode = "";
//			if(builderParams.get("barcode") != null) {
//				barcode = builderParams.get("barcode").toString().toUpperCase();
//				params.put("barcode", barcode);
//			}
//			     //参数处理
//			this.paramsHandle(params);
//			String exportColumns = (String) params.get("exportColumns");
//			String fileName = (String) params.get("fileName");
//			ObjectMapper mapper = new ObjectMapper();
//			if (StringUtils.isNotEmpty(exportColumns)) {
//				try {
//					AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
//					exportColumns = exportColumns.replace("[", "");
//					exportColumns = exportColumns.replace("]", "");
//					exportColumns = "[" + exportColumns + "]";
//
//					//字段名列表
//					List<Map> columnsList = mapper.readValue(exportColumns, new TypeReference<List<Map>>() {
//					});
//					//List<ModelType> list= this .manager .findByBiz(modelType, params);
//					int total = this.billOmDeliverExportManager.findCount(params);
//					String[] sizeCols = getSizeNo();
//					SimplePage page = new SimplePage(1, total, (int) total);
//					List<BillOmDeliverExport> list = this.billOmDeliverExportManager.findByPage(page, "", "", params,
//							authorityParams, DataAccessRuleEnum.BRAND);
//					List<Map> listArrayList = new ArrayList<Map>();
//					if (list != null && list.size() > 0) {
//						List<Map<String, Object>> sizeNum = null;
//						for (BillOmDeliverExport vo : list) {
//							Map map = new HashMap();
//							BeanUtilsCommon.object2MapWithoutNull(vo, map);
//							sizeNum = billOmDeliverExportManager.findDeliverDtlSizeNum(map, authorityParams);
//							for (String sc : sizeCols) {
//								map.put(sc, "  ");
//							}
//							if (sizeNum != null) {
//								for (Map<String, Object> sn : sizeNum) {
//
//									for (Map columns : columnsList) {
//										if (columns.get("title").equals(sn.get("SIZE_CODE"))) {
//											map.put(columns.get("field"), sn.get("QTY"));
//											break;
//										}
//									}
//
//								}
//							}
//
//							listArrayList.add(map);
//
//						}
//						HSSFExport.commonExportData(StringUtils.isNotEmpty(fileName) ? fileName : "导出信息", columnsList,
//								listArrayList, response);
//					}
//				} catch (Exception e) {
//					log.error(e.getMessage(), e);
//				}
//
//			} else {
//
//			}
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//
//	}
    public String[] getSizeNo(){
    	try {
    		List<Map<String, Object>> list = billOmDeliverExportManager.findDeliverDtlSize(new HashMap<String, Object>());
        	if(list != null){
        		String [] array = new String[list.size()];
        		int i = 0;
        		for(Map<String, Object> map:list){
        			array[i++] = map.get("SIZE_NO").toString();
        		}
        		return array;
        	}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    	return new String[0];
    }
 // 获取尺码横排头部信息
 	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getBrandList(String preColNames, String endColNames, String sizeTypeFiledName, String sysNo,
 			List<String> list) throws ManagerException {
 		HashMap returnMap = new HashMap();

 		LinkedList returnList = new LinkedList();
 		ObjectMapper mapper = new ObjectMapper();
 		List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
 		List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();

 		try {
 			if (StringUtils.isNotEmpty(preColNames)) {
 				preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
 				});
 			}

 			if (StringUtils.isNotEmpty(endColNames)) {
 				endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
 				});
 			}

 		} catch (Exception e) {
 			log.error(e.getMessage(), e);
 			throw new ManagerException(e);
 		}

 		// 尺码类型 A-ArrayList<SizeInfo>
 		LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
 		/*
 		 * Map<String, Object> tempParams = new HashMap<String, Object>();
 		 * tempParams.put("sysNo", sysNo); tempParams.put("preColNames",
 		 * preColNames); tempParams.put("endColNames", endColNames);
 		 * tempParams.put("sizeTypeFiledName", sizeTypeFiledName);
 		 */

 		// 查询尺码对应的尺码类别
 		List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(list, sysNo,null);
 		/*
 		 * List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null,
 		 * tempParams);
 		 */
 		if (sizeTypeList != null && sizeTypeList.size() > 0) {
 			for (SizeInfo vo : sizeTypeList) {
 				String sizeTypeName = vo.getSizeKind();
 				if (sizeTypeMap.containsKey(sizeTypeName)) {
 					ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
 					listA.add(vo.getSizeName()); // ===========
 					sizeTypeMap.put(sizeTypeName, listA);
 				} else {
 					ArrayList listA = new ArrayList();
 					listA.add(vo.getSizeName());
 					sizeTypeMap.put(sizeTypeName, listA);
 				}
 			}
 		}

 		int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
 		if (sizeTypeMap != null) {
 			java.util.Iterator it = sizeTypeMap.entrySet().iterator();
 			while (it.hasNext()) {
 				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
 				List tempList = (List) entry.getValue();
 				if (maxSortCount < tempList.size()) {
 					maxSortCount = tempList.size();
 				}
 			}
 		}

 		// ====开始处理====
 		Editor defaultEditor = new Editor();
 		defaultEditor.setType("validatebox");
 		if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
 			int typeSizeV = sizeTypeMap.size(); // 公共的类别数
 			if (typeSizeV > 1) { // 多于一个尺码类别
 				int rowspan = typeSizeV - 2; // 合并的行
 				int preColNamesV = preColNamesList.size(); // 前
 				int endColNamesV = endColNamesList.size(); // 后
 				if (rowspan >= 0 && rowspan != -1) {
 					// ①处理合并表头
 					if (rowspan > 0) { // 大于2个的时候
 						for (int i = 0; i < rowspan; i++) {
 							LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
 							// 1.前面合并
 							if (i == 0) {
 								JqueryDataGrid v = new JqueryDataGrid();
 								v.setTitle("");
 								v.setWidth(80);
 								v.setEditor(defaultEditor);
 								v.setRowspan(rowspan);
 								v.setColspan(preColNamesV);
 								v.setAlign("left");
 								colList.add(v);
 							}
 							// 2.显示尺码
 							int k = 0;
 							for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
 								if (k == i) {
 									int diffCols = maxSortCount - (en.getValue().size());
 									JqueryDataGrid v = new JqueryDataGrid();
 									v.setTitle(en.getKey());
 									v.setWidth(50);
 									v.setEditor(defaultEditor);
 									v.setAlign("left");
 									colList.add(v);

 									for (int p = 0; p < en.getValue().size(); p++) {
 										JqueryDataGrid v1 = new JqueryDataGrid();
 										v1.setTitle(en.getValue().get(p).toString());
 										v1.setWidth(50);
 										v1.setEditor(defaultEditor);
 										v1.setAlign("left");
 										colList.add(v1);
 									}
 									if (diffCols > 0) {
 										for (int m = 1; m <= diffCols; m++) {
 											JqueryDataGrid v1 = new JqueryDataGrid();
 											v1.setTitle("");
 											v1.setWidth(50);
 											v1.setEditor(defaultEditor);
 											v1.setAlign("left");
 											colList.add(v1);
 										}
 									}

 									break;
 								}
 								k = k + 1;
 							}
 							// 3.合并后头
 							if (i == 0) {
 								if (endColNamesV > 0) {
 									JqueryDataGrid v = new JqueryDataGrid();
 									v.setTitle("");
 									v.setWidth(80);
 									v.setEditor(defaultEditor);
 									v.setRowspan(rowspan);
 									v.setColspan(endColNamesV);
 									v.setAlign("left");
 									colList.add(v);
 								}
 							}
 							returnList.add(colList);
 						}
 					}

 					// ②处理业务表头
 					for (int ii = 1; ii >= 0; ii--) {
 						LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
 						// 1.业务头
 						if (ii == 1) {
 							if (preColNamesList.size() > 0) {
 								for (JqueryDataGrid col : preColNamesList) {
 									JqueryDataGrid v = new JqueryDataGrid();
 									v.setField(col.getField());
 									v.setTitle(col.getTitle());
 									v.setWidth(SysConstans.WIDTH_80);
 									if (col.getWidth() != 0) {
 										v.setWidth(col.getWidth());
 									}

 									v.setEditor(defaultEditor);
 									if (col.getEditor() != null) {
 										if (!CommonUtil.hasValue(col.getEditor().getType())) {
 											col.getEditor().setType(defaultEditor.getType());
 										}
 										v.setEditor(col.getEditor());
 									}

 									v.setRowspan(2);
 									v.setAlign("left");
 									colList.add(v);
 								}
 							}
 						}
 						// 2.尺码
 						int k2 = 0;
 						java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
 						while (it2.hasNext()) {
 							java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
 									.next();

 							if ((sizeTypeMap.size() - 1 - ii) == k2) {

 								int diffCols2 = maxSortCount - (entry.getValue().size());
 								// <#-- 这里做判断的原因是为了防止重复写 field: ,上面是List循环的 -->
 								JqueryDataGrid v = new JqueryDataGrid();
 								v.setTitle(entry.getKey());
 								v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
 								v.setEditor(defaultEditor);
 								v.setAlign("left");
 								if (ii == 1) {
 									v.setField(sizeTypeFiledName);
 									v.setAlign("center");
 								}
 								colList.add(v);

 								for (int p = 0; p < entry.getValue().size(); p++) {
 									JqueryDataGrid v1 = new JqueryDataGrid();
 									v1.setTitle(entry.getValue().get(p).toString());
 									v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
 									v1.setEditor(defaultEditor);
 									v1.setAlign("left");
 									if (ii == 1) {
 										v1.setField("v" + (p + 1));
 										v1.setAlign("center");
 									}
 									colList.add(v1);
 								}

 								if (diffCols2 > 0) {
 									for (int m = 1; m <= diffCols2; m++) {
 										JqueryDataGrid v1 = new JqueryDataGrid();
 										v1.setTitle("");
 										v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
 										v1.setEditor(defaultEditor);
 										v1.setAlign("left");
 										if (ii == 1) {
 											v1.setField("v" + (entry.getValue().size() + m));
 											v1.setAlign("center");
 										}
 										colList.add(v1);
 									}
 								}

 								break;
 							}
 							k2 = k2 + 1;
 						}
 						// 3.
 						if (ii == 1) {
 							if (endColNamesList.size() > 0) {
 								for (JqueryDataGrid col : endColNamesList) {
 									JqueryDataGrid v = new JqueryDataGrid();
 									v.setField(col.getField());
 									v.setTitle(col.getTitle());
 									v.setWidth(SysConstans.WIDTH_80);
 									if (col.getWidth() != 0) {
 										v.setWidth(col.getWidth());
 									}
 									v.setEditor(defaultEditor);
 									if (col.getEditor() != null) {
 										if (!CommonUtil.hasValue(col.getEditor().getType())) {
 											col.getEditor().setType(defaultEditor.getType());
 										}
 										v.setEditor(col.getEditor());
 									}
 									v.setRowspan(2);
 									v.setAlign("left");
 									colList.add(v);
 								}
 							}
 						}

 						returnList.add(colList);

 					}
 				}
 			} else if (typeSizeV == 1) {// 只有一个尺码类别
 				LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
 				// 1.业务头 标题栏
 				if (preColNamesList.size() > 0) {
 					for (JqueryDataGrid col : preColNamesList) {
 						JqueryDataGrid v = new JqueryDataGrid();
 						v.setField(col.getField());
 						v.setTitle(col.getTitle());
 						v.setWidth(SysConstans.WIDTH_80);
 						if (col.getWidth() != 0) {
 							v.setWidth(col.getWidth());
 						}

 						v.setEditor(defaultEditor);
 						if (col.getEditor() != null) {
 							if (!CommonUtil.hasValue(col.getEditor().getType())) {
 								col.getEditor().setType(defaultEditor.getType());
 							}
 							v.setEditor(col.getEditor());
 						}

 						v.setRowspan(1);
 						v.setAlign("left");
 						colList.add(v);
 					}
 				}
 				// 尺码横排栏
 				java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
 				while (it2.hasNext()) {
 					java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2.next();
 					// 尺码类别
 					JqueryDataGrid v = new JqueryDataGrid();
 					v.setTitle(entry.getKey());
 					v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
 					v.setEditor(defaultEditor);
 					v.setAlign("left");
 					v.setField(sizeTypeFiledName);
 					v.setAlign("center");
 					colList.add(v);

 					for (int p = 0; p < entry.getValue().size(); p++) {
 						JqueryDataGrid v1 = new JqueryDataGrid();
 						v1.setTitle(entry.getValue().get(p).toString());
 						v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
 						v1.setEditor(defaultEditor);
 						v1.setAlign("left");
 						v1.setField("v" + (p + 1));
 						v1.setAlign("center");
 						colList.add(v1);
 					}
 				}
 				// 合计信息
 				if (endColNamesList.size() > 0) {
 					for (JqueryDataGrid col : endColNamesList) {
 						JqueryDataGrid vend = new JqueryDataGrid();
 						vend.setField(col.getField());
 						vend.setTitle(col.getTitle());
 						vend.setWidth(SysConstans.WIDTH_80);
 						if (col.getWidth() != 0) {
 							vend.setWidth(col.getWidth());
 						}
 						vend.setEditor(defaultEditor);
 						if (col.getEditor() != null) {
 							if (!CommonUtil.hasValue(col.getEditor().getType())) {
 								col.getEditor().setType(defaultEditor.getType());
 							}
 							vend.setEditor(col.getEditor());
 						}
 						vend.setRowspan(1);
 						vend.setAlign("left");
 						colList.add(vend);
 					}
 				}
 				returnList.add(colList);
 			}
 		}

 		// ====开始处理====

 		returnMap.put("returnCols", returnList);
 		returnMap.put("maxType", maxSortCount);
 		returnMap.put("startType", preColNamesList.size() + 1);
 		return returnMap;
 	}
 	
 	
    /**
	 * 参数处理
	 * @param params
	 */
	private void paramsHandle(Map<String, Object> params) {
//		//品牌
//		String brandNo = (String) params.get("brandNo");
//		String[] brandNoValues = null;
//		if (StringUtils.isNotEmpty(brandNo)) {
//			brandNoValues = brandNo.split(",");
//		}
//		params.put("brandNoValues", brandNoValues);
		
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
		
//
//		//品质
//		String quality = (String) params.get("quality");
//		String[] qualityValues = null;
//		if (StringUtils.isNotEmpty(quality)) {
//			qualityValues = quality.split(",");
//		}
//		params.put("qualityValues", qualityValues);
	}
	
	@RequestMapping(value = "/do_export3")
	public void doExport3(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info) throws IOException{
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");
		String fileName = StringUtils.isEmpty(req.getParameter("fileName")) ? "" : req
				.getParameter("fileName");

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
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map params = this.builderParams(req, model);
			
			// 设置查询参数
			BillOmDeliverExport rep = new BillOmDeliverExport();
			
			//大类一
			if (params.get("cateOne").toString() != null && params.get("cateOne").toString() != "") {
				String cateOne = params.get("cateOne").toString();
				String[] cateOneValues = null;
				if (StringUtils.isNotEmpty(cateOne)) {
					cateOneValues = cateOne.split(",");
				}
				rep.setCateOneValues(cateOneValues);
			}
			
			//大类二
			if (params.get("cateTwo").toString() != null && params.get("cateTwo").toString() != "") {
				String cateTwo = params.get("cateTwo").toString();
				String[] cateTwoValues = null;
				if (StringUtils.isNotEmpty(cateTwo)) {
					cateTwoValues = cateTwo.split(",");
				}
				rep.setCateTwoValues(cateTwoValues);
			}
			
			//大类三
			if (params.get("cateThree").toString() != null && params.get("cateThree").toString() != "") {
				String cateThree = params.get("cateThree").toString();
				String[] cateThreeValues = null;
				if (StringUtils.isNotEmpty(cateThree)) {
					cateThreeValues = cateThree.split(",");
				}
				rep.setCateThreeValues(cateThreeValues);
			}
			
			if (params.get("locno") != null && params.get("locno").toString() != "") {
				rep.setLocno(params.get("locno").toString());
			}
			if (params.get("sysNo") != null && params.get("sysNo").toString() != "") {
				rep.setSysNo(params.get("sysNo").toString());
			}
			if (params.get("brandNo") != null && params.get("brandNo").toString() != "") {
				rep.setBrandNo(params.get("brandNo").toString());
			}
			if (params.get("startExpDate") != null && params.get("startExpDate").toString() != "") {
				rep.setStartExpDate(params.get("startExpDate").toString());
			}
			if (params.get("endExpDate") != null && params.get("endExpDate").toString() != "") {
				rep.setEndExpDate(params.get("endExpDate").toString());
			}
			if (params.get("storeNo") != null && params.get("storeNo").toString() != "") {
				rep.setStoreNo(params.get("storeNo").toString());
			}
			if (params.get("itemNo") != null && params.get("itemNo").toString() != "") {
				rep.setItemNo(params.get("itemNo").toString().toUpperCase());
			}
			if (params.get("itemName") != null && params.get("itemName").toString() != "") {
				rep.setItemName(params.get("itemName").toString());
			}
			if (params.get("barcode") != null && params.get("barcode").toString() != "") {
				rep.setBarcode(params.get("barcode").toString());
			}
			if (params.get("expNo") != null && params.get("expNo").toString() != "") {
				rep.setExpNo(params.get("expNo").toString());
			}
			if (params.get("yearsName") != null && params.get("yearsName").toString() != "") {
				rep.setYearsName(params.get("yearsName").toString());
			}
			if (params.get("seasonName") != null && params.get("seasonName").toString() != "") {
				rep.setSeasonName(params.get("seasonName").toString());
			}
			if (params.get("genderName") != null && params.get("genderName").toString() != "") {
				rep.setGenderName(params.get("genderName").toString());
			}
			
			//性别
			String [] seasonValues = null;
			if (params.get("seasonName") != null && params.get("seasonName").toString() != "") {
				String season = params.get("seasonName").toString();
				seasonValues = season.split(",");
			}
			//季节
			String [] genderValues = null;
			if (params.get("genderName") != null && params.get("genderName").toString() != "") {
				String gender = params.get("genderName").toString();
				genderValues = gender.split(",");
			}
			if(seasonValues != null) {
				rep.setSeasonValues(seasonValues);
			}
			if(genderValues != null) {
				rep.setGenderValues(genderValues);
			}
    		
			if(rep.getSysNo() != null) {
				Map<String,Object> obj=new HashMap<String,Object>();
				obj = billOmDeliverExportManager.findBillOmDeliverExportByPage(rep, authorityParams, true);
				
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
				List<BillOmDeliverExport> data = (List<BillOmDeliverExport>) obj.get("rows");
				HSSFWorkbook wb = ExcelUtils.getExcle4Size(preColNamesList, sizeTypeMap, endColNamesList, fileName, data, true);
				
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
				response.setHeader("Pragma", "no-cache");
				wb.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		}
	}
}