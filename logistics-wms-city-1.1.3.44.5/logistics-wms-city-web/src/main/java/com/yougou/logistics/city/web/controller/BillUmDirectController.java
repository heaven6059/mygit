package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmDirectManager;

/**
 * 退仓上架预约
 * 
 * @author zuo.sw
 * @date 2014-01-15 14:36:28
 * @version 1.0.0
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
@RequestMapping("/bill_um_direct")
@ModuleVerify("25060060")
public class BillUmDirectController extends BaseCrudController<BillUmDirect> {

    /**
     * 未定位明细
     */
    private static final String BILLUMDIRECT_ITEMNO_N = "N";

    @Log
    private Logger log;

    @Resource
    private BillUmDirectManager billUmDirectManager;

    @Override
    public CrudInfo init() {
	return new CrudInfo("billumdirect/", billUmDirectManager);
    }

    @RequestMapping(value = "/selectImport4Direct")
    @ResponseBody
    public Map<String, Object> selectUm4Direct(HttpServletRequest req,
	    Model model) throws ManagerException {
    	Map obj = new HashMap();
    	try{
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
    				: Integer.parseInt(req.getParameter("page"));
    			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
    				: Integer.parseInt(req.getParameter("rows"));
    			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
    				: String.valueOf(req.getParameter("sort"));
    			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
    				: String.valueOf(req.getParameter("order"));
    			Map params = builderParams(req, model);

    			int total = billUmDirectManager.selectCount4Direct(params);
    			SimplePage page = new SimplePage(pageNo, pageSize, total);
    			List<BillUmDirect> list = billUmDirectManager.selectByPage4Direct(page,
    				params);
    			obj.put("total", Integer.valueOf(total));
    			obj.put("rows", list);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
    	return obj;
    }

    /**
     * 上架预约
     * 
     * @param keyStr
     * @return
     */
    @RequestMapping(value = "/instockDirect")
    @OperationVerify(OperationVerifyEnum.ADD)
    @ResponseBody
    public Map<String, Object> instockDirect(String keyStr,
	    HttpServletRequest req) {
	Map<String, Object> result = new HashMap<String, Object>();
	try {

	    // 获取登陆用户
	    HttpSession session = req.getSession();
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    billUmDirectManager.instockDirect(keyStr, user.getLoginName());

	    result.put("result", "success");
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    result.put("result", "error");
	    result.put("msg", e.getMessage());
	}
	return result;
    }

    /**
     * 取消定位-按明细
     * 
     * @param locNo
     * @param ownerNo
     * @param sourceNo
     * @param flag
     * @param req
     * @return
     */
    @RequestMapping(value = "/cancelDirectForAll")
    @OperationVerify(OperationVerifyEnum.REMOVE)
    @ResponseBody
    public Map<String, Object> cancelDirectForAll(String locno, String ownerNo,
	    String untreadMmNo, String keyStr, HttpServletRequest req) {
	Map<String, Object> result = new HashMap<String, Object>();

	try {

	    // 获取登陆用户
	    HttpSession session = req.getSession();
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    billUmDirectManager.cancelDirectForAll(locno, ownerNo, untreadMmNo,
		    keyStr, user.getLoginName());
	    result.put("result", "success");
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    result.put("result", "error");
	    result.put("msg", e.getMessage());
	}
	return result;
    }

    /**
     * 退仓-匹配差异，继续定位
     * 
     * @param locno
     * @param ownerNo
     * @param untreadMmNo
     * @param strCheckNoList
     * @param req
     * @return
     */
    @RequestMapping(value = "/continueDirect")
    @OperationVerify(OperationVerifyEnum.ADD)
    @ResponseBody
    public Map<String, Object> continueDirect(String locno, String ownerNo,
	    String untreadMmNo, String strCheckNoList, HttpServletRequest req) {
	Map<String, Object> result = new HashMap<String, Object>();
	try {

	    // 获取登陆用户
	    HttpSession session = req.getSession();
	    SystemUser user = (SystemUser) session
		    .getAttribute(PublicContains.SESSION_USER);
	    billUmDirectManager.continueDirect(locno, ownerNo, untreadMmNo,
		    strCheckNoList, user.getLoginName());
	    result.put("result", "success");
	} catch (ManagerException e) {
	    log.error(e.getMessage(), e);
	    result.put("result", "error");
	    result.put("msg", e.getMessage());
	}
	return result;
    }

    @RequestMapping(value = "/dtl_list.json")
    @ResponseBody
    public Map<String, Object> queryDtlList(HttpServletRequest req, Model model)
	    throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1
    				: Integer.parseInt(req.getParameter("page"));
    			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10
    				: Integer.parseInt(req.getParameter("rows"));
    			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? ""
    				: String.valueOf(req.getParameter("sort"));
    			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? ""
    				: String.valueOf(req.getParameter("order"));
    			Map<String, Object> params = builderParams(req, model);
    			int total = this.billUmDirectManager.findCount(params);
    			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    			List<BillUmDirect> list = this.billUmDirectManager.findByPage(page,
    				sortColumn, sortOrder, params);
    			String cellNo = req.getParameter("cellNo");
    			List<Object> footerList = new ArrayList<Object>();
    			Map<String, Object> footer = new HashMap<String, Object>();
    			BigDecimal totalEstQty = new BigDecimal(0);
    			for (BillUmDirect dtl : list) {
    			    totalEstQty = totalEstQty
    				    .add(dtl.getEstQty() == null ? new BigDecimal(0) : dtl
    					    .getEstQty());
    			}
    			// 未定位明细
    			if (BILLUMDIRECT_ITEMNO_N.equals(cellNo)) {
    			    footer.put("itemNo", "小计");
    			} else {// 已定位信息
    			    footer.put("cellNo", "小计");
    			}

    			footer.put("estQty", totalEstQty);
    			footerList.add(footer);

    			Map<String, Object> sumFoot = new HashMap<String, Object>();
    			if (pageNo == 1) {
    			    sumFoot = billUmDirectManager.selectSumQty(params);
    			    if (sumFoot == null) {
    				sumFoot = new SumUtilMap<String, Object>();
    				sumFoot.put("est_Qty", 0);
    			    }
    			    sumFoot.put("isselectsum", true);
    			    if (BILLUMDIRECT_ITEMNO_N.equals(cellNo)) {
    				sumFoot.put("item_No", "合计");
    			    } else {// 已定位信息
    				sumFoot.put("cell_No", "合计");
    			    }
    			} else {
    			    if (BILLUMDIRECT_ITEMNO_N.equals(cellNo)) {
    				sumFoot.put("itemNo", "合计");
    			    } else {// 已定位信息
    				sumFoot.put("cellNo", "合计");
    			    }
    			}
    			footerList.add(sumFoot);
    			
    			obj.put("total", total);
    			obj.put("rows", list);
    			obj.put("footer", footerList);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
    	return obj;
    }

}