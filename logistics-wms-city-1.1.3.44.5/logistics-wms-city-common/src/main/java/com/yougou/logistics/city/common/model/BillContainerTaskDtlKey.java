package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
public class BillContainerTaskDtlKey {
    /**
     * 仓别
     */
    private String locno;

    /**
     * 单据编号
     */
    private String contaskNo;

    /**
     * 序号
     */
    private Long rowId;

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #contaskNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CONTASK_NO
     */
    public String getContaskNo() {
        return contaskNo;
    }

    /**
     * 
     * {@linkplain #contaskNo}
     * @param contaskNo the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.CONTASK_NO
     */
    public void setContaskNo(String contaskNo) {
        this.contaskNo = contaskNo;
    }

    /**
     * 
     * {@linkplain #rowId}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.ROW_ID
     */
    public Long getRowId() {
        return rowId;
    }

    /**
     * 
     * {@linkplain #rowId}
     * @param rowId the value for USR_ZONE_WMS_DEV.BILL_CONTAINER_TASK_DTL.ROW_ID
     */
    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}