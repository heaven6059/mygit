package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.enums.BillUmCheckDtlStatusEnums;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.model.BillUmCheckKey;
import com.yougou.logistics.city.common.model.BillUmUntread;
import com.yougou.logistics.city.common.model.BillUmUntreadDtl;
import com.yougou.logistics.city.common.model.BillUmUntreadKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmUntreadMapper;

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
@Service("billUmCheckDtlService")
class BillUmCheckDtlServiceImpl extends BaseCrudServiceImpl implements BillUmCheckDtlService {

	@Resource
	private BillUmUntreadMapper billUmUntreadMapper;
	
	@Resource
	private BillUmUntreadDtlMapper billUmUntreadDtlMapper;
	
	@Resource
	private BillUmCheckDtlMapper billUmCheckDtlMapper;
	@Resource
	private BillUmCheckMapper billUmCheckMapper;
	@Override
	public BaseCrudMapper init() {
		return billUmCheckDtlMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillUmCheckDtl> findBillUmCheckDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectBillUmCheckDtlByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findBillUmCheckDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectBillUmCheckDtlCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectItemNoIsHave(BillUmCheckDtl billUmCheckDtl) throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectItemNoIsHave(billUmCheckDtl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> saveCheckDtl(List<BillUmCheckDtl> insertList, List<BillUmCheckDtl> updateList,
			List<BillUmCheckDtl> deleteList, BillUmCheck check) throws ServiceException {
		List<String> repeatList = new ArrayList<String>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Date edittm = check.getEdittm();
			String editor = check.getEditor();
			String editorName = check.getEditorName();
			
			//判断退仓验收单是否已删除或者状态已改变
			BillUmCheckKey billUmCheckKey = new BillUmCheckKey();
			billUmCheckKey.setCheckNo(check.getCheckNo());
			billUmCheckKey.setLocno(check.getLocno());
			billUmCheckKey.setOwnerNo(check.getOwnerNo());
			BillUmCheck BillUmCheck = (BillUmCheck) billUmCheckMapper.selectByPrimaryKey(billUmCheckKey);
			if(null ==BillUmCheck ||!"10".equals(BillUmCheck.getStatus())){
				throw new ManagerException("单据:"+BillUmCheck.getCheckNo()+"已删除或者状态已改变！");
			}
			//删除行
			for (BillUmCheckDtl dtl : deleteList) {
				dtl.setLocno(check.getLocno());
				dtl.setOwnerNo(check.getOwnerNo());
				billUmCheckDtlMapper.deleteByPrimarayKeyForModel(dtl);
			}
			//修改的行
			for (BillUmCheckDtl dtl : updateList) {
				dtl.setLocno(check.getLocno());
				dtl.setOwnerNo(check.getOwnerNo());
				dtl.setEditor(editor);
				dtl.setEditorName(editorName);
				dtl.setEdittm(edittm);
				billUmCheckDtlMapper.updateByPrimaryKeySelective(dtl);
			}
			
			//查询店退仓主档数据-------su.yq
			BillUmUntreadKey key = new BillUmUntreadKey();
			key.setLocno(check.getLocno());
			key.setOwnerNo(check.getOwnerNo());
			key.setUntreadNo(check.getUntreadNo());
			BillUmUntread untreadEntity = (BillUmUntread)billUmUntreadMapper.selectByPrimaryKey(key);
			if(untreadEntity == null){
				throw new ServiceException(check.getCheckNo()+"查询店退仓单主档数据失败!");
			}
			
			//查询店退仓明细-------su.yq
			Map<String, BillUmUntreadDtl> mapUntreadDtls= new HashMap<String, BillUmUntreadDtl>();
			Map<String, Object> paramUntread = new HashMap<String, Object>();
			paramUntread.put("locno", check.getLocno());
			paramUntread.put("ownerNo", check.getOwnerNo());
			paramUntread.put("untreadNo", check.getUntreadNo());
			List<BillUmUntreadDtl> listUntreadDtls=billUmUntreadDtlMapper.selectByParams(null, paramUntread);
			for (BillUmUntreadDtl bud : listUntreadDtls) {
				String mapKey = bud.getBoxNo() + "-" + bud.getItemNo() + "-" + bud.getSizeNo();
				BillUmUntreadDtl dtl = mapUntreadDtls.get(mapKey);
				if(dtl == null){
					mapUntreadDtls.put(mapKey, bud);
				}
			}
			
			//新增的行
			Integer maxRowId = billUmCheckDtlMapper.selectMaxRowId(check);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("locno", check.getLocno());
			param.put("ownerNo", check.getOwnerNo());
			param.put("checkNo", check.getCheckNo());
			List<BillUmCheckDtl> list = billUmCheckDtlMapper.selectByParams(null, param);
			for (BillUmCheckDtl dtl : insertList) {
				for (BillUmCheckDtl result : list) {
					if (dtl.getBoxNo() != null && dtl.getItemNo() != null && dtl.getSizeNo() != null
							&& dtl.getBoxNo().equals(result.getBoxNo()) && dtl.getSizeNo().equals(result.getSizeNo())
							&& dtl.getItemNo().equals(result.getItemNo())) {
						repeatList.add("箱号：" + dtl.getBoxNo() + "<br>" + "商品编码：" + dtl.getItemNo() + "<br>" + "尺码："
								+ dtl.getSizeNo());
						break;
					}
				}
				dtl.setCheckNo(check.getCheckNo());
				dtl.setLocno(check.getLocno());
				dtl.setOwnerNo(check.getOwnerNo());
				dtl.setRowId(++maxRowId);
				dtl.setStatus(BillUmCheckDtlStatusEnums.STATUS10.getStatus());
				dtl.setEditor(editor);
				dtl.setEditorName(editorName);
				dtl.setEdittm(edittm);
				
				//---------店退仓单是否操作相同的商品,如果操作相同商品添加进计划数量-------su.yq
				String mapKey = dtl.getBoxNo() + "-" + dtl.getItemNo() + "-" + dtl.getSizeNo(); 
				BillUmUntreadDtl mapDtl = mapUntreadDtls.get(mapKey);
				if(mapDtl != null){
					dtl.setItemQty(mapDtl.getItemQty());
				}
				
				billUmCheckDtlMapper.insertSelective(dtl);
			}
			
			//修改主单修改人
			billUmCheckMapper.updateByPrimaryKeySelective(check);
			
			if (repeatList.size() > 0) {
				StringBuilder strb = new StringBuilder();
				for (String str : repeatList) {
					strb.append(str + "<br>");
				}
				strb.append("<div style='text-align:center;color:red'>已经存在！<div>");
				throw new ServiceException(strb.toString());
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return resultMap;
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectItemCount4Check(Item item, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectItemCount4Check(item, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<Item> selectItem4Check(Item item, SimplePage page, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectItem4Check(item, page, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectCountForDiff(Map map) throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectCountForDiff(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillUmCheckDtl> selectByPageForDiff(SimplePage page, Map map) throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectByPageForDiff(page, map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectPageSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectPageSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 查询记录总数
	 */
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int getCount(BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlMapper.getCount(model, authorityParams);
	}
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckByGroup(BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlMapper.getBillImCheckByGroup(model, authorityParams);
	}
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillCheckImRep> getBillImCheckDtl(BillCheckImRep model, AuthorityParams authorityParams) {

		return billUmCheckDtlMapper.getBillImCheckDtl(model, authorityParams);
	}
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<String> selectAllDtlSizeKind(@Param("params") BillCheckImRep model, AuthorityParams authorityParams) {
		return billUmCheckDtlMapper.selectAllDtlSizeKind(model, authorityParams);
	}

	@Override
	public int selectValidateUmCheckIsRecheck(Map<String, Object> params) throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectValidateUmCheckIsRecheck(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillUmCheckDtl> selectCheckQtyJoinContent(Map<String, Object> params, List<BillUmCheck> list)
			throws ServiceException {
		try {
			return billUmCheckDtlMapper.selectCheckQtyJoinContent(params, list);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void updateRecheckQty4Convert(Map<String, Object> params)
			throws ServiceException {
		try {
			billUmCheckDtlMapper.updateRecheckQty4Convert(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}