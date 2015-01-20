package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillWmOutstockDirectMapper;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Fri Jan 03 19:15:03 CST 2014
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
@Service("billWmOutstockDirectService")
class BillWmOutstockDirectServiceImpl extends BaseCrudServiceImpl implements BillWmOutstockDirectService {

	@Resource
	private ProcCommonService procCommonService;
	
	@Resource
	private BillWmOutstockService billWmOutstockService;
	
	@Resource
	private BillWmOutstockDtlService billWmOutstockDtlService;
	
	@Resource
	private BillWmOutstockDirectMapper billWmOutstockDirectMapper;
	@Resource
    private AuthorityUserinfoService authorityUserinfoService;
	
	@Override
	public BaseCrudMapper init() {
		return billWmOutstockDirectMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void sendWmOutstockDirect(BillWmOutstock billWmOutstock, List<BillWmOutstockDirect> listDirects)
			throws ServiceException {
		
		try {
			
			if(billWmOutstock == null || !CommonUtil.hasValue(listDirects)){
				throw new ServiceException("参数非法!");
			}
			
			//新增主表信息
			String ownerNo = listDirects.get(0).getOwnerNo();
			String outstockNo = procCommonService.procGetSheetNo(billWmOutstock.getLocno(), CNumPre.WM_OUTSTOCK_PRE);
			billWmOutstock.setOwnerNo(ownerNo);
			billWmOutstock.setOutstockNo(outstockNo);
			billWmOutstock.setOperateDate(new Date());
			billWmOutstock.setOperateType("B");
			billWmOutstock.setStatus("10");
			int m = billWmOutstockService.add(billWmOutstock);
			if(m < 1){
				throw new ServiceException("新增退厂拣货单主表失败!");
			}
			AuthorityUserinfo findUserByCache = authorityUserinfoService.findUserByCache(billWmOutstock.getLocno(), billWmOutstock.getOutstockName(), null);
			if(findUserByCache==null){
			    throw new ServiceException(billWmOutstock.getOutstockName()+"，用户不存在");
			}
			//新增明细表信息
			//int i = 0;
			for (BillWmOutstockDirect b : listDirects) {
				BillWmOutstockDtl outstockDtl = new BillWmOutstockDtl();
				outstockDtl.setLocno(billWmOutstock.getLocno());
				outstockDtl.setOwnerNo(billWmOutstock.getOwnerNo());
				outstockDtl.setOutstockNo(outstockNo);
				outstockDtl.setDivideId(b.getDirectSerial());
				outstockDtl.setSourceNo(b.getSourceNo());
				outstockDtl.setPoId(b.getPoId());
				outstockDtl.setItemNo(b.getItemNo());
				outstockDtl.setItemId(b.getItemId());
				outstockDtl.setPackQty(b.getPackQty());
				outstockDtl.setItemQty(b.getOutstockItemQty());
				outstockDtl.setRealQty(new BigDecimal(0));
				outstockDtl.setsCellNo(b.getsCellNo());
				outstockDtl.setsCellId(b.getsCellId());
				outstockDtl.setsContainerNo(b.getsContainerNo());
				outstockDtl.setdCellNo(b.getdCellNo());
				outstockDtl.setdCellId(b.getdCellId());
				outstockDtl.setdContainerNo(b.getdContainerNo());
				outstockDtl.setAssignName(billWmOutstock.getOutstockName());
				outstockDtl.setStockType(b.getStockType());
				outstockDtl.setStockValue(b.getStockValue());
				outstockDtl.setSizeNo(b.getSizeNo());
				outstockDtl.setBoxNo(b.getBoxNo());
				outstockDtl.setBrandNo(b.getBrandNo());
				outstockDtl.setAssignChName(findUserByCache.getUsername());
				int d = billWmOutstockDtlService.add(outstockDtl);
				if(d < 1){
					throw new ServiceException("新增退厂拣货单明细表失败!");
				}
				
				//更新下架指示表状态
				b.setStatus("13");
				int bNum = billWmOutstockDirectMapper.updateByPrimaryKeySelective(b);
				if(bNum < 1){
					throw new ServiceException("更新退厂拣货下架指示表状态失败!");
				}
			}
		
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public int updateBillWmOutstockDirect(BillWmOutstockDirect direct) throws ServiceException {
		try{
			return billWmOutstockDirectMapper.updateBillWmOutstockDirect(direct);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillWmOutstockDirect> findBillWmOutstockDirectAndOutstockDtl(Map<String, Object> maps)
			throws ServiceException {
		try{
			return billWmOutstockDirectMapper.selectBillWmOutstockDirectAndOutstockDtl(maps);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	

}