package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.enums.ItemTypeEnums;
import com.yougou.logistics.city.common.enums.QualityEnums;
import com.yougou.logistics.city.common.model.BillSmWaste;
import com.yougou.logistics.city.common.model.BillSmWasteDtl;
import com.yougou.logistics.city.common.model.BillSmWasteDtlSizeDto;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillSmWasteDtlService;
import com.yougou.logistics.city.service.BillSmWasteService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ConContentService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2013-12-19 13:47:49
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
@Service("billSmWasteDtlManager")
class BillSmWasteDtlManagerImpl extends BaseCrudManagerImpl implements BillSmWasteDtlManager {
	@Resource
	private BillSmWasteDtlService billSmWasteDtlService;
	@Resource
	private BillSmWasteService billSmWasteService;
	@Resource
	private ConContentService conContentService;
	@Resource
	private BmContainerService bmContainerService;
	
	@Override
	public BaseCrudService init() {
		return billSmWasteDtlService;
	}

	@Override
	public int selectContentCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billSmWasteDtlService.selectContentCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillSmWasteDtl> selectContent(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billSmWasteDtlService.selectContent(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addSmWasteDtl(BillSmWaste billSmWaste,
			Map<CommonOperatorEnum, List<ModelType>> params, AuthorityParams authorityParams) throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		boolean flag = false;

		try {
			Date date = new Date();
			/****************修改退仓收货单主档信息*******************/
			String locno = billSmWaste.getLocno();
			String ownerNo = billSmWaste.getOwnerNo();
			String wasteNo = billSmWaste.getWasteNo();

			Map<String, Object> bill = new HashMap<String, Object>();
			bill.put("locno", locno);
			bill.put("ownerNo", ownerNo);
			bill.put("wasteNo", wasteNo);
			bill.put("status", "10");
			List<BillSmWaste> param = billSmWasteService.findByWaste(billSmWaste, bill, authorityParams);
			if (param != null && param.size() > 0) {
			} else {
				throw new ManagerException("当前单据状态不可编辑！");
			}
			/****************新增退仓收货单明细信息*******************/
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);//新增
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);//删除
			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);//更新

			//删除操作
			if (CommonUtil.hasValue(delList)) {
				flag = false;
				for (ModelType modelType : delList) {
					if (modelType instanceof BillSmWasteDtl) {
						BillSmWasteDtl vo = (BillSmWasteDtl) modelType;
						String wasteNoDtl = wasteNo;
						String locnoDtl = locno;
						String ownerNoDtl = ownerNo;
						//						Long cellIdDtl = vo.getCellId();
						String cellNoDtl = vo.getCellNo();
						String itemNoDtl = vo.getItemNo();
						String sizeNoDtl = vo.getSizeNo();
						String qualityDtl = vo.getQuality();
						String itemTypeDtl = vo.getItemType();
						
						//释放删除的所有箱号的明细
						String panNoDtl = vo.getPanNo();
						String boxNoDtl = vo.getBoxNo();
						List<String> delBoxList = new ArrayList<String>();
						if(StringUtils.isNotBlank(boxNoDtl)){
							Map<String, Object> paramsBox = new HashMap<String, Object>();
							paramsBox.put("locno", locnoDtl);
							paramsBox.put("ownerNo", ownerNoDtl);
							paramsBox.put("wasteNo", wasteNoDtl);
							paramsBox.put("boxNo", boxNoDtl);
							List<BillSmWasteDtl> boxDtl = billSmWasteDtlService.findByBiz(null, paramsBox);
							if(CommonUtil.hasValue(boxDtl)){
								BillSmWasteDtl smWasteDtl = new BillSmWasteDtl();
								smWasteDtl.setLocno(locnoDtl);
								smWasteDtl.setOwnerNo(ownerNoDtl);
								smWasteDtl.setWasteNo(wasteNoDtl);
								smWasteDtl.setBoxNo(boxNoDtl);
								billSmWasteDtlService.deleteById(smWasteDtl);
							}
							delBoxList.add(boxNoDtl);
						}else{
							//获取库存的明细信息
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("locno", locnoDtl);
							queryParam.put("ownerNo", ownerNoDtl);
							queryParam.put("wasteNo", wasteNoDtl);
							//						queryParam.put("cellId", cellIdDtl);
							queryParam.put("cellNo", cellNoDtl);
							queryParam.put("itemNo", itemNoDtl);
							queryParam.put("sizeNo", sizeNoDtl);
							queryParam.put("quality", qualityDtl);
							queryParam.put("itemType", itemTypeDtl);
							queryParam.put("isScattered", "Y");

							List<BillSmWasteDtl> query = billSmWasteDtlService.findByWaste(vo, queryParam, authorityParams);
							if (query.size() == 1) {
								Short rowId = query.get(0).getRowId();

								BillSmWasteDtl delParamerKey = new BillSmWasteDtl();
								delParamerKey.setLocno(locno);
								delParamerKey.setOwnerNo(ownerNo);
								delParamerKey.setWasteNo(wasteNo);
								delParamerKey.setRowId(rowId);

								delParamerKey.setCellNo(cellNoDtl);
								delParamerKey.setItemNo(itemNoDtl);
								delParamerKey.setSizeNo(sizeNoDtl);

								int a = billSmWasteDtlService.deleteById(delParamerKey);
								if (a < 1) {
									throw new ManagerException("删除库存报损信息失败！");
								}
							} else {
								throw new ManagerException("找不到可删除的库存报损信息！");
							}
						}
						
						//如果箱号不为空,是否容器表的箱号
						if(CommonUtil.hasValue(delBoxList)){
							List<BmContainer> delBoxContainerList = new ArrayList<BmContainer>();
							for (String boxNo : delBoxList) {
								BmContainer bmContainer = new BmContainer();
								bmContainer.setLocno(locnoDtl);
								bmContainer.setConNo(boxNo);
								bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
								bmContainer.setFalg("Y");
								delBoxContainerList.add(bmContainer);
							}
							int containerCount = bmContainerService.batchUpdate(delBoxContainerList);
							if(containerCount < 1){
								throw new ServiceException("解锁预分货单箱号容器失败!");
							}
						}
						
					}
				}
				flag = true;
			}
			//新增操作
			if (CommonUtil.hasValue(addList)) {
				flag = false;
				//查询最大的Pid,作为主键 
				BillSmWasteDtl keyObj = new BillSmWasteDtl();
				keyObj.setWasteNo(wasteNo);
				keyObj.setLocno(locno);
				keyObj.setOwnerNo(ownerNo);
				short pidNum = (short) billSmWasteDtlService.selectMaxPid(keyObj);

				for (ModelType modelType : addList) {
					if (modelType instanceof BillSmWasteDtl) {
						BillSmWasteDtl vo = (BillSmWasteDtl) modelType;
						String wasteNoDtl = wasteNo;
						String locnoDtl = locno;
						String ownerNoDtl = ownerNo;
						//						Long cellIdDtl = vo.getCellId();
						String cellNoDtl = vo.getCellNo();
						String itemNoDtl = vo.getItemNo();
						//String itemNameDtl = vo.getItemName();
						String sizeNoDtl = vo.getSizeNo();
						String qualityDtl = vo.getQuality();
						String itemTypeDtl = vo.getItemType();
						BigDecimal wasteQty = vo.getWasteQty();
						if (wasteQty.doubleValue() <= 0) {
							throw new ManagerException("数量不允许为0！");
						}

						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("locno", locnoDtl);
						queryParam.put("ownerNo", ownerNoDtl);
						//						queryParam.put("cellId", cellIdDtl);
						queryParam.put("cellNo", cellNoDtl);
						queryParam.put("itemNo", itemNoDtl);
						queryParam.put("sizeNo", sizeNoDtl);
						queryParam.put("quality", qualityDtl);
						queryParam.put("itemType", itemTypeDtl);

						/*List<BillSmWasteDtl> query = billSmWasteDtlService.findByWaste(vo, queryParam, authorityParams);
						if (query.size() > 0) {
							String doubleNo = "";
							for (BillSmWasteDtl sm : query) {
								String no = sm.getWasteNo();

								Map<String, Object> bi = new HashMap<String, Object>();
								bi.put("locno", locno);
								bi.put("ownerNo", ownerNo);
								bi.put("wasteNo", no);
								bi.put("status", "10");
								List<BillSmWaste> pa = billSmWasteService.findByWaste(billSmWaste, bi, authorityParams);
								if (pa.size() > 0) {
									doubleNo += "[" + no + "]";

								}
							}
							if (doubleNo.trim() != "") {
								throw new ManagerException("商品名称：" + itemNameDtl + "</br>商品编码：" + itemNoDtl
										+ "</br>尺码：" + sizeNoDtl + " 储位：" + cellNoDtl + "</br>与未审核单据：" + doubleNo
										+ " 商品重复," + "</br>请勿重复报损！");
							}
						}*/

						queryParam.put("wasteNo", wasteNoDtl);
						queryParam.put("isScattered", "Y");
						List<BillSmWasteDtl> query = billSmWasteDtlService.findByWaste(vo, queryParam, authorityParams);
						if (query.size() > 0) {
							BillSmWasteDtl dtl = query.get(0);
							String cellNo = dtl.getCellNo();
							String itemNo = dtl.getItemNo();
							String sizeNo = dtl.getSizeNo();
							throw new ManagerException("零散库存：储位:"+cellNo+",商品:"+itemNo+",尺码:"+sizeNo+"数据重复，请核对！");
						} else {
							//获取库存的明细信息
							List<BillSmWasteDtl> conContent = billSmWasteDtlService.selectContentParams(vo, queryParam,
									authorityParams);

							//循环插入箱号明细
							for (BillSmWasteDtl dtl : conContent) {
								BigDecimal conQty = dtl.getConQty();
								if (conQty.compareTo(wasteQty) < 0) {
									vo.setWasteQty(conQty);
								}
								vo.setLocno(locnoDtl);
								vo.setOwnerNo(ownerNoDtl);
								vo.setWasteNo(wasteNoDtl);
								vo.setRowId(++pidNum);
								vo.setItemNo(dtl.getItemNo());
								vo.setSizeNo(dtl.getSizeNo());
								vo.setQuality(dtl.getQuality());
								vo.setItemType(dtl.getItemType());
								vo.setWasteQty(vo.getWasteQty());
								vo.setProduceDate(date);
								vo.setEditor(billSmWaste.getEditor());
								vo.setEditorName(billSmWaste.getEditorname());
								vo.setEdittm(date);
								int a;
								try {
									a = billSmWasteDtlService.add(vo);
								} catch (Exception e) {
									e.printStackTrace();
									throw new ManagerException("添加库存报损明细失败！");
								}
								if (a < 1) {
									throw new ManagerException("添加库存报损信息失败！");
								} 
							}
						}
					}
				}
				flag = true;
			}
			//更新操作
			if (CommonUtil.hasValue(uptList)) {
				flag = false;
				for (ModelType modelType : uptList) {
					if (modelType instanceof BillSmWasteDtl) {
						BillSmWasteDtl vo = (BillSmWasteDtl) modelType;
						String wasteNoDtl = wasteNo;
						String locnoDtl = locno;
						String ownerNoDtl = ownerNo;
						//Long cellIdDtl = vo.getCellId();
						String cellNoDtl = vo.getCellNo();
						String itemNoDtl = vo.getItemNo();
						String sizeNoDtl = vo.getSizeNo();
						String qualityDtl = vo.getQuality();
						String itemTypeDtl = vo.getItemType();
						BigDecimal wasteQty = vo.getWasteQty();
						if (wasteQty.doubleValue() <= 0) {
							throw new ManagerException("数量不允许为0！");
						}

						//获取库存的明细信息
						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("locno", locnoDtl);
						queryParam.put("ownerNo", ownerNoDtl);
						queryParam.put("wasteNo", wasteNoDtl);
						//queryParam.put("cellId", cellIdDtl);
						queryParam.put("cellNo", cellNoDtl);
						queryParam.put("itemNo", itemNoDtl);
						queryParam.put("sizeNo", sizeNoDtl);
						queryParam.put("quality", qualityDtl);
						queryParam.put("itemType", itemTypeDtl);
						queryParam.put("isScattered", "Y");
						List<BillSmWasteDtl> query = billSmWasteDtlService.findByWaste(vo, queryParam, authorityParams);
						if (query.size() == 1) {
							//获取库存的明细信息
							List<BillSmWasteDtl> conContent = billSmWasteDtlService.selectContentParams(vo, queryParam,
									authorityParams);

							//循环插入箱号明细
							for (BillSmWasteDtl dtl : conContent) {
								BigDecimal conQty = dtl.getConQty();
								if (conQty.compareTo(wasteQty) < 0) {

									vo.setWasteQty(conQty);
									//throw new ManagerException("报损数量不允许大于库存数据！");
								}
								Short rowId = query.get(0).getRowId();
								BillSmWasteDtl delParamerKey = new BillSmWasteDtl();
								delParamerKey.setLocno(locno);
								delParamerKey.setOwnerNo(ownerNo);
								delParamerKey.setWasteNo(wasteNo);
								delParamerKey.setRowId(rowId);
								delParamerKey.setWasteQty(vo.getWasteQty());
								delParamerKey.setEditor(billSmWaste.getEditor());
								delParamerKey.setEditorName(billSmWaste.getEditorname());
								delParamerKey.setEdittm(date);
								int a = billSmWasteDtlService.modifyById(delParamerKey);
								if (a < 1) {
									throw new ManagerException("更新库存报损信息失败！");
								}
							}
						} else {
							throw new ManagerException("找不到可更新的库存报损信息！");
						}
					}
				}
				flag = true;
			}
			
			//修改主单修改人信息
			billSmWasteService.modifyById(billSmWaste);
			
			if (flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "明细保存成功");
			} else {
				throw new ManagerException("当前明细保存失败！");
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billSmWasteDtlService.selectSumQty(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String importDtl(List<BillSmWasteDtl> list, String wasteNo, String ownerNo, SystemUser user)
			throws ManagerException {
		StringBuilder sb = new StringBuilder();
		try {
			//
			int pageSize = 500;//批量insert的数据量
			String locno = user.getLocNo();
			Map<String, String> map = new HashMap<String, String>();
			Map<String, BillSmWasteDtl> exist = getExist(wasteNo);
			Short rowId = 1;
			if (exist.size() != 0) {
				BillSmWasteDtl d = new BillSmWasteDtl();
				d.setLocno(locno);
				d.setWasteNo(wasteNo);
				d.setOwnerNo(ownerNo);
				rowId = (short) billSmWasteDtlService.selectMaxPid(d);
			}
			String key = "";
			Map<String, Object> params = new HashMap<String, Object>();
			List<ConContentDto> ccList = null;
			BigDecimal total = null;
			params.put("locno", locno);

			String itemType = "";
			String quality = "";
			int idx = 1;
			for (BillSmWasteDtl dtl : list) {
				idx++;
				//********************判断商品属性和品质是否合法S***********************
				quality = QualityEnums.getValueByDesc(dtl.getQuality());
				if (StringUtils.isBlank(quality)) {
					sb.append("第").append(idx).append("行【品质】非法\\r\\n");
					continue;
				} else {
					dtl.setQuality(quality);
				}
				itemType = ItemTypeEnums.getValueByDesc(dtl.getItemType());
				if (StringUtils.isBlank(itemType)) {
					sb.append("第").append(idx).append("行【属性】非法\\r\\n");
					continue;
				} else {
					dtl.setItemType(itemType);
				}
				//********************判断商品属性和品质是否合法E***********************
				key = dtl.getItemNo() + "_" + dtl.getSizeNo() + "_" + dtl.getItemType() + "_" + dtl.getQuality() + "_"
						+ dtl.getCellNo();
				//********************Excel重复S***********************
				if (map.get(key) == null) {
					map.put(key, "【第" + idx + "行】");
				} else {
					sb.append(map.get(key)).append("与【第" + idx + "行】数据重复\\r\\n");
					continue;
				}
				//********************Excel重复E***********************
				//********************Excel与明细表是否存在重复S***********************
				if (exist.size() > 0 && exist.get(key) != null) {
					sb.append("第").append(idx).append("行数据在明细表中已经存在\\r\\n");
					continue;
				}
				//********************Excel与明细表是否存在重复E***********************

				//********************库存是否存在、是否有足够的库存S***********************
				params.put("itemNo", dtl.getItemNo());
				params.put("sizeNo", dtl.getSizeNo());
				params.put("cellNo", dtl.getCellNo());
				params.put("itemType", itemType);
				params.put("quality", quality);
				params.put("status", "0");
				params.put("hmManualFlag", "1");
				params.put("flag", "0");
				ccList = conContentService.findViewByParams(params);
				if (!CommonUtil.hasValue(ccList)) {
					sb.append("第").append(idx).append("行数据不存在或库存不可用\\r\\n");
					continue;
				} else {
					total = new BigDecimal(0);
					for (ConContent cc : ccList) {
						total = total.add(cc.getQty().subtract(cc.getOutstockQty()));
					}
					if (total.intValue() < dtl.getWasteQty().intValue()) {
						sb.append("第").append(idx).append("行【出库数量】不能大于").append(total.intValue()).append("\\r\\n");
						continue;
					}
					dtl.setBrandNo(ccList.get(0).getBrandNo());
				}
				//********************库存是否存在、是否有足够的库存E***********************
				dtl.setLocno(locno);
				dtl.setWasteNo(wasteNo);
				dtl.setOwnerNo(ownerNo);
				dtl.setRowId(++rowId);
			}
			if (sb.length() > 0) {
				throw new ManagerException(
						"<span style='color:red;'>非法数据异常:以下行数为Excel真实行数</span><br><br><textarea rows='5' cols='40'>"
								+ sb.toString() + "</textarea>");
			}
			//********************批量保存S***********************
			for (int i = 0; i < list.size();) {
				i += pageSize;
				if (i > list.size()) {
					billSmWasteDtlService.batchInsertDtl(list.subList(i - pageSize, list.size()));
				} else {
					billSmWasteDtlService.batchInsertDtl(list.subList(i - pageSize, i));
				}

			}
			//********************批量保存E***********************
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return null;
	}

	public Map<String, BillSmWasteDtl> getExist(String wasteNo) {

		Map<String, BillSmWasteDtl> map = new HashMap<String, BillSmWasteDtl>();
		String key = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("wasteNo", wasteNo);
		try {
			List<BillSmWasteDtl> list = billSmWasteDtlService.findByBiz(null, params);
			for (BillSmWasteDtl dtl : list) {
				key = dtl.getItemNo() + "_" + dtl.getSizeNo() + "_" + dtl.getItemType() + "_" + dtl.getQuality() + "_"
						+ dtl.getCellNo();
				map.put(key, dtl);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return map;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addShipmentDtl(BillSmWaste billSmWaste,
			Map<CommonOperatorEnum, List<ModelType>> params, AuthorityParams authorityParams) throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		boolean flag = false;
		try {
			/****************修改退仓收货单主档信息*******************/
			String locno = billSmWaste.getLocno();
			String ownerNo = billSmWaste.getOwnerNo();
			String wasteNo = billSmWaste.getWasteNo();

			Map<String, Object> bill = new HashMap<String, Object>();
			bill.put("locno", locno);
			bill.put("ownerNo", ownerNo);
			bill.put("wasteNo", wasteNo);
			bill.put("status", "10");
			List<BillSmWaste> param = billSmWasteService.findByWaste(billSmWaste, bill, authorityParams);
			if (param != null && param.size() > 0) {
			} else {
				throw new ManagerException("当前单据状态不可编辑！");
			}
			
			//查询所有明细
			List<BillSmWasteDtl> wasteDtlList = billSmWasteDtlService.findByBiz(null, bill);
			if(!CommonUtil.hasValue(wasteDtlList)){
				throw new ManagerException("单据无明细,请保存明细后在操作！");
			}
			
			//循环所有明细把库存数量=出库数量(零散库存)
			for (BillSmWasteDtl vo : wasteDtlList) {
				String wasteNoDtl = wasteNo;
				String locnoDtl = locno;
				String ownerNoDtl = ownerNo;
				//Long cellIdDtl = vo.getCellId();
				String cellNoDtl = vo.getCellNo();
				String itemNoDtl = vo.getItemNo();
				String sizeNoDtl = vo.getSizeNo();
				String qualityDtl = vo.getQuality();
				String itemTypeDtl = vo.getItemType();
				BigDecimal wasteQty = vo.getWasteQty();
				//只能按库存保存零散的
				String boxNoDtl = vo.getBoxNo();
				//获取库存的明细信息
				Map<String, Object> queryParam = new HashMap<String, Object>();
				queryParam.put("locno", locnoDtl);
				queryParam.put("ownerNo", ownerNoDtl);
				queryParam.put("wasteNo", wasteNoDtl);
				queryParam.put("cellNo", cellNoDtl);
				queryParam.put("itemNo", itemNoDtl);
				queryParam.put("sizeNo", sizeNoDtl);
				queryParam.put("quality", qualityDtl);
				queryParam.put("itemType", itemTypeDtl);
				
				//只处理零散的按库存保存
				if(StringUtils.isEmpty(boxNoDtl)){
					List<BillSmWasteDtl> conContent = billSmWasteDtlService.selectContentParams(vo, queryParam,authorityParams);
					if (conContent.size() != 0) {
						   BigDecimal conQty = conContent.get(0).getConQty();
							if(conQty.intValue() > 0){
								vo.setWasteQty(conQty);
							}
						}else{
							vo.setWasteQty(new BigDecimal(0));
						}
						Short rowId = vo.getRowId();
						BillSmWasteDtl delParamerKey = new BillSmWasteDtl();
						delParamerKey.setLocno(locno);
						delParamerKey.setOwnerNo(ownerNo);
						delParamerKey.setWasteNo(wasteNo);
						delParamerKey.setRowId(rowId);
						delParamerKey.setWasteQty(vo.getWasteQty());
					    delParamerKey.setEditor(billSmWaste.getEditor());
						delParamerKey.setEditorName(billSmWaste.getEditorname());
						delParamerKey.setEdittm(new Date());
						int a = billSmWasteDtlService.modifyById(delParamerKey);
						if (a < 1) {
							throw new ManagerException("更新库存报损信息失败！");
						}
				}
			}
			billSmWasteService.modifyById(billSmWaste);
			flag = true;
			
//			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);//更新
//			//更新操作
//			if (CommonUtil.hasValue(uptList)) {
//				flag = false;
//				for (ModelType modelType : uptList) {
//					if (modelType instanceof BillSmWasteDtl) {
//						BillSmWasteDtl vo = (BillSmWasteDtl) modelType;
//						String wasteNoDtl = wasteNo;
//						String locnoDtl = locno;
//						String ownerNoDtl = ownerNo;
//						//Long cellIdDtl = vo.getCellId();
//						String cellNoDtl = vo.getCellNo();
//						String itemNoDtl = vo.getItemNo();
//						String sizeNoDtl = vo.getSizeNo();
//						String qualityDtl = vo.getQuality();
//						String itemTypeDtl = vo.getItemType();
//						BigDecimal wasteQty = vo.getWasteQty();
//						//只能按库存保存零散的
//						String boxNoDtl = vo.getBoxNo();
//						if (wasteQty.doubleValue() <= 0) {
//							throw new ManagerException("数量不允许为0！");
//						}
//
//						//获取库存的明细信息
//						Map<String, Object> queryParam = new HashMap<String, Object>();
//						queryParam.put("locno", locnoDtl);
//						queryParam.put("ownerNo", ownerNoDtl);
//						queryParam.put("wasteNo", wasteNoDtl);
//						//queryParam.put("cellId", cellIdDtl);
//						queryParam.put("cellNo", cellNoDtl);
//						queryParam.put("itemNo", itemNoDtl);
//						queryParam.put("sizeNo", sizeNoDtl);
//						queryParam.put("quality", qualityDtl);
//						queryParam.put("itemType", itemTypeDtl);
//
//						if(StringUtils.isEmpty(boxNoDtl)){
//							List<BillSmWasteDtl> query = billSmWasteDtlService.findByWaste(vo, queryParam, authorityParams);
//							//获取库存的明细信息
//							List<BillSmWasteDtl> conContent = billSmWasteDtlService.selectContentParams(vo, queryParam,
//									authorityParams);
//							if (query.size() == 1) {
//								//循环插入箱号明细
//								   if (conContent.size() != 0) {
//									   BigDecimal conQty = conContent.get(0).getConQty();
//										if (conQty.compareTo(wasteQty) < 0) {
//											vo.setWasteQty(conQty);
//										}
//									}else{
//										vo.setWasteQty(new BigDecimal(0));
//									}
//									Short rowId = query.get(0).getRowId();
//									BillSmWasteDtl delParamerKey = new BillSmWasteDtl();
//									delParamerKey.setLocno(locno);
//									delParamerKey.setOwnerNo(ownerNo);
//									delParamerKey.setWasteNo(wasteNo);
//									delParamerKey.setRowId(rowId);
//									delParamerKey.setWasteQty(vo.getWasteQty());
//									int a = billSmWasteDtlService.modifyById(delParamerKey);
//									if (a < 1) {
//										throw new ManagerException("更新库存报损信息失败！");
//									}
//							} else {
//								throw new ManagerException("请先保存明细再按库保存！");
//							}
//						}
//					}
//				}
//				flag = true;
//			}
			if (flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "按库保存成功");
			} else {
				throw new ManagerException("当前按库保存失败！");
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	public List<BillSmWasteDtlSizeDto> findDtl4SizeHorizontal(String wasteNo) {
		return this.billSmWasteDtlService.findDtl4SizeHorizontal(wasteNo);
	}
	
	@Override
	public List<BillSmWasteDtl> findDtlSysNo(BillSmWasteDtl billSmWasteDtl, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billSmWasteDtlService.findDtlSysNo(billSmWasteDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public Map<String, Object> findDtlSysNoByPage(BillSmWasteDtl billSmWasteDtl,
			AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.billSmWasteDtlService.findDtlSysNoByPage(billSmWasteDtl, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public void saveWasteBoxContainer(List<BillSmWasteDtl> boxList,String loginName,String userName) throws ManagerException {
		try{
			if(!CommonUtil.hasValue(boxList)){
				throw new ManagerException("请选择箱明细数据!");
			}
			int containerCount = 0;
			List<BmContainer> addContainerList = new ArrayList<BmContainer>();
			for (BillSmWasteDtl billSmWasteDtl : boxList) {
				String locno = billSmWasteDtl.getLocno();
				String boxNo = billSmWasteDtl.getBoxNo();
				String wasteNo = billSmWasteDtl.getWasteNo();
				//容器验证是否锁定
				BmContainer bmContainer = new BmContainer();
				bmContainer.setLocno(locno);
				bmContainer.setConNo(boxNo);
				bmContainer.setStatus(ContainerStatusEnums.STATUS1.getContainerStatus());
				bmContainer.setOptBillNo(wasteNo);
				bmContainer.setOptBillType(ContainerTypeEnums.E.getOptBillType());
				boolean result = bmContainerService.checkBmContainerStatus(bmContainer);
				//如果板号处于锁定状态,而且板号中的箱不存在预分货单中
				BmContainer container = bmContainerService.findById(bmContainer);
				if(container == null || "1".equals(container.getStatus()) || result){
					throw new ServiceException("容器号:"+boxNo+"不存在或已锁定!");
				}
				if(!result){
					addContainerList.add(bmContainer);
				}
			}
			containerCount = bmContainerService.batchUpdate(addContainerList);
			if(containerCount < 1){
				throw new ServiceException("锁定容器号失败!");
			}
			
			//查询最大的ROWID
			String locno = boxList.get(0).getLocno();
			String ownerNo = boxList.get(0).getOwnerNo();
			String wasteNo = boxList.get(0).getWasteNo();
			BillSmWasteDtl billSmWasteDtl = new BillSmWasteDtl();
			billSmWasteDtl.setLocno(locno);
			billSmWasteDtl.setOwnerNo(ownerNo);
			billSmWasteDtl.setWasteNo(wasteNo);
			int rowId = billSmWasteDtlService.selectMaxPid(billSmWasteDtl);
			
			//开始批量插入明细
			Date now = new Date();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("ownerNo", ownerNo);
			params.put("wasteNo", wasteNo);
			params.put("rowId", rowId);
			params.put("produceDate", now);
			params.put("editor", loginName);
			params.put("editorName", userName);
			params.put("edittm", now);
			containerCount = billSmWasteDtlService.batchInsertWasteDtl4Box(params, boxList); 
			if(containerCount < 1){
				throw new ServiceException(wasteNo+"添加箱容器明细数据失败!");
			}
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
}