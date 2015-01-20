/*
 * 类名 com.yougou.logistics.city.common.model.BmDefcar
 * @author qin.dy
 * @date  Mon Sep 23 18:33:13 CST 2013
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

/**
 * 
 * 车辆管理
 * 
 * @author qin.dy
 * @date 2013-9-23 下午7:01:06
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BmDefcar extends BmDefcarKey {
	/**
	 * 车辆类型代码
	 */
    private String cartypeNo;

    /**
     * 车辆名称
     */
    private String carName;

    /**
     * 车牌号
     */
    private String carPlate;

    /**
     * 百公里油耗
     */
    private BigDecimal oilConsume;

    /**
     * 保养里程数
     */
    private BigDecimal careMile;

    /**
     * 里程数
     */
    private BigDecimal mile;

    /**
     * 保养日期
     */
    @JsonSerialize(using =JsonDateSerializer$10.class)
    private Date careDate;

    /**
     * 保养人
     */
    private String careWorker;

    /**
     * 承运商
     */
    private String sanplNo;

    /**
     * 创建人
     */
    private String creator;
   
    /**
     * 创建时间
     */
    private Date createtm;

    /**
     * 编辑人
     */
    private String editor;

    /**
     * 编辑时间
     */
    private Date edittm;

    /**
     * 车辆类型
     */
    private String carClass;

    public String getCartypeNo() {
        return cartypeNo;
    }

    public void setCartypeNo(String cartypeNo) {
        this.cartypeNo = cartypeNo;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public BigDecimal getOilConsume() {
        return oilConsume;
    }

    public void setOilConsume(BigDecimal oilConsume) {
        this.oilConsume = oilConsume;
    }

    public BigDecimal getCareMile() {
        return careMile;
    }

    public void setCareMile(BigDecimal careMile) {
        this.careMile = careMile;
    }

    public BigDecimal getMile() {
        return mile;
    }

    public void setMile(BigDecimal mile) {
        this.mile = mile;
    }

    public Date getCareDate() {
        return careDate;
    }

    public void setCareDate(Date careDate) {
        this.careDate = careDate;
    }

    public String getCareWorker() {
        return careWorker;
    }

    public void setCareWorker(String careWorker) {
        this.careWorker = careWorker;
    }

    public String getSanplNo() {
        return sanplNo;
    }

    public void setSanplNo(String sanplNo) {
        this.sanplNo = sanplNo;
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

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }
}