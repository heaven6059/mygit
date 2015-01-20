package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.BillSmWasteDtlSizeDto;
import com.yougou.logistics.city.common.model.BillSmWastePrintDto;
import com.yougou.logistics.city.common.utils.SumUtilMap;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
public interface BillSmWasteDtlMapper extends BaseCrudMapper {
	
	public int selectContentCount(@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	public List<BillSmWasteDtl> selectContent(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	public List<BillSmWasteDtl> selectContentParams(@Param("model")BillSmWasteDtl modelType,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillSmWasteDtl> selectContentDtl(@Param("model")BillSmWasteDtl modelType,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	public int updateContent(BillSmWasteDtl modelType)throws DaoException;
	
	public int selectMaxPid(BillSmWasteDtl modelType)throws DaoException;
	public int selectIsHave(BillSmWasteDtl modelType)throws DaoException;
	
	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,@Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	
	public List<BillSmWasteDtl> selectByWaste(@Param("model")BillSmWasteDtl modelType,@Param("params")Map<String,Object> params,@Param("authorityParams") AuthorityParams authorityParams);
	/**
	 * 批量插入明细
	 * @param list
	 * @throws DaoException
	 */
	public void batchInsertDtl(List<BillSmWasteDtl> list) throws DaoException;
	
	public List<BillSmWasteDtlSizeDto> selectDtl4SizeHorizontal(@Param("wasteNo") String wasteNo);
	
	/**
	 * 尺码横排
	 * @param modelType
	 * @param authorityParams
	 * @return
	 * @throws DaoException
	 */
	public List<BillSmWasteDtl> selectDtlSysNo(@Param("model")BillSmWasteDtl modelType, @Param("authorityParams") AuthorityParams authorityParams) throws DaoException;
	public int selectSysNoContentCount(@Param("model")BillSmWasteDtl params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillSmWasteDtl> selectSysNoContentByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("model")BillSmWasteDtl params,@Param("authorityParams") AuthorityParams authorityParams);
	public List<BillSmWasteDtl> selectSysNoByPage(@Param("model")BillSmWasteDtl cc,@Param("authorityParams") AuthorityParams authorityParams)throws DaoException;
	/**
	 * 查询打印尺码横排所需要的所有明细
	 * @param params
	 * @return
	 */
	public List<BillSmWastePrintDto> selectPrintDtl4Size(@Param("params") Map<String,Object> params);
	
	/**
	 * 查询箱容器明细
	 * @param params
	 * @return
	 */
	public List<BillSmWasteDtl> selectContentParams4Box(@Param("params") Map<String,Object> params);
	
	/**
	 * 插入批量插入箱明细
	 * @param params
	 * @return
	 */
	public Integer batchInsertWasteDtl4Box(@Param("params") Map<String,Object> params, @Param("list") List<BillSmWasteDtl> boxList);

	/**
	 * 批量更新箱状态-已出库
	 * @param params
	 * @return
	 */
	public Integer batchUpdateWsateBoxStatus4Container(@Param("params") Map<String,Object> params);
	/**
	 * 
	 * @param _map
	 * @return
	 * @throws DaoException
	 */
	public void updateOperateRecord(Map<String, Object> map)throws DaoException;
}