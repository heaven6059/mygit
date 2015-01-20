package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 
 * 容器资料
 * 
 * @author qin.dy
 * @date 2013-9-22 下午2:51:02
 * @copyright yougou.com
 */
public class SysDefcontainer extends SysDefcontainerKey {
	/**
	 * 容器编号前缀 BS-物流箱；BS-原装箱；PS-栈板；RS-笼车
	 */
    private String containerPrefix;

    /**
     * 容器描述
     */
    private String containerDesc;

    /**
     * 长
     */
    private BigDecimal length;

    /**
     * 宽
     */
    private BigDecimal width;

    /**
     * 高
     */
    private BigDecimal height;

    /**
     * 最大容量
     */
    private BigDecimal containerCapacity;

    /**
     * 最大容积比率
     */
    private Short capacityTolerance;

    /**
     * 容器数量
     */
    private Integer availableNoQty;

    /**
     * 容器代码长度
     */
    private Short codeLength;

    /**
     * 状态 0：禁用；1：正常
     */
    private String manageStatus;

    /**
     * 存储收货是否固定容器号标  0：不固定；1：固定
     */
    private String isFixFlag;

    /**
     * 直通收货是否固定容器号标记 0：不固定；1：固定
     */
    private String idFixFlag;

    /**
     * 出货是否固定容器号标记 0：不固定；1：固定
     */
    private String omFixFlag;

    /**
     * 实体容器前缀标识 0：不固定；1：固定
     */
    private String preFlag;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date createtm;

    /**
     * 编辑人
     */
    private String editor;

    /**
     * 编辑时间
     */
    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date edittm;

    private String labelPrefix;

    private String containerMaterial;

    public String getContainerPrefix() {
        return containerPrefix;
    }

    public void setContainerPrefix(String containerPrefix) {
        this.containerPrefix = containerPrefix;
    }

    public String getContainerDesc() {
        return containerDesc;
    }

    public void setContainerDesc(String containerDesc) {
        this.containerDesc = containerDesc;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getContainerCapacity() {
        return containerCapacity;
    }

    public void setContainerCapacity(BigDecimal containerCapacity) {
        this.containerCapacity = containerCapacity;
    }

    public Short getCapacityTolerance() {
        return capacityTolerance;
    }

    public void setCapacityTolerance(Short capacityTolerance) {
        this.capacityTolerance = capacityTolerance;
    }

    public Integer getAvailableNoQty() {
        return availableNoQty;
    }

    public void setAvailableNoQty(Integer availableNoQty) {
        this.availableNoQty = availableNoQty;
    }

    public Short getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Short codeLength) {
        this.codeLength = codeLength;
    }

    public String getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
    }

    public String getIsFixFlag() {
        return isFixFlag;
    }

    public void setIsFixFlag(String isFixFlag) {
        this.isFixFlag = isFixFlag;
    }

    public String getIdFixFlag() {
        return idFixFlag;
    }

    public void setIdFixFlag(String idFixFlag) {
        this.idFixFlag = idFixFlag;
    }

    public String getOmFixFlag() {
        return omFixFlag;
    }

    public void setOmFixFlag(String omFixFlag) {
        this.omFixFlag = omFixFlag;
    }

    public String getPreFlag() {
        return preFlag;
    }

    public void setPreFlag(String preFlag) {
        this.preFlag = preFlag;
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

    public String getLabelPrefix() {
        return labelPrefix;
    }

    public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

    public String getContainerMaterial() {
        return containerMaterial;
    }

    public void setContainerMaterial(String containerMaterial) {
        this.containerMaterial = containerMaterial;
    }
}