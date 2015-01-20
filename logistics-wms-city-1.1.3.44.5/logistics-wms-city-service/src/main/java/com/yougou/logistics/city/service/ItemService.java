package com.yougou.logistics.city.service;

import java.util.List;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.Item;

public interface ItemService extends BaseCrudService {

	public Item selectByCode(String cateCode, AuthorityParams authorityParams) throws ServiceException;

	public int countItemAndSize(Item cc, AuthorityParams authorityParams) throws ServiceException;

	public List<Item> findItemAndSizePage(SimplePage page, Item cc, AuthorityParams authorityParams) throws ServiceException;
	
	public int findChPlanItemAndSizeCount(Item cc, AuthorityParams authorityParams) throws ServiceException;

	public List<Item> findChPlanItemAndSizeByPage(SimplePage page, Item cc, AuthorityParams authorityParams) throws ServiceException;

	public List<Item> findChPlanItemAndSize4Brand(Item cc) throws ServiceException;
	
	public List<Item> isChPlanItemCheck(Item item) throws ServiceException;
	
	
	
	public List<Item> queryBrandListByParentNo(String parentNo ,AuthorityParams authorityParams) throws ServiceException;
}