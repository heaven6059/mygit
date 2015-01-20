package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.vo.ResultVo;
import com.yougou.logistics.base.manager.BaseCrudManager;
import com.yougou.logistics.city.common.model.BillOmDivide;

/**
 * 
 * TODO: 分货任务单manager接口
 * 
 * @author su.yq
 * @date 2013-10-14 下午6:18:10
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDivideManager extends BaseCrudManager {

	/**
	 * 创建分货任务单
	 * @param divide
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> addBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ManagerException;
	
	/**
	 * 创建分货任务单(新)
	 * @param divide
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> addBillOmDivideNew(List<BillOmDivide> listBillOmDivide) throws ManagerException;

	/**
	 * 删除分货任务单
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public void deleteBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ManagerException;

	/**
	 * 完结分货任务单
	 * @param keyStr
	 * @return
	 * @throws ManagerException
	 */
	public ResultVo modifyCompleteBillOmDivide(BillOmDivide divide) throws ManagerException;

	/**
	 * 手工关闭分货单
	 * @param billOmDivide
	 * @throws ServiceException
	 */
	public void procOmDivideOver(BillOmDivide billOmDivide) throws ManagerException;

	/**
	 * 获取批量打印信息
	 * @param listOmDivides
	 * @return
	 * @throws ManagerException
	 */
	public List<Map<String, Object>> getBatchPrintInfo(List<BillOmDivide> listOmDivides) throws ManagerException;
}