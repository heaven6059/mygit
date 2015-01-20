package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.BillChPlanIsByBrandEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanDtl;
import com.yougou.logistics.city.common.model.BillChPlanKey;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.service.BillChPlanDtlService;
import com.yougou.logistics.city.service.BillChPlanService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ItemService;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 14:14:53 CST 2013
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
@Service("billChPlanDtlManager")
class BillChPlanDtlManagerImpl extends BaseCrudManagerImpl implements BillChPlanDtlManager {
    @Resource
    private BillChPlanDtlService billChPlanDtlService;
    @Resource
    private BillChPlanService billChPlanService;
    
    @Resource
    private CmDefcellService cmDefcellService;

    @Resource
	private ItemService itemService;
    @Override
    public BaseCrudService init() {
        return billChPlanDtlService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String save(BillChPlan planParams,List<BillChPlanDtl> insertedList , List<BillChPlanDtl>  deletedList) throws ManagerException {
		try {
			String planNo = planParams.getPlanNo();
			String locno = planParams.getLocno();
			String ownerNo = planParams.getOwnerNo();
			
			BillChPlanKey billChPlanKey = new BillChPlanKey();
			billChPlanKey.setLocno(locno);
			billChPlanKey.setPlanNo(planNo);
			
			BillChPlan billChPlan = (BillChPlan) billChPlanService.findById(billChPlanKey);
			if(billChPlan==null || !billChPlan.getStatus().equals(BillChPlanStatusEnums.CREATE.getValue())){
				throw new ManagerException("盘点计划单不存在或状态已改变。");
			}
			//删除明细
			for(BillChPlanDtl dtl:deletedList){
				billChPlanDtlService.deleteByBillKey(dtl);
			}
			
			//新增明细
			long planId = billChPlanDtlService.findMaxId();
			for(BillChPlanDtl dtl:insertedList){
				dtl.setPlanNo(planNo);
				dtl.setLocno(locno);
				dtl.setOwnerNo(ownerNo);
				dtl.setPlanId(++planId);
			}
			if(insertedList.size() > 0){
				billChPlanDtlService.batchInsertDtl(insertedList);
			}

			//查询明细是否重复
			String planType=billChPlan.getPlanType();
			Map<String, Object> findDupParams = new HashMap<String, Object>();
			findDupParams.put("locno", locno);
			findDupParams.put("planNo", planNo);
			List<BillChPlanDtl> dupList=billChPlanDtlService.findDuplicateRecord(findDupParams);
			if(dupList.size()>0){
				StringBuilder msg = new StringBuilder();
				for(BillChPlanDtl dtl : dupList){
					//商品盘
					if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){
						msg.append("商品:").append(dtl.getItemNo()).append("<br>尺码:").append(dtl.getSizeNo()).append("<br>");
					}else if(BillChPlanTypeEnums.CELL.getValue().equals(planType)){//储位盘
						msg.append("储位:").append(dtl.getCellNo()).append("<br>");
					}
				}
				msg.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
				throw new ManagerException(msg.toString());
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String saveByBrand(BillChPlan billChPlan) throws ManagerException {
		try {
			int a = 0;
			//查找最大PLAN_ID
			long planId = billChPlanDtlService.findMaxId() + 1;
			String brandNo = billChPlan.getBrandNo();
			String planNo = billChPlan.getPlanNo();
			String locno = billChPlan.getLocno();
			String ownerNo = billChPlan.getOwnerNo();
			Item itemDtl = new Item();
			itemDtl.setBrandNo(brandNo);
			int total = this.itemService.countItemAndSize(itemDtl,null);
			SimplePage page = new SimplePage(0, total,  total);
			List<Item> list = this.itemService.findItemAndSizePage(page, itemDtl,null);
			if(list != null && list.size() > 0){
				//1.保存明细
				BillChPlanDtl dtl = new BillChPlanDtl();
				dtl.setLocno(locno);
				dtl.setOwnerNo(ownerNo);
				dtl.setPlanNo(planNo);
				for(Item item:list){
					dtl.setItemNo(item.getItemNo());
					dtl.setSizeNo(item.getSizeNo());
					dtl.setPlanId(planId);
					planId++;
					a = billChPlanDtlService.add(dtl);
					if(a < 1){
						throw new ManagerException("新增明细异常");
					}
				}
				//2.回写
				BillChPlan dto = new BillChPlan();
				dto.setLocno(locno);
				dto.setPlanNo(planNo);
				dto.setBrandNo(brandNo);
				dto.setLimitBrandFlag(BillChPlanIsByBrandEnums.IS.getValue());
				a = billChPlanService.modifyById(dto);
				if(a < 1){
					throw new ManagerException("回写计划单主表【品牌】异常");
				}
			}else{
				throw new ManagerException("没有找到品牌为【"+brandNo+"】的商品");
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
		return null;
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String saveDtlBatch(BillChPlan billChPlan, String wareNo,
			String areaNo, String stockNo) throws ManagerException {
		try {
			long planId = billChPlanDtlService.findMaxId();
			int pageSize = 100;
			int pageNo = 1;
			int totalPage = 0;
			int total = 0;
			String locno = billChPlan.getLocno();
			String ownerNo = billChPlan.getOwnerNo();
			String planNo = billChPlan.getPlanNo();
			String planType = billChPlan.getPlanType();
			if(!BillChPlanTypeEnums.CELL.getValue().equals(planType)){
				throw new ManagerException("请选择按储位盘的计划单");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("planNo", planNo);
			List<Map<String,Object>> cellNoList = billChPlanDtlService.findCellNo(params);
			Map<String, Object> cellMap = listToMap(cellNoList);
			String cellNo;
			List<CmDefcell> cellList = null;
			List<BillChPlanDtl> list = null;
			BillChPlanDtl billChPlanDtl = null;
			params.put("wareNo", wareNo);
			params.put("areaNo", areaNo);
			params.put("stockNo", stockNo);
			total = cmDefcellService.findCmDefcell4PlanCount(params);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			if(total%pageSize == 0){
				totalPage = total / pageSize;
			}else{
				totalPage = total / pageSize + 1;
			}
			while(pageNo <= totalPage){
				page.setPageNo(pageNo);
				cellList = cmDefcellService.findCmDefcell4Plan(page, params);
				list = new ArrayList<BillChPlanDtl>();
				for(CmDefcell c:cellList){
					cellNo = c.getCellNo();
					if(cellMap.get(cellNo) != null){
						throw new ManagerException("储位【"+cellNo+"】已经存在");
					}
					billChPlanDtl = new BillChPlanDtl();
					billChPlanDtl.setLocno(locno);
					billChPlanDtl.setOwnerNo(ownerNo);
					billChPlanDtl.setPlanNo(planNo);
					billChPlanDtl.setPlanId(planId++);
					
					billChPlanDtl.setWareNo(wareNo);
					billChPlanDtl.setAreaNo(areaNo);
					billChPlanDtl.setStockNo(c.getStockNo());
					billChPlanDtl.setCellNo(cellNo);
					list.add(billChPlanDtl);
				}
				if(list.size() > 0){
					billChPlanDtlService.batchInsertDtl(list);
				}
				pageNo++;
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}
	private Map<String, Object> listToMap(List<Map<String, Object>> list){
		Map<String, Object> map = new HashMap<String, Object>();
		if(list == null){
			return map;
		}
		Object obj;
		for(Map<String, Object> m:list){
			obj = m.get("CELL_NO");
			if(obj != null){
				map.put(obj.toString(), obj);
			}
		}
		return map;
	}
}