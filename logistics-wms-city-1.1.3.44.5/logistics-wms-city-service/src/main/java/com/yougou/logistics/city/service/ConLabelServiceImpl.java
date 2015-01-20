package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.ConLabel;
import com.yougou.logistics.city.dal.mapper.ConLabelMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Sep 30 15:09:38 CST 2013
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
@Service("conLabelService")
class ConLabelServiceImpl extends BaseCrudServiceImpl implements ConLabelService {
    @Resource
    private ConLabelMapper conLabelMapper;

    @Override
    public BaseCrudMapper init() {
        return conLabelMapper;
    }

	@Override
	public int modifyStatusByLocnoAndLabelNo(ConLabel conLabel)
			throws ServiceException {
		try{
			return conLabelMapper.modifyStatusByLocnoAndLabelNo(conLabel);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	public List<ConLabel> getLabelStatus(String labelNo, String locno)
			throws ServiceException {
		Map<String,Object> params = new HashMap<String,Object>(0);
		params.put("labelNo", labelNo);
		params.put("locno", locno);
		try{
			return conLabelMapper.selectByParams(null, params);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}
}