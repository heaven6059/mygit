package com.yougou.logistics.city.common.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityResourcesDTO {
	private Long menuId;

	private String menuName;

	private String menuUrl;

	private Long parentId;

	private String remark;

	private Integer sort;

	private String type;
	
	private boolean checked=false;

	private String isleaf;

	private String flag;
	
	private Map<String,String> attributes=new HashMap<String,String>();
	
	private List<AuthorityResourcesDTO> AuthorityResourcesDTOList;
	


	@JsonProperty(value="id")
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@JsonProperty(value="text")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@JsonProperty(value="url")
	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonProperty(value="order")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@JsonProperty(value="children")
	public List<AuthorityResourcesDTO> getAuthorityResourcesDTOList() {
		return AuthorityResourcesDTOList;
	}

	public void setAuthorityResourcesDTOList(
			List<AuthorityResourcesDTO> authorityResourcesDTOList) {
		AuthorityResourcesDTOList = authorityResourcesDTOList;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
}
