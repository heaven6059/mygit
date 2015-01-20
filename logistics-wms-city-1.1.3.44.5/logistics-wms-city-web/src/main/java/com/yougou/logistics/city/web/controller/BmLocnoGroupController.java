package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmLocnoGroup;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BmLocnoGroupManager;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 仓库组别维护
 * @author zo
 * @date  2014-11-07 10:46:51
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
@RequestMapping("/bm_locno_group")
@ModuleVerify("25030260")
public class BmLocnoGroupController extends BaseCrudController<BmLocnoGroup> {
    @Resource
    private BmLocnoGroupManager bmLocnoGroupManager;
    
	@Log
    private Logger log;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bmlocnogroup/",bmLocnoGroupManager);
    }
 
    /**
     * 新增
     * @param bmLocnoGroup
     * @param req
     * @return
     * @throws ManagerException
     */
	@RequestMapping(value = "/addLocnoGroup")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addLocnoGroup(BmLocnoGroup bmLocnoGroup, HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//设置操作人
			bmLocnoGroup.setCreator(user.getLoginName());
			bmLocnoGroup.setEditor(user.getLoginName());
			bmLocnoGroup.setLocno(user.getLocNo());
			bmLocnoGroup.setCreatorname(user.getUsername());
			bmLocnoGroup.setEditorname(user.getUsername());
			obj = bmLocnoGroupManager.addLocnoGroup(bmLocnoGroup);
		}catch (ManagerException e) {
			log.error("=======新增仓库组别维护时操作异常：" + e.getMessage(), e);
			obj.put("flag", "error");
			obj.put("resultMsg", e.getMessage());
		} catch (Exception e) {
			log.error("=======新增仓库组别维护异常：" + e.getMessage(), e);
			obj.put("flag", "error");
			obj.put("returnMsg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	/**
	 * 修改
	 * @param bmLocnoGroup
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyLocnoGroup")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyLocnoGroup(BmLocnoGroup bmLocnoGroup, HttpServletRequest req)
			throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//设置操作人
			bmLocnoGroup.setEditor(user.getLoginName());
			bmLocnoGroup.setLocno(user.getLocNo());
			bmLocnoGroup.setEditorname(user.getUsername());
			obj = bmLocnoGroupManager.modifyLocnoGroup(bmLocnoGroup);
		}catch (ManagerException e) {
			log.error("=======修改仓库组别维护时操作异常：" + e.getMessage(), e);
			obj.put("flag", "error");
			obj.put("resultMsg", e.getMessage());
		} catch (Exception e) {
			log.error("=======修改仓库组别维护异常：" + e.getMessage(), e);
			obj.put("flag", "error");
			obj.put("returnMsg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
	
	
	/**
     * 删除
     * @param locnoStrs
     * @return
     */
	@RequestMapping(value="/deleteLocnoGroup")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public ResponseEntity<Map<String, Object>> deleteLocnoGroup(String locnogroupStrs) throws ManagerException{
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			
			obj = bmLocnoGroupManager.deleteLocnoGroup(locnogroupStrs);
			
		}catch (ManagerException e) {
			log.error("=======删除仓库组别维护时操作异常：" + e.getMessage(), e);
			obj.put("flag", "error");
			obj.put("resultMsg", e.getMessage());
		}catch (Exception e) {
			log.error("=======删除仓库组别维护时异常："+e.getMessage(),e);
			obj.put("flag", "error");
			obj.put("resultMsg", e.getMessage());
        }
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}
    
}