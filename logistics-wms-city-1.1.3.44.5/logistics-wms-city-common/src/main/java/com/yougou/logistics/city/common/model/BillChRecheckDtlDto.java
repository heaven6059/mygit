package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-12-20 下午4:49:50
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillChRecheckDtlDto extends BillChRecheckDtl {
	private int cellCount;
	private int itemCount;
	private int realCount;
	private String planType;
	private String brandNo;
	private String limitBrandFlag;
	
	private BigDecimal differlCount;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date planDate;
	public int getCellCount() {
		return cellCount;
	}

	public void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getRealCount() {
		return realCount;
	}

	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getLimitBrandFlag() {
		return limitBrandFlag;
	}

	public void setLimitBrandFlag(String limitBrandFlag) {
		this.limitBrandFlag = limitBrandFlag;
	}

	public BigDecimal getDifferlCount() {
		return differlCount;
	}

	public void setDifferlCount(BigDecimal differlCount) {
		this.differlCount = differlCount;
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

}
