package com.yougou.logistics.city.common.vo;

import java.io.Serializable;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * 字典表
 * @author wei.hj
 * @date 2013-07-19
 * @version 0.1.0
 * @copyright yougou.com
 *
 */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class LookupDtl implements Serializable  {
	
    private String systemid; // 系统ID
    private String lookupcode;// 字典分类编号
    private String itemvalue;// 字典编号
    private String itemname;// 字典名称
    private String itemnamedetail;
    
    /**
     * 仓别
     */
    private String  locno;
    
	public String getLocno() {
		return locno;
	}
	public void setLocno(String locno) {
		this.locno = locno;
	}
	public String getSystemid() {
		return systemid;
		
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getLookupcode() {
		return lookupcode;
	}
	public void setLookupcode(String lookupcode) {
		this.lookupcode = lookupcode;
	}
	public String getItemvalue() {
		return itemvalue;
	}
	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
		
	}
	public String getItemnamedetail() {
		return itemvalue+"→"+itemname;
	}
	public void setItemnamedetail(String itemnamedetail) {
		this.itemnamedetail = itemnamedetail;
	}
	
	
    
    
    
}
