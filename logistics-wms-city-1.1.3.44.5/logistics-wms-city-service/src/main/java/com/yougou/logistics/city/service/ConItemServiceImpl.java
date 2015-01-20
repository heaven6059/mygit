package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.ConItemInfoMapper;

/**
 * 商品属性
 * 
 * @author jiang.ys
 * @date 2013-12-24 下午2:16:15
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("conItemInfoService")
public class ConItemServiceImpl extends BaseCrudServiceImpl implements ConItemInfoService {

	@Resource
	private ConItemInfoMapper conItemInfoMapper;
	@Override
	public BaseCrudMapper init() {
		return conItemInfoMapper;
	}
	@Override
	public String findMaxItemIdByItemNo(String itemNo) throws ServiceException {
		try {
			return conItemInfoMapper.getMaxItemIdByItemNo(itemNo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

}
