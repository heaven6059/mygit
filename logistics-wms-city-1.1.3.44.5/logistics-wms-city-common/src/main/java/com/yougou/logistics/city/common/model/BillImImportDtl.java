package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 10:24:56
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
public class BillImImportDtl extends BillImImportDtlKey {
    private String itemNo;

    private String sizeNo;

    private BigDecimal packQty;

    private BigDecimal poQty;
    
    private BigDecimal receiptQty;

    private BigDecimal importQty;

    private BigDecimal unitCost;
    
    private BigDecimal differQty;

    private String checkName;

    private String status;

    private Date checkDate;

    private String outStockFlag;

    private String errorStatus;

    private String itemType;

    private BigDecimal planAcrossQty;

    private BigDecimal checkAcrossQty;

    private String qcFlag;

    private String batchSerialNo;

    private String batchSerialName;

    private String boxNo;
    
    private String originalBoxNo;//原箱号

    private String carPlate;

    private String deliverNo;

    private String itemName;
    
    private String colorName;
    
    private String brandName;
    
    private String brandNo;
    
    private String panNo;
    
    private String checkNameCh;//验收人员中文名称
    
    public String getCheckNameCh() {
		return checkNameCh;
	}

	public void setCheckNameCh(String checkNameCh) {
		this.checkNameCh = checkNameCh;
	}

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date transTime;
    private BigDecimal transQty;
    
    public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public BigDecimal getTransQty() {
		return transQty;
	}

	public void setTransQty(BigDecimal transQty) {
		this.transQty = transQty;
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

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

    public BigDecimal getPoQty() {
        return poQty;
    }

    public void setPoQty(BigDecimal poQty) {
        this.poQty = poQty;
    }

    public BigDecimal getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(BigDecimal receiptQty) {
		this.receiptQty = receiptQty;
	}

	public BigDecimal getImportQty() {
        return importQty;
    }

    public void setImportQty(BigDecimal importQty) {
        this.importQty = importQty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getOutStockFlag() {
        return outStockFlag;
    }

    public void setOutStockFlag(String outStockFlag) {
        this.outStockFlag = outStockFlag;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getPlanAcrossQty() {
        return planAcrossQty;
    }

    public void setPlanAcrossQty(BigDecimal planAcrossQty) {
        this.planAcrossQty = planAcrossQty;
    }

    public BigDecimal getCheckAcrossQty() {
        return checkAcrossQty;
    }

    public void setCheckAcrossQty(BigDecimal checkAcrossQty) {
        this.checkAcrossQty = checkAcrossQty;
    }

    public String getQcFlag() {
        return qcFlag;
    }

    public void setQcFlag(String qcFlag) {
        this.qcFlag = qcFlag;
    }

    public String getBatchSerialNo() {
        return batchSerialNo;
    }

    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo;
    }

    public String getBatchSerialName() {
        return batchSerialName;
    }

    public void setBatchSerialName(String batchSerialName) {
        this.batchSerialName = batchSerialName;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOriginalBoxNo() {
		return originalBoxNo;
	}

	public void setOriginalBoxNo(String originalBoxNo) {
		this.originalBoxNo = originalBoxNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public BigDecimal getDifferQty() {
		return differQty;
	}

	public void setDifferQty(BigDecimal differQty) {
		this.differQty = differQty;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	
}