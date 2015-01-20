package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-25 21:07:33
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
public class ConBox extends ConBoxKey {
    private Date creatDate;

    private String status;

    private String withCodeNo;

    private BigDecimal qty;

    private BigDecimal cost;

    private String flag;

    private String sImportNo;

    private String exportNo;

    private String storeNo;

    private String boxType;

    private String batchSerialNo;

    private Date importDate;
    
    private String cellNo;
    
    private String itemType;

    private String ownerName;
    
    private String formerStatus;
    
    private int boxNum;
    
    private String panNo;
    
    public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWithCodeNo() {
        return withCodeNo;
    }

    public void setWithCodeNo(String withCodeNo) {
        this.withCodeNo = withCodeNo;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getsImportNo() {
        return sImportNo;
    }

    public void setsImportNo(String sImportNo) {
        this.sImportNo = sImportNo;
    }

    public String getExportNo() {
        return exportNo;
    }

    public void setExportNo(String exportNo) {
        this.exportNo = exportNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getBatchSerialNo() {
        return batchSerialNo;
    }

    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

	public String getFormerStatus() {
		return formerStatus;
	}

	public void setFormerStatus(String formerStatus) {
		this.formerStatus = formerStatus;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(int boxNum) {
		this.boxNum = boxNum;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
}