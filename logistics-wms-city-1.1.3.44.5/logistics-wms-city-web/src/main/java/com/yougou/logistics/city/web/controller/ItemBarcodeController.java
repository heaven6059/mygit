package com.yougou.logistics.city.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ItemBarcode;
import com.yougou.logistics.city.manager.ItemBarcodeManager;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-11-21 下午3:34:56
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/item_barcode")
public class ItemBarcodeController extends BaseCrudController<ItemBarcode> {

	@Log
	private Logger log;
	
    @Resource
    private ItemBarcodeManager itemBarcodeManager;
	@Override
	protected CrudInfo init() {
		return new CrudInfo("item_barcode/",itemBarcodeManager);
	}

}
