package com.yougou.logistics.city.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmLoadboxDtlMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
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
@Service("billUmLoadboxDtlService")
class BillUmLoadboxDtlServiceImpl extends BaseCrudServiceImpl implements BillUmLoadboxDtlService {
	@Resource
	private BillUmLoadboxDtlMapper billUmLoadboxDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billUmLoadboxDtlMapper;
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billUmLoadboxDtlMapper.selectSumQty(map, authorityParams);
	}
}