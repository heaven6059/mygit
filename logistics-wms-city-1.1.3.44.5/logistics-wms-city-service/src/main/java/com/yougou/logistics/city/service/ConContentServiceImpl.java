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
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.dto.baseinfo.SizeInfoMapDto;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.ConContentMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 14:46:27 CST 2013
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
@Service("conContentService")
class ConContentServiceImpl extends BaseCrudServiceImpl implements ConContentService {

	@Resource
	private ConContentMapper conContentMapper;
	
	@Resource
	private SizeInfoService sizeInfoService;

	@Override
	public BaseCrudMapper init() {
		return conContentMapper;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findCountMx(ConContentDto conContentDto, AuthorityParams authorityParams) throws ServiceException {
		try {
			return conContentMapper.selectCountMx(conContentDto, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<ConContentDto> findConContentByPage(SimplePage page, ConContentDto conContentDto, AuthorityParams authorityParams) throws ServiceException {
		try {
			return conContentMapper.selectConContentByPage(page, conContentDto, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findInstantConContentCount(ConContentDto cc, AuthorityParams authorityParams) throws ServiceException {
		try{
			return conContentMapper.selectConContentCount(cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String,Object> findInstantConContentByPage(ConContentDto cc, AuthorityParams authorityParams, boolean all) throws ServiceException {
		
		//返回汇总列表
		Map<String,Object> obj=new HashMap<String,Object>();
		
		try{
			
			int total = conContentMapper.selectConContentCount(cc, authorityParams);
			SimplePage page = new SimplePage(cc.getPageNo(), cc.getPageSize(), total);
			if(all){
				page = new SimplePage(1, total, total);
				cc.setPageNo(1);
				cc.setPageSize(total);
			}
			
			Map<String, Map<String, SizeInfoMapDto>> map = null;
			if (StringUtils.isNotBlank(cc.getSysNo())) {
				map = getSizeInfoMapList(cc.getSysNo());
			}
			List<ConContentDto> listCC = conContentMapper.selectConContentGroupBy(page, cc, authorityParams);
			if (CollectionUtils.isEmpty(listCC)) {
				return null;
			}
			List<ConContentDto> rs = group4Instant(listCC, map);
			if(cc.getPageNo() == 1){
					page.setPageSize(total);
					listCC = conContentMapper.selectInstantConContentSum(cc, authorityParams);
					if(CommonUtil.hasValue(listCC)){
						List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
						Map<String, Object> footerMap = new HashMap<String, Object>();
						footerMap.put("itemNo", "汇总");
						footerList.add(footerMap);
						footerMap.put("qty", listCC.get(0).getTotalQty());
						footerMap.put("totalSalePrice", listCC.get(0).getTotalSalePrice());
						obj.put("footer", footerList);
				}
			}
			
			obj.put("total", total);
			obj.put("rows", rs);
			return obj;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	

	@Override
	public int procGetContentCellid() throws ServiceException {
		try {
			Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
			conContentMapper.selectContentCellid(map);
			BigDecimal decimal = map.get("cellId");
			int cellId = Integer.parseInt(decimal.toString());
			return cellId;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ConContent findConContentByItemKey(ConContent conContent) throws ServiceException {
		try {
			return conContentMapper.selectConContentByItemKey(conContent);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int updateConContentByItemKey(ConContent conContent) throws ServiceException {
		try {
			return conContentMapper.updateConContentByItemKey(conContent);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ConContent> findCmdefCellIsHaveConContent(Map<String, Object> params, List<CmDefcell> listCmDefcells)
			throws ServiceException {
		try {
			return conContentMapper.selectCmdefCellIsHaveConContent(params, listCmDefcells);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void procUpdtContentqtyByCellID(Map<String, String> map) throws ServiceException {
		try {
			conContentMapper.procUpdtContentqtyByCellID(map);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String getNextvalId() throws ServiceException {
		try {
			return conContentMapper.getNextvalId();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<String> getLocnoByContent() throws ServiceException {
		try {
			return conContentMapper.getLocnoByContent();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
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
	public List<ConContentDto> findViewByParams(Map<String, Object> params)
			throws ServiceException {
		try {
			return conContentMapper.selectViewByParams(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	public List<ConContentDto> selectConBoxViewByParams(Map<String, Object> params)throws ServiceException {
		try {
			return conContentMapper.selectConBoxViewByParams(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findDivideLocConContentByPage(ConContentDto cc,
			AuthorityParams authorityParams, boolean all) throws ServiceException {
		
		Map<String,Object> obj=new HashMap<String,Object>();
		try{
			int total = conContentMapper.selectCount4DivideLocConContent(cc, authorityParams);
			SimplePage page = new SimplePage(cc.getPageNo(), cc.getPageSize(), total);
			if(all){
				page = new SimplePage(1, total, total);
				cc.setPageNo(1);
				cc.setPageSize(total);
			}
			
			//List<ConContentDto> returnDtoList=new LinkedList<ConContentDto>();
			Map<String, Map<String, SizeInfoMapDto>> map = null;
			if (StringUtils.isNotBlank(cc.getSysNo())) {
				map = getSizeInfoMapList(cc.getSysNo());
			}
			/*含尺码和数量的数据*/
			List<ConContentDto> listCC = conContentMapper.selectByPage4DivideLocConContent(page, null, null, cc, authorityParams);
			if (CollectionUtils.isEmpty(listCC)) {
				obj.put("total", 0);
				obj.put("rows", new ArrayList<Object>());
				return obj;
			}
			List<ConContentDto> rs = group(listCC, map);
			
			if(cc.getPageNo() == 1){
					page.setPageSize(total);
					listCC = conContentMapper.selectSizeAndNum4DivideLocConContent(cc, authorityParams);
					if(CommonUtil.hasValue(listCC)){
						List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
						Map<String, Object> footerMap = new HashMap<String, Object>();
						footerMap.put("itemNo", "汇总");
						footerList.add(footerMap);
						footerMap.put("qty", listCC.get(0).getQty());
						obj.put("footer", footerList);
					
				}
			}
			obj.put("total", total);
			obj.put("rows", rs);
			
			return obj;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private List<ConContentDto> group(List<ConContentDto> list,Map<String, Map<String, SizeInfoMapDto>> map){
		if(!CommonUtil.hasValue(list)){
			return null;
		}
		List<ConContentDto> rs = new ArrayList<ConContentDto>();
		Map<String, SizeInfoMapDto> mapS;
		SizeInfoMapDto sd;
		ConContentDto main = null;
		for(ConContentDto d : list){
			mapS = map.get(d.getSizeKind());
			if (mapS!=null) {
				sd = mapS.get(d.getSizeNo());
				if(sd != null && sd.getSizeInfo() != null){
					String filedName="setV"+(sd.getI()+1);
					Object[] arg=new Object[]{String.valueOf(d.getQty())};
					try {
						if(main != null && main.getItemNo().equals(d.getItemNo()) && main.getQuality().equals(d.getQuality())){
							CommonUtil.invokeMethod(main,filedName,arg);
							main.setQty(main.getQty().add(d.getQty()));
							main.setInstockQty(main.getInstockQty().add(d.getInstockQty()));
							main.setOutstockQty(main.getOutstockQty().add(d.getOutstockQty()));
							main.setTotalQty(main.getTotalQty().add(d.getTotalQty()));
						}else{
							CommonUtil.invokeMethod(d,filedName,arg);
							main = d;
							rs.add(main);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return rs;
	}
	
	private List<ConContentDto> group4Instant(List<ConContentDto> list,Map<String, Map<String, SizeInfoMapDto>> map){
		if(!CommonUtil.hasValue(list)){
			return null;
		}
		List<ConContentDto> rs = new ArrayList<ConContentDto>();
		Map<String, SizeInfoMapDto> mapS;
		SizeInfoMapDto sd;
		ConContentDto main = null;
		for(ConContentDto d : list){
			mapS = map.get(d.getSizeKind());
			if (mapS!=null) {
				sd = mapS.get(d.getSizeNo());
				if(sd != null && sd.getSizeInfo() != null){
					String filedName="setV"+(sd.getI()+1);
					Object[] arg=new Object[]{String.valueOf(d.getQty())};
					try {
						if(main != null && main.getItemNo().equals(d.getItemNo())){
							CommonUtil.invokeMethod(main,filedName,arg);
							main.setQty(main.getQty().add(d.getQty()));
							main.setInstockQty(main.getInstockQty().add(d.getInstockQty()));
							main.setOutstockQty(main.getOutstockQty().add(d.getOutstockQty()));
							main.setTotalQty(main.getTotalQty().add(d.getTotalQty()));
							main.setTotalSalePrice(main.getTotalSalePrice().add(main.getSalePrice().multiply(d.getQty())));
						}else{
							CommonUtil.invokeMethod(d,filedName,arg);
							main = d;
							main.setTotalSalePrice(main.getSalePrice().multiply(d.getQty()));
							rs.add(main);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return rs;
	}

	@Override
	public int modifyStatus(Map<String, Object> params) throws ServiceException {
		try {
			return conContentMapper.updateStatus(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<ConContentDto> selectConContent(ConContentDto conContentDto, AuthorityParams authorityParams) throws ServiceException {
		try {
			return conContentMapper.selectConContent(conContentDto, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
}