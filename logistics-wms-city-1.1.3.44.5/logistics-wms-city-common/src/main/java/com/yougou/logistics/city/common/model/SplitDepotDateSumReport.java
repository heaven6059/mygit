package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-18 下午12:26:31
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class SplitDepotDateSumReport {

	private String locno;//仓别

	private String onwerNo;//货主

	private String itemNo;//商品编码

	private String brandNo;//品牌编码

	private String strBrandNo;//品牌名称

	private String colorName;//颜色名称

	private String itemName;//名称

	private String cateNo;//大类

	private String cateCode;//大类CODE

	private String strCateNo;//大类显示

	private String years;//年份

	private String strYears;//年份显示

	private String itemType;//类别

	private String quality;//品质

	private String gender;//性别

	private String season;//季节

	private String sysNo;//品牌库

	private Date startDate;//开始时间

	private Date endDate;//结束时间

	private Date selectDate;//查询日期
	
	private String brandNoValues;//品牌字符

	/**
	 * 库存数
	 */
	private BigDecimal qty;//库存数量
	private BigDecimal thisIssueQty;//本期库存

	/**
	 * 入库
	 */
	private BigDecimal rkCrkQty;//厂入库

	private BigDecimal rkCyrQty;//仓移入

	private BigDecimal rkDthQty;//店退货

	private BigDecimal rkQtrkQty;//其他入库
	
	private BigDecimal rkCytzQty;//入库差异调整

	/**
	 * 出库
	 */
	private BigDecimal ckCcdQty;//仓出店

	private BigDecimal ckCycQty;//仓移出

	private BigDecimal ckQtckQty;//其他出库
	
	private BigDecimal ckKbmzhQty;//跨部门转货
	
	private BigDecimal ckZjckQty;//直接出库
	
	private BigDecimal ckCytzQty;//出库差异调整

	/**
	 * 盘点
	 */
	private BigDecimal pdPyQty;//盘赢

	private BigDecimal pdPkQty;//盘亏

	/**
	 * 库存调整
	 */
	private BigDecimal tzKctzQty;//品质调整
	
	private BigDecimal tzKctzTypeQty;//属性调整
	
	private BigDecimal tzKctzCalcQty;//属性调整

	/**
	 * 预计到货未收
	 */
	private BigDecimal ydhCrQty;//厂入
	private BigDecimal ydhCyrQty;//仓移入
	private BigDecimal ydhDtcQty;//店退仓

	private BigDecimal ydhCrBoxQty;//厂入箱数
	private BigDecimal ydhCyrBoxQty;//仓移入箱数
	private BigDecimal ydhDtcBoxQty;//店退仓箱数
	
	/**
	 * 已收未验
	 */
	private BigDecimal yswyCrQty;//厂入
	private BigDecimal yswyCyrQty;//仓移入
	private BigDecimal yswyDtcQty;//店退仓

	private BigDecimal yswyCrBoxQty;//厂入箱数
	private BigDecimal yswyCyrBoxQty;//仓移入箱数
	private BigDecimal yswyDtcBoxQty;//店退仓箱数
	
	/**
	 * 已收未验
	 */
	private BigDecimal yshCrQty;//厂入
	private BigDecimal yshCyrQty;//仓移入
	private BigDecimal yshDtcQty;//店退仓
	private BigDecimal yshCrBoxQty;//厂入箱数
	private BigDecimal yshCyrBoxQty;//仓移入箱数
	private BigDecimal yshDtcBoxQty;//店退仓箱数

	private String strItemNo;//多条件搜素

	public BigDecimal getYdhCrBoxQty() {
		return ydhCrBoxQty;
	}

	public void setYdhCrBoxQty(BigDecimal ydhCrBoxQty) {
		this.ydhCrBoxQty = ydhCrBoxQty;
	}

	public BigDecimal getYdhCyrBoxQty() {
		return ydhCyrBoxQty;
	}

	public void setYdhCyrBoxQty(BigDecimal ydhCyrBoxQty) {
		this.ydhCyrBoxQty = ydhCyrBoxQty;
	}

	public BigDecimal getYdhDtcBoxQty() {
		return ydhDtcBoxQty;
	}

	public void setYdhDtcBoxQty(BigDecimal ydhDtcBoxQty) {
		this.ydhDtcBoxQty = ydhDtcBoxQty;
	}

	public BigDecimal getYswyCrBoxQty() {
		return yswyCrBoxQty;
	}

	public void setYswyCrBoxQty(BigDecimal yswyCrBoxQty) {
		this.yswyCrBoxQty = yswyCrBoxQty;
	}

	public BigDecimal getYswyCyrBoxQty() {
		return yswyCyrBoxQty;
	}

	public void setYswyCyrBoxQty(BigDecimal yswyCyrBoxQty) {
		this.yswyCyrBoxQty = yswyCyrBoxQty;
	}

	public BigDecimal getYswyDtcBoxQty() {
		return yswyDtcBoxQty;
	}

	public void setYswyDtcBoxQty(BigDecimal yswyDtcBoxQty) {
		this.yswyDtcBoxQty = yswyDtcBoxQty;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getOnwerNo() {
		return onwerNo;
	}

	public void setOnwerNo(String onwerNo) {
		this.onwerNo = onwerNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getStrBrandNo() {
		return strBrandNo;
	}

	public void setStrBrandNo(String strBrandNo) {
		this.strBrandNo = strBrandNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCateNo() {
		return cateNo;
	}

	public void setCateNo(String cateNo) {
		this.cateNo = cateNo;
	}

	public String getCateCode() {
		return cateCode;
	}

	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

	public String getStrCateNo() {
		return strCateNo;
	}

	public void setStrCateNo(String strCateNo) {
		this.strCateNo = strCateNo;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getStrYears() {
		return strYears;
	}

	public void setStrYears(String strYears) {
		this.strYears = strYears;
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

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

	public String getStrItemNo() {
		return strItemNo;
	}

	public void setStrItemNo(String strItemNo) {
		this.strItemNo = strItemNo;
	}

	public Date getSelectDate() {
		return selectDate;
	}

	public void setSelectDate(Date selectDate) {
		this.selectDate = selectDate;
	}

	public BigDecimal getYdhDtcQty() {
		return ydhDtcQty;
	}

	public void setYdhDtcQty(BigDecimal ydhDtcQty) {
		this.ydhDtcQty = ydhDtcQty;
	}

	public BigDecimal getYswyDtcQty() {
		return yswyDtcQty;
	}

	public void setYswyDtcQty(BigDecimal yswyDtcQty) {
		this.yswyDtcQty = yswyDtcQty;
	}

	public BigDecimal getYshDtcQty() {
		return yshDtcQty;
	}

	public void setYshDtcQty(BigDecimal yshDtcQty) {
		this.yshDtcQty = yshDtcQty;
	}

	public String getBrandNoValues() {
		return brandNoValues;
	}

	public void setBrandNoValues(String brandNoValues) {
		this.brandNoValues = brandNoValues;
	}

	public BigDecimal getThisIssueQty() {
		return thisIssueQty;
	}

	public void setThisIssueQty(BigDecimal thisIssueQty) {
		this.thisIssueQty = thisIssueQty;
	}

	public BigDecimal getCkKbmzhQty() {
		return ckKbmzhQty;
	}

	public void setCkKbmzhQty(BigDecimal ckKbmzhQty) {
		this.ckKbmzhQty = ckKbmzhQty;
	}

	public BigDecimal getCkZjckQty() {
		return ckZjckQty;
	}

	public void setCkZjckQty(BigDecimal ckZjckQty) {
		this.ckZjckQty = ckZjckQty;
	}

	public BigDecimal getYshCrBoxQty() {
		return yshCrBoxQty;
	}

	public void setYshCrBoxQty(BigDecimal yshCrBoxQty) {
		this.yshCrBoxQty = yshCrBoxQty;
	}

	public BigDecimal getYshCyrBoxQty() {
		return yshCyrBoxQty;
	}

	public void setYshCyrBoxQty(BigDecimal yshCyrBoxQty) {
		this.yshCyrBoxQty = yshCyrBoxQty;
	}

	public BigDecimal getYshDtcBoxQty() {
		return yshDtcBoxQty;
	}

	public void setYshDtcBoxQty(BigDecimal yshDtcBoxQty) {
		this.yshDtcBoxQty = yshDtcBoxQty;
	}

	public BigDecimal getTzKctzCalcQty() {
		return tzKctzCalcQty;
	}

	public void setTzKctzCalcQty(BigDecimal tzKctzCalcQty) {
		this.tzKctzCalcQty = tzKctzCalcQty;
	}

	public BigDecimal getRkCytzQty() {
		return rkCytzQty;
	}

	public void setRkCytzQty(BigDecimal rkCytzQty) {
		this.rkCytzQty = rkCytzQty;
	}

	public BigDecimal getCkCytzQty() {
		return ckCytzQty;
	}

	public void setCkCytzQty(BigDecimal ckCytzQty) {
		this.ckCytzQty = ckCytzQty;
	}
}
