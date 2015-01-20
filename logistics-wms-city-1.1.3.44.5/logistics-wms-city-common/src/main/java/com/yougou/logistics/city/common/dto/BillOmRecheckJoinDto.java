package com.yougou.logistics.city.common.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$10;
import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * TODO: 分货交接明细
 * 
 * @author luo.hl
 * @date 2013-10-28 上午10:45:55
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillOmRecheckJoinDto {
	private String locno;
	private String recheckNo;
	private String labelNo;
	private String itemNo;
	private String itemName;
	private String scanLabelNo;
	private String sizeNo;
	private String colorName;
	private String realQty;
	private String styleNo;
	private String deliverNo;
	private String status;
	private String statusName;
	
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date recheckStart;
	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date recheckEnd;
	
	/**
	 * 交接人
	 */
	private String joinName;
	private String joinnamech;

	/**
	 * 交接时间
	 */
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date joinDate;

	private String statusStr;

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getRecheckNo() {
		return recheckNo;
	}

	public void setRecheckNo(String recheckNo) {
		this.recheckNo = recheckNo;
	}

	public String getLabelNo() {
		return labelNo;
	}

	public void setLabelNo(String labelNo) {
		this.labelNo = labelNo;
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

	public String getScanLabelNo() {
		return scanLabelNo;
	}

	public void setScanLabelNo(String scanLabelNo) {
		this.scanLabelNo = scanLabelNo;
	}

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getRealQty() {
		return realQty;
	}

	public void setRealQty(String realQty) {
		this.realQty = realQty;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public void setStyleNo(String styleNo) {
		this.styleNo = styleNo;
	}

	public Date getEdittm() {
		return edittm;
	}

	public void setEdittm(Date edittm) {
		this.edittm = edittm;
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

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

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

	public String getJoinnamech() {
		return joinnamech;
	}

	public void setJoinnamech(String joinnamech) {
		this.joinnamech = joinnamech;
	}
	
}
