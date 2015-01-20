package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.manager.BillConConvertGoodsDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
@RequestMapping("/bill_con_convert_goods_dtl")
public class BillConConvertGoodsDtlController extends BaseCrudController<BillConConvertGoodsDtl> {
	
	@Log
	private Logger log;

	@Resource
	private BillConConvertGoodsDtlManager billConConvertGoodsDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billconconvertgoodsdtl/", billConConvertGoodsDtlManager);
	}
	
	/**
	 * 查询明细汇总
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findConvertGoodsDtlGroupByCheckByPage.json")
	@ResponseBody
	public Map<String, Object> findConvertGoodsDtlGroupByCheckByPage(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billConConvertGoodsDtlManager.findConvertGoodsDtlGroupByCheckCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConConvertGoodsDtl> list = billConConvertGoodsDtlManager.findConvertGoodsDtlGroupByCheckByPage(page, sortColumn, sortOrder, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillConConvertGoodsDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("checkNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billConConvertGoodsDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					String itemQty = "0";
					if (sumFoot1.get("itemQty") != null) {
						itemQty = sumFoot1.get("itemQty").toString();
					}
					String checkQty = "0";
					if (sumFoot1.get("realQty") != null) {
						checkQty = sumFoot1.get("realQty").toString();
					}
					sumFoot.put("itemQty", itemQty);
					sumFoot.put("realQty", checkQty);
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("checkNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	/**
	 * 查询明细
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billConConvertGoodsDtlManager.findCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConConvertGoodsDtl> list = billConConvertGoodsDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillConConvertGoodsDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("checkNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billConConvertGoodsDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					String itemQty = "0";
					if (sumFoot1.get("itemQty") != null) {
						itemQty = sumFoot1.get("itemQty").toString();
					}
					String checkQty = "0";
					if (sumFoot1.get("realQty") != null) {
						checkQty = sumFoot1.get("realQty").toString();
					}
					sumFoot.put("itemQty", itemQty);
					sumFoot.put("realQty", checkQty);
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("checkNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	

	// 新增明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveConvertGoodsDtl")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveConvertGoodsDtl(BillConConvertGoods convertGoods, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");

			List<BillUmCheck> insertList = new ArrayList<BillUmCheck>();
			List<BillUmCheck> deleteList = new ArrayList<BillUmCheck>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(inserted)) {
				List<Map> list = mapper.readValue(inserted, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference(mapper, list);
			}

			billConConvertGoodsDtlManager.saveConvertGoodsDtl(convertGoods, insertList, deleteList);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "保存失败,请联系管理员!");
		}
		return map;
	}
	
	private List<BillUmCheck> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmCheck> tl = new ArrayList<BillUmCheck>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmCheck type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillUmCheck.class);
			tl.add(type);
		}
		return tl;
	}
	
	@RequestMapping(value = "/printDetail4SizeHorizontal")
   	@ResponseBody   
   	public Map<String,Object> printDetail4SizeHorizontal(HttpServletRequest req,String keys)throws ManagerException{
    	Map<String, Object> result = new HashMap<String, Object>();
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			List<Map<String, Object>> resultList = this.billConConvertGoodsDtlManager.findDtl4SizeHorizontal(keys,authorityParams);
			result.put("pages", resultList);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		}
    }
}