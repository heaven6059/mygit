package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
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
public interface BillImInstockDirectManager extends BaseCrudManager {
    /**
     * 取消定位
     * 
     * @param locNo
     * @param receiptNo
     * @param ownerNo
     * @param rowIdStr
     * @throws ManagerException
     */
    public void cancelDirect(String locNo, String receiptNo, String ownerNo,
	    String rowIdStr) throws ManagerException;

    /**
     * 取消定位-支持收货和验收
     */
    public void cancelDirectForAll(String locNo, String ownerNo,
	    String sourceNo, String flag) throws ManagerException;

    /**
     * 发单
     * 
     * @param curUser
     * @param locNo
     * @param rowStrs
     * @param instockWorker
     * @throws ManagerException
     */
    public void createInstock(String curUser, String locNo, String rowStrs,
	    String instockWorker) throws ManagerException;

    /**
     * 收货定位
     * 
     * @param rowStr
     * @throws ManagerException
     */
    public void instockDirect(String rowStr) throws ManagerException;

    /**
     * 验收定位
     * 
     * @param rowStr
     * @throws ManagerException
     */
    public void instockDirectForCheck(String rowStr, String loginName)
	    throws ManagerException;

    /**
     * 根据验收单号查询定位信息总数
     * 
     * @param objEntiy
     * @param authorityParams TODO
     * @return
     * @throws ManagerException
     */
    public int countInstockDirectByMainId(BillImInstockDirect objEntiy, AuthorityParams authorityParams)
	    throws ManagerException;

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
	    throws ManagerException;

    public int countInstockDirectByType(Map<String, Object> map, AuthorityParams authorityParams)
	    throws ManagerException;

    public List<BillImInstockDirect> findInstockDirectByTypePage(
	    SimplePage page, Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

    public SumUtilMap<String, Object> selectSumQty4CheckDirect(
	    BillImInstockDirect map, AuthorityParams authorityParams);

    public void sendOrder(String locNo, String ownerNo, String sender,
	    String keyStr, String loginName, int systemId, int areaSystemId) throws ManagerException;

    public SumUtilMap<String, Object> selectInstockDirectByTypePage4Sum(
	    Map<String, Object> map, AuthorityParams authorityParams);

}