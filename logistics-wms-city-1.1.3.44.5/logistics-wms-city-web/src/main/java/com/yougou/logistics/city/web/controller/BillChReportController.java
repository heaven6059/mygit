package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.yougou.logistics.city.common.model.BillChReport;
import com.yougou.logistics.city.manager.BillChReportManager;
import com.yougou.logistics.city.manager.LookupdtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/bill_ch_report")
@ModuleVerify("25130220")
public class BillChReportController extends BaseCrudController<BillChReport> {
	@Log
	private Logger log;
	@Resource
    private BillChReportManager billChReportManager;
	
	@Resource
    private LookupdtlManager  lookupdtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billchreport/",billChReportManager);
    }
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String brandNo = req.getParameter("brandNo");
			String years = req.getParameter("years");
			String season = req.getParameter("season");
			String gender = req.getParameter("gender");
			Map<String, Object> p = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(p);
			params.put("brandNo", splitStr(brandNo));
			params.put("years", splitStr(years));
			params.put("season", splitStr(season));
			params.put("gender", splitStr(gender));
			
			cateHandle(params);
			
			int total = this.billChReportManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			List<BillChReport> list = new ArrayList<BillChReport>();
			if(total > 0){
				SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
				list = this.billChReportManager.findByPage(page, null, null, params,authorityParams, DataAccessRuleEnum.BRAND);
				if(pageNo == 1){
					Map<String, Integer> map = this.billChReportManager.findSumQty(params,authorityParams);
					List<Object> fs = new ArrayList<Object>();
					Map<String, Object> f = mapKeyToLowerCase(map);
					f.put("planNo", "合计");
					fs.add(f);
					obj.put("footer",fs);
					
				}
			}
			
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
		}
		return obj;
	}
    
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/export")
	public void export(HttpServletRequest req,Model model,HttpServletResponse response)throws ManagerException{
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
                	String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
               		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
                     exportColumns=exportColumns.replace( "[" ,"" );
                     exportColumns=exportColumns.replace( "]" ,"" );
                     exportColumns= "[" +exportColumns+"]" ;
                     
                      //字段名列表
                     List<Map> ColumnsList=mapper.readValue(exportColumns, new TypeReference<List<Map>>(){});
                     
                     //List<ModelType> list= this .manager .findByBiz(modelType, params);
                     int total = this.billChReportManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
             		SimplePage page = new SimplePage(1, total, (int) total);
             		List<BillChReport>  list = this.billChReportManager.findByPage(page, null, null, params,authorityParams, DataAccessRuleEnum.BRAND);
                     List<Map> listArrayList= new ArrayList< Map>();
                     
                     
                     Map<String, Object> paramsYEARS = new HashMap<String, Object>();
                     paramsYEARS.put("lookupcode", "YEARS");
                     Map<String, Object> returnYEARS = lookupdtlManager.selectLookupdtlByCode(paramsYEARS);
                     
                     Map<String, Object> paramsSEASON = new HashMap<String, Object>();
                     paramsSEASON.put("lookupcode", "SEASON");
                     Map<String, Object> returnSEASON = lookupdtlManager.selectLookupdtlByCode(paramsSEASON);
                     
                     Map<String, Object> paramsGENDER = new HashMap<String, Object>();
                     paramsGENDER.put("lookupcode", "GENDER");
                     Map<String, Object> returnGENDER = lookupdtlManager.selectLookupdtlByCode(paramsGENDER);
                     
                      if (list!= null&&list.size()>0){
                    	 int itemQty = 0;
                    	 int realQty = 0;
                    	 int diffQty = 0;
                         for (BillChReport vo:list){
                        	 itemQty += vo.getItemQty().intValue();
                        	 realQty += vo.getRealQty().intValue();
                        	 diffQty += vo.getDiffQty().intValue();
                        	if(StringUtils.isNotBlank(vo.getYears())&& null!=returnYEARS && !returnYEARS.isEmpty()) {
                        		vo.setYearsName(String.valueOf(returnYEARS.get(vo.getYears())));
                        	}
                        	if(StringUtils.isNotBlank(vo.getSeason()) && null!=returnSEASON && !returnSEASON.isEmpty()) {
                        		vo.setSeasonName(String.valueOf(returnSEASON.get(vo.getSeason())));
                        	}
                        	if(StringUtils.isNotBlank(vo.getGender()) && null!=returnGENDER && !returnGENDER.isEmpty()) {
                        		vo.setGenderName(String.valueOf(returnGENDER.get(vo.getGender())));
                        	}
                            Map map= new HashMap();
                               BeanUtilsCommon.object2MapWithoutNull(vo,map);
                               listArrayList.add(map);
                                 
                         }
                         BillChReport sumVo = new BillChReport();
                         sumVo.setPlanNo("合计");
                         sumVo.setItemQty(new BigDecimal(itemQty));
                         sumVo.setRealQty(new BigDecimal(realQty));
                         sumVo.setDiffQty(new BigDecimal(diffQty));
                         Map sumMap= new HashMap();
                         BeanUtilsCommon.object2MapWithoutNull(sumVo,sumMap);
                         listArrayList.add(sumMap);
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
//		//品牌
//		String brandNo = (String) params.get("brandNo");
//		String[] brandNoValues = null;
//		if (StringUtils.isNotEmpty(brandNo)) {
//			brandNoValues = brandNo.split(",");
//		}
//		params.put("brandNoValues", brandNoValues);
		
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
		
//		//季节
//		String seasonName = (String) params.get("season");
//		String [] seasonValues = null;
//		if(StringUtils.isNotBlank(seasonName)){
//			seasonValues = seasonName.split(",");
//		}
//		params.put("seasonValues", seasonValues);
//		//性别
//		String genderName = (String) params.get("gender");
//		String [] genderValues = null;
//		if(StringUtils.isNotBlank(genderName)){
//			genderValues = genderName.split(",");
//		}
//		params.put("genderValues", genderValues);
//
//		//品质
//		String quality = (String) params.get("quality");
//		String[] qualityValues = null;
//		if (StringUtils.isNotEmpty(quality)) {
//			qualityValues = quality.split(",");
//		}
//		params.put("qualityValues", qualityValues);
	}
    
    public <V> Map<String, Object> mapKeyToLowerCase(Map<String, V> map){
    	Map<String, Object> rs = new HashMap<String, Object>();
    	String key = null;
    	int idx = 0;
    	int diff = 32;//字母大小写的编码差值
    	for(Entry<String, V> e:map.entrySet()){
    		key = e.getKey();
    		key = key.toLowerCase();
    		while((idx=key.indexOf("_")) >= 0){
    			if(idx == key.length() - 1){
    				key = key.substring(0,idx);
    			}else{
    				key = key.substring(0,idx)+ (char)(key.charAt(idx+1) - diff) + key.substring(idx+2);
    			}
    		}
    		rs.put(key, e.getValue());
    	}
    	return rs;
    }
    
    /**
     * " xxxx , yyyy" to "'xxxx','yyyy'"
     * @param val
     * @return
     */
    public String splitStr(String val){
    	if(StringUtils.isNotEmpty(val)){
    		val = val.replace(" ", "");
    		val = val.replace(",", "','");
    		return "'" + val + "'";
    	}
    	return null;
    }
    public void cateHandle(Map<String, Object> params){
    	Object obj = null;
    	
    	String cateThree = null;
    	if((obj = params.get("cateThree")) != null){
    		cateThree = obj.toString();
    		if(StringUtils.isNotEmpty(cateThree)){
    			cateThree = cateThree.replace(" ", "");
    			params.put("cateThreeValues", cateThree.split(","));
    			return;
    		}
    	}
    	String cateTwo = null;
    	if((obj = params.get("cateTwo")) != null){
    		cateTwo = obj.toString();
    		if(StringUtils.isNotEmpty(cateTwo)){
    			cateTwo = cateTwo.replace(" ", "");
    			params.put("cateTwoValues", cateTwo.split(","));
    			return;
    		}
    	}
    	String cateOne = null;
    	if((obj = params.get("cateOne")) != null){
    		cateOne = obj.toString();
    		if(StringUtils.isNotEmpty(cateOne)){
    			cateOne = cateOne.replace(" ", "");
    			params.put("cateOneValues", cateOne.split(","));
    			return;
    		}
    	}
    }
}