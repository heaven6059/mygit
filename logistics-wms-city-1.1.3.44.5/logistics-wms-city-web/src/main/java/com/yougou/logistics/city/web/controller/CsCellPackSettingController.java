package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.CsCellPackSetting;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.CsCellPackSettingManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Oct 09 18:46:25 CST 2013
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
@RequestMapping("/cs_cell_pack_setting")
@ModuleVerify("25070040")
public class CsCellPackSettingController extends BaseCrudController<CsCellPackSetting> {
	@Log
	private Logger log;

	@Resource
	private CsCellPackSettingManager csCellPackSettingManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("cscellpacksetting/", csCellPackSettingManager);
	}

	@RequestMapping(value = "/addCsCellPackSetting")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addCsCellPackSetting(CsCellPackSetting setting, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			Date currentDate = new Date();
			setting.setCreatetm(currentDate);
			setting.setCreator(user.getLoginName());
			setting.setLocno(user.getLocNo());
			setting.setCreatorName(user.getUsername());
			setting.setEditorName(user.getUsername());
			setting.setEditor(user.getLoginName());
			setting.setEdittm(currentDate);
			csCellPackSettingManager.add(setting);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public String checkExist(CsCellPackSetting setting, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			setting.setLocno(user.getLocNo());
			CsCellPackSetting cur = csCellPackSettingManager.findById(setting);
			if (null != cur) {
				return "exist";
			}
			return "noexist";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/editCsCellPackSetting")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String editCsCellPackSetting(CsCellPackSetting setting, HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			setting.setEdittm(new Date());
			setting.setEditor(user.getLoginName());
			setting.setEditorName(user.getUsername());
			setting.setLocno(user.getLocNo());
			if (csCellPackSettingManager.modifyById(setting) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	@RequestMapping(value = "/deleteCsCellPackSetting")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteDefdock(String keyStr, HttpServletRequest req) {
		try {
			if (csCellPackSettingManager.deleteCsCellPackSetting(keyStr) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
	

	@RequestMapping(value = "/getByPage.json")
	@ResponseBody
	public Map<String, Object> getByPage(BillImCheckDtl billImCheckDtl, HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map params = builderParams(req, model);
			int total = this.csCellPackSettingManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<CsCellPackSetting> list = this.csCellPackSettingManager.findByPage(page, sortColumn, sortOrder, params);
			//返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("areaType", "汇总");
			footerList.add(footerMap);
			for (CsCellPackSetting temp : list) {
				this.setFooterMap("limitQty", temp.getLimitQty(), footerMap);
			}
			
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
			obj.put("footer", footerList);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}
}