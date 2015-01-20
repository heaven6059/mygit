/*
 * 类名 com.yougou.logistics.city.common.model.BmDefcarKey
 * @author qin.dy
 * @date  Mon Sep 23 18:33:13 CST 2013
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
 * 车辆管理  主键
 * 
 * @author qin.dy
 * @date 2013-9-23 下午7:05:26
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BmDefcarKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 车辆代码
     */
    private String carNo;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}