package com.yougou.logistics.city.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.api.dto.BaseLmp2WmsDto;
import com.yougou.logistics.city.dal.mapper.BaseLmp2WmsMapper;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午3:18:36
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("baseLmp2WmsService")
public class BaseLmp2WmsServiceImpl extends BaseCrudServiceImpl implements BaseLmp2WmsService {

	@Resource
	private BaseLmp2WmsMapper baseLmp2WmsMapper;

	@Override
	public BaseCrudMapper init() {
		return null;
	}

	@Override
	public List<BaseLmp2WmsDto> getBaseCheck4Wms(List<String> sysNoList, List<String> interFaceNameList)
			throws ServiceException {
		try {
			List<BaseLmp2WmsDto> list = baseLmp2WmsMapper.selectBaseCheck4Wms(sysNoList, interFaceNameList);
			return list;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
