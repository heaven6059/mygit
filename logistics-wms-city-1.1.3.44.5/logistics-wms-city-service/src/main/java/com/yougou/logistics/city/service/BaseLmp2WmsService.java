package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.api.dto.BaseLmp2WmsDto;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午3:17:49
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BaseLmp2WmsService extends BaseCrudService{
	
	public List<BaseLmp2WmsDto> getBaseCheck4Wms(List<String> sysNoList,List<String> interFaceNameList) throws ServiceException;

}
