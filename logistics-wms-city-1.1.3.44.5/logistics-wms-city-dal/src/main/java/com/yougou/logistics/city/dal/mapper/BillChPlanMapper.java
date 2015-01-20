/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillChPlanMapper
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
package com.yougou.logistics.city.dal.mapper;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChPlan;

public interface BillChPlanMapper extends BaseCrudMapper {

	public BillChPlan selectByChceckNo(BillChCheck check);
}