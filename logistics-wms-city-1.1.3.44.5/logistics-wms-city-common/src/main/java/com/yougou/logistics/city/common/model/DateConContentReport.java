package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.yougou.logistics.city.common.enums.DateConContentReportEnums;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-2-22 下午6:32:14
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class DateConContentReport {
	
	private String locno;

	private String itemNo;

	private String sizeNo;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date audittm;

	private String reportType;

	private BigDecimal jhrkQty;

	private BigDecimal tcrkQty;

	private BigDecimal tcckQty;

	private BigDecimal fhckQty;

	private BigDecimal qtrkQty;

	private BigDecimal bsckQty;

	private BigDecimal pyrkQty;

	private BigDecimal kpckQty;

	private Date startAudittm;

	private Date endAudittm;
	
	private String reportTypeStr;
	
	private String audittmStr;
	
	private String brandNo;

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public Date getAudittm() {
		return audittm;
	}

	public void setAudittm(Date audittm) {
		this.audittm = audittm;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
		this.reportTypeStr = DateConContentReportEnums.getTextByStatus(this.reportType);
	}

	public BigDecimal getJhrkQty() {
		return jhrkQty;
	}

	public void setJhrkQty(BigDecimal jhrkQty) {
		this.jhrkQty = jhrkQty;
	}

	public BigDecimal getTcrkQty() {
		return tcrkQty;
	}

	public void setTcrkQty(BigDecimal tcrkQty) {
		this.tcrkQty = tcrkQty;
	}

	public BigDecimal getTcckQty() {
		return tcckQty;
	}

	public void setTcckQty(BigDecimal tcckQty) {
		this.tcckQty = tcckQty;
	}

	public BigDecimal getFhckQty() {
		return fhckQty;
	}

	public void setFhckQty(BigDecimal fhckQty) {
		this.fhckQty = fhckQty;
	}

	public BigDecimal getQtrkQty() {
		return qtrkQty;
	}

	public void setQtrkQty(BigDecimal qtrkQty) {
		this.qtrkQty = qtrkQty;
	}

	public BigDecimal getBsckQty() {
		return bsckQty;
	}

	public void setBsckQty(BigDecimal bsckQty) {
		this.bsckQty = bsckQty;
	}

	public BigDecimal getPyrkQty() {
		return pyrkQty;
	}

	public void setPyrkQty(BigDecimal pyrkQty) {
		this.pyrkQty = pyrkQty;
	}

	public BigDecimal getKpckQty() {
		return kpckQty;
	}

	public void setKpckQty(BigDecimal kpckQty) {
		this.kpckQty = kpckQty;
	}

	public Date getStartAudittm() {
		return startAudittm;
	}

	public void setStartAudittm(Date startAudittm) {
		this.startAudittm = startAudittm;
	}

	public Date getEndAudittm() {
		return endAudittm;
	}

	public void setEndAudittm(Date endAudittm) {
		this.endAudittm = endAudittm;
	}

	public String getReportTypeStr() {
		return reportTypeStr;
	}

	public void setReportTypeStr(String reportTypeStr) {
		this.reportTypeStr = reportTypeStr;
	}

	public String getAudittmStr() {
		return audittmStr;
	}

	public void setAudittmStr(String audittmStr) {
		this.audittmStr = audittmStr;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
}
