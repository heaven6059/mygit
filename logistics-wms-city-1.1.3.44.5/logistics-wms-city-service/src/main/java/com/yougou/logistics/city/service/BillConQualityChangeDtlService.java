package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillConQualityChangeDtlDto;
import com.yougou.logistics.city.common.dto.BillConQualityChangeDtlDto2;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 24 13:54:46 CST 2013
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
public interface BillConQualityChangeDtlService extends BaseCrudService {
	/**
	 * 查询详情数量
	 * @param dto
	 * @return
	 * @throws DaoException
	 */
	public int findDetailCount(BillConQualityChangeDtlDto dto) throws ServiceException;

	/**
	 * 查询详情
	 * @param dto
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	public List<BillConQualityChangeDtlDto> findDetail(SimplePage page, BillConQualityChangeDtlDto dto)
			throws ServiceException;

	/**
	 * 尺码横排查询商品详情
	 * @param dto
	 * @return
	 * @throws DaoException
	 */
	public List<BillConQualityChangeDtlDto2> findItemDetail(BillConQualityChangeDtlDto dto) throws ServiceException;
}