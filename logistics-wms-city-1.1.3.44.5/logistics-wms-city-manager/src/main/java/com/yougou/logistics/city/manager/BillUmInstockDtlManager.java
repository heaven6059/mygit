package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillUmInstockDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Oct 14 19:59:56 CST 2013
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
public interface BillUmInstockDtlManager extends BaseCrudManager {

    /**
     * 修改或保存明细
     * 
     * @param locno
     * @param instockNo
     * @param ownerNo
     * @param instockWorker
     * @param instockDtlStr
     *            instockId_realCellNo_realQty
     * @return
     * @throws ManagerException
     */
    public String saveDtl(String locno, String instockNo, String ownerNo,
	    String instockWorker, String instockDtlStr) throws ManagerException;

    /**
     * 明细拆分
     * 
     * @param billUmInstockDtl
     * @param realQtyIn
     * @return
     * @throws ManagerException
     */
    public BillUmInstockDtl splitDtl(BillUmInstockDtl billUmInstockDtl,
	    BigDecimal realQtyIn, BigDecimal newRealQty)
	    throws ManagerException;

    /**
     * 删除指定明细
     * 
     * @param billUmInstockDtl
     * @return
     * @throws ManagerException
     */
    public BigDecimal deleteDtl(BillUmInstockDtl billUmInstockDtl)
	    throws ManagerException;

    /**
     * 批量打印
     * 
     * @param locno
     * @param keys
     * @return
     * @throws ManagerException
     */
    public List<String> printDetail(String locno, String keys)
	    throws ManagerException;

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams);
    /**
     * 按计划保存明细(将实际上架储位和实际上架数量设为预上架储位和计划数量)
     * @param locno
     * @param instockNo
     * @param instockWorker TODO
     * @return
     * @throws ManagerException
     */
    public String planSave(String locno, String instockNo, SystemUser user) throws ManagerException;
    /**
     * 单行保存明细
     * @param billUmInstockDtl
     * @return
     */
	public String singleSave(BillUmInstockDtl billUmInstockDtl)throws ManagerException;
	 /**
     * 单行拆分明细
     * @param billUmInstockDtl
     * @return
     */
	public String singleSplit(BillUmInstockDtl billUmInstockDtl)throws ManagerException;
	/**
     * 单行删除明细
     * @param billUmInstockDtl
     * @return
     */
	public String singleDelete(BillUmInstockDtl billUmInstockDtl)throws ManagerException;
}