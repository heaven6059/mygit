package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillUmUntreadDtlService extends BaseCrudService {
	public void saveUntreadDtl(List<ConBoxDtl> insertList, List<ConBoxDtl> updateList, List<ConBoxDtl> deleteList,
			BillUmUntread untread) throws ServiceException;

	public List<BillUmUntreadDtl> selectAllBox(@Param("params") BillUmUntread untread, AuthorityParams authorityParams) throws ServiceException;

	public int findCountByBox(Map<String, Object> params) throws ServiceException;

	/**
	 * 查询明细表中的箱子
	 * 
	 * @param page
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillUmUntreadDtl> findByPageByBox(SimplePage page, Map<String, Object> params) throws ServiceException;

	/**
	 * 根据主键及箱商品编码尺码更新数量
	 * 
	 * @param billUmUntreadDtl
	 * @return
	 * @throws ServiceException
	 */
	public int modifyByItemAndKey(BillUmUntreadDtl billUmUntreadDtl) throws ServiceException;

	public int select4BoxCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	public List<ConBoxDtl> select4Box(Map<String, Object> params, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException;

	public List<Map<String, Object>> queryPrints(String locno, String keys) throws ServiceException;

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
}