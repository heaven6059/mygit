package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BmDefprinterKey;
import com.yougou.logistics.city.service.BmDefprinterService;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 打印机manger实现
 * 
 * @author qin.dy
 * @date 2013-11-1 下午2:37:10
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("bmDefprinterManager")
class BmDefprinterManagerImpl extends BaseCrudManagerImpl implements BmDefprinterManager {
    @Resource
    private BmDefprinterService bmDefprinterService;

    @Override
    public BaseCrudService init() {
        return bmDefprinterService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public int deleteBatch(String ids) {
		
		if(StringUtils.isNotBlank(ids)){
			String[] idArr = ids.split(",");
			for(String id : idArr){
				String[] tmp = id.split("-");
				if(tmp.length==2){
					BmDefprinterKey key = new BmDefprinterKey();
					key.setLocno(tmp[0]);
					key.setPrinterNo(tmp[1]);
					try {
						bmDefprinterService.deleteById(key);
					} catch (ServiceException e) {
						return 0;
					}
				}
				
			}
		}
		return 1;
	}
}