package com.yougou.logistics.city.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmDefreport;
import com.yougou.logistics.city.common.tempdata.TempFile;
import com.yougou.logistics.city.manager.BmDefreportManager;

@Controller
@RequestMapping("/bm_defreport")
@ModuleVerify("25030250")
public class BmDefreportController extends BaseCrudController<BmDefreport> {
	@Log
	private Logger log;
	@Resource
	private BmDefreportManager bmDefreportManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("bmdefreport/", bmDefreportManager);
	}
	@RequestMapping(value = "/iframe")
	@InitpowerInterceptors
	public String iframe() {
		return "bmdefreport/iframe";
	}
	/**
	 * 新建
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addSave", method = { RequestMethod.POST })
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public Object addSave(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		String reportNo = request.getParameter("reportNo");
		String reportName = request.getParameter("reportName");
		String moduleid = request.getParameter("moduleid");
		String status = request.getParameter("status");
		String locno = request.getParameter("locno");
		String loginName = request.getParameter("loginName");
		byte [] reportValue = TempFile.readFile(locno+loginName, reportName);
		BmDefreport bmDefreport = new BmDefreport();
		bmDefreport.setReportNo(reportNo);
		bmDefreport.setReportName(reportName);
		bmDefreport.setModuleid(moduleid);
		bmDefreport.setStatus(status);
		bmDefreport.setReportValue(reportValue);
		bmDefreport.setCreator(loginName);
		bmDefreport.setCreatetm(new Date());
		try {
			int a = bmDefreportManager.add(bmDefreport);
			if(a<1){
				result = "系统异常,请联系管理员!";
			}else{
				TempFile.removeByUser(locno+loginName);
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = "系统异常,请联系管理员!";
		}
		map.put("result", result);
		return map;
	}
	/**
	 * 修改
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editSave", method = { RequestMethod.POST })
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Object editSave(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		String reportNo = request.getParameter("reportNo");
		String reportName = request.getParameter("reportName");
		String moduleid = request.getParameter("moduleid");
		String status = request.getParameter("status");
		String locno = request.getParameter("locno");
		String loginName = request.getParameter("loginName");
		byte [] reportValue = TempFile.readFile(locno+loginName, reportName);
		BmDefreport bmDefreport = new BmDefreport();
		bmDefreport.setReportNo(reportNo);
		bmDefreport.setReportName(reportName);
		bmDefreport.setModuleid(moduleid);
		bmDefreport.setStatus(status);
		bmDefreport.setReportValue(reportValue);
		bmDefreport.setEditor(loginName);
		bmDefreport.setEdittm(new Date());
		try {
			int a = bmDefreportManager.modifyById(bmDefreport);
			if(a<1){
				result = "系统异常,请联系管理员!";
			}else{
				TempFile.removeByUser(locno+loginName);
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = "系统异常,请联系管理员!";
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/deleteReport")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public Object deleteReport(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		String reportNo = request.getParameter("reportNo");
		BmDefreport bmDefreport = new BmDefreport();
		bmDefreport.setReportNo(reportNo);
		try {
			int a = bmDefreportManager.deleteById(bmDefreport);
			if(a<1){
				result = "系统异常,请联系管理员!";
			}else{
				result = "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = "系统异常,请联系管理员!";
		}
		map.put("result", result);
		return map;
	}
	@RequestMapping(value = "/upLoad")
	@InitpowerInterceptors
	public String upLoad(HttpServletRequest request,Model model) {
		try{
			String fileName = request.getParameter("fileName");
			String locno = request.getParameter("locno");
			String loginName = request.getParameter("loginName");
			byte [] reportValue = returnUploadPath(request);
			TempFile.writeFile(locno+loginName, fileName, reportValue);
			model.addAttribute("fileName", fileName);
			model.addAttribute("status", "success");
			return "bmdefreport/iframe";
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return "";
		}
	}

	private byte[] returnUploadPath(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String excelRealPathDir = System.getProperty("user.dir")+"/work/tmp";
		//String name = request.getParameter("reportValue");
		File excelSaveFile = new File(excelRealPathDir);
		if (!excelSaveFile.exists()) {
			excelSaveFile.mkdirs();
		}
		MultipartFile multipartFile = multipartRequest.getFile("reportValue");
		try {
			return multipartFile.getBytes();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}