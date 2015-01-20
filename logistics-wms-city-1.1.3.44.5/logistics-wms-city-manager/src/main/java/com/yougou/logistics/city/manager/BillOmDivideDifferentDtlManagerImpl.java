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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.BillOmDivideDifferent;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentDtl;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentDtlKey;
import com.yougou.logistics.city.common.model.BillOmDivideDifferentKey;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.ItemBarcode;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillOmDivideDifferentDtlService;
import com.yougou.logistics.city.service.BillOmDivideDifferentService;
import com.yougou.logistics.city.service.BillOmDivideDtlService;
import com.yougou.logistics.city.service.ItemBarcodeService;

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
@Service("billOmDivideDifferentDtlManager")
class BillOmDivideDifferentDtlManagerImpl extends BaseCrudManagerImpl implements BillOmDivideDifferentDtlManager {
	
	@Resource
	private BillOmDivideDifferentService billOmDivideDifferentService;
	
	@Resource
	private BillOmDivideDifferentDtlService billOmDivideDifferentDtlService;
	
	@Resource
	private BillAccControlService billAccControlService;
	
	@Resource
	private BillOmDivideDtlService billOmDivideDtlService;
	
	@Resource
	private ItemBarcodeService itemBarcodeService;
	
	private static final String STATUS10 = "10";
	
	private static final String PIXFLAG0 = "0";
	
	private static final String PIXFLAG1 = "1";
	
	private final static String DIFFERENTTYPE1 = "1";
	
	private final static String DIFFERENTTYPE3 = "3";

	@Override
	public BaseCrudService init() {
		return billOmDivideDifferentDtlService;
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDivideDifferentDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void splitDifferentDtl(BillOmDivideDifferentDtl differentDtl) throws ManagerException {
		try{
			if(differentDtl == null){
				throw new ServiceException("请选择需要拆分的明细!");
			}
			//1.验证主单数据
			BillOmDivideDifferentKey differentKey = new BillOmDivideDifferentKey();
			differentKey.setLocno(differentDtl.getLocno());
			differentKey.setDifferentNo(differentDtl.getDifferentNo());
			BillOmDivideDifferent differentEntity=(BillOmDivideDifferent) billOmDivideDifferentService.findById(differentKey);
			if(differentEntity==null||!STATUS10.equals(differentEntity.getStatus())){
				throw new ManagerException("单据: " + differentDtl.getDifferentNo() +"已删除或状态已改变!");
			}
			
			//只有分货差异的可以拆分
			if(DIFFERENTTYPE3.equals(differentEntity.getDifferentType())){
				throw new ManagerException("手工创建的单据不能拆分!");
			}
			
			//验证商品和条码的合法性
			Map<String, Object> barcodeParams = new HashMap<String, Object>();
			barcodeParams.put("itemNo", differentDtl.getdItemNo());
			barcodeParams.put("barcode", differentDtl.getdBarcode());
			barcodeParams.put("packageId", "0");
			List<ItemBarcode> barcodeList = itemBarcodeService.findByBiz(null, barcodeParams);
			if(!CommonUtil.hasValue(barcodeList)){
				StringBuffer sbmsg = getDMsgTip(differentDtl);
				sbmsg.append("填写的数据不合法!");
				throw new ServiceException(sbmsg.toString());
			}
			
			//1.验证明细是否可以拆分
			BillOmDivideDifferentDtlKey differentDtlKey = new BillOmDivideDifferentDtlKey();
			differentDtlKey.setLocno(differentDtl.getLocno());
			differentDtlKey.setDifferentNo(differentDtl.getDifferentNo());
			differentDtlKey.setSerialNo(differentDtl.getSerialNo());
			BillOmDivideDifferentDtl dtlEntity = (BillOmDivideDifferentDtl)billOmDivideDifferentDtlService.findById(differentDtlKey);
			if(dtlEntity == null){
				StringBuffer sbmsg = new StringBuffer();
				sbmsg.append("客户："+differentDtl.getStoreNo()+",");
				sbmsg.append("箱号："+differentDtl.getBoxNo()+",");
				sbmsg.append("商品："+differentDtl.getsItemNo()+",");
				sbmsg.append("条码："+differentDtl.getsBarcode()+",");
				sbmsg.append("已删除或状态已改变!");
				throw new ServiceException(sbmsg.toString());
			}
			if(dtlEntity.getItemQty().intValue() < 2){
				StringBuffer sbmsg = new StringBuffer();
				sbmsg.append("客户："+dtlEntity.getStoreNo()+",");
				sbmsg.append("箱号："+dtlEntity.getBoxNo()+",");
				sbmsg.append("商品："+dtlEntity.getsItemNo()+",");
				sbmsg.append("条码："+dtlEntity.getsBarcode()+",");
				sbmsg.append("可拆分数量不足,不能拆分!");
				throw new ServiceException(sbmsg.toString());
			}
			
			//3.拆分数量减1
			int count = 0;
			BigDecimal splitQtyS = dtlEntity.getItemQty().subtract(new BigDecimal(1));
			dtlEntity.setItemQty(splitQtyS);
			count = billOmDivideDifferentDtlService.modifyById(dtlEntity);
			if(count < 1){
				throw new ServiceException("拆分失败,请检查是否有足够的拆分数量!");
			}
			
			//4.拆分数量加1
			Map<String, Object> checkParams = new HashMap<String, Object>();
			checkParams.put("locno", differentDtl.getLocno());
			checkParams.put("differentNo", differentDtl.getDifferentNo());
			checkParams.put("storeNo", differentDtl.getStoreNo());
			checkParams.put("boxNo", differentDtl.getBoxNo());
			checkParams.put("sItemNo", differentDtl.getsItemNo());
			checkParams.put("sBarcode", differentDtl.getsBarcode());
			checkParams.put("dItemNo", differentDtl.getdItemNo());
			checkParams.put("dBarcode", differentDtl.getdBarcode());
			checkParams.put("expNo", differentDtl.getExpNo());
			checkParams.put("pixFlag", PIXFLAG1);
			List<BillOmDivideDifferentDtl> checkList=billOmDivideDifferentDtlService.findByBiz(null, checkParams);
			if(CommonUtil.hasValue(checkList)){
				BillOmDivideDifferentDtl dtl = checkList.get(0);
				BigDecimal splitQtyI = dtl.getItemQty().add(new BigDecimal(1));
				dtl.setItemQty(splitQtyI);
				dtl.setCheckStatus(STATUS10);
				count = billOmDivideDifferentDtlService.modifyById(dtl);
				if(count < 1){
					throw new ServiceException("更新数量拆分失败!");
				}
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = null;
				try {
					d = sdf.parse(CommonUtil.getCurrentDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int serialNo = billOmDivideDifferentDtlService.selectMaxRowId(differentDtl);
				BillOmDivideDifferentDtl differentDtlIn = new BillOmDivideDifferentDtl();
				differentDtlIn.setLocno(differentDtl.getLocno());
				differentDtlIn.setOwnerNo(differentDtl.getOwnerNo());
				differentDtlIn.setDifferentNo(differentDtl.getDifferentNo());
				differentDtlIn.setOperateDate(d);
				differentDtlIn.setStoreNo(differentDtl.getStoreNo());
				differentDtlIn.setsItemNo(differentDtl.getsItemNo());
				differentDtlIn.setPackQty(new BigDecimal(1));
				differentDtlIn.setItemQty(new BigDecimal(1));
				differentDtlIn.setRealQty(new BigDecimal(0));
				differentDtlIn.setsCellNo(differentDtl.getsCellNo());
				differentDtlIn.setsCellId(differentDtl.getsCellId());
				differentDtlIn.setdCellNo(differentDtl.getdCellNo());
				differentDtlIn.setdCellId(0L);
				differentDtlIn.setStatus(STATUS10);
				differentDtlIn.setBoxNo(differentDtl.getBoxNo());
				differentDtlIn.setBrandNo(differentDtl.getBrandNo());
				differentDtlIn.setsBarcode(differentDtl.getsBarcode());
				differentDtlIn.setPixFlag(new BigDecimal(1));
				differentDtlIn.setItemType(differentDtl.getItemType());
				differentDtlIn.setQuality(differentDtl.getQuality());
				differentDtlIn.setSerialNo(new BigDecimal(++serialNo));
				differentDtlIn.setdItemNo(differentDtl.getdItemNo());
				differentDtlIn.setdBarcode(differentDtl.getdBarcode());
				differentDtlIn.setExpNo(differentDtl.getExpNo());
				count = billOmDivideDifferentDtlService.add(differentDtlIn);
				if(count < 1){
					throw new ServiceException("新增差异调整单拆分明细失败!");
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void saveDifferentDtl(BillOmDivideDifferent different, List<BillOmDivideDifferentDtl> insertList,
			List<BillOmDivideDifferentDtl> updateList, List<BillOmDivideDifferentDtl> delList) throws ManagerException {
		try{
			//验证主单数据
			BillOmDivideDifferentKey differentKey = new BillOmDivideDifferentKey();
			differentKey.setLocno(different.getLocno());
			differentKey.setDifferentNo(different.getDifferentNo());
			BillOmDivideDifferent differentEntity=(BillOmDivideDifferent) billOmDivideDifferentService.findById(differentKey);
			if(differentEntity==null||!STATUS10.equals(differentEntity.getStatus())){
				throw new ManagerException("单据: " + different.getDifferentNo() +"已删除或状态已改变!");
			}
			String divideNo = differentEntity.getSourceNo();//分货单号
			
			if(CommonUtil.hasValue(delList)){
				Map<String, List<BillOmDivideDifferentDtl>> mapData = new HashMap<String, List<BillOmDivideDifferentDtl>>();
				//1.先将拆分和不是拆分的单据放入map
				List<BillOmDivideDifferentDtl> dataList = new ArrayList<BillOmDivideDifferentDtl>();
				for (BillOmDivideDifferentDtl differentDtl : delList) {
					String pixFlag = String.valueOf(differentDtl.getPixFlag());
					if(mapData.get(pixFlag)==null){
						dataList = new ArrayList<BillOmDivideDifferentDtl>();
						dataList.add(differentDtl);
						mapData.put(pixFlag, dataList);
					}else{
						dataList = mapData.get(pixFlag);
						dataList.add(differentDtl);
						mapData.put(pixFlag, dataList);
					}
				}
				
				//2.先回写拆分的单据回父数据
				if(mapData.get(PIXFLAG1) != null){
					List<BillOmDivideDifferentDtl> differentList=mapData.get(PIXFLAG1);
					for (BillOmDivideDifferentDtl differentDtl : differentList) {
						
						BillOmDivideDifferentDtl checkEntity = billOmDivideDifferentDtlService.findById(differentDtl);
						if(checkEntity == null){
							StringBuffer sbmsg = getMsgTip(differentDtl);
							sbmsg.append("已删除或状态已改变!");
							throw new ServiceException(sbmsg.toString());
						}
						
						BillOmDivideDifferentDtl searchDifferentDtl = new BillOmDivideDifferentDtl();
						searchDifferentDtl.setLocno(differentDtl.getLocno());
						searchDifferentDtl.setDifferentNo(differentDtl.getDifferentNo());
						searchDifferentDtl.setStoreNo(differentDtl.getStoreNo());
						searchDifferentDtl.setBoxNo(differentDtl.getBoxNo());
						searchDifferentDtl.setsItemNo(differentDtl.getsItemNo());
						searchDifferentDtl.setsBarcode(differentDtl.getsBarcode());
						searchDifferentDtl.setExpNo(differentDtl.getExpNo());
						BillOmDivideDifferentDtl dtlEntity = billOmDivideDifferentDtlService.selectDifferentByPixFlag(searchDifferentDtl);
						
						//新增父数据itemQty
						if(dtlEntity == null){
							StringBuffer sbmsg = getMsgTip(differentDtl);
							sbmsg.append("查询需要回写的拆分数量失败!");
							throw new ServiceException(sbmsg.toString());
						}
						
						//更新数量
						BigDecimal splitQty = dtlEntity.getItemQty().add(differentDtl.getItemQty());
						dtlEntity.setItemQty(splitQty);
						int count = 0;
						count = billOmDivideDifferentDtlService.modifyById(dtlEntity);
						if(count < 1){
							StringBuffer sbmsg = getMsgTip(differentDtl);
							sbmsg.append("更新父数据拆分数量失败!");
							throw new ServiceException(sbmsg.toString());
						}
						
						//删除拆分数据
						count = billOmDivideDifferentDtlService.deleteById(differentDtl);
						if(count < 1){
							StringBuffer sbmsg = getMsgTip(differentDtl);
							sbmsg.append("删除拆分数据失败!");
							throw new ServiceException(sbmsg.toString());
						}
					}
				}
				
				//3.删除父数据,并且回写分货单明细
				if(mapData.get(PIXFLAG0) != null){
					List<BillOmDivideDifferentDtl> differentList=mapData.get(PIXFLAG0);
					for (BillOmDivideDifferentDtl differentDtl : differentList) {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("locno", differentDtl.getLocno());
						params.put("differentNo", differentDtl.getDifferentNo());
						params.put("storeNo", differentDtl.getStoreNo());
						params.put("boxNo", differentDtl.getBoxNo());
						params.put("sItemNo", differentDtl.getsItemNo());
						params.put("sBarcode", differentDtl.getsBarcode());
						params.put("expNo", differentDtl.getExpNo());
						if(DIFFERENTTYPE3.equals(differentEntity.getDifferentType())){
							params.put("serialNo", differentDtl.getSerialNo());
						}
						params.put("status", STATUS10);
						List<BillOmDivideDifferentDtl> dtlList=billOmDivideDifferentDtlService.findByBiz(null, params);
						if(!CommonUtil.hasValue(dtlList)){
							StringBuffer sbmsg = getMsgTip(differentDtl);
							sbmsg.append("已删除或状态已改变!");
							throw new ServiceException(sbmsg.toString());
						}
						
						for (BillOmDivideDifferentDtl boddd : dtlList) {
							//扣减预下数量
							if(!DIFFERENTTYPE1.equals(differentEntity.getDifferentType())){
								BillAccControlDto controlDtoIn = new BillAccControlDto();
								controlDtoIn.setiLocno(boddd.getLocno());
								controlDtoIn.setiOwnerNo(boddd.getOwnerNo());
								controlDtoIn.setiPaperNo(boddd.getDifferentNo());
								controlDtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
								controlDtoIn.setiIoFlag("O");
								controlDtoIn.setiCreator(different.getCreator());
								controlDtoIn.setiRowId(boddd.getSerialNo());
								controlDtoIn.setiCellNo(boddd.getsCellNo());
								controlDtoIn.setiCellId(new BigDecimal(boddd.getsCellId()));
								controlDtoIn.setiItemNo(boddd.getsItemNo());
								controlDtoIn.setiSizeNo(boddd.getsSizeNo());
								controlDtoIn.setiPackQty(boddd.getPackQty());
								controlDtoIn.setiSupplierNo(differentDtl.getSupplierNo());
								controlDtoIn.setiOutstockQty(new BigDecimal(0).subtract(boddd.getItemQty()));
								controlDtoIn.setiItemType(boddd.getItemType());
								controlDtoIn.setiQuality(boddd.getQuality());
								
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
								dtoIn.setiPaperNo(boddd.getDifferentNo());
								dtoIn.setiLocType("2");
								dtoIn.setiPaperType(CNumPre.OM_DIVIDE_DIFFERENT_PRE);
								dtoIn.setiIoFlag("O");
								dtoIn.setiPrepareDataExt(boddd.getSerialNo());
								dtoIn.setiIsWeb(new BigDecimal(1));
								billAccControlService.procAccApply(dtoIn);
							}
							//删除明细数据
							int count = billOmDivideDifferentDtlService.deleteById(boddd);
							if(count < 1){
								StringBuffer sbmsg = getMsgTip(boddd);
								sbmsg.append("已删除或状态已改变!");
								throw new ServiceException(sbmsg.toString());
							}
						}
						
						//回写分货单明细状态
						int dcount = 0;
						if(DIFFERENTTYPE1.equals(differentEntity.getDifferentType())){
							BillOmDivideDtl divideDtl = new BillOmDivideDtl();
							divideDtl.setLocno(different.getLocno());
							divideDtl.setDivideNo(divideNo);
							divideDtl.setStoreNo(differentDtl.getStoreNo());
							divideDtl.setBoxNo(differentDtl.getBoxNo());
							divideDtl.setItemNo(differentDtl.getsItemNo());
							divideDtl.setSizeNo(differentDtl.getsSizeNo());
							divideDtl.setExpNo(differentDtl.getExpNo());
							divideDtl.setStatus(STATUS10);
							dcount = billOmDivideDtlService.updateDivideDtl4Different(divideDtl);
							if(dcount < 1){
								StringBuffer sbmsg = getMsgTip(differentDtl);
								sbmsg.append("回写分货单明细状态失败!");
								throw new ServiceException(sbmsg.toString());
							}
						}
					}
				}
			}
			
			//修改差异调整单明细
			if(CommonUtil.hasValue(updateList)){
				for (BillOmDivideDifferentDtl differentDtl : updateList) {
					BillOmDivideDifferentDtlKey differentDtlKey=new BillOmDivideDifferentDtlKey();
					differentDtlKey.setLocno(differentDtl.getLocno());
					differentDtlKey.setDifferentNo(differentDtl.getDifferentNo());
					differentDtlKey.setSerialNo(differentDtl.getSerialNo());
					BillOmDivideDifferentDtl dtlEntity=(BillOmDivideDifferentDtl) billOmDivideDifferentDtlService.findById(differentDtlKey);
					if(dtlEntity==null||!STATUS10.equals(dtlEntity.getStatus())){
						StringBuffer sbmsg = getMsgTip(differentDtl);
						sbmsg.append("已删除或状态已改变!");
						throw new ServiceException(sbmsg.toString());
					}
					
					//验证商品和条码的合法性
					Map<String, Object> barcodeParams = new HashMap<String, Object>();
					barcodeParams.put("itemNo", differentDtl.getdItemNo());
					barcodeParams.put("barcode", differentDtl.getdBarcode());
					barcodeParams.put("packageId", "0");
					List<ItemBarcode> barcodeList = itemBarcodeService.findByBiz(null, barcodeParams);
					if(!CommonUtil.hasValue(barcodeList)){
						StringBuffer sbmsg = getDMsgTip(differentDtl);
						sbmsg.append("填写的数据不合法!");
						throw new ServiceException(sbmsg.toString());
					}
					
					dtlEntity.setdItemNo(differentDtl.getdItemNo());
					dtlEntity.setdBarcode(differentDtl.getdBarcode());
					int count = billOmDivideDifferentDtlService.modifyById(dtlEntity);
					if(count < 1){
						StringBuffer sbmsg = getMsgTip(differentDtl);
						sbmsg.append("更新失败,数据已删除或状态改变!");
						throw new ServiceException(sbmsg.toString());
					}
				}
			}
			different.setEdittm(new Date());
			different.setEditor(different.getCreator());
			different.setEditorName(different.getCreatorName());
			different.setCreator(null);
			different.setCreatorName(null);
			billOmDivideDifferentService.modifyById(different);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	public StringBuffer getMsgTip(BillOmDivideDifferentDtl differentDtl){
		StringBuffer sbmsg = new StringBuffer();
		sbmsg.append("客户："+differentDtl.getStoreNo()+",");
		sbmsg.append("箱号："+differentDtl.getBoxNo()+",");
		sbmsg.append("商品："+differentDtl.getsItemNo()+",");
		sbmsg.append("条码："+differentDtl.getsBarcode()+",");
		return sbmsg;
	}
	
	public StringBuffer getDMsgTip(BillOmDivideDifferentDtl differentDtl){
		StringBuffer sbmsg = new StringBuffer();
		sbmsg.append("客户："+differentDtl.getStoreNo()+",");
		sbmsg.append("箱号："+differentDtl.getBoxNo()+",");
		sbmsg.append("商品："+differentDtl.getdItemNo()+",");
		sbmsg.append("条码："+differentDtl.getdBarcode()+",");
		return sbmsg;
	}
	
}