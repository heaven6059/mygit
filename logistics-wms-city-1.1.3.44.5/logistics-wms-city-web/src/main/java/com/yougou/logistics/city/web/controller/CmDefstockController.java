package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.CmDefstock;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.CmDefstockManager;

/**
 * 
 * 通道controller
 * 
 * @author qin.dy
 * @date 2013-9-25 下午4:51:14
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/cm_defstock")
@ModuleVerify("25020010")
public class CmDefstockController extends BaseCrudController<CmDefstock> {
	
	@Log
    private Logger log;
	
    @Resource
    private CmDefstockManager cmDefstockManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("cmdefstock/",cmDefstockManager);
    }
    
    @RequestMapping(value = "/cascade_save")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.ADD)
	public Object cascade_save(CmDefstock cmDefstock) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
		String result = "";
    	try {
    		int count = cmDefstockManager.addCascade(cmDefstock);
    		if(count <= 1){
    			result = "success";
    		}
    	} catch (ManagerException e) {
    		result = CommonUtil.getExceptionMessage(e);
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			result = CommonUtil.getExceptionMessage(e);
			log.error(e.getMessage(), e);
		}
		obj.put("result", result);
		return obj;
	}
    
    /**
     * 检查当前储位是否可用或冻结
     * @param osCust
     * @param req
     * @return
     */
    @RequestMapping(value = "/checkStock")
   	@ResponseBody
   	public Map<String,Object> checkStock(CmDefstock defstock, HttpServletRequest req) {
    	Map<String,Object> result = new HashMap<String,Object>();

    	try {
    		if(defstock.getStockStatus().equals("1")){
    			//检查储位表可用数据
        		boolean isOk0 = false;
       			int count0 = cmDefstockManager.queryStoreNo0(defstock);
       			if (count0<1) {
       				isOk0 = true;
       			}
       			//检查储位表冻结数据
       			boolean isOk2 = false;
       			int count2 = cmDefstockManager.queryStoreNo2(defstock);
       			if (count2<1) {
       				isOk2 = true;
       			}
       			if(isOk0 && isOk2) {
       				result.put("result", "success");
       			} else {
       				result.put("result", "exist");
       			}
    		}
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			result.put("result", "fail");
   		}
    	return result;
   	}
    
    @RequestMapping(value = "/moditfyCmDefstock")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.MODIFY)
	public boolean moditfyCmDefstock(CmDefstock type) throws ManagerException {
    	try {
    		type.setEdittm(new Date());
    		int count = cmDefstockManager.modifyById(type);
    		if(count <1){
    			return false;
    		}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
    
    @RequestMapping(value="/delete_records")
 	@ResponseBody
 	@OperationVerify(OperationVerifyEnum.REMOVE)
 	public ResponseEntity<Map<String, Object>> deleteRecords(String datas,String locno) throws ManagerException{
    	Map<String, Object> flag = new HashMap<String, Object>();
    	List<CmDefstock> listCmDefstocks = new ArrayList<CmDefstock>();
    	try {
    		
    		ObjectMapper mapper = new ObjectMapper();
 			if (StringUtils.isNotEmpty(datas)) {
 				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
 				listCmDefstocks=convertListWithTypeReference(mapper,list);
 			}
    		
 			flag = cmDefstockManager.deleteBatch(locno,listCmDefstocks);
 			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
 		}catch (Exception e) {
 			flag.put("flag", "fail");
 			log.error("===========异常："+e.getMessage(),e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
        }
 	}
    
    /**
   	 * 转换成泛型列表
   	 * @param mapper
   	 * @param list
   	 * @return
   	 * @throws JsonParseException
   	 * @throws JsonMappingException
   	 * @throws JsonGenerationException
   	 * @throws IOException
   	 */
   	@SuppressWarnings({ "unchecked", "rawtypes" })
   	private List<CmDefstock> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
   		Class<CmDefstock> entityClass = (Class<CmDefstock>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
   		List<CmDefstock> tl=new ArrayList<CmDefstock>(list.size());
   		for (int i = 0; i < list.size(); i++) {
   			CmDefstock type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
   			tl.add(type);
   		}
   		return tl;
   	}
}