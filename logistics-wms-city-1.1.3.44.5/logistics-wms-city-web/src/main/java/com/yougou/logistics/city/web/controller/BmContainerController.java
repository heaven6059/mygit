package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.yougou.logistics.city.common.model.AccInventoryCon;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.AccInventoryConManager;
import com.yougou.logistics.city.manager.BmContainerManager;

/**
 * 容器管理
 * @author wanghb
 * @date   2014-8-6
 * @version 1.1.3.37
 */
@Controller
@RequestMapping("/bmContainer")
@ModuleVerify("25030040")
public class BmContainerController extends BaseCrudController<BmContainer> {
	@Log
	private Logger log;
    @Resource
    private BmContainerManager bmContainerManager;
    @Resource
    private AccInventoryConManager accInventoryConManager;
    @Override
    public CrudInfo init() {
        return new CrudInfo("bmcontainer/",bmContainerManager);
    }
    @RequestMapping(value = "/saveBmContainer")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.ADD)
	public Object saveBmContainer(HttpServletRequest req,BmContainer type) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	String result = "";
    	String msg="";
    	try {
    		type.setCreatetm(new Date());
    		type.setEdittm(new Date());
    		//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//设置操作人
			type.setCreator(user.getLoginName());
			type.setEditor(user.getLoginName());
			type.setLocno(user.getLocNo());
			bmContainerManager.addEntity(type);
			result = "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = "error";
			msg=e.getMessage();
		}
    	obj.put("result", result);
    	obj.put("msg", msg);
    	return obj;
	}
    @RequestMapping(value = "/moditfyBmContainer")
    @ResponseBody
    @OperationVerify(OperationVerifyEnum.MODIFY)
	public String moditfyBmContainer(HttpServletRequest req,BmContainer type) throws ManagerException {
    	try {
    		HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
    		type.setEdittm(new Date());
    		type.setEditor(user.getLoginName());
    		BmContainer bm=bmContainerManager.findById(type);
    		if(null!=bm && bm.getStatus().equals("0")){
    			AccInventoryCon con=new AccInventoryCon();
				con.setLocno(user.getLocNo());
				con.setConNo(type.getConNo());
				AccInventoryCon temp=accInventoryConManager.findById(con);
				if(null!=temp){
					int r=temp.getSkuQty().compareTo(BigDecimal.ZERO);
					int r1=temp.getChildrenQty().compareTo(BigDecimal.ZERO);
					if(r==1||r1==1){
						return "fail";
					}
				}
    			bmContainerManager.modifyById(type);
    		}else{
    			return "fail";
    		}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
		return "success";
	}
    @RequestMapping(value = "/get_countValidate.json")
   	public ResponseEntity<Integer> getCount(HttpServletRequest req,BmContainer bmContainer)throws ManagerException{
    	BmContainer obj= bmContainerManager.findById(bmContainer);
       	int total = obj==null?0:1;
       	
   		return new ResponseEntity<Integer>(total, HttpStatus.OK);
   	}
    @RequestMapping(value="/delete_records")
 	@ResponseBody
 	@OperationVerify(OperationVerifyEnum.REMOVE)
 	public String deleteFefloc(HttpServletRequest req,String ids) throws ManagerException{
 		String m="";
 		try {
 			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			String [] strs = ids.split(",");
   			for(String str : strs){
   				String[] id = str.split("-");
   				if(id!=null && id.length==2){
   					BmContainer bmContainer =new BmContainer();
   					bmContainer.setConNo(id[1]);
   					bmContainer.setLocno(id[0]);
   					BmContainer bm=bmContainerManager.findById(bmContainer);
   					if(null!=bm.getOptBillNo()||bm.getStatus().equals("1")){
   						return "1:"+id[1];
   					}else{
   						AccInventoryCon con=new AccInventoryCon();
   						con.setLocno(id[0]);
   						con.setConNo(id[1]);
   						AccInventoryCon temp=accInventoryConManager.findById(con);
   						if(null!=temp){
   							int r=temp.getSkuQty().compareTo(BigDecimal.ZERO);
   							int r1=temp.getChildrenQty().compareTo(BigDecimal.ZERO);
   							if(r==1||r1==1){
   	   							return "2:"+id[1];
   							}
   						}
   					}
					
   				}
   			}
 			int count= bmContainerManager.deleteFefloc(ids,user);
 			if(count>0){
 				m="success";
 			}else{
 				m="fail";
 			}
 		}catch (ManagerException e) {
 			log.error(e.getMessage(), e);
 			m="2";
         }
 	  return m;
 	}
}