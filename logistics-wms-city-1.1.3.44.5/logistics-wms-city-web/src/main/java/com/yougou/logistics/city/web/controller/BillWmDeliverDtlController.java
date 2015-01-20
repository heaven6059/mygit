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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtlKey;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillWmDeliverDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 退厂确认（配送单明细）
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
@RequestMapping("/wmdeliverdtl")
public class BillWmDeliverDtlController<ModelType> extends BaseCrudController<BillWmDeliverDtl> {
	
    @Log
    private Logger log;
	
    @Resource
    private BillWmDeliverDtlManager billWmDeliverDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("wmdeliverdtl/",billWmDeliverDtlManager);
    }
    
    
	/**
	 * 新增和删除退厂配送单
	 */
	@RequestMapping(value = "/saveDtl")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveDtl(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		boolean isSuccess = false;
		try{
			String locno = req.getParameter("locno");
			String ownerNo = req.getParameter("ownerNo");
			String supplierNo = req.getParameter("supplierNo");
			String deliverNo = req.getParameter("deliverNo");
			if(StringUtils.isNotBlank(locno) && StringUtils.isNotBlank(ownerNo)
					&& StringUtils.isNotBlank(supplierNo) && StringUtils.isNotBlank(deliverNo)){
				
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
				
				isSuccess = billWmDeliverDtlManager.addWmDeliverDetail(locno,ownerNo,supplierNo,deliverNo,params,user.getLoginName(),user.getUsername());
			}
			flag.put("success", isSuccess);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("===========新增，修改，删除预到货通知单明细时异常："+e.getMessage(),e);
			flag.put("success", false);
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
    
    
    /**
     * 查询标签下面的商品信息
     * @param req
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ManagerException
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/getLabelInfoDtlsList")
    @ResponseBody
	 public  List<ConLabelDtl> getLabelInfoDtlsList(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
		ManagerException {
	        List<ConLabelDtl> listObj=new ArrayList<ConLabelDtl>();
	        ObjectMapper mapper = new ObjectMapper();
	        String selectLabelList = StringUtils.isEmpty(req.getParameter("selectLabel")) ? "" : req.getParameter("selectLabel");
		    try{
		    	if (StringUtils.isNotBlank(selectLabelList)) {
					List<Map> list = mapper.readValue(selectLabelList, new TypeReference<List<Map>>(){});
					List<BillWmDeliverDtl> oList=this.convertListWithTypeReference(mapper,list);
					listObj = billWmDeliverDtlManager.getLabelInfoDtlsList(oList);
				}
		    }catch(Exception e){
		    	log.error("=======查询标签下面的商品信息异常："+e.getMessage(),e);
		    }
			return listObj;
	 }
    
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<ModelType> tl=new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
	
	/**
	 * 根据仓别，业主，配送单号，查询配送单的明细信息（带分页）
	 * @param billWmDeliverDtlKey
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findLabelNoByRecheckNoPage")
	public ResponseEntity<Map<String,Object>> findLabelNoByRecheckNoPage(BillWmDeliverDtlKey billWmDeliverDtlKey, HttpServletRequest req,HttpServletResponse response) {
    	Map<String,Object> obj=new HashMap<String,Object>();
    	int total=0;
		List<BillWmDeliverDtl> listItem = new ArrayList<BillWmDeliverDtl>(0);
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			
			//总数
			total = billWmDeliverDtlManager.countWmDeliverDtlByMainId(billWmDeliverDtlKey);
			
			//记录list
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			listItem = billWmDeliverDtlManager.findWmDeliverDtlByMainIdPage(page, billWmDeliverDtlKey);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		obj.put("total", total);
		obj.put("rows", listItem);
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
	
	/**
	 * 校验标签号是否合法（状态是否不为61，是否已存在于当前的单据中）
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/validateLabelNo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> validateLabelNo(String  noStrs,String deliverNo,String locno) throws ManagerException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try{
			objMap = billWmDeliverDtlManager.validateLabelNo(noStrs,deliverNo,locno);
			return new ResponseEntity<Map<String, Object>>(objMap, HttpStatus.OK);
		}catch(Exception e){
			log.error("=========== 校验标签号是否合法时异常："+e.getMessage(),e);
			objMap.put("flag", "fail");
			return new ResponseEntity<Map<String, Object>>(objMap, HttpStatus.OK);
		}
	}
	
	/**
	 * 根据仓别，业主，配送单号，查询配送单的汇总明细信息（带分页）
	 * @param billWmDeliverDtlKey
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findBillWmDeliverDtlGroupByPage")
	@ResponseBody    
	public  Map<String, Object> findBillWmDeliverDtlGroupByPage(HttpServletRequest req, Model model) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			Map<String, Object> params = builderParams(req, model);
			int total = billWmDeliverDtlManager.findBillWmDeliverDtlGroupByCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmDeliverDtl> list = billWmDeliverDtlManager.findBillWmDeliverDtlGroupByPage(page, sortColumn, sortOrder, params, authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj; 
		}catch(Exception e){
			log.error("===========查询预到货通知单明细列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
	
	/**
	 * 根据仓别，业主，配送单号，查询配送单的明细信息（带分页）
	 * @param billWmDeliverDtlKey
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findBillWmDeliverDtlByPage")
	@ResponseBody    
	public  Map<String, Object> findBillWmDeliverDtlByPage(HttpServletRequest req, Model model) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			Map<String, Object> params = builderParams(req, model);
			int total = billWmDeliverDtlManager.findBillWmDeliverDtlCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmDeliverDtl> list = billWmDeliverDtlManager.findBillWmDeliverDtlByPage(page, sortColumn, sortOrder, params, authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj; 
		}catch(Exception e){
			log.error("===========查询预到货通知单明细列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
	@RequestMapping(value = "/dtl_findBillWmDeliverDtlByPage.json")
	@ResponseBody
	public  Map<String, Object> findBillWmDeliverDtlByPageDtl(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String,Object>(0);
		try{
			//obj = findBillWmDeliverDtlByPage(req, model);
			
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billWmDeliverDtlManager.findBillWmDeliverDtlCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmDeliverDtl> list = billWmDeliverDtlManager.findBillWmDeliverDtlByPage(page, sortColumn, sortOrder, params, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			
			//小计
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			for(BillWmDeliverDtl dtl:list){
				totalItemQty = totalItemQty.add(dtl.getItemQty());
			}
			footer.put("boxNo", "小计");
			footer.put("itemQty", totalItemQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billWmDeliverDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					sumFoot2.put("itemQty", sumFoot1.get("itemQty"));
					sumFoot2.put("isselectsum", true);
					sumFoot1 = sumFoot2;
				}
			}
			sumFoot1.put("boxNo", "合计");
			footerList.add(sumFoot1);
			obj.put("footer", footerList);
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return obj;
	}
}