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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
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
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmLabelFullPrint;
import com.yougou.logistics.city.common.model.OsCustBuffer;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.OsCustBufferManager;
import com.yougou.logistics.city.manager.StoreManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 门店与暂存区维护
 * @author chen.yl1
 * @date  2013-11-26 14:47:41
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
@RequestMapping("/os_cust_buffer")
@ModuleVerify("25030240")
public class OsCustBufferController extends BaseCrudController<OsCustBuffer> {
    @Resource
    private OsCustBufferManager osCustBufferManager;
    @Resource
    private StoreManager storeManager;
    
    @Log
	private Logger log;

    @Override
    public CrudInfo init() {
        return new CrudInfo("oscustbuffer/",osCustBufferManager);
    }
    
    /**
    	 * 增加品牌权限 重写base查询方法
    	 * modified by liu.t  20140415
         * @return 
    	 */
        @RequestMapping(value="/list.json")
    	@ResponseBody
    	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException{
        	Map<String, Object> obj = new HashMap<String, Object>();
        	try {
    			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    			
    			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 1 : Integer.parseInt(req.getParameter("rows"));
    			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    			
    			Map<String, Object> params = this.builderParams(req, model);
    			int total = osCustBufferManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
    			
    			SimplePage page = new SimplePage(pageNo, pageSize, total);
    			
    			List<OsCustBuffer>  list = osCustBufferManager.findByPage(page, sortColumn, sortOrder, params, authorityParams);
    			
    			obj.put("total", total);
    			obj.put("rows", list);
    			obj.put("result", "success");
    			
    		} catch (ManagerException e) {
    			// TODO Auto-generated catch block
    			obj.put("rows", "");
    			obj.put("result", "fail");
    			obj.put("msg", e.getCause().getMessage());
    			log.error(e.getMessage(),e);
    		}
    		
        
    		
    		return obj;
    }
    
    @RequestMapping(value = "/check")
	@ResponseBody
	public String checkExist(OsCustBuffer osCust, HttpServletRequest req) {
		try {
			OsCustBuffer curLine = osCustBufferManager.findById(osCust);
			if (null != curLine) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
    
    
    
    @RequestMapping(value = "/addOsCustBuffer")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addOsLineBuffer(OsCustBuffer osCust, HttpServletRequest req) {
    	try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String storeNo = req.getParameter("storeNo");
			String storeType = req.getParameter("storeType");
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if(osCust.getStoreNo()!= null){
				storeNo = osCust.getStoreNo().trim();
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("storeNo", storeNo);
			params.put("storeType", storeType);
			int total = storeManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			if(total == 1) {
				Date date = new Date();
				osCust.setStoreNo(storeNo);
				osCust.setLocno(user.getLocNo());
				osCust.setCreator(user.getLoginName());
				osCust.setCreatetm(date);
				osCust.setCreatorName(user.getUsername());//创建人中文名称
				osCust.setEditor(user.getLoginName());
				osCust.setEdittm(date);
				osCust.setEditorName(user.getUsername());//修改人中文名称
				
				osCustBufferManager.add(osCust);
				
				return "success";
			} else {
				return "nocust";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "warn";
		}
	}
    
    @RequestMapping(value = "/delOsCustBuffer")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteOsCustBuffer(String keyStr, HttpServletRequest req) {
		try {
			if (osCustBufferManager.delOsCustBuffer(keyStr) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
    
    @RequestMapping(value = "/editOsCustBuffer")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.MODIFY)
	public String editOsLineBuffer(OsCustBuffer line, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			line.setLocno(user.getLocNo());
			line.setEditor(user.getLoginName());
			line.setEditorName(user.getUsername());//修改人中文名称
			line.setEdittm(new Date());
			
			if (osCustBufferManager.modifyById(line) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
    
    
    //批量新增客户暂存区
 	@RequestMapping(value = "/insertBatch")
 	@OperationVerify(OperationVerifyEnum.ADD)
 	@ResponseBody
 	public Map<String, Object> insertBatch(OsCustBuffer custBuffer, HttpServletRequest req) {
 		Map<String, Object> map = new HashMap<String, Object>();
 		try {
 			
 			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<Store> storeList = new ArrayList<Store>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				storeList = convertListWithTypeReferenceStore(mapper, list);
			}
 			
 			HttpSession session = req.getSession();
 			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
 			custBuffer.setLocno(user.getLocNo());
 			custBuffer.setCreator(user.getLoginName());
 			custBuffer.setCreatorName(user.getUsername());//创建人中文名称
 			custBuffer.setEditor(user.getLoginName());
 			custBuffer.setEditorName(user.getUsername());//修改人中文名称
 			osCustBufferManager.insertBatch(custBuffer, storeList);
 			map.put("result", ResultEnums.SUCCESS.getResultMsg());
 		} catch (ManagerException e) {
 			log.error(e.getMessage(), e);
 			map.put("result", ResultEnums.FAIL.getResultMsg());
 			map.put("msg", e.getMessage());
 		} catch (Exception e) {
 			log.error(e.getMessage(), e);
 			map.put("result", ResultEnums.FAIL.getResultMsg());
 			map.put("msg", "保存失败,请联系管理员!");
 		}
 		return map;
 	}
    
 	@SuppressWarnings("rawtypes")
	private List<Store> convertListWithTypeReferenceStore(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<Store> tl = new ArrayList<Store>(list.size());
		for (int i = 0; i < list.size(); i++) {
			Store type = mapper.readValue(mapper.writeValueAsString(list.get(i)), Store.class);
			tl.add(type);
		}
		return tl;
	}
 	@RequestMapping(value="/getBufferBySys")
	@ResponseBody
	public Object getBufferBySys(HttpServletRequest req, Model model) throws ManagerException{
    	try {
    		Map<String, Object> params = this.builderParams(req, model);
			return osCustBufferManager.findBufferBySys(params);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return new ArrayList<BillUmLabelFullPrint>();
 	}
 	@RequestMapping(value="/fullPrintList")
	@ResponseBody
	public Object queryFullPrintList(HttpServletRequest req, Model model) throws ManagerException{
 		Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		Map<String, Object> params = this.builderParams(req, model);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 1 : Integer.parseInt(req.getParameter("rows"));
			int total = osCustBufferManager.findFullPrintCount(params);
			
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			
			List<BillUmLabelFullPrint> list = osCustBufferManager.findFullPrintByPage(page, params);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
 	}
}