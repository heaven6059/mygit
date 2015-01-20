package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.baseinfo.SizeInfoMapDto;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.BillSmWasteDtlSizeDto;
import com.yougou.logistics.city.common.model.BillSmWastePrintDto;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillSmWasteDtlMapper;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
@Service("billSmWasteDtlService")
class BillSmWasteDtlServiceImpl extends BaseCrudServiceImpl implements BillSmWasteDtlService {
    @Resource
    private BillSmWasteDtlMapper billSmWasteDtlMapper;
    @Resource
	private SizeInfoService sizeInfoService;

    @Override
    public BaseCrudMapper init() {
        return billSmWasteDtlMapper;
    }

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectContentCount(Map<String, Object> params,AuthorityParams authorityParam)
			throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectContentCount(params,authorityParam);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmWasteDtl> selectContent(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParam) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectContent(page, orderByField, orderBy, params, authorityParam);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int selectMaxPid(BillSmWasteDtl billSmWasteDtl)
			throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectMaxPid(billSmWasteDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectIsHave(BillSmWasteDtl billSmWasteDtl)
			throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectIsHave(billSmWasteDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmWasteDtl> selectContentParams(BillSmWasteDtl modelType,
			Map<String, Object> params,AuthorityParams authorityParam) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectContentParams(modelType, params, authorityParam);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int updateContent(BillSmWasteDtl modelType) throws ServiceException {
		try {
			return billSmWasteDtlMapper.updateContent(modelType);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmWasteDtl> selectContentDtl(BillSmWasteDtl modelType,
			Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectContentDtl(modelType, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
    
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectSumQty(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmWasteDtl> findByWaste(BillSmWasteDtl modelType,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectByWaste(modelType, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public void batchInsertDtl(List<BillSmWasteDtl> list)
			throws ServiceException {
		try{
			billSmWasteDtlMapper.batchInsertDtl(list);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public List<BillSmWasteDtlSizeDto> findDtl4SizeHorizontal(String wasteNo) {
		return billSmWasteDtlMapper.selectDtl4SizeHorizontal(wasteNo);
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillSmWasteDtl> findDtlSysNo(BillSmWasteDtl modelType, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectDtlSysNo(modelType, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	
	@Override
	public List<BillSmWasteDtl> selectContentParams4Box(Map<String, Object> params) throws ServiceException {
		try {
			return billSmWasteDtlMapper.selectContentParams4Box(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	

	@Override
	public Integer batchInsertWasteDtl4Box(Map<String, Object> params, List<BillSmWasteDtl> boxList) throws ServiceException {
		try {
			return billSmWasteDtlMapper.batchInsertWasteDtl4Box(params,boxList);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public Integer batchUpdateWsateBoxStatus4Container(Map<String, Object> params) throws ServiceException {
		try{
			return billSmWasteDtlMapper.batchUpdateWsateBoxStatus4Container(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findDtlSysNoByPage(BillSmWasteDtl billSmWasteDtl,
			AuthorityParams authorityParams) throws ServiceException {
		
		Map<String,Object> obj=new HashMap<String,Object>();
//		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		footerMap.put("itemNo", "小计");
//		footerList.add(footerMap);
		try{
			Map<String, Map<String, SizeInfoMapDto>> map = null;
			if (StringUtils.isNotBlank(billSmWasteDtl.getSysNo())) {
				map = getSizeInfoMapList(billSmWasteDtl.getSysNo());
			}
			List<BillSmWasteDtl> listCC = null;
			int total = billSmWasteDtlMapper.selectSysNoContentCount(billSmWasteDtl, authorityParams);
			if(total > 0) {
				SimplePage page = new SimplePage(1, total, total);
				billSmWasteDtl.setPageNo(1);
				billSmWasteDtl.setPageSize(total);
				
				/*不含尺码的数据*/
				listCC = billSmWasteDtlMapper.selectSysNoContentByPage(page, null, null, billSmWasteDtl, authorityParams);
				if (CollectionUtils.isEmpty(listCC)) {
					obj.put("total", 0);
					obj.put("rows", new ArrayList<Object>());
					return obj;
				}
				BigDecimal total4Footer = new BigDecimal(0);
				for (BillSmWasteDtl c : listCC) {
					total4Footer = total4Footer.add(c.getTotal());
					/*获取尺码及数量(已分组)*/
					List<BillSmWasteDtl> listDtos=billSmWasteDtlMapper.selectSysNoByPage(c, authorityParams);
					if (CollectionUtils.isEmpty(listDtos)){
						continue;
					}
					Map<String, SizeInfoMapDto> mapS = null;
					if (map!=null) {
						mapS = map.get(c.getSizeKind());
					}
//					
					for(BillSmWasteDtl d : listDtos){
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
							Object[] arg=new Object[]{String.valueOf(d.getWasteQty())};
							CommonUtil.invokeMethod(c,filedName,arg);
							this.setFooterMap("v" + (sizeInfoMapDTO.getI()+1), d.getWasteQty(), footerMap);
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
		public List<BillSmWastePrintDto> findPrintDtl4Size(
				Map<String, Object> params) {
			return billSmWasteDtlMapper.selectPrintDtl4Size(params);
		}

		@Override
		public void updateOperateRecord(Map<String, Object> map)
				throws ServiceException {
			try {
				billSmWasteDtlMapper.updateOperateRecord(map);
			} catch (DaoException e) {
				throw new ServiceException(e);
			}
			
		}
}