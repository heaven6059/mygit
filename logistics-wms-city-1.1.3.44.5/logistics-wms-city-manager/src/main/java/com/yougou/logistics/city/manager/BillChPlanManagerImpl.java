package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.BillChCheckStautsEnums;
import com.yougou.logistics.city.common.enums.BillChPlanIsByBrandEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanDtl;
import com.yougou.logistics.city.common.model.BillChPlanDtlBrand;
import com.yougou.logistics.city.common.model.BillChPlanDtlBrandKey;
import com.yougou.logistics.city.common.model.BillChPlanKey;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillChCheckDirectService;
import com.yougou.logistics.city.service.BillChCheckService;
import com.yougou.logistics.city.service.BillChPlanDtlBrandService;
import com.yougou.logistics.city.service.BillChPlanDtlService;
import com.yougou.logistics.city.service.BillChPlanService;
import com.yougou.logistics.city.service.BillChRecheckDtlService;
import com.yougou.logistics.city.service.BrandService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ConContentService;
import com.yougou.logistics.city.service.ItemService;
import com.yougou.logistics.city.service.ProcCommonService;

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
@Service("billChPlanManager")
class BillChPlanManagerImpl extends BaseCrudManagerImpl implements BillChPlanManager {
    @Resource
    private BillChPlanService billChPlanService;
    @Resource
    private BillChPlanDtlService billChPlanDtlService;
	@Resource
	private BillChCheckDirectService billChCheckDirectService;
	@Resource
	private CmDefcellService cmDefcellService;
	@Resource
	private ProcCommonService procCommonService;	
	@Resource
	private BillChCheckService billChCheckService;	
	@Resource
	private BillChRecheckDtlService billChRecheckDtlService;
	@Resource
	private BillChPlanDtlBrandService billChPlanDtlBrandService;
	@Resource
	private BrandService brandService;

    @Resource
	private ItemService itemService;
    @Resource
	private ConContentService conContentService;;
    @Override
    public BaseCrudService init() {
        return billChPlanService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String check(BillChPlan billChPlan, String planNos) throws ManagerException {
		
		try {
			String [] array = planNos.split(",");
			billChPlan.setSourceStatus(BillChPlanStatusEnums.CREATE.getValue());
			billChPlan.setStatus(BillChPlanStatusEnums.AUDIT.getValue());
			Map<String, Object> params = new HashMap<String, Object>();
			for(String planNo:array){
				billChPlan.setPlanNo(planNo);
				params.put("planNo", planNo);
				int b = billChPlanDtlService.findCount(params);
				if(b < 1){
					throw new ManagerException("计划单【"+planNo+"】不存在明细,审核失败!");
				}
				int a = billChPlanService.modifyById(billChPlan);
				if(a < 1){
					throw new ManagerException("计划单【"+planNo+"】已不存在或状态不为【"+BillChPlanStatusEnums.CREATE.getText()+"】,审核失败!");
				}
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String invalid(BillChPlan inBillChPlan, String[] planNos, SystemUser user) throws ManagerException {		
		try {			
			String locno=inBillChPlan.getLocno();
			String auditor=inBillChPlan.getAuditor();
			
			for(String planNo:planNos){
				//查询盘点计划单
				BillChPlanKey billChPlanKey=new BillChPlanKey();
				billChPlanKey.setLocno(locno);
				billChPlanKey.setPlanNo(planNo);
				BillChPlan billChPlan=(BillChPlan) billChPlanService.findById(billChPlanKey);
				String status= billChPlan.getStatus();
				if(!BillChPlanStatusEnums.AUDIT.getValue().equals(status) &&!BillChPlanStatusEnums.START.getValue().equals(status) && !BillChPlanStatusEnums.SEND.getValue().equals(status) && !BillChPlanStatusEnums.INITANDRECHECK.getValue().equals(status)){
					throw new ManagerException("只能作废审核、发起、已发单、初盘/复盘状态的单据!");
				}
				
				//解锁容器的状态 add by zuo.sw；
				billChCheckDirectService.callContrainStatus(planNo, locno, user);
					
				
				//解锁定位信息内的储位和库存
				Map<String, Object> directParams = new HashMap<String, Object>();
				directParams.put("planNo", planNo);
				directParams.put("locno", locno);
				directParams.put("statusNotEquals16", "1");
				List<BillChCheckDirect> directList=billChCheckDirectService.findByBiz(null,directParams);
				Map<String, Object> modifyConStatusParams = new HashMap<String, Object>();//锁定库存状态所需参数
				modifyConStatusParams.put("locno", locno);
				modifyConStatusParams.put("status","0");
				modifyConStatusParams.put("sourceStatus","1");
				modifyConStatusParams.put("editor",user.getLoginName());
				modifyConStatusParams.put("edittm",new Date());
				modifyConStatusParams.put("editorname",user.getUsername());
				//置储位盘点状态
				//Set<String> cellHadLockSet=new HashSet<String>();
				for(BillChCheckDirect direct:directList){
					String cellNo=direct.getCellNo();
					/*if(cellHadLockSet.contains(cellNo)){
						continue;
					}else{
						cellHadLockSet.add(cellNo);
					}*/
					CmDefcell cdParams=new CmDefcell();
					cdParams.setLocno(locno);
					cdParams.setCellNo(cellNo);
					cdParams.setCheckStatus(CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus());//盘点状态为可用
					cdParams.setEdittm(new Date());
					cmDefcellService.modifyById(cdParams);	
					
					//将该储位下的所有库存的盘点状态更新为可用
					//procCommonService.UpdateContentStatus(locno, null, cellNo, "0", null, null, auditor);
					modifyConStatusParams.put("itemNo", direct.getItemNo());
					modifyConStatusParams.put("cellNo", cellNo);
					modifyConStatusParams.put("barcode", direct.getBarcode());
					modifyConStatusParams.put("itemType", direct.getItemType());
					modifyConStatusParams.put("quality", direct.getQuality());
					conContentService.modifyStatus(modifyConStatusParams);
				}
				
				//解锁定位信息之外，盘点过程中增锁定的的储位
				Map<String, Object> checkParams = new HashMap<String, Object>();
				checkParams.put("planNo", planNo);
				checkParams.put("locno", locno);
				List<String> cellNoList=billChCheckService.findCellNo4Add(checkParams);
				for(String cellNo:cellNoList){
					CmDefcell cdParams=new CmDefcell();
					cdParams.setLocno(locno);
					cdParams.setCellNo(cellNo);
					cdParams.setCheckStatus(CmDefcellCheckStatusEnums.CHECKSTATUS_0.getStatus());//盘点状态为可用
					cdParams.setEdittm(new Date());
					cmDefcellService.modifyById(cdParams);	
				}
				
//				List<BillChCheckDirectDto> directDtoList=billChCheckDirectService.findDirectAndContent(directParams);
//				//并更新库存盘点状态更新为可用
//				for(BillChCheckDirectDto dto:directDtoList){
//					procCommonService.UpdateContentStatus(locno, dto.getContentId(), null, "0", null, null, auditor);
//				}
				
				
				//该盘点计划单下的盘点单状态置为关闭
				BillChCheck updateBillChCheck=new BillChCheck();
				updateBillChCheck.setLocno(locno);
				updateBillChCheck.setPlanNo(planNo);
				updateBillChCheck.setEditor(auditor);
				updateBillChCheck.setEditorName(inBillChPlan.getAuditorName());//更新人名称
				updateBillChCheck.setEdittm(new Date());
				updateBillChCheck.setStatus(BillChCheckStautsEnums.STATUS90.getStatus());
				billChCheckService.updateStatusByPlanNo(updateBillChCheck);
				
				//更新复盘明细为关闭
				Map<String, Object> updateRecheckDtlParams = new HashMap<String, Object>();
				updateRecheckDtlParams.put("locno", locno);
				updateRecheckDtlParams.put("planNo", planNo);
				updateRecheckDtlParams.put("status", BillChCheckStautsEnums.STATUS90.getStatus());
				billChRecheckDtlService.updateStatusByPlanNo(updateRecheckDtlParams);
								
				
				//更新盘点计划为作废
				BillChPlan modifyBillChPlan=new BillChPlan();
				modifyBillChPlan.setPlanNo(planNo);
				modifyBillChPlan.setLocno(locno);
				modifyBillChPlan.setEditor(auditor);
				modifyBillChPlan.setEditorName(inBillChPlan.getAuditorName());//更新人名称
				modifyBillChPlan.setEdittm(new Date());
				modifyBillChPlan.setStatus(BillChPlanStatusEnums.INVALID.getValue());
				int modifyCount = billChPlanService.modifyById(modifyBillChPlan);
				if(modifyCount < 1){
					throw new ManagerException("计划单["+planNo+"]作废失败!");
				}
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return null;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String deleteMain(BillChPlan billChPlan, String planNos) throws ManagerException {
		try {
			String [] array = planNos.split(",");
			BillChPlanDtl billChPlanDtl = new BillChPlanDtl();
			billChPlanDtl.setLocno(billChPlan.getLocno());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", billChPlan.getLocno());
			int a = 0;
			int dtlNum = 0;
			billChPlan.setSourceStatus(BillChPlanStatusEnums.CREATE.getValue());//只能删除新建状态的单据
			for(String planNo:array){
				billChPlan.setPlanNo(planNo);
				a = billChPlanService.deleteById(billChPlan);
				if(a < 1){
					throw new ManagerException("计划单【"+planNo+"】不存在或状态不为【"+BillChPlanStatusEnums.CREATE.getText()+"】!");
				}
				
				params.put("planNo", planNo);
				dtlNum = billChPlanDtlService.findCount(params);
				if(dtlNum > 0){
					billChPlanDtl.setPlanNo(planNo);
					a = billChPlanDtlService.deleteByPlanNo(billChPlanDtl);
					if(a < 1){
						throw new ManagerException("计划单["+planNo+"]明细删除失败!");
					}
				}
				
				//先删除所有的品牌对于关系
				BillChPlanDtlBrand dtlBrandDel = new BillChPlanDtlBrand();
				dtlBrandDel.setLocno(billChPlan.getLocno());
				dtlBrandDel.setPlanNo(billChPlan.getPlanNo());
				billChPlanDtlBrandService.deleteById(billChPlan);
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Object saveMain(BillChPlan billChPlan, AuthorityParams authorityParams) throws ManagerException {
		try {
			
			String planNo = billChPlan.getPlanNo();
			String planType = billChPlan.getPlanType();
			String limitBrandFlag = billChPlan.getLimitBrandFlag();
			String brandNo = billChPlan.getBrandNo();
			String sysNo = billChPlan.getSysNo();
			String brandNoSelect = "";
			
			
			// 如果不是商品盘、抽盘并且品牌编码为空，品牌库编码不为空 为全选
			if (("0".equals(planType) && "1".equals(limitBrandFlag))
					|| ("1".equals(planType) && "0".equals(limitBrandFlag))
					|| ("1".equals(planType) && "1".equals(limitBrandFlag))) {
				
				//验证品牌库
				if (StringUtils.isEmpty(sysNo)) {
					throw new ManagerException("请选择品牌库!");
				}
				
				//是否全选
				if(StringUtils.isNotBlank(brandNo)){
					String[] brandNos = brandNo.split(",");
					for (String b : brandNos) {
						BillChPlanDtlBrand dtlBrand = new BillChPlanDtlBrand();
						dtlBrand.setLocno(billChPlan.getLocno());
						dtlBrand.setOwnerNo(billChPlan.getOwnerNo());
						dtlBrand.setPlanNo(billChPlan.getPlanNo());
						dtlBrand.setBrandNo(b);
						int count = billChPlanDtlBrandService.add(dtlBrand);
						if(count < 1){
							throw new ManagerException("保存品牌库失败!");
						}
					}
					billChPlan.setBrandNo("");
				}else{
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", billChPlan.getLocno());
					params.put("ownerNo", billChPlan.getOwnerNo());
					params.put("planNo", billChPlan.getPlanNo());
					params.put("sysNo", billChPlan.getSysNo());
					int count = billChPlanDtlBrandService.batchInsertPlanDtlBrand(params,authorityParams);
					if (count < 1) {
						throw new ManagerException("当前用户所属的品牌库权限,没有查询到可添加的品牌!");
					}
				}
			}
			
			//选择主档信息
			int a = billChPlanService.add(billChPlan);
			int pageSize = 1000;
			if(a < 1){
				throw new ManagerException("保存计划单主信息失败!");
			}
			
			//商品盘全盘需要添加所有的商品信息
			if(BillChPlanTypeEnums.ITEM.getValue().equals(planType)){//商品盘
				
				//查询对应品牌的商品
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", billChPlan.getLocno());
				params.put("planNo", billChPlan.getPlanNo());
				List<BillChPlanDtlBrand> dtlBrandList = billChPlanDtlBrandService.findByBiz(null, params);
				if(BillChPlanIsByBrandEnums.IS.getValue().equals(limitBrandFlag)){//限制品牌(全盘)
					
					if(!CommonUtil.hasValue(dtlBrandList)){
						throw new ManagerException("查询品牌关联表数据为空!");
					}
					for (BillChPlanDtlBrand dtlBrand : dtlBrandList) {
						brandNoSelect+="'"+dtlBrand.getBrandNo()+"',"; 
					}
					if(StringUtils.isNotBlank(brandNoSelect)){
						brandNoSelect=brandNoSelect.substring(0, brandNoSelect.length()-1);
					}
					brandNo = brandNoSelect;
					
					long planId = billChPlanDtlService.findMaxId() + 1;
					String locno = billChPlan.getLocno();
					String ownerNo = billChPlan.getOwnerNo();
					Item itemDtl = new Item();
					itemDtl.setBrandNo(brandNoSelect);
					itemDtl.setLocno(locno);
					itemDtl.setPlanNo(planNo);
					itemDtl.setQuality(billChPlan.getQuality());
					itemDtl.setItemType(billChPlan.getItemType());
					
					//检查该品牌下的商品，是否可进行盘点
					List<Item> itemList = itemService.isChPlanItemCheck(itemDtl);
					if(CommonUtil.hasValue(itemList)){
						Item item = itemList.get(0);
						StringBuffer sb = new StringBuffer();
						sb.append("【商品："+item.getItemNo()+"】,");
						sb.append("【品牌："+item.getBrandNo()+"】,");
						sb.append("【储位："+item.getCellNo()+"】,");
						sb.append("不允许盘点。");
						sb.append("原因：该品牌下的商品存在预上或预下，或手工移库标示为<不允许手工移库>。");
						throw new ManagerException(sb.toString());
					}
					
					List<Item> list = this.itemService.findChPlanItemAndSize4Brand(itemDtl);
					if(list != null && list.size() > 0){
						//1.保存明细
						List<BillChPlanDtl> billChPlanDtls = new ArrayList<BillChPlanDtl>();
						for(Item item:list){
							BillChPlanDtl dtl = new BillChPlanDtl();
							dtl.setLocno(locno);
							dtl.setOwnerNo(ownerNo);
							dtl.setPlanNo(planNo);
							dtl.setItemNo(item.getItemNo());
							dtl.setSizeNo(item.getSizeNo());
							dtl.setPlanId(planId);
							dtl.setBrandNo(item.getBrandNo());
							planId++;
							billChPlanDtls.add(dtl);
						}
						for(int i=0;i<billChPlanDtls.size();){
							i += pageSize;
							if(i>billChPlanDtls.size()){
								billChPlanDtlService.batchInsertDtl(billChPlanDtls.subList(i-pageSize, billChPlanDtls.size()));
							}else{
								billChPlanDtlService.batchInsertDtl(billChPlanDtls.subList(i-pageSize, i));
							}
						}
					}else{
						throw new ManagerException("没有找到品牌为【"+brandNo+"】的商品");
					}
				}
			}else{
				//储位盘添加所有的储位
				if(BillChPlanIsByBrandEnums.IS.getValue().equals(limitBrandFlag)){//限制品牌(全盘)
					long planId = billChPlanDtlService.findMaxId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", billChPlan.getLocno());
					params.put("planNo", billChPlan.getPlanNo());
					params.put("planId", planId);
					int count = billChPlanDtlService.batchInsertPlanDtl4AllCell(params);
					if(count < 1){
						throw new ManagerException("添加全盘储位失败!");
					}
				}
			}
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public void editMainInfo(BillChPlan billChPlan, AuthorityParams authorityParams) throws ManagerException {
		try{
			
			//查询
			BillChPlanKey planKey = new BillChPlanKey();
			planKey.setLocno(billChPlan.getLocno());
			planKey.setPlanNo(billChPlan.getPlanNo());
			BillChPlan chPlan = (BillChPlan)billChPlanService.findById(planKey);
			if(chPlan == null || !"10".equals(chPlan.getStatus())){
				throw new ManagerException("盘点计划单【"+billChPlan.getPlanNo()+"】已不存在或状态不为【"+BillChPlanStatusEnums.CREATE.getText()+"】");
			}
			
			//赋值
			String planType = chPlan.getPlanType();
			String limitBrandFlag = chPlan.getLimitBrandFlag();
			String sysNo = chPlan.getSysNo();
			String brandNo = billChPlan.getBrandNo();
			
			
			// 如果不是商品盘、抽盘并且品牌编码为空，品牌库编码不为空 为全选
			if("1".equals(planType)&&"0".equals(limitBrandFlag)){
				if(StringUtils.isEmpty(sysNo)){
					throw new ManagerException("品牌库不能为空!");
				}
				//先删除所有的品牌对于关系
				BillChPlanDtlBrand dtlBrandDel = new BillChPlanDtlBrand();
				dtlBrandDel.setLocno(chPlan.getLocno());
				dtlBrandDel.setPlanNo(chPlan.getPlanNo());
				billChPlanDtlBrandService.deleteById(chPlan);
				
				if(StringUtils.isNotBlank(brandNo)){
					String[] brandNos = brandNo.split(",");
					for (String b : brandNos) {
						BillChPlanDtlBrand dtlBrand = new BillChPlanDtlBrand();
						dtlBrand.setLocno(chPlan.getLocno());
						dtlBrand.setOwnerNo(chPlan.getOwnerNo());
						dtlBrand.setPlanNo(chPlan.getPlanNo());
						dtlBrand.setBrandNo(b);
						int count = billChPlanDtlBrandService.add(dtlBrand);
						if(count < 1){
							throw new ManagerException("保存品牌对应关系表失败!");
						}
					}
					billChPlan.setBrandNo("");
				}else{
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", chPlan.getLocno());
					params.put("ownerNo", chPlan.getOwnerNo());
					params.put("planNo", chPlan.getPlanNo());
					params.put("sysNo", sysNo);
					int count = billChPlanDtlBrandService.batchInsertPlanDtlBrand(params, authorityParams);
					if(count < 1){
						throw new ManagerException("当前用户所属的品牌库权限,没有查询到可添加的品牌!");
					}
				}
			}
			
			//修改主档信息
			int i = billChPlanService.modifyById(billChPlan);
			if(i <= 0){
				throw new ManagerException("盘点计划单【"+billChPlan.getPlanNo()+"】已不存在或状态不为【"+BillChPlanStatusEnums.CREATE.getText()+"】");
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
}