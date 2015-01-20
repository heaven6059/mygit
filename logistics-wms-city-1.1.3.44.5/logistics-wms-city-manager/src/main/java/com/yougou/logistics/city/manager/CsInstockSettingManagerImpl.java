package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.CsInstockSettingDto;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.model.Category;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefstock;
import com.yougou.logistics.city.common.model.CsInstockSetting;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.service.BrandService;
import com.yougou.logistics.city.service.CategoryService;
import com.yougou.logistics.city.service.CmDefareaService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.CmDefstockService;
import com.yougou.logistics.city.service.CsInstockSettingService;
import com.yougou.logistics.city.service.CsInstockSettingdtl2Service;
import com.yougou.logistics.city.service.CsInstockSettingdtlService;
import com.yougou.logistics.city.service.ItemService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Oct 08 15:13:38 CST 2013
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
@Service("csInstockSettingManager")
class CsInstockSettingManagerImpl extends BaseCrudManagerImpl implements CsInstockSettingManager {
	@Resource
	private CsInstockSettingService csInstockSettingService;

	@Resource
	private BrandService brandService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ItemService itemService;

	@Resource
	private CmDefareaService cmDefareaService;

	@Resource
	private CmDefstockService cmDefstockService;

	@Resource
	private CmDefcellService cmDefcellService;

	@Resource
	private CsInstockSettingdtlService csInstockSettingdtlService;

	@Resource
	private CsInstockSettingdtl2Service csInstockSettingdtl2Service;

	private final static String TYPE_0 = "0";

	private final static String TYPE_1 = "1";

	private final static String TYPE_2 = "2";

	private final static String TYPE_3 = "3";

	@Override
	public BaseCrudService init() {
		return csInstockSettingService;
	}

	@Override
	public int addCsInstockSetting(CsInstockSettingDto dto) throws ManagerException {
		int count = 0;
		try {
			count = csInstockSettingService.addCsInstockSetting(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return count;
	}

	@Override
	public int editCsInstockSetting(CsInstockSettingDto dto) throws ManagerException {
		int count = 0;
		try {
			count = csInstockSettingService.editCsInstockSetting(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		return count;
	}

	@Override
	public int deleteCsInstockSetting(String keyStr) throws ManagerException {
		int count = 0;
		try {
			count = csInstockSettingService.deleteCsInstockSetting(keyStr);
		} catch (ServiceException e) {

		}
		return count;
	}

	@Override
	public Map<String, String> selectItemByCode(CsInstockSetting set, String settingValue) throws ManagerException {
		Map<String, String> map = null;
		try {

			int settingCount = csInstockSettingdtlService.selectSelectValueCount(set, settingValue);
			if (settingCount > 0) {
				map = new HashMap<String, String>();
				map.put("result", "error");
				map.put("msg", "编码已经存在，请重新输入");
				return map;
			}
			String type = set.getDetailType();
			if (TYPE_0.equals(type)) {
				Brand brand = brandService.selectByCateCode(settingValue,null);
				if (null != brand) {
					map = new HashMap<String, String>();
					map.put("code", brand.getBrandCode());
					map.put("name", brand.getBrandName());
					map.put("result", "success");
					return map;
				}
			} else if (TYPE_1.equals(type)) {
				Category cate = categoryService.selectByCateCode(settingValue);
				if (null != cate) {
					map = new HashMap<String, String>();
					map.put("code", cate.getCateCode());
					map.put("name", cate.getCateName());
					map.put("result", "success");
					return map;
				}
			} else if (TYPE_2.equals(type)) {
				Item item = itemService.selectByCode(settingValue,null);
				if (null != item) {
					map = new HashMap<String, String>();
					map.put("code", item.getItemCode());
					map.put("name", item.getItemName());
					map.put("result", "success");
					return map;
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return null;
	}

	@Override
	public List<Map<String, String>> queryItemBySettingNo(String type, String locNo, String settingNo)
			throws ManagerException {
		List<Map<String, String>> list = null;
		try {
			if (TYPE_0.equals(type)) {
				list = csInstockSettingdtlService.selectBrandBySettingNo(locNo, settingNo);
			} else if (TYPE_1.equals(type)) {
				list = csInstockSettingdtlService.selectCategoryBySettingNo(locNo, settingNo);
			} else if (TYPE_2.equals(type)) {
				list = csInstockSettingdtlService.selectItemBySettingNo(locNo, settingNo);
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return list;
	}

	@Override
	public Map<String, String> selectCellByCode(String type, String locNo, String code) throws ManagerException {
		Map<String, String> map = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("locno", locNo);
		try {
			if (TYPE_1.equals(type)) {
				paramMap.put("code", code);
				@SuppressWarnings("unchecked")
				List<CmDefarea> list = (List<CmDefarea>) cmDefareaService.findByWareAndArea(paramMap);
				if (list != null) {
					CmDefarea area = list.get(0);
					map = new HashMap<String, String>();
					map.put("code", area.getAreaNo());
					map.put("name", area.getAreaName());
				}
			} else if (TYPE_2.equals(type)) {
				paramMap.put("stockNo", code);
				List<CmDefstock> list = cmDefstockService.findByBiz(null, paramMap);
				if (list != null) {
					CmDefstock stock = list.get(0);
					map = new HashMap<String, String>();
					map.put("code", stock.getStockNo());
					map.put("name", stock.getaStockNo());
				}
			} else if (TYPE_3.equals(type)) {
				paramMap.put("cellNo", code);
				List<CmDefcell> list = cmDefcellService.findByBiz(null, paramMap);
				if (list != null) {
					CmDefcell cell = list.get(0);
					map = new HashMap<String, String>();
					map.put("code", cell.getCellNo());
					map.put("name", cell.getCellNo());
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return map;
	}

	@Override
	public List<Map<String, String>> queryCellBySettingNo(String type, String locNo, String settingNo)
			throws ManagerException {
		List<Map<String, String>> list = null;
		try {
			if (TYPE_1.equals(type)) {
				list = csInstockSettingdtl2Service.selectAreaBySettingNo(locNo, settingNo);
			} else if (TYPE_2.equals(type)) {
				list = csInstockSettingdtl2Service.selectStockBySettingNo(locNo, settingNo);
			} else if (TYPE_3.equals(type)) {
				list = csInstockSettingdtl2Service.selectCellBySettingNo(locNo, settingNo);
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return list;
	}

}