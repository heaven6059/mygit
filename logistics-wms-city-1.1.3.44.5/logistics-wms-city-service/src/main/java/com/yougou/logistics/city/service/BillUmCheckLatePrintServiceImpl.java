package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmCheckLatePrint;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmCheckLatePrintMapper;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-8-14 下午3:08:33
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billUmCheckLatePrintService")
public class BillUmCheckLatePrintServiceImpl extends BaseCrudServiceImpl implements BillUmCheckLatePrintService {

	@Resource
	private BillUmCheckLatePrintMapper billUmCheckLatePrintMapper;

	@Override
	public BaseCrudMapper init() {
		return billUmCheckLatePrintMapper;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckLatePrintMapper.selectSumQty(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillUmCheckLatePrint> findItemInfoByBarcode(Map<String, Object> params, List<String> list)
			throws ServiceException {
		try {
			return billUmCheckLatePrintMapper.selectItemInfoByBarcode(params, list);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
}
