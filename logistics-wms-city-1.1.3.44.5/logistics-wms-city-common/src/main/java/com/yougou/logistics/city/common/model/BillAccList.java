package com.yougou.logistics.city.common.model;

import java.util.Date;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-18 14:03:55
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
public class BillAccList extends BillAccListKey {
    /**
     * 流水号
     */
    private Long serialid;

    /**
     * 记账日期
     */
    private Date createdt;

    /**
     * 记账开始时间
     */
    private Date createtm;

    /**
     * 记账完成时间
     */
    private Date createtmEnd;

    /**
     * 
     * {@linkplain #serialid}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.SERIALID
     */
    public Long getSerialid() {
        return serialid;
    }

    /**
     * 
     * {@linkplain #serialid}
     * @param serialid the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.SERIALID
     */
    public void setSerialid(Long serialid) {
        this.serialid = serialid;
    }

    /**
     * 
     * {@linkplain #createdt}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.CREATEDT
     */
    public Date getCreatedt() {
        return createdt;
    }

    /**
     * 
     * {@linkplain #createdt}
     * @param createdt the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.CREATEDT
     */
    public void setCreatedt(Date createdt) {
        this.createdt = createdt;
    }

    /**
     * 
     * {@linkplain #createtm}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.CREATETM
     */
    public Date getCreatetm() {
        return createtm;
    }

    /**
     * 
     * {@linkplain #createtm}
     * @param createtm the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.CREATETM
     */
    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    /**
     * 
     * {@linkplain #createtmEnd}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.CREATETM_END
     */
    public Date getCreatetmEnd() {
        return createtmEnd;
    }

    /**
     * 
     * {@linkplain #createtmEnd}
     * @param createtmEnd the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.CREATETM_END
     */
    public void setCreatetmEnd(Date createtmEnd) {
        this.createtmEnd = createtmEnd;
    }
}