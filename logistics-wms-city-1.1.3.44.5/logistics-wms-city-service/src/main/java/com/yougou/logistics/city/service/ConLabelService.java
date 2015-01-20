package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.ConLabel;

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
public interface ConLabelService extends BaseCrudService {
	
	/**
	 * 修改标签表的状态
	 * @param conLabel
	 * @return
	 * @throws ServiceException
	 */
	public int  modifyStatusByLocnoAndLabelNo(ConLabel conLabel)throws ServiceException;
	
	
	public List<ConLabel>  getLabelStatus(String labelNo,String locno)throws ServiceException;
}