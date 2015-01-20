package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-6-16 下午2:05:15
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class SplitDepotInOutDtlReport {

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

	private Date startContentDate;//开始时间

	private Date endContentDate;//结束时间
	
	private Date backContentDate;//前一天的实物库存时间

	private BigDecimal qty;//库存数量

	private BigDecimal crkQty;//厂入库

	private BigDecimal cyrQty;//仓移入

	private BigDecimal dthQty;//店退货

	private BigDecimal qtrkQty;//其他入库
	
	private BigDecimal rkCytzQty;//入库差异调整

	private BigDecimal ccdQty;//仓出店

	private BigDecimal cycQty;//仓移出

	private BigDecimal qtckQty;//其他出库
	
	private BigDecimal kbmzhQty;//跨部门转货
	
	private BigDecimal ckCytzQty;//出库差异调整

	private BigDecimal pyQty;//盘赢

	private BigDecimal pkQty;//盘亏
	
	private BigDecimal thisIssueQty;//本期库存

	private String strItemNo;//多条件搜素
	
	private String brandNoValues;//品牌库字符串

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

	public Date getStartContentDate() {
		return startContentDate;
	}

	public void setStartContentDate(Date startContentDate) {
		this.startContentDate = startContentDate;
	}

	public Date getEndContentDate() {
		return endContentDate;
	}

	public void setEndContentDate(Date endContentDate) {
		this.endContentDate = endContentDate;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getCrkQty() {
		return crkQty;
	}

	public void setCrkQty(BigDecimal crkQty) {
		this.crkQty = crkQty;
	}

	public BigDecimal getCyrQty() {
		return cyrQty;
	}

	public void setCyrQty(BigDecimal cyrQty) {
		this.cyrQty = cyrQty;
	}

	public BigDecimal getDthQty() {
		return dthQty;
	}

	public void setDthQty(BigDecimal dthQty) {
		this.dthQty = dthQty;
	}

	public BigDecimal getQtrkQty() {
		return qtrkQty;
	}

	public void setQtrkQty(BigDecimal qtrkQty) {
		this.qtrkQty = qtrkQty;
	}

	public BigDecimal getCcdQty() {
		return ccdQty;
	}

	public void setCcdQty(BigDecimal ccdQty) {
		this.ccdQty = ccdQty;
	}

	public BigDecimal getCycQty() {
		return cycQty;
	}

	public void setCycQty(BigDecimal cycQty) {
		this.cycQty = cycQty;
	}

	public BigDecimal getQtckQty() {
		return qtckQty;
	}

	public void setQtckQty(BigDecimal qtckQty) {
		this.qtckQty = qtckQty;
	}

	public BigDecimal getPyQty() {
		return pyQty;
	}

	public void setPyQty(BigDecimal pyQty) {
		this.pyQty = pyQty;
	}

	public BigDecimal getPkQty() {
		return pkQty;
	}

	public void setPkQty(BigDecimal pkQty) {
		this.pkQty = pkQty;
	}

	public String getStrYears() {
		return strYears;
	}

	public void setStrYears(String strYears) {
		this.strYears = strYears;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getStrCateNo() {
		return strCateNo;
	}

	public void setStrCateNo(String strCateNo) {
		this.strCateNo = strCateNo;
	}

	public String getStrItemNo() {
		return strItemNo;
	}

	public void setStrItemNo(String strItemNo) {
		this.strItemNo = strItemNo;
	}

	public String getCateCode() {
		return cateCode;
	}

	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

	public Date getBackContentDate() {
		return backContentDate;
	}

	public void setBackContentDate(Date backContentDate) {
		this.backContentDate = backContentDate;
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

	public BigDecimal getKbmzhQty() {
		return kbmzhQty;
	}

	public void setKbmzhQty(BigDecimal kbmzhQty) {
		this.kbmzhQty = kbmzhQty;
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
