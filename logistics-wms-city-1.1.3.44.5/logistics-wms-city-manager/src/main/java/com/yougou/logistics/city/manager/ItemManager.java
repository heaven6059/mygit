package com.yougou.logistics.city.manager;

import java.util.List;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.Item;

public interface ItemManager extends BaseCrudManager {

	public int countItemAndSize(Item cc , AuthorityParams authorityParams) throws ManagerException;

	public List<Item> findItemAndSizePage(SimplePage page, Item cc, AuthorityParams authorityParams) throws ManagerException;
	
	public int findChPlanItemAndSizeCount(Item cc, AuthorityParams authorityParams) throws ManagerException;

	public List<Item> findChPlanItemAndSizeByPage(SimplePage page, Item cc, AuthorityParams authorityParams) throws ManagerException;

}