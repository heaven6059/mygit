package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.Lookupdtl;

public interface LookupdtlService extends BaseCrudService {
	
	/**
	 * 查询拣货任务分派表的出库类型
	 * @param lookupdtlKey
	 * @return
	 * @throws ServiceException
	 */
	public  List<Lookupdtl>  selectOutstockDirectExpType(Lookupdtl Lookupdtl)throws ServiceException;

	/**
	 * 根据品牌查询码表明细信息
	 * @param lookupDtl
	 * @return
	 * @throws ServiceException
	 */
	public List<Lookupdtl> selectLookupdtlBySysNo(Lookupdtl lookupDtl)throws ServiceException;
	
	
	public List<Lookupdtl> selectLookupdtlByCode(Map<String, Object> params)throws ServiceException;
	
}