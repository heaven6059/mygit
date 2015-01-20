package com.yougou.logistics.city.dal.database;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.api.dto.BillLmp2WmsDto;

/**
 * 
 * @author jiang.ys
 * 
 */
public interface BillLmp2WmsMapper extends BaseCrudMapper {

	/**
	 * 获取业务单据报表(分销-物流)所需的WMS数据
	 * 
	 * @param billTypes
	 * @param date
	 * @param brandNo
	 * @param locno
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<BillLmp2WmsDto> selectBill4Wms(
			@Param("billTypes") List<String> billTypes,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("sysNo") String sysNo, @Param("locno") String locno,
			@Param("page") SimplePage page);
}
