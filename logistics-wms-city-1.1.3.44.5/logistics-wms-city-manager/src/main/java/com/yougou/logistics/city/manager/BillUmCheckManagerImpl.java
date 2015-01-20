package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.enums.BillUmCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillConConvertGoodsDtlService;
import com.yougou.logistics.city.service.BillConConvertGoodsService;
import com.yougou.logistics.city.service.BillOmRecheckDtlService;
import com.yougou.logistics.city.service.BillOmRecheckService;
import com.yougou.logistics.city.service.BillUmCheckDtlService;
import com.yougou.logistics.city.service.BillUmCheckService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 11 14:40:26 CST 2013
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
@Service("billUmCheckManager")
class BillUmCheckManagerImpl extends BaseCrudManagerImpl implements BillUmCheckManager {

	@Log
	private Logger log;
	@Resource
	private BillUmCheckService billUmCheckService;

	@Resource
	private BillUmCheckDtlService billUmCheckDtlService;
	@Resource
	private BillConConvertGoodsDtlService billConConvertGoodsDtlService;
	@Resource
	private BillConConvertGoodsService billConConvertGoodsService;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private BillOmRecheckService billOmRecheckService;
	@Resource
	private BillOmRecheckDtlService billOmRecheckDtlService;
	
	private final static String STATUS10 = "10";
	
	private final static String STATUS30 = "30";
	    
	private final static String SOURCETYPE1 = "1";
	private final static String SOURCETYPE3 = "3";//转仓
	private final static String RECHECKTYPE1 = "1";
	
	/**
	 * 库存转货单类型
	 * 1、次品转货
	 * 2、部门转货
	 * 3、门店转货
	 * 4、转仓
	 */
	private final static String CONVERTTYPE0 = "0";
    private final static String CONVERTTYPE1 = "1";
    private final static String CONVERTTYPE2 = "2";
    private final static String CONVERTTYPE3 = "3";
    private final static String CONVERTTYPE4 = "4";
	private final static String CONVERTTYPE5 = "5";
	@Override
	public BaseCrudService init() {
		return billUmCheckService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void saveMain(BillUmCheck check, List<BillUmCheckDtl> insertList, List<BillUmCheckDtl> updateList,
			List<BillUmCheckDtl> deleteList) throws ManagerException {
		try {
			String checkNo = check.getCheckNo();
			billUmCheckService.saveMain(check);
			if (StringUtils.isEmpty(checkNo)) {
				billUmCheckDtlService.saveCheckDtl(insertList, updateList, deleteList, check);
			}

		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteBillUmCheck(List<BillUmCheck> listBillUmChecks) throws ManagerException {
		try {
			int count = 0;
			if (CommonUtil.hasValue(listBillUmChecks)) {
				for (BillUmCheck b : listBillUmChecks) {
					b.setCheckStatus(STATUS10); 
					int result = billUmCheckService.deleteById(b);
					if (result < 1) {
						throw new ManagerException("删除退仓验收单失败!");
					}
					BillUmCheckDtl checkDtl = new BillUmCheckDtl();
					checkDtl.setLocno(b.getLocno());
					checkDtl.setOwnerNo(b.getOwnerNo());
					checkDtl.setCheckNo(b.getCheckNo());
					billUmCheckDtlService.deleteById(checkDtl);
					count += result;
				}
			}
			return count;
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public Map<String, Object> procBillUmCheckAuditQuery(List<BillUmCheck> listBillUmChecks) throws ManagerException {
		try {

			Map<String, Object> mapObj = new HashMap<String, Object>();
			String flag = "fail";
			String msg = "";

			if (CommonUtil.hasValue(listBillUmChecks)) {
				mapObj = billUmCheckService.procBillUmCheckAuditQuery(listBillUmChecks);
			} else {
				flag = "warn";
				msg = "参数非法！";
				mapObj.put("flag", flag);
				mapObj.put("msg", msg);
			}
			return mapObj;
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	public void auditCheck(String keyStr, SystemUser user) throws ManagerException {
		try {
			billUmCheckService.auditCheck(keyStr, user);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int selectCountUmNoForInstock(Map map) throws ManagerException {
		try {
			return billUmCheckService.selectCountUmNoForInstock(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillUmCheck> selectByPageUmNoForInstock(SimplePage page, Map map) throws ManagerException {
		try {
			return billUmCheckService.selectByPageUmNoForInstock(page, map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectCountCheckNoForInstock(Map map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmCheckService.selectCountCheckNoForInstock(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillUmCheck> selectByPageCheckNoForInstock(SimplePage page, Map map, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckService.selectByPageCheckNoForInstock(page, map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectCount4loadBox(Map map, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmCheckService.selectCount4loadBox(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillUmCheck> select4loadBoxByPage(Map map, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckService.select4loadBoxByPage(map, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);  
		}
	}

	@Override
	public SumUtilMap<String, Object> selectUntreadJoinCheckDtlSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmCheckService.selectUntreadJoinCheckDtlSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectUntreadJoinCheckSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmCheckService.selectUntreadJoinCheckSumQty(map, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	/**
	 * 批量转货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> transferCargo(Map<String, Object> map,SystemUser currentUser)throws ManagerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			int status=0;
			String ownerNo=null;
			List<BillUmCheck> billUmCheckList=new ArrayList<BillUmCheck>();
			//查询退仓验收单
			String checkNos=(String) map.get("checkNos");//验收单号、货主
			String [] arrCheckNos=checkNos.split(",");
			String locno=(String) map.get("locno");//仓别
			String storeNo=(String) map.get("storeNo");//目标仓库
			String convertType=(String) map.get("convertType");//转货类型
			String dLocno=(String) map.get("dLocno");//转货仓库
			if(StringUtils.isEmpty(convertType)){
				status=4;
				log.error("转货类型不能为空");
			}else if(convertType.equals(CONVERTTYPE1)||convertType.equals(CONVERTTYPE2)||convertType.equals(CONVERTTYPE3)){
				if(StringUtils.isEmpty(dLocno)){
					status=3;
					log.error("目标仓库不能为空");
				}
				if(convertType.equals(CONVERTTYPE3)){
					if(StringUtils.isEmpty(storeNo)){
						status=5;
						log.error("目标门店不能为空");
					}
				}
			}
			if(status==0){
				for(int i=0;i<arrCheckNos.length;i++){
					String [] tempCheckNo=arrCheckNos[i].split(";");
					String checkNo=tempCheckNo[0];
					ownerNo=tempCheckNo[1];
					if(StringUtils.isEmpty(checkNo)){
						status=1;
						break;
					}
					BillUmCheck billUmCheck=new BillUmCheck();
					billUmCheck.setCheckNo(checkNo);
					billUmCheck.setOwnerNo(ownerNo);
					billUmCheck.setLocno(locno);
					billUmCheck=billUmCheckService.findById(billUmCheck);//获取退仓验收单
					if(billUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS11.getStatus())||billUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS13.getStatus())){
						billUmCheckList.add(billUmCheck);
					}else{
						status=2;
						log.error("退仓验收单必须是审核状态");
						break;
					}
				}
			}
			if(status==0){
				if(convertType.equals(CONVERTTYPE0)||convertType.equals(CONVERTTYPE1)||convertType.equals(CONVERTTYPE3)){//部门转货或门店转货
					//新增库存转货单
					String convertGoodsNo = procCommonService.procGetSheetNo(locno, CNumPre.CON_CONVERT_GOODS_PRE);
					BillConConvertGoods convertGoods=new BillConConvertGoods();
					convertGoods.setLocno(locno);
					convertGoods.setConvertGoodsNo(convertGoodsNo);
					convertGoods.setCreator(currentUser.getLoginName());
					convertGoods.setCreatorName(currentUser.getUsername());
					convertGoods.setCreatetm(new Date());
					convertGoods.setStoreNo(storeNo);
					convertGoods.setdLocno(dLocno);
					convertGoods.setConvertType(convertType);
					convertGoods.setOwnerNo(ownerNo);
					if(convertType.equals(CONVERTTYPE0)){
						convertGoods.setsQuality("0");
						convertGoods.setdQuality("A");
					}
					billConConvertGoodsService.add(convertGoods);//保存库存转货单主表
					billConConvertGoodsDtlService.saveConvertGoodsDtl(convertGoods,billUmCheckList,null);//保存库存转货单明细
					resultMap.put("result", "success");
					resultMap.put("tranType",1);
				}else if(convertType.equals(CONVERTTYPE2)){//转仓
					//新增复核单
					resultMap=this.transLocNo(billUmCheckList, resultMap,dLocno,currentUser);
				}
			}else{
				resultMap.put("result", "fail");
				resultMap.put("status", 500);
			}
		}catch(Exception e){
			throw new ManagerException(e.getMessage(), e);
		}
		return resultMap;
	}
	/**
	 * 转仓 
	 * @param map
	 * @param resultMap
	 * @return
	 */
	private  Map<String, Object> transLocNo(List<BillUmCheck> billUmCheckList,Map<String, Object> resultMap,String dLocno,SystemUser currentUser)throws ManagerException{
		Date date = new Date();
		String editor = currentUser.getLoginName();
		String editorname = currentUser.getUsername();
		try {
			for(BillUmCheck billUmCheck:billUmCheckList){
				billUmCheck.setCreator(editor);
				billUmCheck.setCreatorName(editorname);
				billUmCheck.setCreatetm(date);
				billUmCheck.setEditor(editor);
				billUmCheck.setEditorName(editorname);
				billUmCheck.setEdittm(date);
				String recheckNo = procCommonService.procGetSheetNo(billUmCheck.getLocno(), CNumPre.RECHECK_PRE);
				int count=this.addConvertRecheck(billUmCheck, dLocno, recheckNo,SOURCETYPE3);//新增主档
				if(count < 1){
					throw new ManagerException("新增复核单主档失败!");
				}
				Map<String, Object> checkParams = new HashMap<String, Object>();
				checkParams.put("locno", billUmCheck.getLocno());
				checkParams.put("ownerNo", billUmCheck.getOwnerNo());
				List<BillUmCheck> insertList = new ArrayList<BillUmCheck>();
				insertList.add(billUmCheck);
				List<BillUmCheckDtl> checkContentList = billUmCheckDtlService.selectCheckQtyJoinContent(checkParams, insertList);
				if(CommonUtil.hasValue(checkContentList)){
					BillUmCheckDtl ucd = checkContentList.get(0);
					StringBuffer sb = new StringBuffer();
					sb.append("验收单：" + billUmCheck.getCheckNo());
					sb.append("商品：" + ucd.getItemNo());
					sb.append(",尺码：" + ucd.getSizeNo());
					sb.append(",退货暂存区库存不足,库存缺量：" + ucd.getCheckQty());
					throw new ServiceException(sb.toString());
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", billUmCheck.getLocno());
				params.put("ownerNo", billUmCheck.getOwnerNo());
				params.put("checkNo", billUmCheck.getCheckNo());
				params.put("recheckNo", recheckNo);
				params.put("recheckName", billUmCheck.getCreator());
				int dcount = billOmRecheckDtlService.insertRecheckDtl4UmCheck(params);//新增明细
				if(dcount < 1){
					throw new ManagerException("新增复核单明细失败!");
				}
				//更新状态
				if(!STATUS30.equals(billUmCheck.getStatus())){
					billUmCheck.setStatus(STATUS30);
					billUmCheck.setConvertType(CONVERTTYPE4);
					int ucount = billUmCheckService.modifyById(billUmCheck);
					if(ucount < 1){
						throw new ManagerException(billUmCheck.getCheckNo()+"更新验收单状态为<已转货>失败!");
					}
				}
				//更新退仓验收单明细的复核数量=验收数量
				billUmCheckDtlService.updateRecheckQty4Convert(params);
				
				//装箱操作
				BillOmRecheck billOmRecheck = new BillOmRecheck();
				billOmRecheck.setLocno(billUmCheck.getLocno());
				billOmRecheck.setRecheckNo(recheckNo);
				billOmRecheck.setCreator(billUmCheck.getCreator());
				billOmRecheckService.packageBoxOutstockRf(billOmRecheck);
			}
			
			resultMap.put("result", "success");
			resultMap.put("tranType",2);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return resultMap;
	}
	@Override
	public int findBillUmCheckCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billUmCheckService.findBillUmCheckCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillUmCheck> findBillUmCheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billUmCheckService.findBillUmCheckByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public Map<String, Object> validateIsEnable(String checkNoStr, String locno,String ownerNo)throws ManagerException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		StringBuilder strBuilder1 = new StringBuilder();
		StringBuilder strBuilder2 = new StringBuilder();
		String flag = "true";
		String resultMsg = "";
		try {
		    if (StringUtils.isNotBlank(checkNoStr) && StringUtils.isNotBlank(locno) && StringUtils.isNotBlank(ownerNo)) {
				String[] strs = checkNoStr.split(",");
				for (String checkNo : strs) {
					BillUmCheck  b = new  BillUmCheck();
					b.setLocno(locno);
					b.setOwnerNo(ownerNo);
					b.setCheckNo(checkNo);
					//判断状态是否合法
					BillUmCheck resultObj = billUmCheckService.findById(b);
					if(!"11".equals(resultObj.getStatus())&&!"13".equals(resultObj.getStatus()) && !"30".equals(resultObj.getStatus())){
						strBuilder1.append(checkNo+",");
					}
					
					if("30".equals(resultObj.getStatus()) && "2".equals(resultObj.getConvertType())){//状态为已转货，且转货类型为门店转货
						//判断是否存在新建的直通复核单
						Map<String ,Object> params = new HashMap<String ,Object>();
						params.put("divideNo", checkNo);
						params.put("locno", locno);
						params.put("status", "10");
						params.put("sourceType", "1");
						List<BillOmRecheck> lstRecheck = billOmRecheckService.findByBiz(null, params);
						if(lstRecheck.size()>0){
							strBuilder2.append(checkNo+",");
						}
					}
				}
				// 如果有值
				if (strBuilder1.length() > 0 || strBuilder2.length() > 0){
					flag = "warn";
					if (strBuilder1.length() > 0) {
						strBuilder1.insert(0, "单号：");
						strBuilder1.append("状态已更改；");
					} 
					if (strBuilder2.length() > 0) {
						strBuilder2.insert(0, "单号：");
						strBuilder2.append("下有新建的直通复核单；");
					} 
					resultMsg = strBuilder1.toString()+strBuilder2.toString()+"不能进行上架定位！";
				}
		    }else{
		    	flag = "warn";
		    	resultMsg = "请求参数非法，请检查！";
		    }
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
		objMap.put("flag", flag);
		objMap.put("resultMsg", resultMsg);
		return objMap;
	}

	/**
	 * 门店批量转货
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void toStoreConvertRecheck(List<BillUmCheck> umCheckList, List<Store> storeList, SystemUser user) throws ManagerException {
		try{
			Date date = new Date();
			String editor = user.getLoginName();
			String editorname = user.getUsername();
			if(!CommonUtil.hasValue(storeList)){
				throw new ManagerException("选择的门店信息为空!");
			}
			if(!CommonUtil.hasValue(umCheckList)){
				throw new ManagerException("选择的验收单为空!");
			}
			if(storeList.size()>1 && umCheckList.size()>1){
				throw new ManagerException("门店转货不支持多对多选择!");
			}
			
			for (BillUmCheck billUmCheck : umCheckList) {
				
				BillUmCheck tempBillUmCheck = billUmCheckService.findById(billUmCheck);//获取退仓验收单
				if (!(tempBillUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS11.getStatus())
						|| tempBillUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS13.getStatus()) || tempBillUmCheck
						.getStatus().equals(BillUmCheckStatusEnums.STATUS30.getStatus()))) {
					throw new ManagerException("单据:" + billUmCheck.getCheckNo() + "目前所属的状态,不能门店转货!");
				}
				if (!StringUtils.isEmpty(tempBillUmCheck.getConvertType())) {
					if (!tempBillUmCheck.getConvertType().equals(BillUmCheckStatusEnums.STATUS2.getStatus())) {
						throw new ManagerException("单据:" + billUmCheck.getCheckNo() + "目前所属的状态,不能门店转货!");
					}
				}
				billUmCheck.setCreator(editor);
				billUmCheck.setCreatorName(editorname);
				billUmCheck.setCreatetm(date);
				billUmCheck.setEditor(editor);
				billUmCheck.setEditorName(editorname);
				billUmCheck.setEdittm(date);
				//是否只转一个门店
//				boolean isOneStore = false;
//				if(CommonUtil.hasValue(storeList)&&storeList.size()==1){
//					//多对一和一对一 单据状态为“已转货”的退仓验收单不支持二次转单；
//					if (tempBillUmCheck.getConvertType().equals(BillUmCheckStatusEnums.STATUS2.getStatus())
//							&& tempBillUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS30.getStatus())) {
//						throw new ManagerException("单据:" + billUmCheck.getCheckNo() + "多对一、一对一的验收单不支持二次转单!");
//					}
//					isOneStore = true;
//				}
			    
				//验证相同的验收单是否存在相同客户的复核单
				Map<String, Object> cparams = new HashMap<String, Object>();
				cparams.put("locno", billUmCheck.getLocno());
				cparams.put("divideNo", billUmCheck.getCheckNo());
				cparams.put("status", "10");
				cparams.put("sourceType", "1");
				cparams.put("recheckType", "1");
				List<BillOmRecheck> checkList = billOmRecheckService.selectRecheckByStoreAndCheckNo(cparams, storeList);
				if(CommonUtil.hasValue(checkList)){
					BillOmRecheck recheck = checkList.get(0);
					throw new ManagerException("验收单:"+recheck.getDivideNo()+",门店:"+recheck.getStoreName()+"  已有建单的直通复核单,不能重复生成!");
				}
				
				//验证是否转货完成
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", billUmCheck.getLocno());
				params.put("ownerNo", billUmCheck.getOwnerNo());
				params.put("checkNo", billUmCheck.getCheckNo());
				int validate = billUmCheckDtlService.selectValidateUmCheckIsRecheck(params);
				if(validate == 0){
					throw new ManagerException(billUmCheck.getCheckNo()+"验收单明细数量已全部复核,不能门店转货!");
				}
				
				//验证是否存在库存数据
				Map<String, Object> checkParams = new HashMap<String, Object>();
				checkParams.put("locno", billUmCheck.getLocno());
				checkParams.put("ownerNo", billUmCheck.getOwnerNo());
				List<BillUmCheck> insertList = new ArrayList<BillUmCheck>();
				insertList.add(billUmCheck);
				List<BillUmCheckDtl> checkContentList = billUmCheckDtlService.selectCheckQtyJoinContent(checkParams, insertList);
				if(CommonUtil.hasValue(checkContentList)){
					BillUmCheckDtl ucd = checkContentList.get(0);
					StringBuffer sb = new StringBuffer();
					sb.append("验收单：" + billUmCheck.getCheckNo());
					sb.append("商品：" + ucd.getItemNo());
					sb.append(",尺码：" + ucd.getSizeNo());
					sb.append(",退货暂存区库存不足,库存缺量：" + ucd.getCheckQty());
					throw new ServiceException(sb.toString());
				}
				
				String recheckNo = "";
				for (Store store : storeList) {
					//生成复核单主档
					recheckNo = procCommonService.procGetSheetNo(billUmCheck.getLocno(), CNumPre.RECHECK_PRE);
					int count = addConvertRecheck(billUmCheck, store.getStoreNo(),recheckNo,SOURCETYPE1);
					if(count < 1){
						throw new ManagerException("新增复核单失败!");
					}
				}
				
				//如果选择一个门店生成复核单明细，容器号为N
				if(umCheckList.size()>=1&&storeList.size()==1){
					params.put("recheckNo", recheckNo);
					params.put("edittm", date);
					params.put("editor", editor);
					params.put("editorName", editorname);
					int dcount = billOmRecheckDtlService.insertRecheckDtl4UmCheck(params);
					if(dcount < 1){
						throw new ManagerException("新增复核单明细失败!");
					}
					//更新退仓验收单明细的复核数量=验收数量
					billUmCheckDtlService.updateRecheckQty4Convert(params);
					
					//装箱操作
					BillOmRecheck billOmRecheck = new BillOmRecheck();
					billOmRecheck.setLocno(billUmCheck.getLocno());
					billOmRecheck.setRecheckNo(recheckNo);
					billOmRecheck.setCreator(billUmCheck.getCreator());
					billOmRecheck.setCreatorname(billUmCheck.getCreatorName());
					billOmRecheck.setCreatetm(billUmCheck.getCreatetm());
					billOmRecheckService.packageBoxOutstockRf(billOmRecheck);
				}
				
				//更新状态
				if(!STATUS30.equals(billUmCheck.getStatus())){
					billUmCheck.setStatus(STATUS30);
					billUmCheck.setConvertType(CONVERTTYPE2);
					int ucount = billUmCheckService.modifyById(billUmCheck);
					if(ucount < 1){
						throw new ManagerException(billUmCheck.getCheckNo()+"更新验收单状态为<已转货>失败!");
					}
				}
			}
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	/**
	 * 生成转货复核
	 * @param otherin
	 * @throws ServiceException 
	 */
	public int addConvertRecheck(BillUmCheck billUmCheck,String storeNo,String recheckNo,String sourceType) throws ServiceException{
		//String recheckNo = procCommonService.procGetSheetNo(billUmCheck.getLocno(), CNumPre.RECHECK_PRE);
		BillOmRecheck recheck = new BillOmRecheck();
		recheck.setLocno(billUmCheck.getLocno());
		recheck.setRecheckNo(recheckNo);
		recheck.setStoreNo(storeNo);
		recheck.setStatus(STATUS10);
		recheck.setCreator(billUmCheck.getCreator());
		recheck.setCreatetm(billUmCheck.getCreatetm());
		recheck.setCreatorname(billUmCheck.getCreatorName());
		recheck.setEditor(billUmCheck.getCreator());
		recheck.setEdittm(billUmCheck.getCreatetm());
		recheck.setEditorname(billUmCheck.getCreatorName());
		recheck.setExpDate(CommonUtil.getCurrentDateYmd());
		recheck.setDivideNo(billUmCheck.getCheckNo());
		recheck.setSourceType(sourceType);
		recheck.setRecheckType(RECHECKTYPE1);
		int count = billOmRecheckService.add(recheck);
		return count;
	}
	
	/**
	 * 属性转换
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> prpertyChange(Map<String, Object> map,SystemUser currentUser)throws ManagerException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String ownerNo=null;
			List<BillUmCheck> billUmCheckList=new ArrayList<BillUmCheck>();
			//查询退仓验收单
			String checkNos=(String) map.get("checkNos");//验收单号、货主
			String [] arrCheckNos=checkNos.split(",");
			String locno=(String) map.get("locno");//仓别
			String convertType=(String) map.get("convertType");//转货类型
			String itemTypevalues =(String) map.get("itemTypevalues");
			String propertyValue =(String) map.get("propertyValue"); //商品属性转换后类型
				for(int i=0;i<arrCheckNos.length;i++){
					String [] tempCheckNo=arrCheckNos[i].split(";");
					String checkNo=tempCheckNo[0];
					ownerNo=tempCheckNo[1];
					if(StringUtils.isEmpty(checkNo)){
						break;
					}
					BillUmCheck billUmCheck=new BillUmCheck();
					billUmCheck.setCheckNo(checkNo);
					billUmCheck.setOwnerNo(ownerNo);
					billUmCheck.setLocno(locno);
					billUmCheck=billUmCheckService.findById(billUmCheck);//获取退仓验收单
					if(billUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS11.getStatus())||billUmCheck.getStatus().equals(BillUmCheckStatusEnums.STATUS13.getStatus())){
						billUmCheckList.add(billUmCheck);
					}else{
						log.error("退仓验收单必须是验收状态");
						break;
					}
				}
				//新增库存转货单
				String convertGoodsNo = procCommonService.procGetSheetNo(locno, CNumPre.CON_CONVERT_GOODS_PRE);
				BillConConvertGoods convertGoods=new BillConConvertGoods();
				convertGoods.setLocno(locno);
				convertGoods.setConvertGoodsNo(convertGoodsNo);
				convertGoods.setCreator(currentUser.getLoginName());
				convertGoods.setCreatorName(currentUser.getUsername());
				convertGoods.setCreatetm(new Date());
				convertGoods.setConvertType(convertType);
				if(convertType.equals(CONVERTTYPE5)){
					convertGoods.setsQuality(itemTypevalues);
					convertGoods.setdQuality(propertyValue);
				}else{
					convertGoods.setsQuality(itemTypevalues);
					convertGoods.setdQuality(propertyValue);
				}

				convertGoods.setOwnerNo(ownerNo);
				billConConvertGoodsService.add(convertGoods);//保存库存转货单主表
				billConConvertGoodsDtlService.saveConvertGoodsDtl(convertGoods,billUmCheckList,null);//保存库存转货单明细
				resultMap.put("result", "success");
				resultMap.put("tranType",1);
		}catch(Exception e){
			throw new ManagerException(e.getMessage(), e);
		}
		return resultMap;
	}

}