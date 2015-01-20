package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.enums.BaseInfoStoreStatusEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Store {
	private String storeNo;

	private String storeCode;

	private String storeName;

	private String storeLname;

	private String searchCode;

	private String storeNoHead;

	private String storeStatus;

	private Short storeType;

	private String storeType2;

	private String dtsNo;

	private String sysNo;
	
	private String bookNo;

	private String telno;

	private String faxno;

	private String address;

	private String zipno;

	private String email;

	private String cman;
	
	private String cman1;
	
	private String storeEmail;
	
	private String cphone;
	
	private String cphone1;

	private String manager;

	private String class3;

	private BigDecimal areaTotal;

	private BigDecimal area;

	private String manageCity;

	private String manageZoneNo;

	private String workCity;

	private String province;

	private String incity;

	private String circle;
	
	private String circleNo;
	
	private String circleName;

	private String class2;

	private String zoneNo;

	private String storeNoDc;

	private String storeNo2;

	private String storeNo3;

	private String managerNo;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date opendt;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date closedt;

	private String status;

	private String auditStatus;

	private String prioType;

	private String creator;

	private String statusStr;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String remarks;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	private String editor;
	
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date recievetm;

	private String storeTypeStr;

	private String storeNoHeadStr;

	private String sysNoStr;

	private int childrenCount;
	
	private String isTree;//是否加载树形菜单0否，1是

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLname() {
		return storeLname;
	}

	public void setStoreLname(String storeLname) {
		this.storeLname = storeLname;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getStoreNoHead() {
		return storeNoHead;
	}

	public void setStoreNoHead(String storeNoHead) {
		this.storeNoHead = storeNoHead;
	}

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public Short getStoreType() {
		return storeType;
	}

	public void setStoreType(Short storeType) {
		this.storeType = storeType;
	}

	public String getStoreType2() {
		return storeType2;
	}

	public void setStoreType2(String storeType2) {
		this.storeType2 = storeType2;
	}

	public String getDtsNo() {
		return dtsNo;
	}

	public void setDtsNo(String dtsNo) {
		this.dtsNo = dtsNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getFaxno() {
		return faxno;
	}

	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipno() {
		return zipno;
	}

	public void setZipno(String zipno) {
		this.zipno = zipno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCman() {
		return cman;
	}

	public void setCman(String cman) {
		this.cman = cman;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getClass3() {
		return class3;
	}

	public void setClass3(String class3) {
		this.class3 = class3;
	}

	public BigDecimal getAreaTotal() {
		return areaTotal;
	}

	public void setAreaTotal(BigDecimal areaTotal) {
		this.areaTotal = areaTotal;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public String getManageCity() {
		return manageCity;
	}

	public void setManageCity(String manageCity) {
		this.manageCity = manageCity;
	}

	public String getManageZoneNo() {
		return manageZoneNo;
	}

	public void setManageZoneNo(String manageZoneNo) {
		this.manageZoneNo = manageZoneNo;
	}

	public String getWorkCity() {
		return workCity;
	}

	public void setWorkCity(String workCity) {
		this.workCity = workCity;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getIncity() {
		return incity;
	}

	public void setIncity(String incity) {
		this.incity = incity;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}

	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}

	public String getStoreNoDc() {
		return storeNoDc;
	}

	public void setStoreNoDc(String storeNoDc) {
		this.storeNoDc = storeNoDc;
	}

	public String getStoreNo2() {
		return storeNo2;
	}

	public void setStoreNo2(String storeNo2) {
		this.storeNo2 = storeNo2;
	}

	public String getStoreNo3() {
		return storeNo3;
	}

	public void setStoreNo3(String storeNo3) {
		this.storeNo3 = storeNo3;
	}

	public String getManagerNo() {
		return managerNo;
	}

	public void setManagerNo(String managerNo) {
		this.managerNo = managerNo;
	}

	public Date getOpendt() {
		return opendt;
	}

	public void setOpendt(Date opendt) {
		this.opendt = opendt;
	}

	public Date getClosedt() {
		return closedt;
	}

	public void setClosedt(Date closedt) {
		this.closedt = closedt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.statusStr = BaseInfoStoreStatusEnums.getTextByStatus(this.status);
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getPrioType() {
		return prioType;
	}

	public void setPrioType(String prioType) {
		this.prioType = prioType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Date createtm) {
		this.createtm = createtm;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getStoreTypeStr() {
		return storeTypeStr;
	}

	public void setStoreTypeStr(String storeTypeStr) {
		this.storeTypeStr = storeTypeStr;
	}

	public String getStoreNoHeadStr() {
		return storeNoHeadStr;
	}

	public void setStoreNoHeadStr(String storeNoHeadStr) {
		this.storeNoHeadStr = storeNoHeadStr;
	}

	public int getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}

	public String getSysNoStr() {
		return sysNoStr;
	}

	public void setSysNoStr(String sysNoStr) {
		this.sysNoStr = sysNoStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getBookNo() {
		return bookNo;
	}

	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}

	public Date getRecievetm() {
		return recievetm;
	}

	public void setRecievetm(Date recievetm) {
		this.recievetm = recievetm;
	}

	public String getIsTree() {
		return isTree;
	}

	public void setIsTree(String isTree) {
		this.isTree = isTree;
	}

	public String getCircleNo() {
		return circleNo;
	}

	public void setCircleNo(String circleNo) {
		this.circleNo = circleNo;
	}

	public String getCman1() {
		return cman1;
	}

	public void setCman1(String cman1) {
		this.cman1 = cman1;
	}

	public String getCphone() {
		return cphone;
	}

	public void setCphone(String cphone) {
		this.cphone = cphone;
	}

	public String getCphone1() {
		return cphone1;
	}

	public void setCphone1(String cphone1) {
		this.cphone1 = cphone1;
	}

	public String getStoreEmail() {
		return storeEmail;
	}

	public void setStoreEmail(String storeEmail) {
		this.storeEmail = storeEmail;
	}
	
}