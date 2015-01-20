package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
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
import com.yougou.logistics.city.common.enums.BillChCheckAddFlagEnums;
import com.yougou.logistics.city.common.enums.BillChCheckCheckTypeEnums;
import com.yougou.logistics.city.common.enums.BillChCheckDefferentFlagEnums;
import com.yougou.logistics.city.common.enums.BillChCheckDtlStautsEnums;
import com.yougou.logistics.city.common.enums.BillChCheckStautsEnums;
import com.yougou.logistics.city.common.enums.BillChPlanStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefAreaAreaUsetypeEnums;
import com.yougou.logistics.city.common.model.AuthorityUserinfo;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChRecheckDtl;
import com.yougou.logistics.city.common.model.BillChRecheckDtlDto;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;
import com.yougou.logistics.city.dal.mapper.BillChCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillChCheckMapper;
import com.yougou.logistics.city.dal.mapper.BillChPlanMapper;
import com.yougou.logistics.city.dal.mapper.BillChRecheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Tue Dec 17 18:31:03 CST 2013
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
@Service("billChRecheckDtlService")
class BillChRecheckDtlServiceImpl extends BaseCrudServiceImpl implements BillChRecheckDtlService {
	@Resource
	private BillChRecheckDtlMapper billChRecheckDtlMapper;

	@Resource
	private BillChCheckDtlMapper billChCheckDtlMapper;

	@Resource
	private BillChPlanMapper billChPlanMapper;

	@Resource
	private BillChCheckMapper billChCheckMapper;

	@Resource
	private ProcCommonService procCommonService;

	@Resource
	private BillChCheckService billChCheckService;

	@Resource
	private CmDefcellMapper cmDefcellMapper;

	@Resource
	private AuthorityUserinfoService authorityUserinfoService;
	
	private static final int MAXCOUNT = 1000;

	@Override
	public BaseCrudMapper init() {
		return billChRecheckDtlMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void createChReCheckDtl(List<BillChCheckDtl> cellList, String recheckWorker,int systemId,int areaSystemId) throws ServiceException {
		BillChRecheckDtl reDtl = null;
		int rowId = 0;
		String reCheckNo = "";
		Date checkDate = new Date();

		//复盘人员判断
		AuthorityUserinfo userinfo = authorityUserinfoService.findUserByLoginName(cellList.get(0).getLocno(), recheckWorker, systemId,areaSystemId);
		if(userinfo == null){
			throw new ServiceException("复盘人员【" + recheckWorker + "】不存在，请选择正确的复盘人员。");
		}
		Map<String, Object> dtlMap = new HashMap<String, Object>();
		if (cellList == null || cellList.size() == 0) {
			throw new ServiceException("没有可发单的明细");
		}
		BillChCheckDtl cell0 = cellList.get(0);
		BillChCheck check = new BillChCheck();
		check.setLocno(cell0.getLocno());
		check.setCheckNo(cell0.getCheckNo());
		BillChPlan plan = billChPlanMapper.selectByChceckNo(check);
		if (plan == null || plan.getStatus().equals(BillChPlanStatusEnums.CLOSE.getValue())) {
			throw new ServiceException("盘点计划已完结，不允许发单");
		}
		for (BillChCheckDtl cell : cellList) {
			dtlMap.put("locno", cell.getLocno());
			dtlMap.put("checkNo", cell.getCheckNo());
			dtlMap.put("cellNo", cell.getCellNo());
			dtlMap.put("status", BillChCheckDtlStautsEnums.STATUS13.getStatus());
			List<BillChCheckDtl> dtllist = billChCheckDtlMapper.selectByPage(null, null, null, dtlMap, null);

			if(dtllist == null || dtllist.size() == 0){
				throw new ServiceException("记录中存在过期数据，不允许发单");
			}
			for (BillChCheckDtl dtl : dtllist) {
				rowId++;
				if (rowId == 1) {
					reCheckNo = procCommonService.procGetSheetNo(dtl.getLocno(), CNumPre.CH_CHECK_CR);
				}
				reDtl = new BillChRecheckDtl();
				reDtl.setLocno(dtl.getLocno());
				reDtl.setOwnerNo(dtl.getOwnerNo());
				reDtl.setRecheckNo(reCheckNo);
				reDtl.setCheckNo(dtl.getCheckNo());
				reDtl.setRowId(rowId);
				reDtl.setCellNo(dtl.getCellNo());
				reDtl.setItemNo(dtl.getItemNo());
				reDtl.setBarcode(dtl.getBarcode());
				reDtl.setSizeNo(dtl.getSizeNo());
				reDtl.setQuality(dtl.getQuality());
				reDtl.setItemQty(dtl.getItemQty());
				reDtl.setAddFlag(BillChCheckAddFlagEnums.FLAG0.getFlag());
				reDtl.setStatus(BillChCheckStautsEnums.STATUS10.getStatus());
				reDtl.setCheckWorker(recheckWorker);
				reDtl.setCheckWorkerName(userinfo.getUsername());
				reDtl.setItemType(dtl.getItemType());
				reDtl.setLabelNo(dtl.getLabelNo());
				reDtl.setCheckRowId(dtl.getRowId());
				reDtl.setCheckDate(checkDate);
				reDtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG0.getFlag());
				reDtl.setBrandNo(dtl.getBrandNo());
				billChRecheckDtlMapper.insertSelective(reDtl);
				// 更新盘点单明细的状态为10，已发单状态
				BillChCheckDtl chCheckDtlUpdate = new BillChCheckDtl();
				chCheckDtlUpdate.setLocno(dtl.getLocno());
				chCheckDtlUpdate.setCheckNo(dtl.getCheckNo());
				chCheckDtlUpdate.setRowId(dtl.getRowId());
				chCheckDtlUpdate.setStatus(BillChCheckStautsEnums.STATUS10.getStatus());
				chCheckDtlUpdate.setSourceStatus(BillChCheckStautsEnums.STATUS13.getStatus());
				chCheckDtlUpdate.setCheckType(BillChCheckCheckTypeEnums.CHECKTYPE2.getCheckType());
				int count = billChCheckDtlMapper.updateByPrimaryKeySelective(chCheckDtlUpdate);
				if (count == 0) {
					throw new ServiceException("储位【" + dtl.getCellNo() + "】<br>商品编码【" + dtl.getItemNo() + "】<br>尺码【"
							+ dtl.getSizeNo() + "】已发单");
				}
			}
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectReCheckCount(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChRecheckDtlMapper.selectReCheckCount(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChRecheckDtlDto> selectReCheck(Map<String, Object> map, SimplePage page,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChRecheckDtlMapper.selectReCheck(map, page, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillChRecheckDtl> selectCellNo(BillChRecheckDtl check) throws ServiceException {
		try {
			return billChRecheckDtlMapper.selectCellNo(check);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public ResultVo updateReCheckDtl(List<BillChRecheckDtl> insertList, List<BillChRecheckDtl> updateList,
			List<BillChRecheckDtl> deleteList, String recheckNo, String checkNo, String locno) throws ServiceException {
		try {
			Date curDate = null;
			String checkWorker = null;
			BillChCheck param = new BillChCheck();
			ResultVo resultVo = new ResultVo();
			
			param.setLocno(locno);
			param.setCheckNo(checkNo);
			BillChCheck result = billChCheckMapper.selectByPrimaryKey(param);
			Map<String, Object> rcp = new HashMap<String, Object>();
			rcp.put("locno", locno);
			rcp.put("recheckNo", recheckNo);
			rcp.put("status", "10");
			List<BillChRecheckDtl> rcList = billChRecheckDtlMapper.selectByParams(null, rcp);
			if(rcList == null || rcList.size() == 0){
				throw new ServiceException("此复盘单已经盘点,不能保存");
			}
			
			// 删除明细
			for (BillChRecheckDtl dtl : deleteList) {
				billChRecheckDtlMapper.deleteByPrimarayKeyForModel(dtl);
			}
			// 检查明细是否重复
			/*
			 * String errorMsg = beforeSaveReCheckDtl(insertList, recheckNo,
			 * locno); // 有错误信息，则返回 if (errorMsg.length() > 0) {
			 * resultVo.setSuccess(false); resultVo.setErrorMessage(errorMsg);
			 * return resultVo; }
			 */
			
			
			/**
			 * su.yq start
			 */
			
			//验证储位的合法性
			boolean isOkCellNo = false;
			StringBuffer sbCellTip = new StringBuffer();
			Map<String, String> mapCells = new HashMap<String, String>();
			if("1".equals(result.getPlanType())){
				List<BillChCheckDtl> listChCheckDtls = billChCheckDtlMapper.selectCellNobyPlan(result.getPlanNo(), result.getLocno());
				for (BillChCheckDtl bc : listChCheckDtls) {
					String cellNo = mapCells.get(bc.getCellNo());
					if (StringUtils.isEmpty(cellNo)) {
						mapCells.put(bc.getCellNo(), bc.getCellNo());
					}
				}
			}else{
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("locno", result.getLocno());
				params.put("cellStatus", "0");
				List<CmDefcellSimple> listDefcells = cmDefcellMapper.selectSimple(params);
				for (CmDefcellSimple bc : listDefcells) {
					String cellNo = mapCells.get(bc.getCellNo());
					if (StringUtils.isEmpty(cellNo) && !bc.getAreaUsetype().equals(CmDefAreaAreaUsetypeEnums.AREAUSETYPE_7.getAreaUsetype())) {//过滤掉库存调整区的储位
						mapCells.put(bc.getCellNo(), bc.getCellNo());
					}
				}
			}
			
			//验证额外新增的商品明细
			if(CommonUtil.hasValue(insertList)){
				for (BillChRecheckDtl billChRecheckDtl : insertList) {
					if(billChRecheckDtl.getItemQty().intValue() == 0){
						for(Map.Entry<String, String> entry: mapCells.entrySet()) {
							 String cellNo = entry.getKey();
							 if(billChRecheckDtl.getCellNo().equals(cellNo)){
								 isOkCellNo = true;
								 break;
							 }
						}
						if(!isOkCellNo){
							sbCellTip.append("输入储位不合法,明细信息如下：");
							sbCellTip.append("储位："+billChRecheckDtl.getCellNo()+",");
							sbCellTip.append("商品编码："+billChRecheckDtl.getItemNo()+",");
							sbCellTip.append("尺码："+billChRecheckDtl.getSizeNo()+"。");
							throw new ServiceException(sbCellTip.toString());
						}
					}
				}
			}
			
			//更新的储位验证
			if(CommonUtil.hasValue(updateList)){
				for (BillChRecheckDtl billChRecheckDtl : updateList) {
					if(billChRecheckDtl.getItemQty().intValue() == 0){
						for(Map.Entry<String, String> entry: mapCells.entrySet()) {
							 String cellNo = entry.getKey();
							 if(billChRecheckDtl.getCellNo().equals(cellNo)){
								 isOkCellNo = true;
								 break;
							 }
						}
						if(!isOkCellNo){
							sbCellTip.append("输入储位不合法,明细信息如下：");
							sbCellTip.append("储位："+billChRecheckDtl.getCellNo()+",");
							sbCellTip.append("商品编码："+billChRecheckDtl.getItemNo()+",");
							sbCellTip.append("尺码："+billChRecheckDtl.getSizeNo()+"。");
							throw new ServiceException(sbCellTip.toString());
						}
					}
				}
			}
			/**
			 * su.yq end
			 */
			

			BillChRecheckDtl dtlparam = new BillChRecheckDtl();
			dtlparam.setLocno(locno);
			dtlparam.setOwnerNo(result.getOwnerNo());
			dtlparam.setCheckNo(checkNo);
			dtlparam.setRecheckNo(recheckNo);
			int rowId = billChRecheckDtlMapper.selectMaxRowId(dtlparam);
			List<BillChRecheckDtl> addList = new ArrayList<BillChRecheckDtl>();
			List<BillChRecheckDtl> updateList_ = new ArrayList<BillChRecheckDtl>();
			int addCount = 0;
			int updateCount = 0;
			BillChRecheckDtl paramDtl = new BillChRecheckDtl();
			paramDtl.setLocno(locno);
			paramDtl.setRecheckNo(recheckNo);
			BillChRecheckDtl firstDtl = this.billChRecheckDtlMapper.selectFirstDtl(paramDtl);
			if (null != firstDtl) {
				checkWorker = firstDtl.getCheckWorker();
				curDate = firstDtl.getCheckDate();
			}
			Map<String, CmDefcell> cmMap = new HashMap<String, CmDefcell>();
			for (BillChRecheckDtl dtl : insertList) {
				dtl.setLocno(locno);
				dtl.setOwnerNo(result.getOwnerNo());
				dtl.setCheckNo(checkNo);
				dtl.setRecheckNo(recheckNo);
				dtl.setRowId(++rowId);
				dtl.setCheckRowId(0);
				dtl.setCheckDate(curDate);
				dtl.setCheckWorker(checkWorker);
				dtl.setAddFlag(BillChCheckAddFlagEnums.FLAG1.getFlag());
				dtl.setStatus(BillChCheckStautsEnums.STATUS10.getStatus());
				dtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG0.getFlag());
				dtl.setOwnerNo(result.getOwnerNo());
				dtl.setPackQty(new BigDecimal(1));

				CmDefcell cell = null;
				if (cmMap.get(dtl.getCellNo()) == null) {
					CmDefcell paramcell = new CmDefcell();
					paramcell.setCellNo(dtl.getCellNo());
					paramcell.setLocno(locno);
					cell = this.cmDefcellMapper.selectByPrimaryKey(paramcell);
					cmMap.put(dtl.getCellNo(), cell);
				} else {
					cell = cmMap.get(dtl.getCellNo());
				}
				dtl.setQuality(cell.getAreaQuality() == null ? "0" : cell.getAreaQuality());
				dtl.setItemType(cell.getItemType() == null ? "0" : cell.getItemType());
				addList.add(dtl);
				addCount++;
				if (addCount >= MAXCOUNT) {
					this.billChRecheckDtlMapper.batchInsert(addList);
					addList.clear();
					addCount = 0;
				}
			}
			Date checkDate = new Date();
			for (BillChRecheckDtl dtl : updateList) {
				dtl.setCheckDate(checkDate);//盘点时间
				updateList_.add(dtl);
				updateCount++;
				if (updateCount >= MAXCOUNT) {
					this.billChRecheckDtlMapper.batchUpdate(updateList_);
					updateList_.clear();
					updateCount = 0;
				}
			}
			// 批量插入
			if (addList.size() > 0) {
				this.billChRecheckDtlMapper.batchInsert(addList);
			}
			if (updateList.size() > 0) {
				this.billChRecheckDtlMapper.batchUpdate(updateList_);
			}
			// 校验重复

			List<BillChRecheckDtl> repeatList = this.billChRecheckDtlMapper.selectRepeat(dtlparam);
			if (repeatList != null && repeatList.size() > 0) {
				StringBuilder str = new StringBuilder();
				for (BillChRecheckDtl dtl : repeatList) {
					str.append("储位:").append(dtl.getCellNo()).append("<br>商品编码:").append(dtl.getItemNo())
							.append("<br>尺码:").append(dtl.getSizeNo()).append("<br>");
				}
				str.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
				throw new ServiceException(str.toString());
			}
			// 批量更新
			resultVo.setSuccess(true);
			return resultVo;
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void chReCheckAudit(String dtlstrs, String locno) throws ServiceException {
		try {
			if (dtlstrs == null) {
				return;
			}
			String[] dtls = dtlstrs.split(",");
			for (String dtlstr : dtls) {
				String[] dtlArray = dtlstr.split("\\|");
				BillChRecheckDtl recheckDtl = new BillChRecheckDtl();
				recheckDtl.setLocno(locno);
				recheckDtl.setRecheckNo(dtlArray[0]);
				// 查询所有的复盘明细
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("locno", locno);
				map.put("recheckNo", dtlArray[0]);
				List<BillChRecheckDtl> reCheckList = billChRecheckDtlMapper.selectByParams(recheckDtl, map);
				for (BillChRecheckDtl realDtl : reCheckList) {
					if (realDtl.getItemQty() != null && realDtl.getRecheckQty() != null
							&& realDtl.getItemQty().compareTo(realDtl.getRecheckQty()) == 0) {// 更新差异状态
						realDtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG0.getFlag());
					} else {
						realDtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG1.getFlag());
					}
					realDtl.setStatus(BillChCheckStautsEnums.STATUS13.getStatus());
					realDtl.setSourceStatus(BillChCheckStautsEnums.STATUS10.getStatus());
					// 更新复盘单状态
					int count = billChRecheckDtlMapper.updateByPrimaryKeySelective(realDtl);
					if (count == 0) {
						throw new ServiceException("复盘单【" + realDtl.getRecheckNo() + "】已经审核");
					}
					// 更新盘点单的复盘时间，复盘人，复盘数量
					if (BillChCheckAddFlagEnums.FLAG0.getFlag().equals(realDtl.getAddFlag())) {
						BillChCheckDtl checkDtl = new BillChCheckDtl();
						checkDtl.setLocno(locno);
						checkDtl.setCheckNo(realDtl.getCheckNo());
						checkDtl.setRowId(realDtl.getCheckRowId());
						// 复盘人员
						checkDtl.setRecheckWorker(realDtl.getCheckWorker());
						// 复盘人员中文名称
						checkDtl.setRecheckWorkerName(realDtl.getCheckWorkerName());
						// 复盘数量
						checkDtl.setRecheckQty(realDtl.getRecheckQty());
						// 复盘时间
						checkDtl.setRecheckDate(realDtl.getCheckDate());
						// 状态
						checkDtl.setStatus(BillChCheckStautsEnums.STATUS13.getStatus());
						// 实际数量
						checkDtl.setRealQty(realDtl.getRecheckQty());

						if (realDtl.getItemQty() != null && realDtl.getRecheckQty() != null
								&& realDtl.getItemQty().compareTo(realDtl.getRecheckQty()) == 0) {// 更新差异状态
							checkDtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG0.getFlag());
						} else {
							checkDtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG1.getFlag());
						}

						billChCheckDtlMapper.updateByPrimaryKeySelective(checkDtl); 

					} else if (BillChCheckAddFlagEnums.FLAG1.getFlag().equals(realDtl.getAddFlag())) {// 新增的商品
						BillChCheck check = new BillChCheck();
						check.setLocno(locno);
						check.setCheckNo(realDtl.getCheckNo());
						int rowId = billChCheckDtlMapper.selectMaxRowId(check);

						// 写入一条记录到盘点单
						BillChCheckDtl checkDtl = new BillChCheckDtl();
						checkDtl.setLocno(locno);
						checkDtl.setCheckNo(realDtl.getCheckNo());
						checkDtl.setRowId(rowId + 1);
						// 复盘人员
						checkDtl.setRecheckWorker(realDtl.getCheckWorker());
						// 复盘数量
						checkDtl.setRecheckQty(realDtl.getRecheckQty());
						// 盘点时间
						checkDtl.setRecheckDate(realDtl.getCheckDate());
						// 状态
						checkDtl.setStatus(BillChCheckStautsEnums.STATUS13.getStatus());
						// 盘点类型
						checkDtl.setCheckType(BillChCheckCheckTypeEnums.CHECKTYPE2.getCheckType());
						// 实际数量
						checkDtl.setRealQty(realDtl.getRecheckQty());

						checkDtl.setAddFlag(BillChCheckAddFlagEnums.FLAG1.getFlag());
						checkDtl.setBarcode(realDtl.getBarcode());
						checkDtl.setItemNo(realDtl.getItemNo());
						checkDtl.setCellNo(realDtl.getCellNo());
						checkDtl.setSizeNo(realDtl.getSizeNo());
						checkDtl.setQuality(realDtl.getQuality());
						checkDtl.setItemQty(realDtl.getItemQty());
						checkDtl.setCheckType(BillChCheckCheckTypeEnums.CHECKTYPE2.getCheckType());
						checkDtl.setDifferentFlag(BillChCheckDefferentFlagEnums.FLAG1.getFlag());
						checkDtl.setItemType(realDtl.getItemType());
						checkDtl.setLabelNo(realDtl.getLabelNo());
						checkDtl.setItemQty(new BigDecimal(0));
						checkDtl.setOwnerNo(realDtl.getOwnerNo());
						checkDtl.setPackQty(realDtl.getPackQty());
						checkDtl.setBrandNo(realDtl.getBrandNo());
						billChCheckDtlMapper.insertSelective(checkDtl);
					}
				}
				// 更新盘点计划状态
				// updateCheckPlanStatus(locno, dtlArray[1]);
				if (reCheckList != null && reCheckList.size() > 0) {
					billChCheckService.deleteDtl(locno, reCheckList.get(0).getCheckNo());
				}
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
		// 查询所有的初盘明细
		boolean allCheck = true;
		for (BillChCheck dtl : checkList) {
			Map<String, Object> submap = new HashMap<String, Object>();
			submap.put("locno", locno);
			submap.put("checkNo", dtl.getCheckNo());
			int allCount = billChRecheckDtlMapper.selectCount(submap, null);
			submap.put("status", BillChCheckStautsEnums.STATUS13.getStatus());
			int checkCount = billChRecheckDtlMapper.selectCount(submap, null);
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

	@Override
	public void saveByPlan(BillChRecheckDtl check) throws ServiceException {
		try {
			check.setCheckDate(new Date());
			int count = this.billChRecheckDtlMapper.saveByPlan(check);
			if (count == 0) {
				throw new ServiceException("此明细已经盘点,不能再按计划保存！");
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void resetPlan(BillChRecheckDtl check) throws ServiceException {
		try {
			check.setCheckDate(new Date());
			int count = this.billChRecheckDtlMapper.resetPlan(check);
			if (count == 0) {
				throw new ServiceException("此明细实盘数都为0或RF已做复盘,不能再实盘置零！");
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 根据盘点计划单号，更新复盘单状态
	 */
	public int updateStatusByPlanNo(@Param("params") Map<String, Object> map) throws ServiceException {
		try {
			return billChRecheckDtlMapper.updateStatusByPlanNo(map);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billChRecheckDtlMapper.selectSumQty(map, authorityParams);
	}

	@Override
	public SumUtilMap<String, Object> selectChReCheckSumQty(
			Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChRecheckDtlMapper.selectChReCheckSumQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
}