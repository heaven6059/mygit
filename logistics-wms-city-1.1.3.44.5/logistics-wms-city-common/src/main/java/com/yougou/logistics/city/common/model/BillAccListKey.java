package com.yougou.logistics.city.common.model;

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
public class BillAccListKey {
    /**
     * 单据号
     */
    private String paperNo;

    /**
     * 单据类型
     */
    private String paperType;

    /**
     * 进出标识(I-入库 O-出)
     */
    private String ioFlag;

    /**
     * 
     * {@linkplain #paperNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.PAPER_NO
     */
    public String getPaperNo() {
        return paperNo;
    }

    /**
     * 
     * {@linkplain #paperNo}
     * @param paperNo the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.PAPER_NO
     */
    public void setPaperNo(String paperNo) {
        this.paperNo = paperNo;
    }

    /**
     * 
     * {@linkplain #paperType}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.PAPER_TYPE
     */
    public String getPaperType() {
        return paperType;
    }

    /**
     * 
     * {@linkplain #paperType}
     * @param paperType the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.PAPER_TYPE
     */
    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_ACC_LIST.IO_FLAG
     */
    public String getIoFlag() {
        return ioFlag;
    }

    /**
     * 
     * {@linkplain #ioFlag}
     * @param ioFlag the value for USR_ZONE_WMS_DEV.BILL_ACC_LIST.IO_FLAG
     */
    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }
}