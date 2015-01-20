package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillWmOutstockDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
@Service("billWmOutstockDtlManager")
class BillWmOutstockDtlManagerImpl extends BaseCrudManagerImpl implements BillWmOutstockDtlManager {
    @Resource
    private BillWmOutstockDtlService billWmOutstockDtlService;

    @Override
    public BaseCrudService init() {
        return billWmOutstockDtlService;
    }

	@Override
	public List<BillWmOutstockDtlDto> findOutstockDtlItem(BillWmOutstockDtl billWmOutstockDtl, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billWmOutstockDtlService.findOutstockDtlItem(billWmOutstockDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<String> printDetail(String locno, String keys, SystemUser user)
			throws ManagerException {
		List<String> htmlList = new ArrayList<String>();
		try {
			String [] locateNoArray = keys.split("\\|");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			List<BillWmOutstockDtl> list = null;
			int total = 0;
			SimplePage page = null;
			if(locateNoArray != null && locateNoArray.length > 0){
				for(String no:locateNoArray){
					if(no.endsWith("_")){
						no += "&nbsp;";
					}
					String [] main = no.split("_");
					params.put("outstockNo", main[0]);
					total = billWmOutstockDtlService.findCount(params);
					page = new SimplePage(0, total, total);
					list = billWmOutstockDtlService.findByPage(page, null, null, params);
					if(list != null && list.size() > 0){
						htmlList.add(getHtml(locno, no, list,user));
					}
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(),e);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return htmlList;
	}
	public String getHtml(String locno,String no,List<BillWmOutstockDtl> list,SystemUser user){
		String [] main = no.split("_");
		int index = 1;
		String userName = "";
		if(user != null && !StringUtils.isBlank(user.getUsername())){
			userName = user.getUsername();
		}
		StringBuffer headHtml = new StringBuffer();
		StringBuffer tabHtml = new StringBuffer();
		StringBuffer lastHtml = new StringBuffer();
		BigDecimal totalRealQty = new BigDecimal(0);
		//list = groupBy(list);
		tabHtml.append("<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>");
		tabHtml.append("<tr bgcolor='#fff'>" +
				"<td>序号</td>" +
				"<td>退厂通知单号</td>" +
				"<td>储位</td>" +
				"<td>商品编码</td>" +
				"<td>商品名称</td>" +
				"<td>颜色</td>" +
				"<td>尺码</td>" +
				"<td>数量</td>" +
				"</tr>");
		for(BillWmOutstockDtl dtl:list){
			if(dtl.getRealQty() != null){
				totalRealQty = totalRealQty.add(dtl.getRealQty());
			}
			tabHtml.append("<tr bgcolor='#fff'>" +
					"<td>"+(index++)+"</td>" +
					"<td>"+dtl.getSourceNo()+"</td>" +
					"<td>"+dtl.getsCellNo()+"</td>" +
					"<td>"+dtl.getItemNo()+"</td>" +
					"<td>"+dtl.getItemName()+"</td>" +
					"<td>"+(dtl.getColorName()==null?"":dtl.getColorName())+"</td>" +
					"<td>"+dtl.getSizeNo()+"</td>" +
					"<td>"+(dtl.getRealQty()==null?0:dtl.getRealQty().intValue())+"</td>" +
					"</tr>");
		}
		tabHtml.append("</table>");
		headHtml.append("<table style='width:100%;'>");
		headHtml.append("<tr><td colspan='3' style='text-align:center;font-size:30px;'>退厂拣货</td></tr>");
		headHtml.append("<tr><td style='width:50%;'>退厂拣货单号："+main[0]+"</td>");
		headHtml.append("<td style='width:40%;'>拣货人："+main[1]+"</td>");
		headHtml.append("<td>合计："+totalRealQty.intValue()+"</td></tr>");
		headHtml.append("</table>");
		lastHtml.append("</table>");
		lastHtml.append("<table style='width:100%;'>");
		lastHtml.append("<td style='text-align:right;'>制单人："+userName+
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"&nbsp;&nbsp;&nbsp;</td></tr>");
		lastHtml.append("</table>");
		tabHtml.append(lastHtml.toString());
		return headHtml.append(tabHtml.toString()).toString();
	}
	
	/*public List<BillWmOutstockDtl> groupBy(List<BillWmOutstockDtl> list){
		List<BillWmOutstockDtl> newList = new ArrayList<BillWmOutstockDtl>();
		Map<String, BillWmOutstockDtl> map = new HashMap<String, BillWmOutstockDtl>();
		BillOmLocateDtl temp = new BillOmLocateDtl();
		String key = "";
		for(BillWmOutstockDtl dtl:list){
			key = dtl.getStoreNo()+dtl.getItemNo()+dtl.getSizeNo();
			temp = map.get(key);
			if(temp != null){
				temp.setPlanQty(dtl.getPlanQty()==null?new BigDecimal(0):dtl.getPlanQty());
			}else{
				map.put(key, dtl);
			}
		}
		for(Entry<String, BillWmOutstockDtl> m:map.entrySet()){
			newList.add(m.getValue());
		}
		return newList;
	}*/
	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billWmOutstockDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillWmOutstockDtlDto> findOutstockDtlItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billWmOutstockDtlService.findOutstockDtlItemByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findOutstockDtlItemCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmOutstockDtlService.findOutstockDtlItemCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectOutstockDtlItemSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billWmOutstockDtlService.selectOutstockDtlItemSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}