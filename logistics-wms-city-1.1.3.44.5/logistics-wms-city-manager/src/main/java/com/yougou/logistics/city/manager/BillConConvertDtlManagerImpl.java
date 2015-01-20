package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.BillConvertDtlStatusEnums;
import com.yougou.logistics.city.common.model.BillConConvert;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.model.BillConConvertDtlSizeDto;
import com.yougou.logistics.city.common.model.BillConConvertKey;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillConConvertDtlService;
import com.yougou.logistics.city.service.BillConConvertService;
import com.yougou.logistics.city.service.CmDefcellService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
@Service("billConConvertDtlManager")
class BillConConvertDtlManagerImpl extends BaseCrudManagerImpl implements BillConConvertDtlManager {
    @Resource
    private BillConConvertDtlService billConConvertDtlService;

    @Resource
    private BillConConvertService billConConvertService;
    
    @Resource
    private CmDefcellService cmDefcellService;
    
    @Override
    public BaseCrudService init() {
        return billConConvertDtlService;
    }

	@Override
	public int findContentCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConConvertDtlService.findContentCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillConConvertDtl> findContentByPage(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConConvertDtlService.findContentByPage(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String saveDtl(String locno, String convertNo, String ownerNo,String operator,String destLocno,String creatorName,String editorName,
			List<BillConConvertDtl> insertList,
			List<BillConConvertDtl> deleteList,
			List<BillConConvertDtl> updatedList) throws ManagerException {
		try {
			Date date = new Date();
			//判断主档状态是否为建单状态
			BillConConvertKey key = new BillConConvertKey();
			key.setConvertNo(convertNo);
			key.setLocno(locno);
			key.setOwnerNo(ownerNo);
			BillConConvert billConConvert = (BillConConvert) billConConvertService.findById(key);
			if(billConConvert == null || !"10".endsWith(billConConvert.getStatus())){
				throw new ManagerException("当前单据状态不可编辑！");
			}
			int operand = 0;
			//删除
			if(CommonUtil.hasValue(deleteList)){
				for(BillConConvertDtl dtl:deleteList){
					dtl.setLocno(locno);
					dtl.setConvertNo(convertNo);
					dtl.setOwnerNo(ownerNo);
					operand = billConConvertDtlService.deleteById(dtl);
					if(operand <= 0){
						throw new ManagerException("删除明细异常");
					}
				}
			}
			//修改
			if(CommonUtil.hasValue(updatedList)){
				for(BillConConvertDtl dtl:updatedList){
					dtl.setLocno(locno);
					dtl.setConvertNo(convertNo);
					dtl.setOwnerNo(ownerNo);
					dtl.setEditorName(editorName);
					dtl.setEditor(operator);
					dtl.setEdittm(date);
					
					operand = billConConvertDtlService.modifyById(dtl);
					if(operand <= 0){
						throw new ManagerException("修改明细异常");
					}
				}
			}
			//新增
			if(CommonUtil.hasValue(insertList)){
				String destCellNo = "";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", destLocno);
				List<CmDefcell> list = cmDefcellService.findDestCell4Convert(params);
				if(CommonUtil.hasValue(list)){
					if(list.size() > 1){
						throw new ManagerException("转入仓库【"+destLocno+"】找不到明确的目的储位!");
					}
					destCellNo = list.get(0).getCellNo();
				}
				if(StringUtils.isBlank(destCellNo)){
					throw new ManagerException("转入仓库【"+destLocno+"】找不到符合的目的储位!");
				}
				for(BillConConvertDtl dtl:insertList){
					dtl.setLocno(locno);
					dtl.setConvertNo(convertNo);
					dtl.setOwnerNo(ownerNo);
					dtl.setRealyQty(dtl.getItemQty());
					dtl.setCreator(operator);
					dtl.setCreatorName(creatorName);
					dtl.setCreatetm(date);
					dtl.setEditor(operator);
					dtl.setEditorName(editorName);
					dtl.setEdittm(date);
					dtl.setStatus(BillConvertDtlStatusEnums.STATUS_10.getValue());
					dtl.setDestCellNo(destCellNo);
					//System.out.println(dtl.getLocno()+"	"+dtl.getOwnerNo()+"	"+"	"+dtl.getConvertNo()+"	"+dtl.getCellNo()+"	"+dtl.getItemNo()+"	"+dtl.getSizeNo());
				}
				//批量
				int pageNum = 100;
				for(int idx=0;idx<insertList.size();){
					idx += pageNum;
					if(idx > insertList.size()){
						billConConvertDtlService.batchInsertDtl(insertList.subList(idx-pageNum, insertList.size()));
					}else{
						billConConvertDtlService.batchInsertDtl(insertList.subList(idx-pageNum, idx));
					}
				}
			}
			BillConConvert updatebillConConvert = new BillConConvert();
			updatebillConConvert.setLocno(locno);
			updatebillConConvert.setOwnerNo(ownerNo);
			updatebillConConvert.setConvertNo(convertNo);
			updatebillConConvert.setEditor(operator);
			updatebillConConvert.setEditorName(creatorName);
			updatebillConConvert.setEdittm(new Date());
			billConConvertService.modifyById(updatebillConConvert);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params) {
		return billConConvertDtlService.findSumQty(params);
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
				String convertNo = strs[1];
				String ownerNo = strs[2];
				
				//查询主表信息
				BillConConvert convert = new BillConConvert();
				convert.setLocno(locno);
				convert.setConvertNo(convertNo);
				convert.setOwnerNo(ownerNo);
				convert = this.billConConvertService.findById(convert);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("locno", locno);
				params.put("convertNo", convertNo);
				params.put("ownerNo", ownerNo);
				List<BillConConvertDtlSizeDto> list = this.billConConvertDtlService.findDtl4SizeHorizontal(params,authorityParams);
				if(CommonUtil.hasValue(list)){
					Map<String, Object> main = new HashMap<String, Object>();
					Map<String, BillConConvertDtlSizeDto> itemRowDto = new HashMap<String, BillConConvertDtlSizeDto>();
					List<BillConConvertDtlSizeDto> rows = new ArrayList<BillConConvertDtlSizeDto>();
					List<List<String>> sizeList = new ArrayList<List<String>>();
					Map<String, BigDecimal> sizeCodeQtyMap = null;
					BillConConvertDtlSizeDto temp;
					BigDecimal total = new BigDecimal(0);
					
					Map<String, Map<String, String>> sizeHead = new TreeMap<String, Map<String,String>>();
					Map<String, String> sizeRow = null;
					int sizeColNum = 0;
					for(BillConConvertDtlSizeDto d:list){
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
					for(Entry<String, BillConConvertDtlSizeDto> m : itemRowDto.entrySet()){
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
					main.put("convertNo", convertNo);
					main.put("storeNo", convert.getStoreNo());
					main.put("total", total);
					main.put("list", rows);
					main.put("sizeList", sizeList);
					main.put("sizeColNum", sizeColNum);
					resultList.add(main);
				}else{
					throw new ManagerException("单据："+convertNo+"没有明细!");
				}
			}
			return resultList;
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

}