package com.yougou.logistics.city.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.enums.BillHmPlanBusinessTypeEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCellStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillHmPlanDtlService;
import com.yougou.logistics.city.service.BillHmPlanService;
import com.yougou.logistics.city.service.ItemBarcodeService;
import com.yougou.logistics.city.service.ItemService;
import com.yougou.logistics.city.service.SizeInfoService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
@Service("billHmPlanDtlManager")
class BillHmPlanDtlManagerImpl extends BaseCrudManagerImpl implements BillHmPlanDtlManager {
    @Resource
    private BillHmPlanDtlService billHmPlanDtlService;
    
    @Resource
    private BillHmPlanService billHmPlanService;

	@Resource
	private CmDefcellManager cmDefcellManager;
	
	@Resource
	private CmDefareaManager cmDefareaManager;
	
	@Resource
	private BillHmPlanManager billHmPlanManager;
	@Resource
	private ItemService itemService;
	
	@Resource
	private ItemBarcodeService itemBarCodeService;
	
	@Resource
	private com.yougou.logistics.city.service.ConContentService ConContentService;
	
	@Resource
	private SizeInfoService sizeInfoService;
    @Override
    public BaseCrudService init() {
        return billHmPlanDtlService;
    }
    
    @Override
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billHmPlanDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> checkDtlCell(String planNo, String locoNo, String cellNo) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {

			BillHmPlan billHmPlanParam = new BillHmPlan();
			billHmPlanParam.setLocno(locoNo);
			billHmPlanParam.setPlanNo(planNo);
			BillHmPlan billHmPlan;
			billHmPlan = billHmPlanService.findById(billHmPlanParam);
			if(billHmPlan == null){
				throw new ManagerException("单据"+planNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
			}
			CmDefcell defcellParam = new CmDefcell();
			defcellParam.setCellNo(cellNo);
			defcellParam.setLocno(locoNo);
			CmDefcell cmDefcell = cmDefcellManager.findById(defcellParam);
			boolean result = true;

			if (cmDefcell == null) {
				obj.put("msg", "目的储位不存在。");
				result = false;
			} else {
				if (CmDefcellCellStatusEnums.CELLSTATUS_1.getStatus().equals(cmDefcell.getCellStatus())) {
					obj.put("msg", "目的储位已禁用。");
					result = false;
				} else if (CmDefcellCellStatusEnums.CELLSTATUS_2.getStatus().equals(cmDefcell.getCellStatus())) {
					obj.put("msg", "目的储位已冻结。");
					result = false;
				} else if (CmDefcellCheckStatusEnums.CHECKSTATUS_3.getStatus().equals(cmDefcell.getCheckStatus())) {
					obj.put("msg", "目的储位正在盘点中。");
					result = false;
				} else if (BillHmPlanBusinessTypeEnums.BUSINESSTYPE1.getStatus().equals(billHmPlan.getBusinessType())) {
					CmDefarea cmDefareaParam = new CmDefarea();
					cmDefareaParam.setLocno(locoNo);
					cmDefareaParam.setAreaNo(cmDefcell.getAreaNo());
					cmDefareaParam.setWareNo(cmDefcell.getWareNo());
					CmDefarea cmDefarea = cmDefareaManager.findById(cmDefareaParam);
					if (!"0".equals(cmDefarea.getAttributeType())) {
						obj.put("msg", "目的储位属性必须为存储区。");
						result = false;
					}
				}
			}
			if (result) {
				obj.put("result", ResultEnums.SUCCESS.getResultMsg());
			} else {
				obj.put("result", ResultEnums.FAIL.getResultMsg());
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}

		return obj;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> importStorelockDtlExcel(
			List<BillHmPlanDtl> list, AuthorityParams authorityParams,
			Map<String, Object> params) throws ManagerException {
		
		Map<String,Object> mapres  = new  HashMap<String,Object>();
		String locno = params.get("locno").toString();
		String ownerNo = params.get("ownerNo").toString();
		String planno = params.get("planNo").toString();
		try{
		ConContentDto conContentDto = new ConContentDto();
		conContentDto.setLocno(locno);
		conContentDto.setOwnerNo(ownerNo);
		conContentDto.setItemNo("");
		conContentDto.setCellNo("");
		List<ConContentDto>  billConStorelockDtlList =ConContentService.selectConContent(conContentDto, authorityParams);
		StringBuffer strb = new StringBuffer("");
		//数据验证
		for (int i = 0; i < list.size(); i++) {
			BillHmPlanDtl billHmPlanDtl = list.get(i);
			list.get(i).setLocno(locno);
			String scellno = billHmPlanDtl.getsCellNo();
			String dCellNo = billHmPlanDtl.getdCellNo();
			String itemno = billHmPlanDtl.getItemNo();
			String sizeno = billHmPlanDtl.getSizeNo(); 
			
			int itemQty = billHmPlanDtl.getOriginQty().intValue();
			
			//商品编码验证
			Item olditem = new Item();
			olditem.setItemNo(itemno);
			Item newitem = itemService.findById(olditem);
			if(newitem==null){
				strb.append("【"+itemno+"】商品编码不存在.\\r\\n");
			}else{
				list.get(i).setBrandNo(newitem.getBrandNo());
			}
			
			
			//商品尺码验证
			SizeInfo sizeInfo = new SizeInfo();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sizeNo", sizeno);
			List<SizeInfo> listSizeinfo = sizeInfoService.findByBiz(sizeInfo, map);
			if(listSizeinfo.size()==0){
				strb.append("【"+sizeno+"】商品尺码不存在.\\r\\n");
			}
			
			
			//储位编码验证
			CmDefcellKey soldcmDefcellKey = new CmDefcellKey();
			soldcmDefcellKey.setCellNo(scellno);
			soldcmDefcellKey.setLocno(locno);
			CmDefcell snewcmDefcellKey = (CmDefcell) cmDefcellManager.findById(soldcmDefcellKey);
			
			if(snewcmDefcellKey==null){
				strb.append("【"+scellno+"】来源储位不存在.\\r\\n");
			}
			
			//目的储位验证
			CmDefcellKey oldcmDefcellKey = new CmDefcellKey();
			oldcmDefcellKey.setCellNo(dCellNo);
			oldcmDefcellKey.setLocno(locno);
			CmDefcell newcmDefcellKey = (CmDefcell) cmDefcellManager.findById(oldcmDefcellKey);
			
			if(newcmDefcellKey==null){
				strb.append("【"+dCellNo+"】目标储位不存在.\\r\\n");
			}
			Map<String, Object> checkresult = new HashMap<String,Object>();
			try{
				checkresult = checkDtlCell(planno, locno,dCellNo);
			}catch(ManagerException m){
				strb.append(m);
			}
			if(checkresult.get("msg")!=null){
				strb.append(checkresult.get("msg"));
			}
			
			//验证可移库的数量
			int isimport = 0;
			int isdayu = 0;
			for (int j = 0; j < billConStorelockDtlList.size(); j++) {
				ConContentDto bcsd = billConStorelockDtlList.get(j);
				if(bcsd.getCellNo().equals(scellno)&&bcsd.getItemNo().equals(itemno)&&bcsd.getSizeNo().equals(sizeno)){
					isimport++;
					if(itemQty>bcsd.getSumQty()){
						isdayu++;
					}
				}
			}
			if(isimport==0){
				strb.append("【"+scellno+"】储位编码+【"+itemno+"】商品编码+【"+sizeno+"】尺码库存中没有可移库的数量!\\r\\n");
			}
			if(isdayu>0){
				strb.append("【"+scellno+"】储位编码+【"+itemno+"】商品编码+【"+sizeno+"】尺码的移库数量大于计划移库的数量!\\r\\n");
			}
			
		}
		
		if(!"".equals(strb.toString())){
			throw new Exception("<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>"+strb.toString()+"</textarea>");
		}
		
		//保存数据
		BillHmPlanDtl keyObj = new BillHmPlanDtl();
		keyObj.setPlanNo(planno);
		keyObj.setLocno(locno);
		keyObj.setOwnerNo(ownerNo);
		long pidNum = (long) billHmPlanDtlService.selectMaxPid(keyObj);

		for (int i = 0; i < list.size(); i++) {
			
			BillHmPlanDtl b = list.get(i);
			b.setLocno(locno);
			b.setOwnerNo(ownerNo);
			b.setPlanNo(planno);
			b.setMoveDate(new Date());
			b.setRowId(++pidNum);
			int result = billHmPlanDtlService.add(b);
			if (result < 1) {
				throw new ManagerException("插入移库单明细记录时未更新到记录！");
			}
		}
		
		Map<String, Object> findDupParams = new HashMap<String, Object>();
		findDupParams.put("locno", locno);
		findDupParams.put("ownerNo", ownerNo);
		findDupParams.put("planNo", planno);
		List<BillHmPlanDtl> dupList=billHmPlanDtlService.findDuplicateRecord(findDupParams);
		if(dupList.size()>0){
			StringBuilder msg = new StringBuilder();
			for(BillHmPlanDtl dtl : dupList){
				msg.append("<br>储位:").append(dtl.getsCellNo()).append("<br>商品:").append(dtl.getItemNo()).append("<br>尺码:").append(dtl.getSizeNo()).append("<br>");
			}
			msg.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
			throw new ManagerException(msg.toString());
		}
		//插入移库单明细
		mapres.put("result","success");
		mapres.put("msg","导入成功");
	}catch(Exception e){
		mapres.put("result", ResultEnums.FAIL.getResultMsg());
		mapres.put("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
		throw new ManagerException(e.getMessage(), e);
	}
		return mapres;
	}

}