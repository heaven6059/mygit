/*
 * 类名 com.yougou.logistics.city.common.model.BillChCheckDtl
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillChCheckDtl extends BillChCheckDtlKey {
	private String planNo;

	private String ownerNo;

	private String cellNo;

	private String containerNo;

	private String itemNo;

	private String barcode;

	private BigDecimal packQty;

	private String lotNo;

	private Date produceDate;

	private Date expireDate;

	private String quality;

	private BigDecimal itemQty;

	private BigDecimal checkQty;

	private BigDecimal recheckQty;

	private BigDecimal realQty;

	// 初盘差异数量
	private BigDecimal diffQty;

	// 复盘差异数量
	private BigDecimal recheckDiffQty;

	// 盘点明细差异数量
	private BigDecimal dtlDiffQty;

	private String addFlag;

	private String status;

	private String checkType;
	
	private String checkTypeQty;

	private BigDecimal thirdQty;

	private String checkFlag;

	private String reserved1;

	private String reserved2;

	private String reserved3;

	private String reserved4;

	private String checkWorker;

	private Date checkDate;

	private String differentFlag;

	private String recheckWorker;

	private Date recheckDate;

	private String thirdWorker;

	private Date thirdDate;

	private String itemType;

	private String labelNo;

	private String stockType;

	private String stockValue;

	private String batchSerialNo;

	private String sizeNo;

	private String itemName;

	private String colorName;

	private String styleNo;

	private String brandName;

	private String sourceStatus;

	private String brandNo;

	private String realWorker;
	private String realWorkerName;
	
	private String checkWorkerName;
	private String recheckWorkerName;
	private String thirdWorkerName;
	private int pageNo;
	
	private int pageSize;
	private String colorNo;
	private BigDecimal  totalCheckQty;
	private String v0;
	private String v1;
    private String v2;
    private String v3;
    private String v4;
    private String v5;
    private String v6;
    private String v7;
    private String v8;
    private String v9;
    private String v10;
    private String v11;
    private String v12;
    private String v13;
    private String v14;
    private String v15;
    private String v16;
    private String v17;
    private String v18;
    private String v19;
    private String v20;
    private String v21;
    private String v22;
    private String v23;
    private String v24;
    private String v25;
    private String v26;
    private String v27;
    private String v28;
    private String v29;
    private String v30;
    private String v31;
    private String v32;
    private String v33;
    private String v34;
    private String v35;
    private String v36;
    private String v37;
    private String v38;
    private String v39;
    private String v40;
    private String v41;
    private String v42;
    private String v43;
    private String v44;
    private String v45;
    private String v46;
    private String v47;
    private String v48;
    private String v49;
    private String v50;
    private BigDecimal total;
    //品牌
    private String sysNo;
	private String sysNoStr;
    private String sizeKind;//类别
	public String getPlanNo() {
		return planNo;
	}

	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
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

	public BigDecimal getItemQty() {
		return itemQty;
	}

	public void setItemQty(BigDecimal itemQty) {
		this.itemQty = itemQty;
	}

	public BigDecimal getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(BigDecimal checkQty) {
		this.checkQty = checkQty;
	}

	public BigDecimal getRecheckQty() {
		return recheckQty;
	}

	public void setRecheckQty(BigDecimal recheckQty) {
		this.recheckQty = recheckQty;
	}

	public BigDecimal getRealQty() {
		return realQty;
	}

	public void setRealQty(BigDecimal realQty) {
		this.realQty = realQty;
	}

	public BigDecimal getDiffQty() {
		return diffQty;
	}

	public void setDiffQty(BigDecimal diffQty) {
		this.diffQty = diffQty;
	}

	public BigDecimal getRecheckDiffQty() {
		return recheckDiffQty;
	}

	public void setRecheckDiffQty(BigDecimal recheckDiffQty) {
		this.recheckDiffQty = recheckDiffQty;
	}

	public BigDecimal getDtlDiffQty() {
		return dtlDiffQty;
	}

	public void setDtlDiffQty(BigDecimal dtlDiffQty) {
		this.dtlDiffQty = dtlDiffQty;
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

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public BigDecimal getThirdQty() {
		return thirdQty;
	}

	public void setThirdQty(BigDecimal thirdQty) {
		this.thirdQty = thirdQty;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getCheckWorker() {
		return checkWorker;
	}

	public void setCheckWorker(String checkWorker) {
		this.checkWorker = checkWorker;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getDifferentFlag() {
		return differentFlag;
	}

	public void setDifferentFlag(String differentFlag) {
		this.differentFlag = differentFlag;
	}

	public String getRecheckWorker() {
		return recheckWorker;
	}

	public void setRecheckWorker(String recheckWorker) {
		this.recheckWorker = recheckWorker;
	}

	public Date getRecheckDate() {
		return recheckDate;
	}

	public void setRecheckDate(Date recheckDate) {
		this.recheckDate = recheckDate;
	}

	public String getThirdWorker() {
		return thirdWorker;
	}

	public void setThirdWorker(String thirdWorker) {
		this.thirdWorker = thirdWorker;
	}

	public Date getThirdDate() {
		return thirdDate;
	}

	public void setThirdDate(Date thirdDate) {
		this.thirdDate = thirdDate;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStockValue() {
		return stockValue;
	}

	public void setStockValue(String stockValue) {
		this.stockValue = stockValue;
	}

	public String getBatchSerialNo() {
		return batchSerialNo;
	}

	public void setBatchSerialNo(String batchSerialNo) {
		this.batchSerialNo = batchSerialNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSourceStatus() {
		return sourceStatus;
	}

	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getRealWorker() {
		return realWorker;
	}

	public void setRealWorker(String realWorker) {
		this.realWorker = realWorker;
	}

	public String getCheckWorkerName() {
		return checkWorkerName;
	}

	public void setCheckWorkerName(String checkWorkerName) {
		this.checkWorkerName = checkWorkerName;
	}

	public String getRecheckWorkerName() {
		return recheckWorkerName;
	}

	public void setRecheckWorkerName(String recheckWorkerName) {
		this.recheckWorkerName = recheckWorkerName;
	}

	public String getThirdWorkerName() {
		return thirdWorkerName;
	}

	public void setThirdWorkerName(String thirdWorkerName) {
		this.thirdWorkerName = thirdWorkerName;
	}

	public String getRealWorkerName() {
		return realWorkerName;
	}

	public void setRealWorkerName(String realWorkerName) {
		this.realWorkerName = realWorkerName;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getV0() {
		return v0;
	}

	public void setV0(String v0) {
		this.v0 = v0;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public String getV3() {
		return v3;
	}

	public void setV3(String v3) {
		this.v3 = v3;
	}

	public String getV4() {
		return v4;
	}

	public void setV4(String v4) {
		this.v4 = v4;
	}

	public String getV5() {
		return v5;
	}

	public void setV5(String v5) {
		this.v5 = v5;
	}

	public String getV6() {
		return v6;
	}

	public void setV6(String v6) {
		this.v6 = v6;
	}

	public String getV7() {
		return v7;
	}

	public void setV7(String v7) {
		this.v7 = v7;
	}

	public String getV8() {
		return v8;
	}

	public void setV8(String v8) {
		this.v8 = v8;
	}

	public String getV9() {
		return v9;
	}

	public void setV9(String v9) {
		this.v9 = v9;
	}

	public String getV10() {
		return v10;
	}

	public void setV10(String v10) {
		this.v10 = v10;
	}

	public String getV11() {
		return v11;
	}

	public void setV11(String v11) {
		this.v11 = v11;
	}

	public String getV12() {
		return v12;
	}

	public void setV12(String v12) {
		this.v12 = v12;
	}

	public String getV13() {
		return v13;
	}

	public void setV13(String v13) {
		this.v13 = v13;
	}

	public String getV14() {
		return v14;
	}

	public void setV14(String v14) {
		this.v14 = v14;
	}

	public String getV15() {
		return v15;
	}

	public void setV15(String v15) {
		this.v15 = v15;
	}

	public String getV16() {
		return v16;
	}

	public void setV16(String v16) {
		this.v16 = v16;
	}

	public String getV17() {
		return v17;
	}

	public void setV17(String v17) {
		this.v17 = v17;
	}

	public String getV18() {
		return v18;
	}

	public void setV18(String v18) {
		this.v18 = v18;
	}

	public String getV19() {
		return v19;
	}

	public void setV19(String v19) {
		this.v19 = v19;
	}

	public String getV20() {
		return v20;
	}

	public void setV20(String v20) {
		this.v20 = v20;
	}

	public String getV21() {
		return v21;
	}

	public void setV21(String v21) {
		this.v21 = v21;
	}

	public String getV22() {
		return v22;
	}

	public void setV22(String v22) {
		this.v22 = v22;
	}

	public String getV23() {
		return v23;
	}

	public void setV23(String v23) {
		this.v23 = v23;
	}

	public String getV24() {
		return v24;
	}

	public void setV24(String v24) {
		this.v24 = v24;
	}

	public String getV25() {
		return v25;
	}

	public void setV25(String v25) {
		this.v25 = v25;
	}

	public String getV26() {
		return v26;
	}

	public void setV26(String v26) {
		this.v26 = v26;
	}

	public String getV27() {
		return v27;
	}

	public void setV27(String v27) {
		this.v27 = v27;
	}

	public String getV28() {
		return v28;
	}

	public void setV28(String v28) {
		this.v28 = v28;
	}

	public String getV29() {
		return v29;
	}

	public void setV29(String v29) {
		this.v29 = v29;
	}

	public String getV30() {
		return v30;
	}

	public void setV30(String v30) {
		this.v30 = v30;
	}

	public String getV31() {
		return v31;
	}

	public void setV31(String v31) {
		this.v31 = v31;
	}

	public String getV32() {
		return v32;
	}

	public void setV32(String v32) {
		this.v32 = v32;
	}

	public String getV33() {
		return v33;
	}

	public void setV33(String v33) {
		this.v33 = v33;
	}

	public String getV34() {
		return v34;
	}

	public void setV34(String v34) {
		this.v34 = v34;
	}

	public String getV35() {
		return v35;
	}

	public void setV35(String v35) {
		this.v35 = v35;
	}

	public String getV36() {
		return v36;
	}

	public void setV36(String v36) {
		this.v36 = v36;
	}

	public String getV37() {
		return v37;
	}

	public void setV37(String v37) {
		this.v37 = v37;
	}

	public String getV38() {
		return v38;
	}

	public void setV38(String v38) {
		this.v38 = v38;
	}

	public String getV39() {
		return v39;
	}

	public void setV39(String v39) {
		this.v39 = v39;
	}

	public String getV40() {
		return v40;
	}

	public void setV40(String v40) {
		this.v40 = v40;
	}

	public String getV41() {
		return v41;
	}

	public void setV41(String v41) {
		this.v41 = v41;
	}

	public String getV42() {
		return v42;
	}

	public void setV42(String v42) {
		this.v42 = v42;
	}

	public String getV43() {
		return v43;
	}

	public void setV43(String v43) {
		this.v43 = v43;
	}

	public String getV44() {
		return v44;
	}

	public void setV44(String v44) {
		this.v44 = v44;
	}

	public String getV45() {
		return v45;
	}

	public void setV45(String v45) {
		this.v45 = v45;
	}

	public String getV46() {
		return v46;
	}

	public void setV46(String v46) {
		this.v46 = v46;
	}

	public String getV47() {
		return v47;
	}

	public void setV47(String v47) {
		this.v47 = v47;
	}

	public String getV48() {
		return v48;
	}

	public void setV48(String v48) {
		this.v48 = v48;
	}

	public String getV49() {
		return v49;
	}

	public void setV49(String v49) {
		this.v49 = v49;
	}

	public String getV50() {
		return v50;
	}

	public void setV50(String v50) {
		this.v50 = v50;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getSysNoStr() {
		return sysNoStr;
	}

	public void setSysNoStr(String sysNoStr) {
		this.sysNoStr = sysNoStr;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public String getColorNo() {
		return colorNo;
	}

	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	public BigDecimal getTotalCheckQty() {
		return totalCheckQty;
	}

	public void setTotalCheckQty(BigDecimal totalCheckQty) {
		this.totalCheckQty = totalCheckQty;
	}

	public String getCheckTypeQty() {
		return checkTypeQty;
	}

	public void setCheckTypeQty(String checkTypeQty) {
		this.checkTypeQty = checkTypeQty;
	}

}