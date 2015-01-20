package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmLocateDtlService;
import com.yougou.logistics.city.service.BillOmLocateService;
import com.yougou.logistics.city.service.OmTmpBoxGroupService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
@Service("billOmLocateManager")
class BillOmLocateManagerImpl extends BaseCrudManagerImpl implements BillOmLocateManager {

	@Resource
	private BillOmLocateService billOmLocateService;

	@Resource
	private BillOmLocateDtlService billOmLocateDtlService;

	@Resource
	private OmTmpBoxGroupService omTmpBoxGroupService;

	@Override
	public BaseCrudService init() {
		return billOmLocateService;
	}

	@Override
	public int findBillOmLocateCount(BillOmLocate billOmLocate, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billOmLocateService.findBillOmLocateCount(billOmLocate, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillOmLocate> findBillOmLocateByPage(SimplePage page, String orderByField, String orderBy,
			BillOmLocate billOmLocate, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billOmLocateService.findBillOmLocateByPage(page, orderByField, orderBy, billOmLocate,
					authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void procBillOmExpContinueDispatchQuery(List<BillOmLocate> listBillOmLocates, BillOmLocate billOmLocate)
			throws ManagerException {

		try {
			if (CommonUtil.hasValue(listBillOmLocates)) {

				for (BillOmLocate b : listBillOmLocates) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", b.getLocno());
					params.put("locateNo", b.getLocateNo());
					List<BillOmLocateDtl> listDtls = billOmLocateDtlService.findBillOmLocateDtlGroupBy(params);

					String expNo = "";//出库单号
					String expType = "";//出库类型
					String ownerNo = "";//委托业主

					BillOmLocate omLocate = new BillOmLocate();
					omLocate.setLocno(b.getLocno());
					omLocate.setLocateNo(b.getLocateNo());
					billOmLocate = (BillOmLocate) billOmLocateService.findById(omLocate);
					if (billOmLocate != null) {
						expType = billOmLocate.getExpType();
					}

					for (BillOmLocateDtl dtl : listDtls) {
						if (StringUtils.isNotBlank(dtl.getExpNo())) {
							expNo += dtl.getExpNo() + ",";
						}
						ownerNo = dtl.getOwnerNo();
					}

					if (StringUtils.isNotBlank(expNo)) {
						expNo = expNo.substring(0, expNo.length() - 1);
					}

					//判断参数是否非空
					if (StringUtils.isEmpty(b.getLocno()) && StringUtils.isEmpty(ownerNo)
							&& StringUtils.isEmpty(expType) && StringUtils.isEmpty(expNo)
							&& StringUtils.isEmpty(b.getCreator())) {
						throw new ManagerException("参数非法！");
					}

					//执行续调存储过程
					Map<String, String> map = new HashMap<String, String>();
					map.put("v_locno", b.getLocno());
					map.put("v_owner_no", ownerNo);
					map.put("v_exp_type", expType);
					map.put("v_locate_no", b.getLocateNo());
					map.put("v_locate_again", "1");
					map.put("v_creator", b.getCreator());
					billOmLocateService.procBillOmExpContinueDispatchQuery(map);

					if (!"Y".equals(map.get("stroutmsg"))) {
						String message = "";
						String msg = map.get("stroutmsg");
						if (StringUtils.isNotBlank(msg)) {
							String[] msgs = msg.split("\\|");
							message = msgs[1];
						}
						throw new ServiceException(message);
					}
				}

			} else {
				throw new ManagerException("参数非法！");
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public Map<String, String> overBillOmLocate(List<BillOmLocate> lists,SystemUser user) throws ManagerException {
		try {
			return this.billOmLocateService.overBillOmLocate(lists,user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> recoveryLocateSend(List<BillOmLocate> lists) throws ManagerException {
		try {
			return this.billOmLocateService.recoveryLocateSend(lists);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(BillOmLocate billOmLocate, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billOmLocateService.selectSumQty(billOmLocate, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteOmLocate(List<BillOmLocate> lists, String loginName) throws ManagerException {
		try {
			billOmLocateService.deleteOmLocate(lists, loginName);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

}