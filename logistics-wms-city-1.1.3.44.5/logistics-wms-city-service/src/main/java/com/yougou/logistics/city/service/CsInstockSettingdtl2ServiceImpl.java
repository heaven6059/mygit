package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.CsInstockSettingdtl2Mapper;

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
@Service("csInstockSettingdtl2Service")
class CsInstockSettingdtl2ServiceImpl extends BaseCrudServiceImpl implements CsInstockSettingdtl2Service {
	@Resource
	private CsInstockSettingdtl2Mapper csInstockSettingdtl2Mapper;

	@Override
	public BaseCrudMapper init() {
		return csInstockSettingdtl2Mapper;
	}

	@Override
	public List<Map<String, String>> selectAreaBySettingNo(String locNo, String settingNo) throws ServiceException {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("locNo", locNo);
			paramMap.put("settingNo", settingNo);
			return csInstockSettingdtl2Mapper.selectAreaBySettingNo(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, String>> selectStockBySettingNo(String locNo, String settingNo) throws ServiceException {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("locNo", locNo);
			paramMap.put("settingNo", settingNo);
			return csInstockSettingdtl2Mapper.selectStockBySettingNo(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, String>> selectCellBySettingNo(String locNo, String settingNo) throws ServiceException {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("locNo", locNo);
			paramMap.put("settingNo", settingNo);
			return csInstockSettingdtl2Mapper.selectCellBySettingNo(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}