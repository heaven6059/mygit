package com.yougou.logistics.city.common.model;

/**
 * 
 *储位 主键
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:15:56
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefcellKey {
	/**
	 * 仓别
	 */
    private String locno;

    /**
     * 储位编码
     */
    private String cellNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }
}