/*
 * 类名 com.yougou.logistics.city.common.model.BillImReceiptDtl
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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

public class BillImReceiptDtl extends BillImReceiptDtlKey {
	private String importNo;

	private String boxNo;

	private String qcWorker;

	private String iqcStatus;

	private String checkWorker1;

	private String checkWorker2;

	private String batchSerialNo;

	private String status;

	private String itemNo;

	private String itemName;

	private String styleNo;

	private String unitName;

	private String colorName;

	private String brandName;

	private String sizeNo;

	private BigDecimal packQty;

	private BigDecimal receiptQty;

	private BigDecimal checkQty;

	private BigDecimal divideQty;

	private BigDecimal poQty;

	private String deliverNo;

	private String poNo;

	private String barcode;

	private String sysNo;

	private String originalBoxNo;

	private String itemType;

	private String quality;

	private String itemTypeStr;

	private String qualityStr;

	private String cellNo;

	private BigDecimal cellId;

	private String brandNo;

	private String sizeCode;

	private String sizeKind;

	private BigDecimal allCount;
	
	private String checkNo;
	
	private int boxNum;
	
	private String panNo;//父容器号 add wanghb 20141021
	
	private String difFlag;
	
	private String creator;
	
	private Date createtm;
	
	private String editor;
	
	private Date edittm;

	private String editorName;
	
	private String checkName1;//收货人中文名称1
	private String checkName2;//收货人中文名称2
	
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

	public String getImportNo() {
		return importNo;
	}

	public void setImportNo(String importNo) {
		this.importNo = importNo;
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

	public String getIqcStatus() {
		return iqcStatus;
	}

	public void setIqcStatus(String iqcStatus) {
		this.iqcStatus = iqcStatus;
	}

	public String getCheckWorker1() {
		return checkWorker1;
	}

	public void setCheckWorker1(String checkWorker1) {
		this.checkWorker1 = checkWorker1;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}

	public BigDecimal getReceiptQty() {
		return receiptQty;
	}

	public void setReceiptQty(BigDecimal receiptQty) {
		this.receiptQty = receiptQty;
	}

	public BigDecimal getCheckQty() {
		return checkQty;
	}

	public void setCheckQty(BigDecimal checkQty) {
		this.checkQty = checkQty;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public BigDecimal getPoQty() {
		return poQty;
	}

	public void setPoQty(BigDecimal poQty) {
		this.poQty = poQty;
	}

	public BigDecimal getDivideQty() {
		return divideQty;
	}

	public void setDivideQty(BigDecimal divideQty) {
		this.divideQty = divideQty;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getOriginalBoxNo() {
		return originalBoxNo;
	}

	public void setOriginalBoxNo(String originalBoxNo) {
		this.originalBoxNo = originalBoxNo;
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

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public BigDecimal getCellId() {
		return cellId;
	}

	public void setCellId(BigDecimal cellId) {
		this.cellId = cellId;
	}

	public String getBrandNo() {
		return brandNo;
	}

	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}

	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	public String getSizeKind() {
		return sizeKind;
	}

	public void setSizeKind(String sizeKind) {
		this.sizeKind = sizeKind;
	}

	public BigDecimal getAllCount() {
		return allCount;
	}

	public void setAllCount(BigDecimal allCount) {
		this.allCount = allCount;
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

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public int getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(int boxNum) {
		this.boxNum = boxNum;
	}

	public String getCheckName1() {
		return checkName1;
	}

	public void setCheckName1(String checkName1) {
		this.checkName1 = checkName1;
	}

	public String getCheckName2() {
		return checkName2;
	}

	public void setCheckName2(String checkName2) {
		this.checkName2 = checkName2;
	}

	public String getDifFlag() {
		return difFlag;
	}

	public void setDifFlag(String difFlag) {
		this.difFlag = difFlag;
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

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
}