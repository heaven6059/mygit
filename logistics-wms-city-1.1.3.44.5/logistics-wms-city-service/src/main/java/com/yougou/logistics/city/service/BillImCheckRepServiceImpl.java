package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
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
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.dto.baseinfo.SizeInfoMapDto;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillImCheckRepMapper;

/**
 * 入库查询service实现
 * @author chen.yl1
 *
 */
@Service("billImCheckRepService")
class BillImCheckRepServiceImpl extends BaseCrudServiceImpl implements BillImCheckRepService {
	@Resource
	private BillImCheckRepMapper billImCheckRepMapper;

	@Resource
	private SizeInfoService sizeInfoService;
	
	@Log
	private Logger log;
	
	@Override
	public BaseCrudMapper init() {
		return billImCheckRepMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<SizeInfo> getSizeCodeByGroup(BillCheckImRep model,
			AuthorityParams authorityParams) {
		List<SizeInfo> list = new ArrayList<SizeInfo>();
		list = billImCheckRepMapper.getSizeCodeByGroup(model, authorityParams);
		return list;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<String> selectAllDtlSizeKind(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepMapper.selectAllDtlSizeKind(model, authorityParams);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckRepMapper.getCount(model, authorityParams);
	}
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams) {
		return billImCheckRepMapper.getBillImCheckByGroup(model, authorityParams);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckDtlIm(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepMapper.getBillImCheckDtlIm(model, authorityParams);
	}
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckDtlUm(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepMapper.getBillImCheckDtlUm(model, authorityParams);
	}
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckDtlOtm(BillCheckImRep model,
			AuthorityParams authorityParams) {
		return billImCheckRepMapper.getBillImCheckDtlOtm(model, authorityParams);
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(BillCheckImRep model, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImCheckRepMapper.selectSumQty(model, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findBillImCheckRepByPage(BillCheckImRep model,
			AuthorityParams authorityParams, boolean all) throws ServiceException {
		Map<String,Object> obj=new HashMap<String,Object>();
		try{
			int total = billImCheckRepMapper.getCount(model, authorityParams);
			SimplePage page = new SimplePage(model.getPageNo(), model.getPageSize(), total);
			if(all){
				page = new SimplePage(1, total, total);
				model.setPageNo(1);
				model.setPageSize(total);
			}
			Map<String, Map<String, SizeInfoMapDto>> map = null;
			if (StringUtils.isNotBlank(model.getSysNo())) {
				map = getSizeInfoMapList(model.getSysNo());
			}
			/*含尺码和数量的数据*/
			List<BillCheckImRep> list = billImCheckRepMapper.getBillImCheckExportByGroup(model, authorityParams);
			if (CollectionUtils.isEmpty(list)) {
				obj.put("total", 0);
				obj.put("rows", new ArrayList<Object>());
				return obj;
			}
			List<BillCheckImRep> rs = group(list, map);
			
//			if(model.getPageNo() == 1){
//					page.setPageSize(total);
//					listCC = conContentMapper.selectSizeAndNum4DivideLocConContent(cc, authorityParams);
//					if(CommonUtil.hasValue(listCC)){
//						List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
//						Map<String, Object> footerMap = new HashMap<String, Object>();
//						footerMap.put("itemNo", "汇总");
//						footerList.add(footerMap);
//						footerMap.put("qty", listCC.get(0).getQty());
//						obj.put("footer", footerList);		
//				}
//			}
			obj.put("total", total);
			obj.put("rows", rs);
			
			return obj;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private List<BillCheckImRep> group(List<BillCheckImRep> list,Map<String, Map<String, SizeInfoMapDto>> map) throws ServiceException {
		if(!CommonUtil.hasValue(list)){
			return null;
		}
		List<BillCheckImRep> rs = new ArrayList<BillCheckImRep>();
		Map<String, SizeInfoMapDto> mapS;
		SizeInfoMapDto sd;
		BillCheckImRep main = null;
		for(BillCheckImRep d : list){
			mapS = map.get(d.getSizeKind());
			if (mapS!=null) {
				sd = mapS.get(d.getSizeNo());
				if(sd != null && sd.getSizeInfo() != null){
					String filedName="setV"+(sd.getI()+1);
					Object[] arg=new Object[]{String.valueOf(d.getCheckQty())};
					try {
						if(main != null 
								&& main.getImportNo().equals(d.getImportNo())
								&& main.getItemNo().equals(d.getItemNo())
								&& main.getReciveDate().equals(d.getReciveDate())
//								&& main.getCateNo().equals(d.getCateNo())
//								&& main.getYears().equals(d.getYears())
//								&& main.getSeason().equals(d.getSeason())
//								&& main.getGender().equals(d.getGender())
								){
							CommonUtil.invokeMethod(main,filedName,arg);
							if(main.getAllCount()==null) {
								main.setAllCount(new BigDecimal(0));
							}
							main.setAllCount(main.getAllCount().add(d.getCheckQty()));
							main.setCheckQty(main.getCheckQty().add(d.getCheckQty()));
						}else{
							CommonUtil.invokeMethod(d,filedName,arg);
							main = d;
							if(main.getAllCount()==null) {
								main.setAllCount(new BigDecimal(0));
							}
							main.setAllCount(main.getAllCount().add(d.getCheckQty()));
							rs.add(main);
						}
					} catch (Exception e) {
						throw new ServiceException(e);
					}
				}
			}
		}
		return rs;
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
				log.error(e.getMessage(), e);
			}
			return null;
		}
}