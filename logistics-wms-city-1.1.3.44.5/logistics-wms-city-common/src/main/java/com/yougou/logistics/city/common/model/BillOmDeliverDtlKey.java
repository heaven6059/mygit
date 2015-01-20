package com.yougou.logistics.city.common.model;

/**
 * 
 * 装车单明细主键
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:15:30
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmDeliverDtlKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 委托业主
     */
    private String ownerNo;

    /**
     * 装车单号
     */
    private String deliverNo;

    /**
     * 容器编码
     */
    private String containerNo;

    /**
     * 内部容器序号
     */
    private Integer containerId;

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

    public String getDeliverNo() {
        return deliverNo;
    }

    public void setDeliverNo(String deliverNo) {
        this.deliverNo = deliverNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }
}