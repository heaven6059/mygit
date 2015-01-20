package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.BillUmUntreadStatusEnums;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.BillUmReceiptService;
import com.yougou.logistics.city.service.BillUmUntreadService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Jan 14 20:01:36 CST 2014
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
@Service("billUmUntreadManager")
class BillUmUntreadManagerImpl extends BaseCrudManagerImpl implements BillUmUntreadManager {
	@Resource
	private BillUmUntreadService billUmUntreadService;
	@Resource
	private BillUmReceiptService  billUmReceiptService;

	@Override
	public BaseCrudService init() {
		return billUmUntreadService;
	}

	@Override
	public void saveMain(BillUmUntread untread, SystemUser user) throws ManagerException {
		try {
			billUmUntreadService.saveMain(untread, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void deleteUntread(String keyStr, String locnoNo) throws ManagerException {
		try {
			billUmUntreadService.deleteUntread(keyStr, locnoNo);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	public void auditUntread(String keyStr, SystemUser user) throws ManagerException {
		try {
			billUmUntreadService.auditUntread(keyStr, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillUmUntread> findUntread2CheckTask(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, List<BillUmUntread> list, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billUmUntreadService.findUntread2CheckTask(page, orderByField, orderBy, params, list, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	public Map<String, Object> invalid(BillUmUntread untreadMm,String untReadNoStr,SystemUser user) throws ManagerException{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String [] untReadNos=untReadNoStr.split(",");
			List<BillUmUntread> untreadList=new ArrayList<BillUmUntread>();
			boolean f=true;
			StringBuffer untreadNO=new StringBuffer();
			for(int i=0;i<untReadNos.length;i++){
				String[] arryUntread = untReadNos[i].split(";");
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("untreadNo", arryUntread[0]);
				int count=billUmReceiptService.findCount(params);
				if(count>0){
					if (untreadNO.length()>0) {
						untreadNO.append("," + arryUntread[0]);
					} else {
						untreadNO.append(arryUntread[0]);
					}
					f=false;
					continue;
				}
				BillUmUntread untread = new BillUmUntread();
				untread.setUntreadNo(arryUntread[0]);
				untread.setLocno(untreadMm.getLocno());
				untread.setOwnerNo(arryUntread[1]);
				untread.setStatus(BillUmUntreadStatusEnums.STATUS99.getStatus());
				untreadList.add(untread);
			}
			if (f) {
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("status", BillUmUntreadStatusEnums.STATUS99.getStatus());
				params.put("editorname", user.getUsername());
				params.put("editor", user.getLoginName());
				int modifyCount=billUmUntreadService.batchUpdateUntreadStatus(params, untreadList);
				if (modifyCount < 1) {
					throw new ManagerException("批量作废店退仓单失败!");
				}
				map.put("result", "success");
			}else{
				map.put("result", "fail");
				map.put("msg", untreadNO.toString());
			}
			return map;
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

    @Override
    public Map<String, Object> selectSumQty(Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException{
        try {
            return billUmUntreadService.selectSumQty(params,authorityParams);
        } catch (ServiceException e) {
            throw new ManagerException(e.getMessage());
        }
    }

	@Override
	public int judgeObjIsExist(BillUmUntreadKey billUmUntreadKey, String status) throws ManagerException{
		try {
			return billUmUntreadService.judgeObjIsExist(billUmUntreadKey,status);
		} catch (ServiceException e) {
			 throw new ManagerException(e.getMessage());
		}
	}

}