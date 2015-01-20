package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;

/**
 * 导入箱明细初始化
 * @author wanghb
 * @date 2014-10-11 上午11:44:58
 * @version 1.1.3.41
 * @copyright yougou.com 
 */
public interface TmpConBoxExcelService extends BaseCrudService {
	
	void batshSaveConBoxExcel(Map<String,Object> params)throws ServiceException;
	
	Integer batshSaveConBoxDtlExcel(Map<String,Object> params)throws ServiceException;
	
	void batshSaveBmContainerExcel(Map<String,Object> params)throws ServiceException;
	
	void batshSaveAccInventorySku(Map<String,Object> params)throws ServiceException;
	
	void batshSaveAccInventory(Map<String,Object> params)throws ServiceException;
	
	void batchInsertSelective(List<TmpConBoxExcel> list)throws ServiceException;
	
	void batchDeleteBy(String locNo,List<String> conNoList)throws ServiceException;
	
	void deleteByUuid(String uuId)throws ServiceException;
    
	List<String> selectConBoxBy(@Param("params")Map<String,Object> params)throws ServiceException;
	
	List<String> selectConBoxByUuId(@Param("params")Map<String,Object> params)throws ServiceException;
	
	List<String> selectBmContainerByUuId(@Param("params")Map<String,Object> params)throws ServiceException;
	
	List<String> selectCellNoBy(Map<String,Object> params)throws ServiceException;
	
	List<String> selectItemNoBy(Map<String,Object> params)throws ServiceException;
}
