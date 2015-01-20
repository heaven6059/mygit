package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.AuthorityModule;
import com.yougou.logistics.city.common.vo.AuthorityModuleVo;
import com.yougou.logistics.city.common.vo.AuthorityModuleWithOperationsVo;
import com.yougou.logistics.city.manager.AuthorityModuleManager;

@Controller
@RequestMapping("/authority_module")
public class AuthorityModuleController extends BaseCrudController<AuthorityModule> {
	@Resource
	private AuthorityModuleManager authorityModuleManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("authority_module/", authorityModuleManager);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
				.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
				.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = this.authorityModuleManager.findCount(params);
		List<AuthorityModuleWithOperationsVo> list = this.authorityModuleManager.findByPageWithOPerations(
				new SimplePage(pageNo, pageSize, total), sortColumn, sortOrder, params);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		return obj;
	}

	@RequestMapping(value = "/get_is_exist.json")
	@ResponseBody
	public Map<String, Boolean> isExist(AuthorityModule module) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		try {
			boolean exist = this.authorityModuleManager.findModuleIsExistByName(module);
			map.put("success", exist);
		} catch (Exception e) {
			map.put("success", false);
		}
		return map;
	}

	@RequestMapping(value = "/get_all_used_list.json")
	@ResponseBody
	public AuthorityModuleVo getAllUsedList(@RequestParam("menuId") int menuId) {
		AuthorityModuleVo vo = null;
		try {
			vo = this.authorityModuleManager.findAllModulesAndUsedModules(menuId);
			return vo;
		} catch (ManagerException e) {
			return vo;
		}
	}

	public ResponseEntity<Map<String, Boolean>> save(HttpServletRequest req) throws JsonParseException,
			JsonMappingException, IOException, ManagerException {
		Map<String, Boolean> flag = new HashMap<String, Boolean>();

		String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
		String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
		ObjectMapper mapper = new ObjectMapper();
		Map<CommonOperatorEnum, List<AuthorityModuleWithOperationsVo>> params = new HashMap<CommonOperatorEnum, List<AuthorityModuleWithOperationsVo>>();
		if (StringUtils.isNotEmpty(deletedList)) {
			List<AuthorityModuleWithOperationsVo> list = mapper.readValue(deletedList, new TypeReference<List<AuthorityModuleWithOperationsVo>>() {
			});
			params.put(CommonOperatorEnum.DELETED, list);
		}
		if (StringUtils.isNotEmpty(upadtedList)) {
			List<AuthorityModuleWithOperationsVo> list = mapper.readValue(upadtedList, new TypeReference<List<AuthorityModuleWithOperationsVo>>() {
			});
			params.put(CommonOperatorEnum.UPDATED, list);
		}
		if (StringUtils.isNotEmpty(insertedList)) {
			List<AuthorityModuleWithOperationsVo> list = mapper.readValue(insertedList, new TypeReference<List<AuthorityModuleWithOperationsVo>>() {
			});
			params.put(CommonOperatorEnum.INSERTED, list);
		}
		authorityModuleManager.saveMoudleWithOperations(params);
		flag.put("success", true);
		return new ResponseEntity<Map<String, Boolean>>(flag, HttpStatus.OK);
	}

}