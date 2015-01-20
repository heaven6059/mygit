/*
 * 类名 com.yougou.logistics.city.common.model.BillAccControlKey
 * @author su.yq
 * @date  Mon Jan 13 19:48:58 CST 2014
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

public class BillAccControlKey {
    private String paperType;

    private String ioFlag;

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getIoFlag() {
        return ioFlag;
    }

    public void setIoFlag(String ioFlag) {
        this.ioFlag = ioFlag;
    }
}