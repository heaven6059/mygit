package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillChDifferent;
import com.yougou.logistics.city.common.model.BillChDifferentDtl;
import com.yougou.logistics.city.dal.database.BillChDifferentDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("billChDifferentDtlService")
class BillChDifferentDtlServiceImpl extends BaseCrudServiceImpl implements BillChDifferentDtlService {
    @Resource
    private BillChDifferentDtlMapper billChDifferentDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billChDifferentDtlMapper;
    }

	@Override
	public void batchInsertDtl(List<BillChDifferentDtl> list)
			throws ServiceException {
		billChDifferentDtlMapper.batchInsertDtl(list);
	}
}