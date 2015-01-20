package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImDifRecord;
import com.yougou.logistics.city.common.model.BillImDifRecordDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillImDifRecordDtlService;
import com.yougou.logistics.city.service.BillImDifRecordService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
@Service("billImDifRecordDtlManager")
class BillImDifRecordDtlManagerImpl extends BaseCrudManagerImpl implements BillImDifRecordDtlManager {
    @Resource
    private BillImDifRecordDtlService billImDifRecordDtlService;
    @Resource
    private BillImDifRecordService billImDifRecordService;

    @Override
    public BaseCrudService init() {
        return billImDifRecordDtlService;
    }

    @Override
	public int selectContentCount(Map<String, Object> params)
			throws ManagerException {
		try {
			return this.billImDifRecordDtlService.selectContentCount(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillImDifRecordDtl> selectContent(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params)
			throws ManagerException {
		try {
			return billImDifRecordDtlService.selectContent(page, orderByField, orderBy, params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addDtl(BillImDifRecord billImDifRecord,
			Map<CommonOperatorEnum, List<ModelType>> params) throws ManagerException {
		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		boolean flag = false;
		try {
			/****************修改退仓收货单主档信息*******************/
			String remark = billImDifRecord.getImportRemark();
			String locno = billImDifRecord.getLocno();
			String ownerNo = billImDifRecord.getOwnerNo();
			String defRecordNo = billImDifRecord.getDefRecordNo();
			
			Map<String, Object> bill = new HashMap<String, Object>();
			bill.put("locno", locno);
			bill.put("ownerNo", ownerNo);
			bill.put("defRecordNo", defRecordNo);
			bill.put("status", "10");
			List<BillImDifRecord> param = billImDifRecordService.findByBiz(billImDifRecord, bill);
			if(param.size() > 0) {
				bill.put("remark", remark);
				bill.put("editor", billImDifRecord.getEditor());
				bill.put("edittm", new Date());
				billImDifRecordService.modifyById(bill);
			} else {
				throw new ManagerException("当前单据状态不可编辑！");
			}
			/****************新增退仓收货单明细信息*******************/
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);//新增
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);//删除
			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);//更新
			
			//删除操作
			if (CommonUtil.hasValue(delList)) {
				flag = false;
				for(ModelType modelType : delList){
					if(modelType instanceof BillImDifRecordDtl){
						BillImDifRecordDtl vo = (BillImDifRecordDtl) modelType;
						String defRecordNoDtl = defRecordNo;
						String locnoDtl = locno;
						String ownerNoDtl = ownerNo;
						String itemNoDtl = vo.getItemNo();
						String sizeNoDtl = vo.getSizeNo();
						String barcodeDtl = vo.getBarcode();
						
						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("locno", locnoDtl);
						queryParam.put("ownerNo", ownerNoDtl);
						queryParam.put("defRecordNo", defRecordNoDtl);
						queryParam.put("itemNo", itemNoDtl);
						queryParam.put("sizeNo", sizeNoDtl);
						queryParam.put("barcode", barcodeDtl);
						
						List<BillImDifRecordDtl> query = billImDifRecordDtlService.findByBiz(vo,queryParam);
						if(query.size()==1) {
							Short rowId = query.get(0).getPoId();
							BillImDifRecordDtl delParamerKey = new BillImDifRecordDtl();
							delParamerKey.setLocno(locnoDtl);
							delParamerKey.setOwnerNo(ownerNoDtl);
							delParamerKey.setDefRecordNo(defRecordNoDtl);
							delParamerKey.setPoId(rowId);
							int a = billImDifRecordDtlService.deleteById(delParamerKey);
							if(a < 1){
								throw new ManagerException("删除收货差异信息失败！");
							}
						} else {
							throw new ManagerException("找不到可删除的收货差异信息！");
						}
					}
				}
				flag = true;
			}
			//新增操作
			if (CommonUtil.hasValue(addList)) {
				flag = false;
				//查询最大的Pid,作为主键 
				BillImDifRecordDtl keyObj = new BillImDifRecordDtl();
				keyObj.setDefRecordNo(defRecordNo);
				keyObj.setLocno(locno);
				keyObj.setOwnerNo(ownerNo);
				short pidNum = (short) billImDifRecordDtlService.selectMaxPid(keyObj);

				for (ModelType modelType : addList) {
					if (modelType instanceof BillImDifRecordDtl) {
						BillImDifRecordDtl vo = (BillImDifRecordDtl) modelType;
						String defRecordNoDtl = defRecordNo;
						String locnoDtl = locno;
						String ownerNoDtl = ownerNo;
						String itemNoDtl = vo.getItemNo();
						String sizeNoDtl = vo.getSizeNo();
						String barcodeDtl = vo.getBarcode();
						BigDecimal qtyDtl = vo.getQty();
						String status = "10";
						
						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("locno", locnoDtl);
						queryParam.put("ownerNo", ownerNoDtl);
						queryParam.put("defRecordNo", defRecordNoDtl);
						queryParam.put("itemNo", itemNoDtl);
						queryParam.put("sizeNo", sizeNoDtl);
						queryParam.put("barcode", barcodeDtl);
						
						List<BillImDifRecordDtl> query = billImDifRecordDtlService.findByBiz(vo,queryParam);
						if(query.size() > 0) {
							for(BillImDifRecordDtl sm : query) {
								String no = sm.getDefRecordNo();
								throw new ManagerException("表单：" + no + "中存在商品尺码重复，请核对！");
							}
						}
						vo.setLocno(locnoDtl);
						vo.setOwnerNo(ownerNoDtl);
						vo.setDefRecordNo(defRecordNoDtl);
						vo.setPoId(++pidNum);
						vo.setStatus(status);
						vo.setItemNo(itemNoDtl);
						vo.setSizeNo(sizeNoDtl);
						vo.setBarcode(barcodeDtl);
						vo.setQty(qtyDtl);
						int a = 0;
						try {
							a = billImDifRecordDtlService.add(vo);
						} catch (Exception e) {
							throw new ManagerException("添加收货差异明细失败！");
						}
						if (a < 1) {
							throw new ManagerException("添加收货差异信息失败！");
						}
					}
				}
				flag = true;
			}
			//更新操作
			if (CommonUtil.hasValue(uptList)) {
				flag = false;
				for(ModelType modelType : uptList){
					if(modelType instanceof BillImDifRecordDtl){
						BillImDifRecordDtl vo = (BillImDifRecordDtl) modelType;
						String defRecordNoDtl = defRecordNo;
						String locnoDtl = locno;
						String ownerNoDtl = ownerNo;
						String itemNoDtl = vo.getItemNo();
						String sizeNoDtl = vo.getSizeNo();
						String barcodeDtl = vo.getBarcode();
						BigDecimal qtyDtl = vo.getQty();
						
						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("locno", locnoDtl);
						queryParam.put("ownerNo", ownerNoDtl);
						queryParam.put("defRecordNo", defRecordNoDtl);
						queryParam.put("itemNo", itemNoDtl);
						queryParam.put("sizeNo", sizeNoDtl);
						queryParam.put("barcode", barcodeDtl);
						
						List<BillImDifRecordDtl> query = billImDifRecordDtlService.findByBiz(vo,queryParam);
						if(query.size()==1) {
							Short rowId = query.get(0).getPoId();
							BillImDifRecordDtl delParamerKey = new BillImDifRecordDtl();
							delParamerKey.setLocno(locnoDtl);
							delParamerKey.setOwnerNo(ownerNoDtl);
							delParamerKey.setDefRecordNo(defRecordNoDtl);
							delParamerKey.setPoId(rowId);
							delParamerKey.setQty(qtyDtl);
							int a = billImDifRecordDtlService.modifyById(delParamerKey);
							if(a < 1){
								throw new ManagerException("更新收货差异信息失败！");
							}
						} else {
							throw new ManagerException("找不到可更新的收货差异信息！");
						}
					}
				}
				flag = true;
			}
			if(flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "保存成功");
			} else {
				throw new ManagerException("当前单据保存失败！");
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

}