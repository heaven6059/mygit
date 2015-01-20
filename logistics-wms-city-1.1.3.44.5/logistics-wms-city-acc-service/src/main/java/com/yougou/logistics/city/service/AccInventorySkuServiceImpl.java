package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AccInventorySku;
import com.yougou.logistics.city.common.model.AccInventorySkuBook;
import com.yougou.logistics.city.common.model.BillImCheck;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.dal.database.AccInventorySkuBookMapper;
import com.yougou.logistics.city.dal.database.AccInventorySkuMapper;
import com.yougou.logistics.city.dal.mapper.BillImCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImCheckMapper;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-11 15:24:23
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
@Service("accInventorySkuService")
class AccInventorySkuServiceImpl extends BaseCrudServiceImpl implements AccInventorySkuService {
	@Resource
    private AccInventorySkuMapper accInventorySkuMapper;
    @Resource
	private BillImCheckMapper billImCheckMapper;
	@Resource
	private BillImCheckDtlMapper billImCheckDtlMapper;
	@Resource
	private AccInventorySkuBookMapper accInventorySkuBookMapper;

    @Override
    public BaseCrudMapper init() {
        return accInventorySkuMapper;
    }
    

	@Override
	public void addSkuConContentStore(Map<String, Object> params) throws ServiceException {
		try {
			String checkNo=params.get("checkNo").toString();
			String cellId=params.get("cellId").toString();
			String cellNo="testcellNo";
			String locno = "",ownerNo= "",itemType="",itemNo="",quality="";
			List<AccInventorySkuBook> listskubook=new ArrayList<AccInventorySkuBook>();
			
			//查询商品明细
			Map<String, Object> params1=new HashMap<String, Object>();
			params1.put("checkNo", checkNo);
			List<BillImCheckDtl> listBDtl=billImCheckDtlMapper.selectByParams(new BillImCheckDtl(), params1);
			AccInventorySkuBook skubook=null;
			int i=1;
			double qty = 0.0;
			Date curDate=null;
			for(BillImCheckDtl tmp:listBDtl){
				skubook=new AccInventorySkuBook();
				curDate=new Date();
				skubook.setCellNo(cellId);
				skubook.setCellId(Long.valueOf(cellId));
				skubook.setLocno(tmp.getLocno());
				skubook.setBarcode(tmp.getBarcode());
				skubook.setItemNo(tmp.getItemNo());
				skubook.setItemType(tmp.getItemType());
				skubook.setQuality(tmp.getQuality());
				skubook.setMoveQty(tmp.getCheckQty());
				skubook.setBalanceQty(tmp.getCheckQty());
				skubook.setOwnerNo(tmp.getOwnerNo());
				skubook.setDirection(Long.valueOf(1));
				skubook.setBillNo(checkNo);
				skubook.setBillType("SC");
				skubook.setIoFlag("I");
				skubook.setPreFlag("0");
				skubook.setSeqId(BigDecimal.valueOf((i++)));
				skubook.setCreator("");
				skubook.setCreatedt(curDate);
				skubook.setCreatetm(curDate);
				qty+=skubook.getMoveQty().doubleValue();
				locno=tmp.getLocno();
				ownerNo=tmp.getOwnerNo();
				itemType=tmp.getItemType();
				itemNo=tmp.getItemNo();
				quality=tmp.getQuality();
				//保存流水账
				listskubook.add(skubook);
			}
			accInventorySkuBookMapper.batchInsertSkuBook(listskubook);
			
			//查询验收单
			BillImCheck billImCheck=new BillImCheck();
			billImCheck.setCheckNo(checkNo);
			billImCheck.setLocno(locno);
			billImCheck.setOwnerNo(ownerNo);
			BillImCheck tmp=billImCheckMapper.selectByPrimaryKey(billImCheck);
			if(tmp!=null){
				AccInventorySku accsku=new AccInventorySku();
				accsku.setCellNo(cellNo);
				accsku.setLocno(tmp.getLocno());
				accsku.setBarcode("testBarcode");
				accsku.setItemNo(itemNo);
				accsku.setItemType(itemType);
				accsku.setQty(BigDecimal.valueOf(qty));
				accsku.setOutstockQty(BigDecimal.valueOf(0));
				accsku.setInstockQty(BigDecimal.valueOf(0));
				accsku.setStatus("0");
				accsku.setFlag("0");
				accsku.setHmManualFlag("0");
				accsku.setQuality(quality);
				accsku.setOwnerNo(ownerNo);
				accsku.setCreator(tmp.getCreator());
				accsku.setCreatetm(new Date());
				//总账
				accInventorySkuMapper.insert(accsku);
			}
		} catch (Exception e) {
			throw new ServiceException("addSkuConContentStore exception.",e);
		}
	}


	@Override
	public int updateAccInventorySku(AccInventorySku accInventorySku)
			throws ServiceException {
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("locno", accInventorySku.getLocno());
		params.put("ownerNo", accInventorySku.getOwnerNo());
		params.put("itemNo", accInventorySku.getItemNo());
		params.put("barcode", accInventorySku.getBarcode());
		params.put("cellId", accInventorySku.getCellId());
		params.put("quality", accInventorySku.getQuality());
		params.put("itemType", accInventorySku.getItemType());
		
		//更新sku库存
		 List<AccInventorySku>  list=accInventorySkuMapper.selectByParams(new AccInventorySku(), params);
		if(list.size()>0){
			AccInventorySku sku=list.get(0);
			sku.setQty(sku.getQty().add(accInventorySku.getQty()));
			accInventorySkuMapper.updateByPrimaryKey(sku);
		}
		else{
			accInventorySkuMapper.insert(accInventorySku);
		}
		return 1;
	}


	@Override
	public List<AccInventorySku> selectByConNoCellNoParams(
			Map<String, Object> params) throws ServiceException {
		return accInventorySkuMapper.selectByConNoCellNoParams(params);
	}
}