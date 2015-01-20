package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2014-01-17 18:04:08
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
public class BillUmInstockDirect extends BillUmInstockDirectKey {
    private String status;

    private String itemNo;

    private String sizeNo;

    private String cellNo;

    private Long cellId;

    private String destCellNo;

    private Long destCellId;

    private String realCellNo;

    private BigDecimal estQty;

    private String itemType;

    private String quality;

    private BigDecimal packQty;
    
    private String  sourceNo;
    
    private String  itemName;
    
    private String  boxNo;
    
    private String sendUser;
    
    private String workUser;
    
    @JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;
    private String creator;
    private String creatorName;
    
    public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDestCellNo() {
        return destCellNo;
    }

    public void setDestCellNo(String destCellNo) {
        this.destCellNo = destCellNo;
    }

    public Long getDestCellId() {
        return destCellId;
    }

    public void setDestCellId(Long destCellId) {
        this.destCellId = destCellId;
    }

    public String getRealCellNo() {
        return realCellNo;
    }

    public void setRealCellNo(String realCellNo) {
        this.realCellNo = realCellNo;
    }

    public BigDecimal getEstQty() {
        return estQty;
    }

    public void setEstQty(BigDecimal estQty) {
        this.estQty = estQty;
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

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getWorkUser() {
		return workUser;
	}

	public void setWorkUser(String workUser) {
		this.workUser = workUser;
	}

	public Date getCreatetm() {
		return createtm;
	}

	public void setCreatetm(Date createtm) {
		this.createtm = createtm;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
}