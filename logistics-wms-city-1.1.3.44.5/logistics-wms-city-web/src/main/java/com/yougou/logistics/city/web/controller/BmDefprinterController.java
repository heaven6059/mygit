package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmDefprinter;
import com.yougou.logistics.city.manager.BmDefprinterManager;

/**
 * 
 * 打印机controller
 * 
 * @author qin.dy
 * @date 2013-11-1 下午2:39:13
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bm_defprinter")
@ModuleVerify("25030190")
public class BmDefprinterController extends BaseCrudController<BmDefprinter> {
    @Resource
    private BmDefprinterManager bmDefprinterManager;
    
    @Log
    private Logger log;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bmdefprinter/",bmDefprinterManager);
    }
    @RequestMapping(value="/delete_records")
 	@ResponseBody
 	@OperationVerify(OperationVerifyEnum.REMOVE)
 	public String deleteRecords(String ids){
 		String m="";
 		try {
 			int count= bmDefprinterManager.deleteBatch(ids);
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