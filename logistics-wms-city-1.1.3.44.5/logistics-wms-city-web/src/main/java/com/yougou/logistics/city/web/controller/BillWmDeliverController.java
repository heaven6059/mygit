package com.yougou.logistics.city.web.controller;

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
import com.yougou.logistics.city.common.model.BillWmDeliver;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillWmDeliverManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 退厂确认（配送单）
 * 
 * @author zuo.sw
 * @date 2013-10-16 10:44:50
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
@Controller
@RequestMapping("/wmdeliver")
@ModuleVerify("25090020")
public class BillWmDeliverController extends BaseCrudController<BillWmDeliver> {

	@Log
	private Logger log;

	@Resource
	private BillWmDeliverManager billWmDeliverManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("wmdeliver/", billWmDeliverManager);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil
					.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
					: Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
					: Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
					: String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
					: String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billWmDeliverManager.findCount(params,
					authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmDeliver> list = this.billWmDeliverManager.findByPage(
					page, sortColumn, sortOrder, params, authorityParams,
					DataAccessRuleEnum.BRAND);
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

	/**
	 * 删除退厂配送单
	 * 
	 * @param noStrs
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/deleteWmDeliver")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteWmDeliver(String noStrs) throws ManagerException {
		String m = "";
		try {
			boolean isSuccess = billWmDeliverManager.deleteWmDeliver(noStrs);
			if (isSuccess) {
				m = "success";
			} else {
				m = "fail";
			}
		} catch (Exception e) {
			log.error("=======删除退厂配送单异常：" + e.getMessage(), e);
			m = "fail:" + e.getMessage();
			// throw new ManagerException(e);
		}
		return m;
	}

	/**
	 * 新增退厂配送单
	 * 
	 * @param billImImport
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/addWmDeliver")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String, Object>> addWmDeliver(
			BillWmDeliver billWmDeliver, HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			// 获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session
					.getAttribute(PublicContains.SESSION_USER);
			// 设置操作人
			billWmDeliver.setCreator(user.getLoginName());
			billWmDeliver.setEditor(user.getLoginName());
			billWmDeliver.setEditorName(user.getUsername());
			// 设置创建人中文名称
			billWmDeliver.setCreatorName(user.getUsername());
			billWmDeliver.setLocno(user.getLocNo());
			obj = billWmDeliverManager.addWmDeliver(billWmDeliver);
		} catch (Exception e) {
			log.error("=======新增退厂配送单异常：" + e.getMessage(), e);
			obj.put("returnMsg", false);
			// throw new ManagerException(e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	/**
	 * 修改退厂配送单
	 * 
	 * @param billImImport
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyWmDeliver")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String modifyWmDeliver(BillWmDeliver billWmDeliver,
			HttpServletRequest req) throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session
					.getAttribute(PublicContains.SESSION_USER);
			billWmDeliver.setEdittm(new Date());
			billWmDeliver.setEditor(user.getLoginName());
			billWmDeliver.setEditorName(user.getUsername());
			billWmDeliver.setLocno(user.getLocNo());
			if (billWmDeliverManager.modifyById(billWmDeliver) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error("===========修改退厂配送单时异常：" + e.getMessage(), e);
			return "fail";
		}
	}

	/**
	 * 审核退厂配送单
	 * 
	 * @param importNo
	 * @param locno
	 * @param ownerNo
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/auditWmDeliver")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> auditWmDeliver(String noStrs,
			HttpServletRequest req) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session
					.getAttribute(PublicContains.SESSION_USER);
			obj = billWmDeliverManager.auditWmDeliver(noStrs, user.getLocNo(),
					user.getLoginName(),user.getUsername());
		} catch (Exception e) {
			log.error("=======审核退厂配送单异常：" + e.getMessage(), e);
			obj.put("flag", "warn");
			obj.put("resultMsg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	/**
	 * datagrid选中时，就导出当前选中的记录，不导出全部数据
	 * @param modelType
	 * @param req
	 * @param model
	 * @param response
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/doExportByDeliverNo")
	public void doExportByDeliverNo(BillWmDeliver modelType,
			HttpServletRequest req, Model model, HttpServletResponse response)
			throws ManagerException {
		Map<String, Object> params = builderParams(req, model);
		String exportColumns = (String) params.get("exportColumns");
		String fileName = (String) params.get("fileName");
		Map<String,Object> _map = new HashMap<String, Object>();
		_map.putAll(params);
		//退厂确认单据编号组
		String deliverNotemp = (String) req.getParameter("deliverNos");
		String [] deliverNos={};
		if(StringUtils.isNotEmpty(deliverNotemp)){
			deliverNos = deliverNotemp.split(",");
			_map.put("deliverNos", deliverNos);
		}
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(exportColumns)) {
			try {
				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";
				//字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns,new TypeReference<List<Map>>() {});
				int total = this.billWmDeliverManager.findCount(_map);
				SimplePage page = new SimplePage(1, total, (int) total);
				List<BillWmDeliver> list = this.billWmDeliverManager
						.findByPage(page, "", "", _map);
				List<Map> listArrayList = new ArrayList<Map>();
				if (list != null && list.size() > 0) {
					for (BillWmDeliver vo : list) {
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