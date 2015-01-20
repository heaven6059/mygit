package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
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
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillContainerTaskService;
import com.yougou.logistics.city.service.BillOmPrepareDivideDtlService;
import com.yougou.logistics.city.service.BillOmPrepareDivideService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@Service("billOmPrepareDivideManager")
class BillOmPrepareDivideManagerImpl extends BaseCrudManagerImpl implements BillOmPrepareDivideManager {
	@Resource
	private BillOmPrepareDivideService billOmPrepareDivideService;
	@Resource
	private BillOmPrepareDivideDtlService billOmPrepareDivideDtlService;
    @Resource
    private BillAccControlService billAccControlService;
    @Resource
    private ConBoxService conBoxService;
    @Resource
    private BmContainerService bmContainerService;
    @Resource
    private BillContainerTaskService billContainerTaskService;
    @Resource
    private ProcCommonService procCommonService;
    
    private static final String STATUS10 = "10";
    
    private static final String STATUS11 = "11";
    
    private static final String BUSINESSTYPE1 = "1";

	@Override
	public BaseCrudService init() {
		return billOmPrepareDivideService;
	}


	public void saveMain(BillImReceipt receipt) throws ManagerException {
		try {
			billOmPrepareDivideService.saveMain(receipt);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}


	@Override
	public Map<String, Object> update(BillImReceipt receipt, List<BillImReceiptDtl> insert,List<BillImReceiptDtl> del) throws ManagerException {
		try {
			return this.billOmPrepareDivideService.update(receipt, insert,del);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int auditBatch(String keyStr, String locno, String ownerNo, SystemUser user) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BillImReceipt receipt = null;
			for (String obj : strs) {
				try {
					Map<String,Object> selectDtlMap=new HashMap<String,Object>();
					selectDtlMap.put("locno", locno);
					selectDtlMap.put("ownerNo", ownerNo);
					selectDtlMap.put("receiptNo", obj);
					int dtlCount=billOmPrepareDivideDtlService.findCount(selectDtlMap);
					if(dtlCount==0){
						throw new ManagerException("单据无明细，不允许审核。");
					}
					
					receipt = new BillImReceipt();
					receipt.setAuditor(user.getLoginName());
					receipt.setAuditorName(user.getUsername());
					receipt.setAudittm(new Date());
					receipt.setLocno(locno);
					receipt.setOwnerNo(ownerNo);
					receipt.setReceiptNo(obj);
					receipt.setStatus(STATUS11);
					receipt.setCheckStatus(STATUS10);
					count = billOmPrepareDivideService.auditReceipt(receipt);
					if(count < 1){
						throw new ManagerException("单据"+obj+"已删除或状态已改变!");
					}
					
					//更新明细状态为11已审核
					BillImReceiptDtl receiptDtl = new BillImReceiptDtl();
					receiptDtl.setLocno(locno);
					receiptDtl.setOwnerNo(ownerNo);
					receiptDtl.setReceiptNo(obj);
					receiptDtl.setStatus(STATUS11);
					count = billOmPrepareDivideDtlService.modifyById(receiptDtl);
					if(count < 1){
						throw new ManagerException("单据"+obj+"更改明细状态为已审核失败!");
					}
					
					
					/**
					 * 释放拆板得箱容器库存
					 */
					//1.准备预分货单明细中卡板对应的箱数据
					List<String> boxList = new ArrayList<String>();
					List<String> panReceiptList = new ArrayList<String>();
					List<BillContainerTaskDtl> taskDtlList = new ArrayList<BillContainerTaskDtl>();
					Map<String, List<String>> boxNumMap = new HashMap<String, List<String>>();
					Map<String, List<BillImReceiptDtl>> boxReceiptMap = new HashMap<String, List<BillImReceiptDtl>>();
					List<BillImReceiptDtl> dtlList=billOmPrepareDivideDtlService.selectBoxPan4ReceiptDtl(selectDtlMap);
					
					long rowId = 0L;
					for (BillImReceiptDtl bd : dtlList) {
						if(StringUtils.isNotBlank(bd.getPanNo())){
							List<BillImReceiptDtl> dtlReceiptList = null;
							List<String> boxNumList = null;
							if(boxReceiptMap.get(bd.getPanNo())==null){
								boxList.add(bd.getPanNo());//把预分货单有多少个板号添加进list之后查询对比
								dtlReceiptList = new ArrayList<BillImReceiptDtl>();
								dtlReceiptList.add(bd);//把板号对应的箱明细放入map
								boxNumList = new ArrayList<String>();
								boxNumList.add(bd.getBoxNo());//把板号对应的箱放入map
							}else{
								dtlReceiptList = boxReceiptMap.get(bd.getPanNo());
								dtlReceiptList.add(bd);
								//剔除板号中重复的箱号添加到对比的箱map中
								boxNumList = boxNumMap.get(bd.getPanNo());
								if(!boxNumList.contains(bd.getBoxNo())){
									boxNumList.add(bd.getBoxNo());
								}
							}
							boxReceiptMap.put(bd.getPanNo(), dtlReceiptList);
							boxNumMap.put(bd.getPanNo(), boxNumList);
						}else{
							BillContainerTaskDtl taskDtl = new BillContainerTaskDtl();
							setBillContainerTaskDtl(bd, taskDtl, user);
							taskDtl.setRowId(++rowId);
							taskDtlList.add(taskDtl);
						}
					}
					
//					if(!CommonUtil.hasValue(boxList)){
//						throw new ManagerException(obj+"查询预分货单对应的箱明细为空!");
//					}
					
					//2.准备查询卡板中所有箱中的数据
					List<ConBox> boxList2 = conBoxService.selectBoxNumByPanNo(selectDtlMap, boxList);
					Map<String, Integer> boxMap = new HashMap<String, Integer>();
					for (ConBox cb : boxList2) {
						if(boxMap.get(cb.getPanNo()) == null){
							boxMap.put(cb.getPanNo(), cb.getBoxNum());
						}
					}
					
					//3.开始比对是否是拿卡板中部分箱去做预分货单
					if(boxNumMap != null){
						for(Map.Entry<String, List<String>> entry : boxNumMap.entrySet()) {
							String panNo = entry.getKey();
							if(boxMap.get(panNo) != null){
								int boxNum = boxMap.get(panNo);
								int rBoxNum = entry.getValue().size();
								if((boxNum>0&&rBoxNum>0)&&boxNum!=rBoxNum){
									//解板从卡板中部分箱去做预分货单的
									List<BillImReceiptDtl> receiptDtlList=boxReceiptMap.get(panNo);
									for (BillImReceiptDtl bd : receiptDtlList) {
										BillContainerTaskDtl taskDtl = new BillContainerTaskDtl();
										setBillContainerTaskDtl(bd, taskDtl, user);
										taskDtl.setRowId(++rowId);
										taskDtlList.add(taskDtl);
									}
								}else{
									//准备整板的数据准备解锁
									//先把整板板号添加
									panReceiptList.add(panNo);
									List<BillImReceiptDtl> receiptDtlList=boxReceiptMap.get(panNo);
									//添加板下所有的箱
									for (BillImReceiptDtl brd : receiptDtlList) {
										panReceiptList.add(brd.getBoxNo());
									}
								}
							}
						}
					}
					
					
					//4.开始插入容器任务数据
					if(CommonUtil.hasValue(taskDtlList)){
						BillContainerTask containerTask = new BillContainerTask();
						containerTask.setLocno(locno);
						containerTask.setContaskNo(receipt.getReceiptNo());
						containerTask.setCreator(user.getLoginName());
						containerTask.setCreatorname(user.getUsername());
						containerTask.setCreatetm(new Date());
						containerTask.setEditor(user.getLoginName());
						containerTask.setEditorname(user.getUsername());
						containerTask.setEdittm(new Date());
						containerTask.setUseType(ContainerTypeEnums.E.getOptBillType());
						containerTask.setBusinessType(BUSINESSTYPE1);
						int containerCount = billContainerTaskService.insertBillContainerTask(containerTask, taskDtlList);
						if(containerCount < 1){
							throw new ManagerException(receipt.getReceiptNo()+"插入容器任务数据失败!");
						}
						
						//5.开始审核容器
						procCommonService.procContaskAudit(locno, ContainerTypeEnums.E.getOptBillType(),
								receipt.getReceiptNo(), user.getLoginName());
					}
					
					//5.检查是否有需要解锁的整板数据
					List<BmContainer> delBoxContainerList = new ArrayList<BmContainer>();
					if(CommonUtil.hasValue(panReceiptList)){
						for (String panNo : panReceiptList) {
							BmContainer bmContainer = new BmContainer();
							bmContainer.setLocno(receipt.getLocno());
							bmContainer.setConNo(panNo);
							bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
							bmContainer.setFalg("Y");
							delBoxContainerList.add(bmContainer);
						}
						int containerCount = bmContainerService.batchUpdate(delBoxContainerList);
						if(containerCount < 1){
							throw new ServiceException("解锁预分货单箱号容器失败!");
						}
					}
					
					//6.批量更新箱状态为22
					billOmPrepareDivideDtlService.batchUpdateBoxStatus4Prepare(selectDtlMap);
					
				} catch (ManagerException e) {
					throw new ManagerException(e.getMessage());
				}catch (Exception e) {
					throw new ManagerException(e.getMessage());
				}
			}
		}
		return count;
	}
	
	public void setBillContainerTaskDtl(BillImReceiptDtl bd,BillContainerTaskDtl taskDtl,SystemUser user){
		taskDtl.setLocno(bd.getLocno());
		taskDtl.setContaskNo(bd.getReceiptNo());
		taskDtl.setsSubContainerNo(bd.getBoxNo());
		taskDtl.setsCellNo(bd.getCellNo());
		taskDtl.setBrandNo(bd.getBrandNo());
		taskDtl.setItemNo(bd.getItemNo());
		taskDtl.setSizeNo(bd.getSizeNo());
		taskDtl.setQty(Long.valueOf(String.valueOf(bd.getReceiptQty())));
		taskDtl.setCreator(user.getLoginName());
		taskDtl.setCreatorname(user.getUsername());
		taskDtl.setEditor(user.getLoginName());
		taskDtl.setEditorname(user.getUsername());
		taskDtl.setQuality(bd.getQuality());
		taskDtl.setItemType(bd.getItemType());
		taskDtl.setdContainerNo("N");
		taskDtl.setdSubContainerNo("N");
		taskDtl.setdCellNo("N");
		if(StringUtils.isNotBlank(bd.getPanNo())){
			taskDtl.setContainerType(ContainerTypeEnums.P.getOptBillType());
			taskDtl.setsContainerNo(bd.getPanNo());
		}else{
			taskDtl.setContainerType(ContainerTypeEnums.C.getOptBillType());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBatch(String keyStr, String locno, String ownerNo) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BillImReceipt receipt = null;
			for (String obj : strs) {
				try {
					receipt = new BillImReceipt();
					receipt.setLocno(locno);
					receipt.setOwnerNo(ownerNo);
					receipt.setReceiptNo(obj);
					
					Map<String,Object> selectDtlMap=new HashMap<String,Object>();
					selectDtlMap.put("locno", locno);
					selectDtlMap.put("ownerNo", ownerNo);
					selectDtlMap.put("receiptNo", obj);
					
					//释放容器全部明细准备数据
					List<BillImReceiptDtl> receiptDtlList=billOmPrepareDivideDtlService.selectBoxPan4ReceiptDtl(selectDtlMap);
					List<String> delList = new ArrayList<String>();
					List<BmContainer> delContainerList = new ArrayList<BmContainer>();
					if(CommonUtil.hasValue(receiptDtlList)){
						for (BillImReceiptDtl dtl : receiptDtlList) {
							//如果板号为空,锁箱号,否则把整板都锁掉
							//如果板号为空,锁箱号,否则把整板都锁掉
							String panNo = dtl.getPanNo();
							String boxNo = dtl.getBoxNo();
							if(!delList.contains(panNo)&&StringUtils.isNotBlank(panNo)){
								delList.add(panNo);
							}
							if(!delList.contains(boxNo)){
								delList.add(boxNo);
							}
						}
					}
					
					
					int dtlCount=billOmPrepareDivideDtlService.findCount(selectDtlMap);
					//删除时整单记录账
					if(dtlCount>0){
						//调用内部存储过程
						BillAccControlDto dto = new BillAccControlDto();
						dto.setiPaperNo(obj);
						dto.setiLocType("2");
						dto.setiPaperType(CNumPre.IM_RECEDE_PRE);
						dto.setiIoFlag("O");
						dto.setiPrepareDataExt(new BigDecimal(0));
						dto.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dto);
					}
					
					receipt.setCheckStatus(STATUS10);
					count = billOmPrepareDivideService.deleteById(receipt);
					if(count < 1){
						throw new ManagerException("单据"+obj+"已删除或状态已改变!");
					}
					//删除从表
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("receiptNo", obj);
					map.put("locno", locno);
					List<BillImReceiptDtl> dtlList = billOmPrepareDivideDtlService.findByBiz(null, map);
					for (BillImReceiptDtl dto : dtlList) {
						billOmPrepareDivideDtlService.deleteById(dto);
					}
					
					
					/*************删除解锁预分货单所有的容器start************/
					//解锁箱容器数据
					int containerCount = 0;
					if(CommonUtil.hasValue(delList)){
						for (String panNo : delList) {
							BmContainer bmContainer = new BmContainer();
							bmContainer.setLocno(receipt.getLocno());
							bmContainer.setConNo(panNo);
							bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
							bmContainer.setFalg("Y");
							delContainerList.add(bmContainer);
						}
						containerCount = bmContainerService.batchUpdate(delContainerList);
						if(containerCount < 1){
							throw new ServiceException("解锁预分货单箱号容器失败!");
						}
					}
					
					//解锁板号容器
//					if(CommonUtil.hasValue(delPanList)){
//						for (String panNo : delPanList) {
//							BillImReceiptDtl receiptDtl = new BillImReceiptDtl();
//							receiptDtl.setLocno(receipt.getLocno());
//							receiptDtl.setOwnerNo(receipt.getOwnerNo());
//							receiptDtl.setReceiptNo(receipt.getReceiptNo());
//							receiptDtl.setPanNo(panNo);
//							containerCount = billOmPrepareDivideDtlService.selectPanIsExist(receiptDtl);
//							if(containerCount < 1){
//								BmContainer bmContainer = new BmContainer();
//								bmContainer.setLocno(receipt.getLocno());
//								bmContainer.setConNo(panNo);
//								boolean result = bmContainerService.checkBmContainerStatus(bmContainer);
//								if(result){
//									bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
//									bmContainer.setFalg("Y");
//									delPanContainerList.add(bmContainer);
//								}
//							}
//						}
//						
//						if(CommonUtil.hasValue(delPanContainerList)){
//							containerCount = bmContainerService.batchUpdate(delPanContainerList);
//							if(containerCount < 1){
//								throw new ServiceException("解锁预分货单板号容器失败!");
//							}
//						}
//					}
					/*************删除解锁预分货单所有的容器end************/
					
				} catch (Exception e) {
					throw new ManagerException(e.getMessage());
				}
			}
		}
		return count;
	}

	@Override
	public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideService.findMainReciptCount(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideService.findMainRecipt(page, map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}


	@Override
	public int findBoxNo4DivideCount(Map map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideService.findBoxNo4DivideCount(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}

	}

	@Override
	public List<?> findBoxNo4Divide(SimplePage page, Map map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmPrepareDivideService.findBoxNo4Divide(page, map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public void overPrepareDivide(List<BillImReceipt> receiptList, String userName) throws ManagerException {
		try {
			billOmPrepareDivideService.overPrepareDivide(receiptList, userName);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
}