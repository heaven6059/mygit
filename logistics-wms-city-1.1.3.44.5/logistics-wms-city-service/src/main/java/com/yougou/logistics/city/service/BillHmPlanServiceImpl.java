package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.BillConStoreLockEnums;
import com.yougou.logistics.city.common.enums.BillHmPlanStatusEnums;
import com.yougou.logistics.city.common.model.BillHmPlan;
import com.yougou.logistics.city.common.model.BillHmPlanDtl;
import com.yougou.logistics.city.common.model.BillHmPlanKey;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.model.TmpAccDataAllot;
import com.yougou.logistics.city.common.model.TmpAccResult;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillHmPlanDtlMapper;
import com.yougou.logistics.city.dal.database.BillHmPlanMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDirectMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 09:47:01 CST 2013
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
@Service("billHmPlanService")
class BillHmPlanServiceImpl extends BaseCrudServiceImpl implements BillHmPlanService {
	@Resource
	private BillHmPlanMapper billHmPlanMapper;

	@Resource
	private BillHmPlanDtlMapper billHmPlanDtlMapper;
	@Resource
	private BillOmOutstockDirectMapper BillOmOutstockDirectMapper;
	@Resource
	private BillAccControlService billAccControlService;
	@Resource
	private TmpAccDataAllotService accDataAllotService;
	@Resource
	private TmpAccResultService accResultService;
	@Resource
	private ItemMapper itemMapper;
	private static final String STATUS10 = "10";
	
	private static final String STATUS11 = "11";
	
	private static final String STATUS90 = "90";
	
	private static final String STATUS91 = "91";


	private static final String STOCKTYPE4 = "4";
	
	@Log
	private Logger logger;

	@Override
	public BaseCrudMapper init() {
		return billHmPlanMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void audit(String planNo, SystemUser user) throws ServiceException {
		try {
			Date date = new Date();
			String locno = user.getLocNo();
			String oper = user.getLoginName();
			String Username = user.getUsername();
			BillHmPlanKey hmPlanKey = new BillHmPlanKey();
			hmPlanKey.setLocno(locno);
			hmPlanKey.setPlanNo(planNo);
			BillHmPlan hmPlan = (BillHmPlan)billHmPlanMapper.selectByPrimaryKey(hmPlanKey);
			if(hmPlan == null){
				throw new ManagerException("查找移库单主档信息失败！");
			}
			
			//验证储位正确性
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", locno);
			params.put("planNo", planNo);
			List<BillHmPlanDtl> listHmPlanDtls = billHmPlanDtlMapper.selectByParams(null, params);
			if(!CommonUtil.hasValue(listHmPlanDtls)){
				throw new ManagerException("没有明细不能审核！");
			}
			if(BillConStoreLockEnums.BUSINESS_TYPE1.getStatus().equals(hmPlan.getBusinessType())){
				
				List<BillHmPlanDtl> listItemTypeDtls = billHmPlanDtlMapper.selectCheckHwPlanCellNoItemType(params);
				if(CommonUtil.hasValue(listItemTypeDtls)){
					StringBuffer sb = new StringBuffer();
					sb.append("(退厂移库)单号："+listItemTypeDtls.get(0).getPlanNo()+"</br>");
					sb.append("商品："+listItemTypeDtls.get(0).getItemNo()+"</br>");
					sb.append("尺码："+listItemTypeDtls.get(0).getSizeNo()+"</br>");
					sb.append("来源储位："+listItemTypeDtls.get(0).getsCellNo()+"</br>");
					sb.append("目的储位："+listItemTypeDtls.get(0).getdCellNo()+"</br>");
					sb.append("原因：源储位与目的储位库区(需作业存储区,用途:退货区)相关属性不同,不允许移库!");
					throw new ManagerException(sb.toString());
				}
				
				List<BillHmPlanDtl> listDtls = billHmPlanDtlMapper.selectCheckHwPlanCellNo(params);
				if(CommonUtil.hasValue(listDtls)){
					StringBuffer sb = new StringBuffer();
					sb.append("(退厂移库)单号："+listDtls.get(0).getPlanNo()+"</br>");
					sb.append("商品："+listDtls.get(0).getItemNo()+"</br>");
					sb.append("尺码："+listDtls.get(0).getSizeNo()+"</br>");
					sb.append("来源储位："+listDtls.get(0).getsCellNo()+"</br>");
					sb.append("目的储位："+listDtls.get(0).getdCellNo()+"</br>");
					sb.append("原因：正在储位盘点锁定,不允许移库!");
					throw new ManagerException(sb.toString());
				}
				
			}else{
				for (BillHmPlanDtl bm : listHmPlanDtls) {
					Map<String, String> maps = new HashMap<String, String>();
					maps.put("I_locno", bm.getLocno());
					maps.put("I_sCellNo", bm.getsCellNo());
					maps.put("I_sItemType", bm.getItemType());
					maps.put("I_sAreaQuality", bm.getQuality());
					maps.put("I_dCellNo", bm.getdCellNo());
					maps.put("I_rowid", String.valueOf(bm.getRowId()));
					billHmPlanMapper.procHmIsallowmovestock(maps);
					String strOutMsg = maps.get("O_msg");
					if (!"Y|".equals(strOutMsg)) {
						String[] msgs = strOutMsg.split("\\|");
						throw new ServiceException("单号：" + bm.getPlanNo() + "商品：" + bm.getItemNo() + "尺码："
								+ bm.getSizeNo() + msgs[1]);
					}
				}
			}
			
			BillHmPlan plan = new BillHmPlan();
			plan.setPlanNo(planNo);
			plan.setLocno(locno);
			plan.setAuditor(oper);
			plan.setAuditorName(Username);
			plan.setAudittm(date);
			plan.setEditor(oper);
			plan.setEditorName(Username);
			plan.setEdittm(date);
			plan.setStatus(BillHmPlanStatusEnums.STATUS11.getStatus());
			plan.setUpdStatus("10");
			int result = billHmPlanMapper.updateByPrimaryKeySelective(plan);
			if(result < 1){
				throw new ManagerException("单据"+planNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
			}
			
			//记账存储过程(出，把记账类型表的SYN_ACC_IN参数设置为1,会自动调用I(ji))
			BillAccControlDto controlDtoO = new BillAccControlDto();
			controlDtoO.setiPaperNo(planNo);
			controlDtoO.setiLocType("2");
			controlDtoO.setiPaperType(CNumPre.Hm_Move_PRE);
			controlDtoO.setiIoFlag("O");
			controlDtoO.setiPrepareDataExt(new BigDecimal(0));
			controlDtoO.setiIsWeb(new BigDecimal(1));
			billAccControlService.procAccApply(controlDtoO);
			
			
			//记账存储过程(出)
			BillAccControlDto controlDtoI = new BillAccControlDto();
			controlDtoI.setiPaperNo(planNo);
			controlDtoI.setiLocType("2");
			controlDtoI.setiPaperType(CNumPre.Hm_Move_PRE);
			controlDtoI.setiIoFlag("I");
			controlDtoI.setiPrepareDataExt(new BigDecimal(0));
			controlDtoI.setiIsWeb(new BigDecimal(1));
			billAccControlService.procAccApply(controlDtoI);

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("locno", locno);
			param.put("planNo", planNo);
			List<BillHmPlanDtl> list = billHmPlanDtlMapper.selectByParams(null, param);
			for (BillHmPlanDtl b : list) {
				
				//查询来源储位的信息
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("rowId", b.getRowId());
				List<TmpAccDataAllot> listSCell = accDataAllotService.findByBiz(null, maps);
				List<TmpAccResult> listDCell = accResultService.findByBiz(null, maps);
				if(!CommonUtil.hasValue(listSCell)||!CommonUtil.hasValue(listDCell)){
					throw new ServiceException("记账临时表获取储位信息失败,库存不足!");
				}
				for (TmpAccDataAllot td : listSCell) {
					Item itemKey = new Item();
					itemKey.setItemNo(td.getItemNo());
					Item item = (Item)itemMapper.selectByPrimaryKey(itemKey);
					BillOmOutstockDirect d = new BillOmOutstockDirect();
					d.setLocno(b.getLocno());
					d.setOwnerNo(b.getOwnerNo());
					d.setOutstockType(STOCKTYPE4);
					d.setOperateDate(date);
					d.setItemNo(b.getItemNo());
					d.setsCellNo(b.getsCellNo());
					d.setsCellId(td.getCellId());
					d.setdCellNo(b.getdCellNo());
					d.setdCellId(listDCell.get(0).getCellId());
					d.setItemQty(td.getNeedQty());
					d.setStatus(STATUS10);
					d.setCreator(oper);
					d.setCreatetm(date);
					d.setEditor(oper);
					d.setEdittm(date);
					d.setCreatorname(Username);
					d.setEditorname(Username);
					d.setSizeNo(b.getSizeNo());
					d.setExpNo(b.getPlanNo());
					d.setExpDate(date);
					d.setPackQty(d.getPackQty() == null ? new BigDecimal(1) : d.getPackQty());
					d.setOperateType("B");
					d.setQuality(td.getQuality());
					d.setItemType(td.getItemType());
					d.setBrandNo(item.getBrandNo());
					int result2 = BillOmOutstockDirectMapper.insertSelective(d);
					if (result2 < 1) {
						throw new ManagerException("写下架指示信息时未更新到记录！");
					}
				}
			}
		}catch (ServiceException e) {
			logger.error(e.getMessage());
			throw new ServiceException(e.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage());
			throw new ServiceException(e.getMessage());
		}
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void cancelBillHmPlan(List<BillHmPlan> listPlans, SystemUser user) throws ServiceException {
		try{
			Date date = new Date();
			BillHmPlanKey planKey = new BillHmPlanKey();
			for (BillHmPlan billHmPlan : listPlans) {
				
				//数据验证
				planKey.setLocno(billHmPlan.getLocno());
				planKey.setPlanNo(billHmPlan.getPlanNo());
				BillHmPlan plan = (BillHmPlan) billHmPlanMapper.selectByPrimaryKey(planKey);
				if (plan == null ) {
					throw new ServiceException(billHmPlan.getPlanNo()+"单据被删除或状态已改变!");
				}
				if(STATUS10.equals(plan.getStatus()) || STATUS91.equals(plan.getStatus())|| STATUS90.equals(plan.getStatus())){
					throw new ServiceException(billHmPlan.getPlanNo()+"建单或已关闭的单据不能关闭!");
				}
				
				//验证是否可以关闭
				int count = billHmPlanMapper.checkIsCancelHmPlan(billHmPlan);
				if(count > 0){
					throw new ServiceException(billHmPlan.getPlanNo()+"存在未确认的移库回单,不能手工关闭!");
				}
				
				//开始释放库存预上预下
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("locno", billHmPlan.getLocno());
				params.put("expNo", billHmPlan.getPlanNo());
				params.put("status", STATUS10);
				List<BillOmOutstockDirect> listDirects = BillOmOutstockDirectMapper.selectByParams(null, params);
				if(!CommonUtil.hasValue(listDirects)){
					throw new ServiceException(billHmPlan.getPlanNo()+"没有找到可释放的下架指示数据,不能手工关闭!");
				}
				for (BillOmOutstockDirect d : listDirects) {
					Item item = itemMapper.selectByCode(d.getItemNo(),null);//查询供应商
					//扣减预下数量
					BillAccControlDto controlDto = new BillAccControlDto();
					controlDto.setiLocno(d.getLocno());
					controlDto.setiOwnerNo(d.getOwnerNo());
					controlDto.setiPaperNo(d.getExpNo());
					controlDto.setiPaperType(CNumPre.Hm_Move_PRE);
					controlDto.setiIoFlag("O");
					controlDto.setiCreator(billHmPlan.getCreator());
					controlDto.setiRowId(new BigDecimal(d.getDirectSerial()));
					controlDto.setiCellNo(d.getsCellNo());
					controlDto.setiCellId(new BigDecimal(d.getsCellId()));
					controlDto.setiItemNo(d.getItemNo());
					controlDto.setiSizeNo(d.getSizeNo());
					controlDto.setiPackQty(d.getPackQty()==null?new BigDecimal(1):d.getPackQty());
					controlDto.setiSupplierNo(item.getSupplierNo());
					controlDto.setiOutstockQty(new BigDecimal(0).subtract(d.getItemQty()));
					controlDto.setiItemType(d.getItemType());
					controlDto.setiQuality(d.getQuality());
					
					/**默认值**/
					controlDto.setiQty(new BigDecimal(0));
					controlDto.setiInstockQty(new BigDecimal(0));
					controlDto.setiStatus("0");
					controlDto.setiFlag("0");
					controlDto.setiHmManualFlag("1");
					controlDto.setiTerminalFlag("1");
					billAccControlService.procAccPrepareDataExt(controlDto);
					
					//调用外部存储过程
					BillAccControlDto dto = new BillAccControlDto();
					dto.setiPaperNo(d.getLocateNo());
					dto.setiLocType("2");
					dto.setiPaperType(CNumPre.Hm_Move_PRE);
					dto.setiIoFlag("O");
					dto.setiPrepareDataExt(new BigDecimal(d.getDirectSerial()));
					dto.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dto);
					
					//扣减预上数量
					BillAccControlDto controlDtoIn = new BillAccControlDto();
					controlDtoIn.setiLocno(d.getLocno());
					controlDtoIn.setiOwnerNo(d.getOwnerNo());
					controlDtoIn.setiPaperNo(d.getExpNo());
					controlDtoIn.setiPaperType(CNumPre.Hm_Move_PRE);
					controlDtoIn.setiIoFlag("I");
					controlDtoIn.setiCreator(d.getCreator());
					controlDtoIn.setiRowId(new BigDecimal(d.getDirectSerial()));
					controlDtoIn.setiCellNo(d.getdCellNo());
					controlDtoIn.setiCellId(new BigDecimal(d.getdCellId()));
					controlDtoIn.setiItemNo(d.getItemNo());
					controlDtoIn.setiSizeNo(d.getSizeNo());
					controlDtoIn.setiPackQty(d.getPackQty()==null?new BigDecimal(1):d.getPackQty());
					controlDtoIn.setiSupplierNo(item.getSupplierNo());
					controlDtoIn.setiInstockQty(new BigDecimal(0).subtract(d.getItemQty()));
					controlDtoIn.setiItemType(d.getItemType());
					controlDtoIn.setiQuality(d.getQuality());
					
					/**默认值**/
					controlDtoIn.setiQty(new BigDecimal(0));
					controlDtoIn.setiOutstockQty(new BigDecimal(0));
					controlDtoIn.setiStatus("0");
					controlDtoIn.setiFlag("0");
					controlDtoIn.setiHmManualFlag("1");
					controlDtoIn.setiTerminalFlag("1");
					billAccControlService.procAccPrepareDataExt(controlDtoIn);
					
					//调用外部存储过程
					BillAccControlDto dtoIn = new BillAccControlDto();
					dtoIn.setiPaperNo(d.getLocateNo());
					dtoIn.setiLocType("2");
					dtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
					dtoIn.setiIoFlag("I");
					dtoIn.setiPrepareDataExt(new BigDecimal(d.getDirectSerial()));
					dtoIn.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dtoIn);
					
					int dcount = BillOmOutstockDirectMapper.deleteByPrimarayKeyForModel(d);
					if(dcount < 1){
						throw new ServiceException(billHmPlan.getPlanNo() + "删除移库下架指示表失败");
					}
				}
				
				//更改作废状态
				billHmPlan.setStatus(STATUS91);
				billHmPlan.setEdittm(date);
				billHmPlan.setEditor(user.getLoginName());
				billHmPlan.setEditorName(user.getUsername());
				int bcount = billHmPlanMapper.updateByPrimaryKeySelective(billHmPlan);		
				if(bcount < 1){
					throw new ServiceException(billHmPlan.getPlanNo() + "更新移库单状态失败");
				}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public void procHmIsallowmovestock(Map<String, String> maps) throws ServiceException {
		try{
			billHmPlanMapper.procHmIsallowmovestock(maps);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}