/*
 * 类名 com.yougou.logistics.city.common.model.BillHmPlanDtl
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BillHmPlanDtl extends BillHmPlanDtlKey {

	private String itemNo;

	private String sizeNo;

	private String itemId;

	private BigDecimal originQty;

	private String sCellNo;

	private String sContainerNo;

	private String dContainerNo;

	private String dCellNo;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date moveDate;

	private String scanLabelNo;

	private String stockType;

	private String stockValue;
	
	private String brandNo;

	/**附加属性**/

	private String itemName;

	private String styleNo;

	private String colorName;
	
	private int cellId;//来源储位ID
	
	private int conContentQty;//库存数量

	private Date startCreatetm;// 起始创建日期

	private Date endCreatetm;// 结束创建日期

	private Date startAudittm;// 起始审核日期

	private Date endAudittm;// 结束审核日期
	
	private BigDecimal packQty;
	
	private String quality;
	
	private String itemType;
	
	private String itemTypeStr;
	
	private String qualityStr;
	
	public String getItemTypeStr() {
		return itemTypeStr;
	}

	public void setItemTypeStr(String itemTypeStr) {
		this.itemTypeStr = itemTypeStr;
	}

	public String getQualityStr() {
		return qualityStr;
	}

	public void setQualityStr(String qualityStr) {
		this.qualityStr = qualityStr;
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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getOriginQty() {
		return originQty;
	}

	public void setOriginQty(BigDecimal originQty) {
		this.originQty = originQty;
	}

	public String getsCellNo() {
		return sCellNo;
	}

	public void setsCellNo(String sCellNo) {
		this.sCellNo = sCellNo;
	}
	
	public String getSCellNo() {
		return sCellNo;
	}

	public void setSCellNo(String sCellNo) {
		this.sCellNo = sCellNo;
	}

	public String getsContainerNo() {
		return sContainerNo;
	}

	public void setsContainerNo(String sContainerNo) {
		this.sContainerNo = sContainerNo;
	}

	public String getdContainerNo() {
		return dContainerNo;
	}

	public void setdContainerNo(String dContainerNo) {
		this.dContainerNo = dContainerNo;
	}

	public String getdCellNo() {
		return dCellNo;
	}

	public void setdCellNo(String dCellNo) {
		this.dCellNo = dCellNo;
	}
	
	public String getDCellNo() {
		return dCellNo;
	}

	public void setDCellNo(String dCellNo) {
		this.dCellNo = dCellNo;
	}

	public Date getMoveDate() {
		return moveDate;
	}

	public void setMoveDate(Date moveDate) {
		this.moveDate = moveDate;
	}

	public String getScanLabelNo() {
		return scanLabelNo;
	}

	public void setScanLabelNo(String scanLabelNo) {
		this.scanLabelNo = scanLabelNo;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStockValue() {
		return stockValue;
	}

	public void setStockValue(String stockValue) {
		this.stockValue = stockValue;
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

	public Date getStartCreatetm() {
		return startCreatetm;
	}

	public void setStartCreatetm(Date startCreatetm) {
		this.startCreatetm = startCreatetm;
	}

	public Date getEndCreatetm() {
		return endCreatetm;
	}

	public void setEndCreatetm(Date endCreatetm) {
		this.endCreatetm = endCreatetm;
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

	public int getConContentQty() {
		return conContentQty;
	}

	public void setConContentQty(int conContentQty) {
		this.conContentQty = conContentQty;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
}