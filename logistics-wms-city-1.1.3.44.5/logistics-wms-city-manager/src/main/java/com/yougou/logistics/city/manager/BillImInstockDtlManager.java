package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto2;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 30 16:23:58 CST 2013
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
public interface BillImInstockDtlManager extends BaseCrudManager {
    /**
     * 根据商品编号分组查询数量
     * 
     * @param dtl
     * @return
     */
    public int findItemCountBroupByItemNo(@Param("dtl") BillImInstockDtl dtl)
	    throws ManagerException;

    /**
     * 根据商品编码分组查询
     * 
     * @param page
     * @param dtl
     * @return
     * @throws DaoException
     */
    public List<BillImInstockDtl> findItemGroupByItemNo(
	    @Param("page") SimplePage page, @Param("dtl") BillImInstockDtl dtl)
	    throws ManagerException;

    /**
     * 根据商品编号，单号查询详情
     * 
     * @param dtl
     * @return
     * @throws ManagerException
     */
    public List<BillImInstockDtlDto2> findDetailByParams(BillImInstockDtl dtl)
	    throws ManagerException;

    /**
     * 查询品牌库
     * 
     * @param dto
     * @return
     * @throws ManagerException
     */
    public String findSysNo(BillImInstockDtlDto dto) throws ManagerException;

    /**
     * 拆分
     * 
     * @return
     * @throws ManagerException
     */
    public BillImInstockDtlDto splitById(BillImInstockDtlDto dtl)
	    throws ManagerException;

    /**
     * 单行保存明细
     * 
     * @param dtl
     * @throws ManagerException
     */
    public void saveSingle(BillImInstockDtl dtl) throws ManagerException;

    /**
     * 批量保存明细
     * 
     * @param list
     * @throws ManagerException
     */
    public void saveBatch(List<BillImInstockDtlDto> list)
	    throws ManagerException;
    
    public void deleteSingle(BillImInstockDtlDto dto) throws ManagerException;

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

    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);

    public void saveByPlan(BillImInstock instock) throws ManagerException;
}