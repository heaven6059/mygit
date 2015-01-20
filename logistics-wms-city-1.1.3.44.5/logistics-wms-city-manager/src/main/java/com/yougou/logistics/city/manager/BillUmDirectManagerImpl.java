package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.BillUmDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmDirectService;
import com.yougou.logistics.city.service.ProcCommonService;

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
@Service("billUmDirectManager")
class BillUmDirectManagerImpl extends BaseCrudManagerImpl implements
	BillUmDirectManager {

    @Log
    private Logger logger;

    @Resource
    private BillUmDirectService billUmDirectService;

    @Resource
    private ProcCommonService procCommonService;

    @Override
    public BaseCrudService init() {
	return billUmDirectService;
    }

    @Override
    public int selectCount4Direct(Map map) throws ManagerException {
	try {
	    return billUmDirectService.selectCount4Direct(map);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillUmDirect> selectByPage4Direct(SimplePage page, Map map)
	    throws ManagerException {
	try {
	    return billUmDirectService.selectByPage4Direct(page, map);
	} catch (ServiceException e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public void instockDirect(String rowStr, String loginName)
	    throws ManagerException {
	if (StringUtils.isNotBlank(rowStr)) {
	    String[] strs = rowStr.split(",");
	    for (String obj : strs) {
		try {
		    String[] substr = obj.split("\\|");
		    procCommonService.procUmDirect(substr[0], substr[1],
			    substr[2], loginName);
		} catch (ServiceException e) {
		    logger.error(e.getMessage());
		    throw new ManagerException(e.getMessage());
		}
	    }
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public void cancelDirectForAll(String locNo, String ownerNo,
	    String sourceNo, String keyStr, String loginName)
	    throws ManagerException {
	try {
	    procCommonService.cancelDirectForUm(locNo, ownerNo, sourceNo,
		    keyStr, loginName);
	} catch (ServiceException e) {
	    logger.error(e.getMessage());
	    throw new ManagerException(e.getMessage());
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public void continueDirect(String locno, String ownerNo,
	    String untreadMmNo, String strCheckNoList, String loginName)
	    throws ManagerException {
	try {
	    // 继续定位
	    boolean isSuccess = procCommonService.continueDirect(locno,
		    ownerNo, untreadMmNo, strCheckNoList, loginName);
//	    if (isSuccess) {// 如果继续定位成功，会自动生成任务
//	    	procCommonService.createTask(locno, ownerNo, untreadMmNo,
//			strCheckNoList, loginName);
//	    }
	} catch (ServiceException e) {
	    logger.error(e.getMessage());
	    throw new ManagerException(e.getMessage());
	}

    }

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map) {
	return this.billUmDirectService.selectSumQty(map);
    }
}