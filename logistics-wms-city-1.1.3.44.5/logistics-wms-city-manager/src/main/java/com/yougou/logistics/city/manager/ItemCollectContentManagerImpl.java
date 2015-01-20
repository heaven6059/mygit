package com.yougou.logistics.city.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.ItemCollectContentService;

@Service("itemCollectContentManager")
class ItemCollectContentManagerImpl extends BaseCrudManagerImpl implements ItemCollectContentManager {
    @Resource
    private ItemCollectContentService itemCollectContentService;

    @Override
    public BaseCrudService init() {
        return itemCollectContentService;
    }
    
    @Override
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return itemCollectContentService.selectSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

}