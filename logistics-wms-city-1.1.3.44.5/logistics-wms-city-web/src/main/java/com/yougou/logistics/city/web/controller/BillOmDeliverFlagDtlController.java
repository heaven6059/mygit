package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URLDecoder;
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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmDeliverDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 装车单详情controller
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:29:00
 * @version 0.1.0    
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_deliverflag_dtl")
public class BillOmDeliverFlagDtlController<ModelType> extends BaseCrudController<BillOmDeliverDtl> {
    
	@Log
	private Logger log;
	
	@Resource
    private BillOmDeliverDtlManager billOmDeliverDtlManager;  
  
    @Override  
    public CrudInfo init() {  
        return new CrudInfo("billomdeliverflagdtl/",billOmDeliverDtlManager);
    }
    
    @RequestMapping(value = "/get_biz")  
	@ResponseBody
	@Override
	public List<BillOmDeliverDtl> getBiz(BillOmDeliverDtl modelType,HttpServletRequest req,Model model) {
    	List<BillOmDeliverDtl> result = null;
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		Map<String,Object> params=builderParams(req, model);
    		result = this.billOmDeliverDtlManager.flagDtlByParams(modelType, params, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
    
    
    /**
     * 查看明细列表
     */
	@RequestMapping(value = "/findLoadproposeDeliverDtlBoxByPage")
	@ResponseBody
	public  Map<String, Object> findLoadproposeDeliverDtlBoxByPage(HttpServletRequest req, Model model){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmDeliverDtlManager.findLoadproposeDeliverDtlBoxCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.findLoadproposeDeliverDtlBoxByPage(page, sortColumn, sortOrder, params, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
		
    /**
     * 查看明细列表
     */
	@RequestMapping(value = "/viewDtl")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmDeliverDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			
			// 汇总
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalQty = new BigDecimal(0);
			for (BillOmDeliverDtl dtl : list) {
				totalQty = totalQty.add(dtl.getQty()==null?new BigDecimal(0):dtl.getQty());
			}
			footer.put("boxNo", "小计");
			footer.put("qty", totalQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmDeliverDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("box_no", "合计");
			} else {
				sumFoot.put("boxNo", "合计");
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
    
    /**
     * 选箱
     * @param req
     * @param model
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value = "/boxSelectQuery")  
	@ResponseBody
	public Map<String, Object> boxSelectQuery(HttpServletRequest req, Model model) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = this.billOmDeliverDtlManager.flagSelectCount(params,authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.flagSelectQuery(page, sortColumn, sortOrder, params,authorityParams);
    		obj.put("total", total);
    		obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    /**
     * 增删改
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDtlInfo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveDtlInfo(BillOmDeliverDtl billOmDeliverDtl,HttpServletRequest req) {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
        	ObjectMapper mapper = null;
    		String locno = req.getParameter("locno");
    		String deliverNo = req.getParameter("deliverNo");
    		String ownerNo = req.getParameter("ownerNo");
    		String transFlag = "10";
    		//新增的行
    		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
    		insertedList =URLDecoder.decode(insertedList,"UTF-8");
			if(insertedList!=null && StringUtils.isNotBlank(insertedList)) {
				mapper = new ObjectMapper();
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
		    	
		    	List<ModelType> oList=convertListWithTypeReference(mapper,list);
	    		params.put(CommonOperatorEnum.INSERTED, oList);
//		    	if(list.size() > 0) {
//		    		billOmDeliverDtlManager.addDtlInfo(list, locno, deliverNo, ownerNo);
//		    	}
			}
			//删除的行
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			deletedList =URLDecoder.decode(deletedList,"UTF-8");
			if(deletedList!=null && StringUtils.isNotBlank(deletedList)) {
				mapper = new ObjectMapper();
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
				
				List<ModelType> oList=convertListWithTypeReference(mapper,list);
				params.put(CommonOperatorEnum.DELETED, oList);
			}
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//插入、删除数据
			billOmDeliverDtlManager.addBillOmDeliverDtl(locno, deliverNo, ownerNo,params,user, transFlag,authorityParams);
			flag.put("success", "true");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
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
	
	/**
	 * 查询客户
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectFlagStore")
	@ResponseBody
	public  Map<String, Object> selectBoxStore(HttpServletRequest req, Model model){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmDeliverDtlManager.selectFlagCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.selectFlagStore(page, sortColumn, sortOrder, params, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
}