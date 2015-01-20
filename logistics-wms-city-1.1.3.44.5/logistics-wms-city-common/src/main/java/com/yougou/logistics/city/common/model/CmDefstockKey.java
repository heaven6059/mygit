package com.yougou.logistics.city.common.model;
/**
 * 
 * 通道  主键
 * 
 * @author qin.dy
 * @date 2013-9-26 下午3:53:23
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefstockKey {
	/**
	 * 仓别
	 */
    private String locno;

    /**
     * 仓区编码
     */
    private String wareNo;

    /**
     * 储区编码
     */
    private String areaNo;

    /**
     * 通道编码
     */
    private String stockNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getWareNo() {
        return wareNo;
    }

    public void setWareNo(String wareNo) {
        this.wareNo = wareNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }
}