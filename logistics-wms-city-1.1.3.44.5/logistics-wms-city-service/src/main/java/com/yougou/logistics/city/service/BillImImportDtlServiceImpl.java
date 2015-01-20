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
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.dto.BillImImportDtlSizeKind;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.BillImImportDtlKey;
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillImImportDtlForPage;
import com.yougou.logistics.city.dal.mapper.BillImImportDtlMapper;

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
@Service("billImImportDtlService")
class BillImImportDtlServiceImpl extends BaseCrudServiceImpl implements
	BillImImportDtlService {
    @Resource
    private BillImImportDtlMapper billImImportDtlMapper;

    @Override
    public BaseCrudMapper init() {
	return billImImportDtlMapper;
    }

    @Override
    public int deleteImImportDtl(BillImImportDtlKey billImImportDtlKey)
	    throws ServiceException {
	int a = 0;
	try {
	    a = billImImportDtlMapper.deleteByPrimaryKey(billImImportDtlKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
	return a;
    }

    @Override
    public int modifyImImportDtl(BillImImportDtl billImImportDtl)
	    throws ServiceException {
	int a = 0;
	try {
	    a = billImImportDtlMapper.modifyImImportDtl(billImImportDtl);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
	return a;
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int selectBoxNoByDetailCount(Map<String, Object> params,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billImImportDtlMapper.selectBoxNoByDetailCount(params,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillImImportDtl> selectBoxNoByDetailPage(
	    BillImImportDtlForPage dtlPage,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billImImportDtlMapper.selectBoxNoByDetailPage(dtlPage,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public int selectMaxPid(BillImImportDtlKey billImImportDtlKey)
	    throws ServiceException {
	try {
	    return billImImportDtlMapper.selectMaxPid(billImImportDtlKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public List<BillImImportDtl> selectBoxNoListByDetail(
	    BillImImportDtlKey billImImportDtlKey) throws ServiceException {
	try {
	    return billImImportDtlMapper
		    .selectBoxNoListByDetail(billImImportDtlKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int findDetailCountByImportNo(Map map,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billImImportDtlMapper.selectDetailCountByImportNo(map,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillImImportDtlDto> findDetailByImportNo(SimplePage page,
	    Map map,AuthorityParams authorityParams) throws ServiceException {
	try {
	    return billImImportDtlMapper.selectDetailByImportNo(page, map,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public int selectBoxNoIsHave(BillImImportDtlKey billImImportDtlKey)
	    throws ServiceException {
	try {
	    return billImImportDtlMapper.selectBoxNoIsHave(billImImportDtlKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public int selectCountMx(BillImImportDtlSizeKind dto,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billImImportDtlMapper.selectCountMx(dto,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListByImportNo(
	    BillImImportDtlSizeKind dto) throws ServiceException {
	try {
	    return billImImportDtlMapper
		    .queryBillImImportDtlDTOlListByImportNo(dto);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListGroupBy(
	    SimplePage page, BillImImportDtlSizeKind dto,AuthorityParams authorityParams)
	    throws ServiceException {
	try {
	    return billImImportDtlMapper.queryBillImImportDtlDTOlListGroupBy(
		    page, dto,authorityParams);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public List<BillImImportDtl> selectNotCheckBoxNoByDetail(
	    BillImImportDtlKey billImImportDtlKey) throws ServiceException {
	try {
	    return billImImportDtlMapper
		    .selectNotCheckBoxNoByDetail(billImImportDtlKey);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public List<BillImImportDtl> selectRepeat(BillImImport dtl)
	    throws ServiceException {
	try {
	    return billImImportDtlMapper.selectRepeat(dtl);
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
    public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) {
	return billImImportDtlMapper.selectSumQty(map,authorityParams);
    }

	@Override
	public void updateAllReceiptQty(BillImImportDtl billImImportDtl ) {
		billImImportDtlMapper.updateAllReceiptQty(billImImportDtl);
		
	}
}