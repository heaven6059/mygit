package com.yougou.logistics.city.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.dto.AuthorityMenuDto;
import com.yougou.logistics.city.common.model.AuthorityMenu;
import com.yougou.logistics.city.common.model.AuthorityMenuModule;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.AuthorityMenuManager;
import com.yougou.logistics.city.manager.AuthorityMenuModuleManager;

@Controller
@RequestMapping("/authority_menu")
@SessionAttributes(PublicContains.SESSION_USER)
public class AuthorityMenuController extends BaseCrudController<AuthorityMenu> {
	@Log
	private Logger log;
    @Resource
    private AuthorityMenuManager authorityMenuManager;
    @Resource
    private AuthorityMenuModuleManager authorityMenuModuleManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("authority_menu/",authorityMenuManager);
    }
    
    @RequestMapping(value = "/get")
	public ResponseEntity<AuthorityMenu> get(AuthorityMenu authorityMenu) throws ManagerException {
    	AuthorityMenu type = authorityMenuManager.findAuthorityMenuWithIsLeaf(authorityMenu);
		return new ResponseEntity<AuthorityMenu>(type, HttpStatus.OK);
	}

    @RequestMapping(value = "/post_menu")
    @ResponseBody
	public Map<String,Object> addMenu(AuthorityMenu type){
    	Map<String,Object> result=new HashMap<String,Object>(2);
    	try {
			if(authorityMenuManager.addAuthorityMenu(type)){
				result.put("success", true);
				result.put("data", type);
			}else{
				result.put("success", false);
			}
		} catch (ManagerException e) {
			result.put("success", false);
		}
    	return result;
	}
    
    @RequestMapping(value = "/post_relation")
    @ResponseBody
    public Map<String,Boolean> addRelation(@RequestParam("menuId") int menuId,@RequestParam("usedList") String usedList){
    	Map<String,Boolean> result=new HashMap<String,Boolean>(1);
    	try {
    		if(StringUtils.isNotEmpty(usedList)){
    			String moduleList[]=usedList.split(",");
    			List<AuthorityMenuModule> list=new ArrayList<AuthorityMenuModule>(moduleList.length);
    			for (String m : moduleList) {
    				AuthorityMenuModule mm=new AuthorityMenuModule();
    				mm.setMenuId(menuId);
    				mm.setModuleId(Integer.parseInt(m));
    				list.add(mm);
    			}
    			authorityMenuModuleManager.save(menuId, list);
    		}else{
    			authorityMenuModuleManager.save(menuId, null);
    		}
    		result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
		}
    	return result;
    }
    
    @RequestMapping(value = "/delete")
	public ResponseEntity<Map<String, Boolean>> remove(AuthorityMenu type) throws ManagerException {
    	Map<String, Boolean> flag = new HashMap<String, Boolean>();
    	if(type.getMenuId()==1){
    		flag.put("success", false);
    	}else{
    		authorityMenuManager.deleteById(type);
    		flag.put("success", true);
    	}
		return new ResponseEntity<Map<String, Boolean>>(flag, HttpStatus.OK);
	}
    
    @SuppressWarnings("deprecation")
	@RequestMapping(value = "/tree.json")
	@ResponseBody
    public List<AuthorityMenuDto> tree(){
    	List<AuthorityMenuDto> list=new ArrayList<AuthorityMenuDto>();
		try {
			AuthorityMenuDto dto = this.authorityMenuManager.findAllAuthorityMenu(SysConstans.MEMU_ROOT_NODE_ID);
			list.add(dto);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
    	return list;
    }
    
    @RequestMapping(value = "/user_tree.json")
    @ResponseBody
    public List<AuthorityMenuDto> userTree(@ModelAttribute(PublicContains.SESSION_USER) SystemUser user){
    	List<AuthorityMenuDto> list=new ArrayList<AuthorityMenuDto>();
    	try {
    		// TODO 添加admin权限的菜单
    		//AuthorityMenuDto dto = this.authorityMenuManager.findHasAdminMenu(PublicConstans.MEMU_ROOT_NODE_ID);
    		AuthorityMenuDto dto = this.authorityMenuManager.findUserHasMenus(user.getUserid(),SysConstans.MEMU_ROOT_NODE_ID);
    		list.add(dto);
    	} catch (ManagerException e) {
    		log.error(e.getMessage(), e);
    	}
    	return list;
    }
    
    
}