package com.yougou.logistics.city.web.controller;

import java.io.IOException;
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
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillConConvertGoodsManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
@RequestMapping("/bill_con_convert_goods")
@ModuleVerify("25110060")
public class BillConConvertGoodsController extends BaseCrudController<BillConConvertGoods> {
	
	@Log
	private Logger log;
	
	@Resource
	private ProcCommonManager  procCommonManager;
	
    @Resource
    private BillConConvertGoodsManager billConConvertGoodsManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billconconvertgoods/",billConConvertGoodsManager);
    }
    
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
			int total = this.billConConvertGoodsManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillConConvertGoods> list = this.billConConvertGoodsManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
//			List<AuthorityUserOrganizationDto> userList = null;
//			Map<String, Object> mapLocno = new HashMap<String, Object>();
//			Object u = req.getSession().getAttribute(PublicContains.SESSION_USER);
//			Object systemId = req.getSession().getAttribute(PublicContains.SESSION_SYSTEMID);
//			Object areaSystemId = req.getSession().getAttribute(PublicContains.SESSION_AREASYSTEMID);
//			SystemUser user;
//			if(u != null && systemId != null && areaSystemId != null){
//				user = (SystemUser)u;
//				Map<String,Object> paramsLocno = new HashMap<String, Object>();
//				paramsLocno.put("userId", user.getUserid());
//				paramsLocno.put("organizationType", 22);
//				paramsLocno.put("systemId", systemId);
//				paramsLocno.put("areaSystemId", areaSystemId);
//				paramsLocno.put("status", 1);
//				userList = authorityUserOrganizationManager.findUserOrganizationByParams(paramsLocno);
//				for (AuthorityUserOrganizationDto dto : userList) {
//					if(mapLocno.get(dto.getOrganizationNo())==null){
//						mapLocno.put(dto.getOrganizationNo(), dto.getOrganizationName());
//					}
//				}
//			}
//			
//			for (BillConConvertGoods g : list) {
//				if("1".equals(g.getConvertType())){
//					String locnoName = "";
//					if(mapLocno.get(g.getdLocno())!=null){
//						locnoName = (String)mapLocno.get(g.getdLocno());
//						g.setLocnoName(locnoName);
//					}
//				}
//			}
			
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
     * 新增库存转货单主档
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/saveMain")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveMain(HttpServletRequest req, HttpSession session,BillConConvertGoods convertGoods) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			Date date = new Date();
			String convertGoodsNo = procCommonManager.procGetSheetNo(user.getLocNo(), CNumPre.CON_CONVERT_GOODS_PRE);
			convertGoods.setLocno(user.getLocNo());
			convertGoods.setConvertGoodsNo(convertGoodsNo);
			convertGoods.setCreator(user.getLoginName());
			convertGoods.setCreatorName(user.getUsername());
			convertGoods.setCreatetm(date);
			convertGoods.setEditor(user.getLoginName());
			convertGoods.setEditorName(user.getUsername());
			convertGoods.setEdittm(date);
			if("0".equals(convertGoods.getConvertType())){
				convertGoods.setStoreNo("");
			}
			if ("2".equals(convertGoods.getConvertType()) || "1".equals(convertGoods.getConvertType())) {
				if ("1".equals(convertGoods.getConvertType())) { //转部门转货的，传的仓库值放到新增的目的仓库字段中
					convertGoods.setdLocno(convertGoods.getStoreNoLocno());
				}
				convertGoods.setsQuality("");
				convertGoods.setdQuality("");
			}
			billConConvertGoodsManager.add(convertGoods);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
			map.put("convertGoodsNo", convertGoodsNo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
    
    /**
     * 修改退仓验收任务信息
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/updateMain")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> updateMain(BillConConvertGoods convertGoods, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			convertGoods.setEditor(user.getLoginName());
			convertGoods.setEditorName(user.getUsername());
			convertGoods.setEdittm(new Date());
			convertGoods.setUpdStatus("10");
			int i = billConConvertGoodsManager.modifyById(convertGoods);
			if(i < 1){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "单据"+convertGoods.getConvertGoodsNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
			}else{
				map.put("result", ResultEnums.SUCCESS.getResultMsg());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
    
    /**
     * 删除退仓验收任务信息
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/delMain")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> delMain(HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BillConConvertGoods> goodsList = new ArrayList<BillConConvertGoods>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				goodsList = convertListWithTypeReference(mapper, list);
			}
			billConConvertGoodsManager.deleteConvertGoods(goodsList);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
    
    // 审核
 	@RequestMapping(value = "/auditConvertGoods")
 	@OperationVerify(OperationVerifyEnum.VERIFY)
 	@ResponseBody
 	public Map<String, Object> auditConvertGoods(HttpServletRequest req, HttpSession session) {
 		Map<String, Object> map = new HashMap<String, Object>();
 		try {
 			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
 			List<BillConConvertGoods> goodsList = new ArrayList<BillConConvertGoods>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				goodsList = convertListWithTypeReference(mapper, list);
			}
 			billConConvertGoodsManager.auditConvertGoods(user.getLoginName(), user.getUsername(), goodsList);
 			map.put("result", ResultEnums.SUCCESS.getResultMsg());
 		} catch (Exception e) {
 			log.error(e.getMessage(), e);
 			map.put("msg", e.getMessage());
 			map.put("result", ResultEnums.FAIL.getResultMsg());
 		}
 		return map;
 	}
    
    private List<BillConConvertGoods> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillConConvertGoods> tl = new ArrayList<BillConConvertGoods>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillConConvertGoods type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillConConvertGoods.class);
			tl.add(type);
		}
		return tl;
	}
}