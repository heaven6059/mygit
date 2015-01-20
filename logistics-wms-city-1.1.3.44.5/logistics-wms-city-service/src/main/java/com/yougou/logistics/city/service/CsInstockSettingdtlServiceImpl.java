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
import com.yougou.logistics.city.common.model.CsInstockSetting;
import com.yougou.logistics.city.dal.mapper.CsInstockSettingdtlMapper;

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
@Service("csInstockSettingdtlService")
class CsInstockSettingdtlServiceImpl extends BaseCrudServiceImpl implements CsInstockSettingdtlService {
	@Resource
	private CsInstockSettingdtlMapper csInstockSettingdtlMapper;

	@Override
	public BaseCrudMapper init() {
		return csInstockSettingdtlMapper;
	}

	@Override
	public List<Map<String, String>> selectBrandBySettingNo(String locNo, String settingNo) throws ServiceException {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("locNo", locNo);
			paramMap.put("settingNo", settingNo);
			return csInstockSettingdtlMapper.selectBrandBySettingNo(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, String>> selectCategoryBySettingNo(String locNo, String settingNo) throws ServiceException {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("locNo", locNo);
			paramMap.put("settingNo", settingNo);
			return csInstockSettingdtlMapper.selectCategoryBySettingNo(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Map<String, String>> selectItemBySettingNo(String locNo, String settingNo) throws ServiceException {
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("locNo", locNo);
			paramMap.put("settingNo", settingNo);
			return csInstockSettingdtlMapper.selectItemBySettingNo(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectSelectValueCount(CsInstockSetting set, String settingValue) throws ServiceException {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("locno", set.getLocno());
			paramMap.put("setType", set.getSetType());
			paramMap.put("detailType", set.getDetailType());
			paramMap.put("selectValue", settingValue);
			return csInstockSettingdtlMapper.selectSelectValueCount(paramMap);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}