package com.yougou.logistics.city.manager;

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
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmReceipt;
import com.yougou.logistics.city.common.model.BillUmReceiptDtl;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmReceiptDtlService;
import com.yougou.logistics.city.service.BillUmReceiptService;
import com.yougou.logistics.city.service.BillUmUntreadDtlService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Jan 13 20:08:07 CST 2014
 * @version 1.0.6
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("billUmReceiptDtlManager")
class BillUmReceiptDtlManagerImpl extends BaseCrudManagerImpl implements BillUmReceiptDtlManager {
    @Resource
    private BillUmReceiptDtlService billUmReceiptDtlService;
    
    @Resource
    private BillUmReceiptService billUmReceiptService;
    
    @Resource
    private BillUmUntreadDtlService billUmUntreadDtlService;
    
    @Override
    public BaseCrudService init() {
        return billUmReceiptDtlService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String save(BillUmReceipt billUmReceipt, String boxStr, SystemUser user) throws ManagerException {
		try {
			Date edittm = billUmReceipt.getCreatetm();
			String editor = user.getLoginName();
			String editorName = user.getUsername();
			int rowId = 1;
			int n = 0;
			String locno = billUmReceipt.getLocno();
			String receiptNo = billUmReceipt.getReceiptNo();
			String untreadNo = billUmReceipt.getUntreadNo();
			String ownerNo = billUmReceipt.getOwnerNo();
			//1删除收货单所有明细
			BillUmReceiptDtl billUmReceiptDtl = new BillUmReceiptDtl();
			billUmReceiptDtl.setLocno(locno);
			billUmReceiptDtl.setReceiptNo(receiptNo);
			billUmReceiptDtl.setOwnerNo(ownerNo);
			billUmReceiptDtlService.deleteById(billUmReceiptDtl);
			//2查询店退仓明细
			String [] boxNoArray = null;
			if(StringUtils.isBlank(boxStr)){
				boxNoArray = null;
			}else{
				boxNoArray = boxStr.split("\\|");
			}
			
			List<BillUmUntreadDtl> billUmUntreadDtls = null;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("untreadNo", untreadNo);
			params.put("ownerNo", ownerNo);
			if(boxNoArray != null && boxNoArray.length > 0){
				for(String boxNo:boxNoArray){
					params.put("boxNo", boxNo);
					billUmUntreadDtls = billUmUntreadDtlService.findByBiz(null, params);
					if(billUmUntreadDtls == null || billUmUntreadDtls.size() == 0){
						throw new ManagerException("店退仓明细中不存在为【"+boxNo+"】的箱子");
					}else{
						//3保存收货单明细
						BillUmReceiptDtl dtl = new BillUmReceiptDtl();
						dtl.setLocno(locno);
						dtl.setReceiptNo(receiptNo);
						dtl.setOwnerNo(ownerNo);
						dtl.setStatus("10");
						dtl.setBoxNo(boxNo);
						dtl.setEditor(editor);
						dtl.setEditorName(editorName);
						dtl.setEdittm(edittm);
						for(BillUmUntreadDtl untreadDtl:billUmUntreadDtls){
							untreadDtlToReceiptDtl(untreadDtl, dtl, rowId);
							n = billUmReceiptDtlService.add(dtl);
							if(n < 1){
								throw new ManagerException("收货明细添加箱子【"+boxNo+"】异常");
							}
							rowId++;
						}
					}
				}
			}
			
			//4更细收货单主表编辑信息
			n = billUmReceiptService.modifyById(billUmReceipt);
			if(n < 1){
				throw new ManagerException("更新收货主表编辑信息异常");
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return null;
	}
	private void untreadDtlToReceiptDtl(BillUmUntreadDtl billUmUntreadDtl,BillUmReceiptDtl dtl,int rowId){
		dtl.setRowId(rowId);
		dtl.setItemNo(billUmUntreadDtl.getItemNo());
		dtl.setSizeNo(billUmUntreadDtl.getSizeNo());
		dtl.setItemQty(billUmUntreadDtl.getItemQty());
		dtl.setBrandNo(billUmUntreadDtl.getBrandNo());
	}

	@Override
	public int findCountByBox(Map<String, Object> params) throws ManagerException {
		try {
			return this.billUmReceiptDtlService.findCountByBox(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillUmReceiptDtl> findByPageByBox(SimplePage page, Map<String, Object> params) throws ManagerException {
		try {
			return this.billUmReceiptDtlService.findByPageByBox(page, params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String,Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmReceiptDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}