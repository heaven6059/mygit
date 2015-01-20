package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto;
import com.yougou.logistics.city.common.dto.BillImInstockDtlDto2;
import com.yougou.logistics.city.common.model.BillImInstock;
import com.yougou.logistics.city.common.model.BillImInstockDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.ConContentMapper;
import com.yougou.logistics.city.dal.mapper.BillImInstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;

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
@Service("billImInstockDtlService")
class BillImInstockDtlServiceImpl extends BaseCrudServiceImpl implements BillImInstockDtlService {
	@Resource
	private BillImInstockDtlMapper billImInstockDtlMapper;
	
	@Resource
	private BillImInstockService  billImInstockService;

	@Resource
	private CmDefcellMapper cmDefcellMapper;

	@Resource
	private CmDefcellService cmDefcellService;

	@Resource
	private ConContentMapper conContentMapper;

	@Override
	public BaseCrudMapper init() {
		return billImInstockDtlMapper;
	}

	private static final String STAUTS0 = "0";

	private static final String ATTRIBUTE_TYPE0 = "0"; //0：存储区1:进货；2:出货整理；3:出货复核；4:出货滑道；5:发货；6:退货；7:报损；8：直通；9：电子标签

	private static final String AREA_USETYPE1 = "1", AREA_USETYPE5 = "5";// 储区用途 1:普通存储区；2：报损区；3：退货区；5：异常区；6:贵重品区;7-库存调整区:8 分货区

	private static final String AREA_ATTRIBUTE0 = "0"; //0：作业区；1：暂存区；2;已配送区；3:问题区；4：虚拟区。选择作业区时，暂存区性质不可见，选择暂存区时，储区性质不可见；问题区、虚拟区和已配送区时，两个都不可见。移库时，作业区内可以互移，暂存区不可移入不同性质的暂存区，作业区和暂存区都不可移到已配送区；暂存区可往作业区移，作业区不可移入暂存区；作业区和问题区可互移。

	private static final short MIX_FLAG0 = 0;//0:不可混；1：同商品不同属性混；2：不同商品混
	
	private static final String STATUS13="13";

	@Override
	public int findItemCountBroupByItemNo(BillImInstockDtl dtl) throws ServiceException {
		try {
			return billImInstockDtlMapper.selectItemCountBroupByItemNo(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImInstockDtl> findItemGroupByItemNo(SimplePage page, BillImInstockDtl dtl) throws ServiceException {
		try {
			return billImInstockDtlMapper.selectItemGroupByItemNo(page, dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImInstockDtlDto2> findDetailByParams(BillImInstockDtl dtl) throws ServiceException {
		try {
			return billImInstockDtlMapper.selectDetailByParams(dtl);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public String findSysNo(BillImInstockDtlDto dto) throws ServiceException {
		try {
			return billImInstockDtlMapper.selectSysNo(dto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public BillImInstockDtlDto splitById(BillImInstockDtlDto dto) throws ServiceException {
		try {
			BillImInstock instock = new BillImInstock();
			instock.setLocno(dto.getLocno());
			instock.setOwnerNo(dto.getOwnerNo());
			instock.setInstockNo(dto.getInstockNo());
			BillImInstock instockResult = billImInstockService.findById(instock);
			if (STATUS13.equals(instockResult.getStatus())) {
				throw new ServiceException("单据"+instock.getInstockNo()+"状态已改变,不能进行拆分操作！");
			}
			
			// 查询明细
			// 只能拆分源数据
			BillImInstockDtlDto dtltemp = billImInstockDtlMapper.selectDtoByPrimaryKey(dto);
			if (StringUtils.isEmpty(dtltemp.getRealCellNo())) {
				throw new ServiceException("源数据实际上架储位为空不能拆分");
			}
			if (dtltemp.getRealQty() == null || dtltemp.getRealQty().intValue() == 0) {
				throw new ServiceException("源数据实际上架数量为空不能拆分");
			}
			if (dto.getItemQty().intValue() == 0) {
				throw new ServiceException("已经拆分的不能再拆分");
			}
			if (dto.getRealQty() == null) {
				throw new ServiceException("实际上架数量不能为空");
			}
			if (dto.getRealQty().intValue() == 0) {
				throw new ServiceException("实际上架数量不能为0");
			}

			/*// 判断实际储位是否存在
			Map<String, Object> parmMap = new HashMap<String, Object>();
			parmMap.put("cellStatus", STAUTS0);
			parmMap.put("checkStatus", STAUTS0);
			parmMap.put("cellNo", dto.getRealCellNo());
			int count = this.cmDefcellMapper.selectCount(parmMap, null);
			if (count <= 0) {
				throw new ServiceException("实际储位不存在");
			}*/
			checkRealCellNo(dto);
			// 查询可用数量
			int leftQty = billImInstockDtlMapper.selectLeftQty(dto);
			if (leftQty < dto.getRealQty().intValue()) {
				throw new ServiceException("该商品可上架数量必须大于等于拆分数量才能拆分");
			}
			// 查询最大的stockId
			int maxId = billImInstockDtlMapper.selectMaxInsotckId(dto);
			dtltemp.setRealQty(dto.getRealQty());
			dtltemp.setItemQty(new BigDecimal(0));
			dtltemp.setInstockId(String.valueOf(maxId + 1));
			dtltemp.setInstockedQty(new BigDecimal(0));
			dtltemp.setRealCellNo(dto.getRealCellNo());
			dtltemp.setEditor(dto.getEditor());
			dtltemp.setEditorName(dto.getEditorName());
			billImInstockDtlMapper.insertSelective(dtltemp);
			// 判断实际上架储位是否已经存在
			List<BillImInstockDtl> repeatList = billImInstockDtlMapper.selectRepeatRealCell(dto);
			if (repeatList != null && repeatList.size() > 0) {
				throw new ServiceException("该商品实际上架储位【" + dto.getRealCellNo() + "】已经存在");
			}
			dtltemp.setColorName(dto.getColorName());
			dtltemp.setItemName(dto.getItemName());
			return dtltemp;
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteSingle(BillImInstockDtlDto dto) throws ServiceException {
		try {
			BillImInstock instock = new BillImInstock();
			instock.setLocno(dto.getLocno());
			instock.setOwnerNo(dto.getOwnerNo());
			instock.setInstockNo(dto.getInstockNo());
			BillImInstock instockResult = billImInstockService.findById(instock);
			if (STATUS13.equals(instockResult.getStatus())) {
				throw new ServiceException("单据"+instock.getInstockNo()+"状态已改变,不能进行删除操作！");
			}
			
			BillImInstockDtl dtl = this.findById(dto);
			if (dtl.getItemQty().intValue() > 0) {
				throw new ServiceException("此行是源数据,不能删除！");
			}
			this.deleteById(dto);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveSingle(BillImInstockDtl dtl) throws ServiceException {
		try {
			BillImInstock instock = new BillImInstock();
			instock.setLocno(dtl.getLocno());
			instock.setOwnerNo(dtl.getOwnerNo());
			instock.setInstockNo(dtl.getInstockNo());
			BillImInstock instockResult = billImInstockService.findById(instock);
			if (STATUS13.equals(instockResult.getStatus())){
				throw new ServiceException("单据"+instock.getInstockNo()+"状态已改变,不能进行单行保存操作！");
			}
			
			checkRealCellNo(dtl);
			// 查询明细
			if (dtl.getRealQty().intValue() == 0) {
				throw new ServiceException("实际上架数量不能为0");
			}
			if (dtl.getInstockedQty() != null && dtl.getRealQty().intValue() < dtl.getInstockedQty().intValue()) {
				throw new ServiceException("实际上架数量不能小于已上架数量");
			}
			// 更新数据
			billImInstockDtlMapper.updateByPrimaryKeySelective(dtl);
			// 查询可用数量
			int leftQty = billImInstockDtlMapper.selectLeftQty(dtl);
			if (leftQty < 0) {
				throw new ServiceException("实际上架数量必须小于或等于可上架数量");
			}
			// 判断实际储位是否存在
			/*Map<String, Object> parmMap = new HashMap<String, Object>();
			parmMap.put("cellStatus", STAUTS0);
			parmMap.put("checkStatus", STAUTS0);
			parmMap.put("cellNo", dtl.getRealCellNo());
			int count = this.cmDefcellMapper.selectCount(parmMap, null);
			if (count <= 0) {
				throw new ServiceException("实际储位不存在");
			}*/
			checkRealCellNo(dtl);
			// 判断实际上架储位是否已经存在
			List<BillImInstockDtl> repeatList = billImInstockDtlMapper.selectRepeatRealCell(dtl);
			if (repeatList != null && repeatList.size() > 0) {
				throw new ServiceException("实际上架储位【" + dtl.getRealCellNo() + "】已经存在");
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	private void checkRealCellNo(BillImInstockDtl dtl) throws ServiceException {
		/**
		 * 1.判断储位是否存在。

			2.判断储位状态是否可用，是否非盘点状态。
			
			3.存储区：入库：储位必须是存储作业区；储位商品属性与预上库存相同；储位的品质与预上库存相同；
			
			   退仓：储位必须是退货作业区；储位商品属性与预上库存相同；储位的品质与预上库存相同；
			
			   异常区：不判断储位商品属性与预上库存相同；不判断储位的品质与预上库存相同
			
			4.如果储位是不可混的，要判断空储位或者储位上的商品与预上商品相同；
			
			   如果储位是可混的，则不用判断是否有库存或库存商品与预上是否一致。
		 */
		//判断实际储位是否存在  判断储位状态是否可用，是否非盘点状态   是否是存储作业区
		Map<String, Object> parmMap = new HashMap<String, Object>();
		parmMap.put("locno", dtl.getLocno());
		parmMap.put("cellNo", dtl.getRealCellNo());
		List<CmDefcell> list = this.cmDefcellMapper.selectByParams4Instock(parmMap);
		if (list == null || list.size() == 0) {
			throw new ServiceException("实际储位不存在");
		}
		if (list.size() > 1) {
			throw new ServiceException("数据非法");
		}
		CmDefcell cell = list.get(0);
		if (!STAUTS0.equals(cell.getCellStatus())) {
			throw new ServiceException("实际储位状态必须为可用");
		}
		if (!STAUTS0.equals(cell.getCheckStatus())) {
			throw new ServiceException("实际储位必须是非盘点状态");
		}
		if (!ATTRIBUTE_TYPE0.equals(cell.getAttributeType())) {
			throw new ServiceException("实际储位库区属性类型必须为存储区");
		}
		if (!AREA_ATTRIBUTE0.equals(cell.getAreaAttribute())) {
			throw new ServiceException("实际储位库区属性必须为作业区");
		}
		if (AREA_USETYPE1.equals(cell.getAreaUsetype()) || AREA_USETYPE5.equals(cell.getAreaUsetype())) {//存储作业区 
			if (AREA_USETYPE1.equals(cell.getAreaUsetype())) {
				if (!cell.getItemType().equals(dtl.getItemType())) {
					throw new ServiceException("实际储位商品属性与预上库存不相同");
				}
				if (!cell.getAreaQuality().equals(dtl.getQuality())) {
					throw new ServiceException("实际储位商品品质与预上库存不相同");
				}
			}
		} else {
			throw new ServiceException("实际储位库区必须是存储作业区或者异常区");
		}

		/*if (list == null || list.size() == 0) {
			//判断异常区
			parmMap.put("areaUsetypeArray", AREA_USETYPE5); //储区用途    存储作业区
			list = this.cmDefcellMapper.selectByParams4Instock(parmMap);
			if (list == null || list.size() != 1) {
				throw new ServiceException("实际储位不可用");
			}
		} else if (list.size() == 1) { //判断商品属性预预上是否相同。
			CmDefcell cell = list.get(0);
			if (!cell.getAreaQuality().equals(dtl.getQuality()) || !cell.getItemType().equals(dtl.getItemType())) {
				throw new ServiceException("实际储位不可用");
			}
		} else {
			throw new ServiceException("实际储位不可用");
		}*/
		//查询实际储位库存
		Map<String, Object> conMap = new HashMap<String, Object>();
		conMap.put("locno", dtl.getLocno());
		conMap.put("cellNo", dtl.getCellNo());
		List<ConContent> conList = conContentMapper.selectByParams(null, conMap);
		boolean isOk = false;
		if (conList != null && conList.size() >= 1) {
			if (cell.getMixFlag().shortValue() == MIX_FLAG0) {//不可混
				for (ConContent content : conList) {
					if (content.getItemNo().equals(dtl.getItemNo())) { //只要有一个商品跟明细商品相同，储位就合法
						isOk = true;
						break;
					}
				}
				if (!isOk) {
					throw new ServiceException("实际储位不可用");
				}
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveBatch(List<BillImInstockDtlDto> list) throws ServiceException {
		try {
			for (BillImInstockDtl dtl : list) {
				// 判断实际储位是否存在
				Map<String, Object> parmMap = new HashMap<String, Object>();
				parmMap.put("cellStatus", STAUTS0);
				parmMap.put("checkStatus", STAUTS0);
				parmMap.put("cellNo", dtl.getRealCellNo());
				parmMap.put("attributeType", ATTRIBUTE_TYPE0);
				parmMap.put("itemType", dtl.getItemType());
				parmMap.put("areaQuality", dtl.getQuality());
				int count = this.cmDefcellService.findCount(parmMap);
				if (count <= 0) {
					throw new ServiceException("实际储位【" + dtl.getRealCellNo() + "】不存在,或者不是存储区,或者商品属性跟库存属性不一致");
				}
				if (StringUtils.isEmpty(dtl.getInstockId())) {// 拆分的行
					int maxId = billImInstockDtlMapper.selectMaxInsotckId(dtl);
					dtl.setInstockId(String.valueOf(maxId + 1));
					billImInstockDtlMapper.insertSelective(dtl);
				} else { // 更新行
					this.billImInstockDtlMapper.updateByPrimaryKeySelective(dtl);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) {
		return billImInstockDtlMapper.selectSumQty(map, authorityParams);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveByPlan(BillImInstock instock) throws ServiceException {
		try {
			BillImInstock instockResult = billImInstockService.findById(instock);
			if (STATUS13.equals(instockResult.getStatus())){
				throw new ServiceException("单据"+instock.getInstockNo()+"状态已改变,不能进行按计划保存操作！");
			}
			
			int count = billImInstockDtlMapper.saveByPlan(instock);
			if (count == 0) {
				throw new ServiceException("此单据有部分上架,不能按计划保存");
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<BillImInstockDtlDto> selectPrintInf(BillImInstock instock) {
		return billImInstockDtlMapper.selectPrintInf(instock);
	}

}