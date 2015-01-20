package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.database.BmDefdockMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 23 10:24:36 CST 2013
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
@Service("bmDefdockService")
class BmDefdockServiceImpl extends BaseCrudServiceImpl implements BmDefdockService {
	@Resource
	private BmDefdockMapper bmDefdockMapper;

	@Override
	public BaseCrudMapper init() {
		return bmDefdockMapper;
	}
}