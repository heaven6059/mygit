package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-10 18:18:26
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
public class Rolelist {
    private BigDecimal roleid;

    private String roleowner;

    private String rolename;

    private BigDecimal enableflag;

    private String remark;

    public BigDecimal getRoleid() {
        return roleid;
    }

    public void setRoleid(BigDecimal roleid) {
        this.roleid = roleid;
    }

    public String getRoleowner() {
        return roleowner;
    }

    public void setRoleowner(String roleowner) {
        this.roleowner = roleowner;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public BigDecimal getEnableflag() {
        return enableflag;
    }

    public void setEnableflag(BigDecimal enableflag) {
        this.enableflag = enableflag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}