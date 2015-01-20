package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.ItemBarcodeService;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-11-21 下午3:32:41
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("itemBarcodeManager")
public class ItemBarcodeManagerImpl extends BaseCrudManagerImpl implements ItemBarcodeManager{
	
	@Resource
    private ItemBarcodeService itemBarcodeService;
	@Override
	protected BaseCrudService init() {
		return itemBarcodeService;
	}

}
