package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillUmUntreadManager extends BaseCrudManager {
	/**
	 * 保存主档
	 * @param untreadMm
	 */
	public void saveMain(BillUmUntread untread, SystemUser user) throws ManagerException;

	/**
	 * 删除
	 * @param untreadMm
	 * @throws ManagerException
	 */
	public void deleteUntread(String keyStr, String locnoNo) throws ManagerException;

	/**
	 * 审核
	 * @param untreadMm
	 * @throws ManagerException
	 */
	public void auditUntread(String keyStr, SystemUser user) throws ManagerException;
	
	public List<BillUmUntread> findUntread2CheckTask(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, List<BillUmUntread> list, AuthorityParams authorityParams)
			throws ManagerException;

	/**
	 * 作废店退仓单
	 * @param ids
	 * @throws ManagerException
	 * @author wanghb
	 */
	public Map<String, Object> invalid(BillUmUntread untreadMm,String untReadNos,SystemUser user) throws ManagerException;

	/**
	 * 退仓单合计
	 * @param params
	 * @param authorityParams
	 * @return
	 */
    public Map<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)throws ManagerException;

    /**
     * 检查店退仓单是否是建单状态或者是否被删除
     * @param billUmUntreadKey
     * @param status
     * @return
     */
	public int judgeObjIsExist(BillUmUntreadKey billUmUntreadKey, String status) throws ManagerException;
	
}