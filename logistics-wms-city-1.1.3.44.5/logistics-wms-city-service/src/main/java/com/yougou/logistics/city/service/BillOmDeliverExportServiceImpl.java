package com.yougou.logistics.city.service;

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
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.baseinfo.SizeInfoMapDto;
import com.yougou.logistics.city.common.model.BillOmDeliverExport;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillOmDeliverExportMapper;

/**
 * 
 * 装车单详情service实现
 * 
 * @author jiang.ys
 * @date 2013-10-12 下午3:28:10
 * @version 0.1.0 
 * @copyright yougou.com
 */   
@Service("billOmDeliverExportService")
class BillOmDeliverExportServiceImpl extends BaseCrudServiceImpl implements BillOmDeliverExportService {
    @Resource  
    private BillOmDeliverExportMapper billOmDeliverExportMapper;

    @Override   
    public BaseCrudMapper init() {
        return billOmDeliverExportMapper;
    }
    
    @Resource
	private SizeInfoService sizeInfoService;
	
	@Log
	private Logger log;

	@Override
	public List<Map<String, Object>> findDeliverDtlSize(Map<String,Object> params) throws ServiceException {
		return billOmDeliverExportMapper.selectDeliverDtlSize(params);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<Map<String, Object>> findDeliverDtlSizeNum(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmDeliverExportMapper.selectDeliverDtlSizeNum(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<String> findAllDtlSizeKind(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billOmDeliverExportMapper.selectAllDtlSizeKind(params, authorityParams);
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findBillOmDeliverExportByPage(BillOmDeliverExport model,
			AuthorityParams authorityParams, boolean all) throws ServiceException {
		Map<String,Object> obj=new HashMap<String,Object>();
		try{
			int total = billOmDeliverExportMapper.selectExportCount(model, authorityParams);
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
			List<BillOmDeliverExport> list = billOmDeliverExportMapper.selectBillOmDeliverExportByGroup(model, authorityParams);
			if (CollectionUtils.isEmpty(list)) {
				obj.put("total", 0);
				obj.put("rows", new ArrayList<Object>());
				return obj;
			}
			List<BillOmDeliverExport> rs = group(list, map);
			
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
	
	private List<BillOmDeliverExport> group(List<BillOmDeliverExport> list,Map<String, Map<String, SizeInfoMapDto>> map) throws ServiceException {
		if(!CommonUtil.hasValue(list)){
			return null;
		}
		List<BillOmDeliverExport> rs = new ArrayList<BillOmDeliverExport>();
		Map<String, SizeInfoMapDto> mapS;
		SizeInfoMapDto sd;
		BillOmDeliverExport main = null;
		for(BillOmDeliverExport d : list){
			mapS = map.get(d.getSizeKind());
			if (mapS!=null) {
				sd = mapS.get(d.getSizeNo());
				if(sd != null && sd.getSizeInfo() != null){
					String filedName="setV"+(sd.getI()+1);
					Object[] arg=new Object[]{String.valueOf(d.getTotal())};
					try {
						if(main != null 
								&& main.getExpNo().equals(d.getExpNo())
								&& main.getStoreNo().equals(d.getStoreNo())
								&& main.getItemNo().equals(d.getItemNo())
								&& main.getExpDate().equals(d.getExpDate())
//								&& main.getCateNo().equals(d.getCateNo())
//								&& main.getYears().equals(d.getYears())
//								&& main.getSeason().equals(d.getSeason())
//								&& main.getGender().equals(d.getGender())
								){
							CommonUtil.invokeMethod(main,filedName,arg);
//							if(main.getAllCount()==null) {
//								main.setAllCount(new BigDecimal(0));
//							}
//							main.setAllCount(main.getAllCount().add(d.getCheckQty()));
							main.setTotal(main.getTotal().add(d.getTotal()));
						}else{
							CommonUtil.invokeMethod(d,filedName,arg);
							main = d;
//							if(main.getAllCount()==null) {
//								main.setAllCount(new BigDecimal(0));
//							}
//							main.setAllCount(main.getAllCount().add(d.getCheckQty()));
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
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmDeliverExportMapper.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}
}