package com.yougou.logistics.city.common.model;

public class LookupKey {
    private Short systemid;

    private String lookupcode;

    private String lookuptype;

    public Short getSystemid() {
        return systemid;
    }

    public void setSystemid(Short systemid) {
        this.systemid = systemid;
    }

    public String getLookupcode() {
        return lookupcode;
    }

    public void setLookupcode(String lookupcode) {
        this.lookupcode = lookupcode;
    }

    public String getLookuptype() {
        return lookuptype;
    }

    public void setLookuptype(String lookuptype) {
        this.lookuptype = lookuptype;
    }
}