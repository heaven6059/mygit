package com.yougou.logistics.city.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityModule {
    private Integer moduleId;

    private String moduleName;

    private String moduleUrl;

    private String moduleRemark;

    private Integer moduleSort;
    
    private String checkstate;
    
    public String getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(String checkstate) {
		this.checkstate = checkstate;
	}

    @JsonProperty("id")
    public Integer getModuleId() {

        return moduleId;
    }
    @JsonSetter("id")
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    @JsonProperty("name")
    public String getModuleName() {
        return moduleName;
    }
    @JsonSetter("name")
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @JsonProperty("url")
    public String getModuleUrl() {
        return moduleUrl;
    }
    @JsonSetter("url")
    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    @JsonProperty("remark")
    public String getModuleRemark() {
        return moduleRemark;
    }
    @JsonSetter("remark")
    public void setModuleRemark(String moduleRemark) {
        this.moduleRemark = moduleRemark;
    }

    @JsonProperty("sort")
    public Integer getModuleSort() {
        return moduleSort;
    }
    @JsonSetter("sort")
    public void setModuleSort(Integer moduleSort) {
        this.moduleSort = moduleSort;
    }
}