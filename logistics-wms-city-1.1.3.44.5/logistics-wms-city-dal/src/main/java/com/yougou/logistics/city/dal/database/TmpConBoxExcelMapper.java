/**
 * TODO: 增加描述
 * 
 * @author username
 * @date 2014-10-11 上午11:42:23
 * @version 0.1.0
 * @copyright yougou.com 
 */
package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;
/**
 * 导入箱明细初始化
 * @author wanghb
 * @date 2014-10-11 上午11:42:23
 * @version 1.1.3.41
 * @copyright yougou.com 
 */
public interface TmpConBoxExcelMapper extends BaseCrudMapper {
	
	void batshSaveConBoxExcel(@Param("params")Map<String,Object> params)throws DaoException;
	
	Integer batshSaveConBoxDtlExcel(@Param("params")Map<String,Object> params)throws DaoException;
	
	void batshSaveBmContainerExcel(@Param("params")Map<String,Object> params)throws DaoException;
	
	void deleteByUuid(String uuId)throws DaoException;
	
	List<String> selectConBoxBy(@Param("params")Map<String,Object> params)throws DaoException;
	
	List<String> selectConBoxByUuId(@Param("params")Map<String,Object> params)throws DaoException;
	
	void batchInsertSelective(List<TmpConBoxExcel> list)throws DaoException;
	
	void batchDeleteBy(@Param("params")Map<String,Object> map)throws DaoException;
	
	void batshSaveAccInventorySku(@Param("params")Map<String,Object> params)throws DaoException;
	
	void batshSaveAccInventory(@Param("params")Map<String,Object> params)throws DaoException;
	
	List<String> selectCellNoBy(@Param("params")Map<String,Object> params)throws DaoException;
	
	List<String> selectItemNoBy(@Param("params")Map<String,Object> params)throws DaoException;
	
	List<String> selectBmContainerByUuId(@Param("params")Map<String,Object> params)throws DaoException;
}
