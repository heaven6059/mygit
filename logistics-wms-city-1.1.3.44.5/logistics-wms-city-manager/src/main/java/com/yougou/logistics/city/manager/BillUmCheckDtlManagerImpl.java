package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmCheckDtlService;

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
@Service("billUmCheckDtlManager")
class BillUmCheckDtlManagerImpl extends BaseCrudManagerImpl implements BillUmCheckDtlManager {

	@Resource
	private BillUmCheckDtlService billUmCheckDtlService;

	@Override
	public BaseCrudService init() {
		return billUmCheckDtlService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addBillUmCheck(BillUmCheck billUmCheck,
			Map<CommonOperatorEnum, List<ModelType>> params) throws ManagerException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		String msg = "";
		/*try {

			*//****************操作退仓验收单主档信息*******************/
		/*
		
		//修改主档信息
		if (StringUtils.isNotBlank(billUmCheck.getCheckNo())) {
		int result = billUmCheckService.modifyById(billUmCheck);
		if (result < 1) {
		throw new ManagerException("修改时未更新到记录！");
		}
		} else {
		//新增主档信息
		String checkNo = procCommonService.procGetSheetNo(billUmCheck.getLocno(), CNumPre.UM_CHECK_NO_PRE);
		billUmCheck.setCheckNo(checkNo);
		billUmCheck.setStatus("10");
		//billUmCheck.setCheckEndDate(new Date());
		int result = billUmCheckService.add(billUmCheck);
		if (result < 1) {
		throw new ManagerException("新增时未更新到记录！");
		}
		}

		*//****************新增退仓验收单明细信息*******************/
		/*
		List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);//新增
		List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);//删除
		List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);//更新

		//新增退仓验收单明细
		if (CommonUtil.hasValue(addList)) {

		//验证串码商品是否重复
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("locno", billUmCheck.getLocno());
		maps.put("ownerNo", billUmCheck.getOwnerNo());
		maps.put("checkNo", billUmCheck.getCheckNo());
		List<BillUmCheckDtl> listCheckDtls = billUmCheckDtlService.findByBiz(new BillUmCheckDtl(), maps);
		if (CommonUtil.hasValue(listCheckDtls)) {
		boolean checkResult = false;
		for (ModelType modelType : addList) {
		if (modelType instanceof BillUmCheckDtl) {
		BillUmCheckDtl vo = (BillUmCheckDtl) modelType;
		for (BillUmCheckDtl dtl : listCheckDtls) {
		if (StringUtils.isEmpty(vo.getBoxNo()) || "N".equals(vo.getBoxNo())) {
		if (vo.getItemNo().equals(dtl.getItemNo())
		&& vo.getSizeNo().equals(dtl.getSizeNo())) {
		msg = "串码已存在重复的【商品" + vo.getItemNo() + ",尺码" + vo.getSizeNo() + "】，请删除！";
		checkResult = true;
		break;
		}
		}
		}
		if (checkResult) {
		break;
		}
		}
		}
		if (StringUtils.isNotBlank(msg)) {
		mapObj.put("result", ResultEnums.FAIL.getResultMsg());
		mapObj.put("msg", msg);
		return mapObj;
		}
		}

		//查询最大的行号
		BillUmCheckDtl entity = new BillUmCheckDtl();
		entity.setLocno(billUmCheck.getLocno());
		entity.setOwnerNo(billUmCheck.getOwnerNo());
		entity.setCheckNo(billUmCheck.getCheckNo());
		Integer rowId = billUmCheckDtlService.selectMaxPid(entity);

		for (ModelType modelType : addList) {
		if (modelType instanceof BillUmCheckDtl) {
		BillUmCheckDtl vo = (BillUmCheckDtl) modelType;
		vo.setLocno(billUmCheck.getLocno());
		vo.setOwnerNo(billUmCheck.getOwnerNo());
		vo.setCheckNo(billUmCheck.getCheckNo());
		vo.setRowId(++rowId);//行号
		int dtlNum = billUmCheckDtlService.add(vo);
		}
		}
		}

		//删除退仓验收单明细
		if (CommonUtil.hasValue(delList)) {
		for (ModelType modelType : delList) {
		if (modelType instanceof BillUmCheckDtl) {
		BillUmCheckDtl vo = (BillUmCheckDtl) modelType;
		vo.setLocno(billUmCheck.getLocno());
		vo.setOwnerNo(billUmCheck.getOwnerNo());
		vo.setCheckNo(billUmCheck.getCheckNo());
		int dtlNum = billUmCheckDtlService.deleteById(vo);
		}
		}
		}

		//更新
		if (CommonUtil.hasValue(uptList)) {
		for (ModelType modelType : uptList) {
		if (modelType instanceof BillUmCheckDtl) {
		BillUmCheckDtl vo = (BillUmCheckDtl) modelType;
		vo.setLocno(billUmCheck.getLocno());
		vo.setOwnerNo(billUmCheck.getOwnerNo());
		vo.setCheckNo(billUmCheck.getCheckNo());
		int dtlNum = billUmCheckDtlService.modifyById(vo);
		}
		}
		}

		} catch (ServiceException e) {
		throw new ManagerException(e.getMessage(), e);
		}*/
		mapObj.put("result", ResultEnums.FAIL.getResultMsg());
		return mapObj;
	}

	@Override
	public List<BillUmCheckDtl> findBillUmCheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmCheckDtlService.findBillUmCheckDtlByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findBillUmCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckDtlService.findBillUmCheckDtlCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> saveCheckDtl(List<BillUmCheckDtl> insertList, List<BillUmCheckDtl> updateList,
			List<BillUmCheckDtl> deleteList, BillUmCheck check) throws ManagerException {
		try {
			return billUmCheckDtlService.saveCheckDtl(insertList, updateList, deleteList, check);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public int selectItemCount4Check(Item item, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billUmCheckDtlService.selectItemCount4Check(item, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<Item> selectItem4Check(Item item, SimplePage page, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckDtlService.selectItem4Check(item, page, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int selectCountForDiff(Map map) throws ManagerException {
		try {
			return billUmCheckDtlService.selectCountForDiff(map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillUmCheckDtl> selectByPageForDiff(SimplePage page, Map map) throws ManagerException {
		try {
			return billUmCheckDtlService.selectByPageForDiff(page, map);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public SumUtilMap<String, Object> selectPageSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billUmCheckDtlService.selectPageSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	/**
	 * 查询总数
	 */
	@Override
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlService.getCount(model, authorityParams);
	}
	@Override
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlService.getBillImCheckByGroup(model, authorityParams);
	}
	@Override
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlService.getBillImCheckDtl(model, authorityParams);
	}
	@Override
	public List<String> selectAllDtlSizeKind(BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlService.selectAllDtlSizeKind(model, authorityParams);
	}
}