package com.yougou.logistics.city.service;

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
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.api.dto.BillStatusLogDto;
import com.yougou.logistics.city.common.model.BillStatusLog;
import com.yougou.logistics.city.dal.database.BillStatusLogMapper;

@Service("billStatusLogService")
class BillStatusLogServiceImpl extends BaseCrudServiceImpl implements BillStatusLogService {

	@Resource
	private BillStatusLogMapper billStatusLogMapper;

	@Override
	public BaseCrudMapper init() {
		return billStatusLogMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void procInsertBillStatusLog(String locno, String billNo, String billType, String status,
			String description, String operator) throws ServiceException {
		try {
			if (StringUtils.isNotBlank(locno) && StringUtils.isNotBlank(billNo) && StringUtils.isNotBlank(billType)
					&& StringUtils.isNotBlank(status) && StringUtils.isNotBlank(description)
					&& StringUtils.isNotBlank(operator)) {

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("v_locno", locno);
				params.put("v_bill_no", billNo);
				params.put("v_bill_type", billType);
				params.put("v_status", status);
				params.put("v_description", description);
				params.put("v_operator", operator);
				billStatusLogMapper.procInsertBillStatusLog(params);

				String stroutmsg = String.valueOf(params.get("stroutmsg"));
				if (!"Y".equals(stroutmsg)) {
					throw new ServiceException("插入状态日志发生异常!");
				}
			} else {
				throw new ServiceException("插入日志状态参数不合法!");
			}

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public List<BillStatusLogDto> findBillStatusLogByPoNo(String poNo) throws ServiceException {
		try {
			return billStatusLogMapper.selectBillStatusLogByPoNo(poNo);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillStatusLogDto> findBillStatusLogBySourceExpNo(String sourceExpNo) throws ServiceException {
		try {
			return billStatusLogMapper.selectBillStatusLogBySourceExpNo(sourceExpNo);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findCountByIm(Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ServiceException {
		try {
			return billStatusLogMapper.selectCountByIm(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillStatusLog> findPageByIm(SimplePage page, Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ServiceException {
		try {
			return billStatusLogMapper.selectPageByIm(page, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findCountByOm(Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ServiceException {
		try {
			return billStatusLogMapper.selectCountByOm(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillStatusLog> findPageByOm(SimplePage page, Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ServiceException {
		try {
			return billStatusLogMapper.selectPageByOm(page, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findCountByUm(Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ServiceException {
		try {
			return billStatusLogMapper.selectCountByUm(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillStatusLog> findPageByUm(SimplePage page, Map<String, Object> params, AuthorityParams authorityParams, DataAccessRuleEnum... dataAccessRuleEnum) throws ServiceException {
		try {
			return billStatusLogMapper.selectPageByUm(page, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}