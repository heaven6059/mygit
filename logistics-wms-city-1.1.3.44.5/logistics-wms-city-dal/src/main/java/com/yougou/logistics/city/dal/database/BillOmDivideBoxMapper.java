package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillOmDivide;

/**
 * 
 * TODO: 分货任务单
 * 
 * @author su.yq
 * @date 2013-10-14 下午6:08:46
 * @version 0.1.0 
 * @copyright yougou.com
 */
public interface BillOmDivideBoxMapper extends BaseCrudMapper {

	/**
	 * 创建分货任务单
	 * @param divide
	 * @throws DaoException
	 */
	public BillOmDivide insertBillOmDivide(BillOmDivide divide) throws DaoException;

	/**
	 * 查询店铺分组规则 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public  List<Map<String, Object>> selectStoreRurle(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 获取分货单号（存储过程）
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public void getsheetNo(Map<String, String> map)throws DaoException;
	
	/**
	 * 新增分货单主表
	 * @param divide
	 * @return
	 * @throws DaoException
	 */
	public int insertSelective(BillOmDivide divide) throws DaoException;
	
	/**
	 *  查询发货通知单
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public  List<Map<String, Object>>selectBillOmExpBoxCodeByMap(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 查询发货单明细数据
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public  List<Map<String, Object>>selectBillOmExpDtl(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 查询收货单明细数据
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public  List<Map<String, Object>>selectBillImReceiptDtl(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * （整箱）查询箱号信息
	 * @param divide
	 * @param boxSort
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>> getBoxSort(@Param("params") BillOmDivide divide,@Param("boxSort") String boxSort) throws DaoException;
	
	/**
	 * 查询所有门店信息
	 * @param divide
	 * @param cargoSort
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>> getStoreInfo(@Param("params") BillOmDivide divide,@Param("cargoSort") String cargoSort) throws DaoException;
	
	/**
	 *  开始配货  配整箱给当前门店  SQL 比较慢
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public   List<Map<String, Object>>selectBoxMStoreByMap(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 2、 拆箱给当前门店  SQL 比较慢
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>>selectBoxMStoreBoxCodeByMap(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 批量 插入分播单明细数据
	 * @param list
	 * @throws DaoException
	 */
	public void insertBillOmDivideByMap(List<Map<String,Object>> list) throws DaoException;
	
	/**
	 *  3、分货单号、门店找流道编号
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer selectStoreNoSerialNo(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 *  5、单据跟踪中是否有部分分配的状态 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer selectBillStatusCount(@Param("params") Map<String, Object> map)throws DaoException;
	/**
	 * 6、 获取状态中单据最大序列 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer selectBillStatusMax(@Param("params") Map<String, Object> map)throws DaoException;
	/**
	 *  7、  写单据状态跟踪表  
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer insertBillStatusLogByMap(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 9、  出货单明细是否全部分货完成 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer updateOmExpStatusByMap(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 批量更新出货单明细
	 * @param list
	 * @throws DaoException
	 */
	public void updateOmExpDtlStatusByMap(List<Map<String,Object>> list) throws DaoException;
	
	/**
	 * 11、  更新箱码明细  
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer batchUpdateBoxDtlByList(List<Map<String, Object>> list)throws DaoException;
	
	/**
	 * 12、  批量更新箱码明细 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer batchUpdateConBoxByList(List<Map<String, Object>> list)throws DaoException;
	
	/**
	 * 13、批量 回写收货单明细数量 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer batchUpdateBillImReceiptDtlByList(List< Map<String, Object>> list)throws DaoException;
	
	/**
	 * 更新收货单主单状态
	 * @param divide
	 * @return
	 * @throws DaoException
	 */
	public int revertUpdtReceipt(@Param("locNo") String locNo,@Param("receiptNo") String receiptNo) throws DaoException;
	
	/**
	 *  按箱分货  店分组 按发货量  分组  begin
	 */
	
	/**
	 * 1、统计当前流道 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer selectStorGroupCount(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 * 2、获取当前最大流道
	 * @param map
	 * @return
	 * @throws DaoException
	
	public Integer selectStorGroupMaxCount(@Param("params") Map<String, Object> map)throws DaoException;
	*/
	
	/**
	 * 3、新增门店分组临时表
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public  int insertStroreGroup(@Param("params") Map<String, Object> map)throws DaoException;
	 
	/**
	 * 1、未做店分组 则删除
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int deleteStoreGroupByParam(@Param("params") Map<String, Object> map)throws DaoException;
	
	
	/**
	 * 2、查询分组名
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>>selectStorGroupByParam(@Param("params") Map<String, Object> map)throws DaoException;
	
	
	/**
	 * 根据收货单关联出货单按客户分组统计要货量  [ 预发货量降序或者升序 ] (  门店分组 )
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>>selectStoreSendNumByParam(@Param("params") Map<String, Object> map)throws DaoException;
	/**
	 * 根据收货单关联出货单按客户分组统计要货量  [ 门店编号升序 或者降序] (  门店分组 )
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>>selectStoreStoreNoByParam(@Param("params") Map<String, Object> map)throws DaoException;

	/**
	 * 批量插入店分组表
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsertStroreGroup  (List<Map<String,Object>> list) throws DaoException;
	/**
	 *  按箱分货  店分组 按发货量  分组  end
	 */
	
	
	public List<Map<String, Object>> selectBillImReceiptBySerialNo(@Param("serialNo") String serialNo) throws DaoException;
	
	/**
	 * 更新主表状态
	 * @param divide
	 * @return
	 * @throws DaoException
	 */
	public int updateBillOmDivide(@Param("businessType") String businessType,@Param("locNo") String locNo,@Param("divideNo") String divideNo) throws DaoException;
	
	
	/**
	 *  proc_RevertBoxGrp begin
	 */
	
	
	/**
	 *1、 查询箱码按组汇总 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>>selectBoxNoByMap(@Param("params") Map<String, Object> map)throws DaoException;
	

	/**
	 * 2 批量更新更新箱码组别信息
	 * @param list
	 * @throws DaoException
	 */
	public void batchUpdateBillOmDivideDtlForGroupNameByMap  (List<Map<String,Object>> list) throws DaoException;
	/**
	 *  3 批量更新箱码状态
	 * @param list
	 * @throws DaoException
	 */
	public void batchUpdateConBoxForStatusByMap (List<Map<String,Object>> list) throws DaoException;
	
	/**
	 * 10、统计出货量 
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public Integer selectBillOmExpCountByMap(@Param("params") Map<String, Object> map)throws DaoException;
	
	/**
	 *分货任务对于的所有收货单箱子   找到未匹配到的分货箱子  插入SYS_OM_NoDivideBox表   
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int insertNoDivideBox(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
	 * 查询出未匹配到的分货箱子
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String, Object>> selectNoDivideBox() throws DaoException;
	
	/**
	 * 修改预分货单明细状态
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int updateReceiptStatus(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
	 * 修改箱状态，恢复到可用状态
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	public int updateBoxStruts(@Param("params") Map<String, Object> map) throws DaoException;
	
	/**
	 * 查询分货完成的流道与门店的信息
	 * @param divideNo
	 * @return
	 * @throws DaoException
	 */
	public List<Map<String,Object>> selectDivideSerialNo(@Param("divideNo") String divideNo) throws DaoException;
	
	/**
	 *  批量更新分货门店流道信息
	 * @param list
	 * @throws DaoException
	 */
	public void batchUpdateDivideSerialNo(List<Map<String,Object>> list) throws DaoException;
	
}