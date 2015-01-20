package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-23 上午10:26:58
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class SplitDepotLedgerReport {
	
	private String locName;//仓库名称

	private String brandName;//品牌名称

	private String cateName;//大类名称
	
	private String cateOneName;//大类一名称
	
	private String cateTwoName;//大类二名称
	
	private String cateThreeName;//大类三名称

	private String years;//年份
	
	private String gender;//性别

	private String season;//季节

	private String itemType;//类别

	private String quality;//品质

	private String qualityName;//品质名称

	private String sysNo;//品牌库
	
	private Date startDate;//开始时间

	private Date endDate;//结束时间

	private Date selectDate;//查询日期

	/**
	 * 库存数
	 */
	private BigDecimal qty;//库存数量
	

	/**
	 * 上期库存
	 */
	private BigDecimal lastIssueQty;
	
	/**
	 * 本期库存
	 */
	private BigDecimal thisIssueQty;

	/**
	 * 入库
	 */
	private BigDecimal rkCrkQty;//厂入库

	private BigDecimal rkCyrQty;//仓移入

	private BigDecimal rkDthQty;//店退货

	private BigDecimal rkQtrkQty;//其他入库

	/**
	 * 出库
	 */
	private BigDecimal ckCcdQty;//仓出店

	private BigDecimal ckCycQty;//仓移出

	private BigDecimal ckQtckQty;//其他出库

	private BigDecimal ckKbmzhQty;//跨部门转货

	/**
	 * 盘点
	 */
	private BigDecimal pdPyQty;//盘赢

	private BigDecimal pdPkQty;//盘亏

	/**
	 * 调整
	 */
	private BigDecimal tzKctzQty;//库存调整
	private BigDecimal tzKctzTypeQty;//库存属性调整

	/**
	 * 预计到货未收
	 */
	private BigDecimal ydhCrQty;//厂入
	private BigDecimal ydhCyrQty;//仓移入
	private BigDecimal ydhDtcQty;//店退仓

	/**
	 * 已收未验
	 */
	private BigDecimal yswyCrQty;//厂入
	private BigDecimal yswyCyrQty;//仓移入
	private BigDecimal yswyDtcQty;//店退仓

	/**
	 * 已收未验
	 */
	private BigDecimal yshCrQty;//厂入
	private BigDecimal yshCyrQty;//仓移入
	private BigDecimal yshDtcQty;//店退仓

	private String strItemNo;//多条件搜素
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getSelectDate() {
		return selectDate;
	}

	public void setSelectDate(Date selectDate) {
		this.selectDate = selectDate;
	}

	public String getLocName() {
		return locName;
	}

	public void setLocName(String locName) {
		this.locName = locName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getLastIssueQty() {
		return lastIssueQty;
	}

	public void setLastIssueQty(BigDecimal lastIssueQty) {
		this.lastIssueQty = lastIssueQty;
	}

	public BigDecimal getThisIssueQty() {
		return thisIssueQty;
	}

	public void setThisIssueQty(BigDecimal thisIssueQty) {
		this.thisIssueQty = thisIssueQty;
	}

	public BigDecimal getRkCrkQty() {
		return rkCrkQty;
	}

	public void setRkCrkQty(BigDecimal rkCrkQty) {
		this.rkCrkQty = rkCrkQty;
	}

	public BigDecimal getRkCyrQty() {
		return rkCyrQty;
	}

	public void setRkCyrQty(BigDecimal rkCyrQty) {
		this.rkCyrQty = rkCyrQty;
	}

	public BigDecimal getRkDthQty() {
		return rkDthQty;
	}

	public void setRkDthQty(BigDecimal rkDthQty) {
		this.rkDthQty = rkDthQty;
	}

	public BigDecimal getRkQtrkQty() {
		return rkQtrkQty;
	}

	public void setRkQtrkQty(BigDecimal rkQtrkQty) {
		this.rkQtrkQty = rkQtrkQty;
	}

	public BigDecimal getCkCcdQty() {
		return ckCcdQty;
	}

	public void setCkCcdQty(BigDecimal ckCcdQty) {
		this.ckCcdQty = ckCcdQty;
	}

	public BigDecimal getCkCycQty() {
		return ckCycQty;
	}

	public void setCkCycQty(BigDecimal ckCycQty) {
		this.ckCycQty = ckCycQty;
	}

	public BigDecimal getCkQtckQty() {
		return ckQtckQty;
	}

	public void setCkQtckQty(BigDecimal ckQtckQty) {
		this.ckQtckQty = ckQtckQty;
	}

	public BigDecimal getPdPyQty() {
		return pdPyQty;
	}

	public void setPdPyQty(BigDecimal pdPyQty) {
		this.pdPyQty = pdPyQty;
	}

	public BigDecimal getPdPkQty() {
		return pdPkQty;
	}

	public void setPdPkQty(BigDecimal pdPkQty) {
		this.pdPkQty = pdPkQty;
	}

	public BigDecimal getTzKctzQty() {
		return tzKctzQty;
	}

	public void setTzKctzQty(BigDecimal tzKctzQty) {
		this.tzKctzQty = tzKctzQty;
	}

	public BigDecimal getTzKctzTypeQty() {
		return tzKctzTypeQty;
	}

	public void setTzKctzTypeQty(BigDecimal tzKctzTypeQty) {
		this.tzKctzTypeQty = tzKctzTypeQty;
	}

	public BigDecimal getYdhCrQty() {
		return ydhCrQty;
	}

	public void setYdhCrQty(BigDecimal ydhCrQty) {
		this.ydhCrQty = ydhCrQty;
	}

	public BigDecimal getYdhCyrQty() {
		return ydhCyrQty;
	}

	public void setYdhCyrQty(BigDecimal ydhCyrQty) {
		this.ydhCyrQty = ydhCyrQty;
	}

	public BigDecimal getYdhDtcQty() {
		return ydhDtcQty;
	}

	public void setYdhDtcQty(BigDecimal ydhDtcQty) {
		this.ydhDtcQty = ydhDtcQty;
	}

	public BigDecimal getYswyCrQty() {
		return yswyCrQty;
	}

	public void setYswyCrQty(BigDecimal yswyCrQty) {
		this.yswyCrQty = yswyCrQty;
	}

	public BigDecimal getYswyCyrQty() {
		return yswyCyrQty;
	}

	public void setYswyCyrQty(BigDecimal yswyCyrQty) {
		this.yswyCyrQty = yswyCyrQty;
	}

	public BigDecimal getYswyDtcQty() {
		return yswyDtcQty;
	}

	public void setYswyDtcQty(BigDecimal yswyDtcQty) {
		this.yswyDtcQty = yswyDtcQty;
	}

	public BigDecimal getYshCrQty() {
		return yshCrQty;
	}

	public void setYshCrQty(BigDecimal yshCrQty) {
		this.yshCrQty = yshCrQty;
	}

	public BigDecimal getYshCyrQty() {
		return yshCyrQty;
	}

	public void setYshCyrQty(BigDecimal yshCyrQty) {
		this.yshCyrQty = yshCyrQty;
	}

	public BigDecimal getYshDtcQty() {
		return yshDtcQty;
	}

	public void setYshDtcQty(BigDecimal yshDtcQty) {
		this.yshDtcQty = yshDtcQty;
	}

	public String getStrItemNo() {
		return strItemNo;
	}

	public void setStrItemNo(String strItemNo) {
		this.strItemNo = strItemNo;
	}

	public String getQualityName() {
		return qualityName;
	}

	public void setQualityName(String qualityName) {
		this.qualityName = qualityName;
	}

	public String getCateOneName() {
		return cateOneName;
	}

	public void setCateOneName(String cateOneName) {
		this.cateOneName = cateOneName;
	}

	public String getCateTwoName() {
		return cateTwoName;
	}

	public void setCateTwoName(String cateTwoName) {
		this.cateTwoName = cateTwoName;
	}

	public String getCateThreeName() {
		return cateThreeName;
	}

	public void setCateThreeName(String cateThreeName) {
		this.cateThreeName = cateThreeName;
	}

	public BigDecimal getCkKbmzhQty() {
		return ckKbmzhQty;
	}

	public void setCkKbmzhQty(BigDecimal ckKbmzhQty) {
		this.ckKbmzhQty = ckKbmzhQty;
	}

}
