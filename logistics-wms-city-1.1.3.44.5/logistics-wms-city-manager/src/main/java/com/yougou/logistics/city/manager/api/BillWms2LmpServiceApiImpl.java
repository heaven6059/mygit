package com.yougou.logistics.city.manager.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.BillWms2LmpServiceApi;
import com.yougou.logistics.city.common.api.dto.Bill4WmsDto;
import com.yougou.logistics.city.common.api.dto.ItemManageQueryDto;
import com.yougou.logistics.city.common.api.dto.ItemManageResultDto;
import com.yougou.logistics.city.common.api.dto.ItemManageVo;
import com.yougou.logistics.city.common.api.dto.ItemManageVo.SizeKV;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillWms2LmpService;
import com.yougou.logistics.city.service.SizeInfoService;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-5-14 下午2:37:52
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billWms2LmpServiceApi")
public class BillWms2LmpServiceApiImpl implements BillWms2LmpServiceApi {
	@Resource
	BillWms2LmpService billWms2LmpService;

	@Resource
	private SizeInfoService sizeInfoService;
	
	@Override
	public List<Bill4WmsDto> getBill4Wms(String locno, String sysNo, List<String> billType, String beginDate,
			String endDate, SimplePage page) throws RpcException {
		try {
			Assert.hasText(locno, "区域编码不能为空");
			Assert.hasText(sysNo, "品牌库不能为空");
			return billWms2LmpService.getBill4Wms(locno, sysNo, billType, beginDate, endDate, page);
		} catch (Exception e) {
			throw new RpcException("logistics-wms-city", e);
		}
	}

	@Override
	public ItemManageVo findItemManage(ItemManageQueryDto queryDto)
			throws RpcException {
		ItemManageVo vo = new ItemManageVo();
		List<String> locnoList = queryDto.getLocnoList();
		String brandNo = queryDto.getBrandNo();
		if(!CommonUtil.hasValue(locnoList)){
			throw new RpcException("logistics-wms-city", "仓别不能为空!");
		}
		if(!CommonUtil.hasValue(brandNo)){
			throw new RpcException("logistics-wms-city", "品牌编码不能为空!");
		}
		String sysNo = brandNo.substring(0,2);
		int pageNo = queryDto.getPageNo();
		int pageSize = queryDto.getPageSize();
		String productNo = queryDto.getProductNo();
		List<String> majorNoList = queryDto.getMajorNoList();
		List<String> seasonList = queryDto.getSeasonList();
		List<String> yearsList = queryDto.getYearsList();
		List<String> genderList = queryDto.getGenderList();
		Map<String, Object> otherParams = queryDto.getOtherParams();
		try {
			Map<String, Map<String, SizeKV>> sizeHead = getSizeHead(sysNo);
			if(sizeHead == null || sizeHead.size() == 0){
				throw new RpcException("logistics-wms-city", "获取尺码头异常!");
			}
			vo.setSizeHead(sizeHead);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("brandNo", brandNo);
			if(locnoList.size() == 1){
				params.put("locno", locnoList.get(0));
			}else{
				params.put("locnoList", locnoList);
			}
			params.put("sysNo", sysNo);
			if(CommonUtil.hasValue(productNo)){
				params.put("itemNo", sysNo+productNo);				
			}
			if(CommonUtil.hasValue(majorNoList)){
				params.put("majorNoList", majorNoList);				
			}
			if(CommonUtil.hasValue(seasonList)){
				params.put("seasonList", seasonList);				
			}
			if(CommonUtil.hasValue(yearsList)){
				params.put("yearsList", yearsList);				
			}
			if(CommonUtil.hasValue(genderList)){
				params.put("genderList", genderList);				
			}
			if(otherParams != null){
				params.putAll(otherParams);
			}
			int total = billWms2LmpService.findItemManageContentCount(params);
			vo.setTotal(total);
			if(total <= 0){
				return vo;
			}
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			vo.setPageNo(pageNo);
			vo.setPageSize(page.getPageSize());
			List<ItemManageResultDto> list = billWms2LmpService.findItemManageContentByPage(params, page);
			List<ItemManageResultDto> rs = group(list, sizeHead);
			if(CommonUtil.hasValue(rs)){
				vo.setRows(rs);
				vo.setPageNum(rs.size());
				int sumQty = billWms2LmpService.findItemManageContentNum(params);
				vo.setSumQty(new BigDecimal(sumQty));
			}
			
		} catch (Exception e) {
			throw new RpcException("logistics-wms-city", e);
		}
		return vo;
	}
	
	//获取尺码信息表MAP
	private Map<String, Map<String, SizeKV>> getSizeHead(String sysNo) {
		try {
			Map<String, Map<String, SizeKV>> sizeHead = new TreeMap<String, Map<String, SizeKV>>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sysNo", sysNo);
			List<SizeInfo> sizeInfoList = this.sizeInfoService.findByBiz(null, params);
			Map<String, SizeKV> kingMap = null;
			String sizeKind = null;
			String sizeCode = null;
			String sizeNo = null;
			SizeKV kv = null;
			for (SizeInfo si : sizeInfoList) {
				sizeKind = si.getSizeKind();
				sizeCode = si.getSizeCode();
				sizeNo = si.getSizeNo();
				kv = new SizeKV(sizeNo, sizeCode, si.getHcolNo().intValue());
				
				kingMap = sizeHead.get(sizeKind);
				if(kingMap == null){
					kingMap = new TreeMap<String, SizeKV>();
					kingMap.put(sizeNo, kv);
					sizeHead.put(sizeKind, kingMap);
				}else{
					kingMap.put(sizeNo, kv);
				}
			}
			return sizeHead;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<ItemManageResultDto> group(List<ItemManageResultDto> list,Map<String, Map<String, SizeKV>> sizeHead){
		if(!CommonUtil.hasValue(list)){
			return null;
		}
		List<ItemManageResultDto> rs = new ArrayList<ItemManageResultDto>();
		Map<String, SizeKV> sMap;
		SizeKV kv;
		ItemManageResultDto main = null;
		for(ItemManageResultDto d : list){
			sMap = sizeHead.get(d.getSizeKind());
			if(sMap != null){
				kv = sMap.get(d.getSizeNo());
				if(kv != null){
					String filedName="setV"+(kv.getIdx());
					Object[] arg=new Object[]{String.valueOf(d.getQty())};
					try {
						if(main != null && main.getItemNo().equals(d.getItemNo()) && main.getQuality().equals(d.getQuality())){
							CommonUtil.invokeMethod(main,filedName,arg);
							main.setTotalQty(main.getTotalQty().add(d.getQty()));
						}else{
							CommonUtil.invokeMethod(d,filedName,arg);
							main = d;
							main.setTotalQty(main.getQty());
							main.setProductNo(main.getItemNo().substring(2));
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
}
