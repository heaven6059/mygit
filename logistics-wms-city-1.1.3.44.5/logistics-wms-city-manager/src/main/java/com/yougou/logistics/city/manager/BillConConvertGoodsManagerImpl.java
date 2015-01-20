package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.BillConConvert;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillConConvertGoodsKey;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillConConvertDtlService;
import com.yougou.logistics.city.service.BillConConvertGoodsDtlService;
import com.yougou.logistics.city.service.BillConConvertGoodsService;
import com.yougou.logistics.city.service.BillSmWasteDtlService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ProcCommonService;

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
@Service("billConConvertGoodsManager")
class BillConConvertGoodsManagerImpl extends BaseCrudManagerImpl implements BillConConvertGoodsManager {

	@Resource
	private BillConConvertGoodsService billConConvertGoodsService;
	
	@Resource
	private BillConConvertGoodsDtlService billConConvertGoodsDtlService;
	
	@Resource
	private BillConAdjManager billConAdjManager;
	
	@Resource
	private BillConAdjDtlManager billConAdjDtlManager;
	
	@Resource
	private BillConConvertManager billConConvertManager;
	
	@Resource
	private BillConConvertDtlService billConConvertDtlService;
	
	@Resource
	private BillSmWasteManager billSmWasteManager;
	
	@Resource
	private BillSmWasteDtlService billSmWasteDtlService;
	
	@Resource
	private ProcCommonService procCommonService;
	
	@Resource
	private ProcCommonManager procCommonManager;
	
	@Resource
	private CmDefcellService cmDefcellService;

	private final static String CONVERTTYPE0 = "0";

	private final static String CONVERTTYPE1 = "1";

	private final static String CONVERTTYPE2 = "2";
	
	private final static String CONVERTTYPE3 = "3";
	
	private final static String CONVERTTYPE5 = "5";
	
	private final static String STATUS11 = "11";

	private final static String STATUS10 = "10";
	
	private final static String SOURCETYPE1 = "1";
	
	private final static String WASTETYPE04 = "04";

	@Override
	public BaseCrudService init() {
		return billConConvertGoodsService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void deleteConvertGoods(List<BillConConvertGoods> goodsList) throws ManagerException {
		try {
			billConConvertGoodsService.deleteConvertGoods(goodsList);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void auditConvertGoods(String loginName, String userName, List<BillConConvertGoods> goodsList) throws ManagerException {
		try{
			if (!CommonUtil.hasValue(goodsList)) {
				throw new ManagerException("参数非法");
			}
			for (BillConConvertGoods convertGoods : goodsList) {
				
				BillConConvertGoodsKey goodsKey = new BillConConvertGoodsKey();
				goodsKey.setLocno(convertGoods.getLocno());
				goodsKey.setOwnerNo(convertGoods.getOwnerNo());
				goodsKey.setConvertGoodsNo(convertGoods.getConvertGoodsNo());
				BillConConvertGoods g = (BillConConvertGoods)billConConvertGoodsService.findById(goodsKey);
				if(g == null){
					throw new ManagerException("查询转货单主档对象为空!");
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", g.getLocno());
				params.put("ownerNo", g.getOwnerNo());
				params.put("convertGoodsNo", g.getConvertGoodsNo());
				List<BillConConvertGoodsDtl> dtlList = billConConvertGoodsDtlService.findItemDtlByParams(params);
				if(!CommonUtil.hasValue(dtlList)){
					throw new ManagerException(g.getConvertGoodsNo()+"明细为空,不能审核");
				}
				
				List<BillConConvertGoodsDtl> checkList = billConConvertGoodsDtlService.findCheckContent4Convert(params);
				if(CommonUtil.hasValue(checkList)){
					String checkNo = checkList.get(0).getCheckNo();
					String itemNo = checkList.get(0).getItemNo();
					String sizeNo = checkList.get(0).getSizeNo();
					StringBuffer sb = new StringBuffer();
					sb.append("转货单："+g.getConvertGoodsNo()+",");
					sb.append("验收单："+checkNo+",");
					sb.append("商品："+itemNo+",");
					sb.append("尺码："+sizeNo+",");
					throw new ManagerException(sb.toString()+"可移的库存不足!");
				}
				
				//1.添加单据信息
				g.setCreator(loginName);
				if (CONVERTTYPE0.equals(g.getConvertType())||CONVERTTYPE5.equals(g.getConvertType())) {
					addBillConAdj(g,dtlList);
				}
				//2.如果是部门转货或者是跨部门转店进入此处
				if (CONVERTTYPE1.equals(g.getConvertType())||CONVERTTYPE3.equals(g.getConvertType())) {
					addBillConConvert(g, dtlList);
				}
				//3.门店转货
				if (CONVERTTYPE2.equals(g.getConvertType())) {
					addBillSmWaste(g, dtlList);
				}
				
				//2.更新审核状态
				BillConConvertGoods goods = new BillConConvertGoods();
				goods.setLocno(g.getLocno());
				goods.setOwnerNo(g.getOwnerNo());
				goods.setConvertGoodsNo(g.getConvertGoodsNo());
				goods.setAuditor(loginName);
				goods.setAuditorName(userName);
				goods.setAudittm(new Date());
				goods.setEditor(loginName);
				goods.setEditorName(userName);
				goods.setEdittm(new Date());
				goods.setStatus(STATUS11);
				goods.setUpdStatus(STATUS10);
				int count = billConConvertGoodsService.modifyById(goods);
				if(count < 1){
					//throw new ManagerException(g.getConvertGoodsNo()+"审核失败!");
					throw new ManagerException("单据"+g.getConvertGoodsNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");					
				}
				
				//3.更新转货单下所有的箱状态为3-已出库
				billConConvertGoodsDtlService.batchUpdateBoxStatus4Container(params);
				
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
		
	}
	
	/**
	 * 添加库存调整单
	 * @param dtlList
	 * @throws ManagerException 
	 * @throws ServiceException 
	 */
	public void addBillConAdj(BillConConvertGoods convertGoods,List<BillConConvertGoodsDtl> dtlList) throws ManagerException, ServiceException{
		//新增主档
		String locno = convertGoods.getLocno();
		String ownerNo = convertGoods.getOwnerNo();
		String convertGoodsNo = convertGoods.getConvertGoodsNo();
		String loginName = convertGoods.getCreator();
		String adjNo = procCommonManager.procGetSheetNo(convertGoods.getLocno(), CNumPre.SM_ADJ_PRE);
		BillConAdj adj = new BillConAdj();
		adj.setLocno(locno);
		adj.setOwnerNo(ownerNo);
		adj.setAdjNo(adjNo);
		if(CONVERTTYPE5.equals(convertGoods.getConvertType())){
			adj.setAdjType("1");
			adj.setsItemType(convertGoods.getsQuality());
			adj.setdItemType(convertGoods.getdQuality());
		}else{
			adj.setAdjType("0");
			adj.setsItemType("0");
			adj.setdItemType("A");
		}
		adj.setStatus(STATUS10);
		adj.setCreator(loginName);
		adj.setCreatetm(new Date());
		adj.setSourceNo(convertGoodsNo);
		adj.setSourceType(SOURCETYPE1);
		int count = billConAdjManager.add(adj);
		if(count < 1){
			throw new ManagerException("新增库存调整主档失败！");
		}
		
		//新增明细
		List<BillConAdjDtl> adjDtlList = new ArrayList<BillConAdjDtl>();
		short rowId = 0;
		for (BillConConvertGoodsDtl gd : dtlList) {
			BillConAdjDtl adjDtl = new BillConAdjDtl();
			adjDtl.setLocno(gd.getLocno());
			adjDtl.setOwnerNo(gd.getOwnerNo());
			adjDtl.setAdjNo(adjNo);
			adjDtl.setItemNo(gd.getItemNo());
			adjDtl.setSizeNo(gd.getSizeNo());
			adjDtl.setPackQty(new BigDecimal(1));
			adjDtl.setSupplierNo(gd.getSupplierNo());
			adjDtl.setQuality(gd.getQuality());
			adjDtl.setCellNo(gd.getsCellNo());
			adjDtl.setPlanQty(new BigDecimal(0));
			adjDtl.setAdjQty(gd.getRealQty());
			adjDtl.setRealQty(new BigDecimal(0));
			adjDtl.setRowId(++rowId);
			adjDtl.setBarcode(gd.getBarcode());
			adjDtl.setItemType(gd.getItemType());
			adjDtl.setBrandNo(gd.getBrandNo());
			adjDtl.setdCellNo(gd.getdCellNo());
			adjDtlList.add(adjDtl);
		}
		
		for (BillConConvertGoodsDtl gd : dtlList) {
			//解锁库存 手工移库标识,1：可手工移库
			procCommonService.UpdateContentStatus(locno, gd.getCellId().toString(), null, null, null, "0", loginName);
		}
		
		//批量插入
		int pageNum = 100;
		for(int idx=0;idx<adjDtlList.size();){
			idx += pageNum;
			if(idx > adjDtlList.size()){
				billConAdjDtlManager.batchInsertDtl4ConvertGoods(adjDtlList.subList(idx-pageNum, adjDtlList.size()));
			}else{
				billConAdjDtlManager.batchInsertDtl4ConvertGoods(adjDtlList.subList(idx-pageNum, idx));
			}
		}
		//审核库存调整单
		String ids = locno+"-"+ownerNo+"-"+adjNo;
		billConAdjDtlManager.examineAdj(ids, adj.getCreator(),convertGoods.getCreatorName(), null);
	}
	
	//添加跨部门转货
	public void addBillConConvert(BillConConvertGoods convertGoods,List<BillConConvertGoodsDtl> dtlList) throws ManagerException, ServiceException{
		//新增主档
		String convertNo = procCommonManager.procGetSheetNo(convertGoods.getLocno(), CNumPre.CON_CONVERT_PRE);
		String locno = convertGoods.getLocno();
		String ownerNo = convertGoods.getOwnerNo();
		String convertGoodsNo = convertGoods.getConvertGoodsNo();
		String storeNo = convertGoods.getStoreNo();
		String dLocno = convertGoods.getdLocno();
		String loginName = convertGoods.getCreator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(CommonUtil.getCurrentDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		BillConConvert convert = new BillConConvert();
		convert.setLocno(locno);
		convert.setOwnerNo(ownerNo);
		convert.setConvertNo(convertNo);
		convert.setConvertType("01");
		convert.setStoreNo(dLocno);
		convert.setStatus(STATUS10);
		convert.setCreator(loginName);
		convert.setCreatetm(new Date());
		convert.setSourceNo(convertGoodsNo);
		convert.setSourceType(SOURCETYPE1);
		convert.setConvertDate(d);
		int count = billConConvertManager.add(convert);
		if(count < 1){
			throw new ManagerException("新增跨部门转货主档失败！");
		}
		
		//获取目的仓库的储位
		String destCellNo = "";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locno", dLocno);
		List<CmDefcell> list = cmDefcellService.findDestCell4Convert(params);
		if(CommonUtil.hasValue(list)){
			if(list.size() > 1){
				throw new ManagerException("转入仓库【"+dLocno+"】找不到明确的目的储位!");
			}
			destCellNo = list.get(0).getCellNo();
		}
		if(StringUtils.isBlank(destCellNo)){
			throw new ManagerException("转入仓库【"+dLocno+"】找不到符合的目的储位!");
		}
	
		//新增明细
		List<BillConConvertDtl> convertDtlList = new ArrayList<BillConConvertDtl>();
		for (BillConConvertGoodsDtl gd : dtlList) {
			BillConConvertDtl convertDtl = new BillConConvertDtl();
			convertDtl.setLocno(gd.getLocno());
			convertDtl.setOwnerNo(gd.getOwnerNo());
			convertDtl.setConvertNo(convertNo);	
			convertDtl.setCellNo(gd.getsCellNo());
			convertDtl.setItemNo(gd.getItemNo());
			convertDtl.setSizeNo(gd.getSizeNo());
			convertDtl.setItemType(gd.getItemType());
			convertDtl.setQuality(gd.getQuality());
			convertDtl.setBrandNo(gd.getBrandNo());
			convertDtl.setSupplierNo(gd.getSupplierNo());
			convertDtl.setItemQty(gd.getRealQty());
			convertDtl.setRealyQty(gd.getRealQty());
			convertDtl.setStatus(STATUS10);
			convertDtl.setCreator(loginName);
			convertDtl.setCreatetm(new Date());
			convertDtl.setEditor(loginName);
			convertDtl.setEdittm(new Date());
			convertDtl.setRemark(gd.getRemark());
			convertDtl.setCellId(Long.valueOf(gd.getCellId().toString()));
			convertDtl.setDestCellNo(destCellNo);
			convertDtlList.add(convertDtl);
		}
				
		//批量插入
		int pageNum = 100;
		for(int idx=0;idx<convertDtlList.size();){
			idx += pageNum;
			if(idx > convertDtlList.size()){
				billConConvertDtlService.batchInsertDtl(convertDtlList.subList(idx-pageNum, convertDtlList.size()));
			}else{
				billConConvertDtlService.batchInsertDtl(convertDtlList.subList(idx-pageNum, idx));
			}
		}
		
		//审核跨部门转货单
		int type = 0;
		if(CONVERTTYPE3.equals(convertGoods.getConvertType())){
			type = 1;
		}
		String ids = locno+"_"+ownerNo+"_"+convertNo+"_"+dLocno;
		billConConvertManager.check(ids, loginName, type, storeNo,convertGoods.getCreatorName());
	}
	
	//添加直接出库
	public void addBillSmWaste(BillConConvertGoods convertGoods,List<BillConConvertGoodsDtl> dtlList)throws ManagerException, ServiceException{
		//新增主档
		String wasteNo = procCommonManager.procGetSheetNo(convertGoods.getLocno(), CNumPre.SM_WASTE_DIRECT_PRE);
		String locno = convertGoods.getLocno();
		String ownerNo = convertGoods.getOwnerNo();
		String convertGoodsNo = convertGoods.getConvertGoodsNo();
		String storeNo = convertGoods.getStoreNo();
		String loginName = convertGoods.getCreator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(CommonUtil.getCurrentDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		BillSmWaste waste = new BillSmWaste();
		waste.setLocno(locno);
		waste.setOwnerNo(ownerNo);
		waste.setWasteNo(wasteNo);
		waste.setWasteType(WASTETYPE04);
		waste.setWasteDate(d);
		waste.setStoreNo(storeNo);
		waste.setStatus(STATUS10);
		waste.setCreator(loginName);
		waste.setCreatetm(new Date());
		waste.setSourceNo(convertGoodsNo);
		waste.setSourceType(SOURCETYPE1);
		waste.setOperator(loginName);
		int count = billSmWasteManager.add(waste);
		if(count < 1){
			throw new ManagerException("新增门店转货主档失败！");
		}
			
		//新增明细
		short rowId = 0;
		List<BillSmWasteDtl> wasteDtlList = new ArrayList<BillSmWasteDtl>();
		for (BillConConvertGoodsDtl gd : dtlList) {
			BillSmWasteDtl wasteDtl = new BillSmWasteDtl();
			wasteDtl.setLocno(gd.getLocno());
			wasteDtl.setOwnerNo(gd.getOwnerNo());
			wasteDtl.setWasteNo(wasteNo);	
			wasteDtl.setRowId(++rowId);
			wasteDtl.setItemNo(gd.getItemNo());
			wasteDtl.setSizeNo(gd.getSizeNo());
			wasteDtl.setItemType(gd.getItemType());
			wasteDtl.setQuality(gd.getQuality());
			wasteDtl.setProduceDate(new Date());
			wasteDtl.setWasteQty(gd.getRealQty());
			wasteDtl.setOutstockQty(new BigDecimal(0));
			wasteDtl.setRealQty(new BigDecimal(0));
			wasteDtl.setCellNo(gd.getsCellNo());
			wasteDtl.setCellId(Long.valueOf(gd.getCellId().toString()));
			wasteDtl.setBrandNo(gd.getBrandNo());
			wasteDtlList.add(wasteDtl);
		}
						
		//批量插入
		int pageNum = 100;
		for(int idx=0;idx<wasteDtlList.size();){
			idx += pageNum;
			if(idx > wasteDtlList.size()){
				billSmWasteDtlService.batchInsertDtl(wasteDtlList.subList(idx-pageNum, wasteDtlList.size()));
			}else{
				billSmWasteDtlService.batchInsertDtl(wasteDtlList.subList(idx-pageNum, idx));
			}
		}
				
		//审核跨部门转货单
		String ids = locno+"-"+ownerNo+"-"+wasteNo;
		billSmWasteManager.checkBillSmWaste(ids, loginName,null,null, CNumPre.SM_WASTE_DIRECT_PRE);
	}
}