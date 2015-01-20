package com.yougou.logistics.city.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.api.dto.BillLmp2WmsDto;
import com.yougou.logistics.city.dal.database.BillLmp2WmsMapper;

@Service("billLmp2WmsService")
public class BillLmp2WmsServiceImpl extends BaseCrudServiceImpl implements BillLmp2WmsService {

	@Resource
	private BillLmp2WmsMapper billLmp2WmsMapper;
	
	@Override
	public BaseCrudMapper init() {
		return billLmp2WmsMapper;
	}
	@Override
	public List<BillLmp2WmsDto> findBill4Wms(List<String> billTypes, String startDate,
			String endDate, String sysNo, String locno, SimplePage page) throws ServiceException {
		return billLmp2WmsMapper.selectBill4Wms(billTypes, startDate, endDate, sysNo, locno, page);
	}

}
