package com.yougou.logistics.city.common.model;

public class AuthorityRoleModule {
    private Integer roleId;

    private Integer moduleId;

    private Integer operPermissions;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getOperPermissions() {
        return operPermissions;
    }

    public void setOperPermissions(Integer operPermissions) {
        this.operPermissions = operPermissions;
    }
}