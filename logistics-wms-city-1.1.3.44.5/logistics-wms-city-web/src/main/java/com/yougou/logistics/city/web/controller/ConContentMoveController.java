package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ConContentMove;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.ConContentMoveManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Fri Mar 07 11:21:04 CST 2014
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
@RequestMapping("/con_content_move")
public class ConContentMoveController extends
	BaseCrudController<ConContentMove> {
    @Resource
    private ConContentMoveManager conContentMoveManager;

    @Log
    private Logger log;

    @Override
    public CrudInfo init() {
	return new CrudInfo("concontentmove/", conContentMoveManager);
    }

    @RequestMapping(value = "/dtllist.json")
    @ResponseBody
    public Map<String, Object> dtlList(HttpServletRequest req, Model model)
	    throws ManagerException {
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
		String itemNo = "";
		if(paramsAll.get("itemNo") != null) {
			itemNo = paramsAll.get("itemNo").toString().toUpperCase();
			params.put("itemNo", itemNo);
		}
		String barcode = "";
		if(paramsAll.get("barcode") != null) {
			barcode = paramsAll.get("barcode").toString().toUpperCase();
			params.put("barcode", barcode);
		}
	    int total = conContentMoveManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
	    List<ConContentMove> list = conContentMoveManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
	    // 返回汇总列表
	    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
	    Map<String, Object> footerMap = new HashMap<String, Object>();
	    footerMap.put("cellNo", "小计");
	    footerList.add(footerMap);

	    for (ConContentMove temp : list) {
		FooterUtil
			.setFooterMap("moveQty", temp.getMoveQty(), footerMap);
	    }
	    // 合计
	    Map<String, Object> sumFoot = new HashMap<String, Object>();
	    if (pageNo == 1) {
		sumFoot = conContentMoveManager.selectSumQty(params,authorityParams);
		if (sumFoot == null) {
		    sumFoot = new SumUtilMap<String, Object>();
		    sumFoot.put("move_Qty", 0);
		}
		sumFoot.put("isselectsum", true);
		sumFoot.put("cell_No", "合计");
	    } else {
		sumFoot.put("cellNo", "合计");
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
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dtl_export")
	public void dtlExport(ConContentMove modelType,HttpServletRequest req,Model model,HttpServletResponse response)throws ManagerException{
    	Map<String, Object> paramsAll = builderParams(req, model);
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(paramsAll);
		String itemNo = "";
		if(paramsAll.get("itemNo") != null) {
			itemNo = paramsAll.get("itemNo").toString().toUpperCase();
			params.put("itemNo", itemNo);
		}
		String barcode = "";
		if(paramsAll.get("barcode") != null) {
			barcode = paramsAll.get("barcode").toString().toUpperCase();
			params.put("barcode", barcode);
		}
         String exportColumns=(String) params.get( "exportColumns");
         String fileName=(String)params.get( "fileName");
         ObjectMapper mapper = new ObjectMapper();
          if (StringUtils.isNotEmpty(exportColumns)){
                try {
                     exportColumns=exportColumns.replace( "[" ,"" );
                     exportColumns=exportColumns.replace( "]" ,"" );
                     exportColumns= "[" +exportColumns+"]" ;
                     
                      //字段名列表
                     List<Map> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<Map>>(){});
                     
                     //List<ModelType> list= this .manager .findByBiz(modelType, params);
                     int total = conContentMoveManager.findCount(params);
             		SimplePage page = new SimplePage(1, total, (int) total);
             		List<ConContentMove> list = conContentMoveManager.findByPage(page, "", "", params);
                     List<Map> listArrayList= new ArrayList< Map>();
                      if (list!= null&&list.size()>0){
                         for (ConContentMove vo:list){
                            Map map= new HashMap();
                               BeanUtilsCommon.object2MapWithoutNull(vo,map);
                               listArrayList.add(map);
                                 
                         }
                        HSSFExport.commonExportData(StringUtils.isNotEmpty(fileName)?fileName:"导出信息",ColumnsList,listArrayList, response);
                     }
               } catch (Exception e) {
                     e.printStackTrace();
               }
               

         } else {
                
         }
		
	}
}