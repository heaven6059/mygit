package com.yougou.logistics.city.common.model;

import java.util.Date;

public class AuthorityRole {
    private Long roleId;

    private String roleName;

    private String no;

    private Date roleCreatedate;

    private String remark;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Date getRoleCreatedate() {
        return roleCreatedate;
    }

    public void setRoleCreatedate(Date roleCreatedate) {
        this.roleCreatedate = roleCreatedate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}