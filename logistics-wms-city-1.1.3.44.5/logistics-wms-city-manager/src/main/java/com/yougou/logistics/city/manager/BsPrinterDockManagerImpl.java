package com.yougou.logistics.city.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BsPrinterDock;
import com.yougou.logistics.city.common.model.BsPrinterDockKey;
import com.yougou.logistics.city.service.BsPrinterDockService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Nov 01 15:18:23 CST 2013
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
@Service("bsPrinterDockManager")
class BsPrinterDockManagerImpl extends BaseCrudManagerImpl implements BsPrinterDockManager {
	@Resource
	private BsPrinterDockService bsPrinterDockService;

	@Override
	public BaseCrudService init() {
		return bsPrinterDockService;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int saveWorkerLoc(String parmStr, String creator, String locno) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(parmStr)) {
			String[] strs = parmStr.split("\\|");
			BsPrinterDock loc = null;
			String dockNo = strs[0];

			//删除该用户所属的所有仓库
			BsPrinterDockKey locKey = new BsPrinterDockKey();
			locKey.setDockNo(dockNo);
			locKey.setLocno(locno);
			try {
				bsPrinterDockService.deleteByDockNo(locKey);
			} catch (Exception e) {
				throw new ManagerException(e);
			}
			if (strs.length > 1) {
				String[] locStrs = strs[1].split(",");
				for (String obj : locStrs) {
					try {
						loc = new BsPrinterDock();
						loc.setDockNo(dockNo);
						loc.setLocno(locno);
						loc.setPrinterGroupNo(obj);
						loc.setCreatetm(new Date());
						loc.setCreator(creator);
						loc.setEditor(creator);
						loc.setEdittm(new Date());
						count += bsPrinterDockService.add(loc);
					} catch (Exception e) {
						throw new ManagerException(e);
					}
				}
			}
		}
		return count;
	}
}