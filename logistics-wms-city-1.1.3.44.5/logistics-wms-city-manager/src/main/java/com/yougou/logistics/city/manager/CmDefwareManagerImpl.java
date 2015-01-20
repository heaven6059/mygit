package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefware;
import com.yougou.logistics.city.common.model.CmDefwareKey;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.CmDefareaService;
import com.yougou.logistics.city.service.CmDefwareService;

/**
 * 仓区manager实现
 * TODO: 增加描述
 * 
 * @author qin.dy
 * @date 2013-9-25 下午3:39:43
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefwareManager")
class CmDefwareManagerImpl extends BaseCrudManagerImpl implements CmDefwareManager {
	
    @Resource
    private CmDefwareService cmDefwareService;
    
    @Resource
    private CmDefareaService cmDefareaService;

    @Override
    public BaseCrudService init() {
        return cmDefwareService;
    }
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> deleteBatch(String locno,List<CmDefware> listCmDefwares) throws ManagerException {
    	
    	Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";
		
		if(CommonUtil.hasValue(listCmDefwares)){
   			try {
   				Map<String, Object> map = new HashMap<String, Object>();
   	   			map.put("locno", locno);
				List<CmDefarea> listCmDefareas=cmDefareaService.findCmDefareaIsHaveByWareNo(map, listCmDefwares);
				if(CommonUtil.hasValue(listCmDefareas)){
					String wareNoStr = "";
					for (CmDefarea c : listCmDefareas) {
						wareNoStr += c.getWareNo()+",";
					}
					if(StringUtils.isNotBlank(wareNoStr)){
						wareNoStr = wareNoStr.substring(0, wareNoStr.length()-1);
					}
					
					flag = "warn";
					msg = wareNoStr;
		   			mapObj.put("flag", flag);
					mapObj.put("msg", msg);
					return mapObj;
				}
				
				for (CmDefware c : listCmDefwares) {
					CmDefwareKey key = new CmDefwareKey();
					key.setLocno(locno);
					key.setWareNo(c.getWareNo());
					try{
						int a = cmDefwareService.deleteById(key);
						if(a < 1){
							 throw new ManagerException("删除失败");
						}
					}catch (Exception e) {
			            throw new ManagerException("删除失败");
			        }
				}
				
				flag = "success";
				msg = "删除成功!";
	   			mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				
			} catch (ServiceException e) {
				throw new ManagerException(e);
			}
		}
		
		return mapObj;
	}
    
    
}