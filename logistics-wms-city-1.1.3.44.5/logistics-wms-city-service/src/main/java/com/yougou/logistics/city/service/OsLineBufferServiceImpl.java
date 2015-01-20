package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.OsLineBuffer;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.dal.mapper.OsLineBufferMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Sep 27 09:54:25 CST 2013
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
@Service("osLineBufferService")
class OsLineBufferServiceImpl extends BaseCrudServiceImpl implements OsLineBufferService {
    @Resource
    private OsLineBufferMapper osLineBufferMapper;

    @Override
    public BaseCrudMapper init() {
        return osLineBufferMapper;
    }

	@Override
	public List<Store> getStoreInfo(String cellNo,String locno) throws ServiceException {
		
		return osLineBufferMapper.getStoreInfo(cellNo,locno);
	}

	@Override
	public List<OsLineBuffer> selectBufferBySupplierNo(OsLineBuffer osLineBuffer)
			throws ServiceException {
		try{
			return osLineBufferMapper.selectBufferBySupplierNo(osLineBuffer);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
}