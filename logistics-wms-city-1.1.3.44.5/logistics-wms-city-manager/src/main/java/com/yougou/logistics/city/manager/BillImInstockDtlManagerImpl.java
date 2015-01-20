package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto2;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillImInstockDtlService;
import com.yougou.logistics.city.service.BillImInstockService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Sep 30 16:23:58 CST 2013
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
@Service("billImInstockDtlManager")
class BillImInstockDtlManagerImpl extends BaseCrudManagerImpl implements BillImInstockDtlManager {
	@Resource
	private BillImInstockDtlService billImInstockDtlService;

	@Resource
	private BillImInstockService billImInstockService;

	@Override
	public BaseCrudService init() {
		return billImInstockDtlService;
	}

	@Override
	public int findItemCountBroupByItemNo(BillImInstockDtl dtl) throws ManagerException {
		try {
			return billImInstockDtlService.findItemCountBroupByItemNo(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImInstockDtl> findItemGroupByItemNo(SimplePage page, BillImInstockDtl dtl) throws ManagerException {
		try {
			return billImInstockDtlService.findItemGroupByItemNo(page, dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImInstockDtlDto2> findDetailByParams(BillImInstockDtl dtl) throws ManagerException {
		try {
			return billImInstockDtlService.findDetailByParams(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public String findSysNo(BillImInstockDtlDto dto) throws ManagerException {
		try {
			return billImInstockDtlService.findSysNo(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	public BillImInstockDtlDto splitById(BillImInstockDtlDto dtl) throws ManagerException {
		try {
			return billImInstockDtlService.splitById(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	public void saveSingle(BillImInstockDtl dtl) throws ManagerException {
		try {
			billImInstockDtlService.saveSingle(dtl);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	@Override
	public void deleteSingle(BillImInstockDtlDto dto) throws ManagerException {
		try {
			billImInstockDtlService.deleteSingle(dto);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	public void saveBatch(List<BillImInstockDtlDto> list) throws ManagerException {
		try {
			billImInstockDtlService.saveBatch(list);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<String> printDetail(String locno, String keys) throws ManagerException {
		List<String> htmlList = new ArrayList<String>();
		try {

			String[] mainArray = keys.split("_");
			if (mainArray != null && mainArray.length > 0) {

				for (String main : mainArray) {

					String[] array = main.split("\\|");
					/*	boolean authority = billAuthorityService.isHasFullBillAuthority(TableContants.BILL_IM_INSTOCK_DTL,
								array[0], authorityParams);
						if (!authority) {
							throw new ManagerException("【" + array[0] + "】用户品牌权限不足，打印失败！");
						}*/
					BillImInstock instock = new BillImInstock();
					instock.setLocno(locno);
					instock.setInstockNo(array[0]);
					instock.setOwnerNo(array[1]);
					BillImInstock instockResult = billImInstockService.findById(instock);

					List<BillImInstockDtlDto> list = billImInstockDtlService.selectPrintInf(instock);
					Date date = instockResult.getInstockDate();
					String datestr = "";
					if (date != null) {
						datestr = new SimpleDateFormat("yyyy-MM-dd").format(date);
					}
					htmlList.add(getHtml(list, instockResult.getInstockNo(), instockResult.getInstockWorker(), datestr));
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		return htmlList;
	}

	public String getHtml(List<BillImInstockDtlDto> list, String instockNo, String instockWorker, String instockDate) {
		int index = 1;
		BigDecimal total = new BigDecimal(0);
		BigDecimal totalR = new BigDecimal(0);
		StringBuffer sb = new StringBuffer();
		StringBuffer tab = new StringBuffer();
		tab.append("<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>");
		tab.append("<tr bgcolor='#fff'>" + "<td>序号</td>" + "<td>商品编号</td>" + "<td>颜色</td>" + "<td>尺码</td>"
				+ "<td>预上储位</td>" + "<td>预上数量</td></tr>");
		for (BillImInstockDtlDto dtl : list) {
			tab.append("<tr bgcolor='#fff'>" + "<td>" + index + "</td>" + "<td>" + dtl.getItemNo() + "</td>" + "<td>"
					+ (dtl.getColorName() == null ? "" : dtl.getColorName()) + "</td>" + "<td>" + dtl.getSizeNo()
					+ "</td>" + "<td>" + dtl.getDestCellNo() + "</td>" + "<td>" + dtl.getItemQty() + "</td>" + "</tr>");
			if (dtl.getRealQty() == null) {
				dtl.setRealQty(new BigDecimal(0));
			}
			total = total.add(dtl.getItemQty());
			totalR = totalR.add(dtl.getRealQty());
			index++;
		}
		tab.append("<tr bgcolor='#fff'>" + "<td></td>" + "<td>汇总</td>" + "<td></td><td></td><td></td><td>" + total
				+ "</td>" + "</tr>");
		tab.append("</table>");
		sb.append("<table style='width:100%;'>");
		sb.append("<tr><td colspan='3' style='text-align:center;font-size:30px;'>上架单明细</td></tr>");
		sb.append("<tr><td style='width:33%;'>上架单：" + instockNo + "</td>");
		sb.append("<td style='width:33%;'>上架时间：" + instockDate + "</td>");
		sb.append("<td style='width:33%;'>上架人：" + instockWorker + "</td></tr>");
		sb.append("</table>");
		sb.append(tab.toString());
		return sb.toString();
	}

	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return this.billImInstockDtlService.selectSumQty(map, authorityParams);
	}

	@Override
	public void saveByPlan(BillImInstock instock) throws ManagerException {
		try {
			this.billImInstockDtlService.saveByPlan(instock);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}

}