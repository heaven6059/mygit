/*
 * 类名 com.yougou.logistics.city.common.model.BillWmOutstockDtl
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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

public class BillWmOutstockDtl extends BillWmOutstockDtlKey {
	private String sourceNo;

	private Short poId;

	private String itemNo;

	private Long itemId;

	private BigDecimal packQty;

	private BigDecimal itemQty;

	private BigDecimal realQty;

	private String sCellNo;

	private Long sCellId;

	private String sContainerNo;

	private String dCellNo;

	private Long dCellId;

	private String dContainerNo;

	private String outstockCellNo;

	private Long outstockCellId;

	private String outstockContainerNo;

	private String status;

	private String assignName;

	private String outstockName;

	private Date outstockDate;

	private String stockType;

	private String stockValue;

	private String sizeNo;

	private String boxNo;

	private BigDecimal recheckQty;// 复核数量

	private String itemName;
	private String styleNo;
	private String colorNo;
	private String colorName;
	private BigDecimal packageNum;
	private String barcode;

	private String brandNo;
    private String assignChName;
    private String outstockChName;
	public BigDecimal getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(BigDecimal packageNum) {
		this.packageNum = packageNum;
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

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public Short getPoId() {
		return poId;
	}

	public void setPoId(Short poId) {
		this.poId = poId;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public String getsCellNo() {
		return sCellNo;
	}

	public void setsCellNo(String sCellNo) {
		this.sCellNo = sCellNo;
	}

	public Long getsCellId() {
		return sCellId;
	}

	public void setsCellId(Long sCellId) {
		this.sCellId = sCellId;
	}

	public String getsContainerNo() {
		return sContainerNo;
	}

	public void setsContainerNo(String sContainerNo) {
		this.sContainerNo = sContainerNo;
	}

	public String getdCellNo() {
		return dCellNo;
	}

	public void setdCellNo(String dCellNo) {
		this.dCellNo = dCellNo;
	}

	public Long getdCellId() {
		return dCellId;
	}

	public void setdCellId(Long dCellId) {
		this.dCellId = dCellId;
	}

	public String getdContainerNo() {
		return dContainerNo;
	}

	public void setdContainerNo(String dContainerNo) {
		this.dContainerNo = dContainerNo;
	}

	public String getOutstockCellNo() {
		return outstockCellNo;
	}

	public void setOutstockCellNo(String outstockCellNo) {
		this.outstockCellNo = outstockCellNo;
	}

	public Long getOutstockCellId() {
		return outstockCellId;
	}

	public void setOutstockCellId(Long outstockCellId) {
		this.outstockCellId = outstockCellId;
	}

	public String getOutstockContainerNo() {
		return outstockContainerNo;
	}

	public void setOutstockContainerNo(String outstockContainerNo) {
		this.outstockContainerNo = outstockContainerNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getOutstockName() {
		return outstockName;
	}

	public void setOutstockName(String outstockName) {
		this.outstockName = outstockName;
	}

	public Date getOutstockDate() {
		return outstockDate;
	}

	public void setOutstockDate(Date outstockDate) {
		this.outstockDate = outstockDate;
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

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getAssignChName() {
		return assignChName;
	}

	public void setAssignChName(String assignChName) {
		this.assignChName = assignChName;
	}

	public String getOutstockChName() {
		return outstockChName;
	}

	public void setOutstockChName(String outstockChName) {
		this.outstockChName = outstockChName;
	}
	
}