package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Oct 08 09:58:17 CST 2013
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
public interface CsInstockSettingdtl2Service extends BaseCrudService {

	/**
	 * 查询库区
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectAreaBySettingNo(String locNo, String settingNo)
			throws ServiceException;

	/**
	 * 查询通道
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectStockBySettingNo(String locNo, String settingNo)
			throws ServiceException;

	/**
	 * 查询储位
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectCellBySettingNo(String locNo, String settingNo)
			throws ServiceException;
}