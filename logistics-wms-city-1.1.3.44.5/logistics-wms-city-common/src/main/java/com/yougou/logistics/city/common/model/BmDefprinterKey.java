package com.yougou.logistics.city.common.model;

/**
 * 
 * 打印机 主键
 * 
 * @author qin.dy
 * @date 2013-11-1 下午2:30:45
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BmDefprinterKey {
	/**
	 * 仓别
	 */
    private String locno;
    /**
     * 打印机代码
     */
    private String printerNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getPrinterNo() {
        return printerNo;
    }

    public void setPrinterNo(String printerNo) {
        this.printerNo = printerNo;
    }
}