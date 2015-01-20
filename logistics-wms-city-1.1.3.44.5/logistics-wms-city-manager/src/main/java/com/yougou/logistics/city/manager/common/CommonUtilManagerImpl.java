package com.yougou.logistics.city.manager.common;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.vo.Column;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.service.common.CommonUtilService;

/**
 * 系统公用处理
 * @author wei.hj
 * @date 2013-07-19
 * @version 0.1.0
 * @copyright yougou.com
 *
 */

@Repository("commonUtilManager")
public class CommonUtilManagerImpl implements CommonUtilManager {
	
	@Log
	private Logger log;
	
	@Resource
	private CommonUtilService commonUtilService;
	/**
	 * 查询Schema   注意：在使用过程中，参数名称必须使用大写的。
	 * @param catalog  目录名称，一般都为空
	 * @param schema   数据库用户名
	 * @param tableName  所查表名
	 * @param type  类型  表的类型(TABLE | VIEW)
	 * @throws DaoException
	 */
	@Override
	public List<Column> selectDataBaseMetaData(String catalog, String schema,
			String tableName, String[] type) throws ManagerException {
	    try{
	    	return this.commonUtilService.selectDataBaseMetaData(catalog, schema, tableName, type);
	    }catch(Exception e){
	    	throw new ManagerException();
	    }

	}
	
	/**
	 * 根据字典分类编号查询字典明细
	 * @param lookupDtl
	 * @return
	 */
	public List<LookupDtl> queryLookupDtlsList(LookupDtl lookupDtl) throws ManagerException{
		try {
			return this.commonUtilService.queryLookupDtlsList(lookupDtl);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}
	}
	
	/**
	 * 根据模块ID查询中文显示字段
	 * @param vo
	 * @return
	 * @throws DaoException
	 */
	public List<Column> queryCommonQueryConfig(Column vo) throws ManagerException{
		try {
			return this.commonUtilService.queryCommonQueryConfig(vo);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

}
