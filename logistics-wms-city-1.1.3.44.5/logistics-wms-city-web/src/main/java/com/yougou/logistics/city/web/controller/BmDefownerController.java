package com.yougou.logistics.city.web.controller;

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
import com.yougou.logistics.city.common.model.BmDefowner;
import com.yougou.logistics.city.manager.BmDefownerManager;

/**
 * 
 * 委托业主controller
 * 
 * @author qin.dy
 * @date 2013-9-22 下午2:03:59
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/entrust_owner")
@ModuleVerify("25030120")
public class BmDefownerController extends BaseCrudController<BmDefowner> {
	
    @Log
    private Logger log;
    
    @Resource
    private BmDefownerManager bmDefownerManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bmdefowner/",bmDefownerManager);
    }
    @RequestMapping(value = "/get_countValidate.json")
	public ResponseEntity<Integer> getCount(HttpServletRequest req,BmDefowner ownerNo)throws ManagerException{
    	try{
    		BmDefowner obj= this.bmDefownerManager.findById(ownerNo);
        	int total = obj==null?0:1;
    		return new ResponseEntity<Integer>(total, HttpStatus.OK);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    		return new ResponseEntity<Integer>(0, HttpStatus.NOT_FOUND);
    	}
    	
	}
    @RequestMapping(value="/delete_records")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteFefloc(String ids) throws ManagerException{
		String m="";
		try {
			int count= bmDefownerManager.deleteFefloc(ids);
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