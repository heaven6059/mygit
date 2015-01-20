/*
 * 类名 com.yougou.logistics.city.common.model.Loginuser
 * @author luo.hl
 * @date  Mon Sep 23 10:01:03 CST 2013
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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.yougou.logistics.city.common.dto.bill.BillOmDivideDtlDto;
import com.yougou.logistics.city.common.utils.CommonUtil;

public class Loginuser {
    private BigDecimal userid;

    private String usercode;

    private String username;

    private String password;

    private BigDecimal deptid;

    private BigDecimal isadmingroup;

    private BigDecimal enableflag;

    private Date lastlogintime;

    private String lastloginip;

    private String useversion;

    private String remark;
    
    private int roleid;//角色ID
    
    private String roleNames;//角色名称字符逗号分割
    
    private List<Rolelist> listRole;//角色集合
    
    /**
     * 辅助字段，仅用于页面展示
     */
    private String  roleName;

    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public BigDecimal getUserid() {
        return userid;
    }

    public void setUserid(BigDecimal userid) {
        this.userid = userid;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getDeptid() {
        return deptid;
    }

    public void setDeptid(BigDecimal deptid) {
        this.deptid = deptid;
    }

    public BigDecimal getIsadmingroup() {
        return isadmingroup;
    }

    public void setIsadmingroup(BigDecimal isadmingroup) {
        this.isadmingroup = isadmingroup;
    }

    public BigDecimal getEnableflag() {
        return enableflag;
    }

    public void setEnableflag(BigDecimal enableflag) {
        this.enableflag = enableflag;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getLastloginip() {
        return lastloginip;
    }

    public void setLastloginip(String lastloginip) {
        this.lastloginip = lastloginip;
    }

    public String getUseversion() {
        return useversion;
    }

    public void setUseversion(String useversion) {
        this.useversion = useversion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public List<Rolelist> getListRole() {
		return listRole;
	}

	public void setListRole(List<Rolelist> listRole) {
		this.listRole = listRole;
		String roleNamesStr = "";
		if(CommonUtil.hasValue(listRole)){
			for (Rolelist r : listRole) {
				if(StringUtils.isNotBlank(r.getRolename())){
					roleNamesStr+=r.getRolename()+",";
				}
			}
			if(StringUtils.isNotBlank(roleNamesStr)){
				roleNamesStr=roleNamesStr.substring(0, roleNamesStr.length()-1);
			}
		}
		this.roleNames = roleNamesStr;
	}
}