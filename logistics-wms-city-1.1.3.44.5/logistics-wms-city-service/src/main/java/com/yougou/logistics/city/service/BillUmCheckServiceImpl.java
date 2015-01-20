package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.BillUmCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 11 14:40:26 CST 2013
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
@Service("billUmCheckService")
class BillUmCheckServiceImpl extends BaseCrudServiceImpl implements BillUmCheckService {

	private final static String RESULTY = "Y";

	@Resource
	private BillUmCheckMapper billUmCheckMapper;
	@Resource
	private BillUmCheckDtlMapper billUmCheckDtlMapper;

	@Resource
	private ProcCommonService procCommonService;

	@Override
	public BaseCrudMapper init() {
		return billUmCheckMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> procBillUmCheckAuditQuery(List<BillUmCheck> listBillUmChecks) throws ServiceException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";

		try {
			for (BillUmCheck b : listBillUmChecks) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("I_locno", b.getLocno());
				map.put("I_owner_no", b.getOwnerNo());
				map.put("I_check_no", b.getCheckNo());
				map.put("I_oper_user", b.getAuditor());
				billUmCheckMapper.procBillUmCheckAuditQuery(map);

				if (!RESULTY.equals(map.get("stroutmsg"))) {
					String stroutmsg = map.get("stroutmsg");
					if (StringUtils.isNotBlank(stroutmsg)) {
						String[] msgs = stroutmsg.split("\\|");
						msg = msgs[1];
					}
					flag = "warn";
					throw new ServiceException(msg);
				} else {
					flag = "success";
					msg = "审核成功!";
				}
			}

			mapObj.put("flag", flag);
			mapObj.put("msg", msg);
			return mapObj;
		} catch (Exception e) {
			throw new ServiceException(msg);
		}
	}

	@Override
	public void saveMain(BillUmCheck check) throws ServiceException {
		try {
			//新增
			if (StringUtils.isEmpty(check.getCheckNo())) {
				String checkNo = procCommonService.procGetSheetNo(check.getLocno(), CNumPre.UM_CHECK_NO_PRE);
				check.setCheckNo(checkNo);
				check.setStatus(BillUmCheckStatusEnums.STATUS10.getStatus());
				billUmCheckMapper.insertSelective(check);
			} else {//修改
				check.setCheckStatus("10");
				int i  = billUmCheckMapper.updateByPrimaryKeySelective(check);
				if (0 == i){
					throw new ServiceException("单据:" + check.getCheckNo() +"已删除或者状态已改变！");
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void deleteCheck(String keyStr, String locno) throws ServiceException {
		try {
			BillUmCheck check = null;
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					check = new BillUmCheck();
					check.setLocno(locno);
					check.setUntreadNo(subKyes[0]);
					check.setOwnerNo(subKyes[1]);
					billUmCheckMapper.deleteByPrimarayKeyForModel(check);
					//删除明细
					billUmCheckDtlMapper.deleteAllDetail(check);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void auditCheck(String keyStr, SystemUser user) throws ServiceException {
		try {
			String oper = user.getLoginName();
			String locno = user.getLocNo();
			List<String> list = new ArrayList<String>();
			if (StringUtils.isNotEmpty(keyStr)) {
				String keys[] = keyStr.split(",");
				for (String key : keys) {
					String[] subKyes = key.split("\\|");
					if(subKyes[0] != null && subKyes[1] != null && subKyes[2] != null) {
						String ownerNo = subKyes[0];
						String checkNo = subKyes[1];
						String untreadNo = subKyes[2];
						
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("locno", locno);
						param.put("ownerNo", ownerNo);
						param.put("checkNo", checkNo);
						List<BillUmCheckDtl> billUmCheckDtl = billUmCheckDtlMapper.selectByParams(null, param);
						if(billUmCheckDtl.size() > 0) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("locno", locno);
							map.put("ownerNo", ownerNo);
							map.put("untreadNo", untreadNo);
							if(list.contains(untreadNo)){   //查看新集合中是否有指定的元素，如果没有则加入
								throw new ServiceException("店退仓单:" + untreadNo + " 不能重复在退仓验收审核!");
				            } else {
				            	list.add(untreadNo);
				            }
							int count = billUmCheckMapper.selectCountUmNo(map);
							if(count > 0) {
								throw new ServiceException("店退仓单:" + untreadNo + " 已在其他验收单中审核!");
							} else {
								procCommonService.auditUmCheck(locno, subKyes[0], subKyes[1], oper);
							}
						} else {
							throw new ServiceException("退仓验收单:" + checkNo + " 无明细,不能审核!");
						}
						
					} else {
						throw new ServiceException("审核参数不完整!");
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int selectCountUmNoForInstock(Map map) throws ServiceException {
		try {
			return billUmCheckMapper.selectCountUmNoForInstock(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<BillUmCheck> selectByPageUmNoForInstock(SimplePage page, Map map) throws ServiceException {
		try {
			return billUmCheckMapper.selectByPageUmNoForInstock(page, map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectCountCheckNoForInstock(Map map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billUmCheckMapper.selectCountCheckNoForInstock(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillUmCheck> selectByPageCheckNoForInstock(SimplePage page, Map map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckMapper.selectByPageCheckNoForInstock(page, map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectCount4loadBox(Map map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billUmCheckMapper.selectCount4loadBox(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillUmCheck> select4loadBoxByPage(Map map, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckMapper.select4loadBoxByPage(map, page, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectUntreadJoinCheckDtlSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billUmCheckMapper.selectUntreadJoinCheckDtlSumQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectUntreadJoinCheckSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billUmCheckMapper.selectUntreadJoinCheckSumQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findBillUmCheckCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billUmCheckMapper.selectBillUmCheckCount(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillUmCheck> findBillUmCheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billUmCheckMapper.selectBillUmCheckByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}