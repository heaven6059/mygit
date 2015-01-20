package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.model.BillWmRecedeDtlKey;
import com.yougou.logistics.city.common.model.BillWmRecedeKey;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.service.BillWmRecedeDtlService;
import com.yougou.logistics.city.service.BillWmRecedeService;
import com.yougou.logistics.city.service.ProcCommonService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
@Service("billWmRecedeManager")
class BillWmRecedeManagerImpl extends BaseCrudManagerImpl implements BillWmRecedeManager {
    @Resource
    private BillWmRecedeService billWmRecedeService;
    
    @Resource
    private BillWmRecedeDtlService billWmRecedeDtlService;
    
	@Resource
	private ProcCommonService procCommonService;

    @Override
    public BaseCrudService init() {
        return billWmRecedeService;
    }

	@Override
	public Map<String, Object> auditWmrecede(String nosStr,String locno, String loginName,String userName) throws ManagerException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		StringBuilder strBuilder = new StringBuilder();
		String flag = "false";
		String resultMsg = "";
		try {
				if (StringUtils.isNotBlank(nosStr)){
					String[] strs = nosStr.split(",");
					for(String obj : strs){
						String[] wmrecedeArray = obj.split("#");
						if(wmrecedeArray.length > 1){
							String ownerNo = wmrecedeArray[0];
							String recedeNo = wmrecedeArray[1];
							
							BillWmRecedeDtlKey billWmRecedeDtlKey = new BillWmRecedeDtlKey();
							billWmRecedeDtlKey.setLocno(locno);
							billWmRecedeDtlKey.setOwnerNo(ownerNo);
							billWmRecedeDtlKey.setRecedeNo(recedeNo);
							int b = billWmRecedeDtlService.selectMaxPid(billWmRecedeDtlKey);
							if(b < 1 ){
								strBuilder.append(recedeNo+"，");
							}
						}
					}
					//如果有值
					if(strBuilder.length() > 0 ){
						strBuilder.insert(0, "单号：");
						strBuilder.append("没有添加明细，无法审核！");
						flag = "warn";
						resultMsg = strBuilder.toString();
					}else{
						for(String obj : strs){
							String[] wmrecedeArray = obj.split("#");
							String ownerNo = wmrecedeArray[0];
							String recedeNo = wmrecedeArray[1];
							
							BillWmRecede billWmRecede = new  BillWmRecede();
							billWmRecede.setRecedeNo(recedeNo);
							billWmRecede.setLocno(locno);
							billWmRecede.setOwnerNo(ownerNo);
							billWmRecede.setStatus("11");//状态11-建单；10-审核；
							billWmRecede.setAudittm(new Date());
							billWmRecede.setAuditor(loginName);
							billWmRecede.setAuditorName(userName);
							int a = billWmRecedeService.modifyById(billWmRecede);
							 if(a < 1){
								 throw new ManagerException("审核单号【"+recedeNo+"】时未更新到记录！");
							 }
						}
						flag = "true";
					}
				}
		}catch (Exception e) {
            throw new ManagerException(e);
        }
		objMap.put("flag", flag);
		objMap.put("resultMsg", resultMsg);
		return objMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public boolean deleteWmrecede(String idStr) throws ManagerException {
		try{
			if(StringUtils.isNotBlank(idStr)){
				String [] strs = idStr.split(",");
				for(String obj : strs){
					String [] imImportArray = obj.split("#");
						if(imImportArray.length>2){
							String recedeNo = imImportArray[0];
							String locno = imImportArray[1];
							String ownerNo = imImportArray[2];
							
							//删除明细
							BillWmRecedeDtlKey billWmRecedeDtlKey = new  BillWmRecedeDtlKey();
							billWmRecedeDtlKey.setRecedeNo(recedeNo);
							billWmRecedeDtlKey.setLocno(locno);
							billWmRecedeDtlKey.setOwnerNo(ownerNo);
							billWmRecedeDtlService.deleteById(billWmRecedeDtlKey);
							
							//删除主表
							BillWmRecedeKey billWmRecedeKey = new  BillWmRecedeKey();
							billWmRecedeKey.setRecedeNo(recedeNo);
							billWmRecedeKey.setLocno(locno);
							billWmRecedeKey.setOwnerNo(ownerNo);
							int  a  = billWmRecedeService.deleteById(billWmRecedeKey);
							if(a  < 1){
								throw new ManagerException("未删除到对应的信息！");
							}
						}
				}
			}
		}catch (Exception e) {
            throw new ManagerException(e);
        }
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> addWmRecede(BillWmRecede billWmRecede)throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try{
			//设置创建时间
		    billWmRecede.setCreatetm(new Date());
		    billWmRecede.setEdittm(new Date());
		    billWmRecede.setStatus("10");//状态11-建单；10-审核；13-结案
			//自定生成单号
			String recedeNo = procCommonService.procGetSheetNo(billWmRecede.getLocno(), CNumPre.WM_RECEDE_PRE);
			billWmRecede.setRecedeNo(recedeNo);
			int a = billWmRecedeService.add(billWmRecede);
			if (a < 1) {
				throw new ManagerException("新增时未更新到记录！");
			}
			obj.put("returnMsg", true);
			obj.put("recedeNo", recedeNo);
			obj.put("locno", billWmRecede.getLocno());
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return obj;
	}

	@Override
	public void procBillWmRecedeLocateQuery(List<BillWmOutstockDirect> listWmRecedes) throws ManagerException {
		try{
			billWmRecedeService.procBillWmRecedeLocateQuery(listWmRecedes);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findBillWmRecedeJoinDirectCount(BillWmRecede billWmRecede, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billWmRecedeService.findBillWmRecedeJoinDirectCount(billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillWmRecede> findBillWmRecedeJoinDirectByPage(SimplePage page, String orderByField, String orderBy,
			BillWmRecede billWmRecede, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecedeService.findBillWmRecedeJoinDirectByPage(page, orderByField, orderBy, billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findBillWmRecedeGroupCount(BillWmRecede billWmRecede, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billWmRecedeService.findBillWmRecedeGroupCount(billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillWmRecede> findBillWmRecedeGroupByPage(SimplePage page, String orderByField, String orderBy,
			BillWmRecede billWmRecede, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecedeService.findBillWmRecedeGroupByPage(page, orderByField, orderBy, billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
}