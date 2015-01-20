package com.yougou.logistics.city.service;

import java.util.HashMap;
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
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtlKey;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillWmDeliverDtlMapper;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
@Service("billWmDeliverDtlService")
class BillWmDeliverDtlServiceImpl extends BaseCrudServiceImpl implements BillWmDeliverDtlService {
    @Resource
    private BillWmDeliverDtlMapper billWmDeliverDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return billWmDeliverDtlMapper;
    }

	@Override
	public List<BillWmDeliverDtl> selectBoxNoByDetail(
			BillWmDeliverDtlKey billWmDeliverDtlKey) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectBoxNoByDetail(billWmDeliverDtlKey);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectMaxPid(BillWmDeliverDtlKey billWmDeliverDtlKey)
			throws ServiceException {
		try{
			return billWmDeliverDtlMapper.selectMaxPoId(billWmDeliverDtlKey);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	public int countWmDeliverDtlByMainId(BillWmDeliverDtlKey vo)
			throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.countWmDeliverDtlByMainId(vo);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillWmDeliverDtl> findWmDeliverDtlByMainIdPage(SimplePage page,
			BillWmDeliverDtlKey vo) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.findWmDeliverDtlByMainIdPage(page, vo);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectCountLabelNo(String deliverNo,String labelNo)throws ServiceException {
		try{
			Map<String,Object> params  = new  HashMap<String,Object>(0);
			params.put("deliverNo", deliverNo);
			params.put("boxNo", labelNo);
			return  billWmDeliverDtlMapper.selectCount(params,null);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectDeliverDtl(BillWmDeliverDtl billWmDeliverDtl) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectDeliverDtl(billWmDeliverDtl);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillWmDeliverDtl> selectDeliverDtlByLabelNo(Map<String, Object> maps) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectDeliverDtlByLabelNo(maps);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBillWmDeliverDtlGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectBillWmDeliverDtlGroupByCount(params, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmDeliverDtl> findBillWmDeliverDtlGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectBillWmDeliverDtlGroupByPage(page, orderByField, orderBy, params, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBillWmDeliverDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectBillWmDeliverDtlCount(params, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmDeliverDtl> findBillWmDeliverDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectBillWmDeliverDtlByPage(page, orderByField, orderBy, params, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return  billWmDeliverDtlMapper.selectSumQty(map, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateOperateRecord(Map<String, Object> map) throws ServiceException {
		try {
			billWmDeliverDtlMapper.updateOperateRecord(map);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}