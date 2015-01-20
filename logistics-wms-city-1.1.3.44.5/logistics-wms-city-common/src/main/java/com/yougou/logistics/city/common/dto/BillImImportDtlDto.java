package com.yougou.logistics.city.common.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.ConBox;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2013-10-15 下午8:23:43
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillImImportDtlDto extends BillImImportDtl {
	private String brandName;
	private String deliverNo;
	private String poNo;
	private String qty;
	private int rowId;
	private String spoNo;
	private String panNo;//父容器号 add wanghb 20141021
	private List<ConBox> boxList=new ArrayList<ConBox>();
	
	
	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getSpoNo() {
		return spoNo;
	}

	public void setSpoNo(String spoNo) {
		this.spoNo = spoNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public List<ConBox> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<ConBox> boxList) {
		this.boxList = boxList;
	}

	

}
