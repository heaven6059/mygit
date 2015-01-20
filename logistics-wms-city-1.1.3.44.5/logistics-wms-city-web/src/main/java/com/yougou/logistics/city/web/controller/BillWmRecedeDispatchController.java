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
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillWmOutstockDirectManager;
import com.yougou.logistics.city.manager.BillWmRecedeManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-1-10 下午6:47:17
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/wmrecededispatch")
@ModuleVerify("25090050")
public class BillWmRecedeDispatchController extends BaseCrudController<BillWmOutstockDirect> {
	
	@Log
	private Logger log;
	
	@Resource
    private BillWmRecedeManager billWmRecedeManager;
	
	@Resource
    private BillWmOutstockDirectManager billWmOutstockDirectManager;
	
	@Override
    public CrudInfo init() {
        return new CrudInfo("billWmRecedeDispatch/",billWmRecedeManager);
    }
	
	/**
	 * 退厂定位
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/procBillWmRecedeLocateQuery")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String, Object>> procBillWmRecedeLocateQuery(HttpServletRequest req) throws ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			List<BillWmOutstockDirect> listWmRecedes = null;
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>(){});
				listWmRecedes=convertListWithTypeReferenceR(mapper,list);
			}
			
			billWmRecedeManager.procBillWmRecedeLocateQuery(listWmRecedes);
			flag.put("flag", "success");
			flag.put("msg", "定位成功!");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("=========== 退厂定位时异常："+e.getMessage(),e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	
	/**
	 * 查询单明细列表（带分页）
	 */
   	@RequestMapping(value = "/listBillWmRecedeByPage.json")
	@ResponseBody
	public  Map<String, Object> listBillWmRecedeByPage(HttpServletRequest req, BillWmRecede billWmRecede) throws ManagerException {
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			
			int total = billWmRecedeManager.findBillWmRecedeJoinDirectCount(billWmRecede, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillWmRecede> list = billWmRecedeManager.findBillWmRecedeJoinDirectByPage(page, sortColumn, sortOrder, billWmRecede, authorityParams);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		}catch (Exception e) {
			log.error("===========查询退厂调度列表（带分页）时异常："+e.getMessage(),e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
   	
   	
   	/**
     * 新增退厂通知单
     * @param billWmRecede
     * @param req
     * @return
     * @throws ManagerException
     */
	@RequestMapping(value = "/sendWmOutstockDirect")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public ResponseEntity<Map<String, Object>> sendWmOutstockDirect(BillWmOutstock billWmOutstock,HttpServletRequest req)throws ManagerException {
		Map<String, Object> flag = new HashMap<String, Object>();
		try{
			List<BillWmOutstockDirect> listDirects = null;
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>(){});
				listDirects=convertListWithTypeReferenceR(mapper,list);
			}
			
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billWmOutstock.setLocno(user.getLocNo());
			billWmOutstock.setCreator(user.getLoginName());
			billWmOutstock.setCreatorName(user.getUsername());
			billWmOutstock.setCreatetm(new Date());
			//billWmOutstock.setEditor(user.getLoginName());
			//billWmOutstock.setEdittm(new Date());
			billWmOutstockDirectManager.sendWmOutstockDirect(billWmOutstock, listDirects);
			
			flag.put("flag", "success");
			flag.put("msg", "发单成功!");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}catch (Exception e) {
			log.error("=========== 退厂发单时异常："+e.getMessage(),e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
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
	private List<BillWmRecede> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<BillWmRecede> entityClass = (Class<BillWmRecede>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<BillWmRecede> tl=new ArrayList<BillWmRecede>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillWmRecede type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
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
	private List<BillWmOutstockDirect> convertListWithTypeReferenceR(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<BillWmOutstockDirect> entityClass = (Class<BillWmOutstockDirect>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<BillWmOutstockDirect> tl=new ArrayList<BillWmOutstockDirect>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillWmOutstockDirect type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
}
