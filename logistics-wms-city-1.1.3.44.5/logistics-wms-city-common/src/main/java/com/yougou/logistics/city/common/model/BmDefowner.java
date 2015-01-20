package com.yougou.logistics.city.common.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * 
 *  委托业主
 * 
 * @author qin.dy
 * @date 2013-9-22 上午10:55:10
 * @copyright yougou.com
 */
public class BmDefowner {
	
	/**
	 * 委托业主编码
	 */
    private String ownerNo;

    /**
     * 委托业主名称
     */
    private String ownerName;

    /**
     * 委托业主简称
     */
    private Object ownerAlias;

    /**
     * 地址
     */
    private String ownerAddress;

    /**
     * 电话
     */
    private String ownerPhone;

    /**
     * 传真
     */
    private String ownerFax;

    /**
     * 联系人
     */
    private String ownerContact;

    /**
     * 备注
     */
    private String ownerRemark;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 发票地址
     */
    private String invoiceAddr;

    /**
     * 发票抬头
     */
    private String invoiceHeader;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建标识
     */
    private String createFlag;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间     
     */
    @JsonSerialize(using =JsonDateSerializer$10.class)
    private Date createtm;

    /**
     * 编辑人
     */
    private String editor;

    /**
     * 编辑时间
     */
    @JsonSerialize(using =JsonDateSerializer$10.class)
    private Date edittm;

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Object getOwnerAlias() {
        return ownerAlias;
    }

    public void setOwnerAlias(Object ownerAlias) {
        this.ownerAlias = ownerAlias;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerFax() {
        return ownerFax;
    }

    public void setOwnerFax(String ownerFax) {
        this.ownerFax = ownerFax;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getOwnerRemark() {
        return ownerRemark;
    }

    public void setOwnerRemark(String ownerRemark) {
        this.ownerRemark = ownerRemark;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceAddr() {
        return invoiceAddr;
    }

    public void setInvoiceAddr(String invoiceAddr) {
        this.invoiceAddr = invoiceAddr;
    }

    public String getInvoiceHeader() {
        return invoiceHeader;
    }

    public void setInvoiceHeader(String invoiceHeader) {
        this.invoiceHeader = invoiceHeader;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateFlag() {
        return createFlag;
    }

    public void setCreateFlag(String createFlag) {
        this.createFlag = createFlag;
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
}