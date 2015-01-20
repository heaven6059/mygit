package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.api.dto.AccInventoryConDto;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.dto.BillWmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckKey;
import com.yougou.logistics.city.common.model.BillStatusLog;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.model.BillWmRecheck;
import com.yougou.logistics.city.common.model.BillWmRecheckDtl;
import com.yougou.logistics.city.common.model.Supplier;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.mapper.BillWmOutstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillWmRecedeMapper;
import com.yougou.logistics.city.dal.mapper.BillWmRecheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillWmRecheckMapper;
import com.yougou.logistics.city.dal.mapper.ConLabelDtlMapper;
import com.yougou.logistics.city.dal.mapper.ConLabelMapper;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 11:05:09
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
@Service("billWmRecheckService")
class BillWmRecheckServiceImpl extends BaseCrudServiceImpl implements BillWmRecheckService {
    @Resource
    private BillWmRecheckMapper billWmRecheckMapper;
    @Resource
    private BillWmRecheckDtlMapper billWmRecheckDtlMapper;
    @Resource
    private BillWmOutstockDtlMapper billWmOutstockDtlMapper;
    @Resource
    private AccInventoryConSkuServiceApi accInventoryConSkuServiceApi;
    @Resource
    private ConLabelMapper conLabelMapper;
    @Resource
    private ConLabelDtlMapper conLabelDtlMapper;
    @Resource
    private BillStatusLogService billStatusLogService;
    @Resource
    private BillWmRecedeMapper billWmRecedeMapper;
    
    private final static String RESULTY = "Y";
    
    private final static String STATUS40 = "40";
    
    private final static String STATUS45 = "45";

    @Override
    public BaseCrudMapper init() {
        return billWmRecheckMapper;
    }
    
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int countLabelNoByRecheckNo(BillWmRecheck cc, AuthorityParams authorityParams) throws ServiceException {
		try {
			return  billWmRecheckMapper.countLabelNoByRecheckNo(cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecheck> findLabelNoByRecheckNoPage(SimplePage page, BillWmRecheck cc, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return  billWmRecheckMapper.findLabelNoByRecheckNoPage(page, cc, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Supplier> querySupplier(String locno) throws ServiceException {
		try {
			return  billWmRecheckMapper.querySupplier(locno);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillWmOutstockDtl> queryRecheckItem(Map<String, Object> params) throws ServiceException {
		try {
			return  billWmRecheckMapper.queryRecheckItem(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void packageBoxRf(BillWmRecheck billWmRecheck) throws ServiceException {
		try{
			Map<String, String> mapC = new HashMap<String, String>();
			mapC.put("v_locno", billWmRecheck.getLocno());
			mapC.put("v_recedeNo", billWmRecheck.getRecedeNo());
			mapC.put("v_supplierNo", billWmRecheck.getSupplierNo());
			mapC.put("v_userId", billWmRecheck.getCreator());
			mapC.put("v_labelNo", "N");
			String msg = "";
			billWmRecheckMapper.procRecheckComplete(mapC);
			if (!RESULTY.equals(mapC.get("strOutMsg"))) {
				String stroutmsg = mapC.get("strOutMsg");
				if(StringUtils.isNotBlank(stroutmsg)){
					String[] msgs = stroutmsg.split("\\|");
					msg = msgs[1];
				}
				throw new ServiceException(msg);
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void packageBox(List<BillWmOutstockDtlDto> dtlLst, String boxNo,
			String recheckNo, String locno, String supplierNo, String userName,String userChName,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			
			//封箱存储过程
			if(!CommonUtil.hasValue(dtlLst)){
				throw new ServiceException("参数非法!");
			}
			
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("locno", locno);
			mapParams.put("recheckNo", recheckNo);
			mapParams.put("containerNo", "N");
			List<BillWmRecheckDtl> listCheckDtls = billWmRecheckDtlMapper.selectByParams(null, mapParams);
			if(CommonUtil.hasValue(listCheckDtls)){
				throw new ServiceException("RF正在进行封箱操作,不能封箱!");
			}
			
			BillWmRecheckDtl dtl = new BillWmRecheckDtl();
			dtl.setLocno(locno);
			dtl.setRecheckNo(recheckNo);
			int rowid = billWmRecheckDtlMapper.selectMaxPid(dtl);
			
			String recedeNo = "";
			String ownerNo = "";
			for (BillWmOutstockDtlDto d : dtlLst) {
				
				//新增复核明细
				BillWmRecheckDtl recheckDtl = new BillWmRecheckDtl();
				recheckDtl.setLocno(locno);
				recheckDtl.setOwnerNo(d.getOwnerNo());
				recheckDtl.setRecheckNo(recheckNo);
				recheckDtl.setRowId(Long.valueOf(++rowid));
				recheckDtl.setContainerNo("N");
				recheckDtl.setItemNo(d.getItemNo());
				recheckDtl.setItemId(d.getItemId());
				recheckDtl.setItemQty(d.getPackageNum());
				recheckDtl.setRealQty(d.getPackageNum());
				recheckDtl.setStatus("10");
				recheckDtl.setAssignName(userName);
				recheckDtl.setAssignChName(userChName);
				recheckDtl.setRecheckName(userName);
				recheckDtl.setRecheckChName(userChName);
				recheckDtl.setRecheckDate(new Date());
				recheckDtl.setRecedeNo(d.getSourceNo());
				recheckDtl.setRecedeType(d.getRecedeType());
				recheckDtl.setRecedeDate(d.getRecedeDate());
				recheckDtl.setPackQty(d.getPackQty()==null?new BigDecimal(1):d.getPackQty());
				recheckDtl.setRecheckName2(userName);
				recheckDtl.setSizeNo(d.getSizeNo());
				recheckDtl.setCellNo(d.getdCellNo());
				recheckDtl.setCellId(new BigDecimal(d.getdCellId()==null?0L:d.getdCellId()));
				recheckDtl.setBrandNo(d.getBrandNo());
				int dCount = billWmRecheckDtlMapper.insertSelective(recheckDtl);
				if(dCount < 1){
					throw new ServiceException("新增复核明细信息失败!");
				}
				
				//查询拣货单明细,复核数量分摊
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", locno);
				params.put("sourceNo", d.getSourceNo());
				params.put("itemNo", d.getItemNo());
				params.put("sizeNo", d.getSizeNo());
				List<BillWmOutstockDtl> listOutstockDtl = billWmOutstockDtlMapper.selectByParams(null,params);
				if(!CommonUtil.hasValue(listOutstockDtl)){
					throw new ServiceException("查找拣货单明细失败");
				}
				
				boolean result = false;//是否结束当前循环
				BigDecimal packNumIn = d.getPackageNum();//装箱数量
				BigDecimal losePackNum = d.getPackageNum();//剩余装箱的数量
				for (BillWmOutstockDtl odd : listOutstockDtl) {
					if(losePackNum.compareTo(odd.getRealQty().subtract(odd.getRecheckQty()))==1){
						packNumIn = odd.getRealQty().subtract(odd.getRecheckQty());
						losePackNum = losePackNum.subtract(odd.getRealQty().subtract(odd.getRecheckQty()));
					}else{
						packNumIn = losePackNum;
						result = true;
					}
					
					BigDecimal recheckQty = odd.getRecheckQty().add(packNumIn);
					BillWmOutstockDtl wmOutstockDtl = new BillWmOutstockDtl();
					wmOutstockDtl.setLocno(odd.getLocno());
					wmOutstockDtl.setOwnerNo(odd.getOwnerNo());
					wmOutstockDtl.setDivideId(odd.getDivideId());
					wmOutstockDtl.setOutstockNo(odd.getOutstockNo());
					wmOutstockDtl.setRecheckQty(recheckQty);
					int oCount = billWmOutstockDtlMapper.updateByPrimaryKeySelective(wmOutstockDtl);
					if(oCount < 1){
						throw new ServiceException("更新拣货单明细复核数量失败");
					}
					//分摊完成，退出循环
					if(result){
						break;
					}
				}
				
//				//更新复核数量
//				BillWmOutstockDtl wmOutstockDtl = new BillWmOutstockDtl();
//				wmOutstockDtl.setRecheckQty(d.getPackageNum());
//				wmOutstockDtl.setLocno(locno);
//				wmOutstockDtl.setOwnerNo(d.getOwnerNo());
//				wmOutstockDtl.setItemNo(d.getItemNo());
//				wmOutstockDtl.setSizeNo(d.getSizeNo());
//				wmOutstockDtl.setSourceNo(d.getSourceNo());
//				wmOutstockDtl.setDivideId(d.getDivideId());
//				int wCount = billWmOutstockDtlMapper.updateBillWmOutstockDtl(wmOutstockDtl);
//				if(wCount < 1){
//					throw new ServiceException("拣货明细复核数量更新失败!");
//				}
				
				recedeNo = d.getSourceNo();
				ownerNo = d.getOwnerNo();
			}
			
			//复核完成存储过程
			if(StringUtils.isEmpty(recedeNo)){
				throw new ServiceException("封箱操作,参数非法!");
			}
			Map<String, String> mapC = new HashMap<String, String>();
			mapC.put("v_locno", locno);
			mapC.put("v_recedeNo", recedeNo);
			mapC.put("v_supplierNo", supplierNo);
			mapC.put("v_userId", userName);
			mapC.put("v_labelNo", "N");
			String msg = "";
			
			billWmRecheckMapper.procRecheckComplete(mapC);
			if (!RESULTY.equals(mapC.get("strOutMsg"))) {
				String stroutmsg = mapC.get("strOutMsg");
				if(StringUtils.isNotBlank(stroutmsg)){
					String[] msgs = stroutmsg.split("\\|");
					msg = msgs[1];
				}
				throw new ServiceException(msg);
			}
			
			
			//更新通知单复核状态
			BillWmRecede entity = new BillWmRecede();
			entity.setLocno(locno);
			entity.setOwnerNo(ownerNo);
			entity.setRecedeNo(recedeNo);
			int count = billWmRecedeMapper.updateRecedeStatus4Recheck(entity);
			if(count > 0){
				//添加状态
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", locno);
				params.put("billNo", recedeNo);
				params.put("status", STATUS40);
				List<BillStatusLog> listLogs = billStatusLogService.findByBiz(null, params);
				if(!CommonUtil.hasValue(listLogs)){
					String message = "更新退厂通知单状态为部分复核";
					billStatusLogService.procInsertBillStatusLog(locno, recedeNo, "WM", STATUS40, message, userName);
				}
			}
			
			//记录箱容器库存
			int acc_control=SysConstans.ACC_CONTROL;
			if (acc_control == 1) {
				Map<String, Object> conParams =new HashMap<String, Object>();
				conParams.put("recheckNo", recheckNo);
				conParams.put("locno", locno);
				List<BillWmRecheckDtl> conList=billWmRecheckDtlMapper.findBillRecheckBox(conParams, authorityParams);
				if(null!=conList && conList.size()>0){
					for(BillWmRecheckDtl checkDtl:conList){
						AccInventoryConDto accInventoryConDto = new AccInventoryConDto();
						accInventoryConDto.setLocno(locno);
						accInventoryConDto.setBillNo(recheckNo);
						accInventoryConDto.setBillType("RC");
						accInventoryConDto.setConNo(checkDtl.getScanLabelNo());
						accInventoryConDto.setCellNo(checkDtl.getCellNo());
						accInventoryConDto.setDirection(1);
						accInventoryConDto.setCreator(userName);
						accInventoryConDto.setMoveSkuQty(checkDtl.getItemQty());
						accInventoryConDto.setSupplierNo(supplierNo);
						accInventoryConDto.setItemType("0");
						accInventoryConDto.setQuality("0");
						accInventoryConSkuServiceApi.accontingForCon(accInventoryConDto);
					}
				}
			}
//			//更改复核状态
//			String recheckStatus = STATUS45;
//			BillWmOutstockDtl wmOutstockDtl = new BillWmOutstockDtl();
//			wmOutstockDtl.setLocno(locno);
//			wmOutstockDtl.setOwnerNo(ownerNo);
//			wmOutstockDtl.setSourceNo(recedeNo);
//			List<BillWmOutstockDtlDto> listDtlDtos=billWmOutstockDtlMapper.selectOutstockDtlItem(wmOutstockDtl, null);
//			if(CommonUtil.hasValue(listDtlDtos)){
//				for (BillWmOutstockDtlDto b : listDtlDtos) {
//					BigDecimal b1 = b.getRealQty()==null?new BigDecimal(0): b.getRealQty();
//					BigDecimal b2 = b.getRecheckQty()==null?new BigDecimal(0): b.getRecheckQty();
//					if(b1.intValue() > b2.intValue()){
//						recheckStatus = STATUS40;
//						break;
//					}
//				}
//			}
			
			
//			//更新通知单状态
//			BillWmRecede entity = new BillWmRecede();
//			entity.setLocno(locno);
//			entity.setOwnerNo(ownerNo);
//			entity.setRecedeNo(recedeNo);
//			BillWmRecede billWmRecede = billWmRecedeMapper.selectByPrimaryKey(entity);
//			if(billWmRecede==null){
//				throw new ServiceException("查找退厂通知单失败!");
//			}
//			if(!STATUS40.equals(billWmRecede.getStatus())||recheckStatus.equals(STATUS45)){
//				billWmRecede.setStatus(recheckStatus);
//				int rCount = billWmRecedeMapper.updateByPrimaryKeySelective(billWmRecede);
//				if(rCount < 1){
//					throw new ServiceException("退厂通知单状态更新失败!");
//				}
//			}
//			
//			//添加状态
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("locno", locno);
//			params.put("billNo", recedeNo);
//			params.put("status", recheckStatus);
//			List<BillStatusLog> listLogs = billStatusLogService.findByBiz(null, params);
//			if(!CommonUtil.hasValue(listLogs)){
//				String message = "更新退厂通知单状态为部分复核";
//				if(recheckStatus.equals(STATUS45)){
//					message = "更新退厂通知单状态为复核完成";
//				}
//				billStatusLogService.procInsertBillStatusLog(locno, recedeNo, "VM", recheckStatus, message, userName);
//			}
			
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
		
		//装箱逻辑：procRecheckPackageBox
		//1,绑定或更新商品信息到箱子
//		if(!StringUtils.isBlank(containerType) && !StringUtils.isBlank(containerNo)){
//			ConLabel label = new ConLabel();
//			label.setLocno(locno);
//			label.setLabelNo(boxNo);
//			label.setContainerNo(containerNo);
//			label.setContainerType(containerType);
//			conLabelMapper.insertSelective(label);//保存主label表
//			for(Long i=0L;i<dtlLst.size();i++){
//				BillWmOutstockDtl d = dtlLst.get(i.intValue());
//				ConLabelDtl dtl = new ConLabelDtl();
//				dtl.setLocno(locno);
//				dtl.setContainerNo(containerNo);
//				dtl.setContainerType(containerType);
//				dtl.setRowId(i);
//				dtl.setExpDate(new Date());
//				dtl.setItemNo(d.getItemNo());
//				dtl.setQty(d.getItemQty());
//				conLabelDtlMapper.insertSelective(dtl);//保存label_dtl表
//			}
//		}
//		
//		//2,把商品信息更新到复核单详细里面
//		for(Long i=0L;i<dtlLst.size();i++){
//			BillWmOutstockDtl d = dtlLst.get(i.intValue());
//			BillWmRecheckDtl dtl = new BillWmRecheckDtl();
//			dtl.setRecheckNo(recheckNo);
//			dtl.setLocno(locno);
//			dtl.setContainerNo(containerNo);
//			dtl.setRowId(i);
//			dtl.setItemNo(d.getItemNo());
//			dtl.setItemQty(d.getItemQty());
//			dtl.setRealQty(d.getRealQty());
//			dtl.setRecedeDate(new Date());
//			dtl.setPackQty(BigDecimal.ONE);
//			dtl.setStatus("11");
//			billWmRecheckDtlMapper.insertSelective(dtl);
//		}
		
		//3,更新分货单详情数据信息
						
						
	}

	@Override
	public void check(String ids, String loginName, String checkUser) throws ServiceException {
		
		try{
			if(StringUtils.isNotBlank(ids)){
				String[] idArr = ids.split(",");
				for(String id : idArr){
					String[] tmp = id.split("-");
					if(tmp.length==2){
						
						BillWmRecheck recheck = new BillWmRecheck();
						recheck.setRecheckNo(tmp[0]);
						recheck.setLocno(tmp[1]);
						recheck = billWmRecheckMapper.selectByPrimaryKey(recheck);
						
						
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("recheckNo", tmp[0]);
						params.put("locno", tmp[1]);
						List<BillWmRecheckDtl> list = billWmRecheckDtlMapper.selectByParams(null, params);
						if(!CommonUtil.hasValue(list)){
							throw new ServiceException("复核明细为空不能审核!");
						}
						
						
						Map<String, String> mapC = new HashMap<String, String>();
						mapC.put("v_locno", recheck.getLocno());
						mapC.put("v_recedeNo", list.get(0).getRecedeNo());
						mapC.put("v_supplierNo", recheck.getSupplierNo());
						mapC.put("v_userId", loginName);
						
						String msg = "";
						billWmRecheckMapper.procRecheckPackageBox(mapC);
						if (!RESULTY.equals(mapC.get("strOutMsg"))) {
							String stroutmsg = mapC.get("strOutMsg");
							if(StringUtils.isNotBlank(stroutmsg)){
								String[] msgs = stroutmsg.split("\\|");
								msg = msgs[1];
							}
							throw new ServiceException(msg);
						}
						
//						BillWmRecheck recheck = new BillWmRecheck();
//						recheck.setRecheckNo(tmp[0]);
//						recheck.setLocno(tmp[1]);
//						recheck = billWmRecheckMapper.selectByPrimaryKey(recheck);
//						if(recheck!=null && StringUtils.equals("11", recheck.getStatus())){
//							recheck.setStatus("10");
//							recheck.setAuditor(loginName);
//							recheck.setAudittm(new Date());
//							billWmRecheckMapper.updateByPrimaryKeySelective(recheck);
//						}
					}
				}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void deleteBillWmOutStockRecheck(List<BillWmRecheck> listWmRechecks) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(listWmRechecks)){
				throw new ServiceException("参数不合法!");
			}
			for (BillWmRecheck b : listWmRechecks) {
				Map<String, String> map = new HashMap<String, String>();
	   			map.put("I_locno", b.getLocno());
	   			map.put("I_recheckNo", b.getRecheckNo());
	   			
	   			billWmRecheckMapper.procWmOutStockRecheckDel(map);
	   			if (!RESULTY.equals(map.get("O_msg"))) {
	   				String message = "";
	   				String msg = map.get("O_msg");
	   				if(StringUtils.isNotBlank(msg)){
	   					String[] msgs = msg.split("\\|");
	   					message = msgs[1];
	   				}
	   				throw new ServiceException(b.getRecheckNo()+message);
	   			}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
}