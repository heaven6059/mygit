package com.yougou.logistics.city.service;

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

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;
import com.yougou.logistics.city.common.model.BillImCheck;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.mapper.BillImCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillImCheckMapper;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;
import com.yougou.logistics.city.dal.mapper.ProcCommonMapper;

/**
 * 
 * 收货验收单service实现
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:15:56
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billImCheckService")
class BillImCheckServiceImpl extends BaseCrudServiceImpl implements BillImCheckService {
	@Resource
	private BillImCheckMapper billImCheckMapper;
	
	@Resource
	private BillImCheckDtlMapper billImCheckDtlMapper;

	@Resource
	private ProcCommonMapper procCommonMapper;
	
	@Resource
	private BmContainerService bmContainerService;
	
	@Resource
	private BillContainerTaskService billContainerTaskService;
	/*	@Resource
		private BillImCheckDtlMapper billImCheckDtlMapper;
		@Resource
		private BillImReceiptMapper billImReceiptMapper;
		@Resource
		private BillImReceiptDtlMapper billImReceiptDtlMapper;
		@Resource
		private BillImImportMapper billImImportMapper;
		@Resource
		private BillImImportDtlMapper billImImportDtlMapper;
		@Resource
		private BillImCheckPalMapper billImCheckPalMapper;
		@Resource
		private ItemBarcodeMapper itemBarcodeMapper;
		@Resource
		private ProcCommonMapper procCommonMapper;
		@Resource
		private ConItemInfoMapper conItemInfoMapper;*/
	@Resource
	private ProcCommonService procCommonService;
	
	@Resource
	private CmDefcellMapper cmDefcellMapper;

	private final static String RESULTY = "Y";
	
	private final static String BUSINESSTYPE0 = "0";
	private final static String BUSINESSTYPE1 = "1";
	private final static String BUSINESSTYPE2 = "2";
	
	private final static String ISWHOLE0 = "0";
	private final static String ISWHOLE1 = "1";
	private final static String ISWHOLE2 = "2";
	
	private final static int STATUS10=10;

	@Override
	public BaseCrudMapper init() {
		return billImCheckMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void check(List<BillImCheck> checkList, String userId,String username) throws ServiceException {
		try {
			if(!CommonUtil.hasValue(checkList)){
				throw new ServiceException("参数非法!");
			}
			for (BillImCheck b : checkList) {
				
				BillImCheck obj = billImCheckMapper.selectByPrimaryKey(b);
				if(null==obj || STATUS10 != obj.getStatus().intValue()){
					throw new ServiceException("单据"+b.getCheckNo()+"已删除或状态已改变，不能进行审核操作！");
				}
				
				//整箱验收处理解锁
				if(BUSINESSTYPE0.equals(obj.getBusinessType()) || BUSINESSTYPE2.equals(obj.getBusinessType())){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", b.getLocno());
					params.put("ownerNo", b.getOwnerNo());
					params.put("checkNo", b.getCheckNo());
					List<BillImCheckDtl> checkDtlList = billImCheckDtlMapper.selectByParams(null, params);
					if(!CommonUtil.hasValue(checkDtlList)){
						throw new ServiceException("单据"+b.getCheckNo()+"查询验收单明细失败！");
					}
					
					List<String> delPanList = new ArrayList<String>();
					List<BmContainer> delPanContainerList = new ArrayList<BmContainer>();
					List<BillImCheckDtl> addCheckDtlList = new ArrayList<BillImCheckDtl>();
					for (BillImCheckDtl checkDtl : checkDtlList) {
						//如果整板验收，整板的容器号解锁
						if(ISWHOLE2.equals(checkDtl.getIswhole())){
							//如果板号为空,锁箱号,否则把整板都锁掉
							String panNo = checkDtl.getPanNo();
							String boxNo = checkDtl.getBoxNo();
							if(!delPanList.contains(panNo)&&StringUtils.isNotBlank(panNo)){
								delPanList.add(panNo);
							}
							if(!delPanList.contains(boxNo)){
								delPanList.add(boxNo);
							}
						}else{
							addCheckDtlList.add(checkDtl);
						}
					}
					
					//解锁板号容器
					//1.解锁整板
					int containerCount = 0;
					if(CommonUtil.hasValue(delPanList)){
						for (String panNo : delPanList) {
							BmContainer bmContainer = new BmContainer();
							bmContainer.setLocno(b.getLocno());
							bmContainer.setConNo(panNo);
							bmContainer.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
							bmContainer.setFalg("Y");
							delPanContainerList.add(bmContainer);
						}
						if(CommonUtil.hasValue(delPanContainerList)){
							containerCount = bmContainerService.batchUpdate(delPanContainerList);
							if(containerCount < 1){
								throw new ServiceException("解锁验收单板号容器失败!");
							}
						}
					}
					
					
					//2.解锁按箱或者按明细的数据
					Long rowId = 0L;
					List<BillContainerTaskDtl> taskDtlList = new ArrayList<BillContainerTaskDtl>();
					if(CommonUtil.hasValue(addCheckDtlList)){
						for (BillImCheckDtl bd : addCheckDtlList) {
							//获取进货暂存区储位
							CmDefcell cmDefcell = new CmDefcell();
							cmDefcell.setLocno(obj.getLocno());
							String cellNo = cmDefcellMapper.selectJhzcqCellNo(cmDefcell);
							if(StringUtils.isEmpty(cellNo)){
								throw new ServiceException("获取进货暂存区储位失败!");
							}
							//准备容器任务明细数据
							BillContainerTaskDtl taskDtl = new BillContainerTaskDtl();
							setBillContainerTaskDtl(bd, taskDtl, userId,obj.getBusinessType(),cellNo);
							taskDtl.setRowId(++rowId);
							taskDtlList.add(taskDtl);
						}
						
						//3.开始插入容器任务数据
						if(CommonUtil.hasValue(taskDtlList)){
							String optBillType = "";
							if(BUSINESSTYPE2.equals(obj.getBusinessType())){
								optBillType = ContainerTypeEnums.A.getOptBillType();
							}else{
								optBillType = ContainerTypeEnums.E.getOptBillType();
							}
							BillContainerTask containerTask = new BillContainerTask();
							containerTask.setLocno(obj.getLocno());
							containerTask.setContaskNo(obj.getCheckNo());
							containerTask.setCreator(userId);
							containerTask.setCreatetm(new Date());
							containerTask.setEditor(userId);
							containerTask.setEdittm(new Date());
							containerTask.setUseType(optBillType);
							containerTask.setBusinessType(BUSINESSTYPE1);
							containerCount = billContainerTaskService.insertBillContainerTask(containerTask, taskDtlList);
							if(containerCount < 1){
								throw new ServiceException(obj.getCheckNo()+"插入容器任务数据失败!");
							}
							//5.开始审核容器
							procCommonService.procContaskAudit(obj.getLocno(), optBillType,obj.getCheckNo(), userId);
						}
					}
				}
				
				//装箱验收处理新箱号
//				if(BUSINESSTYPE2.equals(obj.getBusinessType())){
//					Map<String, Object> params = new HashMap<String, Object>();
//					params.put("locno", b.getLocno());
//					params.put("ownerNo", b.getOwnerNo());
//					params.put("checkNo", b.getCheckNo());
//					List<BillImCheckDtl> checkDtlList = billImCheckDtlMapper.selectByParams(null, params);
//					if(!CommonUtil.hasValue(checkDtlList)){
//						throw new ServiceException("单据"+b.getCheckNo()+"查询验收单明细失败！");
//					}
//				}
				
				//调用存储过程审核验收单
				Map<String, String> param = new HashMap<String, String>();
				param.put("I_locno", b.getLocno());
				param.put("I_owner", b.getOwnerNo());
				param.put("I_check_no", b.getCheckNo());
				param.put("I_oper_user", userId);
				procCommonMapper.procImCheckAudit(param);
				if (param.get("strOutMsg") != null) {
					String[] results = param.get("strOutMsg").split("\\|");
					if (!RESULTY.equals(results[0])) {
						throw new ServiceException(results[1]);
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	public void setBillContainerTaskDtl(BillImCheckDtl bd,BillContainerTaskDtl taskDtl,String userName,String businessType,String cellNo){
		taskDtl.setLocno(bd.getLocno());
		taskDtl.setContaskNo(bd.getCheckNo());
		//taskDtl.setsCellNo(bd.getCellNo());
		taskDtl.setBrandNo(bd.getBrandNo());
		taskDtl.setItemNo(bd.getItemNo());
		taskDtl.setSizeNo(bd.getSizeNo());
		taskDtl.setQty(Long.valueOf(String.valueOf(bd.getCheckQty())));
		taskDtl.setCreator(userName);
		taskDtl.setEditor(userName);
		taskDtl.setQuality(bd.getQuality());
		taskDtl.setItemType(bd.getItemType());
		taskDtl.setdContainerNo("N");
		if(BUSINESSTYPE2.equals(businessType)){
			taskDtl.setsSubContainerNo("N");
			taskDtl.setdSubContainerNo(bd.getLoadboxno());
			taskDtl.setsCellNo("N");
			taskDtl.setdCellNo(cellNo);
		}else{
			taskDtl.setsSubContainerNo(bd.getBoxNo());
			taskDtl.setdSubContainerNo("N");
			taskDtl.setsCellNo(cellNo);
			taskDtl.setdCellNo("N");
		}
		if(StringUtils.isNotBlank(bd.getPanNo())){
			taskDtl.setContainerType(ContainerTypeEnums.P.getOptBillType());
			taskDtl.setsContainerNo(bd.getPanNo());
		}else{
			taskDtl.setContainerType(ContainerTypeEnums.C.getOptBillType());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findCheckForDirectCount(Map map, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billImCheckMapper.findCheckForDirectCount(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillImCheck> selectByPageForDirect(SimplePage page, Map map, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billImCheckMapper.selectByPageForDirect(page, map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
    @DataAccessAuth({DataAccessRuleEnum.BRAND})
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billImCheckMapper.selectSumQty(params, authorityParams);
	}
}