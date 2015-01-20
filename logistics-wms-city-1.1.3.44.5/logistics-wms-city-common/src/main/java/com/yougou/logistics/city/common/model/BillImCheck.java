package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;
import com.yougou.logistics.city.common.utils.SystemCache;

/**
 * 
 * 收货验收单
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:01:11
 * @version 0.1.0
 * @copyright yougou.com
 */
public class BillImCheck extends BillImCheckKey {
    /**
     * 收货单号 对应收货单号
     */
    private String sImportNo;

    /**
     * 验收汇总单号
     */
    private String sCheckNo;

    /**
     * 预到货通知单号
     */
    private String importNo;

    /**
     * 收货类型
     */
    private String importType;

    /**
     * 供应商编码
     */
    private String supplierNo;

    /**
     * 配送中心,发货方：Cusno
     */
    private String storeNoFrom;

    /**
     * 码头号
     */
    private String dockNo;

    /**
     * 验收人员
     */
    private String checkWorker;

    /**
     * 验收开始时间
     */
    private Date checkStartDate;

    /**
     * 验收结束时间
     */
    @JsonSerialize(using = JsonDateSerializer$10.class)
    private Date checkEndDate;

    /**
     * 打印机组
     */
    private String printerGroupNo;

    /**
     * 打印次数
     */
    private BigDecimal printTimes;

    /**
     * 单据状态
     */
    private BigDecimal status;
    
    private String uptStatus;

    /**
     * 传输状态
     */
    private BigDecimal statusTrans;

    private String creator;
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;

    private Date edittm;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收货日期
     */
    private Date recivedate;

    /**
     * 运输单号,Movenos 托运单号
     */
    private String transNo;

    /**
     * 差异标示
     */
    private String overFlag;

    private String checkType;

    /**
     * 品牌库编码
     */
    private String sysNo;

    private String auditor;

    private Date audittm;

    private String sourceNo;

    private String businessType;

    private String showBusinessType;
    
    private int totalPoQty;
    
    private int totalCheckQty;
    
    private int totalDiffQty;

    private String creatorName;
    private String editorName;
    private String auditorName;
    private String checkName;
    private String sourceType;
    private String sourceTypeName;
    public String getUptStatus() {
		return uptStatus;
	}

	public void setUptStatus(String uptStatus) {
		this.uptStatus = uptStatus;
	}

	public String getsImportNo() {
	return sImportNo;
    }

    public void setsImportNo(String sImportNo) {
	this.sImportNo = sImportNo;
    }

    public String getsCheckNo() {
	return sCheckNo;
    }

    public void setsCheckNo(String sCheckNo) {
	this.sCheckNo = sCheckNo;
    }

    public String getImportNo() {
	return importNo;
    }

    public void setImportNo(String importNo) {
	this.importNo = importNo;
    }

    public String getImportType() {
	return importType;
    }

    public void setImportType(String importType) {
	this.importType = importType;
    }

    public String getSupplierNo() {
	return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
	this.supplierNo = supplierNo;
    }

    public String getStoreNoFrom() {
	return storeNoFrom;
    }

    public void setStoreNoFrom(String storeNoFrom) {
	this.storeNoFrom = storeNoFrom;
    }

    public String getDockNo() {
	return dockNo;
    }

    public void setDockNo(String dockNo) {
	this.dockNo = dockNo;
    }

    public String getCheckWorker() {
	return checkWorker;
    }

    public void setCheckWorker(String checkWorker) {
	this.checkWorker = checkWorker;
    }

    public Date getCheckStartDate() {
	return checkStartDate;
    }

    public void setCheckStartDate(Date checkStartDate) {
	this.checkStartDate = checkStartDate;
    }

    public Date getCheckEndDate() {
	return checkEndDate;
    }

    public void setCheckEndDate(Date checkEndDate) {
	this.checkEndDate = checkEndDate;
    }

    public String getPrinterGroupNo() {
	return printerGroupNo;
    }

    public void setPrinterGroupNo(String printerGroupNo) {
	this.printerGroupNo = printerGroupNo;
    }

    public BigDecimal getPrintTimes() {
	return printTimes;
    }

    public void setPrintTimes(BigDecimal printTimes) {
	this.printTimes = printTimes;
    }

    public BigDecimal getStatus() {
	return status;
    }

    public void setStatus(BigDecimal status) {
	this.status = status;
    }

    public BigDecimal getStatusTrans() {
	return statusTrans;
    }

    public void setStatusTrans(BigDecimal statusTrans) {
	this.statusTrans = statusTrans;
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

    public String getRemark() {
	return remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
    }

    public Date getRecivedate() {
	return recivedate;
    }

    public void setRecivedate(Date recivedate) {
	this.recivedate = recivedate;
    }

    public String getTransNo() {
	return transNo;
    }

    public void setTransNo(String transNo) {
	this.transNo = transNo;
    }

    public String getOverFlag() {
	return overFlag;
    }

    public void setOverFlag(String overFlag) {
	this.overFlag = overFlag;
    }

    public String getSysNo() {
	return sysNo;
    }

    public void setSysNo(String sysNo) {
	this.sysNo = sysNo;
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

    public String getSourceNo() {
	return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
	this.sourceNo = sourceNo;
    }

    public String getCheckType() {
	return checkType;
    }

    public void setCheckType(String checkType) {
	this.checkType = checkType;
    }

    public String getBusinessType() {
	return businessType;
    }

    public void setBusinessType(String businessType) {
	this.showBusinessType = SystemCache.getLookUpName(
		"CITY_IM_CHECK_BUSINESS_TYPE", businessType);
	this.businessType = businessType;
    }

    public String getShowBusinessType() {
	return showBusinessType;
    }

    public void setShowBusinessType(String showBusinessType) {
	this.showBusinessType = showBusinessType;
    }

	public int getTotalPoQty() {
		return totalPoQty;
	}

	public void setTotalPoQty(int totalPoQty) {
		this.totalPoQty = totalPoQty;
	}

	public int getTotalCheckQty() {
		return totalCheckQty;
	}

	public void setTotalCheckQty(int totalCheckQty) {
		this.totalCheckQty = totalCheckQty;
	}

	public int getTotalDiffQty() {
		return totalDiffQty;
	}

	public void setTotalDiffQty(int totalDiffQty) {
		this.totalDiffQty = totalDiffQty;
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

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceTypeName() {
		this.sourceTypeName = SystemCache.getLookUpName(
				"BILL_IM_CHECK_SOURCE_TYPE", this.sourceType);
		return this.sourceTypeName;
	}

	public void setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
	}
	
}