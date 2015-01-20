package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-11-26 14:47:41
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
public class OsCustBuffer extends OsCustBufferKey {
    private String aStockNo;

    private String bufferName;

    private String status;

    private BigDecimal useVolumn;

    private BigDecimal useWeight;

    private BigDecimal useBoxnum;

    private String creator;

    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date createtm;

    private String editor;
    
    @JsonSerialize(using = JsonDateSerializer$19.class)
    private Date edittm;
    
    /** 扩展字段 **/
    private String storeName;
    
    private String wareName;
    
    private String areaName;
    
    /**
     * 创建人名称
     */
    private String creatorName;
    
    /**
     * 修改人名称
     */
    private String editorName;
 
    
    public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getWareName() {
		return wareName;
	}

	public void setWareName(String wareName) {
		this.wareName = wareName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getaStockNo() {
        return aStockNo;
    }

    public void setaStockNo(String aStockNo) {
        this.aStockNo = aStockNo;
    }

    public String getBufferName() {
        return bufferName;
    }

    public void setBufferName(String bufferName) {
        this.bufferName = bufferName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getUseVolumn() {
        return useVolumn;
    }

    public void setUseVolumn(BigDecimal useVolumn) {
        this.useVolumn = useVolumn;
    }

    public BigDecimal getUseWeight() {
        return useWeight;
    }

    public void setUseWeight(BigDecimal useWeight) {
        this.useWeight = useWeight;
    }

    public BigDecimal getUseBoxnum() {
        return useBoxnum;
    }

    public void setUseBoxnum(BigDecimal useBoxnum) {
        this.useBoxnum = useBoxnum;
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
}