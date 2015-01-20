package com.yougou.logistics.city.common.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityModuleDTO2 {
	
    private Integer moduleId;
    

    private String moduleName;

    private String moduleUrl;

    private String moduleRemark;

    private Integer moduleSort;
    
    private String checkstate;
    
    private boolean checked=false;
    
    private List<AuthorityModuleDTO2> authorityModuleDTOList;
    
    
    private Integer oprationId;
    private String operationName;
    private String operPermissions;
    
    private String selectOperation;
    
    
    
    
    public String getSelectOperation() {
		return selectOperation;
	}

	public void setSelectOperation(String selectOperation) {
		this.selectOperation = selectOperation;
	}

	@JsonProperty(value="id")
    public Integer getOprationId() {
		return oprationId;
	}

	public void setOprationId(Integer oprationId) {
		this.oprationId = oprationId;
	}

	@JsonProperty(value="text")
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperPermissions() {
		return operPermissions;
	}

	public void setOperPermissions(String operPermissions) {
		this.operPermissions = operPermissions;
	}

	public String getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(String checkstate) {
		this.checkstate = checkstate;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	//@JsonProperty(value="id")
    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

   // @JsonProperty(value="text")
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @JsonProperty(value="url")
    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    public String getModuleRemark() {
        return moduleRemark;
    }

    public void setModuleRemark(String moduleRemark) {
        this.moduleRemark = moduleRemark;
    }

    @JsonProperty(value="order")
    public Integer getModuleSort() {
        return moduleSort;
    }

    public void setModuleSort(Integer moduleSort) {
        this.moduleSort = moduleSort;
    }

    @JsonProperty(value="children")
	public List<AuthorityModuleDTO2> getAuthorityModuleDTOList() {
		return authorityModuleDTOList;
	}

	public void setAuthorityModuleDTOList(
			List<AuthorityModuleDTO2> authorityModuleDTOList) {
		this.authorityModuleDTOList = authorityModuleDTOList;
	}
    
    
}