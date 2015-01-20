package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface BillImDifRecordDtlService extends BaseCrudService {
	public int selectContentCount(Map<String,Object> params)throws ServiceException;
	public List<BillImDifRecordDtl> selectContent(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ServiceException;
	
	/**
	 * 查询最大序列号
	 * @param billUmReceiptDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillImDifRecordDtl billImDifRecordDtl) throws ServiceException;
}