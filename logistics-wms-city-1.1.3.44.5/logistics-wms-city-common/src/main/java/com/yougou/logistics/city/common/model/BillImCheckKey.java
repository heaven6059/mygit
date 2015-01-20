package com.yougou.logistics.city.common.model;

/**
 * 
 * 收货验收单 主键
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:02:00
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillImCheckKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 验收单号
     */
    private String checkNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }
}