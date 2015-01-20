package com.yougou.logistics.city.common.api;

import java.util.List;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.dto.BaseLmp2WmsDto;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午2:44:30
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BaseLmp2WmsServiceApi {
	
	public List<BaseLmp2WmsDto> getBaseCheck4Wms(List<String> sysNoList,List<String> interFaceNameList) throws RpcException;

}
