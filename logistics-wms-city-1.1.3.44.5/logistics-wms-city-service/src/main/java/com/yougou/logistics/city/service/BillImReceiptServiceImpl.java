package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.BillImImportKey;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.dal.mapper.BillImImportDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImImportMapper;
import com.yougou.logistics.city.dal.mapper.BillImReceiptDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImReceiptMapper;
import com.yougou.logistics.city.dal.mapper.ConBoxMapper;

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
@Service("billImReceiptService")
class BillImReceiptServiceImpl extends BaseCrudServiceImpl implements BillImReceiptService {
	@Resource
	private BillImReceiptMapper billImReceiptMapper;
	@Resource
	private BillImReceiptDtlMapper billImReceiptDtlMapper;
	@Resource
	private BillImImportDtlMapper billImImportDtlMapper;
	@Resource
	private BillImImportMapper billImImportMapper;
	@Resource
	private ConBoxMapper conBoxMapper;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private BillStatusLogService billStatusLogService;
	@Resource
	private BillImImportService billImImportService;
	@Resource
	private BmContainerService bmContainerService;
	@Resource
	private BillContainerTaskService billContainerTaskService;

	private static final String STATUS10 = "10";
	private static final String STATUS11 = "11";
	private static final String STATUS0 = "0";
	private static final String STATUS1 = "1";
	private static final String STATUS20 = "20";
	private static final String STATUS30 = "30";
	private static final String BILLTYPE = "IM";

	@Override
	public BaseCrudMapper init() {
		return billImReceiptMapper;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceipt> findReceiptByPage(SimplePage page, BillImReceipt receipt, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectReceiptByPage(page, receipt, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectCountMx(BillImReceipt receipt, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectCountMx(receipt, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveMain(BillImReceipt receipt) throws ServiceException {
		if (StringUtils.isEmpty(receipt.getReceiptNo())) {
			String receiptNo = procCommonService.procGetSheetNo(receipt.getLocno(), CNumPre.IM_RECEDE_PRE);
			receipt.setReceiptNo(receiptNo);
			billImReceiptMapper.insertSelective(receipt);
		} else {
			receipt.setUptStatus(STATUS10);
			int a = billImReceiptMapper.updateByPrimaryKeySelective(receipt);
			if(a < 1){
				throw new ServiceException("单据"+receipt.getReceiptNo()+"已删除或状态已改变，不能进行修改操作！");
			}
			BillImReceiptDtl receiptDtl = new BillImReceiptDtl();
			receiptDtl.setLocno(receipt.getLocno());
			receiptDtl.setOwnerNo(receipt.getOwnerNo());
			receiptDtl.setReceiptNo(receipt.getReceiptNo());
			receiptDtl.setEditor(receipt.getEditor());
			receiptDtl.setEditorName(receipt.getEditorName());
			receiptDtl.setEdittm(new Date());
			receiptDtl.setCheckWorker1(receipt.getReceiptWorker());
			receiptDtl.setCheckName1(receipt.getReceiptName());
			a = billImReceiptDtlMapper.updateByPrimaryKeySelective(receiptDtl);
			if(a < 1){
				throw new ServiceException("单据"+receipt.getReceiptNo()+"更新明细数据失败！");
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	@Override
	public void save(BillImReceipt receipt, List<BillImImportDtlDto> insertDetail) throws ServiceException {
		try {
			//保存主表
			String receiptNo = procCommonService.procGetSheetNo(receipt.getLocno(), CNumPre.IM_RECEDE_PRE);
			receipt.setReceiptNo(receiptNo);
			billImReceiptMapper.insertSelective(receipt);
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	public void addDtal(BillImReceipt receipt, BillImImportDtl dtl) throws ServiceException {
		try {
			String [] importNos=dtl.getImportNo().split(",");
			for(String importNo:importNos){
				//通过箱号查询遇到或通知单详情
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("locno", dtl.getLocno());
				map.put("boxNo", dtl.getBoxNo());
				map.put("importNo", importNo);
				
				//查找预到货通知单
				BillImImportKey billImImportKey=new BillImImportKey();
				billImImportKey.setImportNo(importNo);
				billImImportKey.setLocno(dtl.getLocno());
				billImImportKey.setOwnerNo(receipt.getOwnerNo());
				
				BillImImport billImImport=(BillImImport) billImImportService.findById(billImImportKey);		
				if(billImImport==null){
					throw new ServiceException("未找到对应的预到货通知单:"+importNo);
				}			
				//预到货通知单品质
				String quality=billImImport.getQuality();
				
				
				List<BillImImportDtl> list = billImImportDtlMapper.selectByParams(null, map);
				BillImReceiptDtl receiptDtl = null;
				int rowId = 0;
				if (list.size() > 0) {
					Integer maxRow = billImReceiptDtlMapper.selectMaxRowId(receipt);
					if (maxRow != null) {
						rowId = maxRow;
					}
				}
				for (BillImImportDtl importDtl : list) {
					rowId++;
					receiptDtl = new BillImReceiptDtl();
					receiptDtl.setRowId(rowId);
					receiptDtl.setLocno(receipt.getLocno());
					receiptDtl.setOwnerNo(receipt.getOwnerNo());
					receiptDtl.setReceiptNo(receipt.getReceiptNo());
					receiptDtl.setImportNo(importDtl.getImportNo());
					receiptDtl.setBoxNo(importDtl.getBoxNo());
					receiptDtl.setStatus(STATUS11);
					receiptDtl.setItemNo(importDtl.getItemNo());
					receiptDtl.setSizeNo(importDtl.getSizeNo());
					receiptDtl.setPackQty(importDtl.getPackQty());
					receiptDtl.setReceiptQty(importDtl.getPoQty());
					receiptDtl.setQuality(quality);
					receiptDtl.setItemType(importDtl.getItemType());
					receiptDtl.setCheckWorker1(receipt.getReceiptWorker());
					receiptDtl.setBrandNo(importDtl.getBrandNo());
					//receiptDtl.setCheckWorker1(receipt.getEditor());
					receiptDtl.setCheckName1(receipt.getReceiptName());//收货人1中文名称
					receiptDtl.setPanNo(dtl.getPanNo());
					receiptDtl.setEditor(receipt.getEditor());//修改人
					receiptDtl.setEditorName(receipt.getEditorName());//修改人中文名称
					billImReceiptDtlMapper.insertSelective(receiptDtl);
					ConBox box = new ConBox();
					box.setLocno(receipt.getLocno());
					box.setOwnerNo(receipt.getOwnerNo());
					box.setBoxNo(importDtl.getBoxNo());
					box.setStatus(STATUS1);
					conBoxMapper.updateByPrimaryKeySelective(box);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addReceiptDtl(BillImReceipt receipt,List<BillImImportDtl> list,String quality,int rowId)  throws ServiceException  {
		BillImReceiptDtl receiptDtl = null;
		try{
			for (BillImImportDtl importDtl : list) {
				rowId++;
				receiptDtl = new BillImReceiptDtl();
				receiptDtl.setRowId(rowId);
				receiptDtl.setLocno(receipt.getLocno());
				receiptDtl.setOwnerNo(receipt.getOwnerNo());
				receiptDtl.setReceiptNo(receipt.getReceiptNo());
				receiptDtl.setImportNo(importDtl.getImportNo());
				receiptDtl.setBoxNo(importDtl.getBoxNo());
				receiptDtl.setStatus(STATUS11);
				receiptDtl.setItemNo(importDtl.getItemNo());
				receiptDtl.setSizeNo(importDtl.getSizeNo());
				receiptDtl.setPackQty(importDtl.getPackQty());
				receiptDtl.setReceiptQty(importDtl.getPoQty());
				receiptDtl.setQuality(quality);
				receiptDtl.setItemType(importDtl.getItemType());
				//receiptDtl.setCheckWorker1(receipt.getReceiptWorker());
				receiptDtl.setBrandNo(importDtl.getBrandNo());
				receiptDtl.setCheckWorker1(receipt.getEditor());
				receiptDtl.setCheckName1(receipt.getEditorName());//收货人1中文名称
				//receiptDtl.setPanNo(dtl.getPanNo());
				billImReceiptDtlMapper.insertSelective(receiptDtl);
				ConBox box = new ConBox();
				box.setLocno(receipt.getLocno());
				box.setOwnerNo(receipt.getOwnerNo());
				box.setBoxNo(importDtl.getBoxNo());
				box.setStatus(STATUS1);
				conBoxMapper.updateByPrimaryKeySelective(box);
			}
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public void delDtal(BillImReceipt receipt, BillImImportDtl dtl) throws ServiceException {
		try {
			//通过箱号查询遇到或通知单详情
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("locno", receipt.getLocno());
			map.put("boxNo", dtl.getBoxNo());
			map.put("receiptNo", receipt.getReceiptNo());
			map.put("ownerNo", receipt.getOwnerNo());
			//更新箱状态
			ConBox box = new ConBox();
			box.setLocno(receipt.getLocno());
			box.setOwnerNo(receipt.getOwnerNo());
			box.setBoxNo(dtl.getBoxNo());
			box.setStatus(STATUS0);
			conBoxMapper.updateByPrimaryKeySelective(box);
			List<BillImReceiptDtl> list = billImReceiptDtlMapper.selectByParams(null, map);
			for (BillImReceiptDtl recDtl : list) {
				billImReceiptDtlMapper.deleteByPrimarayKeyForModel(recDtl);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> update(BillImReceipt receipt, List<BillImImportDtlDto> insert,
			List<BillImImportDtlDto> update, List<BillImImportDtlDto> del,SystemUser user) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> repeatList = new ArrayList<String>();
		List<String> nousedList = new ArrayList<String>();
		try {
			String locno=receipt.getLocno();
			Map<String,BmContainer> bmMap=new HashMap<String,BmContainer>();//容器集合包括箱和板
			//修改主表
			receipt.setUptStatus(STATUS10);
			int a = billImReceiptMapper.updateByPrimaryKeySelective(receipt);
			if(a < 1){
				throw new ServiceException("单据:"+receipt.getReceiptNo()+"已删除或状态已改变，不能进行保存明细操作！");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("receiptNo", receipt.getReceiptNo());
			map.put("locno", receipt.getLocno());
			int count = billImReceiptDtlMapper.selectDetailCount(map,null);
			SimplePage page = new SimplePage(1, count, count);
			List<BillImImportDtlDto> boxList = billImReceiptDtlMapper.selectDetail(page, map,null);
			Map<String,String> delPanNoMap=new HashMap<String,String>();//删除集合使用
			Map<String,String> updatePanNoMap=new HashMap<String,String>();//更新集合使用
			Map<String,String> updateBoxNoMap=new HashMap<String,String>();//更新集合使用
			for (BillImImportDtlDto boxNo : boxList) {
				String tmpPanNo=boxNo.getPanNo();
				String tmpBoxNo=boxNo.getBoxNo();
				if(StringUtils.isNotEmpty(tmpPanNo)){
					String delBoxNos=delPanNoMap.get(tmpPanNo);
					if(StringUtils.isNotEmpty(delBoxNos)){
						delBoxNos+=";"+tmpBoxNo;
					}else{
						delBoxNos=tmpBoxNo;
					}
					delPanNoMap.put(tmpPanNo, delBoxNos);
					String updateBoxNos=updatePanNoMap.get(tmpPanNo);
					if(StringUtils.isNotEmpty(updateBoxNos)){
						updateBoxNos+=";"+tmpBoxNo;
					}else{
						updateBoxNos=tmpBoxNo;
					}
					updatePanNoMap.put(tmpPanNo, updateBoxNos);
					updateBoxNoMap.put(tmpBoxNo, tmpPanNo);
				}
			}
			Map<String,String> delMap=new HashMap<String,String>();
			//删除子表  先做删除，防止删除了箱号，又添加相同的箱号
			for (BillImImportDtlDto dtl : del) {
				delDtal(receipt, dtl);
				delCon(locno, dtl, bmMap, delPanNoMap,user);//解除箱或板锁定
				delMap.put(dtl.getBoxNo()+dtl.getLocno(), dtl.getBoxNo()+dtl.getLocno());
			}
			
			//更新
			for (BillImImportDtlDto dtl : update) {
				String key=dtl.getBoxNo()+dtl.getLocno();
				String delValue=delMap.get(key);
				if(!StringUtils.isNotBlank(delValue)){
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("locno", receipt.getLocno());
					tempMap.put("boxNo", dtl.getBoxNo());
					tempMap.put("receiptNo", receipt.getReceiptNo());
					tempMap.put("panNo", dtl.getPanNo());
					tempMap.put("editor", receipt.getEditor());
					tempMap.put("editorName", receipt.getEditorName());
					tempMap.put("edittm", receipt.getEdittm());
					billImReceiptDtlMapper.updatePanNoBy(tempMap);//更新单明细
					updateCon(locno, receipt, dtl, bmMap, updatePanNoMap,updateBoxNoMap,user);
				}
			}
			HashSet<String> panNoList=new HashSet<String>();//新增明细中所有的箱和板集合
			for (BillImImportDtl dtl : insert) {
				for (BillImImportDtlDto boxNo : boxList) {
					if (dtl.getBoxNo().equals(boxNo.getBoxNo())) {
						repeatList.add(dtl.getBoxNo());
						break;
					}
				}
				//判断箱号是否可用
				ConBox box = new ConBox();
				box.setLocno(dtl.getLocno());
				box.setBoxNo(dtl.getBoxNo());
				box.setOwnerNo(receipt.getOwnerNo());
				ConBox conBox = conBoxMapper.selectByPrimaryKey(box);
				if (null != conBox && !STATUS0.equals(conBox.getStatus())) {
					nousedList.add(dtl.getBoxNo());
				}
				//判断箱是否被占用
				BmContainer bm=new BmContainer();
				bm.setConNo(dtl.getBoxNo());
				bm.setLocno(locno);
				bm.setOptBillNo(receipt.getReceiptNo());
				bm.setType("C");
				boolean f=bmContainerService.checkBmContainerStatus(bm);
				if(f){
					throw new ServiceException("箱号:"+dtl.getBoxNo()+"被占用或不存在！");
				}
				if(StringUtils.isNotEmpty(dtl.getPanNo())){
					panNoList.add(dtl.getPanNo());//板集合
				}
				BmContainer bm1=new BmContainer();
				bm1.setOptBillType("D");
				bm1.setStatus(STATUS1);
				bm1.setOptBillNo(receipt.getReceiptNo());
				bm1.setConNo(dtl.getBoxNo());
				bm1.setLocno(locno);
				bm1.setEditor(user.getLoginName());
				bmMap.put(dtl.getBoxNo(), bm1);//箱锁定
			}
			//判断板被占用
			if(null!=panNoList&&panNoList.size()>0){
				for(String panNo:panNoList){
					BmContainer bm=new BmContainer();
					bm.setConNo(panNo);
					bm.setLocno(receipt.getLocno());
					bm.setOptBillNo(receipt.getReceiptNo());
					bm.setType("P");
					boolean f=bmContainerService.checkBmContainerStatus(bm);
					if(f){
						throw new ServiceException("父容器号:"+panNo+"被占用或不存在！");
					}else{
						Map<String,Object> isNewPanNoNap=new HashMap<String,Object>();
						isNewPanNoNap.put("locno", receipt.getLocno());
						isNewPanNoNap.put("panNo", panNo);
						int i=conBoxMapper.findByPanNo(isNewPanNoNap);
						if(i>0){
							throw new ServiceException("父容器号:"+panNo+"必须是空板");
						}
						bm.setOptBillType("D");
						bm.setStatus(STATUS1);
						bm.setEditor(user.getLoginName());
						bmMap.put(panNo, bm);//板锁定
					}
				}
			}
			//批量处理容器解锁和锁定
			if(bmMap.size()>0){
				List<BmContainer> bmList=new ArrayList<BmContainer>();
				Iterator<?> iter = bmMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					bmList.add((BmContainer) entry.getValue());
				}
				bmContainerService.batchUpdate(bmList);
			}
			if (repeatList.size() > 0) {
				new ServiceException();
				resultMap.put("repeat", repeatList);
				return resultMap;
			}
			if (nousedList.size() > 0) {
				new ServiceException();
				resultMap.put("noused", nousedList);
				return resultMap;
			}

			for (BillImImportDtl dtl : insert) {
				addDtal(receipt, dtl);
			}

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return resultMap;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectMainReciptCount(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectMainRecipt(page, map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceipt> findMainReciptSum(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectMainReciptSum(page, map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public String selectIsReceiptByImportNo(String ImportNo) throws ServiceException {
		try {
			return billImReceiptMapper.selectIsReceiptByImportNo(ImportNo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceipt> findReciptNoChecked(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectReciptNoChecked(page, map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findReciptNoCheckedCount(SimplePage page, Map<?, ?> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectReciptNoCheckedCount(page, map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int auditReceipt(BillImReceipt receipt) throws ServiceException {
		int k  = 0;
		try {
			//更新收货单状态为收货完成
			receipt.setStatus(STATUS20);
			//修改前的状态
			receipt.setUptStatus(STATUS10);
			k  = billImReceiptMapper.updateByPrimaryKeySelective(receipt);
			if( k < 1){
				throw new ServiceException("单据"+receipt.getReceiptNo() + "已删除或状态已改变，不能进行审核操作！");
			}
			
			String auditor = receipt.getAuditor();
			Date date = receipt.getAudittm();
			
			BillContainerTask containerTask=new BillContainerTask();//绑板业务单据
			containerTask.setLocno(receipt.getLocno());
			containerTask.setContaskNo(receipt.getReceiptNo());
			containerTask.setStatus("10");
			containerTask.setCreator(receipt.getAuditor());
			containerTask.setCreatetm(new Date());
			containerTask.setUseType("D");
			containerTask.setBusinessType("1");
			List<BillContainerTaskDtl> taskDtlList=new ArrayList<BillContainerTaskDtl>();//绑板业务单据明细
			//查询收货单详情
			Map<String, Object> parmMap = new HashMap<String, Object>();
			parmMap.put("locno", receipt.getLocno());
			parmMap.put("ownerNo", receipt.getOwnerNo());
			parmMap.put("receiptNo", receipt.getReceiptNo());
			List<BillImReceiptDtl> receiptDtls = billImReceiptDtlMapper.selectByParams(null, parmMap);
			if(receiptDtls.size() > 0) {
				//更新预到货收货数量
				BillImImportDtl imDtl = null;
				for (BillImReceiptDtl tdtl : receiptDtls) {
					imDtl = new BillImImportDtl();
					imDtl.setLocno(tdtl.getLocno());
					imDtl.setOwnerNo(tdtl.getOwnerNo());
					imDtl.setImportNo(tdtl.getImportNo());
					imDtl.setBoxNo(tdtl.getBoxNo());
					imDtl.setSizeNo(tdtl.getSizeNo());
					billImImportDtlMapper.updateReceiptQty(imDtl);
					BillContainerTaskDtl taskDtl=new BillContainerTaskDtl();
					taskDtl.setLocno(receipt.getLocno());
					taskDtl.setContaskNo(receipt.getReceiptNo());
					taskDtl.setsContainerNo("N");
					taskDtl.setsSubContainerNo("N");
					taskDtl.setdContainerNo(tdtl.getPanNo());
					taskDtl.setdSubContainerNo(tdtl.getBoxNo());
					taskDtl.setContainerType("P");
					taskDtl.setsCellNo("N");
					taskDtl.setdCellNo("N");
					taskDtl.setBrandNo(tdtl.getBrandNo());
					taskDtl.setItemNo(tdtl.getItemNo());
					taskDtl.setSizeNo(tdtl.getSizeNo());
					taskDtl.setRowId(Long.parseLong(tdtl.getRowId().toString()));
					taskDtl.setQty(tdtl.getReceiptQty().longValue());
					taskDtl.setEditor(receipt.getAuditor());
					taskDtl.setEdittm(new Date());
					taskDtl.setCreator(receipt.getAuditor());
					taskDtl.setCreatetm(new Date());
					taskDtl.setQuality(tdtl.getQuality());
					taskDtl.setItemType(tdtl.getItemType());
					taskDtlList.add(taskDtl);
				}

				BillImReceiptDtl dtl = new BillImReceiptDtl();
				dtl.setLocno(receipt.getLocno());
				dtl.setOwnerNo(receipt.getOwnerNo());
				dtl.setReceiptNo(receipt.getReceiptNo());
				//查询收货单下的所有预到货通知单
				List<BillImReceiptDtl> importNos = billImReceiptDtlMapper.selectImportNoByReceiptNo(dtl);
				//更新预到货通知单的状态
				Map<String, Object> map = new HashMap<String, Object>();
				String receiptMsg = "部分收货";
				for (BillImReceiptDtl tdtl : importNos) {
					map.put("locno", tdtl.getLocno());
					map.put("ownerNo", tdtl.getOwnerNo());
					map.put("importNo", tdtl.getImportNo());
					int allCount = billImImportDtlMapper.selectCount(map, null);
					//如果明细不为空，并且所有明细收货数量采购数量相等
					String status = "12";
					if (allCount > 0 && billImImportDtlMapper.selectReceiptedCount(map) == allCount) {
						status = "20";
						receiptMsg = "收货完成";
					}

					//更新通知单的状态
					BillImImport im = new BillImImport();
					im.setImportNo(tdtl.getImportNo());
					im.setLocno(tdtl.getLocno());
					im.setOwnerNo(tdtl.getOwnerNo());

					//查询预到货通知单的状态
					BillImImport importDtl = billImImportMapper.selectByPrimaryKey(im);
					if (!importDtl.getStatus().equals(status)) {//如果状态改变，记录状态日志表
						billStatusLogService.procInsertBillStatusLog(tdtl.getLocno(), tdtl.getImportNo(), BILLTYPE, status,
								receiptMsg, receipt.getAuditor());
					}
					
					//更新预到货通知的状态
					BillImImport imStatus = im;
					imStatus.setStatus(status);
					//如果不是部分验证 更新状态
					BillImImport billImImport = billImImportMapper.selectByPrimaryKey(imStatus);
					if(!(STATUS30).equals(billImImport.getStatus())){
						im.setStatus(imStatus.getStatus());
					}
					im.setEditor(auditor);
					im.setEdittm(date);
					billImImportMapper.updateByPrimaryKeySelective(im);
				}
			}else {
				throw new ServiceException(receipt.getReceiptNo() + "无明细数据，不可审核！");
			}
			//插入业务单据表
			billContainerTaskService.insertBillContainerTask(containerTask, taskDtlList);
			procCommonService.procContaskAudit(receipt.getLocno(), "D", receipt.getReceiptNo(), auditor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return k;
	}

	@Override
	public int selectCount4Direct(Map map) throws ServiceException {
		try {
			return billImReceiptMapper.selectCount4Direct(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImImport> selectByPage4Direct(SimplePage page, Map map) throws ServiceException {
		try {
			return billImReceiptMapper.selectByPage4Direct(page, map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImImportDtlDto> selectBatchSelectBox(List<String> importNoList,Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectBatchSelectBox(importNoList,map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceipt> findImReceiptPrint(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectImReceiptPrint(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	private void delCon(String locno,BillImImportDtlDto dtl,Map<String,BmContainer> bmMap,
			Map<String,String> delPanNoMap,SystemUser user){
		BmContainer bm=new BmContainer();
		bm.setFalg("true");
		bm.setLocno(locno);
		bm.setConNo(dtl.getBoxNo());
		bm.setStatus(STATUS0);
		bm.setEditor(user.getLoginName());
		bmMap.put(dtl.getBoxNo(), bm);//解除箱锁定状态
		String panNo=dtl.getPanNo();
		String conNo=dtl.getBoxNo();
		if(StringUtils.isNotEmpty(panNo)){
			String boxNos=delPanNoMap.get(panNo);
			BmContainer bm1=new BmContainer();
			bm1.setFalg("true");
			bm1.setLocno(locno);
			bm1.setStatus(STATUS0);
			bm1.setEditor(user.getLoginName());
			if(StringUtils.isNotEmpty(boxNos)){
				String [] boxNoArray=boxNos.split(";");
				StringBuffer tmpSb=new StringBuffer();
				for(int i=0;i<boxNoArray.length;i++){
					if(!boxNoArray[i].equals(conNo)){
						if(tmpSb.length()>0){
							tmpSb.append(";").append(boxNoArray[i]);
						}else{
							tmpSb.append(boxNoArray[i]);
						}
					}
				}
				if(tmpSb.length()==0){
					bm1.setConNo(panNo);
					bmMap.put(dtl.getPanNo(), bm1);//解除板锁定状态，如果其他箱占用不能解锁
				}else{
					delPanNoMap.put(panNo, tmpSb.toString());
				}
			}else{
				bm1.setConNo(panNo);
				bmMap.put(dtl.getPanNo(), bm1);//解除板锁定状态，如果其他箱占用不能解锁
			}
		}
	}
	private void updateCon(String locno,BillImReceipt receipt,BillImImportDtlDto dtl,Map<String,BmContainer> bmMap,
			Map<String,String> updatePanNoMap,Map<String,String> updateBoxNoMap,SystemUser user)throws Exception{
		String panNo=dtl.getPanNo();
		String boxNo=dtl.getBoxNo();
		BmContainer boxBm=new BmContainer();
		boxBm.setConNo(boxNo);
		boxBm.setLocno(receipt.getLocno());
		boxBm.setOptBillNo(receipt.getReceiptNo());
		boxBm.setStatus(STATUS1);
		boxBm.setOptBillType("D");
		boxBm.setEditor(user.getLoginName());
		bmMap.put(boxNo, boxBm);//锁定箱
		if(StringUtils.isNotEmpty(panNo)){
			//判断板是否被占用
			BmContainer bm=new BmContainer();
			bm.setConNo(panNo);
			bm.setLocno(receipt.getLocno());
			bm.setOptBillNo(receipt.getReceiptNo());
			bm.setType("P");
			boolean f=bmContainerService.checkBmContainerStatus(bm);
			if(f){
				throw new ServiceException("父容器号:"+panNo+"被占用或不存在！");
			}else{
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("locno", receipt.getLocno());
				map.put("panNo", panNo);
				int i=conBoxMapper.findByPanNo(map);
				if(i>0){
					throw new ServiceException("父容器号:"+panNo+"必须是空板");
				}
				//锁定新增托盘
				bm.setEditor(user.getLoginName());
				bm.setStatus(STATUS1);
				bm.setOptBillType("D");
				bmMap.put(panNo, bm);//锁定板
			}
		}
		//解除旧托盘
		String tmpPanNo=updateBoxNoMap.get(boxNo);
		String updateBoxs=updatePanNoMap.get(tmpPanNo);
		StringBuffer sb=new StringBuffer();
		if(StringUtils.isNotBlank(updateBoxs)){
			String [] boxArray=updateBoxs.split(";");
			for(int i=0;i<boxArray.length;i++){
				if(!boxNo.equals(boxArray[i])){
					if(sb.length()>0){
						sb.append(";").append(boxArray[i]);
					}else{
						sb.append(boxArray[i]);
					}
				}
			}
			if(sb.length()==0){
				BmContainer bm1=new BmContainer();
				//解除板锁定
				bm1.setFalg("true");
				bm1.setLocno(locno);
				bm1.setConNo(tmpPanNo);
				bm1.setStatus(STATUS0);
				bm1.setEditor(user.getLoginName());
				bmMap.put(tmpPanNo, bm1);
			}else{
				updatePanNoMap.put(tmpPanNo, sb.toString());
			}
		}
	}

}