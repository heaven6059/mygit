package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.CsInstockSettingDto;
import com.yougou.logistics.city.common.model.CsInstockSetting;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Oct 08 15:13:38 CST 2013
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
public interface CsInstockSettingManager extends BaseCrudManager {
	public abstract int addCsInstockSetting(CsInstockSettingDto dto) throws ManagerException;

	public abstract int editCsInstockSetting(CsInstockSettingDto dto) throws ManagerException;

	public abstract int deleteCsInstockSetting(String keyStr) throws ManagerException;

	public abstract Map<String, String> selectItemByCode(CsInstockSetting set, String settingValue)
			throws ManagerException;

	public List<Map<String, String>> queryItemBySettingNo(String type, String locNo, String settingNo)
			throws ManagerException;

	public abstract Map<String, String> selectCellByCode(String type, String locNo, String code)
			throws ManagerException;

	public List<Map<String, String>> queryCellBySettingNo(String type, String locNo, String settingNo)
			throws ManagerException;
}