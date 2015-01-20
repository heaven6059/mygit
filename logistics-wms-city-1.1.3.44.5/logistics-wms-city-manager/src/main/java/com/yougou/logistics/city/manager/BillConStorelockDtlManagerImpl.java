package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.ItemBarcode;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillConStoreLockQuery;
import com.yougou.logistics.city.service.BillConStorelockDtlService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ItemBarcodeService;
import com.yougou.logistics.city.service.ItemService;
import com.yougou.logistics.city.service.SizeInfoService;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
@Service("billConStorelockDtlManager")
class BillConStorelockDtlManagerImpl extends BaseCrudManagerImpl implements BillConStorelockDtlManager {
    
	@Resource
    private BillConStorelockDtlService billConStorelockDtlService;
	
	@Resource
	private CmDefcellService cmDefcellService;
	
	@Resource
	private SizeInfoService sizeInfoService;
	
	@Resource
	private ItemService itemService;

	@Resource
	private ItemBarcodeService itemBarCodeService;
	
    @Override
    public BaseCrudService init() {
        return billConStorelockDtlService;
    }

    
	@Override
	public void saveStorelockDtl(BillConStoreLockQuery lockQuery) throws ManagerException {
		try {
			this.billConStorelockDtlService.saveStorelockDtl(lockQuery);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	

	@Override
	public void delStorelockDtl(BillConStoreLockQuery lockQuery) throws ManagerException {
		try {
			this.billConStorelockDtlService.delStorelockDtl(lockQuery);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int findConContentGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConStorelockDtlService.findConContentGroupByCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillConStorelockDtl> findConContentGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConStorelockDtlService.findConContentGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}


	@Override
	public int findStorelockGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billConStorelockDtlService.findStorelockGroupByCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}


	@Override
	public List<BillConStorelockDtl> findStorelockGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConStorelockDtlService.findStorelockGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConStorelockDtlService.selectSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}


	@Override
	public List<BillConStorelockDtl> findConContentGroup(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billConStorelockDtlService.findConContentGroup(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	public Map<String,Object> importStorelockDtlExcel(List<BillConStorelockDtl> list,AuthorityParams authorityParams ,Map<String, Object> params)throws ManagerException{
		
		Map<String,Object> mapres  = new  HashMap<String,Object>();
		try{
		List<BillConStorelockDtl> billConStorelockDtllist = billConStorelockDtlService.findConContentGroup(params, authorityParams);
		StringBuffer strb = new StringBuffer("");
		//数据验证
		for (int i = 0; i < list.size(); i++) {
			BillConStorelockDtl billConStorelockDtl = list.get(i);
			list.get(i).setLocno(params.get("locno").toString());
			String cellno = billConStorelockDtl.getCellNo();
			String itemno = billConStorelockDtl.getItemNo();
			String sizeno = billConStorelockDtl.getSizeNo(); 
			
			int itemQty = billConStorelockDtl.getItemQty().intValue();
			
			//储位编码验证
			CmDefcellKey oldcmDefcellKey = new CmDefcellKey();
			oldcmDefcellKey.setCellNo(cellno);
			oldcmDefcellKey.setLocno(params.get("locno").toString());
			CmDefcell newcmDefcellKey = (CmDefcell) cmDefcellService.findById(oldcmDefcellKey);
			
			if(newcmDefcellKey==null){
				strb.append("【"+cellno+"】储位不存在.\\r\\n");
			}
			
			//商品编码验证
			Item olditem = new Item();
			olditem.setItemNo(itemno);
			Item newitem = itemService.findById(olditem);
			if(newitem==null){
				strb.append("【"+itemno+"】商品编码不存在.\\r\\n");
			}
			
			//商品尺码验证
			SizeInfo sizeInfo = new SizeInfo();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sizeNo", sizeno);
			List<SizeInfo> listSizeinfo = sizeInfoService.findByBiz(sizeInfo, map);
			if(listSizeinfo.size()==0){
				strb.append("【"+sizeno+"】商品尺码不存在.\\r\\n");
			}
			
			
			//验证可分配数量
			int isimport = 0;
			int isdayu = 0;
			for (int j = 0; j < billConStorelockDtllist.size(); j++) {
				BillConStorelockDtl bcsd = billConStorelockDtllist.get(j);
				if(bcsd.getCellNo().equals(cellno)&&bcsd.getItemNo().equals(itemno)&&bcsd.getSizeNo().equals(sizeno)){
					isimport++;
					if(itemQty>bcsd.getGoodContentQty().intValue()){
						isdayu++;
					}
				}
			}
			if(isimport==0){
				strb.append("【"+cellno+"】储位编码+【"+itemno+"】商品编码+【"+sizeno+"】尺码库存中没有可锁定的数据!\\r\\n");
			}
			if(isdayu>0){
				strb.append("【"+cellno+"】储位编码+【"+itemno+"】商品编码+【"+sizeno+"】尺码的分配数量大于库存可分配数量!\\r\\n");
			}
			
		}
		
		if(!"".equals(strb.toString())){
			throw new Exception("<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>"+strb.toString()+"</textarea>");
		}
		
		//验证明细数据是否已经存在
		
		//获取已在列表的数据
		List<BillConStorelockDtl>  billConStorelockDtlList = billConStorelockDtlService.findStorelockGroup(params, authorityParams);
		
		//判断数据是否已在列表
		List<BillConStorelockDtl> deleteStorelockDtlList = new ArrayList<BillConStorelockDtl>(); 
		for (int i = 0; i < list.size(); i++) {
			BillConStorelockDtl storelockDtlexl = list.get(i);
			
			//查询条码
			Map<String,Object> barCodeparams  = new  HashMap<String,Object>();
			ItemBarcode itemBarcode = new ItemBarcode();
			barCodeparams.put("itemNo", storelockDtlexl.getItemNo());
			barCodeparams.put("sizeNo", storelockDtlexl.getSizeNo());
			barCodeparams.put("packageId","0");
			List<ItemBarcode> itemBarcodelist = itemBarCodeService.findByBiz(itemBarcode, barCodeparams);
			list.get(i).setBarcode(itemBarcodelist.get(0).getBarcode());
			
			for (int j = 0; j < billConStorelockDtlList.size(); j++) {
				BillConStorelockDtl storelockDtlobj = billConStorelockDtlList.get(j);
				if(storelockDtlexl.getCellNo().equals(storelockDtlobj.getCellNo())&&
						storelockDtlexl.getItemNo().equals(storelockDtlobj.getItemNo())&&
						storelockDtlexl.getSizeNo().equals(storelockDtlobj.getSizeNo())){
					list.get(i).setItemQty(list.get(i).getItemQty().add(storelockDtlobj.getItemQty()));
					deleteStorelockDtlList.add(storelockDtlobj);
				}
			}
		}
		
		//先把存在的数据删除掉
		BillConStoreLockQuery deletelockQuery= new BillConStoreLockQuery();
		deletelockQuery.setOwnerNo(params.get("ownerNo").toString());
		deletelockQuery.setStorelockNo(params.get("storelockNo").toString());
		deletelockQuery.setLocno(params.get("locno").toString());
		deletelockQuery.setCreator(params.get("creator").toString());
		deletelockQuery.setCreatorName(params.get("creatorName").toString());
		deletelockQuery.setEditorName(params.get("creatorName").toString());
		deletelockQuery.setInsertList(deleteStorelockDtlList);
		billConStorelockDtlService.delStorelockDtl(deletelockQuery);
		
		
		//保存数据
		BillConStoreLockQuery lockQuery= new BillConStoreLockQuery();
		lockQuery.setOwnerNo(params.get("ownerNo").toString());
		lockQuery.setStorelockNo(params.get("storelockNo").toString());
		lockQuery.setLocno(params.get("locno").toString());
		lockQuery.setCreator(params.get("creator").toString());
		lockQuery.setCreatorName(params.get("creatorName").toString());
		lockQuery.setEditorName(params.get("creatorName").toString());
		lockQuery.setInsertList(list);
		billConStorelockDtlService.saveStorelockDtl(lockQuery);
		 
		mapres.put("result","success");
		mapres.put("msg","导入成功");
	}catch(Exception e){
		mapres.put("result", ResultEnums.FAIL.getResultMsg());
		mapres.put("msg",CommonUtil.getExceptionMessage(e).replace("\"", "'"));
	}
		return mapres;
	}
	
}