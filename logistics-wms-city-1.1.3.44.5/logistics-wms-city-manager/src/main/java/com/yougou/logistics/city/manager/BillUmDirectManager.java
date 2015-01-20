package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-15 14:36:28
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
public interface BillUmDirectManager extends BaseCrudManager {

    public int selectCount4Direct(Map map) throws ManagerException;

    public List<BillUmDirect> selectByPage4Direct(SimplePage page, Map map)
	    throws ManagerException;

    public void instockDirect(String rowStr, String loginName)
	    throws ManagerException;

    public void continueDirect(String locno, String ownerNo,
	    String untreadMmNo, String strCheckNoList, String loginName)
	    throws ManagerException;

    public void cancelDirectForAll(String locNo, String ownerNo,
	    String sourceNo, String keyStr, String loginName)
	    throws ManagerException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map);
}