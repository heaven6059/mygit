package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillChRequestDtl;
import com.yougou.logistics.city.dal.database.BillChRequestDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billChRequestDtlService")
class BillChRequestDtlServiceImpl extends BaseCrudServiceImpl implements BillChRequestDtlService {
    @Resource
    private BillChRequestDtlMapper billChRequestDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billChRequestDtlMapper;
    }

	@Override
	public int findCountForJoinItem(Map<String, Object> params) throws ServiceException {
		try {
			return billChRequestDtlMapper.selectCountForJoinItem(params,null);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public List<BillChRequestDtl> findForJoinItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params) throws ServiceException {
		try {
			return billChRequestDtlMapper.selectForJoinItemByPage(page, orderByField, orderBy, params, null);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
}