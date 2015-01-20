package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillImDifRecord;
import com.yougou.logistics.city.common.model.BillImDifRecordDtl;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
public interface BillImDifRecordDtlManager extends BaseCrudManager {
	
	/**
	 * 查询商品
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public int selectContentCount(Map<String,Object> params)throws ManagerException;
	public List<BillImDifRecordDtl> selectContent(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ManagerException;
	
	/**
	 * 保存明细
	 */
	public <ModelType> Map<String, Object> addDtl(BillImDifRecord billImDifRecord,
			Map<CommonOperatorEnum, List<ModelType>> params) throws ManagerException;
}