package com.yougou.logistics.city.common.model;

import java.util.Date;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-23 18:31:36
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
public class AccTask {
    /**
     * 序号(由序列SEQ_ACC_TASK生成)
     */
    private Long seqId;

    /**
     * 单据编码
     */
    private String billNo;

    /**
     * 单据类型
     */
    private String billType;

    /**
     * 进出标识(I=入库 O=出)
     */
    private String ioFlag;

    /**
     * 记账类型(0=整单记账 1=明细记账)
     */
    private String accType;

    /**
     * 明细行号(按明细记账时必须写入)
     */
    private Long detailRowid;

    /**
     * 记账申请日期
     */
    private Date createDate;

    /**
     * 记账申请时间
     */
    private Date createTime;

    /**
     * 记账类型(N=未记账 Y=已记账)
     */
    private String accFlag;

    /**
     * 记账日期
     */
    private Date accDate;

    /**
     * 记账开始日期
     */
    private Date accBeginTime;

    /**
     * 记账完成时间
     */
    private Date accEndTime;
    
    /**
     * 单据cn的预下计算标志
     */
    private String outStockFlag;

    /**
     * 
     * {@linkplain #seqId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.SEQ_ID
     */
    public Long getSeqId() {
        return seqId;
    }

    /**
     * 
     * {@linkplain #seqId}
     * @param seqId the value for USR_ZONE_WMS_DEV.ACC_TASK.SEQ_ID
     */
    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    /**
     * 
     * {@linkplain #billNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.BILL_NO
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 
     * {@linkplain #billNo}
     * @param billNo the value for USR_ZONE_WMS_DEV.ACC_TASK.BILL_NO
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * 
     * {@linkplain #billType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.BILL_TYPE
     */
    public String getBillType() {
        return billType;
    }

    /**
     * 
     * {@linkplain #billType}
     * @param billType the value for USR_ZONE_WMS_DEV.ACC_TASK.BILL_TYPE
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.IO_FLAG
     */
    public String getIoFlag() {
        return ioFlag;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     * @param ioFlag the value for USR_ZONE_WMS_DEV.ACC_TASK.IO_FLAG
     */
    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }

    /**
     * 
     * {@linkplain #accType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.ACC_TYPE
     */
    public String getAccType() {
        return accType;
    }

    /**
     * 
     * {@linkplain #accType}
     * @param accType the value for USR_ZONE_WMS_DEV.ACC_TASK.ACC_TYPE
     */
    public void setAccType(String accType) {
        this.accType = accType;
    }

    /**
     * 
     * {@linkplain #detailRowid}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.DETAIL_ROWID
     */
    public Long getDetailRowid() {
        return detailRowid;
    }

    /**
     * 
     * {@linkplain #detailRowid}
     * @param detailRowid the value for USR_ZONE_WMS_DEV.ACC_TASK.DETAIL_ROWID
     */
    public void setDetailRowid(Long detailRowid) {
        this.detailRowid = detailRowid;
    }

    /**
     * 
     * {@linkplain #createDate}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 
     * {@linkplain #createDate}
     * @param createDate the value for USR_ZONE_WMS_DEV.ACC_TASK.CREATE_DATE
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 
     * {@linkplain #createTime}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     * {@linkplain #createTime}
     * @param createTime the value for USR_ZONE_WMS_DEV.ACC_TASK.CREATE_TIME
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * {@linkplain #accFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.ACC_FLAG
     */
    public String getAccFlag() {
        return accFlag;
    }

    /**
     * 
     * {@linkplain #accFlag}
     * @param accFlag the value for USR_ZONE_WMS_DEV.ACC_TASK.ACC_FLAG
     */
    public void setAccFlag(String accFlag) {
        this.accFlag = accFlag;
    }

    /**
     * 
     * {@linkplain #accDate}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.ACC_DATE
     */
    public Date getAccDate() {
        return accDate;
    }

    /**
     * 
     * {@linkplain #accDate}
     * @param accDate the value for USR_ZONE_WMS_DEV.ACC_TASK.ACC_DATE
     */
    public void setAccDate(Date accDate) {
        this.accDate = accDate;
    }

    /**
     * 
     * {@linkplain #accBeginTime}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.ACC_BEGIN_TIME
     */
    public Date getAccBeginTime() {
        return accBeginTime;
    }

    /**
     * 
     * {@linkplain #accBeginTime}
     * @param accBeginTime the value for USR_ZONE_WMS_DEV.ACC_TASK.ACC_BEGIN_TIME
     */
    public void setAccBeginTime(Date accBeginTime) {
        this.accBeginTime = accBeginTime;
    }

    /**
     * 
     * {@linkplain #accEndTime}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK.ACC_END_TIME
     */
    public Date getAccEndTime() {
        return accEndTime;
    }

    /**
     * 
     * {@linkplain #accEndTime}
     * @param accEndTime the value for USR_ZONE_WMS_DEV.ACC_TASK.ACC_END_TIME
     */
    public void setAccEndTime(Date accEndTime) {
        this.accEndTime = accEndTime;
    }

	public String getOutStockFlag() {
		return outStockFlag;
	}

	public void setOutStockFlag(String outStockFlag) {
		this.outStockFlag = outStockFlag;
	}
    
}