package com.yougou.logistics.city.manager;

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
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmOutstockDtlService;
import com.yougou.logistics.city.service.BillOmOutstockService;
import com.yougou.logistics.city.service.CmDefcellService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:13 CST 2013
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
@Service("billOmOutstockDtlManager")
class BillOmOutstockDtlManagerImpl extends BaseCrudManagerImpl implements
	BillOmOutstockDtlManager {
    @Resource
    private BillOmOutstockDtlService billOmOutstockDtlService;
    @Resource
    private BillOmOutstockService billOmOutstockService;
    @Resource
    private CmDefcellService cmDefcellService;

    @Override
    public BaseCrudService init() {
    	return billOmOutstockDtlService;
    }
    
    @Override
	public int findRecheckOutstockItemCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockDtlService.findRecheckOutstockItemCount(params, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtlDto> findRecheckOutstockItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDtlService.findRecheckOutstockItemByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}
	

	@Override
	public SumUtilMap<String, Object> selectRecheckOutstockItemSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDtlService.selectRecheckOutstockItemSumQty(map, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

	
	@Override
	public int findConContentGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockDtlService.findConContentGroupByCount(params, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtlDto> findConContentGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDtlService.findConContentGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}
	

	@Override
	public int findMoveStockGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockDtlService.findMoveStockGroupByCount(params, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtlDto> findMoveStockGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDtlService.findMoveStockGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

	
	@Override
	public SumUtilMap<String, Object> selectMoveStockSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmOutstockDtlService.selectMoveStockSumQty(map, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}
	
	@Override
	public void procImmediateMoveStock(List<BillOmOutstockDtlDto> lists,SystemUser user) throws ManagerException {
		try{
			billOmOutstockDtlService.procImmediateMoveStock(lists, user);
		} catch (Exception e) {
		    throw new ManagerException(e.getMessage());
		}
	}

	@Override
    public void editDetail(List<BillOmOutstockDtl> oList,BillOmOutstock billOmOutstock, SystemUser user)
	    throws ManagerException {
	try {
	    if (CommonUtil.hasValue(oList)) {
		billOmOutstockDtlService.editDetail(oList,billOmOutstock, user);
	    } else {
		throw new ManagerException("参数非法！");
	    }
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    public void omPlanOutStockAudit(String outstockNo, SystemUser user) throws ManagerException {
	try {
	    if (StringUtils.isNotBlank(outstockNo)) {
		billOmOutstockDtlService.omPlanOutStockAudit(outstockNo, user);
	    } else {
		throw new ManagerException("参数非法！");
	    }
	} catch (Exception e) {
	    throw new ManagerException(e.getMessage());
	}
    }

    @Override
    public List<BillOmOutstockDtl> findStoreNo(BillOmOutstockDtl dtl)
	    throws ManagerException {
	try {
	    return billOmOutstockDtlService.findStoreNo(dtl);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmOutstockDtl> findStockDtl(BillOmOutstockDtl dtl)
	    throws ManagerException {
	try {
	    return billOmOutstockDtlService.findStockDtl(dtl);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmOutstockDtl> findStockDtlNoStoreNo(BillOmOutstockDtl dtl)
	    throws ManagerException {
	try {
	    return billOmOutstockDtlService.findStockDtlNoStoreNo(dtl);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmOutstockDtlDto> findRecheckOutstockItem(
	    Map<String, Object> params) throws ManagerException {
	try {
	    return billOmOutstockDtlService.findRecheckOutstockItem(params);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public int findBillOmOutstockCount(Map<String, Object> params,
	    AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billOmOutstockDtlService.findBillOmOutstockCount(params,
		    authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmOutstockDtl> findBillOmOutstockByPage(SimplePage page,
	    String orderByField, String orderBy, Map<String, Object> params,
	    AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billOmOutstockDtlService.findBillOmOutstockByPage(page,
		    orderByField, orderBy, params, authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<Store> findStoreByParam(Map<String, Object> params, AuthorityParams authorityParams)
	    throws ManagerException {
	try {
	    return billOmOutstockDtlService.findStoreByParam(params, authorityParams);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public Map<String, Object> printByArea(String locno, String keys,
	    SystemUser user) throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	Map<String, Object> params;
	List<StringBuilder> resultHtml = new ArrayList<StringBuilder>();
	obj.put("html", resultHtml);
	SimplePage page = new SimplePage(0, 1, 1);
	List<BillOmOutstock> mainList;
	List<BillOmOutstockDtl> dtlList;
	List<CmDefcell> cellList;
	BillOmOutstock main = null;
	BillOmOutstockDtl dtl = null;
	try {
	    String[] outStockNos = keys.split(",");
	    for (String outStockNo : outStockNos) {
		params = new HashMap<String, Object>();
		params.put("locno", locno);
		params.put("outstockNo", outStockNo);
		page = new SimplePage(0, 1, 1);
		mainList = billOmOutstockService.findByPage(page, null, null,
			params);
		dtlList = billOmOutstockDtlService.findByPage(page, null, null,
			params);
		if (mainList != null && mainList.size() == 1 && dtlList != null
			&& dtlList.size() > 0) {
		    main = mainList.get(0);
		    dtl = dtlList.get(0);
		    params.put("cellNo", dtl.getsCellNo());
		    cellList = cmDefcellService.findByPage(page, null, null,
			    params);
		    if (cellList != null && cellList.size() > 0) {
			resultHtml.add(getHtmlByArea(main, cellList.get(0)
				.getAreaName()));
		    }
		}
	    }
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
	obj.put("result", "success");
	return obj;
    }

    private StringBuilder getHtmlByArea(BillOmOutstock main, String areaName) {
	StringBuilder html = new StringBuilder();
	html.append("<table  border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;font-size:12px;'>");
	html.append("<tr bgcolor='#fff'><td width='20%'>拣货类型:</td><td width='23%'>库区拣货</td><td width='20%'>创建时间:</td><td>"
		+ (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()))
		+ "</td></tr>");
	html.append("<tr bgcolor='#fff'><td>库区:</td><td colspan='3'>"
		+ areaName + "</td></tr>");
	html.append("<tr bgcolor='#fff'><td>拣货单号:</td><td colspan='3' style='text-align:center;'>"
		+ main.getOutstockNo() + "</td></tr>");
	html.append("<tr bgcolor='#fff' height='93px'><td colspan='4'>&nbsp;</td></tr>");
	html.append("</table>");
	return html;
    }

    @Override
    public Map<String, Object> printByStore(String locno, String keys,
	    SystemUser user) throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>();
	Map<String, Object> params;
	List<StringBuilder> resultHtml = new ArrayList<StringBuilder>();
	obj.put("html", resultHtml);
	SimplePage page = new SimplePage(0, 1, 1);
	List<BillOmOutstock> mainList;
	List<BillOmOutstockDtl> dtlList;
	List<CmDefcell> cellList;
	BillOmOutstock main = null;
	BillOmOutstockDtl dtl = null;
	try {
	    String[] outStockNos = keys.split(",");
	    for (String outStockNo : outStockNos) {
		params = new HashMap<String, Object>();
		params.put("locno", locno);
		params.put("outstockNo", outStockNo);
		page = new SimplePage(0, 1, 1);
		mainList = billOmOutstockService.findByPage(page, null, null,
			params);
		dtlList = billOmOutstockDtlService.findByPage(page, null, null,
			params);
		if (mainList != null && mainList.size() == 1 && dtlList != null
			&& dtlList.size() > 0) {
		    main = mainList.get(0);
		    dtl = dtlList.get(0);
		    resultHtml.add(getHtmlByStore(main, dtl.getStoreName()));
		    /*
		     * params.put("outStockNo", dtl.getsCellNo()); cellList =
		     * cmDefcellService.findByPage(page, null, null, params);
		     * if(cellList != null && cellList.size() > 0){
		     * resultHtml.add
		     * (getHtmlByStore(main,cellList.get(0).getAreaName())); }
		     */
		}
	    }
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
	obj.put("result", "success");
	return obj;
    }

    private StringBuilder getHtmlByStore(BillOmOutstock main, String storeName) {
	StringBuilder html = new StringBuilder();
	html.append("<table  border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;font-size:12px;'>");
	html.append("<tr bgcolor='#fff'><td width='20%'>拣货类型:</td><td width='23%'>客户拣货</td><td width='20%'>创建时间:</td><td>"
		+ (new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()))
		+ "</td></tr>");
	html.append("<tr bgcolor='#fff'><td>客户:</td><td colspan='3'>"
		+ storeName + "</td></tr>");
	html.append("<tr bgcolor='#fff'><td>拣货单号:</td><td colspan='3' style='text-align:center;'>"
		+ main.getOutstockNo() + "</td></tr>");
	html.append("<tr bgcolor='#fff' height='93px'><td colspan='4'>&nbsp;</td></tr>");
	html.append("</table>");
	return html;
    }

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
	return this.billOmOutstockDtlService.selectSumQty(map, authorityParams);
    }

    public List<Map<String, Object>> getPrintInf4AreaCut(String locno,
	    String keystr, String curOper) throws ManagerException {
	try {
	    return this.billOmOutstockDtlService.getPrintInf4AreaCut(locno,
		    keystr, curOper);
	} catch (ServiceException e) {
	    throw new ManagerException(e.getMessage(), e);
	}
    }

	@Override
	public int saveByPlan(BillOmOutstock instock,SystemUser user) throws ManagerException {
		try{
			return billOmOutstockDtlService.saveByPlan(instock,user);
		} catch (ServiceException e) {
		    throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int selectCheckDtlRealQty(BillOmOutstock instock) throws ManagerException {
		try{
			return billOmOutstockDtlService.selectCheckDtlRealQty(instock);
		} catch (ServiceException e) {
		    throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int selectCheckDtlRealQtyEq(BillOmOutstock instock)
			throws ManagerException {
		try{
			return billOmOutstockDtlService.selectCheckDtlRealQtyEq(instock);
		} catch (ServiceException e) {
		    throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<String> selectSysNo(Map<String, String> map) throws ManagerException {
		try {
			return billOmOutstockDtlService.selectSysNo(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmOutstockDtlService.selectItemSizeKind(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
    
	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmOutstockDtlService.selectBoxQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillOmOutstockDtl> selectDetailBySizeNo(Map<String, Object> map) throws ManagerException {
		try {
			return billOmOutstockDtlService.selectDetailBySizeNo(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmOutstockDtlService.selectItemDetailByGroupCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ManagerException {
		try {
			return billOmOutstockDtlService.selectItemDetailByGroup(map, authorityParams, page);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
//	@Override
//	public void toStoreLock(String locno, String outstockNo, String creator) throws ManagerException {
//		try {
//			billOmOutstockDtlService.toStoreLock(locno, outstockNo, creator);
//		} catch (ServiceException e) {
//		    throw new ManagerException(e.getMessage());
//		}
//	}
    
}