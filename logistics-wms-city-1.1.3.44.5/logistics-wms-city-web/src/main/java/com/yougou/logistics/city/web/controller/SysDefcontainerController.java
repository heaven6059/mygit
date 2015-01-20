package com.yougou.logistics.city.web.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.yougou.logistics.city.common.model.SysDefcontainer;
import com.yougou.logistics.city.common.model.SysDefcontainerKey;
import com.yougou.logistics.city.manager.SysDefcontainerManager;

/**
 * 容器资料controller
 * 
 * @author qin.dy
 * @date 2013-9-22 下午3:04:11
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/sys_defcontainer")
@ModuleVerify("25030040")
public class SysDefcontainerController extends BaseCrudController<SysDefcontainer> {
	@Log
	private Logger log;
    @Resource
    private SysDefcontainerManager sysDefcontainerManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("sysdefcontainer/",sysDefcontainerManager);
    }
    @RequestMapping(value = "/saveSysDefcontainer")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.ADD)
	public Boolean saveSysDefcontainer(SysDefcontainer type) throws ManagerException {
    	try {
    		type.setCreatetm(new Date());
    		type.setEdittm(new Date());
			sysDefcontainerManager.add(type);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
    @RequestMapping(value = "/moditfySysDefcontainer")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.MODIFY)
	public String moditfySysDefcontainer(SysDefcontainer type) throws ManagerException {
    	try {
    		type.setEdittm(new Date());
    		sysDefcontainerManager.modifyById(type);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
		return "success";
	}
    @RequestMapping(value = "/get_countValidate.json")
   	public ResponseEntity<Integer> getCount(HttpServletRequest req,SysDefcontainerKey sysDefcontainerKey)throws ManagerException{
    	SysDefcontainerKey obj= this.sysDefcontainerManager.findById(sysDefcontainerKey);
       	int total = obj==null?0:1;
   		return new ResponseEntity<Integer>(total, HttpStatus.OK);
   	}
    @RequestMapping(value="/delete_records")
 	@ResponseBody
 	@OperationVerify(OperationVerifyEnum.REMOVE)
 	public String deleteFefloc(String ids) throws ManagerException{
 		String m="";
 		try {
 			int count= sysDefcontainerManager.deleteFefloc(ids);
 			if(count>0){
 				m="success";
 			}else{
 				m="fail";
 			}
 		}catch (Exception e) {
 			log.error(e.getMessage(), e);
 			m="fail:"+e.getMessage();
         }
 	  return m;
 	}
}