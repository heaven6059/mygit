package com.yougou.logistics.city.common.dto;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityRoleDTO {
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
