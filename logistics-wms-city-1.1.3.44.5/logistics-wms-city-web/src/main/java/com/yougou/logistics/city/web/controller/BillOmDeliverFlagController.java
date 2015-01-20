package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
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
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillOmDeliverManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

@Controller
@RequestMapping("/bill_om_deliverflag")
@ModuleVerify("25040080")
public class BillOmDeliverFlagController extends BaseCrudController<BillOmDeliver> {
	@Log
    private Logger log;
	@Resource
    private BillOmDeliverManager billOmDeliverManager;
	
	@Resource
    private ProcCommonManager procCommonManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("billomdeliverflag/",billOmDeliverManager);
    }
	
	/**
	 * 查询
	 */
	@RequestMapping(value = "/deliverlist.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model){
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			String flag = req.getParameter("flag");
			
			int total = 0;
			List<BillOmDeliver> list = null;
			if(flag != null && flag.equals("10")) {
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
				total = this.billOmDeliverManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
				SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
				list = this.billOmDeliverManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			}
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
	  public ResponseEntity<Map<String, Object>> checkBillOmDeliver(String ids, HttpServletRequest req) {
	    Map<String, Object> flag = new HashMap<String, Object>();
	    try {
	    	CurrentUser currentUser=new CurrentUser(req);
	   		flag = billOmDeliverManager.checkBillFlagOmDeliver(ids,currentUser.getLoginName());
	   		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	   	} catch (ManagerException e) {
	   		flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
	   	} catch (Exception e) {
	   		flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
	   	}
	    return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	  }
	
	/**
     * 新建主表
     * @param req
     * @param billOmDeliver
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws ManagerException
     */
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String saveMainInfo(HttpServletRequest req,BillOmDeliver billOmDeliver)  {
		String deliverNo = "";
		try {
			CurrentUser currentUser = new CurrentUser(req);
			deliverNo = procCommonManager.procGetSheetNo(billOmDeliver.getLocno(), CNumPre.OM_DELIVER);
			String carPlate = req.getParameter("carPlate1");
			if(carPlate != null) {
				carPlate = URLDecoder.decode(carPlate, "UTF-8");
				carPlate = leftTrim(carPlate.trim());
			}
			String remarks = req.getParameter("remarks1");
			if(remarks != null) {
				remarks = URLDecoder.decode(remarks, "UTF-8");
				remarks = leftTrim(remarks.trim());
			}
			billOmDeliver.setDeliverNo(deliverNo);
			billOmDeliver.setStatus("10");//新建状态
			billOmDeliver.setCarPlate(carPlate);
			billOmDeliver.setRemarks(remarks);
			billOmDeliver.setTransFlag("10");
			billOmDeliver.setCreator(currentUser.getLoginName());
			billOmDeliver.setCreatorname(currentUser.getUsername());
			billOmDeliver.setCreatetm(new Date());
			billOmDeliver.setEditor(currentUser.getLoginName());
			billOmDeliver.setEditorname(currentUser.getUsername());
			billOmDeliver.setEdittm(new Date());
			billOmDeliverManager.add(billOmDeliver);
			return "{\"success\":\"" + true + "\",\"deliverNo\":\"" + deliverNo + "\"}";
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "{\"success\":\"" + false + "\"}";
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
	 public ResponseEntity<Map<String, Object>>  deleteRecords(String ids) throws ManagerException{
		 Map<String, Object> flag = new HashMap<String, Object>();
	 	try {
	 		int count= billOmDeliverManager.deleteBatch(ids);
	 		if (count > 0) {
				flag.put("success", "true");
			} else {
				flag.put("success", "false");
				flag.put("msg", "未删除到数据！");
			}
	 	}catch (ManagerException e) {
	 		log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
	 	}catch (Exception e) {
	 		log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
	 	}
	 	return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
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
