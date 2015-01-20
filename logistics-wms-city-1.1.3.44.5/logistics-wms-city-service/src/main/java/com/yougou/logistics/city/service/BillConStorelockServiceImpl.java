package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.BillConStoreLockEnums;
import com.yougou.logistics.city.common.enums.BillWmPlanStatusEnums;
import com.yougou.logistics.city.common.enums.BillWmRequestStatusEnums;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.BillConStorelockKey;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.model.BillWmRequest;
import com.yougou.logistics.city.common.model.BillWmRequestDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillConStorelockDtlMapper;
import com.yougou.logistics.city.dal.database.BillConStorelockMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillWmPlanMapper;
import com.yougou.logistics.city.dal.mapper.BillWmRequestDtlMapper;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
@Service("billConStorelockService")
class BillConStorelockServiceImpl extends BaseCrudServiceImpl implements BillConStorelockService {
	
    @Resource
    private BillConStorelockMapper billConStorelockMapper;
    
    @Resource
    private BillConStorelockDtlMapper billConStorelockDtlMapper;
    
    @Resource
    private BillAccControlService billAccControlService;
    
    @Resource
    private ItemMapper itemMapper;
    
    @Resource
    private ProcCommonService procCommonService;
    
    @Resource
    private BillWmPlanMapper billWmPlanMapper;
    
    @Resource
    private BillWmRequestService billWmRequestService;
    
    @Resource
    private BillWmRequestDtlMapper billWmRequestDtlMapper;
    
    @Log
    private Logger log;
    
    private final static String STATUS10 = "10";
    
    //private final static String STATUS12 = "12";
    
    private final static String STATUS11 = "11";
    
    private final static String STATUS91 = "91";
    
    private final static String BUSINESSTYPE1 = "1";

    @Override
    public BaseCrudMapper init() {
        return billConStorelockMapper;
    }
    
    
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void auditStorelock(List<BillConStorelock> lists) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(lists)){
				throw new ServiceException("参数非法");
			}
			
			for (BillConStorelock bs : lists) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", bs.getLocno());
				params.put("ownerNo", bs.getOwnerNo());
				params.put("storelockNo", bs.getStorelockNo());
				List<BillConStorelockDtl> listDtls = billConStorelockDtlMapper.selectByParams(null, params);
				if(!CommonUtil.hasValue(listDtls)){
					throw new ServiceException(bs.getStorelockNo()+"没有明细,不能审核!");
				}
				
				bs.setStatus(STATUS11);
				bs.setAuditor(bs.getAuditor());
				bs.setAuditorName(bs.getAuditorName());
				bs.setAudittm(new Date());
				bs.setUpdStatus(STATUS10);
				int count = billConStorelockMapper.updateByPrimaryKeySelective(bs);
				if(count < 1){
					throw new ServiceException("单据"+bs.getStorelockNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void delStorelock(List<BillConStorelock> lists) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(lists)){
				throw new ServiceException("参数非法");
			}
			for (BillConStorelock bs : lists) {
				//删除前验证状态
				/*BillConStorelockKey conStorelockKey = new BillConStorelockKey();
				conStorelockKey.setLocno(bs.getLocno());
				conStorelockKey.setOwnerNo(bs.getOwnerNo());
				conStorelockKey.setStorelockNo(bs.getStorelockNo());
				BillConStorelock conStorelock = (BillConStorelock)billConStorelockMapper.selectByPrimaryKey(conStorelockKey);
				if(conStorelock == null){
					throw new ServiceException(bs.getStorelockNo()+"查找数据失败!");
				}
				if(!BillConStoreLockEnums.STATUS10.getStatus().equals(conStorelock.getStatus())){
					throw new ServiceException(bs.getStorelockNo()+"只能删除状态是<建单>的单据!");
				}*/
				
				//循环单据所有的明细
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", bs.getLocno());
				params.put("ownerNo", bs.getOwnerNo());
				params.put("storelockNo", bs.getStorelockNo());
				List<BillConStorelockDtl> listStoreLockDtls = billConStorelockDtlMapper.selectByParams(null, params);
				if(CommonUtil.hasValue(listStoreLockDtls)){
					//调用内部存储过程
					BillAccControlDto dto = new BillAccControlDto();
					dto.setiPaperNo(bs.getStorelockNo());
					dto.setiLocType("2");
					dto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					dto.setiIoFlag("O");
					dto.setiPrepareDataExt(new BigDecimal(0));
					dto.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dto);
				}
				//删除主表
				bs.setUpdStatus("10");
				int count = billConStorelockMapper.deleteByPrimarayKeyForModel(bs);
				if(count < 1){
					throw new ServiceException("单据"+bs.getStorelockNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
				//删除数据库明细
				BillConStorelockDtl dtl = new BillConStorelockDtl();
				dtl.setLocno(bs.getLocno());
				dtl.setOwnerNo(bs.getOwnerNo());
				dtl.setStorelockNo(bs.getStorelockNo());
				billConStorelockDtlMapper.deleteModel(dtl);
				
				//是否走退厂流程,回写计划单
				if (!BillConStoreLockEnums.BUSINESS_TYPE0.getStatus().equals(bs.getBusinessType())
						&& StringUtils.isNotBlank(bs.getSourceNo()) && StringUtils.isNotBlank(bs.getSourceType())) {
					BillWmPlanKey planKey = new BillWmPlanKey();
					planKey.setLocno(bs.getLocno());
					planKey.setOwnerNo(bs.getOwnerNo());
					planKey.setPlanNo(bs.getSourceNo());
					BillWmPlan billWmPlan = (BillWmPlan)billWmPlanMapper.selectByPrimaryKey(planKey);
					if(billWmPlan == null){
						throw new ServiceException(bs.getStorelockNo() + "回写状态：查询退厂计划单失败!");
					}
					if(BillConStoreLockEnums.BUSINESS_TYPE2.getStatus().equals(bs.getBusinessType())){
						billWmPlan.setStatus(BillWmPlanStatusEnums.STATUS20.getStatus());
					}else{
						billWmPlan.setStatus(BillWmPlanStatusEnums.STATUS11.getStatus());
					}
					int wcount = billWmPlanMapper.updateByPrimaryKeySelective(billWmPlan);
					if(wcount < 1){
						throw new ServiceException(bs.getStorelockNo() + "回写退厂计划单状态失败!");
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void overStoreLock(List<BillConStorelock> lists) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(lists)){
				throw new ServiceException("参数非法");
			}
			for (BillConStorelock bs : lists) {
				//删除前验证状态
				BillConStorelockKey conStorelockKey = new BillConStorelockKey();
				conStorelockKey.setLocno(bs.getLocno());
				conStorelockKey.setOwnerNo(bs.getOwnerNo());
				conStorelockKey.setStorelockNo(bs.getStorelockNo());
				BillConStorelock conStorelock = (BillConStorelock)billConStorelockMapper.selectByPrimaryKey(conStorelockKey);
				if(conStorelock == null){
					throw new ServiceException(bs.getStorelockNo()+"查找数据失败!");
				}
				if(!BillConStoreLockEnums.STATUS11.getStatus().equals(conStorelock.getStatus())){
					throw new ServiceException(bs.getStorelockNo()+"只能关闭状态是<审核>的单据!");
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", bs.getLocno());
				params.put("ownerNo", bs.getOwnerNo());
				params.put("storelockNo", bs.getStorelockNo());
				params.put("status", "10");
				List<BillConStorelockDtl> listDtls = billConStorelockDtlMapper.selectByParams(null, params);
				if(!CommonUtil.hasValue(listDtls)){
					throw new ServiceException(bs.getStorelockNo()+"没有可关闭的明细!");
				}
				bs.setStatus(STATUS91);
				int count = billConStorelockMapper.updateByPrimaryKeySelective(bs);
				if(count < 1){
					throw new ServiceException(bs.getStorelockNo() + "关闭失败!");
				}
				
				//循环单据所有的明细
				if(CommonUtil.hasValue(listDtls)){
					for (BillConStorelockDtl bsd : listDtls) {
						Item item = itemMapper.selectByCode(bsd.getItemNo(),null);//查询供应商
						BillAccControlDto controlDto = new BillAccControlDto();
						controlDto.setiLocno(bsd.getLocno());
						controlDto.setiOwnerNo(bsd.getOwnerNo());
						controlDto.setiPaperNo(bsd.getStorelockNo());
						controlDto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
						controlDto.setiIoFlag("O");
						controlDto.setiCreator(bsd.getCreator());
						controlDto.setiRowId(new BigDecimal(bsd.getRowId()));
						controlDto.setiCellNo(bsd.getCellNo());
						controlDto.setiCellId(new BigDecimal(bsd.getCellId()));
						controlDto.setiItemNo(bsd.getItemNo());
						controlDto.setiSizeNo(bsd.getSizeNo());
						controlDto.setiPackQty(bsd.getPackQty());
						controlDto.setiSupplierNo(item.getSupplierNo());
						controlDto.setiOutstockQty(new BigDecimal(0).subtract(bsd.getItemQty().subtract(bsd.getRealQty())));
						
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
						dto.setiPaperNo(bs.getStorelockNo());
						dto.setiLocType("2");
						dto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
						dto.setiIoFlag("O");
						dto.setiPrepareDataExt(new BigDecimal(bsd.getRowId()));
						dto.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dto);
					}
					
				}
				
				
				//是否走退厂流程,回写计划单
				if (!BillConStoreLockEnums.BUSINESS_TYPE0.getStatus().equals(conStorelock.getBusinessType())
						&& StringUtils.isNotBlank(conStorelock.getSourceNo()) && StringUtils.isNotBlank(conStorelock.getSourceType())) {
					BillWmPlanKey planKey = new BillWmPlanKey();
					planKey.setLocno(conStorelock.getLocno());
					planKey.setOwnerNo(conStorelock.getOwnerNo());
					planKey.setPlanNo(conStorelock.getSourceNo());
					BillWmPlan billWmPlan = (BillWmPlan)billWmPlanMapper.selectByPrimaryKey(planKey);
					if(billWmPlan == null){
						throw new ServiceException(conStorelock.getStorelockNo() + "回写状态：查询退厂计划单失败!");
					}
					if(BillConStoreLockEnums.BUSINESS_TYPE2.getStatus().equals(conStorelock.getBusinessType())){
						billWmPlan.setStatus(BillWmPlanStatusEnums.STATUS20.getStatus());
					}else{
						billWmPlan.setStatus(BillWmPlanStatusEnums.STATUS11.getStatus());
					}
					int wcount = billWmPlanMapper.updateByPrimaryKeySelective(billWmPlan);
					if(wcount < 1){
						throw new ServiceException(bs.getStorelockNo() + "回写退厂计划单状态失败!");
					}
				}
				
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void toWmRequest(BillConStorelock storelock) throws ServiceException {
		try{
			BillConStorelockKey conStorelockKey = new BillConStorelockKey();
			conStorelockKey.setLocno(storelock.getLocno());
			conStorelockKey.setOwnerNo(storelock.getOwnerNo());
			conStorelockKey.setStorelockNo(storelock.getStorelockNo());
			BillConStorelock conStorelock = (BillConStorelock)billConStorelockMapper.selectByPrimaryKey(conStorelockKey);
			if(conStorelock == null){
				throw new ServiceException(storelock.getStorelockNo()+"查找数据失败!");
			}
			if(!STATUS11.equals(conStorelock.getStatus())){
				throw new ServiceException(storelock.getStorelockNo()+"只能转换状态是<审核>的单据!");
			}
			//1.新建退厂申请单据，并设为审核状态
			//退厂申请单号,新增退厂申请主档
			String requestNo = procCommonService.procGetSheetNo(storelock.getLocno(), CNumPre.WM_REQUEST_WR);
			BillWmRequest billWmRequest=new BillWmRequest();
			billWmRequest.setLocno(storelock.getLocno());
			billWmRequest.setOwnerNo(storelock.getOwnerNo());
			billWmRequest.setRequestNo(requestNo);
			billWmRequest.setRequestType(conStorelock.getSourceType());
			billWmRequest.setStatus(BillWmRequestStatusEnums.STATUS11.getStatus());
			billWmRequest.setCreator(storelock.getCreator());
			billWmRequest.setCreatetm(new Date());
			billWmRequest.setEditor(storelock.getCreator());
			billWmRequest.setEdittm(new Date());
			billWmRequest.setAuditor(storelock.getCreator());
			billWmRequest.setAudittm(new Date());
			billWmRequest.setSourceNo(storelock.getStorelockNo());
			billWmRequest.setSourceType(storelock.getSourceType());
			billWmRequest.setBusinessType(BUSINESSTYPE1);
			//billWmRequest.setSysNo(billWmPlan.getSysNo());
			billWmRequestService.add(billWmRequest);
			
			//2.生成申请明细
			List<BillWmRequestDtl> addList = new ArrayList<BillWmRequestDtl>();
			BillConStorelockDtl storelockDtl = new BillConStorelockDtl();
			storelockDtl.setLocno(storelock.getLocno());
			storelockDtl.setOwnerNo(storelock.getOwnerNo());
			storelockDtl.setStorelockNo(storelock.getStorelockNo());
			List<BillConStorelockDtl> listStorelockDtls=billConStorelockDtlMapper.selectToWmRequestDtlGroupBy(storelockDtl);
			if(!CommonUtil.hasValue(listStorelockDtls)){
				throw new ServiceException(storelock.getStorelockNo()+"查找库存锁定明细失败!");
			}
			for (BillConStorelockDtl bcd : listStorelockDtls) {
				BillWmRequestDtl wmRequestDtl = new BillWmRequestDtl();
				wmRequestDtl.setLocno(bcd.getLocno());
				wmRequestDtl.setOwnerNo(bcd.getOwnerNo());
				wmRequestDtl.setRequestNo(requestNo);
				wmRequestDtl.setItemNo(bcd.getItemNo());
				wmRequestDtl.setSizeNo(bcd.getSizeNo());
				wmRequestDtl.setPackQty(bcd.getPackQty() == null ? new BigDecimal(1) : bcd.getPackQty());
				wmRequestDtl.setItemQty(bcd.getItemQty());
				wmRequestDtl.setBrandNo(bcd.getBrandNo());
				wmRequestDtl.setStatus(STATUS10);
				addList.add(wmRequestDtl);
			}
			billWmRequestDtlMapper.insertBatchWmPlan(addList);
			
			//3.更新库存锁定状态
			conStorelock.setStatus(BillConStoreLockEnums.STATUS12.getStatus());
			int count = billConStorelockMapper.updateByPrimaryKeySelective(conStorelock);
			if(count < 1){
				throw new ServiceException(storelock.getStorelockNo()+"更新库存锁定状态失败!");
			}
			
			//4.更新库存锁定明细状态
			storelockDtl.setStatus(BillConStoreLockEnums.STATUS12.getStatus());
			int dcount = billConStorelockDtlMapper.updateStorelockDtlStatus(storelockDtl);
			if(dcount < 1){
				throw new ServiceException(storelock.getStorelockNo()+"更新库存锁定明细状态失败!");
			}	
			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Map<String, Object> selectSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return this.billConStorelockMapper.selectSumQty(params,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
    
}