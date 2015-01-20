/*
 * 类名 com.yougou.logistics.city.common.model.BillLocateRuleDtl
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
package com.yougou.logistics.city.common.model;

public class BillLocateRuleDtl extends BillLocateRuleDtlKey {
    private String ruleCatename;

    public String getRuleCatename() {
        return ruleCatename;
    }

    public void setRuleCatename(String ruleCatename) {
        this.ruleCatename = ruleCatename;
    }
}