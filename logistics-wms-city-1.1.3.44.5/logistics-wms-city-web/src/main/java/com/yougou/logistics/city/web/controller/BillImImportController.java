package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillImImportManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.RequestBillImportVo;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 10:24:56
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
/**
 * 预到货通知单
 * @author zuo.sw
 * @param <ModelType>
 *
 */
@Controller
@RequestMapping("/billimimport")
@ModuleVerify("25070020")
public class BillImImportController<ModelType> extends BaseCrudController<BillImImport> {

	@Log
	private Logger log;

	@Resource
	private BillImImportManager billImImportManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billimimport/", billImImportManager);
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
			int total = this.billImImportManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImImport> list = this.billImImportManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
		    // 返回小计列表
		    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		    Map<String, Object> footerMap = new HashMap<String, Object>();
		    footerMap.put("importNo", "小计");
		    footerList.add(footerMap);

		    for (BillImImport temp : list) {
		    	if(temp.getSumQty() != null) {
			    	FooterUtil.setFooterMap("sumQty", temp.getSumQty(), footerMap);
		    	}
		    	if(temp.getBoxNoQty() != null) {
		    		FooterUtil.setFooterMap("boxNoQty", temp.getBoxNoQty(), footerMap);
		    	}
		    	if(temp.getReceiptBoxNoQty() != null) {
		    		FooterUtil.setFooterMap("receiptBoxNoQty", temp.getReceiptBoxNoQty(), footerMap);
		    	}
		    }
		    // 返回合计列表
		    Map<String, Object> sumFootMap = new HashMap<String, Object>();
		    if (pageNo == 1) {
				if(pageSize >= total){
					sumFootMap.putAll(footerMap);
				}else{
					Map<String, Object> sumFoot = billImImportManager.findSumQty(params,authorityParams);
					if(sumFoot.get("SUMQTY")!= null) {
						sumFootMap.put("sumQty", sumFoot.get("SUMQTY"));
					} else {
						 sumFootMap.put("sumQty", 0);
					}
					if(sumFoot.get("BOXNOQTY")!= null) {
						sumFootMap.put("boxNoQty", sumFoot.get("BOXNOQTY"));
					} else {
						sumFootMap.put("boxNoQty", 0);
					}
					if(sumFoot.get("RECEIPTBOXNOQTY")!= null) {
						sumFootMap.put("receiptBoxNoQty", sumFoot.get("RECEIPTBOXNOQTY"));
					} else {
						sumFootMap.put("receiptBoxNoQty", 0);
					}
					sumFootMap.put("isselectsum", true);
				}
				sumFootMap.put("importNo", "合计");
			    footerList.add(sumFootMap); 
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
			obj.put("footer", footerList);
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	/**
	 * 删除预到货通知单
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/deleteImImport")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> deleteImImport(HttpServletRequest req,String noStrs) throws ManagerException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			boolean isSuccess = billImImportManager.deleteImImport(noStrs,authorityParams);
			if (isSuccess) {
				result.put("result", ResultEnums.SUCCESS.getResultMsg());
			} else {
				result.put("result", ResultEnums.FAIL.getResultMsg());
			}
		} catch (Exception e) {
			log.error("=======删除预到货通知单异常：" + e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getCause().getMessage());
			//throw new ManagerException(e);
		}
		return result;
	}

	/**
	 * 新增
	 * @param billImImport
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/addBillImImport")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addBillImImport(BillImImport billImImport, HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//设置操作人
			billImImport.setCreator(user.getLoginName());
			billImImport.setEditor(user.getLoginName());
			billImImport.setLocno(user.getLocNo());
			billImImport.setCreatorName(user.getUsername());
			billImImport.setEditorName(user.getUsername());
			obj = billImImportManager.addBillImImport(billImImport);
		} catch (Exception e) {
			log.error("=======新增预到货通知单异常：" + e.getMessage(), e);
			obj.put("returnMsg", false);
			//throw new ManagerException(e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	/**
	 * 审核
	 * @param importNo
	 * @param locno
	 * @param ownerNo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/auditImImport")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> auditImImport(String noStrs, HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			obj = billImImportManager.auditImImport(noStrs, user,authorityParams);
		}catch (ManagerException e) {
			obj.put("flag", "warn");
			obj.put("resultMsg", e.getCause().getMessage());
		} catch (Exception e) {
			log.error("=======删除预到货通知单异常：" + e.getMessage(), e);
			obj.put("flag", "false");
			//throw new ManagerException(e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	/**
	 * 修改预到货通知单
	 * @param billImImport
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyImImport")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> modifyImImport(BillImImport billImImport, HttpServletRequest req) throws ManagerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			boolean isSuccess = billImImportManager.updateImImport(billImImport,user,authorityParams);
			if (isSuccess) {
				resultMap.put("result", ResultEnums.SUCCESS.getResultMsg());
			} else {
				resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			}
		} catch (Exception e) {
			log.error("===========修改预到货通知单时异常：" + e.getMessage(), e);
			resultMap.put("result", ResultEnums.FAIL.getResultMsg());
			resultMap.put("msg", e.getCause().getMessage());
		}
		return resultMap;
	}

	/**
	 * 结案-判断是否有对应的收货单
	 * @param importNo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectIsReceiptByImportNo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> selectIsReceiptByImportNo(String importNo, HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			obj = billImImportManager.selectIsReceiptByImportNo(importNo);
		} catch (Exception e) {
			log.error("=======结案-判断是否有对应的收货单异常：" + e.getMessage(), e);
			obj.put("flag", false);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	/**
	 *  结案
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/overImImport")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> overImImport(String importNo, String locno, String ownerNo)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			obj = billImImportManager.overImImport(importNo, locno, ownerNo);
		} catch (Exception e) {
			log.error("=======预到货通知单手动结案异常：" + e.getMessage(), e);
			obj.put("flag", "fail");
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	/**
	 * 差异验收--手工关闭
	 * @param keyStr
	 * @param locNo
	 * @param ownerNo
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/overFlocByDif")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> overFlocByDif(String keyStr, String locNo, String ownerNo, HttpServletRequest req) {
		Map obj = new HashMap();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if (billImImportManager.overFlocByDif(keyStr, locNo, ownerNo, user.getLoginName(),user.getUsername(),authorityParams) > 0) {
				obj.put("result", "success");
			} else {
				obj.put("result", "fail");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("returnMsg", e.getMessage());
			obj.put("result", "fail");
		}
		return obj;
	}
	
	/**
	 * 差异验收-分批上传
	 * @param locno
	 * @param importNoStr
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/bathUpload")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> bathUpload(String locno, String ownerNo,String importNoStr,HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			//获取登陆用户
			billImImportManager.bathUpload(locno, ownerNo,importNoStr,authorityParams);
			result.put("result", "success");
		}catch (ManagerException e){
			log.error("================差异验收-分批上传异常："+e.getMessage(),e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(value = "/findImportNoByPage")
	@ResponseBody
	public Map<String, Object> findImpoertNoByPage(HttpServletRequest req, Model model) throws ManagerException {
		Map obj = new HashMap();
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map params = builderParams(req, model);

			int total = billImImportManager.findImpoertNoCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List list = billImImportManager.findImportNoByPage(page, params,authorityParams);
			
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		}catch (ManagerException e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/findSupplierNo")
	@ResponseBody
	public Map<String, Object> findSupplierNo(HttpServletRequest req, BillImImport imp) throws ManagerException {
		Map obj = new HashMap();
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List list = billImImportManager.findSupplierNo(imp,authorityParams);
			obj.put("rows", list);
		}catch (ManagerException e){
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	@RequestMapping(value = "/toBillimReceipt")
	@ResponseBody
	public Map<String ,Object> toBillimReceipt(RequestBillImportVo reqVo,HttpServletRequest req)throws ManagerException {
		Map<String,Object> obj = new HashMap<String, Object>();
		HttpSession session = req.getSession();
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		try{
			billImImportManager.toBillimReceipt(reqVo.getListIm(), user);
			obj.put("result", "success");
		}catch (Exception e) {
			obj.put("result", "error");
			throw new ManagerException(e);
		}
		return obj;
	}
}