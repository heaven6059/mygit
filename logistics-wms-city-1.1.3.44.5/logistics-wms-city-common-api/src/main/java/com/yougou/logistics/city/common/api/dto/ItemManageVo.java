package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class ItemManageVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Map<String,SizeKV>> sizeHead;
	private int total;
	private List<ItemManageResultDto> rows;
	private BigDecimal sumQty;
	private int pageNo;
	private int pageNum;
	private int pageSize;
	
	
	public Map<String, Map<String, SizeKV>> getSizeHead() {
		return sizeHead;
	}
	public void setSizeHead(Map<String, Map<String, SizeKV>> sizeHead) {
		this.sizeHead = sizeHead;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<ItemManageResultDto> getRows() {
		return rows;
	}
	public void setRows(List<ItemManageResultDto> rows) {
		this.rows = rows;
	}
	public BigDecimal getSumQty() {
		return sumQty;
	}
	public void setSumQty(BigDecimal sumQty) {
		this.sumQty = sumQty;
	}
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public static class SizeKV implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String sizeNo;
		private String sizeCode;
		private int idx;
		
		public SizeKV(String sizeNo, String sizeCode, int idx) {
			this.sizeNo = sizeNo;
			this.sizeCode = sizeCode;
			this.idx = idx;
		}
		public String getSizeNo() {
			return sizeNo;
		}
		public void setSizeNo(String sizeNo) {
			this.sizeNo = sizeNo;
		}
		public String getSizeCode() {
			return sizeCode;
		}
		public void setSizeCode(String sizeCode) {
			this.sizeCode = sizeCode;
		}
		public int getIdx() {
			return idx;
		}
		public void setIdx(int idx) {
			this.idx = idx;
		}
		
	}
}
