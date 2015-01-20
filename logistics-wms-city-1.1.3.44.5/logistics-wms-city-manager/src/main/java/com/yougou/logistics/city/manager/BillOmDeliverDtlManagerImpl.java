package com.yougou.logistics.city.manager;

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

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlKey;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlSizeDto;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConLabel;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillOmDeliverDtlService;
import com.yougou.logistics.city.service.BillOmDeliverService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ConLabelDtlService;
import com.yougou.logistics.city.service.ConLabelService;

/**
 * 
 * 装车单详情manager
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:26:33
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDeliverDtlManager")
class BillOmDeliverDtlManagerImpl extends BaseCrudManagerImpl implements BillOmDeliverDtlManager {
	
    @Resource
    private BillOmDeliverDtlService billOmDeliverDtlService;
    
    @Resource
    private BillOmDeliverService billOmDeliverService;
    
    @Resource
    private ConLabelDtlService  conLabelDtlService;
    
    @Resource
    private ConLabelService conLabelService;

    @Resource
    private ConBoxService conBoxService;
    
    @Resource
    private BmContainerService bmContainerService;
    
    private static final  String  STATUS10 = "10";
    
    @Override
    public BaseCrudService init() {
        return billOmDeliverDtlService;
    }

    
	/**
	 * 添加装车明细
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public <ModelType> void addBillOmDeliverDtl(String locno, String deliverNo, String ownerNo,
			Map<CommonOperatorEnum, List<ModelType>> params, SystemUser user, String transFlag,AuthorityParams authorityParams)
			throws ManagerException {
		//boolean isOk = true;
		Date date = new Date();
		List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);
		List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);
		
		BillOmDeliver  bobj = new  BillOmDeliver();
		bobj.setLocno(locno);
		bobj.setDeliverNo(deliverNo);
		BillOmDeliver singojb = null;
		try {
			singojb = billOmDeliverService.findById(bobj);
			if(null == singojb || !STATUS10.equals(singojb.getStatus())){
				throw new ManagerException("单据"+deliverNo+"不为新建状态，不能添加明细！");
			}
		} catch (ServiceException e1) {
			throw new ManagerException(e1);
		}
		
		//容器记账，锁定箱子  占用，status=1，optBillNo，optBillType都要传
		List<BmContainer> lstContainer = new  ArrayList<BmContainer>();
		Map<String,String> returnMap = new HashMap<String,String>();
		
		//新增操作
		for(ModelType modelType : addList){
			try {
				//转换成对象
				BillOmDeliverDtl vo = (BillOmDeliverDtl)modelType;
				String containerNo = vo.getContainerNo().trim();
				String labelNo = vo.getBoxNo().trim();
				String storeNo = vo.getStoreNo().trim();
				String expNo = "";
				
				BillOmDeliverDtlKey keyObj = new BillOmDeliverDtlKey();
				keyObj.setLocno(locno);
				keyObj.setDeliverNo(deliverNo);
				keyObj.setOwnerNo(ownerNo);
				keyObj.setContainerNo(containerNo);
				//检查本装车单信息对否重复
				int isDeliverDtl = billOmDeliverDtlService.selectDeliverDtl(keyObj);
				if(isDeliverDtl > 0) {
				} else {
					//查询复核单信息
					Map<String,Object> param = new HashMap<String, Object>();
					param.put("locno", locno);
					param.put("containerNo", containerNo);
					param.put("labelNo", labelNo);
					param.put("storeNo", storeNo);
					param.put("expNo", expNo);
					List<BillOmDeliverDtl> add = null;
					if(transFlag.equals("10")) {
						add = billOmDeliverDtlService.selectLoadproposeDetail(param, authorityParams);
					} else {
						add = billOmDeliverDtlService.selectBillOmRecheckDtl(param, authorityParams);
					}
					if(add != null && add.size() > 0) {
						//查询最大ID
						int num = billOmDeliverDtlService.selectMaxNum(keyObj);
						for(int j = 0; j <add.size(); j++) {
							BillOmDeliverDtl deliverDtl = new BillOmDeliverDtl();
							deliverDtl.setLocno(keyObj.getLocno());
							deliverDtl.setDeliverNo(keyObj.getDeliverNo());
							deliverDtl.setOwnerNo(keyObj.getOwnerNo());
							deliverDtl.setContainerNo(keyObj.getContainerNo());
							deliverDtl.setContainerId(++num);
							
							String itemNo = add.get(j).getItemNo();
							String sizeNo = add.get(j).getSizeNo();
							BigDecimal itemQty = add.get(j).getQty();
							
							deliverDtl.setItemNo(itemNo);
							deliverDtl.setBoxNo(labelNo);
							deliverDtl.setStoreNo(storeNo);
							deliverDtl.setQty(itemQty);
							deliverDtl.setItemType("0");
							deliverDtl.setSizeNo(sizeNo);
							deliverDtl.setExpDate(date);
							deliverDtl.setExpNo(add.get(j).getExpNo());
							deliverDtl.setExpType(add.get(j).getExpType());
							deliverDtl.setBarcode(add.get(j).getBarcode());
							deliverDtl.setBrandNo(add.get(j).getBrandNo());
							deliverDtl.setSupplierNo(add.get(j).getSupplierNo());
							deliverDtl.setCreator(user.getLoginName());
							deliverDtl.setCreatorname(user.getUsername());
							deliverDtl.setCreatetm(date);
							deliverDtl.setEditor(user.getLoginName());
							deliverDtl.setEditorname(user.getUsername());
							deliverDtl.setEdittm(date);
							
							//装车出库计划添加计划单号
							if(transFlag.equals("10")) {
								deliverDtl.setLoadproposeNo(add.get(j).getLoadproposeNo());
							}
							int count = billOmDeliverDtlService.add(deliverDtl);
							if(count<1) {
								throw new ManagerException("新增时未更新记录！");
							}
						}
					} else {
						throw new ManagerException("未找到可保存记录！");
					}
				}
				
				//更改标签状态
				ConLabel  conLabel = new ConLabel();
				conLabel.setStatus("A1");//18)	标签状态为A0 (待装车)
				conLabel.setLocno(locno);
				conLabel.setScanLabelNo(labelNo);
				int a = conLabelService.modifyStatusByLocnoAndLabelNo(conLabel);
				if(a < 1){
					throw new ManagerException("未更新到标签号【"+labelNo+"】的状态！");
				}
				
				ConLabelDtl  conLabelDtl = new  ConLabelDtl();
				conLabelDtl.setStatus("A1");//标签状态为6E（外复核完成）
				conLabelDtl.setLocno(locno);
				conLabelDtl.setScanLabelNo(labelNo);
				int c = conLabelDtlService.modifyLabelDtlStatusByLabelNo(conLabelDtl);
				if(c < 1){
					throw new ManagerException("未更新到标签明细【"+labelNo+"】的状态！");
				}
				
				ConBox conBox = new ConBox();
				conBox.setStatus("5");
				conBox.setLocno(locno);
				conBox.setOwnerNo(ownerNo);
				conBox.setBoxNo(labelNo);
				int b1 = conBoxService.modifyById(conBox);
				if(b1 < 1){
					throw new ManagerException("未更新到箱号【"+labelNo+"】的状态！");
				}
				
				//容器验证是否锁定
				BmContainer bc = new BmContainer();
				bc.setLocno(locno);
				bc.setConNo(labelNo);
				bc.setStatus(ContainerStatusEnums.STATUS1.getContainerStatus());
				bc.setOptBillNo(deliverNo);
				bc.setOptBillType(ContainerTypeEnums.OD.getOptBillType());
				boolean result = bmContainerService.checkBmContainerStatus(bc);
				if(result){
					throw new ManagerException("容器号:"+labelNo+"不存在或已锁定!");
				}
				
				//添加锁定的容器
				if(!returnMap.containsKey(labelNo)){
					bc.setEdittm(DateUtil.getCurrentDateTime());
					bc.setEditor(user.getLoginName());
					lstContainer.add(bc);
					returnMap.put(labelNo, labelNo);
				}
				
			} catch (ServiceException e) {
				throw new ManagerException(e);
			}
		}
		
		//锁定容器号
		if(CommonUtil.hasValue(lstContainer)){
			try{
				bmContainerService.batchUpdate(lstContainer);
				lstContainer.clear();
				returnMap.clear();
			} catch (ServiceException e) {
				throw new ManagerException(e);
			}
		}
		
		//删除操作
		for(ModelType modelType : delList){
			try {
				if(modelType instanceof  BillOmDeliverDtl){
					//转换成对象
					BillOmDeliverDtl vo = (BillOmDeliverDtl)modelType;
					String containerNo = vo.getContainerNo().trim();
					String labelNo = vo.getBoxNo().trim();
//					String storeNo = vo.getStoreNo().trim();
//					String expNo = vo.getExpNo().trim();
					
					BillOmDeliverDtlKey keyObj = new BillOmDeliverDtlKey();
					keyObj.setLocno(locno);
					keyObj.setDeliverNo(deliverNo);
					keyObj.setOwnerNo(ownerNo);
					keyObj.setContainerNo(containerNo);
					
					int b = billOmDeliverDtlService.deleteByPrimaryKey(keyObj);				
					if(b < 1){
						 throw new ManagerException("删除时未更新到记录！");
					}
					
					ConLabel  conLabel = new  ConLabel();
					conLabel.setStatus("A0");//标签状态为6E（外复核完成）
					conLabel.setLocno(locno);
					conLabel.setScanLabelNo(labelNo);
					int a = conLabelService.modifyStatusByLocnoAndLabelNo(conLabel);
					if(a < 1){
						throw new ManagerException("未更新到标签号【"+labelNo+"】的状态！");
					}
					
					ConLabelDtl  conLabelDtl = new  ConLabelDtl();
					conLabelDtl.setStatus("A0");//标签状态为6E（外复核完成）
					conLabelDtl.setLocno(locno);
					conLabelDtl.setScanLabelNo(labelNo);
					int c = conLabelDtlService.modifyLabelDtlStatusByLabelNo(conLabelDtl);
					if(c < 1){
						throw new ManagerException("未更新到标签明细【"+labelNo+"】的状态！");
					}
					
					ConBox conBox = new ConBox();
					conBox.setStatus("2");
					conBox.setLocno(locno);
					conBox.setOwnerNo(ownerNo);
					conBox.setBoxNo(labelNo);
					int b1 = conBoxService.modifyById(conBox);
					if(b1 < 1){
						throw new ManagerException("未更新到箱号【"+labelNo+"】的状态！");
					}
					
					//添加锁定的容器
					if(!returnMap.containsKey(labelNo)){
						BmContainer  bc = new BmContainer();
						bc.setLocno(locno);
						bc.setConNo(labelNo);
						bc.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
						bc.setFalg("Y");
						lstContainer.add(bc);
						returnMap.put(labelNo, labelNo);
					}
				}
			} catch (ServiceException e) {
				throw new ManagerException(e);
			} 
		}
		
		//解锁容器号
		if (CommonUtil.hasValue(lstContainer)) {
			try {
				bmContainerService.batchUpdate(lstContainer);
				lstContainer.clear();
				returnMap.clear();
			} catch (ServiceException e) {
				throw new ManagerException(e);
			}
		}
		
	}
	
    
	@Override
	public int boxSelectCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.boxSelectCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<BillOmDeliverDtl> boxSelectQuery(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.boxSelectQuery(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillOmDeliverDtl> boxDtlByParams(BillOmDeliverDtl modelType, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.boxDtlByParams(modelType, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public int flagSelectCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.flagSelectCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillOmDeliverDtl> flagSelectQuery(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.flagSelectQuery(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public List<BillOmDeliverDtl> flagDtlByParams(BillOmDeliverDtl modelType, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.flagDtlByParams(modelType, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
	
	@Override
	public int selectBoxCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billOmDeliverDtlService.selectBoxCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<BillOmDeliverDtl> selectBoxStore(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.selectBoxStore(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int selectFlagCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billOmDeliverDtlService.selectFlagCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<BillOmDeliverDtl> selectFlagStore(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverDtlService.selectFlagStore(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findDeliverDtlBoxCount(Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverDtlService.findDeliverDtlBoxCount(params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmDeliverDtl> findDeliverDtlBoxByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverDtlService.findDeliverDtlBoxByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverDtlService.selectSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public int findLoadproposeDeliverDtlBoxCount(Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverDtlService.findLoadproposeDeliverDtlBoxCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmDeliverDtl> findLoadproposeDeliverDtlBoxByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverDtlService.findLoadproposeDeliverDtlBoxByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}


	@Override
	public List<BillOmDeliverDtlSizeDto> findDtl4SizeHorizontal(String deliverNo) {
		return billOmDeliverDtlService.findDtl4SizeHorizontal(deliverNo);
	}
	
}