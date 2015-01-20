package com.yougou.logistics.city.service;

import java.math.BigDecimal;
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
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.BillImReceiptKey;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConBoxDtl;
import com.yougou.logistics.city.common.model.ConBoxKey;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillImReceiptDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImReceiptMapper;
import com.yougou.logistics.city.dal.mapper.ConBoxMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Thu Oct 10 10:10:38 CST 2013
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
@Service("billOmPrepareDivideService")
class BillOmPrepareDivideServiceImpl extends BaseCrudServiceImpl implements BillOmPrepareDivideService {
	@Resource
	private BillImReceiptMapper billImReceiptMapper;

	@Resource
	private BillImReceiptDtlMapper billImReceiptDtlMapper;

    @Resource
    private BillAccControlService billAccControlService;
    
    @Resource
    private BmContainerService bmContainerService;
    
    @Resource
    private ItemMapper itemMapper;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private ConBoxDtlService conBoxDtlService;
	@Resource
	private ConBoxMapper conBoxMapper;

	private static final String STATUS10 = "10";
	private static final String STATUS11 = "11";
	private static final String BUSINESSTYPE1 = "1";

	@Override
	public BaseCrudMapper init() {
		return billImReceiptMapper;
	}

	@Override
	public void saveMain(BillImReceipt receipt) throws ServiceException {
		if (StringUtils.isEmpty(receipt.getReceiptNo())) {
			String receiptNo = procCommonService.procGetSheetNo(receipt.getLocno(), CNumPre.OM_RECEDE_PRE);
			receipt.setReceiptNo(receiptNo);
			receipt.setStatus(STATUS10);
			receipt.setBusinessType(BUSINESSTYPE1);
			billImReceiptMapper.insertSelective(receipt);
		} else {
			receipt.setCheckStatus(STATUS10);
			int count = billImReceiptMapper.updateByPrimaryKeySelective(receipt);
			if(count < 1){
				throw new ServiceException("单据"+receipt.getReceiptNo()+"已删除或状态已改变!");
			}
		}
	}

	private void addDtal(BillImReceipt receipt, BillImReceiptDtl dtl) throws ServiceException {
		try {
			//通过箱号查询箱库存
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("locno", receipt.getLocno());
			map.put("ownerNo", receipt.getOwnerNo());
			map.put("boxNo", dtl.getBoxNo());
			map.put("quality", dtl.getQuality());
			map.put("itemType", dtl.getItemType());

			List<BillImReceiptDtl> contentBoxList = billImReceiptDtlMapper.selectContentBox4Divide(map);

			BillImReceiptDtl receiptDtl = null;
			int rowId = 0;
			if (contentBoxList.size() > 0) {
				Integer maxRow = billImReceiptDtlMapper.selectMaxRowId(receipt);
				if (maxRow != null) {
					rowId = maxRow;
				}
			} else {
				throw new ServiceException("箱【" + dtl.getBoxNo() + "】库存不足，保存失败");
			}
			
			//查询箱明细
			Map<String, Object> boxDtlParam = new HashMap<String, Object>();
			boxDtlParam.put("locno", receipt.getLocno());
			boxDtlParam.put("ownerNo", receipt.getOwnerNo());
			boxDtlParam.put("boxNo", dtl.getBoxNo());
			boxDtlParam.put("isQty", "0");
			List<ConBoxDtl> boxDtlList = conBoxDtlService.findByParam(boxDtlParam);

			for (BillImReceiptDtl contentBox : contentBoxList) {
				if (contentBox.getCellId() == null) {
					String itemNo = contentBox.getItemNo();
					String sizeNo = contentBox.getSizeNo();
					throw new ServiceException("箱：" + dtl.getBoxNo() + 
							" 商品:" + itemNo + 
							" 尺码：" + sizeNo +
							" 库存不足，保存失败");
				}

				for (ConBoxDtl conBoxDtl : boxDtlList) {
					if (contentBox.getItemNo().equals(conBoxDtl.getItemNo())
							&& contentBox.getSizeNo().equals(conBoxDtl.getSizeNo())) {
						
						rowId++;
						receiptDtl = new BillImReceiptDtl();
						receiptDtl.setRowId(rowId);
						receiptDtl.setLocno(receipt.getLocno());
						receiptDtl.setOwnerNo(receipt.getOwnerNo());
						receiptDtl.setReceiptNo(receipt.getReceiptNo());
						receiptDtl.setBoxNo(contentBox.getBoxNo());
						receiptDtl.setStatus(STATUS10);
						receiptDtl.setItemNo(contentBox.getItemNo());
						receiptDtl.setSizeNo(contentBox.getSizeNo());
						receiptDtl.setPackQty(contentBox.getPackQty());
						receiptDtl.setReceiptQty(conBoxDtl.getQty());
						receiptDtl.setQuality(contentBox.getQuality());
						receiptDtl.setItemType(contentBox.getItemType());
						receiptDtl.setCellId(contentBox.getCellId());
						receiptDtl.setCellNo(contentBox.getCellNo());
						receiptDtl.setBrandNo(contentBox.getBrandNo());
						receiptDtl.setImportNo(conBoxDtl.getImportNo());

						billImReceiptDtlMapper.insertSelective(receiptDtl);

						//记账
						Item item = itemMapper.selectByCode(receiptDtl.getItemNo(), null);
						BillAccControlDto controlDto = new BillAccControlDto();
						controlDto.setiLocno(receiptDtl.getLocno());
						controlDto.setiOwnerNo(receiptDtl.getOwnerNo());
						controlDto.setiPaperNo(receiptDtl.getReceiptNo());
						controlDto.setiPaperType(CNumPre.IM_RECEDE_PRE);
						controlDto.setiIoFlag("I");
						controlDto.setiCreator(receipt.getEditor());
						controlDto.setiRowId(new BigDecimal(receiptDtl.getRowId()));
						controlDto.setiCellNo(receiptDtl.getCellNo());
						controlDto.setiCellId(receiptDtl.getCellId());
						controlDto.setiItemNo(receiptDtl.getItemNo());
						controlDto.setiSizeNo(receiptDtl.getSizeNo());
						controlDto.setiPackQty(receiptDtl.getPackQty());
						controlDto.setiSupplierNo(item.getSupplierNo());
						controlDto.setiOutstockQty(receiptDtl.getReceiptQty());
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
						dto.setiPaperNo(receiptDtl.getReceiptNo());
						dto.setiLocType("2");
						dto.setiPaperType(CNumPre.IM_RECEDE_PRE);
						dto.setiIoFlag("I");
						dto.setiPrepareDataExt(new BigDecimal(receiptDtl.getRowId()));
						dto.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dto);
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void delDtal(BillImReceipt receipt, BillImReceiptDtl dtl) throws ServiceException {
		try {
			//通过箱号查询遇到或通知单详情
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("locno", receipt.getLocno());
			map.put("boxNo", dtl.getBoxNo());
			map.put("receiptNo", receipt.getReceiptNo());
			map.put("ownerNo", receipt.getOwnerNo());
			List<BillImReceiptDtl> list = billImReceiptDtlMapper.selectByParams(null, map);
			for (BillImReceiptDtl recDtl : list) {
				Item item = itemMapper.selectByCode(recDtl.getItemNo(),null);//查询供应商
				BillAccControlDto controlDto = new BillAccControlDto();
				controlDto.setiLocno(recDtl.getLocno());
				controlDto.setiOwnerNo(recDtl.getOwnerNo());
				controlDto.setiPaperNo(recDtl.getReceiptNo());
				controlDto.setiPaperType(CNumPre.IM_RECEDE_PRE);
				controlDto.setiIoFlag("O");
				controlDto.setiCreator(receipt.getEditor());
				controlDto.setiRowId(new BigDecimal(recDtl.getRowId()));
				controlDto.setiCellNo(recDtl.getCellNo());
				controlDto.setiCellId(recDtl.getCellId());
				controlDto.setiItemNo(recDtl.getItemNo());
				controlDto.setiSizeNo(recDtl.getSizeNo());
				controlDto.setiPackQty(recDtl.getPackQty());
				controlDto.setiSupplierNo(item.getSupplierNo());
				controlDto.setiOutstockQty(new BigDecimal(0).subtract(recDtl.getReceiptQty()));
				
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
				dto.setiPaperNo(recDtl.getReceiptNo());
				dto.setiLocType("2");
				dto.setiPaperType(CNumPre.IM_RECEDE_PRE);
				dto.setiIoFlag("O");
				dto.setiPrepareDataExt(new BigDecimal(recDtl.getRowId()));
				dto.setiIsWeb(new BigDecimal(1));
				billAccControlService.procAccApply(dto);	
				
				//删除明细
				billImReceiptDtlMapper.deleteByPrimarayKeyForModel(recDtl);
			}
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public Map<String, Object> update(BillImReceipt receipt, List<BillImReceiptDtl> insert, List<BillImReceiptDtl> del) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> repeatList = new ArrayList<String>();
		try {
			
			//查询预分货单状态
			BillImReceiptKey receiptKey = new BillImReceiptKey();
			receiptKey.setLocno(receipt.getLocno());
			receiptKey.setOwnerNo(receipt.getOwnerNo());
			receiptKey.setReceiptNo(receipt.getReceiptNo());
			BillImReceipt imReceipt = (BillImReceipt)billImReceiptMapper.selectByPrimaryKey(receiptKey);
			if(imReceipt == null||!STATUS10.equals(imReceipt.getStatus())){
				throw new ServiceException("单据"+receipt.getReceiptNo()+"已删除或状态已改变!");
			}
//			if(!STATUS10.equals(imReceipt.getStatus())){
//				throw new ServiceException("只能保存建单状态的预分货单!");
//			}
			
			
			/**************删除明细增加解锁板号start*****************/
			//删除明细锁容器,有添加整板后,再删除整板的部分箱情况
			//如果整板的箱全部删除,需要解锁整板
			int containerCount = 0;
			List<String> delPanList = new ArrayList<String>();
			List<String> delBoxList = new ArrayList<String>();
			List<BmContainer> delPanContainerList = new ArrayList<BmContainer>();
			List<BmContainer> delBoxContainerList = new ArrayList<BmContainer>();
			for (BillImReceiptDtl dtl : del) {
				delDtal(receipt, dtl);
				
				//查询箱是否有板号
				ConBoxKey boxKey = new ConBoxKey();
				boxKey.setLocno(receipt.getLocno());
				boxKey.setOwnerNo(receipt.getOwnerNo());
				boxKey.setBoxNo(dtl.getBoxNo());
				ConBox conBox = (ConBox)conBoxMapper.selectByPrimaryKey(boxKey);
				if(conBox == null){
					throw new ServiceException("箱号"+dtl.getBoxNo()+"未在系统中找到!");
				}
				//如果板号为空,锁箱号,否则把整板都锁掉
				String panNo = conBox.getPanNo();
				String boxNo = conBox.getBoxNo();
				if(!delPanList.contains(panNo)&&StringUtils.isNotBlank(panNo)){
					delPanList.add(panNo);
				}
				if(!delBoxList.contains(boxNo)){
					delBoxList.add(boxNo);
				}
			}
			
			//解锁箱容器数据
			if(CommonUtil.hasValue(delBoxList)){
				for (String panNo : delBoxList) {
					BmContainer bmContainer = new BmContainer();
					bmContainer.setLocno(receipt.getLocno());
					bmContainer.setConNo(panNo);
					bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
					bmContainer.setFalg("Y");
					delBoxContainerList.add(bmContainer);
				}
				containerCount = bmContainerService.batchUpdate(delBoxContainerList);
				if(containerCount < 1){
					throw new ServiceException("解锁预分货单箱号容器失败!");
				}
			}
			
			//解锁板号容器,如果删除的数据存在板号进行解锁板号处理
			if(CommonUtil.hasValue(delPanList)){
				for (String panNo : delPanList) {
					BillImReceiptDtl receiptDtl = new BillImReceiptDtl();
					receiptDtl.setLocno(receipt.getLocno());
					receiptDtl.setOwnerNo(receipt.getOwnerNo());
					receiptDtl.setReceiptNo(receipt.getReceiptNo());
					receiptDtl.setPanNo(panNo);
					//验证预分货单是否存在板号的数据
					int count = billImReceiptDtlMapper.selectPanIsExist(receiptDtl);
					if(count < 1){
						BmContainer bmContainer = new BmContainer();
						bmContainer.setLocno(receipt.getLocno());
						bmContainer.setConNo(panNo);
						//如果该板号处于锁定状态,对板号进行解锁
						boolean result = bmContainerService.checkBmContainerStatus(bmContainer);
						if(result){
							bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
							bmContainer.setFalg("Y");
							delPanContainerList.add(bmContainer);
						}
					}
				}
				//板号执行解锁
				if(CommonUtil.hasValue(delPanContainerList)){
					containerCount = bmContainerService.batchUpdate(delPanContainerList);
					if(containerCount < 1){
						throw new ServiceException("解锁预分货单板号容器失败!");
					}
				}
			}
			/**************删除明细增加解锁板号end*****************/
			
			
			//删除子表  先做删除，防止删除了箱号，又添加相同的箱号
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("receiptNo", receipt.getReceiptNo());
			map.put("locno", receipt.getLocno());
			int count = billImReceiptDtlMapper.selectPrepareDivideDetailCount(map,null);
			SimplePage page = new SimplePage(1, count, count);
			List<BillImReceiptDtl> boxList = billImReceiptDtlMapper.selectPrepareDivideDetail(page, map,null);
			for (BillImReceiptDtl dtl : insert) {
				for (BillImReceiptDtl boxNo : boxList) {
					if (dtl.getBoxNo().equals(boxNo.getBoxNo())) {
						repeatList.add(dtl.getBoxNo());
						break;
					}
				}
			}
			if (repeatList.size() > 0) {
				new ServiceException();
				resultMap.put("repeat", repeatList);
				return resultMap;
			}
			
			/**************保存明细增加锁定板号start*****************/
			List<String> addList = new ArrayList<String>();
			for (BillImReceiptDtl dtl : insert) {
				addDtal(receipt, dtl);
				
				//查询箱是否有板号
				ConBoxKey boxKey = new ConBoxKey();
				boxKey.setLocno(receipt.getLocno());
				boxKey.setOwnerNo(receipt.getOwnerNo());
				boxKey.setBoxNo(dtl.getBoxNo());
				ConBox conBox = (ConBox)conBoxMapper.selectByPrimaryKey(boxKey);
				if(conBox == null){
					throw new ServiceException("箱号"+dtl.getBoxNo()+"未在系统中找到!");
				}
				//如果板号为空,锁箱号,否则把整板都锁掉
				String panNo = conBox.getPanNo();
				String boxNo = conBox.getBoxNo();
				if(!addList.contains(panNo)&&StringUtils.isNotBlank(panNo)){
					addList.add(panNo);
				}
				if(!addList.contains(boxNo)){
					addList.add(boxNo);
				}
			}
			
			//新增明细锁容器
			if(CommonUtil.hasValue(addList)){
				List<BmContainer> addContainerList = new ArrayList<BmContainer>();
				for (String panNo : addList) {
					
					//容器验证是否锁定
					BmContainer bmContainer = new BmContainer();
					bmContainer.setLocno(receipt.getLocno());
					bmContainer.setConNo(panNo);
					bmContainer.setStatus(ContainerStatusEnums.STATUS1.getContainerStatus());
					bmContainer.setOptBillNo(receipt.getReceiptNo());
					bmContainer.setOptBillType(ContainerTypeEnums.E.getOptBillType());
					boolean result = bmContainerService.checkBmContainerStatus(bmContainer);
					
					//如果板号处于锁定状态,而且板号中的箱不存在预分货单中
					BmContainer container = bmContainerService.findById(bmContainer);
					if(container == null){
						throw new ServiceException("容器号:"+panNo+"不存在容器表中!");
					}
					
					//验证箱容器
					String containerType = container.getType();
					if(ContainerTypeEnums.C.getOptBillType().equals(containerType)){
						if(result){
							throw new ServiceException("容器号:"+panNo+"不存在或已锁定!");
						}
					}
					//验证板号容器,板号容器在同一个预分货单可以加存在板的箱号
					if(ContainerTypeEnums.P.getOptBillType().equals(containerType)){
						//验证预分货单是否存在板号的数据
						BillImReceiptDtl receiptDtl = new BillImReceiptDtl();
						receiptDtl.setLocno(receipt.getLocno());
						receiptDtl.setOwnerNo(receipt.getOwnerNo());
						receiptDtl.setReceiptNo(receipt.getReceiptNo());
						receiptDtl.setPanNo(panNo);
						int existCount = billImReceiptDtlMapper.selectPanIsExistLock(receiptDtl); 
						if(result&&existCount>0){
							throw new ServiceException("容器号:"+panNo+"不存在或已锁定!");
						}
					}
					if(!result){
						addContainerList.add(bmContainer);
					}
				}
				containerCount = bmContainerService.batchUpdate(addContainerList);
				if(containerCount < 1){
					throw new ServiceException("锁定预分货单容器失败!");
				}
			}
			/**************保存明细增加锁定板号end*****************/

		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return resultMap;
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findMainReciptCount(Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectMainReciptCount(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillImReceipt> findMainRecipt(SimplePage page, Map<?, ?> map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectMainRecipt(page, map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int auditReceipt(BillImReceipt receipt) throws ServiceException {
		try {			
			return billImReceiptMapper.updateByPrimaryKeySelective(receipt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBoxNo4DivideCount(Map map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectBoxNo4DivideCount(map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<?> findBoxNo4Divide(SimplePage page, Map map,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImReceiptMapper.selectBoxNo4Divide(page, map,authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void overPrepareDivide(List<BillImReceipt> receiptList,String userName) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(receiptList)){
				throw new ServiceException("选择的预分货单数据为空!");
			}
			for (BillImReceipt billImReceipt : receiptList) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("I_locno", billImReceipt.getLocno());
				params.put("I_ownerNo", billImReceipt.getOwnerNo());
				params.put("I_receiptNo", billImReceipt.getReceiptNo());
				params.put("I_creator", userName);
				billImReceiptMapper.procOmCancelPreparedivide(params);
				String strOutMsg = params.get("O_msg");
				if (!"Y|".equals(strOutMsg)) {
					String[] msgs = strOutMsg.split("\\|");
					throw new ServiceException(msgs[1]);
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
}