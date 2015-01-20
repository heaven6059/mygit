package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 
 * 装车单
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:09:51
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmDeliver extends BillOmDeliverKey {
	/**
	 * 委托业主
	 */
    private String ownerNo;

    /**
     * 装车建议单号
     */
    private String loadproposeNo;

    /**
     * 车辆类型编码
     */
    private String cartypeNo;

    /**
     * 车牌号
     */
    private String carPlate;

    /**
     * 发货人
     */
    private String sendName;

    /**
     * 操作状态
     */
    private String status;

    /**
     * 子客户编码
     */
    private String subStoreNo;

    /**
     * 封条号
     */
    private String sealNo;

    /**
     * 传输标志
     */
    private BigDecimal sendFlag;

    /**
     * 板载物流箱数
     */
    private BigDecimal logboxNum;

    /**
     * 配送跟踪标志
     */
    private String dipFlag;

    private String creator;
    
    private String creatorname;
    
    @JsonSerialize(using=JsonDateSerializer$10.class)
    private Date operateDate;
    
    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;
    
    private String editorname;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date edittm;

    /**
     * 出货日期
     */
    @JsonSerialize(using=JsonDateSerializer$10.class)
    private Date expDate;

    /**
     * 配送状态
     */
    private String statusTrans;

    /**
     * 打印次数
     */
    private BigDecimal printcount;

    /**
     * 配送方式
     */
    private String deliverType;

    private String sysNo;

    /**
     * 线路编码
     */
    private String lineNo;

    private String shipperNo;

    private String auditor;
    
    private String auditorname;
    
    private String storeNo;
    private String containerNo;

    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date audittm;
    
    private String statusStr;
    
    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 出货码头编号
     */
    private String dockNo;
    
    private String transFlag;
    
    /**
     * 是否是RF设备作业:0-不是；1-是
     */
    private String isDevice;
    
    private String brandNo;
    
    private BigDecimal sumQty;
    
    public BigDecimal getSumQty() {
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty) {
		this.sumQty = sumQty;
	}

	public String getIsDevice() {
		return isDevice;
	}

	public void setIsDevice(String isDevice) {
		this.isDevice = isDevice;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public String getDockNo() {
		return dockNo;
	}

	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}

	public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getLoadproposeNo() {
        return loadproposeNo;
    }

    public void setLoadproposeNo(String loadproposeNo) {
        this.loadproposeNo = loadproposeNo;
    }

    public String getCartypeNo() {
        return cartypeNo;
    }

    public void setCartypeNo(String cartypeNo) {
        this.cartypeNo = cartypeNo;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubStoreNo() {
        return subStoreNo;
    }

    public void setSubStoreNo(String subStoreNo) {
        this.subStoreNo = subStoreNo;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public BigDecimal getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(BigDecimal sendFlag) {
        this.sendFlag = sendFlag;
    }

    public BigDecimal getLogboxNum() {
        return logboxNum;
    }

    public void setLogboxNum(BigDecimal logboxNum) {
        this.logboxNum = logboxNum;
    }

    public String getDipFlag() {
        return dipFlag;
    }

    public void setDipFlag(String dipFlag) {
        this.dipFlag = dipFlag;
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

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getStatusTrans() {
        return statusTrans;
    }

    public void setStatusTrans(String statusTrans) {
        this.statusTrans = statusTrans;
    }

    public BigDecimal getPrintcount() {
        return printcount;
    }

    public void setPrintcount(BigDecimal printcount) {
        this.printcount = printcount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
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