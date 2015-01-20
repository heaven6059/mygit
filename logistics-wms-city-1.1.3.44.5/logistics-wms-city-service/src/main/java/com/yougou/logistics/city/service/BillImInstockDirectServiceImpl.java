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
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDirect;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillImInstockDirectMapper;
import com.yougou.logistics.city.dal.mapper.BillImInstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImInstockMapper;
import com.yougou.logistics.city.dal.mapper.BillImReceiptDtlMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:56:15 CST 2013
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
@Service("billImInstockDirectService")
class BillImInstockDirectServiceImpl extends BaseCrudServiceImpl implements BillImInstockDirectService {
	@Resource
	private BillImInstockDirectMapper billImInstockDirectMapper;

	@Resource
	private BillImReceiptDtlMapper billImReceiptDtlMapper;

	@Resource
	private ProcCommonService procCommonService;

	// 上架单主表
	@Resource
	private BillImInstockMapper billImInstockMapper;

	// 上架单从表
	@Resource
	private BillImInstockDtlMapper billImInstockDtlMapper;

	private final static String AUTOLOCATEFLAG0 = "0";
	private final static String STATUS10 = "10";
	private final static String OPERATE_TYPEB = "B";
	private final static String LOCATE_TYPE1 = "1";
	private final static String CONTAINER_LOCATE_FLAG1 = "1";
	private final static String INSTOCK_TYPE0 = "0";
	private final static String MID_LABEL_NON = "N", LABEL_NON = "N";
	private final static String RESULTY = "Y";

	@Override
	public BaseCrudMapper init() {
		return billImInstockDirectMapper;
	}

	// 10-未处理；13-已发单16-取消
	private static final String STAUTS_16 = "16";
	private static final String STAUTS_13 = "13";

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void cancelDirect(String locNo, String receiptNo, String ownerNo, String rowIdStr) throws ServiceException {
		if (StringUtils.isNotBlank(rowIdStr)) {
			try {
				String[] strs = rowIdStr.split(",");
				for (String obj : strs) {

					String[] substr = obj.split("\\|");
					// 更新定位信息状态
					updateDirectStatus(locNo, substr);
					// 更新收货单详情定位数量
					updateReceiptDtl(locNo, receiptNo, ownerNo, substr);

				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		} else {
			throw new ServiceException();
		}
	}

	/**
	 * 更新定位信息
	 * 
	 * @param locNo
	 * @param rowIdAndItemNo
	 */
	private void updateDirectStatus(String locNo, String[] rowIdAndItemNo) {
		BillImInstockDirect direct = new BillImInstockDirect();
		direct.setLocno(locNo);
		direct.setRowId(Long.parseLong(rowIdAndItemNo[0]));
		BillImInstockDirect cruDirect = billImInstockDirectMapper.selectByPrimaryKey(direct);
		cruDirect.setStatus(STAUTS_16);
		billImInstockDirectMapper.updateByPrimaryKey(cruDirect);
	}

	/**
	 * 更新收货单详情已定位数量
	 */
	private void updateReceiptDtl(String locNo, String receiptNo, String ownerNo, String[] rowIdAndItemNo) {
		BillImInstockDirect direct = new BillImInstockDirect();
		direct.setLocno(locNo);
		direct.setRowId(Long.parseLong(rowIdAndItemNo[0]));

		BillImInstockDirect cureDirect = billImInstockDirectMapper.selectByPrimaryKey(direct);
		// 取得预定位数量
		int instockQty = cureDirect.getInstockQty().intValue();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("locno", locNo);
		map.put("receiptNo", receiptNo);
		map.put("ownerNo", ownerNo);
		map.put("itemNo", rowIdAndItemNo[1]);
		List<BillImReceiptDtl> dtls = billImReceiptDtlMapper.selectByParams(null, map);
		for (BillImReceiptDtl redtl : dtls) {
			int checkQty = redtl.getCheckQty().intValue();
			// 如果当前的预定数量为0，直接跳过。
			if (checkQty == 0) {
				continue;
			}
			instockQty = instockQty - checkQty;
			// 如果预定位数小于商品验收数量，那么减去相应的数量，结束循环，否则一直循环凑够预定数。
			if (instockQty <= 0) {
				redtl.setCheckQty(new BigDecimal(Math.abs(instockQty)));
				billImReceiptDtlMapper.updateByPrimaryKey(redtl);
				break;
			} else {
				redtl.setCheckQty(new BigDecimal(0));
				billImReceiptDtlMapper.updateByPrimaryKey(redtl);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void createInstock(String curUser, String locNo, String rowStrs, String instockWorker)
			throws ServiceException {
		if (StringUtils.isNotBlank(rowStrs)) {
			try {
				String[] strs = rowStrs.split(",");
				BillImInstockDirect direct = null;
				String instockNo = null;
				for (int i = 0, length = strs.length; i < length; i++) {
					direct = new BillImInstockDirect();
					direct.setLocno(locNo);
					direct.setRowId(Long.parseLong(strs[i]));
					BillImInstockDirect result = billImInstockDirectMapper.selectDetail(direct);
					if (null == result) {
						continue;
					}
					/*
					 * if (sysNoMap.get(result.getSysNo()) == null) {//查看品牌库是否存在
					 * 保存主表信息 instockNo = saveInstock(result, curUser,
					 * instockWorker); sysNoMap.put(result.getSysNo(),
					 * instockNo); }
					 */
					if (i == 0) {
						instockNo = saveInstock(result, curUser, instockWorker);
					}
					// 保存详情表信息
					saveInstockDetail(String.valueOf(i + 1), instockNo, result);
					// 更新定位表信息状态
					result.setStatus(STAUTS_13);
					billImInstockDirectMapper.updateByPrimaryKeySelective(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException(e.getMessage());
			}
		} else {
			throw new ServiceException();
		}
	}

	public String saveInstock(BillImInstockDirect result, String curUser, String instockWorker) throws ServiceException {
		try {
			String instockNo = procCommonService.procGetSheetNo(result.getLocno(), CNumPre.IM_INSTOCK_PRE);
			// 上架单主表
			BillImInstock instock = new BillImInstock();
			instock.setLocno(result.getLocno());
			instock.setInstockNo(instockNo);
			instock.setAutoLocateFlag(AUTOLOCATEFLAG0);
			instock.setStatus(STATUS10);
			instock.setDispatchWorker(curUser);
			instock.setDispatchDate(new Date());
			instock.setInstockWorker(instockWorker);
			instock.setOperateType(OPERATE_TYPEB);
			instock.setLocateType(result.getLocateType());// 定位类型
			instock.setOwnerNo(result.getOwnerNo());
			Date curDate = new Date();
			instock.setInstockDate(curDate);
			instock.setCreator(curUser);
			instock.setCreatetm(curDate);
			instock.setContainerLocateFlag(CONTAINER_LOCATE_FLAG1);
			billImInstockMapper.insertSelective(instock);
			return instockNo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public void saveInstockDetail(String num, String instockNo, BillImInstockDirect result) throws ServiceException {
		try {
			// 上架单从表
			BillImInstockDtl dtl = new BillImInstockDtl();
			dtl.setInstockId(num);
			dtl.setLocno(result.getLocno());
			dtl.setOwnerNo(result.getOwnerNo());
			dtl.setInstockNo(instockNo);
			dtl.setDirectSerial(result.getRowId());
			dtl.setInstockType(INSTOCK_TYPE0);
			dtl.setItemNo(result.getItemNo());
			dtl.setItemId(result.getItemId());
			dtl.setSizeNo(result.getSizeNo());
			if (StringUtils.isEmpty(result.getItemNo()) || StringUtils.isEmpty(result.getSizeNo())) {
				throw new Exception("商品编码，尺码不能为空！");
			}
			dtl.setPackQty(result.getPackQty());
			// 如果是验收定位，则需要设置来源储位
			if ("1".equals(result.getLocateType())) {
				if (result.getCellId() == null || StringUtils.isEmpty(result.getCellNo())) {
					throw new Exception("验收定位：来源储位，来源储位ID不能为空！");
				}
				dtl.setCellId(result.getCellId());
				dtl.setCellNo(result.getCellNo());
			}
			if (result.getDestCellId() == null || StringUtils.isEmpty(result.getDestCellNo())) {
				throw new Exception("预上储位，预上储位ID不能为空！");
			}
			dtl.setDestCellId(result.getDestCellId());
			dtl.setDestCellNo(result.getDestCellNo());
			dtl.setItemQty(result.getInstockQty());
			dtl.setContainerNo(result.getContainerNo());// 容器号填充N
			dtl.setSourceNo(result.getSourceNo());
			dtl.setStatus(STATUS10);
			dtl.setMidLabelNo(MID_LABEL_NON);
			dtl.setLabelNo(result.getLabelNo());
			billImInstockDtlMapper.insertSelective(dtl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int countInstockDirectByMainId(BillImInstockDirect objEntiy, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImInstockDirectMapper.countInstockDirectByMainId(objEntiy, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImInstockDirect> findInstockDirectByMainIdPage(SimplePage page, BillImInstockDirect objEntiy,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImInstockDirectMapper.findInstockDirectByMainIdPage(page, objEntiy, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImInstockDirectMapper.selectSumQty(map, authorityParams);
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty4CheckDirect(BillImInstockDirect map, AuthorityParams authorityParams) {
		return billImInstockDirectMapper.selectSumQty4CheckDirect(map, authorityParams);
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int countInstockDirectByType(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImInstockDirectMapper.countInstockDirectByType(map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImInstockDirect> findInstockDirectByTypePage(SimplePage page, Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImInstockDirectMapper.findInstockDirectByTypePage(page, map, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectInstockDirectByTypePage4Sum(Map<String, Object> map,
			AuthorityParams authorityParams) {
		return billImInstockDirectMapper.selectInstockDirectByTypePage4Sum(map, authorityParams);
	}
}