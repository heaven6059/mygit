package com.yougou.logistics.city.dal.database;

import java.util.List;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.AccInventoryCon;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-15 17:22:25
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
public interface AccInventoryConMapper extends BaseCrudMapper {
	
	public int insertAccInverntory(AccInventoryCon accInventoryCon);
	
	public void batchInsertAccInventory(List<AccInventoryCon> list);
	
	/**
	 * 加锁更新
	 * @param accInventoryCon
	 */
	public void updatelockAccinverntoryCon(AccInventoryCon accInventoryCon);
	
	public AccInventoryCon selectAccInvertyroConExist(AccInventoryCon accInventoryCon);
	
	public int deleteAccinverntoryCon(AccInventoryCon accInventoryCon);
	
	public void updateAccinverntoryCon(AccInventoryCon accInventoryCon);
	
	/**
	 * 更新容器总账数量及修改人信息
	 * @param accInventoryCon
	 */
	public void updateAccountConByPrimaryKey(AccInventoryCon accInventoryCon);
}