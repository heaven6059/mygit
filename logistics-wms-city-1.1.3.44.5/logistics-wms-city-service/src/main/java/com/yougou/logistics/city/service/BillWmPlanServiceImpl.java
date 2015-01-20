package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.BillConStoreLockEnums;
import com.yougou.logistics.city.common.enums.BillHmPlanBusinessTypeEnums;
import com.yougou.logistics.city.common.enums.BillHmPlanPlanTypeEnums;
import com.yougou.logistics.city.common.enums.BillHmPlanStatusEnums;
import com.yougou.logistics.city.common.enums.BillWmPlanStatusEnums;
import com.yougou.logistics.city.common.enums.BillWmRequestStatusEnums;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.model.BillWmRequest;
import com.yougou.logistics.city.common.model.BillWmRequestDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillConStorelockDtlMapper;
import com.yougou.logistics.city.dal.database.BillConStorelockMapper;
import com.yougou.logistics.city.dal.database.BillHmPlanMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillWmPlanDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillWmPlanMapper;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Mar 21 13:37:10 CST 2014
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
@Service("billWmPlanService")
class BillWmPlanServiceImpl extends BaseCrudServiceImpl implements
	BillWmPlanService {
    @Log
    private Logger log;
    @Resource
    private BillWmPlanMapper billWmPlanMapper;
    @Resource
    private BillHmPlanService billHmPlanService;
    @Resource
    private BillHmPlanDtlService billHmPlanDtlService;
    
    @Resource
    private CmDefcellService cmDefcellService;

    @Resource
    private BillWmPlanDtlMapper billWmPlanDtlMapper;

    @Resource
    private ProcCommonService procCommonService;
    
    @Resource
    private BillConStorelockMapper billConStorelockMapper;
    
    @Resource
    private BillConStorelockDtlMapper billConStorelockDtlMapper;
    
    @Resource
    private ItemMapper itemMapper;
    
    @Resource
    private BillAccControlService billAccControlService;
    
    @Resource
    private BillWmRequestService billWmRequestService;
    
    @Resource
    private BillWmRequestDtlService billWmRequestDtlService;
    
    @Resource
    private BillHmPlanMapper billHmPlanMapper;
    
    @Resource
    private BillConStorelockDtlService billConStorelockDtlService;
    
    private final static String STATUS10 = "10";
    
    private final static String PLANTYPE11 = "11";

    private final static String PLANTYPE12 = "12";
    
   // private final static String PLANTYPE13 = "13";
    
   // private final static String PLANTYPE14 = "14";

    @Override
    public BaseCrudMapper init() {
	return billWmPlanMapper;
    }

    @Override
    public void saveMain(BillWmPlan billWmPlan) throws ServiceException {
	try {
	    // 新增
	    if (StringUtils.isEmpty(billWmPlan.getPlanNo())) {
		String planNO = procCommonService.procGetSheetNo(
			billWmPlan.getLocno(), CNumPre.WM_RECEDE_WP);
		billWmPlan.setPlanNo(planNO);
		billWmPlan
			.setStatus(BillWmPlanStatusEnums.STATUS10.getStatus());
		billWmPlanMapper.insertSelective(billWmPlan);
	    } else {// 修改
		billWmPlanMapper.updateByPrimaryKeySelective(billWmPlan);
	    }
	} catch (ServiceException e) {
	    throw e;
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public void deletePlan(String keyStr, String locnoNo)
	    throws ServiceException {
	BillWmPlan plan = null;
	if (StringUtils.isNotEmpty(keyStr)) {
	    String keys[] = keyStr.split(",");
	    for (String key : keys) {
		String[] subKyes = key.split("\\|");
		plan = new BillWmPlan();
		plan.setLocno(locnoNo);
		plan.setPlanNo(subKyes[0]);
		plan.setOwnerNo(subKyes[1]);
		plan.setSourceStatus(BillWmPlanStatusEnums.STATUS10.getStatus());
		int count = billWmPlanMapper.deleteByPrimarayKeyForModel(plan);
		if (count == 0) {
		    throw new ServiceException("只有建单状态的数据才能删除");
		}
	    }
	} else {
	    throw new ServiceException("参数错误");
	}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
    public void auditPlan(String keyStr, String locno, String oper,String userName)
	    throws ServiceException {
	BillWmPlan plan = null;
	Date curDate = new Date();
	if (StringUtils.isNotEmpty(keyStr)) {
	    String keys[] = keyStr.split(",");
	    for (String key : keys) {
		String[] subKyes = key.split("\\|");
		plan = new BillWmPlan();
		plan.setLocno(locno);
		plan.setPlanNo(subKyes[0]);
		plan.setOwnerNo(subKyes[1]);
		plan.setAuditor(oper);
		plan.setAudittm(curDate);
		plan.setAuditorName(userName);
		plan.setEditor(oper);
		plan.setEditorName(userName);
		plan.setEdittm(curDate);
		plan.setStatus(BillWmPlanStatusEnums.STATUS11.getStatus());
		plan.setSourceStatus(BillWmPlanStatusEnums.STATUS10.getStatus());
		int count = billWmPlanMapper.updateByPrimaryKeySelective(plan);
		if (count == 0) {
		    throw new ServiceException("只有建单状态的数据才能审核");
		}
		billWmPlanDtlMapper.updateAllDetail(plan);
	    }
	} else {
	    throw new ServiceException("参数错误");
	}
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void toStoreLock(List<BillWmPlan> list, SystemUser user) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(list)){
				throw new ServiceException("参数非法");
			}
			BillWmPlanKey planKey = new BillWmPlanKey();
			planKey.setLocno(list.get(0).getLocno());
			planKey.setOwnerNo(list.get(0).getOwnerNo());
			planKey.setPlanNo(list.get(0).getPlanNo());
			BillWmPlan checkWmPlan = (BillWmPlan)billWmPlanMapper.selectByPrimaryKey(planKey);
			if(checkWmPlan==null){
				throw new ServiceException(list.get(0).getPlanNo()+"数据不存在！");
			}
			if(!list.get(0).getStatus().equals(checkWmPlan.getStatus())){
				throw new ServiceException(list.get(0).getPlanNo()+"该状态已经发生改变,请重新刷新再操作！");
			}
			Date date=new Date();
			List<BillConStorelockDtl> addList = new ArrayList<BillConStorelockDtl>();
			for (BillWmPlan billWmPlan : list) {
				String itemType = "0";
				String businessType = BillConStoreLockEnums.BUSINESS_TYPE1.getStatus();
				if(PLANTYPE11.equals(billWmPlan.getPlanType())||PLANTYPE12.equals(billWmPlan.getPlanType())){
					itemType = "9";
				}
				//如果是已转移库的
				if(BillWmPlanStatusEnums.STATUS20.getStatus().equals(billWmPlan.getStatus())){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", billWmPlan.getLocno());
					params.put("ownerNo", billWmPlan.getOwnerNo());
					params.put("planNo", billWmPlan.getPlanNo());
					params.put("itemType", itemType);
					params.put("sourceType", billWmPlan.getPlanType());
					int num = billConStorelockDtlMapper.selectPlanStockNum(params);
					if(num > 0){
						throw new ServiceException(billWmPlan.getPlanNo()+"有上架或下架操作,不能转锁定!");
					}
					BillHmPlan plan = new BillHmPlan();
					plan.setLocno(billWmPlan.getLocno());
					plan.setSourceNo(billWmPlan.getPlanNo());
					plan.setSourceType(billWmPlan.getPlanType());
					List<BillHmPlan> listHmPlans = billHmPlanMapper.selectHmPlanBySourceNo(plan);
					if(!CommonUtil.hasValue(listHmPlans)){
						throw new ServiceException(billWmPlan.getPlanNo()+"查询移库计划单失败!");
					}
					BillHmPlan billHmPlan = listHmPlans.get(0);
					if(STATUS10.equals(billHmPlan.getStatus())){
						throw new ServiceException(billWmPlan.getPlanNo()+"退厂计划单未审核,不能转库存锁定!");
					}
					businessType = BillConStoreLockEnums.BUSINESS_TYPE2.getStatus();
				}
				
				//查询退厂明细关联库存转库存锁定
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", billWmPlan.getLocno());
				params.put("ownerNo", billWmPlan.getOwnerNo());
				params.put("planNo", billWmPlan.getPlanNo());
				params.put("itemType", itemType);
				params.put("sourceType", billWmPlan.getPlanType());
				List<BillConStorelockDtl> listStorelockDtls = billConStorelockDtlMapper.selectWmPlanDtlInnerStock(params);
				if(!CommonUtil.hasValue(listStorelockDtls)){
					throw new ServiceException(billWmPlan.getPlanNo()+"退厂商品明细没有找到关联的库存!");
				}
				
				//1.生成库存锁定主档部分
				String storelockNo = procCommonService.procGetSheetNo(billWmPlan.getLocno(), CNumPre.CON_STORELOCK_PRE);
				BillConStorelock storelock = new BillConStorelock();
				storelock.setLocno(billWmPlan.getLocno());
				storelock.setOwnerNo(billWmPlan.getOwnerNo());
				storelock.setStorelockNo(storelockNo);
				storelock.setStorelockType(BillConStoreLockEnums.STORELOCK_TYPE1.getStatus());
				storelock.setStatus(BillConStoreLockEnums.STATUS10.getStatus());
				storelock.setCreator(billWmPlan.getCreator());
				storelock.setCreatorName(billWmPlan.getCreatorName());
				storelock.setCreatetm(new Date());
				storelock.setSourceType(billWmPlan.getPlanType());
				storelock.setSourceNo(billWmPlan.getPlanNo());
				storelock.setBusinessType(businessType);
				int mcount = billConStorelockMapper.insertSelective(storelock);
				if(mcount < 1){
					throw new ServiceException(billWmPlan.getPlanNo()+"转库存锁定生成数据失败!");
				}
				
				//2.生成库存锁定明细部分
				Long rowId = 0L;
				for (BillConStorelockDtl bcs : listStorelockDtls) {
					bcs.setRowId(++rowId);
					bcs.setStorelockNo(storelockNo);
					bcs.setCreator(billWmPlan.getCreator());
					bcs.setCreatorName(billWmPlan.getCreatorName());
					bcs.setCreatetm(new Date());
					bcs.setRealQty(new BigDecimal(0));
					bcs.setStatus(BillConStoreLockEnums.STATUS10.getStatus());
					addList.add(bcs);
				}
				
				//3.批量新增
				billConStorelockDtlMapper.saveStorelockDtl(addList);
				//循环调用记账外部存储过程
				for (BillConStorelockDtl bsd : addList) {
					Item item = itemMapper.selectByCode(bsd.getItemNo(),null);//查询供应商
					BillAccControlDto controlDto = new BillAccControlDto();
					controlDto.setiLocno(bsd.getLocno());
					controlDto.setiOwnerNo(bsd.getOwnerNo());
					controlDto.setiPaperNo(bsd.getStorelockNo());
					controlDto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					controlDto.setiIoFlag("I");
					controlDto.setiCreator(bsd.getCreator());
					controlDto.setiRowId(new BigDecimal(bsd.getRowId()));
					controlDto.setiCellNo(bsd.getCellNo());
					controlDto.setiCellId(new BigDecimal(bsd.getCellId()));
					controlDto.setiItemNo(bsd.getItemNo());
					controlDto.setiSizeNo(bsd.getSizeNo());
					controlDto.setiPackQty(bsd.getPackQty());
					controlDto.setiSupplierNo(item.getSupplierNo());
					controlDto.setiOutstockQty(bsd.getItemQty());
					/**默认值**/
					controlDto.setiItemType("0");
					controlDto.setiQuality("0");
					controlDto.setiQty(new BigDecimal(0));
					controlDto.setiInstockQty(new BigDecimal(0));
					controlDto.setiStatus("0");
					controlDto.setiFlag("0");
					controlDto.setiHmManualFlag("1");
					controlDto.setiTerminalFlag("1");
					billAccControlService.procAccPrepareDataExt(controlDto);
					
					//调用外部存储过程
					BillAccControlDto dto = new BillAccControlDto();
					dto.setiPaperNo(bsd.getStorelockNo());
					dto.setiLocType("2");
					dto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					dto.setiIoFlag("I");
					dto.setiPrepareDataExt(new BigDecimal(bsd.getRowId()));
					dto.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dto);
				}
				
				//3.更新退厂计划状态为30已转锁定
				billWmPlan.setStatus(BillWmPlanStatusEnums.STATUS30.getStatus());
				billWmPlan.setEditor(user.getLoginName());
				billWmPlan.setEditorName(user.getUsername());
				billWmPlan.setEdittm(date);
				int pcount = billWmPlanMapper.updateByPrimaryKeySelective(billWmPlan);
				if(pcount < 1){
					throw new ServiceException(billWmPlan.getPlanNo()+"更新退厂计划单为<已转锁定>状态失败!");
				}
			}
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void changeWMRequest(BillWmPlanKey billWmPlanKey,String userName,String chUserName) throws ServiceException {
		try{
			BillWmPlan billWmPlan=(BillWmPlan) billWmPlanMapper.selectByPrimaryKey(billWmPlanKey);
			//当退厂计划为“已转锁定库存”时，才可转退厂申请
			if(!billWmPlan.getStatus().equals(BillWmPlanStatusEnums.STATUS30.getStatus())&&
					!billWmPlan.getStatus().equals(BillWmPlanStatusEnums.STATUS11.getStatus())){
				throw new ServiceException("已审核、已转锁定状态的退厂计划单才可转申请。");
			}
			//新建退厂申请单据，并设为审核状态
			//退厂申请单号
			String requestNo = procCommonService.procGetSheetNo(billWmPlan.getLocno(), CNumPre.WM_REQUEST_WR);
			
			BillWmRequest billWmRequest=new BillWmRequest();
			billWmRequest.setLocno(billWmPlanKey.getLocno());
			billWmRequest.setOwnerNo(billWmPlanKey.getOwnerNo());
			billWmRequest.setRequestNo(requestNo);
			billWmRequest.setRequestType(billWmPlan.getPlanType());
			billWmRequest.setStatus(BillWmRequestStatusEnums.STATUS11.getStatus());
			billWmRequest.setCreator(userName);
			billWmRequest.setCreatorName(chUserName);
			billWmRequest.setCreatetm(new Date());
			billWmRequest.setEditor(userName);
			billWmRequest.setEditorName(chUserName);
			billWmRequest.setEdittm(new Date());
			billWmRequest.setAuditor(userName);
			billWmRequest.setAuditorName(chUserName);
			billWmRequest.setAudittm(new Date());
			billWmRequest.setSourceNo(billWmPlanKey.getPlanNo());
			billWmRequest.setSourceType(billWmPlan.getPlanType());
			billWmRequest.setSysNo(billWmPlan.getSysNo());
			//新增退厂申请主档
			billWmRequestService.add(billWmRequest);
			
			//从库存锁定中查找可转申请的明细记录
			Map<String,Object> selectDtlMap=new HashMap<String,Object>();
			selectDtlMap.put("locno", billWmPlanKey.getLocno());
			selectDtlMap.put("ownerNo", billWmPlanKey.getOwnerNo());
			selectDtlMap.put("planNo", billWmPlanKey.getPlanNo());
			
			List<BillConStorelockDtl> conStorelockDtlList=billConStorelockDtlService.find4WmPlan(selectDtlMap);
			
			if(conStorelockDtlList.size()==0){
				throw new ServiceException("未找到可转申请的商品明细，转换失败。");
			}
			//待新增的退厂申请明细
			List<BillWmRequestDtl> requestDtl4AddList=new ArrayList<BillWmRequestDtl>();
			BigDecimal itemQty;//锁定库存表中的可转申请数量
			boolean isExists;
			//待新增的退厂申请明细根据商品编号和尺码汇总
			for(BillConStorelockDtl conStorelockDtl:conStorelockDtlList){
				isExists=false;					
				itemQty=conStorelockDtl.getItemQty().subtract(conStorelockDtl.getRealQty());//锁定库存表中的可转申请数量
				for(BillWmRequestDtl requestDtl:requestDtl4AddList){
					if(conStorelockDtl.getItemNo().equals(requestDtl.getItemNo())&&conStorelockDtl.getSizeNo().equals(requestDtl.getSizeNo())){		
						requestDtl.setItemQty(requestDtl.getItemQty().add(itemQty));
						isExists=true;
						break;
					}
				}
				//不存在则新增一条记录
				if(!isExists){
					BillWmRequestDtl billWmRequestDtl=new BillWmRequestDtl();
					billWmRequestDtl.setLocno(conStorelockDtl.getLocno());
					billWmRequestDtl.setOwnerNo(conStorelockDtl.getOwnerNo());
					billWmRequestDtl.setRequestNo(requestNo);
					billWmRequestDtl.setItemNo(conStorelockDtl.getItemNo());
					billWmRequestDtl.setSizeNo(conStorelockDtl.getSizeNo());
					billWmRequestDtl.setItemQty(itemQty);	
					billWmRequestDtl.setBrandNo(conStorelockDtl.getBrandNo());
					requestDtl4AddList.add(billWmRequestDtl);
				}				
			}
			
			//保存退厂申请明细
			for(BillWmRequestDtl requestDtl:requestDtl4AddList){
				billWmRequestDtlService.add(requestDtl);
			}
			//标记库存锁定明细为已转退厂申请
			BillConStorelockDtl conStorelockDtl4Modify=null;
			for(BillConStorelockDtl conStorelockDtl:conStorelockDtlList){
				conStorelockDtl4Modify=new BillConStorelockDtl();
				conStorelockDtl4Modify.setLocno(conStorelockDtl.getLocno());
				conStorelockDtl4Modify.setOwnerNo(conStorelockDtl.getOwnerNo());
				conStorelockDtl4Modify.setStorelockNo(conStorelockDtl.getStorelockNo());
				conStorelockDtl4Modify.setRowId(conStorelockDtl.getRowId());	
				conStorelockDtl4Modify.setEdittm(new Date());
				conStorelockDtl4Modify.setEditor(userName);
				conStorelockDtl4Modify.setEditorName(chUserName);
				conStorelockDtl4Modify.setStatus(BillConStoreLockEnums.STATUS12.getStatus());
				billConStorelockDtlService.modifyById(conStorelockDtl4Modify);
			}
			
			//退厂计划置为已转申请
			BillWmPlan updateBillWmPlan=new BillWmPlan();
			updateBillWmPlan.setLocno(billWmPlanKey.getLocno());
			updateBillWmPlan.setOwnerNo(billWmPlanKey.getOwnerNo());
			updateBillWmPlan.setPlanNo(billWmPlanKey.getPlanNo());
			updateBillWmPlan.setStatus(BillWmPlanStatusEnums.STATUS40.getStatus());
			updateBillWmPlan.setEditor(userName);
			updateBillWmPlan.setEditorName(chUserName);
			updateBillWmPlan.setEdittm(new Date());
			
			billWmPlanMapper.updateByPrimaryKeySelective(updateBillWmPlan);			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void changeHMPlan(BillWmPlanKey billWmPlanKey,String userName,String chUserName) throws ServiceException {
		try{
			BillWmPlan billWmPlan=(BillWmPlan) billWmPlanMapper.selectByPrimaryKey(billWmPlanKey);
			//当退厂计划为“已审核”时，才可转转移库计划
			if(!billWmPlan.getStatus().equals(BillWmPlanStatusEnums.STATUS11.getStatus())){
				throw new ServiceException("已审核状态的退厂计划单才可转移库计划。");
			}
			//新建移库计划单据
			//移库计划单号
			String planNo = procCommonService.procGetSheetNo(billWmPlan.getLocno(), CNumPre.Hm_Move_PRE);
			BillHmPlan billHmPlan=new BillHmPlan();
			billHmPlan.setLocno(billWmPlanKey.getLocno());
			billHmPlan.setOwnerNo(billWmPlanKey.getOwnerNo());
			billHmPlan.setPlanNo(planNo);
			billHmPlan.setSourceType(billWmPlan.getPlanType());
			billHmPlan.setStatus(BillHmPlanStatusEnums.STATUS10.getStatus());
			billHmPlan.setMoveDate(new Date());
			billHmPlan.setCreator(userName);
			billHmPlan.setCreatorName(chUserName);
			billHmPlan.setCreatetm(new Date());
			billHmPlan.setEditor(userName);
			billHmPlan.setEditorName(chUserName);
			billHmPlan.setEdittm(new Date());
			billHmPlan.setSourceNo(billWmPlanKey.getPlanNo());
			billHmPlan.setBusinessType(BillHmPlanBusinessTypeEnums.BUSINESSTYPE1.getStatus());
			billHmPlanService.add(billHmPlan);
			
			
			// 1）当退厂计划为批发退货/批发召回时，将库存满足（库区属性= 作业区0，库区类型=存储区0，库区用途=普通存储1，商品编码in退厂计划商品编码，库存属性=批发品，计划数量=库存-预下，储位、库存状态可用）的商品库存，保存到移库计划明细。
			// 2）当退厂计划为自营退货/自营召回时，将库存满足（库区属性= 作业区0，库区类型=存储区0，库区用途=普通存储，商品编码in退厂计划商品编码，库存属性=零售品，计划数量=库存-预下，储位、库存状态可用）的商品库存，保存到移库计划明细。
					  
			//计划类型11-批发退货；12-批发召回；13-自营退货；14-自营召回
			String planType=billWmPlan.getPlanType();
			String itemType="";
			if(planType.equals(BillHmPlanPlanTypeEnums.PLANTYPE11.getStatus())||planType.equals(BillHmPlanPlanTypeEnums.PLANTYPE12.getStatus())){
				itemType="9";
			}else if(planType.equals(BillHmPlanPlanTypeEnums.PLANTYPE13.getStatus())||planType.equals(BillHmPlanPlanTypeEnums.PLANTYPE14.getStatus())){
				itemType="0";
			}
			
			
			Map<String,Object> returnCellMap=new HashMap<String,Object>();
			Map<String,Object> findCellParams=new HashMap<String,Object>();
			findCellParams.put("locno", billWmPlanKey.getLocno());
			findCellParams.put("ownerNo", billWmPlanKey.getOwnerNo());
			//findCellParams.put("rownum", "2");
			List<CmDefcell> cmDefcellList=cmDefcellService.find4ReturnedGoods(findCellParams);
			//CmDefcell cmDefcell=null;
			if(cmDefcellList.size()==0){
				throw new ServiceException("未找到可用的退厂储位，转换失败。");
			}else{
				//把该库区的所有储位放到map
				for (CmDefcell cdf : cmDefcellList) {
					if(StringUtils.isNotEmpty(cdf.getAreaQuality())&&StringUtils.isNotEmpty(cdf.getItemType())){
						String key = cdf.getAreaQuality()+"|"+cdf.getItemType();
						if(returnCellMap.get(key) == null){
							returnCellMap.put(key, cdf.getCellNo());
						}
					}
				}
				//cmDefcell=cmDefcellList.get(0);
			}
			
			//批量新增
			Map<String,Object> addDtlMap=new HashMap<String,Object>();
			addDtlMap.put("locno", billWmPlanKey.getLocno());
			addDtlMap.put("ownerNo", billWmPlanKey.getOwnerNo());
			//addDtlMap.put("planNo", planNo);
			addDtlMap.put("wmPlanNo", billWmPlanKey.getPlanNo());
			//addDtlMap.put("cellNo", cmDefcell.getCellNo());
			addDtlMap.put("itemType",itemType);			
			addDtlMap.put("sourceType",billWmPlan.getPlanType());
			List<BillHmPlanDtl> planDtlList = billHmPlanDtlService.findInsertHmPlan4WmPlan(addDtlMap);
			if(!CommonUtil.hasValue(planDtlList)){
				throw new ServiceException("未找到可转移库计划的商品明细，转换失败。");
			}
			
			List<BillHmPlanDtl> addPlanDtlList = new ArrayList<BillHmPlanDtl>();
			long rowId = 0;
			for (BillHmPlanDtl billHmPlanDtl : planDtlList) {
				String key = billHmPlanDtl.getQuality()+"|"+billHmPlanDtl.getItemType();
				String dCellNo = (String)returnCellMap.get(key);
				if(StringUtils.isEmpty(dCellNo)){
					throw new ServiceException("未找到可用的目的储位,储位规则：库区是退货作业存储区、用途地面堆叠。当前商品品质："+billHmPlanDtl.getQuality()+",商品属性："+billHmPlanDtl.getItemType());
				}
				billHmPlanDtl.setPlanNo(planNo);
				billHmPlanDtl.setdCellNo(dCellNo);
				billHmPlanDtl.setRowId(++rowId);
				addPlanDtlList.add(billHmPlanDtl);
			}
			
			//批量插入
			int count = 0;
			int pageNum = 100;
			for(int idx=0;idx<addPlanDtlList.size();){
				idx += pageNum;
				if(idx > addPlanDtlList.size()){
					count = billHmPlanDtlService.addByWmPlan(addPlanDtlList.subList(idx-pageNum, addPlanDtlList.size()));
				}else{
					count = billHmPlanDtlService.addByWmPlan(addPlanDtlList.subList(idx-pageNum, idx));
				}
				if(count < 1){
					throw new ServiceException("新增移库计划明细失败!");
				}
			}
			
//			int addDtlCount=billHmPlanDtlService.addByWmPlan(addDtlMap);
//			if(addDtlCount==0){
//				throw new ServiceException("未找到可转移库计划的商品明细，转换失败。");
//			}
			
			//退厂计划置为已转计划
			BillWmPlan updateBillWmPlan=new BillWmPlan();
			updateBillWmPlan.setLocno(billWmPlanKey.getLocno());
			updateBillWmPlan.setOwnerNo(billWmPlanKey.getOwnerNo());
			updateBillWmPlan.setPlanNo(billWmPlanKey.getPlanNo());
			updateBillWmPlan.setStatus(BillWmPlanStatusEnums.STATUS20.getStatus());
			updateBillWmPlan.setEditor(userName);
			updateBillWmPlan.setEditorName(chUserName);
			updateBillWmPlan.setEdittm(new Date());
			
			billWmPlanMapper.updateByPrimaryKeySelective(updateBillWmPlan);			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}