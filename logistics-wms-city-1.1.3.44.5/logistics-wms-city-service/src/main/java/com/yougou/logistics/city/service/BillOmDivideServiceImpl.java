package com.yougou.logistics.city.service;

import java.util.ArrayList;
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
import com.yougou.logistics.base.common.vo.ResultVo;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillOmDivideDtlMapper;
import com.yougou.logistics.city.dal.database.BillOmDivideMapper;
import com.yougou.logistics.city.dal.database.BillOmLocateDtlMapper;
import com.yougou.logistics.city.dal.database.OmTmpBoxGroupMapper;

/**
 * 
 * TODO: 分货任务单Service实现
 * 
 * @author su.yq
 * @date 2013-10-14 下午6:09:22
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDivideService")
class BillOmDivideServiceImpl extends BaseCrudServiceImpl implements BillOmDivideService {

	private final static String RESULTY = "Y";

	@Resource
	private BillOmDivideMapper billOmDivideMapper;

	@Resource
	private BillOmDivideDtlMapper billOmDivideDtlMapper;
	
	@Resource
	private BillOmLocateDtlMapper billOmLocateDtlMapper;

	@Resource
	private OmTmpBoxGroupMapper omTmpBoxGroupMapper;

	@Override
	public BaseCrudMapper init() {
		return billOmDivideMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> addBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ServiceException {

		if (!CommonUtil.hasValue(listBillOmDivide)) {
			throw new ServiceException("参数非法!");
		}

		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "fail";
		String msg = "";
		String businessType = "0";
		//		String receiptNos="";

		try {

			//			String serialNo = omTmpBoxGroupMapper.getNextvalId();
			//			for (BillOmDivide b : listBillOmDivide) {
			//				OmTmpBoxGroupKey key = new OmTmpBoxGroupKey();
			//				key.setLocno(b.getLocno());
			//				key.setSerialNo(serialNo);
			//				key.setBillNo(b.getReceiptNo());
			//				int k = omTmpBoxGroupMapper.insertSelective(key);
			//				if(k < 1){
			//					throw new ServiceException("新增收货单临时表失败!");
			//				}
			//			}

			//			for (BillOmDivide b : listBillOmDivide) {
			//				if(StringUtils.isNotBlank(b.getReceiptNo())){
			//					receiptNos += b.getReceiptNo()+",";
			//				}
			//			}
			//			
			//			if(StringUtils.isNotBlank(receiptNos)){
			//				receiptNos = receiptNos.substring(0, receiptNos.length()-1);
			//			}

			String expNo = "";
			String checkExpNos = "";
			for (BillOmDivide b : listBillOmDivide) {
				if (StringUtils.isNotBlank(b.getReceiptNo())) {
					expNo += b.getExpNo() + ",";
					checkExpNos += "'" + b.getExpNo() + "',";
				}
			}
			
			//验证是否已经调度
			if (StringUtils.isNotBlank(checkExpNos)){
				checkExpNos = checkExpNos.substring(0, checkExpNos.length() - 1);
				String checkBusinessType = listBillOmDivide.get(0).getBusinessType();
				String locno = listBillOmDivide.get(0).getLocno();
				if ("1".equals(checkBusinessType)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("locno", locno);
					map.put("checkExpNos", checkExpNos);
					List<BillOmLocateDtl> locateDtlList = billOmLocateDtlMapper.selectLocateDtlByExpNo(map);
					if(CommonUtil.hasValue(locateDtlList)){
						mapObj.put("flag", "warn");
						mapObj.put("msg", locateDtlList.get(0).getExpNo()+"发货通知单已经做调度,不能分货！");
						return mapObj;
					}
				}
			}
			
			if (StringUtils.isNotBlank(expNo)) {
				businessType = listBillOmDivide.get(0).getBusinessType();
				expNo = expNo.substring(0, expNo.length() - 1);
				String divideNoS = "N";
				String flagValue = "1";
				if (expNo != null && !expNo.equals("")) {
					BillOmDivide b = listBillOmDivide.get(0);
					String receiptNo = b.getReceiptNo();
					//					String[] res = receiptNo.split(",");

					b.setSerialNo(receiptNo);
					b.setDivideNoS(divideNoS);
					b.setFlag(flagValue);
					b.setExpNo(expNo);
					b.setReceiptNo("12345");
					if ("1".equals(businessType)) {
						billOmDivideMapper.insertBillOmDivideBoxCC(b);
					} else {
						billOmDivideMapper.insertBillOmDivide(b);
					}

					divideNoS = b.getDivideNo();

					String strOutMsg = b.getStrOutMsg();
					if (!RESULTY.equals(strOutMsg)) {
						if (StringUtils.isNotBlank(strOutMsg)) {
							String[] msgs = strOutMsg.split("\\|");
							msg = msgs[1];
						}
						flag = "warn";
						throw new ServiceException(msg);
					}
				}
			} else {
				throw new ServiceException("无可操作发货单！");
			}

			flag = "success";
			msg = "创建成功!";
			mapObj.put("flag", flag);
			mapObj.put("msg", msg);

			return mapObj;
		} catch (Exception e) {
			throw new ServiceException(msg);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteBillOmDivide(List<BillOmDivide> listBillOmDivide) throws ServiceException {
		try {
			String msg = "";
			if (!CommonUtil.hasValue(listBillOmDivide)) {
				throw new ServiceException("参数为空!");
			}

			for (BillOmDivide d : listBillOmDivide) {
				billOmDivideMapper.deleteBillOmDivide(d);
				String strOutMsg = d.getStrOutMsg();
				if (!RESULTY.equals(strOutMsg)) {
					if (StringUtils.isNotBlank(strOutMsg)) {
						String[] msgs = strOutMsg.split("\\|");
						msg = msgs[1];
					}
					throw new ServiceException(msg);
				}
			}

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public ResultVo modifyCompleteBillOmDivide(BillOmDivide divide) throws ServiceException {
		ResultVo vo = new ResultVo();
		try {

			billOmDivideMapper.updateCompleteBillOmDivide(divide);
			if (divide == null) {
				return vo;
			}

			String strOutMsg = divide.getStrOutMsg();
			if (!"Y".equals(strOutMsg)) {
				String[] returnArray = strOutMsg.split("\\|");
				if (returnArray.length > 1) {
					vo.setErrorCode(2);
					vo.setErrorMessage(returnArray[1]);
				}
			}

			if ("Y".equals(strOutMsg)) {
				vo.setErrorCode(1);
			}

			return vo;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDivide> selectDivideCollectByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		return billOmDivideMapper.selectDivideCollectByPage(page, orderByField, orderBy, params, authorityParams);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int selectDivideCollectCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		return billOmDivideMapper.selectDivideCollectCount(params, authorityParams);
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<Store> selectStoreByParam(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		return billOmDivideMapper.selectStoreByParam(params, authorityParams);
	}

	@Override
	public List<BillOmDivideDtl> queryRecheckBoxItem(Map<String, Object> params) throws ServiceException {
		return billOmDivideMapper.queryRecheckBoxItem(params);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void procOmDivideOver(BillOmDivide billOmDivide) throws ServiceException {
		try {
			//调用存储过程..添加验收表、库存表
			Map<String, String> map = new HashMap<String, String>();
			map.put("I_locno", billOmDivide.getLocno());
			map.put("I_divideNo", billOmDivide.getDivideNo());
			map.put("I_creator", billOmDivide.getCreator());
			billOmDivideMapper.procOmDivideOver(map);
			String strOutMsg = map.get("O_msg");
			if (!"Y|".equals(strOutMsg)) {
				String[] msgs = strOutMsg.split("\\|");
				throw new ServiceException(msgs[1]);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public List<Map<String, Object>> getBatchPrintInfo(List<BillOmDivide> listOmDivides) throws ServiceException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (listOmDivides == null || listOmDivides.isEmpty()) {
				throw new ServiceException("参数错误");
			}
			Map<String, Object> map = null;
			for (BillOmDivide omdivide : listOmDivides) {
				map = new HashMap<String, Object>();
				map.put("divideNo", omdivide.getDivideNo());
				List<String> sourceNoList = billOmDivideDtlMapper.selectAllSourceNo(omdivide);
				List<BillOmDivideDtl> dtlList = billOmDivideDtlMapper.selectAllDetail4Print(omdivide);
				map.put("dtlList", dtlList);
				map.put("sourceNoList", sourceNoList);
				list.add(map);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return list;
	}
}