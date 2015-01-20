package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmOutstockDtlManager;
import com.yougou.logistics.city.manager.BillOmOutstockManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 即时移库
 * 
 * @author su.yq
 * @date 2014-3-18 下午3:31:15
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/immediate_move_stock")
@ModuleVerify("25100040")
public class ImmediateMoveStockController extends BaseCrudController<BillOmOutstock> {
	
	@Log
    private Logger log;
	
	@Resource
	private BillOmOutstockManager billOmOutstockManager;
	
	@Resource
	private BillOmOutstockDtlManager billOmOutstockDtlManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("immediatemovestock/",billOmOutstockManager);
    }
	
	
	/**
     * 即时移库
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	@Override
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmOutstockManager.findMoveStockCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstock> list = this.billOmOutstockManager.findMoveStockByPage(page, sortColumn, sortOrder, params, authorityParams);
			
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			for (BillOmOutstock billOmOutstock : list) {
				totalItemQty = totalItemQty.add(billOmOutstock.getItemQty());
			}
			footer.put("outstockNo", "小计");
			footer.put("itemQty", totalItemQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmOutstockManager.selectImmediateMoveSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("outstock_no", "合计");
			} else {
				sumFoot.put("outstockNo", "合计");
			}
			footerList.add(sumFoot);
			
			obj.put("footer", footerList);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj; 
	} 
    
	
	 /**
     * 查询库存
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value = "/findConContentGroupByPage.json")
	@ResponseBody    
	public  Map<String, Object> findConContentGroupByPage(HttpServletRequest req, Model model) throws ManagerException {
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
			//大类一
			String cateOne = (String) paramsAll.get("cateOne");
			String[] cateOneValues = null;
			if (StringUtils.isNotEmpty(cateOne)) {
				cateOneValues = cateOne.split(",");
				params.put("cateOne", cateOneValues);
			}
			
			//大类二
			String cateTwo = (String) paramsAll.get("cateTwo");
			String[] cateTwoValues = null;
			if (StringUtils.isNotEmpty(cateTwo)) {
				cateTwoValues = cateTwo.split(",");
				params.put("cateTwo", cateTwoValues);
			}
			
			//大类三
			String cateThree = (String) paramsAll.get("cateThree");
			String[] cateThreeValues = null;
			if (StringUtils.isNotEmpty(cateThree)) {
				cateThreeValues = cateThree.split(",");
				params.put("cateThree", cateThreeValues);
			}
			
			//性别
			String season = (String)req.getParameter("season");
			String [] seasonValues = null;
			if(StringUtils.isNotBlank(season)){
				seasonValues = season.split(",");
			}
			if(seasonValues != null) {
				params.put("seasonValues", seasonValues);
			}
			//季节
			String gender = (String)req.getParameter("gender");
			String [] genderValues = null;
			if(StringUtils.isNotBlank(gender)){
				genderValues = gender.split(",");
			}
			if(genderValues != null) {
				params.put("genderValues", genderValues);
			}
			int total = this.billOmOutstockDtlManager.findConContentGroupByCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDtlDto> list = this.billOmOutstockDtlManager.findConContentGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj; 
	}  
    
    
    /**
     * 查询明细
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value = "/findMoveStockGroupByPage.json")
	@ResponseBody    
	public  Map<String, Object> findMoveStockGroupByPage(HttpServletRequest req, Model model) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmOutstockDtlManager.findMoveStockGroupByCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDtlDto> list = this.billOmOutstockDtlManager.findMoveStockGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
			
			obj.put("total", total);
			obj.put("rows", list);
			
			// 汇总
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalQty = new BigDecimal(0);
			BigDecimal totalGoodContentQty = new BigDecimal(0);
			for (BillOmOutstockDtlDto dtl : list) {
				totalQty = totalQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
				totalGoodContentQty = totalGoodContentQty.add(dtl.getGoodContentQty() == null ? new BigDecimal(0) : dtl.getGoodContentQty());
			}
			footer.put("sCellNo", "小计");
			footer.put("itemQty", totalQty);
			footer.put("goodContentQty", totalGoodContentQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmOutstockDtlManager.selectMoveStockSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("good_content_qty", 0);
					sumFoot.put("item_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("s_cell_no", "合计");
			} else {
				sumFoot.put("sCellNo", "合计");
			}
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj; 
	} 
    
    
    /**
     * 即时移库
     * @param BillOmOutstockDtlDto
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveMainInfo(BillOmOutstockDtlDto dtlDto,HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			List<BillOmOutstockDtlDto> listDtlDtos = new ArrayList<BillOmOutstockDtlDto>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
				listDtlDtos=convertListWithTypeReference(mapper,list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmOutstockDtlManager.procImmediateMoveStock(listDtlDtos, user);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	/**
     * 确认即时移库
     * @param BillOmOutstockDtlDto
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryBill")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> queryBill(BillOmOutstock billOmOutstock,HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillOmOutstock> listOutstocks = new ArrayList<BillOmOutstock>();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>(){});
				listOutstocks=convertListWithTypeReferenceO(mapper,list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmOutstockManager.queryBill(listOutstocks, user);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	
	@SuppressWarnings({ "rawtypes" })
	private List<BillOmOutstockDtlDto> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillOmOutstockDtlDto> tl=new ArrayList<BillOmOutstockDtlDto>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmOutstockDtlDto type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillOmOutstockDtlDto.class);
			tl.add(type);
		}
		return tl;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private List<BillOmOutstock> convertListWithTypeReferenceO(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillOmOutstock> tl=new ArrayList<BillOmOutstock>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmOutstock type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillOmOutstock.class);
			tl.add(type);
		}
		return tl;
	}
    
}
