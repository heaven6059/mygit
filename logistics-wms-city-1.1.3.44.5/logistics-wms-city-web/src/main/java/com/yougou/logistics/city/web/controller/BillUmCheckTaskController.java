package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.yougou.logistics.city.common.model.BillUmCheckTask;
import com.yougou.logistics.city.common.model.BillUmCheckTaskKey;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillUmCheckTaskManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 退仓验收任务
 * @author su.yq
 * @date  Tue Jul 08 18:01:46 CST 2014
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
@RequestMapping("/bill_um_check_task")
@ModuleVerify("25060120")
public class BillUmCheckTaskController extends BaseCrudController<BillUmCheckTask> {
	
	private static final String STATUS10 = "10";
	@Log
	private Logger log;
	
    @Resource
    private BillUmCheckTaskManager billUmCheckTaskManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billumchecktask/",billUmCheckTaskManager);
    }
    
   
    @RequestMapping(value = "/list.json")
    @ResponseBody
    public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException{
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
            int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
            int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
            String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
            String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
            
            Map<String, Object> params = builderParams(req, model);
            int total = this.billUmCheckTaskManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
            SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
            List<BillUmCheckTask> list = this.billUmCheckTaskManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
            //小计
            Map<String, Object> footer = new HashMap<String, Object>();
            List<Object> footerList = new ArrayList<Object>();
            BigDecimal totalItemQty = new BigDecimal(0);
            BigDecimal totalCheckQty = new BigDecimal(0);
            for(BillUmCheckTask t : list){
                totalItemQty = totalItemQty.add(t.getItemQty());
                totalCheckQty = totalCheckQty.add(t.getCheckQty());
            }
            footer.put("checkTaskNo", "小计");
            footer.put("itemQty", totalItemQty);
            footer.put("checkQty", totalCheckQty);
            footerList.add(footer);
            //合计
            Map<String, Object> sumFoot = new HashMap<String, Object>();
            if (pageNo == 1) {
                sumFoot = this.billUmCheckTaskManager.selectSumQty(params, authorityParams);
                if (sumFoot == null) {
                    sumFoot = new SumUtilMap<String, Object>();
                    sumFoot.put("item_qty", 0);
                    sumFoot.put("check_qty", 0);
                }
                sumFoot.put("isselectsum", true);
                sumFoot.put("check_task_no", "合计");
            } else {
                sumFoot.put("checkTaskNo", "合计");
            }
            footerList.add(sumFoot);
            obj.put("total", total);
            obj.put("rows", list);
            obj.put("footer", footerList);
            obj.put("result", "success");
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            obj.put("rows", "");
            obj.put("result", "fail");
            obj.put("msg", e.getCause().getMessage());
        }
        return obj;
    }
    
    
    
    /**
     * 新增退仓验收任务信息
     * @param req
     * @param session
     * @return
     * @throws ManagerException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/saveUmCheckTask")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public Map<String, Object> saveUmCheckTask(HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BillUmUntread> untreadList = new ArrayList<BillUmUntread>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				untreadList = convertListWithTypeReference(mapper, list);
			}
			
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			BillUmCheckTask checkTask = new BillUmCheckTask();
			checkTask.setLocno(user.getLocNo());
			checkTask.setCreator(user.getLoginName());
			checkTask.setCreatorName(user.getUsername());
			checkTask.setCreatetm(new Date());
			billUmCheckTaskManager.saveUmCheckTask(checkTask, untreadList);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
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
    @RequestMapping(value = "/updateUmCheckTask")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public Map<String, Object> updateUmCheckTask(BillUmCheckTask checkTask, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			checkTask.setEditor(user.getLoginName());
			checkTask.setEditorName(user.getUsername());
			checkTask.setEdittm(new Date());
			BillUmCheckTaskKey billUmCheckTaskKey = new BillUmCheckTaskKey();
			billUmCheckTaskKey.setCheckTaskNo(checkTask.getCheckTaskNo());
			billUmCheckTaskKey.setLocno(checkTask.getLocno());
			billUmCheckTaskKey.setOwnerNo(checkTask.getOwnerNo());
			BillUmCheckTask billUmCheckTask = (BillUmCheckTask) billUmCheckTaskManager.findById(billUmCheckTaskKey);
			if (null == billUmCheckTask){
				map.put("result", "notExits");
				map.put("msg", "单据: " + checkTask.getCheckTaskNo() +"不存在");
				return map;
			}
			if(!STATUS10.equals(billUmCheckTask.getStatus())){
				map.put("result", "notExits");
				map.put("msg", "单据: " + checkTask.getCheckTaskNo() +"状态发生了变化，不能进行当前操作");
				return map;
			}
			int i = billUmCheckTaskManager.modifyById(checkTask);
			if(i < 1){
				map.put("result", ResultEnums.FAIL.getResultMsg());
				map.put("msg", "修改失败!");
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
    @RequestMapping(value = "/delUmCheckTask")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String, Object> delUmCheckTask(HttpServletRequest req, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BillUmCheckTask> taskList = new ArrayList<BillUmCheckTask>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				taskList = convertListWithTypeReferenceTask(mapper, list);
			}
			
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmCheckTaskManager.deleteUmCheckTask(taskList);
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("result", ResultEnums.FAIL.getResultMsg());
			map.put("msg", e.getMessage());
		}
		return map;
	}
    
    
	// 审核
	@RequestMapping(value = "/auditUmCheckTask")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> auditUmCheckTask(String keyStr, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			billUmCheckTaskManager.auditUmCheckTask(keyStr, user.getLoginName(), user.getUsername(),user.getLocNo());
			map.put("result", ResultEnums.SUCCESS.getResultMsg());
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("msg", e.getMessage());
			map.put("result", ResultEnums.FAIL.getResultMsg());
		}
		return map;
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
    private List<BillUmUntread> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmUntread> tl = new ArrayList<BillUmUntread>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmUntread type = mapper
					.readValue(mapper.writeValueAsString(list.get(i)), BillUmUntread.class);
			tl.add(type);
		}
		return tl;
	}
    
    private List<BillUmCheckTask> convertListWithTypeReferenceTask(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillUmCheckTask> tl = new ArrayList<BillUmCheckTask>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillUmCheckTask type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillUmCheckTask.class);
			tl.add(type);
		}
		return tl;
	}
	
}