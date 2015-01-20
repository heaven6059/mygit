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
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.SumUtil;
import com.yougou.logistics.city.manager.BillSmOtherinManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/*
 * 其它入库
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
@RequestMapping("/bill_sm_otherin")
@ModuleVerify("25110030")
public class BillSmOtherinController extends BaseCrudController<BillSmOtherin> {
	@Log
	private Logger log;
	
    @Resource
    private BillSmOtherinManager billSmOtherinManager;
	
	@Resource
    private ProcCommonManager procCommonManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billsmotherin/",billSmOtherinManager);
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
			//int total = this.billSmOtherinManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			int total = this.billSmOtherinManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			//List<BillSmOtherin> list = this.billSmOtherinManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			List<BillSmOtherin> list = this.billSmOtherinManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);

			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("otherinNo", "小计");
			footerList.add(footerMap);
			for (BillSmOtherin c : list) {
				SumUtil.setFooterMap("instorageQty", c.getInstorageQty(), footerMap);
			}
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				if(pageSize >= total){
					sumFoot.putAll(footerMap);
				}else{
					Map<String, Object> map = billSmOtherinManager.findSumQty(params, authorityParams);
					SumUtil.setSumMap(map, sumFoot);
				}
				sumFoot.put("otherinNo", "总计");
				footerList.add(sumFoot);
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
			obj.put("footer", footerList);
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error("=======新增其它入库异常：" + e.getMessage(), e);
		}
		return obj;
	}
    /**
	 * 保存主表信息  新增
	 * @param billumreceipt
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String saveMainInfo(BillSmOtherin billSmOtherin,HttpServletRequest req)throws ManagerException {
		String otherinNo = null;
  		try {
			CurrentUser currentUser=new CurrentUser(req);
  			otherinNo = procCommonManager.procGetSheetNo(billSmOtherin.getLocno(), CNumPre.SM_OTHERIN_PRE);
  			billSmOtherin.setOtherinNo(otherinNo);
  			billSmOtherin.setStatus("10");
			billSmOtherin.setCreator(currentUser.getLoginName());
			billSmOtherin.setCreatorName(currentUser.getUsername());
  			billSmOtherin.setCreatetm(new Date());
  			billSmOtherinManager.add(billSmOtherin);
  		} catch (Exception e) {
  			log.error("=======新增其它入库异常："+e.getMessage(),e);
  			return "{\"success\":\"" + false + "\"}";
  		}
  		return "{\"success\":\"" + true + "\",\"otherinNo\":\"" + otherinNo + "\"}";
	}
	
	/**
	 * 保存主表信息  修改
	 * @param billumreceipt
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/editMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Map<String, Object> editMainInfo(BillSmOtherin billSmOtherin,HttpServletRequest req)throws ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
  		try {
			CurrentUser currentUser=new CurrentUser(req);
			billSmOtherin.setEditor(currentUser.getLoginName());
			billSmOtherin.setEditorName(currentUser.getUsername());
  			billSmOtherin.setEdittm(new Date());
  			billSmOtherin.setUpdStatus("10");
  			int count = billSmOtherinManager.modifyById(billSmOtherin);
  			if(count >0 ){
  				flag.put("flag", "success");
  			}else{
  				flag.put("flag", "fail");
	 			flag.put("msg", "单据"+billSmOtherin.getOtherinNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
  			}
  		} catch (Exception e) {
  			log.error("=======更新其它入库异常："+e.getMessage(),e);
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
	 public Map<String,Object> deleteRecords(String ids) throws ManagerException{
		 Map<String, Object> flag = new HashMap<String, Object>();
	 	try {
	 		int count= billSmOtherinManager.deleteBatch(ids);
	 		if(count > 0){
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
	 public ResponseEntity<Map<String, Object>> checkBillOtherin(String ids, HttpServletRequest req)
			 throws JsonParseException, JsonMappingException, IOException, ManagerException {
		 Map<String, Object> flag = new HashMap<String, Object>();
		 try {
			 CurrentUser currentUser=new CurrentUser(req);
			 flag = billSmOtherinManager.checkBillSmOtherin(ids,currentUser.getLoginName(),currentUser.getUsername());
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
	    @RequestMapping(value = "/print")
		@ResponseBody
		public Map<String, Object> print(HttpServletRequest req, String nos,String locno) throws ManagerException {
			//Map<String, Object> obj = new HashMap<String, Object>();
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			return billSmOtherinManager.print(nos, locno, user);
			
		}
}