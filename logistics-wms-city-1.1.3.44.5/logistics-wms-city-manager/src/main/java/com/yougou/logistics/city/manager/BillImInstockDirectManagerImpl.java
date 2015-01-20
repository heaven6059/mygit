package com.yougou.logistics.city.manager;

import java.util.HashMap;
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
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.AuthorityUserinfoService;
import com.yougou.logistics.city.service.BillImInstockDirectService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 上架任务
 * @author luo.hl
 * @date  Thu Oct 10 10:56:15 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billImInstockDirectManager")
class BillImInstockDirectManagerImpl extends BaseCrudManagerImpl implements BillImInstockDirectManager {
	@Log
	private Logger logger;
	@Resource
	private BillImInstockDirectService billImInstockDirectService;

	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private AuthorityUserinfoService authorityUserinfoService;
	

	@Override
	public BaseCrudService init() {
		return billImInstockDirectService;
	}

	@Override
	public void cancelDirect(String locNo, String receiptNo, String ownerNo, String rowIdStr) throws ManagerException {
		try {
			billImInstockDirectService.cancelDirect(locNo, receiptNo, ownerNo, rowIdStr);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void cancelDirectForAll(String locNo, String ownerNo, String sourceNo, String flag) throws ManagerException {
		try {
			procCommonService.cancelDirectForAll(locNo, ownerNo, sourceNo, flag);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void createInstock(String curUser, String locNo, String rowStrs, String instockWorker)
			throws ManagerException {
		try {
			billImInstockDirectService.createInstock(curUser, locNo, rowStrs, instockWorker);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void instockDirect(String rowStr) throws ManagerException {
		if (StringUtils.isNotBlank(rowStr)) {
			String[] strs = rowStr.split(",");
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					procCommonService.procImInstockDirectByReceipt(substr[0], substr[1], substr[2], substr[3]);
				} catch (ServiceException e) {
					logger.error(e.getMessage());
					throw new ManagerException(e.getMessage());
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void instockDirectForCheck(String rowStr, String loginName) throws ManagerException {
		if (StringUtils.isNotBlank(rowStr)) {
			String[] strs = rowStr.split(",");
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
//					String workerNo = "";
//					if (StringUtils.isBlank(substr[3])) {
//						workerNo = loginName;
//					}
					procCommonService.procImInstockDirectByCheck(substr[0], substr[1], substr[2], loginName);
				} catch (ServiceException e) {
					logger.error(e.getMessage());
					throw new ManagerException(e.getMessage());
				}
			}
		}
	}

	@Override
	public int countInstockDirectByMainId(BillImInstockDirect objEntiy, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImInstockDirectService.countInstockDirectByMainId(objEntiy, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImInstockDirect> findInstockDirectByMainIdPage(SimplePage page, BillImInstockDirect objEntiy,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImInstockDirectService.findInstockDirectByMainIdPage(page, objEntiy, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImInstockDirectService.selectSumQty(map, authorityParams);
	}

	public SumUtilMap<String, Object> selectSumQty4CheckDirect(BillImInstockDirect map, AuthorityParams authorityParams) {
		return billImInstockDirectService.selectSumQty4CheckDirect(map, authorityParams);
	}

	@Override
	public int countInstockDirectByType(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImInstockDirectService.countInstockDirectByType(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImInstockDirect> findInstockDirectByTypePage(SimplePage page, Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImInstockDirectService.findInstockDirectByTypePage(page, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void sendOrder(String locNo, String ownerNo, String sender, String keyStr, String loginName,int systeId,int areaSystemId)
			throws ManagerException {
		try {
			//校验上架人
			String [] senderArr = sender.split(",");
			for(int i=0;i<senderArr.length;i++){
				String userStr = senderArr[i];
				AuthorityUserinfo userinfo= authorityUserinfoService.findUserByLoginName(locNo, userStr,systeId,areaSystemId);
				if(userinfo == null){
					throw new ServiceException("用户【" + userStr + "】不存在，请选择正确的用户。");
				}
			}
			procCommonService.sendOrderForIm(locNo, ownerNo, sender, keyStr, loginName);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			throw new ManagerException(e.getMessage());
		}

	}

	public SumUtilMap<String, Object> selectInstockDirectByTypePage4Sum(Map<String, Object> map,
			AuthorityParams authorityParams) {
		return billImInstockDirectService.selectInstockDirectByTypePage4Sum(map, authorityParams);
	}
}