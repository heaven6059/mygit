package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 
 * 收货验收单详情
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:08:09
 * @version 0.1.0 
 * @copyright yougou.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillImCheckDtl extends BillImCheckDtlKey {
	/**
	 * 商品编码
	 */
	private String itemNo;

	/**
	 * 品牌库编码
	 */
	private String sysNo;

	/**
	 * 条码
	 */
	private String barcode;

	/**
	 * 包装大小
	 */
	private BigDecimal packQty;

	/**
	 * 尺码
	 */
	private String sizeNo;

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * 批号
	 */
	private String lotNo;

	/**
	 * 生产日期
	 */
	private Date produceDate;

	/**
	 * 有效期
	 */
	private Date expireDate;

	/**
	 * 品质
	 */
	private String quality;

	/**
	 * 验收数量
	 */
	private BigDecimal checkQty;
	
	/**
	 * 分货数量
	 */
	private BigDecimal  divideQty;

	/**
	 * 箱号,xh
	 */
	private String boxNo;
	
	/**
	 * 原箱号
	 */
	private String originalBoxNo;

	/**
	 * QC作业人员
	 */
	private String qcWorker;

	/**
	 * 整单验收开始日期
	 */
	private Date checkStartDate;

	/**
	 * 整单验收结束日期
	 */
	private Date checkEndDate;

	/**
	 * IQC状态
	 */
	private String iqcStatus;

	/**
	 * 验收人1
	 */
	private String authorizedWorker;

	/**
	 * 发货数量,outnb
	 */
	private BigDecimal outQty;

	/**
	 * 授权人
	 */
	private String checkWorker2;

	/**
	 * 批次
	 */
	private String batchSerialNo;

	/**
	 * 单据状态
	 */
	private BigDecimal status;

	/**
	 * 订货数量
	 */
	private BigDecimal poQty;
	
	/**
	 * 板号
	 */
	private String panNo;
	
	/**
	 * 新箱号
	 */
	private String loadboxno;
	
	/**
	 * 是否是按箱验收：0-按明细验收（默认）,1-按箱验收,2-按板验收
	 */
	private String iswhole;
	
	private int difQty;

	/**
	 * 验收人1
	 */
	private String checkWorker1;

	private String itemName;

	private String styleNo;

	@JsonIgnore
	private Item item;
	
	private String sourceNo;
	
	private String itemType;
	
	private String itemTypeStr;
	
	private String qualityStr;
	
	private int sourceType = 1;
	
	private String brandNo;
	
	private String receiptNo;
	
	private String isUpdatePan;
	
	/**
	 * 已定位数量
	 */
	private BigDecimal  directQty;
	
	private String checkName1;
	private String editor;
	private String editorName;
	private Date edittm;
	
	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public String getCheckName1() {
		return checkName1;
	}

	public void setCheckName1(String checkName1) {
		this.checkName1 = checkName1;
	}

	public BigDecimal getDirectQty() {
		return directQty;
	}

	public void setDirectQty(BigDecimal directQty) {
		this.directQty = directQty;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
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

	public String getQcWorker() {
		return qcWorker;
	}

	public void setQcWorker(String qcWorker) {
		this.qcWorker = qcWorker;
	}

	public Date getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(Date checkStartDate) {
		this.checkStartDate = checkStartDate;
	}

	public Date getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(Date checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

	public String getIqcStatus() {
		return iqcStatus;
	}

	public void setIqcStatus(String iqcStatus) {
		this.iqcStatus = iqcStatus;
	}

	public String getAuthorizedWorker() {
		return authorizedWorker;
	}

	public void setAuthorizedWorker(String authorizedWorker) {
		this.authorizedWorker = authorizedWorker;
	}

	public BigDecimal getOutQty() {
		return outQty;
	}

	public void setOutQty(BigDecimal outQty) {
		this.outQty = outQty;
	}

	public String getCheckWorker2() {
		return checkWorker2;
	}

	public void setCheckWorker2(String checkWorker2) {
		this.checkWorker2 = checkWorker2;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public BigDecimal getStatus() {
		return status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}

	public BigDecimal getPoQty() {
		return poQty;
	}

	public void setPoQty(BigDecimal poQty) {
		this.poQty = poQty;
		if(null!=poQty && poQty.intValue() == 0){
			sourceType = 0;
		}
	}

	public String getCheckWorker1() {
		return checkWorker1;
	}

	public void setCheckWorker1(String checkWorker1) {
		this.checkWorker1 = checkWorker1;
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

	public BigDecimal getDivideQty() {
		return divideQty;
	}

	public void setDivideQty(BigDecimal divideQty) {
		this.divideQty = divideQty;
	}

	public String getOriginalBoxNo() {
		return originalBoxNo;
	}

	public void setOriginalBoxNo(String originalBoxNo) {
		this.originalBoxNo = originalBoxNo;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeStr() {
		return itemTypeStr;
	}

	public void setItemTypeStr(String itemTypeStr) {
		this.itemTypeStr = itemTypeStr;
	}

	public String getQualityStr() {
		return qualityStr;
	}

	public void setQualityStr(String qualityStr) {
		this.qualityStr = qualityStr;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public int getDifQty() {
		return difQty;
	}

	public void setDifQty(int difQty) {
		this.difQty = difQty;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getIswhole() {
		return iswhole;
	}

	public void setIswhole(String iswhole) {
		this.iswhole = iswhole;
	}

	public String getIsUpdatePan() {
		return isUpdatePan;
	}

	public void setIsUpdatePan(String isUpdatePan) {
		this.isUpdatePan = isUpdatePan;
	}

	public String getLoadboxno() {
		return loadboxno;
	}

	public void setLoadboxno(String loadboxno) {
		this.loadboxno = loadboxno;
	}
}