package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.BillSmWasteDtlSizeDto;
import com.yougou.logistics.city.common.model.BillSmWastePrintDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
public interface BillSmWasteDtlService extends BaseCrudService {
	/**
	 * 查询库存信息
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int selectContentCount(Map<String,Object> params,AuthorityParams authorityParam)throws ServiceException;
	public List<BillSmWasteDtl> selectContent(SimplePage page,String orderByField,String orderBy,Map<String,Object> params,AuthorityParams authorityParam)throws ServiceException;
	public List<BillSmWasteDtl> selectContentParams(BillSmWasteDtl modelType,Map<String,Object> params,AuthorityParams authorityParam)throws ServiceException;
	public List<BillSmWasteDtl> selectContentDtl(BillSmWasteDtl modelType,Map<String,Object> params,AuthorityParams authorityParam)throws ServiceException;
	public int updateContent(BillSmWasteDtl modelType)throws ServiceException;
	/**
	 * 查询最大序列号
	 * @param billUmReceiptDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectMaxPid(BillSmWasteDtl billSmWasteDtl) throws ServiceException;
	
	/**
	 * 查询是否有重复数据
	 * @param billSmWasteDtl
	 * @return
	 * @throws ServiceException
	 */
	public int selectIsHave(BillSmWasteDtl billSmWasteDtl) throws ServiceException;
	
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException;
	
	public List<BillSmWasteDtl> findByWaste(BillSmWasteDtl billSmWasteDtl,Map<String,Object> params, AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 批量插入明细
	 * @param list
	 * @throws ServiceException
	 */
	public void batchInsertDtl(List<BillSmWasteDtl> list) throws ServiceException;
	
	public List<BillSmWasteDtlSizeDto> findDtl4SizeHorizontal(String wasteNo);
	
	public List<BillSmWasteDtl> findDtlSysNo(BillSmWasteDtl billSmWasteDtl, AuthorityParams authorityParams) throws ServiceException;
	
	public Map<String,Object> findDtlSysNoByPage(BillSmWasteDtl billSmWasteDtl, AuthorityParams authorityParams)throws ServiceException;
	/**
	 * 查询打印尺码横排所需要的所有明细
	 * @param params
	 * @return
	 */
	public List<BillSmWastePrintDto> findPrintDtl4Size(Map<String,Object> params);
	
	/**
	 * 查询箱容器明细
	 * @param params
	 * @return
	 */
	public List<BillSmWasteDtl> selectContentParams4Box(Map<String,Object> params) throws ServiceException;

	/**
	 * 插入批量插入箱明细
	 * @param params
	 * @param boxList TODO
	 * @return
	 */
	public Integer batchInsertWasteDtl4Box(Map<String,Object> params, List<BillSmWasteDtl> boxList) throws ServiceException;

	/**
	 * 批量更新箱状态-已出库
	 * @param params
	 * @return
	 */
	public Integer batchUpdateWsateBoxStatus4Container(Map<String,Object> params) throws ServiceException;
	/**
	 * 主表审核时更细明细表修改记录
	 * @param _map
	 * @throws ServiceException
	 */
	public void updateOperateRecord(Map<String, Object> map) throws ServiceException;
}