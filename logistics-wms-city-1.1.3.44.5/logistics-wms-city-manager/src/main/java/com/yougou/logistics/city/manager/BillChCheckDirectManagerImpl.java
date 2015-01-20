package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillChCheckDirectBoxDto;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.enums.BillChCheckDirectStatusEnums;
import com.yougou.logistics.city.common.enums.BillChPlanLimitBrandFlagEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCellStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BillChCheckDirectBox;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanDtl;
import com.yougou.logistics.city.common.model.BillChPlanDtlBrand;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.service.BillChCheckDirectBoxService;
import com.yougou.logistics.city.service.BillChPlanDtlBrandService;
import com.yougou.logistics.city.service.BillChCheckDirectService;
import com.yougou.logistics.city.service.BillChPlanDtlService;
import com.yougou.logistics.city.service.BillChPlanService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ConContentService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 14:54:50 CST 2013
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
@Service("billChCheckDirectManager")
class BillChCheckDirectManagerImpl extends BaseCrudManagerImpl implements BillChCheckDirectManager {
	@Log
	private Logger log;
	@Resource
	private BillChCheckDirectService billChCheckDirectService;
	@Resource
	private BillChPlanDtlService billChPlanDtlService;
	@Resource
	private BillChPlanService billChPlanService;
	@Resource
	private BillChCheckDirectBoxService billChCheckDirectBoxService;
	@Resource
	private CmDefcellService cmDefcellService;
	@Resource
	private ProcCommonService procCommonService;
    @Resource
    private BmContainerService bmContainerService;
	
    @Resource
	private BillChPlanDtlBrandService billChPlanDtlBrandService;
	@Resource
	private ConContentService conContentService;;
	
	//批量插入提交事务的数量
	private final static int BATH_INSERT_COUNT = 1000;
	
	@Override
	public BaseCrudService init() {
		return billChCheckDirectService;
	}

	@Override
	public int findDirectCount(BillChCheckDirectDto dto) throws ManagerException {
		try {
			return billChCheckDirectService.findDirectCount(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillChCheckDirectDto> findDirectList(BillChCheckDirectDto dto, String orderBy, SimplePage page) throws ManagerException {
		try {
			return billChCheckDirectService.findDirectList(dto, orderBy, page);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<Brand> findBrandInDirect(BillChCheckDirectDto dto) throws ManagerException {
		try {
			return billChCheckDirectService.findBrandInDirect(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String createDirect(BillChPlan billChPlanParam,SystemUser user, AuthorityParams authorityParams) throws ManagerException {		
		try {
			int pageSize = 1000;
			BillChPlan billChPlan=billChPlanService.findById(billChPlanParam);
			
			if (billChPlan==null) {
				throw new ManagerException("计划单[" + billChPlanParam.getPlanNo() + "]不存在,定位失败!");
			}
			
			String planNo = billChPlan.getPlanNo();
			String planType = billChPlan.getPlanType();
			String ownerNo = billChPlan.getOwnerNo();
			String locno = billChPlan.getLocno();
			String limitBrandFlag=billChPlan.getLimitBrandFlag();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("planNo", planNo);
			params.put("locno", locno);
			
			List<BillChCheckDirect> list = null;
			int dtlLen = 0;
			if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
				dtlLen = billChPlanDtlService.findCount(params,authorityParams,DataAccessRuleEnum.BRAND);
			}else{
				dtlLen = billChPlanDtlService.findCount(params);
			}
			if (dtlLen < 1) {
				throw new ManagerException("计划单[" + planNo + "]没有明细,定位失败!");
			}
			if (planType == null) {
				throw new ManagerException("计划单[" + planNo + "]没有选择定位类型,定位失败!");
			} else if (planType.equals("0")) {//按商品盘
				params.put("quality", billChPlan.getQuality());
				params.put("itemType", billChPlan.getItemType());
				params.put("limitBrandFlag", limitBrandFlag);
				
				//全盘需校验所有商品是否都可以盘点
				if(BillChPlanLimitBrandFlagEnums.limitBrandFlag_1.getStatus().equals(limitBrandFlag)){
					if(!billChCheckDirectService.beforeDirectCheck(params)){
						throw new ManagerException("全盘的商品存在预上或预下操作，生成定位失败。");
					}
				}				
				//list = billChCheckDirectService.findDirectByItem(params, authorityParams);
				//整箱记账的调整
				list = billChCheckDirectService.findDirectByItem4RQLS(params, authorityParams);
				List<BillChCheckDirect> rqzxList = billChCheckDirectService.findDirectByItem4RQZX(params, authorityParams);
				if(null != rqzxList && !rqzxList.isEmpty()){
					list.addAll(rqzxList);
				}
				
			} else {//按储位盘
			
			    List<BillChPlanDtlBrand> bcpdbs = billChPlanDtlBrandService.findByBiz(null, params);
				if(CommonUtil.hasValue(bcpdbs)){//定位时添加品牌限制条件
					String brandNoArrStr = "";
					for(BillChPlanDtlBrand bcpdb:bcpdbs){
						brandNoArrStr += "'" + bcpdb.getBrandNo() + "',";
					}
					params.put("brandNoArrStr", brandNoArrStr.substring(0, brandNoArrStr.length()-1));
				}
				//list = billChCheckDirectService.findDirectByCell(params, null);
				
				//此处要修改品牌权限的逻辑 add by zuo.sw 2014-11-19
				list = billChCheckDirectService.findDirectByCell4RQLS(params, null);
				List<BillChCheckDirect> rqzxList = billChCheckDirectService.findDirectByCell4RQZX(params, null);
				if(null!=rqzxList && CollectionUtils.isNotEmpty(rqzxList)){
					list.addAll(rqzxList);
				}
				
				//去除不可盘点的储位
				params.put("excludeCannotCheckCell", "true");
				List<BillChPlanDtl> planDtlList = billChPlanDtlService.findByBiz(new BillChPlanDtl(), params);
				BillChCheckDirect checkDirect = null;
				if(list == null || list.size() == 0){//无定位信息时，每个计划明细生成一个定位单(item_no为N)
					list = new ArrayList<BillChCheckDirect>();
					if(planDtlList == null || planDtlList.size() == 0){
						throw new ManagerException("计划单[" + planNo + "]没有查询到可定位的计划明细,定位失败!");
					}else{
						for(BillChPlanDtl planDtl:planDtlList){
							CmDefcell cmDefcell = new CmDefcell();
							cmDefcell.setLocno(locno);
							cmDefcell.setCellNo(planDtl.getCellNo());
							cmDefcell = cmDefcellService.findById(cmDefcell);
														
							//储位为储位状态为可用、盘点状态 为可用时，可定位
							if (!CmDefcellCellStatusEnums.CELLSTATUS_0.getStatus().equals(cmDefcell.getCellStatus())
									|| !CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus().equals(cmDefcell.getCheckStatus())) {
								continue;
							}
							
							checkDirect = new BillChCheckDirect();
							checkDirect.setLocno(locno);
							checkDirect.setOwnerNo(ownerNo);
							checkDirect.setPlanNo(planNo);
							checkDirect.setItemNo("N");
							checkDirect.setBrandNo("N");
							checkDirect.setSizeNo("N");
							checkDirect.setCellNo(planDtl.getCellNo());
							checkDirect.setDirectSerial(null);
							checkDirect.setItemQty(new BigDecimal(0));
							
							if(StringUtils.isBlank(cmDefcell.getItemType())){
								checkDirect.setItemType("0");
							}else{
								checkDirect.setItemType(cmDefcell.getItemType());
							}
							if(StringUtils.isBlank(cmDefcell.getAreaQuality())){
								checkDirect.setQuality("0");
							}else{
								checkDirect.setQuality(cmDefcell.getAreaQuality());
							}
							list.add(checkDirect);
						}
					}
				}else{//有定位信息，且存在无库存盘点
					String planDtlCellNo = null;
					boolean exist;
					for(BillChPlanDtl planDtl:planDtlList){
						planDtlCellNo = planDtl.getCellNo();
						exist = false;
						for(BillChCheckDirect dto : list){
							if(dto.getCellNo().equals(planDtlCellNo)){
								exist = true;
								break;
							}
						}
						if(!exist){							
							CmDefcell cmDefcell = new CmDefcell();
							cmDefcell.setLocno(locno);
							cmDefcell.setCellNo(planDtl.getCellNo());
							cmDefcell = cmDefcellService.findById(cmDefcell);	
							
							//储位为储位状态为可用、盘点状态 为可用时，可定位
							if(!CmDefcellCellStatusEnums.CELLSTATUS_0.getStatus().equals(cmDefcell.getCellStatus()) 
									|| !CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus().equals(cmDefcell.getCheckStatus())){
								continue;
							}
							
							checkDirect = new BillChCheckDirect();
							checkDirect.setLocno(locno);
							checkDirect.setOwnerNo(ownerNo);
							checkDirect.setPlanNo(planNo);
							checkDirect.setItemNo("N");
							checkDirect.setBrandNo("N");
							checkDirect.setSizeNo("N");
							checkDirect.setCellNo(planDtl.getCellNo());
							checkDirect.setDirectSerial(null);
							checkDirect.setItemQty(new BigDecimal(0));
							
							if(StringUtils.isBlank(cmDefcell.getItemType())){
								checkDirect.setItemType("0");
							}else{
								checkDirect.setItemType(cmDefcell.getItemType());
							}
							if(StringUtils.isBlank(cmDefcell.getAreaQuality())){
								checkDirect.setQuality("0");
							}else{
								checkDirect.setQuality(cmDefcell.getAreaQuality());
							}
							list.add(checkDirect);
						}
					}
				}
			}
			if (list == null || list.size() == 0) {
				throw new ManagerException("计划单[" + planNo + "]没有查询到定位信息,定位失败!");
			} else {
				
				//容器记账，锁定箱子  占用，status=1，optBillNo，optBillType都要传
				List<BmContainer> lstContainer = new  ArrayList<BmContainer>();
				Map<String,String> returnMap = new HashMap<String,String>();
				
				Date now = new Date();
				int a = 0;
				for (BillChCheckDirect dto : list) {
					dto.setPlanNo(planNo);
					dto.setOwnerNo(ownerNo);
					dto.setPlanType(planType);
					dto.setStatus(BillChCheckDirectStatusEnums.CREATE.getStatus());
					dto.setRequestDate(now);
					//锁定箱信息
					if(StringUtils.isNotBlank(dto.getLabelNo())&& !"N".equals(dto.getLabelNo()) && !returnMap.containsKey(dto.getLabelNo())){
						BmContainer  bc = new BmContainer();
						bc.setLocno(locno);
						bc.setEdittm(DateUtil.getCurrentDateTime());
						bc.setEditor(user.getLoginName());
						bc.setStatus("1");
						bc.setOptBillNo(planNo);
						bc.setOptBillType(ContainerTypeEnums.CH.getOptBillType());
						bc.setConNo(dto.getLabelNo());
						lstContainer.add(bc);
						returnMap.put(dto.getLabelNo(), dto.getLabelNo());
					}
					
					/*a = billChCheckDirectService.add(dto);
					if (a < 1) {
						throw new ManagerException("计划单[" + planNo + "]保存定位信息失败!");
					}*/
				}
				for(int i=0;i<list.size();){
					i += pageSize;
					if(i>list.size()){
						billChCheckDirectService.batchInsertDtl(list.subList(i-pageSize, list.size()));
					}else{
						billChCheckDirectService.batchInsertDtl(list.subList(i-pageSize, i));
					}					
				}
				
				//容器锁定
				if(CollectionUtils.isNotEmpty(lstContainer)){
					bmContainerService.batchUpdate(lstContainer);
				}
				
				BillChPlan plan = new BillChPlan();
				plan.setLocno(locno);
				plan.setPlanNo(planNo);
				plan.setSourceStatus(BillChPlanStatusEnums.AUDIT.getValue());
				plan.setStatus(BillChPlanStatusEnums.START.getValue());
				plan.setBeginDate(new Date());
				a = billChPlanService.modifyById(plan);
				if (a < 1) {
					throw new ManagerException("计划单【" + planNo + "】状态不为【"+BillChPlanStatusEnums.AUDIT.getText()+"】,定位失败!");
				}
				
				//置储位盘点状态
				//Set<String> cellHadLockSet=new HashSet<String>();
				Map<String, Object> modifyConStatusParams = new HashMap<String, Object>();//锁定库存状态所需参数
				modifyConStatusParams.put("locno", locno);
				modifyConStatusParams.put("status","1");
				modifyConStatusParams.put("sourceStatus","0");
				modifyConStatusParams.put("editor",user.getLoginName());
				modifyConStatusParams.put("edittm",new Date());
				modifyConStatusParams.put("editorname",user.getUsername());
				
				for(BillChCheckDirect direct : list){
					String cellNo=direct.getCellNo();
					/* 暂时取消掉【置储位盘点状态为盘点】的功能——JYS
					if(cellHadLockSet.contains(cellNo)){
						continue;
					}else{
						cellHadLockSet.add(cellNo);
					}
					CmDefcell cdParams=new CmDefcell();
					cdParams.setLocno(locno);
					cdParams.setCellNo(cellNo);
					cdParams.setCheckStatus(CmDefcellCheckStatusEnums.CHECKSTATUS_3.getStatus());//盘点
					cdParams.setEdittm(new Date());
					cmDefcellService.modifyById(cdParams);*/			
					
					//将该储位下的所有库存的盘点状态更新为盘点
					if(!"N".equals(direct.getItemNo())){
						//procCommonService.UpdateContentStatus(locno, null, cellNo, "1", null, null, editor);
						modifyConStatusParams.put("itemNo", direct.getItemNo());
						modifyConStatusParams.put("cellNo", cellNo);
						modifyConStatusParams.put("barcode", direct.getBarcode());
						modifyConStatusParams.put("itemType", direct.getItemType());
						modifyConStatusParams.put("quality", direct.getQuality());
						conContentService.modifyStatus(modifyConStatusParams);
					}
					
				}
				
				//更新库存盘点状态为盘点
//				Map<String, Object> directParams = new HashMap<String, Object>();
//				directParams.put("planNo", planNo);
//				directParams.put("locno", locno);
//				//directParams.put("statusNotEquals16", "1");
//				List<BillChCheckDirectDto> directDtoList=billChCheckDirectService.findDirectAndContent(directParams);
//				for(BillChCheckDirectDto dto:directDtoList){
//					procCommonService.UpdateContentStatus(locno, dto.getContentId(), null, "1", null, null, editor);
//				}
				
			}
			
			//容器记账，释放锁定箱子  释放，  status=0，falg不为空
//			List<BmContainer> lstContainer = new  ArrayList<BmContainer>();
//			BmContainer  bc = new  BmContainer();
//			bc.setStatus("0");
//			bc.setFalg("A");
//			lstContainer.add(bc);
//			bmContainerService.batchUpdate(lstContainer);

			
			//容器记账调整-批量写入盘点定位容器的数据 add by zuo.sw
			//this.updateChCheckDirect(locno, ownerNo, planNo, authorityParams);
			
		} catch (Exception e) {
			log.error("=============="+e.getMessage(),e);
			throw new ManagerException(e);
		}
		return null;
	}
	
	/**
	 * 写入盘点定位容器表的数据
	 * @throws ManagerException
	 * @throws ServiceException 
	 */
	private  void updateChCheckDirect(String locno,String ownerNo,String planNo,AuthorityParams authorityParams)throws Exception{
		List<BillChCheckDirectBox> lstDirectBox = new ArrayList<BillChCheckDirectBox>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("locno", locno);
		map.put("ownerNo", ownerNo);
		map.put("planNo", planNo);
		List<BillChCheckDirect> lstCheckDirect = billChCheckDirectService.selectDirectCellNoByGroup(map);
		for(BillChCheckDirect dto : lstCheckDirect){
			Map<String,Object> boxNoMap = new HashMap<String,Object>();
			boxNoMap.put("locno", locno);
			boxNoMap.put("ownerNo", ownerNo);
			boxNoMap.put("cellNo", dto.getCellNo());
			boxNoMap.put("itemType", dto.getItemType());
			boxNoMap.put("quality", dto.getQuality());
			List<BillChCheckDirectBoxDto> lstBoxDto = billChCheckDirectService.selectDirectBoxNoList(boxNoMap, authorityParams);
			for(BillChCheckDirectBoxDto boxDto : lstBoxDto){
				BillChCheckDirectBox directBoxObj = new  BillChCheckDirectBox();
				directBoxObj.setLocno(locno);
				directBoxObj.setOwnerNo(ownerNo);
				directBoxObj.setPlanNo(planNo);
				directBoxObj.setBoxNo(boxDto.getBoxNo());
				directBoxObj.setCellNo(boxDto.getCellNo());
				directBoxObj.setItemType(dto.getItemType());
				directBoxObj.setQuality(dto.getQuality());
				directBoxObj.setItemQty(boxDto.getReceiptQty());
				directBoxObj.setFlag("0");
				lstDirectBox.add(directBoxObj);
			}
		}
		//批量插入
		if(null!=lstDirectBox && !lstDirectBox.isEmpty()){
			for(int i=0;i<lstDirectBox.size();){
				i += BATH_INSERT_COUNT;
				if(i>lstDirectBox.size()){
					billChCheckDirectBoxService.insert4Bath(lstDirectBox.subList(i-BATH_INSERT_COUNT, lstDirectBox.size()));
				}else{
					billChCheckDirectBoxService.insert4Bath(lstDirectBox.subList(i-BATH_INSERT_COUNT, i));
				}					
			}
		}
	}

	@Override
	public List<BillChCheckDirect> selectPlanNo(Map<String, String> map, SimplePage page) throws ManagerException {
		try {
			return billChCheckDirectService.selectPlanNo(map, page);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectPlanNoCount(Map<String, String> map) throws ManagerException {
		try {
			return billChCheckDirectService.selectPlanNoCount(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String changeDirect(String paramStr, String locno, String planNo,String startStatus,String targetStatus,SystemUser user) throws ManagerException {
		BillChPlan billChPlan = new BillChPlan();
		billChPlan.setLocno(locno);
		billChPlan.setPlanNo(planNo);
		int pageSize = 500;
		try {
			billChPlan = billChPlanService.findById(billChPlan);
			String planStatus = billChPlan.getStatus();
			if(StringUtils.isBlank(planStatus) || !BillChPlanStatusEnums.START.getValue().equals(planStatus)){
				throw new ManagerException("计划单[" + planNo + "]的状态不为【发起】");
			}
			String [] directStrArray = paramStr.split("\\|");
			String [] directArray = null;
			String directStatus = "";
			//int a = 0;
			List<BillChCheckDirect> list = new ArrayList<BillChCheckDirect>();
			for(String directStr:directStrArray){
				directArray = directStr.split("_");
				BillChCheckDirect billChCheckDirect = new BillChCheckDirect();
				billChCheckDirect.setLocno(locno);
				//billChCheckDirect.setSourceStatus(startStatus);
				//billChCheckDirect.setStatus(targetStatus);
				billChCheckDirect.setOwnerNo(directArray[0]);
				billChCheckDirect.setDirectSerial(Long.parseLong(directArray[1]));
				
				billChCheckDirect = billChCheckDirectService.findById(billChCheckDirect);
				if(billChCheckDirect == null){
					throw new ManagerException("存在已经失效的定位单");
				}
				directStatus = billChCheckDirect.getStatus();
				if(!startStatus.equals(directStatus)){
					throw new ManagerException("存在定位单状态不为【"+BillChCheckDirectStatusEnums.getText(startStatus)+"】");
				}
				billChCheckDirect.setStatus(targetStatus);
				billChCheckDirect.setSourceStatus(startStatus);
				list.add(billChCheckDirect);
			}
			for(int i=0;i<list.size();){
				i += pageSize;
				if(i>list.size()){
					billChCheckDirectService.batchUpdate4Status(list.subList(i-pageSize, list.size()));
				}else{
					billChCheckDirectService.batchUpdate4Status(list.subList(i-pageSize, i));
				}
				
			}
			Map<String, Object> modifyConStatusParams = new HashMap<String, Object>();//锁定库存状态所需参数
			modifyConStatusParams.put("locno", locno);
			modifyConStatusParams.put("editor",user.getLoginName());
			modifyConStatusParams.put("edittm",new Date());
			modifyConStatusParams.put("editorname",user.getUsername());
			//取消定位操作
			if(BillChCheckDirectStatusEnums.CANCEL.getStatus().equals(targetStatus)){
				//查找定位记录为新建状态的储位
				Map<String, Object> cellNoParams = new HashMap<String, Object>();
				cellNoParams.put("planNo", planNo);
				cellNoParams.put("locno", locno);
				cellNoParams.put("status", BillChCheckDirectStatusEnums.CREATE.getStatus());
				List<String> cellNoList=billChCheckDirectService.findDirectCellNo(cellNoParams);
				
				//如果刚取消的定位的储位，在新建状态的定位记录中不存在，则该储位的盘点状态需标为可用		
				//Set<String> cellHadUpdateSet=new HashSet<String>();
				for(BillChCheckDirect direct:list){
					/*if(cellHadUpdateSet.contains(direct.getCellNo())){
						continue;
					}
					boolean cellExist=false;
					for(String cellNo:cellNoList){
						if(direct.getCellNo().equals(cellNo)){
							cellExist=true;
							break;
						}
					}*/
					
					//if(!cellExist){
					//	cellHadUpdateSet.add(direct.getCellNo());
						
						CmDefcell cdParams=new CmDefcell();
						cdParams.setLocno(locno);
						cdParams.setCellNo(direct.getCellNo());
						cdParams.setCheckStatus(CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus());//盘点状态为可用
						cdParams.setEdittm(new Date());
						//cmDefcellService.modifyById(cdParams);	//取消定位是不需要将储位盘点状态置为可用(与恢复定位定位对应)——JYS
						
						//将该储位下的所有库存的盘点状态更新为可用
						//procCommonService.UpdateContentStatus(locno, null, direct.getCellNo(), "0", null, null, editor);
						modifyConStatusParams.put("status","0");
						modifyConStatusParams.put("sourceStatus","1");
						modifyConStatusParams.put("itemNo", direct.getItemNo());
						modifyConStatusParams.put("cellNo", direct.getCellNo());
						modifyConStatusParams.put("barcode", direct.getBarcode());
						modifyConStatusParams.put("itemType", direct.getItemType());
						modifyConStatusParams.put("quality", direct.getQuality());
						conContentService.modifyStatus(modifyConStatusParams);
						
					//}
				}
			}else{
				//恢复定位操作	
				//置储位盘点状态
				//Set<String> cellHadLockSet=new HashSet<String>();
				for(BillChCheckDirect direct : list){
					String cellNo=direct.getCellNo();
					/* 恢复定位 暂时取消掉【置储位盘点状态为盘点】的功能——JYS
					if(cellHadLockSet.contains(cellNo)){
						continue;
					}else{
						cellHadLockSet.add(cellNo);
					}
					CmDefcell cdParams=new CmDefcell();
					cdParams.setLocno(locno);
					cdParams.setCellNo(cellNo);
					cdParams.setCheckStatus(CmDefcellCheckStatusEnums.CHECKSTATUS_3.getStatus());//状态为盘点
					cdParams.setEdittm(new Date());
					cmDefcellService.modifyById(cdParams);	*/
					
					//将该储位下的所有库存的盘点状态更新为盘点
					//procCommonService.UpdateContentStatus(locno, null, cellNo, "1", null, null, editor);
					modifyConStatusParams.put("status","1");
					modifyConStatusParams.put("sourceStatus","0");
					modifyConStatusParams.put("itemNo", direct.getItemNo());
					modifyConStatusParams.put("cellNo", direct.getCellNo());
					modifyConStatusParams.put("barcode", direct.getBarcode());
					modifyConStatusParams.put("itemType", direct.getItemType());
					modifyConStatusParams.put("quality", direct.getQuality());
					conContentService.modifyStatus(modifyConStatusParams);
				}	
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return null;
	}

	@Override
	public Map<String, Object> selectAllCellCountAndStockCount(
			BillChCheckDirectDto dto) throws ManagerException {
		try {
			return billChCheckDirectService.selectAllCellCountAndStockCount(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}
