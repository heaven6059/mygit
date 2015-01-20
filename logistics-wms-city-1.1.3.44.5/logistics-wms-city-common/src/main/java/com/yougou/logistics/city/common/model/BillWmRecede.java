package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.SystemCache;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public class BillWmRecede extends BillWmRecedeKey {
    private String recedeType;

    private String poType;

    private String poNo;

    private String supplierNo;
    
    private String classType;

    private String untreadNo;

    @JsonSerialize(using =JsonDateSerializer$10.class)
    private Date recedeDate;

    private String status;
    //展示的状态
    private String showStatus;
    private String createFlag;

    private String errorStatus;

    private String creator;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private BigDecimal sendFlag;

    private String stockType;

    private String stockValue;

    private String quality;

    private String sysNo;

    private String recedeReason;

    private String memo;

    private String auditor;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date audittm;
    
    private String creatorName;
    private String editorName;
    private String auditorName;
    /**
     * 用户页面展示
     * @return
     */
    
    private int totalItemQty;//总品项数

    private int totalRecedeQty;//总计划数量
    
    private int totalOutstockQty;//总下架数
    
	private int totalAvailableRecedeQty;//总可退厂数量

	private BigDecimal totalVolumeQty;//总体积数

	private BigDecimal totalWeightQty;//总重量
	
	private int totalNoenoughQty;//总缺量
	
	private int totalDifferenceQty;//总可用数量
	
	private String createTmStart;// 起始创建日期
	
	private String createTmEnd;// 结束创建日期
	
	private String supplierName;//供应商
	
	private String brandNo;//品牌

	//显示退厂类型
	private String showRecedeType;
    public String getRecedeType() {
        return recedeType;
    }

    public void setRecedeType(String recedeType) {
        this.recedeType = recedeType;
        this.showRecedeType = SystemCache.getLookUpName("CITY_RECEDE_TYPE", recedeType);
    }

    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getUntreadNo() {
        return untreadNo;
    }

    public void setUntreadNo(String untreadNo) {
        this.untreadNo = untreadNo;
    }

    public Date getRecedeDate() {
        return recedeDate;
    }

    public void setRecedeDate(Date recedeDate) {
        this.recedeDate = recedeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
		this.showStatus = SystemCache.getLookUpName("CITY_RECEDE_STATUS", status);
    }

    public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
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

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public BigDecimal getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(BigDecimal sendFlag) {
        this.sendFlag = sendFlag;
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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getRecedeReason() {
        return recedeReason;
    }

    public void setRecedeReason(String recedeReason) {
        this.recedeReason = recedeReason;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Date getAudittm() {
        return audittm;
    }

    public void setAudittm(Date audittm) {
        this.audittm = audittm;
    }

	public int getTotalItemQty() {
		return totalItemQty;
	}

	public void setTotalItemQty(int totalItemQty) {
		this.totalItemQty = totalItemQty;
	}

	public int getTotalRecedeQty() {
		return totalRecedeQty;
	}

	public void setTotalRecedeQty(int totalRecedeQty) {
		this.totalRecedeQty = totalRecedeQty;
	}

	public int getTotalAvailableRecedeQty() {
		return totalAvailableRecedeQty;
	}

	public void setTotalAvailableRecedeQty(int totalAvailableRecedeQty) {
		this.totalAvailableRecedeQty = totalAvailableRecedeQty;
	}

	public BigDecimal getTotalVolumeQty() {
		return totalVolumeQty;
	}

	public void setTotalVolumeQty(BigDecimal totalVolumeQty) {
		this.totalVolumeQty = totalVolumeQty;
	}

	public BigDecimal getTotalWeightQty() {
		return totalWeightQty;
	}

	public void setTotalWeightQty(BigDecimal totalWeightQty) {
		this.totalWeightQty = totalWeightQty;
	}

	public int getTotalNoenoughQty() {
		return totalNoenoughQty;
	}

	public void setTotalNoenoughQty(int totalNoenoughQty) {
		this.totalNoenoughQty = totalNoenoughQty;
	}

	public int getTotalDifferenceQty() {
		return totalDifferenceQty;
	}

	public void setTotalDifferenceQty(int totalDifferenceQty) {
		this.totalDifferenceQty = totalDifferenceQty;
	}

	public int getTotalOutstockQty() {
		return totalOutstockQty;
	}

	public void setTotalOutstockQty(int totalOutstockQty) {
		this.totalOutstockQty = totalOutstockQty;
	}

	public String getCreateTmStart() {
		return createTmStart;
	}

	public void setCreateTmStart(String createTmStart) {
		this.createTmStart = createTmStart;
	}

	public String getCreateTmEnd() {
		return createTmEnd;
	}

	public void setCreateTmEnd(String createTmEnd) {
		this.createTmEnd = createTmEnd;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public String getShowRecedeType() {
		return showRecedeType;
	}

	public void setShowRecedeType(String showRecedeType) {
		this.showRecedeType = showRecedeType;
	}
	
}