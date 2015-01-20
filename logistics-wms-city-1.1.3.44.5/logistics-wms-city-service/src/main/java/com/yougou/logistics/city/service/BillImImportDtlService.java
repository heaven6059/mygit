package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface BillImImportDtlService extends BaseCrudService {
    /**
     * 删除预到货通知单明细
     * 
     * @param locnoStrs
     * @return
     * @throws ManagerException
     */
    public int deleteImImportDtl(BillImImportDtlKey billImImportDtlKey)
	    throws ServiceException;

    public int selectBoxNoByDetailCount(Map<String, Object> params,AuthorityParams authorityParams)
	    throws ServiceException;

    public int selectMaxPid(BillImImportDtlKey billImImportDtlKey)
	    throws ServiceException;

    public List<BillImImportDtl> selectBoxNoByDetailPage(
	    BillImImportDtlForPage dtlPage,AuthorityParams authorityParams) throws ServiceException;

    public List<BillImImportDtl> selectBoxNoListByDetail(
	    BillImImportDtlKey billImImportDtlKey) throws ServiceException;

    public List<BillImImportDtl> selectNotCheckBoxNoByDetail(
	    BillImImportDtlKey billImImportDtlKey) throws ServiceException;

    public int findDetailCountByImportNo(Map map,AuthorityParams authorityParams) throws ServiceException;

    public List<BillImImportDtlDto> findDetailByImportNo(SimplePage page,
	    Map map,AuthorityParams authorityParams) throws ServiceException;

    public int modifyImImportDtl(BillImImportDtl billImImportDtl)
	    throws ServiceException;

    public int selectBoxNoIsHave(BillImImportDtlKey billImImportDtlKey)
	    throws ServiceException;

    public int selectCountMx(BillImImportDtlSizeKind dto,AuthorityParams authorityParams)
	    throws ServiceException;

    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListByImportNo(
	    BillImImportDtlSizeKind dto) throws ServiceException;

    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListGroupBy(
	    SimplePage page, BillImImportDtlSizeKind dto,AuthorityParams authorityParams)
	    throws ServiceException;

    /***
     * 查询重复的箱号
     * 
     * @param dtl
     * @return
     * @throws ServiceException
     */
    public List<BillImImportDtl> selectRepeat(BillImImport dtl)
	    throws ServiceException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams);
    
    /**
     * 更新全部收货数量
     * @param billImInstockDirect
     */
    public void updateAllReceiptQty(BillImImportDtl billImImportDtl );
}