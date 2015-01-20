package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
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
import com.yougou.logistics.city.common.model.BillUmBoxDtl;
import com.yougou.logistics.city.common.model.BillUmLoadbox;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmBoxDtlManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:21:11 CST 2014
 * @version 1.0.6
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
@RequestMapping("/bill_um_box_dtl")
@ModuleVerify("25060100")
public class BillUmBoxDtlController extends BaseCrudController<BillUmBoxDtl> {

	@Log
	private Logger log;

	@Resource
	private BillUmBoxDtlManager billUmBoxDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billUmBoxDtl/", billUmBoxDtlManager);
	}

	@RequestMapping(value = "/dtllist.json")
	@ResponseBody
	public Map<String, Object> dtlList(HttpServletRequest req, Model model) throws ManagerException {
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
			int total = billUmBoxDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillUmBoxDtl> list = billUmBoxDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("boxNo", "小计");
			footerList.add(footerMap);

			for (BillUmBoxDtl temp : list) {
				FooterUtil.setFooterMap("itemQty", temp.getItemQty(), footerMap);
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billUmBoxDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("box_No", "合计");
			} else {
				sumFoot.put("boxNo", "合计");
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

	@RequestMapping(value = "/createBoxDtl")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> createLoadBox(BillUmLoadbox box, HttpServletRequest req, String keyStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			box.setCreatetm(new Date());
			box.setCreator(user.getLoginName());
			box.setLocno(user.getLocNo());
			billUmBoxDtlManager.createBoxDtl(box, keyStr, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			////判断封箱时 是否出现是已装箱数大于实际数量标识
			map.put("flag", "error");
			map.put("msg", e.getMessage());
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}

	@RequestMapping(value = "/printByBox")
	@ResponseBody
	public Map<String, Object> printByBox(Model model, HttpServletRequest request, HttpSession session, String keys) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();
			if (StringUtils.isEmpty(keys)) {
				result.put("result", "error");
				result.put("msg", "参数错误");
			}
			String[] keyArray = keys.split(",");
			List<String> list = new ArrayList<String>();
			for (String keyStr : keyArray) {
				String[] params = keyStr.split("\\|");
				if (!list.contains(params[1])) {
					list.add(params[1]);
					Map<String, Object> resultMap = new HashMap<String, Object>();
					BillUmBoxDtl dtl = new BillUmBoxDtl();
					dtl.setLoadboxNo(params[0]);
					dtl.setBoxNo(params[1]);
					String creator = params[2];
					String createtm = params[3];
					createtm = StringToDate(createtm, "yyyy-MM-dd");

					//返回参数列表
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("loadboxNo", params[0]);
					paramMap.put("locno", user.getLocNo());
					paramMap.put("boxNo", params[1]);
					List<BillUmBoxDtl> listTemp = billUmBoxDtlManager.findByBiz(dtl, paramMap);
					if (CollectionUtils.isEmpty(listTemp)) {
						result.put("result", "error");
						result.put("msg", "没有查询到明细");
					}
					StringBuffer str = getHeadHtml(listTemp, params[0], creator, createtm);
					resultMap.put("rows", listTemp);
					resultMap.put("html", str);
					resultMap.put("boxNo", params[1]);
					resultData.add(resultMap);
				}

			}
			result.put("data", resultData);
			result.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常,请联系管理员");
		}
		return result;
	}

	private StringBuffer getHeadHtml(List<BillUmBoxDtl> listTemp, String loadboxNo, String creator, String createtm) {
		StringBuffer headHtml = new StringBuffer();
		int total = 0;
		//			String boxNo = params.get("boxNo").toString();

		for (BillUmBoxDtl list : listTemp) {
			BigDecimal qty = list.getItemQty();
			total += qty.intValue();
		}
		headHtml.append("<table  border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>");
		headHtml.append("<tr bgcolor='#fff' style='height:38px;'>");
		headHtml.append("<td colspan='6'>");
		headHtml.append("<table border='0' cellpadding='0' cellspacing='0'>");
		headHtml.append("<tr>");
		headHtml.append("<td bgcolor='#fff' align='left' width='50%'>装箱人:&nbsp;" + creator + "</td>");
		headHtml.append("<td bgcolor='#fff' align='left' >装箱日期:&nbsp;" + createtm + "</td>");
		headHtml.append("</tr>");
		headHtml.append("</table>");
		headHtml.append("</td>");
		headHtml.append("</tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:38px;'>");
		headHtml.append("<td >装箱单号:</td><td colspan='5' align='center'>" + loadboxNo + "</td>");
		headHtml.append("</tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:60px;'>");
		headHtml.append("<td>箱号:</td><td colspan='5' align='center'>&nbsp;</td>");
		headHtml.append("</tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:36px;'>");
		headHtml.append("<td style='width:23%;'>总数:</td>");
		headHtml.append("<td style='width:13%;'>" + total + "</td>");
		headHtml.append("<td style='width:20%;'>体积:</td>");
		headHtml.append("<td style='width:13%;'>&nbsp;</td>");
		headHtml.append("<td style='width:20%;'>重量:</td>");
		headHtml.append("<td>&nbsp;</td>");
		headHtml.append("</tr>");
		headHtml.append("<tr bgcolor='#fff' style='height:26px;'>");
		headHtml.append("<td colspan='3' align='center'>商品编码</td>");
		headHtml.append("<td align='center'>尺码</td>");
		headHtml.append("<td colspan='2' align='center'>数量</td>");
		headHtml.append("</tr>");

		for (int i = 0; i < listTemp.size() && i < 15; i++) {
			headHtml.append("<tr bgcolor='#fff' style='height:26px;'>");
			if (i < listTemp.size()) {
				BillUmBoxDtl dtl = listTemp.get(i);
				headHtml.append("<td colspan='3' align='center'>" + dtl.getItemNo() + "</td>");
				headHtml.append("<td align='right'>" + dtl.getSizeNo() + "</td>");
				headHtml.append("<td colspan='2' align='right'>" + dtl.getItemQty() + "</td>");
			} else {
				headHtml.append("<td colspan='3' align='center'>&nbsp;</td>");
				headHtml.append("<td align='right'>&nbsp;</td>");
				headHtml.append("<td colspan='2' align='right'>&nbsp;</td>");
			}
			headHtml.append("</tr>");
		}

		headHtml.append("</table>");
		return headHtml;
	}

	/**
	 * 字符串转换到时间格式
	 * @param dateStr 需要转换的字符串
	 * @param formatStr 需要格式的目标字符串  举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException 转换异常
	 */
	private static String StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String startTime = sdf.format(date);
		return startTime;
	}

	@RequestMapping(value = "/createRfNoSealed")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> createRfNoSealed(BillUmBoxDtl box, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			box.setLocno(user.getLocNo());
			billUmBoxDtlManager.createRfNoSealed(box, user);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
	}
}