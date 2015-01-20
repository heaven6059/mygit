package com.yougou.logistics.city.service;

import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;

public interface ProcCommonService extends BaseCrudService {
	/**
	 * 通过 仓别编码，前缀创建单号
	 * @param locno 仓别编码
	 * @param strpapertype 前缀    调用常量 CNumPre.IM_INSTOCK_PRE
	 * @return
	 * @throws DaoException
	 */
	public String procGetSheetNo(String locno, String strpapertype) throws ServiceException;

	/**
	 * 获取箱号
	 * @param locno
	 * @param strpapertype
	 * @param userId
	 * @param getType
	 * @param getNum
	 * @param useType
	 * @param containerMetrial
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, String> procGetContainerNoBase(String locno, String strpapertype, String userId, String getType,
			String getNum, String useType, String containerMetrial) throws ServiceException;

	/**
	 * 收货定位
	 * @param strLocno
	 * @param strReceiptNo
	 * @param strWorkerNo
	 * @return
	 * @throws ServiceException
	 */
	public boolean procImInstockDirectByReceipt(String strLocno, String strOwnerNo, String strReceiptNo,
			String strWorkerNo) throws ServiceException;

	/**
	 * 验收定位
	 * @param strLocno
	 * @param strCheckNo
	 * @param strWorkerNo
	 * @return
	 * @throws ServiceException
	 */
	public boolean procImInstockDirectByCheck(String strLocno, String strOwnerNo, String strCheckNo, String strWorkerNo)
			throws ServiceException;

	/**
	 * 取消定位-支持收货验收
	 * @param locNo
	 * @param ownerNo
	 * @param sourceNo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public boolean cancelDirectForAll(String locNo, String ownerNo, String sourceNo, String flag)
			throws ServiceException;

	/**
	 * 获取储位ID
	 * @return
	 * @throws ServiceException
	 */
	public Long procGetCellId() throws ServiceException;

	/**
	 * 库存统一记账过程
	 */
	public boolean procAccApply(String iPaperNo, String iLocType, String iPaperType, String iIoFLAG,
			Integer iPrepareDataExt) throws ServiceException;

	/**
	 * 退仓上架预约
	 * @param strLocno
	 * @param strReceiptNo
	 * @param strWorkerNo
	 * @return
	 * @throws ServiceException
	 */
	public boolean procUmDirect(String strLocno, String strOwnerNo, String strUmNo, String strWorkerNo)
			throws ServiceException;

	/**
	 * 退仓预约-取消定位
	 * @param locNo
	 * @param ownerNo
	 * @param sourceNo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public boolean cancelDirectForUm(String locNo, String strOwnerNo, String sourceNo, String keyStr, String strWorkerNo)
			throws ServiceException;
	
	
	/**
	 * 生成退仓上架任务
	 * @param locNo
	 * @param ownerNo
	 * @param sourceNo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public boolean createTask(String locNo, String strOwnerNo, String sourceNo, String keyStr, String strWorkerNo)
			throws ServiceException;
	
	/**
	 * 退仓上架任务-发单
	 * @param locNo
	 * @param ownerNo
	 * @param sourceNo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public boolean sendOrder(String locNo, String strOwnerNo, String sennder, String keyStr, String strWorkerNo)
			throws ServiceException;
	
	/**
	 * 入库上架任务-发单
	 * @param locNo
	 * @param ownerNo
	 * @param sourceNo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public boolean sendOrderForIm(String locNo, String strOwnerNo, String sennder, String keyStr, String strWorkerNo)
			throws ServiceException;

	/**
	 * 匹配差异的-继续定位
	 * @param locNo
	 * @param ownerNo
	 * @param sourceNo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public boolean continueDirect(String locno, String ownerNo,String untreadMmNo, String strCheckNoList,String strWorkerNo) throws ServiceException;
	
	public void auditUmCheck(String locno, String ownerNo, String checkNo, String operUser) throws ServiceException;
	
	/**
	 * 预到货通知单-分批上传
	 * @param locno
	 * @param importNo
	 * @return
	 * @throws ServiceException
	 */
	public boolean bathUpload(String locno, String importNo) throws ServiceException;
	
	/**
	 * 更新库存盘点锁定状态、库存冻结标识、手工移库标识
	 * @param locno
	 * @param cellId
	 * @param cellNo
	 * @param status
	 * @param flag
	 * @param hmManualFlag
	 * @param editor
	 * @return
	 * @throws ServiceException
	 */
	public boolean UpdateContentStatus(String locno,String cellId,String cellNo,String status,String flag,String hmManualFlag,String editor) throws ServiceException;
	
	/**
	 * 退仓验收任务审核
	 * @param locno
	 * @param checkTaskNo
	 * @param operUser
	 * @param openUserName
	 * @throws ServiceException
	 */
	public void auditUmCheckTask(String locno, String checkTaskNo, String operUser,String openUserName) throws ServiceException;
	
	/**
	 * 获取储位编码
	 * @param strLocno
	 * @param strArea_usetype
	 * @param strArea_quality
	 * @param strArea_attribute
	 * @param strAttribute_type
	 * @param strItem_type
	 * @throws ServiceException
	 */
	public String getSpecailCellNo(String strLocno, String strArea_usetype, String strArea_quality,String strArea_attribute,String strAttribute_type,
			  String strItem_type) throws ServiceException;
	
	/**
	 * 审核容器
	 * @param locno
	 * @param userType
	 * @param conTaskNo
	 * @param creator
	 * @throws ServiceException
	 */
	public void procContaskAudit(String locno,String userType,String conTaskNo,String creator) throws ServiceException;
}