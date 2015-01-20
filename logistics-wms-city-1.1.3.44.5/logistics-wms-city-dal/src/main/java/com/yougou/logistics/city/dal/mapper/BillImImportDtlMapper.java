package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.dto.BillImImportDtlSizeKind;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.BillImImportDtlKey;
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillImImportDtlForPage;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-25 10:24:56
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
public interface BillImImportDtlMapper extends BaseCrudMapper {

    /**
     * 根据主键删除数据
     * 
     * @param billImImportDtlKey
     * @return
     * @throws DaoException
     */
    public int deleteByPrimaryKey(BillImImportDtlKey billImImportDtlKey)
	    throws DaoException;

    public int selectBoxNoByDetailCount(@Param("params")Map<String, Object> params,@Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    public int selectMaxPid(BillImImportDtlKey billImImportDtlKey)
	    throws DaoException;

    public List<BillImImportDtl> selectBoxNoByDetailPage(
    		@Param("dtlPage")BillImImportDtlForPage dtlPage,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

    public List<BillImImportDtl> selectBoxNoListByDetail(
	    BillImImportDtlKey billImImportDtlKey) throws DaoException;

    public List<BillImImportDtl> selectNotCheckBoxNoByDetail(
	    BillImImportDtlKey billImImportDtlKey) throws DaoException;

    public int selectDetailCountByImportNo(@Param("params") Map map,@Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    public List<BillImImportDtlDto> selectDetailByImportNo(
	    @Param("page") SimplePage page, @Param("params") Map map,@Param("authorityParams") AuthorityParams authorityParams)
	    throws DaoException;

    public int modifyImImportDtl(BillImImportDtl billImImportDtl)
	    throws DaoException;

    public int selectBoxNoIsHave(BillImImportDtlKey billImImportDtlKey)
	    throws DaoException;

    public int selectCountMx(@Param("dto")BillImImportDtlSizeKind dto,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListByImportNo(
	    BillImImportDtlSizeKind dto) throws DaoException;

    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListGroupBy(
	    @Param("page") SimplePage page,
	    @Param("dto") BillImImportDtlSizeKind dto,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;

    /**
     * 确认收货更新预到货通知单收货数量
     * 
     * @param dtl
     */
    public void updateReceiptQty(@Param("dtl") BillImImportDtl dtl);

    /**
     * 查询已收货的数量
     * 
     * @param dtl
     * @return
     */
    public int selectReceiptedCount(@Param("dtl") Map<String, Object> map);

    public List<BillImImportDtl> selectRepeat(@Param("dtl") BillImImport dtl);

    public SumUtilMap<String, Object> selectSumQty(
	    @Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams);
    
    /**
     * 更新全部收货数量
     * @param billImInstockDirect
     */
    public void updateAllReceiptQty(BillImImportDtl billImImportDtl);
}