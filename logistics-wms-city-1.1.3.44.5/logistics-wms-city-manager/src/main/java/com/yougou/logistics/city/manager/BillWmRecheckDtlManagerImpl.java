package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.BillWmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillWmRecheckDtlService;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
@Service("billWmRecheckDtlManager")
class BillWmRecheckDtlManagerImpl extends BaseCrudManagerImpl implements BillWmRecheckDtlManager {
    @Resource
    private BillWmRecheckDtlService billWmRecheckDtlService;

    @Override
    public BaseCrudService init() {
        return billWmRecheckDtlService;
    }

	@Override
	public Map<String, Object> printByBox(String locno,
			BillWmRecheck billWmRecheck, String boxKeys,boolean noneDtl)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locno", locno);
		params.put("recheckNo", billWmRecheck.getRecheckNo());
		List<StringBuilder> htmlList = new ArrayList<StringBuilder>();
		try {
			int rowTotal = billWmRecheckDtlService.findCount(params, null);
			SimplePage page = new SimplePage(0, rowTotal, rowTotal);
			List<BillWmRecheckDtl> allDtllist = billWmRecheckDtlService.findByPage(page, null, null, params, null);
			Map<String, Integer> boxRowIdMap = buildBoxRowId(allDtllist);
			String[] boxNos = boxKeys.split("\\|");
	        List<String> list = new ArrayList<String>();  
	        for (String box:boxNos) {  
	            if(!list.contains(box)) {  
	                list.add(box);  
	    			params.put("scanLabelNo", box);
	    			List<BillWmRecheckDtl> dtllist = null;
	    			rowTotal = billWmRecheckDtlService.findCount(params, null);
	    			page = new SimplePage(0, rowTotal, rowTotal);
	    			dtllist = billWmRecheckDtlService.findByPage(page, null, null, params, null);
	    			if(dtllist != null && dtllist.size() > 0){
	    				htmlList.add(getHtml(billWmRecheck.getRecedeNo(), billWmRecheck.getSupplierName(), dtllist,boxRowIdMap.get(box),noneDtl));
	    			}
	            }  
	        }
	        if(htmlList.size() > 0){
	        	obj.put("box", list);
	        	obj.put("html", htmlList);
	        }
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return obj;
	}
	public StringBuilder getHtml(String recedeNo,String supplierName,List<BillWmRecheckDtl> list,Integer bowRowId,boolean noneDtl){
		StringBuilder sb = new StringBuilder();
		StringBuilder tab = new StringBuilder();
		BigDecimal total = new BigDecimal(0);
		if(!noneDtl){
			tab.append("<tr bgcolor='#fff'><td colspan='3' style='text-align:center;'>商品编码</td><td style='text-align:center;'>尺码</td><td colspan='2' style='text-align:center;'>数量</td></tr>");
			for(BillWmRecheckDtl dtl:list){
				total = total.add(dtl.getItemQty()!= null?dtl.getItemQty():new BigDecimal(0));
				tab.append("<tr bgcolor='#fff'><td colspan='3' style='text-align:center;'>"+dtl.getItemNo()+"</td><td style='text-align:center;'>"+dtl.getSizeNo()+"</td><td colspan='2' style='text-align:center;'>"+dtl.getItemQty()+"</td></tr>");
			}
		}else{
			for(BillWmRecheckDtl dtl:list){
				total = total.add(dtl.getItemQty()!= null?dtl.getItemQty():new BigDecimal(0));
			}
		}
		sb.append("<table  border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>");
		sb.append("<tr bgcolor='#fff'><td>退厂通知：</td><td colspan='5'>"+recedeNo+"</td></tr>");
		sb.append("<tr bgcolor='#fff' height='55px;'><td>供应商：</td><td colspan='3'><strong>"+supplierName+"</strong></td><td>箱序号:</td><td>"+bowRowId+"</td></tr>");
		sb.append("<tr bgcolor='#fff' height='55px;'><td>箱号：</td><td colspan='5'>&nbsp;</td></tr>");
		sb.append("<tr bgcolor='#fff'><td width='27%'>总数：</td><td width='12%'>"+total+"</td>");
		sb.append("<td width='18%'>体积：</td><td width='15%'>&nbsp;</td>");
		sb.append("<td width='20%'>重量：</td><td>&nbsp;</td></tr>");
		sb.append(tab);
		sb.append("</table>");
		return sb;
	}
	public Map<String, Integer> buildBoxRowId(List<BillWmRecheckDtl> allDtllist){
		Map<String, String> map = new TreeMap<String, String>();
		for(BillWmRecheckDtl dtl:allDtllist){
			map.put(dtl.getContainerNo(), dtl.getScanLabelNo());
		}
		Map<String, Integer> resullt = new HashMap<String, Integer>();
		int i = 1;
		for(Entry<String, String> m:map.entrySet()){
			resullt.put(m.getValue(), (i++));
		}
		return resullt;
	}

	@Override
	public int findWmRecheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billWmRecheckDtlService.findWmRecheckDtlCount(params, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillWmRecheckDtl> findWmRecheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecheckDtlService.findWmRecheckDtlByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findWmRecheckDtlGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billWmRecheckDtlService.findWmRecheckDtlGroupByCount(params, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillWmRecheckDtl> findWmRecheckDtlGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecheckDtlService.findWmRecheckDtlGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectWmRecheckDtlSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecheckDtlService.selectWmRecheckDtlSumQty(params, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}
	
}