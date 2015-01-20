package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.SizeComposeDTO;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 收货验收单详情manager
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:14:56
 * @version 0.1.0
 * @copyright yougou.com
 */
public interface BillImCheckDtlManager extends BaseCrudManager {

	int selectCountMx(SizeComposeDTO dtoParam);

	List<SizeComposeDTO> queryBillImCheckDTOGroupBy(SimplePage page, SizeComposeDTO dtoParam);

	List<SizeComposeDTO> queryBillImCheckDTOBExpNo(SizeComposeDTO dtoParam);

	/**
	 * 查询所有sizecode
	 * 
	 * @return
	 */
	public List<SizeInfo> getSizeCodeByGroup();

	/**
	 * 查询明细信息
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams);

	/**
	 * 查询记录总数
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams);

	/**
	 * 分组计算入库订单编号和商品编号
	 * 
	 * @param model
	 * @param authorityParams TODO
	 * @return
	 */
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams);

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams);
	public SumUtilMap<String, Object> selectDtlSumQty(Map<String, Object> map, AuthorityParams authorityParams);

	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model, AuthorityParams authorityParams);
	
	public int selectCheckDtlDiffCount(BillImCheckDtl billImCheckDtl) throws ManagerException;
	
	/**
	 * 查询验收明细差异数量
	 * @param billImCheckDtl
	 * @return
	 * @throws DaoException
	 */
	public int selectCheckDtlDiffQty(BillImCheckDtl billImCheckDtl) throws ManagerException;
	
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
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 验收单汇总查询总数
	 * @param params
	 * @param authorityParams
	 * @return
	 */
	public int findBillImCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException;
}