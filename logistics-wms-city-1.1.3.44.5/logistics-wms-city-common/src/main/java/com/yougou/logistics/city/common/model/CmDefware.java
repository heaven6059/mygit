package com.yougou.logistics.city.common.model;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

/**
 * 
 * 仓区
 * 
 * @author qin.dy
 * @date 2013-9-25 下午3:36:22
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class CmDefware extends CmDefwareKey {
	/**
	 * 仓区名称
	 */
    private String wareName;

    /**
     * 仓区备注
     */
    private String wareRemark;

    /**
     * 创建人员
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date createtm;

    /**
     * 编辑人员
     */
    private String editor;

    /**
     * 编辑时间
     */
    @JsonSerialize(using=JsonDateSerializer$19.class)
    private Date edittm;

    private Object wareTaskType;

    /**
	 * 创建人中文名次
	 */
	private String creatorName;
	/**
	 * 修改人中文名称
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

	public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getWareRemark() {
        return wareRemark;
    }

    public void setWareRemark(String wareRemark) {
        this.wareRemark = wareRemark;
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

    public Object getWareTaskType() {
        return wareTaskType;
    }

    public void setWareTaskType(Object wareTaskType) {
        this.wareTaskType = wareTaskType;
    }
}