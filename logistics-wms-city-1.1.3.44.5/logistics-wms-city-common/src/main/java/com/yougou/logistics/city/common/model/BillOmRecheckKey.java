package com.yougou.logistics.city.common.model;

/**
 * 
 * 分货复核单  主键
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:17:58
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmRecheckKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 复核单号
     */
    private String recheckNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getRecheckNo() {
        return recheckNo;
    }

    public void setRecheckNo(String recheckNo) {
        this.recheckNo = recheckNo;
    }
}