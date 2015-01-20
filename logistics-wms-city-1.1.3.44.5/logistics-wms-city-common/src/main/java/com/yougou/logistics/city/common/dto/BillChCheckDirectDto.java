package com.yougou.logistics.city.common.dto;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-12-18 上午10:08:27
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillChCheckDirectDto extends BillChCheckDirect {
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date audittm;

	private String areaNo;

	private String areaName;

	private String stockNo;

	private String brandNo;

	private String sysNo;
	
	private String itemName;

	private String styleNo;

	private String colorName;

	private String produceDateStart;

	private String produceDateEnd;

	private String requestDateStart;

	private String requestDateEnd;

	private String planType;

	private List<String> stockNoList;
	
	private String sourceStatus;
	
	private String contentId;
	
	private String cellNoLike;

	public String getCellNoLike() {
		return cellNoLike;
	}

	public void setCellNoLike(String cellNoLike) {
		this.cellNoLike = cellNoLike;
	}

	public Date getAudittm() {
		return audittm;
	}

	public void setAudittm(Date audittm) {
		this.audittm = audittm;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getProduceDateStart() {
		return produceDateStart;
	}

	public void setProduceDateStart(String produceDateStart) {
		this.produceDateStart = produceDateStart;
	}

	public String getProduceDateEnd() {
		return produceDateEnd;
	}

	public void setProduceDateEnd(String produceDateEnd) {
		this.produceDateEnd = produceDateEnd;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public List<String> getStockNoList() {
		return stockNoList;
	}

	public void setStockNoList(List<String> stockNoList) {
		this.stockNoList = stockNoList;
	}

	public String getRequestDateStart() {
		return requestDateStart;
	}

	public void setRequestDateStart(String requestDateStart) {
		this.requestDateStart = requestDateStart;
	}

	public String getRequestDateEnd() {
		return requestDateEnd;
	}

	public void setRequestDateEnd(String requestDateEnd) {
		this.requestDateEnd = requestDateEnd;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

}
