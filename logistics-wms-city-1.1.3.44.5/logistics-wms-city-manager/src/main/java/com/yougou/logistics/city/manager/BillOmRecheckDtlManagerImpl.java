package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckDtlSizeKind;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmDivideDtlService;
import com.yougou.logistics.city.service.BillOmRecheckDtlService;
import com.yougou.logistics.city.service.BillOmRecheckService;
import com.yougou.logistics.city.service.StoreService;

/**
 * 
 * 分货复核单明细manager实现
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:20:52
 * @version 0.1.0
 * @copyright yougou.com
 */
@Service("billOmRecheckDtlManager")
class BillOmRecheckDtlManagerImpl extends BaseCrudManagerImpl implements BillOmRecheckDtlManager {

	@Resource
	private BillOmRecheckDtlService billOmRecheckDtlService;
	@Resource
	private BillOmRecheckService billOmRecheckService;

	@Resource
	private StoreService storeService;

	@Resource
	private BillOmDivideDtlService billOmDivideDtlService;

	@Override
	public BaseCrudService init() {
		return billOmRecheckDtlService;
	}

	@Override
	public List<BillOmRecheckDtl> findBillOmRecheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billOmRecheckDtlService.findBillOmRecheckDtlByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findBillOmRecheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billOmRecheckDtlService.findBillOmRecheckDtlCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillOmRecheckDtl> findGroupByBoxAndItem(BillOmRecheckDtl dtl) throws ManagerException {
		try {
			return this.billOmRecheckDtlService.findGroupByBoxAndItem(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	public List<BillOmRecheckDtl> findSizeNoDetail(BillOmRecheckDtl dtl) throws ManagerException {
		try {
			return this.billOmRecheckDtlService.findSizeNoDetail(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	

	@Override
	public List<BillOmRecheckDtl> findRecheckDtlGroupByBoxPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmRecheckDtlService.findRecheckDtlGroupByBoxPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findRecheckDtlGroupByBoxCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmRecheckDtlService.findRecheckDtlGroupByBoxCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> printByBox(String locno, String recheckNo, String boxkNokeys, boolean noneDtl,
			SystemUser user) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, StringBuffer>> resultHtml = new ArrayList<Map<String, StringBuffer>>();
		obj.put("html", resultHtml);
		try {
			String[] boxNos = boxkNokeys.split(",");
			List<String> list = new ArrayList<String>();
			for (String box : boxNos) {
				if (!list.contains(box)) {
					list.add(box);
					params.put("locno", locno);
					params.put("recheckNo", recheckNo);
					params.put("scanLabelNo", box);
					List<BillOmRecheckDtl> dtllist = null;
					int rowTotal = billOmRecheckDtlService.findBillOmRecheckDtlCount(params, null);
					SimplePage page = new SimplePage(0, rowTotal, rowTotal);
					dtllist = billOmRecheckDtlService.findBillOmRecheckDtlByPage(page, null, null, params, null);
					StringBuffer headHtml = new StringBuffer();
					StringBuffer html = new StringBuffer();
					if (list != null && list.size() > 0) {
						headHtml = getHeadHtml(locno, recheckNo, dtllist, user);
						html.append(headHtml);
						html.append("</table>");
						Map<String, StringBuffer> resultObj = new HashMap<String, StringBuffer>();
						resultObj.put("table", html);
						resultObj.put("scanLabelNo", new StringBuffer(box));
						resultHtml.add(resultObj);
					} else {
						throw new ManagerException("没有可以打印的数据");
					}
				}
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}

		return obj;
	}

	private StringBuffer getHeadHtml(String locno, String recheckNo, List<BillOmRecheckDtl> list, SystemUser user) {
		StringBuffer headHtml = new StringBuffer();
		BigDecimal qtyTotal = new BigDecimal(0);
		Map<String, Object> itemMap = new HashMap<String, Object>();
		String storeName = "";
		for (BillOmRecheckDtl dtl : list) {
			qtyTotal = qtyTotal.add(dtl.getItemQty());
			itemMap.put(dtl.getItemNo(), "");
		}
		BillOmRecheck billOmRecheck = new BillOmRecheck();
		billOmRecheck.setRecheckNo(recheckNo);
		billOmRecheck.setLocno(locno);
		try {
			billOmRecheck = billOmRecheckService.findById(billOmRecheck);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		String storeNo = billOmRecheck.getStoreNo();
		if (!StringUtils.isBlank(storeNo)) {
			Store store = new Store();
			store.setStoreNo(storeNo);
			try {
				store = storeService.findById(store);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			if (store != null) {
				storeName = store.getStoreName();
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expNo", list.get(0).getExpNo());
		params.put("boxNo", list.get(0).getBoxNo());
		// String serialNo = "";
		// List<BillOmDivideDtl> divideDtls = null;
		/*
		 * try { divideDtls = billOmDivideDtlService.findByBiz(null, params); }
		 * catch (ServiceException e) { e.printStackTrace(); }
		 */
		/*
		 * if(divideDtls != null && divideDtls.size() > 0){ serialNo =
		 * divideDtls.get(0).getSerialNo(); }
		 */
		headHtml.append("<table  border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;font-size:12px;'>");
		headHtml.append("<tr bgcolor='#fff'><td  colspan='6' style='text-align:center;font-size:15px;'><b>送货箱标签</b></td></tr>");
		headHtml.append("<tr bgcolor='#fff'><td>配送单号:</td><td  colspan='5' style='font-size:14px;'><b>" + recheckNo
				+ "</b></td></tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:45px;'><td>店铺:</td><td  colspan='5' style='font-size:16px'><b>"
				+ storeName + "-" + storeNo + "<b></td></tr>");// <td>流道:</td><td>"+serialNo+"</td>
		headHtml.append("<tr bgcolor='#fff'><td>品项数:</td><td colspan='2' style='text-align:right;'>" + itemMap.size()
				+ "</td><td>商品数:</td><td colspan='2' style='text-align:right;'>" + qtyTotal.intValue() + "</td></tr>");
		headHtml.append("<tr bgcolor='#fff'><td style='width:24%;'>体积:</td><td style='width:14%;'>&nbsp;</td><td style='width:14%;'>重量:</td><td style='width:20%;'>&nbsp;</td><td style='width:20%;'>箱序号:</td><td style='width:14%;'>"
				+ list.get(0).getBoxRowId() + "</td></tr>");
		headHtml.append("<tr bgcolor='#fff'><td>打印人:</td><td colspan='5'><span style='width:55%;'>"
				+ user.getUsername() + "</span><span style='text-align:right;'>"
				+ (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())) + "</span></td></tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:70px;'><td  colspan='6'></td></tr>");
		headHtml.append("</table><div style='height:1px;font-size:1px;'>&nbsp;</div>");
		headHtml.append("<table  border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;font-size:12px;'>");
		headHtml.append("<tr bgcolor='#fff'><td style='width:24%;'>配送单号:</td><td colspan='2' style='width:48%;'>"
				+ recheckNo + "</td><td style='width:20%;'>箱序号:</td><td>" + list.get(0).getBoxRowId() + "</td></tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:38px;'><td>店铺:</td><td colspan='4'>" + storeName + "-"
				+ storeNo + "</td></tr>");
		headHtml.append("<tr bgcolor='#fff'><td colspan='5' style='height:72px;'>&nbsp;</td></tr>");
		headHtml.append("<tr bgcolor='#fff'><td>品项数:</td><td style='text-align:right;width:28%;'>" + itemMap.size()
				+ "</td><td>商品数:</td><td colspan='2' style='text-align:right;'>" + qtyTotal.intValue() + "</td></tr>");
		return headHtml;
	}

	/*
	 * private List<StringBuffer> getItemDtl(int pageSize,List<BillOmRecheckDtl>
	 * list){ List<StringBuffer> itemList = new ArrayList<StringBuffer>();
	 * BillOmRecheckDtl dtl; int rowNum = list.size(); StringBuffer sb = new
	 * StringBuffer(); sb.append(
	 * "<tr bgcolor='#fff'><td  colspan='3'>商品</td><td>尺码</td><td  colspan='2'>数量</td></tr>"
	 * ); for(int i=0;i<rowNum;i++){ dtl = list.get(i);
	 * sb.append("<tr bgcolor='#fff'><td  colspan='3'>"
	 * +dtl.getItemNo()+"</td><td>"
	 * +dtl.getSizeNo()+"</td><td  colspan='2'>"+dtl.
	 * getItemQty().intValue()+"</td></tr>"); if(rowNum>pageSize){
	 * if((i+1)%pageSize == 0){ itemList.add(sb); sb = new StringBuffer(); }
	 * if((i+1) == rowNum){ itemList.add(sb); } }else{ if((i+1) == rowNum){
	 * itemList.add(sb); } } } return itemList; }
	 */

	@Override
	public Map<String, Object> printDetailShowBox(String keys, SystemUser user) throws ManagerException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String locno = null;
			String recheckNo = null;
			String storeNo = null;
			String[] keysArray = keys.split("_");
			String[] main = null;
			List<Object> data = new ArrayList<Object>();
			result.put("data", data);
			Map<String, Object> map = null;
			Map<String, Object> params = new HashMap<String, Object>();
			List<BillOmRecheckDtl> dtls = null;
			Store store = new Store();
			for (String key : keysArray) {
				map = new HashMap<String, Object>();
				main = key.split("\\|");
				locno = main[0];
				recheckNo = main[1];
				storeNo = main[2];
				// 查询客户
				store.setStoreNo(storeNo);
				store = storeService.findById(store);
				map.put("receipterName", store.getStoreName());
				map.put("receipter", storeNo);
				map.put("sender", user.getLocName());
				map.put("creator", user.getUsername());
				map.put("creattm", dateStr);
				map.put("recheckNo", recheckNo);
				// 查询明细
				params.put("locno", locno);
				params.put("recheckNo", recheckNo);
				dtls = billOmRecheckDtlService.findBillOmRecheckDtlByShowBox(params);
				builderItemBySize(map, dtls);
				if (map.get("sizeCols") != null) {
					data.add(map);
				}
			}
			if (data.size() == 0) {
				result.put("result", "没有商品信息");
			} else {
				result.put("result", "success");
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return result;
	}

	public void builderItemBySize(Map<String, Object> map, List<BillOmRecheckDtl> dtls) {
		if (dtls == null || dtls.size() == 0) {
			return;
		} else {
			String key = null;
			Integer allCount = 0;
			Integer total = 0;
			Set<String> sizeCols = new TreeSet<String>();
			List<Object> rows = new ArrayList<Object>();
			Map<String, Object> rowMap;
			Map<String, Map<String, Object>> rowsMap = new TreeMap<String, Map<String, Object>>();
			for (BillOmRecheckDtl dtl : dtls) {
				sizeCols.add(dtl.getSizeCode());
				key = dtl.getBoxNo() + "_" + dtl.getItemNo();
				rowMap = rowsMap.get(key);
				if (rowMap == null) {
					rowMap = new HashMap<String, Object>();
					rowMap.put("boxNo", dtl.getBoxNo());
					rowMap.put("itemNo", dtl.getItemNo());
					rowMap.put("itemName", dtl.getItemName());
					rowMap.put("colorName", dtl.getColorName());
					rowMap.put("allCount", dtl.getRealQty().intValue());
					rowMap.put(dtl.getSizeCode(), dtl.getRealQty().intValue());
					rowsMap.put(key, rowMap);
				} else {
					allCount = ((Integer) rowMap.get("allCount")) + dtl.getRealQty().intValue();
					rowMap.put("allCount", allCount);
					rowMap.put(dtl.getSizeCode(), dtl.getRealQty().intValue());
				}
				total += dtl.getRealQty().intValue();
			}
			for (Entry<String, Map<String, Object>> r : rowsMap.entrySet()) {
				rows.add(r.getValue());
			}
			map.put("total", total);
			map.put("rows", rows);
			map.put("sizeCols", sizeCols);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckDtlService.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectRfSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckDtlService.selectRfSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNo(BillOmRecheck recheck) throws ManagerException {
		try {
			return billOmRecheckDtlService.selectDtlGroupByItemNo(recheck);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtlSizeKind> selectAllDtl4Print(BillOmRecheckDtlSizeKind dtl) throws ManagerException {
		try {
			return billOmRecheckDtlService.selectAllDtl4Print(dtl);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNoAndBox(BillOmRecheck recheck) throws ManagerException {
		try {
			return billOmRecheckDtlService.selectDtlGroupByItemNoAndBox(recheck);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtlSizeKind> selectAllDtlBox4Print(BillOmRecheckDtlSizeKind recheck)
			throws ManagerException {
		try {
			return billOmRecheckDtlService.selectAllDtlBox4Print(recheck);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	public List<String> selectAllDtlSizeKind(BillOmRecheck recheck) throws ManagerException {
		try {
			return billOmRecheckDtlService.selectAllDtlSizeKind(recheck);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void updateOmRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> updateList,SystemUser user) throws ManagerException {
		try {
			billOmRecheckDtlService.updateOmRecheckDtl(recheck, updateList,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void deleteOmRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> deleteList,SystemUser user) throws ManagerException {
		try {
			billOmRecheckDtlService.deleteOmRecheckDtl(recheck, deleteList,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void updateOmOutstockRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> updateList,List<BillOmRecheckDtl> insertList,SystemUser user)
			throws ManagerException {
		try {
			billOmRecheckDtlService.updateOmOutstockRecheckDtl(recheck, updateList, insertList,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void deleteOmOutstockRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> deleteList,SystemUser user)
			throws ManagerException {
		try {
			billOmRecheckDtlService.deleteOmOutstockRecheckDtl(recheck, deleteList,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
}