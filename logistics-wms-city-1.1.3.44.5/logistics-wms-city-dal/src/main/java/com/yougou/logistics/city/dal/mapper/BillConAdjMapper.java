package com.yougou.logistics.city.dal.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.vo.BillConAdjVo;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillConAdjMapper extends BaseCrudMapper {

	public void deleteStockAdjDetail(BillConAdjDtl model);
	/**
	 * 库存调整单合计
	 * @param params
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public Map<String, Object> selectSumQty(@Param("params")Map<String, Object> params,
			@Param("authorityParams")AuthorityParams authorityParams) throws DaoException;
	
	
	/**
	 * 查询一条BillConAdj
	 * @param list
	 * @throws DaoException
	 */
	public BillConAdj selectOneByAdjNo(BillConAdj billConAdj) throws DaoException;
	
	/**
	 * 查询城市仓库存调整单调入目的cellNo
	 * @param billConAdjVo
	 * @return
	 * @throws DaoException
	 */
	public BillConAdjVo selectBillConAdjVoBytype(BillConAdjVo billConAdjVo) throws DaoException;
}