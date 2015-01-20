package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckDtlSizeKind;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillOmRecheckDtlManager;
import com.yougou.logistics.city.manager.BillOmRecheckManager;
import com.yougou.logistics.city.manager.ItemManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.manager.StoreManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 分货复核单明细controller
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:22:51
 * @version 0.1.0
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_recheck_dtl")
public class BillOmRecheckDtlController extends BaseCrudController<BillOmRecheckDtl> {
	@Log
	private Logger log;
	@Resource
	private BillOmRecheckDtlManager billOmRecheckDtlManager;
	@Resource
	private BillOmRecheckManager billOmRecheckManager;

	@Resource
	private ItemManager itemManager;

	@Resource
	private StoreManager storeManager;

	@Resource
	private SizeInfoManager sizeInfoManager;

	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public CrudInfo init() {
		return new CrudInfo("billomrecheckdtl/", billOmRecheckDtlManager);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	@Override
	public Map<String, Object> queryList(HttpServletRequest req, Model model) {
		// int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 :
		// Integer.parseInt(req.getParameter("page"));
		// int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 :
		// Integer.parseInt(req.getParameter("rows"));
		// String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ?
		// "" : String.valueOf(req.getParameter("sort"));
		// String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ?
		// "" : String.valueOf(req.getParameter("order"));
		// Map<String, Object> params = builderParams(req, model);
		// int total = this.billOmRecheckDtlManager.findCount(params);
		// SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		// List<BillOmRecheckDtl> list =
		// this.billOmRecheckDtlManager.findByPage(page, sortColumn, sortOrder,
		// params);
		// for(BillOmRecheckDtl dtl : list){
		// Item item = new Item();
		// item.setItemNo(dtl.getItemNo());
		// item = itemManager.findById(item);
		// dtl.setItem(item);
		// }
		// Map<String, Object> obj = new HashMap<String, Object>();
		// obj.put("total", total);
		// obj.put("rows", list);
		// return obj;
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmRecheckDtlManager.findBillOmRecheckDtlCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmRecheckDtl> list = this.billOmRecheckDtlManager.findBillOmRecheckDtlByPage(page, sortColumn,
					sortOrder, params, authorityParams);

			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillOmRecheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty() == null ? new BigDecimal(0) : dtl.getRealQty());
			}

			footer.put("scanLabelNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmRecheckDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("scan_label_no", "合计");
			} else {
				sumFoot.put("scanLabelNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;

	}

	@RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public Map<String, Object> dtlList(HttpServletRequest req, Model model) {
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
			int total = this.billOmRecheckDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmRecheckDtl> list = this.billOmRecheckDtlManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);

			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillOmRecheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty() == null ? new BigDecimal(0) : dtl.getRealQty());
			}

			footer.put("itemNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmRecheckDtlManager.selectRfSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_no", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	// 保存明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateOmRecheckDtl")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> updateOmRecheckDtl(BillOmRecheck recheck, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			List<BillOmRecheckDtl> updateList = new ArrayList<BillOmRecheckDtl>();
			
			//获取登陆用户
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(updated)) {
				List<Map> list = mapper.readValue(updated, new TypeReference<List<Map>>() {
				});
				updateList = convertListWithTypeReference(mapper, list);
			}
			billOmRecheckDtlManager.updateOmRecheckDtl(recheck, updateList,user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "更新失败,请联系管理员!");
		}
		return map;
	}
	
	// 删除明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteOmRecheckDtl")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteOmRecheckDtl(BillOmRecheck recheck, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			List<BillOmRecheckDtl> deleteList = new ArrayList<BillOmRecheckDtl>();
			
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference(mapper, list);
			}
			
			//获取登陆用户
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmRecheckDtlManager.deleteOmRecheckDtl(recheck, deleteList,user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "删除失败,请联系管理员!");
		}
		return map;
	}
	
	
	// 保存拣货明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateOmOutstockRecheckDtl")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> updateOmOutstockRecheckDtl(BillOmRecheck recheck, HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String inserted = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			String updated = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			
			List<BillOmRecheckDtl> updateList = new ArrayList<BillOmRecheckDtl>();
			List<BillOmRecheckDtl> insertList = new ArrayList<BillOmRecheckDtl>();
			
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
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if(recheck != null){
				recheck.setCreator(user.getLoginName());
				recheck.setCreatorname(user.getUsername());
			}
			billOmRecheckDtlManager.updateOmOutstockRecheckDtl(recheck, updateList, insertList,user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "更新失败,请联系管理员!");
		}
		return map;
	}
	
	// 删除拣货明细
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteOmOutstockRecheckDtl")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteOmOutstockRecheckDtl(BillOmRecheck recheck, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String deleted = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			List<BillOmRecheckDtl> deleteList = new ArrayList<BillOmRecheckDtl>();
			
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted, new TypeReference<List<Map>>() {
				});
				deleteList = convertListWithTypeReference(mapper, list);
			}
			//获取登陆用户
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmRecheckDtlManager.deleteOmOutstockRecheckDtl(recheck, deleteList,user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "删除失败,请联系管理员!");
		}
		return map;
	}

	@RequestMapping(value = "/printDetail")
	@ResponseBody
	public Map<String, Object> printDetail(HttpServletRequest req, HttpSession session, String keys) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			result.put("data", resultList);
			if (StringUtils.isEmpty(keys)) {
				result.put("result", "error");
				result.put("msg", "参数错误");
			}

			// keys = "006|006OC14021500017|006DO14021500010|SDEUT2";
			// //locno|recheckNo|divideNo|storeNo;

			String[] keysArray = keys.split(",");
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			for (String str : keysArray) {
				BigDecimal totalAllCount = new BigDecimal(0);
				String[] strs = str.split("\\|");
				Map<String, Object> resultObj = new HashMap<String, Object>();
				BillOmRecheckDtl dtl = new BillOmRecheckDtl();
				dtl.setLocno(user.getLocNo());
				dtl.setRecheckNo(strs[0]);
				resultObj.put("recheckNo", strs[1]);
				resultObj.put("receipter", strs[3]);
				Store store = new Store();
				store.setStoreNo(strs[3]);
				store = storeManager.findById(store);
				if (store != null && !StringUtils.isBlank(store.getStoreName())) {
					resultObj.put("receipterName", store.getStoreName());
				}
				int total = 0;
				// 返回参数列表
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Map<String, String> sizeColMap = new TreeMap<String, String>();
				List<String> sizeColList = new ArrayList<String>();

				// List<BillOmRecheckDtl> listTempGroup =
				// billOmRecheckDtlManager.findGroupByBoxAndItem(dtl);
				// storeNo=SDEUT2, page=1, pageSize=20, locno=006, pageNumber=1,
				// orderby=asc, divideNo=006DO14021500010, rows=20, pageIndex=0
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", strs[0]);
				params.put("divideNo", strs[2]);
				params.put("storeNo", strs[3]);
				List<BillOmDivideDtl> listTempGroup = billOmRecheckManager.queryRecheckBoxItem(params);
				if (CollectionUtils.isEmpty(listTempGroup)) {
					result.put("result", "error");
					result.put("msg", "没有查询到明细");
				}
				for (BillOmDivideDtl gvo : listTempGroup) {

					if (CommonUtil.hasValue(gvo.getItemNo())) {
						BillOmRecheckDtl omRecheckDtl = new BillOmRecheckDtl();
						omRecheckDtl.setRecheckNo(strs[1]);
						omRecheckDtl.setItemNo(gvo.getItemNo());
						omRecheckDtl.setBoxNo(gvo.getBoxNo());
						List<BillOmRecheckDtl> listTempMxList = billOmRecheckDtlManager.findSizeNoDetail(omRecheckDtl);
						BigDecimal allCounts = new BigDecimal(0);
						if (CollectionUtils.isEmpty(listTempMxList)) {
							continue;
						}
						Map<String, Object> boxAndItem = new HashMap<String, Object>();

						BillOmRecheckDtl dto = listTempMxList.get(0);

						boxAndItem.put("itemNo", dto.getItemNo());
						boxAndItem.put("itemName", dto.getItemName());
						boxAndItem.put("boxNo", dto.getBoxNo());
						boxAndItem.put("colorName", dto.getColorName());
						SizeInfo sizeInfoParamInfo = new SizeInfo();
						Map<String, Object> mapParaMap = new HashMap<String, Object>();
						mapParaMap.put("sysNo", dto.getSysNo());
						mapParaMap.put("sizeKind", dto.getSizeKind());
						List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
						// for (BillOmRecheckDtl temp : listTempMxList) {
						for (int i = 0; i < sizeInfoList.size(); i++) {
							SizeInfo sizeInfo = sizeInfoList.get(i);
							if (gvo.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对

								BigDecimal a = new BigDecimal(0);
								BigDecimal temp = new BigDecimal(0);
								if (null != gvo.getRealQty()) {
									a = gvo.getRealQty();
									if (a != null && a.intValue() != 0) {
										sizeColMap.put(sizeInfo.getSizeCode(), sizeInfo.getSizeCode());
										if (boxAndItem.get(sizeInfo.getSizeCode()) == null) {
											boxAndItem.put(sizeInfo.getSizeCode(), a);
										} else {
											temp = (BigDecimal) boxAndItem.get(sizeInfo.getSizeCode());
											boxAndItem.put(sizeInfo.getSizeCode(), temp.add(a));
										}

									}
								}
								allCounts = allCounts.add(a);
								break;
							}
						}
						// }
						boxAndItem.put("allCount", allCounts);
						totalAllCount = totalAllCount.add(allCounts);
						list.add(boxAndItem);
					}
				}
				for (Entry<String, String> m : sizeColMap.entrySet()) {
					sizeColList.add(m.getKey());
				}
				resultObj.put("creator", user.getUsername());
				resultObj.put("creattm", format.format(new Date()));
				resultObj.put("sender", user.getLocName());
				resultObj.put("result", "success");
				resultObj.put("total", total);
				resultObj.put("rows", groupByItemNo(list, sizeColList));
				resultObj.put("sizeCols", sizeColList);
				resultObj.put("totalAllCount", totalAllCount);
				resultList.add(resultObj);
			}
			result.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		}
		return result;
	}

	public List<Map<String, Object>> groupByItemNo(List<Map<String, Object>> list, List<String> sizeColList) {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		Map<String, List<Map<String, Object>>> group = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> tempList = null;
		for (Map<String, Object> map : list) {
			tempList = group.get(map.get("itemNo").toString());
			if (tempList == null) {
				tempList = new ArrayList<Map<String, Object>>();
				tempList.add(map);
				group.put(map.get("itemNo").toString(), tempList);
			} else {
				tempList.add(map);
			}
		}
		for (Entry<String, List<Map<String, Object>>> itemListMap : group.entrySet()) {
			List<Map<String, Object>> itemList = itemListMap.getValue();
			Map<String, Object> itemF = null;
			for (Map<String, Object> item : itemList) {
				if (itemF == null) {
					itemF = item;
				} else {
					for (String size : sizeColList) {
						Object obj = item.get(size);
						if (obj != null) {
							itemF.put(size, obj);
							itemF.put("allCount",
									((BigDecimal) itemF.get("allCount")).add((BigDecimal) item.get("allCount")));
						}
					}
				}
			}
			newList.add(itemF);
		}
		return newList;
	}

	@RequestMapping(value = "/printByBox")
	@ResponseBody
	public Map<String, Object> printByBox(HttpServletRequest req, HttpSession session) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String result = "";
			String locno = req.getParameter("locno");
			String recheckNo = req.getParameter("recheckNo");
			String keys = req.getParameter("keys");
			boolean noneDtl = "true".equals(req.getParameter("noneDtl"));
			Object u = session.getAttribute(PublicContains.SESSION_USER);
			SystemUser user = null;
			if (u != null) {
				user = (SystemUser) u;
			}
			if (StringUtils.isBlank(locno) || StringUtils.isBlank(keys)) {
				result = "缺少参数";
				obj.put("result", result);
			} else {
				obj = billOmRecheckDtlManager.printByBox(locno, recheckNo, keys, noneDtl, user);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		}
		return obj;
	}

	@RequestMapping(value = "/printDetailShowBox")
	@ResponseBody
	public Map<String, Object> printDetailShowBox(HttpServletRequest req, HttpSession session) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String result = "";
			String keys = req.getParameter("keys");
			Object u = session.getAttribute(PublicContains.SESSION_USER);
			SystemUser user = null;
			if (u != null) {
				user = (SystemUser) u;
			}
			if (StringUtils.isBlank(keys)) {
				result = "缺少参数";
				obj.put("result", result);
			} else {
				obj = billOmRecheckDtlManager.printDetailShowBox(keys, user);
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", e.getMessage());
		}
		return obj;
	}

	@RequestMapping(value = "/printDetail4SizeHorizontal")
	@ResponseBody
	public Map<String, Object> printDetail4SizeHorizontal(HttpServletRequest req, HttpSession session, String keys,
			SizeInfo info, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");

		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			String[] keysArray = keys.split(",");
			for (String str : keysArray) {
				String[] strs = str.split("\\|");
				// 返回 Map集合
				Map<String, Object> obj = new HashMap<String, Object>();
				// 查询主档信息
				BillOmRecheck param = new BillOmRecheck();
				param.setLocno(strs[0]);
				param.setRecheckNo(strs[1]);
				BillOmRecheck recheck = this.billOmRecheckManager.findById(param);
				Store store = new Store();
				store.setStoreNo(recheck.getStoreNo());
				store = storeManager.findById(store);
				Map<String, Object> main = new HashMap<String, Object>();
				if (store != null && !StringUtils.isBlank(store.getStoreName())) {
					main.put("receipterName", store.getStoreName());
				} else {
					main.put("receipterName", "");
				}
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				main.put("sender", user.getLocName());
				main.put("receipter", store.getStoreNo());
				main.put("recheckNo", recheck.getRecheckNo());
				main.put("creator", user.getUsername());
				main.put("creattm", format.format(new Date()));
				obj.put("main", main);
				List<BillOmRecheckDtlSizeKind> list = new ArrayList<BillOmRecheckDtlSizeKind>();
				// 返回汇总列表
				List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
				Map<String, Object> footerMap = new HashMap<String, Object>();
				footerMap.put("itemNo", "汇总");
				footerList.add(footerMap);
				// 根据商品编码分组查询明细
				List<BillOmRecheckDtlSizeKind> listTempGroup = billOmRecheckDtlManager.selectDtlGroupByItemNo(param);
				if (CollectionUtils.isEmpty(listTempGroup)) {
					result.put("result", ResultEnums.FAIL.getResultMsg());
					result.put("msg", "没有查询到明细");
					return result;
				}
				String sysNo = "";
				BigDecimal sumQty = new BigDecimal(0);
				for (BillOmRecheckDtlSizeKind gvo : listTempGroup) {
					if (CommonUtil.hasValue(gvo.getItemNo())) {

						List<BillOmRecheckDtlSizeKind> listTempMxList = billOmRecheckDtlManager.selectAllDtl4Print(gvo);
						BigDecimal allCounts = new BigDecimal(0);
						if (CollectionUtils.isEmpty(listTempMxList)) {
							continue;
						}
						BillOmRecheckDtlSizeKind dto = listTempMxList.get(0);
						sysNo = dto.getSysNo();
						// 查询品牌下所以尺码号
						SizeInfo sizeInfoParamInfo = new SizeInfo();
						Map<String, Object> mapParaMap = new HashMap<String, Object>();
						mapParaMap.put("sysNo", dto.getSysNo());
						mapParaMap.put("sizeKind", dto.getSizeKind());
						List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
						for (BillOmRecheckDtlSizeKind temp : listTempMxList) {
							for (int i = 0; i < sizeInfoList.size(); i++) {
								SizeInfo tempSizeInfo = sizeInfoList.get(i);
								// 匹配尺码
								if (temp.getSizeNo().equals(tempSizeInfo.getSizeNo())) { // 相对
									BigDecimal a = new BigDecimal(0);
									if (null != temp.getRealQty()) {
										a = temp.getRealQty();
									}
									Object[] arg = new Object[] { a.toString() };
									String filedName = "setV" + (i + 1);
									CommonUtil.invokeMethod(dto, filedName, arg);
									allCounts = allCounts.add(a);
									FooterUtil.setFooterMap("v" + (i + 1), temp.getRealQty(), footerMap);
									FooterUtil.setFooterMap("allCount", temp.getRealQty(), footerMap);
									sumQty = sumQty.add(temp.getRealQty());
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
				List<String> allKind = billOmRecheckDtlManager.selectAllDtlSizeKind(recheck);
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

	@RequestMapping(value = "/printDetailBox4SizeHorizontal")
	@ResponseBody
	public Map<String, Object> printDetailBox4SizeHorizontal(HttpServletRequest req, HttpSession session, String keys,
			SizeInfo info, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");

		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			String[] keysArray = keys.split(",");
			for (String str : keysArray) {
				String[] strs = str.split("\\|");
				// 返回 Map集合
				Map<String, Object> obj = new HashMap<String, Object>();
				// 查询主档信息
				BillOmRecheck param = new BillOmRecheck();
				param.setLocno(strs[0]);
				param.setRecheckNo(strs[1]);
				BillOmRecheck recheck = this.billOmRecheckManager.findById(param);
				Store store = new Store();
				store.setStoreNo(recheck.getStoreNo());
				store = storeManager.findById(store);
				Map<String, Object> main = new HashMap<String, Object>();
				if (store != null && !StringUtils.isBlank(store.getStoreName())) {
					main.put("receipterName", store.getStoreName());
				} else {
					main.put("receipterName", "");
				}
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				main.put("sender", user.getLocName());
				main.put("receipter", store.getStoreNo());
				main.put("recheckNo", recheck.getRecheckNo());
				main.put("creator", user.getUsername());
				main.put("creattm", format.format(new Date()));
				obj.put("main", main);
				List<BillOmRecheckDtlSizeKind> list = new ArrayList<BillOmRecheckDtlSizeKind>();
				// 返回汇总列表
				List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
				Map<String, Object> footerMap = new HashMap<String, Object>();
				footerMap.put("itemNo", "汇总");
				footerList.add(footerMap);
				// 根据商品编码分组查询明细
				List<BillOmRecheckDtlSizeKind> listTempGroup = billOmRecheckDtlManager
						.selectDtlGroupByItemNoAndBox(param);
				if (CollectionUtils.isEmpty(listTempGroup)) {
					result.put("result", ResultEnums.FAIL.getResultMsg());
					result.put("msg", "没有查询到明细");
					return result;
				}
				String sysNo = "";
				BigDecimal sumQty = new BigDecimal(0);
				for (BillOmRecheckDtlSizeKind gvo : listTempGroup) {
					if (CommonUtil.hasValue(gvo.getItemNo())) {

						List<BillOmRecheckDtlSizeKind> listTempMxList = billOmRecheckDtlManager
								.selectAllDtlBox4Print(gvo);
						BigDecimal allCounts = new BigDecimal(0);
						if (CollectionUtils.isEmpty(listTempMxList)) {
							continue;
						}
						BillOmRecheckDtlSizeKind dto = listTempMxList.get(0);
						sysNo = dto.getSysNo();
						// 查询品牌下所以尺码号
						SizeInfo sizeInfoParamInfo = new SizeInfo();
						Map<String, Object> mapParaMap = new HashMap<String, Object>();
						mapParaMap.put("sysNo", dto.getSysNo());
						mapParaMap.put("sizeKind", dto.getSizeKind());
						List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
						for (BillOmRecheckDtlSizeKind temp : listTempMxList) {
							for (int i = 0; i < sizeInfoList.size(); i++) {
								SizeInfo tempSizeInfo = sizeInfoList.get(i);
								// 匹配尺码
								if (temp.getSizeNo().equals(tempSizeInfo.getSizeNo())) { // 相对
									BigDecimal a = new BigDecimal(0);
									if (null != temp.getRealQty()) {
										a = temp.getRealQty();
									}
									Object[] arg = new Object[] { a.toString() };
									String filedName = "setV" + (i + 1);
									CommonUtil.invokeMethod(dto, filedName, arg);
									allCounts = allCounts.add(a);
									FooterUtil.setFooterMap("v" + (i + 1), temp.getRealQty(), footerMap);
									FooterUtil.setFooterMap("allCount", temp.getRealQty(), footerMap);
									sumQty = sumQty.add(temp.getRealQty());
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
				}
				info.setSysNo(sysNo);
				//查询所有的商品明细
				List<String> sizeKindList = billOmRecheckDtlManager.selectAllDtlSizeKind(recheck);

				Map header = getBrandList(preColNames, endColNames, sizeTypeFiledName, sysNo, sizeKindList);
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
		List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(list, sysNo,null);
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
	
	private List<BillOmRecheckDtl> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmRecheckDtl> tl = new ArrayList<BillOmRecheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmRecheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmRecheckDtl.class);
			tl.add(type);
		}
		return tl;
	}
}
