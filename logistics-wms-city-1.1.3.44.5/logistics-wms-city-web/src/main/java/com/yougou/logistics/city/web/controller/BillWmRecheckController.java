package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillWmRecheckManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/**
 * 反配复核单
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
@RequestMapping("/wmrecheck")
@ModuleVerify("25090030")
public class BillWmRecheckController extends BaseCrudController<BillWmRecheck> {
	
	@Log
	private Logger log;
	
    @Resource
    private BillWmRecheckManager billWmRecheckManager;
    
    @Resource
    private ProcCommonManager procCommonManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("wmrecheck/",billWmRecheckManager);
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
			int total = this.billWmRecheckManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmRecheck> list = this.billWmRecheckManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
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
    
    
    @RequestMapping(value = "/check")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.VERIFY)
   	public ResponseEntity<Map<String, Object>> check(String ids,String checkUser,HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
   			ManagerException {
    	Map<String,Object> flag=new HashMap<String,Object>();
    	try {
   			CurrentUser currentUser=new CurrentUser(req);
   			billWmRecheckManager.check(ids,currentUser.getLoginName(),checkUser);
   			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			flag.put("success", "false");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   		}
   	}
    
    /**
	 * 删除复核单
	 * @param datas
	 * @param req
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/deleteBillWmRecheck")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.REMOVE)
   	public ResponseEntity<Map<String, Object>> deleteBillWmRecheck(String datas,HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
   			ManagerException {
    	Map<String,Object> flag=new HashMap<String,Object>();
   		try {
   			
   			datas = URLDecoder.decode(datas,"UTF-8");
   			ObjectMapper mapper = new ObjectMapper();
   			List<BillWmRecheck> listOmRechecks = new ArrayList<BillWmRecheck>();
   			if (StringUtils.isNotEmpty(datas)) {
   				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
   				listOmRechecks=convertListWithTypeReferenceR(mapper,list);
   			}
   			billWmRecheckManager.deleteBillWmOutStockRecheck(listOmRechecks);
   			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			flag.put("success", "false");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   		}
   	}
    
	@RequestMapping(value = "/findLabelNoByRecheckNoPage")
	public ResponseEntity<Map<String,Object>> findLabelNoByRecheckNoPage(BillWmRecheck billWmRecheck, HttpServletRequest req,HttpServletResponse response) {
    	Map<String,Object> obj=new HashMap<String,Object>();
    	int total=0;
		List<BillWmRecheck> listItem = new ArrayList<BillWmRecheck>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			
			//总数
			total = billWmRecheckManager.countLabelNoByRecheckNo(billWmRecheck, authorityParams);
			
			//记录list
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			listItem = billWmRecheckManager.findLabelNoByRecheckNoPage(page, billWmRecheck, authorityParams);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		obj.put("total", total);
		obj.put("rows", listItem);
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/querySupplier")
	@ResponseBody
	public List<Supplier> querySupplier(String locno) throws ManagerException{
		return billWmRecheckManager.querySupplier(locno);
	}
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody    
	@OperationVerify(OperationVerifyEnum.ADD)
	public String saveMainInfo(HttpServletRequest req,BillWmRecheck billWmRecheck) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		String recheckNo = "";
		try {
			//获取登陆用户
			Date date=new Date();
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			recheckNo = procCommonManager.procGetSheetNo(billWmRecheck.getLocno(), CNumPre.WM_RECHECK_PRE);
			billWmRecheck.setCreatorName(user.getUsername());
			billWmRecheck.setCreator(user.getLoginName());
			billWmRecheck.setRecheckNo(recheckNo);
			billWmRecheck.setStatus("10");//新建状态
			billWmRecheck.setRecedeDate(date);
			billWmRecheck.setCreatetm(date);
			billWmRecheck.setEditor(user.getLoginName());
			billWmRecheck.setEditorName(user.getUsername());
			billWmRecheck.setEdittm(date);
			
			billWmRecheckManager.add(billWmRecheck);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "{\"success\":\"" + false + "\"}";
		}
		return "{\"success\":\"" + true + "\",\"recheckNo\":\"" + recheckNo + "\"}";
	}
	
	 	@RequestMapping(value = "/queryRecheckItem")
	   	@ResponseBody    
	   	public  List<BillWmOutstockDtl> queryRecheckItem(HttpServletRequest req, Model model) throws ManagerException {
	 		List<BillWmOutstockDtl> result = new ArrayList<BillWmOutstockDtl>(0);
	 		try{
	 			Map<String, Object> params = builderParams(req, model);
		   		result = billWmRecheckManager.queryRecheckItem(params);
	 		}catch(Exception e){
	 			log.error(e.getMessage(), e);
	 		}
	   		return result;
	   	}
	 	
	 	
	 	@RequestMapping(value = "/packageBoxRf")
		@ResponseBody    
		public ResponseEntity<Map<String, Object>> packageboxRf(HttpServletRequest req,String datas,String boxNo,String recheckNo,String locno,String recedeNo,String supplierNo,String recheckName) throws JsonParseException, JsonMappingException, IOException,
				ManagerException {
			Map<String,Object> flag=new HashMap<String,Object>();
			try {
				HttpSession session = req.getSession();
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				BillWmRecheck billWmRecheck = new BillWmRecheck();
				billWmRecheck.setLocno(locno);
				billWmRecheck.setRecheckNo(recheckNo);
				billWmRecheck.setRecedeNo(recedeNo);
				billWmRecheck.setSupplierNo(supplierNo);
				billWmRecheck.setCreator(user.getLoginName());
				billWmRecheckManager.packageBoxRf(billWmRecheck);
				flag.put("success", "true");
				return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				flag.put("success", "false");
				flag.put("msg", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			}
		}
	 	
		@RequestMapping(value = "/packagebox")
		@ResponseBody    
		@OperationVerify(OperationVerifyEnum.MODIFY)
		public ResponseEntity<Map<String, Object>> packagebox(String datas,String boxNo,String recheckNo,String locno,String supplierNo,HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
				ManagerException {
			
			Map<String,Object> flag=new HashMap<String,Object>();
			datas = URLDecoder.decode(datas,"UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			//退厂通知单详细  刚复核的数据
			List<BillWmOutstockDtlDto> dtlLst = new ArrayList<BillWmOutstockDtlDto>();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
				dtlLst=convertListWithTypeReference(mapper,list);
			}
			
			try {
				HttpSession session = req.getSession();
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				billWmRecheckManager.packageBox(dtlLst,boxNo,recheckNo,locno,supplierNo,user.getLoginName(),user.getUsername(),authorityParams);
				flag.put("success", "true");
				return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				flag.put("success", "false");
				flag.put("msg", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			}
		}
		
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private List<BillWmOutstockDtlDto> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
			List<BillWmOutstockDtlDto> tl=new ArrayList<BillWmOutstockDtlDto>(list.size());
			for (int i = 0; i < list.size(); i++) {
				BillWmOutstockDtlDto type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillWmOutstockDtlDto.class);
				tl.add(type);
			}
			return tl;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private List<BillWmRecheck> convertListWithTypeReferenceR(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
			List<BillWmRecheck> tl=new ArrayList<BillWmRecheck>(list.size());
			for (int i = 0; i < list.size(); i++) {
				BillWmRecheck type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillWmRecheck.class);
				tl.add(type);
			}
			return tl;
		}
}