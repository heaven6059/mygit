package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.dal.database.BmContainerMapper;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 容器基本信息
 * @author wanghb
 * @date   2014-7-30
 * @version 1.1.3.36
 * @copyright yougou.com
 */
@Service("bmContainerService")
class BmContainerServiceImpl extends BaseCrudServiceImpl implements BmContainerService {
    @Resource
    private BmContainerMapper bmContainerMapper;

    @Override
    public BaseCrudMapper init() {
        return bmContainerMapper;
    }

	public int batchUpdate(List<BmContainer> bmContainer) throws ServiceException {
		try {
			bmContainerMapper.batchUpdate(bmContainer);
    		return 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	public int batchStatusByBillConAdj(Map<String,Object> map)throws ServiceException{
		try {
			bmContainerMapper.batchStatusByBillConAdj(map);
    		return 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	public int batchConBoxStatusByBillConAdj(Map<String,Object> map)throws ServiceException{
		try {
			bmContainerMapper.batchConBoxStatusByBillConAdj(map);
    		return 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public boolean checkBmContainerStatus(BmContainer bmContainer) throws ServiceException {
		boolean f = false;
		try {
			BmContainer tempBM = this.findById(bmContainer);
			if (null != tempBM) {// 判断是否有记录
				if (null == bmContainer.getType()||tempBM.getType().equals(bmContainer.getType())) {
					if (tempBM.getStatus().equals("1")) {// 被占用
						if (!tempBM.getOptBillNo().equals(bmContainer.getOptBillNo())) {
							f = true;
						}
					}
				} else {
					f = true;
				}
			} else {
				f = true;
			}
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
		return f;
	}

}