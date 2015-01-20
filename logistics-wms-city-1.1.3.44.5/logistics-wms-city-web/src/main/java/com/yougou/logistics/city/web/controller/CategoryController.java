package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.CategorySimpleDto;
import com.yougou.logistics.city.common.dto.baseinfo.CategoryDTO;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.manager.CategoryManager;
import com.yougou.logistics.city.manager.ItemManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/category")
@ModuleVerify("25030070")
public class CategoryController extends BaseCrudController<Category> {
	@Log
	private Logger log;
	@Resource
	private CategoryManager categoryManager;
	@Resource
	private ItemManager itemManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("category/", categoryManager);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.categoryManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<Category> list = this.categoryManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
	/**
	 * 获取树数据
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryCategoryTree")
	public ModelAndView queryCategoryTree(String id) {
		ModelAndView result = null;
		try {
			result = new ModelAndView("jsonView");
			List<CategoryDTO> tempList = categoryManager.queryCategoryListByParentNo(id, null);
			result.addObject(tempList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 获取树数据
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryCategoryTree2")
	public ModelAndView queryCategoryTree2(String id, String sysNo) {
		ModelAndView result = null;
		try {
			result = new ModelAndView("jsonView");
			List<CategoryDTO> tempList = categoryManager.queryCategoryListByParentNo(id, sysNo);
			result.addObject(tempList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;

	}

	@RequestMapping(value = "/queryMainTree")
	@ResponseBody
	public Object getMainTree() {
		List<CategoryDTO> result = new ArrayList<CategoryDTO>();
		Map<String, Object> params;
		CategoryDTO dto;
		try {
			List<LookupDtl> list = InitCacheController.lookupdMap.get("SYS_NO");
			for (LookupDtl dtl : list) {
				dto = new CategoryDTO();
				dto.setCateName(dtl.getItemname() + "(" + dtl.getItemvalue() + ")");
				dto.setCateNo(dtl.getItemvalue());
				params = new HashMap<String, Object>();
				params.put("headCateNo", dtl.getItemvalue());
				int a = categoryManager.findCount(params);
				if (a > 0) {
					dto.setState("closed");
				} else {
					dto.setState("open");
				}
				result.add(dto);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	@RequestMapping(value = "/queryChildrenTree")
	@ResponseBody
	public Object getChildrenTree(String headCateNo) {
		List<CategoryDTO> result = new ArrayList<CategoryDTO>();
		Map<String, Object> params;
		CategoryDTO dto;
		try {
			params = new HashMap<String, Object>();
			params.put("headCateNo", headCateNo);
			int total = categoryManager.findCount(params);
			SimplePage page = new SimplePage(1, total, total);
			List<Category> list = categoryManager.findByPage(page, null, null, params);
			if (total > 0) {
				for (Category c : list) {
					dto = new CategoryDTO();
					dto.setCateNo(c.getCateNo());
					dto.setCateName(c.getCateName());
					params.put("headCateNo", c.getCateNo());
					int a = categoryManager.findCount(params);
					if (a > 0) {
						dto.setState("closed");
					} else {
						dto.setState("open");
					}
					result.add(dto);
				}
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	@RequestMapping(value = "/queryItemByCate")
	@ResponseBody
	public Map<String, Object> queryItemByCate(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			boolean hasChildren = StringUtils.isEmpty(req.getParameter("hasChildren")) ? false : Boolean.valueOf(req
					.getParameter("hasChildren"));
			Map<String, Object> params = builderParams(req, model);
			
			String cateNo = params.get("cateNo") == null ? "" : params.get("cateNo").toString();
			String cateOne = params.get("cateOne") == null ? "" : params.get("cateOne").toString();
			String cateTwo = params.get("cateTwo") == null ? "" : params.get("cateTwo").toString();
			String cateThree = params.get("cateThree") == null ? "" : params.get("cateThree").toString();
			
			String itemNo = params.get("itemNo") == null ? "" : params.get("itemNo").toString();
			String brandNo = params.get("brandNo") == null ? "" : params.get("brandNo").toString();
			String planNo = params.get("planNo") == null ? "" : params.get("planNo").toString();
			String itemType = params.get("itemType") == null ? "" : params.get("itemType").toString();
			String quality = params.get("quality") == null ? "" : params.get("quality").toString();
			String sysNo = params.get("sysNo") == null ? "" : params.get("sysNo").toString();
			
			String yearsStr = params.get("yearsStr") == null ? "" : params.get("yearsStr").toString();
			String seasonStr = params.get("seasonStr") == null ? "" : params.get("seasonStr").toString();
			String genderStr = params.get("genderStr") == null ? "" : params.get("genderStr").toString();
			
			int total = 0;
			SimplePage page;
			List<Item> list = null;
			Item itemDtl = new Item();
			
			if (!StringUtils.isBlank(cateNo)) {
				if (!hasChildren) {
					itemDtl.setQueryCondition("and cate_no = '" + cateNo + "'");
				} else {
					String cateNoArray = getAllRootCate(cateNo);
					if (!StringUtils.isBlank(cateNoArray)) {
						cateNoArray = cateNoArray.substring(1);
					}
					itemDtl.setQueryCondition("and cate_no in (" + cateNoArray + ")");
				}
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if (!StringUtils.isBlank(user.getLocNo())) {
				itemDtl.setLocno(user.getLocNo());
			}
			if (!StringUtils.isBlank(itemNo)) {
				itemDtl.setItemNo(itemNo.toUpperCase());
			}
			if (!StringUtils.isBlank(brandNo)) {
				itemDtl.setBrandNo(brandNo);
			}
			if (!StringUtils.isBlank(planNo)) {
				itemDtl.setPlanNo(planNo);
			}
			if (!StringUtils.isBlank(itemType)) {
				itemDtl.setItemType(itemType);
			}
			if (!StringUtils.isBlank(quality)) {
				itemDtl.setQuality(quality);
			}
			if (!StringUtils.isBlank(sysNo)) {
				itemDtl.setSysNo(sysNo);
			}
			if (!StringUtils.isBlank(yearsStr)) {
				itemDtl.setYearsStr(yearsStr);
			}
			//季节
			String [] seasonValues = null;
			if (!StringUtils.isBlank(seasonStr)) {
				seasonValues = seasonStr.split(",");
			}
			itemDtl.setSeasonValues(seasonValues);
			//性别
			String [] genderValues = null;
			if (!StringUtils.isBlank(genderStr)) {
				genderValues = genderStr.split(",");
			}
			itemDtl.setGenderValues(genderValues);
			itemDtl.setCateOne(cateOne);
			itemDtl.setCateTwo(cateTwo);
			itemDtl.setCateThree(cateThree);
			total = this.itemManager.findChPlanItemAndSizeCount(itemDtl, authorityParams);
			page = new SimplePage(pageNo, pageSize, (int) total);
			list = this.itemManager.findChPlanItemAndSizeByPage(page, itemDtl, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	public String getAllRootCate(String cateNo) throws ManagerException {
		String cateNoList = "";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headCateNo", cateNo);
		List<Category> list = categoryManager.findByBiz(null, params);
		if (list == null || list.size() == 0) {
			cateNoList += ",'" + cateNo + "'";
		} else {
			for (Category c : list) {
				cateNoList += getAllRootCate(c.getCateNo());
			}
		}
		return cateNoList;
	}
	
	@RequestMapping(value = "/getCategory4Simple")
	@ResponseBody
	public List<CategorySimpleDto> getCategory4Simple(HttpServletRequest req, Model model) throws ManagerException {
		List<CategorySimpleDto> list = new ArrayList<CategorySimpleDto>();
		try {
			Map<String, Object> builderParams=builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>(builderParams);
			
			//大类
			String headCateCode = (String) params.get("headCateCode");
			String[] headCateCodeValues = null;
			if (StringUtils.isNotEmpty(headCateCode)) {
				headCateCodeValues = headCateCode.split(",");
			}
			params.put("headCateCodeValues", headCateCodeValues);
			
			
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			list = this.categoryManager.findCategory4Simple(params, authorityParams);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

}