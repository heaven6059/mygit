package com.yougou.logistics.city.dal.mapper;

import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 
 *调用存储过程同用mapper
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:13:53
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface ProcCommonMapper extends BaseCrudMapper {
	/**
	 * 调用存储过程获取单号
	 * @param map
	 * @throws DaoException
	 */
	public void procGetSheetNo(Map<String, String> map) throws DaoException;

	/**
	 * 调用存储过程获取箱号
	 * @param map
	 * @throws DaoException
	 */
	public void procGetContainerNoBase(Map<String, String> map) throws DaoException;

	/**
	 * 上架定位-收货
	 * @param map
	 * @throws DaoException
	 */
	public void procImInstockDirectByReceipt(Map<String, String> map) throws DaoException;

	/**
	 * 上架定位-验收
	 * @param map
	 * @throws DaoException
	 */
	public void procImInstockDirectByCheck(Map<String, String> map) throws DaoException;

	/**
	 * 取消定位-支持收货和验收
	 * @param map
	 * @throws DaoException
	 */
	public void cancelDirectForAll(Map<String, String> map) throws DaoException;

	/**
	 * 获取储位ID
	 * @param map
	 * @throws DaoException
	 */
	public void procGetCellId(Map<String, Object> map) throws DaoException;

	/**
	 * 收货验收单  审核
	 * @param map
	 * @throws DaoException
	 */
	public void procImCheckAudit(Map<String, String> map) throws DaoException;

	/**
	 * 审核
	 * @param map
	 * @throws DaoException
	 */
	public void procImInstockAudit(Map<String, String> map) throws DaoException;

	/**
	 * 库存统一记账过程
	 * @param map
	 * @throws DaoException
	 */
	public void procAccApply(Map<String, Object> map) throws DaoException;

	/**
	 *  退仓-上架定位
	 * @param map
	 * @throws DaoException
	 */
	public void procUmDirect(Map<String, String> map) throws DaoException;

	/**
	 * 退仓-取消定位-按明细
	 * @param map
	 * @throws DaoException
	 */
	public void cancelDirectForUm(Map<String, String> map) throws DaoException;

	/**
	 * 退仓验收
	 * @param map
	 * @throws DaoException
	 */
	public void auditUmCheck(Map<String, String> map) throws DaoException;
	
	/**
	 * 生成退仓上架任务
	 * @param map
	 * @throws DaoException
	 */
	public void createTask(Map<String, String> map) throws DaoException;
	
	/**
	 * 退仓上架任务-发单
	 * @param map
	 * @throws DaoException
	 */
	public void sendOrder(Map<String, String> map) throws DaoException;
	
	/**
	 * 入库上架任务-发单
	 * @param map
	 * @throws DaoException
	 */
	public void sendOrderForIm(Map<String, String> map) throws DaoException;
	
	/**
	 * 退仓-匹配差异-继续定位
	 * @param map
	 * @throws DaoException
	 */
	public void continueDirect(Map<String, String> map) throws DaoException;
	
	/**
	 * 预到货通知单-分批上传
	 * @param map
	 * @throws DaoException
	 */
	public void bathUpload(Map<String, String> map) throws DaoException;
	/**
	 * 更新库存盘点锁定状态、库存冻结标识、手工移库标识
	 * @param map
	 * @throws DaoException
	 */
	public void updateContentStatus(Map<String, String> map) throws DaoException;
	
	/**
	 * 退仓验收任务审核
	 * @param map
	 * @throws DaoException
	 */
	public void auditUmCheckTask(Map<String, String> map) throws DaoException;
	
	/**
	 * 获取储位编码
	 * @param map
	 * @throws DaoException
	 */
	public void getSpecailCellNo(Map<String,String> map)  throws DaoException;
	
	/**
	 * 容器审核
	 * @param map
	 * @throws DaoException
	 */
	public void procContaskAudit(Map<String, String> map) throws DaoException;
}