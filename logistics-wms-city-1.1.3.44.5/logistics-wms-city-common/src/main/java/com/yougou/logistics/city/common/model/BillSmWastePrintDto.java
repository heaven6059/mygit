
package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Map;

import com.yougou.logistics.city.common.utils.SystemCache;
/**
 * 用于其它入库打印
 * @author jys
 *
 */
public class BillSmWastePrintDto {
    private String cellNo;

    private String itemNo;

    private String itemName;

    private String sizeNo;

    private BigDecimal wasteQty;
    
    private Integer totalQty;
    
    private String sysNo;

	private String sizeKind;//类别
	
	private String quality;
	
	private String qualityName;
	
	private String itemType;
	
	private String itemTypeName;
	
	private String creatorName;
	
	private String auditorName;

	private Map<String, Integer> sizeQtyMap;
	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public BigDecimal getWasteQty() {
		return wasteQty;
	}

	public void setWasteQty(BigDecimal wasteQty) {
		this.wasteQty = wasteQty;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getQualityName() {
		this.qualityName = SystemCache.getLookUpName("AREA_QUALITY", this.quality);
		return qualityName;
	}

	public void setQualityName(String qualityName) {
		this.qualityName = qualityName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeName() {
		this.itemTypeName = SystemCache.getLookUpName("ITEM_TYPE", this.itemType);
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public Map<String, Integer> getSizeQtyMap() {
		return sizeQtyMap;
	}

	public void setSizeQtyMap(Map<String, Integer> sizeQtyMap) {
		this.sizeQtyMap = sizeQtyMap;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	
	
}