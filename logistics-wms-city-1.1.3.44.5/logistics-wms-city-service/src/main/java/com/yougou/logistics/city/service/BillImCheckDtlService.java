package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.SizeComposeDTO;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BillImCheckDtlKey;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 收货验收单详情service
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:16:16
 * @version 0.1.0
 * @copyright yougou.com
 */
public interface BillImCheckDtlService extends BaseCrudService {
	public Integer selectMaxRowId(BillImCheckDtlKey billImCheckDtlKey) throws ServiceException;

	public int selectCountMx(SizeComposeDTO dtoParam);

	public List<SizeComposeDTO> queryBillImCheckDTOGroupBy(SimplePage page, SizeComposeDTO dtoParam);

	public List<SizeComposeDTO> queryBillImCheckDTOBExpNo(SizeComposeDTO dtoParam);

	public List<SizeInfo> getSizeCodeByGroup();

	/**
	 * 查询记录总数
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams);

	/**
	 * 通过商品编码和入库订单编号分组查询
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams);

	/**
	 * 查询入库订单报表明细信息
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams);

	/**
	 * 查询是否有重复的验收明细
	 * 
	 * @param billImCheckDtl
	 * @return
	 */
	public List<BillImCheckDtl> findCheckDtlGroupByCount(BillImCheckDtl billImCheckDtl);

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);
	
	public SumUtilMap<String, Object> selectDtlSumQty(Map<String, Object> map, AuthorityParams authorityParams);

	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model, AuthorityParams authorityParams);
	
	/**
	 * 根据收货单号查询组装验收单的插入信息
	 * @param map
	 * @param authorityParams
	 * @return
	 * @throws ServiceException
	 */
	public List<BillImCheckDtl> selectCheckDetailByReciptNo(Map<String, Object> map,
			 AuthorityParams authorityParams)throws ServiceException;
	
	/**
	 * 批量插入验收单明细
	 * @param lstCheckDtl
	 * @throws ServiceException
	 */
	public void  insertBathCheckDtl(List<BillImCheckDtl> lstCheckDtl)throws ServiceException;
	
	public int selectCheckDtlDiffCount(BillImCheckDtl billImCheckDtl) throws ServiceException;
	
	/**
	 * 查询验收明细差异数量
	 * @param billImCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectCheckDtlDiffQty(BillImCheckDtl billImCheckDtl) throws ServiceException;
	
	/**
	 * 验收单汇总分页查询
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public List<BillImCheckDtl> findBillImCheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException;

	/**
	 * 验收单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findBillImCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException;
	
	public List<BillImCheckDtl> selectGroupByBox(Map<String, Object> params) throws ServiceException;
	
	public int selectCheckDtlPanNum (BillImCheckDtl billImCheckDtl) throws ServiceException;
}