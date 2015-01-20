package com.yougou.logistics.city.common.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class OrganizStructDTO {

	
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
	
	
	
	private List<OrganizStructDTO> OrganizStructDTOList;

	private boolean checked=false;
	
	
	
	
	
	
	
	

//	@JsonProperty(value="order")
//	public Integer getSort() {
//		return sort;
//	}
//
//	public void setSort(Integer sort) {
//		this.sort = sort;
//	}

	
	
	

	@JsonProperty(value="id")
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	
	@JsonProperty(value="text")
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

	@JsonProperty(value="level")
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
	
	
	

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@JsonProperty(value="children")
	public List<OrganizStructDTO> getOrganizStructDTOList() {
		return OrganizStructDTOList;
	}

	public void setOrganizStructDTOList(
			List<OrganizStructDTO> OrganizStructDTOList) {
		this.OrganizStructDTOList = OrganizStructDTOList;
	}
}
