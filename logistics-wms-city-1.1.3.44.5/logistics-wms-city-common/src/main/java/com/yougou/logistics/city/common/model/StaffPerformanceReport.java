package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-7-1 上午10:05:45
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class StaffPerformanceReport {

	private String loginName;//登录账户

	private String userName;//用户名

	private String cateNo;//大类

	private BigDecimal dhshQty;//到货收货	

	private BigDecimal shysQty;//收货验收

	private BigDecimal fhfhQty;//分货复核

	private BigDecimal jhlQty;//拣货量

	private BigDecimal fhdbQty;//复核打包

	private BigDecimal thshQty;//退货收货

	private BigDecimal thysQty;//退货验收

	private BigDecimal qtrkQty;//其他入库

	private BigDecimal qtckQty;//其他出库

	private BigDecimal rksjQty;//入库上架

	private BigDecimal tcsjQty;//退仓上架

	private BigDecimal jsykQty;//即时移库

	private BigDecimal cpQty;//初盘

	private BigDecimal fpQty; //复盘

	private BigDecimal totalQty;//合计
	
	private String cateOneName;//大类一名称
	
	private String cateTwoName;//大类二名称
	
	private String cateThreeName;//大类三名称
	
	private int boxNum;//箱数量
	
	private String showDhshQty;//到货收货显示

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCateNo() {
		return cateNo;
	}

	public void setCateNo(String cateNo) {
		this.cateNo = cateNo;
	}

	public BigDecimal getDhshQty() {
		return dhshQty;
	}

	public void setDhshQty(BigDecimal dhshQty) {
		this.dhshQty = dhshQty;
	}

	public BigDecimal getShysQty() {
		return shysQty;
	}

	public void setShysQty(BigDecimal shysQty) {
		this.shysQty = shysQty;
	}

	public BigDecimal getFhfhQty() {
		return fhfhQty;
	}

	public void setFhfhQty(BigDecimal fhfhQty) {
		this.fhfhQty = fhfhQty;
	}

	public BigDecimal getJhlQty() {
		return jhlQty;
	}

	public void setJhlQty(BigDecimal jhlQty) {
		this.jhlQty = jhlQty;
	}

	public BigDecimal getFhdbQty() {
		return fhdbQty;
	}

	public void setFhdbQty(BigDecimal fhdbQty) {
		this.fhdbQty = fhdbQty;
	}

	public BigDecimal getThshQty() {
		return thshQty;
	}

	public void setThshQty(BigDecimal thshQty) {
		this.thshQty = thshQty;
	}

	public BigDecimal getThysQty() {
		return thysQty;
	}

	public void setThysQty(BigDecimal thysQty) {
		this.thysQty = thysQty;
	}

	public BigDecimal getQtrkQty() {
		return qtrkQty;
	}

	public void setQtrkQty(BigDecimal qtrkQty) {
		this.qtrkQty = qtrkQty;
	}

	public BigDecimal getQtckQty() {
		return qtckQty;
	}

	public void setQtckQty(BigDecimal qtckQty) {
		this.qtckQty = qtckQty;
	}

	public BigDecimal getRksjQty() {
		return rksjQty;
	}

	public void setRksjQty(BigDecimal rksjQty) {
		this.rksjQty = rksjQty;
	}

	public BigDecimal getTcsjQty() {
		return tcsjQty;
	}

	public void setTcsjQty(BigDecimal tcsjQty) {
		this.tcsjQty = tcsjQty;
	}

	public BigDecimal getJsykQty() {
		return jsykQty;
	}

	public void setJsykQty(BigDecimal jsykQty) {
		this.jsykQty = jsykQty;
	}

	public BigDecimal getCpQty() {
		return cpQty;
	}

	public void setCpQty(BigDecimal cpQty) {
		this.cpQty = cpQty;
	}

	public BigDecimal getFpQty() {
		return fpQty;
	}

	public void setFpQty(BigDecimal fpQty) {
		this.fpQty = fpQty;
	}

	public BigDecimal getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}

	public String getCateOneName() {
		return cateOneName;
	}

	public void setCateOneName(String cateOneName) {
		this.cateOneName = cateOneName;
	}

	public String getCateTwoName() {
		return cateTwoName;
	}

	public void setCateTwoName(String cateTwoName) {
		this.cateTwoName = cateTwoName;
	}

	public String getCateThreeName() {
		return cateThreeName;
	}

	public void setCateThreeName(String cateThreeName) {
		this.cateThreeName = cateThreeName;
	}

	public int getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(int boxNum) {
		this.boxNum = boxNum;
	}

	public String getShowDhshQty() {
		return showDhshQty;
	}

	public void setShowDhshQty(String showDhshQty) {
		this.showDhshQty = showDhshQty;
	}
}
