package com.yougou.logistics.city.common.vo;

import org.apache.commons.lang.StringUtils;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2014-2-11 上午11:26:51
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class AuthorityUserVo {

	private String workerNo;
	private String workerName;
	/**
	 * 编号和名称的联合值:admin→管理员
	 */
	private String unionName;
	public String getWorkerNo() {
		return workerNo;
	}
	public void setWorkerNo(String workerNo) {
		this.workerNo = workerNo;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getUnionName() {
		if(!StringUtils.isBlank(this.workerNo) && !StringUtils.isBlank(this.workerName)){
			this.unionName = this.workerNo + "→" +this.workerName;
		}
		return unionName;
	}
	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}
	
	
}
