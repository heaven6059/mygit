package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * @分货单从表
 * @author su.yq
 *
 */
public class BillOmDivideDtl extends BillOmDivideDtlKey {
	
    private String batchNo;//批次

    private String sourceNo;//来源单号

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date operateDate;//操作日期

    private String storeNo;//客户编码

    private String subStoreNo;//子客户编码

    private String expType;//出货通知单类型

    private String expNo;//出货通知单号

    private String locateNo;//波次号

    private String itemNo;//商品编码

    private Long itemId;//商品属性ID

    private BigDecimal packQty;//包装数量

    private BigDecimal itemQty;//商品数量

    private BigDecimal realQty;//实际分货数量

	private String sCellNo;//来源储位编码

    private Long sCellId;//来源储位ID

    private String sContainerNo;//来源容器

    private String dCellNo;//目的储位编码

    private Long dCellId;//目的储位ID

    private String custContainerNo;//客户容器编码

    private String dContainerNo;//目的容器

    private String deliverArea;//发货区货位

    private String status;//状态

    private String lineNo;//线路编码

    private String trunckCellNo;

    private String checkChuteNo;

    private String deliverObj;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date outstockDate;//下架日期

    private String assignName;//指定分货人
    private String assignNameCh;//指定分货人

    private String divideName;//实际分货人
    private String divideNameCh;//实际分货人

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date divideDate;//实际分货日期

    private String dpsCellNo;

    private String aSorterChuteNo;

    @JsonSerialize(using =JsonDateSerializer$19.class)
    private Date expDate;
    
    private String boxNo;//箱号
    
    private String serialNo;//流到编码
    
    private String groupNo;//组编码（箱分组）
    
    private String poNo;//合同号
    
    private String groupName;//店分组
    
    private String ownerName;
    
    private String brandNo;
    
    private String difFlag;
    
    private BigDecimal packageNoRecheckQty;//复核未装箱数量
    
    private String itemType;//属性
    
    private String quality;//品质
    
    private String barcode;//条码
    
    private String supplierNo;//供应商
    
    private int difStatus;//0调整后的状态 1没有调整状态的数据
    
    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	/** 附加属性 **/
    private String storeName;//客户名称
	
	private String itemName;//商品名称
	
	private String statusStr;//显示状态
	
	private int diffQty;//差异数量
	
	private String colorName;//颜色
	
	private String styleNo;//款号
	
	private String sizeNo;//尺寸编码
	
	private String colorNo;
	
	private BigDecimal packageNum;//封箱/装箱数量

    public BigDecimal getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(BigDecimal packageNum) {
		this.packageNum = packageNum;
	}

	public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
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

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public String getLocateNo() {
        return locateNo;
    }

    public void setLocateNo(String locateNo) {
        this.locateNo = locateNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPackQty() {
        return packQty;
    }

    public void setPackQty(BigDecimal packQty) {
        this.packQty = packQty;
    }

    public BigDecimal getItemQty() {
        return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
        this.itemQty = itemQty;
    }

    public BigDecimal getRealQty() {
        return realQty;
    }

    public void setRealQty(BigDecimal realQty) {
        this.realQty = realQty;
    }

    public String getsCellNo() {
        return sCellNo;
    }

    public void setsCellNo(String sCellNo) {
        this.sCellNo = sCellNo;
    }

    public Long getsCellId() {
        return sCellId;
    }

    public void setsCellId(Long sCellId) {
        this.sCellId = sCellId;
    }

    public String getsContainerNo() {
        return sContainerNo;
    }

    public void setsContainerNo(String sContainerNo) {
        this.sContainerNo = sContainerNo;
    }

    public String getdCellNo() {
        return dCellNo;
    }

    public void setdCellNo(String dCellNo) {
        this.dCellNo = dCellNo;
    }

    public Long getdCellId() {
        return dCellId;
    }

    public void setdCellId(Long dCellId) {
        this.dCellId = dCellId;
    }

    public String getCustContainerNo() {
        return custContainerNo;
    }

    public void setCustContainerNo(String custContainerNo) {
        this.custContainerNo = custContainerNo;
    }

    public String getdContainerNo() {
        return dContainerNo;
    }

    public void setdContainerNo(String dContainerNo) {
        this.dContainerNo = dContainerNo;
    }

    public String getDeliverArea() {
        return deliverArea;
    }

    public void setDeliverArea(String deliverArea) {
        this.deliverArea = deliverArea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getTrunckCellNo() {
        return trunckCellNo;
    }

    public void setTrunckCellNo(String trunckCellNo) {
        this.trunckCellNo = trunckCellNo;
    }

    public String getCheckChuteNo() {
        return checkChuteNo;
    }

    public void setCheckChuteNo(String checkChuteNo) {
        this.checkChuteNo = checkChuteNo;
    }

    public String getDeliverObj() {
        return deliverObj;
    }

    public void setDeliverObj(String deliverObj) {
        this.deliverObj = deliverObj;
    }

    public Date getOutstockDate() {
        return outstockDate;
    }

    public void setOutstockDate(Date outstockDate) {
        this.outstockDate = outstockDate;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public String getDivideName() {
        return divideName;
    }

    public void setDivideName(String divideName) {
        this.divideName = divideName;
    }

    public Date getDivideDate() {
        return divideDate;
    }

    public void setDivideDate(Date divideDate) {
        this.divideDate = divideDate;
    }

    public String getDpsCellNo() {
        return dpsCellNo;
    }

    public void setDpsCellNo(String dpsCellNo) {
        this.dpsCellNo = dpsCellNo;
    }

    public String getaSorterChuteNo() {
        return aSorterChuteNo;
    }

    public void setaSorterChuteNo(String aSorterChuteNo) {
        this.aSorterChuteNo = aSorterChuteNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public int getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(int diffQty) {
		this.diffQty = diffQty;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public BigDecimal getPackageNoRecheckQty() {
		return packageNoRecheckQty;
	}

	public void setPackageNoRecheckQty(BigDecimal packageNoRecheckQty) {
		this.packageNoRecheckQty = packageNoRecheckQty;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getAssignNameCh() {
		return assignNameCh;
	}

	public void setAssignNameCh(String assignNameCh) {
		this.assignNameCh = assignNameCh;
	}

	public String getDivideNameCh() {
		return divideNameCh;
	}

	public void setDivideNameCh(String divideNameCh) {
		this.divideNameCh = divideNameCh;
	}

	public String getDifFlag() {
		return difFlag;
	}

	public void setDifFlag(String difFlag) {
		this.difFlag = difFlag;
	}

	public int getDifStatus() {
		return difStatus;
	}

	public void setDifStatus(int difStatus) {
		this.difStatus = difStatus;
	}
}