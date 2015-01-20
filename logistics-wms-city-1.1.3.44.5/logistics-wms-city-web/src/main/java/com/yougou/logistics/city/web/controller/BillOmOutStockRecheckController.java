package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmOutstockDtlManager;
import com.yougou.logistics.city.manager.BillOmRecheckDtlManager;
import com.yougou.logistics.city.manager.BillOmRecheckManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.manager.StoreManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/**
 * TODO: 拣货复核
 * 
 * @author su.yq
 * @date 2013-12-16 下午3:29:46
 * @version 0.1.0 
 * @copyright yougou.com 
 */

@Controller
@RequestMapping("/bill_om_outstock_recheck")
@ModuleVerify("25080060")
public class BillOmOutStockRecheckController extends BaseCrudController<BillOmRecheck> {
	@Log
	private Logger log;
	@Resource
	private BillOmRecheckManager billOmRecheckManager;

	@Resource
	private ProcCommonManager procCommonManager;

	@Resource
	private StoreManager storeManager;

	@Resource
	private BillOmOutstockDtlManager billOmOutstockDtlManager;

	@Resource
	BillOmRecheckDtlManager billOmRecheckDtlManager;

	@Resource
	SizeInfoManager sizeInfoManager;

	private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public CrudInfo init() {
		return new CrudInfo("outstockrecheck/", billOmRecheckManager);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmRecheckManager.findOutstockRecheckCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmRecheck> list = this.billOmRecheckManager.findOutstockRecheckByPage(page, sortColumn, sortOrder, params,authorityParams);
			
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal totalRecheckQty = new BigDecimal(0);
			BigDecimal totalPackageQty = new BigDecimal(0);
			for (BillOmRecheck billOmRecheck : list) {
//				Map<String, Object> mapObj = new HashMap<String, Object>();
//				mapObj.put("locno", billOmRecheck.getLocno());
//				mapObj.put("storeNo", billOmRecheck.getStoreNo());
//				mapObj.put("divideNo", billOmRecheck.getDivideNo());
//				Map<String, Object> sumFoot = billOmRecheckManager.selectSumQty(mapObj, authorityParams);
//				BigDecimal itemQty = (BigDecimal) sumFoot.get("itemQty");
//				BigDecimal realQty = (BigDecimal)sumFoot.get("realQty");
				totalRealQty = totalRealQty.add(billOmRecheck.getRealQty());
				totalRecheckQty = totalRecheckQty.add(billOmRecheck.getRecheckQty());
				totalPackageQty = totalPackageQty.add(billOmRecheck.getPackageQty());
			}
			
			footer.put("recheckNo", "小计");
			footer.put("realQty", totalRealQty);
			footer.put("recheckQty", totalRecheckQty);
			footer.put("packageQty", totalPackageQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmRecheckManager.selectOutstockRecheckSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("real_qty", 0);
					sumFoot.put("recheck_qty", 0);
					sumFoot.put("package_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("recheck_no", "合计");
			} else {
				sumFoot.put("recheckNo", "合计");
			}
			footerList.add(sumFoot);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			obj.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	
	
	
	@RequestMapping(value = "/findConvertRecheck.json")
	@ResponseBody
	public Map<String, Object> findConvertRecheck(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmRecheckDtlManager.findRecheckDtlGroupByBoxCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmRecheckDtl> list = this.billOmRecheckDtlManager.findRecheckDtlGroupByBoxPage(page, sortColumn,sortOrder, params, authorityParams);

			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			for (BillOmRecheckDtl dtl : list) {
				totalItemQty = totalItemQty.add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty());
				totalRealQty = totalRealQty.add(dtl.getRealQty() == null ? new BigDecimal(0) : dtl.getRealQty());
				dtl.setRecheckQty(dtl.getRealQty());
				dtl.setDiffQty(new BigDecimal(0));
				dtl.setPackageNoRecheckQty(new BigDecimal(0));
			}

			footer.put("itemNo", "小计");
			footer.put("itemQty", 0);
			footer.put("realQty", totalRealQty);
			footer.put("recheckQty", totalRealQty);
			footer.put("diffQty", 0);
			footer.put("packageNoRecheckQty", 0);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmRecheckDtlManager.selectSumQty(params, authorityParams);
				String itemQty = "0";
				String realQty = "0";
				if (sumFoot != null) {
//					if (sumFoot.get("itemQty") != null) {
//						itemQty = sumFoot.get("itemQty").toString();
//					}
					if (sumFoot.get("realQty") != null) {
						realQty = sumFoot.get("realQty").toString();
					}
				}else{
					sumFoot = new SumUtilMap<String, Object>();
				}
				sumFoot.put("item_qty", itemQty);
				sumFoot.put("real_qty", realQty);
				sumFoot.put("recheck_qty", realQty);
				sumFoot.put("diff_qty", 0);
				sumFoot.put("package_no_recheck_qty", 0);
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
	
	
	
	/**
	 * 获取箱号
	 * @param locno
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/getLabelNo")
	@ResponseBody
	public Map<String, String> getLabelNo(String locno, HttpServletRequest req) throws ManagerException {
		Map<String, String> result = null;
		try {
			CurrentUser currentUser = new CurrentUser(req);
			result = procCommonManager.procGetContainerNoBase(locno, "C", currentUser.getUserid().toString(), "F", "1",
					"1", "");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
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

			//keys = "006|006OC14021500017|006DO14021500010|SDEUT2"; //locno|recheckNo|divideNo|storeNo;
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
				//返回参数列表
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Map<String, String> sizeColMap = new TreeMap<String, String>();
				List<String> sizeColList = new ArrayList<String>();

				//List<BillOmRecheckDtl> listTempGroup = billOmRecheckDtlManager.findGroupByBoxAndItem(dtl);
				//storeNo=SDEUT2, page=1, pageSize=20, locno=006, pageNumber=1, orderby=asc, divideNo=006DO14021500010, rows=20, pageIndex=0
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", strs[0]);
				params.put("locateNo", strs[2]);
				params.put("storeNo", strs[3]);
				List<BillOmOutstockDtlDto> listTempGroup = billOmOutstockDtlManager.findRecheckOutstockItem(params);
				if (CollectionUtils.isEmpty(listTempGroup)) {
					result.put("result", "error");
					result.put("msg", "没有查询到明细");
				}
				for (BillOmOutstockDtlDto gvo : listTempGroup) {

					if (CommonUtil.hasValue(gvo.getItemNo())) {
						BillOmRecheckDtl omRecheckDtl = new BillOmRecheckDtl();
						omRecheckDtl.setRecheckNo(strs[1]);
						omRecheckDtl.setItemNo(gvo.getItemNo());
						omRecheckDtl.setBoxNo(gvo.getScanLabelNo());
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
						//for (BillOmRecheckDtl temp : listTempMxList) {
						for (int i = 0; i < sizeInfoList.size(); i++) {
							SizeInfo sizeInfo = sizeInfoList.get(i);
							if (gvo.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对

								BigDecimal a = new BigDecimal(0);
								if (null != gvo.getRealQty()) {
									a = gvo.getRealQty();
									sizeColMap.put(sizeInfo.getSizeCode(), sizeInfo.getSizeCode());
									boxAndItem.put(sizeInfo.getSizeCode(), a);
								}
								allCounts = allCounts.add(a);
								break;
							}
						}
						//}
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
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		}
		return result;
	}

	public List<Map<String, Object>> groupByItemNo(List<Map<String, Object>> list, List<String> sizeColList) {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		try {
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
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return newList;
	}

	/**
	 * 保存主表信息
	 * @param req
	 * @param billOmRecheck
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String saveMainInfo(HttpServletRequest req, BillOmRecheck billOmRecheck) throws JsonParseException,
			JsonMappingException, IOException, ManagerException {
		String recheckNo = "";
		try {
			//判断新建状态的复核单中是否存在改波次单号
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("locno", billOmRecheck.getLocno());
			params.put("status", "10");
			params.put("storeNo", billOmRecheck.getStoreNo());
			params.put("divideNo", billOmRecheck.getDivideNo());
			List<BillOmRecheck> list=billOmRecheckManager.findByBiz(billOmRecheck, params);
			if(!(null!=list && list.size()>0)){
				recheckNo = procCommonManager.procGetSheetNo(billOmRecheck.getLocno(), CNumPre.RECHECK_PRE);
				billOmRecheck.setRecheckNo(recheckNo);
				billOmRecheck.setStatus("10");//新建状态
				billOmRecheck.setRecheckType("1");
				billOmRecheck.setCreatetm(new Date());
				billOmRecheck.setEdittm(new Date());
				billOmRecheckManager.add(billOmRecheck);
				return "{'success':'true','recheckNo':'"+recheckNo+"'}";
			}else{
				return "{'success':'false','status':'1'}";
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "{'success':'false','status':'0'}";
		}
	}

	/**
	 * 审核复核单
	 * @param ids
	 * @param checkUser
	 * @param req
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/check")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> check(String ids, String checkUser, HttpServletRequest req)
			throws JsonParseException, JsonMappingException, IOException, ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmRecheckManager.checkOutStock(ids, user, checkUser);
			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	/**
	 * 删除复核单
	 * @param datas
	 * @param req
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ManagerException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteBillOmRecheck")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public ResponseEntity<Map<String, Object>> deleteBillOmRecheck(String datas, HttpServletRequest req)
			throws JsonParseException, JsonMappingException, IOException, ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {

			datas = URLDecoder.decode(datas, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillOmRecheck> listOmRechecks = new ArrayList<BillOmRecheck>();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				listOmRechecks = convertListWithTypeReferenceR(mapper, list);
			}
			billOmRecheckManager.deleteBillOmOutStockRecheck(listOmRechecks);
			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	/**
	 * 复核装箱操作
	 * @param req
	 * @param datas
	 * @param boxNo
	 * @param recheckNo
	 * @param locno
	 * @param divideNo
	 * @param storeNo
	 * @param recheckName
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws ManagerException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/packagebox")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public ResponseEntity<Map<String, Object>> packagebox(HttpServletRequest req, String datas, String boxNo,
			String recheckNo, String locno, String divideNo, String storeNo, String recheckName)
			throws JsonParseException, JsonMappingException, IOException, ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			datas = URLDecoder.decode(datas, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();

			//分货单详细  刚复核的数据
			List<BillOmOutstockDtlDto> dtlLst = new ArrayList<BillOmOutstockDtlDto>();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				dtlLst = convertListWithTypeReferenceD(mapper, list);
			}

			try {
				HttpSession session = req.getSession();
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
				BillOmRecheck billOmRecheck = new BillOmRecheck();
				billOmRecheck.setLocno(locno);
				billOmRecheck.setRecheckNo(recheckNo);
				billOmRecheck.setDivideNo(divideNo);
				billOmRecheck.setStoreNo(storeNo);
				billOmRecheck.setCreator(user.getLoginName());
				billOmRecheck.setEditor(user.getLoginName());
				billOmRecheck.setEditorname(user.getUsername());
				billOmRecheckManager.packageBoxOutstock(dtlLst, billOmRecheck, boxNo);
				flag.put("success", "true");
				return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				flag.put("success", "false");
				flag.put("msg", e.getMessage());
				return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/packageBoxOutstockRf")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> packageBoxOutstockRf(HttpServletRequest req, String datas, String boxNo,
			String recheckNo, String locno, String divideNo, String storeNo, String recheckName)
			throws JsonParseException, JsonMappingException, IOException, ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			BillOmRecheck billOmRecheck = new BillOmRecheck();
			billOmRecheck.setLocno(locno);
			billOmRecheck.setRecheckNo(recheckNo);
			billOmRecheck.setDivideNo(divideNo);
			billOmRecheck.setStoreNo(storeNo);
			billOmRecheck.setCreator(user.getLoginName());
			billOmRecheckManager.packageBoxOutstockRf(billOmRecheck);
			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/findBillOmOutStock")
	@ResponseBody
	public Map<String, Object> findBillOmOutStock(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = this.billOmOutstockDtlManager.findBillOmOutstockCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDtl> list = this.billOmOutstockDtlManager.findBillOmOutstockByPage(page, sortColumn,
					sortOrder, params, authorityParams);

			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/selectStoreFromOutStockDtl")
	@ResponseBody
	public List<Store> selectStoreFromOutStockDtl(HttpServletRequest req, Model model) throws ManagerException {
		List<Store> result = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			result = billOmOutstockDtlManager.findStoreByParam(params, authorityParams);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	@RequestMapping(value = "/queryRecheckOutstockItem")
	@ResponseBody
	public Map<String, Object> queryRecheckBoxItem(HttpServletRequest req, Model model) throws ManagerException {

		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List<Object> footerList = new ArrayList<Object>();
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmOutstockDtlManager.findRecheckOutstockItemCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDtlDto> result = this.billOmOutstockDtlManager.findRecheckOutstockItemByPage(page,
					sortColumn, sortOrder, params, authorityParams);

			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal torecheckQty = new BigDecimal(0);
			BigDecimal totalPackageNoRecheckQty = new BigDecimal(0);
			int totalDiffQty = 0;
			for (BillOmOutstockDtlDto b : result) {
				totalItemQty = totalItemQty.add(b.getItemQty() == null ? new BigDecimal(0) : b.getItemQty());
				totalRealQty = totalRealQty.add(b.getRealQty() == null ? new BigDecimal(0) : b.getRealQty());
				torecheckQty = torecheckQty.add(b.getItemQty() == null ? new BigDecimal(0) : b.getRecheckQty());
				totalDiffQty = totalDiffQty + (b.getDiffQty() == null ? 0 : b.getDiffQty().intValue());
				totalPackageNoRecheckQty = totalPackageNoRecheckQty.add(b.getPackageNoRecheckQty() == null ? new BigDecimal(0) : b.getPackageNoRecheckQty());
			}

			Map<String, Object> footer = new HashMap<String, Object>();
			footer.put("ownerName", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footer.put("diffQty", totalDiffQty);
			footer.put("recheckQty", torecheckQty);
			footer.put("packageNoRecheckQty", totalPackageNoRecheckQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmOutstockDtlManager.selectRecheckOutstockItemSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
					sumFoot.put("diff_qty", 0);
					sumFoot.put("recheck_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("owner_name", "合计");
			} else {
				sumFoot.put("ownerName", "合计");
			}
			footerList.add(sumFoot);
			obj.put("total", total);
			obj.put("rows", result);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	//	public  List<BillOmOutstockDtlDto> queryRecheckBoxItem(HttpServletRequest req, Model model) throws ManagerException {
	//	Map<String, Object> params = builderParams(req, model);
	//	List<BillOmOutstockDtlDto> result = billOmOutstockDtlManager.findRecheckOutstockItem(params);
	//	return result;
	//}

	@SuppressWarnings({ "rawtypes" })
	private List<BillOmRecheck> convertListWithTypeReferenceR(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmRecheck> tl = new ArrayList<BillOmRecheck>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmRecheck type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillOmRecheck.class);
			tl.add(type);
		}
		return tl;
	}

	@SuppressWarnings({ "rawtypes" })
	private List<BillOmOutstockDtlDto> convertListWithTypeReferenceD(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmOutstockDtlDto> tl = new ArrayList<BillOmOutstockDtlDto>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmOutstockDtlDto type = mapper.readValue(mapper.writeValueAsString(list.get(i)),
					BillOmOutstockDtlDto.class);
			tl.add(type);
		}
		return tl;
	}
}
