package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.yougou.logistics.city.common.enums.BillImCheckSourceTypeEnums;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillImCheck;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BillImCheckDtlKey;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.service.BillImCheckDtlService;
import com.yougou.logistics.city.service.BillImCheckService;
import com.yougou.logistics.city.service.BillImReceiptDtlService;
import com.yougou.logistics.city.service.BillImReceiptService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ProcCommonService;

/**
 * 
 * 收货验收单manager实现
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:14:39
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billImCheckManager")
class BillImCheckManagerImpl extends BaseCrudManagerImpl implements BillImCheckManager {
	@Resource
	private BillImCheckService billImCheckService;

	@Resource
	private BillImCheckDtlService billImCheckDtlService;


	@Resource
	private BillImReceiptDtlService billImReceiptDtlService;
	
	@Resource
	private BillImReceiptService billImReceiptService;
	
	@Resource
    private BmContainerService bmContainerService;
    
	@Resource
	private ProcCommonService procCommonService;
	
	private final static String STATUS10 ="10";

	@Override
	public BaseCrudService init() {
		return billImCheckService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void add(BillImCheck billImCheck, List<Item> itemLst) throws ManagerException {
		try {
			Integer pi = 1;
			for (Item item : itemLst) {
				BillImCheckDtl dtl = new BillImCheckDtl();
				dtl.setItemNo(item.getItemNo());
				dtl.setCheckNo(billImCheck.getCheckNo());
				dtl.setLocno(billImCheck.getLocno());
				dtl.setOwnerNo(billImCheck.getOwnerNo());
				dtl.setRowId(pi++);
				dtl.setSizeNo(item.getSizeNo());
				dtl.setBoxNo(item.getBoxNo());
				dtl.setCheckQty(item.getCheckQty());
				dtl.setPoQty(item.getPoQty());
				dtl.setStatus(BigDecimal.valueOf(11));
				dtl.setSysNo("TM");//TODO:
				dtl.setBarcode("BARCODE");
				billImCheckDtlService.add(dtl);
			}
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> update(BillImCheck billImCheck, List<BillImCheckDtl> insertItemLst,
			List<BillImCheckDtl> updatedItemLst, List<BillImCheckDtl> deletedItemLst) throws ManagerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//		List<String> repeatList = new ArrayList<String>();
		try {
			
			Map<String, Object> paramsValidate = new HashMap<String, Object>();
			paramsValidate.put("locno", billImCheck.getLocno());
			paramsValidate.put("ownerNo", billImCheck.getOwnerNo());
			paramsValidate.put("checkNo", billImCheck.getCheckNo());
			//更新前的数据
			List<BillImCheckDtl> validateList1 = billImCheckDtlService.selectGroupByBox(paramsValidate);
			billImCheck.setUptStatus(STATUS10);
			int k = billImCheckService.modifyById(billImCheck);
			if( k < 1){
				throw new ManagerException("单据"+billImCheck.getCheckNo()+"已删除或状态已改变，不能进行保存明细操作！");
			}

			for (BillImCheckDtl dtl : updatedItemLst) {
				
				//验证箱号是否存在于验收单中
				BillImCheckDtl checkDtl = new BillImCheckDtl();
				checkDtl.setLocno(billImCheck.getLocno());
				checkDtl.setOwnerNo(billImCheck.getOwnerNo());
				checkDtl.setCheckNo(billImCheck.getCheckNo());
				checkDtl.setBoxNo(dtl.getBoxNo());
				int panCount = billImCheckDtlService.selectCheckDtlPanNum(checkDtl);
				if(panCount < 1){
					StringBuffer sb = new StringBuffer();
					sb.append("箱号：" + dtl.getBoxNo());
					sb.append("不存在验收单中,请检查是否被删除");
					throw new ManagerException(sb.toString());
				}
				
				//如果是串码的商品需要把箱号付给原箱号，并且把boxNo设置为N
				//如果是串款串码的验证是否修改成其他板号的箱号
				if(dtl.getPoQty().intValue()==0||dtl.getPoQty()==null){
					BillImReceiptDtl billImReceiptDtl = new BillImReceiptDtl();
					billImReceiptDtl.setLocno(billImCheck.getLocno());
					billImReceiptDtl.setOwnerNo(billImCheck.getOwnerNo());
					billImReceiptDtl.setReceiptNo(billImCheck.getsImportNo());
					billImReceiptDtl.setBoxNo(dtl.getBoxNo());
					BillImReceiptDtl receiptDtl = billImReceiptDtlService.selectReceiptDtlPanByBox(billImReceiptDtl);
					if(receiptDtl != null){
						if(StringUtils.isEmpty(receiptDtl.getPanNo())){
							dtl.setIsUpdatePan("Y");//修改箱号有空的情况需要清除板号
							dtl.setPanNo("");
						}else{
							dtl.setPanNo(receiptDtl.getPanNo());
						}
					}
				}
				dtl.setCheckWorker1(billImCheck.getCheckWorker());
				dtl.setCheckName1(billImCheck.getCheckName());
				dtl.setEditor(billImCheck.getEditor());
				dtl.setEditorName(billImCheck.getEditorName());
				dtl.setEdittm(new Date());
				billImCheckDtlService.modifyById(dtl);
			}
			for (BillImCheckDtl dtl : deletedItemLst) {
				billImCheckDtlService.deleteById(dtl);
			}
			//判断重复的商品
			//1、查询该验收单下所有明细
			//			Map<String, Object> parmas = new HashMap<String, Object>();
			//			//{checkNo=006IC13112000008, locno=006, rows=20, ownerNo=BL, page=1}
			//			parmas.put("checkNo", billImCheck.getCheckNo());
			//			parmas.put("locno", billImCheck.getLocno());
			//			parmas.put("ownerNo", billImCheck.getOwnerNo());
			//			int total = this.billImCheckDtlService.findCount(parmas);
			//			SimplePage page = new SimplePage(1, total, total);
			//			List<BillImCheckDtl> list = this.billImCheckDtlService.findByPage(page, null, null, parmas);
			//
			//			for (BillImCheckDtl item : insertItemLst) {
			//				for (BillImCheckDtl dtl : list) {
			//					if (item.getBoxNo().equals(dtl.getBoxNo())&&item.getItemNo().equals(dtl.getItemNo()) && item.getSizeNo().equals(dtl.getSizeNo())) {
			//						repeatList.add(item.getItemNo() + "/" + item.getSizeNo() + "/" + item.getBoxNo());
			//						break;
			//					}
			//				}
			//			}

			//			if (repeatList.size() > 0) {
			//				new ServiceException();
			//				resultMap.put("repeat", repeatList);
			//				return resultMap;
			//			}

			BillImCheckDtlKey billImCheckDtlKey = new BillImCheckDtlKey();
			billImCheckDtlKey.setCheckNo(billImCheck.getCheckNo());
			billImCheckDtlKey.setLocno(billImCheck.getLocno());
			billImCheckDtlKey.setOwnerNo(billImCheck.getOwnerNo());
			Integer maxRowId = billImCheckDtlService.selectMaxRowId(billImCheckDtlKey);
			maxRowId = maxRowId == null ? 0 : maxRowId;
			for (BillImCheckDtl item : insertItemLst) {
				BillImCheckDtl dtl = new BillImCheckDtl();
				dtl.setItemNo(item.getItemNo());
				dtl.setCheckNo(billImCheck.getCheckNo());
				dtl.setLocno(billImCheck.getLocno());
				dtl.setOwnerNo(billImCheck.getOwnerNo());
				dtl.setRowId(++maxRowId);
				dtl.setSizeNo(item.getSizeNo());

				//如果是串码的商品需要把箱号付给原箱号，并且把boxNo设置为N
				dtl.setBoxNo(StringUtils.isEmpty(item.getBoxNo()) ? "N" : item.getBoxNo());
				dtl.setCheckQty(item.getCheckQty());
				dtl.setPoQty(item.getPoQty());
				dtl.setStatus(BigDecimal.valueOf(11));
				dtl.setBrandNo(item.getBrandNo());

				//判断是否是串码的商品,取任意箱子的商品属性和品质
				if (item.getSourceType() == 0) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", billImCheck.getLocno());
					params.put("receiptNo", billImCheck.getsImportNo());
					params.put("boxNo", item.getBoxNo());
					List<BillImReceiptDtl> listReDtls = billImReceiptDtlService.findByBiz(null, params);
					if (CommonUtil.hasValue(listReDtls)) {
						BillImReceiptDtl receiptDtl = listReDtls.get(0);
						dtl.setItemType(receiptDtl.getItemType());
						dtl.setQuality(receiptDtl.getQuality());
						dtl.setPanNo(receiptDtl.getPanNo());
					}
				}

				if (StringUtils.isEmpty(item.getBoxNo())) {//串码的箱数量，取同商品的的packQty.
					BillImReceiptDtl recdtl = new BillImReceiptDtl();
					recdtl.setLocno(billImCheck.getLocno());
					recdtl.setReceiptNo(billImCheck.getsImportNo());
					recdtl.setItemNo(item.getItemNo());
					BillImReceiptDtl result = billImReceiptDtlService.selectDtlByItemNo(recdtl);
					if (result != null) {
						dtl.setPackQty(result.getPackQty());
					}
				} else {
					dtl.setPackQty(item.getPackQty());
				}
				dtl.setPackQty(dtl.getPackQty() == null ? new BigDecimal(1) : dtl.getPackQty());
				//以下三个参数需要确认，需要在页面端查询的时候查询处理，保存的时候在传到这里来。
				dtl.setBarcode(item.getBarcode());
				dtl.setSysNo(item.getSysNo());
				//dtl.setPackQty(item.getPackQty() == null ? BigDecimal.valueOf(0) : item.getPackQty());
				dtl.setCheckWorker1(billImCheck.getCheckWorker());
				dtl.setCheckName1(billImCheck.getCheckName());
				dtl.setEditor(billImCheck.getEditor());
				dtl.setEditorName(billImCheck.getEditorName());
				dtl.setEdittm(billImCheck.getEdittm());
				billImCheckDtlService.add(dtl);
			}

			//校验重复性
			BillImCheckDtl entity = new BillImCheckDtl();
			entity.setLocno(billImCheck.getLocno());
			entity.setOwnerNo(billImCheck.getOwnerNo());
			entity.setCheckNo(billImCheck.getCheckNo());
			List<BillImCheckDtl> listDtls = billImCheckDtlService.findCheckDtlGroupByCount(entity);
			if (CommonUtil.hasValue(listDtls)) {
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + listDtls.get(0).getBoxNo() + ",");
				sb.append("商品：" + listDtls.get(0).getItemNo() + ",");
				sb.append("尺码：" + listDtls.get(0).getSizeNo() + "重复!");
				throw new ManagerException(sb.toString());
			}
			
			//验证更新和删除前后对比数据释放容器(前台限制了只能选验收单的箱串款串码,RF可以选收货单的任意箱号)
			List<BillImCheckDtl> delContainerList = new ArrayList<BillImCheckDtl>();
			List<BillImCheckDtl> validateList2 = billImCheckDtlService.selectGroupByBox(paramsValidate);
			if(CommonUtil.hasValue(validateList1)){
				for (BillImCheckDtl v1 : validateList1) {
					boolean result = true;
					for (BillImCheckDtl v2 : validateList2) {
						if(v1.getBoxNo().equals(v2.getBoxNo())){
							result = false;
							break;
						}
					}
					//如果串款串码的箱号不存在明细中,表示被删掉了
					if(result){
						delContainerList.add(v1);
					}
				}
			}
			
			//是否有需要释放的容器
			if(CommonUtil.hasValue(delContainerList)){
				//1.先释放箱号
				List<BmContainer> delBoxContainerList = new ArrayList<BmContainer>();
				List<String> delPanContainerList = new ArrayList<String>();
				for (BillImCheckDtl checkDtl : delContainerList) {
					BmContainer bmContainer = new BmContainer();
					bmContainer.setLocno(billImCheck.getLocno());
					bmContainer.setConNo(checkDtl.getBoxNo());
					bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
					bmContainer.setFalg("Y");
					delBoxContainerList.add(bmContainer);
					//添加板号
					if(StringUtils.isNotBlank(checkDtl.getPanNo())){
						if(!delPanContainerList.contains(checkDtl.getPanNo())){
							delPanContainerList.add(checkDtl.getPanNo());
						}
					}
				}
				
				//2.释放板号
				for (String panNo : delPanContainerList) {
					BillImCheckDtl checkDtl = new BillImCheckDtl();
					checkDtl.setLocno(billImCheck.getLocno());
					checkDtl.setOwnerNo(billImCheck.getOwnerNo());
					checkDtl.setCheckNo(billImCheck.getCheckNo());
					checkDtl.setPanNo(panNo);
					int panCount = billImCheckDtlService.selectCheckDtlPanNum(checkDtl);
					if(panCount < 1){
						BmContainer bmContainer = new BmContainer();
						bmContainer.setLocno(billImCheck.getLocno());
						bmContainer.setConNo(panNo);
						bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
						bmContainer.setFalg("Y");
						delBoxContainerList.add(bmContainer);
					}
				}
				
				//3.开始解锁容器
				int containerCount = bmContainerService.batchUpdate(delBoxContainerList);
				if(containerCount < 1){
					throw new ServiceException("解锁预分货单箱号容器失败!");
				}
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
		return resultMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBatch(String ids) throws ManagerException {
		if (StringUtils.isNotBlank(ids)) {
			String[] idArr = ids.split(",");
			for (String id : idArr) {
				String[] tmp = id.split("-");
				if (tmp.length == 3) {
					BillImCheck key = new BillImCheck();
					BillImCheckDtl dtlKey = new BillImCheckDtl();
					key.setLocno(tmp[0]);
					key.setOwnerNo(tmp[1]);
					key.setCheckNo(tmp[2]);
					dtlKey.setLocno(tmp[0]);
					dtlKey.setOwnerNo(tmp[1]);
					dtlKey.setCheckNo(tmp[2]);
					try {
						
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("locno", tmp[0]);
						params.put("ownerNo", tmp[1]);
						params.put("checkNo", tmp[2]);
						List<BillImCheckDtl> checkDtlList = billImCheckDtlService.findByBiz(null, params);
						if(CommonUtil.hasValue(checkDtlList)){
							List<String> delPanList = new ArrayList<String>();
							List<BmContainer> delPanContainerList = new ArrayList<BmContainer>();
							for (BillImCheckDtl checkDtl : checkDtlList) {
								//如果板号为空,锁箱号,否则把整板都锁掉
								String panNo = checkDtl.getPanNo();
								String boxNo = checkDtl.getBoxNo();
								if(!delPanList.contains(panNo)&&StringUtils.isNotBlank(panNo)){
									delPanList.add(panNo);
								}
								if(!delPanList.contains(boxNo)){
									delPanList.add(boxNo);
								}
							}
							
							//解锁板号容器
							int containerCount = 0;
							if(CommonUtil.hasValue(delPanList)){
								for (String panNo : delPanList) {
									BmContainer bmContainer = new BmContainer();
									bmContainer.setLocno(key.getLocno());
									bmContainer.setConNo(panNo);
									bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
									bmContainer.setFalg("Y");
									delPanContainerList.add(bmContainer);
								}
								if(CommonUtil.hasValue(delPanContainerList)){
									containerCount = bmContainerService.batchUpdate(delPanContainerList);
									if(containerCount < 1){
										throw new ServiceException("解锁验收单板号容器失败!");
									}
								}
							}
						}
						
						key.setUptStatus(STATUS10);
						int k = billImCheckService.deleteById(key);
						if(k < 1){
							throw new ManagerException("单据"+key.getCheckNo()+"已删除或状态已改变，不能进行删除操作！");
						}
						billImCheckDtlService.deleteById(dtlKey);
					} catch (Exception e) {
						throw new ManagerException(e.getMessage());
					}
				}
			}
		}
		return 1;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> editMainInfo(BillImCheck billImCheck,String currUser) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			
			BillImReceipt billImReceipt = new BillImReceipt();
			billImReceipt.setReceiptNo(billImCheck.getsImportNo());
			billImReceipt.setLocno(billImCheck.getLocno());
			billImReceipt.setOwnerNo(billImCheck.getOwnerNo());
			BillImReceipt bir = billImReceiptService.findById(billImReceipt);
			if(bir != null) {
				String receiptNo = bir.getReceiptNo();
				if(receiptNo != null) {
					Date checkEndDate = billImCheck.getCheckEndDate();
					Date recivedate = bir.getAudittm();
					int i = DateUtil.isDate(recivedate,checkEndDate);
					if(i == -1 || i == 0) {
						billImCheck.setEdittm(new Date());
						billImCheck.setEditor(currUser);
						billImCheck.setUptStatus(STATUS10);
						int k = billImCheckService.modifyById(billImCheck);
						if(k < 1){
							throw new ManagerException("单据"+billImCheck.getCheckNo()+"已删除或状态已改变，不能进行修改操作！");
						}
						BillImCheckDtl checkDtl = new BillImCheckDtl();
					    checkDtl.setLocno(billImCheck.getLocno());
					    checkDtl.setOwnerNo(billImCheck.getOwnerNo());
					    checkDtl.setCheckNo(billImCheck.getCheckNo());
					    checkDtl.setEditor(billImCheck.getEditor());
					    checkDtl.setEditorName(billImCheck.getEditorName());
					    checkDtl.setEdittm(new Date());
					    checkDtl.setCheckWorker1(billImCheck.getCheckWorker());
					    checkDtl.setCheckName1(billImCheck.getCheckName());
					    k = billImCheckDtlService.modifyById(checkDtl);
					    if(k < 1){
							throw new ManagerException("单据"+billImCheck.getCheckNo()+"更新明细失败！");
						}
						obj.put("result", "true");
						obj.put("msg", "ok");
					} else if(i == 1) {
						obj.put("result", "false");
						obj.put("msg", "验收日期不可小于收货单审核日期");
					} else {
						obj.put("result", "false");
						obj.put("msg", "验收日期判断出错");
					}
				} else {
					obj.put("result", "false");
					obj.put("msg", "验收日期判断出错");
				}
			} else {
				obj.put("result", "false");
				obj.put("msg", "收货单检查出错");
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
		return obj;
	}

	@Override
	public void check(List<BillImCheck> checkList, String userId,String username) throws ManagerException {
		try {
			billImCheckService.check(checkList, userId,username);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public int findCheckForDirectCount(Map map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImCheckService.findCheckForDirectCount(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImCheck> selectByPageForDirect(SimplePage page, Map map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billImCheckService.selectByPageForDirect(page, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void addMain(BillImCheck billImCheck, List<BillImCheckDtl> insertItemLst,
			List<BillImCheckDtl> updatedItemLst, List<BillImCheckDtl> deletedItemLst) throws ManagerException {
		try {
			this.add(billImCheck);
			billImCheckService.modifyById(billImCheck);

			for (BillImCheckDtl dtl : updatedItemLst) {
				billImCheckDtlService.modifyById(dtl);
			}
			for (BillImCheckDtl dtl : deletedItemLst) {
				billImCheckDtlService.deleteById(dtl);
			}
			//			//判断重复的商品
			//			//1、查询该验收单下所有明细
			//			Map<String, Object> parmas = new HashMap<String, Object>();
			//			//{checkNo=006IC13112000008, locno=006, rows=20, ownerNo=BL, page=1}
			//			parmas.put("checkNo", billImCheck.getCheckNo());
			//			parmas.put("locno", billImCheck.getLocno());
			//			parmas.put("ownerNo", billImCheck.getOwnerNo());
			//			int total = this.billImCheckDtlService.findCount(parmas);
			//			SimplePage page = new SimplePage(1, total, total);
			//			List<BillImCheckDtl> list = this.billImCheckDtlService.findByPage(page, null, null, parmas);
			//			StringBuilder sb = new StringBuilder();
			//			for (BillImCheckDtl item : insertItemLst) {
			//				for (BillImCheckDtl dtl : list) {
			//					if (item.getBoxNo() == null) {
			//						if (item.getItemNo().equals(dtl.getItemNo()) && item.getSizeNo().equals(dtl.getSizeNo())) {
			//							sb.append("商品编码：" + item.getItemNo() + ",尺码：" + item.getSizeNo() + ",箱号：" + item.getBoxNo()
			//									+ "<br>");
			//							break;
			//						}
			//					}
			//				}
			//			}
			//
			//			if (sb.length() > 0) {
			//				sb.append("重复!");
			//				new ServiceException(sb.toString());
			//			}
			BillImCheckDtlKey billImCheckDtlKey = new BillImCheckDtlKey();
			billImCheckDtlKey.setCheckNo(billImCheck.getCheckNo());
			billImCheckDtlKey.setLocno(billImCheck.getLocno());
			billImCheckDtlKey.setOwnerNo(billImCheck.getOwnerNo());
			Integer maxRowId = billImCheckDtlService.selectMaxRowId(billImCheckDtlKey);
			maxRowId = maxRowId == null ? 0 : maxRowId;
			for (BillImCheckDtl item : insertItemLst) {
				BillImCheckDtl dtl = new BillImCheckDtl();
				dtl.setItemNo(item.getItemNo());
				dtl.setCheckNo(billImCheck.getCheckNo());
				dtl.setLocno(billImCheck.getLocno());
				dtl.setOwnerNo(billImCheck.getOwnerNo());
				dtl.setRowId(++maxRowId);
				dtl.setSizeNo(item.getSizeNo());
				dtl.setBoxNo(StringUtils.isEmpty(item.getBoxNo()) ? "N" : item.getBoxNo());
				dtl.setCheckQty(item.getCheckQty());
				dtl.setPoQty(item.getPoQty());
				dtl.setStatus(BigDecimal.valueOf(11));
				dtl.setQuality(item.getQuality());
				dtl.setItemType(item.getItemType());
				dtl.setBrandNo(item.getBrandNo());
				if (StringUtils.isEmpty(item.getBoxNo())) {//串码的箱数量，取同商品的的packQty.
					BillImReceiptDtl recdtl = new BillImReceiptDtl();
					recdtl.setLocno(billImCheck.getLocno());
					recdtl.setReceiptNo(billImCheck.getsImportNo());
					recdtl.setItemNo(item.getItemNo());
					BillImReceiptDtl result = billImReceiptDtlService.selectDtlByItemNo(recdtl);
					if (result != null) {
						dtl.setPackQty(result.getPackQty() == null ? new BigDecimal(1) : result.getPackQty());
					}
				} else {
					dtl.setPackQty(item.getPackQty() == null ? new BigDecimal(1) : item.getPackQty());
				}

				//以下三个参数需要确认，需要在页面端查询的时候查询处理，保存的时候在传到这里来。
				dtl.setBarcode(item.getBarcode());
				dtl.setSysNo(item.getSysNo());
				dtl.setCheckWorker1(billImCheck.getCreator());
				//dtl.setPackQty(item.getPackQty() == null ? BigDecimal.valueOf(0) : item.getPackQty());
				billImCheckDtlService.add(dtl);
			}

			//校验重复性
			BillImCheckDtl entity = new BillImCheckDtl();
			entity.setLocno(billImCheck.getLocno());
			entity.setOwnerNo(billImCheck.getOwnerNo());
			entity.setCheckNo(billImCheck.getCheckNo());
			List<BillImCheckDtl> listDtls = billImCheckDtlService.findCheckDtlGroupByCount(entity);
			if (CommonUtil.hasValue(listDtls)) {
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + listDtls.get(0).getBoxNo() + ",");
				sb.append("商品：" + listDtls.get(0).getItemNo() + ",");
				sb.append("尺码：" + listDtls.get(0).getSizeNo() + "重复!");
				throw new ManagerException(sb.toString());
			}

		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void addMain(BillImCheck billImCheck,AuthorityParams authorityParams) throws ManagerException {
		try {
			
			Map<String, Object> checkParams = new HashMap<String, Object>();
			checkParams.put("locno", billImCheck.getLocno());
			checkParams.put("ownerNo", billImCheck.getOwnerNo());
			checkParams.put("sImportNo", billImCheck.getsImportNo());
			checkParams.put("status", STATUS10);
			List<BillImCheck> checkList=billImCheckService.findByBiz(null, checkParams);
			if(CommonUtil.hasValue(checkList)){
				throw new ServiceException("收货单:"+billImCheck.getsImportNo()+"已存在建单状态的验收单!");
			}
			
			//保存主档
			this.add(billImCheck);
			billImCheckService.modifyById(billImCheck);
	
//			BillImCheckDtlKey billImCheckDtlKey = new BillImCheckDtlKey();
//			billImCheckDtlKey.setCheckNo(billImCheck.getCheckNo());
//			billImCheckDtlKey.setLocno(billImCheck.getLocno());
//			billImCheckDtlKey.setOwnerNo(billImCheck.getOwnerNo());
//			Integer maxRowId = billImCheckDtlService.selectMaxRowId(billImCheckDtlKey);
//			maxRowId = maxRowId == null ? 0 : maxRowId;
			
			//根据收货单号获取收货单的信息
//			BillImReceiptDtl  obj = new  BillImReceiptDtl();
//			obj.setReceiptNo(billImCheck.getsImportNo());
//			obj.setLocno(billImCheck.getLocno());
//			List<BillImReceiptDtl> listReDtls = billImReceiptDtlService.findAllDetailByReciptNo(obj, authorityParams);
			
			//根据收货单号组装插入验收单的信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("locno", billImCheck.getLocno());
			map.put("receiptNo", billImCheck.getsImportNo());
			//map.put("checkWorker1", billImCheck.getCreator());
			map.put("checkWorker1", billImCheck.getCheckWorker());
			map.put("checkName1", billImCheck.getCheckName());//验收人中文名次
			map.put("checkNo", billImCheck.getCheckNo());
			map.put("editor", billImCheck.getEditor());
			map.put("editorName", billImCheck.getEditorName());
			map.put("edittm", billImCheck.getEdittm());
			List<BillImCheckDtl> lstCheckDtl = billImCheckDtlService.selectCheckDetailByReciptNo(map, authorityParams);
//			if(!CommonUtil.hasValue(lstCheckDtl)){
//				throw new ManagerException("保存验收明细失败,查询收货单明细数据为空!");
//			}
			
			//批量插入明细
			billImCheckDtlService.insertBathCheckDtl(lstCheckDtl);
			
			//锁定容器
			List<String> addList = new ArrayList<String>();
			for (BillImCheckDtl checkDtl : lstCheckDtl) {
				//如果板号为空,锁箱号,否则把整板都锁掉
				String panNo = checkDtl.getPanNo();
				String boxNo = checkDtl.getBoxNo();
				if(!addList.contains(panNo)&&StringUtils.isNotBlank(panNo)){
					addList.add(panNo);
				}
				if(!addList.contains(boxNo)){
					addList.add(boxNo);
				}
			}
			
			//新增明细锁容器
			int containerCount = 0;
			if(CommonUtil.hasValue(addList)){
				List<BmContainer> addContainerList = new ArrayList<BmContainer>();
				for (String panNo : addList) {
					BmContainer bmContainer = new BmContainer();
					bmContainer.setLocno(billImCheck.getLocno());
					bmContainer.setConNo(panNo);
					bmContainer.setStatus(ContainerStatusEnums.STATUS1.getContainerStatus());
					bmContainer.setOptBillNo(billImCheck.getCheckNo());
					bmContainer.setOptBillType(ContainerTypeEnums.E.getOptBillType());
					boolean result = bmContainerService.checkBmContainerStatus(bmContainer);
					if(result){
						throw new ServiceException("容器号:"+panNo+"不存在或已锁定!");
					}
					addContainerList.add(bmContainer);
				}
				containerCount = bmContainerService.batchUpdate(addContainerList);
				if(containerCount < 1){
					throw new ServiceException("锁定验收单容器失败!");
				}
			}
			
//			for (BillImReceiptDtl item : listReDtls) {
//				BillImCheckDtl dtl = new BillImCheckDtl();
//				dtl.setItemNo(item.getItemNo());
//				dtl.setCheckNo(billImCheck.getCheckNo());
//				dtl.setLocno(billImCheck.getLocno());
//				dtl.setOwnerNo(billImCheck.getOwnerNo());
//				dtl.setRowId(++maxRowId);
//				dtl.setSizeNo(item.getSizeNo());
//				dtl.setBoxNo(StringUtils.isEmpty(item.getBoxNo()) ? "N" : item.getBoxNo());
//				
//				//计划数量
//				if (item.getPoQty() != null && item.getCheckQty() != null) {
//					dtl.setPoQty(new BigDecimal(item.getPoQty().intValue()
//							- item.getCheckQty().intValue()));
//				}
//				
//				dtl.setCheckQty(dtl.getPoQty());
//				
//				dtl.setStatus(BigDecimal.valueOf(11));
//				dtl.setQuality(item.getQuality());
//				dtl.setItemType(item.getItemType());
//				dtl.setBrandNo(item.getBrandNo());
//			    dtl.setPackQty(item.getPackQty());
//
//				//以下三个参数需要确认，需要在页面端查询的时候查询处理，保存的时候在传到这里来。
//				dtl.setBarcode(item.getBarcode());
//				dtl.setSysNo(item.getSysNo());
//				dtl.setCheckWorker1(billImCheck.getCreator());
//				billImCheckDtlService.add(dtl);
//			}

			//校验重复性
			BillImCheckDtl entity = new BillImCheckDtl();
			entity.setLocno(billImCheck.getLocno());
			entity.setOwnerNo(billImCheck.getOwnerNo());
			entity.setCheckNo(billImCheck.getCheckNo());
			List<BillImCheckDtl> listDtls = billImCheckDtlService.findCheckDtlGroupByCount(entity);
			if (CommonUtil.hasValue(listDtls)) {
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + listDtls.get(0).getBoxNo() + ",");
				sb.append("商品：" + listDtls.get(0).getItemNo() + ",");
				sb.append("尺码：" + listDtls.get(0).getSizeNo() + "重复!");
				throw new ManagerException(sb.toString());
			}

		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billImCheckService.findSumQty(params, authorityParams);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> directCheck(String nos, String locno,
			SystemUser user,AuthorityParams authorityParams) throws ManagerException {
		/*
		 * 1.检验各个收货单的状态是否是【收货完成】且不能已经存在对应的验收单
		 * 2.为每个收货单生成一张验收单：验收人为【当前登录人】,验收日期为【当时日期】,货主为【百丽】,备注为【来自验收转单】
		 * 3.将每张收货单的明细添加至对应的验收单明细中：
		 * 4.审核验收单
		 */
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Map<String,Object> obj = new HashMap<String, Object>();
		if(!CommonUtil.hasValue(nos) || !CommonUtil.hasValue(locno)){
			obj.put("status", "error");
			obj.put("msg", "缺少参数!");
			return obj;
		}
		String [] noArr = nos.split(",");
		String ownerNo = "BL";
		BillImCheck billImCheck = null;
		String checkNo = null;
		List<BillImCheck> list = new ArrayList<BillImCheck>();
		try {
			for(String receiptNo:noArr){
				billImCheck = new BillImCheck();
				billImCheck.setLocno(locno);
				checkNo = procCommonService.procGetSheetNo(billImCheck.getLocno(), CNumPre.IM_CHECK_NO_PRE);
				billImCheck.setOwnerNo(ownerNo);
				billImCheck.setCheckNo(checkNo);
				billImCheck.setsImportNo(receiptNo);
				billImCheck.setCreator(user.getLoginName());
				billImCheck.setCreatorName(user.getUsername());
				billImCheck.setCreatetm(date);
				billImCheck.setEditor(user.getLoginName());
				billImCheck.setEditorName(user.getUsername());
				billImCheck.setEdittm(date);
				billImCheck.setCheckWorker(user.getLoginName());
				billImCheck.setCheckName(user.getUsername());
				billImCheck.setRemark("来自验收转单");
				billImCheck.setCheckEndDate(format.parse(format.format(date)));
				billImCheck.setCheckStartDate(date);
				billImCheck.setSourceType(BillImCheckSourceTypeEnums.TYPE1.getType());
				addMain(billImCheck, authorityParams);
				list.add(billImCheck);
			}
			billImCheckService.check(list, user.getLoginName(),user.getUsername());
		} catch (ServiceException e) {
			throw new ManagerException(e);
		} catch (ParseException e) {
			throw new ManagerException(e);
		}
		obj.put("status", "success");
		return obj;
	}
}