package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmDefcar;
import com.yougou.logistics.city.manager.BmDefcarManager;

/**
 * 
 * 车辆管理controller
 * 
 * @author qin.dy
 * @date 2013-9-23 下午7:07:49
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bm_defcar")
public class BmDefcarController extends BaseCrudController<BmDefcar> {
	
	@Log
    private Logger log;
	
    @Resource
    private BmDefcarManager bmDefcarManager;  

    @Override  
    public CrudInfo init() {
        return new CrudInfo("bmdefcar/",bmDefcarManager);
    }
    
    
    
    @RequestMapping(value="/delete_records")
 	@ResponseBody
 	public String deleteFefloc(String ids) throws ManagerException{
    	System.out.println(ids);
 		String m="";
 		try {
 			int count= bmDefcarManager.deleteFefloc(ids);
 			if(count>0){
 				m="success";
 			}else{
 				m="fail";
 			}
 		}catch (Exception e) {
 			log.error(e.getMessage(),e);
 			m="fail:"+e.getMessage();
         }
 	  return m;
 	}
}