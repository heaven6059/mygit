package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillChCheckDirectBoxDto;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.model.SystemUser;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 14:54:50 CST 2013
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
public interface BillChCheckDirectService extends BaseCrudService {
	/**
	 * 出盘发单查询定位表信息数量
	 * @param dto
	 * @return
	 */
	public int findDirectCount(BillChCheckDirectDto dto) throws ServiceException;

	/**
	 * 出盘发单查询定位表信息
	 * @param dto
	 * @param orderBy TODO
	 * @return
	 */
	public List<BillChCheckDirectDto> findDirectList(BillChCheckDirectDto dto, String orderBy, SimplePage page) throws ServiceException;

	/**
	 * 查询定位表中存在的商品的品牌
	 * @param dto
	 * @return
	 * @throws DaoException
	 */
	public List<Brand> findBrandInDirect(BillChCheckDirectDto dto) throws ServiceException;

	/**
	 * 根据商品获取定位信息
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirect> findDirectByItem(@Param("params") Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;
	
	/**
	 * 根据商品获取定位信息 容器-整箱
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirect> findDirectByItem4RQZX(@Param("params") Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;
	
	
	/**
	 * 根据商品获取定位信息 容器-零散
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirect> findDirectByItem4RQLS(@Param("params") Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;

	/**
	 * 当全盘时，检查盘点计划中的商品是否可以盘点
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public boolean beforeDirectCheck(@Param("params") Map<String, Object> params) throws ServiceException;
	/**
	 * 根据储位获取定位信息
	 * @param params
	 * @param authorityParams TODO
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirect> findDirectByCell(@Param("params") Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;
	
	/**
	 * 查询储位号查询箱子及其数量
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirectBoxDto> selectDirectBoxNoList(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 查询盘点定位和库存信息
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirectDto> findDirectAndContent(@Param("params") Map<String, Object> params)
			throws ServiceException;
	/**
	 * 查询定位的储位编码
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<String> findDirectCellNo(@Param("params") Map<String, Object> params) throws ServiceException;

	public List<BillChCheckDirect> selectPlanNo(Map<String, String> map, SimplePage page) throws ServiceException;

	public int selectPlanNoCount(Map<String, String> map) throws ServiceException;
	
	public Map<String,Object> selectAllCellCountAndStockCount(BillChCheckDirectDto dto)throws ServiceException;
	/**
	 * 批量插入定位信息
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl(List<BillChCheckDirect> list) throws ServiceException;
	/**
	 * 批量更新状态
	 * @param list
	 * @throws ServiceException
	 */
	public void batchUpdate4Status(List<BillChCheckDirect> list) throws ServiceException;
	
	/**
	 * 查询储位信息，根据计划单号，储位，属性，品质进行分组
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<BillChCheckDirect> selectDirectCellNoByGroup(Map<String, Object> params) throws ServiceException;
	
	public List<BillChCheckDirect> findDirectByCell4RQLS(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	public List<BillChCheckDirect> findDirectByCell4RQZX(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;
	
	/**
	 * 盘点计划单的容器解锁
	 * @param planNo
	 * @param locno
	 * @param opeUser
	 * @throws ServiceException
	 */
	public void callContrainStatus(String planNo,String locno,SystemUser user) throws ServiceException;
	
}