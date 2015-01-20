/*
 * 类名 com.yougou.logistics.city.common.model.SystemUserRoleKey
 * @author su.yq
 * @date  Mon Nov 04 11:56:46 CST 2013
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

import java.math.BigDecimal;

public class SystemUserRoleKey {
    private BigDecimal userid;

    private BigDecimal roleid;

    public BigDecimal getUserid() {
        return userid;
    }

    public void setUserid(BigDecimal userid) {
        this.userid = userid;
    }

    public BigDecimal getRoleid() {
        return roleid;
    }

    public void setRoleid(BigDecimal roleid) {
        this.roleid = roleid;
    }
}