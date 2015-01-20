package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
import com.yougou.logistics.city.common.model.CmDefware;
import com.yougou.logistics.city.common.model.CmDefwareKey;
import com.yougou.logistics.city.manager.CmDefwareManager;

/**
 * 
 * 仓区Controller
 * 
 * @author qin.dy
 * @date 2013-9-25 下午3:27:37
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/cm_defware")
@ModuleVerify("25020040")
public class CmDefwareController extends BaseCrudController<CmDefware> {
	
	@Log
    private Logger log;
	
    @Resource
    private CmDefwareManager cmDefwareManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("cmdefware/",cmDefwareManager);
    }
    @RequestMapping(value = "/get_countValidate.json")
   	public ResponseEntity<Integer> getCount(HttpServletRequest req,CmDefwareKey cmDefwareKey)throws ManagerException{
    	try{
    		CmDefwareKey obj= this.cmDefwareManager.findById(cmDefwareKey);
           	int total = obj==null?0:1;
       		return new ResponseEntity<Integer>(total, HttpStatus.OK);
    	}catch(Exception e){
    		log.error(e.getMessage(),e);
    		return new ResponseEntity<Integer>(0, HttpStatus.NOT_FOUND);
    	}
    	
   	}
    
    
    @RequestMapping(value="/delete_records")
 	@ResponseBody
 	@OperationVerify(OperationVerifyEnum.REMOVE)
 	public ResponseEntity<Map<String, Object>> deleteFefloc(String datas,String locno) throws ManagerException{
    	Map<String, Object> flag = new HashMap<String, Object>();
    	List<CmDefware> listCmDefwares = new ArrayList<CmDefware>();
 		try {
 			
 			ObjectMapper mapper = new ObjectMapper();
 			if (StringUtils.isNotEmpty(datas)) {
 				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
 				listCmDefwares=convertListWithTypeReference(mapper,list);
 			}
 			
 			flag = cmDefwareManager.deleteBatch(locno, listCmDefwares);
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
	private List<CmDefware> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<CmDefware> entityClass = (Class<CmDefware>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<CmDefware> tl=new ArrayList<CmDefware>(list.size());
		for (int i = 0; i < list.size(); i++) {
			CmDefware type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
}