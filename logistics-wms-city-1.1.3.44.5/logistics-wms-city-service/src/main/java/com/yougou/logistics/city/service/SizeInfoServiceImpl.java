package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.dal.database.SizeInfoMapper;

@Service("sizeInfoService")
class SizeInfoServiceImpl extends BaseCrudServiceImpl implements
	SizeInfoService {

    @Resource
    private SizeInfoMapper sizeInfoMapper;

    @Override
    public BaseCrudMapper init() {
	return sizeInfoMapper;
    }
    
    public List<SizeInfo> selectSizeInfoBySizeNoList(List<String> list,
	    String sysNo,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return sizeInfoMapper.selectSizeInfoBySizeNoList(list, sysNo,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

	@Override
	public List<String> findSizeCodeBySysAndKind(Map<String, Object> params) {
		return sizeInfoMapper.selectSizeCodeBySysAndKind(params);
	}
	
	@Override
    public List<SizeInfo> findSizeInfoBySizeNo(SizeInfo sizeInfo,AuthorityParams authorityParams)
	    throws ServiceException {
		try {
		    return sizeInfoMapper.selectSizeInfoBySizeNo(sizeInfo,authorityParams);
		} catch (Exception e) {
		    throw new ServiceException(e);
		}
    }
}