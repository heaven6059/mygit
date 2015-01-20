package com.yougou.logistics.city.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Options;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.CsInstockSettingDto;
import com.yougou.logistics.city.common.model.CsInstockSetting;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.CsInstockSettingManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Oct 08 15:13:38 CST 2013
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
@RequestMapping("/cs_instock_setting")
@ModuleVerify("25070010")
public class CsInstockSettingController extends BaseCrudController<CsInstockSetting> {
	@Log
	private Logger log;
	@Resource
	private CsInstockSettingManager csInstockSettingManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("csinstocksetting/", csInstockSettingManager);
	}

	@RequestMapping(value = "/addCsInstockSetting")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public Object addCsInstockSetting(CsInstockSettingDto setting, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			Date date = new Date();
			setting.setCreatetm(date);
			setting.setCreator(user.getLoginName());
			setting.setLocno(user.getLocNo());
			setting.setCreatorName(user.getUsername());
			setting.setEditor(user.getLoginName());
			setting.setEditorName(user.getUsername());
			setting.setEdittm(date);
			if(csInstockSettingManager.addCsInstockSetting(setting)>0){
				result = "success";
			}else{
				result = "fail";
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getCause().getMessage();
			if(result.indexOf("保存失败")<0){
				result = "系统异常,保存失败!";
			}
		}
		map.put("result", result);
		return map;
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(CsInstockSettingDto setting, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			setting.setLocno(user.getLocNo());
			CsInstockSetting cur = csInstockSettingManager.findById(setting);
			if (null != cur) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editCsInstockSetting")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Object editCsInstockSetting(CsInstockSettingDto setting, HttpServletRequest req) throws ManagerException {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "";
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			setting.setEdittm(new Date());
			setting.setEditor(user.getLoginName());
			setting.setEditorName(user.getUsername());
			setting.setLocno(user.getLocNo());
			if(setting.getSameItemFlag() == null){
				setting.setSameItemFlag("");
			}
			if(setting.getEmptyCellFlag() == null){
				setting.setEmptyCellFlag("");
			}
			
			if (csInstockSettingManager.editCsInstockSetting(setting) > 0) {
				result = "success";
			} else {
				result = "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = e.getMessage();
			if(result.indexOf("保存失败")<0){
				result = "系统异常,保存失败!";
			}
		}
		map.put("result", result);
		return map;
	}

	@RequestMapping(value = "/deleteOsLineBuffer")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteOsLineBuffer(String keyStr, HttpServletRequest req) {
		try {
			if (csInstockSettingManager.deleteCsInstockSetting(keyStr) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	/**
	 * 通过编码查询商品信息
	 * @param type
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/selectItemByCode")
	@ResponseBody
	public Map<String, String> selectItemByCode(CsInstockSetting set, String settingValue) {
		Map<String, String> modelMap = null;
		try {
			modelMap = csInstockSettingManager.selectItemByCode(set, settingValue);
		} catch (Exception e) {
			modelMap.put("result", "error");
			modelMap.put("msg", "系统异常请联系管理员");
			log.error(e.getMessage(), e);
		}
		return modelMap;
	}

	/**
	 * 通过策略编号查询商品
	 * @param type
	 * @param locNo
	 * @param settingNo
	 * @return
	 */
	@RequestMapping(value = "/selectItemBySettingNo")
	@ResponseBody
	public Map<String, Object> selectItemBySettingNo(String type, String locNo, String settingNo) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			List<Map<String, String>> list = csInstockSettingManager.queryItemBySettingNo(type, locNo, settingNo);
			modelMap.put("data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelMap;
	}

	/**
	 * 通过编号查询储位
	 * @param type
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/selectCellByCode")
	@ResponseBody
	public Map<String, String> selectCellByCode(String type, String locNo, String code) {
		Map<String, String> modelMap = null;
		try {
			modelMap = csInstockSettingManager.selectCellByCode(type, locNo, code);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelMap;
	}

	/**
	 * 通过策略编号查询储位
	 * @param type
	 * @param locNo
	 * @param settingNo
	 * @return
	 */
	@RequestMapping(value = "/selectCellBySettingNo")
	@ResponseBody
	public Map<String, Object> selectCellBySettingNo(String type, String locNo, String settingNo) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			List<Map<String, String>> list = csInstockSettingManager.queryCellBySettingNo(type, locNo, settingNo);
			modelMap.put("data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelMap;
	}
}