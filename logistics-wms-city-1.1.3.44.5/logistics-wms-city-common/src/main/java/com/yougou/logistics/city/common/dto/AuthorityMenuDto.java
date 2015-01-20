package com.yougou.logistics.city.common.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import com.yougou.logistics.city.common.model.AuthorityMenu;

/**
 * 扩展menu DTO用于
 * 
 * @author wei.b
 * @date 2013-8-21 下午6:19:59
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityMenuDto extends AuthorityMenu {
	
	/**
	 * 子菜单
	 */
	private List<AuthorityMenuDto> authorityMenuDtoList;
	
	private Map<String,String> attributes=new HashMap<String,String>(1);
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@JsonProperty(value="children")
	public List<AuthorityMenuDto> getAuthorityMenuDtoList() {
		return authorityMenuDtoList;
	}

	public void setAuthorityMenuDtoList(List<AuthorityMenuDto> authorityMenuDtoList) {
		this.authorityMenuDtoList = authorityMenuDtoList;
	}
}
