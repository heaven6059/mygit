package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillOmExpDtlDTO;
import com.yougou.logistics.city.common.model.BillOmExp;
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
public interface BillOmExpDtlMapper extends BaseCrudMapper {

    public int deleteByPrimaryKey(BillOmExpDtlKey billOmExpDtlKey)
	    throws DaoException;

    public int selectItemNoByDetailCount(Map<String, Object> params)
	    throws DaoException;

    public List<BillOmExpDtl> selectItemNoByDetailPage(
	    BillOmExpDtlForPage dtlPage) throws DaoException;

    public int selectCountMx(@Param("dto") BillOmExpDtlDTO dto,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOBExpNo( @Param("dto") BillOmExpDtlDTO dto,@Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOGroupBy(
	    @Param("page") SimplePage page, @Param("dto") BillOmExpDtlDTO dto,@Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 出库调度明细总页数
     * 
     * @param billOmLocate
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public int selectBillOmExpDtlDispatchCount(
	    @Param("params") BillOmExpDispatchDtlDTO billOmExpDtl,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 出库调度明细信息列表
     * 
     * @param page
     * @param orderByField
     * @param orderBy
     * @param billOmExp
     * @param authorityParams
     * @return
     * @throws DaoException
     */
    public List<BillOmExpDispatchDtlDTO> selectBillOmExpDtlDispatchByPage(
	    @Param("page") SimplePage page,
	    @Param("orderByField") String orderByField,
	    @Param("orderBy") String orderBy,
	    @Param("params") BillOmExpDispatchDtlDTO billOmExpDtl,
	    @Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    /**
     * 根据主键
     * 
     * @param billOmExpDtl
     * @return
     * @throws DaoException
     */
    public BillOmExpDtl selectBillOmExpDtlByItemInfo(BillOmExpDtl billOmExpDtl)
	    throws DaoException;

    /**
     * 查询客户
     * 
     * @param billOmExpDtl
     * @return
     */
    public List<BillOmExpDtl> selectStore(BillOmExpDtl billOmExpDtl) throws DaoException;
    public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
    public SumUtilMap<String, Object> selectDispatchSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

    /**
	 * 修改发货通知单状态
	 * @param billOmExp
	 * @return
	 * @throws DaoException
	 */
	public int updateOmExpDtlStatusByExpNo(BillOmExpDtl billOmExpDtl) throws DaoException;
	
	/**
	 * 插入明细
	 * @param billOmExpDtl
	 * @return
	 * @throws DaoException
	 */
	public int insertSplitByExpDtl(BillOmExpDtl billOmExpDtl) throws DaoException;
	
	public int insertSplitASNByExpDtl(BillOmExpDtl billOmExpDtl) throws DaoException;
	
    // /**
    // * 复核完成更新出库明细表
    // * @param billOmExpDtl
    // * @return
    // * @throws DaoException
    // */
    // public int updateBillOmExpDtlByRecheckInfo(BillOmExpDtl
    // billOmExpDtl)throws DaoException;
}