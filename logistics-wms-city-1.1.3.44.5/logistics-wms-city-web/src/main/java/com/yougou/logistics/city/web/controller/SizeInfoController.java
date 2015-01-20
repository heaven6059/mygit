package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ColorInfo;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/size_info")
@ModuleVerify("25030090")
public class SizeInfoController extends BaseCrudController<SizeInfo> {
	@Log
	private Logger log;
    @Resource
    private SizeInfoManager sizeInfoManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("sizeinfo/",sizeInfoManager);
    }
    
    
    @RequestMapping(value = "/list.json")
  	@ResponseBody
  	public  Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
      	Map<String, Object> obj = new HashMap<String, Object>();
      	try {
      		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
      		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
      		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
      		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
      		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
      		Map<String, Object> params = builderParams(req, model);
      		int total = sizeInfoManager.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
      		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
      		List<ColorInfo> list = sizeInfoManager.findByPage(page, sortColumn, sortOrder, params,authorityParams);    		
      		obj.put("total", total);
      		obj.put("rows", list);
  		} catch (Exception e) {
  			log.error(e.getMessage(), e);
  		}
  		
  		return obj;
  	}
}