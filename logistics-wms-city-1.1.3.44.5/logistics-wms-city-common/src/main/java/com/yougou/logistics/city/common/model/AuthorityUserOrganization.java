/*
 * 类名 com.yougou.logistics.city.common.model.AuthorityUserOrganization
 * @author su.yq
 * @date  Mon Feb 10 14:51:59 CST 2014
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
package com.yougou.logistics.city.common.model;

import java.util.Date;

public class AuthorityUserOrganization {
    private Integer authorityUserOrganizationId;

    private String userId;

    private Short organizationType;

    private String organizationNo;

    private String organizationCode;

    private Integer systemId;

    private Integer areaSystemId;

    private String systemCode;

    private String organizationName;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String createUserId;

    private String updateUserId;

    private String status;

    public Integer getAuthorityUserOrganizationId() {
        return authorityUserOrganizationId;
    }

    public void setAuthorityUserOrganizationId(Integer authorityUserOrganizationId) {
        this.authorityUserOrganizationId = authorityUserOrganizationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Short getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Short organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Integer getAreaSystemId() {
        return areaSystemId;
    }

    public void setAreaSystemId(Integer areaSystemId) {
        this.areaSystemId = areaSystemId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}