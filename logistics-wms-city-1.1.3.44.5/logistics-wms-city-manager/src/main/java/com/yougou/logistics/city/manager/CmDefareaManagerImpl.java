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
import com.yougou.logistics.city.common.model.CmDefareaKey;
import com.yougou.logistics.city.common.model.CmDefstock;
import com.yougou.logistics.city.common.model.CsInstockSettingdtl2;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.CmDefareaService;
import com.yougou.logistics.city.service.CmDefstockService;
import com.yougou.logistics.city.service.CsInstockSettingdtl2Service;

/**
 * 
 * 库区manager实现
 * 
 * @author qin.dy
 * @date 2013-9-26 上午10:07:43
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefareaManager")
class CmDefareaManagerImpl extends BaseCrudManagerImpl implements CmDefareaManager {
	
	@Resource
    private CmDefstockService cmDefstockService;
	
    @Resource
    private CmDefareaService cmDefareaService;
    
    @Resource
    private CsInstockSettingdtl2Service csInstockSettingdtl2Service;

    @Override
    public BaseCrudService init() {
        return cmDefareaService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> deleteBatch(String locno,List<CmDefarea> listCmDefareas) throws ManagerException {
		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";
		String areaNameStr = "";
		String settingNo = "";
		
		try{
			if(CommonUtil.hasValue(listCmDefareas)){
				for (CmDefarea c : listCmDefareas) {
					Map<String,Object> mapParams = new HashMap<String, Object>();
					mapParams.put("locno",locno);
					mapParams.put("wareNo",c.getWareNo());
					mapParams.put("areaNo",c.getAreaNo());
					
					CmDefstock cmDefstock = null;
					List<CmDefstock> listCs = cmDefstockService.findByBiz(null, mapParams);
					
					if(CommonUtil.hasValue(listCs)){
						cmDefstock = listCs.get(0);
					}
					
					if(cmDefstock!=null){
						areaNameStr = cmDefstock.getAreaName();
						break;
					}
					//验证是否被上架策略引用
					mapParams.put("cellNo", c.getWareNo()+c.getAreaNo());
					List<CsInstockSettingdtl2> settingList = csInstockSettingdtl2Service.findByBiz(null, mapParams);
					CsInstockSettingdtl2 settingdtl2 = null;
					if(CommonUtil.hasValue(settingList)){
						settingdtl2 = settingList.get(0);
						settingNo = settingdtl2.getSettingNo();
						break;
					}
				}
				
				if(StringUtils.isNotBlank(areaNameStr)){
					flag = "warn";
					msg = areaNameStr;
		   			mapObj.put("flag", flag);
					mapObj.put("msg", msg);
					return mapObj;
				}
				if(StringUtils.isNotBlank(settingNo)){
					flag = "hasSettingNo";
					msg = areaNameStr;
		   			mapObj.put("flag", flag);
					mapObj.put("msg", msg);
					mapObj.put("settingNo", settingNo);
					return mapObj;
				}
				
				for (CmDefarea c : listCmDefareas) {
					CmDefareaKey key = new CmDefareaKey();
					key.setLocno(locno);
					key.setWareNo(c.getWareNo());
					key.setAreaNo(c.getAreaNo());
					int count = cmDefareaService.deleteById(key);
					if(count < 1){
						 throw new ManagerException("删除失败");
					}
				}
			}
			
			flag = "success";
			msg = "删除成功!";
   			mapObj.put("flag", flag);
			mapObj.put("msg", msg);
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		
		return mapObj;
		
//		if(StringUtils.isNotBlank(ids)){
//			String[] idArr = ids.split(",");
//			for(String id : idArr){
//				String[] temp = id.split("-");
//				if(temp.length==3){
//					CmDefareaKey key = new CmDefareaKey();
//					key.setLocno(temp[0]);
//					key.setWareNo(temp[1]);
//					key.setAreaNo(temp[2]);
//					try {
//						cmDefareaService.deleteById(key);
//					} catch (ServiceException e) {
//						return 0;
//					}
//				}
//				
//				
//			}
//		}
		
		
		
	}

	@Override
	public List<CmDefarea> findByStoreroom(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return cmDefareaService.findByStoreroom(params);
	}
}