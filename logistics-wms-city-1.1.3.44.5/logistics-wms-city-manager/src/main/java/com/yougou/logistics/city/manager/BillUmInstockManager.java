package com.yougou.logistics.city.manager;

import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.SystemUser;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-17 20:35:58
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public interface BillUmInstockManager extends BaseCrudManager {
    /**
     * 审核
     * 
     * @param locno
     * @param instockNo
     * @param ownerNo
     * @param oper
     *            TODO
     * @return
     * @throws ManagerException
     */
    public String check(String locno, String instockNo, String ownerNo, SystemUser user) throws ManagerException;
    /**
     * 退仓上架单合计
     * @param params
     * @param authorityParams
     * @return
     * @throws ManagerException
     */
    public Map<String, Object> selectSumQty(Map<String, Object> params,
            AuthorityParams authorityParams)throws ManagerException;

}