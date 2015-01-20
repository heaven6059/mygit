package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.BillSmWastePrintDto;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillContainerTaskService;
import com.yougou.logistics.city.service.BillSmWasteDtlService;
import com.yougou.logistics.city.service.BillSmWasteService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ProcCommonService;
import com.yougou.logistics.city.service.SizeInfoService;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
@Service("billSmWasteManager")
class BillSmWasteManagerImpl extends BaseCrudManagerImpl implements BillSmWasteManager {
    @Resource
    private BillSmWasteService billSmWasteService;
    @Resource
    private BillSmWasteDtlService billSmWasteDtlService;
    @Resource
    private BillAccControlService billAccControlService;
    @Resource
    private SizeInfoService sizeInfoService;
    @Resource
    private BillContainerTaskService billContainerTaskService;
    @Resource
    private ProcCommonService procCommonService;
    @Resource
    private BmContainerService bmContainerService;
    
    private static final String BUSINESSTYPE1 = "1";
    
    @Override
    public BaseCrudService init() {
        return billSmWasteService;
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int deleteBatch(String ids) throws ManagerException {
		int count = 0;
		if(StringUtils.isNotBlank(ids)){
			String[] idArr = ids.split(",");
			for(String str : idArr){
				String[] tmp = str.split("-");
				if(tmp.length==3){
					BillSmWaste key = new BillSmWaste();
					key.setLocno(tmp[0]);
					key.setOwnerNo(tmp[1]);
					key.setWasteNo(tmp[2]);
					key.setUpdStatus("10");
					try {
						int result = billSmWasteService.deleteById(key);
						if(result > 0 ){
							
							String locno = tmp[0];
							String ownerNo = tmp[1];
							String wasteNo = tmp[2];
							
							//添加需要解锁的箱号
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("locno", locno);
							params.put("ownerNo", ownerNo);
							params.put("wasteNo", wasteNo);
							List<BillSmWasteDtl> dtlList = billSmWasteDtlService.findByBiz(null, params);
							List<String> delBoxList = new ArrayList<String>();
							if(CommonUtil.hasValue(dtlList)){
								for (BillSmWasteDtl wasteDtl : dtlList) {
									String boxNo = wasteDtl.getBoxNo();
									if(StringUtils.isNotBlank(boxNo)){
										if(!delBoxList.contains(boxNo)){
											delBoxList.add(boxNo);
										}
									}
								}
							}
							
							//解锁箱容器数据
							List<BmContainer> delContainerList = new ArrayList<BmContainer>();
							int containerCount = 0;
							if(CommonUtil.hasValue(delBoxList)){
								for (String boxNo : delBoxList) {
									BmContainer bmContainer = new BmContainer();
									bmContainer.setLocno(locno);
									bmContainer.setConNo(boxNo);
									bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
									bmContainer.setFalg("Y");
									delContainerList.add(bmContainer);
								}
								containerCount = bmContainerService.batchUpdate(delContainerList);
								if(containerCount < 1){
									throw new ServiceException("解锁预分货单箱号容器失败!");
								}
							}
							
							//删除明细的数据
							billSmWasteService.deleteDtlById(key);
							
						}else{
							throw new ManagerException("单据"+tmp[2]+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
						}
						count++;
					} catch (ServiceException e) {
						throw new ManagerException(e);
					}
				}
			}
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String, Object> checkBillSmWaste(String ids, String auditor, String auditorname, AuthorityParams authorityParams, String paperType)
			throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			boolean flag = false;
			if(StringUtils.isNotBlank(ids)){
				String[] idArr = ids.split(",");
				for(String id : idArr){
					flag = false;
					String[] tmp = id.split("-");
					if(tmp.length==3){
						String locno = tmp[0];
						String ownerNo = tmp[1];
						String wasteNo = tmp[2];
						Map<String, Object> bill = new HashMap<String, Object>();
						bill.put("locno", locno);
						bill.put("ownerNo", ownerNo);
						bill.put("wasteNo", wasteNo);
						bill.put("status", "10");
						BillSmWaste billSmWaste = new BillSmWaste();
						billSmWaste.setLocno(locno);
						billSmWaste.setOwnerNo(ownerNo);
						billSmWaste.setWasteNo(wasteNo);
						billSmWaste.setStatus("10");
						//查询主表
						//List<BillSmWaste> param = billSmWasteService.findByWaste(billSmWaste,bill, authorityParams);
						//if(param.size() > 0) {
							BillSmWasteDtl billSmWasteDtl = new BillSmWasteDtl();
							billSmWasteDtl.setLocno(locno);
							billSmWasteDtl.setOwnerNo(ownerNo);
							billSmWasteDtl.setWasteNo(wasteNo);
							//查询明细
							List<BillSmWasteDtl> query = billSmWasteDtlService.findByWaste(billSmWasteDtl,bill, authorityParams);
							if(query.size() > 0) {
//								for(BillSmWasteDtl vo : query) {
//									String locnoDtl = locno;
//									String ownerNoDtl = ownerNo;
//									Long cellIdDtl = vo.getCellId();
//									String cellNoDtl = vo.getCellNo();
//									String itemNoDtl = vo.getItemNo();
//									String sizeNoDtl = vo.getSizeNo();
//									String qualityDtl = vo.getQuality();
//									BigDecimal wasteQty = vo.getWasteQty();
//									
//									Map<String, Object> queryParam = new HashMap<String, Object>();
//									queryParam.put("locno", locnoDtl);
//									queryParam.put("ownerNo", ownerNoDtl);
//									queryParam.put("cellId", cellIdDtl);
//									queryParam.put("cellNo", cellNoDtl);
//									queryParam.put("itemNo", itemNoDtl);
//									queryParam.put("sizeNo", sizeNoDtl);
//									queryParam.put("quality", qualityDtl);
//									//获取库存的明细信息
//									List<BillSmWasteDtl> conContent = billSmWasteDtlService.selectContentDtl(vo,queryParam);
//									//循环插入
//									for (BillSmWasteDtl dtl : conContent) {
//										if(wasteQty.equals(BigDecimal.ZERO)){ //是否等于0
//											break;
//										}
//										BigDecimal conQty = dtl.getConQty();	
//										//如果当前数量大于等于报损数量
//										if(conQty.compareTo(wasteQty)>=0) {
//											dtl.setConQty(conQty);
//											dtl.setWasteQty(wasteQty);
//											int count = billSmWasteDtlService.updateContent(dtl);
//											if(count != 1) {
//												throw new ManagerException("库存报损单扣减库存失败！");
//											}
//											wasteQty = BigDecimal.ZERO;
//										} else {
//											dtl.setConQty(conQty);
//											dtl.setWasteQty(conQty);
//											int count = billSmWasteDtlService.updateContent(dtl);
//											if(count != 1) {
//												throw new ManagerException("库存报损单扣减库存失败！");
//											}
//											wasteQty = new BigDecimal(wasteQty.subtract(conQty).doubleValue());
//										}
//									}
//								}
								
								billSmWaste.setStatus("13");
								billSmWaste.setAuditor(auditor);
								billSmWaste.setAuditorname(auditorname);
								billSmWaste.setAudittm(new Date());
								billSmWaste.setUpdStatus("10");
								int count = billSmWasteService.modifyById(billSmWaste);
								if(count != 1) {
									//throw new ManagerException("库存报损单状态更新失败！");
									throw new ManagerException("单据"+billSmWaste.getWasteNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
								} else {
									flag = true;
								}
								
								//调用统一记账方法
								//procCommonService.procAccApply(wasteNo, "2", "SW", "O", 0);
								BillAccControlDto controlDto = new BillAccControlDto();
								controlDto.setiPaperNo(wasteNo);
								controlDto.setiLocType("2");
								controlDto.setiPaperType(paperType);
								controlDto.setiIoFlag("O");
								controlDto.setiPrepareDataExt(new BigDecimal(0));
								controlDto.setiIsWeb(new BigDecimal(1));
								billAccControlService.procAccApply(controlDto);
								
								
								//开始释放板
								long rowId = 0L; 
								List<BillContainerTaskDtl> taskDtlList = new ArrayList<BillContainerTaskDtl>();
								for (BillSmWasteDtl wasteDtl : query) {
									if(StringUtils.isNotBlank(wasteDtl.getBoxNo())){
										BillContainerTaskDtl taskDtl = new BillContainerTaskDtl();
										setBillContainerTaskDtl(wasteDtl, taskDtl, auditor, auditorname);
										taskDtl.setRowId(++rowId);
										taskDtlList.add(taskDtl);
									}
								}
								
								//4.开始插入容器任务数据
								if(CommonUtil.hasValue(taskDtlList)){
									BillContainerTask containerTask = new BillContainerTask();
									containerTask.setLocno(locno);
									containerTask.setContaskNo(wasteNo);
									containerTask.setCreator(auditor);
									containerTask.setCreatorname(auditorname);
									containerTask.setCreatetm(new Date());
									containerTask.setEditor(auditor);
									containerTask.setEditorname(auditorname);
									containerTask.setEdittm(new Date());
									containerTask.setUseType(ContainerTypeEnums.E.getOptBillType());
									containerTask.setBusinessType(BUSINESSTYPE1);
									int containerCount = billContainerTaskService.insertBillContainerTask(containerTask, taskDtlList);
									if(containerCount < 1){
										throw new ManagerException(wasteNo+"插入容器任务数据失败!");
									}
									//5.开始审核容器
									procCommonService.procContaskAudit(locno, ContainerTypeEnums.E.getOptBillType(),wasteNo, auditorname);
								}
								
								//更新出库的箱号为已出库
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("locno", locno);
								params.put("ownerNo", ownerNo);
								params.put("wasteNo", wasteNo);
								billSmWasteDtlService.batchUpdateWsateBoxStatus4Container(params);
								
								//往明细表中插入修改人、修改时间 、以及对应的修改人中文名称
								Map<String, Object> _map = new HashMap<String, Object>();
								_map.putAll(params);
								_map.put("editor", auditor);
								_map.put("editorName", auditorname);
								_map.put("edittm", new Date());
								billSmWasteDtlService.updateOperateRecord(_map);
								
							} else {
								throw new ManagerException("库存报损单不存在明细,不允许审核！");
							}
						//} else {
						//	throw new ManagerException("当前单据状态不可编辑！");
						//}
					}
				}
			}
			if(flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "审核成功");
			} else {
				throw new ManagerException("当前单据审核失败！");
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	public void setBillContainerTaskDtl(BillSmWasteDtl bd,BillContainerTaskDtl taskDtl,String auditor,String auditorname){
		taskDtl.setLocno(bd.getLocno());
		taskDtl.setContaskNo(bd.getWasteNo());
		taskDtl.setsSubContainerNo(bd.getBoxNo());
		taskDtl.setsCellNo(bd.getCellNo());
		taskDtl.setBrandNo(bd.getBrandNo());
		taskDtl.setItemNo(bd.getItemNo());
		taskDtl.setSizeNo(bd.getSizeNo());
		taskDtl.setQty(Long.valueOf(String.valueOf(bd.getWasteQty())));
		taskDtl.setCreator(auditor);
		taskDtl.setCreatorname(auditorname);
		taskDtl.setEditor(auditor);
		taskDtl.setEditorname(auditorname);
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
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billSmWasteService.findSumQty(params, authorityParams);
	}

	@Override
	public Map<String, Object> print(String nos, String locno, SystemUser user) {
		Map<String, Object> obj = new HashMap<String, Object>();
		if(!CommonUtil.hasValue(nos) || !CommonUtil.hasValue(nos)){
			obj.put("status", "error");
			obj.put("msg", "缺少参数!");
			return obj;
		}
		List<Object> rows = new ArrayList<Object>();
		String [] noArr = nos.split(",");
		Map<String, List<String>> allSizeKind = new TreeMap<String, List<String>>();//缓存所有单据明细中所需要的尺码表 key=sizeKind+sysNo,value=sizeCodeList
		for(String wasteNo:noArr){
			
			Map<String, Object> page = joinDataByItem(wasteNo, locno, allSizeKind);
			if(CommonUtil.hasValue(page)){				
				rows.add(page);
			}
		}
		if(CommonUtil.hasValue(rows)){				
			obj.put("status", "success");
			obj.put("rows", rows);
		}else{			
			obj.put("status", "error");
			obj.put("msg", "没有需要打印的数据!");
		}
		
		return obj;
	}
	private Map<String, Object> joinDataByItem(String wasteNo, String locno,Map<String, List<String>> allSizeKind){
		Map<String, Object> queryDtlParams = new HashMap<String, Object>();//
		queryDtlParams.put("locno", locno);
		queryDtlParams.put("wasteNo", wasteNo);
		List<BillSmWastePrintDto> list = billSmWasteDtlService.findPrintDtl4Size(queryDtlParams);
		Map<String, Integer> sizeQtyMap;
		Map<String, BillSmWastePrintDto> row = new HashMap<String, BillSmWastePrintDto>();
		Map<String, Object> sizeCodeQueryParams = new HashMap<String, Object>();
		if(CommonUtil.hasValue(list)){
			Map<String, Object> sizeMap = new TreeMap<String, Object>();//每个单据对应的size列表,key=sizeKind value=sizeCodeList
			int sizeMaxLength = 0;//所有尺码数组中最大的数组长度
			int total = 0;//每一张单据的总数量
			String creatorName = null;
			String auditorName = null;
			for(BillSmWastePrintDto dto:list){
				String cellNo = dto.getCellNo();
				String itemNo = dto.getItemNo();
				String key = cellNo+"_"+itemNo;
				BillSmWastePrintDto first;
				total = total + dto.getWasteQty().intValue();
				if((first=row.get(key)) != null){
					first.getSizeQtyMap().put(dto.getSizeNo().substring(2), dto.getWasteQty().intValue());
					first.setTotalQty(first.getTotalQty()+dto.getWasteQty().intValue());
				}else{
					sizeQtyMap = new TreeMap<String, Integer>();
					sizeQtyMap.put(dto.getSizeNo().substring(2), dto.getWasteQty().intValue());
					dto.setTotalQty(dto.getWasteQty().intValue());
					dto.setSizeQtyMap(sizeQtyMap);
					dto.setSizeNo(null);
					dto.setWasteQty(null);
					creatorName = dto.getCreatorName();
					auditorName = dto.getAuditorName();
					dto.setAuditorName(null);
					dto.setCreatorName(null);
					
					row.put(key, dto);
				}
				//获取size数组
				String sysNo = dto.getSysNo();
				String sizeKind = dto.getSizeKind();
				
				if(sizeMap.get(sizeKind) == null){
					List<String> sizeArr = null;
					if((sizeArr = allSizeKind.get(sizeKind+"_"+sysNo)) == null){
						sizeCodeQueryParams.put("sizeKind", sizeKind);
						sizeCodeQueryParams.put("sysNo", sysNo);
						sizeArr = sizeInfoService.findSizeCodeBySysAndKind(sizeCodeQueryParams);
						allSizeKind.put(sizeKind+"_"+sysNo,sizeArr);
					}
					if(sizeArr.size() > sizeMaxLength){
						sizeMaxLength = sizeArr.size();
					}
					sizeMap.put(sizeKind, sizeArr);
				}
			}
			/*test S*/
				//sizeMap.put("E", sizeMap.get("A"));
			/*test E*/
			Map<String, Object> page = new HashMap<String, Object>();
			page.put("list", row.values());
			page.put("wasteNo", wasteNo);
			page.put("creatorName", creatorName);
			page.put("auditorName", auditorName);
			page.put("total", total);
			page.put("sizeList", sizeMap);
			page.put("sizeMaxLength", sizeMaxLength);
			return page;
		}
		return null;
	}
}