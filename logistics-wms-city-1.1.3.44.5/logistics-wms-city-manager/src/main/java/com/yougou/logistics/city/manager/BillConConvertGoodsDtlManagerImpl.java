package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtlSizeDto;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillConConvertGoodsDtlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
@Service("billConConvertGoodsDtlManager")
class BillConConvertGoodsDtlManagerImpl extends BaseCrudManagerImpl implements BillConConvertGoodsDtlManager {
	@Resource
	private BillConConvertGoodsDtlService billConConvertGoodsDtlService;

	@Override
	public BaseCrudService init() {
		return billConConvertGoodsDtlService;
	}

	@Override
	public void saveConvertGoodsDtl(BillConConvertGoods convertGoods, List<BillUmCheck> insertList,
			List<BillUmCheck> deleteList) throws ManagerException {
		try {
			billConConvertGoodsDtlService.saveConvertGoodsDtl(convertGoods, insertList, deleteList);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheckByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billConConvertGoodsDtlService.findConvertGoodsDtlGroupByCheckByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findConvertGoodsDtlGroupByCheckCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billConConvertGoodsDtlService.findConvertGoodsDtlGroupByCheckCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheck(Map<String, Object> params)
			throws ManagerException {
		try {
			return billConConvertGoodsDtlService.findConvertGoodsDtlGroupByCheck(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectGroupByCheckSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billConConvertGoodsDtlService.selectGroupByCheckSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billConConvertGoodsDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<Map<String, Object>> findDtl4SizeHorizontal(String keys, AuthorityParams authorityParams)
			throws ManagerException {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			String[] keysArray = keys.split(",");
			for (String str : keysArray) {
				String[] strs = str.split("\\|");
				String locno = strs[0];
 				String convertGoodsNo = strs[1];
				String ownerNo = strs[2];
				String convertTypeStr = strs[3];
				String storeNo = strs[4];
				String storeName = strs[6];
				String locnoName = strs[5];
				
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("locno", locno);
				params.put("convertGoodsNo", convertGoodsNo);
				params.put("ownerNo", ownerNo);
				List<BillConConvertGoodsDtlSizeDto> list = this.billConConvertGoodsDtlService.findDtl4SizeHorizontal(params,authorityParams);
				if(CommonUtil.hasValue(list)){
					Map<String, Object> main = new HashMap<String, Object>();
					Map<String, BillConConvertGoodsDtlSizeDto> itemRowDto = new HashMap<String, BillConConvertGoodsDtlSizeDto>();
					List<BillConConvertGoodsDtlSizeDto> rows = new ArrayList<BillConConvertGoodsDtlSizeDto>();
					List<List<String>> sizeList = new ArrayList<List<String>>();
					Map<String, BigDecimal> sizeCodeQtyMap = null;
					BillConConvertGoodsDtlSizeDto temp;
					BigDecimal total = new BigDecimal(0);
					
					Map<String, Map<String, String>> sizeHead = new TreeMap<String, Map<String,String>>();
					Map<String, String> sizeRow = null;
					int sizeColNum = 0;
					for(BillConConvertGoodsDtlSizeDto d:list){
						String itemNo = d.getItemNo();
						if((temp = itemRowDto.get(itemNo)) != null){
							sizeCodeQtyMap = temp.getSizeCodeQtyMap();
							temp.setTotalQty(temp.getTotalQty().add(d.getItemQty()));
							if(sizeCodeQtyMap.get(d.getSizeCode()) != null){
								sizeCodeQtyMap.put(d.getSizeCode(), sizeCodeQtyMap.get(d.getSizeCode()).add(d.getItemQty()));
							}else{
								sizeCodeQtyMap.put(d.getSizeCode(), d.getItemQty());
							}
						}else{
							sizeCodeQtyMap = new TreeMap<String, BigDecimal>();
							sizeCodeQtyMap.put(d.getSizeCode(), d.getItemQty());
							d.setSizeCodeQtyMap(sizeCodeQtyMap);
							d.setTotalQty(d.getItemQty());
							itemRowDto.put(itemNo, d);
						}
						if((sizeRow = sizeHead.get(d.getSizeKind())) != null){
							sizeRow.put(d.getSizeCode(), d.getSizeCode());
						}else{
							sizeRow = new TreeMap<String, String>();
							sizeRow.put(d.getSizeCode(), d.getSizeCode());
							sizeHead.put(d.getSizeKind(), sizeRow);
						}
						total = total.add(d.getItemQty());
					}
					for(Entry<String, BillConConvertGoodsDtlSizeDto> m : itemRowDto.entrySet()){
						rows.add(m.getValue());
					}
					List<String> sizeSingleRow = null;
					for(Entry<String, Map<String, String>> m : sizeHead.entrySet()){
						sizeSingleRow = new ArrayList<String>();
						sizeSingleRow.add(m.getKey());
						for(Entry<String, String> s:m.getValue().entrySet()){
							sizeSingleRow.add(s.getValue());
						}
						if(sizeColNum < sizeSingleRow.size()){
							sizeColNum = sizeSingleRow.size();
						}
						sizeList.add(sizeSingleRow);
					}
					main.put("convertGoodsNo", convertGoodsNo);
					main.put("convertTypeStr", convertTypeStr);
					main.put("storeNo", storeNo != null && !storeNo.equals("null")?storeNo:"");
					main.put("locnoName", locnoName != null && !locnoName.equals("null")?locnoName:"");
					main.put("storeName", storeName != null && !storeName.equals("null")?storeName:"");
					main.put("total", total);
					main.put("list", rows);
					main.put("sizeList", sizeList);
					main.put("sizeColNum", sizeColNum);
					resultList.add(main);
				}else{
					throw new ManagerException("单据："+convertGoodsNo+"没有明细！");
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return resultList;
	}
}