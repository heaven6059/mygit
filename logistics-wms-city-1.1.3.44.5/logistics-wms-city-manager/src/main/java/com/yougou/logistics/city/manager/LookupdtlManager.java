package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.Lookupdtl;

public interface LookupdtlManager extends BaseCrudManager {
	
	/**
	 * 查询拣货任务分派表的出库类型
	 * @param lookupcode
	 * @return
	 * @throws ManagerException
	 */
	public  List<Lookupdtl>  selectOutstockDirectExpType(String  lookupcode,String locno)throws ManagerException;

	/**
	 * 根据品牌查询码表明细信息
	 * @param lookupcode
	 * @param sysNo
	 * @return
	 * @throws ManagerException
	 */
	public List<Lookupdtl> selectLookupdtlBySysNo(String lookupcode,
			String sysNo) throws ManagerException;
	
	
	public Map<String,Object> selectLookupdtlByCode(Map<String, Object> params)throws ManagerException;
	
}