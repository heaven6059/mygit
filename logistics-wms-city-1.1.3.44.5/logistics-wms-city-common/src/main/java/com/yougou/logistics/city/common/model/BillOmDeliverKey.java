package com.yougou.logistics.city.common.model;

/**
 * 
 * 装车单  主键
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:10:20
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmDeliverKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 装车单号
     */
    private String deliverNo;
   
    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }
}