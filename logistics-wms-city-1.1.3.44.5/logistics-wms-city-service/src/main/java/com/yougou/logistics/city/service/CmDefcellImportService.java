package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.TmpCmDefCellExcel;

/**
 * 批量导入储位 
 * @author wanghb
 * @date 2014-10-15 上午9:42:36
 * @version 1.1.3.41
 * @copyright yougou.com
 */
public interface CmDefcellImportService extends BaseCrudService {
	
	/**
	 * 批量保存到中间表
	 * @param list
	 * @throws ServiceException
	 */
	void batchInsertSelective(List<TmpCmDefCellExcel> list,String uuId)throws ServiceException;
	
	/**
	 * 删除本次导入的数据
	 * @param uuId
	 * @return
	 * @throws ServiceException
	 */
	int deleteByUuid(String uuId)throws ServiceException;
	/**
	 * 查询仓区编码是否存在
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	List<String> selectCmDefwareBy(Map<String,Object> map)throws ServiceException;
	/**
	 *  查询库区编码是否存在
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	List<String> selectCmDefareaBy(Map<String,Object> map)throws ServiceException;
	
	/**
	 * 增加通道表
	 * @param map
	 * @throws DaoException
	 */
	void batshSaveCmDdefstockExcel(Map<String,Object> map)throws ServiceException;
	/**
	 * 增加储位表
	 * @param map
	 * @throws ServiceException
	 */
	int batshSaveCmDefcellExcel(Map<String,Object> map)throws ServiceException;
	
	/**
	 *  查询储位是否存在
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	List<String> selectCellNoByUuId(Map<String,Object> map)throws ServiceException;
	
}