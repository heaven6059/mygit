package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillChCheckDirectBoxDto;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.dal.mapper.BillChCheckDirectMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 14:54:50 CST 2013
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
@Service("billChCheckDirectService")
class BillChCheckDirectServiceImpl extends BaseCrudServiceImpl implements BillChCheckDirectService {
	@Resource
	private BillChCheckDirectMapper billChCheckDirectMapper;
	
    @Resource
    private BmContainerService bmContainerService;

	@Override
	public BaseCrudMapper init() {
		return billChCheckDirectMapper;
	}

	@Override
	public int findDirectCount(BillChCheckDirectDto dto) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectCount(dto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillChCheckDirectDto> findDirectList(BillChCheckDirectDto dto, String orderBy, SimplePage page) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectList(dto, orderBy, page);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Brand> findBrandInDirect(BillChCheckDirectDto dto) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectBrandInDirect(dto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDirect> findDirectByItem(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectByItem(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDirect> findDirectByItem4RQZX(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectByItem4RQZX(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDirect> findDirectByItem4RQLS(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectByItem4RQLS(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDirectBoxDto> selectDirectBoxNoList(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectBoxNoList(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
    
	
	@Override
	public List<BillChCheckDirect> selectDirectCellNoByGroup(Map<String, Object> params) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectCellNoByGroup(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public boolean beforeDirectCheck(Map<String, Object> params) throws ServiceException {
		try {
			Integer count= billChCheckDirectMapper.directCheck(params);
			if(count>0){
				return false;
			}else{
				return true;
			}
			
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDirect> findDirectByCell(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectByCell(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	public void callContrainStatus(String planNo,String locno,SystemUser user) throws ServiceException {
		//容器记账，释放锁定箱子  释放，  status=0，falg不为空
		List<BmContainer> lstContainer = new  ArrayList<BmContainer>();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("planNo", planNo);
			params.put("locno", locno);
			List<BillChCheckDirect>  lstLabel = billChCheckDirectMapper.selectDirectLableNo(params);
			for(BillChCheckDirect  objDirect :lstLabel){
				//锁定箱信息
				if(StringUtils.isNotBlank(objDirect.getLabelNo())&& !"N".equals(objDirect.getLabelNo())){
					BmContainer  bc = new BmContainer();
					bc.setConNo(objDirect.getLabelNo());
					bc.setLocno(locno);
					bc.setStatus("0");
					bc.setFalg("A");
					bc.setEdittm(DateUtil.getCurrentDateTime());
					bc.setEditor(user.getLocName());
					lstContainer.add(bc);
				}
			}
			
			//容器锁定
			if(CollectionUtils.isNotEmpty(lstContainer)){
				bmContainerService.batchUpdate(lstContainer);
			}
			
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	public List<BillChCheckDirect> findDirectByCell4RQLS(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectByCell4RQLS(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	public List<BillChCheckDirect> findDirectByCell4RQZX(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectByCell4RQZX(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	public List<BillChCheckDirectDto> findDirectAndContent(Map<String, Object> params) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectAndContent(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@Override
	public List<String> findDirectCellNo(Map<String, Object> params) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectDirectCellNo(params);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public List<BillChCheckDirect> selectPlanNo(Map<String, String> map, SimplePage page) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectPlanNo(map, page);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public int selectPlanNoCount(Map<String, String> map) throws ServiceException {
		try {
			return billChCheckDirectMapper.selectPlanNoCount(map);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public Map<String, Object> selectAllCellCountAndStockCount(
			BillChCheckDirectDto dto) throws ServiceException {
		try {
			Integer cellCount = this.billChCheckDirectMapper.selectAllCellCount(dto);
			Integer stockCount = this.billChCheckDirectMapper.selectAllStockCount(dto);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("cellCount",cellCount);
			map.put("stockCount",stockCount);
			return map;
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public void batchInsertDtl(List<BillChCheckDirect> list)
			throws ServiceException {
		billChCheckDirectMapper.batchInsertDtl(list);
	}

	@Override
	public void batchUpdate4Status(List<BillChCheckDirect> list)
			throws ServiceException {
		billChCheckDirectMapper.batchUpdate4Status(list);
	}
}