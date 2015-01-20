/*
 * 类名 com.yougou.logistics.city.common.model.ItemPack
 * @author luo.hl
 * @date  Wed Oct 09 19:26:37 CST 2013
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

import java.math.BigDecimal;
import java.util.Date;

public class ItemPack extends ItemPackKey {
    private String packUnit;

    private String packSpec;

    private BigDecimal mpackQty;

    private String mpackUnit;

    private BigDecimal packWeight;

    private BigDecimal palBaseQbox;

    private BigDecimal palHeightQbox;

    private Short sorterFlag;

    private String ruleFlag;

    private BigDecimal qpalette;

    private String creator;

    private Date createtm;

    private Date edittm;

    private String editor;
    
    private BigDecimal packLength;
    
    private BigDecimal packWight;
    
    private BigDecimal packHight;
    
    private BigDecimal packVolum;
    
    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
    }

    public String getPackSpec() {
        return packSpec;
    }

    public void setPackSpec(String packSpec) {
        this.packSpec = packSpec;
    }

    public BigDecimal getMpackQty() {
        return mpackQty;
    }

    public void setMpackQty(BigDecimal mpackQty) {
        this.mpackQty = mpackQty;
    }

    public String getMpackUnit() {
        return mpackUnit;
    }

    public void setMpackUnit(String mpackUnit) {
        this.mpackUnit = mpackUnit;
    }

    public BigDecimal getPackWeight() {
        return packWeight;
    }

    public void setPackWeight(BigDecimal packWeight) {
        this.packWeight = packWeight;
    }

    public BigDecimal getPalBaseQbox() {
        return palBaseQbox;
    }

    public void setPalBaseQbox(BigDecimal palBaseQbox) {
        this.palBaseQbox = palBaseQbox;
    }

    public BigDecimal getPalHeightQbox() {
        return palHeightQbox;
    }

    public void setPalHeightQbox(BigDecimal palHeightQbox) {
        this.palHeightQbox = palHeightQbox;
    }

    public Short getSorterFlag() {
        return sorterFlag;
    }

    public void setSorterFlag(Short sorterFlag) {
        this.sorterFlag = sorterFlag;
    }

    public String getRuleFlag() {
        return ruleFlag;
    }

    public void setRuleFlag(String ruleFlag) {
        this.ruleFlag = ruleFlag;
    }

    public BigDecimal getQpalette() {
        return qpalette;
    }

    public void setQpalette(BigDecimal qpalette) {
        this.qpalette = qpalette;
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

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

	public BigDecimal getPackLength() {
		return packLength;
	}

	public void setPackLength(BigDecimal packLength) {
		this.packLength = packLength;
	}

	public BigDecimal getPackWight() {
		return packWight;
	}

	public void setPackWight(BigDecimal packWight) {
		this.packWight = packWight;
	}

	public BigDecimal getPackHight() {
		return packHight;
	}

	public void setPackHight(BigDecimal packHight) {
		this.packHight = packHight;
	}

	public BigDecimal getPackVolum() {
		return packVolum;
	}

	public void setPackVolum(BigDecimal packVolum) {
		this.packVolum = packVolum;
	}
}