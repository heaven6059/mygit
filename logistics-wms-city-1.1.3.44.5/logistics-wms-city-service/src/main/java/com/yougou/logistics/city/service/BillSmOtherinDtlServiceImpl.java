package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillSmOtherinDtlDto;
import com.yougou.logistics.city.common.dto.baseinfo.SizeInfoMapDto;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.BillSmOtherinPrintDto;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillSmOtherinDtlMapper;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
@Service("billSmOtherinDtlService")
class BillSmOtherinDtlServiceImpl extends BaseCrudServiceImpl implements BillSmOtherinDtlService {
    @Resource
    private BillSmOtherinDtlMapper billSmOtherinDtlMapper;
    @Resource
	private SizeInfoService sizeInfoService;
    @Override
    public BaseCrudMapper init() {
        return billSmOtherinDtlMapper;
    }
    @Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectContentCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectContentCount(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmOtherinDtlDto> selectContent(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectContent(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectPageSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectPageSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public int selectMaxPid(BillSmOtherinDtl billSmOtherinDtl)
			throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectMaxPid(billSmOtherinDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectIsHave(BillSmOtherinDtl billSmOtherinDtl)
			throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectIsHave(billSmOtherinDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillSmOtherinDtl> selectContentParams(BillSmOtherinDtl modelType,
			Map<String, Object> params) throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectContentParams(modelType, params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int updateContent(BillSmOtherinDtl modelType) throws ServiceException {
		try {
			return billSmOtherinDtlMapper.updateContent(modelType);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillSmOtherinDtl> selectContentDtl(BillSmOtherinDtl modelType,
			Map<String, Object> params) throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectContentDtl(modelType, params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public List<BillSmOtherinDtl> findDuplicateRecord(Map<String, Object> params)
			throws ServiceException {
		try{
			return billSmOtherinDtlMapper.selectDuplicateRecord(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
		
	}
	@Override
	public void batchInsertDtl(List<BillSmOtherinDtl> list) throws ServiceException {
		try{
			billSmOtherinDtlMapper.batchInsertDtl(list);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmOtherinDtl> findDtlSysNo(BillSmOtherinDtl modelType, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmOtherinDtlMapper.selectDtlSysNo(modelType, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public void updateRecheckQty4Convert(Map<String, Object> params)
			throws ServiceException {
		try {
			billSmOtherinDtlMapper.updateRecheckQty4Convert(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> findDtlSysNoByPage(BillSmOtherinDtl billSmOtherinDtl,
			AuthorityParams authorityParams) throws ServiceException {
		
		Map<String,Object> obj=new HashMap<String,Object>();
//		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		footerMap.put("itemNo", "小计");
//		footerList.add(footerMap);
		try{
			Map<String, Map<String, SizeInfoMapDto>> map = null;
			if (StringUtils.isNotBlank(billSmOtherinDtl.getSysNo())) {
				map = getSizeInfoMapList(billSmOtherinDtl.getSysNo());
			}
			List<BillSmOtherinDtl> listCC = null;
			int total = billSmOtherinDtlMapper.selectSysNoContentCount(billSmOtherinDtl, authorityParams);
			if(total > 0) {
				SimplePage page = new SimplePage(1, total, total);
				billSmOtherinDtl.setPageNo(1);
				billSmOtherinDtl.setPageSize(total);
				
				/*不含尺码的数据*/
				listCC = billSmOtherinDtlMapper.selectSysNoContentByPage(page, null, null, billSmOtherinDtl, authorityParams);
				if (CollectionUtils.isEmpty(listCC)) {
					obj.put("total", 0);
					obj.put("rows", new ArrayList<Object>());
					return obj;
				}
				BigDecimal total4Footer = new BigDecimal(0);
				for (BillSmOtherinDtl c : listCC) {
					total4Footer = total4Footer.add(c.getTotal());
					/*获取尺码及数量(已分组)*/
					List<BillSmOtherinDtl> listDtos=billSmOtherinDtlMapper.selectSysNoByPage(c, authorityParams);
					if (CollectionUtils.isEmpty(listDtos)){
						continue;
					}
					Map<String, SizeInfoMapDto> mapS = null;
					if (map!=null) {
						mapS = map.get(c.getSizeKind());
					}
//					
					for(BillSmOtherinDtl d : listDtos){
						SizeInfoMapDto sizeInfoMapDTO = null;
						if (mapS!=null) {
							sizeInfoMapDTO = mapS.get(d.getSizeNo());
						}
						boolean sizeInfoFlag = true;
						if(sizeInfoMapDTO==null||sizeInfoMapDTO.getSizeInfo()==null||d.getSizeNo()==null){
							sizeInfoFlag = false;
						}
						if(sizeInfoFlag){
							String filedName="setV"+(sizeInfoMapDTO.getI()+1);
							Object[] arg=new Object[]{String.valueOf(d.getInstorageQty())};
							CommonUtil.invokeMethod(c,filedName,arg);
							this.setFooterMap("v" + (sizeInfoMapDTO.getI()+1), d.getInstorageQty(), footerMap);
						}
					}
				}
				this.setFooterMap("total", total4Footer, footerMap);
			}
			
			obj.put("total", total);
			obj.put("rows", listCC);
//			obj.put("footer", footerList);
			return obj;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}
	//获取尺码信息表MAP
		public Map<String, Map<String, SizeInfoMapDto>> getSizeInfoMapList(String sysNo) {
			try {
				Map<String, Map<String, SizeInfoMapDto>> map = new HashMap<String, Map<String, SizeInfoMapDto>>();
				Map<String, Object> mapParaMap = new HashMap<String, Object>();
				mapParaMap.put("sysNo", sysNo);
				List<SizeInfo> sizeInfoList = this.sizeInfoService.findByBiz(null, mapParaMap);
				Map<String, Integer> indexMap = new HashMap<String, Integer>();
				for (SizeInfo sizeInfo : sizeInfoList) {
					String sizeKind = sizeInfo.getSizeKind();
					Map<String, SizeInfoMapDto> sizeInfoMap = map.get(sizeKind);
					if (sizeInfoMap == null) {
						sizeInfoMap = new HashMap<String, SizeInfoMapDto>();
						map.put(sizeKind, sizeInfoMap);
					}
					Integer index = indexMap.get(sizeKind);
					if (index == null) {
						index = 0;
						indexMap.put(sizeKind, index);
					} else {
						index++;
						indexMap.put(sizeKind, index);
					}
					SizeInfoMapDto sizeInfoMapDTO = new SizeInfoMapDto();
					sizeInfoMapDTO.setI(index);
					sizeInfoMapDTO.setSizeInfo(sizeInfo);
					String sizeNo = sizeInfo.getSizeNo();
					sizeInfoMap.put(sizeNo, sizeInfoMapDTO);
				}
				return map;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		public List<BillSmOtherinPrintDto> findPrintDtl4Size(
				Map<String, Object> params) {
			return billSmOtherinDtlMapper.selectPrintDtl4Size(params);
		}
		@Override
		public void updateOperateRecord(Map<String, Object> map)
				throws ServiceException {
			try {
				billSmOtherinDtlMapper.updateOperateRecord(map);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
}