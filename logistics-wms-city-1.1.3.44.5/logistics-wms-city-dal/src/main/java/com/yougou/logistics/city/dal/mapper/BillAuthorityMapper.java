package com.yougou.logistics.city.dal.mapper;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AuthorityDtlTableBean;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-4-11 下午3:31:45
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillAuthorityMapper extends BaseCrudMapper {

	/**
     * 查询是否有整单的品牌权限，
     * 返回值等于0表示有整单的品牌权限
     * @param params
     * @param authorityParams
     * @return
     * @throws ServiceException
     */
	public int selectHasFullBillAuthority(@Param("bean")AuthorityDtlTableBean bean,@Param("authorityParams") AuthorityParams authorityParams);
}
