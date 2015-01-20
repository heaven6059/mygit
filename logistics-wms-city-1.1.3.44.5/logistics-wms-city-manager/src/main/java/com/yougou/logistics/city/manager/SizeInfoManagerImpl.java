package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.service.SizeInfoService;

@Service("sizeInfoManager")
class SizeInfoManagerImpl extends BaseCrudManagerImpl implements
	SizeInfoManager {
    @Resource
    private SizeInfoService sizeInfoService;

    @Override
    public BaseCrudService init() {
	return sizeInfoService;
    }



	@Override
	public List<SizeInfo> selectSizeInfoBySizeNoList(List<String> sizeKindList, String sysNo,
			AuthorityParams authorityParams) throws ManagerException {
		try {
		    return sizeInfoService.selectSizeInfoBySizeNoList(sizeKindList, sysNo,authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}



	@Override
	public List<SizeInfo> findSizeInfoBySizeNo(SizeInfo sizeInfo,
			AuthorityParams authorityParams) throws ManagerException {
		try {
		    return sizeInfoService.findSizeInfoBySizeNo(sizeInfo, authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

}