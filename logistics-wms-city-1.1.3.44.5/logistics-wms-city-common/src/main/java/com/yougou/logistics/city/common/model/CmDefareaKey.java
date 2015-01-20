/*
 * 类名 com.yougou.logistics.city.common.model.CmDefareaKey
 * @author qin.dy
 * @date  Wed Sep 25 16:42:38 CST 2013
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

/**
 * 
 * 库区 主键
 * 
 * @author qin.dy
 * @date 2013-9-26 上午9:57:16
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefareaKey {
	/**
	 * 仓别
	 */
    private String locno;

    /**
     * 仓区编码
     */
    private String wareNo;

    /**
     * 库区编码
     */
    private String areaNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getWareNo() {
        return wareNo;
    }

    public void setWareNo(String wareNo) {
        this.wareNo = wareNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }
}