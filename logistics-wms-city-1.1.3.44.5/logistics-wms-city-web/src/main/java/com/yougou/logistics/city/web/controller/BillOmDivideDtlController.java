package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.BillOmDivideDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * TODO: 分货任务单明细Controller
 * 
 * @author su.yq
 * @date 2013-10-14 下午8:33:15
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_divide_dtl")
public class BillOmDivideDtlController extends BaseCrudController<BillOmDivideDtl> {

	@Log
	private Logger log;
	@Resource
	private BillOmDivideDtlManager billOmDivideDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billOmDivideDtl/", billOmDivideDtlManager);
	}

	@RequestMapping(value = "/updateBillOmDivideDtl")
	@ResponseBody
	public String updateBillOmDivideDtl(HttpServletRequest req) {

		List<BillOmDivideDtl> oList = null;
		try {
			String assignNames = StringUtils.isEmpty(req.getParameter("assignNames")) ? "" : req
					.getParameter("assignNames");
			String assignNamesCh = StringUtils.isEmpty(req.getParameter("assignNamesCh")) ? "" : req
					.getParameter("assignNamesCh");
			String insertedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				oList = convertListWithTypeReference(mapper, list);
			}

			if (!CommonUtil.hasValue(oList)) {
				return "fail";
			}

			BillOmDivide divide = new BillOmDivide();
			divide.setAssignNames(assignNames);
			divide.setAssignNamesCh(assignNamesCh);
			divide.setListBillOmDivideDtl(oList);
			if (billOmDivideDtlManager.modifyBillOmDivideByDivideNoAndlocno(divide) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "fail";
	}

	/**
	 * 转换成泛型列表
	 * @param mapper
	 * @param list
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillOmDivideDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<BillOmDivideDtl> entityClass = (Class<BillOmDivideDtl>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		List<BillOmDivideDtl> tl = new ArrayList<BillOmDivideDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmDivideDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/printDetail")
	@ResponseBody
	public Map<String, Object> printDetail(Model model, HttpServletRequest req, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int total = 0;
			BillOmDivideDtl dtl = new BillOmDivideDtl();
			//返回参数列表
			Map<String, Object> params = builderParams(req, model);
			List<BillOmDivideDtl> listTemp = billOmDivideDtlManager.findByBiz(dtl, params);
			if (CollectionUtils.isEmpty(listTemp)) {
				result.put("result", "error");
				result.put("msg", "没有查询到明细");
			}
			for (BillOmDivideDtl list : listTemp) {
				BigDecimal qty = list.getItemQty();
				total += qty.intValue();
			}
			result.put("result", "success");
			result.put("rows", listTemp);
			result.put("total", total);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常,请联系管理员");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常,请联系管理员");
		}
		return result;
	}

	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> queryDtlList(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			/*//将排序字段转换数据库字段
			String [] columns = {"BOX_NO","STATUSSTR","GROUP_NAME","GROUP_NO","SERIAL_NO","STORE_NO","STORENAME","EXP_NO","ITEM_NO",
			        "ITEM_NAME","COLOR_NAME","SIZE_NO","ITEM_QTY","ASSIGN_NAME","OPERATE_DATE","REAL_QTY","DIFFQTY"};
			for(int i=0;i<columns.length;i++){
			    if(columns[i].replace("_", "").equals(sortColumn.toUpperCase())){
			        sortColumn = columns[i];
			        if(sortColumn.equals("SERIAL_NO")){
			            if("asc".equals(sortOrder.toLowerCase())){
			                sortColumn = "length(SERIAL_NO) asc, SERIAL_NO";
			            }else{
			                sortColumn = "length(SERIAL_NO) desc, SERIAL_NO";
			            }
			            
			        }
			    }
			}*/
			Map<String, Object> params = builderParams(req, model);
			int total = billOmDivideDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDivideDtl> list = billOmDivideDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);

			//		Map<String, Object> obj = super.queryList(req, model);
			//		List<BillOmDivideDtl> list = CommonUtil.getRowsByListJson(obj, BillOmDivideDtl.class);

			List<Object> footerList = new ArrayList<Object>();

			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			int totalDiffQty = 0;
			for (BillOmDivideDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty() == null ? new BigDecimal(0) : dtl.getRealQty());
				totalDiffQty += dtl.getDiffQty();
			}
			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("statusStr", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footer.put("diffQty", totalDiffQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot1 = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot1 = billOmDivideDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot1 != null) {
					Map<String, Object> sumFoot2 = new HashMap<String, Object>();
					sumFoot2.put("itemQty", sumFoot1.get("itemQty"));
					sumFoot2.put("realQty", sumFoot1.get("realQty"));
					sumFoot2.put("diffQty", sumFoot1.get("diffqty"));
					sumFoot2.put("isselectsum", true);
					sumFoot1 = sumFoot2;
				}
			}
			sumFoot1.put("statusStr", "合计");
			footerList.add(sumFoot1);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

}