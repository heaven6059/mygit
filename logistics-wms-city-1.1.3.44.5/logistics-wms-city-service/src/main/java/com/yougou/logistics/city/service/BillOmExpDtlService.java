package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillOmExpDtlDTO;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.model.BillOmExpDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmExpDtlForPage;

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
public interface BillOmExpDtlService extends BaseCrudService {

    public int deleteByPrimaryKey(BillOmExpDtlKey billOmExpDtlKey)
	    throws ServiceException;

    public int selectItemNoByDetailCount(Map<String, Object> params)
	    throws ServiceException;

    public List<BillOmExpDtl> selectItemNoByDetailPage(
	    BillOmExpDtlForPage dtlPage) throws ServiceException;

    public int selectCountMx(BillOmExpDtlDTO dto,AuthorityParams authorityParams) throws ServiceException;

    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOBExpNo(BillOmExpDtlDTO dto,AuthorityParams authorityParams)
	    throws ServiceException;

    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOGroupBy(
	    @Param("page") SimplePage page, @Param("dto") BillOmExpDtlDTO dto,AuthorityParams authorityParams)
	    throws ServiceException;

    /**
     * 出库调度明细总页数
     * 
     * @param billOmLocate
     * @param authorityParams
     * @return
     * @throws ServiceException
     */
    public int findBillOmExpDtlDispatchCount(
	    @Param("params") BillOmExpDispatchDtlDTO billOmExpDtl,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws ServiceException;

    /**
     * 出库调度明细信息列表
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param billOmExp
     * @param authorityParams
     * @return
     * @throws ServiceException
     */
    public List<BillOmExpDispatchDtlDTO> findBillOmExpDtlDispatchByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params") BillOmExpDispatchDtlDTO billOmExpDtl,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws ServiceException;

    public List<BillOmExpDtl> selectStore(BillOmExpDtl billOmExpDtl)
	    throws ServiceException;
    
    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
    
    public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException;
}