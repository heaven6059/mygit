package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

public class AuthorityResources {
    private Long menuId;

    private String menuName;

    private String menuUrl;

    private Long parentId;

    private String structure;

    private String remark;

    private Integer sort;

    private String type;

    private String isleaf;

    private String flag;
    
    private String checkstate;
    
    // 不知为什么用menuUrl页面JSON传不到Controller上,改用转换
    private String tempMenuUrl;
    
	public String getTempMenuUrl() {
		return tempMenuUrl;
	}

	public void setTempMenuUrl(String tempMenuUrl) {
		this.tempMenuUrl = tempMenuUrl;
	}
	
	

	public String getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(String checkstate) {
		this.checkstate = checkstate;
	}



	private BigDecimal menuLevel;

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

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public BigDecimal getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(BigDecimal menuLevel) {
        this.menuLevel = menuLevel;
    }
}