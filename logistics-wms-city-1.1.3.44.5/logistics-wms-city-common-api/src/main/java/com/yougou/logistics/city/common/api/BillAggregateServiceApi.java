package com.yougou.logistics.city.common.api;

import java.util.Date;
import java.util.List;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.dto.BillUnusualDto;

/**
 * TODO: 业务单据汇总查询接口
 * 
 * @author ye.kl
 * @date 2014-1-23 上午11:30:37
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public interface BillAggregateServiceApi {
	
	/**
	 * 根据仓别编码、单据时间范围(大于或等于开始日期、小于或等于结束日期),
	 * 查询存在异常动作业务环节的商品数量
	 * 
	 * 接口使用者 LMP
	 * 
	 * @param zoneNo 区域编码
	 * @param locNo 仓别编码
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param page 分页
	 * @return List<BillUnusualDto>
	 * @throws RpcException
	 */
	public List<BillUnusualDto> getBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate,SimplePage page) throws RpcException;
	
	
	/**
	 * 根据仓别编码、单据时间范围(大于或等于开始日期、小于或等于结束日期),
	 * 查询存在异常动作业务环节的记录数
	 * 
	 * 接口使用者 LMP
	 * 
	 * @param zoneNo 区域编码
	 * @param locNo 仓别编码
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 * @throws RpcException
	 */
	public int getBillUnusualCount(String zoneNo, String locNo, Date beginDate, Date endDate) throws RpcException;
	
	/**modified by liu.t 不再采用分页
	 * 根据仓别编码、单据时间范围(大于或等于开始日期、小于或等于结束日期),
	 * 查询存在异常动作业务环节的商品数量
	 * 
	 * 接口使用者 LMP
	 * 
	 * @param zoneNo 区域编码
	 * @param locNo 仓别编码
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return List<BillUnusualDto>
	 * @throws RpcException
	 */
	public List<BillUnusualDto> getBillUnusual(String zoneNo, String locNo, Date beginDate, Date endDate) throws RpcException;
}
