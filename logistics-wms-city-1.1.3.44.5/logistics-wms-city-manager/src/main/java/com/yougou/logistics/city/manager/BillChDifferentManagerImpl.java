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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.enums.BillChCheckStautsEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDirect;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChDifferent;
import com.yougou.logistics.city.common.model.BillChDifferentDtl;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChRecheckDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillChCheckDirectService;
import com.yougou.logistics.city.service.BillChCheckDtlService;
import com.yougou.logistics.city.service.BillChCheckService;
import com.yougou.logistics.city.service.BillChDifferentDtlService;
import com.yougou.logistics.city.service.BillChDifferentService;
import com.yougou.logistics.city.service.BillChPlanService;
import com.yougou.logistics.city.service.BillChRecheckDtlService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ConContentService;
import com.yougou.logistics.city.service.ProcCommonService;

@Service("billChDifferentManager")
class BillChDifferentManagerImpl extends BaseCrudManagerImpl implements
		BillChDifferentManager {
	@Resource
	private BillChDifferentService billChDifferentService;
	@Resource
	private BillChDifferentDtlService billChDifferentDtlService;
	@Resource
	private BillChCheckService billChCheckService;
	@Resource
	private BillChCheckDtlService billChCheckDtlService;
	@Resource
	private BillChRecheckDtlService billChRecheckDtlService;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private ConContentService conContentService;
	@Resource
	private BillChPlanService billChPlanService;
	@Resource
	private BillAccControlService accControlService;
	@Resource
	private BillChCheckDirectService billChCheckDirectService;
	@Resource
	private CmDefcellService cmDefcellService;

	@Override
	public BaseCrudService init() {
		return billChDifferentService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String addDiff(BillChDifferent billChDifferent,SystemUser user)
			throws ManagerException {
		int pageSize = 1000;
		String planNo = billChDifferent.getPlanNo();
		String locno = billChDifferent.getLocno();
		String ownerNo = billChDifferent.getOwnerNo();
		String creator = billChDifferent.getCreator();
		String creatorName = billChDifferent.getCreatorName();

		String checkNo;
		List<BillChCheck> list;
		List<BillChCheckDtl> chCheckDtls;
		List<BillChRecheckDtl> chRecheckDtls;
		List<ConContent> ccList;// 差异明细对应的库存
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> ccMap;// 查询库存传入参数
		BigDecimal itemQty;// 计划数量
		BigDecimal checkQty;// 初盘数量
		BigDecimal recheckQty;// 复盘数量
		BigDecimal realQty;// 差异明细实际数量
		BigDecimal diffQty;
		boolean existDiff = false;// 当前盘点明细是否存在差异
		boolean createDiffMain = false;// 是否已经生成了差异主信息
		String differentNo = null;
		int a = 0;
		try {
			params.put("planNo", planNo);
			params.put("locno", locno);
			params.put("ownerNo", ownerNo);
			list = billChCheckService.findByBiz(null, params);
			if (list == null || list.size() == 0) {
				throw new ManagerException("计划单[" + planNo + "]没有找到盘点单");
			}
			boolean isI = false;
			boolean isO = false;
			List<BillChDifferentDtl> diffDtkList = new ArrayList<BillChDifferentDtl>();
			for (BillChCheck check : list) {
				checkNo = check.getCheckNo();
				if (!"25".equals(check.getStatus())) {
					throw new ManagerException("计划单[" + planNo
							+ "]存在状态不为[初盘/复盘回单]的盘点单");
				} else {
					params.put("checkNo", checkNo);
					chCheckDtls = billChCheckDtlService.findByBiz(null, params);
					if (chCheckDtls == null || chCheckDtls.size() == 0) {
						throw new ManagerException("计划单[" + planNo
								+ "]没有找到盘点明细");
					}
					chRecheckDtls = billChRecheckDtlService.findByBiz(null, params);
					if(chRecheckDtls != null && chRecheckDtls.size() > 0){
						for(BillChRecheckDtl chRecheckDtl : chRecheckDtls){
							if(!"13".equals(chRecheckDtl.getStatus())){
								throw new ManagerException("计划单[" + planNo
										+ "]存在没有完成的复盘明细");
							}
						}
					}
					for (BillChCheckDtl chCheckDtl : chCheckDtls) {// 循环检验差异
						if(!"13".equals(chCheckDtl.getStatus())){
							throw new ManagerException("计划单[" + planNo
									+ "]存在没有完成的盘点明细");
						}
						existDiff = false;
						itemQty = chCheckDtl.getItemQty();
						if (itemQty == null || itemQty.intValue() == 0) {
							itemQty = new BigDecimal(0);
							// throw new
							// ManagerException("计划单["+planNo+"]存在计划数量为0的盘点明细");
						}

						String itemCode = chCheckDtl.getItemNo();
						String cellNo = chCheckDtl.getCellNo();
						String itemSize = chCheckDtl.getSizeNo();

						checkQty = chCheckDtl.getCheckQty();
						recheckQty = chCheckDtl.getRecheckQty();
						realQty = new BigDecimal(0);
						System.out.println(cellNo + "," + itemCode + ","
								+ itemSize + "," + checkQty + "," + recheckQty
								+ "," + itemQty);
						if ("1".equals(chCheckDtl.getCheckType())) {// 初盘
							if (checkQty == null
									|| checkQty.intValue() != itemQty
											.intValue()) {// 初盘存在差异
								existDiff = true;
								realQty = checkQty == null ? new BigDecimal(0)
										: checkQty;
								if (checkQty.compareTo(itemQty) > 0) {
									isI = true;
								} else if (checkQty.compareTo(itemQty) < 0) {
									isO = true;
								}
							}
						} else {// 复盘
							if (recheckQty == null
									|| recheckQty.intValue() != itemQty
											.intValue()) {// 复盘存在差异
								existDiff = true;
								realQty = recheckQty == null ? new BigDecimal(0)
										: recheckQty;
								if (recheckQty.compareTo(itemQty) > 0) {
									isI = true;
								} else if (recheckQty.compareTo(itemQty) < 0) {
									isO = true;
								}
							}
						}
						if (existDiff) {// 存在差异
							if (!createDiffMain) {// 生成差异主信息
								differentNo = procCommonService.procGetSheetNo(
										locno, CNumPre.CH_DIFF_PRE);
								BillChDifferent different = new BillChDifferent();
								different.setLocno(locno);
								different.setDifferentNo(differentNo);
								different.setOwnerNo(ownerNo);
								different.setPlanNo(planNo);
								different.setCheckNo(checkNo);
								different.setStatus("10");
								different.setCreator(creator);
								different.setCreatorName(creatorName);
								different.setCreatetm(new Date());
								different.setAuditor(creator);
								different.setAuditorName(creatorName);
								different.setAudittm(different.getCreatetm());
								different.setEditor(creator);
								different.setEditorName(creatorName);
								different.setEdittm(different.getCreatetm());
								a = billChDifferentService.add(different);
								if (a < 1) {
									throw new ManagerException("计划单[" + planNo
											+ "]生成差异主信息异常");
								}
								createDiffMain = true;
							}
							/**
							 * 生成差异明细
							 */
							BillChDifferentDtl dtlDto = new BillChDifferentDtl();
							dtlDto.setLocno(locno);
							dtlDto.setOwnerNo(ownerNo);
							dtlDto.setDifferentNo(differentNo);
							dtlDto.setCellNo(chCheckDtl.getCellNo());
							dtlDto.setItemNo(chCheckDtl.getItemNo());
							dtlDto.setBarcode(chCheckDtl.getBarcode());
							dtlDto.setPackQty(chCheckDtl.getPackQty() == null ? new BigDecimal(
									1) : chCheckDtl.getPackQty());
							dtlDto.setLotNo("N");
							dtlDto.setQuality(chCheckDtl.getQuality());
							dtlDto.setProduceDate(new Date());
							dtlDto.setExpireDate(new Date());
							dtlDto.setItemType(chCheckDtl.getItemType());
							dtlDto.setBatchSerialNo(chCheckDtl
									.getBatchSerialNo());
							dtlDto.setStockValue(chCheckDtl.getStockValue());
							dtlDto.setLabelNo(chCheckDtl.getLabelNo());

							dtlDto.setItemQty(itemQty);
							dtlDto.setRealQty(realQty);
							dtlDto.setSizeNo(chCheckDtl.getSizeNo());
							dtlDto.setBrandNo(chCheckDtl.getBrandNo());

							diffDtkList.add(dtlDto);
							/*
							 * a = billChDifferentDtlService.add(dtlDto); if(a <
							 * 1){ throw new
							 * ManagerException("计划单["+planNo+"]生成差异明细异常"); }
							 */
							/**
							 * 更细或删除储位库存信息
							 */
							// procCommonService.procAccApply(planNo, "2", "CP",
							// "I", 0);

						}
					}
				}
				/**
				 * 更新盘点单状态为结案
				 */
				check.setSourceStatus(BillChCheckStautsEnums.STATUS25
						.getStatus());
				check.setStatus(BillChCheckStautsEnums.STATUS90.getStatus());
				a = billChCheckService.modifyById(check);
				if (a < 1) {
					throw new ManagerException("计划单[" + planNo + "]更新盘点单状态异常");
				}
			}
			// 批量添加差异明细
			for (int i = 0; i < diffDtkList.size();) {
				i += pageSize;
				if (i > diffDtkList.size()) {
					billChDifferentDtlService.batchInsertDtl(diffDtkList
							.subList(i - pageSize, diffDtkList.size()));
				} else {
					billChDifferentDtlService.batchInsertDtl(diffDtkList
							.subList(i - pageSize, i));
				}
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
				//procCommonService.UpdateContentStatus(locno, null, cellNo, "0", null, null, user.getLoginName());
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
			
//			List<BillChCheckDirectDto> directDtoList=billChCheckDirectService.findDirectAndContent(directParams);
//			//并更新库存盘点状态更新为可用
//			for(BillChCheckDirectDto dto:directDtoList){
//				procCommonService.UpdateContentStatus(locno, dto.getContentId(), null, "0", null, null, editor);
//			}
			
			
			
			// 记账存储过程
			BillAccControlDto controlDto = null;
			// if(isI) {
			// controlDto = new BillAccControlDto();
			// controlDto.setiPaperNo(planNo);
			// controlDto.setiLocType("2");
			// controlDto.setiPaperType("CP");
			// controlDto.setiIoFlag("I");
			// controlDto.setiPrepareDataExt(new BigDecimal(0));
			// controlDto.setiIsWeb(new BigDecimal(1));
			// try {
			// accControlService.procAccApply(controlDto);
			// } catch (Exception e) {
			// System.out.println(e.getMessage());
			// throw new ManagerException("计划"+e.getMessage().trim());
			// }
			// }
			// if(isO) {
			controlDto = new BillAccControlDto();
			controlDto.setiPaperNo(planNo);
			controlDto.setiLocType("2");
			controlDto.setiPaperType("CP");
			controlDto.setiIoFlag("O");
			controlDto.setiPrepareDataExt(new BigDecimal(0));
			controlDto.setiIsWeb(new BigDecimal(1));
			try {
				accControlService.procAccApply(controlDto);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new ManagerException("计划" + e.getMessage().trim());
			}
			// }
						
			
			/**
			 * 更新盘点计划单为结案
			 */
			BillChPlan billChPlan = new BillChPlan();
			billChPlan.setLocno(locno);
			billChPlan.setPlanNo(planNo);
			billChPlan.setSourceStatus(BillChPlanStatusEnums.INITANDRECHECK
					.getValue());
			billChPlan.setStatus(BillChPlanStatusEnums.CLOSE.getValue());
			billChPlan.setEndDate(new Date());
			a = billChPlanService.modifyById(billChPlan);
			if (a < 1) {
				throw new ManagerException("计划单[" + planNo + "]更新状态异常");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}

		return null;
	}

}