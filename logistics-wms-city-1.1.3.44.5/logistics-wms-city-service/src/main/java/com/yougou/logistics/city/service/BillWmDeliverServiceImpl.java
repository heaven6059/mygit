package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillWmDeliverKey;
import com.yougou.logistics.city.dal.mapper.BillWmDeliverMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
@Service("billWmDeliverService")
class BillWmDeliverServiceImpl extends BaseCrudServiceImpl implements BillWmDeliverService {
    @Resource
    private BillWmDeliverMapper billWmDeliverMapper;

    @Override
    public BaseCrudMapper init() {
        return billWmDeliverMapper;
    }

	@Override
	public List<String> selectLabelListByDeliverNo(BillWmDeliverKey billWmDeliverKey) throws ServiceException {
		try{
			return billWmDeliverMapper.selectLabelListByDeliverNo(billWmDeliverKey);
		}catch(Exception e){
			throw  new  ServiceException(e);
		}
	}

	@Override
	public void procWmDeliverAuditCar(Map<String, String> maps) throws ServiceException {
		try{
			billWmDeliverMapper.procWmDeliverAuditCar(maps);
		}catch(Exception e){
			throw  new  ServiceException(e);
		}
	}
	
	
}