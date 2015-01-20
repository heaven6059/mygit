package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillConAdjDtlManager;
import com.yougou.logistics.city.manager.BillConAdjManager;
import com.yougou.logistics.city.service.ProcCommonService;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/**
 * 库存调整控制器
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
@RequestMapping("/bill_stock_adj")
@ModuleVerify("25110010")
public class BillConAdjController extends BaseCrudController<BillConAdj> {
	@Log
	private Logger log;
	@Resource
    private BillConAdjManager billConAdjManager;
    @Resource
    private BillConAdjDtlManager billConAdjDtlManager;
    @Resource
	private ProcCommonService procCommonService;
    
    @Override
    public CrudInfo init() {
        return new CrudInfo("stockadj/",billConAdjManager);
    }
    
    /**
	 * 分页查询调整库存主表信息
	 * @param req
	 * @param model
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
    @RequestMapping(value = "/adjlist")
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
			int total = billConAdjManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConAdj> list = billConAdjManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			// 小计列表
		    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		    Map<String, Object> footerMap = new HashMap<String, Object>();
			BigDecimal totalAdjQty = new BigDecimal(0);
			for(BillConAdj billConAdj : list){
				totalAdjQty = totalAdjQty.add(billConAdj.getAdjQty());
			}
			footerMap.put("adjNo", "小计");
			footerMap.put("adjQty", totalAdjQty);
			footerList.add(footerMap);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
            if (pageNo == 1) {
                sumFoot = this.billConAdjManager.selectSumQty(params, authorityParams);
                if (sumFoot == null) {
                    sumFoot = new SumUtilMap<String, Object>();
                    sumFoot.put("item_qty", 0);
                }
                sumFoot.put("isselectsum", true);
                sumFoot.put("adj_no", "合计");
            } else {
                sumFoot.put("adjNo", "合计");
            }
            footerList.add(sumFoot);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    /**
	 * 新增主表信息
	 * @param model
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/adjadd")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addStockAdj(BillConAdj model, HttpServletRequest req)
			throws ManagerException {
		String adjNo = null;
		String flag=null;
  		try {
  			adjNo = procCommonService.procGetSheetNo(
					model.getLocno(), CNumPre.SM_ADJ_PRE);
			model.setAdjNo(adjNo);
			flag=model.getCellAdjFlag();
  		// 获取登陆用户信息
  			SystemUser user = (SystemUser) req.getSession().getAttribute(
  					PublicContains.SESSION_USER);
  			model.setLocno(user.getLocNo());
  			model.setCreator(user.getLoginName());
  			model.setCreatorName(user.getUsername());
  			model.setEditorName(user.getUsername());
  			model.setCreatetm(new Date());
  			model.setStatus("10");
  			billConAdjManager.add(model);
  		} catch (Exception e) {
  			log.error("=======新增主表异常："+e.getMessage(),e);
  			return "{\"success\":\"" + false + "\"}";
  		}
  		return "{\"success\":\"" + true + "\",\"adjNo\":\"" + adjNo + "\",\"flag\":\"" + flag + "\"}";
	}
    
    /**
     * 修改主表信息
     * @param model
     * @param req
     * @throws ManagerException
     */
    @RequestMapping(value = "/adjupdate")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String updateStockAdj(BillConAdj model, HttpServletRequest req)
			throws ManagerException {
    	String adjNo = null;
  		try {
  			adjNo = model.getAdjNo();
  			model.setEdittm(new Date());
  			model.setUpdStatus("10");
  			int count = billConAdjManager.modifyById(model);
  			if(count < 1 ){
  				return "{\"success\":\"" + "\",\"result\":\"" + count + "\",\"adjNo\":\"" + adjNo + "\"}";
  			}
  		} catch (Exception e) {
  			log.error("=======更新主表异常："+e.getMessage(),e);
  			return "{\"success\":\"" + false + "\"}";
  		}
  		return "{\"success\":\"" + true + "\",\"adjNo\":\"" + adjNo + "\"}";
	}
    
    /**
	 * 删除单据
	 * 
	 * @param model
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/adjdelete")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public Map<String, Object> deleteRecords(String ids, HttpServletRequest req) throws ManagerException{
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
			String editor = user.getLoginName();
	 		int count= billConAdjManager.deleteStockAdj(ids,editor);
	 		if(count>0){
	 			flag.put("flag", "success");
	 		}else{
	 			flag.put("flag", "fail");
	 		}
	 	}catch (Exception e) {
	 		log.error(e.getMessage(),e);
	 		flag.put("flag", "fail");
	 		flag.put("msg", e.getMessage());
	 	}
		return flag;
	}
	
	/**
	  * 审核
	  * @param ids
	  * @param req
	  * @return
	  * @throws JsonParseException
	  * @throws JsonMappingException
	  * @throws IOException
	  * @throws ManagerException
	  */
	 @RequestMapping(value = "/examineAdj")
	 @ResponseBody
	 @OperationVerify(OperationVerifyEnum.VERIFY)
	 public ResponseEntity<Map<String, Object>> examineAdj(String ids, HttpServletRequest req)
			 throws JsonParseException, JsonMappingException, IOException, ManagerException {
		 Map<String, Object> flag = new HashMap<String, Object>();
		 try {
			 AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			 CurrentUser currentUser=new CurrentUser(req);
			 flag = billConAdjDtlManager.examineAdj(ids,currentUser.getLoginName(),currentUser.getUsername(),authorityParams);
			 return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		 } catch (Exception e) {
			 flag.put("flag", "warn");
			 flag.put("msg", e.getMessage());
			 log.error("审核时异常："+e.getMessage(),e);
			 return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	   	}
	}
//	
//	/***
//	 * 审核
//	 * 
//	 * @param model
//	 * @throws ManagerException
//	*/
//	@RequestMapping(value = "/examineAdj")
//	@ResponseBody
//	@OperationVerify(OperationVerifyEnum.VERIFY)
//	public Map<String,Object> examineAdj(BillConAdj model,HttpServletRequest req) throws ManagerException{
//		
//		Map<String, Object> flag = new HashMap<String, Object>();
//		try{
//			SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
//			String auditor = user.getLoginName();
//			model.setLocno(user.getLocNo());
//			return billConAdjDtlManager.examineAdj(model, auditor);
//		}catch (Exception e) {
//			log.error("=======审核异常："+e.getMessage(),e);
//			flag.put("success", false);
//			flag.put("msg", e.getMessage());
//			return flag;
//		}
//	}
	
}