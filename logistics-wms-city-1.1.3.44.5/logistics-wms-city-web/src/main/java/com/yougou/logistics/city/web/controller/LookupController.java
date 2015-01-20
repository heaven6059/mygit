package com.yougou.logistics.city.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.Lookup;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.manager.LookupManager;
import com.yougou.logistics.city.manager.common.CommonUtilManager;

@Controller
@RequestMapping("/lookup")
@ModuleVerify("25030140")
public class LookupController extends BaseCrudController<Lookup> {
	@Log
	private Logger log;
	@Resource
	private LookupManager lookupManager;

	@Resource
	private CommonUtilManager commonUtilManager;
	
	@Override
	public CrudInfo init() {
		return new CrudInfo("lookup/", lookupManager);
	}

	@RequestMapping(value = "/addLookup")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public void addLookup(Lookup lookup, HttpServletRequest request) throws ManagerException {
		try {
			lookup.setSystemid((short) 22);
			SystemUser user = (SystemUser) request.getSession().getAttribute(PublicContains.SESSION_USER);
			lookup.setCreator(user.getUsername());
			this.add(lookup);
			LookupDtl lookupDtl=new LookupDtl();
			List<LookupDtl> listDtls=commonUtilManager.queryLookupDtlsList(lookupDtl);
			InitCacheController.reLoadMap(listDtls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/findlookupByCode")
	@ResponseBody
	public ResponseEntity<Lookup> findlookupByCode(String lookuptype, String systemid, String lookupcode)
			throws ManagerException {
		Lookup l = new Lookup();
		try {
			l.setLookupcode(lookupcode);
			l.setSystemid(Short.valueOf(systemid));
			l.setLookuptype(lookuptype);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return this.get(l);

	}

	@RequestMapping(value = "/checkItemValue")
	@ResponseBody
	public int checkItemValue(String itemval, String lookupcode, int systemid) throws ManagerException {
		int item = 0;
		try {
			item = lookupManager.checkItemValue(itemval, lookupcode, systemid);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return item;
	}

	@RequestMapping(value = "/deleteLookupCode")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public void deleteLookupCode(String lookupcode) throws ManagerException {
		try {
			lookupManager.deletelookup(lookupcode);
			LookupDtl lookupDtl=new LookupDtl();
			List<LookupDtl> listDtls=commonUtilManager.queryLookupDtlsList(lookupDtl);
			InitCacheController.reLoadMap(listDtls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/checkLookupCode")
	@ResponseBody
	public int checkLookupCode(String lookupcode) throws ManagerException {
		int item = 0;
		try {
			item = lookupManager.checkLookuoCode(lookupcode);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return item;
	}
	
	/**
	 * 修改码表主表
	 * @param lookup
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value="/update")
	//@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public ResponseEntity<Lookup> updateLookup(Lookup lookup) throws ManagerException{
		lookup.setSystemid((short)22);
		ResponseEntity<Lookup> up =  this.moditfy(lookup);
		try {
			LookupDtl l = new LookupDtl();
			List<LookupDtl> listDtls=commonUtilManager.queryLookupDtlsList(l);
			InitCacheController.reLoadMap(listDtls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return up;
	}
}