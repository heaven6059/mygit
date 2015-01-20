package com.yougou.logistics.city.common.api.dto;

import java.io.Serializable;

/**
 * 	储位容器库存记帐Vo
 * @author wugy
 * @date  2014-07-15 17:22:25
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
public class AccConInfoDto  implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
     * 容器编码
     */
    private String conNo;

    /**
     * 储位编码
     */
    private String cellNo;
    

	public String getConNo() {
		return conNo;
	}

	public void setConNo(String conNo) {
		this.conNo = conNo;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
    
}