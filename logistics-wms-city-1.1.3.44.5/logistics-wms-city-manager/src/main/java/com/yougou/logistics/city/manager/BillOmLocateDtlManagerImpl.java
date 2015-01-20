package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.BillOmLocateDtlSizeKind;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmLocateDtlService;

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
@Service("billOmLocateDtlManager")
class BillOmLocateDtlManagerImpl extends BaseCrudManagerImpl implements BillOmLocateDtlManager {

	@Resource
	private BillOmLocateDtlService billOmLocateDtlService;

	@Override
	public BaseCrudService init() {
		return billOmLocateDtlService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmLocateDtlService.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<String> printDetail(String locno, String keys, SystemUser user) throws ManagerException {
		List<String> htmlList = new ArrayList<String>();
		try {
			String[] locateNoArray = keys.split(",");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			List<BillOmLocateDtl> list = null;
			int total = 0;
			SimplePage page = null;
			if (locateNoArray != null && locateNoArray.length > 0) {
				for (String locateNo : locateNoArray) {
					params.put("locateNo", locateNo);
					total = billOmLocateDtlService.findCount(params);
					page = new SimplePage(0, total, total);
					list = billOmLocateDtlService.findByPage(page, null, null, params);
					if (list != null && list.size() > 0) {
						htmlList.add(getHtml(locno, locateNo, list, user));
					}
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return htmlList;
	}

	public String getHtml(String locno, String locateNo, List<BillOmLocateDtl> list, SystemUser user) {
		String userName = "";
		if (user != null && !StringUtils.isBlank(user.getUsername())) {
			userName = user.getUsername();
		}
		StringBuffer headHtml = new StringBuffer();
		StringBuffer tabHtml = new StringBuffer();
		StringBuffer lastHtml = new StringBuffer();
		BigDecimal totalPlanQty = new BigDecimal(0);
		list = groupBy(list);
		tabHtml.append("<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>");
		tabHtml.append("<tr bgcolor='#fff'>" + "<td>客户编号</td>" + "<td>客户名称</td>" + "<td>商品编码</td>" + "<td>商品名称</td>"
				+ "<td>颜色</td>" + "<td>尺码</td>" + "<td>数量</td>" + "</tr>");
		for (BillOmLocateDtl dtl : list) {
			if (dtl.getPlanQty() != null) {
				totalPlanQty = totalPlanQty.add(dtl.getPlanQty());
			}
			tabHtml.append("<tr bgcolor='#fff'>" + "<td>" + dtl.getStoreNo() + "</td>" + "<td>" + dtl.getStoreName()
					+ "</td>" + "<td>" + dtl.getItemNo() + "</td>" + "<td>" + dtl.getItemName() + "</td>" + "<td>"
					+ (dtl.getColorName() == null ? "" : dtl.getColorName()) + "</td>" + "<td>" + dtl.getSizeNo()
					+ "</td>" + "<td>" + (dtl.getPlanQty() == null ? 0 : dtl.getPlanQty().intValue()) + "</td>"
					+ "</tr>");
		}
		tabHtml.append("</table>");
		headHtml.append("<table style='width:100%;'>");
		headHtml.append("<tr><td colspan='2' style='text-align:center;font-size:30px;'>拣货调度波次</td></tr>");
		headHtml.append("<tr><td style='width:90%;'>拣货波次单号：" + locateNo + "</td>");
		headHtml.append("<td>合计：" + totalPlanQty.intValue() + "</td></tr>");
		headHtml.append("</table>");
		lastHtml.append("</table>");
		lastHtml.append("<table style='width:100%;'>");
		lastHtml.append("<td style='text-align:right;'>制单人：" + userName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制单时间："
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "&nbsp;&nbsp;&nbsp;</td></tr>");
		lastHtml.append("</table>");
		tabHtml.append(lastHtml.toString());
		return headHtml.append(tabHtml.toString()).toString();
	}

	public List<BillOmLocateDtl> groupBy(List<BillOmLocateDtl> list) {
		List<BillOmLocateDtl> newList = new ArrayList<BillOmLocateDtl>();
		Map<String, BillOmLocateDtl> map = new TreeMap<String, BillOmLocateDtl>();
		BillOmLocateDtl temp = new BillOmLocateDtl();
		String key = "";
		for (BillOmLocateDtl dtl : list) {
			key = dtl.getStoreNo() + dtl.getItemNo() + dtl.getSizeNo();
			temp = map.get(key);
			if (temp != null) {
				temp.setPlanQty(dtl.getPlanQty() == null ? new BigDecimal(0) : dtl.getPlanQty());
			} else {
				map.put(key, dtl);
			}
		}
		for (Entry<String, BillOmLocateDtl> m : map.entrySet()) {
			newList.add(m.getValue());
		}
		return newList;
	}

	@Override
	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNoItemNoExpNo(Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmLocateDtlService.selectDtlByStoreNoItemNoExpNo(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillOmLocateDtlSizeKind> selectAllDtl4Print(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmLocateDtlService.selectAllDtl4Print(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	public List<String> selectAllDtlSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmLocateDtlService.selectAllDtlSizeKind(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	public List<BillOmLocateDtlSizeKind> selectDtlByStoreNo(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmLocateDtlService.selectDtlByStoreNo(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

}