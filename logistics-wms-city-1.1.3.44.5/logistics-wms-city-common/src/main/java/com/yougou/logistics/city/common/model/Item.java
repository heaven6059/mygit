package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Item {
    private String itemNo;

    private String itemCode;

    private String itemName;

    private String itemLname;

    private String itemEname;

    private String searchCode;

    private String barcode;

    private String cateNo;

    private String colorNo;

    private String sysNo;

    private String itemSpec;

    private String unitName;

    private String packUnit;

    private String packSpec;

    private Short qminOperatePacking;

    private Short packQty;

    private Short skuL;

    private Short skuW;

    private Short skuH;

    private String producingArea;

    private Short stockType;

    private Short itemTypeid;

    private Short itemKind;

    private BigDecimal saletaxrate;

    private String itemStatus;

    private String styleNo;

    private BigDecimal prodays;

    private BigDecimal salePrice;

    private String location;

    private String sizeKind;

    private String mainqdb;

    private String supplierNo;

    private String workItemNo;

    @JsonSerialize(using = JsonDateSerializer$10.class)
    private Date saleDt;

    private BigDecimal salePrice0;

    private String creator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String remarks;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;

    private String editor;

    private String brandNo;

    private BigDecimal cost;

    private BigDecimal cost0;

    private String colorNoStr;

    private String brandNoStr;

    private String cateNoStr;

    private String itemStatusStr;

    private String supplierNoStr;

    private String heeltypeStr;

    private String bottomtypeStr;

    private String seasonStr;

    private String genderStr;

    private String ingredientsStr;

    private String yearsStr;

    private String styleStr;

    private String specialfeaturesStr;

    private String orderfromStr;

    private String liningStr;

    private String seriesStr;

    private String repeatlistingStr;

    private String productlineStr;

    private String monthStr;

    private String otclistingdateStr;

    private String brandstyleStr;

    private String distributionmustStr;

    private String marketsupportStr;

    private String pricerangeStr;

    private String maincolorStr;

    private String secondarycolorStr;

    private String supporttypeStr;

    private String sysNoStr;

    private String cateNo1;

    private String cateNo1Str;

    private String sizeNo;

    private String tBarcode;

    private String colorName;
    
    private String brandName;

    // 下面属性用于增加、修改时前端传过来的数据 add by qindayin
    private BigDecimal untreadQty;
    private String boxNo;
    private BigDecimal checkQty;
    private BigDecimal poQty;
    private BigDecimal outQty;
    private String queryCondition;
    private String locno;
    private String cellNo;
    
    
    private String[] seasonValues;
	private String[] genderValues;

    /**
     * 模糊查询用的字段
     * 
     * @return
     */
    private String itemNoFuzzy;// 商品编码模糊查询

    private String itemCodeFuzzy;// 商品操作码模糊查询

    private String searchCodeFuzzy;// 检索码模糊查询

    private String planNo;

    private String quality;

    private String itemType;

    private String cateOne;
    private String cateTwo;
    private String cateThree;
    public String getQueryCondition() {
	return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
	this.queryCondition = queryCondition;
    }

    public BigDecimal getOutQty() {
	return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
	this.outQty = outQty;
    }

    public String gettBarcode() {
	return tBarcode;
    }

    public void settBarcode(String tBarcode) {
	this.tBarcode = tBarcode;
    }

    public BigDecimal getPoQty() {
	return poQty;
    }

    public void setPoQty(BigDecimal poQty) {
	this.poQty = poQty;
    }

    public BigDecimal getCheckQty() {
	return checkQty;
    }

    public void setCheckQty(BigDecimal checkQty) {
	this.checkQty = checkQty;
    }

    public String getBoxNo() {
	return boxNo;
    }

    public void setBoxNo(String boxNo) {
	this.boxNo = boxNo;
    }

    public BigDecimal getUntreadQty() {
	return untreadQty;
    }

    public void setUntreadQty(BigDecimal untreadQty) {
	this.untreadQty = untreadQty;
    }

    public String getSizeNo() {
	return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
	this.sizeNo = sizeNo;
    }

    public String getItemNo() {
	return itemNo;
    }

    public void setItemNo(String itemNo) {
	this.itemNo = itemNo;
    }

    public String getItemCode() {
	return itemCode;
    }

    public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
    }

    public String getItemName() {
	return itemName;
    }

    public void setItemName(String itemName) {
	this.itemName = itemName;
    }

    public String getItemLname() {
	return itemLname;
    }

    public void setItemLname(String itemLname) {
	this.itemLname = itemLname;
    }

    public String getItemEname() {
	return itemEname;
    }

    public void setItemEname(String itemEname) {
	this.itemEname = itemEname;
    }

    public String getSearchCode() {
	return searchCode;
    }

    public void setSearchCode(String searchCode) {
	this.searchCode = searchCode;
    }

    public String getBarcode() {
	return barcode;
    }

    public void setBarcode(String barcode) {
	this.barcode = barcode;
    }

    public String getCateNo() {
	return cateNo;
    }

    public void setCateNo(String cateNo) {
	this.cateNo = cateNo;
    }

    public String getColorNo() {
	return colorNo;
    }

    public void setColorNo(String colorNo) {
	this.colorNo = colorNo;
    }

    public String getSysNo() {
	return sysNo;
    }

    public void setSysNo(String sysNo) {
	this.sysNo = sysNo;
    }

    public String getItemSpec() {
	return itemSpec;
    }

    public void setItemSpec(String itemSpec) {
	this.itemSpec = itemSpec;
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public String getPackUnit() {
	return packUnit;
    }

    public void setPackUnit(String packUnit) {
	this.packUnit = packUnit;
    }

    public String getPackSpec() {
	return packSpec;
    }

    public void setPackSpec(String packSpec) {
	this.packSpec = packSpec;
    }

    public Short getPackQty() {
	return packQty;
    }

    public void setPackQty(Short packQty) {
	this.packQty = packQty;
    }

    public Short getSkuL() {
	return skuL;
    }

    public void setSkuL(Short skuL) {
	this.skuL = skuL;
    }

    public Short getSkuW() {
	return skuW;
    }

    public void setSkuW(Short skuW) {
	this.skuW = skuW;
    }

    public Short getSkuH() {
	return skuH;
    }

    public void setSkuH(Short skuH) {
	this.skuH = skuH;
    }

    public String getProducingArea() {
	return producingArea;
    }

    public void setProducingArea(String producingArea) {
	this.producingArea = producingArea;
    }

    public Short getStockType() {
	return stockType;
    }

    public void setStockType(Short stockType) {
	this.stockType = stockType;
    }

    public Short getItemTypeid() {
	return itemTypeid;
    }

    public void setItemTypeid(Short itemTypeid) {
	this.itemTypeid = itemTypeid;
    }

    public Short getItemKind() {
	return itemKind;
    }

    public void setItemKind(Short itemKind) {
	this.itemKind = itemKind;
    }

    public BigDecimal getSaletaxrate() {
	return saletaxrate;
    }

    public void setSaletaxrate(BigDecimal saletaxrate) {
	this.saletaxrate = saletaxrate;
    }

    public String getItemStatus() {
	return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
	this.itemStatus = itemStatus;
    }

    public String getStyleNo() {
	return styleNo;
    }

    public void setStyleNo(String styleNo) {
	this.styleNo = styleNo;
    }

    public BigDecimal getProdays() {
	return prodays;
    }

    public void setProdays(BigDecimal prodays) {
	this.prodays = prodays;
    }

    public BigDecimal getSalePrice() {
	return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
	this.salePrice = salePrice;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getSizeKind() {
	return sizeKind;
    }

    public void setSizeKind(String sizeKind) {
	this.sizeKind = sizeKind;
    }

    public String getMainqdb() {
	return mainqdb;
    }

    public void setMainqdb(String mainqdb) {
	this.mainqdb = mainqdb;
    }

    public String getSupplierNo() {
	return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
	this.supplierNo = supplierNo;
    }

    public String getWorkItemNo() {
	return workItemNo;
    }

    public void setWorkItemNo(String workItemNo) {
	this.workItemNo = workItemNo;
    }

    public Date getSaleDt() {
	return saleDt;
    }

    public void setSaleDt(Date saleDt) {
	this.saleDt = saleDt;
    }

    public BigDecimal getSalePrice0() {
	return salePrice0;
    }

    public void setSalePrice0(BigDecimal salePrice0) {
	this.salePrice0 = salePrice0;
    }

    public String getCreator() {
	return creator;
    }

    public void setCreator(String creator) {
	this.creator = creator;
    }

    public Date getCreatetm() {
	return createtm;
    }

    public void setCreatetm(Date createtm) {
	this.createtm = createtm;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public Date getEdittm() {
	return edittm;
    }

    public void setEdittm(Date edittm) {
	this.edittm = edittm;
    }

    public String getEditor() {
	return editor;
    }

    public void setEditor(String editor) {
	this.editor = editor;
    }

    public String getBrandNo() {
	return brandNo;
    }

    public void setBrandNo(String brandNo) {
	this.brandNo = brandNo;
    }

    public BigDecimal getCost() {
	return cost;
    }

    public void setCost(BigDecimal cost) {
	this.cost = cost;
    }

    public BigDecimal getCost0() {
	return cost0;
    }

    public void setCost0(BigDecimal cost0) {
	this.cost0 = cost0;
    }

    public String getColorNoStr() {
	return colorNoStr;
    }

    public void setColorNoStr(String colorNoStr) {
	this.colorNoStr = colorNoStr;
    }

    public String getBrandNoStr() {
	return brandNoStr;
    }

    public void setBrandNoStr(String brandNoStr) {
	this.brandNoStr = brandNoStr;
    }

    public String getCateNoStr() {
	return cateNoStr;
    }

    public void setCateNoStr(String cateNoStr) {
	this.cateNoStr = cateNoStr;
    }

    public String getItemStatusStr() {
	return itemStatusStr;
    }

    public void setItemStatusStr(String itemStatusStr) {
	this.itemStatusStr = itemStatusStr;
    }

    public String getSupplierNoStr() {
	return supplierNoStr;
    }

    public void setSupplierNoStr(String supplierNoStr) {
	this.supplierNoStr = supplierNoStr;
    }

    public String getHeeltypeStr() {
	return heeltypeStr;
    }

    public void setHeeltypeStr(String heeltypeStr) {
	this.heeltypeStr = heeltypeStr;
    }

    public String getBottomtypeStr() {
	return bottomtypeStr;
    }

    public void setBottomtypeStr(String bottomtypeStr) {
	this.bottomtypeStr = bottomtypeStr;
    }

    public String getSeasonStr() {
	return seasonStr;
    }

    public void setSeasonStr(String seasonStr) {
	this.seasonStr = seasonStr;
    }

    public String getGenderStr() {
	return genderStr;
    }

    public void setGenderStr(String genderStr) {
	this.genderStr = genderStr;
    }

    public String getIngredientsStr() {
	return ingredientsStr;
    }

    public void setIngredientsStr(String ingredientsStr) {
	this.ingredientsStr = ingredientsStr;
    }

    public String getYearsStr() {
	return yearsStr;
    }

    public void setYearsStr(String yearsStr) {
	this.yearsStr = yearsStr;
    }

    public String getStyleStr() {
	return styleStr;
    }

    public void setStyleStr(String styleStr) {
	this.styleStr = styleStr;
    }

    public String getSpecialfeaturesStr() {
	return specialfeaturesStr;
    }

    public void setSpecialfeaturesStr(String specialfeaturesStr) {
	this.specialfeaturesStr = specialfeaturesStr;
    }

    public String getOrderfromStr() {
	return orderfromStr;
    }

    public void setOrderfromStr(String orderfromStr) {
	this.orderfromStr = orderfromStr;
    }

    public String getLiningStr() {
	return liningStr;
    }

    public void setLiningStr(String liningStr) {
	this.liningStr = liningStr;
    }

    public String getSeriesStr() {
	return seriesStr;
    }

    public void setSeriesStr(String seriesStr) {
	this.seriesStr = seriesStr;
    }

    public String getRepeatlistingStr() {
	return repeatlistingStr;
    }

    public void setRepeatlistingStr(String repeatlistingStr) {
	this.repeatlistingStr = repeatlistingStr;
    }

    public String getProductlineStr() {
	return productlineStr;
    }

    public void setProductlineStr(String productlineStr) {
	this.productlineStr = productlineStr;
    }

    public String getMonthStr() {
	return monthStr;
    }

    public void setMonthStr(String monthStr) {
	this.monthStr = monthStr;
    }

    public String getOtclistingdateStr() {
	return otclistingdateStr;
    }

    public void setOtclistingdateStr(String otclistingdateStr) {
	this.otclistingdateStr = otclistingdateStr;
    }

    public String getBrandstyleStr() {
	return brandstyleStr;
    }

    public void setBrandstyleStr(String brandstyleStr) {
	this.brandstyleStr = brandstyleStr;
    }

    public String getDistributionmustStr() {
	return distributionmustStr;
    }

    public void setDistributionmustStr(String distributionmustStr) {
	this.distributionmustStr = distributionmustStr;
    }

    public String getMarketsupportStr() {
	return marketsupportStr;
    }

    public void setMarketsupportStr(String marketsupportStr) {
	this.marketsupportStr = marketsupportStr;
    }

    public String getPricerangeStr() {
	return pricerangeStr;
    }

    public void setPricerangeStr(String pricerangeStr) {
	this.pricerangeStr = pricerangeStr;
    }

    public String getMaincolorStr() {
	return maincolorStr;
    }

    public void setMaincolorStr(String maincolorStr) {
	this.maincolorStr = maincolorStr;
    }

    public String getSecondarycolorStr() {
	return secondarycolorStr;
    }

    public void setSecondarycolorStr(String secondarycolorStr) {
	this.secondarycolorStr = secondarycolorStr;
    }

    public String getSupporttypeStr() {
	return supporttypeStr;
    }

    public void setSupporttypeStr(String supporttypeStr) {
	this.supporttypeStr = supporttypeStr;
    }

    public String getSysNoStr() {
	return sysNoStr;
    }

    public void setSysNoStr(String sysNoStr) {
	this.sysNoStr = sysNoStr;
    }

    public String getCateNo1() {
	return cateNo1;
    }

    public void setCateNo1(String cateNo1) {
	this.cateNo1 = cateNo1;
    }

    public String getCateNo1Str() {
	return cateNo1Str;
    }

    public void setCateNo1Str(String cateNo1Str) {
	this.cateNo1Str = cateNo1Str;
    }

    public Short getQminOperatePacking() {
	return qminOperatePacking;
    }

    public void setQminOperatePacking(Short qminOperatePacking) {
	this.qminOperatePacking = qminOperatePacking;
    }

    public String getItemNoFuzzy() {
	return itemNoFuzzy;
    }

    public void setItemNoFuzzy(String itemNoFuzzy) {
	this.itemNoFuzzy = itemNoFuzzy;
    }

    public String getItemCodeFuzzy() {
	return itemCodeFuzzy;
    }

    public void setItemCodeFuzzy(String itemCodeFuzzy) {
	this.itemCodeFuzzy = itemCodeFuzzy;
    }

    public String getSearchCodeFuzzy() {
	return searchCodeFuzzy;
    }

    public void setSearchCodeFuzzy(String searchCodeFuzzy) {
	this.searchCodeFuzzy = searchCodeFuzzy;
    }

    public String getPlanNo() {
	return planNo;
    }

    public void setPlanNo(String planNo) {
	this.planNo = planNo;
    }

    public String getLocno() {
	return locno;
    }

    public void setLocno(String locno) {
	this.locno = locno;
    }

    public String getQuality() {
	return quality;
    }

    public void setQuality(String quality) {
	this.quality = quality;
    }

    public String getItemType() {
	return itemType;
    }

    public void setItemType(String itemType) {
	this.itemType = itemType;
    }

    public String getColorName() {
	return colorName;
    }

    public void setColorName(String colorName) {
	this.colorName = colorName;
    }

	public String getCateOne() {
		return cateOne;
	}

	public void setCateOne(String cateOne) {
		this.cateOne = cateOne;
	}

	public String getCateTwo() {
		return cateTwo;
	}

	public void setCateTwo(String cateTwo) {
		this.cateTwo = cateTwo;
	}

	public String getCateThree() {
		return cateThree;
	}

	public void setCateThree(String cateThree) {
		this.cateThree = cateThree;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String[] getSeasonValues() {
		return seasonValues;
	}

	public void setSeasonValues(String[] seasonValues) {
		this.seasonValues = seasonValues;
	}

	public String[] getGenderValues() {
		return genderValues;
	}

	public void setGenderValues(String[] genderValues) {
		this.genderValues = genderValues;
	}
}