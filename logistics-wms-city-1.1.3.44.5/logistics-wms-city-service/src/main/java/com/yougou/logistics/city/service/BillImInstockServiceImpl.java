package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.dal.mapper.BillImInstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImInstockMapper;
import com.yougou.logistics.city.dal.mapper.ProcCommonMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 30 09:51:28 CST 2013
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
@Service("billImInstockService")
class BillImInstockServiceImpl extends BaseCrudServiceImpl implements BillImInstockService {
	@Resource
	private BillImInstockMapper billImInstockMapper;
	
	@Resource
	private BillImInstockDtlMapper billImInstockDtlMapper;

	@Resource
	private ProcCommonMapper procCommonMapper;

	@Override
	public BaseCrudMapper init() {
		return billImInstockMapper;
	}

	private static final String RESULTY = "Y";

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> audit(BillImInstock instock) throws ServiceException {
		try {

			/*boolean authority = billAuthorityService.isHasFullBillAuthority(TableContants.BILL_IM_INSTOCK_DTL,
					instock.getInstockNo(), authorityParams);
			if (!authority) {
				throw new ServiceException("【" + instock.getInstockNo() + "】用户品牌权限不足，审核失败！");
			}*/
			Map<String, Object> result = new HashMap<String, Object>();
			Map<String, String> param = new HashMap<String, String>();
			param.put("i_locno", instock.getLocno());
			param.put("i_owner", instock.getOwnerNo());
			param.put("i_instock_no", instock.getInstockNo());
			param.put("i_oper", instock.getAuditor());
			procCommonMapper.procImInstockAudit(param);
			if (param.get("stroutmsg") != null) {
				String[] results = param.get("stroutmsg").split("\\|");
				if (RESULTY.equals(results[0])) {
					BillImInstockDtl dtl  = new BillImInstockDtl();
					dtl.setStatus("13");
					dtl.setInstockNo(instock.getInstockNo());
					dtl.setLocno(instock.getLocno());
					dtl.setOwnerNo(instock.getOwnerNo());
					billImInstockDtlMapper.updateByAudit(dtl);
					result.put("result", "success");
				} else {
					throw new ServiceException(results[1]);
				}
			}
			return result;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}
}