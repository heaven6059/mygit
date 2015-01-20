package com.yougou.logistics.city.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
/**
 * 导入储位
 * @author wanghb
 * @date 2014-10-11 上午11:42:23
 * @version 1.1.3.41
 * @copyright yougou.com 
 */
public interface TmpCmDefCellExcelMapper extends BaseCrudMapper {
	

	void batchInsertSelective(@Param("params")Map<String,Object> map)throws DaoException;
	
	int deleteByUuid(String uuId)throws DaoException;
	/**
	 * 查询仓区
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	List<String> selectCmDefwareBy(@Param("params")Map<String,Object> map)throws DaoException;
	/**
	 * 查询库区
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	List<String> selectCmDefareaBy(@Param("params")Map<String,Object> map)throws DaoException;
	
	/**
	 * 增加通道表
	 * @param map
	 * @throws DaoException
	 */
	void batshSaveCmDdefstockExcel(@Param("params")Map<String,Object> map)throws DaoException;
	/**
	 * 增加储位表
	 * @param map
	 * @throws DaoException
	 */
	int batshSaveCmDefcellExcel(@Param("params")Map<String,Object> map)throws DaoException;
	/**
	 * 查询储位是否存在
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	List<String> selectCellNoByUuId(@Param("params")Map<String,Object> map)throws DaoException;
}
