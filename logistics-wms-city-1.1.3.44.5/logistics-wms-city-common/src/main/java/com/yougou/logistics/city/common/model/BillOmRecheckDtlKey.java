package com.yougou.logistics.city.common.model;

/**
 * 
 * 分货复核单明细  主键
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:18:42
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmRecheckDtlKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 单据单号
     */
    private String recheckNo;

    /**
     * 内部容器号
     */
    private String containerNo;

    /**
     * 单内序号
     */
    private Long rowId;

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

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
}