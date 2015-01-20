package com.yougou.logistics.city.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDtlDto;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillWmRecedeDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 退厂通知单明细
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
 * @version 1.0.0
 * @param <ModelType>
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
@RequestMapping("/wmrecededtl")
public class BillWmRecedeDtlController<ModelType> extends BaseCrudController<BillWmRecedeDtl> {
	
    @Log
    private Logger log;
    
    @Resource
    private BillWmRecedeDtlManager billWmRecedeDtlManager;
    
	@Resource
	private SizeInfoManager sizeInfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("wmrecededtl/",billWmRecedeDtlManager);
    }
    
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billWmRecedeDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherin> list = this.billWmRecedeDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(),e);
		}
		return obj;
	}
    
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/save")
	public ResponseEntity<Map<String, Boolean>> save(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		Map<String, Boolean> flag = new HashMap<String, Boolean>();
		boolean isSuccess = false;
		try{
			String locno = req.getParameter("locno");
			String ownerNo = req.getParameter("ownerNo");
			String recedeNo = req.getParameter("recedeNo");
			if(StringUtils.isNotBlank(locno) && StringUtils.isNotBlank(ownerNo)
					&& StringUtils.isNotBlank(recedeNo) ){
				//获取登陆用户
				HttpSession session = req.getSession();
			    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				
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
				
				isSuccess = billWmRecedeDtlManager.addWmRecedeDtl(locno, ownerNo, recedeNo,  params, user.getLoginName(),user.getUsername());
			}
			flag.put("success", isSuccess);
			return new ResponseEntity<Map<String, Boolean>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("===========新增和删除出库订单明细时异常："+e.getMessage(),e);
			flag.put("success", false);
			return new ResponseEntity<Map<String, Boolean>>(flag, HttpStatus.OK);
		}
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
	
	
	@RequestMapping(value = "/queryBillWmRecedeDtlDTOlList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> queryBillWmRecedeDtlDTOlList(String recedeNo, HttpServletRequest req,
			Model model) throws ManagerException {
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int total = 0;
			//返回参数列表
			List<BillWmRecedeDtlDto> returnDtoList = new ArrayList<BillWmRecedeDtlDto>();
			//返回 Map集合
			Map<String, Object> obj = new HashMap<String, Object>();

			obj.put("total", total);
			obj.put("rows", returnDtoList);
			//返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "汇总");
			footerList.add(footerMap);
			obj.put("footer", footerList);
			ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(obj,
					HttpStatus.OK);
			if (StringUtils.isEmpty(recedeNo)) {
				return responseEntity;
			}

			//查询盘差单详情
			BillWmRecedeDtlDto dtoParam = new BillWmRecedeDtlDto();
			dtoParam.setRecedeNo(recedeNo);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			total = billWmRecedeDtlManager.selectCountMx(dtoParam,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);

			List<BillWmRecedeDtlDto> listTempGroup = billWmRecedeDtlManager.queryBillWmRecedeDtlDtoGroupBy(
					page, dtoParam,authorityParams);
			if (CollectionUtils.isEmpty(listTempGroup)) {
				return responseEntity;
			}

			for (BillWmRecedeDtlDto gvo : listTempGroup) {

				if (CommonUtil.hasValue(gvo.getItemNo())) {
					dtoParam.setItemNo(gvo.getItemNo());

					List<BillWmRecedeDtlDto> listTempMxList = billWmRecedeDtlManager
							.queryBillWmRecedeDtlDtoByExpNo(dtoParam);
					BigDecimal allCounts = new BigDecimal(0);
					if (CollectionUtils.isEmpty(listTempMxList)) {
						continue;
					}

					BillWmRecedeDtlDto dto = listTempMxList.get(0);
					SizeInfo sizeInfoParamInfo = new SizeInfo();
					Map<String, Object> mapParaMap = new HashMap<String, Object>();
					mapParaMap.put("sysNo", dto.getSysNo());
					mapParaMap.put("sizeKind", dto.getSizeKind());
					List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
					for (BillWmRecedeDtlDto temp : listTempMxList) {
						for (int i = 0; i < sizeInfoList.size(); i++) {
							SizeInfo sizeInfo = sizeInfoList.get(i);
							if (temp.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对
								Object[] arg = new Object[] { temp.getRecedeQty().toString() };
								String filedName = "setV" + (i + 1);
								CommonUtil.invokeMethod(dto, filedName, arg);
								allCounts = allCounts.add(temp.getRecedeQty());
								
								////////////////////
								this.setFooterMap("v" + (i + 1), temp.getRecedeQty(), footerMap);
								this.setFooterMap("allCount", temp.getRecedeQty(), footerMap);
								////////////////////
								break;
							}
						}
					}
					dto.setAllCount(allCounts);
					dto.setAllCost(new BigDecimal(0));
					returnDtoList.add(dto);
				}
			}
			obj.put("total", total);
			obj.put("rows", returnDtoList);
			return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}
	}
	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}
	/**
   	 * 查询库存表
   	 */
   	@RequestMapping(value = "/findItemAndSize")
   	@ResponseBody
   	public Map<String, Object> getBiz(HttpServletRequest req, Model model) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String, Object>();
   		try{
   			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
   			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
   	   		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
   	   		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
   	   		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
   	   		Map<String, Object> params = builderParams(req, model);
   	   		int total = this.billWmRecedeDtlManager.selectItemCount(params,authorityParams);
   	   		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
   	   		List<BillWmRecedeDtl> list = this.billWmRecedeDtlManager.selectItem(page, sortColumn, sortOrder, params,authorityParams);
   	   		
   	   		obj.put("total", total);
   	   		obj.put("rows", list);
   		}catch(Exception e){
   			log.error(e.getMessage(), e);
   		}
   		return obj;
   	}
   	
   	/**
   	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
   	 */
   	@RequestMapping(value = "/findItemAndSizeTest")
   	@ResponseBody
   	public Map<String, Object> getBizTest(HttpServletRequest req, Model model) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String, Object>();
   		try{
   			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
   			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
   	   		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
   	   		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
   	   		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
   	   		Map<String, Object> params = builderParams(req, model);
   	   		int total = this.billWmRecedeDtlManager.selectItemCountTest(params,authorityParams);
   	   		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
   	   		List<BillWmRecedeDtl> list = this.billWmRecedeDtlManager.selectItemTest(page, sortColumn, sortOrder, params,authorityParams);
   	   		
   	   		obj.put("total", total);
   	   		obj.put("rows", list);
   		}catch(Exception e){
   			log.error(e.getMessage(), e);
   		}
   		return obj;
   	}
   	
   	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String,Object>(0);
   		try{
   			obj = queryList(req, model);
   			List<Object> footerList = new ArrayList<Object>();
   			Map<String, Object> footer = new HashMap<String, Object>();
   			BigDecimal totalRecedeQty = new BigDecimal(0);
   			List<BillWmRecedeDtl> list = CommonUtil.getRowsByListJson(obj, BillWmRecedeDtl.class);
   			for(BillWmRecedeDtl dtl:list){
   				totalRecedeQty = totalRecedeQty.add(dtl.getRecedeQty());
   			}
   			footer.put("itemNo", "汇总");
   			footer.put("recedeQty", totalRecedeQty);
   			footerList.add(footer);
   			obj.put("footer", footerList);
   		}catch(Exception e){
   			log.error(e.getMessage(), e);
   		}
		return obj;
	}
   	
   	
   	/**
   	 * 查询库存表
   	 */
   	@RequestMapping(value = "/findWmRecedeDtlDispatchByPage")
   	@ResponseBody
   	public Map<String, Object> findWmRecedeDtlDispatchByPage(HttpServletRequest req, Model model) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String, Object>();
   		try{
   			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
   			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
   	   		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
   	   		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
   	   		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
   	   		Map<String, Object> params = builderParams(req, model);
   	   		int total = this.billWmRecedeDtlManager.findWmRecedeDtlDispatchCount(params,authorityParams);
   	   		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
   	   		List<BillWmRecedeDispatchDtlDTO> list = this.billWmRecedeDtlManager.findWmRecedeDtlDispatchByPage(page, sortColumn, sortOrder, params,authorityParams);
   	   		
   	   		
   	   		// 返回汇总列表
   		    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
   		    Map<String, Object> footerMap = new HashMap<String, Object>();
   		    footerMap.put("itemNo", "小计");
   		    footerList.add(footerMap);
   		    for (BillWmRecedeDispatchDtlDTO temp : list) {
   		    	FooterUtil.setFooterMapByInt("recedeQty", temp.getRecedeQty(),footerMap);
   		    	FooterUtil.setFooterMapByInt("differenceQty",temp.getDifferenceQty(), footerMap);
   		    	FooterUtil.setFooterMapByInt("noenoughQty",temp.getNoenoughQty(), footerMap);
   		    	FooterUtil.setFooterMapByInt("usableQty", temp.getUsableQty(), footerMap);
   			}
   		    
   			// 合计
   			Map<String, Object> sumFoot = new HashMap<String, Object>();
   			if (pageNo == 1) {
   				sumFoot = billWmRecedeDtlManager.selectDispatchSumQty(params,authorityParams);
   				if (sumFoot == null) {
   					sumFoot = new SumUtilMap<String, Object>();
   					sumFoot.put("item_qty", 0);
   					sumFoot.put("difference_Qty", 0);
   					sumFoot.put("noenough_Qty", 0);
   					sumFoot.put("usable_Qty", 0);
   				}
   				sumFoot.put("isselectsum", true);
   				sumFoot.put("item_no", "合计");
   			} else {
   				sumFoot.put("itemNo", "合计");
   			}
   			
   			footerList.add(sumFoot);
   		    obj.put("total", total);
   		    obj.put("rows", list);
   		    obj.put("footer", footerList);
   	   		
   	   		//obj.put("total", total);
   	   		//obj.put("rows", list);
   		}catch(Exception e){
   			log.error(e.getMessage(), e);
   		}
   		return obj;
   	}
   	

	@RequestMapping("/iframe")
	public ModelAndView iframe(String recedeNo,String ownerNo,String dataGridId) throws Exception {
		ModelAndView mode = new ModelAndView("wmrecede/iframe");
		mode.addObject("recedeNo", recedeNo);
		mode.addObject("ownerNo", ownerNo);
		return mode;
	}
	@RequestMapping(value = "/import")
	public ModelAndView upLoad(HttpServletRequest request,Model model,String recedeNo,String ownerNo) {
		ModelAndView mode = new ModelAndView("wmrecede/iframe");
		mode.addObject("recedeNo", recedeNo);
		mode.addObject("ownerNo", ownerNo);
		InputStream in = null;
		HttpSession session = request.getSession();
	    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
	    File file = null;
	    try{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("reportValue");
		    in= multipartFile.getInputStream();
		    Workbook  wb = null;
			try{
				wb = new HSSFWorkbook(in);
			}catch(OfficeXmlFileException e){
				long curTime = System.currentTimeMillis();
				try{
					String realPath = session.getServletContext().getRealPath("/");
					String fileName = multipartFile.getOriginalFilename();
					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
					SimpleDateFormat formater = new SimpleDateFormat("yyyyMM");
					String saveDir = formater.format(new Date());
					String fileDir = realPath + "temp" + "/"+saveDir+"/";
					File dir = new File(fileDir);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String filePath = fileDir + curTime+"."+suffix;
					file = new File(filePath);
					multipartFile.transferTo(file);
				 wb = new XSSFWorkbook(filePath);
				}catch(Exception ex){
					throw ex;
				}
			}
			Sheet sheet = wb.getSheetAt(0);
			//第一行是标题，所以从第二行开始读取
			List<BillWmRecedeDtl> list = new ArrayList<BillWmRecedeDtl>();
			BillWmRecedeDtl dtl = null;
			 for(int j=1,length = sheet.getLastRowNum()+1;j<length;j++) {
				//创建一个行对象
				Row row = sheet.getRow(j);
				int cellLenth = row.getLastCellNum();
				if(cellLenth==4){
					String itemNo = String.valueOf(GetValueTypeForXLSX(row.getCell(0)));
					String sizeNo = String.valueOf(GetValueTypeForXLSX(row.getCell(2)));
					String recedeQty = String.valueOf(GetValueTypeForXLSX(row.getCell(3)));
					if(StringUtils.isEmpty(itemNo)){
						mode.addObject("result", ResultEnums.FAIL.getResultMsg());
						mode.addObject("msg","第"+j+"行商品编码不能为空");
						return mode;
					}
					if(StringUtils.isEmpty(sizeNo)){
						mode.addObject("result", ResultEnums.FAIL.getResultMsg());
						mode.addObject("msg","第"+j+"行尺码不能为空");
						return mode;
					}
					if(StringUtils.isEmpty(recedeQty)){
						mode.addObject("result", ResultEnums.FAIL.getResultMsg());
						mode.addObject("msg","第"+j+"行退厂数量不能为空");
						return mode;
					}
					if(!com.yougou.logistics.city.web.utils.StringUtils.isNumber(recedeQty)){
						mode.addObject("result", ResultEnums.FAIL.getResultMsg());
						mode.addObject("msg","第"+j+"行退厂数量必须为数字");
						return mode;
					}
					if(Double.parseDouble(recedeQty)<=0){
						mode.addObject("result", ResultEnums.FAIL.getResultMsg());
						mode.addObject("msg","第"+j+"行退厂数量必须大于0");
						return mode;
					}
					dtl = new BillWmRecedeDtl();
					dtl.setLocno(user.getLocNo());
					dtl.setOwnerNo(ownerNo);
					dtl.setRecedeNo(recedeNo);
					dtl.setItemNo(itemNo);
					dtl.setSizeNo(sizeNo);
					dtl.setRecedeQty(new BigDecimal(recedeQty));
					list.add(dtl);
				}else{
					mode.addObject("result", ResultEnums.FAIL.getResultMsg());
					mode.addObject("msg","请使用模板进行导入");
					return mode;
				}
			 }
			 if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 this.billWmRecedeDtlManager.excelImportData(list, user.getLocNo(), recedeNo, ownerNo);
			 mode.addObject("result", ResultEnums.SUCCESS.getResultMsg());
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mode.addObject("result", ResultEnums.FAIL.getResultMsg());
			mode.addObject("msg",e.getMessage());
		}
		return mode;
	}
	
	private static Object GetValueTypeForXLSX(Cell cell)  
	{  
	    if (cell == null)  
	        return null;  
	    switch (cell.getCellType())  
	    {  
	        case Cell.CELL_TYPE_BLANK: //BLANK:  
	            return null;  
	        case Cell.CELL_TYPE_BOOLEAN: //BOOLEAN:  
	            return cell.getBooleanCellValue();  
	        case Cell.CELL_TYPE_NUMERIC: //NUMERIC:  
	            return cell.getNumericCellValue();  
	        case Cell.CELL_TYPE_STRING: //STRING:  
	            return cell.getStringCellValue();  
	        case Cell.CELL_TYPE_ERROR: //ERROR:  
	            return cell.getErrorCellValue();  
	        case Cell.CELL_TYPE_FORMULA: //FORMULA:  
	        default:  
	            return cell.getCellFormula();  
	    }  
	}  
	
	@RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 response.setContentType("application/vnd.ms-excel");
 		//文件名
 		 response.setHeader("Content-Disposition", "attachment;filename=template.xls");
 		 response.setHeader("Pragma", "no-cache");
 		 String realPath = session.getServletContext().getRealPath("/");
		 File file = new File(realPath+"template"+"template.xls");
		 InputStream in = null;
		 OutputStream out = null;
		 try{
			 in = new FileInputStream(file);
			 out = response.getOutputStream();
			 byte[] temp = new byte[1024*1024];
			 int len = 0;
			 while((len = in.read(temp))!=-1){
				 out.write(temp);
			 }
			 out.flush();
		 }catch(Exception e){
			 log.error(e.getMessage(), e);
		 }finally{
			 if(null!=in){
				 try{
					 in.close();
				 }catch(Exception e){
					 in = null;
				 }
			 }
			 if(out!=null){
				 try{
					 out.close();
				 }catch(Exception e){
					 out = null;
				 }
			 }
		 }
	}
	
}