package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.baseinfo.StoreDTO;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.StoreSimple;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.StoreManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/store")
@ModuleVerify("25030150")
public class StoreController extends BaseCrudController<Store> {
	@Resource
	private StoreManager storeManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("store/", storeManager);
	}

	@Log
	private Logger log;

	/**
	 * 增加品牌权限 重写base查询方法 modified by liu.t 20140414
	 */
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil
					.getAuthorityParams(req);
			// 确定页面参数
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
					: Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
					: Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
					: String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
					: String.valueOf(req.getParameter("order"));
			// 构建参数体
			Map<String, Object> params = builderParams(req, model);
			// 获取查询结果总条数
			int total = this.storeManager.findCount(params, authorityParams,
					DataAccessRuleEnum.BRAND);
			// 生成页面对象
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			// 将分页结果返回给model
			List<Store> list = this.storeManager.findByPage(page, sortColumn,
					sortOrder, params, authorityParams);
			// 组装结果集Map[数据总条数,数据行,结果标识]
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");

		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 获取树数据
	 * 
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryStoreTree")
	public ModelAndView queryStoreTree(String id) {
		ModelAndView result = new ModelAndView("jsonView");
		try {
			List<StoreDTO> tempList = storeManager.queryStoreListByParentNo(id);
			result.addObject(tempList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 通过客户名称查找对应客户编码
	 * 
	 * @param storeName
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkStore")
	public Map<String, Object> checkCust(HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String storeName = req.getParameter("storeName");
			Store check = storeManager.queryStoreNo(storeName);
			if (null != check) {
				String storeNo = check.getStoreNo();
				result.put("result", "success");
				result.put("storeNo", storeNo);
			} else {
				result.put("result", "fail");
			}
		} catch (ManagerException e) {
			result.put("result", "fail");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			result.put("result", "fail");
			log.error(e.getMessage(), e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/listByName.josn")
	public Map<String, Object> getByStoreName(HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String storeName = req.getParameter("storeName");
			String status = req.getParameter("status");
			String storeType = req.getParameter("storeType");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("storeName", storeName);
			map.put("status", status);
			map.put("storeType", storeType);
			List<Store> list = storeManager.selectByStoreName(map);
			if (null != list) {
				result.put("total", list.size());
				result.put("rows", list);
			} else {
				result.put("total", 0);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/doExport")
	public void doExport(HttpServletRequest req, Model model,
			HttpServletResponse response) throws ManagerException {
		Map<String, Object> params = builderParams(req, model);
		String exportColumns = (String) params.get("exportColumns");
		String fileName = (String) params.get("fileName");
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(exportColumns)) {
			try {
				AuthorityParams authorityParams = UserLoginUtil
						.getAuthorityParams(req);
				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";

				// 字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns,
						new TypeReference<List<Map>>() {
						});

				// List<ModelType> list= this .manager .findByBiz(modelType,
				// params);
				int total = this.storeManager.findCount(params,
						authorityParams, DataAccessRuleEnum.BRAND);
				SimplePage page = new SimplePage(1, total, (int) total);
				List<Store> list = this.storeManager.findByPage(page, "", "",
						params, authorityParams, DataAccessRuleEnum.BRAND);
				List<Map> listArrayList = new ArrayList<Map>();
				if (list != null && list.size() > 0) {
					for (Store vo : list) {
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

		} else {

		}

	}

	@RequestMapping(value = "/get_all")
	@ResponseBody
	public List<StoreSimple> getBiz(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> params = builderParams(req, model);
		return this.storeManager.selectAll4Simple(params);
	}

	@RequestMapping(value = "/get_Circle")
	@ResponseBody
	public List<Store> getCircle(HttpServletRequest req, Model model)
			throws ManagerException {
		return this.storeManager.queryCircle();
	}
	
	@RequestMapping(value = "/getBizByBrand")
	@ResponseBody
	public List<Store> getBizByBrand(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> params = builderParams(req, model);
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		return this.storeManager.findByParamsByBrand(params, authorityParams);
	}
	
	
	@RequestMapping(value = "/getWarehouse")
	@ResponseBody
	public Map<String, Object> getWarehouse(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> map = new HashMap<String, Object>();
		Object u = req.getSession().getAttribute(PublicContains.SESSION_USER);
		Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
		Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
		SystemUser user;
		if(u != null && systemId != null && areaSystemId != null){
			user = (SystemUser)u;
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("userId", user.getUserid());
			params.put("organizationType", 22);
			params.put("systemId", systemId);
			params.put("areaSystemId", areaSystemId);
			params.put("status", 1);
			params.put("locno",user.getLocNo());
			map.put("storeNolist", this.storeManager.selectWarehouseListByLocno(params));
		}
		return map;
	}
}