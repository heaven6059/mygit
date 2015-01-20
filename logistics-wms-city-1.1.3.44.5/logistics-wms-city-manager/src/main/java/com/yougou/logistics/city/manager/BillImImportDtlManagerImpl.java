package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillImImportDtlDto;
import com.yougou.logistics.city.common.dto.BillImImportDtlSizeKind;
import com.yougou.logistics.city.common.model.BillImImport;
import com.yougou.logistics.city.common.model.BillImImportDtl;
import com.yougou.logistics.city.common.model.BillImImportDtlKey;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillImImportDtlForPage;
import com.yougou.logistics.city.service.BillImImportDtlService;
import com.yougou.logistics.city.service.BillImImportService;
import com.yougou.logistics.city.service.ConBoxDtlService;
import com.yougou.logistics.city.service.ConBoxService;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-25 10:24:56
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
@Service("billImImportDtlManager")
class BillImImportDtlManagerImpl extends BaseCrudManagerImpl implements BillImImportDtlManager {
	@Resource
	private BillImImportDtlService billImImportDtlService;
	
	@Resource
	private BillImImportService billImImportService;

	@Resource
	private ConBoxDtlService conBoxDtlService;

	@Resource
	private ConBoxService conBoxService;
	
	private static final String  STATUS10 ="10";

	@Override
	public BaseCrudService init() {
		return billImImportDtlService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addImImportDetail(String locno, String ownerNo, String importNo,
			Map<CommonOperatorEnum, List<ModelType>> params, String loginName,String username) throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String flag = "false";
		String msg = "";
		try {
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);
			List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);

			// 新增操作
			if (null != addList && addList.size() > 0) {
				
				BillImImport objImImport = new  BillImImport();
				objImImport.setLocno(locno);
				objImImport.setImportNo(importNo);
				objImImport.setOwnerNo(ownerNo);
				
				BillImImport obj = billImImportService.findById(objImImport);
				if(null==obj || !STATUS10.equals(obj.getStatus())){
					flag = "warn";
					msg = "单据" + importNo + "已删除或状态已改变，不能进行保存明细操作！";
					mapObj.put("flag", flag);
					mapObj.put("msg", msg);
					return mapObj;
				}
				
				// 查询最大的Pid,作为主键
				BillImImportDtlKey keyObj = new BillImImportDtlKey();
				keyObj.setImportNo(importNo);
				keyObj.setLocno(locno);
				keyObj.setOwnerNo(ownerNo);
				short pidNum = (short) billImImportDtlService.selectMaxPid(keyObj);

				// 剔除重复的list
				CommonUtil.removeDuplicateWithOrder(addList);

				for (ModelType modelType : addList) {
					if (modelType instanceof BillImImportDtl) {
						BillImImportDtl vo = (BillImImportDtl) modelType;

						// 校验箱号是否已经存在于明细单中

						keyObj.setBoxNo(vo.getBoxNo());
						int num = billImImportDtlService.selectBoxNoIsHave(keyObj);
						if (num > 0) {
							flag = "warn";
							msg = "箱号【" + vo.getBoxNo() + "】已存在，请删除！";
							mapObj.put("flag", flag);
							mapObj.put("msg", msg);
							return mapObj;
						}

						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("locno", locno);
						queryParam.put("ownerNo", ownerNo);
						queryParam.put("boxNo", vo.getBoxNo());
						// 获取箱码的明细信息
						List<ConBoxDtl> lstConBoxDtl = conBoxDtlService.findByParam(queryParam);

						for (ConBoxDtl box : lstConBoxDtl) {
							BillImImportDtl insertVo = new BillImImportDtl();
							insertVo.setImportNo(importNo);
							insertVo.setLocno(locno);
							insertVo.setOwnerNo(ownerNo);
							insertVo.setCarPlate(vo.getCarPlate());
							insertVo.setDeliverNo(vo.getDeliverNo());
							insertVo.setBoxNo(box.getBoxNo());
							insertVo.setPoQty(box.getQty());
							insertVo.setItemNo(box.getItemNo());
							insertVo.setSizeNo(box.getSizeNo());
							insertVo.setCheckName(loginName);
							insertVo.setCheckNameCh(username);//验收人员中文名称
							insertVo.setCheckDate(new Date());
							insertVo.setStatus("10");// 新建单
							insertVo.setPoId(++pidNum);// 行号
							insertVo.setQcFlag("0");// 质检标识0-正常；1-质检
							insertVo.setPackQty(new BigDecimal(1));
							insertVo.setBrandNo(box.getBrandNo());
							// 插入预到货通知单明细记录
							int a = billImImportDtlService.add(insertVo);
							if (a < 1) {
								throw new ManagerException("插入预到货通知单明细记录时未更新到记录！");
							}
						}

						// 更新箱码的入库单号为预到货通知单号;
						ConBox conBox = new ConBox();
						conBox.setBoxNo(vo.getBoxNo());
						// conBox.setsImportNo(importNo);
						conBox.setStatus("0");// 箱状态
						// 0:未使用1:入库扫描2:已进仓3:已出仓4:已拆箱5:出库扫描6:返配扫描7:已返配进仓7:退货扫描8:已退货
						conBox.setLocno(locno);
						conBox.setOwnerNo(ownerNo);
						int b = conBoxService.modifyById(conBox);

						if (b < 1) {
							throw new ManagerException("更新箱码的入库单号为预到货通知单号时未更新到记录！");
						} else {
							ConBoxDtl cbDtl = new ConBoxDtl();
							cbDtl.setImportNo(importNo);
							cbDtl.setLocno(locno);
							cbDtl.setOwnerNo(ownerNo);
							cbDtl.setBoxNo(conBox.getBoxNo());
							int d = conBoxDtlService.modifyById(cbDtl);
							if (d > 0) {
							} else {
								throw new ManagerException("更新箱码的入库单号为预到货通知单号时未更新到记录！");
							}
						}
					}
				}
			}

			// 修改操作
			for (ModelType modelType : uptList) {
				if (modelType instanceof BillImImportDtl) {
					BillImImportDtl vo = (BillImImportDtl) modelType;
					vo.setImportNo(importNo);
					vo.setLocno(locno);
					vo.setOwnerNo(ownerNo);
					int b = billImImportDtlService.modifyImImportDtl(vo);
					if (b < 1) {
						throw new ManagerException("修改预到货通知单明细信息时未更新到记录！");
					}
				}

			}

			// 删除操作
			for (ModelType modelType : delList) {
				if (modelType instanceof BillImImportDtl) {
					BillImImportDtl vo = (BillImImportDtl) modelType;

					Map<String, Object> queryParam = new HashMap<String, Object>();
					queryParam.put("locno", locno);
					queryParam.put("ownerNo", ownerNo);
					queryParam.put("boxNo", vo.getBoxNo());

					// 把箱表信息的入库单号清空
					ConBox conBox = new ConBox();
					conBox.setBoxNo(vo.getBoxNo());
					conBox.setLocno(locno);
					conBox.setOwnerNo(ownerNo);
					conBox.setsImportNo("9999");// 9999表示清空
					int b = conBoxService.modifyById(conBox);
					if (b < 1) {
						throw new ManagerException("把箱表信息的入库单号清空时未更新到记录！");
					} else {
						ConBoxDtl cbDtl = new ConBoxDtl();
						cbDtl.setImportNo("");
						cbDtl.setLocno(locno);
						cbDtl.setOwnerNo(ownerNo);
						cbDtl.setBoxNo(conBox.getBoxNo());
						int d = conBoxDtlService.modifyById(cbDtl);
						if (d > 0) {
						} else {
							throw new ManagerException("把箱表信息的入库单号清空时未更新到记录！");
						}
					}
					// System.out.println("======================把箱表信息的入库单号清空:"+b);

					// 删除预到货通知单明细信息，根据箱号
					BillImImportDtlKey delParamerKey = new BillImImportDtlKey();
					delParamerKey.setBoxNo(vo.getBoxNo());
					delParamerKey.setImportNo(importNo);
					delParamerKey.setLocno(locno);
					delParamerKey.setOwnerNo(ownerNo);
					int a = billImImportDtlService.deleteImImportDtl(delParamerKey);
					if (a < 1) {
						throw new ManagerException("根据箱号删除预到货通知单明细信息时未更新到记录！");
					}
					// System.out.println("======================删除预到货通知单明细信息，根据箱号:"+a);
				}
			}
			BillImImport billImImport = new BillImImport();
			billImImport.setImportNo(importNo);
			billImImport.setEditor(loginName);
			billImImport.setEditorName(username);
			billImImport.setEdittm(new Date());
			billImImport.setLocno(locno);
			billImImport.setOwnerNo(ownerNo);
			int result = billImImportService.modifyById(billImImport);
			if(result < 1){
				throw new ManagerException("主档操作人更新错误！");
			}
			flag = "true";
		} catch (Exception e) {
			throw new ManagerException(e);
		}
		mapObj.put("flag", flag);

		return mapObj;
	}

	@Override
	public Map<String, Object> selectBoxNoByDetailPageCount(int pageNo, int pageSize, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int total = billImImportDtlService.selectBoxNoByDetailCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			BillImImportDtlForPage dtlPage = new BillImImportDtlForPage();
			dtlPage.setImportNo(String.valueOf(params.get("importNo")));
			dtlPage.setLocno(String.valueOf(params.get("locno")));
			dtlPage.setOwnerNo(String.valueOf(params.get("ownerNo")));
			dtlPage.setStartRowNum(page.getStartRowNum());
			dtlPage.setEndRowNum(page.getEndRowNum());
			List<BillImImportDtl> list = billImImportDtlService.selectBoxNoByDetailPage(dtlPage, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public Map<String, Object> findDetailByImportNo(int pageNo, int pageSize, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			int total = billImImportDtlService.findDetailCountByImportNo(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImImportDtlDto> list = billImImportDtlService.findDetailByImportNo(page, params, authorityParams);

			Map<String, Object> boxParamsMap = new HashMap<String, Object>();
			String locno = params.get("locno").toString();
			String ownerNo = params.get("ownerNo").toString();
			boxParamsMap.put("locno", locno);
			boxParamsMap.put("ownerNo", ownerNo);

			List<BillImImportDtl> tempList;
			for (BillImImportDtlDto dtl : list) {
				boxParamsMap.put("boxNo", dtl.getBoxNo());
				List<ConBox> boxList = new ArrayList<ConBox>();
				tempList = billImImportDtlService.findByBiz(null, boxParamsMap);
				Set<String> hsdinit = new HashSet<String>();
				for (int i = 0; i < tempList.size(); i++) {
					ConBox conBox = new ConBox();
					BillImImportDtl iTempDtl = tempList.get(i);
					String iImportNo = iTempDtl.getImportNo();
					BigDecimal iQty = iTempDtl.getPoQty();

					if (hsdinit.contains(iImportNo)) {
						continue;
					} else {
						hsdinit.add(iImportNo);
					}
					for (int j = i + 1; j < tempList.size(); j++) {
						BillImImportDtl jTempDtl = tempList.get(j);
						String jImportNo = jTempDtl.getImportNo();
						BigDecimal jQty = jTempDtl.getPoQty();
						if (iImportNo.equals(jImportNo)) {
							iQty = iQty.add(jQty);
						}
					}
					conBox.setsImportNo(iImportNo);
					conBox.setQty(iQty);
					boxList.add(conBox);
				}
				dtl.setBoxList(boxList);
			}

			obj.put("total", total);
			obj.put("rows", list);
			return obj;
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectCountMx(BillImImportDtlSizeKind dto, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImImportDtlService.selectCountMx(dto, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListByImportNo(BillImImportDtlSizeKind dto)
			throws ManagerException {
		try {
			return billImImportDtlService.queryBillImImportDtlDTOlListByImportNo(dto);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillImImportDtlSizeKind> queryBillImImportDtlDTOlListGroupBy(SimplePage page,
			BillImImportDtlSizeKind dto, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billImImportDtlService.queryBillImImportDtlDTOlListGroupBy(page, dto, authorityParams);
		} catch (Exception e) {
			throw new ManagerException(e);
		}
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImImportDtlService.selectSumQty(map, authorityParams);
	}
}