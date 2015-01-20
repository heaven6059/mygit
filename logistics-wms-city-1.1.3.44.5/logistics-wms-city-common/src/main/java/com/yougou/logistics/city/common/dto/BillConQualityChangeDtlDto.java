package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.BillConQualityChangeDtl;

/**
 * TODO: 品质转换
 * 
 * @author luo.hl
 * @date 2013-10-24 下午2:11:23
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillConQualityChangeDtlDto extends BillConQualityChangeDtl {
	private String locno;

	private String ownerNo;

	private String changeNo;

	private String sCellNo;

	private String sQuality;

	private String dQuality;

	private String itemNo;

	private String itemName;

	private String colorName;

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getChangeNo() {
		return changeNo;
	}

	public void setChangeNo(String changeNo) {
		this.changeNo = changeNo;
	}

	public String getsCellNo() {
		return sCellNo;
	}

	public void setsCellNo(String sCellNo) {
		this.sCellNo = sCellNo;
	}

	public String getsQuality() {
		return sQuality;
	}

	public void setsQuality(String sQuality) {
		this.sQuality = sQuality;
	}

	public String getdQuality() {
		return dQuality;
	}

	public void setdQuality(String dQuality) {
		this.dQuality = dQuality;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
