package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.util.Date;

public class BillStatusLogDto extends BillStatusLogKeyDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String locno;

    private String billType;

    private String status;
    
    private String statusName;

    private String description;

    private String operator;

    private Date operatetm;
    
    private String operatorName;
    
    private String poNo;
    
    private String sourceExpNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperatetm() {
        return operatetm;
    }

    public void setOperatetm(Date operatetm) {
        this.operatetm = operatetm;
    }

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getSourceExpNo() {
		return sourceExpNo;
	}

	public void setSourceExpNo(String sourceExpNo) {
		this.sourceExpNo = sourceExpNo;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
    
}