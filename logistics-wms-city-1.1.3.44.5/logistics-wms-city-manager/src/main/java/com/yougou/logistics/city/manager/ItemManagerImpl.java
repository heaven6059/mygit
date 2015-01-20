package com.yougou.logistics.city.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.service.ItemService;

@Service("itemManager")
class ItemManagerImpl extends BaseCrudManagerImpl implements ItemManager {
	@Resource
	private ItemService itemService;

	@Override
	public BaseCrudService init() {
		return itemService;
	}

	@Override
	public int countItemAndSize(Item cc, AuthorityParams authorityParams) throws ManagerException {
		try {
			return itemService.countItemAndSize(cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Item> findItemAndSizePage(SimplePage page, Item cc, AuthorityParams authorityParams) throws ManagerException {
		try {
			return itemService.findItemAndSizePage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findChPlanItemAndSizeCount(Item cc, AuthorityParams authorityParams) throws ManagerException {
		try {
			return itemService.findChPlanItemAndSizeCount(cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Item> findChPlanItemAndSizeByPage(SimplePage page, Item cc, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return itemService.findChPlanItemAndSizeByPage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

}