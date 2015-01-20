package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.enums.BillConStoreLockEnums;
import com.yougou.logistics.city.common.enums.BillWmPlanStatusEnums;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillOmOutstockKey;
import com.yougou.logistics.city.common.model.BillWmPlan;
import com.yougou.logistics.city.common.model.BillWmPlanKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillConStorelockDtlMapper;
import com.yougou.logistics.city.dal.database.BillConStorelockMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockMapper;
import com.yougou.logistics.city.dal.mapper.BillWmPlanMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Fri Oct 18 16:35:13 CST 2013
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
@Service("billOmOutstockDtlService")
class BillOmOutstockDtlServiceImpl extends BaseCrudServiceImpl implements BillOmOutstockDtlService {

	private final static String RESULTY = "Y";

	@Resource
	private BillOmOutstockDtlMapper billOmOutstockDtlMapper;

	@Resource
	private BillOmOutstockMapper billOmOutstockMapper;

	@Resource
	private ProcCommonService procCommonService;

	@Resource
	private BillConStorelockMapper billConStorelockMapper;

	@Resource
	private BillConStorelockDtlMapper billConStorelockDtlMapper;

	@Resource
	private ItemMapper itemMapper;

	@Resource
	private BillWmPlanMapper billWmPlanMapper;

	@Resource
	private BillAccControlService billAccControlService;

	private final static String OWNERNOBL = "BL";

	private final static String STATUS90 = "90";

	private final static String STATUS30 = "30";

	private final static String STATUS10 = "10";

	@Override
	public BaseCrudMapper init() {
		return billOmOutstockDtlMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findRecheckOutstockItemCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectRecheckOutstockItemCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmOutstockDtlDto> findRecheckOutstockItemByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectRecheckOutstockItemByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectRecheckOutstockItemSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectRecheckOutstockItemSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findConContentGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectConContentGroupByCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmOutstockDtlDto> findConContentGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectConContentGroupByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findMoveStockGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectMoveStockGroupByCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmOutstockDtlDto> findMoveStockGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectMoveStockGroupByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectMoveStockSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectMoveStockSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void editDetail(List<BillOmOutstockDtl> oList, BillOmOutstock billOmOutstockParam, SystemUser user)
			throws ServiceException {

		try {
			String locno = user.getLocNo();
			String creator = user.getLoginName();
			String outstockNo = billOmOutstockParam.getOutstockNo();

			BillOmOutstockKey billOmOutstockKey = new BillOmOutstockKey();
			billOmOutstockKey.setLocno(locno);
			billOmOutstockKey.setOutstockNo(outstockNo);
			BillOmOutstock billOmOutstock = (BillOmOutstock) billOmOutstockMapper.selectByPrimaryKey(billOmOutstockKey);
			//String status = billOmOutstock.getStatus();

			if (billOmOutstock == null || !billOmOutstock.getStatus().equals(STATUS10)) {
				throw new ManagerException("非新建状态下的单据，不允许修改！");
			}
			Date dd = new Date();
			String loginName = user.getLoginName();
			String username = user.getUsername();
			billOmOutstock.setEditor(loginName);
			billOmOutstock.setEditorname(username);
			billOmOutstock.setEdittm(dd);
			billOmOutstockMapper.updateByPrimaryKeySelective(billOmOutstock);
			//检查单据是否已被RF操作
			if (isOperateByRF(locno, outstockNo)) {
				throw new ManagerException("正在下架或下架中,不能进行回单操作！");
			}

			BillOmOutstockDtl updateDtl = new BillOmOutstockDtl();
			updateDtl.setLocno(locno);
			updateDtl.setOutstockNo(outstockNo);
			if (CommonUtil.hasValue(oList)) {
				for (BillOmOutstockDtl b : oList) {
					if (b.getDivideId() != null) {
						//保存明细的实际移库数量   
						updateDtl.setOwnerNo(b.getOwnerNo());
						updateDtl.setRealQty(b.getRealQty());
						updateDtl.setDivideId(b.getDivideId());
						updateDtl.setOutstockDate(dd);
//						updateDtl.setOutstockName(loginName);
//						updateDtl.setOutstockNameCh(username);
//						updateDtl.setInstockDate(dd);
//						updateDtl.setInstockName(loginName);
//						updateDtl.setInstockNameCh(username);
						billOmOutstockDtlMapper.updateByPrimaryKeySelective(updateDtl);
					} else {
						throw new ManagerException("参数非法！");
					}
				}
			}

			//保存明细后进行记账操作
			Map<String, String> map = new HashMap<String, String>();
			map.put("v_locno", locno);
			map.put("v_outstock_no", outstockNo);
			map.put("v_creator", creator);
			billOmOutstockDtlMapper.procOmPlanOutStockDtlQuery(map);
			if (!RESULTY.equals(map.get("stroutmsg"))) {
				String message = "";
				String msg = map.get("stroutmsg");
				if (StringUtils.isNotBlank(msg)) {
					String[] msgs = msg.split("\\|");
					message = msgs[1];
				}
				throw new ServiceException(message);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void omPlanOutStockAudit(String outstockNo, SystemUser user) throws ServiceException {
		try {
			String locno = user.getLocNo();
			String creator = user.getLoginName();
			BillOmOutstockKey billOmOutstockKey = new BillOmOutstockKey();
			billOmOutstockKey.setLocno(locno);
			billOmOutstockKey.setOutstockNo(outstockNo);
			BillOmOutstock billOmOutstock = (BillOmOutstock) billOmOutstockMapper.selectByPrimaryKey(billOmOutstockKey);
			String status = billOmOutstock.getStatus();

			if (!status.equals(STATUS10) && !status.equals(STATUS30)) {
				throw new ManagerException("非新建、下架完成状态的单据，不允许审核！");
			}

			//检查单据是否已被RF操作
			if (isOperateByRF(locno, outstockNo)) {
				throw new ManagerException("正在下架或下架中,不能进行回单操作！");
			}

			billOmOutstock.setEditor(user.getLoginName());
			billOmOutstock.setEditorname(user.getUsername());
			billOmOutstock.setEdittm(new Date());
			billOmOutstockMapper.updateByPrimaryKeySelective(billOmOutstock);
			
			//将实际移库数量改为等于计划数量
			BillOmOutstock billOmOutstockParam = new BillOmOutstock();
			billOmOutstockParam.setLocno(locno);
			billOmOutstockParam.setOutstockNo(outstockNo);
			if (status.equals(STATUS30)) {
				billOmOutstockDtlMapper.updateRealQtyEqInstockQty(billOmOutstockParam);
			} else {
				billOmOutstockDtlMapper.updateRealQtyByPrimaryKeySelective(billOmOutstockParam);
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("v_locno", locno);
			map.put("v_outstock_no", outstockNo);
			map.put("v_creator", creator);
			billOmOutstockDtlMapper.procOmPlanOutStockDtlQuery(map);
			if (!RESULTY.equals(map.get("stroutmsg"))) {
				String message = "";
				String msg = map.get("stroutmsg");
				if (StringUtils.isNotBlank(msg)) {
					String[] msgs = msg.split("\\|");
					message = msgs[1];
				}
				throw new ServiceException(message);
			}

			//转库存锁定
			//toStoreLockUtil(locno, outstockNo, creator);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillOmOutstockDtl> findStoreNo(BillOmOutstockDtl dtl) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectStoreNo(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtl> findStockDtl(BillOmOutstockDtl dtl) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectStockDtl(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtl> findStockDtlNoStoreNo(BillOmOutstockDtl dtl) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectStockDtl(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtlDto> findRecheckOutstockItem(Map<String, Object> params) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectRecheckOutstockItem(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findBillOmOutstockCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectBillOmOutstockCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmOutstockDtl> findBillOmOutstockByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectBillOmOutstockByPage(page, orderByField, orderBy, params,
					authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<Store> findStoreByParam(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectStoreByParam(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return this.billOmOutstockDtlMapper.selectSumQty(map, authorityParams);
	}

	public List<Map<String, Object>> getPrintInf4AreaCut(String locno, String keystr, String curOper)
			throws ServiceException {
		try {
			if (StringUtils.isEmpty(keystr)) {
				throw new ServiceException("参数错误");
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			String[] keys = keystr.split(",");
			for (String key : keys) {
				BillOmOutstock stock = new BillOmOutstock();
				stock.setLocno(locno);
				stock.setOutstockNo(key);
				List<BillOmOutstockDtlDto> list = billOmOutstockDtlMapper.selectAllDetail(stock);
				BillOmOutstock sotckResult = this.billOmOutstockMapper.selectByPrimaryKey(stock);
				List<Map<String, Object>> dtlList = new ArrayList<Map<String, Object>>();
				Map<String, Object> single = new HashMap<String, Object>();
				List<String> allSizeNo = new ArrayList<String>();
				single.put("list", dtlList);
				single.put("allSizeNo", allSizeNo);
				single.put("curDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				single.put("curOper", curOper);
				single.put("stock", sotckResult);
				BigDecimal allSum = new BigDecimal(0);
				resultList.add(single);
				//要排序，所有用TreeMap
				Map<String, List<BillOmOutstockDtlDto>> temObj = new TreeMap<String, List<BillOmOutstockDtlDto>>();
				String locateNo = "";
				for (BillOmOutstockDtlDto dtl : list) {
					if (StringUtils.isEmpty(locateNo)) {
						locateNo = dtl.getLocateNo();
						single.put("locateNo", locateNo);
					}

					// 将所有的尺码放到一个set里面，会自动去重
					if (!allSizeNo.contains(dtl.getSizeNo())) {
						allSizeNo.add(dtl.getSizeNo());
					}
					allSum = allSum.add(dtl.getItemQty());
					// 根据储位 商品编码分组
					String cellItem = dtl.getsCellNo() + dtl.getItemNo();
					String value = String.valueOf(temObj.get(cellItem));
					if (value == null || "null".equals(value)) {
						// 创建一个行对象
						List<BillOmOutstockDtlDto> tempList = new ArrayList<BillOmOutstockDtlDto>();
						temObj.put(cellItem, tempList);
						tempList.add(dtl);
					} else {
						List<BillOmOutstockDtlDto> tempList = temObj.get(cellItem);
						tempList.add(dtl);
					}
				}
				Collections.sort(allSizeNo);
				single.put("allSum", allSum);
				// 遍历temObj再根据 cellno storeno itemno 重新分组
				Iterator<String> iterator = temObj.keySet().iterator();
				while (iterator.hasNext()) {
					Map<String, Object> lineObj = new HashMap<String, Object>();
					dtlList.add(lineObj);
					List<BillOmOutstockDtlDto> tempList = temObj.get(iterator.next());
					List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
					lineObj.put("children", childList);
					Map<String, Object> obj = new HashMap<String, Object>();
					for (BillOmOutstockDtlDto dtl : tempList) {
						if (lineObj.get("sum" + dtl.getSizeNo()) != null) {
							BigDecimal lineSum = (BigDecimal) lineObj.get("sum" + dtl.getSizeNo());
							lineSum = lineSum.add(dtl.getItemQty());
							lineObj.put("sum" + dtl.getSizeNo(), lineSum);
						} else {
							lineObj.put("sum" + dtl.getSizeNo(), dtl.getItemQty());
						}

						String cellStoreItem = dtl.getsCellNo() + dtl.getStoreNo() + dtl.getItemNo();
						String value = String.valueOf(obj.get(cellStoreItem));
						// 新创建一个子对象
						if (((value == null || "null".equals(value)))
								&& lineObj.get(dtl.getStoreNo() + "storeObj") == null) {
							obj = new HashMap<String, Object>();
							lineObj.put(dtl.getStoreNo() + "storeObj", obj);
							obj.put(cellStoreItem, cellStoreItem);
							obj.put("sCellNo", dtl.getsCellNo());
							obj.put("storeNo", dtl.getStoreNo());
							obj.put("storeName", dtl.getStoreName());
							obj.put("itemNo", dtl.getItemNo());
							obj.put("colorName", dtl.getColorName() == null ? "" : dtl.getColorName());
							obj.put(dtl.getSizeNo(), dtl.getItemQty());
							obj.put("lineSum", dtl.getItemQty());
							childList.add(obj);
							// 找到客户编码一样的子对象
						} else if (((value == null || "null".equals(value)))
								&& lineObj.get(dtl.getStoreNo() + "storeObj") != null) {
							obj = (Map<String, Object>) lineObj.get(dtl.getStoreNo() + "storeObj");
							obj.put(cellStoreItem, cellStoreItem);
							obj.put("sCellNo", dtl.getsCellNo());
							obj.put("storeNo", dtl.getStoreNo());
							obj.put("storeName", dtl.getStoreName());
							obj.put("itemNo", dtl.getItemNo());
							obj.put("colorName", dtl.getColorName() == null ? "" : dtl.getColorName());
							obj.put(dtl.getSizeNo(), dtl.getItemQty());
							obj.put("lineSum", dtl.getItemQty());
						} else {
							Object sizeNoQtyObj = obj.get(dtl.getSizeNo());
							if (sizeNoQtyObj != null) {
								BigDecimal sizeNoQty = (BigDecimal) sizeNoQtyObj;
								sizeNoQty = sizeNoQty.add(dtl.getItemQty());
								obj.put(dtl.getSizeNo(), sizeNoQty);
							} else {
								obj.put(dtl.getSizeNo(), dtl.getItemQty());
							}
							if (obj.get("lineSum") != null) {
								BigDecimal lineSum = (BigDecimal) obj.get("lineSum");
								lineSum = lineSum.add(dtl.getItemQty());
								obj.put("lineSum", lineSum);
							} else {
								obj.put("lineSum", dtl.getItemQty());
							}
						}
					}
				}
			}
			return resultList;
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void procImmediateMoveStock(List<BillOmOutstockDtlDto> lists,SystemUser user) throws ServiceException {
		try {
			if (!CommonUtil.hasValue(lists)) {
				throw new ServiceException("参数非法!");
			}
			String locno = user.getLocNo();
			String auditor = user.getLoginName();
			String auditorname = user.getUsername();
			
			String outstockNo = "";
			Map<String, String> mapParams;
			for (BillOmOutstockDtlDto b : lists) {
				mapParams = new HashMap<String, String>();
				mapParams.put("I_locno", locno);
				mapParams.put("I_destCellNo", b.getdCellNo());
				
				billOmOutstockDtlMapper.procValidteCellNo(mapParams);
				if (!"Y|".equals(mapParams.get("O_msg"))) {
					String message = "";
					String msg = mapParams.get("O_msg");
					if (StringUtils.isNotBlank(msg)) {
						String[] msgs = msg.split("\\|");
						message = msgs[1];
					}
					throw new ServiceException(message);
				} 
				
				mapParams.put("I_outStockNo", outstockNo);
				mapParams.put("I_ownerNo", OWNERNOBL);
				mapParams.put("I_sysNo", b.getSysNo());
				mapParams.put("I_barcode", b.getBarcode());
				mapParams.put("I_sCellNo", b.getsCellNo());
				mapParams.put("I_qty", String.valueOf(b.getItemQty()));
				mapParams.put("I_creator", b.getCreator());
				
				billOmOutstockDtlMapper.procImmediateMoveStock(mapParams);
				if (!"Y|".equals(mapParams.get("O_msg"))) {
					String message = "";
					String msg = mapParams.get("O_msg");
					if (StringUtils.isNotBlank(msg)) {
						String[] msgs = msg.split("\\|");
						message = msgs[1];
					}
					throw new ServiceException(message);
				} else {
					outstockNo = mapParams.get("O_outStockNo");
				}
			}
			
			Date audittm = new Date();
			//修改状态为90移库完成
			BillOmOutstock billOmOutstock = new BillOmOutstock();
			billOmOutstock.setLocno(locno);
			billOmOutstock.setOutstockNo(outstockNo);
			billOmOutstock.setStatus(STATUS90);
			billOmOutstock.setAuditor(auditor);
			billOmOutstock.setAudittm(audittm);
			billOmOutstock.setAuditorname(auditorname);
			billOmOutstock.setEditor(auditor);
			billOmOutstock.setEditorname(auditorname);
			billOmOutstock.setEdittm(audittm);
			int count = billOmOutstockMapper.updateByPrimaryKeySelective(billOmOutstock);
			if (count < 1) {
				throw new ServiceException(outstockNo + "更新主档状态失败!");
			}

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	//    @Override
	//	public void toStoreLock(String locno, String outstockNo, String creator) throws ServiceException {
	//    	toStoreLockUtil(locno, outstockNo, creator);
	//	}

	/**
	 * 转库存锁定
	 * @param locno
	 * @param outstockNo
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void toStoreLockUtil(String locno, String outstockNo, String creator) throws ServiceException {

		try {
			BillOmOutstockKey key = new BillOmOutstockKey();
			key.setLocno(locno);
			key.setOutstockNo(outstockNo);
			BillOmOutstock billOmOutstock = (BillOmOutstock) billOmOutstockMapper.selectByPrimaryKey(key);
			if (billOmOutstock == null) {
				throw new ServiceException(outstockNo + "查询移库回单失败!");
			}

			//必须是退厂计划才进行锁定操作
			if (StringUtils.isNotBlank(billOmOutstock.getSourceNo()) && !"N".equals(billOmOutstock.getSourceNo())
					&& !"0".equals(billOmOutstock.getSourceType())) {

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", locno);
				params.put("outstockNo", outstockNo);
				List<BillOmOutstockDtlDto> listOutstockDtls = billOmOutstockDtlMapper
						.selectOutstockDtlAndBarcode(params);
				if (!CommonUtil.hasValue(listOutstockDtls)) {
					throw new ServiceException(outstockNo + "查询移库回单明细失败!");
				}

				//1.生成库存锁定主档部分
				List<BillConStorelockDtl> addList = new ArrayList<BillConStorelockDtl>();
				String storelockNo = procCommonService.procGetSheetNo(locno, CNumPre.CON_STORELOCK_PRE);
				BillConStorelock storelock = new BillConStorelock();
				storelock.setLocno(billOmOutstock.getLocno());
				storelock.setOwnerNo(listOutstockDtls.get(0).getOwnerNo());
				storelock.setStorelockNo(storelockNo);
				storelock.setStorelockType(BillConStoreLockEnums.STORELOCK_TYPE1.getStatus());
				storelock.setStatus(BillConStoreLockEnums.STATUS10.getStatus());
				storelock.setCreator(creator);
				storelock.setCreatetm(new Date());
				storelock.setSourceType(billOmOutstock.getSourceType());
				storelock.setSourceNo(billOmOutstock.getSourceNo());
				storelock.setBusinessType(BillConStoreLockEnums.BUSINESS_TYPE2.getStatus());
				int mcount = billConStorelockMapper.insertSelective(storelock);
				if (mcount < 1) {
					throw new ServiceException(billOmOutstock.getSourceNo() + "转库存锁定生成数据失败!");
				}

				//2.生成库存锁定明细部分
				Long rowId = 0L;
				for (BillOmOutstockDtl bod : listOutstockDtls) {
					BillConStorelockDtl bcs = new BillConStorelockDtl();
					bcs.setLocno(locno);
					bcs.setOwnerNo(listOutstockDtls.get(0).getOwnerNo());
					bcs.setStorelockNo(storelockNo);
					bcs.setItemNo(bod.getItemNo());
					bcs.setSizeNo(bod.getSizeNo());
					bcs.setCellNo(bod.getdCellNo());
					bcs.setCellId(bod.getdCellId());
					bcs.setBarcode(bod.getBatchNo());
					bcs.setRowId(++rowId);
					bcs.setCreator(creator);
					bcs.setCreatetm(new Date());
					bcs.setItemQty(bod.getRealQty());
					bcs.setRealQty(new BigDecimal(0));
					bcs.setStatus(BillConStoreLockEnums.STATUS10.getStatus());
					bcs.setPackQty(bod.getPackQty() == null ? new BigDecimal(0) : bod.getPackQty());
					addList.add(bcs);
				}

				//3.批量新增
				billConStorelockDtlMapper.saveStorelockDtl(addList);
				//循环调用记账外部存储过程
				for (BillConStorelockDtl bsd : addList) {
					Item item = itemMapper.selectByCode(bsd.getItemNo(), null);//查询供应商
					BillAccControlDto controlDto = new BillAccControlDto();
					controlDto.setiLocno(bsd.getLocno());
					controlDto.setiOwnerNo(bsd.getOwnerNo());
					controlDto.setiPaperNo(bsd.getStorelockNo());
					controlDto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					controlDto.setiIoFlag("I");
					controlDto.setiCreator(bsd.getCreator());
					controlDto.setiRowId(new BigDecimal(bsd.getRowId()));
					controlDto.setiCellNo(bsd.getCellNo());
					controlDto.setiCellId(new BigDecimal(bsd.getCellId()));
					controlDto.setiItemNo(bsd.getItemNo());
					controlDto.setiSizeNo(bsd.getSizeNo());
					controlDto.setiPackQty(bsd.getPackQty());
					controlDto.setiSupplierNo(item.getSupplierNo());
					controlDto.setiOutstockQty(bsd.getItemQty());
					/**默认值**/
					controlDto.setiItemType("0");
					controlDto.setiQuality("0");
					controlDto.setiQty(new BigDecimal(0));
					controlDto.setiInstockQty(new BigDecimal(0));
					controlDto.setiStatus("0");
					controlDto.setiFlag("0");
					controlDto.setiHmManualFlag("1");
					controlDto.setiTerminalFlag("1");
					billAccControlService.procAccPrepareDataExt(controlDto);

					//调用外部存储过程
					BillAccControlDto dto = new BillAccControlDto();
					dto.setiPaperNo(bsd.getStorelockNo());
					dto.setiLocType("2");
					dto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					dto.setiIoFlag("I");
					dto.setiPrepareDataExt(new BigDecimal(bsd.getRowId()));
					dto.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dto);
				}

				//3.更新退厂计划状态为30已转锁定
				BillWmPlanKey planKey = new BillWmPlanKey();
				planKey.setLocno(locno);
				planKey.setOwnerNo(listOutstockDtls.get(0).getOwnerNo());
				planKey.setPlanNo(billOmOutstock.getSourceNo());
				BillWmPlan billWmPlan = (BillWmPlan) billWmPlanMapper.selectByPrimaryKey(planKey);
				if (billWmPlan == null) {
					throw new ServiceException(planKey.getPlanNo() + "更新30已锁定时,查询退厂计划单失败!");
				}
				billWmPlan.setStatus(BillWmPlanStatusEnums.STATUS30.getStatus());
				int pcount = billWmPlanMapper.updateByPrimaryKeySelective(storelock);
				if (pcount < 1) {
					throw new ServiceException(billWmPlan.getPlanNo() + "更新退厂计划单为<已转锁定>状态失败!");
				}

			}

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean isOperateByRF(String locno, String outstockNo) throws ServiceException {
		try {
			BillOmOutstock billOmOutstock = new BillOmOutstock();
			billOmOutstock.setLocno(locno);
			billOmOutstock.setOutstockNo(outstockNo);
			int count = billOmOutstockDtlMapper.selectIsOperateByRF(billOmOutstock);
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int saveByPlan(BillOmOutstock instock,SystemUser user) throws ServiceException {
		try {
			instock.setEditor(user.getLoginName());
			instock.setEditorname(user.getUsername());
			instock.setEdittm(new Date());
			billOmOutstockMapper.updateByPrimaryKeySelective(instock);
			return billOmOutstockDtlMapper.saveByPlan(instock);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int selectCheckDtlRealQty(BillOmOutstock instock) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectCheckDtlRealQty(instock);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public int selectCheckDtlRealQtyEq(BillOmOutstock instock) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectCheckDtlRealQtyEq(instock);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillOmOutstockDtl> selectOutstockDtlCheckoedQty(BillOmOutstock instock) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectOutstockDtlCheckoedQty(instock);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<String> selectSysNo(Map<String, String> map) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectSysNo(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<String> selectItemSizeKind(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectItemSizeKind(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectBoxQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectBoxQty(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectItemDetailByGroupCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectItemDetailByGroupCount(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillOmOutstockDtl> selectItemDetailByGroup(Map<String, Object> map, AuthorityParams authorityParams,
			SimplePage page) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectItemDetailByGroup(map, authorityParams, page);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillOmOutstockDtl> selectDetailBySizeNo(Map<String, Object> map) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectDetailBySizeNo(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectCheckAssignName(BillOmOutstock instock) throws ServiceException {
		try {
			return billOmOutstockDtlMapper.selectCheckAssignName(instock);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void updateOutstockName4AssignName(BillOmOutstock instock) throws ServiceException {
		try {
			billOmOutstockDtlMapper.updateOutstockName4AssignName(instock);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
