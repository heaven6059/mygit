package com.yougou.logistics.city.manager.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.BaseLmp2WmsServiceApi;
import com.yougou.logistics.city.common.api.dto.BaseLmp2WmsDto;
import com.yougou.logistics.city.service.BaseLmp2WmsService;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午2:44:58
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("baseLmp2WmsServiceApi")
public class BaseLmp2WmsServiceApiImpl implements BaseLmp2WmsServiceApi {
	
	@Resource
    private BaseLmp2WmsService baseLmp2WmsService;

	@Override
	public List<BaseLmp2WmsDto> getBaseCheck4Wms(List<String> sysNoList, List<String> interFaceNameList) throws RpcException {
		try {
			return baseLmp2WmsService.getBaseCheck4Wms(sysNoList, interFaceNameList);
		} catch (Exception e) {
			throw new RpcException("logistics-wms-city", e);
		}
	}
	
}
