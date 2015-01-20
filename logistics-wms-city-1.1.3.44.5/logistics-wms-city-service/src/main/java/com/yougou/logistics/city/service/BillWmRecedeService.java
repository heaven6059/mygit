package com.yougou.logistics.city.service;

import java.util.List;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.common.model.BillWmRecede;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
public interface BillWmRecedeService extends BaseCrudService {

	/**
	 * 退厂通知单定位
	 * @param maps
	 * @throws DaoException
	 */
	public void procBillWmRecedeLocateQuery(List<BillWmOutstockDirect> listWmRecedes) throws ServiceException;

	/**
	 * 退厂任务分派总页数
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findBillWmRecedeJoinDirectCount(BillWmRecede billWmRecede, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 查询退厂任务分派下架指示表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmRecede> findBillWmRecedeJoinDirectByPage(SimplePage page, String orderByField, String orderBy,
			BillWmRecede billWmRecede, AuthorityParams authorityParams) throws ServiceException;
	
	
	/**
	 * 退仓复核退厂调度汇总查询总页数
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public int findBillWmRecedeGroupCount(BillWmRecede billWmRecede,AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 退仓复核退厂调度信息列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param billWmRecede
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillWmRecede> findBillWmRecedeGroupByPage(SimplePage page, String orderByField, String orderBy,
			BillWmRecede billWmRecede, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 更新退厂通知单状态为拣货状态
	 * @return
	 */
	public int updateRecedeStatus4Outstock(BillWmRecede billWmRecede) throws ServiceException;
}