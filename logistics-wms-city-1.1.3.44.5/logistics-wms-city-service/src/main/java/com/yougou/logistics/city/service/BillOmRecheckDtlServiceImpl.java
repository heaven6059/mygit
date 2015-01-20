package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckDtlSizeKind;
import com.yougou.logistics.city.common.model.BillOmRecheckKey;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillOmDivideDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmRecheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmRecheckMapper;
import com.yougou.logistics.city.dal.mapper.BillSmOtherinDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.ConBoxDtlMapper;
import com.yougou.logistics.city.dal.mapper.ConBoxMapper;
import com.yougou.logistics.city.dal.mapper.ConLabelDtlMapper;
import com.yougou.logistics.city.dal.mapper.ConLabelMapper;

/**
 * 
 * 分货复核单明细service实现
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:22:10
 * @version 0.1.0
 * @copyright yougou.com
 */
@Service("billOmRecheckDtlService")
class BillOmRecheckDtlServiceImpl extends BaseCrudServiceImpl implements BillOmRecheckDtlService {

	@Resource
	private BillOmRecheckMapper billOmRecheckMapper;
	
	@Resource
	private BillOmRecheckDtlMapper billOmRecheckDtlMapper;
	
	@Resource
	private BillOmDivideDtlMapper billOmDivideDtlMapper;
	
	@Resource
	private ConLabelMapper conLabelMapper;
	
	@Resource
	private ConLabelDtlMapper conLabelDtlMapper;
	
	@Resource
	private ConBoxMapper conBoxMapper;
	
	@Resource
	private ConBoxDtlMapper conBoxDtlMapper;
	
	@Resource
	private BillOmOutstockDtlMapper billOmOutstockDtlMapper;
	
	@Resource
	private BillUmCheckDtlMapper billUmCheckDtlMapper;
	
	@Resource
	private BillSmOtherinDtlMapper billSmOtherinDtlMapper;
	
	private final static String STATUS10 = "10";
	
	private final static String STATUS13 = "13";
	
	private final static String SOURCETYPE0 = "0";
	private final static String SOURCETYPE1 = "1";
	private final static String SOURCETYPE2 = "2";
	private final static String SOURCETYPE3 = "3";
	private final static String RESULTY = "Y|";

	@Override
	public BaseCrudMapper init() {
		return billOmRecheckDtlMapper;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmRecheckDtl> findBillOmRecheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectBillOmRecheckDtlByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBillOmRecheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectBillOmRecheckDtlCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtl> findGroupByBoxAndItem(BillOmRecheckDtl dtl) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectGroupByBoxAndItem(dtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<BillOmRecheckDtl> findSizeNoDetail(BillOmRecheckDtl dtl) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectSizeNoDetail(dtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtl> findBillOmRecheckDtlByShowBox(Map<String, Object> params) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectBillOmRecheckDtlByShowBox(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmRecheckDtl> findRecheckDtlGroupByBoxPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckDtlMapper.selectRecheckDtlGroupByBoxPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findRecheckDtlGroupByBoxCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmRecheckDtlMapper.selectRecheckDtlGroupByBoxCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectRfSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectRfSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNo(BillOmRecheck recheck) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectDtlGroupByItemNo(recheck);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmRecheckDtlSizeKind> selectAllDtl4Print(BillOmRecheckDtlSizeKind recheck) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectAllDtl4Print(recheck);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<BillOmRecheckDtlSizeKind> selectDtlGroupByItemNoAndBox(BillOmRecheck recheck) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectDtlGroupByItemNoAndBox(recheck);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<BillOmRecheckDtlSizeKind> selectAllDtlBox4Print(BillOmRecheckDtlSizeKind recheck)
			throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectAllDtlBox4Print(recheck);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<String> selectAllDtlSizeKind(BillOmRecheck recheck) throws ServiceException {
		try {
			return billOmRecheckDtlMapper.selectAllDtlSizeKind(recheck);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int insertRecheckDtl4UmCheck(Map<String, Object> params)
			throws ServiceException {
		try {
			return billOmRecheckDtlMapper.insertRecheckDtl4UmCheck(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	

	@Override
	public int insertRecheckDtl4SmOtherin(Map<String, Object> params)
			throws ServiceException {
		try {
			return billOmRecheckDtlMapper.insertRecheckDtl4SmOtherin(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void updateOmRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> updateList,SystemUser user) throws ServiceException {
		try {
			recheck.setEditor(user.getLoginName());
			recheck.setEditorname(user.getUsername());
			recheck.setEdittm(new Date());
			billOmRecheckMapper.updateByPrimaryKeySelective(recheck);
			BillOmRecheckKey rechecKey = new BillOmRecheckKey();
			rechecKey.setLocno(recheck.getLocno());
			rechecKey.setRecheckNo(recheck.getRecheckNo());
			BillOmRecheck billOmRecheck = (BillOmRecheck)billOmRecheckMapper.selectByPrimaryKey(rechecKey);
			if(billOmRecheck == null||!STATUS10.equals(billOmRecheck.getStatus())){
				throw new ServiceException("单据:"+recheck.getRecheckNo()+"已删除或状态已改变!");
			}
//			if(!STATUS10.equals(billOmRecheck.getStatus())){
//				throw new ServiceException(recheck.getRecheckNo()+"状态已改变!");
//			}
			
			//保存明细 更新箱明细
			if(CommonUtil.hasValue(updateList)){
				Map<String, String> recheckMap = new HashMap<String, String>();
				for (BillOmRecheckDtl rd : updateList) {
					
					Map<String, Object> checkParams = new HashMap<String, Object>();
					checkParams.put("locno", recheck.getLocno());
					checkParams.put("recheckNo", recheck.getRecheckNo());
					checkParams.put("containerNo", rd.getContainerNo());
					checkParams.put("itemNo", rd.getItemNo());
					checkParams.put("sizeNo", rd.getSizeNo());
					BillOmRecheckDtl billOmRecheckDtl =billOmRecheckDtlMapper.selectRecheckDtlSumRealQty(checkParams);
					
					//更新了哪些箱号
					if(recheckMap.get(rd.getScanLabelNo())==null){
						recheckMap.put(rd.getScanLabelNo(), rd.getContainerNo());
					}
					
					if(billOmRecheckDtl.getRealQty()!=rd.getRealQty()){
						//装箱数量改小
						if(rd.getRealQty().intValue() < billOmRecheckDtl.getRealQty().intValue()){
							reduceNum(recheck, rd , billOmRecheckDtl.getRealQty().subtract(rd.getRealQty()));
						}
						//装箱数量改大
						if(rd.getRealQty().intValue() > billOmRecheckDtl.getRealQty().intValue()){
							increaseNum(recheck, rd,rd.getRealQty().subtract(billOmRecheckDtl.getRealQty()));
						}
					}
				}
				
				//验证实际数量是否大于计划数量
				Map<String, Object> checkParams = new HashMap<String, Object>();
				checkParams.put("locno", recheck.getLocno());
				checkParams.put("recheckNo", recheck.getRecheckNo());
				checkParams.put("divideNo", recheck.getDivideNo());
				checkParams.put("storeNo", recheck.getStoreNo());
				int divideItemQty = billOmDivideDtlMapper.selectDivideDtlItemQtySum(checkParams);
				int recheckRealQty = billOmRecheckDtlMapper.selectCheckRecheckDtlRealQtySum(checkParams);
				if(recheckRealQty > divideItemQty){
					throw new ServiceException("门店"+recheck.getStoreNo()+"不能超量填写装箱数量,请检查是否装箱完成!");
				}
				
				//开始删除扣减完成后的数量为0的明细
				for(Map.Entry<String, String> entry: recheckMap.entrySet()) {
					 String boxNo = entry.getKey();
					 String containerNo = entry.getValue();
					 deleteRecheckJoinInfo(recheck, boxNo, containerNo,0);
				}
				
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void deleteOmRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> deleteList,SystemUser user) throws ServiceException {
		try{
			recheck.setEditor(user.getLoginName());
			recheck.setEditorname(user.getUsername());
			recheck.setEdittm(new Date());
			billOmRecheckMapper.updateByPrimaryKeySelective(recheck);
			BillOmRecheckKey rechecKey = new BillOmRecheckKey();
			rechecKey.setLocno(recheck.getLocno());
			rechecKey.setRecheckNo(recheck.getRecheckNo());
			BillOmRecheck billOmRecheck = (BillOmRecheck)billOmRecheckMapper.selectByPrimaryKey(rechecKey);
			if(billOmRecheck == null||!STATUS10.equals(billOmRecheck.getStatus())){
				throw new ServiceException("单据:"+recheck.getRecheckNo()+"已删除或状态已改变!");
			}
			
			//删除明细 更新箱明细
			if(CommonUtil.hasValue(deleteList)){
				Map<String, String> recheckMap = new HashMap<String, String>();
				for (BillOmRecheckDtl rd : deleteList) {
					
					//更新了哪些箱号
					if(recheckMap.get(rd.getScanLabelNo())==null){
						recheckMap.put(rd.getScanLabelNo(), rd.getContainerNo());
					}
					
					//清空关联的数据
					Map<String, Object> checkParams = new HashMap<String, Object>();
					checkParams.put("locno", recheck.getLocno());
					checkParams.put("recheckNo", recheck.getRecheckNo());
					checkParams.put("containerNo", rd.getContainerNo());
					checkParams.put("itemNo", rd.getItemNo());
					checkParams.put("sizeNo", rd.getSizeNo());
					BillOmRecheckDtl billOmRecheckDtl =billOmRecheckDtlMapper.selectRecheckDtlSumRealQty(checkParams);
					if(billOmRecheckDtl == null){
						throw new ServiceException("删除复核明细失败!");
					}
					
					//清空关联的数据
					reduceNum(recheck, rd, billOmRecheckDtl.getRealQty());
				}
				
				//开始删除扣减完成后的数量为0的明细
				for(Map.Entry<String, String> entry: recheckMap.entrySet()) {
					 String boxNo = entry.getKey();
					 String containerNo = entry.getValue();
					 deleteRecheckJoinInfo(recheck, boxNo, containerNo,0);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void updateOmOutstockRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> updateList, List<BillOmRecheckDtl> insertList,SystemUser user)
			throws ServiceException {
		
		try{
			Date now = new Date();
			recheck.setEditor(user.getLoginName());
			recheck.setEditorname(user.getUsername());
			recheck.setEdittm(now);
			billOmRecheckMapper.updateByPrimaryKeySelective(recheck);
			BillOmRecheckKey rechecKey = new BillOmRecheckKey();
			rechecKey.setLocno(recheck.getLocno());
			rechecKey.setRecheckNo(recheck.getRecheckNo());
			BillOmRecheck billOmRecheck = (BillOmRecheck)billOmRecheckMapper.selectByPrimaryKey(rechecKey);
			if(!STATUS10.equals(billOmRecheck.getStatus())){
				throw new ServiceException("只能保存主档状态是<建单>的单据明细!");
			}
			billOmRecheck.setEditor(user.getLoginName());
			billOmRecheck.setEditorname(user.getUsername());
			billOmRecheck.setEdittm(now);
			recheck.setSourceType(billOmRecheck.getSourceType());
			
			
			//保存明细 更新箱明细
			if(CommonUtil.hasValue(updateList)){
				Map<String, String> recheckMap = new HashMap<String, String>();
				for (BillOmRecheckDtl rd : updateList) {
					//更新了哪些箱号
					if(recheckMap.get(rd.getScanLabelNo())==null){
						recheckMap.put(rd.getScanLabelNo(), rd.getContainerNo());
					}
					//回滚装箱操作的数量
					outstockUpdateNum(recheck, rd, rd.getRealQty(), 0);
				}
				
				//开始删除扣减完成后的数量为0的明细
				for(Map.Entry<String, String> entry: recheckMap.entrySet()) {
					 String boxNo = entry.getKey();
					 String containerNo = entry.getValue();
					 deleteRecheckJoinInfo(recheck, boxNo, containerNo,1);
				}
			}
			
			//保存明细 新增明细 封箱操作
			if(CommonUtil.hasValue(insertList)){
				if(StringUtils.isEmpty(recheck.getBoxNo())){
					throw new ServiceException("箱号不能为空!");
				}
				billOmRecheck.setBoxNo(recheck.getBoxNo());
				packageBox(billOmRecheck, insertList);
			}
			
			//验证实际数量是否大于计划数量
			Map<String, Object> checkParams = new HashMap<String, Object>();
			checkParams.put("locno", recheck.getLocno());
			checkParams.put("recheckNo", recheck.getRecheckNo());
			checkParams.put("locateNo", recheck.getDivideNo());
			checkParams.put("divideNo", recheck.getDivideNo());
			checkParams.put("storeNo", recheck.getStoreNo());
			int outstockItemQty = 0;
			int recheckRealQty = billOmRecheckDtlMapper.selectCheckRecheckDtlRealQtySum(checkParams);
			if(SOURCETYPE1.equals(recheck.getSourceType())||SOURCETYPE3.equals(recheck.getSourceType())){
				outstockItemQty = billUmCheckDtlMapper.selectCheckDtlRecheckQtySum(checkParams);
			}else if(SOURCETYPE2.equals(recheck.getSourceType())){
				outstockItemQty = billSmOtherinDtlMapper.selectOtherinDtlRecheckQtySum(checkParams);
			}else{
				outstockItemQty = billOmOutstockDtlMapper.selectOutstockDtlRecheckQtySum(checkParams);
			}
			if(recheckRealQty > outstockItemQty){
				throw new ServiceException("门店"+recheck.getStoreNo()+"不能超量填写装箱数量,请检查是否装箱完成!");
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void deleteOmOutstockRecheckDtl(BillOmRecheck recheck, List<BillOmRecheckDtl> deleteList,SystemUser user)
			throws ServiceException {
		try{
			recheck.setEditor(user.getLoginName());
			recheck.setEditorname(user.getUsername());
			recheck.setEdittm(new Date());
			billOmRecheckMapper.updateByPrimaryKeySelective(recheck);
			BillOmRecheckKey rechecKey = new BillOmRecheckKey();
			rechecKey.setLocno(recheck.getLocno());
			rechecKey.setRecheckNo(recheck.getRecheckNo());
			BillOmRecheck billOmRecheck = (BillOmRecheck)billOmRecheckMapper.selectByPrimaryKey(rechecKey);
			if(!STATUS10.equals(billOmRecheck.getStatus())){
				throw new ServiceException("只能删除主档状态是<建单>的单据明细!");
			}
			recheck.setSourceType(billOmRecheck.getSourceType());
			
			//删除明细 更新箱明细
			if(CommonUtil.hasValue(deleteList)){
				Map<String, String> recheckMap = new HashMap<String, String>();
				for (BillOmRecheckDtl rd : deleteList) {
					//更新了哪些箱号
					if(recheckMap.get(rd.getScanLabelNo())==null){
						recheckMap.put(rd.getScanLabelNo(), rd.getContainerNo());
					}
					
					//清空关联的数据
					Map<String, Object> checkParams = new HashMap<String, Object>();
					checkParams.put("locno", recheck.getLocno());
					checkParams.put("recheckNo", recheck.getRecheckNo());
					checkParams.put("containerNo", rd.getContainerNo());
					checkParams.put("itemNo", rd.getItemNo());
					checkParams.put("sizeNo", rd.getSizeNo());
					BillOmRecheckDtl billOmRecheckDtl =billOmRecheckDtlMapper.selectRecheckDtlSumRealQty(checkParams);
					
					//回滚装箱操作的数量
					outstockUpdateNum(recheck, rd, billOmRecheckDtl.getRealQty(), 1);
				}
				
				//开始删除扣减完成后的数量为0的明细
				for(Map.Entry<String, String> entry: recheckMap.entrySet()) {
					 String boxNo = entry.getKey();
					 String containerNo = entry.getValue();
					 deleteRecheckJoinInfo(recheck, boxNo, containerNo,1);
				}
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}	
			
	}

	/**
	 * 清空数量0、删除为0的记录
	 * @param recheck
	 * @param boxNo
	 * @param containerNo
	 * @param recheckType 0-分货复核
	 * @throws DaoException
	 */
	public void deleteRecheckJoinInfo(BillOmRecheck recheck,String boxNo,String containerNo,int recheckType) throws DaoException{
		//1.删除箱明细
		 Map<String, Object> delBoxParams = new HashMap<String, Object>();
		 delBoxParams.put("locno", recheck.getLocno());
		 delBoxParams.put("boxNo", boxNo);
		 conBoxDtlMapper.deleteBoxDtl4Recheck(delBoxParams);
		 conBoxMapper.deleteBox4Recheck(delBoxParams);
		 
		 //1-2.更新箱主表的汇总数量
		 conBoxMapper.updateBoxSumQty4Recheck(delBoxParams);
		 
		 //2.删除箱标签明细
		 Map<String, Object> delLabelParams = new HashMap<String, Object>();
		 delLabelParams.put("locno", recheck.getLocno());
		 delLabelParams.put("containerNo", containerNo);
		 conLabelDtlMapper.deleteConLabelDtl4Recheck(delLabelParams);
		 conLabelMapper.deleteConLabel4Recheck(delLabelParams);
		 
		 if(recheckType == 0){
			 //3.删除分货复核串款串码明细
			 Map<String, Object> delDivideParams = new HashMap<String, Object>();
			 delDivideParams.put("locno", recheck.getLocno());
			 delDivideParams.put("divideNo", recheck.getDivideNo());
			 delDivideParams.put("storeNo", recheck.getStoreNo());
			 billOmDivideDtlMapper.deleteDivideDtl4Recheck(delDivideParams);
		 }
		 
		 //4.删除复核明细
		 Map<String, Object> delRecheckParams = new HashMap<String, Object>();
		 delRecheckParams.put("locno", recheck.getLocno());
		 delRecheckParams.put("recheckNo", recheck.getRecheckNo());
		 delRecheckParams.put("containerNo", containerNo);
		 billOmRecheckDtlMapper.deleteRecheckDtlByRealQty(delRecheckParams);
	}

	//减少数量
	public void reduceNum(BillOmRecheck recheck,BillOmRecheckDtl rd ,BigDecimal subNum) throws ServiceException, DaoException{
		Date now = new Date();
		//更新回写装箱后的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locno", recheck.getLocno());
		params.put("recheckNo", recheck.getRecheckNo());
		params.put("containerNo", rd.getContainerNo());
		params.put("itemNo", rd.getItemNo());
		params.put("sizeNo", rd.getSizeNo());
		List<BillOmRecheckDtl> recheckDtlList = billOmRecheckDtlMapper.selectByParams(null, params);
		boolean result = false;//是否结束当前循环
		BigDecimal packNumIn = new BigDecimal(0);//装箱数量
		BigDecimal losePackNum = subNum;//剩余装箱的数量
		for (BillOmRecheckDtl rdEntity : recheckDtlList) {
			//数量分摊
			if(losePackNum.compareTo(rdEntity.getRealQty())==1){
				packNumIn = rdEntity.getRealQty();
				losePackNum = losePackNum.subtract(rdEntity.getRealQty());
			}else{
				packNumIn = losePackNum;
				result = true;
			}
			
			//1.开始更新复核数量
			BigDecimal inRealQty = rdEntity.getRealQty().subtract(packNumIn);
			rdEntity.setRealQty(inRealQty);
			rdEntity.setRecheckName(recheck.getEditor());
			rdEntity.setRechecknamech(recheck.getEditorname());
			rdEntity.setRecheckDate(now);
			int rcount = billOmRecheckDtlMapper.updateByPrimaryKeySelective(rdEntity);
			if(rcount < 1){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号："+rd.getScanLabelNo()+",");
				sb.append("商品："+rd.getItemNo()+",");
				sb.append("尺码："+rd.getSizeNo()+",");
				sb.append("更新复核明细数量失败!");
				throw new ServiceException(sb.toString());
			}
			
			//2.更新分货明细复核数量(需要分摊分货明细,有可能存在一箱多单的情况)
			Map<String, Object> divideParams = new HashMap<String, Object>();
			divideParams.put("locno", rdEntity.getLocno());
			divideParams.put("divideNo", recheck.getDivideNo());
			divideParams.put("storeNo", recheck.getStoreNo());
			divideParams.put("boxNo", rdEntity.getBoxNo());
			divideParams.put("itemNo", rdEntity.getItemNo());
			divideParams.put("sizeNo", rdEntity.getSizeNo());
			divideParams.put("expNo", rdEntity.getExpNo());
			List<BillOmDivideDtl> divideDtlList = billOmDivideDtlMapper.selectDivideDtl4Recheck(divideParams);
			if(!CommonUtil.hasValue(divideDtlList)){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + rd.getScanLabelNo() + ",");
				sb.append("商品：" + rd.getItemNo() + ",");
				sb.append("尺码：" + rd.getSizeNo() + ",");
				sb.append("查找分货明细失败!");
				throw new ServiceException(sb.toString());
			}
			
			boolean result2 = false;//是否结束当前循环
			BigDecimal packNumIn2 = new BigDecimal(0);//装箱数量
			BigDecimal losePackNum2 = packNumIn;//需要扣减的数量
			for (BillOmDivideDtl divideDtl : divideDtlList) {
				//数量分摊
				if(divideDtl.getRealQty().intValue() > 0){
					if(losePackNum2.compareTo(divideDtl.getRealQty())==1){
						packNumIn2 = divideDtl.getRealQty();
						losePackNum2 = losePackNum2.subtract(divideDtl.getRealQty());
					}else{
						packNumIn2 = losePackNum2;
						result2 = true;
					}
					if(divideDtl.getRealQty().compareTo(packNumIn2) == -1){
						StringBuffer sb = new StringBuffer();
						sb.append("箱号："+rd.getScanLabelNo()+",");
						sb.append("商品："+rd.getItemNo()+",");
						sb.append("尺码："+rd.getSizeNo()+",");
						sb.append("扣减分货单明细复核数量可能出现负数!");
						throw new ServiceException(sb.toString());
					}
					BigDecimal inDivideRealQty = divideDtl.getRealQty().subtract(packNumIn2);
					divideDtl.setRealQty(inDivideRealQty);
					divideDtl.setStatus(STATUS10);
					int dcount = billOmDivideDtlMapper.updateByPrimaryKeySelective(divideDtl);
					if(dcount < 1){
						StringBuffer sb = new StringBuffer();
						sb.append("箱号："+rd.getScanLabelNo()+",");
						sb.append("商品："+rd.getItemNo()+",");
						sb.append("尺码："+rd.getSizeNo()+",");
						sb.append("更新分货明细复核数量失败!");
						throw new ServiceException(sb.toString());
					}
					
					//分摊完了跳出循环
					if(result2){
						break;
					}
				}
			}
			
			//3.更新标签明细表
			Map<String, Object> labelParams = new HashMap<String, Object>();
			labelParams.put("locno", rdEntity.getLocno());
			labelParams.put("containerNo", rdEntity.getContainerNo());
			//labelParams.put("divideId", divideDtl.getDivideId());
			labelParams.put("divideId", rdEntity.getRowId());
			labelParams.put("itemNo", rdEntity.getItemNo());
			labelParams.put("sizeNo", rdEntity.getSizeNo());
			ConLabelDtl labelDtl = conLabelDtlMapper.selectConLabelDtl4Recheck(labelParams);
			if(labelDtl == null){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + rd.getScanLabelNo() + ",");
				sb.append("商品：" + rd.getItemNo() + ",");
				sb.append("尺码：" + rd.getSizeNo() + ",");
				sb.append("查找箱标签明细失败!");
				throw new ServiceException(sb.toString());
			}
			if(labelDtl.getQty().compareTo(packNumIn) == -1){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号："+rd.getScanLabelNo()+",");
				sb.append("商品："+rd.getItemNo()+",");
				sb.append("尺码："+rd.getSizeNo()+",");
				sb.append("扣减箱标签明细数量可能出现负数!");
				throw new ServiceException(sb.toString());
			}
			BigDecimal inLabelQty = labelDtl.getQty().subtract(packNumIn);
			labelDtl.setQty(inLabelQty);
			int ccount = conLabelDtlMapper.updateByPrimaryKeySelective(labelDtl);
			if(ccount < 1){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号："+rd.getScanLabelNo()+",");
				sb.append("商品："+rd.getItemNo()+",");
				sb.append("尺码："+rd.getSizeNo()+",");
				sb.append("更新箱标签明细数量失败!");
				throw new ServiceException(sb.toString());
			}
			
			//4.更新箱明细数量
			Map<String, Object> boxParams = new HashMap<String, Object>();
			boxParams.put("locno", rdEntity.getLocno());
			boxParams.put("boxNo", rd.getScanLabelNo());
			boxParams.put("itemNo", rdEntity.getItemNo());
			boxParams.put("sizeNo", rdEntity.getSizeNo());
			ConBoxDtl boxDtl = conBoxDtlMapper.selectBoxDtl4Recheck(boxParams);
			if(boxDtl == null){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + rd.getScanLabelNo() + ",");
				sb.append("商品：" + rd.getItemNo() + ",");
				sb.append("尺码：" + rd.getSizeNo() + ",");
				sb.append("查找箱明细失败!");
				throw new ServiceException(sb.toString());
			}
			if(boxDtl.getQty().compareTo(packNumIn) == -1){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号："+rd.getScanLabelNo()+",");
				sb.append("商品："+rd.getItemNo()+",");
				sb.append("尺码："+rd.getSizeNo()+",");
				sb.append("扣减箱明细数量可能出现负数!");
				throw new ServiceException(sb.toString());
			}
			BigDecimal inBoxQty = boxDtl.getQty().subtract(packNumIn);
			boxDtl.setQty(inBoxQty);
			int bcount = conBoxDtlMapper.updateByPrimaryKeySelective(boxDtl);
			if(bcount < 1){
				StringBuffer sb = new StringBuffer();
				sb.append("箱号："+rd.getScanLabelNo()+",");
				sb.append("商品："+rd.getItemNo()+",");
				sb.append("尺码："+rd.getSizeNo()+",");
				sb.append("更新箱明细数量失败!");
				throw new ServiceException(sb.toString());
			}
			
			//分摊完了跳出循环
			if(result){
				break;
			}
		}
	}
	
	//增加数量
	public void increaseNum(BillOmRecheck recheck, BillOmRecheckDtl rd,BigDecimal addNum) throws ServiceException, DaoException {
		Date now =new Date();
		//更新回写装箱后的数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locno", recheck.getLocno());
		params.put("recheckNo", recheck.getRecheckNo());
		params.put("containerNo", rd.getContainerNo());
		params.put("itemNo", rd.getItemNo());
		params.put("sizeNo", rd.getSizeNo());
		BillOmRecheckDtl rdEntity = billOmRecheckDtlMapper.selectRecheckDtlIncreaseNum(params);
		if(rdEntity == null){
			StringBuffer sb = new StringBuffer();
			sb.append("箱号："+rd.getScanLabelNo()+",");
			sb.append("商品："+rd.getItemNo()+",");
			sb.append("尺码："+rd.getSizeNo()+",");
			sb.append("查找需要更新的复核明细失败!");
			throw new ServiceException(sb.toString());
		}
		
		//1.开始更新复核数量
		BigDecimal inRealQty = rdEntity.getRealQty().add(addNum);
		if (inRealQty.compareTo(rdEntity.getItemQty()) == 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("更新的复核数量不能大于计划数量!");
			throw new ServiceException(sb.toString());
		}
		rdEntity.setRealQty(inRealQty);
		rdEntity.setRecheckName(recheck.getEditor());
		rdEntity.setRechecknamech(recheck.getEditorname());
		rdEntity.setRecheckDate(now);
		int rcount = billOmRecheckDtlMapper.updateByPrimaryKeySelective(rdEntity);
		if (rcount < 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("更新复核明细数量失败!");
			throw new ServiceException(sb.toString());
		}

		//2.更新分货明细复核数量(存在一箱多单的情况，需要分摊)
		Map<String, Object> divideParams = new HashMap<String, Object>();
		divideParams.put("locno", recheck.getLocno());
		divideParams.put("divideNo", recheck.getDivideNo());
		divideParams.put("storeNo", recheck.getStoreNo());
		divideParams.put("boxNo", rdEntity.getBoxNo());
		divideParams.put("itemNo", rdEntity.getItemNo());
		divideParams.put("sizeNo", rdEntity.getSizeNo());
		divideParams.put("expNo", rdEntity.getExpNo());
		List<BillOmDivideDtl> divideDtlList = billOmDivideDtlMapper.selectDivideDtl4Recheck(divideParams);
		if(!CommonUtil.hasValue(divideDtlList)){
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("查找分货明细失败!");
			throw new ServiceException(sb.toString());
		}
		
		int surplusNum = addNum.intValue();
		BigDecimal divideQtyIn = new BigDecimal(0);//实际写入的复核数量
		for (BillOmDivideDtl divideDtl : divideDtlList) {
			if(divideDtl.getItemQty().intValue() > divideDtl.getRealQty().intValue()){
				BigDecimal realQty = divideDtl.getRealQty()==null?new BigDecimal(0):divideDtl.getRealQty();
				if((realQty.intValue()+surplusNum) > divideDtl.getItemQty().intValue()){
					divideQtyIn = new BigDecimal(divideDtl.getItemQty().intValue()-realQty.intValue());
					surplusNum = surplusNum - divideQtyIn.intValue();
				}else{
					divideQtyIn = new BigDecimal(surplusNum);
					surplusNum = 0;
				}
				
				BigDecimal inDivideRealQty = divideDtl.getRealQty().add(divideQtyIn);
				if (inDivideRealQty.compareTo(divideDtl.getItemQty()) == 1) {
					StringBuffer sb = new StringBuffer();
					sb.append("箱号：" + rd.getScanLabelNo() + ",");
					sb.append("商品：" + rd.getItemNo() + ",");
					sb.append("尺码：" + rd.getSizeNo() + ",");
					sb.append("不能超量装箱!");
					throw new ServiceException(sb.toString());
				}
				
				divideDtl.setRealQty(inDivideRealQty);
				if(inDivideRealQty.compareTo(divideDtl.getItemQty()) == 0){
					divideDtl.setStatus(STATUS13);
				}
				int dcount = billOmDivideDtlMapper.updateByPrimaryKeySelective(divideDtl);
				if (dcount < 1) {
					StringBuffer sb = new StringBuffer();
					sb.append("箱号：" + rd.getScanLabelNo() + ",");
					sb.append("商品：" + rd.getItemNo() + ",");
					sb.append("尺码：" + rd.getSizeNo() + ",");
					sb.append("更新分货明细数量失败!");
					throw new ServiceException(sb.toString());
				}
				
				//分摊完成退出循环
				if(surplusNum == 0){
					break;
				}
			}
		}
		
		//3.更新标签明细表
		Map<String, Object> labelParams = new HashMap<String, Object>();
		labelParams.put("locno", rdEntity.getLocno());
		labelParams.put("containerNo", rdEntity.getContainerNo());
		//labelParams.put("divideId", divideDtl.getDivideId());
		labelParams.put("divideId", rdEntity.getRowId());
		labelParams.put("itemNo", rdEntity.getItemNo());
		labelParams.put("sizeNo", rdEntity.getSizeNo());
		ConLabelDtl labelDtl = conLabelDtlMapper.selectConLabelDtl4Recheck(labelParams);
		if(labelDtl == null){
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("查找箱标签明细失败!");
			throw new ServiceException(sb.toString());
		}
		BigDecimal inLabelQty = labelDtl.getQty().add(addNum);
		labelDtl.setQty(inLabelQty);
		int ccount = conLabelDtlMapper.updateByPrimaryKeySelective(labelDtl);
		if (ccount < 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("更新箱标签明细数量失败!");
			throw new ServiceException(sb.toString());
		}

		//4.更新箱明细数量
		Map<String, Object> boxParams = new HashMap<String, Object>();
		boxParams.put("locno", rdEntity.getLocno());
		boxParams.put("boxNo", rd.getScanLabelNo());
		boxParams.put("itemNo", rdEntity.getItemNo());
		boxParams.put("sizeNo", rdEntity.getSizeNo());
		ConBoxDtl boxDtl = conBoxDtlMapper.selectBoxDtl4Recheck(boxParams);
		if(boxDtl == null){
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("查找箱明细失败!");
			throw new ServiceException(sb.toString());
		}
		BigDecimal inBoxQty = boxDtl.getQty().add(addNum);
		boxDtl.setQty(inBoxQty);
		int bcount = conBoxDtlMapper.updateByPrimaryKeySelective(boxDtl);
		if (bcount < 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("更新箱明细数量失败!");
			throw new ServiceException(sb.toString());
		}
		
	}
	
	/**
	 * 拣货复核调整数量
	 * @param recheck
	 * @param rd
	 * @param num  调整的装箱数量
	 * @param type 类型0调整数量大小，1删除明细
	 * @throws ServiceException
	 * @throws DaoException
	 */
	public void outstockUpdateNum(BillOmRecheck recheck, BillOmRecheckDtl rd,BigDecimal num,int type) throws ServiceException, DaoException {
		String editor = recheck.getEditor();
		String editorname = recheck.getEditorname();
		Date now = recheck.getEdittm();
		
		int calcType = 0;
		//查询商品装箱数量汇总
		Map<String, Object> checkParams = new HashMap<String, Object>();
		checkParams.put("locno", recheck.getLocno());
		checkParams.put("recheckNo", recheck.getRecheckNo());
		checkParams.put("containerNo", rd.getContainerNo());
		checkParams.put("itemNo", rd.getItemNo());
		checkParams.put("sizeNo", rd.getSizeNo());
		BillOmRecheckDtl billOmRecheckDtl = billOmRecheckDtlMapper.selectRecheckDtlSumRealQty(checkParams);
		if(billOmRecheckDtl == null){
			throw new ServiceException("查询复核明细数据为空!");
		}
		
		
		//更新复核明细数量,需要分摊，因为RF有可能除了ROWID 其他数据都相同
		Map<String, Object> paramsList = new HashMap<String, Object>();
		paramsList.put("locno", recheck.getLocno());
		paramsList.put("recheckNo", recheck.getRecheckNo());
		paramsList.put("containerNo", rd.getContainerNo());
		paramsList.put("itemNo", rd.getItemNo());
		paramsList.put("sizeNo", rd.getSizeNo());
		List<BillOmRecheckDtl> recheckDtlList = billOmRecheckDtlMapper.selectByParams(null, paramsList);
		if (!CommonUtil.hasValue(recheckDtlList)) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("查找需要更新的复核明细失败!");
			throw new ServiceException(sb.toString());
		}
				
		
		//判断数量加多还是减少
		BigDecimal realQty = billOmRecheckDtl.getRealQty();
		
		//来源类型：0正常复核，1直通复核，2转货复核
		//0正常复核：回写拣货单的复核数量
		//1直通复核：回写退仓验收单明细的复核数量
		//2转货复核：回写其他入库单的复核数量
		if(SOURCETYPE1.equals(recheck.getSourceType())||SOURCETYPE3.equals(recheck.getSourceType())){
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", recheck.getLocno());
			params.put("checkNo", recheck.getDivideNo());
			params.put("itemNo", rd.getItemNo());
			params.put("sizeNo", rd.getSizeNo());
			List<BillUmCheckDtl> checkDtlList=billUmCheckDtlMapper.selectByParams(null, params);
			if(!CommonUtil.hasValue(checkDtlList)){
				throw new ServiceException("验收单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"查找退仓验收单失败!");
			}
			BillUmCheckDtl checkDtl = checkDtlList.get(0);
			BigDecimal inRecheckQty = new BigDecimal(0);
			BigDecimal packNumIn = new BigDecimal(0);
			int calcNumType = 0;
			//回写退仓验收单复核数量
			if (realQty.compareTo(num) == -1) {
				packNumIn = num.subtract(realQty);//增加数量
				//如果增加的复核数量大于实际的验收数量拦截
				inRecheckQty = checkDtl.getRecheckQty().add(packNumIn);
				if(inRecheckQty.compareTo(checkDtl.getCheckQty())==1){
					throw new ServiceException("验收单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"复核数量不能大于实际验收数量!");
				}
			}else{
				packNumIn = type==1?realQty:realQty.subtract(num);//需要扣减的复核数量
				inRecheckQty = checkDtl.getRecheckQty().subtract(packNumIn);
				if(inRecheckQty.intValue() < 0){
					throw new ServiceException("验收单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"复核数量不能小于0!");
				}
				calcNumType = 1;
				calcType = 1;
			}
			checkDtl.setRecheckQty(inRecheckQty);
			int count = billUmCheckDtlMapper.updateByPrimaryKeySelective(checkDtl);
			if(count < 1){
				throw new ServiceException("验收单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"更新验收明细复核数量失败!");
			}
			
			
			
			outstockDtlOtherNum(recheckDtlList, rd, null, packNumIn, calcNumType);
			
		}else if(SOURCETYPE2.equals(recheck.getSourceType())){
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", recheck.getLocno());
			params.put("otherinNo", recheck.getDivideNo());
			params.put("itemNo", rd.getItemNo());
			params.put("sizeNo", rd.getSizeNo());
			List<BillSmOtherinDtl> otherinDtlList=billSmOtherinDtlMapper.selectByParams(null, params);
			if(!CommonUtil.hasValue(otherinDtlList)){
				throw new ServiceException("其他入库单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"查找其他入库单明细失败!");
			}
			BillSmOtherinDtl otherinDtl = otherinDtlList.get(0);
			BigDecimal inRecheckQty = new BigDecimal(0);
			BigDecimal packNumIn = new BigDecimal(0);
			int calcNumType = 0;
			//回写退仓验收单复核数量
			if (realQty.compareTo(num) == -1) {
				packNumIn = num.subtract(realQty);//增加数量
				//如果增加的复核数量大于实际的验收数量拦截
				inRecheckQty = otherinDtl.getRecheckQty().add(packNumIn);
				if(inRecheckQty.compareTo(otherinDtl.getInstorageQty())==1){
					throw new ServiceException("其他入库单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"复核数量不能大于实际入库数量!");
				}
			}else{
				packNumIn = type==1?realQty:realQty.subtract(num);//需要扣减的复核数量
				inRecheckQty = otherinDtl.getRecheckQty().subtract(packNumIn);
				if(inRecheckQty.intValue() < 0){
					throw new ServiceException("其他入库单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"复核数量不能小于0!");
				}
				calcNumType = 1;
				calcType = 1;
			}
			otherinDtl.setRecheckQty(inRecheckQty);
			int count = billSmOtherinDtlMapper.updateByPrimaryKeySelective(otherinDtl);
			if(count < 1){
				throw new ServiceException("其他入库单:"+recheck.getDivideNo()+"商品："+rd.getItemNo()+"尺码："+rd.getSizeNo()+"更新验收明细复核数量失败!");
			}
			outstockDtlOtherNum(recheckDtlList, rd, null, packNumIn, calcNumType);
			
		}else{
			//数量加多、执行增量操作
			if (realQty.compareTo(num) == -1) {
				//查询拣货单明细outstockDtl
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", recheck.getLocno());
				params.put("locateNo", recheck.getDivideNo());
				params.put("storeNo", recheck.getStoreNo());
				params.put("itemNo", rd.getItemNo());
				params.put("sizeNo", rd.getSizeNo());
				List<BillOmOutstockDtl> listOutstockDtl = billOmOutstockDtlMapper.selectOutstockDtl4Recheck(params);
				if(!CommonUtil.hasValue(listOutstockDtl)){
					StringBuffer sb = new StringBuffer();
					sb.append("箱号：" + rd.getScanLabelNo() + ",");
					sb.append("商品：" + rd.getItemNo() + ",");
					sb.append("尺码：" + rd.getSizeNo() + ",");
					sb.append("查询拣货明细数据失败,请检查该门店是否装箱完成!");
					throw new ServiceException(sb.toString());
				}
				
				boolean result = false;//是否结束当前循环
				BigDecimal totalOperNum = new BigDecimal(0);
				BigDecimal packNumIn = num.subtract(realQty);//装箱数量
				BigDecimal losePackNum = num.subtract(realQty);//剩余装箱的数量
				for (BillOmOutstockDtl bo : listOutstockDtl) {
					if (bo.getRealQty().intValue() > bo.getRecheckQty().intValue()) {
						if (losePackNum.compareTo(bo.getRealQty().subtract(bo.getRecheckQty())) == 1) {
							packNumIn = bo.getRealQty().subtract(bo.getRecheckQty());
							losePackNum = losePackNum.subtract(bo.getRealQty().subtract(bo.getRecheckQty()));
						} else {
							packNumIn = losePackNum;
							result = true;
						}

						BigDecimal inRecheckQty = packNumIn.add(bo.getRecheckQty());
						BillOmOutstockDtl uOutstockDtl = new BillOmOutstockDtl();
						uOutstockDtl.setLocno(bo.getLocno());
						uOutstockDtl.setOwnerNo(bo.getOwnerNo());
						uOutstockDtl.setDivideId(bo.getDivideId());
						uOutstockDtl.setOutstockNo(bo.getOutstockNo());
						uOutstockDtl.setRecheckQty(inRecheckQty);
						int oCount = billOmOutstockDtlMapper.updateByPrimaryKeySelective(uOutstockDtl);
						if (oCount < 1) {
							throw new ServiceException("更新拣货单明细复核数量失败");
						}
						
						//操作标签表、箱表数据
						//outstockDtlOtherNum(recheckDtlList, rd, null, packNumIn, 0);
						totalOperNum = totalOperNum.add(packNumIn);
						if (result) {
							break;
						}
					}
				}
				
				outstockDtlOtherNum(recheckDtlList, rd, null, totalOperNum, 0);
				
			} else {
				calcType = 1;
				//查询拣货单明细outstockDtl
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", recheck.getLocno());
				params.put("locateNo", recheck.getDivideNo());
				params.put("storeNo", recheck.getStoreNo());
				params.put("itemNo", rd.getItemNo());
				params.put("sizeNo", rd.getSizeNo());
				List<BillOmOutstockDtl> listOutstockDtl = billOmOutstockDtlMapper.selectOutstockDtl4RecheckReduceNum(params);
				if(!CommonUtil.hasValue(listOutstockDtl)){
					throw new ServiceException("拣货明细数据为空!");
				}
				
				//如果是数量减少了或者类型是删除的
				BigDecimal totalOperNum = new BigDecimal(0);
				if (realQty.compareTo(num) == 1 || type == 1) {
					boolean result = false;//是否结束当前循环
					BigDecimal packNumIn = new BigDecimal(0);//写入的数量
					BigDecimal losePackNum = type==1?realQty:realQty.subtract(num);//需要操作的数量
					for (BillOmOutstockDtl bo : listOutstockDtl) {
						//数量分摊
						if(losePackNum.compareTo(bo.getRecheckQty())==1){
							packNumIn = bo.getRecheckQty();
							losePackNum = losePackNum.subtract(bo.getRecheckQty());
						}else{
							packNumIn = losePackNum;
							result = true;
						}
						
						BigDecimal inRecheckQty = bo.getRecheckQty().subtract(packNumIn);
						BillOmOutstockDtl uOutstockDtl = new BillOmOutstockDtl();
						uOutstockDtl.setLocno(bo.getLocno());
						uOutstockDtl.setOwnerNo(bo.getOwnerNo());
						uOutstockDtl.setDivideId(bo.getDivideId());
						uOutstockDtl.setOutstockNo(bo.getOutstockNo());
						uOutstockDtl.setRecheckQty(inRecheckQty);
						int oCount = billOmOutstockDtlMapper.updateByPrimaryKeySelective(uOutstockDtl);
						if (oCount < 1) {
							throw new ServiceException("更新拣货单明细复核数量失败");
						}
						totalOperNum = totalOperNum.add(packNumIn);
						//操作标签表、箱表数据
						//outstockDtlOtherNum(recheckDtlList, rd, null, packNumIn, 1);

						if (result) {
							break;
						}
					}
					
					outstockDtlOtherNum(recheckDtlList, rd, null, totalOperNum, 1);
					
				}
			}
		}
		
		
		boolean result = false;//是否结束当前循环
		BigDecimal packNumIn = new BigDecimal(0);//装箱数量
		BigDecimal losePackNum = new BigDecimal(0);//剩余装箱的数量
		if (calcType == 0) {
			losePackNum = num.subtract(realQty);
		}else{
			//如果是删除 直接扣除汇总数量
			if(type == 1){
				losePackNum = realQty;
			}else{
				losePackNum = realQty.subtract(num);
			}
		}
		//需要数量分摊，因为RF输入箱号封箱，会在复核明细表生成2条相同的明细除ROWID不同外
		for (BillOmRecheckDtl rdEntity : recheckDtlList) {
			BigDecimal inRealQty = new BigDecimal(0);
			if(calcType == 1){
				//数量分摊
				if(losePackNum.compareTo(rdEntity.getRealQty())==1){
					packNumIn = rdEntity.getRealQty();
					losePackNum = losePackNum.subtract(rdEntity.getRealQty());
				}else{
					packNumIn = losePackNum;
					result = true;
				}
				
				//1.开始更新复核数量
				inRealQty = rdEntity.getRealQty().subtract(packNumIn);
			}else{
				inRealQty = rdEntity.getRealQty().add(num.subtract(realQty));
				result = true;
			}
			
			rdEntity.setRealQty(inRealQty);
			rdEntity.setEditor(editor);
			rdEntity.setEditorname(editorname);
			rdEntity.setEdittm(now);
			int rcount = billOmRecheckDtlMapper.updateByPrimaryKeySelective(rdEntity);
			if (rcount < 1) {
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + rd.getScanLabelNo() + ",");
				sb.append("商品：" + rd.getItemNo() + ",");
				sb.append("尺码：" + rd.getSizeNo() + ",");
				sb.append("更新复核明细数量失败!");
				throw new ServiceException(sb.toString());
			}
			
			if(result){
				break;
			}
		}
		
//		BillOmRecheckDtl rdEntity = recheckDtlList.get(0);
//		//1.开始更新复核数量
//		BigDecimal inRealQty = new BigDecimal(0);
//		if (calcType == 0) {
//			inRealQty = rdEntity.getRealQty().add(num.subtract(realQty));
//		} else {
//			//如果是删除数据
//			if(type == 1){
//				inRealQty = rdEntity.getRealQty().subtract(num);
//			}else{
//				inRealQty = rdEntity.getRealQty().subtract(realQty.subtract(num));
//			}
//		}
//		rdEntity.setRealQty(inRealQty);
//		int rcount = billOmRecheckDtlMapper.updateByPrimaryKeySelective(rdEntity);
//		if (rcount < 1) {
//			StringBuffer sb = new StringBuffer();
//			sb.append("箱号：" + rd.getScanLabelNo() + ",");
//			sb.append("商品：" + rd.getItemNo() + ",");
//			sb.append("尺码：" + rd.getSizeNo() + ",");
//			sb.append("更新复核明细数量失败!");
//			throw new ServiceException(sb.toString());
//		}
	}
	
	
	/**
	 * 计算扣减其他数量
	 * @param recheck
	 * @param rd
	 * @param num
	 * @param calcType 0-相加、1-相减 
	 * @throws ServiceException
	 * @throws DaoException
	 */
	public void outstockDtlOtherNum(List<BillOmRecheckDtl> recheckDtlList, BillOmRecheckDtl rd,BigDecimal divideId, BigDecimal num,int calcType)
			throws ServiceException, DaoException {
		
		boolean result = false;//是否停止循环
		BigDecimal packNumIn = new BigDecimal(0);//装箱数量
		BigDecimal losePackNum = num;//剩余装箱的数量
		//直通复核和转货复核，标签明细数量分摊，因为RF输入箱号封箱，会在复核明细表生成2条相同的明细除ROWID不同外
		for (BillOmRecheckDtl bd : recheckDtlList) {
			//更新标签明细表
			BigDecimal inLabelQty = new BigDecimal(0);
			Map<String, Object> labelParams = new HashMap<String, Object>();
			labelParams.put("locno", rd.getLocno());
			labelParams.put("containerNo", rd.getContainerNo());
			//labelParams.put("divideId", divideId==null?bd.getRowId():divideId);//直通复核和转货复核divideId用的是复核明细的rowid
			labelParams.put("divideId", bd.getRowId());
			labelParams.put("itemNo", rd.getItemNo());
			labelParams.put("sizeNo", rd.getSizeNo());
			ConLabelDtl labelDtl = conLabelDtlMapper.selectConLabelDtl4Recheck(labelParams);
			if (labelDtl == null) {
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + rd.getScanLabelNo() + ",");
				sb.append("商品：" + rd.getItemNo() + ",");
				sb.append("尺码：" + rd.getSizeNo() + ",");
				sb.append("查找箱标签明细失败!");
				throw new ServiceException(sb.toString());
			}
			
			//如果是数量相减需要分摊减掉封箱的数量，相加任意获取一条
			if(calcType == 1){
				//数量分摊
				if(losePackNum.compareTo(bd.getRealQty())==1){
					packNumIn = bd.getRealQty();
					losePackNum = losePackNum.subtract(bd.getRealQty());
				}else{
					packNumIn = losePackNum;
					result = true;
				}
				inLabelQty = labelDtl.getQty().subtract(packNumIn);
			}else{
				inLabelQty = labelDtl.getQty().add(num);
				result = true;
			}
			
			labelDtl.setQty(inLabelQty);
			int ccount = conLabelDtlMapper.updateByPrimaryKeySelective(labelDtl);
			if (ccount < 1) {
				StringBuffer sb = new StringBuffer();
				sb.append("箱号：" + rd.getScanLabelNo() + ",");
				sb.append("商品：" + rd.getItemNo() + ",");
				sb.append("尺码：" + rd.getSizeNo() + ",");
				sb.append("更新箱标签明细数量失败!");
				throw new ServiceException(sb.toString());
			}
			
			if(result){
				break;
			}
		}
		
//		//2.更新标签明细表
//		Map<String, Object> labelParams = new HashMap<String, Object>();
//		labelParams.put("locno", rd.getLocno());
//		labelParams.put("containerNo", rd.getContainerNo());
//		labelParams.put("divideId", divideId);
//		labelParams.put("itemNo", rd.getItemNo());
//		labelParams.put("sizeNo", rd.getSizeNo());
//		ConLabelDtl labelDtl = conLabelDtlMapper.selectConLabelDtl4Recheck(labelParams);
//		if (labelDtl == null) {
//			StringBuffer sb = new StringBuffer();
//			sb.append("箱号：" + rd.getScanLabelNo() + ",");
//			sb.append("商品：" + rd.getItemNo() + ",");
//			sb.append("尺码：" + rd.getSizeNo() + ",");
//			sb.append("查找箱标签明细失败!");
//			throw new ServiceException(sb.toString());
//		}
//		BigDecimal inLabelQty = new BigDecimal(0);
//		if (calcType == 0) {
//			inLabelQty = labelDtl.getQty().add(num);
//		} else {
//			inLabelQty = labelDtl.getQty().subtract(num);
//		}
//		labelDtl.setQty(inLabelQty);
//		int ccount = conLabelDtlMapper.updateByPrimaryKeySelective(labelDtl);
//		if (ccount < 1) {
//			StringBuffer sb = new StringBuffer();
//			sb.append("箱号：" + rd.getScanLabelNo() + ",");
//			sb.append("商品：" + rd.getItemNo() + ",");
//			sb.append("尺码：" + rd.getSizeNo() + ",");
//			sb.append("更新箱标签明细数量失败!");
//			throw new ServiceException(sb.toString());
//		}
//		

		//4.更新箱明细数量
		Map<String, Object> boxParams = new HashMap<String, Object>();
		boxParams.put("locno", rd.getLocno());
		boxParams.put("boxNo", rd.getScanLabelNo());
		boxParams.put("itemNo", rd.getItemNo());
		boxParams.put("sizeNo", rd.getSizeNo());
		ConBoxDtl boxDtl = conBoxDtlMapper.selectBoxDtl4Recheck(boxParams);
		if (boxDtl == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("查找箱明细失败!");
			throw new ServiceException(sb.toString());
		}
		BigDecimal inBoxQty = new BigDecimal(0);
		if (calcType == 0) {
			inBoxQty = boxDtl.getQty().add(num);
		} else {
			inBoxQty = boxDtl.getQty().subtract(num);
		}
		boxDtl.setQty(inBoxQty);
		int bcount = conBoxDtlMapper.updateByPrimaryKeySelective(boxDtl);
		if (bcount < 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("箱号：" + rd.getScanLabelNo() + ",");
			sb.append("商品：" + rd.getItemNo() + ",");
			sb.append("尺码：" + rd.getSizeNo() + ",");
			sb.append("更新箱明细数量失败!");
			throw new ServiceException(sb.toString());
		}

	}
	
	//添加明细封箱
	public void packageBox(BillOmRecheck billOmRecheck,List<BillOmRecheckDtl> insertList) throws ServiceException, DaoException{
		String editor = billOmRecheck.getEditor();
		String editorname = billOmRecheck.getEditorname();
		Date now = billOmRecheck.getEdittm();
		//验证RF是否在封箱操作
		Map<String, Object> mapParams = new HashMap<String, Object>();
		mapParams.put("locno", billOmRecheck.getLocno());
		mapParams.put("recheckNo", billOmRecheck.getRecheckNo());
		mapParams.put("containerNo", "N");
		List<BillOmRecheckDtl> listCheckDtls = billOmRecheckDtlMapper.selectByParams(null, mapParams);
		if(CommonUtil.hasValue(listCheckDtls)){
			throw new ServiceException("RF正在进行封箱操作,不能封箱!");
		}
		
		//1.查询最大的ROWID
		BillOmRecheckDtl bDtl = new BillOmRecheckDtl();
		bDtl.setLocno(billOmRecheck.getLocno());
		bDtl.setRecheckNo(billOmRecheck.getRecheckNo());
		int rowId = billOmRecheckDtlMapper.selectMaxPid(bDtl);
		
		//2.新增复核明细表
		//验证添加的明细是否有重复项
		Map<String, String> mapCheck = new HashMap<String, String>();
		int count = 0;
		if(!(SOURCETYPE0).equals(billOmRecheck.getSourceType())){
			for (BillOmRecheckDtl d : insertList) {
				String key = d.getItemNo()+"|"+d.getSizeNo();
				if(mapCheck.get(key) != null){
					throw new ServiceException("箱号:"+billOmRecheck.getBoxNo()+",商品:"+d.getItemNo()+",尺码:"+d.getSizeNo()+"新增重复!");
				}else{
					mapCheck.put(key, key);
				}
				
				++rowId;
				BillOmRecheckDtl dtl = new BillOmRecheckDtl();
				dtl.setRowId(Long.valueOf(rowId));
				dtl.setLocno(billOmRecheck.getLocno());
				dtl.setRecheckNo(billOmRecheck.getRecheckNo());
				dtl.setContainerNo("N");
				dtl.setItemNo(d.getItemNo());
				dtl.setItemQty(new BigDecimal(0));
				dtl.setAssignName(billOmRecheck.getCreator());
				dtl.setOwnerNo(d.getOwnerNo());
				dtl.setPackQty(d.getPackQty()!=null?d.getPackQty():new BigDecimal(1));
				dtl.setRealQty(d.getRealQty());
				dtl.setSizeNo(d.getSizeNo());
				dtl.setStatus("10");
				dtl.setExpDate(new Date());
				dtl.setRecheckName(billOmRecheck.getCreator());
				dtl.setRecheckDate(new Date());
				dtl.setBoxNo(d.getBoxNo());
				dtl.setBrandNo(d.getBrandNo());
				dtl.setEditor(editor);
				dtl.setEditorname(editorname);
				dtl.setEdittm(now);
				count = billOmRecheckDtlMapper.insertSelective(dtl);
				if(count < 1){
					throw new ServiceException("新增复核明细失败");
				}
				
				//3.回写验收单的复核数量
				if ((SOURCETYPE1).equals(billOmRecheck.getSourceType())||(SOURCETYPE3).equals(billOmRecheck.getSourceType())) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", billOmRecheck.getLocno());
					params.put("onwerNo", d.getOwnerNo());
					params.put("checkNo", billOmRecheck.getDivideNo());
					params.put("itemNo", d.getItemNo());
					params.put("sizeNo", d.getSizeNo());
					List<BillUmCheckDtl> checkDtlList = billUmCheckDtlMapper.selectByParams(null, params);
					if(!CommonUtil.hasValue(checkDtlList)){
						throw new ServiceException("回写验收明细复核数量,查找验收单明细失败!");
					}
					BillUmCheckDtl checkDtl = checkDtlList.get(0);
					BigDecimal inRecheckQty = checkDtl.getRecheckQty().add(d.getRealQty());
					checkDtl.setRecheckQty(inRecheckQty);
					count = billUmCheckDtlMapper.updateByPrimaryKeySelective(checkDtl);
					if(count < 1){
						throw new ServiceException("商品:"+checkDtl.getItemNo()+",尺码:"+checkDtl.getSizeNo()+"更新复核明细复核数量失败");
					}
					if(inRecheckQty.compareTo(checkDtl.getCheckQty()) == 1){
						throw new ServiceException("验收单:"+checkDtl.getCheckNo()+"商品："+checkDtl.getItemNo()+"尺码："+checkDtl.getSizeNo()+"复核数量不能大于实际验收数量!");
					}
					
				}
				
				//4.其他入库单的复核数量
				if ((SOURCETYPE2).equals(billOmRecheck.getSourceType())) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", billOmRecheck.getLocno());
					params.put("onwerNo", d.getOwnerNo());
					params.put("otherinNo", billOmRecheck.getDivideNo());
					params.put("itemNo", d.getItemNo());
					params.put("sizeNo", d.getSizeNo());
					List<BillSmOtherinDtl> smOtherinDtlList = billSmOtherinDtlMapper.selectByParams(null, params);
					if(!CommonUtil.hasValue(smOtherinDtlList)){
						throw new ServiceException("回写其他入库单复核数量,查找验收单明细失败!");
					}
					BillSmOtherinDtl otherinDtl = smOtherinDtlList.get(0);
					BigDecimal inRecheckQty = otherinDtl.getRecheckQty().add(d.getRealQty());
					otherinDtl.setRecheckQty(inRecheckQty);
					count = billSmOtherinDtlMapper.updateByPrimaryKeySelective(otherinDtl);
					if(count < 1){
						throw new ServiceException("商品:"+otherinDtl.getItemNo()+",尺码:"+otherinDtl.getSizeNo()+"更新复核明细复核数量失败");
					}
					if(inRecheckQty.compareTo(otherinDtl.getInstorageQty()) == 1){
						throw new ServiceException("其他入库单:"+otherinDtl.getOtherinNo()+"商品："+otherinDtl.getItemNo()+"尺码："+otherinDtl.getSizeNo()+"复核数量不能大于实际入库数量!");
					}
				}
			}
			
			//5.封箱
			String msg = "";
			Map<String, String> map = new HashMap<String, String>();
   			map.put("I_locno", billOmRecheck.getLocno());
   			map.put("I_recheckNo", billOmRecheck.getRecheckNo());
   			map.put("I_boxNo", billOmRecheck.getBoxNo());
   			map.put("I_creator", billOmRecheck.getCreator());
   			billOmRecheckDtlMapper.procDirectRecheckSealBox(map);
   			if (!RESULTY.equals(map.get("O_msg"))) {
   				String stroutmsg = map.get("O_msg");
   				if(StringUtils.isNotBlank(stroutmsg)){
   					String[] msgs = stroutmsg.split("\\|");
   					msg = msgs[1];
   				}
   				throw new ServiceException(msg);
   			}
   			
		}
		
		
	}
	
}