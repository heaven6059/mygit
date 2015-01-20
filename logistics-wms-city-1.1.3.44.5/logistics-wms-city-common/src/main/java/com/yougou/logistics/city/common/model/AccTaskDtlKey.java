package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-24 13:44:52
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
public class AccTaskDtlKey {
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
     * 明细行号
     */
    private Long rowId;

    /**
     * 
     * {@linkplain #billNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.BILL_NO
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 
     * {@linkplain #billNo}
     * @param billNo the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.BILL_NO
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * 
     * {@linkplain #billType}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.BILL_TYPE
     */
    public String getBillType() {
        return billType;
    }

    /**
     * 
     * {@linkplain #billType}
     * @param billType the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.BILL_TYPE
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.IO_FLAG
     */
    public String getIoFlag() {
        return ioFlag;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     * @param ioFlag the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.IO_FLAG
     */
    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }

    /**
     * 
     * {@linkplain #rowId}
     *
     * @return the value of USR_ZONE_WMS_DEV.ACC_TASK_DTL.ROW_ID
     */
    public Long getRowId() {
        return rowId;
    }

    /**
     * 
     * {@linkplain #rowId}
     * @param rowId the value for USR_ZONE_WMS_DEV.ACC_TASK_DTL.ROW_ID
     */
    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}