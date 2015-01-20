/*
 * 类名 com.yougou.logistics.city.dal.mapper.BillUmLoadboxDtlMapper
 * @author luo.hl
 * @date  Thu Jan 16 16:20:50 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.BillUmBoxDtl;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmLoadbox;
import com.yougou.logistics.city.common.model.BillUmLoadboxDtl;
import com.yougou.logistics.city.common.model.BillUmUntreadMm;
import com.yougou.logistics.city.common.utils.SumUtilMap;

public interface BillUmLoadboxDtlMapper extends BaseCrudMapper {
	public List<BillUmLoadboxDtl> selectLoadBoxTask(@Param("params") BillUmUntreadMm untreadMm,
			@Param("list") List<BillUmCheck> list) throws DaoException;

	public int selectMaxRowId(@Param("params") BillUmLoadbox box);

	public void updateBoxingQty(@Param("params") BillUmLoadboxDtl box);

	public BillUmLoadboxDtl selectByItemNoAndSizeNo(@Param("params") BillUmBoxDtl boxDtl);

	public int checkHaveBoxing(@Param("params") BillUmLoadbox box);

	public SumUtilMap<String, Object> selectSumQty(@Param("params") Map<String, Object> map,
			@Param("authorityParams") AuthorityParams authorityParams);

	public List<BillUmLoadboxDtl> judgeLoadboxDtlBoxNo(@Param("locno")String locno,
			@Param("loadboxNo")String loadboxNo);
}