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
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.BillOmDivideDifFlagEnums;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDifferent;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentDtl;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmDivideKey;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillImReceiptDtlService;
import com.yougou.logistics.city.service.BillImReceiptService;
import com.yougou.logistics.city.service.BillOmDivideDifferentDtlService;
import com.yougou.logistics.city.service.BillOmDivideDifferentService;
import com.yougou.logistics.city.service.BillOmDivideDtlService;
import com.yougou.logistics.city.service.BillOmDivideService;
import com.yougou.logistics.city.service.ConBoxDtlService;
import com.yougou.logistics.city.service.ConContentService;
import com.yougou.logistics.city.service.ProcCommonService;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-10-14 14:35:45
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
@Service("billOmDivideDifferentManager")
class BillOmDivideDifferentManagerImpl extends BaseCrudManagerImpl implements BillOmDivideDifferentManager {
	
	@Resource
	private BillOmDivideDifferentService billOmDivideDifferentService;

	@Resource
	private BillOmDivideDifferentDtlService billOmDivideDifferentDtlService;
	
	@Resource
	private BillOmDivideService billOmDivideService;
	
	@Resource
	private BillOmDivideDtlService billOmDivideDtlService;
	
	@Resource
	private ProcCommonService procCommonService;
	
	@Resource
	private ConContentService conContentService;
	
	@Resource
	private BillImReceiptDtlService billImReceiptDtlService;
	
	@Resource
    private BillAccControlService billAccControlService;
	
	@Resource
	private BillImReceiptService billImReceiptService;
	
	@Resource
	private ConBoxDtlService conBoxDtlService;
	
	private final static String DIFFERENTTYPE1 = "1";
	
	private final static String businessType1 = "1";
	
	private final static String STATUS10 = "10";
	
	private final static String STATUS11 = "11";
	
	private final static String STATUS13 = "13";
	
	private final static String STATUS14 = "14";
	
	private final static String STATUS91 = "91";
	
	private final static String STATUS40 = "40";
	
	private final static int DIFSTATUS_0 = 0;
	
	private final static int DIFSTATUS_1 = 1;
	
	@Override
	public BaseCrudService init() {
		return billOmDivideDifferentService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void toDivideDifferent(List<BillOmDivideDtl> divideDtlList, String loginName,String userName) throws ManagerException {
		try{
			if(!CommonUtil.hasValue(divideDtlList)){
				throw new ManagerException("请选择分货单明细数据!");
			}
			
			//1.准备验证数据
			BillOmDivideDtl divideDtl = divideDtlList.get(0);
			String locno = divideDtl.getLocno();
			String ownerNo = divideDtl.getOwnerNo();
			String divideNo = divideDtl.getDivideNo();
			
			//验证分货单状态
			BillOmDivideKey divideKey = new BillOmDivideKey();
			divideKey.setLocno(locno);
			divideKey.setDivideNo(divideNo);
			BillOmDivide divide = (BillOmDivide)billOmDivideService.findById(divideKey);
			if(divide == null){
				throw new ManagerException("分货单数据不存在!");
			}
			if(!businessType1.equals(divide.getBusinessType())){
				throw new ManagerException("只能操作库存分货的单据!");
			}
			if(!STATUS40.equals(divide.getStatus())){
				throw new ManagerException("只能操作<部分复核>状态的单据!");
			}
			
			//准备查询验证分货单明细数据
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("ownerNo", ownerNo);
			params.put("divideNo", divideNo);
			List<BillOmDivideDtl> checkList = billOmDivideDtlService.findDivideDtl4Different(params);
			if(!CommonUtil.hasValue(checkList)){
				throw new ManagerException("验证分货单明细数据为空!");
			}
			
			//验证状态
			Map<String, BillOmDivideDtl> checkMap = new HashMap<String, BillOmDivideDtl>();
			for (BillOmDivideDtl billOmDivideDtl : checkList) {
				StringBuffer sb = new StringBuffer();
				sb.append(billOmDivideDtl.getLocno()+"|");
				sb.append(billOmDivideDtl.getOwnerNo()+"|");
				sb.append(billOmDivideDtl.getDivideNo()+"|");
				sb.append(billOmDivideDtl.getDivideId());
				String key = sb.toString();
				if(checkMap.get(key) == null){
					checkMap.put(key, billOmDivideDtl);
				}
			}
			//验证箱号取储位
			Map<String, String> boxMap = new HashMap<String, String>();
			List<BillOmDivideDtl> boxDivideDtl = billOmDivideDtlService.findDivideDtlNotRecheckBox(params);
			for (BillOmDivideDtl billOmDivideDtl : boxDivideDtl) {
				String key = billOmDivideDtl.getBoxNo();
				if(boxMap.get(key) == null){
					boxMap.put(key, key);
				}
			}
			
			//2.开始验证明细是否被转商品差异调整
			List<BillOmDivideDtl> dataDtlList = new ArrayList<BillOmDivideDtl>();
			for (BillOmDivideDtl billOmDivideDtl : divideDtlList) {
				StringBuffer sb = new StringBuffer();
				sb.append(billOmDivideDtl.getLocno()+"|");
				sb.append(billOmDivideDtl.getOwnerNo()+"|");
				sb.append(billOmDivideDtl.getDivideNo()+"|");
				sb.append(billOmDivideDtl.getDivideId());
				String key = sb.toString();
				if(checkMap.get(key) != null){
					BillOmDivideDtl mapDivideDtl = checkMap.get(key);
					String status = mapDivideDtl.getStatus();
					if(STATUS14.equals(status)){
						StringBuffer sbmsg = new StringBuffer();
						sbmsg.append("箱号："+mapDivideDtl.getBoxNo()+",");
						sbmsg.append("商品："+mapDivideDtl.getItemNo()+",");
						sbmsg.append("尺码："+mapDivideDtl.getSizeNo());
						sbmsg.append("已经转商品差异调整,状态发生改变!");
						throw new ServiceException(sbmsg.toString());
					}
					dataDtlList.add(mapDivideDtl);
				}
			}
			if(!CommonUtil.hasValue(dataDtlList)){
				throw new ManagerException("分货单明细数据为空!");
			}
			
			//3.开始生成差异调整单单据数据
			//添加主档
			int count = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = null;
			try {
				d = sdf.parse(CommonUtil.getCurrentDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String defferentNo = procCommonService.procGetSheetNo(locno, CNumPre.OM_DIVIDE_DIFFERENT_PRE);
			BillOmDivideDifferent different = new BillOmDivideDifferent();
			Date date = new Date();
			different.setLocno(locno);
			different.setOwnerNo(ownerNo);
			different.setDifferentNo(defferentNo);
			different.setOperateDate(d);
			different.setDifferentType(DIFFERENTTYPE1);
			different.setStatus(STATUS10);
			different.setCreator(loginName);
			different.setCreatorName(userName);
			different.setCreatetm(date);
			different.setEditor(loginName);
			different.setEditorName(userName);
			different.setEdittm(date);
			different.setSourceNo(divideNo);
			count = billOmDivideDifferentService.add(different);
			if(count < 1){
				throw new ServiceException("新增差异调整单数据失败!");
			}
			
			//添加明细
			//获取进货暂存区储位编码
			int serialNo = 0;
			List<BillOmDivideDifferentDtl> batchList = new ArrayList<BillOmDivideDifferentDtl>();
			for (BillOmDivideDtl dtl : dataDtlList) {
				String cellNo = "";
				Long cellId = 0L;
				BigDecimal difQty = new BigDecimal(dtl.getDiffQty());
				cellNo = dtl.getsCellNo();
				cellId = dtl.getsCellId();
				
				//验证调整的是否已经复核完成了
				if(difQty.intValue() <= 0){
					StringBuffer sbmsg = new StringBuffer();
					sbmsg.append("客户："+dtl.getStoreNo()+",");
					sbmsg.append("箱号："+dtl.getBoxNo()+",");
					sbmsg.append("商品："+dtl.getItemNo()+",");
					sbmsg.append("尺码："+dtl.getSizeNo());
					sbmsg.append("可调整数量不足,请检查是否装箱完成!");
					throw new ServiceException(sbmsg.toString());
				}
				
				/**注释：改成部分复核做差异调整单,所有商品还在原储位**/
				//如果有箱号没有做分货复核,表示还是在原来的储位
//				String cellNoJh = procCommonService.getSpecailCellNo(locno,"1",dtl.getQuality(),"1","1",dtl.getItemType());
//				if(boxMap.get(dtl.getBoxNo()) != null){
//					cellNo = dtl.getsCellNo();
//					cellId = dtl.getsCellId();
//				}else{
//					Map<String,Object> conParams = new HashMap<String, Object>();
//					conParams.put("locno", dtl.getLocno());
//					conParams.put("ownerNo", dtl.getOwnerNo());
//					conParams.put("cellNo", cellNoJh);
//					conParams.put("itemNo", dtl.getItemNo());
//					conParams.put("sizeNo", dtl.getSizeNo());
//					conParams.put("itemType", dtl.getItemType());
//					conParams.put("quality", dtl.getQuality());
//					conParams.put("status", "0");
//					conParams.put("hmManualFlag", "1");
//					List<ConContentDto> conList = conContentService.findViewByParams(conParams);
//					if(!CommonUtil.hasValue(conList)){
//						StringBuffer sbmsg = new StringBuffer();
//						sbmsg.append("箱号："+dtl.getBoxNo()+",");
//						sbmsg.append("储位："+cellNoJh+",");
//						sbmsg.append("商品："+dtl.getItemNo()+",");
//						sbmsg.append("尺码："+dtl.getSizeNo());
//						sbmsg.append("没有查询到库存数据!");
//						throw new ServiceException(sbmsg.toString());
//					}
//					ConContentDto contentDto = conList.get(0);
////					int qty = contentDto.getQty().intValue()-contentDto.getOutstockQty().intValue();
////					int difQtyCon = difQty.intValue();
////					if(qty == 0 ||difQtyCon > qty){
////						StringBuffer sbmsg = new StringBuffer();
////						sbmsg.append("箱号："+dtl.getBoxNo()+",");
////						sbmsg.append("储位："+cellNoJh+",");
////						sbmsg.append("商品："+dtl.getItemNo()+",");
////						sbmsg.append("尺码："+dtl.getSizeNo());
////						sbmsg.append("库存不足!");
////						throw new ServiceException(sbmsg.toString());
////					}
//					cellId = contentDto.getCellId();
//					cellNo = contentDto.getCellNo();
//				}
				++serialNo;
				BillOmDivideDifferentDtl differentDtl = new BillOmDivideDifferentDtl();
				differentDtl.setLocno(locno);
				differentDtl.setOwnerNo(ownerNo);
				differentDtl.setDifferentNo(defferentNo);
				differentDtl.setOperateDate(d);
				differentDtl.setStoreNo(dtl.getStoreNo());
				differentDtl.setsItemNo(dtl.getItemNo());
				differentDtl.setPackQty(new BigDecimal(1));
				differentDtl.setItemQty(difQty);
				differentDtl.setRealQty(new BigDecimal(0));
				differentDtl.setsCellNo(cellNo);
				differentDtl.setsCellId(cellId);
				differentDtl.setdCellNo(cellNo);
				differentDtl.setdCellId(0L);
				differentDtl.setStatus(STATUS10);
				differentDtl.setBoxNo(dtl.getBoxNo());
				differentDtl.setBrandNo(dtl.getBrandNo());
				differentDtl.setsBarcode(dtl.getBarcode());
				differentDtl.setPixFlag(new BigDecimal(0));
				differentDtl.setsSizeNo(dtl.getSizeNo());
				differentDtl.setSupplierNo(dtl.getSupplierNo());
				differentDtl.setItemType(dtl.getItemType());
				differentDtl.setQuality(dtl.getQuality());
				differentDtl.setSerialNo(new BigDecimal(serialNo));
				differentDtl.setExpNo(dtl.getExpNo());
				differentDtl.setdItemNo("N");
				differentDtl.setdBarcode("N");
				count = billOmDivideDifferentDtlService.add(differentDtl);
				if(count < 1){
					throw new ServiceException("新增差异调整单明细数据失败!");
				}
				
				//更新分货单明细状态为14已转商品差异调整
				BillOmDivideDtl updateDivideDtl = new BillOmDivideDtl();
				updateDivideDtl.setLocno(dtl.getLocno());
				updateDivideDtl.setOwnerNo(dtl.getOwnerNo());
				updateDivideDtl.setDivideNo(dtl.getDivideNo());
				updateDivideDtl.setDivideId(dtl.getDivideId());
				updateDivideDtl.setStatus(STATUS14);
				count = billOmDivideDtlService.modifyById(updateDivideDtl);
				if(count < 1){
					throw new ServiceException("更新分货单明细状态失败!");
				}
				batchList.add(differentDtl);
			}
			
//			//4.开始锁定预下库存数据
//			int x = 1;
//			for (BillOmDivideDifferentDtl bodd : batchList) {
//				//锁定预下数量
//				int dNum = x++;
//				BillAccControlDto controlDtoIn = new BillAccControlDto();
//				controlDtoIn.setiLocno(bodd.getLocno());
//				controlDtoIn.setiOwnerNo(ownerNo);
//				controlDtoIn.setiPaperNo(bodd.getDifferentNo());
//				controlDtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
//				controlDtoIn.setiIoFlag("I");
//				controlDtoIn.setiCreator(loginName);
//				controlDtoIn.setiRowId(new BigDecimal(dNum));
//				controlDtoIn.setiCellNo(bodd.getsCellNo());
//				controlDtoIn.setiCellId(new BigDecimal(bodd.getsCellId()));
//				controlDtoIn.setiItemNo(bodd.getsItemNo());
//				controlDtoIn.setiSizeNo(bodd.getsSizeNo());
//				controlDtoIn.setiPackQty(bodd.getPackQty());
//				controlDtoIn.setiSupplierNo(bodd.getSupplierNo());
//				controlDtoIn.setiOutstockQty(bodd.getItemQty());
//				controlDtoIn.setiItemType(bodd.getItemType());
//				controlDtoIn.setiQuality(bodd.getQuality());
//				
//				/**默认值**/
//				controlDtoIn.setiQty(new BigDecimal(0));
//				controlDtoIn.setiInstockQty(new BigDecimal(0));
//				controlDtoIn.setiStatus("0");
//				controlDtoIn.setiFlag("0");
//				controlDtoIn.setiHmManualFlag("1");
//				controlDtoIn.setiTerminalFlag("1");
//				billAccControlService.procAccPrepareDataExt(controlDtoIn);
//				
//				//调用外部存储过程
//				BillAccControlDto dtoIn = new BillAccControlDto();
//				dtoIn.setiPaperNo(bodd.getDifferentNo());
//				dtoIn.setiLocType("2");
//				dtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
//				dtoIn.setiIoFlag("I");
//				dtoIn.setiPrepareDataExt(new BigDecimal(dNum));
//				dtoIn.setiIsWeb(new BigDecimal(1));
//				billAccControlService.procAccApply(dtoIn);
//			}
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void delDivideDifferent(List<BillOmDivideDifferent> differentList, String loginName) throws ManagerException {
		try{
			if(!CommonUtil.hasValue(differentList)){
				throw new ManagerException("请选择差异调整单!");
			}
			for (BillOmDivideDifferent different : differentList) {
				
				//1.开始删除主单
				int count = 0;
				different.setCheckStatus(STATUS10);
				count = billOmDivideDifferentService.deleteById(different);
				if(count < 1){
					throw new ManagerException("单据"+different.getDifferentNo()+"已删除或状态已改变!");
				}
				
				//2.查询差异单的所有明细
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", different.getLocno());
				params.put("differentNo", different.getDifferentNo());
				//4.回写分货单明细状态为建单
				if(DIFFERENTTYPE1.equals(different.getDifferentType())){
					List<BillOmDivideDifferentDtl> differentDtl=billOmDivideDifferentDtlService.findDifferentDtlGroupByItem(params);
					for (BillOmDivideDifferentDtl dtl : differentDtl) {
						BillOmDivideDtl divideDtl = new BillOmDivideDtl();
						divideDtl.setLocno(different.getLocno());
						divideDtl.setDivideNo(different.getSourceNo());
						divideDtl.setStoreNo(dtl.getStoreNo());
						divideDtl.setBoxNo(dtl.getBoxNo());
						divideDtl.setItemNo(dtl.getsItemNo());
						divideDtl.setSizeNo(dtl.getsSizeNo());
						divideDtl.setExpNo(dtl.getExpNo());
						divideDtl.setStatus(STATUS10);
						int dcount = billOmDivideDtlService.updateDivideDtl4Different(divideDtl);
						if(dcount < 1){
							StringBuffer sbmsg = new StringBuffer();
							sbmsg.append("单据："+different.getDifferentNo()+",");
							sbmsg.append("箱号："+dtl.getBoxNo()+",");
							sbmsg.append("商品："+dtl.getsItemNo()+",");
							sbmsg.append("条码："+dtl.getsBarcode());
							sbmsg.append("回写分货单明细状态失败!");
							throw new ServiceException(sbmsg.toString());
						}
					}
				}else{
					List<BillOmDivideDifferentDtl> dtlList = billOmDivideDifferentDtlService.findByBiz(null, params);
					//3.开始释放明细的预下库存
					if(CommonUtil.hasValue(differentList)){
						for (BillOmDivideDifferentDtl differentDtl : dtlList) {
							//扣减预下数量
							BillAccControlDto controlDtoIn = new BillAccControlDto();
							controlDtoIn.setiLocno(differentDtl.getLocno());
							controlDtoIn.setiOwnerNo(differentDtl.getOwnerNo());
							controlDtoIn.setiPaperNo(differentDtl.getDifferentNo());
							controlDtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
							controlDtoIn.setiIoFlag("O");
							controlDtoIn.setiCreator(loginName);
							controlDtoIn.setiRowId(differentDtl.getSerialNo());
							controlDtoIn.setiCellNo(differentDtl.getsCellNo());
							controlDtoIn.setiCellId(new BigDecimal(differentDtl.getsCellId()));
							controlDtoIn.setiItemNo(differentDtl.getsItemNo());
							controlDtoIn.setiSizeNo(differentDtl.getsSizeNo());
							controlDtoIn.setiPackQty(differentDtl.getPackQty());
							controlDtoIn.setiSupplierNo(differentDtl.getSupplierNo());
							controlDtoIn.setiOutstockQty(new BigDecimal(0).subtract(differentDtl.getItemQty()));
							controlDtoIn.setiItemType(differentDtl.getItemType());
							controlDtoIn.setiQuality(differentDtl.getQuality());
							
							/**默认值**/
							controlDtoIn.setiQty(new BigDecimal(0));
							controlDtoIn.setiInstockQty(new BigDecimal(0));
							controlDtoIn.setiStatus("0");
							controlDtoIn.setiFlag("0");
							controlDtoIn.setiHmManualFlag("1");
							controlDtoIn.setiTerminalFlag("1");
							billAccControlService.procAccPrepareDataExt(controlDtoIn);
							
							//调用外部存储过程
							BillAccControlDto dtoIn = new BillAccControlDto();
							dtoIn.setiPaperNo(differentDtl.getDifferentNo());
							dtoIn.setiLocType("2");
							dtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
							dtoIn.setiIoFlag("O");
							dtoIn.setiPrepareDataExt(differentDtl.getSerialNo());
							dtoIn.setiIsWeb(new BigDecimal(1));
							billAccControlService.procAccApply(dtoIn);
						}
					}
				}
				
				//5.开始删除明细
				BillOmDivideDifferentDtl divideDifferentDtl = new BillOmDivideDifferentDtl();
				divideDifferentDtl.setLocno(different.getLocno());
				divideDifferentDtl.setDifferentNo(different.getDifferentNo());
				billOmDivideDifferentDtlService.deleteById(divideDifferentDtl);
				
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void auditDivideDifferent(List<BillOmDivideDifferent> differentList, String loginName,String userName)
			throws ManagerException {
		try{
			if(!CommonUtil.hasValue(differentList)){
				throw new ManagerException("请选择差异调整单!");
			}
			for (BillOmDivideDifferent different : differentList) {
				
				//1.开始审核主单
				int count = 0;
				different.setCheckStatus(STATUS10);
				different.setStatus(STATUS11);
				different.setAuditor(loginName);
				different.setAuditorName(userName);
				different.setAudittm(new Date());
				count = billOmDivideDifferentService.modifyById(different);
				if(count < 1){
					throw new ManagerException("单据"+different.getDifferentNo()+"已删除或状态已改变!");
				}
				
				//2.更改明细状态
				BillOmDivideDifferentDtl divideDifferentDtl = new BillOmDivideDifferentDtl();
				divideDifferentDtl.setLocno(different.getLocno());
				divideDifferentDtl.setDifferentNo(different.getDifferentNo());
				divideDifferentDtl.setStatus(STATUS11);
				billOmDivideDifferentDtlService.modifyById(divideDifferentDtl);
				
				//2.查询差异单的所有明细
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", different.getLocno());
				params.put("differentNo", different.getDifferentNo());
				List<BillOmDivideDifferentDtl> dtlList = billOmDivideDifferentDtlService.findByBiz(null, params);
				//3.开始释放明细的预下库存
				if(CommonUtil.hasValue(differentList)){
					for (BillOmDivideDifferentDtl differentDtl : dtlList) {
						
						if("N".equals(differentDtl.getdBarcode())||StringUtils.isEmpty(differentDtl.getdBarcode())){
							StringBuffer sbmsg = new StringBuffer();
							sbmsg.append("单据："+different.getDifferentNo()+",");
							sbmsg.append("客户："+differentDtl.getStoreNo()+",");
							sbmsg.append("箱号："+differentDtl.getBoxNo()+",");
							sbmsg.append("商品："+differentDtl.getsItemNo()+",");
							sbmsg.append("调整的条码为空,不能审核!");
							throw new ServiceException(sbmsg.toString());
						}
						
						//扣减预下数量、库存数量
						BillAccControlDto controlDtoOut = new BillAccControlDto();
						controlDtoOut.setiLocno(differentDtl.getLocno());
						controlDtoOut.setiOwnerNo(differentDtl.getOwnerNo());
						controlDtoOut.setiPaperNo(differentDtl.getDifferentNo());
						controlDtoOut.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
						controlDtoOut.setiIoFlag("O");
						controlDtoOut.setiCreator(loginName);
						controlDtoOut.setiRowId(differentDtl.getSerialNo());
						controlDtoOut.setiCellNo(differentDtl.getsCellNo());
						controlDtoOut.setiCellId(new BigDecimal(differentDtl.getsCellId()));
						controlDtoOut.setiItemNo(differentDtl.getsItemNo());
						controlDtoOut.setiSizeNo(differentDtl.getsSizeNo());
						controlDtoOut.setiPackQty(differentDtl.getPackQty());
						controlDtoOut.setiSupplierNo(differentDtl.getSupplierNo());
						controlDtoOut.setiOutstockQty(new BigDecimal(0).subtract(differentDtl.getItemQty()));
						controlDtoOut.setiItemType(differentDtl.getItemType());
						controlDtoOut.setiQuality(differentDtl.getQuality());
						controlDtoOut.setiQty(differentDtl.getItemQty());
						
						/**默认值**/
						controlDtoOut.setiInstockQty(new BigDecimal(0));
						controlDtoOut.setiStatus("0");
						controlDtoOut.setiFlag("0");
						controlDtoOut.setiHmManualFlag("1");
						controlDtoOut.setiTerminalFlag("1");
						billAccControlService.procAccPrepareDataExt(controlDtoOut);
						
						//调用外部存储过程
						BillAccControlDto dtoOut = new BillAccControlDto();
						dtoOut.setiPaperNo(differentDtl.getDifferentNo());
						dtoOut.setiLocType("2");
						dtoOut.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
						dtoOut.setiIoFlag("O");
						dtoOut.setiPrepareDataExt(differentDtl.getSerialNo());
						dtoOut.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dtoOut);
						
						//增加库存数量
						BillAccControlDto controlDtoIn = new BillAccControlDto();
						controlDtoIn.setiLocno(differentDtl.getLocno());
						controlDtoIn.setiOwnerNo(differentDtl.getOwnerNo());
						controlDtoIn.setiPaperNo(differentDtl.getDifferentNo());
						controlDtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
						controlDtoIn.setiIoFlag("I");
						controlDtoIn.setiCreator(loginName);
						controlDtoIn.setiRowId(differentDtl.getSerialNo());
						controlDtoIn.setiCellNo(differentDtl.getdCellNo());
						controlDtoIn.setiItemNo(differentDtl.getdItemNo());
						controlDtoIn.setiSizeNo(differentDtl.getdSizeNo());
						controlDtoIn.setiPackQty(differentDtl.getPackQty());
						controlDtoIn.setiSupplierNo(differentDtl.getdSupplierNo());
						controlDtoIn.setiItemType(differentDtl.getItemType());
						controlDtoIn.setiQuality(differentDtl.getQuality());
						controlDtoIn.setiQty(differentDtl.getItemQty());
						
						/**默认值**/
						controlDtoIn.setiInstockQty(new BigDecimal(0));
						//controlDtoIn.setiCellId(new BigDecimal(0));
						controlDtoIn.setiOutstockQty(new BigDecimal(0));
						controlDtoIn.setiStatus("0");
						controlDtoIn.setiFlag("0");
						controlDtoIn.setiHmManualFlag("1");
						controlDtoIn.setiTerminalFlag("1");
						billAccControlService.procAccPrepareDataExt(controlDtoIn);
						
						//调用外部存储过程
						BillAccControlDto dtoIn = new BillAccControlDto();
						dtoIn.setiPaperNo(differentDtl.getDifferentNo());
						dtoIn.setiLocType("2");
						dtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
						dtoIn.setiIoFlag("I");
						dtoIn.setiPrepareDataExt(differentDtl.getSerialNo());
						dtoIn.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dtoIn);
					}
				}
				
				//库存分货
				if("1".equals(different.getDifferentType())){
					//开始关联库存查询库存ID写入分货明细和预分货明细
					List<BillOmDivideDifferentDtl> dtlDiffList = billOmDivideDifferentDtlService.selectDifferentDtl4Content(params);
					if(!CommonUtil.hasValue(dtlDiffList)){
						throw new ManagerException("调整后没有关联到库存数据!");
					}
					
					BillOmDivideDtl entity = new BillOmDivideDtl();
					BillImReceipt receipt = new BillImReceipt();
					ConBoxDtl boxd = new ConBoxDtl();
					BillOmDivideDtl divideInsert = new BillOmDivideDtl();
					BillImReceiptDtl receiptInsert = new BillImReceiptDtl();
					ConBoxDtl insertBoxDtl = new ConBoxDtl();
					
					String expMsg = "";
					int x = 1;
					for (BillOmDivideDifferentDtl difDtl : dtlDiffList) {
						
						BigDecimal difItemQty = difDtl.getItemQty();
						int dNum = x++;
						//1.先插入分货明细,如果存在相同的调整后记录,只新增数量
						BillOmDivideDtl divideDtl = null;
						entity.setLocno(difDtl.getLocno());
						entity.setOwnerNo(difDtl.getOwnerNo());
						entity.setDivideNo(difDtl.getSourceNo());
						entity.setBoxNo(difDtl.getBoxNo());
						entity.setStoreNo(difDtl.getStoreNo());
						entity.setsCellNo(difDtl.getsCellNo());
						entity.setItemNo(difDtl.getsItemNo());
						entity.setSizeNo(difDtl.getsSizeNo());
						entity.setExpNo(difDtl.getExpNo());
						//entity.setDifStatus(DIFSTATUS_0);
						divideDtl = billOmDivideDtlService.selectDivideDtlByDifferent(entity);
						if(divideDtl == null){
							expMsg = "查找分货单明细失败!";
							throw new ManagerException(getExpMsg(difDtl) + expMsg);
						}
						BigDecimal difDivideQty = divideDtl.getItemQty().subtract(difItemQty);
						if(difDivideQty.intValue() <= 0){
							count = billOmDivideDtlService.deleteById(divideDtl);
							if(count < 1){
								expMsg = "删除分货单明细失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}else{
							divideDtl.setItemQty(difDivideQty);
							count = billOmDivideDtlService.modifyById(divideDtl);
							if(count < 1){
								expMsg = "更新调整前的分货单明细计划数量失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}
						setDivideDtl(difDtl, divideDtl, divideInsert);//新增明细
						
						//处理调整后商品信息
						//更新调整后的记录的分货计划数量=复核数量(部分复核可以差异调整)
						//如果调整的分货明细实际数量是0,代表整条记录调整,删掉记录
						entity.setItemNo(difDtl.getdItemNo());
						entity.setSizeNo(difDtl.getdSizeNo());
						//entity.setDifStatus(DIFSTATUS_1);
						divideDtl = billOmDivideDtlService.selectDivideDtlByDifferent(entity);
						if(divideDtl != null){
							BigDecimal itemQty = divideDtl.getItemQty().add(difItemQty);
							divideDtl.setItemQty(itemQty);
							count = billOmDivideDtlService.modifyById(divideDtl);
							if(count < 1){
								expMsg = "更新调整后的分货单明细计划数量失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}else{
							//新增一条调整后的记录到分货明细
							Long divideId = billOmDivideDtlService.selectDivideId();
							divideInsert.setDivideId(divideId);
							divideInsert.setAssignNameCh(userName);
							divideInsert.setAssignName(loginName);
							count = billOmDivideDtlService.add(divideInsert);
							if(count < 1){
								expMsg = "插入调整后的分货单明细失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}
						
						
						//处理收货单调整前的商品信息
						//根据箱、商品、尺码更新预分货单的商品明细计划数量
						List<BillImReceiptDtl> receiptDtlList = null;
						Map<String, Object> receiptMap = new HashMap<String, Object>();
						receiptMap.put("locno", divideInsert.getLocno());
						receiptMap.put("ownerNo", divideInsert.getOwnerNo());
						receiptMap.put("receiptNo", divideInsert.getSourceNo());
						receiptMap.put("boxNo", difDtl.getBoxNo());
						receiptMap.put("itemNo", difDtl.getsItemNo());
						receiptMap.put("sizeNo", difDtl.getsSizeNo());
						receiptDtlList = billImReceiptDtlService.findByBiz(null, receiptMap);
						if(!CommonUtil.hasValue(receiptDtlList)){
							expMsg = "查找预分货单"+divideInsert.getSourceNo()+"明细失败!";
							throw new ManagerException(getExpMsg(difDtl) + expMsg);
						}
						BillImReceiptDtl receiptDtl = receiptDtlList.get(0);
						BigDecimal receiptQty = receiptDtl.getReceiptQty().subtract(difItemQty);
						BigDecimal rDivideQty = receiptDtl.getDivideQty().subtract(difItemQty);
						if(receiptQty.intValue() <= 0){
							count = billImReceiptDtlService.deleteById(receiptDtl);
							if(count < 1){
								expMsg = "预分货"+divideInsert.getSourceNo()+"明细删除失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}else{
							receiptDtl.setReceiptQty(receiptQty);
							receiptDtl.setDivideQty(rDivideQty);
							count = billImReceiptDtlService.modifyById(receiptDtl);
							if(count < 1){
								expMsg = "预分货"+divideInsert.getSourceNo()+"明细更新失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}
						setReceiptDtl(difDtl, receiptDtl, receiptInsert);//新增赋值
						
						
						//处理调整后的预分货单的商品
						receiptMap.put("itemNo", difDtl.getdItemNo());
						receiptMap.put("sizeNo", difDtl.getdSizeNo());
						receiptDtlList = billImReceiptDtlService.findByBiz(null, receiptMap);
						if(CommonUtil.hasValue(receiptDtlList)){
							BillImReceiptDtl imReceiptDtl = receiptDtlList.get(0);
							BigDecimal itemQty = imReceiptDtl.getReceiptQty().add(difItemQty);
							BigDecimal rDivideQty2 = imReceiptDtl.getDivideQty().add(difItemQty);
							imReceiptDtl.setReceiptQty(itemQty);
							imReceiptDtl.setDivideQty(rDivideQty2);
							count = billImReceiptDtlService.modifyById(imReceiptDtl);
							if(count < 1){
								expMsg = "更新调整后的预分货单明细失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}else{
							receipt.setLocno(divideInsert.getLocno());
							receipt.setOwnerNo(divideInsert.getOwnerNo());
							receipt.setReceiptNo(divideInsert.getSourceNo());
							int rowId = billImReceiptDtlService.selectMaxRowId(receipt);
							receiptInsert.setRowId(++rowId);
							receiptInsert.setCreator(loginName);
							receiptInsert.setEditor(loginName);
							receiptInsert.setEditorName(userName);
							count = billImReceiptDtlService.add(receiptInsert);
							if(count < 1){
								expMsg = "插入调整后的预分货单明细失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}
						
						
						//处理调整前商品
						//根据箱、商品、尺码更新箱码表的商品明细计划数量
						ConBoxDtl boxDtl = null;
						Map<String, Object> boxMap = new HashMap<String, Object>();
						boxMap.put("locno", difDtl.getLocno());
						boxMap.put("ownerNo", difDtl.getOwnerNo());
						boxMap.put("boxNo", difDtl.getBoxNo());
						boxMap.put("itemNo", difDtl.getsItemNo());
						boxMap.put("sizeNo", difDtl.getsSizeNo());
						boxDtl = conBoxDtlService.selectBoxDtl4Recheck(boxMap);
						if(boxDtl == null){
							expMsg = "查找箱明细失败!";
							throw new ManagerException(getExpMsg(difDtl) + expMsg);
						}
						
						BigDecimal boxQty = boxDtl.getQty().subtract(difItemQty);
						BigDecimal divideQty = boxDtl.getDivideQty().subtract(difItemQty);
						if(boxQty.intValue() <= 0){
							count = conBoxDtlService.deleteById(boxDtl);
							if(count < 1){
								expMsg = "删除箱明细失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}else{
							boxDtl.setQty(boxQty);
							boxDtl.setDivideQty(divideQty);
							count = conBoxDtlService.modifyById(boxDtl);
							if(count < 1){
								expMsg = "更新箱明细失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}
						setConBoxDtl(difDtl, boxDtl, insertBoxDtl);//新增箱号赋值
						
						//处理调整后的商品
						boxMap.put("itemNo", difDtl.getdItemNo());
						boxMap.put("sizeNo", difDtl.getdSizeNo());
						boxDtl = conBoxDtlService.selectBoxDtl4Recheck(boxMap);
						if(boxDtl != null){
							BigDecimal qty = boxDtl.getQty().add(difItemQty);
							BigDecimal divideQty2 = boxDtl.getDivideQty().add(difItemQty);
							boxDtl.setQty(qty);
							boxDtl.setDivideQty(divideQty2);
							count = conBoxDtlService.modifyById(boxDtl);
							if(count < 1){
								expMsg = "更新箱明细数量失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}else{
							boxd.setLocno(difDtl.getLocno());
							boxd.setOwnerNo(difDtl.getOwnerNo());
							boxd.setBoxNo(difDtl.getBoxNo());
							int boxId = conBoxDtlService.selectMaxBoxId(boxd);
							insertBoxDtl.setBoxId(BigDecimal.valueOf(++boxId));
							count = conBoxDtlService.add(insertBoxDtl);
							if(count < 1){
								expMsg = "插入箱明细数据失败!";
								throw new ManagerException(getExpMsg(difDtl) + expMsg);
							}
						}
						
						//更新分货单主档编辑人和编辑时间
						BillOmDivide omDivide = new BillOmDivide();
						omDivide.setLocno(divideInsert.getLocno());
						omDivide.setDivideNo(divideInsert.getDivideNo());
						omDivide.setEditor(loginName);
						omDivide.setEditorname(userName);
						omDivide.setEdittm(new Date());
						count = billOmDivideService.modifyById(omDivide); 
						if(count < 1){
							throw new ManagerException("分货单："+divideInsert.getDivideNo()+"已删除或状态已改变!");
						}
						
						
						//更新收货单主档编辑人和编辑时间
						BillImReceipt imReceipt = new BillImReceipt();
						imReceipt.setLocno(receiptInsert.getLocno());
						imReceipt.setOwnerNo(receiptInsert.getOwnerNo());
						imReceipt.setReceiptNo(receiptInsert.getReceiptNo());
						imReceipt.setEditor(loginName);
						imReceipt.setEditorName(userName);
						imReceipt.setEdittm(new Date());
						count = billImReceiptService.modifyById(imReceipt); 
						if(count < 1){
							throw new ManagerException("预分货单："+divideDtl.getDivideNo()+"已删除或状态已改变!");
						}
						
						//锁定预下
						BillAccControlDto controlDtoIn = new BillAccControlDto();
						controlDtoIn.setiLocno(difDtl.getLocno());
						controlDtoIn.setiOwnerNo(difDtl.getOwnerNo());
						controlDtoIn.setiPaperNo(difDtl.getDifferentNo());
						controlDtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
						controlDtoIn.setiIoFlag("I");
						controlDtoIn.setiCreator(loginName);
						controlDtoIn.setiRowId(new BigDecimal(dNum));
						controlDtoIn.setiCellNo(difDtl.getdCellNo());
						controlDtoIn.setiCellId(new BigDecimal(difDtl.getdCellId()));
						controlDtoIn.setiItemNo(difDtl.getdItemNo());
						controlDtoIn.setiSizeNo(difDtl.getdSizeNo());
						controlDtoIn.setiPackQty(new BigDecimal(1));
						controlDtoIn.setiSupplierNo(difDtl.getSupplierNo());
						controlDtoIn.setiOutstockQty(difDtl.getItemQty());
						controlDtoIn.setiItemType(difDtl.getItemType());
						controlDtoIn.setiQuality(difDtl.getQuality());
						
						/**默认值**/
						controlDtoIn.setiQty(new BigDecimal(0));
						controlDtoIn.setiInstockQty(new BigDecimal(0));
						controlDtoIn.setiStatus("0");
						controlDtoIn.setiFlag("0");
						controlDtoIn.setiHmManualFlag("1");
						controlDtoIn.setiTerminalFlag("1");
						billAccControlService.procAccPrepareDataExt(controlDtoIn);
						
						//调用外部存储过程
						BillAccControlDto dtoIn = new BillAccControlDto();
						dtoIn.setiPaperNo(difDtl.getDifferentNo());
						dtoIn.setiLocType("2");
						dtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
						dtoIn.setiIoFlag("I");
						dtoIn.setiPrepareDataExt(new BigDecimal(dNum));
						dtoIn.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dtoIn);
					}
				}
				
				
//				//将调整的商品写入分货明细中
//				int insertCount = 0;
//				if("1".equals(different.getDifferentType())){
//					for (BillOmDivideDifferentDtl differentDtl : dtlList) {
//						BillOmDivideDtl entity = new BillOmDivideDtl();
//						entity.setLocno(differentDtl.getLocno());
//						entity.setOwnerNo(differentDtl.getOwnerNo());
//						entity.setDivideNo(different.getSourceNo());
//						entity.setStoreNo(differentDtl.getStoreNo());
//						entity.setBoxNo(differentDtl.getBoxNo());
//						entity.setsCellNo(differentDtl.getdCellNo());
//						entity.setItemNo(differentDtl.getdItemNo());
//						entity.setSizeNo(differentDtl.getdSizeNo());
//						BillOmDivideDtl divideDtl = billOmDivideDtlService.selectDivideDtlByDifferent(entity);
//						if(divideDtl != null){
//							BigDecimal itemQty = divideDtl.getItemQty();
//							divideDtl.setItemQty(itemQty.add(differentDtl.getItemQty()));
//						}
//					}
//				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	
	public void setDivideDtl(BillOmDivideDifferentDtl differentDtl,BillOmDivideDtl divideDtl,BillOmDivideDtl divideInsert){
		divideInsert.setLocno(divideDtl.getLocno());
		divideInsert.setOwnerNo(divideDtl.getOwnerNo());
		divideInsert.setDivideNo(divideDtl.getDivideNo());
		//divideInsert.setDivideId(divideDtl.getDivideId());
		divideInsert.setSourceNo(divideDtl.getSourceNo());
		divideInsert.setBatchNo(divideDtl.getBatchNo());
		divideInsert.setOperateDate(new Date());
		divideInsert.setStoreNo(divideDtl.getStoreNo());
		divideInsert.setExpNo(divideDtl.getExpNo());
		divideInsert.setExpType(divideDtl.getExpType());
		//divideInsert.setAssignName(divideDtl.getAssignName());
		//divideInsert.setAssignNameCh(divideDtl.getAssignNameCh());
		divideInsert.setSerialNo(divideDtl.getSerialNo());
		divideInsert.setGroupNo(divideDtl.getGroupNo());
		divideInsert.setExpDate(divideDtl.getExpDate());
		divideInsert.setBrandNo(differentDtl.getBrandNo());
		divideInsert.setItemNo(differentDtl.getdItemNo());
		divideInsert.setSizeNo(differentDtl.getdSizeNo());
		divideInsert.setPackQty(new BigDecimal(1));
		divideInsert.setsCellNo(differentDtl.getdCellNo());
		divideInsert.setsCellId(differentDtl.getdCellId());
		divideInsert.setBoxNo(differentDtl.getBoxNo());
		divideInsert.setDifFlag(BillOmDivideDifFlagEnums.DIF_FLAG1.getOptBillType());
		divideInsert.setItemType(differentDtl.getItemType());
		divideInsert.setQuality(differentDtl.getQuality());
		divideInsert.setItemQty(differentDtl.getItemQty());
	}
	
	public void setReceiptDtl(BillOmDivideDifferentDtl differentDtl,BillImReceiptDtl receiptDtl,BillImReceiptDtl receiptInsert){
		receiptInsert.setLocno(receiptDtl.getLocno());
		receiptInsert.setOwnerNo(receiptDtl.getOwnerNo());
		receiptInsert.setReceiptNo(receiptDtl.getReceiptNo());
		receiptInsert.setImportNo(receiptDtl.getImportNo());
		//receiptInsert.setRowId(receiptDtl.getRowId());
		//receiptInsert.setCreator(receiptDtl.getCreator());
		//receiptInsert.setEditor(receiptDtl.getCreator());
		//receiptInsert.setEditorname(receiptDtl.getEditorname());
		receiptInsert.setBoxNo(differentDtl.getBoxNo());
		receiptInsert.setStatus(STATUS11);
		receiptInsert.setBrandNo(differentDtl.getBrandNo());
		receiptInsert.setItemNo(differentDtl.getdItemNo());
		receiptInsert.setSizeNo(differentDtl.getdSizeNo());
		receiptInsert.setPackQty(new BigDecimal(1));
		receiptInsert.setCellNo(differentDtl.getdCellNo());
		receiptInsert.setCellId(new BigDecimal(differentDtl.getdCellId()));
		receiptInsert.setItemType(differentDtl.getItemType());
		receiptInsert.setQuality(differentDtl.getQuality());
		receiptInsert.setDifFlag(BillOmDivideDifFlagEnums.DIF_FLAG1.getOptBillType());
		receiptInsert.setReceiptQty(differentDtl.getItemQty());
		receiptInsert.setDivideQty(differentDtl.getItemQty());
		receiptInsert.setCreatetm(new Date());
		receiptInsert.setEdittm(new Date());
	}
	
	public void setConBoxDtl(BillOmDivideDifferentDtl differentDtl,ConBoxDtl boxDtl, ConBoxDtl insertBoxDtl){
		insertBoxDtl.setLocno(differentDtl.getLocno());
		insertBoxDtl.setOwnerNo(differentDtl.getOwnerNo());
		insertBoxDtl.setBoxNo(differentDtl.getBoxNo());
		insertBoxDtl.setItemNo(differentDtl.getdItemNo());
		insertBoxDtl.setSizeNo(differentDtl.getdSizeNo());
		insertBoxDtl.setStyleNo("N");
		insertBoxDtl.setQty(differentDtl.getItemQty());
		//insertBoxDtl.setImportNo(boxDtl.getImportNo());
		insertBoxDtl.setDivideQty(differentDtl.getItemQty());
		insertBoxDtl.setAddFlag("1");
	}
	
	public String getExpMsg(BillOmDivideDifferentDtl difDtl){
		StringBuffer sb = new StringBuffer();
		sb.append("商品编码:"+difDtl.getsItemNo());
		sb.append(",尺码:"+difDtl.getsSizeNo());
		sb.append(",箱号:"+difDtl.getBoxNo());
		return sb.toString();
	}
}