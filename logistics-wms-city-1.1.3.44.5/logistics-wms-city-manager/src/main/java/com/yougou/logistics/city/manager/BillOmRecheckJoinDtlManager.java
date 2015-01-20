package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto2;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 
 * 分货交接单明细manager
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:20:37
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmRecheckJoinDtlManager extends BaseCrudManager {

	/**
	 * 查询单号数量
	 * @param map
	 * @return
	 * @throws ManagerException
	 */
	public int findRecheckNoCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询单号
	 * @param page TODO
	 * @param map
	 * @return
	 * @throws ManagerException
	 */
	public List<?> findRecheckNo(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询未交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findNoReCheckCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询未交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<?> findNoReCheck(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询已交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int findReCheckedCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 查询已交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillOmRecheckJoinDto> findReChecked(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ManagerException;

	/**
	 * 尺码横排
	 * @param dtl
	 * @return
	 * @throws ManagerException
	 */
	public List<BillOmRecheckJoinDto2> findItemDetail(@Param("dtl") BillOmRecheckJoinDto dtl) throws ManagerException;

	/**
	 * 交接单确认
	 * @param rowIdstr
	 * @param locno TODO
	 * @param recheckList TODO
	 * @throws ManagerException
	 */
	public Map<String, Object> sendReCheck(String rowIdstr, String locno,String user, List<BillOmRecheck> recheckList) throws ManagerException;
	
	/**
	 * 交接确认
	 * @param rowIdstr
	 * @param locno
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	public void queryReCheck(String rowIdstr, String locno,String user) throws ManagerException;
	
	public SumUtilMap<String, Object> selectNoReCheckSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
	
	public SumUtilMap<String, Object> selectNoReCheckedSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException;
}