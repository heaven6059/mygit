package com.yougou.logistics.city.common.vo;

import java.util.Date;
import java.util.List;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-3-8 下午1:49:39
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillConStoreLockQuery {

	private String locno;
	private String ownerNo;
	private String storelockNo;
	private String creator;
	private Date createtm;
	private String creatorName;
	private String editorName;
	private String auditorName;
	private List<BillConStorelockDtl> insertList;
	private List<BillConStorelockDtl> updateList;
	private List<BillConStorelockDtl> deleteList;

	public String getLocno() {
		return locno;
	}

	public void setLocno(String locno) {
		this.locno = locno;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getStorelockNo() {
		return storelockNo;
	}

	public void setStorelockNo(String storelockNo) {
		this.storelockNo = storelockNo;
	}

	public List<BillConStorelockDtl> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<BillConStorelockDtl> insertList) {
		this.insertList = insertList;
	}

	public List<BillConStorelockDtl> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<BillConStorelockDtl> updateList) {
		this.updateList = updateList;
	}

	public List<BillConStorelockDtl> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<BillConStorelockDtl> deleteList) {
		this.deleteList = deleteList;
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

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	
}
