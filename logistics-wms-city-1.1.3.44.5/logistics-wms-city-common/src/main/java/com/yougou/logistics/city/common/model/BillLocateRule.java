/*
 * 类名 com.yougou.logistics.city.common.model.BillLocateRule
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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

public class BillLocateRule extends BillLocateRuleKey {
    private String ruleName;

    private String ruleDesc;

    private String status;

    private String ruleFirst;

    private String ruleBox;

    private String ruleItem;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private String remark;
    
    /**
     * 用于页面显示
     * @return
     */
    private String statusStr;//状态
    
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRuleFirst() {
        return ruleFirst;
    }

    public void setRuleFirst(String ruleFirst) {
        this.ruleFirst = ruleFirst;
    }

    public String getRuleBox() {
        return ruleBox;
    }

    public void setRuleBox(String ruleBox) {
        this.ruleBox = ruleBox;
    }

    public String getRuleItem() {
        return ruleItem;
    }

    public void setRuleItem(String ruleItem) {
        this.ruleItem = ruleItem;
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

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
}