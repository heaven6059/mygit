package com.yougou.logistics.city.common.model;

/**
 * 
 * 称重 主键
 * 
 * @author qin.dy
 * @date 2013-9-29 下午9:32:44
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmCheckWeightKey {
    private String locno;

    private String containerNo;

    private String labelNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }
}