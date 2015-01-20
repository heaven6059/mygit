package com.yougou.logistics.city.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-12-19 下午1:49:09
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillChCheckDto extends BillChCheck {
	private Integer cellCount;
	private Integer itemCount;
	private Integer realCount;
	private String differentFlag;
	private String brandNo;
	private String limitBrandFlag;
	private BigDecimal itemQty;//计划数量
	
	private BigDecimal differlCount;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date planDate;
	
	public Integer getCellCount() {
		return cellCount;
	}

	public void setCellCount(Integer cellCount) {
		this.cellCount = cellCount;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public Integer getRealCount() {
		return realCount;
	}

	public void setRealCount(Integer realCount) {
		this.realCount = realCount;
	}

	public String getDifferentFlag() {
		return differentFlag;
	}

	public void setDifferentFlag(String differentFlag) {
		this.differentFlag = differentFlag;
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

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
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
