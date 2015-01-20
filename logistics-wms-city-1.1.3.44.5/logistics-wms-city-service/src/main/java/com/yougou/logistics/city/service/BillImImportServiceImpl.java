package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportKey;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillImImportMapper;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-25 10:24:56
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
@Service("billImImportService")
class BillImImportServiceImpl extends BaseCrudServiceImpl implements
	BillImImportService {

    @Log
    private Logger log;
    @Resource
    private BillImImportMapper billImImportMapper;

    @Override
    public BaseCrudMapper init() {
	return billImImportMapper;
    }

    @Override
    public int deleteImImport(BillImImportKey billImImportKey)
	    throws ServiceException {
	int a = 0;
	try {
	    a = billImImportMapper.deleteByPrimaryKey(billImImportKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
	return a;
    }
    
    @Override
    public int deleteByPrimarayKeyForModel(BillImImport billImImport)
	    throws ServiceException {
	int a = 0;
	try {
	    a = billImImportMapper.deleteByPrimarayKeyForModel(billImImport);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
	return a;
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int findImpoertNoCount(Map map,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billImImportMapper.selectImpoertNoCount(map,authorityParams);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<?> findImportNoByPage(SimplePage page, Map map,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billImImportMapper.selectImportNoByPage(page, map,authorityParams);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<Supplier> findSupplierNo(BillImImport imp,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billImImportMapper.selectSupplierNo(imp,authorityParams);
	} catch (DaoException e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public BillImImport selectByPrimaryKey(BillImImport key)
	    throws ServiceException {
	try {
	    return billImImportMapper.selectByPrimaryKey(key);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public boolean isCanOverFlocByReceipt(BillImImport billImImport)
	    throws ServiceException {
	try {
	    int count = billImImportMapper.selectVerFlocByReceipt(billImImport);

	    if (count > 0) {
		return false;
	    } else {
		return true;
	    }
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public boolean isCanOverFlocByDivide(BillImImport billImImport)
	    throws ServiceException {
	try {
	    int count = billImImportMapper.selectVerFlocByDivide(billImImport);

	    if (count > 0) {
		return false;
	    } else {
		return true;
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    throw new ServiceException(e);
	}
    }

    @Override
    public boolean isCanOverFlocByCheck(BillImImport billImImport)
	    throws ServiceException {
	try {
	    int count = billImImportMapper.selectVerFlocByCheck(billImImport);

	    if (count > 0) {
		return false;
	    } else {
		return true;
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    throw new ServiceException(e);
	}
    }
    
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billImImportMapper.selectSumQty(params, authorityParams);
	}
}