/*
 * 类名 com.yougou.logistics.city.common.model.CmDefstock
 * @author qin.dy
 * @date  Wed Sep 25 16:43:55 CST 2013
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

/**
 * 	通道
 * 
 * @author qin.dy
 * @date 2013-9-25 下午4:48:28
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefstock extends CmDefstockKey {
	/**
	 * 通道全程
	 */
    private String aStockNo;

    /**
     * 通道格数
     */
    private Short qStockX;

    /**
     * 储格位数
     */
    private Short qBayX;

    /**
     * 通道层数
     */
    private Short qStockY;

    /**
     * 储区类型
     */
    private String areaType;

    /**
     * 混载标志  0:不可混；1：同商品不同属性混；2：不同商品混
     */
    private Short mixFlag;

    /**
     * 供应商混载标志  退货时是否混供应商.0:不混供应商；1：混供应商'
     */
    private String mixSupplier;

    /**
     * 拣货标志  0：非拣货区；1：拣货区
     */
    private String areaPick;

    /**
     * 通道升降序设置
     */
    private String orderType;

    /**
     * 货架状态
     */
    private String stockStatus;

    /**
     * 最大板数
     */
    private Short maxQty;

    /**
     * 最大重量
     */
    private BigDecimal maxWeight;

    /**
     * 最大材积
     */
    private BigDecimal maxVolume;

    /**
     * 限制入库类型  0->标准堆叠比率；1->件数或箱数；默认为0
     */
    private String limitType;

    /**
     * 限制比率  0：表示不限制，100：表示必须满板'
     */
    private Short limitRate;

    /**
     * 是否试算物流箱  0：不做物流箱试算；1：做虚拟物流箱试算；2：按实际物流箱做试算
     */
    private String bPick;

    /**
     * 是否A类储区  0：非A类储区；1：A类储区
     */
    private String aFlag;

    /**
     * 总可用储位数
     */
    private Integer sumCanuseCellno;

    /**
     * 已占储位数
     */
    private Integer usedCell;

    /**
     * 拣货标示
     */
    private String pickFlag;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    /**
     * 最大箱数
     */
    private BigDecimal maxCase;
    
    private String areaName;//库区
    
    private String wareName;//

    private String ownerNo;
    
    /**
	 * 储区品质    0-9为良品，A-Z为不良品
	 */
	private String areaQuality;
	/**
	 * 商品类型
	 */
	private String itemType;
	
	/**
	 * 创建人中文名次
	 */
	private String creatorName;
	/**
	 * 修改人中文名称
	 */
	private String editorName;
	
	
    public String getaStockNo() {
        return aStockNo;
    }

    public void setaStockNo(String aStockNo) {
        this.aStockNo = aStockNo;
    }

    public Short getqStockX() {
        return qStockX;
    }

    public void setqStockX(Short qStockX) {
        this.qStockX = qStockX;
    }

    public Short getqBayX() {
        return qBayX;
    }

    public void setqBayX(Short qBayX) {
        this.qBayX = qBayX;
    }

    public Short getqStockY() {
        return qStockY;
    }

    public void setqStockY(Short qStockY) {
        this.qStockY = qStockY;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Short getMixFlag() {
        return mixFlag;
    }

    public void setMixFlag(Short mixFlag) {
        this.mixFlag = mixFlag;
    }

    public String getMixSupplier() {
        return mixSupplier;
    }

    public void setMixSupplier(String mixSupplier) {
        this.mixSupplier = mixSupplier;
    }

    public String getAreaPick() {
        return areaPick;
    }

    public void setAreaPick(String areaPick) {
        this.areaPick = areaPick;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Short getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Short maxQty) {
        this.maxQty = maxQty;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume) {
        this.maxVolume = maxVolume;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public Short getLimitRate() {
        return limitRate;
    }

    public void setLimitRate(Short limitRate) {
        this.limitRate = limitRate;
    }

    public String getbPick() {
        return bPick;
    }

    public void setbPick(String bPick) {
        this.bPick = bPick;
    }

    public String getaFlag() {
        return aFlag;
    }

    public void setaFlag(String aFlag) {
        this.aFlag = aFlag;
    }

    public Integer getSumCanuseCellno() {
        return sumCanuseCellno;
    }

    public void setSumCanuseCellno(Integer sumCanuseCellno) {
        this.sumCanuseCellno = sumCanuseCellno;
    }

    public Integer getUsedCell() {
        return usedCell;
    }

    public void setUsedCell(Integer usedCell) {
        this.usedCell = usedCell;
    }

    public String getPickFlag() {
        return pickFlag;
    }

    public void setPickFlag(String pickFlag) {
        this.pickFlag = pickFlag;
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

    public BigDecimal getMaxCase() {
        return maxCase;
    }

    public void setMaxCase(BigDecimal maxCase) {
        this.maxCase = maxCase;
    }

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getAreaQuality() {
		return areaQuality;
	}

	public void setAreaQuality(String areaQuality) {
		this.areaQuality = areaQuality;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	
}