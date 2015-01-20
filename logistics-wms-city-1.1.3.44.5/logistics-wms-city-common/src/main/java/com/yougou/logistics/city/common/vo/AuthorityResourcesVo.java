package com.yougou.logistics.city.common.vo;

public class AuthorityResourcesVo {
	
	private Long menuId;
	
	private String menuName;
	
	private String menuurl;
	
	
	private Long parentId;
	
	private String remark;
	
	private String type;
	
	private String flag;
	
	private int sort;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getMenuurl() {
		return menuurl;
	}

	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}
	
	
}
