package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmReceiptMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
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
@Service("billUmReceiptService")
class BillUmReceiptServiceImpl extends BaseCrudServiceImpl implements BillUmReceiptService {
    @Resource
    private BillUmReceiptMapper billUmReceiptMapper;

    @Override
    public BaseCrudMapper init() {
        return billUmReceiptMapper;
    }

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectUmReceiptSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billUmReceiptMapper.selectUmReceiptSumQty(map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}
    
}