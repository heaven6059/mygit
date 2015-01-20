package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillSmWasteManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/**
 * 库存报损
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
@RequestMapping("/bill_sm_waste_direct")
@ModuleVerify("25080070")
public class BillSmWasteDirectController extends BaseCrudController<BillSmWaste> {
	
	@Log
	private Logger log;
	
	@Resource
    private ProcCommonManager procCommonManager;
	
    @Resource
    private BillSmWasteManager billSmWasteManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billsmwastedirect/",billSmWasteManager);
    }
    
    @RequestMapping(value = "/d_list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
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
			
			String brandNo = "";
			String sysNo = "";
			if(paramsAll.get("brandNo") != null) {
				brandNo = paramsAll.get("brandNo").toString();
			}
			if(paramsAll.get("sysNo") != null) {
				sysNo = paramsAll.get("sysNo").toString();
			}
			if(!brandNo.equals("") || !sysNo.equals("")) {
				params.put("joinIn", "true");
			}
			
			int total = billSmWasteManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmWaste> list = billSmWasteManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			// 返回小计列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("wasteNo", "小计");
			footerList.add(footerMap);
			for (BillSmWaste temp : list) {
				FooterUtil.setFooterMap("wasteQty", temp.getWasteQty(), footerMap);
			}
			// 返回合计列表
		    Map<String, Object> sumFootMap = new HashMap<String, Object>();
		    if (pageNo == 1) {
				if(pageSize >= total){
					sumFootMap.putAll(footerMap);
				}else{
					Map<String, Object> sumFoot = billSmWasteManager.findSumQty(params,authorityParams);
					if(sumFoot.get("WASTE_QTY")!= null) {
						sumFootMap.put("wasteQty", sumFoot.get("WASTE_QTY"));
					} else {
						 sumFootMap.put("wasteQty", 0);
					}
					sumFootMap.put("isselectsum", true);
				}
				sumFootMap.put("wasteNo", "合计");
			    footerList.add(sumFootMap); 
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    /**
	 * 保存主表信息
	 * @param billumreceipt
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String saveMainInfo(BillSmWaste billSmWaste,HttpServletRequest req)throws ManagerException {
		String wasteNo = null;
  		try {
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
  			wasteNo = procCommonManager.procGetSheetNo(billSmWaste.getLocno(), CNumPre.SM_WASTE_DIRECT_PRE);
  			billSmWaste.setWasteNo(wasteNo);
  			billSmWaste.setStatus("10");
  			billSmWaste.setCreatorname(user.getUsername());
  			billSmWaste.setCreator(user.getLoginName());
  			billSmWaste.setCreatetm(new Date());
  			billSmWaste.setEditor(user.getLoginName());
  			billSmWaste.setEditorname(user.getUsername());
  			billSmWaste.setEdittm(new Date());
  			billSmWasteManager.add(billSmWaste);
  		} catch (Exception e) {
  			log.error("=======新增库存报损主表异常："+e.getMessage(),e);
  			return "{\"success\":\"" + false + "\"}";
  		}
  		return "{\"success\":\"" + true + "\",\"wasteNo\":\"" + wasteNo + "\"}";
	}
	
	/**
	 * 保存主表信息
	 * @param billumreceipt
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/editMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Map<String, Object> editMainInfo(BillSmWaste billSmWaste,HttpServletRequest req)throws ManagerException {
	    Map<String, Object> flag = new HashMap<String, Object>();
  		try {
  			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billSmWaste.setEditor(user.getLoginName());
  			billSmWaste.setEditorname(user.getUsername());
			billSmWaste.setEdittm(new Date());
			billSmWaste.setUpdStatus("10");
  			int count = billSmWasteManager.modifyById(billSmWaste);
  	  	    if(count > 0){
  	  	        flag.put("flag", "success");
  	  	    }else{
  	  	        flag.put("flag", "fail");
  	  	        flag.put("msg", "单据"+billSmWaste.getWasteNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
  	  	    }
  		} catch (Exception e) {
  			log.error("=======更细库存报损主表异常："+e.getMessage(),e);
  			flag.put("flag", "fail");
  			flag.put("msg", e.getMessage());
  		}
  		return flag;
	}
	
	/**
	 * 删除主表和主表明细
	 * @param ids
	 * @return
	 * @throws ManagerException
	 */
	 @RequestMapping(value="/delete_records")
	 @ResponseBody
	 @OperationVerify(OperationVerifyEnum.REMOVE)
	 public Map<String, Object> deleteRecords(String ids) throws ManagerException{
		 Map<String, Object> flag = new HashMap<String, Object>();
	 	try {
	 		int count= billSmWasteManager.deleteBatch(ids);
	 		if(count>0){
	 			flag.put("flag", "success");
	 		}else{
	 			flag.put("flag", "fail");
	 			flag.put("msg", "删除失败！");
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
	 @RequestMapping(value = "/check")
	 @ResponseBody
	 @OperationVerify(OperationVerifyEnum.VERIFY)
	 public ResponseEntity<Map<String, Object>> checkBillSmWaste(String ids, HttpServletRequest req)
			 throws JsonParseException, JsonMappingException, IOException, ManagerException {
		 Map<String, Object> flag = new HashMap<String, Object>();
		 try {
			 AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			 CurrentUser currentUser=new CurrentUser(req);
			 flag = billSmWasteManager.checkBillSmWaste(ids,currentUser.getLoginName(),currentUser.getUsername(), authorityParams, CNumPre.SM_WASTE_DIRECT_PRE);
			 return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		 } catch (Exception e) {
			 flag.put("flag", "warn");
			 flag.put("msg", e.getMessage());
			 log.error("审核时异常："+e.getMessage(),e);
			 return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	   	}
	}
	    
	 /**
	  * 去前端空格
	  * @param s
	  * @return
	  */
	    public static String leftTrim(String s) {
	        if (s == null || s.trim().length() == 0)
	            return null;
	        if (s.trim().length() == s.length())
	            return s;
	        if (!s.startsWith(" ")) {
	            return s;
	        } else {
	            return s.substring(s.indexOf(s.trim().substring(0, 1)));
	        }
	    }
	
}