package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
public interface BillConAdjDtlManager extends BaseCrudManager {
	
	/**
	 * 库存商品列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public List<BillConAdjDtl> selectItem(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	public int selectItemCount(Map<String,Object> params,AuthorityParams authorityParams)throws ManagerException;
	
	/**
	 * 审核
	 * @param locNo
	 * @param cellNo
	 * @param ownerNo
	 * @param itemNo
	 * @param barCode
	 * @param adjType
	 * @param sType
	 * @return
	 * @throws ManagerException
	 */
	public Map<String,Object> examineAdj(String ids, String auditor, String userName,AuthorityParams authorityParams) throws ManagerException;
	
	/**
	 * 
	 * @param billConAdj
	 * @param params
	 * @return
	 * @throws ManagerException
	 */
	public <ModelType> Map<String, Object> addDtlsave(BillConAdj billConAdj,
			Map<CommonOperatorEnum, List<ModelType>> params,String editor) throws ManagerException;
	
	public Map<String, Object> addDtlByCell(List<String> list,Map<String,String> map) throws ManagerException;
	
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
	
//	/**
//	 * 查找最大ID
//	 * @param model
//	 * @return
//	 * @throws ManagerException
//	 */
//	public int selectMaxRowId(BillConAdjDtl model)throws ManagerException;
//	
//	/**
//	 * 储位检查
//	 * @param cellNo
//	 * @param itemNo
//	 * @param sizeNo
//	 * @param adjNo
//	 * @return
//	 * @throws ManagerException
//	 */
//	public int selectCellId(Map<String, Object> map)throws ManagerException;
//	
//	/**
//	 * 库存数据检查
//	 * @param locNo
//	 * @param cellNo
//	 * @param ownerNo
//	 * @param itemNo
//	 * @param barCode
//	 * @param adjType
//	 * @param sType
//	 * @return
//	 * @throws ManagerException
//	 */
//	public int checkAty(String locNo,String cellNo,String ownerNo,String itemNo,String barCode,String adjType,String sType)throws ManagerException;
//	
	/**
	 * 导入Excel
	 * @param list
	 * @param adjNo
	 * @param ownerNo
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public String importDtl(List<BillConAdjDtl> list,String adjNo,String ownerNo,SystemUser user) throws ManagerException;
	
	/**
	 * 批量插入明细
	 * @param list
	 * @throws ManagerException
	 */
	public void batchInsertDtl4ConvertGoods(List<BillConAdjDtl> list) throws ManagerException;
	
	/**
     * 库存调整打印明细
     * @param list
     * @throws ManagerException
     */
	public List<Map<String, Object>> findAllDtl(String keys,AuthorityParams authorityParams)throws ManagerException;
	/**
	 * 根据容器库存调整
	 * @param list
	 * @param map
	 * @return
	 * @throws ManagerException
	 */
	public Map<String, Object> addDtlByConNo(List<String> list,Map<String,String> map) throws ManagerException;
}