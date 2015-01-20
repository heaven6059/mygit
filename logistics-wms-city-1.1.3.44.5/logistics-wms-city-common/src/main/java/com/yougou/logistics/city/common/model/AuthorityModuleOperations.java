package com.yougou.logistics.city.common.model;

public class AuthorityModuleOperations {
	private Integer moduleId;

	private Integer oprationId;

	private String operationName;

	private String operPermissions;

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

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getOprationId() {
		return oprationId;
	}

	public void setOprationId(Integer oprationId) {
		this.oprationId = oprationId;
	}
}