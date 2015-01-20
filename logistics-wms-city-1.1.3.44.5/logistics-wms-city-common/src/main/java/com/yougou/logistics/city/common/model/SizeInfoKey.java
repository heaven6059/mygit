package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

public class SizeInfoKey {
    private BigDecimal sizeKind2;

    private String sizeKind;

    private String sizeNo;

    public BigDecimal getSizeKind2() {
        return sizeKind2;
    }

    public void setSizeKind2(BigDecimal sizeKind2) {
        this.sizeKind2 = sizeKind2;
    }

    public String getSizeKind() {
        return sizeKind;
    }

    public void setSizeKind(String sizeKind) {
        this.sizeKind = sizeKind;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }
}