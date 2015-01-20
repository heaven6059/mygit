package com.yougou.logistics.city.web.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
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
import com.yougou.logistics.city.common.model.BmCircle;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BmCircleManager;

@Controller
@RequestMapping("/bmcircle")
@ModuleVerify("25030030")
public class BmCircleController extends BaseCrudController<BmCircle> {
    
	@Log
    private Logger log;
	
	@Resource
    private BmCircleManager bmCircleManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("bmcircle/",bmCircleManager);
    }
    /**
	 * 新增商圈
	 * @param bmDefloc
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/addCircle")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String addDefloc(BmCircle bmCircle,HttpServletRequest req)throws ManagerException {
		String m="";
		try {
			//获取登陆用户
			HttpSession session = req.getSession();
		    SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		    //0-手建 ;1-系统下发
		    bmCircle.setCreateFlag("0");//默认为手建
		    bmCircle.setCreator(user.getLoginName());
		    //设置创建时间
		    bmCircle.setCreatetm(new Date());
		    bmCircle.setEditor(user.getLoginName());
		    //设置修改时间
		    bmCircle.setEdittm(new Date());
			int a = bmCircleManager.add(bmCircle);
			if(a > 0 ){
				m="success";
			}else{
				m="fail";
			}
		}catch (Exception e) {
			log.error("=======新增商圈异常："+e.getMessage(),e);
			m="fail:"+e.getMessage();
            //throw new ManagerException(e);
        }
		 return m;
	}
	
	/**
	 * 修改商圈
	 * @param bmDefloc
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/modifyCircle")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public String modifyFloc(BmCircle bmCircle, HttpServletRequest req)throws ManagerException {
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			bmCircle.setEdittm(new Date());
			bmCircle.setEditor(user.getLoginName());
			if (bmCircleManager.modifyById(bmCircle) > 0) {
				return "success";
			} else {
				return "fail";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}
	
	/**
     * 校验商圈下是否有绑定客户时异常
     * @param locnoStrs
     * @return
     */
	@RequestMapping(value="/findIsStore")
	@ResponseBody
	public String findIsLocUser(String circleNoStrs) throws ManagerException{
		String m="";
		try {
			boolean flag = bmCircleManager.findIsStore(circleNoStrs);
			if(!flag){
				m="warn";
			}else{
				m="success";
			}
		}catch (Exception e) {
			log.error("=======删除商圈异常："+e.getMessage(),e);
			m = "fail";
            //throw new ManagerException(e);
        }
	    return m;
	}
	/**
     * 删除商圈
     * @param locnoStrs
     * @return
     */
	@RequestMapping(value="/deleteCircle")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteCircle(String circleNoStrs) throws ManagerException{
		String m="";
		try {
			int count= bmCircleManager.deleteCircle(circleNoStrs);
			if(count>0){
				m="success";
			}else{
				m="fail";
			}
		}catch (Exception e) {
			log.error("=======删除商圈异常："+e.getMessage(),e);
			m="fail:"+e.getMessage();
            //throw new ManagerException(e);
        }
	    return m;
	}
}