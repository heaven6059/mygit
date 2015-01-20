package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmLocateManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 波次信息查询
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
@RequestMapping("/bill_om_locate")
public class BillOmLocateController extends BaseCrudController<BillOmLocate> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillOmLocateManager billOmLocateManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billOmLocate/",billOmLocateManager);
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
			
			//返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("statusStr", "小计");
			footerList.add(footerMap);
			for (BillOmLocate temp : list) {
				try {
					FooterUtil.setFooterMapByInt("totalItemQty", temp.getTotalItemQty(), footerMap);
					FooterUtil.setFooterMap("totalPlanQty", temp.getTotalPlanQty(), footerMap);
					FooterUtil.setFooterMap("totalVolumeQty", temp.getTotalVolumeQty(), footerMap);
					FooterUtil.setFooterMap("totalWeightQty", temp.getTotalWeightQty(), footerMap);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmLocateManager.selectSumQty(billOmLocate,authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("totalItemQty", 0);
					sumFoot.put("totalPlanQty", 0);
					sumFoot.put("totalVolumeQty", 0);
					sumFoot.put("totalWeightQty", 0);
				}
				
				sumFoot2.put("totalItemQty", sumFoot.get("totalitemqty")==null?"0":sumFoot.get("totalitemqty"));
				sumFoot2.put("totalPlanQty", sumFoot.get("totalplanqty")==null?"0":sumFoot.get("totalplanqty"));
				sumFoot2.put("totalVolumeQty", sumFoot.get("totalvolumeqty")==null?"0":sumFoot.get("totalvolumeqty"));
				sumFoot2.put("totalWeightQty", sumFoot.get("totalweightqty")==null?"0":sumFoot.get("totalweightqty"));
				sumFoot2.put("isselectsum", true);
				sumFoot2.put("statusStr", "合计");
			} else {
				sumFoot2.put("statusStr", "合计");
			}
			footerList.add(sumFoot2);
			
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			return obj;
		}catch (Exception e) {
			log.error("===========查询调度波次列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
   	
   	/**
	 *  手工关闭
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/overBillOmLocate")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public ResponseEntity<Map<String, String>> overBillOmLocate(HttpServletRequest req,BillOmLocate billOmLocate)
			throws ManagerException {
		Map<String, String> obj = new HashMap<String, String>();
		try {
			List<BillOmLocate> lists = null;
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>(){});
				lists =convertListWithTypeReference(mapper,list);
			}
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			obj = billOmLocateManager.overBillOmLocate(lists,user);
			return new ResponseEntity<Map<String, String>>(obj, HttpStatus.OK);
		} catch (Exception e) {
			log.error("=======波次手工关闭异常：" + e.getMessage(), e);
			obj.put("flag", "fail");
			obj.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, String>>(obj, HttpStatus.OK);
		}
	}
	
	
	/**
	 *  发单还原
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/recoveryLocateSend")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public ResponseEntity<Map<String, String>> recoveryLocateSend(HttpServletRequest req,BillOmLocate billOmLocate)
			throws ManagerException {
		Map<String, String> obj = new HashMap<String, String>();
		try {
			List<BillOmLocate> lists = null;
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>(){});
				lists =convertListWithTypeReference(mapper,list);
			}
			obj = billOmLocateManager.recoveryLocateSend(lists);
			return new ResponseEntity<Map<String, String>>(obj, HttpStatus.OK);
		} catch (Exception e) {
			log.error("=======波次发单还原异常：" + e.getMessage(), e);
			obj.put("flag", "fail");
			obj.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, String>>(obj, HttpStatus.OK);
		}
	}
	
	/**
	 *  删除波茨
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/deleteOmLocate")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteOmLocate(HttpServletRequest req,BillOmLocate billOmLocate)
			throws ManagerException {
		Map<String, String> obj = new HashMap<String, String>();
		try {
			List<BillOmLocate> lists = null;
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>(){});
				lists =convertListWithTypeReference(mapper,list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmLocateManager.deleteOmLocate(lists, user.getLoginName());
			obj.put("flag", "success");
			return new ResponseEntity<Map<String, String>>(obj, HttpStatus.OK);
		} catch (Exception e) {
			log.error("=======波次手工关闭异常：" + e.getMessage(), e);
			obj.put("flag", "fail");
			obj.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, String>>(obj, HttpStatus.OK);
		}
	}
	
    
   	public SimpleDateFormat getDateFormat(String text){
   		SimpleDateFormat dateFormat = null;
   		if(text.length()>10){
			dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		}else{
			dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		}
   		return dateFormat;
   	}
   	
   	/**
	 * 转换成泛型列表
	 * @param mapper
	 * @param list
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillOmLocate> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<BillOmLocate> entityClass = (Class<BillOmLocate>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<BillOmLocate> tl=new ArrayList<BillOmLocate>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmLocate type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
	
}