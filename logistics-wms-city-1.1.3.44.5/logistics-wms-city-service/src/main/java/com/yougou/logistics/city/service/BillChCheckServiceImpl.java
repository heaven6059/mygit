package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillChCheckDirectDto;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.enums.BillChCheckAddFlagEnums;
import com.yougou.logistics.city.common.enums.BillChCheckCheckTypeEnums;
import com.yougou.logistics.city.common.enums.BillChCheckDefferentFlagEnums;
import com.yougou.logistics.city.common.enums.BillChCheckDirectStatusEnums;
import com.yougou.logistics.city.common.enums.BillChCheckDirectStautsEnums;
import com.yougou.logistics.city.common.enums.BillChCheckStautsEnums;
import com.yougou.logistics.city.common.enums.BillChCheckStautsTransEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChCheckKey;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanKey;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillChCheckDirectMapper;
import com.yougou.logistics.city.dal.mapper.BillChCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillChCheckMapper;
import com.yougou.logistics.city.dal.mapper.BillChPlanMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Dec 05 10:01:44 CST 2013
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
@Service("billChCheckService")
class BillChCheckServiceImpl extends BaseCrudServiceImpl implements BillChCheckService {
	@Resource
	private BillChCheckMapper billChCheckMapper;

	@Resource
	private BillChCheckDirectMapper billChCheckDirectMapper;
	@Resource
	private BillChPlanMapper billChPlanMapper;

	@Resource
	private BillChCheckDtlMapper billChCheckDtlMapper;
	
	@Resource
	private BillChCheckDtlService  billChCheckDtlService;

	@Resource
	private ProcCommonService procCommonService;

	@Resource
	private BillChPlanService billChPlanService;
	@Resource
	private AuthorityUserinfoService authorityUserinfoService;
	@Override
	public BaseCrudMapper init() {
		return billChCheckMapper;
	}

	private static final String STATUS20 = "20";

	private static final String STATUS25 = "25";
	
	private static final int  BATH_INSERT_COUNT = 1000;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void distributionAssignNoBatch(String locno, String checkNosStr, String assignNo,int systemId,int areaSystemId) throws ServiceException {
		try {
			if (StringUtils.isNotBlank(checkNosStr)) {
				String[] checkNos = checkNosStr.split(",");
				BillChCheck check = null;
				BillChCheck oldCheck = null;
				BillChCheckKey billChCheckKey = null;
                //盘点盘点用户是否存在
				AuthorityUserinfo userinfo = authorityUserinfoService.findUserByLoginName(locno, assignNo, systemId, areaSystemId);
				if(userinfo == null){
					throw new ServiceException("盘点人员【" + assignNo + "】不存在，请选择正确的盘点人员。");
				}
				for (String checkNo : checkNos) {

					//校验盘点单状态是否为建单、已发单
					billChCheckKey = new BillChCheckKey();
					billChCheckKey.setLocno(locno);
					billChCheckKey.setCheckNo(checkNo);
					oldCheck = (BillChCheck) billChCheckMapper.selectByPrimaryKey(billChCheckKey);
					if (!oldCheck.getStatus().equals(BillChCheckStautsEnums.STATUS10.getStatus())
							&& !oldCheck.getStatus().equals(BillChCheckStautsEnums.STATUS20.getStatus())) {
						throw new ServiceException("盘点单【" + checkNo + "】状态已改变，分配失败。");
					}

					check = new BillChCheck();
					check.setLocno(locno);
					check.setCheckNo(checkNo);
					check.setAssignNo(assignNo);
					check.setAssignName(userinfo.getUsername());
					check.setStatus(BillChCheckStautsEnums.STATUS20.getStatus());
					// check.setSourceStatus(BillChCheckStautsEnums.STATUS10
					// .getStatus());
					// 更新盘点单主表盘点人员
					int count = billChCheckMapper.updateByPrimaryKeySelective(check);
					if (count == 0) {
						throw new ServiceException("盘点单【" + checkNo + "】已发单");
					}
					// 更新盘点单从表盘点人员
					billChCheckDtlMapper.updateCheckWorkerAndStatusBatch(check);
				}
			} else {
				throw new ServiceException("参数错误");
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void createBillChCheck(BillChCheckDirectDto dto, String check_date, Integer stockCount, Integer cellCount,
			String creator,String creatorName) throws ServiceException {
		try {
			//查询盘点计划单
			BillChPlanKey billChPlanKey = new BillChPlanKey();
			billChPlanKey.setLocno(dto.getLocno());
			billChPlanKey.setPlanNo(dto.getPlanNo());
			BillChPlan billChPlan = (BillChPlan) billChPlanService.findById(billChPlanKey);

			if (!billChPlan.getStatus().equals(BillChPlanStatusEnums.START.getValue())) {
				throw new ServiceException("发起状态下的盘点计划单才可生成任务！");
			}

			// 根据通道分割
			if (stockCount != null && cellCount == null) {
				createChCheck(dto, check_date, stockCount, creator, 0,creatorName);
			} else if (stockCount == null && cellCount != null) {// 根据储位分割
				createChCheck(dto, check_date, cellCount, creator, 1,creatorName);
			} else if (stockCount != null && cellCount != null) {// 储位通道都不为空
				createChCheck2(dto, check_date, stockCount, cellCount, creator,creatorName);
			}
			// 更新定位信息状态
			int updateCount = billChCheckDirectMapper.updateStatusBatch(dto);
			if (updateCount == 0) {
				throw new ServiceException("该盘点单已经生成任务！");
			}

			// 盘点单生成完成之后，判断定位表所有明细是否状态已经全部更新为13- 生成任务
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", dto.getLocno());
			params.put("planNo", dto.getPlanNo());
			params.put("status", BillChCheckStautsEnums.STATUS10.getStatus());
			int count = billChCheckDirectMapper.selectCount(params, null);
			if (count == 0) {// 全部更新为13，更新计划单的状态
				BillChPlan plan = new BillChPlan();
				plan.setPlanNo(dto.getPlanNo());
				plan.setLocno(dto.getLocno());
				plan.setStatus(STATUS20);
				billChPlanMapper.updateByPrimaryKeySelective(plan);
			}
			
			this.updateCheckFlag(dto.getLocno(), dto.getPlanNo());
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 更新盘点单的盘点明细容器状态
	 * @param locno
	 * @param planNo
	 * @throws Exception
	 */
	private  void  updateCheckFlag(String locno,String planNo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locno", locno);
		params.put("planNo",planNo);
		//查询盘点计划单的盘点单号
		List<String> lstChCheckNo = billChCheckMapper.selectChCheckContainerFlag(params);
		for(String chCheckNo : lstChCheckNo){
			Map<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("locno", locno);
			pMap.put("checkNo", chCheckNo);
			
			int a = billChCheckDtlService.selectCountSingFlag(pMap);
			
			pMap.put("CountSingFlag", "A");
			
			int b = billChCheckDtlService.selectCountSingFlag(pMap);
			
			int containerFlag = 0;
			if(a > 0 && b > 0){//既有整箱也有零散
				containerFlag = 3 ;
			}
			if( a > 0 && b < 1){//只有零散
				containerFlag = 1 ;
			}
			if( a < 1 && b > 0){//只有整箱
				containerFlag = 2 ;
			}
			
			BillChCheck  billChCheck = new  BillChCheck();
			billChCheck.setLocno(locno);
			billChCheck.setCheckNo(chCheckNo);
			billChCheck.setContainerFlag(containerFlag);
			billChCheckMapper.updateByPrimaryKeySelective(billChCheck);
			
		}
	}

	/**
	 * 根据通道分割
	 * 
	 * @param dto
	 * @param check_date
	 * @param stockCount
	 */
	private void createChCheck(BillChCheckDirectDto dto, String check_date, Integer count, String creator, int type,String creatorName)
			throws ServiceException {
		try {
			Date checkDate = null;
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
			checkDate = formate.parse(check_date);
			// 分组查询出所有通道
			dto.setStatus(BillChCheckDirectStautsEnums.CREATE.getValue());
			List<BillChCheckDirectDto> list = billChCheckDirectMapper.selectDirectListGroupByStockNo(dto);
			if (type == 1) {// 分组查询所有储位
				list = billChCheckDirectMapper.selectDirectListGroupByCellNo(dto);
			}
			String checkNo = "";
			int rowId = 0;
			List<BillChCheckDtl> dtlList = new ArrayList<BillChCheckDtl>();
//			int index = 0;
			for (int i = 0, length = list.size(); i < length; i++) {
				
				if (i % count == 0) {// 根据数量分割
					//插入主单信息
					checkNo = createChCheckMain(list.get(i), checkDate, creator,null,creatorName);
					BillChCheck check = new BillChCheck();
					check.setLocno(dto.getLocno());
					check.setCheckNo(checkNo);
					rowId = billChCheckDtlMapper.selectMaxRowId(check);
				}
				
				BillChCheckDirectDto dto2 = list.get(i);
				dto2.setWareNo(dto.getWareNo());
				dto2.setAreaNo(dto.getAreaNo());
				dto2.setSysNo(dto.getSysNo());
				dto2.setBrandNo(dto.getBrandNo());
				dto2.setRequestDateStart(dto.getRequestDateStart());
				dto2.setRequestDateEnd(dto.getRequestDateEnd());
				if (StringUtils.isNotEmpty(dto.getStockNo())) {
					dto2.setStockNo(dto.getStockNo());
				}
				if (StringUtils.isNotEmpty(dto.getCellNoLike())) {//填充储位查询条件
					dto2.setCellNoLike(dto.getCellNoLike());
				}
				dto2.setStatus(BillChCheckDirectStautsEnums.CREATE.getValue());
				
				//老逻辑注释掉  add by zuo.sw 
				List<BillChCheckDirectDto> detailList = billChCheckDirectMapper.selectDirectListAll(dto2);
//				//容器记账逻辑的调整- 只查询零散的商品的定位信息
//				List<BillChCheckDirectDto> detailList = billChCheckDirectMapper.selectDirectList4Single(dto2);
//				
//				//查询定位容器箱明细信息
//				Map<String,Object> boxParam = new HashMap<String,Object>();
//				boxParam.put("locno", dto.getLocno());
//				boxParam.put("planNo", dto.getPlanNo());
//				boxParam.put("cellNo", dto2.getCellNo());//储位
//				boxParam.put("stockNo", dto2.getStockNo());//通道
//				List<BillChCheckDirectDto> boxdetailList =billChCheckDirectMapper.selectDirectBoxInfo(boxParam);
//				
//				int containerFlag = 1 ;//盘点明细容器状态
//				if((null == detailList || detailList.isEmpty()) && (null != boxdetailList && !boxdetailList.isEmpty())){//整箱
//					containerFlag = 2;
//				}
//				if((null != detailList && !detailList.isEmpty()) && (null == boxdetailList || boxdetailList.isEmpty())){//零散
//					containerFlag = 1;
//				}
//				if((null != detailList && !detailList.isEmpty()) && (null != boxdetailList && !boxdetailList.isEmpty())){//整箱&零散
//					containerFlag = 3;
//				}
//				//合并结果集
//				detailList.addAll(boxdetailList);
				
				// 批量更新定位信息
				/*
				 * dto2.setSourceStatus(BillChCheckDirectStautsEnums.CREATE
				 * .getValue()); int updateCount = billChCheckDirectMapper
				 * .updateStatusBatch(dto2); if (updateCount == 0) { throw new
				 * ServiceException("该盘点单已经生成任务！"); }
				 */
				for (BillChCheckDirectDto detail : detailList) {
					BillChCheckDtl dtl = createChCheckDtl(checkNo, detail, checkDate);
					dtl.setRowId(++rowId);
					dtlList.add(dtl);
//					index++;
//					if (index >= 1000) {
//						billChCheckDtlMapper.batchInsertDtl(dtlList);
//						index = 0;
//						dtlList.clear();
//					}
				}
			}
			
			// 1000条提交一次，提交剩下的
//			if (dtlList.size() > 0) { // 批量插入明细
//				billChCheckDtlMapper.batchInsertDtl(dtlList);
//			}
			
			//批量插入
			if(null!=dtlList && !dtlList.isEmpty()){
				for(int i=0;i<dtlList.size();){
					i += BATH_INSERT_COUNT;
					if(i>dtlList.size()){
						billChCheckDtlMapper.batchInsertDtl(dtlList.subList(i-BATH_INSERT_COUNT, dtlList.size()));
					}else{
						billChCheckDtlMapper.batchInsertDtl(dtlList.subList(i-BATH_INSERT_COUNT, i));
					}					
				}
			}

		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		} catch (ParseException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private void createChCheck2(BillChCheckDirectDto dto, String check_date, Integer stockCount, Integer cellCount,
			String creator,String creatorName) throws ServiceException {
		try {
			Date checkDate = null;
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
			checkDate = formate.parse(check_date);
			dto.setStatus(BillChCheckDirectStautsEnums.CREATE.getValue());
			// 分组查询出所有通道
			List<BillChCheckDirectDto> list = billChCheckDirectMapper.selectDirectListGroupByStockNo(dto);
			String checkNo = "";
			int rowId = 0;
			int index = 0;
			/*
			 * 将所有的通道按照切割数量进行分割保存到cutStockNoList 集合中
			 */
			List<List<BillChCheckDirectDto>> cutStockNoList = new ArrayList<List<BillChCheckDirectDto>>();
			List<BillChCheckDirectDto> stockNoList = null;
			if (list != null && list.size() > 0) {
				// 按照数量进行分割
				for (int i = 0, length = list.size(); i < length; i++) {
					if (i % stockCount == 0) {// 根据数量分割
						// 刚好是分割数，创建一个新的集合保存通道，并把通道集合保存到汇总通道结合
						stockNoList = new ArrayList<BillChCheckDirectDto>();
						cutStockNoList.add(stockNoList);
					}
					stockNoList.add(list.get(i));
				}
				// 便利汇总通道集合
				List<BillChCheckDtl> dtlList = new ArrayList<BillChCheckDtl>();
				for (List<BillChCheckDirectDto> cutStockNo : cutStockNoList) {
					// 查询该切割段中所有通道，按储位分组查询储位信息
					List<BillChCheckDirectDto> resultList = billChCheckDirectMapper
							.selectDirectListGroupByCellNoInStockNo(dto, cutStockNo);
					if (resultList != null && resultList.size() > 0) {
						// 遍历储位

						for (int i = 0, length = resultList.size(); i < length; i++) {
							
							if (i % cellCount == 0) {// 根据数量分割
								checkNo = createChCheckMain(resultList.get(i), checkDate, creator,null,creatorName);
								BillChCheck check = new BillChCheck();
								check.setLocno(dto.getLocno());
								check.setCheckNo(checkNo);
								rowId = billChCheckDtlMapper.selectMaxRowId(check);
							}
							
							// 查询储位下所有明细
							BillChCheckDirectDto dto2 = resultList.get(i);
							dto2.setWareNo(dto.getWareNo());
							dto2.setAreaNo(dto.getAreaNo());
							dto2.setSysNo(dto.getSysNo());
							dto2.setBrandNo(dto.getBrandNo());
							dto2.setRequestDateStart(dto.getRequestDateStart());
							dto2.setRequestDateEnd(dto.getRequestDateEnd());
							dto2.setStockNo(dto.getStockNo());
							if (StringUtils.isNotEmpty(dto.getCellNoLike())) {//填充储位查询条件
								dto2.setCellNoLike(dto.getCellNoLike());
							}
							dto2.setStatus(BillChCheckDirectStautsEnums.CREATE.getValue());
							
							//老逻辑注释掉  add by zuo.sw 
							List<BillChCheckDirectDto> detailList = billChCheckDirectMapper.selectDirectListAll(dto2);
//							//容器记账逻辑的调整- 只查询零散的商品的定位信息
//							List<BillChCheckDirectDto> detailList = billChCheckDirectMapper.selectDirectList4Single(dto2);
//							
//							//查询定位容器箱明细信息
//							Map<String,Object> boxParam = new HashMap<String,Object>();
//							boxParam.put("locno", dto.getLocno());
//							boxParam.put("planNo", dto.getPlanNo());
//							boxParam.put("cellNo", dto2.getCellNo());//储位
//							boxParam.put("stockNo", dto2.getStockNo());//通道
//							List<BillChCheckDirectDto> boxdetailList =billChCheckDirectMapper.selectDirectBoxInfo(boxParam);
//							
//							//合并结果集
//							detailList.addAll(boxdetailList);
//							
//							int containerFlag = 1 ;//盘点明细容器状态
//							if((null == detailList || detailList.isEmpty()) && (null != boxdetailList && !boxdetailList.isEmpty())){//整箱
//								containerFlag = 2;
//							}
//							if((null != detailList && !detailList.isEmpty()) && (null == boxdetailList || boxdetailList.isEmpty())){//零散
//								containerFlag = 1;
//							}
//							if((null != detailList && !detailList.isEmpty()) && (null != boxdetailList && !boxdetailList.isEmpty())){//整箱&零散
//								containerFlag = 3;
//							}
					
							
							/*
							 * // 批量更新定位信息
							 * dto2.setSourceStatus(BillChCheckDirectStautsEnums
							 * .CREATE .getValue()); int updateCount =
							 * billChCheckDirectMapper .updateStatusBatch(dto2);
							 * if (updateCount == 0) { throw new
							 * ServiceException("该盘点单已经生成任务！"); }
							 */
							for (BillChCheckDirectDto detail : detailList) {
								// 保存从表
								BillChCheckDtl dtl = createChCheckDtl(checkNo, detail, checkDate);
								dtl.setRowId(++rowId);
								dtlList.add(dtl);
//								index++;
//								if (index >= 1000) {
//									billChCheckDtlMapper.batchInsertDtl(dtlList);
//									index = 0;
//									dtlList.clear();
//								}
							}
						}
					} else {
						throw new ServiceException("查找不到对应盘点计划储位！");
					}
				}

//				if (dtlList.size() > 0) { // 批量插入明细
//					billChCheckDtlMapper.batchInsertDtl(dtlList);
//				}
				//批量插入
				if(null!=dtlList && !dtlList.isEmpty()){
					for(int i=0;i<dtlList.size();){
						i += BATH_INSERT_COUNT;
						if(i>dtlList.size()){
							billChCheckDtlMapper.batchInsertDtl(dtlList.subList(i-BATH_INSERT_COUNT, dtlList.size()));
						}else{
							billChCheckDtlMapper.batchInsertDtl(dtlList.subList(i-BATH_INSERT_COUNT, i));
						}					
					}
				}
			} else {
				throw new ServiceException("查找不到对应盘点计划通道！");
			}
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		} catch (ParseException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 保存主表
	 * 
	 * @param dto
	 * @param checkDate
	 * @param creator
	 * @return
	 * @throws ServiceException
	 */
	private String createChCheckMain(BillChCheckDirectDto dto, Date checkDate, String creator,Integer containerFlag,String creatorName) throws ServiceException {
		BillChCheck check = new BillChCheck();
		String checkNo = procCommonService.procGetSheetNo(dto.getLocno(), CNumPre.CH_CHECK_PRE);
		check.setCheckNo(checkNo);
		check.setLocno(dto.getLocno());
		check.setOwnerNo(dto.getOwnerNo());
		check.setPlanNo(dto.getPlanNo());
		check.setCheckDate(checkDate);
		check.setStatus(BillChCheckStautsEnums.STATUS10.getStatus());
		check.setCreator(creator);
		check.setCreatorName(creatorName);
		Date currDate = new Date();
		check.setCreatetm(currDate);
		check.setEditor(creator);
		check.setEditorName(creatorName);
		check.setEdittm(currDate);
		check.setStatusTrans(BillChCheckStautsTransEnums.STAUTSTRANS10.getStautsTrans());
		check.setPlanType(dto.getPlanType());
		check.setCheckType(BillChCheckCheckTypeEnums.CHECKTYPE1.getCheckType());
		check.setContainerFlag(containerFlag);
		billChCheckMapper.insertSelective(check);
		return checkNo;
	}

	private BillChCheckDtl createChCheckDtl(String checkNo, BillChCheckDirectDto dto, Date checkDate)
			throws DaoException {
		BillChCheckDtl dtl = new BillChCheckDtl();
		dtl.setLocno(dto.getLocno());
		dtl.setOwnerNo(dto.getOwnerNo());
		dtl.setCheckNo(checkNo);
		dtl.setCellNo(dto.getCellNo());
		dtl.setItemNo(dto.getItemNo());
		dtl.setBarcode(dto.getBarcode());
		dtl.setSizeNo(dto.getSizeNo());
		dtl.setQuality(dto.getQuality());
		dtl.setItemQty(dto.getItemQty());
		dtl.setAddFlag(BillChCheckAddFlagEnums.FLAG0.getFlag());
		dtl.setCheckType(BillChCheckCheckTypeEnums.CHECKTYPE1.getCheckType());
		dtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG0.getFlag());
		dtl.setItemType(dto.getItemType());
		dtl.setLabelNo(dto.getLabelNo());
		if(StringUtils.isNotBlank(dto.getBrandNo())){
			dtl.setBrandNo(dto.getBrandNo());
		}
		dtl.setCheckDate(checkDate);
		dtl.setStatus(BillChCheckStautsEnums.STATUS10.getStatus());
		dtl.setPackQty(dto.getPackQty());
		dtl.setCheckQty(new BigDecimal(0));
		dto.setStatus(BillChCheckDirectStatusEnums.STATUS13.getStatus());
		return dtl;
		// billChCheckDtlMapper.insertSelective(dtl);

		// 更新定位表状态
		// billChCheckDirectMapper.updateByPrimaryKeySelective(dto);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDto> selectChCheck(Map<String, Object> map, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException {

		try {
			return billChCheckMapper.selectChCheck(map, page, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void chCheck(String checkNos, String locno, String curUser,String username) throws ServiceException {
		try {
			String[] _checkNos = checkNos.split(",");
			for (String checkNoStr : _checkNos) {
				
				String noS [] = checkNoStr.split("_");
				
				// 盘点单对应的盘点计划单如果还有其他明细没有发单，则不能进行回单审核操作
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", locno);
				params.put("planNo", noS[1]);
				params.put("status", BillChCheckStautsEnums.STATUS10.getStatus());
				int counta = billChCheckDirectMapper.selectCount(params, null);
				if (counta > 0) {
					throw new ServiceException("盘点计划单【" + noS[1] + "】还有其他明细未发单,不能进行回单操作");
				}
				
				BillChCheck chCheck = new BillChCheck();
				chCheck.setLocno(locno);
				chCheck.setCheckNo(noS[0]);
				chCheck.setAudittm(new Date());
				chCheck.setAuditor(curUser);
				chCheck.setAuditorName(username);
				chCheck.setStatus(BillChCheckStautsEnums.STATUS25.getStatus());
				chCheck.setSourceStatus(BillChCheckStautsEnums.STATUS20.getStatus());
				/**
				 * 更新主表状态
				 */
				int count = billChCheckMapper.updateByPrimaryKeySelective(chCheck);
				if (count == 0) {
					throw new ServiceException("盘点单【" + chCheck.getCheckNo() + "】已经审核,或未发单");
				}
				/**
				 * 删除itemNo为'N'且对应【记录1】的cellNo在所有明细里面存在itemNo不为'N'的【记录1】
				 */
				deleteDtl(locno, noS[0]);
				/**
				 * 更新明细状态和差异标示
				 */
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("checkNo", noS[0]);
				paramMap.put("locno", locno);
				List<BillChCheckDtl> dtlList = billChCheckDtlMapper.selectByParams(null, paramMap);
				for (BillChCheckDtl dtl : dtlList) {
					dtl.setStatus(BillChCheckStautsEnums.STATUS13.getStatus());
					// 实际数量为实盘数量
					dtl.setRealQty(dtl.getCheckQty());
					if (dtl.getItemQty().compareTo(dtl.getCheckQty()) == 0) {
						dtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG0.getFlag());
					} else {
						dtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG1.getFlag());
					}
					billChCheckDtlMapper.updateByPrimaryKeySelective(dtl);
				}
				// 更新计划单状态
				updateCheckPlanStatus(locno, noS[0]);
			}

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 更新盘点单对应的计划单的状态
	 * 
	 * @param locno
	 * @param checkNo
	 */
	private void updateCheckPlanStatus(String locno, String checkNo) {
		// 查询复盘单对应的盘点单的计划单号
		BillChCheck check = new BillChCheck();
		check.setLocno(locno);
		check.setCheckNo(checkNo);
		check = billChCheckMapper.selectByPrimaryKey(check);
		String planNo = check.getPlanNo();
		// 查询计划单下的所有盘点单
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("locno", locno);
		map.put("planNo", planNo);
		List<BillChCheck> checkList = billChCheckMapper.selectByParams(null, map);
		boolean allCheck = true;
		for (BillChCheck dtl : checkList) {
			Map<String, Object> submap = new HashMap<String, Object>();
			submap.put("locno", locno);
			submap.put("checkNo", dtl.getCheckNo());
			// 查询所有的复盘明细数量
			int allCount = billChCheckDtlMapper.selectCount(submap, null);
			submap.put("status", BillChCheckStautsEnums.STATUS13.getStatus());
			int checkCount = billChCheckDtlMapper.selectCount(submap, null);
			if (allCount != checkCount) {
				allCheck = false;
				break;
			}
		}
		if (allCheck) {// 更新盘点计划状态为复盘
			BillChPlan plan = new BillChPlan();
			plan.setLocno(locno);
			plan.setPlanNo(planNo);
			plan.setStatus(BillChCheckStautsEnums.STATUS25.getStatus());
			billChPlanMapper.updateByPrimaryKeySelective(plan);
		}
	}

	/**
	 * 根据盘点计划单号，更新盘点单状态
	 */
	public int updateStatusByPlanNo(BillChCheck billChCheck) throws ServiceException {
		try {
			return billChCheckMapper.updateStatusByPlanNo(billChCheck);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * 获取itemNo为'N'且对应【记录1】的cellNo在所有明细里面存在itemNo不为'N'的【记录1】
	 * 
	 * @param locno
	 * @param checkNo
	 * @return
	 */
	public void deleteDtl(String locno, String checkNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("locno", locno);
		params.put("checkNo", checkNo);
		/** 全部明细 **/
		List<BillChCheckDtl> list = billChCheckDtlMapper.selectByParams(null, params);
		/** 待检验删除的明细 **/
		List<BillChCheckDtl> delList = new ArrayList<BillChCheckDtl>();
		if (list != null && list.size() > 0) {
			for (BillChCheckDtl dtl : list) {
				if ("N".equals(dtl.getItemNo())) {
					delList.add(dtl);
				}
			}
			if (delList.size() > 0) {
				boolean isDelete = false;
				for (BillChCheckDtl delDtl : delList) {
					isDelete = false;
					for (BillChCheckDtl dtl : list) {
						if (delDtl.getCellNo().equals(dtl.getCellNo()) && !"N".equals(dtl.getItemNo())) {
							isDelete = true;
							break;
						}
					}
					if (isDelete) {
						billChCheckDtlMapper.deleteByPrimarayKeyForModel(delDtl);
					}
				}
			}
		}
	}

	@Override
	public SumUtilMap<String, Object> selectChCheckSumQty(
			Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChCheckMapper.selectChCheckSumQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<String> findCellNo4Add(Map<String, Object> map) throws ServiceException {
		try {
			return billChCheckMapper.selectCellNo4Add(map);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<String> selectChCheckContainerFlag(Map<String, Object> map) throws ServiceException {
		try {
			return billChCheckMapper.selectChCheckContainerFlag(map);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectChCheckPlanSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChCheckMapper.selectChCheckPlanSumQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}