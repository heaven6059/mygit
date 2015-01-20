package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 单据权限公共服务
 * 
 * @author ye.kl
 * @date 2014-4-11 下午3:18:12
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillAuthorityService extends BaseCrudService {
    /**
     * 
     * 查询是否有整单的品牌权限，
     * ture表示有整单的品牌权限
     * false表示无整单的品牌权限
     * @param tableAndColumn  表名和单据编号列名
     * @param billNo 单据编号
     * @param authorityParams 权限参数
     * @return
     * @throws ServiceException
     */
    public boolean isHasFullBillAuthority(String tableAndColumn,String billNo, AuthorityParams authorityParams) throws ServiceException;

}
