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
import javax.servlet.http.HttpSession;

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

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.CmDefcellCellStatusEnums;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.CmDefareaManager;

/**
 * 
 * 	库区 controller
 * 
 * @author qin.dy
 * @date 2013-9-25 下午4:52:06
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/cm_defarea")
@ModuleVerify("25020030")
public class CmDefareaController extends BaseCrudController<CmDefarea> {
    
	@Log
    private Logger log;
	
	@Resource
    private CmDefareaManager cmDefareaManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("cmdefarea/",cmDefareaManager);
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/delete_records")
 	@ResponseBody
 	@OperationVerify(OperationVerifyEnum.REMOVE)
 	public ResponseEntity<Map<String, Object>> deleteFefloc(String datas,String locno) throws ManagerException{
    	Map<String, Object> flag = new HashMap<String, Object>();
    	List<CmDefarea> listCmDefareas = new ArrayList<CmDefarea>();
 		try {
 			ObjectMapper mapper = new ObjectMapper();
 			if (StringUtils.isNotEmpty(datas)) {
 				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
 				listCmDefareas=convertListWithTypeReference(mapper,list);
 			}
    		
 			flag = cmDefareaManager.deleteBatch(locno,listCmDefareas);
 			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
 		}catch (Exception e) {
 			flag.put("flag", "fail");
 			log.error("===========异常："+e.getMessage(),e);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
         }
 	}
    
    @RequestMapping(value = "/addDefcartype")
    @ResponseBody
    public String addDefcartype(CmDefarea type, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String areaNo = type.getAreaNo();
			if(areaNo.length() >  5) {
				return "length";
			} else {
				Date date = new Date();
				type.setCreatetm(date);
				type.setCreator(user.getLoginName());
				type.setCreatorName(user.getUsername());
				type.setEdittm(date);
				type.setEditor(user.getLoginName());
				type.setEditorName(user.getUsername());
				
				cmDefareaManager.add(type);
				return "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
    
    @RequestMapping(value = "/update")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Object update(CmDefarea type) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			type.setEdittm(new Date());
			cmDefareaManager.modifyById(type);
			obj.put("result", "success");
		} catch (ManagerException e) {
			obj.put("result", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			obj.put("result", "系统异常");
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    /**
     * 仓区多选时,选择多个库区
     */
    @RequestMapping(value = "/get_storeroom")
   	@ResponseBody
   	public List<CmDefarea> getStoreroom(HttpServletRequest req,Model model)throws ManagerException{
   		Map<String,Object> p=builderParams(req, model);
   		Map<String,Object> params = new HashMap<String,Object>(p);
   		List<String> wareNoList = new ArrayList<String>();
		String wareNotemp = (String)params.get("wareNo");
		if( null != wareNotemp || !"".equals(wareNotemp)||!"null".equals(wareNotemp))
		{
			String  [] areaNos = wareNotemp.split(",");
			for (String string : areaNos) {
				wareNoList.add(string);
			}
			params.put("wareNo", wareNoList);
		}
   		return this.cmDefareaManager.findByStoreroom(params);
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
   	private List<CmDefarea> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
   		Class<CmDefarea> entityClass = (Class<CmDefarea>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
   		List<CmDefarea> tl=new ArrayList<CmDefarea>(list.size());
   		for (int i = 0; i < list.size(); i++) {
   			CmDefarea type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
   			tl.add(type);
   		}
   		return tl;
   	}
}