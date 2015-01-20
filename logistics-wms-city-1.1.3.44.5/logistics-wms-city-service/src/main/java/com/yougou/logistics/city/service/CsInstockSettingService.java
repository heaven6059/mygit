package com.yougou.logistics.city.service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.CsInstockSettingDto;

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
public interface CsInstockSettingService extends BaseCrudService {
	public abstract int addCsInstockSetting(CsInstockSettingDto dto) throws ServiceException;

	public abstract int editCsInstockSetting(CsInstockSettingDto dto) throws ServiceException;

	public abstract int deleteCsInstockSetting(String keyStr) throws ServiceException;
}