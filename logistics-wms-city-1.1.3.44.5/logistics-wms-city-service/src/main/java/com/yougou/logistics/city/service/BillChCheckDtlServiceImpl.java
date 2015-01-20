package com.yougou.logistics.city.service;

import java.math.BigDecimal;
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
import org.springframework.util.CollectionUtils;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillChCheckDto;
import com.yougou.logistics.city.common.dto.baseinfo.SizeInfoMapDto;
import com.yougou.logistics.city.common.enums.BillChCheckAddFlagEnums;
import com.yougou.logistics.city.common.enums.BillChCheckCheckTypeEnums;
import com.yougou.logistics.city.common.enums.BillChCheckStautsEnums;
import com.yougou.logistics.city.common.enums.BillChPlanLimitBrandFlagEnums;
import com.yougou.logistics.city.common.enums.BillChPlanTypeEnums;
import com.yougou.logistics.city.common.enums.CmDefAreaAreaUsetypeEnums;
import com.yougou.logistics.city.common.model.BillChCheck;
import com.yougou.logistics.city.common.model.BillChCheckDtl;
import com.yougou.logistics.city.common.model.BillChPlan;
import com.yougou.logistics.city.common.model.BillChPlanDtlBrand;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.CmDefcellSimple;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.ItemBarcode;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.ResultVo;
import com.yougou.logistics.city.dal.database.BillChPlanDtlBrandMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillChCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillChCheckMapper;
import com.yougou.logistics.city.dal.mapper.BillChPlanMapper;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;
import com.yougou.logistics.city.dal.mapper.ItemBarcodeMapper;

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
@Service("billChCheckDtlService")
class BillChCheckDtlServiceImpl extends BaseCrudServiceImpl implements
		BillChCheckDtlService {
	@Resource
	private BillChCheckDtlMapper billChCheckDtlMapper;
	
	@Resource
	private SizeInfoService sizeInfoService;
	@Resource
	private BillChCheckMapper billChCheckMapper;

	@Resource
	private BillChPlanMapper billChPlanMapper;
	@Resource
	private ItemMapper itemMapper;
	@Resource
	private ItemBarcodeMapper itemBarcodeMapper;
	@Resource
	private CmDefcellMapper cmDefcellMapper;
	@Resource
	private BillChPlanDtlBrandMapper billChPlanDtlBrandMapper;

	private static final int MAXCOUNT = 1000;

	@Override
	public BaseCrudMapper init() {
		return billChCheckDtlMapper;
	}

	public List<BillChCheckDtl> selectCellNo(BillChCheck check)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectCellNo(check);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public List<BillChCheckDtl> findCellNobyPlan(String planNo, String locNo)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectCellNobyPlan(planNo, locNo);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	private void getBrandAndBarcode(BillChCheckDtl d) throws ServiceException {
		/* 验证商品编码并获取品牌编码 */
		Item i = new Item();
		i.setItemNo(d.getItemNo());
		i = itemMapper.selectByPrimaryKey(i);
		if (i == null) {
			throw new ServiceException("商品【" + d.getItemNo() + "】不存在");
		}
		d.setBrandNo(i.getBrandNo());
		/* 验证商品编码和尺码并获取条码 */
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("packageId", 0);
		p.put("itemNo", d.getItemNo());
		p.put("sizeNo", d.getSizeNo());
		List<ItemBarcode> ibList = itemBarcodeMapper.selectByParams(null, p);
		if (!CommonUtil.hasValue(ibList)) {
			throw new ServiceException("商品:" + d.getItemNo() + "<br>尺码:"
					+ d.getSizeNo() + "<br>不存在");
		}
		d.setBarcode(ibList.get(0).getBarcode());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public ResultVo saveCheckDtl(List<BillChCheckDtl> insertList,
			List<BillChCheckDtl> updateList, List<BillChCheckDtl> deleteList,
			String checkNo, String locno) throws ServiceException {
		try {
			ResultVo resultVo = new ResultVo();
			Date curDate = null;
			String checkWorker = null;
			String checkWorkerName = null;
			// 获取盘点单
			BillChCheck param = new BillChCheck();
			param.setLocno(locno);
			param.setCheckNo(checkNo);
			BillChCheck result = billChCheckMapper.selectByPrimaryKey(param);
			if(result == null){
				throw new ServiceException("盘点单已不存在，不能按照计划保存");
			}
			if (!BillChCheckStautsEnums.STATUS20.getStatus().equals(
					result.getStatus())) {
				throw new ServiceException("盘点单非发单状态，不能按照计划保存");
			}
			// 删除明细
			if (CommonUtil.hasValue(deleteList)) {
				for (BillChCheckDtl dtl : deleteList) {
					billChCheckDtlMapper.deleteByPrimarayKeyForModel(dtl);
				}
			}
			
			// 获取改盘点单对应的计划单下的所有限制品牌JYS BEGIN
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("planNo", result.getPlanNo());
			List<BillChPlanDtlBrand> bcpdbs = billChPlanDtlBrandMapper.selectByParams(null, params);
			Map<String, String> bcpdbMap = new HashMap<String, String>();
			for(BillChPlanDtlBrand bcpdb:bcpdbs){
				bcpdbMap.put(bcpdb.getBrandNo(), bcpdb.getBrandNo());
			}
			// 获取改盘点单对应的计划单下的所有限制品牌JYS END
			if (BillChPlanTypeEnums.CELL.getValue()
					.equals(result.getPlanType())) {// 储位盘
				// 只允许添加盘点计划明细内的储位，而商品只要合法
				Map<String, String> existCellMap = new HashMap<String, String>();// 按储位盘，允许的储位
				List<BillChCheckDtl> cellList = billChCheckDtlMapper
						.selectCellNobyCheck(checkNo, locno);
				for (BillChCheckDtl d : cellList) {
					String cellNo = d.getCellNo();
					if (existCellMap.get(cellNo) == null) {
						existCellMap.put(cellNo, cellNo);
					}
				}
				for (BillChCheckDtl d : insertList) {// 校验新增
					/* 验证储位 */
					if (existCellMap.get(d.getCellNo()) == null) {
						throw new ServiceException("储位【" + d.getCellNo()
								+ "】不在盘点单明细之内");
					}
					/* 验证商品编码并获取品牌编码 */
					/* 验证商品编码和尺码并获取条码 */
					getBrandAndBarcode(d);
					
					if (bcpdbMap.get(d.getBrandNo()) == null) {
						throw new ServiceException("商品编码【" + d.getItemNo()
								+ "】不属于该计划单的品牌");
					}
				}
				for (BillChCheckDtl d : updateList) {// 校验修改(用于PC端)
					/* 验证储位 */
					if (existCellMap.get(d.getCellNo()) == null) {
						throw new ServiceException("储位【" + d.getCellNo()
								+ "】不在盘点单明细之内");
					}
				}
			} else {// 商品盘
				BillChPlan billChPlan = new BillChPlan();
				billChPlan.setLocno(locno);
				billChPlan.setPlanNo(result.getPlanNo());
				billChPlan = billChPlanMapper.selectByPrimaryKey(billChPlan);

				Map<String, String> allCellMap = new HashMap<String, String>();// 按商品盘，允许的储位(所有)
				Map<String, Object> p = new HashMap<String, Object>();
				p.put("locno", locno);
				p.put("cellStatus", "0");
				List<CmDefcellSimple> cellList = cmDefcellMapper
						.selectSimple(p);
				for (CmDefcellSimple d : cellList) {
					String cellNo = d.getCellNo();
					if (allCellMap.get(cellNo) == null) {
						allCellMap.put(cellNo, d.getAreaUsetype());
					}
				}
				if (BillChPlanLimitBrandFlagEnums.limitBrandFlag_0.getStatus()
						.equals(billChPlan.getLimitBrandFlag())) {// 抽盘
					// 只允许添加盘点计划明细内的商品，而储位只要合法
					Map<String, String> existItemMap = new HashMap<String, String>();// 抽盘，允许的商品
					List<BillChCheckDtl> itemList = billChCheckDtlMapper
							.selectItemNobyCheck(checkNo, locno);
					for (BillChCheckDtl d : itemList) {
						String itemNo = d.getItemNo();
						if (existItemMap.get(itemNo) == null) {
							existItemMap.put(itemNo, itemNo);
						}
					}
					for (BillChCheckDtl d : insertList) {// 校验新增
						/* 验证储位 */
						if (allCellMap.get(d.getCellNo()) == null) {
							throw new ServiceException("储位【" + d.getCellNo()
									+ "】不存在");
						} else {
							if (CmDefAreaAreaUsetypeEnums.AREAUSETYPE_7
									.getAreaUsetype().equals(
											allCellMap.get(d.getCellNo()))) {
								throw new ServiceException("储位【"
										+ d.getCellNo() + "】属于库存调整区");
							}
						}
						/* 验证商品编码 */
						if (existItemMap.get(d.getItemNo()) == null) {
							throw new ServiceException("商品编码【" + d.getItemNo()
									+ "】不在盘点单明细之内");
						}
						/* 验证商品编码并获取品牌编码 */
						/* 验证商品编码和尺码并获取条码 */
						getBrandAndBarcode(d);
					}
					for (BillChCheckDtl d : updateList) {// 校验修改(用于PC端)
						/* 验证储位 */
						if (allCellMap.get(d.getCellNo()) == null) {
							throw new ServiceException("储位【" + d.getCellNo()
									+ "】不存在");
						} else {
							if (CmDefAreaAreaUsetypeEnums.AREAUSETYPE_7
									.getAreaUsetype().equals(
											allCellMap.get(d.getCellNo()))) {
								throw new ServiceException("储位【"
										+ d.getCellNo() + "】属于库存调整区");
							}
						}
					}
				} else {// 全盘
						// 只允许添加盘点计划品牌以内的商品，而储位只要合法
					for (BillChCheckDtl d : insertList) {// 校验新增
						/* 验证储位 */
						if (allCellMap.get(d.getCellNo()) == null) {
							throw new ServiceException("储位【" + d.getCellNo()
									+ "】不存在");
						} else {
							if (CmDefAreaAreaUsetypeEnums.AREAUSETYPE_7
									.getAreaUsetype().equals(
											allCellMap.get(d.getCellNo()))) {
								throw new ServiceException("储位【"
										+ d.getCellNo() + "】属于库存调整区");
							}
						}
						/* 验证商品编码并获取品牌编码 */
						/* 验证商品编码和尺码并获取条码 */
						getBrandAndBarcode(d);
						if (bcpdbMap.get(d.getBrandNo()) == null) {
							throw new ServiceException("商品编码【" + d.getItemNo()
									+ "】不属于该计划单的品牌");
						}
					}
					for (BillChCheckDtl d : updateList) {// 校验修改(用于PC端)
						/* 验证储位 */
						if (allCellMap.get(d.getCellNo()) == null) {
							throw new ServiceException("储位【" + d.getCellNo()
									+ "】不存在");
						} else {
							if (CmDefAreaAreaUsetypeEnums.AREAUSETYPE_7
									.getAreaUsetype().equals(
											allCellMap.get(d.getCellNo()))) {
								throw new ServiceException("储位【"
										+ d.getCellNo() + "】属于库存调整区");
							}
						}
					}
				}
			}
			
			//储位盘和商品盘公共部分
			List<BillChCheckDtl> addList = new ArrayList<BillChCheckDtl>();
			int addCount = 0;
			BillChCheck check = new BillChCheck();
			check.setLocno(locno);
			check.setCheckNo(checkNo);
			int rowId = billChCheckDtlMapper.selectMaxRowId(check);
			Map<String, CmDefcell> cmMap = new HashMap<String, CmDefcell>();
			for (BillChCheckDtl dtl : insertList) {
				if (dtl.getCheckDate() == null) {
					Map<String, Object> tem = new HashMap<String, Object>();
					tem.put("locno", locno);
					tem.put("checkNo", checkNo);
					List<BillChCheckDtl> checkList = billChCheckDtlMapper
							.selectByParams(null, tem);
					if (checkList != null) {
						BillChCheckDtl temCheck = checkList.get(0);
						curDate = temCheck.getCheckDate();
						checkWorker = temCheck.getCheckWorker();
						checkWorkerName = temCheck.getCheckWorkerName();
					}
				}
				dtl.setRowId(++rowId);
				dtl.setOwnerNo(result.getOwnerNo());
				dtl.setCheckNo(checkNo);
				dtl.setLocno(locno);
				dtl.setCheckWorker(checkWorker);
				dtl.setCheckWorkerName(checkWorkerName);
				dtl.setCheckDate(curDate);
				dtl.setStatus(BillChCheckStautsEnums.STATUS20.getStatus());
				dtl.setAddFlag(BillChCheckAddFlagEnums.FLAG1.getFlag());
				dtl.setCheckType(BillChCheckCheckTypeEnums.CHECKTYPE1
						.getCheckType());
				// dtl.setItemQty(new BigDecimal(0));
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
				dtl.setQuality(cell.getAreaQuality() == null ? "0" : cell
						.getAreaQuality());
				dtl.setItemType(cell.getItemType() == null ? "0" : cell
						.getItemType());
				dtl.setPackQty(new BigDecimal(1));
				dtl.setLabelNo("N");
				dtl.setDifferentFlag("1");
				if (dtl.getItemQty() == null) {
					dtl.setItemQty(new BigDecimal(0));
				}
				addList.add(dtl);
				addCount++;
				if (addCount >= MAXCOUNT) {
					this.billChCheckDtlMapper.batchInsertDtl(addList);
					addList.clear();
					addCount = 0;
				}
			}
			List<BillChCheckDtl> updateList_ = new ArrayList<BillChCheckDtl>();
			int updateCount = 0;
			Date checkDate = new Date();
			for (BillChCheckDtl dtl : updateList) {
				dtl.setCheckDate(checkDate);//盘点时间
				updateList_.add(dtl);
				updateCount++;
				if (updateCount >= MAXCOUNT) {
					this.billChCheckDtlMapper.batchUpdateDtl(updateList_);
					updateList_.clear();
					updateCount = 0;
				}
			}
			// 批量插入
			if (addList.size() > 0) {
				this.billChCheckDtlMapper.batchInsertDtl(addList);
			}
			if (updateList.size() > 0) {
				this.billChCheckDtlMapper.batchUpdateDtl(updateList_);
			}
			List<BillChCheckDtl> repeatList = this.billChCheckDtlMapper
					.selectRepeat(check);
			if (repeatList != null && repeatList.size() > 0) {
				StringBuilder str = new StringBuilder();
				for (BillChCheckDtl dtl : repeatList) {
					str.append("储位:").append(dtl.getCellNo())
							.append("<br>商品编码:").append(dtl.getItemNo())
							.append("<br>尺码:").append(dtl.getSizeNo())
							.append("<br>");
				}
				str.append("<div style='text-align:center'>&nbsp;&nbsp;&nbsp;&nbsp;重复!</div>");
				throw new ServiceException(str.toString());
			}
			resultVo.setSuccess(true);
			return resultVo;
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDtl> selectItem4ChCheck(SimplePage page,
			BillChCheckDtl check, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectItem4ChCheck(page, check,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	@Override
	public int selectItem4ChCheckCount(BillChCheckDtl check,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectItem4ChCheckCount(check,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}
	
	@Override
	public int selectCountSingFlag(Map<String, Object> map) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectCountSingFlag(map);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDtl> findItemByPlan(SimplePage page,
			BillChCheckDtl check, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectItemByPlan(page, check,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	@Override
	public int findItemByPlanCount(BillChCheckDtl check,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectItemByPlanCount(check,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int selectReChCheckCount(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectReChCheckCount(map,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDto> selectReChCheck(Map<String, Object> map,
			SimplePage page, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectReChCheck(map, page,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int findCountByPlanNo(Map<String, Object> params)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectCountByPlanNo(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillChCheckDtl> findByPageByPlanNo(SimplePage page,
			Map<String, Object> params) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectByPageByPlanNo(page, params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int selectBrandLimitItemCount(Map<String, Object> params)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.selectBrandLimitItemCount(params);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	public List<BillChCheckDtl> selectBrandLimitItem(SimplePage page,
			Map<String, Object> params) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectBrandLimitItem(page, params);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveByPlan(BillChCheck check) throws ServiceException {
		try {
			// 判断主单的状态
			BillChCheck resultCheck = billChCheckMapper
					.selectByPrimaryKey(check);
			if (resultCheck == null) {
				throw new ServiceException("盘点单不存在");
			}
			if (!resultCheck.getStatus().equals(
					BillChCheckStautsEnums.STATUS20.getStatus())) {
				throw new ServiceException("盘点单非发单状态，不能按照计划保存");
			}
			check.setCheckDate(new Date());
			int count = this.billChCheckDtlMapper.saveByPlan(check);
			if (count == 0) {
				throw new ServiceException("明细已经盘点,不能按计划保存");
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void resetPlan(BillChCheck check) throws ServiceException {
		try {
			// 判断主单的状态
			BillChCheck resultCheck = billChCheckMapper
					.selectByPrimaryKey(check);
			if (resultCheck == null) {
				throw new ServiceException("盘点单不存在");
			}
			if (!resultCheck.getStatus().equals(
					BillChCheckStautsEnums.STATUS20.getStatus())) {
				throw new ServiceException("盘点单非发单状态，不能实盘置零");
			}
			check.setCheckDate(new Date());
			int count = billChCheckDtlMapper.resetPlan(check);
			if (count == 0) {
				throw new ServiceException("实盘数都为零，无需实盘置零");
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findByPage4CellCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectByPage4CellCount(params,
					authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillChCheckDtl> findByPage4Cell(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectByPage4Cell(page, orderByField,
					orderBy, params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}

	public List<Map<String, Object>> printBatch(String keyStr)
			throws ServiceException {
		if (StringUtils.isEmpty(keyStr)) {
			throw new ServiceException("参数错误");
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			String[] kyes = keyStr.split(",");
			BillChCheck check = null;
			Map<String, Object> map = null;
			for (String key : kyes) {
				String[] subKeys = key.split("\\|");
				check = new BillChCheck();
				check.setLocno(subKeys[0]);
				check.setCheckNo(subKeys[1]);
				check.setOwnerNo(subKeys[2]);
				map = new HashMap<String, Object>();
				List<BillChCheckDtl> list = this.billChCheckDtlMapper
						.selectAllDetail4Print(check);
				map.put("list", list);
				int cellCount = this.billChCheckDtlMapper
						.selectCellNoCount(check);
				map.put("cellCount", cellCount);
				BillChCheck resultCheck = this.billChCheckMapper
						.selectByPrimaryKey(check);
				map.put("main", resultCheck);
				result.add(map);
			}
		} catch (DaoException e) {
			throw new ServiceException();
		}
		return result;
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty4Cell(
			Map<String, Object> map, AuthorityParams authorityParams) {
		return billChCheckDtlMapper.selectSumQty4Cell(map, authorityParams);
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) {
		return billChCheckDtlMapper.selectSumQty(map, authorityParams);
	}

	@Override
	public int modityCellCheckStatusByCheckDtl(Map<String, Object> params)
			throws ServiceException {
		try {
			return billChCheckDtlMapper.updateCellCheckStatusByCheckDtl(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillChCheckDtl> findDtlSysNo(BillChCheckDtl billChCheckDtl,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billChCheckDtlMapper.selectDtlSysNo(billChCheckDtl, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> findDtlSysNoByPage(
			BillChCheckDtl billChCheckDtl, AuthorityParams authorityParams)
			throws ServiceException {
		Map<String,Object> obj=new HashMap<String,Object>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		footerMap.put("itemNo", "小计");
		try {
			Map<String, Map<String, SizeInfoMapDto>> map = null;
			if(StringUtils.isNotBlank(billChCheckDtl.getSysNo())){
				map = getSizeInfoMapList(billChCheckDtl.getSysNo());
			}
			List<BillChCheckDtl> listCC = null;
			int total = billChCheckDtlMapper.selectSysNoContentCount(billChCheckDtl, authorityParams);
			if(total > 0) {
				SimplePage page = new SimplePage(1, total, total);
				billChCheckDtl.setPageNo(1);
				billChCheckDtl.setPageSize(total);
				/*不含尺码的数据*/
				listCC = billChCheckDtlMapper.selectSysNoContentByPage(page, null, null, billChCheckDtl, authorityParams);
				if (CollectionUtils.isEmpty(listCC)) {
					obj.put("total", 0);
					obj.put("rows", new ArrayList<Object>());
					return obj;
				}
				BigDecimal total4Footer = new BigDecimal(0);
				for (BillChCheckDtl c : listCC) {
					total4Footer = total4Footer.add(c.getTotalCheckQty());
					/*获取尺码及数量(已分组)*/
					String type = "";
					if(c.getCheckType().equals("1")){
						type = "nvl(sum(bck.CHECK_QTY), 0) ";
					} else {
						type = "nvl(sum(bck.recheck_qty), 0)";
					}
					c.setCheckTypeQty(type);
					List<BillChCheckDtl> listDtos=billChCheckDtlMapper.selectSysNoByPage(c, authorityParams);
					if (CollectionUtils.isEmpty(listDtos)){
						continue;
					}
					Map<String, SizeInfoMapDto> mapS = null;
					if (map!=null) {
						mapS = map.get(c.getSizeKind());
					}
					for(BillChCheckDtl d : listDtos){
						SizeInfoMapDto sizeInfoMapDTO = null;
						if (mapS!=null) {
							sizeInfoMapDTO = mapS.get(d.getSizeNo());
						}
						boolean sizeInfoFlag = true;
						if(sizeInfoMapDTO==null||sizeInfoMapDTO.getSizeInfo()==null||d.getSizeNo()==null){
							sizeInfoFlag = false;
						}
						if(sizeInfoFlag){
							String filedName="setV"+(sizeInfoMapDTO.getI()+1);
							Object[] arg=new Object[]{String.valueOf(d.getTotalCheckQty())};
							CommonUtil.invokeMethod(c,filedName,arg);
							this.setFooterMap("v" + (sizeInfoMapDTO.getI()+1), d.getTotalCheckQty(), footerMap);
						}
					}
				}
				this.setFooterMap("total", total4Footer, footerMap);
			}
			obj.put("total", total);
			obj.put("rows", listCC);
			return obj;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
    private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
    	BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}

	/*获取尺码信息表MAP*/
	private Map<String, Map<String, SizeInfoMapDto>> getSizeInfoMapList(
			String SysNo) {
		try {
			Map<String, Map<String, SizeInfoMapDto>> map = new HashMap<String, Map<String, SizeInfoMapDto>>();
			Map<String, Object> mapParaMap = new HashMap<String, Object>();
			mapParaMap.put("sysNo", SysNo);
			List<SizeInfo> sizeInfoList = this.sizeInfoService.findByBiz(null, mapParaMap);
			Map<String, Integer> indexMap = new HashMap<String, Integer>();
			for (SizeInfo sizeInfo : sizeInfoList) {
				String sizeKind = sizeInfo.getSizeKind();
				Map<String, SizeInfoMapDto> sizeInfoMap = map.get(sizeKind);
				if (sizeInfoMap == null) {
					sizeInfoMap = new HashMap<String, SizeInfoMapDto>();
					map.put(sizeKind, sizeInfoMap);
				}
				Integer index = indexMap.get(sizeKind);
				if (index == null) {
					index = 0;
					indexMap.put(sizeKind, index);
				} else {
					index++;
					indexMap.put(sizeKind, index);
				}
				SizeInfoMapDto sizeInfoMapDTO = new SizeInfoMapDto();
				sizeInfoMapDTO.setI(index);
				sizeInfoMapDTO.setSizeInfo(sizeInfo);
				String sizeNo = sizeInfo.getSizeNo();
				sizeInfoMap.put(sizeNo, sizeInfoMapDTO);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}