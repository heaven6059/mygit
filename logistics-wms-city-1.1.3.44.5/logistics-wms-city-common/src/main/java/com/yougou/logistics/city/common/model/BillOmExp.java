package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-29 16:50:42
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
public class BillOmExp extends BillOmExpKey {
    private String ownerNo;

    private String ownerStoreNo;

    private String storeNo;

    private String subStoreNo;

    private String sourceexpType;

    private String sourceexpNo;

    @JsonSerialize(using =JsonDateSerializer$10.class)
    private Date expDate;

    private BigDecimal fastFlag;

    private String status;

    private Short priority;

    private String addExpNo;

    private String classType;

    private String importNo;

    private String deliverType;

    private String transportType;

    private String batchNo;

    private String lineNo;

    private String fullLineName;

    private String custAddress;

    private String custAddressCode;

    private String contactorName;

    private String custPhone;

    private String custMail;

    private String errorStatus;

    private String createFlag;

    private String returnFlag;

    private String expRemark;

    private String creator;

    private String creatorname;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;
    
    private String editorname;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private String belongFlag;

    private String sendFlag;

    private String bufferLineNo;

    private String specialArticleGroup;

    private String expStatus;

    private String stockType;

    private Short orderPeriod;

    private String financeType;

    private String kickFlag;

    private String sysNo;

    private String statusTrans;

    private String shipperNo;

    private String auditor;
    
    private String auditorname;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date audittm;
    
    private String createtmBegin;
    
    private String createtmEnd;
    
    private String sourceType;
    
    private String sourceNo;
    
    private String splitStatus;
    
    private String sourceTypeStr;
    
    private String splitStatusStr;
    
    
    /**
     * 用户页面展示
     * @return
     */
    
    private int divideFlag;//是否允许分拨
    
    private String storeName;//客户名称
    
    private int totalItemQty;//总品项数

	private int totalExpQty;//总数量

	private BigDecimal totalVolumeQty;//总体积数

	private BigDecimal totalWeightQty;//总重量
	
	private int totalNoenoughQty;//总缺量
	
	private int totalDifferenceQty;//总可用数量
	
	private int itemQty;//总计划数量
	
	private int realQty;//总复核数量
	
	private String statusStr;//用于在页面展示
	
	private String serialNo;//序列ID
	
	private String classTypeStr;//是否直通
	
	private String poNo;//合同号
	
	private String brandNo;//品牌
	
	private String receiptNos;
	
	private String businessType;
	
    private String deliverNo;
    
    private String isPoNo;
    
    private String sysNoName;//品牌库名称
    
    public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getReceiptNos() {
		return receiptNos;
	}

	public void setReceiptNos(String receiptNos) {
		this.receiptNos = receiptNos;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getOwnerStoreNo() {
        return ownerStoreNo;
    }

    public void setOwnerStoreNo(String ownerStoreNo) {
        this.ownerStoreNo = ownerStoreNo;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getSubStoreNo() {
        return subStoreNo;
    }

    public void setSubStoreNo(String subStoreNo) {
        this.subStoreNo = subStoreNo;
    }

    public String getSourceexpType() {
        return sourceexpType;
    }

    public void setSourceexpType(String sourceexpType) {
        this.sourceexpType = sourceexpType;
    }

    public String getSourceexpNo() {
        return sourceexpNo;
    }

    public void setSourceexpNo(String sourceexpNo) {
        this.sourceexpNo = sourceexpNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public BigDecimal getFastFlag() {
        return fastFlag;
    }

    public void setFastFlag(BigDecimal fastFlag) {
        this.fastFlag = fastFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }

    public String getAddExpNo() {
        return addExpNo;
    }

    public void setAddExpNo(String addExpNo) {
        this.addExpNo = addExpNo;
    }

//    public String getClass() {
//        return class;
//    }
//
//    public void setClass(String class) {
//        this.class = class;
//    }

    public String getImportNo() {
        return importNo;
    }

    public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void setImportNo(String importNo) {
        this.importNo = importNo;
    }

    public String getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getFullLineName() {
        return fullLineName;
    }

    public void setFullLineName(String fullLineName) {
        this.fullLineName = fullLineName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustAddressCode() {
        return custAddressCode;
    }

    public void setCustAddressCode(String custAddressCode) {
        this.custAddressCode = custAddressCode;
    }

    public String getContactorName() {
        return contactorName;
    }

    public void setContactorName(String contactorName) {
        this.contactorName = contactorName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustMail() {
        return custMail;
    }

    public void setCustMail(String custMail) {
        this.custMail = custMail;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getExpRemark() {
        return expRemark;
    }

    public void setExpRemark(String expRemark) {
        this.expRemark = expRemark;
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

    public String getBelongFlag() {
        return belongFlag;
    }

    public void setBelongFlag(String belongFlag) {
        this.belongFlag = belongFlag;
    }

    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getBufferLineNo() {
        return bufferLineNo;
    }

    public void setBufferLineNo(String bufferLineNo) {
        this.bufferLineNo = bufferLineNo;
    }

    public String getSpecialArticleGroup() {
        return specialArticleGroup;
    }

    public void setSpecialArticleGroup(String specialArticleGroup) {
        this.specialArticleGroup = specialArticleGroup;
    }

    public String getExpStatus() {
        return expStatus;
    }

    public void setExpStatus(String expStatus) {
        this.expStatus = expStatus;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Short getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Short orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public String getFinanceType() {
        return financeType;
    }

    public void setFinanceType(String financeType) {
        this.financeType = financeType;
    }

    public String getKickFlag() {
        return kickFlag;
    }

    public void setKickFlag(String kickFlag) {
        this.kickFlag = kickFlag;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getStatusTrans() {
        return statusTrans;
    }

    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
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

	public int getTotalExpQty() {
		return totalExpQty;
	}

	public void setTotalExpQty(int totalExpQty) {
		this.totalExpQty = totalExpQty;
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

	public int getItemQty() {
		return itemQty;
	}

	public void setItemQty(int itemQty) {
		this.itemQty = itemQty;
	}

	public int getRealQty() {
		return realQty;
	}

	public void setRealQty(int realQty) {
		this.realQty = realQty;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getDivideFlag() {
		return divideFlag;
	}

	public void setDivideFlag(int divideFlag) {
		this.divideFlag = divideFlag;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getClassTypeStr() {
		return classTypeStr;
	}

	public void setClassTypeStr(String classTypeStr) {
		this.classTypeStr = classTypeStr;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getCreatetmBegin() {
		return createtmBegin;
	}

	public void setCreatetmBegin(String createtmBegin) {
		this.createtmBegin = createtmBegin;
	}

	public String getCreatetmEnd() {
		return createtmEnd;
	}

	public void setCreatetmEnd(String createtmEnd) {
		this.createtmEnd = createtmEnd;
	}

	public String getIsPoNo() {
		return isPoNo;
	}

	public void setIsPoNo(String isPoNo) {
		this.isPoNo = isPoNo;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public String getSplitStatus() {
		return splitStatus;
	}

	public void setSplitStatus(String splitStatus) {
		this.splitStatus = splitStatus;
	}

	public String getSourceTypeStr() {
		return sourceTypeStr;
	}

	public void setSourceTypeStr(String sourceTypeStr) {
		this.sourceTypeStr = sourceTypeStr;
	}

	public String getSplitStatusStr() {
		return splitStatusStr;
	}

	public void setSplitStatusStr(String splitStatusStr) {
		this.splitStatusStr = splitStatusStr;
	}

	public String getSysNoName() {
		return sysNoName;
	}

	public void setSysNoName(String sysNoName) {
		this.sysNoName = sysNoName;
	}

	public String getCreatorname() {
		return creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}

	public String getAuditorname() {
		return auditorname;
	}

	public void setAuditorname(String auditorname) {
		this.auditorname = auditorname;
	}
	
}