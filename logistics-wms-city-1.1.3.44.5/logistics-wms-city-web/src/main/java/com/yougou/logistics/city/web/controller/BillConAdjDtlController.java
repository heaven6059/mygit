package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillConAdjDtlManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
@RequestMapping("/bill_stock_adj_dtl")
public class BillConAdjDtlController extends BaseCrudController<BillConAdjDtl> {
	@Log
	private Logger log;
	@Resource
    private BillConAdjDtlManager billConAdjDtlManager;
    @Override
    public CrudInfo init() {
        return new CrudInfo("stockadj/",billConAdjDtlManager);
    }
    
    /**
	 * 分页查询调整库从表信息
	 * 
	 * @param req
	 * @param model
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
    @RequestMapping(value = "/getDetail.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model){
    	Map<String, Object> obj=null;
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			Map<String, Object> findDtlParams=new HashMap<String, Object>();
			findDtlParams.putAll(params);
			
			int total = billConAdjDtlManager.findCount(findDtlParams,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConAdjDtl> list = billConAdjDtlManager.findByPage(page, sortColumn, sortOrder, findDtlParams,authorityParams, DataAccessRuleEnum.BRAND);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("findDtlParams", findDtlParams);
    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    /**
     * 分页查询调整库从表信息(含汇总)
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value = "/dtl_getDetail.json")
	@ResponseBody
	public  Map<String, Object> queryDtlList(HttpServletRequest req, Model model){
    	Map<String, Object> obj=null;
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			obj = queryList(req, model);
			Map<String, Object> findDtlParams = (Map<String, Object>) obj.get("findDtlParams");
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalAdjQty = new BigDecimal(0);
			List<BillConAdjDtl> list = CommonUtil.getRowsByListJson(obj, BillConAdjDtl.class);
			for(BillConAdjDtl dtl:list){
				totalAdjQty = totalAdjQty.add(dtl.getAdjQty());
			}		
			footer.put("cellNo", "小计");
			footer.put("adjQty", totalAdjQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
			    sumFoot = billConAdjDtlManager.findSumQty(findDtlParams,authorityParams);
			    if (sumFoot == null) {
				sumFoot = new SumUtilMap<String, Object>();
				sumFoot.put("adj_qty", 0);
			    }
			    sumFoot.put("isselectsum", true);
			    sumFoot.put("cell_No", "合计");
			} else {
			    sumFoot.put("cellNo", "合计");
			}		
			footerList.add(sumFoot);
			obj.put("footer", footerList);
    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    /**
   	 * 获取商品列表
   	 * 
   	 * @param req
   	 * @param model
   	 * @param params
   	 * @return
   	 * @throws ManagerException
   	 */
   	@RequestMapping(value = "/getItem")
   	@ResponseBody
   	public  Map<String, Object> getStockAdjDetail(HttpServletRequest req, Model model){
   		Map<String, Object> obj=null;
   		try {
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
			String barCode = "";
			if(paramsAll.get("barCode") != null) {
				barCode = paramsAll.get("barCode").toString().toUpperCase();
				params.put("barCode", barCode);
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
			int total = billConAdjDtlManager.selectItemCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConAdjDtl> list = billConAdjDtlManager.selectItem(page, sortColumn, sortOrder, params,authorityParams);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
   		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
   	}
    
    /**
     * 保存明细
     * @param req
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ManagerException
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addDtl")
	public ResponseEntity<Map<String, Object>> addDtl(HttpServletRequest req,BillConAdj billConAdjDtl) 
			throws JsonParseException, JsonMappingException, IOException,ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();

		try {
			SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
			
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			
			ObjectMapper mapper = new ObjectMapper();
			Map<CommonOperatorEnum, List<BillConAdjDtl>> params = new HashMap<CommonOperatorEnum, List<BillConAdjDtl>>();
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
				List<BillConAdjDtl> oList=convertListWithTypeReference2(mapper,list);
				params.put(CommonOperatorEnum.DELETED, oList);
			}
			if (StringUtils.isNotEmpty(upadtedList)) {
				List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>(){});
				List<BillConAdjDtl> oList=convertListWithTypeReference2(mapper,list);
				params.put(CommonOperatorEnum.UPDATED, oList);
			}
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
				List<BillConAdjDtl> oList=convertListWithTypeReference2(mapper,list);
				params.put(CommonOperatorEnum.INSERTED, oList);
			}
			if (params.size() > 0) {
				flag = billConAdjDtlManager.addDtlsave(billConAdjDtl, params, user.getLoginName());
			}
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error("保存库存报损明细时异常："+e.getMessage(),e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
    
    @RequestMapping(value = "/addDtlByCell")
	@ResponseBody
	public Map<String, Object> addDtlByCell(String[] cellNos, String adjNo,String locNo, String ownerNo, HttpServletRequest req) {
		Map obj = new HashMap();		
		try {
			SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
			String editor = user.getLoginName();
			
			List<String> cellNoList =new ArrayList<String>();	
			for(String cellNo:cellNos){	
				cellNoList.add(cellNo);				
			}
			
			Map map = new HashMap();
			map.put("adjNo", adjNo);
			map.put("locNo", locNo);
			map.put("ownerNo", ownerNo);
			map.put("editor", editor);
			billConAdjDtlManager.addDtlByCell(cellNoList, map);
			obj.put("result", "true");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}
    /**
     * 根据容器保存库存调整 明细
     * @param conNos
     * @param adjNo
     * @param locNo
     * @param ownerNo
     * @param req
     * @return
     */
    @RequestMapping(value = "/addDtlByConNo")
	@ResponseBody
	public Map<String, Object> addDtlByConNo(String[] conNos, String adjNo,String locNo, String ownerNo, HttpServletRequest req) {
		Map<String,Object> obj = new HashMap<String,Object>();		
		try {
			SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
			String editor = user.getLoginName();
			String editorName = user.getUsername();
			List<String> conNoList =new ArrayList<String>();	
			for(String cellNo:conNos){	
				conNoList.add(cellNo);				
			}
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("adjNo", adjNo);
			map.put("locNo", locNo);
			map.put("ownerNo", ownerNo);
			map.put("editor", editor);
			map.put("editorName", editorName);
			billConAdjDtlManager.addDtlByConNo(conNoList, map);
			obj.put("result", "true");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "fail");
			obj.put("msg", e.getMessage());
		}
		return obj;
	}
    
    /**
	 *检查cellid是否存在 
	 * @param cellId
	 * @return
	 * @throws ManagerException
	 */
//	@RequestMapping(value = "/checkItem")
//	@ResponseBody
//	public int checkCellId(String cellNo,String itemNo,String sizeNo,String adjNo) throws ManagerException{
//		Map<String, Object> map = new Map<String, Object>();
//		
//		return billConAdjDtlManager.selectCellId(cellNo,itemNo,sizeNo,adjNo);
//		
//	}
    
	/**
	 * 检查库存数量
	 * @param locNo
	 * @param cellNo
	 * @param ownerNo
	 * @param itemNo
	 * @param barCode
	 * @param adjType
	 * @param sType
	 * @return
	 * @throws ManagerException
	 */
//  @RequestMapping(value="/checkAdjAty")
//	@ResponseBody
//	public int checkQty(String locNo,String cellNo,String ownerNo,String itemNo,String barCode,String adjType,String sType) throws ManagerException{
//		return  billConAdjDtlManager.checkAty(locNo,cellNo,ownerNo,itemNo,barCode,adjType, sType);
//		
//	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillConAdjDtl> convertListWithTypeReference2(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<BillConAdjDtl> entityClass = (Class<BillConAdjDtl>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<BillConAdjDtl> tl=new ArrayList<BillConAdjDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillConAdjDtl type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
    
    @RequestMapping("/downloadTemple")
	public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
		 FileUtils.downloadTemple(session, response, "adjTemplate.xls");
	}
    
    @RequestMapping("/iframe")
	public ModelAndView iframe(String adjNo,String ownerNo) throws Exception {
		ModelAndView mode = new ModelAndView("stockadj/iframe");
		mode.addObject("adjNo", adjNo);
		mode.addObject("ownerNo", ownerNo);
		return mode;
	}
    
    @RequestMapping(value = "/importExcel")
	public ModelAndView upLoad(HttpServletRequest request,Model model,String adjNo,String ownerNo) {
		ModelAndView mode = new ModelAndView("stockadj/iframe");
		mode.addObject("adjNo", adjNo);
		mode.addObject("ownerNo", ownerNo);
		SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
	    try{
	    	String [] colNames = {"itemNo","sizeNo","itemType","quality","cellNo","adjQty"};
	    	String [] mainKey  = {"itemNo","sizeNo","itemType","quality","cellNo"};
	    	boolean [] mustArray = {true,true,true,true,true,true,true};
			List<BillConAdjDtl> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, mainKey, BillConAdjDtl.class);
			 if(list.size()==0){
			    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg","导入的文件没有数据");
				return mode;
			 }
			 
			 //this.billSmOtherinDtlManager.excelImportData(list, user.getLocNo(), otherinNo,ownerNo,authorityParams);
			 //billSmWasteDtlManager.importDtl(list, wasteNo, ownerNo, user);
			 billConAdjDtlManager.importDtl(list, adjNo, ownerNo, user);
			 mode.addObject("result", ResultEnums.SUCCESS.getResultMsg());
		}catch(Exception e){
			log.error(e.getMessage(), e);
			mode.addObject("result", ResultEnums.FAIL.getResultMsg());
			mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
		}
		return mode;
	}
    
    /**
     * 库存调整单打印
     * @param req
     * @param session
     * @param keys
     * @return
     */
    @RequestMapping(value = "/printDetail")
    @ResponseBody
    public Map<String, Object> findAlldtl(HttpServletRequest req,HttpSession session, String keys){
        Map<String, Object> result = new HashMap<String, Object>();
        //List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
        try {
            if (StringUtils.isEmpty(keys)) {
                result.put("result", ResultEnums.FAIL.getResultMsg());
                result.put("msg", "参数错误");
                return result;
            }
            List<Map<String, Object>> resultList = this.billConAdjDtlManager.findAllDtl(keys, authorityParams);
            result.put("pages", resultList);
            result.put("result", ResultEnums.SUCCESS.getResultMsg());
            return result;
        } catch (ManagerException e) {
            log.error(e.getMessage(), e);
            result.put("result", ResultEnums.FAIL.getResultMsg());
            result.put("msg", e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.put("result", ResultEnums.FAIL.getResultMsg());
            result.put("msg", "系统异常请联系管理员");
            return result;
        }
    }
}