package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.dto.Bill4WmsDto;
import com.yougou.logistics.city.common.api.dto.ItemManageResultDto;
import com.yougou.logistics.city.dal.mapper.BillWms2LmpMapper;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-5-14 下午2:43:19
 * @version 0.1.0 
 * @copyright yougou.com 
 */

@Service("billWms2LmpService")
public class BillWms2LmpServiceImpl implements BillWms2LmpService {
	@Resource
	private BillWms2LmpMapper billWms2LmpMapper;

	@Override
	public List<Bill4WmsDto> getBill4Wms(String locno, String sysNo, List<String> billType, String beginDate,
			String endDate, SimplePage page) throws ServiceException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("sysNo", sysNo);
			params.put("beginDate", beginDate);
			params.put("endDate", endDate);
			return billWms2LmpMapper.getBill4Wms(params, billType, page);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int findItemManageContentCount(Map<String, Object> params)
			throws ServiceException {
		try {
			return billWms2LmpMapper.selectItemManageContentCount(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ItemManageResultDto> findItemManageContentByPage(
			Map<String, Object> params, SimplePage page)
			throws ServiceException {
		try {
			return billWms2LmpMapper.selectItemManageContentByPage(params, page);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int findItemManageContentNum(Map<String, Object> params)
			throws ServiceException {
		try {
			return billWms2LmpMapper.selectItemManageContentNum(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
