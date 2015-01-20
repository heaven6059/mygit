/*
 * 类名 com.yougou.logistics.city.dal.database.BillHmPlanDtlMapper
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillHmPlanDtlMapper extends BaseCrudMapper {
	
	/**
	 * 查询最大的行号
	 * @param billHmPlanDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillHmPlanDtl billHmPlanDtl) throws DaoException;
	

    /**
     * 根据退厂计划生成明细
     * @param params
     * @return
     */
    public int insertByWmPlan(@Param("list")List<BillHmPlanDtl> addPlanDtlList);
    
    /**
     * 查询退厂计划是否重复
     * @param params
     * @return
     * @throws DaoException
     */
    public List<BillHmPlanDtl> selectDuplicateRecord(@Param("params") Map<String, Object> params) throws DaoException;
    
    /**
     * 验证退厂计划过来的储位
     * @param params
     * @return
     * @throws DaoException
     */
    public List<BillHmPlanDtl> selectCheckHwPlanCellNo(@Param("params") Map<String, Object> params) throws DaoException;
    
    /**
     * 验证退厂计划过来的储位属性品质
     * @param params
     * @return
     * @throws DaoException
     */
    public List<BillHmPlanDtl> selectCheckHwPlanCellNoItemType(@Param("params") Map<String, Object> params) throws DaoException;
	
    public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> params, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
    
    public List<BillHmPlanDtl> selectInsertHmPlan4WmPlan(@Param("params") Map<String, Object> params) throws DaoException;
    
    public List<BillHmPlanDtl> selectBillHmPlanDtl (@Param("params") Map<String, Object> params, @Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
}