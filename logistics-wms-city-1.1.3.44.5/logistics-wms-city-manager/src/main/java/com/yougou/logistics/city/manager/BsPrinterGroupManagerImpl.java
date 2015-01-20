package com.yougou.logistics.city.manager;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BsPrinterGroup;
import com.yougou.logistics.city.service.BsPrinterGroupService;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Nov 04 10:16:07 CST 2013
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
@Service("bsPrinterGroupManager")
class BsPrinterGroupManagerImpl extends BaseCrudManagerImpl implements BsPrinterGroupManager {
    @Resource
    private BsPrinterGroupService bsPrinterGroupService;

    @Override
    public BaseCrudService init() {
        return bsPrinterGroupService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public void savePrinterGroup(String printerGroupNo, String printerNoStr, String loginName, String locno) throws ManagerException {
		if(StringUtils.isNotBlank(printerGroupNo)){
			BsPrinterGroup pg = new BsPrinterGroup();
			pg.setLocno(locno);
			pg.setPrinterGroupNo(printerGroupNo);
			try {
				bsPrinterGroupService.deleteById(pg);//首先删除原来的对应关系
				if(StringUtils.isNotBlank(printerNoStr)){//建立新的对应关系
					String[] printerNoArr = printerNoStr.split(",");
					for(String printerNo : printerNoArr){
						BsPrinterGroup bsPrinterGroup = new BsPrinterGroup();
						bsPrinterGroup.setLocno(locno);
						bsPrinterGroup.setPrinterGroupNo(printerGroupNo);
						bsPrinterGroup.setPrinterNo(printerNo);
						bsPrinterGroup.setCreator(loginName);
						bsPrinterGroup.setCreatetm(new Date());
						bsPrinterGroupService.add(bsPrinterGroup);
					}
				}
				
			} catch (ServiceException e) {
				throw new ManagerException(e);
			}
		}
		
	}
}