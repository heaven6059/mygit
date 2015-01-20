package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.manager.ConContentManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 14:46:27 CST 2013
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
@RequestMapping("/con_content")
public class ConContentController extends BaseCrudController<ConContent> {
	
	@Log
	private Logger log;
	
    @Resource
    private ConContentManager conContentManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("conContent/",conContentManager);
    }
    
    @RequestMapping(value = "/findConContentByPage.json")
	@ResponseBody
	public Map<String, Object> findConContentByPage(HttpServletRequest req, ConContentDto conContentDto) throws ManagerException {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try{
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			if(conContentDto.getItemNo() != null) {
				conContentDto.setItemNo(conContentDto.getItemNo().toUpperCase());
			}
			if(conContentDto.getBarcode() != null) {
				conContentDto.setBarcode(conContentDto.getBarcode().toUpperCase());
			}
    		int total = conContentManager.findCountMx(conContentDto,authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<ConContentDto> list = conContentManager.findConContentByPage(page, conContentDto,authorityParams);
    		
    		obj.put("total", total);
    		obj.put("rows", list);
    	}catch(Exception e){
    		log.error(e.getMessage(), e);
    	}
		return obj;
	}
}