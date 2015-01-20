package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillOmExpDtlDTO;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-29 16:50:42
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
public interface BillOmExpDtlManager extends BaseCrudManager {

    public <ModelType> boolean addBillOmExpDtl(String locno, String ownerNo,
	    String expNo, String expDate,
	    Map<CommonOperatorEnum, List<ModelType>> params, String loginName)
	    throws ManagerException;

    public Map<String, Object> selectItemNoByDetailPageCount(int pageNo,
	    int pageSize, String orderByField, String orderBy,
	    Map<String, Object> params) throws ManagerException;

    public int selectCountMx(BillOmExpDtlDTO dto,AuthorityParams authorityParams) throws ManagerException;

    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOBExpNo(BillOmExpDtlDTO dto,AuthorityParams authorityParams)
	    throws ManagerException;

    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOGroupBy(
	    @Param("page") SimplePage page, @Param("dto") BillOmExpDtlDTO dto,AuthorityParams authorityParams)
	    throws ManagerException;

    /**
     * 出库调度明细总页数
     * 
     * @param billOmLocate
     * @param authorityParams
     * @return
     * @throws ManagerException
     */
    public int findBillOmExpDtlDispatchCount(
	    @Param("params") BillOmExpDispatchDtlDTO billOmExpDtl,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws ManagerException;

    /**
     * 出库调度明细信息列表
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param billOmExp
     * @param authorityParams
     * @return
     * @throws ManagerException
     */
    public List<BillOmExpDispatchDtlDTO> findBillOmExpDtlDispatchByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params") BillOmExpDispatchDtlDTO billOmExpDtl,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws ManagerException;

    public List<BillOmExpDtl> selectStore(BillOmExpDtl billOmExpDtl)
	    throws ManagerException;
    
    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
    
    public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
}