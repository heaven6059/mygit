/*
 * 类名 com.yougou.logistics.city.common.model.BmPrintLog
 * @author yougoupublic
 * @date  Fri Mar 07 17:41:16 CST 2014
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

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BmPrintLog extends BmPrintLogKey {
	private String startSerial;

	private String endSerial;

	private Integer getQty;

	private Integer sumGetQty;

	private String creator;

	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String storeName;
	
	private String creatorName;

	public String getStartSerial() {
		return startSerial;
	}

	public void setStartSerial(String startSerial) {
		this.startSerial = startSerial;
	}

	public String getEndSerial() {
		return endSerial;
	}

	public void setEndSerial(String endSerial) {
		this.endSerial = endSerial;
	}

	public Integer getGetQty() {
		return getQty;
	}

	public void setGetQty(Integer getQty) {
		this.getQty = getQty;
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

	public Integer getSumGetQty() {
		return sumGetQty;
	}

	public void setSumGetQty(Integer sumGetQty) {
		this.sumGetQty = sumGetQty;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

}