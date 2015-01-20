package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmExpDtlKey;
import com.yougou.logistics.city.common.model.BillOmExpKey;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.service.BillOmExpDtlService;
import com.yougou.logistics.city.service.BillOmExpService;
import com.yougou.logistics.city.service.ProcCommonService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-09-29 16:50:42
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
@Service("billOmExpManager")
class BillOmExpManagerImpl extends BaseCrudManagerImpl implements BillOmExpManager {
	
    @Resource
    private ProcCommonService  procCommonService;
	
    @Resource
    private BillOmExpService billOmExpService;
    
    @Resource
    private  BillOmExpDtlService  billOmExpDtlService;
    
    private final static String SOURCE_TYPE_1 = "1";
	
	private final static String SPLIT_STATUS_1 = "1";
	
	private final static String SOURCE_TYPE_0 = "0";
	
	private final static String SPLIT_STATUS_0 = "0";
	
	private final static String STATUS10 = "10";
    
    @Log
    private Logger log;

    @Override
    public BaseCrudService init() {
        return billOmExpService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public boolean deleteBillOmExp(String noStrs) throws ManagerException {
		try{
			if(StringUtils.isNotBlank(noStrs)){
				String [] strs = noStrs.split(",");
				for(String obj : strs){
					String [] imImportArray = obj.split("#");
						if(imImportArray.length>2){
							String expNo = imImportArray[0];
							String locno = imImportArray[1];
							String ownerNo = imImportArray[2];
							String expType = imImportArray[3];
							
							//如果有是拆分单据,回退拆分状态su.yq
							BillOmExpKey expKey = new BillOmExpKey();
							expKey.setLocno(locno);
							expKey.setExpNo(expNo);
							expKey.setExpType(expType);
							BillOmExp exp = (BillOmExp)billOmExpService.findById(expKey);
							if(exp == null||!(STATUS10).equals(exp.getStatus())){
								throw new ManagerException("单据"+expNo+"已删除或状态已改变!");
							}
							if(SOURCE_TYPE_1.equals(exp.getSourceType())){
								BillOmExp upExp = new BillOmExp();
								upExp.setLocno(exp.getLocno());
								upExp.setExpNo(exp.getSourceNo());
								upExp.setExpType(exp.getExpType());
								upExp.setSplitStatus(SPLIT_STATUS_0);
								int count = billOmExpService.modifyById(upExp);
								if(count < 1){
									throw new ManagerException(expNo+"回写发货单拆分单据状态失败！");
								}
							}
							
							//删除主表
							BillOmExp billOmExp = new  BillOmExp();
							billOmExp.setExpNo(expNo);
							billOmExp.setLocno(locno);
							billOmExp.setOwnerNo(ownerNo);
							billOmExp.setExpType(expType);
							int b = billOmExpService.deleteById(billOmExp);
							if(b < 1){
								throw new ManagerException("未删除到对应的信息！");
							}else{
								BillOmExpDtlKey billOmExpDtlKey = new  BillOmExpDtlKey();
								billOmExpDtlKey.setExpNo(expNo);
								billOmExpDtlKey.setLocno(locno);
								billOmExpDtlKey.setOwnerNo(ownerNo);
								//删除明细表
								billOmExpDtlService.deleteByPrimaryKey(billOmExpDtlKey);
							}
							
							
						}
				}
			}
		}catch (Exception e) {
            throw new ManagerException(e.getMessage());
        }
		return true;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String,Object> addBillOmExp(BillOmExp billOmExp)
			throws ManagerException {
		Map<String,Object> obj=new HashMap<String,Object>();
		try{
			 billOmExp.setStatus("10");//状态11-建单；10-审核；
			 billOmExp.setCreatetm(new Date());
			 billOmExp.setEdittm(new Date());
			//自定生成单号
			String expNo = procCommonService.procGetSheetNo(billOmExp.getLocno(),CNumPre.OM_EXPNO_PRE);
			billOmExp.setExpNo(expNo);
			int a = billOmExpService.add(billOmExp);
			if(a < 1){
				 throw new ManagerException("新增时未更新到记录！");
			}
			obj.put("returnMsg", true);
			obj.put("expNo", expNo);
			obj.put("locno", billOmExp.getLocno());
		}catch (Exception e) {
            throw new ManagerException(e);
        }
		return obj;
	}

	@Override
	public int findBillOmExpDispatchCount(BillOmExp billOmExp, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billOmExpService.findBillOmExpDispatchCount(billOmExp, authorityParams);
		}catch (Exception e) {
            throw new ManagerException(e);
        }
	}

	@Override
	public List<BillOmExp> findBillOmExpDispatchByPage(SimplePage page, String orderByField, String orderBy,
			BillOmExp billOmExp, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billOmExpService.findBillOmExpDispatchByPage(page, orderByField, orderBy, billOmExp, authorityParams);
		}catch (Exception e) {
            throw new ManagerException(e);
        }
	}

	@Override
	public Map<String, Object> procBillOmExpDispatchQuery(BillOmExp billOmExp) throws ManagerException {
		try {

			Map<String, Object> mapObj = new HashMap<String, Object>();
			String flag = "fail";
			String msg = "";

			if (StringUtils.isBlank(billOmExp.getLocno()) || StringUtils.isBlank(billOmExp.getOwnerNo())
					|| StringUtils.isBlank(billOmExp.getExpType()) || StringUtils.isBlank(billOmExp.getExpNo())
					|| StringUtils.isBlank(billOmExp.getCreator())) {
				flag = "warn";
				msg = "参数非法！";
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				return mapObj;
			}
			
			//查询待定位的发货通知单中，是否存在不同的品质和属性
			String[] expNos=billOmExp.getExpNo().split(",");
			if(billOmExpService.isDiffAttribute(billOmExp.getLocno(), expNos)){
				flag = "warn";
				msg = "所选单据品牌库、属性、品质不一致，不允许合并调度生成拣货波次！";
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
				return mapObj;
			}
			
			
			
			mapObj = billOmExpService.procBillOmExpDispatchQuery(billOmExp);
			return mapObj;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public BillOmExp findBillOmExpViewTotalQty(BillOmExp billOmExp) throws ManagerException {
		try{
			return billOmExpService.findBillOmExpViewTotalQty(billOmExp);
		}catch(Exception e){
			throw new ManagerException(e.getMessage());
		}
	}
	
	@Override
	public int findCountExp(BillOmExp params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmExpService.findCountExp(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<BillOmExp> findByPageExp(SimplePage page, String orderByField, String orderBy, BillOmExp params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmExpService.findByPageExp(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public void overExpBill(BillOmExp billOmExp) throws ManagerException {
		try{
			billOmExpService.overExpBill(billOmExp);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public void toClass0ForExp(BillOmExp billOmExp) throws ManagerException {
		try{
			billOmExpService.toClass0ForExp(billOmExp);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	@Override
	public void toClass1ForExp(BillOmExp billOmExp) throws ManagerException {
		try{
			billOmExpService.toClass1ForExp(billOmExp);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billOmExpService.findSumQty(params, authorityParams);
	}
	
	@Override
	public int findCountExpForPre(BillOmExp params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmExpService.findCountExpForPre(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<BillOmExp> findByPageExpForPre(SimplePage page, String orderByField, String orderBy, BillOmExp params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmExpService.findByPageExpForPre(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
}