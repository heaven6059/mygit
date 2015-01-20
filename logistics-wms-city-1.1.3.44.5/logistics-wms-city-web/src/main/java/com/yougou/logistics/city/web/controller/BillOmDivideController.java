package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.common.vo.ResultVo;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmDivideManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-10-14 下午8:23:36
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_divide")
@ModuleVerify("25040010")
public class BillOmDivideController extends BaseCrudController<BillOmDivide> {

	@Log
	private Logger log;

	@Resource
	private BillOmDivideManager billOmDivideManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billomdivide/", billOmDivideManager);
	}

	//	@RequestMapping(value="/list_tabDetail")
	//	public String list_tabMain(HttpServletRequest request){
	//		String thisPower=request.getParameter("thisPower");
	//		request.setAttribute("thisPower",thisPower);
	//		return "billomdivide/list_tabDetail";
	//	}
	
	
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmDivideManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDivide> list = this.billOmDivideManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 创建分货任务单
	 * 
	 * @param req
	 * @return
	 * @throws ManagerException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/saveBillOmDivide")
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String, Object>> saveBillOmDivide(HttpServletRequest req, BillOmDivide divide)
			throws ManagerException, JsonParseException, JsonMappingException, IOException {

		List<BillOmDivide> oList = null;
		Map<String, Object> flag = new HashMap<String, Object>();

		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(insertedList)) {
			List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
			});
			oList = convertListWithTypeReference(mapper, list);
		}

		try {
			flag = billOmDivideManager.addBillOmDivide(oList);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error("===========创建分货任务单时异常：" + e.getMessage(), e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}
	
	/**
	 * 创建分货任务单(新)
	 * 
	 * @param req
	 * @return
	 * @throws ManagerException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value = "/saveBillOmDivideNew")
	@OperationVerify(OperationVerifyEnum.ADD)
	public ResponseEntity<Map<String, Object>> saveBillOmDivideNew(HttpServletRequest req, BillOmDivide divide)
			throws ManagerException, JsonParseException, JsonMappingException, IOException {

		List<BillOmDivide> oList = null;
		Map<String, Object> flag = new HashMap<String, Object>();

		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
		ObjectMapper mapper = new ObjectMapper();
		if (StringUtils.isNotEmpty(insertedList)) {
			List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
			});
			oList = convertListWithTypeReference(mapper, list);
		}

		try {
			flag = billOmDivideManager.addBillOmDivideNew(oList);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error("===========创建分货任务单时异常：" + e.getMessage(), e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	/**
	 * 删除分货单
	 * @param keyStr
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/deleteBillOmDivide")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public ResponseEntity<Map<String, Object>> deleteBillOmDivide(HttpServletRequest req) {
		Map<String, Object> flag = new HashMap<String, Object>();
		List<BillOmDivide> listOmDivides = null;
		try {
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				listOmDivides = convertListWithTypeReference(mapper, list);
			}
			billOmDivideManager.deleteBillOmDivide(listOmDivides);
			flag.put("flag", "success");
			flag.put("msg", "删除成功!");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error("===========删除分货任务单时异常：" + e.getMessage(), e);
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/getBatchPrintInfo")
	@ResponseBody
	public Map<String, Object> getBatchPrintInfo(HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<BillOmDivide> listOmDivides = null;
		try {
			String datas = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				listOmDivides = convertListWithTypeReference(mapper, list);
			}
			List<Map<String, Object>> list = billOmDivideManager.getBatchPrintInfo(listOmDivides);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			result.put("list", list);
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "获取打印信息失败");
			return result;
		}
	}

	/**
	 * 完结分货单
	 * @param keyStr
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateCompleteBillOmDivide")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public ResponseEntity<Map<String, Object>> updateCompleteBillOmDivide(HttpServletRequest req, BillOmDivide divide)
			throws ManagerException, JsonParseException, JsonMappingException, IOException {

		ResultVo resultVo = null;
		Map<String, Object> flag = new HashMap<String, Object>();

		//创建分货单
		try {
			if (divide != null) {
				resultVo = billOmDivideManager.modifyCompleteBillOmDivide(divide);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}

		flag.put("resultVo", resultVo);
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}

	/**
	 *  手工关闭
	 * @param locnoStrs
	 * @return
	 */
	@RequestMapping(value = "/overOmDivide")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> overOmDivide(HttpServletRequest req, BillOmDivide billOmDivide)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billOmDivide.setCreator(user.getLoginName());
			billOmDivideManager.procOmDivideOver(billOmDivide);
			obj.put("flag", "success");
			return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
		} catch (Exception e) {
			log.error("=======预到货通知单手动结案异常：" + e.getMessage(), e);
			obj.put("flag", "fail");
			obj.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
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
	private List<BillOmDivide> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<BillOmDivide> entityClass = (Class<BillOmDivide>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<BillOmDivide> tl = new ArrayList<BillOmDivide>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmDivide type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}
}