package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto2;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmRecheckJoinDtlService;

/**
 * 
 * 分货交接单明细manager实现
 * 
 * @author luo.hl
 * @date 2013-10-11 上午11:20:52
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmRecheckJoinDtlManagerImpl")
class BillOmRecheckJoinDtlManagerImpl extends BaseCrudManagerImpl implements BillOmRecheckJoinDtlManager {
	@Resource
	private BillOmRecheckJoinDtlService billOmRecheckJoinDtlService;

	@Override
	public BaseCrudService init() {
		return billOmRecheckJoinDtlService;
	}

	@Override
	public SumUtilMap<String, Object> selectNoReCheckSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.selectNoReCheckSumQty(map,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public SumUtilMap<String, Object> selectNoReCheckedSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.selectNoReCheckedSumQty(map,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public List<?> findRecheckNo(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findRecheckNo(page, map,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	public int findRecheckNoCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findRecheckNoCount(map,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public int findNoReCheckCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findNoReCheckCount(map,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public List<?> findNoReCheck(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findNoReCheck(map, page,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public int findReCheckedCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findReCheckedCount(map,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public List<BillOmRecheckJoinDto> findReChecked(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findReChecked(map, page,authorityParams);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public List<BillOmRecheckJoinDto2> findItemDetail(@Param("dtl") BillOmRecheckJoinDto dtl) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.findItemDetail(dtl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException();
		}
	}

	@Override
	public Map<String, Object> sendReCheck(String rowIdstr, String locno,String user, List<BillOmRecheck> recheckList) throws ManagerException {
		try {
			return billOmRecheckJoinDtlService.sendReCheck(rowIdstr, locno, user, recheckList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void queryReCheck(String rowIdstr, String locno, String user) throws ManagerException {
		String msg2 = "";
		try {
			if (StringUtils.isNotBlank(rowIdstr)) {
				String[] strs = rowIdstr.split(",");
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("labelNo", strs[0]);
				paramMap.put("locno", locno);
				
				//获取单号
				String recheckNo = "";
				BillOmRecheckDtl dtl = billOmRecheckJoinDtlService.selectReCheckNoByLabelNo(paramMap);
				if (null == dtl) {
					msg2 = "无可用复核单记录！";
					throw new ServiceException(msg2);
				} else {
					recheckNo = dtl.getRecheckNo();
				}
				for (String boxNo : strs) {
					if(StringUtils.isNotBlank(locno) 
							&& StringUtils.isNotBlank(recheckNo)
							&& StringUtils.isNotBlank(boxNo)) {
						
						Map<String, String> paramMap2 = null;
						//根据箱号更新状态
						paramMap2 = new HashMap<String, String>();
						paramMap2.put("I_locno", locno);
						paramMap2.put("I_reCheckNo", recheckNo);
						paramMap2.put("I_boxNo", boxNo);
						paramMap2.put("I_creator", user);
						billOmRecheckJoinDtlService.queryReCheck(paramMap2);
					} else {
						msg2 = recheckNo+"参数非法！";
						throw new ServiceException(msg2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
	}

}