package com.yougou.logistics.city.manager;

import java.util.ArrayList;
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
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellKey;
import com.yougou.logistics.city.common.model.CmDefcellSimple;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.CsInstockSettingdtl2;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ConContentService;
import com.yougou.logistics.city.service.CsInstockSettingdtl2Service;

/**
 * 
 * 储位manager实现
 * 
 * @author qin.dy
 * @date 2013-9-26 下午5:23:06
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefcellManager")
class CmDefcellManagerImpl extends BaseCrudManagerImpl implements CmDefcellManager {

	@Resource
	private CmDefcellService cmDefcellService;

	@Resource
	private ConContentService conContentService;

	@Resource
    private CsInstockSettingdtl2Service csInstockSettingdtl2Service;
	
	@Override
	public BaseCrudService init() {
		return cmDefcellService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> deleteBatch(String locno, List<CmDefcell> listCmDefcells) throws ManagerException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";

		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("locno", locno);
			List<ConContent> listConContents = conContentService.findCmdefCellIsHaveConContent(map, listCmDefcells);

			if (CommonUtil.hasValue(listConContents)) {
				String cellNoStr = "";
				for (ConContent c : listConContents) {
					cellNoStr += c.getCellNo() + ",";
				}
				if (StringUtils.isNotBlank(cellNoStr)) {
					cellNoStr = cellNoStr.substring(0, cellNoStr.length() - 1);
				}

				flag = "warn";
				msg = cellNoStr;
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				return mapObj;
			}
			
			//验证是否被上架策略引用
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("locno", locno);
			String cellNoStr = "";
			for (CmDefcell c : listCmDefcells) {
				mapParams.put("cellNo", c.getCellNo());
				List<CsInstockSettingdtl2> settingList = csInstockSettingdtl2Service.findByBiz(null, mapParams);
				if(CommonUtil.hasValue(settingList)){
					cellNoStr += c.getCellNo();
				}
			}
			if(StringUtils.isNotBlank(cellNoStr)){
				cellNoStr = cellNoStr.substring(0,cellNoStr.length()-1);
				flag = "hasSettingNo";
				msg = cellNoStr;
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				return mapObj;
			}
			for (CmDefcell c : listCmDefcells) {
				CmDefcellKey key = new CmDefcellKey();
				key.setLocno(c.getLocno());
				key.setCellNo(c.getCellNo());
				int count = cmDefcellService.deleteById(key);
				if (count < 1) {
					throw new ManagerException("删除失败");
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
		//				if(temp.length==2){
		//					CmDefcellKey key = new CmDefcellKey();
		//					key.setLocno(temp[0]);
		//					key.setCellNo(temp[1]);
		//					try {
		//						cmDefcellService.deleteById(key);
		//					} catch (ServiceException e) {
		//						return 0;
		//					}
		//				}
		//			}
		//		}
		//		return 1;
	}

	@Override
	public List<CmDefcell> findCmDefcellByArea(CmDefcell defcell) throws ManagerException {
		try {
			return this.cmDefcellService.findCmDefcellByArea(defcell);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<CmDefcell> findCmDefcell4Adj(SimplePage page,Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.cmDefcellService.findCmDefcell4Adj(page,params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public int findCmDefcell4AdjCount(Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException {
		int count = 0;
		try {
			count = cmDefcellService.findCmDefcell4AdjCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return count;
	}
	
	
	@Override
	public List<CmDefcell> findCmDefcell4Plan(SimplePage page,Map<String,Object> params) throws ManagerException {
		try {
			return this.cmDefcellService.findCmDefcell4Plan(page,params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public int findCmDefcell4PlanCount(Map<String,Object> params) throws ManagerException {
		int count = 0;
		try {
			count = cmDefcellService.findCmDefcell4PlanCount(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return count;
	}
	
	

	@Override
	public int queryContent(CmDefcell defcell) throws ManagerException {
		int count = 0;
		try {
			count = cmDefcellService.queryContent(defcell);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return count;
	}

	public CmDefcell selectCellNo4BillHmPlan(CmDefcell defcell) throws ManagerException {
		try {
			return cmDefcellService.selectCellNo4BillHmPlan(defcell);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public boolean modifyCmDefcell(CmDefcell cmDefcell) throws ManagerException {
		try {
			//查找储位原数据
			CmDefcellKey cmDefcellKey=new CmDefcellKey();
			cmDefcellKey.setLocno(cmDefcell.getLocno());
			cmDefcellKey.setCellNo(cmDefcell.getCellNo());			
			CmDefcell oldCmDefcell=(CmDefcell) cmDefcellService.findById(cmDefcellKey);
			
			//查找储位是否有库存
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("locno", cmDefcell.getLocno());
			List<CmDefcell> defcellList=new ArrayList<CmDefcell>();
			defcellList.add(cmDefcell);
			List<ConContent> contentList = conContentService.findCmdefCellIsHaveConContent(map, defcellList);
			
			// 当该储位存在库存时，不可修改品质或属性
			if ((!cmDefcell.getAreaQuality().equals(
					oldCmDefcell.getAreaQuality()) || !cmDefcell.getItemType()
					.equals(oldCmDefcell.getItemType()))
					&& contentList.size() > 0) {
				throw new ManagerException("该储位已存在商品库存，不可修改品质或属性。");
			}
			
			cmDefcell.setEdittm(new Date());
			cmDefcellService.modifyById(cmDefcell);
			return true;
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String batchModifyStatus(String keys, String status,SystemUser user)
			throws ManagerException {
		String locno = user.getLocNo();
		if(StringUtils.isBlank(locno)){
			throw new ManagerException("缺少仓库编码");
		}
		if(StringUtils.isBlank(keys)){
			throw new ManagerException("缺少储位信息");
		}
		String [] keyArray = keys.split(",");
		String [] cellNoAndStatus = null;
		CmDefcell cmDefcell = new CmDefcell();
		cmDefcell.setLocno(locno);
		cmDefcell.setCellStatus(status);
		cmDefcell.setEdittm(new Date());
		cmDefcell.setEditor(user.getLoginName());
		cmDefcell.setEditorName(user.getUsername());//修改人中文名称
		String cellNo;
		String sourceCellStatus;
		int num = 0;
		if(keyArray.length == 0){
			throw new ManagerException("缺少储位信息");
		}
		for(String key:keyArray){
			cellNoAndStatus = key.split("\\|");
			cellNo = cellNoAndStatus[0];
			if(cellNoAndStatus.length != 2){
				throw new ManagerException("储位【"+cellNo+"】信息有误");
			}
			sourceCellStatus = cellNoAndStatus[1];
			cmDefcell.setCellNo(cellNo);
			cmDefcell.setSourceCellStatus(sourceCellStatus);
			
			try {
				num = cmDefcellService.modifyById(cmDefcell);
				if(num <= 0){
					throw new ManagerException("储位【"+cellNo+"】修改状态有误");
				}
			} catch (ServiceException e) {
				throw new ManagerException(e);
			}
		}
		return null;
	}

	@Override
	public String findOwnerByStock(CmDefcell cmDefcell) throws ManagerException {
		try {
			return this.cmDefcellService.findOwnerByStock(cmDefcell);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<CmDefcellSimple> findSimple(Map<String,Object> params) throws ManagerException {
		try {
			return this.cmDefcellService.findSimple(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}