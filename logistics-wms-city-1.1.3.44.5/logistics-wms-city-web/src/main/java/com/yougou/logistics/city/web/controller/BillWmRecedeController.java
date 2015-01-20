package com.yougou.logistics.city.web.controller;

import java.text.SimpleDateFormat;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillWmRecedeManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 退厂通知单
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
@RequestMapping("/wmrecede")
@ModuleVerify("25090040")
public class BillWmRecedeController extends BaseCrudController<BillWmRecede> {
	
    @Log
    private Logger log;
    
    @Resource
    private BillWmRecedeManager billWmRecedeManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("wmrecede/",billWmRecedeManager);
    }
    
    /**
     * 退厂通知单调度
     * @return
     */
    @RequestMapping(value = "/toBillWmRecedeDispatch")
	@InitpowerInterceptors
	public String toBillWmRecedeDispatch() {
		return "billWmRecedeDispatch/list";
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
			if(paramsAll.get("brandNo") != null) {
				brandNo = paramsAll.get("brandNo").toString();
			}
			if(!brandNo.equals("")) {
				params.put("joinIn", "true");
			}
			
			int total = this.billWmRecedeManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherin> list = this.billWmRecedeManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(),e);
		}
		return obj;
	}
	
	@RequestMapping(value = "/listDispatch.json")
	@ResponseBody
	public Map<String, Object> queryDispatch(HttpServletRequest req, Model model) throws ManagerException {
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
			params.put("joinIn", "true");
			
			int total = this.billWmRecedeManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillSmOtherin> list = this.billWmRecedeManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(),e);
		}
		return obj;
	}
    /**
     * 新增退厂通知单
     * @param billWmRecede
     * @param req
     * @return
     * @throws ManagerException
     */
	@RequestMapping(value = "/addWmRecede")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String, Object>> addWmRecede(BillWmRecede billWmRecede,HttpServletRequest req)throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		    //修改人
		    billWmRecede.setCreator(user.getLoginName());
		    billWmRecede.setEditor(user.getLoginName());
		    billWmRecede.setEditorName(user.getUsername());
		    billWmRecede.setCreatorName(user.getUsername());
		    billWmRecede.setLocno(user.getLocNo());
		    obj = billWmRecedeManager.addWmRecede(billWmRecede);
		}catch(Exception e){
			log.error("=======新增退厂通知单异常："+e.getMessage(),e);
			obj.put("returnMsg", false);
            //throw new ManagerException(e);
        }
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	/**
	 * 审核退厂通知单
	 * @param recedeNo
	 * @param locno
	 * @param ownerNo
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value="/auditWmrecede")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> auditWmrecede(String noStrs, HttpServletRequest req) throws ManagerException{
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			obj = billWmRecedeManager.auditWmrecede(noStrs,user.getLocNo(),user.getLoginName(),user.getUsername());
		}catch (Exception e) {
			log.error("======审核退厂通知单异常："+e.getMessage(),e);
			obj.put("flag", "false");
            //throw new ManagerException(e);
        }
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	/**
	 * 修改退厂通知单
	 * @param billWmRecede
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyWmrecede")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String modifyWmrecede(BillWmRecede billWmRecede, HttpServletRequest req)throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billWmRecede.setEdittm(new Date());
			billWmRecede.setEditor(user.getLoginName());
			billWmRecede.setEditorName(user.getUsername());
			billWmRecede.setLocno(user.getLocNo());
			if (billWmRecedeManager.modifyById(billWmRecede) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error("===========修改退厂通知单时异常："+e.getMessage(),e);
			return "fail";
		}
	}
	
	/**
	 * 删除退厂通知单
	 * @param noStrs
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value="/deleteWmrecede")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteWmrecede(String noStrs) throws ManagerException{
		String m="";
		try {
			boolean isSuccess= billWmRecedeManager.deleteWmrecede(noStrs);
			if(isSuccess){
				m="success";
			}else{
				m="fail";
			}
		}catch (Exception e) {
			log.error("=======删除退厂通知单异常："+e.getMessage(),e);
			m="fail:"+e.getMessage();
            //throw new ManagerException(e);
        }
	    return m;
	}
	
   	
   	
   	/**
	 * 查询单明细列表（带分页）
	 */
   	@RequestMapping(value = "/findBillWmRecedeGroupByPage.json")
	@ResponseBody
	public  Map<String, Object> findBillWmRecedeGroupByPage(HttpServletRequest req, BillWmRecede billWmRecede) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			if(billWmRecede!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String createTmStart = billWmRecede.getCreateTmStart();
			String createTmEnd = billWmRecede.getCreateTmEnd();
			if(StringUtils.isNotBlank(createTmStart)){
				if(createTmStart.length() <= 10){
					createTmStart+= " 00:00:00";
				}
				billWmRecede.setCreatetm(sdf.parse(createTmStart));
			}
			if(StringUtils.isNotBlank(createTmEnd)){
				if(createTmEnd.length() <= 10){
					createTmEnd+= " 23:59:59";
				}
				billWmRecede.setCreatetm(sdf.parse(createTmEnd));
			}
			}
			int total = billWmRecedeManager.findBillWmRecedeGroupCount(billWmRecede, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmRecede> list = billWmRecedeManager.findBillWmRecedeGroupByPage(page, sortColumn, sortOrder, billWmRecede, authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		}catch (Exception e) {
			log.error("===========查询复核退厂调度列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
   	/**
	 * datagrid选中时，就导出当前选中的记录，不导出全部数据
	 * @param modelType
	 * @param req
	 * @param model
	 * @param response
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/doExportByRecedeNo")
	public void doExportByRecedeNo(BillWmRecede modelType,
			HttpServletRequest req, Model model, HttpServletResponse response)
			throws ManagerException {
		Map<String, Object> params = builderParams(req, model);
		String exportColumns = (String) params.get("exportColumns");
		String fileName = (String) params.get("fileName");
		Map<String,Object> _map = new HashMap<String, Object>();
		_map.putAll(params);
		//退厂确认单据编号组
		String recedeNostemp = (String) req.getParameter("recedeNos");
		String [] recedeNos={};
		if(StringUtils.isNotEmpty(recedeNostemp)){
			recedeNos = recedeNostemp.split(",");
			_map.put("recedeNos", recedeNos);
		}
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(exportColumns)) {
			try {
				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";
				//字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns,new TypeReference<List<Map>>() {});
				int total = this.billWmRecedeManager.findCount(_map);
				SimplePage page = new SimplePage(1, total, (int) total);
				List<BillWmRecede> list = this.billWmRecedeManager
						.findByPage(page, "", "", _map);
				List<Map> listArrayList = new ArrayList<Map>();
				if (list != null && list.size() > 0) {
					for (BillWmRecede vo : list) {
						Map map = new HashMap();
						BeanUtilsCommon.object2MapWithoutNull(vo, map);
						listArrayList.add(map);

					}
					HSSFExport.commonExportData(StringUtils
							.isNotEmpty(fileName) ? fileName : "导出信息",
							ColumnsList, listArrayList, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}