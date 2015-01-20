package com.yougou.logistics.city.common.api.dto;

public class BillStatusLogKeyDto {
    private String billNo;

    private Integer rowId;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }
}