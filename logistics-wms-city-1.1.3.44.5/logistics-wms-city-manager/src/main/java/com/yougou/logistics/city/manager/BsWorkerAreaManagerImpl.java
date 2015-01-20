package com.yougou.logistics.city.manager;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BsWorkerArea;
import com.yougou.logistics.city.service.BsWorkerAreaService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Wed Sep 25 14:31:21 CST 2013
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
@Service("bsWorkerAreaManager")
class BsWorkerAreaManagerImpl extends BaseCrudManagerImpl implements BsWorkerAreaManager {
	@Resource
	private BsWorkerAreaService bsWorkerAreaService;

	@Override
	public BaseCrudService init() {
		return bsWorkerAreaService;
	}

	@Override
	public int deleteBsWorkerAreaBatch(String keyStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] strs = keyStr.split(",");
			BsWorkerArea area = null;
			for (String obj : strs) {
				try {
					String[] substr = obj.split("\\|");

					area = new BsWorkerArea();
					//仓别
					area.setLocno(substr[0]);
					//员工编号
					area.setWorkerNo(substr[1]);
					//操作类型
					area.setOperateType(substr[2]);
					//仓区
					area.setWareNo(substr[3]);
					//库区
					area.setAreaNo(substr[4]);
					//通道
					area.setStockNo(substr[5]);
					count += bsWorkerAreaService.deleteById(area);
				} catch (Exception e) {
					throw new ManagerException(e);
				}
			}
		}
		return count;
	}
}