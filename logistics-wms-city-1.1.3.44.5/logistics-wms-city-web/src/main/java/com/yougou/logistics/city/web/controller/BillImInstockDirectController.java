package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillImInstockDirectManager;
import com.yougou.logistics.city.manager.BillOmOutstockDirectManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 上架任务
 * @author luo.hl
 * @date  Thu Oct 10 10:56:15 CST 2013
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
@RequestMapping("/bill_im_instock_direct")
@ModuleVerify("25070030")
public class BillImInstockDirectController extends BaseCrudController<BillImInstockDirect> {
	@Log
	private Logger log;

	@Resource
	private BillImInstockDirectManager billImInstockDirectManager;

	@Resource
	private BillOmOutstockDirectManager billOmOutstockDirectManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billiminstockdirect/", billImInstockDirectManager);
	}

	@RequestMapping(value = "/cancelDirect")
	@ResponseBody
	public String cancelDirect(String locNo, String receiptNo, String ownerNo, String keyStr, HttpServletRequest req) {
		try {
			billImInstockDirectManager.cancelDirect(locNo, receiptNo, ownerNo, keyStr);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
		return "success";
	}

	/**
	 * 取消定位-验收或收货
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
	public Map<String, Object> cancelDirectForAll(String locNo, String ownerNo, String sourceNo, String flag,
			HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("result", "success");
			billImInstockDirectManager.cancelDirectForAll(locNo, ownerNo, sourceNo, flag);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * 发单
	 * 
	 * @param locNo
	 * @param rowStrs
	 * @param instockWorker
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/createInstock")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> createInstock(String locNo, String rowStrs, String instockWorker, HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImInstockDirectManager.createInstock(user.getLoginName(), locNo, rowStrs, instockWorker);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * 最新发单
	 * 
	 * @param locno
	 * @param ownerNo
	 * @param sender
	 * @param rowIdList
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/sendOrder")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> sendOrder(String locno, String ownerNo, String sender, String rowIdList,
			HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// 获取登陆用户
			HttpSession session = req.getSession();
			Object systemId = session.getAttribute(PublicContains.SESSION_SYSTEMID);
			Object areaSystemId = session.getAttribute(PublicContains.SESSION_AREASYSTEMID);
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			if(systemId != null && areaSystemId != null){
				billImInstockDirectManager.sendOrder(locno, ownerNo, sender, rowIdList, user.getLoginName(),
						Integer.parseInt(systemId.toString()),Integer.parseInt(areaSystemId.toString()));
				result.put("result", "success");
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/instockDirect")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> instockDirect(String keyStr) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("result", "success");
			billImInstockDirectManager.instockDirect(keyStr);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/instockDirectForCheck")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> instockDirectForCheck(String keyStr, HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("result", "success");
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billImInstockDirectManager.instockDirectForCheck(keyStr, user.getLoginName());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * 根据验收单号查询定位信息
	 * 
	 * @param locno
	 * @param ownerNo
	 * @param checkNo
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findInstockDirectByCheckNo")
	public ResponseEntity<Map<String, Object>> findInstockDirectByCheckNo(String locno, String ownerNo, String checkNo,
			Model model, HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		int total = 0;
		List<BillImInstockDirect> listItem = new ArrayList<BillImInstockDirect>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			BillImInstockDirect objEntiy = new BillImInstockDirect();
			objEntiy.setLocno(locno);
			objEntiy.setOwnerNo(ownerNo);
			objEntiy.setSourceNo(checkNo);

			// 总数
			total = billImInstockDirectManager.countInstockDirectByMainId(objEntiy, authorityParams);

			// 记录list
			SimplePage page = new SimplePage(pageNo, pageSize, total);

			List<BillImInstockDirect> lstObjs = billImInstockDirectManager.findInstockDirectByMainIdPage(page,
					objEntiy, authorityParams);
			if (null != lstObjs && lstObjs.size() > 0) {
				listItem = lstObjs;
			}
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("cellNo", "小计");
			footerList.add(footerMap);
			for (BillImInstockDirect temp : lstObjs) {
				this.setFooterMap("instockQty", temp.getInstockQty(), footerMap);
			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImInstockDirectManager.selectSumQty4CheckDirect(objEntiy, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("instock_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("cell_No", "合计");
			} else {
				sumFoot.put("cellNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", listItem);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/findInstockDirectByType")
	public Map<String, Object> findInstockDirectByType(String locno, String ownerNo, String status, Model model,
			HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> obj = new HashMap<String, Object>();
		int total = 0;
		List<BillImInstockDirect> listItem = new ArrayList<BillImInstockDirect>(0);
		try {

			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));

			Map<String, Object> params = builderParams(req, model);

			BillImInstockDirect objEntiy = new BillImInstockDirect();
			objEntiy.setLocno(locno);
			objEntiy.setOwnerNo(ownerNo);
			// objEntiy.setSourceNo(checkNo);

			// 总数
			total = billImInstockDirectManager.countInstockDirectByType(params, authorityParams);

			// 记录list
			SimplePage page = new SimplePage(pageNo, pageSize, total);

			List<BillImInstockDirect> lstObjs = billImInstockDirectManager.findInstockDirectByTypePage(page, params,
					authorityParams);
			if (null != lstObjs && lstObjs.size() > 0) {
				listItem = lstObjs;
			}

			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("cellNo", "小计");
			footerList.add(footerMap);

			for (BillImInstockDirect temp : lstObjs) {

				FooterUtil.setFooterMap("instockQty", temp.getInstockQty(), footerMap);
			}
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImInstockDirectManager.selectInstockDirectByTypePage4Sum(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("instockQty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("cell_No", "合计");
			} else {
				sumFoot.put("cellNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", listItem);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 根据角色ID查询对应下的所有用户信息
	 * 
	 * @param roleId
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/getUserListByRoleId")
	@ResponseBody
	public List<SystemUser> getUserListByRoleId(HttpServletRequest req, Integer roleId, String locno)
			throws ManagerException {
		List<SystemUser> lstObjs = new ArrayList<SystemUser>(0);
		try {
			SystemUser u = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
			SystemUser user = new SystemUser();
			user.setLocNo(u.getLocNo());
			user.setRoleId(roleId);
			lstObjs = billOmOutstockDirectManager.getUserListByRoleId(user);
		} catch (Exception e) {
			log.error("=========== 根据角色ID查询对应下的所有用户信息时异常：" + e.getMessage(), e);
		}
		return lstObjs;
	}

	@RequestMapping(value = "/selectDirect")
	@ResponseBody
	public Map<String, Object> selectDetail4Instock(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map paramMap = this.builderParams(req, model);
			int total = this.billImInstockDirectManager.findCount(paramMap);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImInstockDirect> list = billImInstockDirectManager.findByPage(page, "", "", paramMap);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("destCellNo", "小计");
			footerList.add(footerMap);
			for (BillImInstockDirect temp : list) {
				this.setFooterMap("instockQty", temp.getInstockQty(), footerMap);
			}

			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billImInstockDirectManager.selectSumQty(paramMap, null);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("instock_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("dest_Cell_No", "合计");
			} else {
				sumFoot.put("destCellNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}
}