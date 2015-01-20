package com.yougou.logistics.city.web.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BmDefloc;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BmDeflocManager;

/**
 * 仓别维护
 * @author zuo.sw
 *
 */
@Controller
@RequestMapping("/bmdefloc")
@ModuleVerify("25030170")
public class BmDeflocController extends BaseCrudController<BmDefloc> {
	
    @Log
    private Logger log;
	
    @Resource
    private BmDeflocManager bmDeflocManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bmdefloc/",bmDeflocManager);
    }
    
    /**
     * 删除仓别
     * @param locnoStrs
     * @return
     */
	@RequestMapping(value="/deleteFefloc")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteFefloc(String locnoStrs) throws ManagerException{
		String m="";
		try {
			int count= bmDeflocManager.deleteFefloc(locnoStrs);
			if(count>0){
				m="success";
			}else{
				m="fail";
			}
		}catch (Exception e) {
			log.error("=======删除仓别异常："+e.getMessage(),e);
			m="fail:"+e.getMessage();
            //throw new ManagerException(e);
        }
	    return m;
	}
	
	
    /**
     * 校验仓别下是否有绑定用户时异常
     * @param locnoStrs
     * @return
     */
	@RequestMapping(value="/findIsLocUser")
	@ResponseBody
	public String findIsLocUser(String locnoStrs) throws ManagerException{
		String m="";
		try {
			boolean flag = bmDeflocManager.findIsLocUser(locnoStrs);
			if(!flag){
				m="warn";
			}else{
				m="success";
			}
		}catch (Exception e) {
			log.error("=======删除仓别异常："+e.getMessage(),e);
			m = "fail";
            //throw new ManagerException(e);
        }
	    return m;
	}
	
	/**
	 * 新增仓别
	 * @param bmDefloc
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/addDefloc")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addDefloc(BmDefloc bmDefloc,HttpServletRequest req)throws ManagerException {
		String m="";
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		    //0-手建 ;1-系统下发
		    bmDefloc.setCreateFlag("0");//默认为手建
		    bmDefloc.setCreator(user.getLoginName());
		    //设置创建时间
		    bmDefloc.setCreatetm(new Date());
		    bmDefloc.setEditor(user.getLoginName());
		    //设置修改时间
		    bmDefloc.setEdittm(new Date());
			int a = bmDeflocManager.add(bmDefloc);
			if(a > 0 ){
				m="success";
			}else{
				m="fail";
			}
		}catch (Exception e) {
			log.error("=======新增仓别异常："+e.getMessage(),e);
			m="fail:"+e.getMessage();
            //throw new ManagerException(e);
        }
		 return m;
	}
	
	/**
	 * 修改仓别
	 * @param bmDefloc
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyFloc")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String modifyFloc(BmDefloc bmDefloc, HttpServletRequest req)throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmDefloc.setEdittm(new Date());
			bmDefloc.setEditor(user.getLoginName());
			if (bmDeflocManager.modifyById(bmDefloc) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
	
	
	@RequestMapping(value = "/getCountValidate")
	public ResponseEntity<Integer> getCountValidate(BmDefloc bmDefloc)throws ManagerException {
		try{
			BmDefloc obj= this.bmDeflocManager.findById(bmDefloc);
	    	int total = obj==null?0:1;
			return new ResponseEntity<Integer>(total, HttpStatus.OK);
		}catch(Exception e){
			log.error(e.getMessage(), e);
			return new ResponseEntity<Integer>(0, HttpStatus.NOT_FOUND);
		}
		
	}
	
 
	
}