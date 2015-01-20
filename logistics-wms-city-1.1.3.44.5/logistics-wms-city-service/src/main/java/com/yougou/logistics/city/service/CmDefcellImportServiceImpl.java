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
import com.yougou.logistics.city.common.model.TmpCmDefCellExcel;
import com.yougou.logistics.city.dal.database.TmpCmDefCellExcelMapper;

/**
 * 批量导入储位 
 * @author wanghb
 * @date 2014-10-15 上午9:42:36
 * @version 1.1.3.41
 * @copyright yougou.com
 */
@Service("cmDefcellImportService")
class CmDefcellImportServiceImpl extends BaseCrudServiceImpl implements CmDefcellImportService {

	@Resource
	private TmpCmDefCellExcelMapper tmpCmDefCellExcelMapper;

	@Override
	public BaseCrudMapper init() {
		return tmpCmDefCellExcelMapper;
	}
	public void batchInsertSelective(List<TmpCmDefCellExcel> list,String uuId)throws ServiceException{
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("list", list);
			map.put("uuId", uuId);
			tmpCmDefCellExcelMapper.batchInsertSelective(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public int deleteByUuid(String uuId)throws ServiceException{
		try {
			return tmpCmDefCellExcelMapper.deleteByUuid(uuId);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectCmDefwareBy(Map<String,Object> map)throws ServiceException{
		try {
			return tmpCmDefCellExcelMapper.selectCmDefwareBy(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectCmDefareaBy(Map<String,Object> map)throws ServiceException{
		try {
			return tmpCmDefCellExcelMapper.selectCmDefareaBy(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public void batshSaveCmDdefstockExcel(@Param("params")Map<String,Object> map)throws ServiceException{
		try {
			tmpCmDefCellExcelMapper.batshSaveCmDdefstockExcel(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public int batshSaveCmDefcellExcel(@Param("params")Map<String,Object> map)throws ServiceException{
		try {
			return tmpCmDefCellExcelMapper.batshSaveCmDefcellExcel(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<String> selectCellNoByUuId(Map<String,Object> map)throws ServiceException{
		try {
			return tmpCmDefCellExcelMapper.selectCellNoByUuId(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}