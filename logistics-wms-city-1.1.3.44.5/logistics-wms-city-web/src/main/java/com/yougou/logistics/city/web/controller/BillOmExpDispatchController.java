package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.manager.BillOmExpManager;
import com.yougou.logistics.city.manager.BillOmLocateManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-1-10 下午4:35:15
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/billomexpdispatch")
@ModuleVerify("25080040")
public class BillOmExpDispatchController extends BaseCrudController<BillOmExp> {

	@Log
    private Logger log;
	
    @Resource
    private BillOmExpManager billOmExpManager;
    
    @Resource
    private BillOmLocateManager billOmLocateManager;
    
    
	@Override
	public CrudInfo init() {
		return new CrudInfo("billOmExpDispatch/", billOmExpManager);
	}
	
	/**
	 * 查询单明细列表（带分页）
	 */
   	@RequestMapping(value = "/listBillOmExpDispatch.json")
	@ResponseBody
	public  Map<String, Object> listBillOmExpDispatch(HttpServletRequest req, BillOmExp billOmExp) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			int total = billOmExpManager.findBillOmExpDispatchCount(billOmExp, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmExp> list = billOmExpManager.findBillOmExpDispatchByPage(page, sortColumn, sortOrder, billOmExp, authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		}catch (Exception e) {
			log.error("===========查询出库调度列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
   	
	
	/**
	 * 出库调度试图汇总数据
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findBillOmExpViewTotalQty")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findBillOmExpViewTotalQty(BillOmExp billOmExp,HttpServletRequest req) throws ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			BillOmExp exp = billOmExpManager.findBillOmExpViewTotalQty(billOmExp);
			flag.put("flag", "success");
			flag.put("exp", exp);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("=========== 出库调度时异常："+e.getMessage(),e);
			flag.put("flag", "warn");
			flag.put("msg", "查询出库调度试图汇总数据异常");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	/**
	 * 出库调度
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/procBillOmExpDispatchQuery")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> procBillOmExpDispatchQuery(BillOmExp billOmExp,HttpServletRequest req) throws ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			
			flag = billOmExpManager.procBillOmExpDispatchQuery(billOmExp);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("=========== 出库调度时异常："+e.getMessage(),e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	/**
	 * 出库续调
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/procBillOmExpContinueDispatchQuery")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> procBillOmExpContinueDispatchQuery(String datas,BillOmLocate billOmLocate, HttpServletRequest req) throws ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			List<BillOmLocate> listBillOmLocates = new ArrayList<BillOmLocate>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {});
				listBillOmLocates = convertListWithTypeReference(mapper, list);
			}
			billOmLocateManager.procBillOmExpContinueDispatchQuery(listBillOmLocates,billOmLocate);
			flag.put("flag", "success");
			flag.put("msg", "续调成功");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("=========== 出库调度时异常："+e.getMessage(),e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	/**
	 * 查询单明细列表（带分页）
	 */
   	@RequestMapping(value = "/listBillOmLocate.json")
	@ResponseBody
	public  Map<String, Object> listBillOmLocate(HttpServletRequest req, BillOmLocate billOmLocate) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			
//			if(billOmLocate!=null){
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//				if(StringUtils.isNotBlank(startCreatetmStr)){
//					if(startCreatetmStr.length() <= 10){
//						startCreatetmStr+= " 00:00:00";
//					}
//					billOmLocate.setStartCreatetm(sdf.parse(startCreatetmStr));
//				}
//				if(StringUtils.isNotBlank(endCreatetmStr)){
//					if(endCreatetmStr.length() <= 10){
//						endCreatetmStr+= " 23:59:59";
//					}
//					billOmLocate.setEndCreatetm(sdf.parse(endCreatetmStr));
//				}
//			}
			
			int total = billOmLocateManager.findBillOmLocateCount(billOmLocate, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmLocate> list = billOmLocateManager.findBillOmLocateByPage(page, sortColumn, sortOrder, billOmLocate, authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		}catch (Exception e) {
			log.error("===========查询调度波次列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
	
	private List<BillOmLocate> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmLocate> tl = new ArrayList<BillOmLocate>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmLocate type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmLocate.class);
			tl.add(type);
		}
		return tl;
	}
	
}
