package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.BillUmReceiptStatusEnums;
import com.yougou.logistics.city.common.enums.BillUmUntreadStatusEnums;
import com.yougou.logistics.city.common.model.BillUmReceipt;
import com.yougou.logistics.city.common.model.BillUmReceiptDtl;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmReceiptDtlService;
import com.yougou.logistics.city.service.BillUmReceiptService;
import com.yougou.logistics.city.service.BillUmUntreadDtlService;
import com.yougou.logistics.city.service.BillUmUntreadService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ProcCommonService;

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
@Service("billUmReceiptManager")
class BillUmReceiptManagerImpl extends BaseCrudManagerImpl implements BillUmReceiptManager {
    @Resource
    private BillUmReceiptService billUmReceiptService;
    @Resource
    private BillUmReceiptDtlService billUmReceiptDtlService;
    @Resource
    private BillUmUntreadDtlService billUmUntreadDtlService;
    @Resource
    private BillUmUntreadService billUmUntreadService;
    @Resource
    private ProcCommonService procCommonService;
    @Resource
    private ConBoxService conBoxService;
    
    private static final String STATUS11 = "11";
    @Override
    public BaseCrudService init() {
        return billUmReceiptService;
    }
    

	@Override
	public SumUtilMap<String, Object> selectUmReceiptSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billUmReceiptService.selectUmReceiptSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String addMain(BillUmReceipt billUmReceipt, SystemUser user) throws ManagerException {
		try {
			Date date = new Date();
			String receiptNo = procCommonService.procGetSheetNo(billUmReceipt.getLocno(), CNumPre.UM_RECEDE_PRE);
			billUmReceipt.setReceiptNo(receiptNo);
			billUmReceipt.setCreatetm(date);
			billUmReceipt.setEdittm(date);
			billUmReceipt.setCreator(user.getLoginName());
			billUmReceipt.setEditor(user.getLoginName());
			billUmReceipt.setCreatorName(user.getUsername());
			billUmReceipt.setEditorName(user.getUsername());
			
			int a = billUmReceiptService.add(billUmReceipt);
			if(a < 1){
				throw new ManagerException("系统异常,请联系管理员!");
			}
			return receiptNo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String deleteReceipt(String deleted) throws ManagerException {
		try {
			String [] receiptArray = deleted.split("\\|");
			String [] receiptObj = null;
			BillUmReceipt billUmReceipt = new BillUmReceipt();
			BillUmReceiptDtl billUmReceiptDtl = new BillUmReceiptDtl();
			int a = 0;
			for(String receiptStr:receiptArray){
				receiptObj = receiptStr.split("_");
				billUmReceipt.setLocno(receiptObj[0]);
				billUmReceipt.setReceiptNo(receiptObj[1]);
				billUmReceipt.setOwnerNo(receiptObj[2]);
				billUmReceipt.setCheckStatus("10");
				a = billUmReceiptService.deleteById(billUmReceipt);
				if(a < 1){
					throw new ManagerException("单据  :"+receiptObj[1]+"已删除或者状态已改变！");
				}
				billUmReceiptDtl.setLocno(receiptObj[0]);
				billUmReceiptDtl.setReceiptNo(receiptObj[1]);
				billUmReceiptDtl.setOwnerNo(receiptObj[2]);
				billUmReceiptDtlService.deleteById(billUmReceiptDtl);
			}
			if(a < 1){
				throw new ManagerException("系统异常,请联系管理员!");
			}
			return null;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String check(BillUmReceipt billUmReceipt, SystemUser user) throws ManagerException {
		try {
			int a = 0;
			Date dd = new Date();
			String editor = user.getLoginName();
			String editorName = user.getUsername();
			String locno = billUmReceipt.getLocno();
			String [] keys = billUmReceipt.getKeys().split("\\|");
			String [] array;
			String receiptNo = null;
			String ownerNo = null;
			String untreadNo = null;
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> _map = new HashMap<String, Object>();
			Map<String,String> boxNoMap=new HashMap<String,String>();
			params.put("locno", locno);
			for(String key:keys){
				array = key.split("_");
				if(array.length != 3){
					throw new ManagerException("缺少参数");
				}
				receiptNo = array[0];
				ownerNo = array[1];
				untreadNo = array[2];
				//1查询收货单所有明细
				params.put("receiptNo", receiptNo);
				params.put("ownerNo", ownerNo);
				_map.putAll(params);
				_map.put("untreadNo", untreadNo);
				_map.put("status", "10");
				int num = billUmReceiptService.findCount(_map);
				if(0 == num){
					throw new ManagerException("单据 :"+receiptNo+"已删除或者状态已改变！");
				}
				List<BillUmReceiptDtl> rdList = billUmReceiptDtlService.findByBiz(null, params);
				if(rdList == null || rdList.size() == 0){
					throw new ManagerException("收货单["+receiptNo+"]不存在明细");
				}else{
					//2更新店退仓单明细收货数量
					BillUmUntreadDtl uuDtl = new BillUmUntreadDtl();
					uuDtl.setLocno(locno);
					uuDtl.setOwnerNo(ownerNo);
					uuDtl.setUntreadNo(untreadNo);
					//uuDtl.setStatus(BillUmUntreadDtlStatusEnums.STATUS13.getStatus());
					
					for(BillUmReceiptDtl rd:rdList){
						uuDtl.setBoxNo(rd.getBoxNo());
						uuDtl.setItemNo(rd.getItemNo());
						uuDtl.setSizeNo(rd.getSizeNo());
						uuDtl.setReceiptQty(rd.getItemQty());
						rd.setEditor(editor);
						rd.setEditorName(editorName);
						rd.setEdittm(dd);
						//左圣巍确认状态无需修改
//						rd.setStatus("13");
						billUmReceiptDtlService.modifyById(rd);
						
						a = billUmUntreadDtlService.modifyByItemAndKey(uuDtl);
						if(a < 1){
							throw new ManagerException("更新收店退仓明细异常");
						}
						String boxNo=rd.getBoxNo();
						if(StringUtils.isNotEmpty(boxNo)&&!boxNo.equals("N")){
							boxNoMap.put(rd.getBoxNo(), rd.getBoxNo());
						}
					}
				}
				
				//3更新店退仓单状态
				params.put("untreadNo", untreadNo);
				List<BillUmUntreadDtl> uudList = billUmUntreadDtlService.findByBiz(null, params);
				BillUmUntread billUmUntread = new BillUmUntread();
				billUmUntread.setLocno(locno);
				billUmUntread.setUntreadNo(untreadNo);
				billUmUntread.setOwnerNo(ownerNo);
				billUmUntread.setAudittm(dd);
				billUmUntread.setSourceStatus(BillUmUntreadStatusEnums.STATUS11.getStatus());
				if(uudList.size() > rdList.size()){//部分收货
					billUmUntread.setStatus(BillUmUntreadStatusEnums.STATUS20.getStatus());
					billUmReceipt.setStatus(BillUmReceiptStatusEnums.STATUS20.getStatus());
				}else{//收货完成
					billUmUntread.setStatus(BillUmUntreadStatusEnums.STATUS25.getStatus());
					billUmReceipt.setStatus(BillUmReceiptStatusEnums.STATUS11.getStatus());
				}
				a = billUmUntreadService.modifyById(billUmUntread);
				if(a < 1){
					throw new ManagerException("此店退仓单["+untreadNo+"]已收货");
				}
				//4更新收货单状态
				billUmReceipt.setSourceStatus(BillUmReceiptStatusEnums.STATUS10.getStatus());
				billUmReceipt.setReceiptNo(receiptNo);
				billUmReceipt.setOwnerNo(ownerNo);
				billUmReceipt.setUntreadNo(untreadNo);
				billUmReceipt.setAudittm(dd);
				billUmReceipt.setAuditorName(user.getUsername());
				a = billUmReceiptService.modifyById(billUmReceipt);
				if(a < 1){
					throw new ManagerException("更新收货单状态异常");
				}
				if(boxNoMap.size()>0){
					Iterator<Entry<String, String>> entryKeyIterator = boxNoMap.entrySet().iterator();
					while (entryKeyIterator.hasNext()) {
						Entry<String, String> e = entryKeyIterator.next();
						String value = e.getValue();
						ConBox conBox=new ConBox();
						conBox.setOwnerNo(ownerNo);
						conBox.setLocno(locno);
						conBox.setBoxNo(value);
						conBox.setStatus("1");
						conBoxService.modifyById(conBox);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage(),e);
		}
		
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String batchCreate(String locnoStr, String untreadNoStr,
			String ownerNoStr, SystemUser user) throws ManagerException {
		String regex = "\\|";
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String [] locnoArray = locnoStr.split(regex);
			String [] untreadNoArray = untreadNoStr.split(regex);
			String [] ownerNoArray = ownerNoStr.split(regex);
			int len = locnoArray.length;
			if(len != untreadNoArray.length || len != ownerNoArray.length){
				throw new ManagerException("数据格式错误,批量生成失败!");
			}
			BillUmReceipt billUmReceipt = null;
			BillUmUntread billUmUntread = new BillUmUntread();
			String receiptNo = null;
			String locno = null;
			String untreadNo = null;
			String ownerNo = null;
			List<BillUmUntreadDtl> billUmUntreadDtls = null;
			int rowId = 1, n = 0;
			BillUmReceiptDtl dtl = null;
			for(int idx=0;idx<len;idx++){
				locno = locnoArray[idx];
				untreadNo = untreadNoArray[idx];
				ownerNo = ownerNoArray[idx];
				params.put("locno", locno);
  				params.put("untreadNo", untreadNo);
  				params.put("ownerNo", ownerNo);
  				int num = billUmReceiptService.findCount(params);
  				//1校验店退仓单是否已经生成退仓收货单
  				if(num > 0){
  					throw new ManagerException("店退仓单【"+untreadNo+"】已经生成退仓收货单,请重新选择!");
  				}
  				//2生成退仓收货单主信息
  				billUmUntread.setLocno(locno);
  				billUmUntread.setUntreadNo(untreadNo);
  				billUmUntread.setOwnerNo(ownerNo);
  				billUmUntread = billUmUntreadService.findById(billUmUntread);
  				if(null ==billUmUntread ||!STATUS11.equals(billUmUntread.getStatus())){
  					throw new ManagerException("单据 :" + billUmUntread.getUntreadNo() +"已删除或者状态已改变");
  				}
  				//2.1组装退仓收货单主信息
  				billUmReceipt = new BillUmReceipt();
  				billUmReceipt.setLocno(locno);
  				billUmReceipt.setOwnerNo(ownerNo);
  				billUmReceipt.setUntreadNo(untreadNo);
  				billUmReceipt.setUntreadMmNo(billUmUntread.getUntreadMmNo());
  				billUmReceipt.setStatus(BillUmReceiptStatusEnums.STATUS10.getStatus());
  				billUmReceipt.setItemType("0");
  				billUmReceipt.setRemark(billUmUntread.getRemark());
  				billUmReceipt.setQuality(billUmUntread.getQuality());
  				billUmReceipt.setStoreNo(billUmUntread.getStoreNo());
  				
  				receiptNo = addMain(billUmReceipt, user);
  				if(StringUtils.isBlank(receiptNo)){
  					throw new ManagerException("由店退仓单【"+untreadNo+"】生成退仓收货单主信息异常!");
  				}
  				//3生成退仓收货单明细
  				rowId = 1;
  				billUmUntreadDtls = billUmUntreadDtlService.findByBiz(null, params);
  				dtl = new BillUmReceiptDtl();
				dtl.setLocno(locno);
				dtl.setReceiptNo(receiptNo);
				dtl.setOwnerNo(ownerNo);
				dtl.setStatus("10");
  				for(BillUmUntreadDtl buud:billUmUntreadDtls){
					dtl.setBoxNo(buud.getBoxNo());
					dtl.setRowId(rowId);
					dtl.setItemNo(buud.getItemNo());
					dtl.setSizeNo(buud.getSizeNo());
					dtl.setItemQty(buud.getItemQty());
					dtl.setBrandNo(buud.getBrandNo());
					n = billUmReceiptDtlService.add(dtl);
					if(n < 1){
						throw new ManagerException("由店退仓单【"+untreadNo+"】生成退仓收货单明细异常");
					}
  					rowId++;
  				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}
}