package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.BillOmDeliverKey;
import com.yougou.logistics.city.dal.mapper.BillOmDeliverMapper;

/**
 * 
 * 装车单service实现
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:27:20
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDeliverService")
class BillOmDeliverServiceImpl extends BaseCrudServiceImpl implements BillOmDeliverService {
    @Resource
    private BillOmDeliverMapper billOmDeliverMapper;
    
	private final static String RESULTY = "Y";

    @Override
    public BaseCrudMapper init() {
        return billOmDeliverMapper;
    }
    
	@Override
	public int deleteByDeliverDtl(BillOmDeliverKey key) throws ServiceException {
		try {
			return billOmDeliverMapper.deleteByDeliverDtl(key);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public void checkBillOmDeliver(Map<String, String> map)
			throws ServiceException {
		try {
			billOmDeliverMapper.checkBillOmDeliver(map);
			if (RESULTY.equals(map.get("strResult").split("\\|")[0])) {
				
			} else {
				throw new DaoException(map.get("strResult").split("\\|")[1]);
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillOmDeliver> selectLoadproposeDtl(
			BillOmDeliver billOmDeliver) throws ServiceException {
		try {
			return billOmDeliverMapper.selectLoadproposeDtl(billOmDeliver);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public int updateLoadproposeDtl(BillOmDeliver billOmDeliver) throws ServiceException {
		try {
			return billOmDeliverMapper.updateLoadproposeDtl(billOmDeliver);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public int updateLoadpropose(BillOmDeliver billOmDeliver) throws ServiceException {
		try {
			return billOmDeliverMapper.updateLoadpropose(billOmDeliver);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int loadproposeDtlCount(BillOmDeliver billOmDeliver)
			throws ServiceException {
		try {
			return billOmDeliverMapper.loadproposeDtlCount(billOmDeliver);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int updateBillOmDeliverDtl(BillOmDeliver billOmDeliver)
			throws ServiceException {
		try {
			return billOmDeliverMapper.updateBillOmDeliverDtl(billOmDeliver);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliver> findBillOmDeliverSum(Map<String,Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmDeliverMapper.selectBillOmDeliverSum(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDeliver> findPrintOmDeliverList(String sortColumn,
			String sortOrder, Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmDeliverMapper.selectPrintOmDeliverList(sortColumn,sortOrder,params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}