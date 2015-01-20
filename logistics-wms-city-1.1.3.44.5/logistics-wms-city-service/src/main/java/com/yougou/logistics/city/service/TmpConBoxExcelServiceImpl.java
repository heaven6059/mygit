package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.TmpConBoxExcel;
import com.yougou.logistics.city.dal.database.TmpConBoxExcelMapper;

/**
 * 导入箱明细初始化
 * 
 * @author wanghb
 * @date 2014-10-11 上午11:44:58
 * @version 1.1.3.41
 * @copyright yougou.com
 */
@Service("tmpConBoxExcelService")
public class TmpConBoxExcelServiceImpl extends BaseCrudServiceImpl implements TmpConBoxExcelService {

	@Resource
	private TmpConBoxExcelMapper tmpConBoxExcelMapper;

	@Override
	public BaseCrudMapper init() {
		return tmpConBoxExcelMapper;
	}
	public void batshSaveConBoxExcel(Map<String,Object> params)throws ServiceException{
		try {
			tmpConBoxExcelMapper.batshSaveConBoxExcel(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public Integer batshSaveConBoxDtlExcel(Map<String,Object> params)throws ServiceException{
		try {
			return tmpConBoxExcelMapper.batshSaveConBoxDtlExcel(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public void batshSaveBmContainerExcel(Map<String,Object> params)throws ServiceException{
		try {
			tmpConBoxExcelMapper.batshSaveBmContainerExcel(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public void batshSaveAccInventorySku(Map<String,Object> params)throws ServiceException{
		try {
			tmpConBoxExcelMapper.batshSaveAccInventorySku(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public void batshSaveAccInventory(Map<String,Object> params)throws ServiceException{
		try {
			tmpConBoxExcelMapper.batshSaveAccInventory(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public void deleteByUuid(String uuId)throws ServiceException{
		try {
			tmpConBoxExcelMapper.deleteByUuid(uuId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectConBoxBy(@Param("params")Map<String,Object> params)throws ServiceException{
		try {
			return tmpConBoxExcelMapper.selectConBoxBy(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectConBoxByUuId(@Param("params")Map<String,Object> params)throws ServiceException{
		try {
			return tmpConBoxExcelMapper.selectConBoxByUuId(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public void batchInsertSelective(List<TmpConBoxExcel> list)throws ServiceException{
		try {
			tmpConBoxExcelMapper.batchInsertSelective(list);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public void batchDeleteBy(String locNo,List<String> conNoList)throws ServiceException{
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("locNo", locNo);
			map.put("list", conNoList);
			tmpConBoxExcelMapper.batchDeleteBy(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectCellNoBy(Map<String,Object> params)throws ServiceException{
		try {
			return tmpConBoxExcelMapper.selectCellNoBy(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectItemNoBy(Map<String,Object> params)throws ServiceException{
		try {
			return tmpConBoxExcelMapper.selectItemNoBy(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectBmContainerByUuId(@Param("params")Map<String,Object> params)throws ServiceException{
		try {
			return tmpConBoxExcelMapper.selectBmContainerByUuId(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
