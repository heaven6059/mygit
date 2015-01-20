package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmOutstockDirectForQuery;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDirectMapper;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-09 11:09:10
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
@Service("billOmOutstockDirectService")
class BillOmOutstockDirectServiceImpl extends BaseCrudServiceImpl implements BillOmOutstockDirectService {
    @Resource
    private BillOmOutstockDirectMapper billOmOutstockDirectMapper;

    private final static String RESULTY = "Y";
    
    @Override
    public BaseCrudMapper init() {
        return billOmOutstockDirectMapper;
    }
    

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findOutstockDirectByStoreCount(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmOutstockDirectMapper.selectOutstockDirectByStoreCount(vo, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> findOutstockDirectByStoreByPage(SimplePage page, String orderByField,
			String orderBy, BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmOutstockDirectMapper.selectOutstockDirectByStoreByPage(page, orderByField, orderBy, vo, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectOutstockDirectByStoreSumQty(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmOutstockDirectMapper.selectOutstockDirectByStoreSumQty(vo, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public int findOutstockDirectByAreaCount(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmOutstockDirectMapper.selectOutstockDirectByAreaCount(vo, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	public List<BillOmOutstockDirect> findOutstockDirectByAreaByPage(SimplePage page, String orderByField,
			String orderBy, BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmOutstockDirectMapper.selectOutstockDirectByAreaByPage(page, orderByField, orderBy, vo, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectOutstockDirectByAreaSumQty(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmOutstockDirectMapper.selectOutstockDirectByAreaSumQty(vo, authorityParams);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> queryLocateNoByExpType(BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return  billOmOutstockDirectMapper.queryLocateNoByExpType(vo, authorityParams);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> queryOperateTypeByParam(
			BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams) throws ServiceException {
		try{
			return  billOmOutstockDirectMapper.queryOperateTypeByParam(vo, authorityParams);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> queryAreaInfoByParam(
			BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams, String orderByField, String orderBy) throws ServiceException {
		try{
			return  billOmOutstockDirectMapper.queryAreaInfoByParam(vo, authorityParams, orderByField, orderBy);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> queryStoreInfoByParam(
			BillOmOutstockDirectForQuery vo, AuthorityParams authorityParams, String orderByField, String orderBy) throws ServiceException {
		try{
			return  billOmOutstockDirectMapper.queryStoreInfoByParam(vo, authorityParams, orderByField, orderBy);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryOutstockDirectByStore(
			BillOmOutstockDirectForQuery vo) throws ServiceException {
		try{
			return  billOmOutstockDirectMapper.queryOutstockDirectByStore(vo);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	public List<BillOmOutstockDirect> queryOutstockDirectByArea(
			BillOmOutstockDirectForQuery vo) throws ServiceException {
		try{
			return  billOmOutstockDirectMapper.queryOutstockDirectByArea(vo);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}
	
	/**
	 * 拣货任务分派 - 发单调用存储过程
	 * @param map
	 * @throws DaoException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void procOmOutStockDirect(BillOmOutstockDirectForQuery vo) throws ServiceException{
		String message = "发单失败,请检查是否已经发单!";
		try{
			List<BillOmOutstockDirect> dlist = vo.getDlist();
			if(CommonUtil.hasValue(dlist)){
				for (BillOmOutstockDirect b : dlist) {
					try{
						vo.setStoreNo(b.getStoreNo());
						vo.setAreaNo(b.getAreaNo());
						Map<String, String> map = procOmOutStockDirectQuery(vo);
						billOmOutstockDirectMapper.procOmOutStockDirect(map);
						if (!RESULTY.equals(map.get("stroutmsg"))) {
							
							String msg = map.get("stroutmsg");
							if(StringUtils.isNotBlank(msg)){
								String[] msgs = msg.split("\\|");
								message = msgs[1];
							}
							throw new ServiceException(message);
						}
					}catch(Exception e){
						throw new  ServiceException(message);
					}
				}
			}else{
				throw new  ServiceException("参数非法!");
//				Map<String, String> map = procOmOutStockDirectQuery(vo);
//				billOmOutstockDirectMapper.procOmOutStockDirect(map);
//				if (!RESULTY.equals(map.get("stroutmsg"))) {
//					
//					String msg = map.get("stroutmsg");
//					if(StringUtils.isNotBlank(msg)){
//						String[] msgs = msg.split("\\|");
//						message = msgs[1];
//					}
//					throw new ServiceException(message);
//				}
			}
			
		}catch(Exception e){
			throw new  ServiceException(message);
		}
	}
	
	public Map<String, String> procOmOutStockDirectQuery(BillOmOutstockDirectForQuery vo){
		Map<String, String> map = new HashMap<String, String>();
		map.put("v_exp_type", vo.getExpType());
		map.put("v_sort_type", vo.getSortType());
		//map.put("v_direct_serial", vStrDirectSerial);
		map.put("v_work_qty", String.valueOf(vo.getWorkQty()));
		map.put("v_operate_type", vo.getOperateType());
		map.put("v_batch_no", vo.getBatchNo());
		map.put("v_locate_no", vo.getLocateNo());
		map.put("v_locno", vo.getLocno());
		map.put("v_area_no", vo.getAreaNo());
		map.put("v_store_no", vo.getStoreNo());
		map.put("v_pickingpeople", vo.getPickingPeople());
		map.put("v_creator", vo.getCreator());
		map.put("v_floor", String.valueOf(vo.getIsFloor()));
		return map;
	}
	
	@Override
	public void procOmPlanOutStockDirect(BillOmOutstockDirectForQuery vo) throws ServiceException {
		try{
			
			String vStrDirectSerial = "";
			List<BillOmOutstockDirect> dlist = vo.getDlist();
			if(!CommonUtil.hasValue(dlist)){
				throw new ServiceException("下架指示ID为空!");
			}
			
			for (BillOmOutstockDirect d : dlist) {
				vStrDirectSerial += d.getDirectSerial()+",";
			}
			if(StringUtils.isNotBlank(vStrDirectSerial)){
				vStrDirectSerial=vStrDirectSerial.substring(0, vStrDirectSerial.length()-1);
			}
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("v_locno", vo.getLocno());
			map.put("v_ware_no", vo.getWareNo());
			map.put("v_area_no", vo.getAreaNo());
			map.put("v_str_direct_serial", vStrDirectSerial);
			map.put("v_outstock_people", vo.getOutstockPeople());
			map.put("v_creator", vo.getCreator());
			map.put("v_cell_type", vo.getCellType());
			
			billOmOutstockDirectMapper.procOmPlanOutStockDirect(map);
			if (!RESULTY.equals(map.get("stroutmsg"))) {
				String message = "";
				String msg = map.get("stroutmsg");
				if(StringUtils.isNotBlank(msg)){
					String[] msgs = msg.split("\\|");
					message = msgs[1];
				}
				throw new ServiceException(message);
			}
		
		}catch(Exception e){
			throw new  ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findOutstockDirectCount(BillOmOutstockDirect billOmOutstockDirect, AuthorityParams authorityParams) throws ServiceException {
		try{
			return this.billOmOutstockDirectMapper.selectOutstockDirectCount(billOmOutstockDirect, authorityParams);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> findOutstockDirectByPage(SimplePage page,
			BillOmOutstockDirect billOmOutstockDirect, AuthorityParams authorityParams) throws ServiceException {
		try{
			return this.billOmOutstockDirectMapper.selectOutstockDirectByPage(page, billOmOutstockDirect, authorityParams);
		}catch(Exception e){
			throw new  ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(BillOmOutstockDirect billOmOutstockDirect, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDirectMapper.selectSumQty(billOmOutstockDirect, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmOutstockDirect> queryLocateNoByMore(
			Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return this.billOmOutstockDirectMapper.queryLocateNoByMore(map, authorityParams);
		} catch (DaoException e) {
			throw new  ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findHmPlanCmDefareaCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDirectMapper.selectHmPlanCmDefareaCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<CmDefarea> findHmPlanCmDefareaByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDirectMapper.selectHmPlanCmDefareaByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
}