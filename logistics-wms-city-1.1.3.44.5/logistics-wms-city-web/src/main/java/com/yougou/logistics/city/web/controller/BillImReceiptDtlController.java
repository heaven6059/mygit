package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.utils.SystemCache;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillImReceiptDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@RequestMapping("/bill_im_receipt_dtl")
public class BillImReceiptDtlController extends BaseCrudController<BillImReceiptDtl> {
	@Log
	private Logger log;

	@Resource
	private BillImReceiptDtlManager billImReceiptDtlManager;

	@Resource
	private SizeInfoManager sizeInfoManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billimreceiptdtl/", billImReceiptDtlManager);
	}

	@RequestMapping(value = "/findReceiptDetail")
	@ResponseBody
	public Map<String, Object> findReceiptDetail(HttpServletRequest req, Model model) {
		Map<String, Object> obj = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map paramMap = this.builderParams(req, model);
			int total = billImReceiptDtlManager.findDetailCount(paramMap, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImImportDtlDto> list = billImReceiptDtlManager.findDetail(page, paramMap, authorityParams);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/selectItemDetail")
	@ResponseBody
	public Map<String, Object> selectItemDetail(HttpServletRequest req, Model model) {
		Map<String, Object> obj = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map paramMap = this.builderParams(req, model);
			int total = billImReceiptDtlManager.selectItemDetailCount(paramMap, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceiptDtl> list = billImReceiptDtlManager.selectItemDetail(paramMap, page, authorityParams);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("importNo", "小计");
			footerList.add(footerMap);
			for (BillImReceiptDtl temp : list) {
				this.setFooterMap("receiptQty", temp.getReceiptQty(), footerMap);
				this.setFooterMap("checkQty", temp.getCheckQty(), footerMap);
				this.setFooterMap("divideQty", temp.getDivideQty(), footerMap);
			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImReceiptDtlManager.selectSumQty(paramMap, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("receipt_Qty", 0);
					sumFoot.put("check_Qty", 0);
					sumFoot.put("divide_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("import_No", "合计");
			} else {
				sumFoot.put("importNo", "合计");
			}
			footerList.add(sumFoot);
			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
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

	@RequestMapping(value = "/selectDetail4Instock")
	@ResponseBody
	public Map<String, Object> selectDetail4Instock(HttpServletRequest req, Model model) {
		Map<String, Object> obj = null;
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map paramMap = this.builderParams(req, model);
			int total = this.billImReceiptDtlManager.findCount(paramMap);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceiptDtl> list = billImReceiptDtlManager.findByPage(page, "", "", paramMap);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillImReceiptDtl temp : list) {
				this.setFooterMap("receiptQty", temp.getReceiptQty(), footerMap);
			}
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImReceiptDtlManager.selectSumQty(paramMap, null);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("receipt_Qty", 0);
					sumFoot.put("check_Qty", 0);
					sumFoot.put("divide_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/getDetailBoxQty")
	@ResponseBody
	public Map<String, Object> getDetailBoxQty(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			int boxQty = billImReceiptDtlManager.selectBoxQty(params, authorityParams);
			obj.put("boxQty", boxQty);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/listReceiptDtlBox")
	@ResponseBody
	public List<BillImReceiptDtl> listReceiptDtlBox(HttpServletRequest req, Model model) {
		List<BillImReceiptDtl> list = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			list = this.billImReceiptDtlManager.findBillImReceiptDtlBox(params, authorityParams);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	@RequestMapping(value = "/initHead")
	@ResponseBody
	public Map<String, Object> initHead(HttpServletRequest req, HttpSession session, Model model)
			throws ManagerException {
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		Map<String, Object> map = new HashMap<String, Object>();

		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");

		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		Map param = this.builderParams(req, model);
		List<String> sizeKindList = billImReceiptDtlManager.selectItemSizeKind(param, authorityParams);
		if (sizeKindList == null || sizeKindList.size() == 0) {
			return map;
		}
		String sysNo = billImReceiptDtlManager.selectSysNo(param);
		Map header = getBrandList(preColNames, endColNames, sizeTypeFiledName, sysNo, sizeKindList);
		map.put("header", header);
		return map;
	}

	@RequestMapping(value = "/selectItemDetail4SizeHorizontal")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectItemDetail4SizeHorizontal(HttpServletRequest req, Model model,
			HttpSession session) throws ManagerException {
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		int total = 0;
		// 返回参数列表
		List<BillImReceiptDtl> billCheckImRepList = new ArrayList<BillImReceiptDtl>();
		//返回汇总列表
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		Map<String, Object> footer1Map = new HashMap<String, Object>();
		footerMap.put("importNo", "小计");
		footerList.add(footerMap);
		Map param = this.builderParams(req, model);
		String sysNo = billImReceiptDtlManager.selectSysNo(param);
		// 返回 Map集合
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);

			/*	List<SizeInfo> list = billImReceiptDtlManager.selectItemSizeKind(param, authorityParams);
				Map<String, Integer> map = new HashMap<String, Integer>();
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j) == null) {
						break;
					} else {
						map.put(list.get(j).getSizeCode(), j);
					}

				}*/
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			//** 统计总数 **/

			total = billImReceiptDtlManager.selectItemDetailByGroupCount(param, authorityParams);
			/** 分组计算拿到订单编号和商品编号 **/
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImReceiptDtl> tempList = billImReceiptDtlManager.selectItemDetailByGroup(param, authorityParams,
					page);
			for (int z = 0; z < tempList.size(); z++) {
				BigDecimal allCounts = new BigDecimal(0);
				BillCheckImRep model2 = new BillCheckImRep();
				model2.setItemNo(tempList.get(z).getItemNo());
				model2.setImportNo(tempList.get(z).getImportNo());
				model2.setLocNo(user.getLocNo());

				Map<String, Object> param2 = new HashMap<String, Object>();
				param2.put("locno", tempList.get(z).getLocno());
				param2.put("receiptNo", tempList.get(z).getReceiptNo());
				param2.put("itemNo", tempList.get(z).getItemNo());
				param2.put("boxNo", tempList.get(z).getBoxNo());

				List<BillImReceiptDtl> dtlList = billImReceiptDtlManager.selectDetailBySizeNo(param2);
				BillImReceiptDtl dtl = tempList.get(z);

				SizeInfo sizeInfoParamInfo = new SizeInfo();
				Map<String, Object> mapParaMap = new HashMap<String, Object>();
				mapParaMap.put("sysNo", sysNo);
				mapParaMap.put("sizeKind", dtl.getSizeKind());
				List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
				Map<String, BigDecimal> maps = new HashMap<String, BigDecimal>();
				for (int k = 0; k < dtlList.size(); k++) {
					for (int i = 0; i < sizeInfoList.size(); i++) {
						BillImReceiptDtl temp = dtlList.get(k);
						SizeInfo tempSizeInfo = sizeInfoList.get(i);
						// 匹配尺码
						if (temp.getSizeNo().equals(tempSizeInfo.getSizeNo())) { // 相对
							BigDecimal a = new BigDecimal(0);
							if (null != temp.getReceiptQty()) {
								a = temp.getReceiptQty();
							}
							Object[] arg = new Object[] { a.toString() };
							String filedName = "setV" + (i + 1);
							CommonUtil.invokeMethod(dtl, filedName, arg);
							allCounts = allCounts.add(a);
							FooterUtil.setFooterMap("v" + (i + 1), temp.getReceiptQty(), footerMap);
							FooterUtil.setFooterMap("allCount", temp.getReceiptQty(), footerMap);
							break;
						}
					}
					/*BillCheckImRep r = (BillCheckImRep) dtlList.get(k);
					String key = r.getImportNo() + "|" + r.getItemNo() + "|" + r.getSizeCode();
					BigDecimal qty = maps.get(key);
					if (qty == null) {
						maps.put(key, r.getCheckQty());
					} else {
						maps.put(key, qty.add(r.getCheckQty()));
					}
					int s = map.get(dtlList.get(k).getSizeCode());
					String filedName = "setV" + s;
					if (maps.get(key) != null) {
						Object[] arg = new Object[] { maps.get(key).toString() };
						CommonUtil.invokeMethod(dtl, filedName, arg);
						allCounts = allCounts.add(dtlList.get(k).getCheckQty());
					} else {
						Object[] arg = new Object[] { "0" };
						CommonUtil.invokeMethod(dtl, filedName, arg);
						allCounts = allCounts.add(new BigDecimal(0));
					}
					////////////////////
					FooterUtil.setFooterMap("v" + s, dtlList.get(k).getCheckQty(), footerMap);
					FooterUtil.setFooterMap("allCount", dtlList.get(k).getCheckQty(), footerMap);*/
					////////////////////

				}
				dtl.setAllCount(allCounts);
				billCheckImRepList.add(dtl);
			}
			/*if (pageNo == 1) {
				footer1Map.putAll(footerMap);//1.将小计直接设值到汇总
				footer1Map.putAll(footerMap);//1.将小计直接设值到汇总
				footer1Map.put("brandName", "汇总");
				footer1Map.put("isselectsum", true);
				footerList.add(footer1Map);
				if (total > pageSize) {
					while ((pageNo * pageSize) < total) {//2.批量处理(以防数据过大),从第二页开始
						pageNo++;
						rep.setPageNo(pageNo);
						rep.setPageSize(pageSize);
						tempList = billImCheckDtlManager.getBillImCheckByGroup(rep, authorityParams);
						for (int z = 0; z < tempList.size(); z++) {
							BigDecimal allCounts = new BigDecimal(0);
							BillCheckImRep model2 = new BillCheckImRep();
							model2.setItemNo(tempList.get(z).getItemNo());
							model2.setImportNo(tempList.get(z).getImportNo());
							model2.setLocNo(user.getLocNo());

							List<BillCheckImRep> dtlList = billImCheckDtlManager.getBillImCheckDtl(model2,
									authorityParams);
							BillCheckImRep dtl = tempList.get(z);
							Map<String, BigDecimal> maps = new HashMap<String, BigDecimal>();
							for (int k = 0; k < dtlList.size(); k++) {
								BillCheckImRep r = (BillCheckImRep) dtlList.get(k);
								String key = r.getImportNo() + "|" + r.getItemNo() + "|" + r.getSizeCode();
								BigDecimal qty = maps.get(key);
								if (qty == null) {
									maps.put(key, r.getCheckQty());
								} else {
									maps.put(key, qty.add(r.getCheckQty()));
								}
								int s = map.get(dtlList.get(k).getSizeCode());
								String filedName = "setV" + s;
								if (maps.get(key) != null) {
									Object[] arg = new Object[] { maps.get(key).toString() };
									CommonUtil.invokeMethod(dtl, filedName, arg);
									allCounts = allCounts.add(dtlList.get(k).getCheckQty());
								} else {
									Object[] arg = new Object[] { "0" };
									CommonUtil.invokeMethod(dtl, filedName, arg);
									allCounts = allCounts.add(new BigDecimal(0));
								}
								////////////////////
								FooterUtil.setFooterMap("v" + s, dtlList.get(k).getCheckQty(), footer1Map);
								FooterUtil.setFooterMap("allCount", dtlList.get(k).getCheckQty(), footer1Map);
								////////////////////
							}
						}
					}
				}
			}*/
			obj.put("total", total);
			obj.put("rows", billCheckImRepList);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			new ManagerException(e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	// 获取尺码横排头部信息
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

	@RequestMapping(value = "/do_export4Detail")
	public void doExport(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info)
			throws ManagerException {
		try {
			/*导出信息*/
			String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req.getParameter("preColNames");
			/*合计*/
			String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req.getParameter("endColNames");
			String fileName = StringUtils.isEmpty(req.getParameter("fileName")) ? "" : req.getParameter("fileName");
			String locno = StringUtils.isEmpty(req.getParameter("locno")) ? "" : req.getParameter("locno");
			/*收货单号*/
			String receiptNo = StringUtils.isEmpty(req.getParameter("receiptNo")) ? "" : req.getParameter("receiptNo");
			ObjectMapper mapper = new ObjectMapper();
			List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
			List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();
			if (StringUtils.isNotEmpty(preColNames)) {
				preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}
			if (StringUtils.isNotEmpty(endColNames)) {
				endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("receiptNo", receiptNo);
			List<BillImReceiptDtl> dtls = billImReceiptDtlManager.find4Export(params);
			List<String> sysArray = billImReceiptDtlManager.findSysNoList(params);
			if(sysArray != null && sysArray.size() > 0){
				HSSFWorkbook wb = null;
				int idx = 0;
				params = new HashMap<String, Object>();
				for(String sysNo : sysArray){
					params.put("sysNo", sysNo);
					List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null, params);
					Map<String,List<String>> sizeTypeMap = new TreeMap<String, List<String>>();
					for(SizeInfo si : sizeTypeList){
						String sizeKind = si.getSizeKind();
						if(sizeTypeMap.get(sizeKind) == null){
							List<String> sizeCodeByKind = new ArrayList<String>();
							sizeCodeByKind.add(si.getSizeCode());
							sizeTypeMap.put(sizeKind, sizeCodeByKind);
						}else{
							sizeTypeMap.get(sizeKind).add(si.getSizeCode());
						}
					}
					List<BillImReceiptDtl> data = billImReceiptDtlManager.findDataBySys(locno, receiptNo, sysNo,dtls);
					if(data != null && data.size() > 0){						
						wb = ExcelUtils.getSheet(preColNamesList, sizeTypeMap, endColNamesList, fileName, data, true, idx++, SystemCache.getLookUpName("SYS_NO", sysNo), wb);	
					}
				}
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
				response.setHeader("Pragma", "no-cache");
				wb.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}
}