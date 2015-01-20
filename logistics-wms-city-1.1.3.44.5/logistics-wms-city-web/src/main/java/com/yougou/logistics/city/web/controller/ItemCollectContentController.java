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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ItemCollectContent;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.ItemCollectContentManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/item_collect_content")
@ModuleVerify("25130010")
public class ItemCollectContentController extends BaseCrudController<ItemCollectContent> {
	
	@Log
	private Logger log;
	
    @Resource
    private ItemCollectContentManager itemCollectContentManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("itemcollectcontent/",itemCollectContentManager);
    }
    
    @RequestMapping(value = "/dtl_list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) {
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
			
       	    //参数处理
			this.paramsHandle(params);
    		
    		int total = itemCollectContentManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<ItemCollectContent> list = itemCollectContentManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
    		
    		//返回汇总列表
    				List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
    				Map<String, Object> footerMap = new HashMap<String, Object>();
    				footerMap.put("itemNo", "小计");
    				footerList.add(footerMap);
    				for (ItemCollectContent temp : list) {
    					try {
    						FooterUtil.setFooterMap("qty", temp.getQty(), footerMap);
    					} catch (Exception e) {
    						log.error(e.getMessage(), e);
    					}
    				}
    		// 合计
    		Map<String, Object> sumFoot = new HashMap<String, Object>();
    		if (pageNo == 1) {
    			sumFoot = itemCollectContentManager.findSumQty(params,authorityParams);
    			if (sumFoot == null) {
    				sumFoot = new SumUtilMap<String, Object>();
    				sumFoot.put("qty", 0);
    			}
    			sumFoot.put("isselectsum", true);
    			sumFoot.put("item_no", "合计");
    		} else {
    			sumFoot.put("itemNo", "合计");
    		}
    		footerList.add(sumFoot);
    				
    				
    		obj.put("total", total);
    		obj.put("rows", list);
    		obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    @RequestMapping(value = "/exportlist.json")
	@ResponseBody
	public  Map<String, Object> exportQueryList(HttpServletRequest req, Model model) throws ManagerException {
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
	   	    //参数处理
			this.paramsHandle(params);
			int total = itemCollectContentManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<ItemCollectContent> list = itemCollectContentManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/do_exportlist")
	public void doExport(ItemCollectContent modelType,HttpServletRequest req,Model model,HttpServletResponse response)throws ManagerException{
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
   		
   	     //参数处理
		 this.paramsHandle(params);
         String exportColumns=(String) params.get( "exportColumns");
         String fileName=(String)params.get( "fileName");
         ObjectMapper mapper = new ObjectMapper();
          if (StringUtils.isNotEmpty(exportColumns)){
                try {
                	 AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
                     exportColumns=exportColumns.replace( "[" ,"" );
                     exportColumns=exportColumns.replace( "]" ,"" );
                     exportColumns= "[" +exportColumns+"]" ;
                     
                      //字段名列表
                     List<Map> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<Map>>(){});
                     int total = itemCollectContentManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
             		SimplePage page = new SimplePage(1, total, (int) total);
             		List<ItemCollectContent> list = itemCollectContentManager.findByPage(page, "", "", params, authorityParams, DataAccessRuleEnum.BRAND);
                     List<Map> listArrayList= new ArrayList< Map>();
                      if (list!= null&&list.size()>0){
                         for (ItemCollectContent vo:list){
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
    
    /**
   	 * 参数处理
   	 * @param params
   	 */
   	private void paramsHandle(Map<String, Object> params) {
//   		//品牌
//   		String brandNo = (String) params.get("brandNo");
//   		String[] brandNoValues = null;
//   		if (StringUtils.isNotEmpty(brandNo)) {
//   			brandNoValues = brandNo.split(",");
//   		}
//   		params.put("brandNoValues", brandNoValues);
   		
   		//大类一
   		String cateOne = (String) params.get("cateOne");
   		String[] cateOneValues = null;
   		if (StringUtils.isNotEmpty(cateOne)) {
   			cateOneValues = cateOne.split(",");
   		}
   		params.put("cateOneValues", cateOneValues);		
   		
   		//大类二
   		String cateTwo = (String) params.get("cateTwo");
   		String[] cateTwoValues = null;
   		if (StringUtils.isNotEmpty(cateTwo)) {
   			cateTwoValues = cateTwo.split(",");
   		}
   		params.put("cateTwoValues", cateTwoValues);		
   		
   		//大类三
   		String cateThree = (String) params.get("cateThree");
   		String[] cateThreeValues = null;
   		if (StringUtils.isNotEmpty(cateThree)) {
   			cateThreeValues = cateThree.split(",");
   		}
   		params.put("cateThreeValues", cateThreeValues);			
		// 季节
		String seasonName = (String) params.get("seasonName");
		String [] seasonValues = null;
		if (StringUtils.isNotBlank(seasonName)) {
			seasonValues = seasonName.split(",");
		}
		params.put("seasonValues", seasonValues);
		// 性别
		String genderName = (String) params.get("genderName");
		String [] genderValues = null;
		if (StringUtils.isNotBlank(genderName)) {
			genderValues = genderName.split(",");
		}
		params.put("genderValues", genderValues);
   //
//   		//品质
//   		String quality = (String) params.get("quality");
//   		String[] qualityValues = null;
//   		if (StringUtils.isNotEmpty(quality)) {
//   			qualityValues = quality.split(",");
//   		}
//   		params.put("qualityValues", qualityValues);
   	}
}