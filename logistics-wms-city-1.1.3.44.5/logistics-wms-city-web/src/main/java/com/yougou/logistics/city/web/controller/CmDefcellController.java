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
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.CmDefAreaAttributeTypeEnums;
import com.yougou.logistics.city.common.enums.CmDefCellMixFlagEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCellStatusEnums;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.manager.CmDefareaManager;
import com.yougou.logistics.city.manager.CmDefcellManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 储位Controller
 * 
 * @author qin.dy
 * @date 2013-9-25 下午4:45:14
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/cm_defcell")
@ModuleVerify("25020020")
public class CmDefcellController extends BaseCrudController<CmDefcell> {

	@Log
	private Logger log;

	@Resource
	private CmDefcellManager cmDefcellManager;

	@Resource
	private CmDefareaManager cmDefareaManager;
	
	@Override
	public CrudInfo init() {
		return new CrudInfo("cmdefcell/", cmDefcellManager);
	}

	@RequestMapping(value = "/add_post")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public Object addCell(CmDefcell type) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String result = "";
		boolean pass = true;
		try {
			String locno = type.getLocno();
			String wareNo = type.getWareNo();
			String areaNo = type.getAreaNo();
			if(StringUtils.isBlank(locno)||
					StringUtils.isBlank(wareNo)||
					StringUtils.isBlank(areaNo)){
				result = "缺少库区参数";
				pass = false;
			}else{
				CmDefarea cmDefarea = new CmDefarea();
				cmDefarea.setLocno(locno);
				cmDefarea.setWareNo(wareNo);
				cmDefarea.setAreaNo(areaNo);
				cmDefarea = cmDefareaManager.findById(cmDefarea);
				if(cmDefarea == null){
					result = "没有找到对应库区";
					pass = false;
				}else{
					if(CmDefAreaAttributeTypeEnums.ATTRIBUTETYPE_0.getAttributeType().equals(cmDefarea.getAttributeType())){
						if(CmDefCellMixFlagEnums.MIXFLAG_1.getMixFlag().equals(String.valueOf(type.getMixFlag()))){
							result = "存储区储位混载标示不能为【同商品不同属性混】";
							pass = false;
						}
					}
				}
			}
			if(pass){
				Date date = new Date();
				type.setCreatetm(date);
				type.setEdittm(date);
				cmDefcellManager.add(type);
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

	@RequestMapping(value = "/add_put")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Object modifyCmDefcell(CmDefcell type) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String result = "";
		boolean pass = true;
		try {
			String locno = type.getLocno();
			String wareNo = type.getWareNo();
			String areaNo = type.getAreaNo();
			if(StringUtils.isBlank(locno)||
					StringUtils.isBlank(wareNo)||
					StringUtils.isBlank(areaNo)){
				result = "缺少库区参数";
				pass = false;
			}else{
				CmDefarea cmDefarea = new CmDefarea();
				cmDefarea.setLocno(locno);
				cmDefarea.setWareNo(wareNo);
				cmDefarea.setAreaNo(areaNo);
				cmDefarea = cmDefareaManager.findById(cmDefarea);
				if(cmDefarea == null){
					result = "没有找到对应库区";
					pass = false;
				}else{
					if(CmDefAreaAttributeTypeEnums.ATTRIBUTETYPE_0.getAttributeType().equals(cmDefarea.getAttributeType())){
						if(CmDefCellMixFlagEnums.MIXFLAG_1.getMixFlag().equals(String.valueOf(type.getMixFlag()))){
							result = "存储区储位混载标示不能为【同商品不同属性混】";
							pass = false;
						}
					}
				}
			}
			if(pass){
				//type.setCreatetm(new Date());
				cmDefcellManager.modifyCmDefcell(type);
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

	
	@RequestMapping(value = "/disableCell")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Object disableCell(HttpServletRequest req,HttpSession session) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String keys = req.getParameter("keys");
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			
			cmDefcellManager.batchModifyStatus(keys, CmDefcellCellStatusEnums.CELLSTATUS_1.getStatus(),user);
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
	 * 储位解禁
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/enableCell")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Object enableCell(HttpServletRequest req,HttpSession session) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			String keys = req.getParameter("keys");
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			
			cmDefcellManager.batchModifyStatus(keys, CmDefcellCellStatusEnums.CELLSTATUS_0.getStatus(),user);
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
	@RequestMapping(value = "/delete_records")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public ResponseEntity<Map<String, Object>> deleteRecords(String datas, String locno) {
		Map<String, Object> flag = new HashMap<String, Object>();
		List<CmDefcell> listCmDefcells = new ArrayList<CmDefcell>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>() {
				});
				listCmDefcells = convertListWithTypeReference(mapper, list);
			}

			flag = cmDefcellManager.deleteBatch(locno, listCmDefcells);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (ManagerException e) {
			flag.put("flag", "fail");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			flag.put("flag", "fail");
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}

	/**
	 * 分货储位查询
	 * @param defcell
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findCmDefcellByArea")
	@ResponseBody
	public List<CmDefcell> findCmDefcellByArea(CmDefcell defcell, HttpServletRequest req) {
		List<CmDefcell> list = null;
		try {
			list = this.cmDefcellManager.findCmDefcellByArea(defcell);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 允许整储位调整的储位查询
	 * @param defcell
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findCmDefcell4Adj")
	@ResponseBody
	public Map<String, Object> findCmDefcell4Adj(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = cmDefcellManager.findCmDefcell4AdjCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<CmDefcell> list = cmDefcellManager.findCmDefcell4Adj(page, params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 允许盘点的储位查询
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findCmDefcell4Plan")
	@ResponseBody
	public Map<String, Object> findCmDefcell4Plan(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = cmDefcellManager.findCmDefcell4PlanCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<CmDefcell> list = cmDefcellManager.findCmDefcell4Plan(page, params);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 根据库区或通道查询允许盘点的储位
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findCmDefcell4PlanByAS")
	@ResponseBody
	public Map<String, Object> findCmDefcell4PlanByAS(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			Map<String, Object> params = builderParams(req, model);
			List<CmDefcell> list = cmDefcellManager.findCmDefcell4Plan(null, params);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 检查当前库存是否存在
	 * @param osCust
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/checkCell")
	@ResponseBody
	public Map<String, Object> checkStock(CmDefcell defcell, HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (defcell.getCellStatus().equals("1")) {
				int count = cmDefcellManager.queryContent(defcell);
				if (count < 1) {
					result.put("result", "success");
				} else {
					result.put("result", "exist");
				}
			}
		} catch (ManagerException e) {
			result.put("result", "fail");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			result.put("result", "fail");
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 打印储位
	 * @param req
	 * @param session
	 * @param keys
	 * @return
	 */
	@RequestMapping(value = "/printCell")
	@ResponseBody
	public Map<String, Object> printCell(HttpServletRequest req, HttpSession session, String keys) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			result.put("data", resultList);
			if (StringUtils.isEmpty(keys)) {
				result.put("result", "error");
				result.put("msg", "参数错误");
				return result;
			}

			String[] keysArray = keys.split(",");
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			for (String str : keysArray) {

				String[] strs = str.split("\\|");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", user.getLocNo());
				params.put("cellNo", strs[1]);
				List<CmDefcell> listCells = cmDefcellManager.findByBiz(null, params);
				if (!CommonUtil.hasValue(listCells)) {
					result.put("result", "error");
					result.put("msg", "没有查询到明细");
				}

				Map<String, Object> resultObj = new HashMap<String, Object>();
				resultObj.put("result", "success");
				resultObj.put("rows", listCells);
				resultList.add(resultObj);
			}
			result.put("result", "success");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", "error");
			result.put("msg", "系统异常请联系管理员");
		}
		return result;
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
	private List<CmDefcell> convertListWithTypeReference(ObjectMapper mapper, List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<CmDefcell> tl = new ArrayList<CmDefcell>(list.size());
		Class<CmDefcell> entityClass = (Class<CmDefcell>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		for (int i = 0; i < list.size(); i++) {
			CmDefcell type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/all_list.json")
	@ResponseBody
	public Map<String, Object> queryAllList(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.cmDefcellManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, total, (int) total);
			List<CmDefcell> list = this.cmDefcellManager.findByPage(page, sortColumn, sortOrder, params);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	@RequestMapping(value = "/OwnerNoByStockNo")
	@ResponseBody
	public Object queryOwnerNoByStock(CmDefcell cmDefcell) {
		String ownerNo = "";
		try {
			ownerNo = cmDefcellManager.findOwnerByStock(cmDefcell);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ownerNo;
	}
	
	@RequestMapping(value = "/findSimple")
	@ResponseBody
	public List<CmDefcellSimple> findSimple(HttpServletRequest req, Model model) throws ManagerException {
		List<CmDefcellSimple> list = new ArrayList<CmDefcellSimple>();
		try {
			Map<String, Object> params = builderParams(req, model);
			list = this.cmDefcellManager.findSimple(params);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			/*if(StringUtils.isNotBlank(sortColumn) && sortColumn.equals("showCellStatus")){
				sortColumn = "CELL_STATUS";
			}*/
			Map<String, Object> params = builderParams(req, model);
			int total = this.cmDefcellManager.findCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<CmDefcell> list = this.cmDefcellManager.findByPage(page, sortColumn, sortOrder, params);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return obj;
	}
}