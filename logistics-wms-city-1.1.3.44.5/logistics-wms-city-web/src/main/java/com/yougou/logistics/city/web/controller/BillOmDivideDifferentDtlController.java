package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmDivideDifferent;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmDivideDifferentDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
@RequestMapping("/bill_om_divide_different_dtl")
public class BillOmDivideDifferentDtlController extends BaseCrudController<BillOmDivideDifferentDtl> {
    
	@Log
	private Logger log;
	
	@Resource
    private BillOmDivideDifferentDtlManager billOmDivideDifferentDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bill_om_divide_different_dtl/",billOmDivideDifferentDtlManager);
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
			int total = billOmDivideDifferentDtlManager.findCount(params, authorityParams,DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDivideDifferentDtl> list = billOmDivideDifferentDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);
			
			List<Object> footerList = new ArrayList<Object>();
			//当页小计
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillOmDivideDifferentDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty()==null?new BigDecimal(0):dtl.getRealQty());
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("storeNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);
			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billOmDivideDifferentDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					String itemQty = "0";
					if (sumFoot1.get("itemQty") != null) {
						itemQty = sumFoot1.get("itemQty").toString();
					}
					String realQty = "0";
					if (sumFoot1.get("realQty") != null) {
						realQty = sumFoot1.get("realQty").toString();
					}
					sumFoot.put("itemQty", itemQty);
					sumFoot.put("realQty", realQty);
					sumFoot.put("isselectsum", true);
				}
			}
			sumFoot.put("storeNo", "合计");
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	// 拆分商品差异调整
	@RequestMapping(value = "/splitDifferentDtl")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> splitDifferentDtl(BillOmDivideDifferentDtl differentDtl,HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			billOmDivideDifferentDtlManager.splitDifferentDtl(differentDtl);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "拆分失败,请联系管理员!");
		}
		return map;
	}

	// 保存明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDifferentDtl")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveDifferentDtl(BillOmDivideDifferent different, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			different.setCreator(user.getLoginName());
			different.setCreatorName(user.getUsername());

			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");

			List<BillOmDivideDifferentDtl> insertList = new ArrayList<BillOmDivideDifferentDtl>();
			List<BillOmDivideDifferentDtl> updateList = new ArrayList<BillOmDivideDifferentDtl>();
			List<BillOmDivideDifferentDtl> deleteList = new ArrayList<BillOmDivideDifferentDtl>();
			
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(inserted)) {
				List<Map> list = mapper.readValue(inserted, new TypeReference<List<Map>>() {
				});
				insertList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(updated)) {
				List<Map> list = mapper.readValue(updated, new TypeReference<List<Map>>() {
				});
				updateList = convertListWithTypeReference(mapper, list);
			}
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference(mapper, list);
			}
			
			billOmDivideDifferentDtlManager.saveDifferentDtl(different, insertList, updateList, deleteList);
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
	
	@SuppressWarnings("rawtypes")
	private List<BillOmDivideDifferentDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmDivideDifferentDtl> tl = new ArrayList<BillOmDivideDifferentDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmDivideDifferentDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmDivideDifferentDtl.class);
			tl.add(type);
		}
		return tl;
	}
}