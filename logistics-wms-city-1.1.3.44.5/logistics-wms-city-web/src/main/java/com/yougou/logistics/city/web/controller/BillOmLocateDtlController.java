package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.BillOmLocateDtlSizeKind;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillOmLocateDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
@RequestMapping("/bill_om_locate_dtl")
public class BillOmLocateDtlController extends BaseCrudController<BillOmLocateDtl> {
	@Log
	private Logger log;
	@Resource
	private BillOmLocateDtlManager billOmLocateDtlManager;

	@Resource
	private SizeInfoManager sizeInfoManager;
	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public CrudInfo init() {
		return new CrudInfo("billOmLocateDtl/", billOmLocateDtlManager);
	}

	@RequestMapping(value = "/dlist.json")
	@ResponseBody
	public Map<String, Object> queryList1(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();

		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = billOmLocateDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmLocateDtl> list = billOmLocateDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);

			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("storeNo", "小计");
			footerList.add(footerMap);
			if (CommonUtil.hasValue(list)) {
				for (BillOmLocateDtl temp : list) {
					FooterUtil.setFooterMap("planQty",
							temp.getPlanQty() == null ? new BigDecimal(0) : temp.getPlanQty(), footerMap);
					FooterUtil.setFooterMap("locatedQty",
							temp.getLocatedQty() == null ? new BigDecimal(0) : temp.getLocatedQty(), footerMap);
					FooterUtil.setFooterMap("outstockQty",
							temp.getLocatedQty() == null ? new BigDecimal(0) : temp.getOutstockQty(), footerMap);
					FooterUtil.setFooterMap("recheckQty",
							temp.getLocatedQty() == null ? new BigDecimal(0) : temp.getRecheckQty(), footerMap);
				}
			} else {
				FooterUtil.setFooterMap("planQty", new BigDecimal(0), footerMap);
				FooterUtil.setFooterMap("locatedQty", new BigDecimal(0), footerMap);
				FooterUtil.setFooterMap("outstockQty", new BigDecimal(0), footerMap);
				FooterUtil.setFooterMap("recheckQty", new BigDecimal(0), footerMap);
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmLocateDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("plan_qty", 0);
					sumFoot.put("located_qty", 0);
					sumFoot.put("outstock_qty", 0);
					sumFoot.put("recheck_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("store_no", "合计");
			} else {
				sumFoot.put("storeNo", "合计");
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

	@RequestMapping(value = "/printDetail")
	@ResponseBody
	public Object printDetail(HttpServletRequest req, HttpSession session, String keys, String locno) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String result = "";
		try {
			Object u = session.getAttribute(PublicContains.SESSION_USER);
			SystemUser user = null;
			if (u != null) {
				user = (SystemUser) u;
			}
			if (StringUtils.isBlank(keys)) {
				result = "请传入正确的波次号!";
			} else if (StringUtils.isBlank(locno)) {
				result = "请传入正确的仓库编号!";
			} else {
				List<String> htmlList = billOmLocateDtlManager.printDetail(locno, keys, user);
				if (htmlList.size() > 0) {
					obj.put("data", htmlList);
					result = "success";
				} else {
					result = "没有任何明细!";
				}

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		obj.put("result", result);
		return obj;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/do_export_dtl")
	public void doExportDtl(HttpServletRequest req, Model model, HttpServletResponse response) throws ManagerException {
		Map<String, Object> params = builderParams(req, model);
		String exportColumns = (String) params.get("exportColumns");
		String fileName = (String) params.get("fileName");
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(exportColumns)) {
			try {
				AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";

				// 字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns, new TypeReference<List<Map>>() {
				});

				int total = billOmLocateDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
				SimplePage page = new SimplePage(1, total, (int) total);
				List<BillOmLocateDtl> list = billOmLocateDtlManager.findByPage(page, "", "", params, authorityParams,
						DataAccessRuleEnum.BRAND);
				List<Map> listArrayList = new ArrayList<Map>();
				if (list != null && list.size() > 0) {
					for (BillOmLocateDtl vo : list) {
						Map map = new HashMap();
						BeanUtilsCommon.object2MapWithoutNull(vo, map);
						listArrayList.add(map);

					}
					HSSFExport.commonExportData(StringUtils.isNotEmpty(fileName) ? fileName : "导出信息", ColumnsList,
							listArrayList, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

	}

	@RequestMapping(value = "/printDetail4SizeHorizontal")
	@ResponseBody
	public Map<String, Object> printDetail4SizeHorizontal(HttpServletRequest req, HttpSession session, String keys,
			SizeInfo info, Model model) {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");

		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		String locno = user.getLocNo();
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			String[] keysArray = keys.split(",");
			String locateNo = "";
			for (String str : keysArray) {
				locateNo = str;
				// 返回 Map集合
				Map<String, Object> obj = new HashMap<String, Object>();
				// 查询主档信息
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("locno", locno);
				param.put("locateNo", locateNo);
				/*BillOmRecheck recheck = this.billOmRecheckManager.findById(param);
				Store store = new Store();
				store.setStoreNo(recheck.getStoreNo());
				store = storeManager.findById(store);*/
				Map<String, Object> main = new HashMap<String, Object>();
				main.put("locateNo", locateNo);
				main.put("creator", user.getUsername());
				main.put("creattm", format.format(new Date()));
				/*if (store != null && !StringUtils.isBlank(store.getStoreName())) {
					main.put("receipterName", store.getStoreName());
				} else {
					main.put("receipterName", "");
				}*/
				/*main.put("sender", user.getLocName());
				main.put("receipter", store.getStoreNo());
				main.put("recheckNo", recheck.getRecheckNo());
				main.put("creator", user.getUsername());
				main.put("creattm", format.format(new Date()));*/
				obj.put("main", main);
				List<BillOmLocateDtlSizeKind> list = new ArrayList<BillOmLocateDtlSizeKind>();
				// 返回汇总列表
				List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
				Map<String, Object> footerMap = new HashMap<String, Object>();
				footerMap.put("itemNo", "汇总");
				footerList.add(footerMap);
				// 根据商品编码客户编码发货单分组查询
				List<BillOmLocateDtlSizeKind> listTempGroup = billOmLocateDtlManager.selectDtlByStoreNoItemNoExpNo(
						param, authorityParams);
				if (CollectionUtils.isEmpty(listTempGroup)) {
					result.put("result", ResultEnums.FAIL.getResultMsg());
					result.put("msg", "没有查询到明细");
					return result;
				}
				String sysNo = "";
				BigDecimal sumQty = new BigDecimal(0);
				for (BillOmLocateDtlSizeKind gvo : listTempGroup) {
					if (CommonUtil.hasValue(gvo.getItemNo())) {
						Map<String, Object> subParam = new HashMap<String, Object>();
						subParam.put("locno", locno);
						subParam.put("locateNo", locateNo);
						subParam.put("itemNo", gvo.getItemNo());
						subParam.put("storeNo", gvo.getStoreNo());
						subParam.put("expNo", gvo.getExpNo());
						List<BillOmLocateDtlSizeKind> listTempMxList = billOmLocateDtlManager.selectAllDtl4Print(
								subParam, authorityParams);
						BigDecimal allCounts = new BigDecimal(0);
						if (CollectionUtils.isEmpty(listTempMxList)) {
							continue;
						}
						BillOmLocateDtlSizeKind dto = listTempMxList.get(0);
						dto.setStoreName(gvo.getStoreName());
						dto.setStoreNo(gvo.getStoreNo());
						sysNo = dto.getSysNo();
						// 查询品牌下所以尺码号
						SizeInfo sizeInfoParamInfo = new SizeInfo();
						Map<String, Object> mapParaMap = new HashMap<String, Object>();
						mapParaMap.put("sysNo", dto.getSysNo());
						mapParaMap.put("sizeKind", dto.getSizeKind());
						List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
						for (BillOmLocateDtlSizeKind temp : listTempMxList) {
							for (int i = 0; i < sizeInfoList.size(); i++) {
								SizeInfo tempSizeInfo = sizeInfoList.get(i);
								// 匹配尺码
								if (temp.getSizeNo().equals(tempSizeInfo.getSizeNo())) { // 相对
									BigDecimal a = new BigDecimal(0);
									if (null != temp.getPlanQty()) {
										a = temp.getPlanQty();
									}
									Object[] arg = new Object[] { a.toString() };
									String filedName = "setV" + (i + 1);
									CommonUtil.invokeMethod(dto, filedName, arg);
									allCounts = allCounts.add(a);
									FooterUtil.setFooterMap("v" + (i + 1), temp.getPlanQty(), footerMap);
									FooterUtil.setFooterMap("allCount", temp.getPlanQty(), footerMap);
									sumQty = sumQty.add(temp.getPlanQty());
									break;
								}
							}
						}
						dto.setAllCount(allCounts);
						list.add(dto);
					}
				}
				main.put("sumQty", sumQty);
				obj.put("rows", list);
				obj.put("footer", footerList);
				// 获取头部信息
				if (StringUtils.isEmpty(sysNo)) {
					result.put("result", ResultEnums.FAIL.getResultMsg());
					result.put("msg", "没有获取到品牌库，无法打印");
					return result;
				}
				info.setSysNo(sysNo);
				//查询所有的商品明细
				List<String> allKind = billOmLocateDtlManager.selectAllDtlSizeKind(param, authorityParams);
				Map header = getBrandList(preColNames, endColNames, sizeTypeFiledName, sysNo, allKind);
				obj.put("header", header);
				resultList.add(obj);
			}
			result.put("rows", resultList);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		}
	}

	//获取尺码横排头部信息
	private Map getBrandList(String preColNames, String endColNames, String sizeTypeFiledName, String sysNo,
			List<String> list) throws ManagerException {
		HashMap returnMap = new HashMap();

		LinkedList returnList = new LinkedList();
		ObjectMapper mapper = new ObjectMapper();
		List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
		List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();

		try {
			if (StringUtils.isNotEmpty(preColNames)) {
				preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}

			if (StringUtils.isNotEmpty(endColNames)) {
				endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}

		// 尺码类型 A-ArrayList<SizeInfo>
		LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
		/*
		 * Map<String, Object> tempParams = new HashMap<String, Object>();
		 * tempParams.put("sysNo", sysNo); tempParams.put("preColNames",
		 * preColNames); tempParams.put("endColNames", endColNames);
		 * tempParams.put("sizeTypeFiledName", sizeTypeFiledName);
		 */

		// 查询尺码对应的尺码类别
		List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(list, sysNo, null);
		/*
		 * List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null,
		 * tempParams);
		 */
		if (sizeTypeList != null && sizeTypeList.size() > 0) {
			for (SizeInfo vo : sizeTypeList) {
				String sizeTypeName = vo.getSizeKind();
				if (sizeTypeMap.containsKey(sizeTypeName)) {
					ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
					listA.add(vo.getSizeName()); // ===========
					sizeTypeMap.put(sizeTypeName, listA);
				} else {
					ArrayList listA = new ArrayList();
					listA.add(vo.getSizeName());
					sizeTypeMap.put(sizeTypeName, listA);
				}
			}
		}

		int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
		if (sizeTypeMap != null) {
			java.util.Iterator it = sizeTypeMap.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				List tempList = (List) entry.getValue();
				if (maxSortCount < tempList.size()) {
					maxSortCount = tempList.size();
				}
			}
		}

		// ====开始处理====
		Editor defaultEditor = new Editor();
		defaultEditor.setType("validatebox");
		if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
			int typeSizeV = sizeTypeMap.size(); // 公共的类别数
			if (typeSizeV > 1) { // 多于一个尺码类别
				int rowspan = typeSizeV - 2; // 合并的行
				int preColNamesV = preColNamesList.size(); // 前
				int endColNamesV = endColNamesList.size(); // 后
				if (rowspan >= 0 && rowspan != -1) {
					// ①处理合并表头
					if (rowspan > 0) { // 大于2个的时候
						for (int i = 0; i < rowspan; i++) {
							LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
							// 1.前面合并
							if (i == 0) {
								JqueryDataGrid v = new JqueryDataGrid();
								v.setTitle("");
								v.setWidth(80);
								v.setEditor(defaultEditor);
								v.setRowspan(rowspan);
								v.setColspan(preColNamesV);
								v.setAlign("left");
								colList.add(v);
							}
							// 2.显示尺码
							int k = 0;
							for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
								if (k == i) {
									int diffCols = maxSortCount - (en.getValue().size());
									JqueryDataGrid v = new JqueryDataGrid();
									v.setTitle(en.getKey());
									v.setWidth(50);
									v.setEditor(defaultEditor);
									v.setAlign("left");
									colList.add(v);

									for (int p = 0; p < en.getValue().size(); p++) {
										JqueryDataGrid v1 = new JqueryDataGrid();
										v1.setTitle(en.getValue().get(p).toString());
										v1.setWidth(50);
										v1.setEditor(defaultEditor);
										v1.setAlign("left");
										colList.add(v1);
									}
									if (diffCols > 0) {
										for (int m = 1; m <= diffCols; m++) {
											JqueryDataGrid v1 = new JqueryDataGrid();
											v1.setTitle("");
											v1.setWidth(50);
											v1.setEditor(defaultEditor);
											v1.setAlign("left");
											colList.add(v1);
										}
									}

									break;
								}
								k = k + 1;
							}
							// 3.合并后头
							if (i == 0) {
								if (endColNamesV > 0) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setTitle("");
									v.setWidth(80);
									v.setEditor(defaultEditor);
									v.setRowspan(rowspan);
									v.setColspan(endColNamesV);
									v.setAlign("left");
									colList.add(v);
								}
							}
							returnList.add(colList);
						}
					}

					// ②处理业务表头
					for (int ii = 1; ii >= 0; ii--) {
						LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
						// 1.业务头
						if (ii == 1) {
							if (preColNamesList.size() > 0) {
								for (JqueryDataGrid col : preColNamesList) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setField(col.getField());
									v.setTitle(col.getTitle());
									v.setWidth(SysConstans.WIDTH_80);
									if (col.getWidth() != 0) {
										v.setWidth(col.getWidth());
									}

									v.setEditor(defaultEditor);
									if (col.getEditor() != null) {
										if (!CommonUtil.hasValue(col.getEditor().getType())) {
											col.getEditor().setType(defaultEditor.getType());
										}
										v.setEditor(col.getEditor());
									}

									v.setRowspan(2);
									v.setAlign("left");
									colList.add(v);
								}
							}
						}
						// 2.尺码
						int k2 = 0;
						java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
						while (it2.hasNext()) {
							java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
									.next();

							if ((sizeTypeMap.size() - 1 - ii) == k2) {

								int diffCols2 = maxSortCount - (entry.getValue().size());
								// <#-- 这里做判断的原因是为了防止重复写 field: ,上面是List循环的 -->
								JqueryDataGrid v = new JqueryDataGrid();
								v.setTitle(entry.getKey());
								v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
								v.setEditor(defaultEditor);
								v.setAlign("left");
								if (ii == 1) {
									v.setField(sizeTypeFiledName);
									v.setAlign("center");
								}
								colList.add(v);

								for (int p = 0; p < entry.getValue().size(); p++) {
									JqueryDataGrid v1 = new JqueryDataGrid();
									v1.setTitle(entry.getValue().get(p).toString());
									v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
									v1.setEditor(defaultEditor);
									v1.setAlign("left");
									if (ii == 1) {
										v1.setField("v" + (p + 1));
										v1.setAlign("center");
									}
									colList.add(v1);
								}

								if (diffCols2 > 0) {
									for (int m = 1; m <= diffCols2; m++) {
										JqueryDataGrid v1 = new JqueryDataGrid();
										v1.setTitle("");
										v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
										v1.setEditor(defaultEditor);
										v1.setAlign("left");
										if (ii == 1) {
											v1.setField("v" + (entry.getValue().size() + m));
											v1.setAlign("center");
										}
										colList.add(v1);
									}
								}

								break;
							}
							k2 = k2 + 1;
						}
						// 3.
						if (ii == 1) {
							if (endColNamesList.size() > 0) {
								for (JqueryDataGrid col : endColNamesList) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setField(col.getField());
									v.setTitle(col.getTitle());
									v.setWidth(SysConstans.WIDTH_80);
									if (col.getWidth() != 0) {
										v.setWidth(col.getWidth());
									}
									v.setEditor(defaultEditor);
									if (col.getEditor() != null) {
										if (!CommonUtil.hasValue(col.getEditor().getType())) {
											col.getEditor().setType(defaultEditor.getType());
										}
										v.setEditor(col.getEditor());
									}
									v.setRowspan(2);
									v.setAlign("left");
									colList.add(v);
								}
							}
						}

						returnList.add(colList);

					}
				}
			} else if (typeSizeV == 1) {// 只有一个尺码类别
				LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
				// 1.业务头 标题栏
				if (preColNamesList.size() > 0) {
					for (JqueryDataGrid col : preColNamesList) {
						JqueryDataGrid v = new JqueryDataGrid();
						v.setField(col.getField());
						v.setTitle(col.getTitle());
						v.setWidth(SysConstans.WIDTH_80);
						if (col.getWidth() != 0) {
							v.setWidth(col.getWidth());
						}

						v.setEditor(defaultEditor);
						if (col.getEditor() != null) {
							if (!CommonUtil.hasValue(col.getEditor().getType())) {
								col.getEditor().setType(defaultEditor.getType());
							}
							v.setEditor(col.getEditor());
						}

						v.setRowspan(1);
						v.setAlign("left");
						colList.add(v);
					}
				}
				// 尺码横排栏
				java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
				while (it2.hasNext()) {
					java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2.next();
					// 尺码类别
					JqueryDataGrid v = new JqueryDataGrid();
					v.setTitle(entry.getKey());
					v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
					v.setEditor(defaultEditor);
					v.setAlign("left");
					v.setField(sizeTypeFiledName);
					v.setAlign("center");
					colList.add(v);

					for (int p = 0; p < entry.getValue().size(); p++) {
						JqueryDataGrid v1 = new JqueryDataGrid();
						v1.setTitle(entry.getValue().get(p).toString());
						v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
						v1.setEditor(defaultEditor);
						v1.setAlign("left");
						v1.setField("v" + (p + 1));
						v1.setAlign("center");
						colList.add(v1);
					}
				}
				// 合计信息
				if (endColNamesList.size() > 0) {
					for (JqueryDataGrid col : endColNamesList) {
						JqueryDataGrid vend = new JqueryDataGrid();
						vend.setField(col.getField());
						vend.setTitle(col.getTitle());
						vend.setWidth(SysConstans.WIDTH_80);
						if (col.getWidth() != 0) {
							vend.setWidth(col.getWidth());
						}
						vend.setEditor(defaultEditor);
						if (col.getEditor() != null) {
							if (!CommonUtil.hasValue(col.getEditor().getType())) {
								col.getEditor().setType(defaultEditor.getType());
							}
							vend.setEditor(col.getEditor());
						}
						vend.setRowspan(1);
						vend.setAlign("left");
						colList.add(vend);
					}
				}
				returnList.add(colList);
			}
		}

		// ====开始处理====

		returnMap.put("returnCols", returnList);
		returnMap.put("maxType", maxSortCount);
		returnMap.put("startType", preColNamesList.size() + 1);
		return returnMap;
	}

	@RequestMapping(value = "/printDetailSummary")
	@ResponseBody
	public Map<String, Object> printDetailSummary(HttpServletRequest req, HttpSession session, String keys,
			SizeInfo info, Model model) {
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		Map<String, Object> result = new HashMap<String, Object>();
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		String locno = user.getLocNo();
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			String[] keysArray = keys.split(",");
			String locateNo = "";
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (String str : keysArray) {
				locateNo = str;
				// 查询主档信息
				Map<String, Object> obj = new HashMap<String, Object>();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("locno", locno);
				param.put("locateNo", locateNo);
				Map<String, Object> main = new HashMap<String, Object>();
				main.put("locateNo", locateNo);
				main.put("creator", user.getUsername());
				main.put("creattm", format.format(new Date()));

				int sumPlanQty = 0;
				int sumRealQty = 0;
				int sumRecheckQty = 0;
				List<BillOmLocateDtlSizeKind> list = billOmLocateDtlManager.selectDtlByStoreNo(param, authorityParams);
				for (BillOmLocateDtlSizeKind kind : list) {
					sumPlanQty = sumPlanQty + kind.getPlanQty().intValue();
					sumRealQty = sumRealQty + kind.getRealQty().intValue();
					sumRecheckQty = sumRecheckQty + kind.getRecheckQty().intValue();
				}
				main.put("sumPlanQty", sumPlanQty);
				main.put("sumRealQty", sumRealQty);
				main.put("sumRecheckQty", sumRecheckQty);
				// 返回汇总列表
				obj.put("list", list);
				obj.put("main", main);
				resultList.add(obj);
			}
			result.put("rows", resultList);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		}
	}
}
