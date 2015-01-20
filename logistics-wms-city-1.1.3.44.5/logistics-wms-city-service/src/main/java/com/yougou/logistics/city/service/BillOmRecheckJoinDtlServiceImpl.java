package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.Iterator;
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
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto2;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.StoreMapper;
import com.yougou.logistics.city.dal.mapper.BillOmRecheckJoinDtlMapper;

/**
 * 
 * 分货交接单明细service实现
 * 
 * @author luo.hl
 * @date 2013-10-11 上午11:22:10
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmRecheckJoinDtlServiceImpl")
class BillOmRecheckJoinDtlServiceImpl extends BaseCrudServiceImpl implements BillOmRecheckJoinDtlService {
	@Resource
	private BillOmRecheckJoinDtlMapper billOmRecheckJoinDtlMapper;
	@Resource
	private StoreMapper storeMapper;

	@Override
	public BaseCrudMapper init() {
		return billOmRecheckJoinDtlMapper;
	}
	

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectNoReCheckSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckJoinDtlMapper.selectNoReCheckSumQty(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}



	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectNoReCheckedSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckJoinDtlMapper.selectNoReCheckedSumQty(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}


	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<?> findRecheckNo(SimplePage page, Map<?, ?> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			//箱号不为空
			if (StringUtils.isNotEmpty(String.valueOf(params.get("labelNo")))) {
				return billOmRecheckJoinDtlMapper.selectRecheckNoByConLabel(page, params,authorityParams);
			} else {
				return billOmRecheckJoinDtlMapper.selectRecheckNoByItemNo(page, params,authorityParams);
			}
			//商品编号为空
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findRecheckNoCount(Map<?, ?> params,AuthorityParams authorityParams) throws ServiceException {
		try {
			//箱号不为空
			if (StringUtils.isNotEmpty(String.valueOf(params.get("labelNo")))) {
				return billOmRecheckJoinDtlMapper.selectRecheckNoCountByConLabel(params, authorityParams);
			} else {
				return billOmRecheckJoinDtlMapper.selectRecheckNoCountByItemNo(params,authorityParams);
			}
			//商品编号为空
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findNoReCheckCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmRecheckJoinDtlMapper.selectNoReCheckCount(map,authorityParams);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<?> findNoReCheck(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ServiceException {
		try {
			Map<String, Object> curMap = new HashMap<String, Object>();
			curMap.put("endRowNum", page.getEndRowNum());
			curMap.put("startRowNum", page.getStartRowNum());
			String key = "";

			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
				key = String.valueOf(iterator.next());
				curMap.put(key, map.get(key));
			}
			return billOmRecheckJoinDtlMapper.selectNoReCheck(curMap,authorityParams);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	/**
	 * 查询已交接的箱数量
	 * @param map
	 * @return
	 * @throws DaoException
	 */
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findReCheckedCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmRecheckJoinDtlMapper.selectReCheckedCount(map,authorityParams);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	/**
	 * 查询已交接的箱集合
	 * @param map
	 * @param page TODO
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("rawtypes")
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmRecheckJoinDto> findReChecked(Map<?, ?> map, SimplePage page,AuthorityParams authorityParams) throws ServiceException {
		try {
			Map<String, Object> curMap = new HashMap<String, Object>();
			curMap.put("endRowNum", page.getEndRowNum());
			curMap.put("startRowNum", page.getStartRowNum());
			String key = "";

			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
				key = String.valueOf(iterator.next());
				curMap.put(key, map.get(key));
			}
			return billOmRecheckJoinDtlMapper.selectReChecked(curMap,authorityParams);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<BillOmRecheckJoinDto2> findItemDetail(@Param("dtl") BillOmRecheckJoinDto dtl) throws ServiceException {
		try {
			return billOmRecheckJoinDtlMapper.selectItemDetail(dtl);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	
	@Override
	public BillOmRecheckDtl selectReCheckNoByLabelNo(Map<?, ?> map) throws ServiceException {
		try {
			return billOmRecheckJoinDtlMapper.selectReCheckNoByLabelNo(map);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> sendReCheck(String rowIdstr, String locno, String user, List<BillOmRecheck> recheckList) throws ServiceException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String msg1 = "N";
		String msg2 = "交接失败！";
		try {
			if(!CommonUtil.hasValue(recheckList)){
				throw new ServiceException("所选的复核单参数为空！");
			}
			//String[] strs = rowIdstr.split(",");
			int num = 1;
			String strSubNo = "";
			for (BillOmRecheck billOmRecheck : recheckList) {
				String boxNo = billOmRecheck.getBoxNo();
				String recheckNo = billOmRecheck.getRecheckNo();
				String storeNo = billOmRecheck.getStoreNo();
				if(StringUtils.isNotBlank(locno) 
						&& StringUtils.isNotBlank(boxNo)
						&& StringUtils.isNotBlank(recheckNo)
						&& StringUtils.isNotBlank(storeNo)) {
					//检查客户状态是否为正常
					Store store = new Store();
					store.setStoreNo(storeNo);
					store.setStatus("0");
					int result = storeMapper.queryStoreCount(store);
					if(result <= 0){
						throw new ServiceException("所选数据中存在非正常状态的门店，请重新选择！");
					}
					strSubNo += recheckNo+"|"+boxNo+",";
					if(num % 200 == 0 || recheckList.size() == num){
						//根据箱号更新状态
						Map<String, String> paramMap2 = new HashMap<String, String>();
						paramMap2.put("I_locno", locno);
						paramMap2.put("I_strRecheckBoxs", strSubNo);
						paramMap2.put("I_creator", user);
						billOmRecheckJoinDtlMapper.checkStatus(paramMap2);
						String stroutmsg = paramMap2.get("O_msg");
						if(StringUtils.isNotBlank(stroutmsg)){
							String[] msgs = stroutmsg.split("\\|");
							if(msgs != null && msgs.length >= 1) {
								msg1 = msgs[0];
								if(msgs.length >= 2) {
									msg2 = msgs[1];
								}
							}
							if (!"Y".equals(msg1)) {
				   				throw new ServiceException(msg2);
				   			}else{
				   				strSubNo = "";
				   			}
						} else {
							throw new ServiceException(msg2);
						}
					}
					num++;
				}
			}
			msg2 = "交接成功！";
	   		mapObj.put("flag", "success");
			mapObj.put("msg", msg2);
			
//			if (StringUtils.isNotBlank(rowIdstr)) {
//				String[] strs = rowIdstr.split(",");
//				int num = 1;
//				String strSubNo = "";
//				for (String str : strs) {
//					String[] strSplit = str.split("-");
//					String boxNo = strSplit[0];
//					String recheckNo = strSplit[1];
//					String storeNo = strSplit[2];
//					if(StringUtils.isNotBlank(locno) 
//							&& StringUtils.isNotBlank(boxNo)
//							&& StringUtils.isNotBlank(recheckNo)
//							&& StringUtils.isNotBlank(storeNo)) {
//						//检查客户状态是否为正常
//						Store store = new Store();
//						store.setStoreNo(storeNo);
//						store.setStatus("0");
//						int result = storeMapper.queryStoreCount(store);
//						if(result <= 0){
//							throw new ServiceException("所选数据中存在非正常状态的门店，请重新选择！");
//						}
//						strSubNo += recheckNo+"|"+boxNo+",";
//						if(num % 200 == 0 || strs.length == num){
//							//根据箱号更新状态
//							Map<String, String> paramMap2 = new HashMap<String, String>();
//							paramMap2.put("I_locno", locno);
//							paramMap2.put("I_strRecheckBoxs", strSubNo);
//							paramMap2.put("I_creator", user);
//							billOmRecheckJoinDtlMapper.checkStatus(paramMap2);
//							String stroutmsg = paramMap2.get("O_msg");
//							if(StringUtils.isNotBlank(stroutmsg)){
//								String[] msgs = stroutmsg.split("\\|");
//								if(msgs != null && msgs.length >= 1) {
//									msg1 = msgs[0];
//									if(msgs.length >= 2) {
//										msg2 = msgs[1];
//									}
//								}
//								if (!"Y".equals(msg1)) {
//					   				throw new ServiceException(msg2);
//					   			}else{
//					   				strSubNo = "";
//					   			}
//							} else {
//								throw new ServiceException(msg2);
//							}
//						}
//						num++;
//					}
//				}
//				
//				msg2 = "交接成功！";
//   	   			mapObj.put("flag", "success");
//   				mapObj.put("msg", msg2);
//			}
			return mapObj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void queryReCheck(Map<String, String> paramMap2) throws ServiceException {
		String msg1 = "";
		String msg2 = "";
		try{
			billOmRecheckJoinDtlMapper.checkStatus(paramMap2);
			String stroutmsg = paramMap2.get("O_msg");
			
			if(StringUtils.isNotBlank(stroutmsg)){
				String[] msgs = stroutmsg.split("\\|");
				if(msgs != null && msgs.length >= 1) {
					msg1 = msgs[0];
					if(msgs.length >= 2) {
						msg2 = msgs[1];
					}
				}
				if (!"Y".equals(msg1)) {
	   				throw new ServiceException(msg2);
	   			}
			} else {
				throw new ServiceException(msg2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
	}
	
}