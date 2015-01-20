package com.yougou.logistics.city.common.model;

/**
 * 
 * 收货验收单详情 主键
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:08:31
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillImCheckDtlKey {
	/**
	 * 仓别
	 */
    private String locno;

    /**
     * 验收单号
     */
    private String checkNo;

    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 验收序列
     */
    private Integer rowId;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }
}