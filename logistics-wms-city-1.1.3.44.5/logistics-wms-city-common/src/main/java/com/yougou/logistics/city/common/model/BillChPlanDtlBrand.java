package com.yougou.logistics.city.common.model;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-11-12 14:17:19
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
public class BillChPlanDtlBrand extends BillChPlanDtlBrandKey {
    /**
     * 委托业主编码
     */
    private String ownerNo;

    /**
     * 
     * {@linkplain #ownerNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.OWNER_NO
     */
    public String getOwnerNo() {
        return ownerNo;
    }

    /**
     * 
     * {@linkplain #ownerNo}
     * @param ownerNo the value for USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.OWNER_NO
     */
    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }
}