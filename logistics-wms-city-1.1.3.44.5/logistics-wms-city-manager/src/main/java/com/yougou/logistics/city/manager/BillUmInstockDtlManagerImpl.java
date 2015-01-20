package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.CmDefAreaAreaAttributeEnums;
import com.yougou.logistics.city.common.enums.CmDefAreaAreaUsetypeEnums;
import com.yougou.logistics.city.common.enums.CmDefAreaAttributeTypeEnums;
import com.yougou.logistics.city.common.enums.CmDefCellMixFlagEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCellStatusEnums;
import com.yougou.logistics.city.common.enums.CmDefcellCheckStatusEnums;
import com.yougou.logistics.city.common.model.BillUmInstockDtl;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillUmInstockDtlService;
import com.yougou.logistics.city.service.CmDefareaService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ConContentService;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Oct 14 19:59:56 CST 2013
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
@Service("billUmInstockDtlManager")
class BillUmInstockDtlManagerImpl extends BaseCrudManagerImpl implements BillUmInstockDtlManager {
	@Resource
	private BillUmInstockDtlService billUmInstockDtlService;

	@Resource
	private CmDefcellService cmDefcellService;

	@Resource
	private CmDefareaService cmDefareaService;
	
	@Resource
	private ConContentService conContentService;
	
	@Override
	public BaseCrudService init() {
		return billUmInstockDtlService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String saveDtl(String locno, String instockNo, String ownerNo, String instockWorker, String instockDtlStr)
			throws ManagerException {
		try {
			int a = 0;
			Map<String, Object> params = new HashMap<String, Object>();
			BillUmInstockDtl billUmInstockDtl = new BillUmInstockDtl();
			billUmInstockDtl.setLocno(locno);
			billUmInstockDtl.setInstockNo(instockNo);
			billUmInstockDtl.setOwnerNo(ownerNo);
			params.put("locno", locno);
			// 1更新
			String[] dtlStrArray = null;
			String[] dtlArray = null;
			if (!StringUtils.isBlank(instockDtlStr)) {
				dtlStrArray = instockDtlStr.split("\\|");
			}
			if (dtlStrArray != null && dtlStrArray.length > 0) {
				for (String dtlStr : dtlStrArray) {
					if (!StringUtils.isBlank(dtlStr)) {
						dtlArray = dtlStr.split("_");
					}
					// instockId_realCellNo_realQty
					if (dtlArray != null) {
						if (dtlArray.length == 3) {
							params.put("cellNo", dtlArray[1]);
							a = cmDefcellService.findCount(params);
							if (a < 1) {
								throw new ManagerException("储位【" + dtlArray[1] + "】无效");
							}
							billUmInstockDtl.setInstockId(Long.parseLong(dtlArray[0]));
							billUmInstockDtl.setRealCellNo(dtlArray[1]);
							billUmInstockDtl.setRealQty(new BigDecimal(dtlArray[2]));
							a = billUmInstockDtlService.modifyById(billUmInstockDtl);
							if (a < 1) {
								throw new ManagerException("更新明细异常");
							}
						} else {
							throw new ManagerException("明细数据非法");
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public BillUmInstockDtl splitDtl(BillUmInstockDtl billUmInstockDtl, BigDecimal realQtyIn, BigDecimal newRealQty)
			throws ManagerException {
		try {
			int a = 0;
			// 1查询需要拆分的明细
			billUmInstockDtl = billUmInstockDtlService.findById(billUmInstockDtl);
			if (billUmInstockDtl == null) {
				throw new ManagerException("没有需要拆分的明细");
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", billUmInstockDtl.getLocno());
			params.put("instockNo", billUmInstockDtl.getInstockNo());
			params.put("ownerNo", billUmInstockDtl.getOwnerNo());
			long nowInstockId = billUmInstockDtl.getInstockId();
			long maxInstockId = billUmInstockDtlService.findMaxInstockId(params);
			maxInstockId++;
			billUmInstockDtl.setInstockId(maxInstockId);
			/*
			 * if(billUmInstockDtl.getRealQty() != null){
			 * billUmInstockDtl.setRealQty
			 * (billUmInstockDtl.getRealQty().subtract(realQtyIn)); }else{
			 * billUmInstockDtl
			 * .setRealQty(billUmInstockDtl.getItemQty().subtract(realQtyIn)); }
			 */

			params = new HashMap<String, Object>();
			params.put("locno", billUmInstockDtl.getLocno());
			params.put("instockNo", billUmInstockDtl.getInstockNo());
			params.put("ownerNo", billUmInstockDtl.getOwnerNo());
			params.put("itemNo", billUmInstockDtl.getItemNo());
			params.put("sizeNo", billUmInstockDtl.getSizeNo());
			params.put("boxNo", billUmInstockDtl.getBoxNo());
			params.put("sourceNo", billUmInstockDtl.getSourceNo());
			params.put("destCellNo", billUmInstockDtl.getDestCellNo());
			params.put("destCellId", billUmInstockDtl.getDestCellId());
			List<BillUmInstockDtl> list = billUmInstockDtlService.findByBiz(null, params);
			BigDecimal totalRealQty = new BigDecimal(0);
			if (list != null && list.size() > 0) {
				BillUmInstockDtl parentDtl = null;
				for (BillUmInstockDtl dtl : list) {
					if (dtl.getItemQty() != null && dtl.getItemQty().intValue() > 0) {
						parentDtl = dtl;
						totalRealQty = totalRealQty.add(realQtyIn);
					} else {
						totalRealQty = totalRealQty
								.add(dtl.getRealQty() == null ? new BigDecimal(0) : dtl.getRealQty());
					}
				}
				billUmInstockDtl.setRealQty(parentDtl.getItemQty().subtract(totalRealQty));
				if (billUmInstockDtl.getRealQty().intValue() <= 0) {
					throw new ManagerException("实际上架数量之和必须小于计划数量");
				}
			}
			billUmInstockDtl.setItemQty(new BigDecimal(0));

			// 2插入新生成的明细
			a = billUmInstockDtlService.add(billUmInstockDtl);
			if (a < 1) {
				throw new ManagerException("插入新明细异常");
			}
			// 3更新需要拆分明细的实际数量
			BillUmInstockDtl dtl = new BillUmInstockDtl();
			dtl.setLocno(billUmInstockDtl.getLocno());
			dtl.setInstockNo(billUmInstockDtl.getInstockNo());
			dtl.setOwnerNo(billUmInstockDtl.getOwnerNo());
			dtl.setInstockId(nowInstockId);
			dtl.setRealQty(realQtyIn);
			a = billUmInstockDtlService.modifyById(dtl);
			if (a < 1) {
				throw new ManagerException("更新实际上架数量异常");
			}
			return billUmInstockDtl;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public BigDecimal deleteDtl(BillUmInstockDtl billUmInstockDtl) throws ManagerException {
		try {
			int a = 0;
			// 2将上架数量加到父明细上
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", billUmInstockDtl.getLocno());
			params.put("instockNo", billUmInstockDtl.getInstockNo());
			params.put("ownerNo", billUmInstockDtl.getOwnerNo());
			params.put("itemNo", billUmInstockDtl.getItemNo());
			params.put("sizeNo", billUmInstockDtl.getSizeNo());
			params.put("boxNo", billUmInstockDtl.getBoxNo());
			params.put("sourceNo", billUmInstockDtl.getSourceNo());
			params.put("destCellNo", billUmInstockDtl.getDestCellNo());
			params.put("destCellId", billUmInstockDtl.getDestCellId());
			List<BillUmInstockDtl> list = billUmInstockDtlService.findByBiz(null, params);
			if (list != null && list.size() > 0) {
				BillUmInstockDtl parentDtl = null;
				for (BillUmInstockDtl dtl : list) {
					if (dtl.getItemQty() != null && dtl.getItemQty().intValue() > 0) {
						parentDtl = dtl;
						break;
					}
				}
				if (parentDtl == null) {
					throw new ManagerException("没有找到被删除明细的来源明细");
				} else {
					billUmInstockDtl = billUmInstockDtlService.findById(billUmInstockDtl);
					// 删除指定明细
					a = billUmInstockDtlService.deleteById(billUmInstockDtl);
					if (a < 1) {
						throw new ManagerException("删除明细异常");
					}
					/*
					 * parentDtl.setRealQty(parentDtl.getRealQty().add(
					 * billUmInstockDtl.getRealQty())); a =
					 * billUmInstockDtlService.modifyById(parentDtl); if(a < 1){
					 * throw new ManagerException("更新明细异常"); }
					 */
					return parentDtl.getRealQty();
				}
			} else {

			}

			return null;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<String> printDetail(String locno, String keys) throws ManagerException {
		List<String> htmlList = new ArrayList<String>();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String[] mainArray = keys.split("_");
			List<BillUmInstockDtl> list;
			if (mainArray != null && mainArray.length > 0) {
				params.put("locno", locno);
				for (String main : mainArray) {
					String[] array = main.split("\\|");
					params.put("instockNo", array[0]);
					int total = this.billUmInstockDtlService.findCount(params);
					SimplePage page = new SimplePage(0, total, total);
					list = billUmInstockDtlService.findByPage(page, "d.item_no,d.size_no,d.dest_cell_no", null, params);
					if (list != null && list.size() > 0)
						htmlList.add(getHtml(list, array[0], array[1], array[2]));
				}
			}
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
		return htmlList;
	}

	public String getHtml(List<BillUmInstockDtl> list, String instockNo, String instockWorker, String instockDate) {
		int index = 1;
		BigDecimal total = new BigDecimal(0);
		StringBuffer sb = new StringBuffer();
		StringBuffer tab = new StringBuffer();
		tab.append("<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>");
		tab.append("<tr bgcolor='#fff'>" + "<td>序号</td>" + "<td>商品编号</td>" + "<td>颜色</td>" + "<td>尺码</td>"
				+ "<td>预上储位</td>" + "<td>预上数量</td>" + "<td>箱号</td>" + "</tr>");
		list = groupBy(list);
		for (BillUmInstockDtl dtl : list) {
			tab.append("<tr bgcolor='#fff'>" + "<td>" + index + "</td>" + "<td>" + dtl.getItemNo() + "</td>" + "<td>"
					+ dtl.getColorName() + "</td>" + "<td>" + dtl.getSizeNo() + "</td>" + "<td>" + dtl.getDestCellNo()
					+ "</td>" + "<td>" + dtl.getItemQty() + "</td>" + "<td>" + dtl.getBoxNo() + "</td>" + "</tr>");
			if (dtl.getRealQty() == null) {
				dtl.setRealQty(new BigDecimal(0));
			}
			total = total.add(dtl.getItemQty());
			index++;
		}
		tab.append("<tr bgcolor='#fff'>" + "<td></td>" + "<td>汇总</td>" + "<td></td>" + "<td></td>" + "<td></td>"
				+ "<td>" + total + "</td>" + "<td></td>" + "</tr>");
		tab.append("</table>");
		sb.append("<table style='width:100%;'>");
		sb.append("<tr><td colspan='2' style='text-align:center;font-size:30px;'>退仓上架单明细</td></tr>");
		sb.append("<tr><td>上架单：" + instockNo + "</td>");
		sb.append("<td style='text-align:right;'>上架人：" + instockWorker + "&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>");
		sb.append("</table>");
		sb.append(tab.toString());
		return sb.toString();
	}

	public List<BillUmInstockDtl> groupBy(List<BillUmInstockDtl> list) {
		List<BillUmInstockDtl> newList = new ArrayList<BillUmInstockDtl>();
		Map<String, BillUmInstockDtl> map = new HashMap<String, BillUmInstockDtl>();
		BillUmInstockDtl temp = new BillUmInstockDtl();
		String key = "";
		for (BillUmInstockDtl dtl : list) {
			key = dtl.getItemNo() + dtl.getSizeNo() + dtl.getDestCellNo() + dtl.getBoxNo();
			temp = map.get(key);
			if (temp != null) {
				if (temp.getItemQty() == null) {
					temp.setItemQty(new BigDecimal(0));
				}
				temp.setItemQty(temp.getItemQty().add(dtl.getItemQty() == null ? new BigDecimal(0) : dtl.getItemQty()));
			} else {
				map.put(key, dtl);
			}
		}
		for (Entry<String, BillUmInstockDtl> m : map.entrySet()) {
			newList.add(m.getValue());
		}
		return newList;
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams) {
		return this.billUmInstockDtlService.selectSumQty(params, authorityParams);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String planSave(String locno, String instockNo, SystemUser user)
			throws ManagerException {
		try {
			Date dd = new Date();
			String instockWorker = user.getLoginName();
			String instockName = user.getUsername();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("instockNo", instockNo);
			params.put("instockWorker", instockWorker);
			params.put("instockName", instockName);
			params.put("instockDate", dd);
			int total = billUmInstockDtlService.findCount(params);
			int modifyTotal = billUmInstockDtlService.planSave(params);
			if(total != modifyTotal){
				throw new ManagerException("已经存在包含实际上架储位或实际上架数量的明细");
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String singleSave(BillUmInstockDtl billUmInstockDtl) throws ManagerException{
		try {
			Date dd = new Date();
			String locno = billUmInstockDtl.getLocno();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			String instockNo = billUmInstockDtl.getInstockNo();
			Long instockId = billUmInstockDtl.getInstockId();
			String itemNo = billUmInstockDtl.getItemNo();
			String sizeNo = billUmInstockDtl.getSizeNo();
			String boxNo = billUmInstockDtl.getBoxNo();
			String sourceNo = billUmInstockDtl.getSourceNo();
			//String cellNo = billUmInstockDtl.getCellNo();
			Long cellId = billUmInstockDtl.getCellId();
			Long destCellId = billUmInstockDtl.getDestCellId();
			String destCellNo = billUmInstockDtl.getDestCellNo();
			String realCellNo = billUmInstockDtl.getRealCellNo();
			BigDecimal realQty = billUmInstockDtl.getRealQty();
			String instockWorker = billUmInstockDtl.getInstockWorker();
			String instockName = billUmInstockDtl.getInstockName();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("cellNo", realCellNo);
			CmDefcell cmDefcell = new CmDefcell();
			cmDefcell.setLocno(locno);
			cmDefcell.setCellNo(realCellNo);
			cmDefcell = cmDefcellService.findById(cmDefcell);
			/**************************jys start******************************/
			//校验实际上架储位是否有效
			if(cmDefcell == null){
				throw new ManagerException("储位【"+realCellNo+"】不存在");
			}
			//判断储位状态是否可用
			if(!CmDefcellCellStatusEnums.CELLSTATUS_0.getStatus().equals(cmDefcell.getCellStatus())){
				throw new ManagerException("储位【"+realCellNo+"】不可用");
			}
			//是否非盘点状态
			if(CmDefcellCheckStatusEnums.CHECKSTATUS_3.getStatus().equals(cmDefcell.getCheckStatus())){
				throw new ManagerException("储位【"+realCellNo+"】必须非盘点状态");
			}
			//储位必须是退货作业区——退货作业区：是指【库区属性类型】为存储区(0),【库区属性】为作业区(0)，【库区用途】为退货区(3)
			CmDefarea cmDefarea = new CmDefarea();
			cmDefarea.setLocno(locno);
			cmDefarea.setAreaNo(cmDefcell.getAreaNo());
			cmDefarea.setWareNo(cmDefcell.getWareNo());
			cmDefarea = cmDefareaService.findById(cmDefarea);
			if(cmDefarea == null){
				throw new ManagerException("储位【"+realCellNo+"】不存在对应的库区");
			}
			if(!CmDefAreaAttributeTypeEnums.ATTRIBUTETYPE_0.getAttributeType().equals(cmDefarea.getAttributeType())){
				throw new ManagerException("储位【"+realCellNo+"】对应的【库区属性类型】必须为存储区");
			}
			if(!CmDefAreaAreaAttributeEnums.AREAAREAATTRIBUTE_0.getAreaareaAttribute().equals(cmDefarea.getAreaAttribute())){
				throw new ManagerException("储位【"+realCellNo+"】对应的【库区属性】必须为作业区");
			}
			if(!CmDefAreaAreaUsetypeEnums.AREAUSETYPE_3.getAreaUsetype().equals(cmDefarea.getAreaUsetype())
					&& !CmDefAreaAreaUsetypeEnums.AREAUSETYPE_1.getAreaUsetype().equals(cmDefarea.getAreaUsetype())){
				throw new ManagerException("储位【"+realCellNo+"】对应的【库区用途】必须为退货区或普通存储区");
			}
			//储位商品属性与预上库存相同；储位的品质与预上库存相同；
			ConContent conContent = new ConContent();
			conContent.setLocno(locno);
			conContent.setCellNo(destCellNo);
			conContent.setCellId(destCellId);
			conContent = conContentService.findById(conContent);
			if(conContent == null){
				throw new ManagerException("没有找到预上储位【"+destCellNo+"】对应的库存信息");
			}
			if(!cmDefcell.getItemType().equals(conContent.getItemType())){
				throw new ManagerException("实际上架储位【"+realCellNo+"】与预上储位【"+destCellNo+"】的商品属性必须相同");
			}
			if(!cmDefcell.getAreaQuality().equals(conContent.getQuality())){
				throw new ManagerException("实际上架储位【"+realCellNo+"】与预上储位【"+destCellNo+"】的商品品质必须相同");
			}
			//如果储位是不可混的，要判断空储位或者储位上的商品与预上商品相同
			if(CmDefCellMixFlagEnums.MIXFLAG_0.getMixFlag().equals(cmDefcell.getMixFlag()+"")){
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("locno", locno);
				params1.put("cellNo", realCellNo);
				List<ConContent> list = conContentService.findByBiz(null, params1);
				if(CommonUtil.hasValue(list)){
					for(ConContent cc:list){
						if(!itemNo.equals(cc.getItemNo())){
							throw new ManagerException("实际上架储位【"+realCellNo+"】不可混");
						}
					}
				}
			}
			/**************************jys end******************************/
			//计算剩余数量
			params.put("ownerNo", ownerNo);
			params.put("instockNo", instockNo);
			params.put("itemNo", itemNo);
			params.put("sizeNo", sizeNo);
			params.put("boxNo", boxNo);
			params.put("sourceNo", sourceNo);
			params.put("cellId", cellId);
			params.put("destCellId", destCellId);
			List<BillUmInstockDtl> list = billUmInstockDtlService.findByBiz(null, params);
			if(list.size() == 0){
				throw new ManagerException("没有需要保存的数据");
			}
			int itemQtyTotal = 0;
			int realQtyTotal = 0;
			//int instockedQtyTotal = 0;
			for(BillUmInstockDtl dtl:list){
				if(dtl.getItemQty() != null && dtl.getItemQty().intValue() > 0){
					itemQtyTotal += dtl.getItemQty().intValue();
				}else{
					realQtyTotal += dtl.getRealQty()==null?0:dtl.getRealQty().intValue();
				}
				//instockedQtyTotal += dtl.getInstockedQty()==null?0:dtl.getInstockedQty().intValue();
			}
			if(itemQtyTotal < (realQtyTotal + realQty.intValue())){
				throw new ManagerException("实际上架数量不能大于"+(itemQtyTotal-realQtyTotal));
			}
			billUmInstockDtl = new BillUmInstockDtl();
			billUmInstockDtl.setLocno(locno);
			billUmInstockDtl.setOwnerNo(ownerNo);
			billUmInstockDtl.setInstockNo(instockNo);
			billUmInstockDtl.setInstockId(instockId);
			billUmInstockDtl.setRealCellNo(realCellNo);
			billUmInstockDtl.setRealQty(realQty);
			billUmInstockDtl.setInstockWorker(instockWorker);
			billUmInstockDtl.setInstockName(instockName);
			billUmInstockDtl.setInstockDate(dd);
			int modifyCount = billUmInstockDtlService.modifyById(billUmInstockDtl);
			if(modifyCount != 1){
				throw new ManagerException("修改明细异常");
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String singleSplit(BillUmInstockDtl billUmInstockDtl)
			throws ManagerException {
		try {
			String locno = billUmInstockDtl.getLocno();
			String ownerNo = billUmInstockDtl.getOwnerNo();
			String instockNo = billUmInstockDtl.getInstockNo();
			Long instockId = billUmInstockDtl.getInstockId();
			String itemNo = billUmInstockDtl.getItemNo();
			String sizeNo = billUmInstockDtl.getSizeNo();
			String boxNo = billUmInstockDtl.getBoxNo();
			String sourceNo = billUmInstockDtl.getSourceNo();
			//String cellNo = billUmInstockDtl.getCellNo();
			Long cellId = billUmInstockDtl.getCellId();
			Long destCellId = billUmInstockDtl.getDestCellId();
			String destCellNo = billUmInstockDtl.getDestCellNo();
			String realCellNo = billUmInstockDtl.getRealCellNo();
			BigDecimal realQty = billUmInstockDtl.getRealQty();
			String instockWorker = billUmInstockDtl.getInstockWorker();
			String instockName = billUmInstockDtl.getInstockName();
			Date instockDate = new Date();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("cellNo", realCellNo);
			//校验实际上架储位是否有效
			CmDefcell cmDefcell = new CmDefcell();
			cmDefcell.setLocno(locno);
			cmDefcell.setCellNo(realCellNo);
			cmDefcell = cmDefcellService.findById(cmDefcell);
			/**************************jys start******************************/
			/*int cellCount = cmDefcellService.findCount(params);
			if(cellCount != 1){
				throw new ManagerException("储位【"+realCellNo+"】不存在");
			}*/
			//校验实际上架储位是否有效
			if(cmDefcell == null){
				throw new ManagerException("储位【"+realCellNo+"】不存在");
			}
			//判断储位状态是否可用
			if(!CmDefcellCellStatusEnums.CELLSTATUS_0.getStatus().equals(cmDefcell.getCellStatus())){
				throw new ManagerException("储位【"+realCellNo+"】不可用");
			}
			//是否非盘点状态
			if(CmDefcellCheckStatusEnums.CHECKSTATUS_3.getStatus().equals(cmDefcell.getCheckStatus())){
				throw new ManagerException("储位【"+realCellNo+"】必须非盘点状态");
			}
			//储位必须是退货作业区——退货作业区：是指【库区属性类型】为存储区(0),【库区属性】为作业区(0)，【库区用途】为退货区(3)
			CmDefarea cmDefarea = new CmDefarea();
			cmDefarea.setLocno(locno);
			cmDefarea.setAreaNo(cmDefcell.getAreaNo());
			cmDefarea.setWareNo(cmDefcell.getWareNo());
			cmDefarea = cmDefareaService.findById(cmDefarea);
			if(cmDefarea == null){
				throw new ManagerException("储位【"+realCellNo+"】不存在对应的库区");
			}
			if(!CmDefAreaAttributeTypeEnums.ATTRIBUTETYPE_0.getAttributeType().equals(cmDefarea.getAttributeType())){
				throw new ManagerException("储位【"+realCellNo+"】对应的【库区属性类型】必须为存储区");
			}
			if(!CmDefAreaAreaAttributeEnums.AREAAREAATTRIBUTE_0.getAreaareaAttribute().equals(cmDefarea.getAreaAttribute())){
				throw new ManagerException("储位【"+realCellNo+"】对应的【库区属性】必须为作业区");
			}
			if(!CmDefAreaAreaUsetypeEnums.AREAUSETYPE_3.getAreaUsetype().equals(cmDefarea.getAreaUsetype())&&!CmDefAreaAreaUsetypeEnums.AREAUSETYPE_1.getAreaUsetype().equals(cmDefarea.getAreaUsetype())){
				throw new ManagerException("储位【"+realCellNo+"】对应的【库区用途】必须为退货区或普通存储区");
			}
			//储位商品属性与预上库存相同；储位的品质与预上库存相同；
			ConContent conContent = new ConContent();
			conContent.setLocno(locno);
			conContent.setCellNo(destCellNo);
			conContent.setCellId(destCellId);
			conContent = conContentService.findById(conContent);
			if(conContent == null){
				throw new ManagerException("没有找到预上储位【"+destCellNo+"】对应的库存信息");
			}
			if(!cmDefcell.getItemType().equals(conContent.getItemType())){
				throw new ManagerException("实际上架储位【"+realCellNo+"】与预上储位【"+destCellNo+"】的商品属性必须相同");
			}
			if(!cmDefcell.getAreaQuality().equals(conContent.getQuality())){
				throw new ManagerException("实际上架储位【"+realCellNo+"】与预上储位【"+destCellNo+"】的商品品质必须相同");
			}
			//如果储位是不可混的，要判断空储位或者储位上的商品与预上商品相同
			if(CmDefCellMixFlagEnums.MIXFLAG_0.getMixFlag().equals(cmDefcell.getMixFlag()+"")){
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("locno", locno);
				params1.put("cellNo", realCellNo);
				List<ConContent> list = conContentService.findByBiz(null, params1);
				if(CommonUtil.hasValue(list)){
					for(ConContent cc:list){
						if(!itemNo.equals(cc.getItemNo())){
							throw new ManagerException("实际上架储位【"+realCellNo+"】不可混");
						}
					}
				}
			}
			/**************************jys end******************************/
			params.put("ownerNo", ownerNo);
			params.put("instockNo", instockNo);
			//获取新的instockId
			long maxInstockId = billUmInstockDtlService.findMaxInstockId(params);
			maxInstockId++;
			//计算剩余数量
			params.put("itemNo", itemNo);
			params.put("sizeNo", sizeNo);
			params.put("boxNo", boxNo);
			params.put("sourceNo", sourceNo);
			params.put("cellId", cellId);
			params.put("destCellId", destCellId);
			List<BillUmInstockDtl> list = billUmInstockDtlService.findByBiz(null, params);
			if(list.size() == 0){
				throw new ManagerException("没有需要保存的数据");
			}
			int itemQtyTotal = 0;
			int realQtyTotal = 0;
			//int instockedQtyTotal = 0;
			for(BillUmInstockDtl dtl:list){
				if(realCellNo.equals(dtl.getRealCellNo())){
					throw new ManagerException("储位【"+realCellNo+"】已经存在");
				}
				if(dtl.getItemQty() != null && dtl.getItemQty().intValue() > 0){
					itemQtyTotal += dtl.getItemQty().intValue();
				}
				realQtyTotal += dtl.getRealQty()==null?0:dtl.getRealQty().intValue();
				
				//instockedQtyTotal += dtl.getInstockedQty()==null?0:dtl.getInstockedQty().intValue();
			}
			if(itemQtyTotal < (realQtyTotal + realQty.intValue())){
				if(itemQtyTotal-realQtyTotal <= 0){
					throw new ManagerException("此明细已经不能再拆分");
				}
				throw new ManagerException("实际上架数量不能大于"+(itemQtyTotal-realQtyTotal));
			}
			billUmInstockDtl = billUmInstockDtlService.findById(billUmInstockDtl);
			billUmInstockDtl.setItemQty(new BigDecimal(0));
			billUmInstockDtl.setInstockId(maxInstockId);
			billUmInstockDtl.setRealCellNo(realCellNo);
			billUmInstockDtl.setRealQty(realQty);
			billUmInstockDtl.setInstockWorker(instockWorker);
			billUmInstockDtl.setInstockName(instockName);
			billUmInstockDtl.setInstockDate(instockDate);
			int addCount = billUmInstockDtlService.add(billUmInstockDtl);
			if(addCount != 1){
				throw new ManagerException("拆分明细异常");
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String singleDelete(BillUmInstockDtl billUmInstockDtl)
			throws ManagerException {
		try {
			
			int deleteCount = billUmInstockDtlService.deleteById(billUmInstockDtl);
			if(deleteCount != 1){
				throw new ManagerException("删除明细异常");
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}

}