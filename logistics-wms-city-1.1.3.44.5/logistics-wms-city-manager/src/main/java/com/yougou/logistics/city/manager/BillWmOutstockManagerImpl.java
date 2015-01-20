package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.model.BillWmOutstock;
import com.yougou.logistics.city.common.model.BillWmOutstockDtl;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.service.AuthorityUserinfoService;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillWmOutstockDirectService;
import com.yougou.logistics.city.service.BillWmOutstockDtlService;
import com.yougou.logistics.city.service.BillWmOutstockService;
import com.yougou.logistics.city.service.BillWmRecedeDtlService;
import com.yougou.logistics.city.service.BillWmRecedeService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:54 CST 2013
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
@Service("billWmOutstockManager")
class BillWmOutstockManagerImpl extends BaseCrudManagerImpl implements BillWmOutstockManager {
	
	@Resource
	private BillWmOutstockService billWmOutstockService;
	
	@Resource
	private BillWmRecedeService billWmRecedeService;

	@Resource
	private BillWmOutstockDtlService billWmOutstockDtlService;
	
	@Resource
	private BillWmRecedeDtlService billWmRecedeDtlService;
	
	@Resource
	private BillWmOutstockDirectService billWmOutstockDirectService;
	
	@Resource
	private BillAccControlService billAccControlService;
	
	@Resource
    private AuthorityUserinfoService authorityUserinfoService;
	private static final String STATUS13 = "13";

	@Override
	public BaseCrudService init() {
		return billWmOutstockService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void checkOutstock(String locno, String outstockNo, String editor, String keyStr, String ownerNo,String userChName)
			throws ManagerException {
		try {
			
			String recedeNo = "N";
			Date  date = new Date();
			//更新主表信息
			BillWmOutstock stock = new BillWmOutstock();
			stock.setLocno(locno);
			stock.setOutstockNo(outstockNo);
			stock.setStatus(STATUS13);
			stock.setOwnerNo(ownerNo);
			stock.setEditor(editor);
			stock.setEdittm(date);
			stock.setEditorName(userChName);
			stock.setAuditor(editor);
			stock.setAudittm(date);
			stock.setAuditorName(userChName);
			stock.setAudittm(date);
			int m = billWmOutstockService.modifyById(stock);
			if(m < 1){
				throw new ManagerException("更新拣货单状态失败!");
			}
			if (StringUtils.isEmpty(keyStr)) {
				throw new ManagerException("明细不能为空");
			}
			String[] keys = keyStr.split(",");
			BillWmOutstockDtl dtl = null;
			
			Map<String,AuthorityUserinfo> map = new HashMap<String, AuthorityUserinfo>();
			for (String key : keys) {
				dtl = new BillWmOutstockDtl();
				String[] values = key.split("\\|");
				dtl.setLocno(locno);
				dtl.setOutstockNo(outstockNo);
				dtl.setOwnerNo(values[0]);
				dtl.setDivideId(Integer.valueOf(values[1]));
				dtl.setRealQty(new BigDecimal(Integer.valueOf(values[2])));
				dtl.setStatus(STATUS13);
				dtl.setOutstockName(values[3]);
				AuthorityUserinfo findUserByCache = authorityUserinfoService.findUserByCache("", values[3], map);
	            if(findUserByCache==null){
	                throw new ServiceException(values[3]+"，用户不存在");
	            }
	            dtl.setOutstockChName(findUserByCache.getUsername());
				dtl.setOutstockDate(date);
				int d = billWmOutstockDtlService.modifyById(dtl);
				if(d < 1){
					throw new ManagerException("更新拣货单实际拣货数量失败!");
				}
				
				//查找退厂拣货明细对象
				BillWmOutstockDtl e1 = new BillWmOutstockDtl();
				e1.setLocno(locno);
				e1.setOwnerNo(values[0]);
				e1.setOutstockNo(outstockNo);
				e1.setDivideId(Integer.valueOf(values[1]));
				BillWmOutstockDtl billWmOutstockDtl = billWmOutstockDtlService.findById(e1);
				if(billWmOutstockDtl == null){
					throw new ManagerException("查询拣货单明细失败!");
				}
				
				//回写退厂通知单下架数量, su.yq 20140924 注释 因为退厂调度就已经回写了outstockQty
//				BillWmRecedeDtlKey recedeDtlKey = new BillWmRecedeDtlKey();
//				recedeDtlKey.setLocno(billWmOutstockDtl.getLocno());
//				recedeDtlKey.setOwnerNo(billWmOutstockDtl.getOwnerNo());
//				recedeDtlKey.setRecedeNo(billWmOutstockDtl.getSourceNo());
//				recedeDtlKey.setPoId(billWmOutstockDtl.getPoId());
//				BillWmRecedeDtl wmRecedeDtl = (BillWmRecedeDtl)billWmRecedeDtlService.findById(recedeDtlKey);
//				if(wmRecedeDtl == null){
//					throw new ServiceException("查询退厂通知单明细回写拣货数量失败!单号:"+billWmOutstockDtl.getSourceNo());
//				}
//				BigDecimal outstockQty = wmRecedeDtl.getOutstockQty().add(new BigDecimal(Integer.valueOf(values[2])));
//				wmRecedeDtl.setOutstockQty(outstockQty);
//				System.out.println(wmRecedeDtl.getOutstockQty()+"-"+new BigDecimal(Integer.valueOf(values[2])));
//				
//				int rNum = billWmRecedeDtlService.modifyById(wmRecedeDtl);
//				if(rNum < 1){
//					throw new ServiceException("更新退厂通知单下架数量失败!");
//				}
				
				//更改下架指示表状态为13
//				BillWmOutstockDirect direct = new BillWmOutstockDirect();
//				direct.setLocno(billWmOutstockDtl.getLocno());
//				direct.setOwnerNo(billWmOutstockDtl.getOwnerNo());
//				direct.setSourceNo(billWmOutstockDtl.getSourceNo());
//				direct.setItemNo(billWmOutstockDtl.getItemNo());
//				direct.setItemId(billWmOutstockDtl.getItemId());
//				direct.setSizeNo(billWmOutstockDtl.getSizeNo());
//				direct.setPoId(billWmOutstockDtl.getPoId());
//				direct.setStatus(STATUS13);
//				int od = billWmOutstockDirectService.updateBillWmOutstockDirect(direct);
//				if(od < 1){
//					throw new ServiceException("更新退厂下架指示表状态失败!");
//				}
				recedeNo = billWmOutstockDtl.getSourceNo();
			}
			
			//更新退厂通知单状态为拣货完成,需要调度后全部发单并且退厂通知单对应的拣货单全部拣货完成
			BillWmRecede wmRecede = new BillWmRecede();
			wmRecede.setLocno(locno);
			wmRecede.setOwnerNo(ownerNo);
			wmRecede.setRecedeNo(recedeNo);
			billWmRecedeService.updateRecedeStatus4Outstock(wmRecede);
			
//			if(!"N".equals(recedeNo)&&StringUtils.isNotBlank(locno)&&StringUtils.isNotBlank(ownerNo)&&StringUtils.isNotBlank(recedeNo)){
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("locno", locno);
//				params.put("ownerNo", ownerNo);
//				params.put("sourceNo", recedeNo);
//				params.put("status", "10");
//				List<BillWmOutstockDirect> listDirects=billWmOutstockDirectService.findBillWmOutstockDirectAndOutstockDtl(params);
//				if(!CommonUtil.hasValue(listDirects)){
//					BillWmRecede billWmRecede = new BillWmRecede();
//					billWmRecede.setLocno(locno);
//					billWmRecede.setOwnerNo(ownerNo);
//					billWmRecede.setRecedeNo(recedeNo);
//					billWmRecede.setStatus("20");
//					int wr = billWmRecedeService.modifyById(billWmRecede);
//					if(wr < 1){
//						throw new ServiceException("更新退厂通知单下架状态失败!");
//					}
//				}
//			}
			
			//记账存储过程扣减库存
			BillAccControlDto controlDto = new BillAccControlDto();
			controlDto.setiPaperNo(outstockNo);
			controlDto.setiLocType("2");
			controlDto.setiPaperType("RO");
			controlDto.setiIoFlag("O");
			controlDto.setiPrepareDataExt(new BigDecimal(0));
			controlDto.setiIsWeb(new BigDecimal(1));
			billAccControlService.procAccApply(controlDto);

			
			//记账存储过程扣减库存
			BillAccControlDto controlDto2 = new BillAccControlDto();
			controlDto2.setiPaperNo(outstockNo);
			controlDto2.setiLocType("2");
			controlDto2.setiPaperType("RO");
			controlDto2.setiIoFlag("I");
			controlDto2.setiPrepareDataExt(new BigDecimal(0));
			controlDto2.setiIsWeb(new BigDecimal(1));
			billAccControlService.procAccApply(controlDto2);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		}
	}
}