package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yougou.logistics.city.common.enums.BillConStoreLockEnums;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillConStorelockManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FileUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

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

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
@RequestMapping("/bill_con_storelock")
@ModuleVerify("25110040")
public class BillConStorelockController extends BaseCrudController<BillConStorelock> {
	
	@Log
    private Logger log;
	
    @Resource
    private BillConStorelockManager billConStorelockManager;
    
    @Resource
    private ProcCommonManager procCommonManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billconstorelock/",billConStorelockManager);
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
			int total = this.billConStorelockManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConStorelock> list = this.billConStorelockManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			//小计
			List<Map<String,Object>> footerList = new ArrayList<Map<String,Object>>();
			Map<String,Object> footerMap = new HashMap<String,Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			for (BillConStorelock billConStorelock : list) {
				totalItemQty = totalItemQty.add(billConStorelock.getItemQty());
			}
			footerMap.put("storelockNo", "小计");
			footerMap.put("itemQty", totalItemQty);
			footerList.add(footerMap);
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
            if (pageNo == 1) {
                sumFoot = this.billConStorelockManager.selectSumQty(params, authorityParams);
                if (sumFoot == null) {
                    sumFoot = new SumUtilMap<String, Object>();
                    sumFoot.put("item_qty", 0);
                }
                sumFoot.put("isselectsum", true);
                sumFoot.put("storelock_no", "合计");
            } else {
                sumFoot.put("storelockNo", "合计");
            }
            footerList.add(sumFoot);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
			obj.put("footer", footerList);
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    
    /**
     * 新建主表
     * @param req
     * @param BillConStorelock
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ManagerException
     */
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String, Object>> saveMainInfo(HttpServletRequest req,BillConStorelock storelock) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			CurrentUser currentUser=new CurrentUser(req);
			String storelockNo = procCommonManager.procGetSheetNo(storelock.getLocno(), CNumPre.CON_STORELOCK_PRE);
			storelock.setStorelockNo(storelockNo);
			storelock.setCreatetm(new Date());
			storelock.setCreatorName(currentUser.getUsername());
			storelock.setEditor(currentUser.getLoginName());
			storelock.setEditorName(currentUser.getUsername());
			storelock.setEdittm(new Date());
			if(BillConStoreLockEnums.STORELOCK_TYPE1.getStatus().equals(storelock.getStorelockType())){
				storelock.setStoreNo("");
			}else{
				storelock.setSourceType("0");
			}
			billConStorelockManager.add(storelock);
			flag.put("result", "success");
			flag.put("storelockNo", storelockNo);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
   			log.error("保存客户库存锁定异常："+e.getMessage(),e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	/**
     * 审核
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/auditStorelock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> auditStorelock(HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillConStorelock> listConStoreLocks = new ArrayList<BillConStorelock>();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>(){});
				listConStoreLocks=convertListWithTypeReference(mapper,list);
			}
			billConStorelockManager.auditStorelock(listConStoreLocks);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	
	/**
     * 手工关闭
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/overStoreLock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> overStoreLock(HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			List<BillConStorelock> listConStoreLocks = new ArrayList<BillConStorelock>();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>(){});
				listConStoreLocks=convertListWithTypeReference(mapper,list);
			}
			billConStorelockManager.overStoreLock(listConStoreLocks);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	
	/**
     * 删除
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delStorelock")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public ResponseEntity<Map<String, Object>> delStorelock(HttpServletRequest req) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			List<BillConStorelock> listConStoreLocks = new ArrayList<BillConStorelock>();
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
				listConStoreLocks=convertListWithTypeReference(mapper,list);
			}
			billConStorelockManager.delStorelock(listConStoreLocks);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	/**
     * 转退厂申请
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/toWmRequest")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> toWmRequest(HttpServletRequest req,BillConStorelock conStorelock) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
			billConStorelockManager.toWmRequest(conStorelock);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillConStorelock> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillConStorelock> tl=new ArrayList<BillConStorelock>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillConStorelock type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillConStorelock.class);
			tl.add(type);
		}
		return tl;
	}
}