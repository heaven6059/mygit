package com.yougou.logistics.city.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityMenu {
	
    private Integer menuId;

    private String menuName;

    private Integer parentId;

    private String menuRemark;

    private Integer menuSort;

    private String menuType;

    private String menuIsleaf;

    private String menuFlag;

    private Integer menuLevel;
    
    private String state = "open";

    @JsonProperty("id")
    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @JsonProperty(value="text")
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @JsonProperty("remark")
    public String getMenuRemark() {
        return menuRemark;
    }

    public void setMenuRemark(String menuRemark) {
        this.menuRemark = menuRemark;
    }

    @JsonProperty(value="order")
    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    @JsonIgnore
    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    @JsonProperty("isLeaf")
    public String getMenuIsleaf() {
        return menuIsleaf;
    }

    public void setMenuIsleaf(String menuIsleaf) {
        this.menuIsleaf = menuIsleaf;
    }

    @JsonIgnore
    public String getMenuFlag() {
        return menuFlag;
    }

    public void setMenuFlag(String menuFlag) {
        this.menuFlag = menuFlag;
    }

    @JsonIgnore
    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
    
    
}