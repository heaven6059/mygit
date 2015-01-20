package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 
 * 打印机manager
 * 
 * @author qin.dy
 * @date 2013-11-1 下午2:36:57
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BmDefprinterManager extends BaseCrudManager {

	int deleteBatch(String ids);
}