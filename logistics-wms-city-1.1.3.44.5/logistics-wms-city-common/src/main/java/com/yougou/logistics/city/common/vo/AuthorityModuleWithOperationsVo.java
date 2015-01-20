package com.yougou.logistics.city.common.vo;

import com.yougou.logistics.city.common.model.AuthorityModule;

/**
 * 模块页面操作权限使用vo
 * 
 * @author wei.b
 * @date 2013-8-22 下午2:24:35
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class AuthorityModuleWithOperationsVo extends AuthorityModule{
	/**
	 * 拥有操作权限
	 */
	private String operations;

	public String getOperations() {
		return operations;
	}

	public void setOperations(String operations) {
		this.operations = operations;
	}
}
