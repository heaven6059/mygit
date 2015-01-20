/*
 * 类名 com.yougou.logistics.city.common.model.ConContentMove
 * @author yougoupublic
 * @date  Fri Mar 07 11:21:04 CST 2014
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
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class ConContentMove {
	
    private Long rowId;

    private Long cellId;

    private String locno;

    private String cellNo;

    private String itemNo;

    private String barcode;

    private String itemType;

    private String quality;

    private String ownerNo;

    private String supplierNo;

    private String boxNo;

    private BigDecimal moveQty;

    private BigDecimal balanceQty;

    private BigDecimal direction;

    private String paperNo;

    private String paperType;

    private String ioFlag;

    private String creator;
    
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;
    
    @JsonSerialize(using = JsonDateSerializer$10.class)
    private Date createdt;

    private String preFlag;

    private BigDecimal seqId;

    private String statusTrans;

    private BigDecimal packQty;

    private String terminalFlag;
    
    /**用于页面展示**/
    
    private String areaNo;
    
    private String areaName;
    
    private String itemName;
    
    private String sizeNo;
    
    private String billName;
    
    private String itemTypeStr;
    
    private String qualityStr;
    
    private String ioFlagStr;
    
    private Date startCreatetm;
    
    private Date endCreatetm;
    
    private String preFlagStr;

    public String getPreFlagStr() {
		return preFlagStr;
	}

	public void setPreFlagStr(String preFlagStr) {
		this.preFlagStr = preFlagStr;
	}

	public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public Long getCellId() {
        return cellId;
    }

    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public BigDecimal getMoveQty() {
        return moveQty;
    }

    public void setMoveQty(BigDecimal moveQty) {
        this.moveQty = moveQty;
    }

    public BigDecimal getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(BigDecimal balanceQty) {
        this.balanceQty = balanceQty;
    }

    public BigDecimal getDirection() {
        return direction;
    }

    public void setDirection(BigDecimal direction) {
        this.direction = direction;
    }

    public String getPaperNo() {
        return paperNo;
    }

    public void setPaperNo(String paperNo) {
        this.paperNo = paperNo;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getIoFlag() {
        return ioFlag;
    }

    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
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

    public Date getCreatedt() {
        return createdt;
    }

    public void setCreatedt(Date createdt) {
        this.createdt = createdt;
    }

    public String getPreFlag() {
        return preFlag;
    }

    public void setPreFlag(String preFlag) {
        this.preFlag = preFlag;
    }

    public BigDecimal getSeqId() {
        return seqId;
    }

    public void setSeqId(BigDecimal seqId) {
        this.seqId = seqId;
    }

    public String getStatusTrans() {
        return statusTrans;
    }

    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
    }

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

    public String getTerminalFlag() {
        return terminalFlag;
    }

    public void setTerminalFlag(String terminalFlag) {
        this.terminalFlag = terminalFlag;
    }

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
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

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

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

	public String getIoFlagStr() {
		return ioFlagStr;
	}

	public void setIoFlagStr(String ioFlagStr) {
		this.ioFlagStr = ioFlagStr;
	}
}