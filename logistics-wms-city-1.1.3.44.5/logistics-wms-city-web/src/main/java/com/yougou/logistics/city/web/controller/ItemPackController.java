package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.ItemPack;
import com.yougou.logistics.city.manager.ItemPackManager;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Oct 09 19:26:37 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Controller
@RequestMapping("/item_pack")
public class ItemPackController extends BaseCrudController<ItemPack> {
	
	@Log
	private Logger log;
	
	@Resource
	private ItemPackManager itemPackManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("itempack/", itemPackManager);
	}

	@RequestMapping(value = "/selectPackSpec")
	@ResponseBody
	public Map<String, Object> selectPackSpec(String type, String code) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			List<Map<String, String>> list = itemPackManager.selectPackSpec();
			modelMap.put("data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelMap;
	}
}