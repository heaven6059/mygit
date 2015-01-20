package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.city.common.dto.AuthorityResourcesDTO;
import com.yougou.logistics.city.common.model.AuthorityResources;
import com.yougou.logistics.city.manager.AuthorityResourcesManager;

@Controller
public class AuthorityController {
	
	@Autowired
	private AuthorityResourcesManager authorityResourcesManager;
	  
	@RequestMapping(value="/toResourceManager")
	public String toUserManager(){
		
		return "systemsetting/add_resource";
	}
	
	@RequestMapping(value="/queryMenuTree")
	public ModelAndView queryResourceTree() throws ManagerException{
		ModelAndView mav = new ModelAndView("jsonView");
		AuthorityResourcesDTO dto=this.authorityResourcesManager.queryAllAuthorityResources(1l);
		List<AuthorityResourcesDTO> list=new ArrayList<AuthorityResourcesDTO>();
		list.add(dto);
		mav.addObject(list);
		return mav;
	}
 
	@RequestMapping(value="/queryResourceById")
	public ModelAndView queryResourceById(@RequestParam("id")Long id) throws ManagerException{
		ModelAndView mav = new ModelAndView("jsonView");
		AuthorityResourcesDTO dto=this.authorityResourcesManager.queryResourceById(id);
		mav.addObject(dto);
		return mav;
	}
	
	@RequestMapping(value="/addResource")
	public ResponseEntity<AuthorityResourcesDTO> addResource(AuthorityResources resource) throws ManagerException{
		    resource.setMenuUrl((resource.getTempMenuUrl()!=null?resource.getTempMenuUrl():resource.getMenuUrl()));
			AuthorityResourcesDTO dto=this.authorityResourcesManager.addResource(resource);
			return new ResponseEntity<AuthorityResourcesDTO>(dto,HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateResource")
	public ResponseEntity<AuthorityResourcesDTO> updateResource(AuthorityResources resource) throws ManagerException{
		resource.setMenuUrl((resource.getTempMenuUrl()!=null?resource.getTempMenuUrl():resource.getMenuUrl()));
		AuthorityResourcesDTO dto=this.authorityResourcesManager.updateResource(resource);
		return new ResponseEntity<AuthorityResourcesDTO>(dto,HttpStatus.OK);
	}
	
	@RequestMapping(value="/delResource")
	@ResponseBody
	public String removeResource(@RequestParam("menuId") Long menuId){
		String m="";
		try {
			int count=this.authorityResourcesManager.removeResourceById(menuId);
			if(count>0){
				m="success";
			}else{
				m="fail";
			}
		} catch (Exception e) {
			m="fail:"+e.getMessage();
		}
		return m;
	}
	
	@RequestMapping(value="/leftMenuTree/queryMenuTree")
	public ModelAndView queryResourceTreeLeftMenuTree() throws ManagerException{
		ModelAndView mav = new ModelAndView("jsonView");
		AuthorityResourcesDTO dto=this.authorityResourcesManager.queryAllAuthorityResources(1l);
		mav.addObject(dto.getAuthorityResourcesDTOList());
		return mav;
	}
}
