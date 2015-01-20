package com.yougou.logistics.city.common.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 	储位容器库存记帐Vo
 * @author wugy
 * @date  2014-07-15 17:22:25
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
public class AccInventoryConVo  implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
     * 容器编码
     */
    private String conNo;

    /**
     * 仓别
     */
    private String locno;

    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 储位编码
     */
    private String cellNo;
    
    /**
     * 子容器数量
     */
    private BigDecimal childrenQty;

    /**
     * SKU数量
     */
    private BigDecimal skuQty;

    /**
     * 商品属性类型
     */
    private String itemType;

    /**
     * 品质
     */
    private String quality;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 记账方向(1：增加库存，-1：减少库存)
     */
    private Integer direction;

    /**
     *发生子容器数
     */
    private BigDecimal moveChildrenQty;
    
    /**
     *发生SKU数量
     */
    private BigDecimal moveSkuQty;

    /**
     * 记帐人员
     */
    private String creator;

    /**
     * 记帐时间
     */
    private Date createtm;

    /**
     * 单据编码
     */
    private String billNo;
    /**
     * 单据类型
     */
    private String billType;


    /**
     * 修改时间
     */
    private Date edittm;
    

    /**
     * 操作终端标识(1=前台 2=RF 3=DPS 4=AS)
     */
    private String terminalFlag;

    /**
     * 传输标识(10=未传输 13=已传输)
     */
    private String statusTrans;

    /**
     * 操作类型：A:装箱B:拼箱C:拆箱D:绑板E:解板F:拼板
     */
    private String userType;
    
    private String sContainerNo;
    
	public String getsContainerNo() {
		return sContainerNo;
	}
	public void setsContainerNo(String sContainerNo) {
		this.sContainerNo = sContainerNo;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}


	public String getConNo() {
		return conNo;
	}


	public void setConNo(String conNo) {
		this.conNo = conNo;
	}


	public String getLocno() {
		return locno;
	}


	public void setLocno(String locno) {
		this.locno = locno;
	}


	public String getOwnerNo() {
		return ownerNo;
	}


	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}


	public String getCellNo() {
		return cellNo;
	}


	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
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


	public String getSupplierNo() {
		return supplierNo;
	}


	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}


	public Integer getDirection() {
		return direction;
	}


	public void setDirection(Integer direction) {
		this.direction = direction;
	}


	public BigDecimal getMoveChildrenQty() {
		return moveChildrenQty;
	}


	public void setMoveChildrenQty(BigDecimal moveChildrenQty) {
		this.moveChildrenQty = moveChildrenQty;
	}


	public BigDecimal getMoveSkuQty() {
		return moveSkuQty;
	}


	public void setMoveSkuQty(BigDecimal moveSkuQty) {
		this.moveSkuQty = moveSkuQty;
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


	public String getBillNo() {
		return billNo;
	}


	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}


	public String getBillType() {
		return billType;
	}


	public void setBillType(String billType) {
		this.billType = billType;
	}


	public Date getEdittm() {
		return edittm;
	}


	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}


	public BigDecimal getChildrenQty() {
		return childrenQty;
	}


	public void setChildrenQty(BigDecimal childrenQty) {
		this.childrenQty = childrenQty;
	}


	public BigDecimal getSkuQty() {
		return skuQty;
	}


	public void setSkuQty(BigDecimal skuQty) {
		this.skuQty = skuQty;
	}


	public String getTerminalFlag() {
		return terminalFlag;
	}


	public void setTerminalFlag(String terminalFlag) {
		this.terminalFlag = terminalFlag;
	}


	public String getStatusTrans() {
		return statusTrans;
	}


	public void setStatusTrans(String statusTrans) {
		this.statusTrans = statusTrans;
	}

}