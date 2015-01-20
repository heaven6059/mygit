package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillOmOutstockKey;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.AuthorityUserinfoService;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillOmOutstockDtlService;
import com.yougou.logistics.city.service.BillOmOutstockService;
import com.yougou.logistics.city.service.ConContentService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Oct 14 14:47:37 CST 2013
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
@Service("billOmOutstockManager")
class BillOmOutstockManagerImpl extends BaseCrudManagerImpl implements BillOmOutstockManager {

	private final static String RESULTY = "Y|";

	@Resource
	private BillOmOutstockService billOmOutstockService;

	@Resource
	private BillOmOutstockDtlService billOmOutstockDtlService;

	@Resource
	private ConContentService conContentService;
	
	@Resource
	private BillAccControlService billAccControlService;

	@Resource
	private AuthorityUserinfoService authorityUserinfoService;
	private static final String STATUS30 = "30";
	
	/**
	 * 拣货完成
	 */
	private static final String STATUS13 = "13";

	@Override
	public BaseCrudService init() {
		return billOmOutstockService;
	}
	

	@Override
	public int findMoveStockCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billOmOutstockService.findMoveStockCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}


	@Override
	public List<BillOmOutstock> findMoveStockByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billOmOutstockService.findMoveStockByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void auditOutstock(List<BillOmOutstock> oList,String loginName, String username) throws ManagerException {
		try{
			
			if(!CommonUtil.hasValue(oList)){
				throw new ManagerException("参数非法!");
			}
			
			for (BillOmOutstock outstock : oList) {
				String locno = outstock.getLocno();
				String outstockNo = outstock.getOutstockNo();
				
				//验证审核合法性
				if(billOmOutstockDtlService.selectCheckAssignName(outstock)>0){
					throw new ManagerException(outstockNo+"未指定拣货人,请发单再审核!");
				}
				
				BillOmOutstockKey key = new BillOmOutstockKey();
				key.setLocno(locno);
				key.setOutstockNo(outstockNo);
				BillOmOutstock omOutstock = (BillOmOutstock) billOmOutstockService.findById(key);
				if(STATUS13.equals(omOutstock.getStatus())){
					throw new ManagerException(outstockNo+"已经拣货完成,不能审核!");
				}
				
				List<BillOmOutstockDtl> listDtls=billOmOutstockDtlService.selectOutstockDtlCheckoedQty(outstock);
				if(CommonUtil.hasValue(listDtls)){
					StringBuffer sb = new StringBuffer();
					sb.append("单号："+outstockNo+",");
					sb.append("商品："+listDtls.get(0).getItemNo()+",");
					sb.append("尺码："+listDtls.get(0).getSizeNo()+",");
					throw new ManagerException(sb.toString()+"实际数量必须大于或等于RF数量!");
				}
				
				//记账存储过程扣减库存
				BillAccControlDto controlDto = new BillAccControlDto();
				controlDto.setiPaperNo(outstockNo);
				controlDto.setiLocType("2");
				controlDto.setiPaperType("HO");
				controlDto.setiIoFlag("O");
				controlDto.setiPrepareDataExt(new BigDecimal(0));
				controlDto.setiIsWeb(new BigDecimal(1));
				billAccControlService.procAccApply(controlDto);
				
				//记账存储过程扣减库存
				BillAccControlDto controlDto2 = new BillAccControlDto();
				controlDto2.setiPaperNo(outstockNo);
				controlDto2.setiLocType("2");
				controlDto2.setiPaperType("HO");
				controlDto2.setiIoFlag("I");
				controlDto2.setiPrepareDataExt(new BigDecimal(0));
				controlDto2.setiIsWeb(new BigDecimal(1));
				billAccControlService.procAccApply(controlDto2);
				
				//更新主表信息  更改状态
				Date date=new Date();
				outstock.setStatus(STATUS13);
				outstock.setAuditor(loginName);
				outstock.setAuditorname(username);
				outstock.setAudittm(date);
				outstock.setEditor(loginName);
				outstock.setEditorname(username);
				outstock.setEdittm(date);
				int count = billOmOutstockService.modifyById(outstock);
				if (count < 0) {
					throw new ManagerException(outstockNo+"拣货单状态更新失败!");
				}
				
				//更新明细的状态
				BillOmOutstockDtl dtl  = new BillOmOutstockDtl();
				dtl.setLocno(locno);
				dtl.setOutstockNo(outstockNo);
				dtl.setStatus(STATUS13);
				int dcount = billOmOutstockDtlService.modifyById(dtl);
				if (dcount < 0) {
					throw new ManagerException(outstockNo+"拣货单明细状态更新失败!");
				}
				
				//如果实际拣货人为空,更新指定拣货人为实际拣货人
				billOmOutstockDtlService.updateOutstockName4AssignName(outstock);
			}
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void checkOutstock(String locno, String outstockNo, String outstockName, String keyStr,SystemUser systemUser)
			throws ManagerException {
		try {
			Date now = new Date();
			BillOmOutstock omOutstock=new BillOmOutstock();
			omOutstock.setOutstockNo(outstockNo);
			omOutstock.setLocno(locno);
			omOutstock=billOmOutstockService.findById(omOutstock);
			String status=omOutstock.getStatus();
			if(StringUtils.isNotEmpty(status)){
				if(!status.equals("10")){
					throw new ManagerException("上架完成的单据不能修改");
				}
			}else{
				throw new ManagerException("上架完成的单据不能修改");
			}
			//先更新明细的数量
			String[] keys = keyStr.split(",");
			BillOmOutstockDtl dtl = null;
			Map<String, AuthorityUserinfo> userCache = new HashMap<String, AuthorityUserinfo>();
			for (String key : keys) {
				dtl = new BillOmOutstockDtl();
				String[] values = key.split("\\|");
				dtl.setLocno(locno);
				dtl.setOutstockNo(outstockNo);
				dtl.setOwnerNo(values[0]);
				dtl.setDivideId(Integer.valueOf(values[1]));
				dtl.setRealQty(new BigDecimal(Integer.valueOf(values[2])));
				dtl.setOutstockName(values[3]);
				dtl.setOutstockDate(now);
				AuthorityUserinfo user = authorityUserinfoService.findUserByCache(locno, values[3], userCache);
				if(user != null){
					dtl.setOutstockNameCh(user.getUsername());
				}
				billOmOutstockDtlService.modifyById(dtl);
			}
			omOutstock.setEditor(systemUser.getLoginName());
			omOutstock.setEditorname(systemUser.getUsername());
			omOutstock.setEdittm(new Date());
			billOmOutstockService.modifyById(omOutstock);
//			//记账存储过程扣减库存
//			BillAccControlDto controlDto = new BillAccControlDto();
//			controlDto.setiPaperNo(outstockNo);
//			controlDto.setiLocType("2");
//			controlDto.setiPaperType("HO");
//			controlDto.setiIoFlag("O");
//			controlDto.setiPrepareDataExt(new BigDecimal(0));
//			controlDto.setiIsWeb(new BigDecimal(1));
//			billAccControlService.procAccApply(controlDto);
//
//			
//			//记账存储过程扣减库存
//			BillAccControlDto controlDto2 = new BillAccControlDto();
//			controlDto2.setiPaperNo(outstockNo);
//			controlDto2.setiLocType("2");
//			controlDto2.setiPaperType("HO");
//			controlDto2.setiIoFlag("I");
//			controlDto2.setiPrepareDataExt(new BigDecimal(0));
//			controlDto2.setiIsWeb(new BigDecimal(1));
//			billAccControlService.procAccApply(controlDto2);
//			
//			
//			//更新主表信息  更改状态
//			BillOmOutstock stock = new BillOmOutstock();
//			stock.setLocno(locno);
//			stock.setOutstockNo(outstockNo);
//			stock.setStatus(STATUS13);
//			billOmOutstockService.modifyById(stock);
//			if (StringUtils.isEmpty(keyStr)) {
//				throw new ManagerException("明细不能为空");
//			}
//			
//			//更新明细的状态
//			dtl = new BillOmOutstockDtl();
//			dtl.setLocno(locno);
//			dtl.setOutstockNo(outstockNo);
//			dtl.setStatus(STATUS13);
//			billOmOutstockDtlService.modifyById(dtl);
			
			//扣减库存
//			Map<String, Object> mapParams = new HashMap<String, Object>();
//			mapParams.put("locno", locno);
//			mapParams.put("outstockNo", outstockNo);
//			List<BillOmOutstockDtl> listOds = billOmOutstockDtlService.findByBiz(new BillOmOutstockDtl(), mapParams);
//			if (CommonUtil.hasValue(listOds)) {
//				String msg = "";
//				for (BillOmOutstockDtl b : listOds) {
//					if (StringUtils.isNotBlank(b.getsCellNo()) && b.getsCellId() > 0) {
//						Map<String, String> maps = new HashMap<String, String>();
//						maps.put("I_locno", b.getLocno());
//						maps.put("I_cell_id", String.valueOf(b.getsCellId()));
//						maps.put("I_cell_no", b.getsCellNo());
//						maps.put("I_rcell_no", b.getdCellNo());
//						maps.put("I_nQty", String.valueOf(b.getRealQty()));
//						maps.put("I_noutstock_qty", String.valueOf(b.getRealQty()));
//						maps.put("I_User_id", outstockName);
//						maps.put("I_paper_no", b.getOutstockNo());
//						maps.put("I_terminal_flag", "1");
//						maps.put("I_nRow_id", conContentService.getNextvalId());
//						maps.put("I_nInsertLog_flag", "1");
//
//						conContentService.procUpdtContentqtyByCellID(maps);
//						if (!RESULTY.equals(maps.get("strOutMsg"))) {
//							String stroutmsg = maps.get("strOutMsg");
//							if (StringUtils.isNotBlank(stroutmsg)) {
//								String[] msgs = stroutmsg.split("\\|");
//								msg = msgs[1];
//							}
//							throw new ServiceException(msg);
//						}
//					}
//				}
//			}

		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void sendOrder(String locno, String assignName, String assignNameCh, String keyStr,SystemUser user ) throws ManagerException {
		try{
			String[] keys = keyStr.split(",");
			BillOmOutstockDtl dtl = null;
			BillOmOutstock omOutstock=new BillOmOutstock();
			
			for (String key : keys) {
				dtl = new BillOmOutstockDtl();
				String[] values = key.split("\\|");
				dtl.setLocno(locno);
				dtl.setOutstockNo(values[0]);
				dtl.setAssignName(assignName);
				dtl.setAssignNameCh(assignNameCh);
				billOmOutstockDtlService.modifyById(dtl);
				
				omOutstock=new BillOmOutstock();
				omOutstock.setOutstockNo(values[0]);
				omOutstock.setLocno(locno);
				omOutstock.setEditor(user.getLoginName());
				omOutstock.setEditorname(user.getUsername());
				omOutstock.setEdittm(new Date());
				billOmOutstockService.modifyById(omOutstock);
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		
	}

	@Override
	public void queryBill(List<BillOmOutstock> listOutstocks, SystemUser user) throws ManagerException {
		try{
			billOmOutstockService.queryBill(listOutstocks, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}


	@Override
	public SumUtilMap<String, Object> selectImmediateMoveSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmOutstockService.selectImmediateMoveSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billOmOutstockService.findSumQty(params, authorityParams);
	}
}