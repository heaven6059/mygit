/*
 * 类名 com.yougou.logistics.city.common.model.CmDefwareKey
 * @author qin.dy
 * @date  Wed Sep 25 15:27:15 CST 2013
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
 * 仓区  主键
 * TODO: 增加描述
 * 
 * @author qin.dy
 * @date 2013-9-25 下午3:36:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefwareKey {
	/**
	 * 仓别
	 */
    private String locno;

    /**
     * 仓区编码
     */
    private String wareNo;

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
}