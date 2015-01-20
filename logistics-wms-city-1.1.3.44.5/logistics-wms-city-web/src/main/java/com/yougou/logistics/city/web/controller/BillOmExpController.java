package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmExpKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmExpManager;
import com.yougou.logistics.city.manager.BillStatusLogManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 出库订单
 * @author zuo.sw
 * @date  2013-09-29 16:50:42
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
@RequestMapping("/billomexp")
@ModuleVerify("25040070")
public class BillOmExpController extends BaseCrudController<BillOmExp> {
	
    @Log
    private Logger log;
	
    @Resource
    private BillOmExpManager billOmExpManager;
    
    @Resource
    private BillStatusLogManager billStatusLogManager;
    
    private static final String OVER_STATUS = "91";
    
    private static final String STATUS10 = "10";
    private static final String STATUS90 = "90";

    @Override
    public CrudInfo init() {
        return new CrudInfo("billomexp/",billOmExpManager);
    }
    
    @RequestMapping(value = "/expList.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			
			String poNo = "";
			String brandNo = "";
			String sysNo = "";
			if(paramsAll.get("poNo") != null) {
				poNo = paramsAll.get("poNo").toString();
			}
			if(paramsAll.get("brandNo") != null) {
				brandNo = paramsAll.get("brandNo").toString();
			}
			if(paramsAll.get("sysNo") != null) {
				sysNo = paramsAll.get("sysNo").toString();
			}
			if(!poNo.equals("") || !brandNo.equals("") || !sysNo.equals("")) {
				params.put("joinIn", "true");
			}
			int total = billOmExpManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmExp> list = billOmExpManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			 // 返回小计列表
		    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		    Map<String, Object> footerMap = new HashMap<String, Object>();
		    footerMap.put("expNo", "小计");
		    footerList.add(footerMap);
		    for (BillOmExp temp : list) {
		    	FooterUtil.setFooterMapByInt("itemQty", temp.getItemQty(), footerMap);
		    	FooterUtil.setFooterMapByInt("realQty", temp.getRealQty(), footerMap);
		    }
		    // 返回合计列表
		    Map<String, Object> sumFootMap = new HashMap<String, Object>();
		    if (pageNo == 1) {
				if(pageSize >= total){
					sumFootMap.putAll(footerMap);
				}else{
					Map<String, Object> sumFoot = billOmExpManager.findSumQty(params,authorityParams);
					if(sumFoot.get("ITEM_QTY")!= null) {
						sumFootMap.put("itemQty", sumFoot.get("ITEM_QTY"));
					} else {
						 sumFootMap.put("itemQty", 0);
					}
					if(sumFoot.get("REAL_QTY")!= null) {
						sumFootMap.put("realQty", sumFoot.get("REAL_QTY"));
					} else {
						sumFootMap.put("realQty", 0);
					}
					sumFootMap.put("isselectsum", true);
				}
				sumFootMap.put("expNo", "合计");
			    footerList.add(sumFootMap); 
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    /**
     * 出库调度
     * @return
     */
    @RequestMapping(value = "/toBillOmExpDispatch")
	@InitpowerInterceptors
	public String toBillOmExpDispatch() {
		return "billOmExpDispatch/list";
	}
    
    /**
     * 删除出库订单
     * @param locnoStrs
     * @return
     */
	@RequestMapping(value="/deleteBillOmExp")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public Map<String,Object> deleteBillOmExp(String noStrs) throws ManagerException{
		String m="success";
		String msg = "";
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			boolean isSuccess= billOmExpManager.deleteBillOmExp(noStrs);
			if(isSuccess){
				msg = "删除成功!";
			}else{
				m="fail";
				msg = "删除失败!";
			}
		}catch (Exception e) {
			log.error("=======删除出库订单异常："+e.getMessage(),e);
			m="fail";
			msg = e.getMessage();
        }
		map.put("result", m);
		map.put("msg", msg);
	    return map;
	}
	
	/**
	 * 新增出库订单
	 * @param billImImport
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/addBillOmExp")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String,Object>> addBillOmExp(BillOmExp billOmExp,HttpServletRequest req)throws ManagerException {
		Map<String,Object> obj=new HashMap<String,Object>();
		try {
			//获取登陆用户和仓别
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		    //修改人和新建人
		    billOmExp.setCreator(user.getLoginName());
		    billOmExp.setCreatorname(user.getUsername());
		    billOmExp.setEditor(user.getLoginName());
		    billOmExp.setEditorname(user.getUsername());
		    billOmExp.setLocno(user.getLocNo());
		    obj = billOmExpManager.addBillOmExp(billOmExp);
		}catch (Exception e) {
			log.error("=======新增出库订单异常："+e.getMessage(),e);
			obj.put("returnMsg", false);
            //throw new ManagerException(e);
        }
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
    
	/**
	 * 修改出库订单
	 * @param billOmExp
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyBillOmExp")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Map<String, Object> modifyBillOmExp(BillOmExp billOmExp, HttpServletRequest req)throws ManagerException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			
			//验证状态是否是建单
			BillOmExpKey expKey = new BillOmExpKey();
			expKey.setLocno(user.getLocNo());
			expKey.setExpType(billOmExp.getExpType());
			expKey.setExpNo(billOmExp.getExpNo());
			BillOmExp expCheck = (BillOmExp)billOmExpManager.findById(expKey);
			if(expCheck == null||!STATUS10.equals(expCheck.getStatus())){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "单据"+billOmExp.getExpNo()+"已删除或状态已改变");
				return map;
			}
//			if(!STATUS10.equals(expCheck.getStatus())){
//				return "fail";
//			}
			
			String status = billOmExp.getStatus();
			billOmExp.setEdittm(new Date());
			billOmExp.setEditor(user.getLoginName());
			billOmExp.setEditorname(user.getUsername());
			billOmExp.setLocno(user.getLocNo());
			if (billOmExpManager.modifyById(billOmExp) > 0) {
				if(StringUtils.isNotBlank(status)&&OVER_STATUS.equals(status)){
					billStatusLogManager.procInsertBillStatusLog(user.getLocNo(), 
					billOmExp.getExpNo(), "OM", OVER_STATUS, 
					"更新发货通知单状态为"+OVER_STATUS, user.getLoginName());
				}
				map.put("result", ResultEnums.SUCCESS.getResultMsg());
				map.put("msg", "修改成功!");
				return map;
			} else {
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "单据"+billOmExp.getExpNo()+"修改失败!");
				return map;
			}
		} catch (Exception e) {
			log.error("===========修改出库订单时异常："+e.getMessage(),e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "单据"+billOmExp.getExpNo()+"修改失败,出现异常!");
			return map;
		}
	}
	/**
	 * 关单
	 * @param billOmExp
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/closeBillOmExp")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public Map<String, Object> closeBillOmExp(BillOmExp billOmExp, HttpServletRequest req)throws ManagerException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			
			//验证状态是否是建单
			BillOmExpKey expKey = new BillOmExpKey();
			expKey.setLocno(user.getLocNo());
			expKey.setExpType(billOmExp.getExpType());
			expKey.setExpNo(billOmExp.getExpNo());
			BillOmExp expCheck = (BillOmExp)billOmExpManager.findById(expKey);
			if(expCheck == null||!STATUS10.equals(expCheck.getStatus())){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "单据"+billOmExp.getExpNo()+"已删除或状态已改变");
				return map;
			}
			
			String status = billOmExp.getStatus();
			billOmExp.setEdittm(new Date());
			billOmExp.setEditor(user.getLoginName());
			billOmExp.setEditorname(user.getUsername());
			billOmExp.setLocno(user.getLocNo());
			if (billOmExpManager.modifyById(billOmExp) > 0) {
				if(StringUtils.isNotBlank(status)&&OVER_STATUS.equals(status)){
					billStatusLogManager.procInsertBillStatusLog(user.getLocNo(), 
							billOmExp.getExpNo(), "OM", OVER_STATUS, 
							"更新发货通知单状态为"+OVER_STATUS, user.getLoginName());
				}
				map.put("result", ResultEnums.SUCCESS.getResultMsg());
				map.put("msg", "关单成功!");
				return map;
			} else {
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "单据"+billOmExp.getExpNo()+"关单失败!");
				return map;
			}
		} catch (Exception e) {
			log.error("===========修改出库订单时异常："+e.getMessage(),e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", "单据"+billOmExp.getExpNo()+"关单失败,出现异常!");
			return map;
		}
	}
	
	@RequestMapping(value = "/list_divide")
	@ResponseBody
	public  Map<String, Object> queryListExp(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			String keyStr = req.getParameter("keyStr");
			String[] receiptNos=keyStr.split(",");
			keyStr = "";
			for(int i = 0; i < receiptNos.length; i++) {
				keyStr += "'" + receiptNos[i] + "',";
			}
			keyStr = keyStr.substring(0, keyStr.length()-1);

			Map<String, Object> params = builderParams(req, model);
			
			BillOmExp exp = new BillOmExp();
			exp.setReceiptNos(keyStr);
			if(params.get("locno") != null) {
				exp.setLocno(params.get("locno").toString());
			}
			if(params.get("expNo") != null) {
				exp.setExpNo(params.get("expNo").toString());
				
			}
			if(params.get("status") != null) {
				exp.setStatus(params.get("status").toString());
			}
			if(params.get("brandNo") != null) {
				exp.setBrandNo(params.get("brandNo").toString());
			}
			if(params.get("startCreatetmExp") != null) {
				exp.setCreatetmBegin(params.get("startCreatetmExp").toString());
			}
			if(params.get("endCreatetmExp") != null) {
				exp.setCreatetmEnd(params.get("endCreatetmExp").toString());
			}
			if(params.get("businessType") != null) {
				exp.setBusinessType(params.get("businessType").toString());
			}
			if(params.get("isPoNo") != null) {
				String poNo = params.get("isPoNo").toString();
				if(poNo.equals("Y")){
					exp.setIsPoNo(poNo);
				}
			}
			
			int total = billOmExpManager.findCountExp(exp, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmExp> list = billOmExpManager.findByPageExp(page, sortColumn, sortOrder, exp, authorityParams);
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 预分货单查询发货通知单
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/list_divide_pre")
	@ResponseBody
	public  Map<String, Object> queryListExpForPre(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			

			Map<String, Object> params = builderParams(req, model);
			
			BillOmExp exp = new BillOmExp();
			
			if(params.get("locno") != null) {
				exp.setLocno(params.get("locno").toString());
			}
			if(params.get("expNo") != null) {
				exp.setExpNo(params.get("expNo").toString());
				
			}
			if(params.get("status") != null) {
				exp.setStatus(params.get("status").toString());
			}
			if(params.get("brandNo") != null) {
				exp.setBrandNo(params.get("brandNo").toString());
			}
			if(params.get("startCreatetmExp") != null) {
				exp.setCreatetmBegin(params.get("startCreatetmExp").toString());
			}
			if(params.get("endCreatetmExp") != null) {
				exp.setCreatetmEnd(params.get("endCreatetmExp").toString());
			}
			
			int total = billOmExpManager.findCountExpForPre(exp, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmExp> list = billOmExpManager.findByPageExpForPre(page, sortColumn, sortOrder, exp, authorityParams);
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
     * 手工关闭
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/overExpBill")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> overExpBill(HttpServletRequest req,BillOmExp billOmExp) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
			billOmExpManager.overExpBill(billOmExp);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	
	/**
     * 转存储发货
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/toClass0ForExp")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> toClass0ForExp(HttpServletRequest req,BillOmExp billOmExp) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		//获取登陆用户和仓别
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		    if(billOmExp!=null){
		    	billOmExp.setCreator(user.getLoginName());
		    	billOmExp.setCreatorname(user.getUsername());
		    	billOmExp.setEditor(user.getLoginName());
		    	billOmExp.setEditorname(user.getUsername());
		    }
			billOmExpManager.toClass0ForExp(billOmExp);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
	/**
     * 转分货发货
     * @param billOmDeliverDtl
     * @param req
     * @return
     * @throws ManagerException
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/toClass1ForExp")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> toClass1ForExp(HttpServletRequest req,BillOmExp billOmExp) throws ManagerException {
    	Map<String, Object> flag = new HashMap<String, Object>();
    	try {
    		//获取登陆用户和仓别
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		    if(billOmExp!=null){
		    	billOmExp.setCreator(user.getLoginName());
		    	billOmExp.setEditor(user.getLoginName());
		    }
			billOmExpManager.toClass1ForExp(billOmExp);
			flag.put("result", "success");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("result", "fail");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
    }
}