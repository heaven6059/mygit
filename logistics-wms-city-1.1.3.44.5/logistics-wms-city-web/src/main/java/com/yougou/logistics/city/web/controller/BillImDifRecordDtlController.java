package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillImDifRecord;
import com.yougou.logistics.city.common.model.BillImDifRecordDtl;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.manager.BillImDifRecordDtlManager;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
@RequestMapping("/bill_im_dif_record_dtl")
public class BillImDifRecordDtlController<ModelType> extends BaseCrudController<BillImDifRecordDtl> {
	@Log
	private Logger log;
	@Resource
    private BillImDifRecordDtlManager billImDifRecordDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bill_im_dif_record_dtl/",billImDifRecordDtlManager);
    }
    
    /**
     * 明细查询
     */
    @RequestMapping(value = "/dtlList.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = this.billImDifRecordDtlManager.findCount(params);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillImDifRecordDtl> list = this.billImDifRecordDtlManager.findByPage(page, sortColumn, sortOrder, params);
    		
    		obj.put("total", total);
    		obj.put("rows", list);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
		return obj;
	}
    
    /**
   	 * 查询商品信息
   	 */
   	@RequestMapping(value = "/get_Content")
   	@ResponseBody
   	public Map<String, Object> getBiz(HttpServletRequest req, Model model) throws ManagerException {
   		Map<String, Object> obj = new HashMap<String, Object>();
   		try{
   			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
   	   		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
   	   		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
   	   		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
   	   		Map<String, Object> params = builderParams(req, model);
   	   		int total = billImDifRecordDtlManager.selectContentCount(params);
   	   		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
   	   		List<BillImDifRecordDtl> list = billImDifRecordDtlManager.selectContent(page, sortColumn, sortOrder, params);
   	   		
   	   		obj.put("total", total);
   	   		obj.put("rows", list);
   		}catch(Exception e){
   			log.error(e.getMessage(), e);
   		}
   		return obj;
   	}
   	
   	/**
   	 * 新增和删除预到货通知单明细
   	 */
   	@SuppressWarnings("rawtypes")
   	@RequestMapping(value = "/addDtl")
   	public ResponseEntity<Map<String, Object>> addDtl(HttpServletRequest req,BillImDifRecord billImDifRecord)
   			throws JsonParseException, JsonMappingException, IOException,ManagerException {
   		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			if(null!=billImDifRecord){
				String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
				String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
				String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
				
				ObjectMapper mapper = new ObjectMapper();
				Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
				if (StringUtils.isNotBlank(deletedList)) {
					List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
					List<ModelType> oList=convertListWithTypeReference(mapper,list);
					params.put(CommonOperatorEnum.DELETED, oList);
				}
				if (StringUtils.isNotBlank(upadtedList)) {
					List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>(){});
					List<ModelType> oList=convertListWithTypeReference(mapper,list);
					params.put(CommonOperatorEnum.UPDATED, oList);
				}
				if (StringUtils.isNotBlank(insertedList)) {
					List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
					List<ModelType> oList=convertListWithTypeReference(mapper,list);
					params.put(CommonOperatorEnum.INSERTED, oList);
				}
				flag = billImDifRecordDtlManager.addDtl(billImDifRecord, params);
			}
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
			
		}catch (ManagerException e1) {
			flag.put("flag", "warn");
			flag.put("msg", e1.getMessage());
			log.error("保存明细时异常："+e1.getMessage(),e1);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error("保存明细时异常："+e.getMessage(),e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
   	}
    
	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<ModelType> tl=new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
}