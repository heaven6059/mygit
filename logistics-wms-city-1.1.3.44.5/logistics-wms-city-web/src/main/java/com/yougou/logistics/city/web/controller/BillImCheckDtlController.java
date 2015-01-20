package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.yougou.logistics.city.common.dto.SizeComposeDTO;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillImCheckDtlManager;
import com.yougou.logistics.city.manager.ItemManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 收货验收单详情controller
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:17:09
 * @version 0.1.0
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_im_check_dtl")
public class BillImCheckDtlController extends
	BaseCrudController<BillImCheckDtl> {
	
	@Log
	private Logger log;
    @Resource
    private BillImCheckDtlManager billImCheckDtlManager;
    @Resource
    private SizeInfoManager sizeInfoManager;
    @Resource
    private ItemManager itemManager;

    @Override
    public CrudInfo init() {
	return new CrudInfo("billimcheckdtl/", billImCheckDtlManager);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getByPage.json")
	@ResponseBody
	public Map<String, Object> getByPage(BillImCheckDtl billImCheckDtl, HttpServletRequest req, Model model)
			throws ManagerException {

		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map params = builderParams(req, model);
			int total = this.billImCheckDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillImCheckDtl> list = this.billImCheckDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillImCheckDtl temp : list) {
				this.setFooterMap("poQty", temp.getPoQty() == null ? new BigDecimal(0) : temp.getPoQty(), footerMap);
				this.setFooterMap("checkQty", temp.getCheckQty() == null ? new BigDecimal(0) : temp.getCheckQty(),
						footerMap);
				this.setFooterMap("directQty", temp.getDirectQty() == null ? new BigDecimal(0) : temp.getDirectQty(),
						footerMap);
				//int difQty = (temp.getPoQty().subtract(temp.getCheckQty() == null ? new BigDecimal(0) : temp.getCheckQty())).intValue();
				this.setFooterMap("difQty",
						new BigDecimal(temp.getDifQty()),
						footerMap);
				this.setFooterMap("divideQty", temp.getDivideQty() == null ? new BigDecimal(0) : temp.getDivideQty(),
						footerMap);
				if (StringUtils.isNotBlank(temp.getOriginalBoxNo()) && !"N".equals(temp.getOriginalBoxNo())) {
					temp.setBoxNo(temp.getOriginalBoxNo());
				}
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImCheckDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("po_Qty", 0);
					sumFoot.put("check_Qty", 0);
					sumFoot.put("direct_Qty", 0);
					sumFoot.put("dif_Qty", 0);
					sumFoot.put("divide_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
			obj.put("footer", footerList);
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
    
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getByPageDtl.json")
	@ResponseBody
	public Map<String, Object> getByPageDtl(BillImCheckDtl billImCheckDtl, HttpServletRequest req, Model model)
			throws ManagerException {

		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map params = builderParams(req, model);
			int total = this.billImCheckDtlManager.findBillImCheckDtlCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillImCheckDtl> list = this.billImCheckDtlManager.findBillImCheckDtlByPage(page, sortColumn, sortOrder, params,authorityParams);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillImCheckDtl temp : list) {
				this.setFooterMap("poQty", temp.getPoQty() == null ? new BigDecimal(0) : temp.getPoQty(), footerMap);
				this.setFooterMap("checkQty", temp.getCheckQty() == null ? new BigDecimal(0) : temp.getCheckQty(),footerMap);
				this.setFooterMap("directQty", temp.getDirectQty() == null ? new BigDecimal(0) : temp.getDirectQty(),footerMap);
				this.setFooterMap("difQty",new BigDecimal(temp.getDifQty()),footerMap);
				this.setFooterMap("divideQty", temp.getDivideQty() == null ? new BigDecimal(0) : temp.getDivideQty(),footerMap);
				if (StringUtils.isNotBlank(temp.getOriginalBoxNo()) && !"N".equals(temp.getOriginalBoxNo())) {
					temp.setBoxNo(temp.getOriginalBoxNo());
				}
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImCheckDtlManager.selectDtlSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("po_Qty", 0);
					sumFoot.put("check_Qty", 0);
					sumFoot.put("direct_Qty", 0);
					sumFoot.put("dif_Qty", 0);
					sumFoot.put("divide_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
			obj.put("footer", footerList);
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

    private void setFooterMap(String key, BigDecimal val,
	    Map<String, Object> footerMap) {
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

    @RequestMapping(value = "/queryBillImCheckDtlList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> queryBillImCheckDtlList(
	    String checkNo, BillImCheckDtl billImCheckDtl,
	    HttpServletRequest req, Model model) throws ManagerException {
	try {
	    int total = 0;
	    // 返回参数列表
	    List<SizeComposeDTO> returnDtoList = new ArrayList<SizeComposeDTO>();
	    // 返回 Map集合
	    Map<String, Object> obj = new HashMap<String, Object>();

	    obj.put("total", total);
	    obj.put("rows", returnDtoList);
	    ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(
		    obj, HttpStatus.OK);
	    if (StringUtils.isEmpty(checkNo)) {
		return responseEntity;
	    }

	    // 查询盘差单详情
	    SizeComposeDTO dtoParam = new SizeComposeDTO();
	    dtoParam.setCheckNo(checkNo);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    total = billImCheckDtlManager.selectCountMx(dtoParam);
	    SimplePage page = new SimplePage(pageNo, pageSize, total);

	    List<SizeComposeDTO> listTempGroup = billImCheckDtlManager
		    .queryBillImCheckDTOGroupBy(page, dtoParam);
	    if (CollectionUtils.isEmpty(listTempGroup)) {
		return responseEntity;
	    }

	    for (SizeComposeDTO gvo : listTempGroup) {

		if (CommonUtil.hasValue(gvo.getItemNo())) {
		    dtoParam.setItemNo(gvo.getItemNo());

		    List<SizeComposeDTO> listTempMxList = billImCheckDtlManager
			    .queryBillImCheckDTOBExpNo(dtoParam);
		    BigDecimal allCounts = new BigDecimal(0);
		    if (CollectionUtils.isEmpty(listTempMxList)) {
			continue;
		    }

		    SizeComposeDTO dto = listTempMxList.get(0);
		    SizeInfo sizeInfoParamInfo = new SizeInfo();
		    Map<String, Object> mapParaMap = new HashMap<String, Object>();
		    mapParaMap.put("sysNo", dto.getSysNo());
		    mapParaMap.put("sizeKind", dto.getSizeKind());
		    List<SizeInfo> sizeInfoList = this.sizeInfoManager
			    .findByBiz(sizeInfoParamInfo, mapParaMap);
		    for (SizeComposeDTO temp : listTempMxList) {
			for (int i = 0; i < sizeInfoList.size(); i++) {
			    SizeInfo sizeInfo = sizeInfoList.get(i);
			    if (temp.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对
				Object[] arg = new Object[] { temp
					.getCheckQty().toString() };
				String filedName = "setV" + (i + 1);
				CommonUtil.invokeMethod(dto, filedName, arg);
				allCounts = allCounts.add(temp.getCheckQty());
				break;
			    }
			}
		    }
		    dto.setAllCount(allCounts);
		    dto.setAllCost(new BigDecimal(0));
		    returnDtoList.add(dto);
		}
	    }
	    obj.put("total", total);
	    obj.put("rows", returnDtoList);
	    return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	} catch (Exception e) {
		log.error(e.getMessage(), e);
	    throw new ManagerException(e);
	}
    }
    
	@RequestMapping(value = "/selectCheckDtlDiffCount")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectCheckDtlDiffCount(HttpServletRequest req,
			BillImCheckDtl billImCheckDtl) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		int total = 0;
		try {
			total = this.billImCheckDtlManager.selectCheckDtlDiffCount(billImCheckDtl);
			obj.put("result", total);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

}