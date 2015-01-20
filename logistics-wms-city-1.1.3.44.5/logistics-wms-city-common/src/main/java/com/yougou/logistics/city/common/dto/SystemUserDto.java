package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.yougou.logistics.city.common.model.AuthorityRole;
import com.yougou.logistics.city.common.model.Rolelist;
import com.yougou.logistics.city.common.utils.CommonUtil;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2013-11-4 上午10:41:16
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class SystemUserDto {
	
	private Long userid; // 主键ID

	private String username; // 中文姓名  管理员

	private String sex;

	private String loginName; // 登陆名称  admin 

	private String loginPassword; //  密码

	private String mobilePhone;

	private String telPhone;

	private String email;

	private String qqNum;

	private String msnNum;

	private String state;

	private String category;

	private String no;

	private String userlevel;

	private String organizName;

	private BigDecimal organizNo;

	private String supplierCode;

	private String warehouseCode;

	private String userGroupId;

	private Date pwdUpdateTime;

	private String giftCardPermission;

	private String storeNo;
	
	private String  roleName;//角色名称
	
	private int roleid;//角色ID
    
    private String roleNames;//角色名称字符逗号分割
	
	private List<AuthorityRole> listAuthorityRoles;//角色list

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQqNum() {
		return qqNum;
	}

	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}

	public String getMsnNum() {
		return msnNum;
	}

	public void setMsnNum(String msnNum) {
		this.msnNum = msnNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}

	public String getOrganizName() {
		return organizName;
	}

	public void setOrganizName(String organizName) {
		this.organizName = organizName;
	}

	public BigDecimal getOrganizNo() {
		return organizNo;
	}

	public void setOrganizNo(BigDecimal organizNo) {
		this.organizNo = organizNo;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Date getPwdUpdateTime() {
		return pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}

	public String getGiftCardPermission() {
		return giftCardPermission;
	}

	public void setGiftCardPermission(String giftCardPermission) {
		this.giftCardPermission = giftCardPermission;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public List<AuthorityRole> getListAuthorityRoles() {
		return listAuthorityRoles;
	}

	public void setListAuthorityRoles(List<AuthorityRole> listAuthorityRoles) {
		this.listAuthorityRoles = listAuthorityRoles;
		String roleNamesStr = "";
		if(CommonUtil.hasValue(listAuthorityRoles)){
			for (AuthorityRole r : listAuthorityRoles) {
				if(StringUtils.isNotBlank(r.getRoleName())){
					roleNamesStr+=r.getRoleName()+",";
				}
			}
			if(StringUtils.isNotBlank(roleNamesStr)){
				roleNamesStr=roleNamesStr.substring(0, roleNamesStr.length()-1);
			}
		}
		this.roleNames = roleNamesStr;
	}
}
