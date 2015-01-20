package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 
 * 分货复核单明细
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:18:20
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BillOmRecheckDtl extends BillOmRecheckDtlKey {
	/**
	 * 委托业主
	 */
	private String ownerNo;

	/**
	 * 商品编码
	 */
	private String itemNo;

	/**
	 * 商品属性id  0:进货；1：出货补货；2；普通补货；3：出货；4：报损；5：退厂
	 */
	private Long itemId;

	/**
	 * 商品数量
	 */
	private BigDecimal itemQty;

	/**
	 * 实际数量
	 */
	private BigDecimal realQty;

	/**
	 * 是否新增品质  是否新增品项，0：不新增；1：新增
	 */
	private String addFlag;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 指定复核人员
	 */
	private String assignName;
	private String assignnamech;

	/**
	 * 实际复核人员
	 */
	private String recheckName;
	private String rechecknamech;

	/**
	 * 实际复核时间
	 */
	private Date recheckDate;

	/**
	 * 出货单号
	 */
	private String expNo;

	/**
	 * 出货类型
	 */
	private String expType;

	/**
	 * 出货日期
	 */
	private Date expDate;

	/**
	 * 包装数量
	 */
	private BigDecimal packQty;

	/**
	 * 预留字段
	 */
	private String recheckName2;
	private String rechecknamech2;

	/**
	 * 尺码
	 */
	private String sizeNo;
	
	private String sizeCode;

	/**
	 * 箱号
	 */
	private String boxNo;
	
	/**
	 * 交接人
	 */
	private String joinName;
	private String joinnamech;
	
	/**
	 * 交接时间
	 */
	private Date joinDate;

	private BigDecimal totalRealQty;//实际复核数量

	private String scanLabelNo;//外箱码

	private String itemName;

	private String styleNo;

	private String colorName;

	private String poNo;

	private String statusStr;

	private String sysNo;

	private String sizeKind;
	
	private String brandNo;
	private String editor;
	private String editorname;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date recheckStart;
	
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date recheckEnd;
	
	private Long boxRowId;
	
	private BigDecimal recheckQty;
	
	private BigDecimal diffQty;
	
	private BigDecimal packageNoRecheckQty;
	
	private String ownerName;
	
	private String sourceType;
	
	public Date getRecheckStart() {
		return recheckStart;
	}

	public void setRecheckStart(Date recheckStart) {
		this.recheckStart = recheckStart;
	}

	public Date getRecheckEnd() {
		return recheckEnd;
	}

	public void setRecheckEnd(Date recheckEnd) {
		this.recheckEnd = recheckEnd;
	}

	public String getSizeNo() {
		return sizeNo;
	}
	
	private String storeNo;
	
	private String storeName;
	
	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	private Item item;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public String getAddFlag() {
		return addFlag;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

	public String getRecheckName() {
		return recheckName;
	}

	public void setRecheckName(String recheckName) {
		this.recheckName = recheckName;
	}

	public Date getRecheckDate() {
		return recheckDate;
	}

	public void setRecheckDate(Date recheckDate) {
		this.recheckDate = recheckDate;
	}

	public String getExpNo() {
		return expNo;
	}

	public void setExpNo(String expNo) {
		this.expNo = expNo;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getRecheckName2() {
		return recheckName2;
	}

	public void setRecheckName2(String recheckName2) {
		this.recheckName2 = recheckName2;
	}

	public BigDecimal getTotalRealQty() {
		return totalRealQty;
	}

	public void setTotalRealQty(BigDecimal totalRealQty) {
		this.totalRealQty = totalRealQty;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public String getScanLabelNo() {
		return scanLabelNo;
	}

	public void setScanLabelNo(String scanLabelNo) {
		this.scanLabelNo = scanLabelNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditorname() {
		return editorname;
	}

	public void setEditorname(String editorname) {
		this.editorname = editorname;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public Long getBoxRowId() {
		return boxRowId;
	}

	public void setBoxRowId(Long boxRowId) {
		this.boxRowId = boxRowId;
	}

	public String getJoinName() {
		return joinName;
	}

	public void setJoinName(String joinName) {
		this.joinName = joinName;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

	public BigDecimal getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(BigDecimal diffQty) {
		this.diffQty = diffQty;
	}

	public BigDecimal getPackageNoRecheckQty() {
		return packageNoRecheckQty;
	}

	public void setPackageNoRecheckQty(BigDecimal packageNoRecheckQty) {
		this.packageNoRecheckQty = packageNoRecheckQty;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getAssignnamech() {
		return assignnamech;
	}

	public void setAssignnamech(String assignnamech) {
		this.assignnamech = assignnamech;
	}

	public String getRechecknamech() {
		return rechecknamech;
	}

	public void setRechecknamech(String rechecknamech) {
		this.rechecknamech = rechecknamech;
	}

	public String getRechecknamech2() {
		return rechecknamech2;
	}

	public void setRechecknamech2(String rechecknamech2) {
		this.rechecknamech2 = rechecknamech2;
	}

	public String getJoinnamech() {
		return joinnamech;
	}

	public void setJoinnamech(String joinnamech) {
		this.joinnamech = joinnamech;
	}
	
}