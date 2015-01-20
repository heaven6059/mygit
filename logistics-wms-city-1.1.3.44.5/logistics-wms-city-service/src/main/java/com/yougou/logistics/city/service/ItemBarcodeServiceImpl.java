package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.dal.mapper.ItemBarcodeMapper;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-11-21 下午3:28:07
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("itemBarcodeService")
public class ItemBarcodeServiceImpl  extends BaseCrudServiceImpl implements ItemBarcodeService{
	
	@Resource
	private ItemBarcodeMapper itemBarcodeMapper;
	@Override
	public BaseCrudMapper init() {
		return itemBarcodeMapper;
	}

}
