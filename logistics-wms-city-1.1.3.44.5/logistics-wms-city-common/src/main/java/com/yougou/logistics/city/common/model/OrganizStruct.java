package com.yougou.logistics.city.common.model;

public class OrganizStruct {
    private Long orgId;

    private String orgName;

    private String struct;

    private String yitianInd;

    private String remark;

    private String no;

    private String isleaf;

    private Long child;

    private Long orgLevel;

    private Long parentId;

    
    
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStruct() {
        return struct;
    }

    public void setStruct(String struct) {
        this.struct = struct;
    }

    public String getYitianInd() {
        return yitianInd;
    }

    public void setYitianInd(String yitianInd) {
        this.yitianInd = yitianInd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    public Long getChild() {
        return child;
    }

    public void setChild(Long child) {
        this.child = child;
    }

    public Long getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Long orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}