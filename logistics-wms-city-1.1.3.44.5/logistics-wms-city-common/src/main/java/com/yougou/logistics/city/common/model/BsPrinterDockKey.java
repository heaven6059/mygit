/*
 * 类名 com.yougou.logistics.city.common.model.BsPrinterDockKey
 * @author luo.hl
 * @date  Fri Nov 01 15:18:23 CST 2013
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

public class BsPrinterDockKey {
    private String locno;

    private String dockNo;

    private String printerGroupNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getDockNo() {
        return dockNo;
    }

    public void setDockNo(String dockNo) {
        this.dockNo = dockNo;
    }

    public String getPrinterGroupNo() {
        return printerGroupNo;
    }

    public void setPrinterGroupNo(String printerGroupNo) {
        this.printerGroupNo = printerGroupNo;
    }
}