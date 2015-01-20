package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillImImportDtlSizeKind;
import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillImImportDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 预到货通知单明细
 * 
 * @author zuo.sw
 * @date 2013-09-25 10:24:56
 * @version 1.0.0
 * @param <ModelType>
 * @param <ModelType>
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
@RequestMapping("/bill_im_import_dtl")
public class BillImImportDtlController<ModelType> extends
	BaseCrudController<BillImImportDtl> {

    @Log
    private Logger log;

    @Resource
    private BillImImportDtlManager billImImportDtlManager;

    @Resource
    private SizeInfoManager sizeInfoManager;

    @Override
    public CrudInfo init() {
	return new CrudInfo("billimimport/", billImImportDtlManager);
    }

    @RequestMapping(value = "/dtllist.json")
    @ResponseBody
    public Map<String, Object> dtlList(HttpServletRequest req, Model model)
	    throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
		    : String.valueOf(req.getParameter("sort"));
	    String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
		    : String.valueOf(req.getParameter("order"));
	    Map<String, Object> params = builderParams(req, model);
	    int total = billImImportDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
	    SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
	    List<BillImImportDtl> list = billImImportDtlManager.findByPage(
		    page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
	    // 返回汇总列表
	    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
	    Map<String, Object> footerMap = new HashMap<String, Object>();
	    footerMap.put("itemNo", "小计");
	    footerList.add(footerMap);

	    for (BillImImportDtl temp : list) {

		FooterUtil.setFooterMap("poQty", temp.getPoQty(), footerMap);
		FooterUtil.setFooterMap("receiptQty", temp.getReceiptQty(), footerMap);
		FooterUtil.setFooterMap("importQty", temp.getImportQty(), footerMap);
		FooterUtil.setFooterMap("differQty", temp.getDifferQty(), footerMap);
	    }

	    // 合计
	    Map<String, Object> sumFoot = new HashMap<String, Object>();
	    if (pageNo == 1) {
		sumFoot = billImImportDtlManager.selectSumQty(params,authorityParams);
		if (sumFoot == null) {
		    sumFoot = new SumUtilMap<String, Object>();
		    sumFoot.put("po_Qty", 0);
		    sumFoot.put("receipt_Qty", 0);
		    sumFoot.put("import_Qty", 0);
		    sumFoot.put("differ_Qty", 0);
		}
		sumFoot.put("isselectsum", true);
		sumFoot.put("item_No", "合计");
	    } else {
		sumFoot.put("itemNo", "合计");
	    }
	    footerList.add(sumFoot);
	    obj.put("total", total);
	    obj.put("rows", list);
	    obj.put("footer", footerList);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	}
	return obj;
    }

    @RequestMapping(value = "/queryBillImImportDtlDTOlList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> queryBillImImportDtlDTOlList(
	    String importNo, String sysNo, HttpServletRequest req, Model model)
	    throws ManagerException {
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int total = 0;
	    // 返回参数列表
	    List<BillImImportDtlSizeKind> returnDtoList = new ArrayList<BillImImportDtlSizeKind>(
		    0);
	    // 返回汇总列表
	    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
	    Map<String, Object> footerMap = new HashMap<String, Object>();
	    footerMap.put("itemNo", "汇总");
	    footerList.add(footerMap);

	    // 返回 Map集合
	    Map<String, Object> obj = new HashMap<String, Object>();
	    obj.put("total", total);
	    obj.put("rows", returnDtoList);
	    obj.put("footer", footerList);

	    ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(
		    obj, HttpStatus.OK);
	    if (StringUtils.isEmpty(importNo) || StringUtils.isEmpty(sysNo)
		    || "0".equals(sysNo) || "N".equals(sysNo)
		    || "null".equals(sysNo)) {
		return responseEntity;
	    }
	    /** 查询箱详情 **/
	    BillImImportDtlSizeKind dtoParam = new BillImImportDtlSizeKind();
	    dtoParam.setImportNo(importNo);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    total = billImImportDtlManager.selectCountMx(dtoParam,authorityParams);
	    SimplePage page = new SimplePage(pageNo, pageSize, total);
	    List<BillImImportDtlSizeKind> listTempGroup = billImImportDtlManager
		    .queryBillImImportDtlDTOlListGroupBy(page, dtoParam,authorityParams);
	    if (CollectionUtils.isEmpty(listTempGroup)) {
		return responseEntity;
	    }

	    /** 箱单品列表 **/
	    for (BillImImportDtlSizeKind gvo : listTempGroup) {
		if (CommonUtil.hasValue(gvo.getItemNo())) {
		    dtoParam.setItemNo(gvo.getItemNo());
		    dtoParam.setBoxNo(gvo.getBoxNo());
		    List<BillImImportDtlSizeKind> listTempMxList = billImImportDtlManager
			    .queryBillImImportDtlDTOlListByImportNo(dtoParam);
		    BigDecimal allCounts = new BigDecimal(0);
		    if (CollectionUtils.isEmpty(listTempMxList)) {
			continue;
		    }
		    BillImImportDtlSizeKind dto = listTempMxList.get(0);

		    SizeInfo sizeInfoParamInfo = new SizeInfo();
		    Map<String, Object> mapParaMap = new HashMap<String, Object>();
		    mapParaMap.put("sysNo", dto.getSysNo());
		    mapParaMap.put("sizeKind", dto.getSizeKind());

		    // 查询品牌下所以尺码号
		    List<SizeInfo> sizeInfoList = this.sizeInfoManager
			    .findByBiz(sizeInfoParamInfo, mapParaMap);
		    for (BillImImportDtlSizeKind temp : listTempMxList) {
			for (int i = 0; i < sizeInfoList.size(); i++) {
			    SizeInfo tempSizeInfo = sizeInfoList.get(i);
			    // 匹配尺码
			    if (temp.getSizeNo().equals(
				    tempSizeInfo.getSizeNo())) { // 相对
				BigDecimal a = new BigDecimal(0);
				if (null != temp.getPoQty()) {
				    a = temp.getPoQty();
				}
				Object[] arg = new Object[] { a.toString() };
				String filedName = "setV" + (i + 1);
				CommonUtil.invokeMethod(dto, filedName, arg);
				allCounts = allCounts.add(a);

				// //////////////////
				FooterUtil.setFooterMap("v" + (i + 1),
					temp.getPoQty(), footerMap);
				FooterUtil.setFooterMap("allCount",
					temp.getPoQty(), footerMap);
				// //////////////////
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
	    obj.put("footer", footerList);
	    return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    throw new ManagerException(e);
	}
    }

    /**
     * 新增和删除预到货通知单明细
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/saveImImportDetail")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveImImportDetail(
	    HttpServletRequest req) throws JsonParseException,
	    JsonMappingException, IOException, ManagerException {
	Map<String, Object> flag = new HashMap<String, Object>();
	try {
	    String locno = req.getParameter("locno");
	    String ownerNo = req.getParameter("ownerNo");
	    String importNo = req.getParameter("importNo");
	    if (StringUtils.isNotBlank(locno)
		    && StringUtils.isNotBlank(ownerNo)
		    && StringUtils.isNotBlank(importNo)) {

		// 获取登陆用户
		HttpSession session = req.getSession();
		SystemUser user = (SystemUser) session
			.getAttribute(PublicContains.SESSION_USER);

		String deletedList = StringUtils.isEmpty(req
			.getParameter("deleted")) ? "" : req
			.getParameter("deleted");
		String upadtedList = StringUtils.isEmpty(req
			.getParameter("updated")) ? "" : req
			.getParameter("updated");
		String insertedList = StringUtils.isEmpty(req
			.getParameter("inserted")) ? "" : req
			.getParameter("inserted");
		ObjectMapper mapper = new ObjectMapper();
		Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
		if (StringUtils.isNotBlank(deletedList)) {
		    List<Map> list = mapper.readValue(deletedList,
			    new TypeReference<List<Map>>() {
			    });
		    List<ModelType> oList = convertListWithTypeReference(
			    mapper, list);
		    params.put(CommonOperatorEnum.DELETED, oList);
		}
		if (StringUtils.isNotBlank(upadtedList)) {
		    List<Map> list = mapper.readValue(upadtedList,
			    new TypeReference<List<Map>>() {
			    });
		    List<ModelType> oList = convertListWithTypeReference(
			    mapper, list);
		    params.put(CommonOperatorEnum.UPDATED, oList);
		}
		if (StringUtils.isNotBlank(insertedList)) {
		    List<Map> list = mapper.readValue(insertedList,
			    new TypeReference<List<Map>>() {
			    });
		    List<ModelType> oList = convertListWithTypeReference(
			    mapper, list);
		    params.put(CommonOperatorEnum.INSERTED, oList);
		}

		flag = billImImportDtlManager.addImImportDetail(locno, ownerNo,
			importNo, params, user.getLoginName(),user.getUsername());
	    }
	    return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	} catch (Exception e) {
	    log.error("===========新增，修改，删除预到货通知单明细时异常：" + e.getMessage(), e);
	    flag.put("flag", "false");
	    return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
    private <ModelType> List<ModelType> convertListWithTypeReference(
	    ObjectMapper mapper, List<Map> list) throws JsonParseException,
	    JsonMappingException, JsonGenerationException, IOException {
	Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass()
		.getGenericSuperclass()).getActualTypeArguments()[0];
	List<ModelType> tl = new ArrayList<ModelType>(list.size());
	for (int i = 0; i < list.size(); i++) {
	    ModelType type = mapper.readValue(
		    mapper.writeValueAsString(list.get(i)), entityClass);
	    tl.add(type);
	}
	return tl;
    }

    /**
     * 查询预到货通知单明细列表（带分页）
     */
    @RequestMapping(value = "/listBoxNo")
    @ResponseBody
    public Map<String, Object> queryList(HttpServletRequest req, Model model)
	    throws ManagerException {
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
		    : String.valueOf(req.getParameter("sort"));
	    String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
		    : String.valueOf(req.getParameter("order"));

	    Map<String, Object> params = builderParams(req, model);
	    return billImImportDtlManager.selectBoxNoByDetailPageCount(pageNo,
		    pageSize, sortColumn, sortOrder, params,authorityParams);
	} catch (Exception e) {
	    log.error("===========查询预到货通知单明细列表（带分页）时异常：" + e.getMessage(), e);
	    Map<String, Object> obj = new HashMap<String, Object>();
	    obj.put("success", false);
	    return obj;
	}
    }

    @RequestMapping(value = "/detail")
    @ResponseBody
    public Map<String, Object> detail(HttpServletRequest req, Model model)
	    throws ManagerException {
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
		    : Integer.parseInt(req.getParameter("rows"));
	    String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
		    : String.valueOf(req.getParameter("sort"));
	    String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
		    : String.valueOf(req.getParameter("order"));

	    Map<String, Object> params = builderParams(req, model);
	    return billImImportDtlManager.findDetailByImportNo(pageNo,
		    pageSize, sortColumn, sortOrder, params,authorityParams);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    Map<String, Object> obj = new HashMap<String, Object>();
	    obj.put("success", false);
	    return obj;
	}
    }

    @RequestMapping(value = "/queryBillImImportDtlByPrint")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> queryBillImImportDtlByPrint(
	    String importNo, String sysNo, HttpServletRequest req, Model model)
	    throws ManagerException {
	try {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
	    int total = 0;
	    // 返回参数列表
	    List<Object> list = new ArrayList<Object>();
	    // 返回 Map集合
	    Map<String, Object> obj = new HashMap<String, Object>();

	    obj.put("total", total);
	    obj.put("rows", null);
	    ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(
		    obj, HttpStatus.OK);
	    if (StringUtils.isEmpty(importNo) || StringUtils.isEmpty(sysNo)
		    || "0".equals(sysNo) || "N".equals(sysNo)
		    || "null".equals(sysNo)) {
		return responseEntity;
	    }
	    Map<String, String> sizeColMap = new TreeMap<String, String>();
	    List<String> sizeColList = new ArrayList<String>();
	    // 查询盘差单详情
	    BillImImportDtlSizeKind dtoParam = new BillImImportDtlSizeKind();
	    dtoParam.setImportNo(importNo);
	    int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
		    : Integer.parseInt(req.getParameter("page"));
	    /*
	     * int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
	     * : Integer.parseInt(req .getParameter("rows"));
	     */
	    total = billImImportDtlManager.selectCountMx(dtoParam,authorityParams);
	    SimplePage page = new SimplePage(pageNo, total, total);

	    List<BillImImportDtlSizeKind> listTempGroup = billImImportDtlManager
		    .queryBillImImportDtlDTOlListGroupBy(page, dtoParam,authorityParams);
	    if (CollectionUtils.isEmpty(listTempGroup)) {
		return responseEntity;
	    }
	    for (BillImImportDtlSizeKind gvo : listTempGroup) {

		if (CommonUtil.hasValue(gvo.getItemNo())) {
		    dtoParam.setItemNo(gvo.getItemNo());
		    dtoParam.setBoxNo(gvo.getBoxNo());

		    List<BillImImportDtlSizeKind> listTempMxList = billImImportDtlManager
			    .queryBillImImportDtlDTOlListByImportNo(dtoParam);
		    BigDecimal allCounts = new BigDecimal(0);
		    if (CollectionUtils.isEmpty(listTempMxList)) {
			continue;
		    }
		    Map<String, Object> boxAndItem = new HashMap<String, Object>();

		    BillImImportDtlSizeKind dto = listTempMxList.get(0);

		    boxAndItem.put("brandNoStr", dto.getBrandNoStr());
		    boxAndItem.put("itemNo", dto.getItemNo());
		    boxAndItem.put("itemName", dto.getItemName());
		    boxAndItem.put("boxNo", dto.getBoxNo());
		    Map<String, Object> size = new TreeMap<String, Object>();

		    SizeInfo sizeInfoParamInfo = new SizeInfo();
		    Map<String, Object> mapParaMap = new HashMap<String, Object>();
		    mapParaMap.put("sysNo", dto.getSysNo());
		    mapParaMap.put("sizeKind", dto.getSizeKind());
		    List<SizeInfo> sizeInfoList = this.sizeInfoManager
			    .findByBiz(sizeInfoParamInfo, mapParaMap);
		    for (BillImImportDtlSizeKind temp : listTempMxList) {
			for (int i = 0; i < sizeInfoList.size(); i++) {
			    SizeInfo sizeInfo = sizeInfoList.get(i);
			    if (temp.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对

				BigDecimal a = new BigDecimal(0);
				if (null != temp.getPoQty()) {
				    a = temp.getPoQty();
				    size.put(sizeInfo.getSizeCode(), a);
				    sizeColMap.put(sizeInfo.getSizeCode(),
					    sizeInfo.getSizeCode());
				    boxAndItem.put(sizeInfo.getSizeCode(), a);
				}
				allCounts = allCounts.add(a);
				break;
			    }
			}
		    }
		    boxAndItem.put("allCount", allCounts);
		    // boxAndItem.put("size", size);
		    list.add(boxAndItem);
		}
	    }
	    for (Entry<String, String> m : sizeColMap.entrySet()) {
		sizeColList.add(m.getKey());
	    }
	    obj.put("total", total);
	    obj.put("rows", list);
	    obj.put("sizeCols", sizeColList);

	    return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	} catch (Exception e) {
	    log.error(e.getMessage(),e);
	    throw new ManagerException(e);
	}
    }
}