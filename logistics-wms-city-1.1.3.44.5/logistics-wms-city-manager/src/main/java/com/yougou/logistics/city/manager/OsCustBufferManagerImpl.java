package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillUmLabelFullPrint;
import com.yougou.logistics.city.common.model.OsCustBuffer;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.service.OsCustBufferService;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-11-26 14:47:41
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
@Service("osCustBufferManager")
class OsCustBufferManagerImpl extends BaseCrudManagerImpl implements OsCustBufferManager {
    @Resource
    private OsCustBufferService osCustBufferService;

    @Override
    public BaseCrudService init() {
        return osCustBufferService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int delOsCustBuffer(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			OsCustBuffer cust = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");
					cust = new OsCustBuffer();
					cust.setLocno(substr[0]);
					cust.setStoreNo(substr[1]);
					cust.setWareNo(substr[2]);
					cust.setAreaNo(substr[3]);
					cust.setStockNo(substr[4]);
					cust.setCellNo(substr[5]);
					count += osCustBufferService.deleteById(cust);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}

	@Override
	public void insertBatch(OsCustBuffer custBuffer, List<Store> storeList) throws ManagerException {
		try{
			osCustBufferService.insertBatch(custBuffer, storeList);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillUmLabelFullPrint> findBufferBySys(Map<String, Object> params) {
		return osCustBufferService.findBufferBySys(params);
	}

	@Override
	public int findFullPrintCount(Map<String, Object> params) {
		return osCustBufferService.findFullPrintCount(params);
	}

	@Override
	public List<BillUmLabelFullPrint> findFullPrintByPage(SimplePage page,
			Map<String, Object> params) {
		return osCustBufferService.findFullPrintByPage(page, params);
	}
}