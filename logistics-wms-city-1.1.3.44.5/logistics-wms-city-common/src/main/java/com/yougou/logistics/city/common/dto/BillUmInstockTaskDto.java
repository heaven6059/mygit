package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.ConContent;

/**
 * TODO: 退仓上架任务
 * 
 * @author luo.hl
 * @date 2013-11-13 上午11:45:01
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillUmInstockTaskDto extends ConContent {

	//c.item_no,i.item_name,c.qty,c.INSTOCK_QTY,i.style_no,co.color_name, ib.size_no,b.brand_name,i.unit_name,ii.QUALITY,ii.IMPORT_BATCH_NO
	private String itemName;
	private String styleNo;
	private String colorName;
	private String sizeNo;
	private String brandName;
	private String unitName;
	private String quality;
	private String importBatchNo;
	
	private String cellNo;
	private Long cellId;
	
	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Long getCellId() {
		return cellId;
	}

	public void setCellId(Long cellId) {
		this.cellId = cellId;
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

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getImportBatchNo() {
		return importBatchNo;
	}

	public void setImportBatchNo(String importBatchNo) {
		this.importBatchNo = importBatchNo;
	}

}
