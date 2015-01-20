package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillSmOtherinDtlDto;
import com.yougou.logistics.city.common.enums.CmDefcellCellStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.ItemBarcode;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;
import com.yougou.logistics.city.service.AuthorityUserBrandService;
import com.yougou.logistics.city.service.BillSmOtherinDtlService;
import com.yougou.logistics.city.service.BillSmOtherinService;
import com.yougou.logistics.city.service.CmDefcellService;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
@Service("billSmOtherinDtlManager")
class BillSmOtherinDtlManagerImpl extends BaseCrudManagerImpl implements
		BillSmOtherinDtlManager {
	@Resource
	private BillSmOtherinDtlService billSmOtherinDtlService;
	@Resource
	private BillSmOtherinService billSmOtherinService;	
	@Resource
	private CmDefcellService cmDefcellService;
	@Log
	private Logger log;
	@Resource
	private CmDefcellMapper cmDefcellMapper;
	@Resource
	private ItemManager itemManager;		
	@Resource
	private ItemBarcodeManager itemBarcodeManager;
	@Resource
	private AuthorityUserBrandService authorityUserBrandService;
	
	private static String STATUS10="10";
	  
	@Override
	public BaseCrudService init() {
		return billSmOtherinDtlService;
	}

	@Override
	public int selectContentCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billSmOtherinDtlService.selectContentCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillSmOtherinDtlDto> selectContent(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billSmOtherinDtlService.selectContent(page, orderByField,
					orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	/**
	 * 明细保存前核验
	 * @param billSmOtherin
	 * @param params
	 * @throws ManagerException
	 */
	private <ModelType> void beforeSaveDtl(BillSmOtherin billSmOtherin,Map<CommonOperatorEnum, List<ModelType>> params) throws ManagerException{		
		try {
			String locno = billSmOtherin.getLocno();
			String ownerNo = billSmOtherin.getOwnerNo();
			String otherinNo = billSmOtherin.getOtherinNo();
			
			List<BillSmOtherinDtl> addList=getDtlList(params.get(CommonOperatorEnum.INSERTED));// 新增
			List<BillSmOtherinDtl> delList=getDtlList(params.get(CommonOperatorEnum.DELETED));// 删除
			List<BillSmOtherinDtl> uptList=getDtlList(params.get(CommonOperatorEnum.UPDATED));// 更新
			
			//begin检查主单状态是否可修改
			Map<String, Object> bill = new HashMap<String, Object>();
			bill.put("locno", locno);
			bill.put("ownerNo", ownerNo);
			bill.put("otherinNo", otherinNo);
			bill.put("status", "10");
			List<BillSmOtherin> otherinList;
			otherinList = billSmOtherinService.findByBiz(
					billSmOtherin, bill);
			if (otherinList != null && otherinList.size() > 0) {
			} else {
				throw new ManagerException("当前单据状态不可编辑！");
			}
			//end检查主单状态是否可修改
			
			//begin判断明细是否有重复
			Map<String, Object> findByBizparams = new HashMap<String, Object>();
			findByBizparams.put("locno", locno);
			findByBizparams.put("ownerNo", ownerNo);
			findByBizparams.put("otherinNo", otherinNo);
			
			Map<String,String> dtlMap =new HashMap<String, String>(); 			
			List<BillSmOtherinDtl> oldDtlList=billSmOtherinDtlService.findByBiz(new BillSmOtherinDtl(), findByBizparams);
			String dtlTemp="";
			String repeatMsg="";
			String uuid;
			
			//初始化已持久货的明细到map
			for(BillSmOtherinDtl dtl: oldDtlList){
				dtlTemp=dtl.getCellNo()+"_"+dtl.getItemNo()+"_"+dtl.getSizeNo();
				dtlMap.put(dtl.getRowId().toString(), dtlTemp);
			}
			//已经删除的明细不需比较
			for(BillSmOtherinDtl dtl: delList){
				dtlMap.remove(dtl.getRowId().toString());
			}
			//修改的明细，需从map中删除后再对比，对比完后再放入map
			for(BillSmOtherinDtl dtl: uptList){
				dtlTemp=dtl.getCellNo()+"_"+dtl.getItemNo()+"_"+dtl.getSizeNo();
				dtlMap.remove(dtl.getRowId().toString());				
				if(dtlMap.containsValue(dtlTemp)){
					repeatMsg+="["+dtlTemp+"]";
				}
				dtlMap.put(dtl.getRowId().toString(), dtlTemp);
			}
			//新增的明细对比对完后放入map
			for(BillSmOtherinDtl dtl: addList){
				dtlTemp=dtl.getCellNo()+"_"+dtl.getItemNo()+"_"+dtl.getSizeNo();
				if(dtlMap.containsValue(dtlTemp)){
					repeatMsg+="["+dtlTemp+"]";
				}
				uuid=UUID.randomUUID().toString();
				dtlMap.put(uuid, dtlTemp);
			}
			if(repeatMsg.length()>0){
				throw new ManagerException("存在重复记录如下："+repeatMsg);
			}
			//end判断明细是否有重复
			
			//being校验入库数量是否大于0
			String instorageQtyMsg="";
			for(BillSmOtherinDtl dtl: uptList){
				if(dtl.getInstorageQty()!=null && dtl.getInstorageQty().compareTo(new BigDecimal(1))==-1){
					dtlTemp="["+dtl.getCellNo()+"_"+dtl.getItemNo()+"_"+dtl.getSizeNo()+"]";
					instorageQtyMsg+=dtlTemp;					
				}
			}
			if(instorageQtyMsg.length()>0){
				throw new ManagerException("入库数量必须大于0："+instorageQtyMsg);
			}
			//end校验入库数量是否大于0
						
			//begin校验明细储位是否与单头品质和商品类型一致
			List<CmDefcell> cmDefcellList;
			CmDefcell cmDefcell;
			BillSmOtherin otherin=otherinList.get(0);
			String areaQuality=otherin.getAreaQuality();
			String itemType=otherin.getItemType();
			String cellNoTemp="";
			String cellNoMsg="";
			String cellNoNoExistsMsg="";
			
			Map<String, Object> cmDefcellParams = new HashMap<String, Object>();
			cmDefcellParams.put("locno", locno);
			
			//校验更新明细的储位
			for(BillSmOtherinDtl dtl: uptList){
				cmDefcellParams.put("cellNo", dtl.getCellNo());
				cmDefcellList=cmDefcellService.findByBiz(null, cmDefcellParams);
				cellNoTemp="["+dtl.getCellNo()+"]";
				//储位是否存在
				if(cmDefcellList.size()==0){					
					if(cellNoNoExistsMsg.indexOf(cellNoTemp)==-1){
						cellNoNoExistsMsg+=cellNoTemp;						
					}					
					continue;
				}
				//判断明细品质和商品类型是否和单头一致
				cmDefcell=cmDefcellList.get(0);
				if(!areaQuality.equals(cmDefcell.getAreaQuality())||!itemType.equals(cmDefcell.getItemType())){
					if(cellNoMsg.indexOf(cellNoTemp)==-1){
						cellNoMsg+=cellNoTemp;						
					}
				}
			}
			//校验新增明细的储位
			for(BillSmOtherinDtl dtl: addList){
				cmDefcellParams.put("cellNo", dtl.getCellNo());
				cmDefcellList=cmDefcellService.findByBiz(null, cmDefcellParams);
				cellNoTemp="["+dtl.getCellNo()+"]";
				//储位是否存在
				if(cmDefcellList.size()==0){					
					if(cellNoNoExistsMsg.indexOf(cellNoTemp)==-1){
						cellNoNoExistsMsg+=cellNoTemp;						
					}
					continue;
				}
				//判断明细品质和商品类型是否和单头一致
				cmDefcell=cmDefcellList.get(0);
				if(!areaQuality.equals(cmDefcell.getAreaQuality())||!itemType.equals(cmDefcell.getItemType())){					
					if(cellNoMsg.indexOf(cellNoTemp)==-1){
						cellNoMsg+=cellNoTemp;						
					}
				}
			}
			
			
			if(cellNoNoExistsMsg.length()>0){
				throw new ManagerException("储位不存在："+cellNoNoExistsMsg);
			}
			if(cellNoMsg.length()>0){
				throw new ManagerException("储位品质或商品类型和主单不一致："+cellNoMsg);
			}
			//end校验明细储位是否与单头品质和商品类型一致
			
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			
		}
	}
	
	/**
	 * ModelType类型的明细数据转换
	 * @param modelTypeList
	 * @return
	 */
	private <ModelType> List<BillSmOtherinDtl> getDtlList(List<ModelType> modelTypeList) {
		List<BillSmOtherinDtl> list=new ArrayList<BillSmOtherinDtl>();
		if (CommonUtil.hasValue(modelTypeList)) {
			for (ModelType modelType : modelTypeList) {
				if (modelType instanceof BillSmOtherinDtl) {
					BillSmOtherinDtl vo = (BillSmOtherinDtl) modelType;
					list.add(vo);
				}
			}
		}
		return list;
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addSmOtherinDtl(
			BillSmOtherin billSmOtherin,
			Map<CommonOperatorEnum, List<ModelType>> params)
			throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		boolean flag = false;
		String STAUTS0 = "0";
		try {
			
			Date date = new Date();
			/**************** 修改其它入库单主档信息 *******************/
			String locno = billSmOtherin.getLocno();
			String ownerNo = billSmOtherin.getOwnerNo();
			String otherinNo = billSmOtherin.getOtherinNo();
			
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);// 新增
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);// 删除
			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);// 更新
			
			

			beforeSaveDtl(billSmOtherin, params);
			
			/**************** 新增退仓收货单明细信息 *******************/

			// 删除操作
			if (CommonUtil.hasValue(delList)) {
				flag = false;
				for (ModelType modelType : delList) {
					if (modelType instanceof BillSmOtherinDtl) {
						BillSmOtherinDtl vo = (BillSmOtherinDtl) modelType;
						Short rowId = vo.getRowId();

						BillSmOtherinDtl delParamer = new BillSmOtherinDtl();
						delParamer.setLocno(locno);
						delParamer.setOwnerNo(ownerNo);
						delParamer.setOtherinNo(otherinNo);
						delParamer.setRowId(rowId);

						int a = billSmOtherinDtlService.deleteById(delParamer);
						if (a != 1) {
							throw new ManagerException("删除其它入库信息失败！");
						}
					}
				}
				flag = true;
			}
			// 新增操作
			if (CommonUtil.hasValue(addList)) {
				flag = false;
				// 查询最大的Pid,作为主键
				BillSmOtherinDtl keyObj = new BillSmOtherinDtl();
				keyObj.setOtherinNo(otherinNo);
				keyObj.setLocno(locno);
				keyObj.setOwnerNo(ownerNo);
				short pidNum = (short) billSmOtherinDtlService
						.selectMaxPid(keyObj);
				
				for (ModelType modelType : addList) {
					if (modelType instanceof BillSmOtherinDtl) {
						BillSmOtherinDtl vo = (BillSmOtherinDtl) modelType;
						String cellNo = vo.getCellNo();
						String itemNo = vo.getItemNo();
						String sizeNo = vo.getSizeNo();
						String brandNo = vo.getBrandNO();
						
						BigDecimal instorageQty = vo.getInstorageQty();
						if (instorageQty.doubleValue() <= 0) {
							throw new ManagerException("数量不允许为0！");
						}
						
						Map<String, Object> parmMap = new HashMap<String, Object>();
						parmMap.put("locno", locno);
						parmMap.put("cellNo", cellNo);
						List<CmDefcell> list = this.cmDefcellMapper.selectByParams4Instock(parmMap);
						if (list == null || list.size() == 0) {
							throw new ManagerException("实际储位不存在！");
						}
						if (list.size() > 1) {
							throw new ManagerException(cellNo + "储位非法！");
						}
						CmDefcell cell = list.get(0);
						if (!STAUTS0.equals(cell.getCellStatus())) {
							throw new ManagerException("储位状态必须为可用！");
						}
						if (!STAUTS0.equals(cell.getCheckStatus())) {
							throw new ManagerException("储位必须是非盘点状态！");
						}
						
						BillSmOtherinDtl addDtl = new BillSmOtherinDtl();
						addDtl.setLocno(locno);
						addDtl.setOwnerNo(ownerNo);
						addDtl.setOtherinNo(otherinNo);

						addDtl.setCellNo(cellNo);
						addDtl.setItemNo(itemNo);
						addDtl.setSizeNo(sizeNo);
						addDtl.setBrandNO(brandNo);
						addDtl.setInstorageQty(instorageQty);
						addDtl.setEditor(billSmOtherin.getEditor());
						addDtl.setEditorName(billSmOtherin.getEditorName());
						addDtl.setEdittm(date);
						addDtl.setRowId(++pidNum);
						int a;
						try {
							a = billSmOtherinDtlService.add(addDtl);
						} catch (Exception e) {
							throw new ManagerException("添加其它入库明细失败！");
						}
						if (a < 1) {
							throw new ManagerException("添加其它入库明细失败！");
						}

					}
				}
				flag = true;
			}
			// 更新操作
			if (CommonUtil.hasValue(uptList)) {
				flag = false;
				for (ModelType modelType : uptList) {
					if (modelType instanceof BillSmOtherinDtl) {
						BillSmOtherinDtl vo = (BillSmOtherinDtl) modelType;
						String cellNo = vo.getCellNo();
						BigDecimal instorageQty = vo.getInstorageQty();
						Short rowId = vo.getRowId();
						
						Map<String, Object> parmMap = new HashMap<String, Object>();
						parmMap.put("locno", locno);
						parmMap.put("cellNo", cellNo);
						List<CmDefcell> list = this.cmDefcellMapper.selectByParams4Instock(parmMap);
						if (list == null || list.size() == 0) {
							throw new ManagerException("实际储位不存在！");
						}
						if (list.size() > 1) {
							throw new ManagerException(cellNo + "储位非法！");
						}
						CmDefcell cell = list.get(0);
						if (!STAUTS0.equals(cell.getCellStatus())) {
							throw new ManagerException("储位状态必须为可用！");
						}
						if (!STAUTS0.equals(cell.getCheckStatus())) {
							throw new ManagerException("储位必须是非盘点状态！");
						}
						
						BillSmOtherinDtl paramer = new BillSmOtherinDtl();
						paramer.setLocno(locno);
						paramer.setOwnerNo(ownerNo);
						paramer.setOtherinNo(otherinNo);
						paramer.setRowId(rowId);
						paramer.setEditor(billSmOtherin.getEditor());
						paramer.setEditorName(billSmOtherin.getEditorName());
						paramer.setEdittm(date);
						paramer.setCellNo(cellNo);
						paramer.setInstorageQty(instorageQty);
						int a = billSmOtherinDtlService.modifyById(paramer);
						if (a != 1) {
							throw new ManagerException("更新其它入库信息失败！");
						}
					}
				}
				flag = true;
			}			
			
			billSmOtherinService.modifyById(billSmOtherin);
			
			//查询明细是否重复
			Map<String, Object> findDupParams = new HashMap<String, Object>();
			findDupParams.put("locno", locno);
			findDupParams.put("ownerNo", ownerNo);
			findDupParams.put("otherinNo", otherinNo);
			List<BillSmOtherinDtl> dupList=billSmOtherinDtlService.findDuplicateRecord(findDupParams);
			if(dupList.size()>0){
				StringBuilder msg = new StringBuilder();
				for(BillSmOtherinDtl dtl : dupList){
					msg.append("<br>储位:").append(dtl.getCellNo()).append("<br>商品:").append(dtl.getItemNo()).append("<br>尺码:").append(dtl.getSizeNo()).append("<br>");
				}
				msg.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
				throw new ManagerException(msg.toString());
			}
			
			if (flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "明细保存成功");
			} else {
				throw new ManagerException("当前明细保存失败！");
			}
			return mapObj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e.getMessage());
		}
	}
	@Override
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billSmOtherinDtlService.selectSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	

	@Override
	public SumUtilMap<String, Object> findPageSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billSmOtherinDtlService.selectPageSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void excelImportData(List<BillSmOtherinDtl> list, String locno, String otherinNo, String ownerNo,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			//批量insert的数据量
			int batchInsertSize = 500;
			//begin检查主单状态是否可修改
			BillSmOtherin smOtherinParams = new BillSmOtherin();
			smOtherinParams.setLocno(locno);
			smOtherinParams.setOwnerNo(ownerNo);
			smOtherinParams.setOtherinNo(otherinNo);
			BillSmOtherin billSmOtherin = billSmOtherinService.findById(smOtherinParams);

			if (billSmOtherin == null || !STATUS10.equals(billSmOtherin.getStatus())) {
				throw new ManagerException("该单据不存在或状态已改变。");
			}

			//品牌权限
			List<String> brandNoList = authorityUserBrandService.findByUserIdBrandList(authorityParams.getUserId(),
					Integer.valueOf(authorityParams.getSystemNoVerify()),
					Integer.valueOf(authorityParams.getAreaSystemNoVerify()));

			//主档属性
			String itemType = billSmOtherin.getItemType();
			//主档品质
			String quality = billSmOtherin.getAreaQuality();

			CmDefcell cmDefcellParams = new CmDefcell();
			CmDefcell cmDefcell;
			Map<String, Object> itemParams = new HashMap<String, Object>();
			List<Item> itemList;
			Map<String, Object> itemBarcodeParams = new HashMap<String, Object>();
			List<ItemBarcode> itemBarcodeList;
			//导入文件中的行数
			int rowCount = 2;

			//查找明细的最大序号
			BillSmOtherinDtl smOtherinDtlParams = new BillSmOtherinDtl();
			smOtherinDtlParams.setOtherinNo(otherinNo);
			smOtherinDtlParams.setLocno(locno);
			smOtherinDtlParams.setOwnerNo(ownerNo);
			short rowId = (short) billSmOtherinDtlService.selectMaxPid(smOtherinDtlParams);
			
			String brandNO="";			
			StringBuilder msg = new StringBuilder();

			for (BillSmOtherinDtl dtl : list) {
				cmDefcellParams.setLocno(locno);
				cmDefcellParams.setCellNo(dtl.getCellNo());
				cmDefcell = cmDefcellMapper.selectByPrimaryKey(cmDefcellParams);
				if (cmDefcell == null) {
					msg.append("第" + rowCount + "行储位" + dtl.getCellNo() + "不存在。\\r\\n");
				}

				if (cmDefcell!=null && (!CmDefcellCellStatusEnums.CELLSTATUS_0.getStatus().equals(cmDefcell.getCellStatus())
						|| !CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus().equals(cmDefcell.getCheckStatus()))) {
					msg.append("第" + rowCount + "行储位" + dtl.getCellNo() + "的状态为不可用。\\r\\n");
				}
				
				if (cmDefcell!=null && (!itemType.equals(cmDefcell.getItemType()) || !quality.equals(cmDefcell.getAreaQuality()))) {
					msg.append("第" + rowCount + "行储位" + dtl.getCellNo() + "的品质、属性和入库单主档不一致。\\r\\n");
				}

				//查询商品
				itemParams.put("itemNo", dtl.getItemNo());
				itemList = itemManager.findByBiz(null, itemParams);
				if (itemList.size() == 0) {
					msg.append("第" + rowCount + "行商品编码" + dtl.getItemNo() + "不存在。\\r\\n");
				}else{
					//查找尺码
					itemBarcodeParams.put("itemNo", dtl.getItemNo());
					itemBarcodeParams.put("sizeNo", dtl.getSizeNo());
					itemBarcodeParams.put("packQty", Short.valueOf("0"));
					itemBarcodeList = itemBarcodeManager.findByBiz(null, itemBarcodeParams);
					if (itemBarcodeList.size() == 0) {
						msg.append("第" + rowCount + "行商品尺码" + dtl.getSizeNo() + "不存在。\\r\\n");
					}

					brandNO = itemList.get(0).getBrandNo();
					
					if(!brandNoList.contains(brandNO)){
						msg.append("第" + rowCount + "行商品"+dtl.getItemNo()+",用户无品牌权限。\\r\\n");
					}
				}
				
				if(dtl.getInstorageQty().compareTo(new BigDecimal(0))<=0){
					msg.append("第" + rowCount + "行数量需大于0\\r\\n");
				}
				
				dtl.setBrandNO(brandNO);
				dtl.setLocno(locno);
				dtl.setOwnerNo(ownerNo);
				dtl.setOtherinNo(otherinNo);
				dtl.setRowId(++rowId);

				rowCount++;
			}
			
			if(msg.length() > 0){
				throw new ManagerException("<span style='color:red;'>请确认导入的 数据是否正确</span><br><br><textarea rows='5' cols='40'>"+msg.toString()+"</textarea>");
			}
			//保存明细
			for(int i=0;i<list.size();){
				i += batchInsertSize;
				if(i>list.size()){
					billSmOtherinDtlService.batchInsertDtl(list.subList(i-batchInsertSize, list.size()));
				}else{
					billSmOtherinDtlService.batchInsertDtl(list.subList(i-batchInsertSize, i));
				}				
			}

			//查询明细是否重复
			Map<String, Object> findDupParams = new HashMap<String, Object>();
			findDupParams.put("locno", locno);
			findDupParams.put("ownerNo", ownerNo);
			findDupParams.put("otherinNo", otherinNo);
			List<BillSmOtherinDtl> dupList = billSmOtherinDtlService.findDuplicateRecord(findDupParams);
			if (dupList.size() > 0) {			
				msg.append("重复记录如下:\\r\\n");
				for (BillSmOtherinDtl dtl : dupList) {
					msg.append("储位:").append(dtl.getCellNo()).append("商品:").append(dtl.getItemNo())
							.append("尺码:").append(dtl.getSizeNo()).append("\\r\\n");
				}				
			}
			if(msg.length() > 0){
				throw new ManagerException("<span style='color:red;'>请确认导入的 数据是否正确</span><br><br><textarea rows='5' cols='40'>"+msg.toString()+"</textarea>");
			}

		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
		}
	}
	@Override
	public List<BillSmOtherinDtl> findDtlSysNo(BillSmOtherinDtl billSmOtherinDtl, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billSmOtherinDtlService.findDtlSysNo(billSmOtherinDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	@Override
	public Map<String, Object> findDtlSysNoByPage(BillSmOtherinDtl billSmOtherinDtl,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billSmOtherinDtlService.findDtlSysNoByPage(billSmOtherinDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}