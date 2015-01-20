package com.yougou.logistics.city.manager;

import java.util.Date;
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
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefstock;
import com.yougou.logistics.city.common.model.CmDefstockKey;
import com.yougou.logistics.city.common.model.CsInstockSettingdtl2;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.CmDefstockService;
import com.yougou.logistics.city.service.CsInstockSettingdtl2Service;

/**
 * 
 * 通道manager实现
 * 
 * @author qin.dy
 * @date 2013-9-26 下午4:02:04
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefstockManager")
class CmDefstockManagerImpl extends BaseCrudManagerImpl implements CmDefstockManager {
    @Resource
    private CmDefstockService cmDefstockService;
    
    @Resource
    private CmDefcellService cmDefcellService;

    @Resource
    private CsInstockSettingdtl2Service csInstockSettingdtl2Service;
    
    @Override
    public BaseCrudService init() {
        return cmDefstockService;
    }
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public  int addCascade(CmDefstock cmDefstock) throws ManagerException {
		try {
			if(cmDefstock.getStockNo() == null){
				throw new ManagerException("通道编码不能为空 ");
			} else if(cmDefstock.getStockNo().length() > 5) {
				throw new ManagerException("通道编码不能超过 5位");
			}
			if(cmDefstock.getqStockX() == null){
				throw new ManagerException("通道格数不能为空");
			}
			if(cmDefstock.getqStockY() == null) {
				throw new ManagerException("通道层数不能为空 ");
			}
			if(cmDefstock.getqBayX() == null) {
				throw new ManagerException("储格位数不能为空 ");
			}
			Date dd = new Date();
			CmDefcell cell = new CmDefcell();
			cell.setaFlag(cmDefstock.getaFlag());
			cell.setAreaNo(cmDefstock.getAreaNo());
			cell.setbPick(cmDefstock.getbPick());
			cell.setCreatetm(dd);
			cell.setCreator(cmDefstock.getCreator());
			cell.setCreatorName(cmDefstock.getCreatorName());//创建人中文名次
			cell.setEditor(cmDefstock.getEditor());
			cell.setEditorName(cmDefstock.getEditorName());//修改人中文名称
			cell.setEdittm(dd);
			cell.setLimitRate(cmDefstock.getLimitRate());
			cell.setLimitType(cmDefstock.getLimitType());
			cell.setLocno(cmDefstock.getLocno());
			//cell.setMaxCase(cmDefstock.getMaxCase());
			cell.setMixFlag(cmDefstock.getMixFlag());
			cell.setMixSupplier(cmDefstock.getMixSupplier());
			cell.setOwnerNo(cmDefstock.getOwnerNo());
			cell.setPickFlag(cmDefstock.getPickFlag());
			cell.setStockNo(cmDefstock.getStockNo());
			cell.setWareNo(cmDefstock.getWareNo());
			cell.setCellStatus(cmDefstock.getStockStatus());
			cell.setItemType(cmDefstock.getItemType());
			cell.setAreaQuality(cmDefstock.getAreaQuality());
			
			StringBuilder cellNoPrefix = new StringBuilder();
			cellNoPrefix.append(cmDefstock.getWareNo());
			cellNoPrefix.append(cmDefstock.getAreaNo());
			cellNoPrefix.append(cmDefstock.getStockNo());
			
			for(int i=1;i<=cmDefstock.getqStockX();i++){
				String sx = Integer.toString(i);
				if(sx.length()==1){
					sx = "0" + sx;
				}
				cell.setStockX(sx);//通道格数
				for(int j=1;j<=cmDefstock.getqBayX();j++){
					cell.setBayX(String.valueOf(j));//储格位数
					for(int n=1;n<=cmDefstock.getqStockY();n++){
						cell.setStockY(String.valueOf(n));//通道层数
						StringBuilder cellNoSuffix = new StringBuilder();
//						cellNoSuffix.append("0").append(i).append(j).append(n);
						cellNoSuffix.append(sx).append(j).append(n);
						
						cell.setCellNo(cellNoPrefix.toString()+cellNoSuffix.toString());
						int count = cmDefcellService.add(cell);
						if(count < 1){
							throw new ManagerException("新增失败 ");
						}
					}
				}
			}
			cmDefstock.setCreatetm(dd);
			cmDefstock.setEdittm(dd);
			int n = this.cmDefstockService.add(cmDefstock);
			return n;
		} catch (ServiceException e) {
			throw new ManagerException("新增失败");
		}

	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String, Object> deleteBatch(String locno,List<CmDefstock> listCmDefstocks) throws ManagerException {
		
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";
		String stockNo = "";
		String settingNo = "";
		
		try{
			if(CommonUtil.hasValue(listCmDefstocks)){
				for (CmDefstock c : listCmDefstocks) {
					
					Map<String,Object> mapParams = new HashMap<String, Object>();
					mapParams.put("locno",locno);
					mapParams.put("wareNo",c.getWareNo());
					mapParams.put("areaNo",c.getAreaNo());
					mapParams.put("stockNo",c.getStockNo());
					
					CmDefstock cmDefstock = null;
					CmDefcell cmDefcell = null;
					List<CmDefstock> listCs = cmDefstockService.findByBiz(null, mapParams);
					List<CmDefcell> listCd = cmDefcellService.findByBiz(null, mapParams);
					
					if(CommonUtil.hasValue(listCs)){
						cmDefstock = listCs.get(0);
					}
					if(CommonUtil.hasValue(listCd)){
						cmDefcell = listCd.get(0);
					}
					
					if(cmDefcell!=null&&cmDefstock!=null){
						stockNo = cmDefstock.getStockNo();
						break;
					}
					//验证是否被上架策略引用
					mapParams.put("cellNo", c.getWareNo()+c.getAreaNo()+c.getStockNo());
					List<CsInstockSettingdtl2> settingList = csInstockSettingdtl2Service.findByBiz(null, mapParams);
					CsInstockSettingdtl2 settingdtl2 = null;
					if(CommonUtil.hasValue(settingList)){
						settingdtl2 = settingList.get(0);
						settingNo = settingdtl2.getSettingNo();
						break;
					}
				}
				
				if(StringUtils.isNotBlank(stockNo)){
					flag = "warn";
					msg = stockNo;
		   			mapObj.put("flag", flag);
					mapObj.put("msg", msg);
					return mapObj;
				}
				if(StringUtils.isNotBlank(settingNo)){
					flag = "hasSettingNo";
					msg = stockNo;
		   			mapObj.put("flag", flag);
					mapObj.put("msg", msg);
					mapObj.put("settingNo", settingNo);
					return mapObj;
				}
				
				for (CmDefstock c : listCmDefstocks) {
					CmDefstockKey key = new CmDefstockKey();
					key.setLocno(locno);
					key.setWareNo(c.getWareNo());
					key.setAreaNo(c.getAreaNo());
					key.setStockNo(c.getStockNo());
					int count = cmDefstockService.deleteById(key);
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
//				if(temp.length==4){
//					CmDefstockKey key = new CmDefstockKey();
//					key.setLocno(temp[0]);
//					key.setWareNo(temp[1]);
//					key.setAreaNo(temp[2]);
//					key.setStockNo(temp[3]);
//					try {
//						cmDefstockService.deleteById(key);
//					} catch (ServiceException e) {
//						return 0;
//					}
//				}
//			}
//		}
		
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int queryStoreNo0(CmDefstock cmDefstock) throws ManagerException {
		int count = 0;
		try {
			count = cmDefstockService.queryStoreNo0(cmDefstock);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return count;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int queryStoreNo1(CmDefstock cmDefstock) throws ManagerException {
		int count = 0;
		try {
			count = cmDefstockService.queryStoreNo1(cmDefstock);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return count;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int queryStoreNo2(CmDefstock cmDefstock) throws ManagerException {
		int count = 0;
		try {
			count = cmDefstockService.queryStoreNo2(cmDefstock);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return count;
	}
    
}