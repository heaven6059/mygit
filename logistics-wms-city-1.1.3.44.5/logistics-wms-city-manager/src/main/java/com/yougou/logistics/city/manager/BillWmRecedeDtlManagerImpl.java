package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillWmRecedeDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillWmRecedeDtlDto;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.model.BillWmRecedeDtl;
import com.yougou.logistics.city.common.model.BillWmRecedeDtlKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillWmRecedeDtlService;
import com.yougou.logistics.city.service.BillWmRecedeService;
import com.yougou.logistics.city.service.ItemService;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
@Service("billWmRecedeDtlManager")
class BillWmRecedeDtlManagerImpl extends BaseCrudManagerImpl implements BillWmRecedeDtlManager {
    @Resource
    private BillWmRecedeDtlService billWmRecedeDtlService;
    
    @Resource
    private BillWmRecedeService billWmRecedeService;
    
    @Resource
    private ItemService itemService;
    

    @Override
    public BaseCrudService init() {
        return billWmRecedeDtlService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> boolean addWmRecedeDtl(String locno, String ownerNo,
			String recedeNo, Map<CommonOperatorEnum, List<ModelType>> params,
			String loginName ,String loginCHName) throws ManagerException {
		try{
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);
			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);
			
			BillWmRecede billWmRecede=new BillWmRecede();
			billWmRecede.setEditor(loginName);
			billWmRecede.setEditorName(loginCHName);
			billWmRecede.setEdittm(new Date());
			billWmRecede.setLocno(locno);
			billWmRecede.setOwnerNo(ownerNo);
			billWmRecede.setRecedeNo(recedeNo);
			billWmRecedeService.modifyById(billWmRecede);
			
			if(null!= addList && addList.size()>0){
				
				
				BillWmRecedeDtlKey billWmRecedeDtlKey = new  BillWmRecedeDtlKey();
				billWmRecedeDtlKey.setLocno(locno);
				billWmRecedeDtlKey.setOwnerNo(ownerNo);
				billWmRecedeDtlKey.setRecedeNo(recedeNo);
				short poId = billWmRecedeDtlService.selectMaxPid(billWmRecedeDtlKey);
				//新增操作
				for(ModelType modelType : addList){
					if(modelType instanceof  BillWmRecedeDtl){
						//转换成对象
						BillWmRecedeDtl vo = (BillWmRecedeDtl)modelType;	
						Item item = itemService.selectByCode(vo.getItemNo(),null);
						vo.setRecedeNo(recedeNo);
						vo.setLocno(locno);
						vo.setOwnerNo(ownerNo);
						vo.setPoId(++poId);
						vo.setPackQty(vo.getPackQty()==null?new BigDecimal(1):vo.getPackQty());
						vo.setBrandNo(item.getBrandNo());
						int a = billWmRecedeDtlService.add(vo);
						if(a<1){
							throw new ManagerException("插入退厂通知单明细记录时未更新到记录！");
						}
					}
				}
			}
			
			
			//删除操作
			for(ModelType modelType : delList){
				if(modelType instanceof  BillWmRecedeDtl){
					BillWmRecedeDtl vo = (BillWmRecedeDtl)modelType;
					
					BillWmRecedeDtlKey  billWmRecedeDtlKey = new  BillWmRecedeDtlKey();
					billWmRecedeDtlKey.setRecedeNo(recedeNo);
					billWmRecedeDtlKey.setLocno(locno);
					billWmRecedeDtlKey.setOwnerNo(ownerNo);
					billWmRecedeDtlKey.setPoId(vo.getPoId());
					
					int b =billWmRecedeDtlService.deleteById(billWmRecedeDtlKey); 
					if(b < 1){
						 throw new ManagerException("删除退厂通知单明细信息时未更新到记录！");
					 }
				}
				
			}	
			
			//修改操作
			for(ModelType modelType : uptList){
				if(modelType instanceof  BillWmRecedeDtl){
					BillWmRecedeDtl vo = (BillWmRecedeDtl)modelType;
					vo.setRecedeNo(recedeNo);
					vo.setLocno(locno);
					vo.setOwnerNo(ownerNo);
					int b =billWmRecedeDtlService.modifyById(vo); 
					if(b < 1){
						 throw new ManagerException("修改退厂通知单明细信息时未更新到记录！");
					 }
				}
				
			}
			
		}catch (Exception e) {
			throw new  ManagerException(e);
		}
		return true;
	}

	@Override
	public int selectCountMx(BillWmRecedeDtlDto dto,AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecedeDtlService.selectCountMx(dto,authorityParams);
		}catch (Exception e) {
			throw new  ManagerException(e);
		}
	}

	@Override
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoByExpNo(
			BillWmRecedeDtlDto dto) throws ManagerException {
		try{
			return billWmRecedeDtlService.queryBillWmRecedeDtlDtoByExpNo(dto);
		}catch (Exception e) {
			throw new  ManagerException(e);
		}
	}

	@Override
	public List<BillWmRecedeDtlDto> queryBillWmRecedeDtlDtoGroupBy(
			SimplePage page, BillWmRecedeDtlDto dto,AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmRecedeDtlService.queryBillWmRecedeDtlDtoGroupBy(page,dto,authorityParams);
		}catch (Exception e) {
			throw new  ManagerException(e);
		}
	}
	
	@Override
	public int selectItemCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecedeDtlService.selectItemCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillWmRecedeDtl> selectItem(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecedeDtlService.selectItem(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	@Override
	public int selectItemCountTest(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecedeDtlService.selectItemCountTest(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	/**
	 * 查询库存表(演示用，同findItemAndSize,体验后删除)
	 * @param params
	 * @return
	 */
	@Override
	public List<BillWmRecedeDtl> selectItemTest(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecedeDtlService.selectItemTest(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	public void excelImportData(List<BillWmRecedeDtl> list, String locno, String recedeNo, String ownerNo)throws ManagerException{
		try {
			 billWmRecedeDtlService.excelImportData(list,locno,recedeNo,ownerNo);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findWmRecedeDtlDispatchCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecedeDtlService.findWmRecedeDtlDispatchCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillWmRecedeDispatchDtlDTO> findWmRecedeDtlDispatchByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billWmRecedeDtlService.findWmRecedeDtlDispatchByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billWmRecedeDtlService.selectDispatchSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
}