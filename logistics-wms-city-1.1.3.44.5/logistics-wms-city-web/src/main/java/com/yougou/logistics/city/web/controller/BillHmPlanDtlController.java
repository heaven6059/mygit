package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillHmPlanDtlManager;
import com.yougou.logistics.city.manager.BillHmPlanManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
@RequestMapping("/bill_hm_plan_dtl")
@ModuleVerify("25100010")
public class BillHmPlanDtlController<ModelType> extends BaseCrudController<BillHmPlanDtl> {

	@Log
	private Logger log;

	@Resource
	private BillHmPlanManager billHmPlanManager;

	@Resource
	private BillHmPlanDtlManager billHmPlanDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billHmPlanDtl/", billHmPlanDtlManager);
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
			int total = this.billHmPlanDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillHmPlanDtl> list = this.billHmPlanDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 新增和删除预到货通知单明细
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveBillHmPlan")
	public ResponseEntity<Map<String, Object>> saveBillHmPlan(HttpServletRequest req, BillHmPlan billHmPlan)
			throws JsonParseException, JsonMappingException, IOException, ManagerException {

		Map<String, Object> flag = new HashMap<String, Object>();
		BillHmPlan entity = new BillHmPlan();

		try {
			if (null != billHmPlan) {

				String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req
						.getParameter("deleted");
				String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req
						.getParameter("updated");
				String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req
						.getParameter("inserted");

				ObjectMapper mapper = new ObjectMapper();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				mapper.getDeserializationConfig().setDateFormat(formatter);
				Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
				if (StringUtils.isNotBlank(deletedList)) {
					List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.DELETED, oList);
				}
				if (StringUtils.isNotBlank(upadtedList)) {
					List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.UPDATED, oList);
				}
				if (StringUtils.isNotBlank(insertedList)) {
					List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.INSERTED, oList);
				}

				//获取登陆用户
				HttpSession session = req.getSession();
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				entity = billHmPlanManager.addBillHmPlan(billHmPlan, params,user);
			}

			flag.put("result", "success");
			flag.put("entity", entity);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);

		} catch (ManagerException e) {
			log.error("===========新增，修改，删除移库单明细时异常：" + e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("entity", entity);
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<ModelType> tl = new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getByPage.json")
	@ResponseBody
	public Map<String, Object> getByPage(HttpServletRequest req, Model model) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map params = builderParams(req, model);
			int total = this.billHmPlanDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillHmPlanDtl> list = this.billHmPlanDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			//返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillHmPlanDtl temp : list) {
				this.setFooterMap("originQty", temp.getOriginQty(), footerMap);
			}
			
			// 合计
						Map<String, Object> sumFoot = new HashMap<String, Object>();
						if (pageNo == 1) {
							sumFoot = billHmPlanDtlManager.selectSumQty(params, authorityParams);
							if (sumFoot != null) {
								sumFoot.put("isselectsum", true);
							}
						}
						sumFoot.put("itemNo", "合计");
						footerList.add(sumFoot);
						
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
			obj.put("footer", footerList);
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
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
	
	 @RequestMapping("/downloadTemple")
		public void downloadTemple(HttpServletRequest req,HttpSession session,HttpServletResponse response) throws Exception {
			 FileUtils.downloadTemple(session, response, "billhmplandtlTemplate.xls");
		}
	 
	 @RequestMapping(value = "/iframe")
		public ModelAndView iframe(HttpServletRequest req) throws Exception {
			ModelAndView mode = new ModelAndView("billhmplan/iframe");
			return mode;
		}
	 
	 @RequestMapping(value = "/importExcel")
		public ModelAndView upLoad(HttpServletRequest request,Model model) {
			ModelAndView mode = new ModelAndView("billhmplan/iframe");
			SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
			
		    try{
		    	String [] colNames = {"itemNo","sizeNo","sCellNo","dCellNo","originQty"};
		    	String [] keyNames= {"itemNo","sizeNo","sCellNo","dCellNo","originQty"};
		    	boolean [] mustArray = {true,true,true,true,true};
		    	
		    	AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(request);
				Map<String, Object> paramsAll = builderParams(request, model);
				Map<String, Object> params = new HashMap<String, Object>();
				params.putAll(paramsAll);
				params.put("locno", user.getLocNo());
				
				List<BillHmPlanDtl> list = ExcelUtils.getData(request, 0, 1, colNames,mustArray, keyNames, BillHmPlanDtl.class);
				 if(list.size()==0){
				    mode.addObject("result", ResultEnums.FAIL.getResultMsg());
					mode.addObject("msg","导入的文件没有数据");
					return mode;
				 }
				 
				Map<String ,Object> map = billHmPlanDtlManager.importStorelockDtlExcel(list, authorityParams, params);
				
				 mode.addObject("result",map.get("result"));
				 mode.addObject("msg",map.get("msg"));
			}catch(Exception e){
				log.error(e.getMessage(), e);
				mode.addObject("result", ResultEnums.FAIL.getResultMsg());
				mode.addObject("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
			}
			return mode;
		}
	
}