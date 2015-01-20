package com.yougou.logistics.city.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillConQualityChangeDtlDto;
import com.yougou.logistics.city.common.dto.BillConQualityChangeDtlDto2;
import com.yougou.logistics.city.dal.mapper.BillConQualityChangeDtlMapper;

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
@Service("billConQualityChangeDtlService")
class BillConQualityChangeDtlServiceImpl extends BaseCrudServiceImpl implements BillConQualityChangeDtlService {
	@Resource
	private BillConQualityChangeDtlMapper billConQualityChangeDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billConQualityChangeDtlMapper;
	}

	@Override
	public int findDetailCount(BillConQualityChangeDtlDto dto) throws ServiceException {
		try {
			return billConQualityChangeDtlMapper.selectDetailCount(dto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillConQualityChangeDtlDto> findDetail(SimplePage page, BillConQualityChangeDtlDto dto)
			throws ServiceException {
		try {
			return billConQualityChangeDtlMapper.selectDetail(page, dto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillConQualityChangeDtlDto2> findItemDetail(BillConQualityChangeDtlDto dto) throws ServiceException {
		try {
			return billConQualityChangeDtlMapper.selectItemDetail(dto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}