package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillOmExpDtlDTO;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.model.BillOmExpDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmExpDtlForPage;
import com.yougou.logistics.city.dal.mapper.BillOmExpDtlMapper;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-29 16:50:42
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
@Service("billOmExpDtlService")
class BillOmExpDtlServiceImpl extends BaseCrudServiceImpl implements
	BillOmExpDtlService {
    @Resource
    private BillOmExpDtlMapper billOmExpDtlMapper;

    @Override
    public BaseCrudMapper init() {
	return billOmExpDtlMapper;
    }

    @Override
    public int deleteByPrimaryKey(BillOmExpDtlKey billOmExpDtlKey)
	    throws ServiceException {
	try {
	    return billOmExpDtlMapper.deleteByPrimaryKey(billOmExpDtlKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}

    }

    @Override
    public int selectItemNoByDetailCount(Map<String, Object> params)
	    throws ServiceException {
	try {
	    return billOmExpDtlMapper.selectItemNoByDetailCount(params);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public List<BillOmExpDtl> selectItemNoByDetailPage(
	    BillOmExpDtlForPage dtlPage) throws ServiceException {
	try {
	    return billOmExpDtlMapper.selectItemNoByDetailPage(dtlPage);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int selectCountMx(BillOmExpDtlDTO dto,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billOmExpDtlMapper.selectCountMx(dto,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOBExpNo(BillOmExpDtlDTO dto,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billOmExpDtlMapper.queryBillOmExpDtlDTOBExpNo(dto, authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOGroupBy(SimplePage page,
	    BillOmExpDtlDTO dto,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billOmExpDtlMapper.queryBillOmExpDtlDTOGroupBy(page, dto, authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int findBillOmExpDtlDispatchCount(
	    BillOmExpDispatchDtlDTO billOmExpDtl,
	    AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billOmExpDtlMapper.selectBillOmExpDtlDispatchCount(
		    billOmExpDtl, authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillOmExpDispatchDtlDTO> findBillOmExpDtlDispatchByPage(
	    SimplePage page, String orderByField, String orderBy,
	    BillOmExpDispatchDtlDTO billOmExpDtl,
	    AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billOmExpDtlMapper.selectBillOmExpDtlDispatchByPage(page,
		    orderByField, orderBy, billOmExpDtl, authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    public List<BillOmExpDtl> selectStore(BillOmExpDtl billOmExpDtl)
	    throws ServiceException {
	try {
	    return billOmExpDtlMapper.selectStore(billOmExpDtl);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try {
		    return billOmExpDtlMapper.selectSumQty(map,authorityParams);
		} catch (Exception e) {
		    throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try {
		    return billOmExpDtlMapper.selectDispatchSumQty(map,authorityParams);
		} catch (Exception e) {
		    throw new ServiceException(e);
		}
	}
	
	
}