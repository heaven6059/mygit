package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

public class Lookupdtl extends LookupdtlKey {
    private String itemname;

    private BigDecimal status;

    private BigDecimal auditstatus;

    private String remarks;
    
    private String locno;
    
    private String itemOld;
    
    private String lookupname;
    
    private String lookuptype;
    
    private String sysNo;

    public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

    public String getLocno(){
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    public BigDecimal getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(BigDecimal auditstatus) {
        this.auditstatus = auditstatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	public String getItemOld() {
		return itemOld;
	}

	public void setItemOld(String itemOld) {
		this.itemOld = itemOld;
	}

	public String getLookupname() {
		return lookupname;
	}

	public void setLookupname(String lookupname) {
		this.lookupname = lookupname;
	}

	public String getLookuptype() {
		return lookuptype;
	}

	public void setLookuptype(String lookuptype) {
		this.lookuptype = lookuptype;
	}
	
}