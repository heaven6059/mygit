package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:56:15 CST 2013
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
public interface BillImInstockDirectService extends BaseCrudService {
    /**
     * 取消定位，更新定位状态
     * 
     * @param locNo
     * @param ownerNo
     * @param sourceNo
     * @param itemNoStr
     * @throws ServiceException
     */
    public void cancelDirect(String locNo, String receiptNo, String ownerNo,
	    String rowIdStr) throws ServiceException;

    /**
     * 发单
     * 
     * @param locNo
     * @param rowStrs
     * @throws ServiceException
     */
    public void createInstock(String curUser, String locNo, String rowStrs,
	    String instockWorker) throws ServiceException;

    /**
     * 根据验收单号查询定位信息总数
     * 
     * @param objEntiy
     * @param authorityParams TODO
     * @return
     * @throws ManagerException
     */
    public int countInstockDirectByMainId(BillImInstockDirect objEntiy, AuthorityParams authorityParams)
	    throws ServiceException;

    /**
     * 根据验收单号查询定位信息列表带分页
     * @param objEntiy
     * @param authorityParams TODO
     * 
     * @return
     * @throws ManagerException
     */
    public List<BillImInstockDirect> findInstockDirectByMainIdPage(
	    SimplePage page, BillImInstockDirect objEntiy, AuthorityParams authorityParams)
	    throws ServiceException;

    public int countInstockDirectByType(Map<String, Object> map, AuthorityParams authorityParams)
	    throws ServiceException;

    public List<BillImInstockDirect> findInstockDirectByTypePage(
	    SimplePage page, Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

    public SumUtilMap<String, Object> selectSumQty4CheckDirect(
	    BillImInstockDirect map, AuthorityParams authorityParams);

    public SumUtilMap<String, Object> selectInstockDirectByTypePage4Sum(
	    Map<String, Object> map, AuthorityParams authorityParams);
}