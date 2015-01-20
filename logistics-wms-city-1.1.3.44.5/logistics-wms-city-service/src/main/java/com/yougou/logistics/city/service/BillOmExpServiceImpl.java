package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.model.BillOmExpKey;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.OmTmpBoxGroupKey;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillOmDivideMapper;
import com.yougou.logistics.city.dal.database.BillOmLocateMapper;
import com.yougou.logistics.city.dal.database.OmTmpBoxGroupMapper;
import com.yougou.logistics.city.dal.mapper.BillOmExpDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmExpMapper;
import com.yougou.logistics.city.dal.mapper.ProcCommonMapper;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-29 16:50:42
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
@Service("billOmExpService")
class BillOmExpServiceImpl extends BaseCrudServiceImpl implements
		BillOmExpService {

	private final static String RESULTY = "Y";
	
	private final static String STATUS90 = "90";

	private final static String STATUS91 = "91";

	private final static String STATUS10 = "10";
	
	private final static String STATUS12 = "12";
	
	private final static String STATUS16 = "16";
	
	private final static String STATUS40 = "40";
	
	private final static String STATUS50 = "50";

	private final static String SOURCE_TYPE = "1";

	private final static String SPLIT_STATUS = "1";

	private final static String CLASS_TYPE = "0";//存储分货
	
	private final static String CLASS_TYPE2 = "2";//按ASN分货
	

	@Resource
	private BillOmExpMapper billOmExpMapper;

	@Resource
	private BillOmExpDtlMapper billOmExpDtlMapper;

	@Resource
	private BillOmLocateMapper billOmLocateMapper;

	@Resource
	private BillOmDivideMapper billOmDivideMapper;

	@Resource
	private ProcCommonService procCommonService;

	@Override
	public BaseCrudMapper init() {
		return billOmExpMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findBillOmExpDispatchCount(BillOmExp billOmExp,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmExpMapper.selectBillOmExpDispatchCount(billOmExp,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmExp> findBillOmExpDispatchByPage(SimplePage page,
			String orderByField, String orderBy, BillOmExp billOmExp,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmExpMapper.selectBillOmExpDispatchByPage(page,
					orderByField, orderBy, billOmExp, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> procBillOmExpDispatchQuery(BillOmExp billOmExp)
			throws ServiceException {

		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";

		try {

			// String serialNo = omTmpBoxGroupMapper.getNextvalId();
			// String[] expNos = billOmExp.getExpNo().split(",");
			// for (String expNo : expNos) {
			// OmTmpBoxGroupKey key = new OmTmpBoxGroupKey();
			// key.setLocno(billOmExp.getLocno());
			// key.setSerialNo(serialNo);
			// key.setBillNo(expNo);
			// int k = omTmpBoxGroupMapper.insertSelective(key);
			// if(k < 1){
			// throw new ServiceException("新增出库单号临时表失败!");
			// }
			// }

			Map<String, String> map = new HashMap<String, String>();
			map.put("v_locno", billOmExp.getLocno());
			map.put("v_owner_no", billOmExp.getOwnerNo());
			map.put("v_exp_type", billOmExp.getExpType());
			map.put("v_exp_no", billOmExp.getExpNo());
			map.put("v_divide_flag", String.valueOf(billOmExp.getDivideFlag()));
			map.put("v_creator", billOmExp.getCreator());

			billOmExpMapper.procBillOmExpDispatchQuery(map);
			if (!RESULTY.equals(map.get("stroutmsg"))) {
				String stroutmsg = map.get("stroutmsg");
				if (StringUtils.isNotBlank(stroutmsg)) {
					String[] msgs = stroutmsg.split("\\|");
					msg = msgs[1];
				}
				throw new ServiceException(msg);
			}

			flag = "success";
			msg = "审核成功!";
			mapObj.put("flag", flag);
			mapObj.put("msg", msg);
			return mapObj;

		} catch (Exception e) {
			throw new ServiceException(msg);
		}
	}

	@Override
	public BillOmExp findBillOmExpViewTotalQty(BillOmExp billOmExp)
			throws ServiceException {
		try {
			return billOmExpMapper.selectBillOmExpViewTotalQty(billOmExp);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findCountExp(BillOmExp params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmExpMapper.selectCountExp(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmExp> findByPageExp(SimplePage page, String orderByField,
			String orderBy, BillOmExp params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmExpMapper.selectByPageExp(page, orderByField, orderBy,
					params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	public boolean isDiffAttribute(String locno, String[] expNos)
			throws ServiceException {
		boolean isOk = true;
		int count = billOmExpMapper.selectIsDiffAttribute(locno, expNos);
		if (count == 1) {
			int expCount = billOmExpMapper.selectIsExpSysNo(locno, expNos);
			if (expCount == 1) {
				isOk = false;
			}
		}
		return isOk;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void overExpBill(BillOmExp billOmExp) throws ServiceException {
		try {
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("locno", billOmExp.getLocno());
			maps.put("expNo", billOmExp.getExpNo());
			List<BillOmLocate> omLocateList = billOmLocateMapper
					.selectOmLocateByExpNo(maps);
			if (CommonUtil.hasValue(omLocateList)) {
				throw new ServiceException(omLocateList.get(0).getLocateNo()
						+ "波茨号未关闭,不能手工关闭发货通知单!");
			}
			List<BillOmDivide> omDivideList = billOmDivideMapper
					.selectOmDivideByExpNo(maps);
			if (CommonUtil.hasValue(omDivideList)) {
				throw new ServiceException(omDivideList.get(0).getDivideNo()
						+ "分货单号未关闭,不能手工关闭发货通知单!");
			}
			BillOmExpKey omExpKey = new BillOmExpKey();
			omExpKey.setLocno(billOmExp.getLocno());
			omExpKey.setExpNo(billOmExp.getExpNo());
			omExpKey.setExpType(billOmExp.getExpType());
			BillOmExp omExp = (BillOmExp) billOmExpMapper
					.selectByPrimaryKey(omExpKey);
			if (omExp == null) {
				throw new ServiceException("查询发货通知单记录失败!");
			}
			if (STATUS91.equals(omExp.getStatus())||STATUS90.equals(omExp.getStatus())) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "已经关闭,不能再次关闭!");
			}
			omExp.setStatus(STATUS91);
			int count = billOmExpMapper.updateByPrimaryKeySelective(omExp);
			if (count < 1) {
				throw new ServiceException("更新状态失败!");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void toClass0ForExp(BillOmExp billOmExp) throws ServiceException {
		try {
			BillOmExpKey expKey = new BillOmExpKey();
			expKey.setLocno(billOmExp.getLocno());
			expKey.setExpNo(billOmExp.getExpNo());
			expKey.setExpType(billOmExp.getExpType());
			BillOmExp exp = (BillOmExp) billOmExpMapper
					.selectByPrimaryKey(expKey);
			if (exp == null) {
				throw new ServiceException(billOmExp.getExpNo() + "查找发货通知单失败!");
			}
			if (SOURCE_TYPE.equals(exp.getSourceType())) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "拆分创建的发货单不能拆分!");
			}
			if (SPLIT_STATUS.equals(exp.getSplitStatus())) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "发货单已拆分,不能再次拆分!");
			}
			if (CLASS_TYPE.equals(exp.getClassType())) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "只能拆分分货类型为非<存储发货>的单据!");
			}
			if (STATUS10.equals(exp.getStatus())) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "不能拆分建单状态的单据!");
			}
			if (STATUS90.equals(exp.getStatus())||STATUS91.equals(exp.getStatus())) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "不能拆分关闭状态的单据!");
			}

			// 自定生成单号,生成主档数据
			String expNo = procCommonService.procGetSheetNo(
					billOmExp.getLocno(), CNumPre.OM_EXPNO_PRE);
			BillOmExp newExp = new BillOmExp();
			newExp.setLocno(billOmExp.getLocno());
			newExp.setExpNo(expNo);
			newExp.setExpType(billOmExp.getExpType());
			newExp.setSourceNo(billOmExp.getExpNo());
			newExp.setSourceType(SOURCE_TYPE);
			newExp.setCreator(billOmExp.getCreator());
			newExp.setEditor(billOmExp.getEditor());
			newExp.setClassType(CLASS_TYPE);
			int count = billOmExpMapper.insertSplitByExp(newExp);
			if (count < 1) {
				throw new ServiceException(billOmExp.getExpNo() + "拆分单据失败!");
			}

			// 更新单据为已拆分
			BillOmExp upExp = new BillOmExp();
			upExp.setLocno(billOmExp.getLocno());
			upExp.setExpNo(billOmExp.getExpNo());
			upExp.setExpType(billOmExp.getExpType());
			upExp.setSplitStatus(SPLIT_STATUS);
			int ucount = billOmExpMapper.updateByPrimaryKeySelective(upExp);
			if (ucount < 1) {
				throw new ServiceException(billOmExp.getExpNo() + "更新单据拆分状态失败!");
			}

			// 生成明细
			BillOmExpDtl newExpDtl = new BillOmExpDtl();
			newExpDtl.setLocno(billOmExp.getLocno());
			newExpDtl.setExpNo(expNo);
			newExpDtl.setSourceNo(billOmExp.getExpNo());
			int dcount = billOmExpDtlMapper.insertSplitByExpDtl(newExpDtl);
			if (dcount < 1) {
				throw new ServiceException(billOmExp.getExpNo()
						+ "拆分失败,没有明细可拆分!");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void toClass1ForExp(BillOmExp billOmExp) throws ServiceException {
		try {
			BillOmExpKey expKey = new BillOmExpKey();
			expKey.setLocno(billOmExp.getLocno());
			expKey.setExpNo(billOmExp.getExpNo());
			expKey.setExpType(billOmExp.getExpType());
			BillOmExp exp = (BillOmExp) billOmExpMapper.selectByPrimaryKey(expKey);
			if (exp == null) {
				throw new ServiceException(billOmExp.getExpNo() + "查找发货通知单失败!");
			}
			if (SOURCE_TYPE.equals(exp.getSourceType())) {
				throw new ServiceException(billOmExp.getExpNo() + "拆分创建的发货单不能拆分!");
			}
			if (SPLIT_STATUS.equals(exp.getSplitStatus())) {
				throw new ServiceException(billOmExp.getExpNo() + "发货单已拆分,不能再次拆分!");
			}
			if (!(CLASS_TYPE.equals(exp.getClassType()))) {
				throw new ServiceException(billOmExp.getExpNo() + "只能拆分分货类型为【存储发货】的单据!");
			}
			if (!(STATUS12.equals(exp.getStatus())||STATUS16.equals(exp.getStatus())||STATUS40.equals(exp.getStatus())||STATUS50.equals(exp.getStatus()))) {
				throw new ServiceException(billOmExp.getExpNo() + "只能拆分状态为只有状态为【部分分配】、【部分调度】、【部分复核】、【部分装车】的单据!");
			}
			
			// 自定生成单号,生成主档数据
			String expNo = procCommonService.procGetSheetNo(billOmExp.getLocno(), CNumPre.OM_EXPNO_PRE);
			BillOmExp newExp = new BillOmExp();
			newExp.setLocno(billOmExp.getLocno());
			newExp.setExpNo(expNo);
			newExp.setExpType(billOmExp.getExpType());
			newExp.setSourceNo(billOmExp.getExpNo());
			newExp.setSourceType(SOURCE_TYPE);
			newExp.setCreator(billOmExp.getCreator());
			newExp.setEditor(billOmExp.getEditor());
			newExp.setClassType(CLASS_TYPE2);
			int count = billOmExpMapper.insertSplitByExp(newExp);
			if (count < 1) {
				throw new ServiceException(billOmExp.getExpNo() + "拆分单据失败!");
			}
			// 更新单据为已拆分
			BillOmExp upExp = new BillOmExp();
			upExp.setLocno(billOmExp.getLocno());
			upExp.setExpNo(billOmExp.getExpNo());
			upExp.setExpType(billOmExp.getExpType());
			upExp.setSplitStatus(SPLIT_STATUS);
			int ucount = billOmExpMapper.updateByPrimaryKeySelective(upExp);
			if (ucount < 1) {
				throw new ServiceException(billOmExp.getExpNo() + "更新单据拆分状态失败!");
			}

			// 生成明细
			BillOmExpDtl newExpDtl = new BillOmExpDtl();
			newExpDtl.setLocno(billOmExp.getLocno());
			newExpDtl.setExpNo(expNo);
			newExpDtl.setSourceNo(billOmExp.getExpNo());
			int dcount = billOmExpDtlMapper.insertSplitASNByExpDtl(newExpDtl);
			if (dcount < 1) {
				throw new ServiceException(billOmExp.getExpNo()+ "拆分失败,没有明细可拆分!");
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billOmExpMapper.selectSumQty(params, authorityParams);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findCountExpForPre(BillOmExp params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmExpMapper.selectCountExpPre(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmExp> findByPageExpForPre(SimplePage page, String orderByField,
			String orderBy, BillOmExp params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmExpMapper.selectByPageExpPre(page, orderByField, orderBy,
					params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
}