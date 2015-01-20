package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.ItemCellContent;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.ConBoxDtlManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
@RequestMapping("/con_box_dtl")
public class ConBoxDtlController extends BaseCrudController<ConBoxDtl> {
	
	@Log
	private Logger log;
	
    @Resource
    private ConBoxDtlManager conBoxDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("conboxdtl/",conBoxDtlManager);
    }
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
    	AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = conBoxDtlManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<ConBoxDtl> list = conBoxDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams,DataAccessRuleEnum.BRAND);
		//返回汇总列表
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		footerMap.put("boxNo", "小计");
		footerList.add(footerMap);
		if(null!=list && list.size()>0){
			for (ConBoxDtl temp : list) {
				try {
					FooterUtil.setFooterMap("qty", new BigDecimal(0) , footerMap);
					FooterUtil.setFooterMap("qty", temp.getQty(), footerMap);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}else{
			footerMap.put("qty", 0);
		}
		// 合计
		Map<String, Object> sumFoot = new HashMap<String, Object>();
		if (pageNo == 1) {
			sumFoot = conBoxDtlManager.findSumQty(params,authorityParams);
			if (sumFoot == null) {
				sumFoot = new SumUtilMap<String, Object>();
				sumFoot.put("qty", 0);
			}
			sumFoot.put("isselectsum", true);
			sumFoot.put("box_no", "合计");
		} else {
			sumFoot.put("boxNo", "合计");
		}
		footerList.add(sumFoot);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		obj.put("footer", footerList);
		return obj;
	}
	@RequestMapping(value = "/findCnBoxAndNum")
	public ResponseEntity<Map<String,Object>> findCnBoxAndNum(ConBoxDtl conBoxDtl, HttpServletRequest req,HttpServletResponse response) {
    	Map<String,Object> obj=new HashMap<String,Object>();
    	int total=0;
		List<ConBoxDtl> listItem = new ArrayList<ConBoxDtl>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			
			//总数
			total = conBoxDtlManager.countBoxAndNum(conBoxDtl,authorityParams);
			
			//记录list
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			listItem = conBoxDtlManager.findCnBoxAndNumPage(page, conBoxDtl,authorityParams);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		obj.put("total", total);
		obj.put("rows", listItem);
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
}