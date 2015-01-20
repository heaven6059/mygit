package com.yougou.logistics.city.common.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import com.yougou.logistics.city.common.model.AuthorityModule;

/**
 * 菜单添加模块页面使用vo
 * 
 * @author wei.b
 * @date 2013-8-22 下午2:24:35
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class AuthorityModuleVo {
	/**
	 * 所有模块
	 */
	private List<AuthorityModule> allAuthorityModuleList;
	/**
	 * 菜单下已使用的模块
	 */
	@JsonProperty("used")
	private List<AuthorityModule> usedAuthorityModuleList;

	/**
	 * {@linkplain #allAuthorityModuleList}
	 * @return
	 */
	@JsonProperty("all")
	public List<AuthorityModule> getAllAuthorityModuleList() {
		return allAuthorityModuleList;
	}

	/**
	 * {@linkplain #allAuthorityModuleList}
	 * @param allAuthorityModuleList
	 */
	public void setAllAuthorityModuleList(List<AuthorityModule> allAuthorityModuleList) {
		this.allAuthorityModuleList = allAuthorityModuleList;
	}

	/**
	 * {@linkplain #usedAuthorityModuleList}
	 * @return
	 */
	
	public List<AuthorityModule> getUsedAuthorityModuleList() {
		return usedAuthorityModuleList;
	}

	/**
	 * {@linkplain #usedAuthorityModuleList}
	 * @param usedAuthorityModuleList
	 */
	public void setUsedAuthorityModuleList(List<AuthorityModule> usedAuthorityModuleList) {
		this.usedAuthorityModuleList = usedAuthorityModuleList;
	}
}
