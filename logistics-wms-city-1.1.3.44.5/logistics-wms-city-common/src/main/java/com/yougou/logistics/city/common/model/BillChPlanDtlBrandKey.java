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
public class BillChPlanDtlBrandKey {
    /**
     * 仓别代码
     */
    private String locno;

    /**
     * 盘点计划单号
     */
    private String planNo;

    /**
     * 品牌编码
     */
    private String brandNo;

    /**
     * 
     * {@linkplain #locno}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.LOCNO
     */
    public String getLocno() {
        return locno;
    }

    /**
     * 
     * {@linkplain #locno}
     * @param locno the value for USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.LOCNO
     */
    public void setLocno(String locno) {
        this.locno = locno;
    }

    /**
     * 
     * {@linkplain #planNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.PLAN_NO
     */
    public String getPlanNo() {
        return planNo;
    }

    /**
     * 
     * {@linkplain #planNo}
     * @param planNo the value for USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.PLAN_NO
     */
    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    /**
     * 
     * {@linkplain #brandNo}
     *
     * @return the value of USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.BRAND_NO
     */
    public String getBrandNo() {
        return brandNo;
    }

    /**
     * 
     * {@linkplain #brandNo}
     * @param brandNo the value for USR_ZONE_WMS_DEV.BILL_CH_PLAN_DTL_BRAND.BRAND_NO
     */
    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }
}