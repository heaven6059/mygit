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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillSmOtherinDtlDto;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillSmOtherinDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
@RequestMapping("/bill_sm_otherin_dtl")
public class BillSmOtherinDtlController<ModelType> extends BaseCrudController<BillSmOtherinDtlDto> {
	@Log
	private Logger log;
	@Resource
	private SizeInfoManager sizeInfoManager;
	
    @Resource
    private BillSmOtherinDtlManager billSmOtherinDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billsmotherindtl/",billSmOtherinDtlManager);
    }
    
    @RequestMapping(value = "/dtl_List.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			
			int total = billSmOtherinDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherinDtl> dtlList = 
					billSmOtherinDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", dtlList);
			
			//小计
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalInstorageQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalNoRecheckQty = new BigDecimal(0);
			for(BillSmOtherinDtl dtl:dtlList){
				totalInstorageQty = totalInstorageQty.add(dtl.getInstorageQty());
				totalRecheckQty = totalRecheckQty.add(dtl.getRecheckQty());
				totalNoRecheckQty = totalNoRecheckQty.add(dtl.getNoRecheckQty()==null?new BigDecimal(0):dtl.getNoRecheckQty());
			}
			footer.put("itemNo", "小计");
			footer.put("instorageQty", totalInstorageQty);
			footer.put("recheckQty", totalRecheckQty);
			footer.put("noRecheckQty", totalNoRecheckQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
			    sumFoot = billSmOtherinDtlManager.findSumQty(params,authorityParams);
			    if (sumFoot == null) {
				sumFoot = new SumUtilMap<String, Object>();
				sumFoot.put("instorage_qty", 0);
				sumFoot.put("recheck_qty", 0);
				sumFoot.put("no_recheck_qty", 0);
			    }
			    sumFoot.put("isselectsum", true);
			    sumFoot.put("item_no", "合计");
			} else {
			    sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);		

			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}	
		return obj;
	}
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/export_OtherinNo")
	public void doExportOtherinNo(HttpServletRequest req,Model model,HttpServletResponse response)throws ManagerException{
    	AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req); 
    	Map<String,Object> params=builderParams(req, model);
         String exportColumns=(String) params.get( "exportColumns");
         String fileName=(String)params.get( "fileName");
         ObjectMapper mapper = new ObjectMapper();
          if (StringUtils.isNotEmpty(exportColumns)){
                try {
                     exportColumns=exportColumns.replace( "[" ,"" );
                     exportColumns=exportColumns.replace( "]" ,"" );
                     exportColumns= "[" +exportColumns+"]" ;
                     
                      //字段名列表
                     List<Map> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<Map>>(){});
                     
                     int total = billSmOtherinDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
             		SimplePage page = new SimplePage(1, total, (int) total);
             		List<BillSmOtherinDtl> dtlList = billSmOtherinDtlManager.findByPage(page, "", "", params,authorityParams, DataAccessRuleEnum.BRAND);
                     List<Map> listArrayList= new ArrayList< Map>();
                      if (dtlList!= null&&dtlList.size()>0){
                         for (BillSmOtherinDtl vo:dtlList){
                            Map map= new HashMap();
                               BeanUtilsCommon.object2MapWithoutNull(vo,map);
                               listArrayList.add(map);
                                 
                         }
                        HSSFExport.commonExportData(StringUtils.isNotEmpty(fileName)?fileName:"导出信息",ColumnsList,listArrayList, response);
                     }
               } catch (Exception e) {
                     e.printStackTrace();
               }
               

         } else {
                
         }
		
	}
    /**
	 * 查询库存表
	 */
	@RequestMapping(value = "/get_Content")
	@ResponseBody
	public Map<String, Object> getBiz(HttpServletRequest req, Model model){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
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
			int total = this.billSmOtherinDtlManager.selectContentCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherinDtlDto> list = this.billSmOtherinDtlManager.selectContent(page, sortColumn, sortOrder, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}	
		return obj;
	}
	
	/**
   	 * 新增和删除其它入库明细
   	 */
   	@SuppressWarnings("rawtypes")
   	@RequestMapping(value = "/addSmOtherinDtl")
   	public ResponseEntity<Map<String, Object>> addDtl(HttpServletRequest req,BillSmOtherin BillSmOtherin) {
   		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			if(null!=BillSmOtherin){
				String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
				String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
				String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
				
				ObjectMapper mapper = new ObjectMapper();
				Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
				if (StringUtils.isNotBlank(deletedList)) {
					List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
					List<ModelType> oList=convertListWithTypeReference(mapper,list);
					params.put(CommonOperatorEnum.DELETED, oList);
				}
				if (StringUtils.isNotBlank(upadtedList)) {
					List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>(){});
					List<ModelType> oList=convertListWithTypeReference(mapper,list);
					params.put(CommonOperatorEnum.UPDATED, oList);
				}
				if (StringUtils.isNotBlank(insertedList)) {
					List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
					List<ModelType> oList=convertListWithTypeReference(mapper,list);
					params.put(CommonOperatorEnum.INSERTED, oList);
				}
				flag = billSmOtherinDtlManager.addSmOtherinDtl(BillSmOtherin, params);
			}
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			
		}catch (ManagerException e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}catch (Exception e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   	}
   	
   	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<ModelType> tl=new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
	@RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 FileUtils.downloadTemple(session, response, "otherinTemplate.xls");
	}
	
	@RequestMapping("/iframe")
	public ModelAndView iframe(String otherinNo,String ownerNo) throws Exception {
		ModelAndView mode = new ModelAndView("billsmotherin/iframe");
		mode.addObject("otherinNo", otherinNo);
		mode.addObject("ownerNo", ownerNo);
		return mode;
	}
	
	@RequestMapping(value = "/import")
	public ModelAndView upLoad(HttpServletRequest request,Model model,String otherinNo,String ownerNo) {
		ModelAndView mode = new ModelAndView("billsmotherin/iframe");
		mode.addObject("otherinNo", otherinNo);
		mode.addObject("ownerNo", ownerNo);
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(request);
		SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
	    try{
	    	String [] colNames = {"itemNo","sizeNo","cellNo","instorageQty"};
	    	boolean [] mustArray = {true,true,true,true};
	    	List<BillSmOtherinDtl> list= ExcelUtils.getData(request, 0, 1, colNames,mustArray, null, BillSmOtherinDtl.class);
	    	if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 this.billSmOtherinDtlManager.excelImportData(list, user.getLocNo(), otherinNo,ownerNo,authorityParams);
			 mode.addObject("result", ResultEnums.SUCCESS.getResultMsg());
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mode.addObject("result", ResultEnums.FAIL.getResultMsg());
			mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
		}
		return mode;
	}
	
	@RequestMapping(value = "/dtlExport")
	public void export(BillSmOtherinDtl modelType, HttpServletRequest req, Model model, HttpServletResponse response)throws ManagerException{
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req.getParameter("endColNames");
		String fileName = StringUtils.isEmpty(req.getParameter("fileName")) ? "" : req.getParameter("fileName");
		String locno = StringUtils.isEmpty(req.getParameter("locno")) ? "" : req.getParameter("locno");
		String ownerNo = StringUtils.isEmpty(req.getParameter("ownerNo")) ? "" : req.getParameter("ownerNo");
		String otherinNo = StringUtils.isEmpty(req.getParameter("otherinNo")) ? "" : req.getParameter("otherinNo");
		
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
			BillSmOtherinDtl dtl = new BillSmOtherinDtl();
			dtl.setLocno(locno);
			dtl.setOwnerNo(ownerNo);
			dtl.setOtherinNo(otherinNo);
			
			List<BillSmOtherinDtl> sysNoList = billSmOtherinDtlManager.findDtlSysNo(dtl, authorityParams);
			HSSFWorkbook wb = null;
			if(sysNoList != null && sysNoList.size() > 0) {
				for(int i=0; i < sysNoList.size(); i++){
					BillSmOtherinDtl dd = sysNoList.get(i);
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
					obj = billSmOtherinDtlManager.findDtlSysNoByPage(dtl, authorityParams);
					List<BillSmOtherinDtl> data = (List<BillSmOtherinDtl>) obj.get("rows");
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
	
}