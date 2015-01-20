package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CsInstockSetting;

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
public interface CsInstockSettingdtlService extends BaseCrudService {
	/**
	 * 查询品牌
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectBrandBySettingNo(String locNo, String settingNo)
			throws ServiceException;

	/**
	 * 查询类别
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectCategoryBySettingNo(String locNo, String settingNo)
			throws ServiceException;

	/**
	 * 查询商品
	 * @param locNo
	 * @param settingNo
	 * @return
	 * @throws DaoException
	 */
	public abstract List<Map<String, String>> selectItemBySettingNo(String locNo, String settingNo)
			throws ServiceException;

	/**
	 * 查询同一生效对象在整个策略中是否存在
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	public int selectSelectValueCount(CsInstockSetting set, String settingValue) throws ServiceException;
}