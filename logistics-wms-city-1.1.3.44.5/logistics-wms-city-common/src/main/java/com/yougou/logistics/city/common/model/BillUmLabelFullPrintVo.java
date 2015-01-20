package com.yougou.logistics.city.common.model;

import java.util.List;

public class BillUmLabelFullPrintVo {

	private String storeNo;
	private String storeName;
	private String address;
	private String bufferName;
	private List<String> labels;
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBufferName() {
		return bufferName;
	}
	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
}
