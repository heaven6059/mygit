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
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.model.BillOmExpKey;
import com.yougou.logistics.city.common.model.BillOmLocate;
import com.yougou.logistics.city.common.model.BillOmLocateDtl;
import com.yougou.logistics.city.common.model.BillOmLocateKey;
import com.yougou.logistics.city.common.model.BillOmOutstock;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.BillOmOutstockDirectKey;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillStatusLog;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillConStorelockDtlMapper;
import com.yougou.logistics.city.dal.database.BillConStorelockMapper;
import com.yougou.logistics.city.dal.database.BillOmLocateDtlMapper;
import com.yougou.logistics.city.dal.database.BillOmLocateMapper;
import com.yougou.logistics.city.dal.database.BillStatusLogMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;
import com.yougou.logistics.city.dal.mapper.BillOmExpDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmExpMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDirectMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockMapper;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 13:58:52 CST 2013
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
@Service("billOmLocateService")
class BillOmLocateServiceImpl extends BaseCrudServiceImpl implements BillOmLocateService {
	
    @Resource
    private BillOmLocateMapper billOmLocateMapper;
    
    @Resource
    private BillOmLocateDtlMapper billOmLocateDtlMapper;
    
    @Resource
    private BillOmOutstockDtlMapper billOmOutstockDtlMapper;
    
    @Resource
    private BillOmOutstockDirectMapper billOmOutstockDirectMapper;
    
    @Resource
    private BillOmExpMapper billOmExpMapper;
    
    @Resource
    private BillStatusLogMapper billStatusLogMapper;
    
    @Resource
    private BillOmOutstockMapper billOmOutstockMapper;
    
    @Resource
    private BillAccControlService billAccControlService;
    
    @Resource
    private BillConStorelockMapper billConStorelockMapper;
    
    @Resource
    private BillConStorelockDtlMapper billConStorelockDtlMapper;
    
    @Resource
    private BillOmExpDtlMapper billOmExpDtlMapper;
    
    @Resource
    private ItemMapper itemMapper;
    
    @Resource
    private CmDefcellService cmDefcellService;
    
    private final static String STATUS91 = "91";
    
    private final static String STATUS10 = "10";
    
    private final static String STATUS18 = "18";

    @Override
    public BaseCrudMapper init() {
        return billOmLocateMapper;
    }

    
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(BillOmLocate billOmLocate,AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmLocateMapper.selectSumQty(billOmLocate,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}



	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBillOmLocateCount(BillOmLocate billOmLocate, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billOmLocateMapper.selectBillOmLocateCount(billOmLocate, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmLocate> findBillOmLocateByPage(SimplePage page, String orderByField, String orderBy,
			BillOmLocate billOmLocate, AuthorityParams authorityParams) throws ServiceException {
		try {
			return billOmLocateMapper.selectBillOmLocateByPage(page, orderByField, orderBy, billOmLocate, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void procBillOmExpContinueDispatchQuery(Map<String, String> map) throws ServiceException {
		try {
			billOmLocateMapper.procBillOmExpContinueDispatchQuery(map);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public Map<String, String> overBillOmLocate(List<BillOmLocate> lists,SystemUser user) throws ServiceException {
		Map<String, String> flag = new HashMap<String, String>();
		try{
			//关闭拣货波茨单
			for (BillOmLocate billOmLocate : lists) {
				if(StringUtils.isEmpty(billOmLocate.getLocno())||StringUtils.isEmpty(billOmLocate.getLocateNo())){
					flag.put("result", "fail");
					flag.put("msg", "参数非法");
					return flag;
				}
				
				int count = billOmLocateMapper.checkBillOmLocateOver(billOmLocate);
				if(count > 0){
					flag.put("result", "fail");
					flag.put("msg", billOmLocate.getLocateNo()+"波次存在未确认拣货或未确认复核的单据,不能关闭!");
					return flag;
				}
				
				//拣货波次下创建的拣货单已经全部确认，但没有创建一个复核单，该拣货波次不能关闭；
				int countNo = billOmLocateMapper.checkBillOmLocateOverNo(billOmLocate);
				if(countNo > 0){
					flag.put("result", "fail");
					flag.put("msg", billOmLocate.getLocateNo()+"波次存在已确认拣货单，但没有复核的单据,不能关闭!");
					return flag;
				}
				
				//修改状态
				BillOmLocate locate = new BillOmLocate();
				locate.setLocno(billOmLocate.getLocno());
				locate.setLocateNo(billOmLocate.getLocateNo());
				locate.setStatus(STATUS91);
				locate.setEditor(user.getLoginName());
				locate.setEditorname(user.getUsername());
				locate.setEdittm(new Date());
				int uCount = billOmLocateMapper.updateByPrimaryKeySelective(locate);
				if(uCount <= 0){
					flag.put("result", "fail");
					flag.put("msg", billOmLocate.getLocateNo()+"更新状态失败!");
					return flag;
				}
				
				
				//如果下架指示表存在状态为10的明细，则需要回滚预上和预下库存数；
				billOmLocate.setStatus(STATUS10);
				List<BillOmOutstockDirect> lstOutstockDirect = billOmOutstockDirectMapper.selectNoOutstockQty(billOmLocate);
				int x  = 1;
				for(BillOmOutstockDirect objDirect : lstOutstockDirect){
					int dNum = x++;
					Item item = itemMapper.selectByCode(objDirect.getItemNo(),null);//查询供应商
					//扣减预下数量
					BillAccControlDto controlDto = new BillAccControlDto();
					controlDto.setiLocno(billOmLocate.getLocno());
					controlDto.setiOwnerNo(objDirect.getOwnerNo());
					controlDto.setiPaperNo(billOmLocate.getLocateNo());
					controlDto.setiPaperType(CNumPre.OM_LOCATE_PRE);
					controlDto.setiIoFlag("O");
					controlDto.setiCreator(user.getLoginName());
					controlDto.setiRowId(new BigDecimal(dNum));
					controlDto.setiCellNo(objDirect.getsCellNo());
					controlDto.setiCellId(new BigDecimal(objDirect.getsCellId()));
					controlDto.setiItemNo(objDirect.getItemNo());
					controlDto.setiSizeNo(objDirect.getSizeNo());
					controlDto.setiPackQty(objDirect.getPackQty());
					controlDto.setiSupplierNo(item.getSupplierNo());
					controlDto.setiOutstockQty(new BigDecimal(0).subtract(objDirect.getLocateQty()));
					controlDto.setiItemType(objDirect.getItemType());
					controlDto.setiQuality(objDirect.getQuality());
					
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
					dto.setiPaperNo(billOmLocate.getLocateNo());
					dto.setiLocType("2");
					dto.setiPaperType(CNumPre.OM_LOCATE_PRE);
					dto.setiIoFlag("O");
					dto.setiPrepareDataExt(new BigDecimal(dNum));
					dto.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dto);
					
					//扣减预上数量
					BillAccControlDto controlDtoIn = new BillAccControlDto();
					controlDtoIn.setiLocno(billOmLocate.getLocno());
					controlDtoIn.setiOwnerNo(objDirect.getOwnerNo());
					controlDtoIn.setiPaperNo(billOmLocate.getLocateNo());
					controlDtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
					controlDtoIn.setiIoFlag("I");
					controlDtoIn.setiCreator(user.getLoginName());
					controlDtoIn.setiRowId(new BigDecimal(dNum));
					controlDtoIn.setiCellNo(objDirect.getdCellNo());
					controlDtoIn.setiCellId(new BigDecimal(objDirect.getdCellId()));
					controlDtoIn.setiItemNo(objDirect.getItemNo());
					controlDtoIn.setiSizeNo(objDirect.getSizeNo());
					controlDtoIn.setiPackQty(objDirect.getPackQty());
					controlDtoIn.setiSupplierNo(item.getSupplierNo());
					controlDtoIn.setiInstockQty(new BigDecimal(0).subtract(objDirect.getLocateQty()));
					controlDtoIn.setiItemType(objDirect.getItemType());
					controlDtoIn.setiQuality(objDirect.getQuality());
					
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
					dtoIn.setiPaperNo(billOmLocate.getLocateNo());
					dtoIn.setiLocType("2");
					dtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
					dtoIn.setiIoFlag("I");
					dtoIn.setiPrepareDataExt(new BigDecimal(dNum));
					dtoIn.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dtoIn);
				}
				
				
				//查询已拣货但未复核的明细数据，需要将理货区的库存转移到异常区
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("locno", billOmLocate.getLocno());
				params.put("locateNo", billOmLocate.getLocateNo());
				List<BillOmOutstockDtl> listPickNoRecheckDtl = billOmOutstockDtlMapper.selectPickNoRecheckDtl(params);
				if(null!=listPickNoRecheckDtl && !listPickNoRecheckDtl.isEmpty()){
					
					//查询异常区储位
					Map<String,Object> paramsCellNo = new HashMap<String, Object>();
					paramsCellNo.put("locno", billOmLocate.getLocno());
					paramsCellNo.put("areaUsetype", "5");
					paramsCellNo.put("areaAttribute", "0");
					paramsCellNo.put("attributeType", "0");
					List<CmDefcell> lstCellNo =  cmDefcellService.selectCellNoByUserType(paramsCellNo);
					if(null== lstCellNo || lstCellNo.isEmpty()){
						flag.put("result", "fail");
						flag.put("msg", billOmLocate.getLocateNo()+"波次存在部分复核的单据,请先设置异常区储位!");
						return flag;
					}
					
					String exceptionCellNo  = lstCellNo.get(0).getCellNo();
					
					//获取异常区的储位ID和储位编码
					for(BillOmOutstockDtl objDtl : listPickNoRecheckDtl){
						int rowId = x++;
						Item item = itemMapper.selectByCode(objDtl.getItemNo(),null);//查询供应商
						
						//扣减理货区的数量
						BillAccControlDto controlDto = new BillAccControlDto();
						controlDto.setiLocno(billOmLocate.getLocno());
						controlDto.setiOwnerNo(objDtl.getOwnerNo());
						controlDto.setiPaperNo(billOmLocate.getLocateNo());
						controlDto.setiPaperType(CNumPre.OM_LOCATE_PRE);
						controlDto.setiIoFlag("O");
						controlDto.setiCreator(user.getLoginName());
						controlDto.setiRowId(new BigDecimal(rowId));
						controlDto.setiCellNo(objDtl.getdCellNo());
						controlDto.setiCellId(new BigDecimal(objDtl.getdCellId()));
						controlDto.setiItemNo(objDtl.getItemNo());
						controlDto.setiSizeNo(objDtl.getSizeNo());
						controlDto.setiPackQty(objDtl.getPackQty());
						controlDto.setiSupplierNo(item.getSupplierNo());
						controlDto.setiOutstockQty(new BigDecimal(0));
						controlDto.setiItemType(objDtl.getItemType());
						controlDto.setiQuality(objDtl.getQuality());
						
						/**默认值**/
						controlDto.setiQty(objDtl.getRealQty());
						controlDto.setiInstockQty(new BigDecimal(0));
						controlDto.setiStatus("0");
						controlDto.setiFlag("0");
						controlDto.setiHmManualFlag("1");
						controlDto.setiTerminalFlag("1");
						billAccControlService.procAccPrepareDataExt(controlDto);
						
						//调用外部存储过程
						BillAccControlDto dto = new BillAccControlDto();
						dto.setiPaperNo(billOmLocate.getLocateNo());
						dto.setiLocType("2");
						dto.setiPaperType(CNumPre.OM_LOCATE_PRE);
						dto.setiIoFlag("O");
						dto.setiPrepareDataExt(new BigDecimal(rowId));
						dto.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dto);
						
						
						
						//加异常区的库存数量
						BillAccControlDto controlDtoIn = new BillAccControlDto();
						controlDtoIn.setiLocno(billOmLocate.getLocno());
						controlDtoIn.setiOwnerNo(objDtl.getOwnerNo());
						controlDtoIn.setiPaperNo(billOmLocate.getLocateNo());
						controlDtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
						controlDtoIn.setiIoFlag("I");
						controlDtoIn.setiCreator(user.getLoginName());
						controlDtoIn.setiRowId(new BigDecimal(rowId));
						controlDtoIn.setiCellNo(exceptionCellNo);
						controlDtoIn.setiCellId(null);
						controlDtoIn.setiItemNo(objDtl.getItemNo());
						controlDtoIn.setiSizeNo(objDtl.getSizeNo());
						controlDtoIn.setiPackQty(objDtl.getPackQty());
						controlDtoIn.setiSupplierNo(item.getSupplierNo());
						controlDtoIn.setiInstockQty(new BigDecimal(0));
						controlDtoIn.setiItemType(objDtl.getItemType());
						controlDtoIn.setiQuality(objDtl.getQuality());
						
						/**默认值**/
						controlDtoIn.setiQty(objDtl.getRealQty());
						controlDtoIn.setiOutstockQty(new BigDecimal(0));
						controlDtoIn.setiStatus("0");
						controlDtoIn.setiFlag("0");
						controlDtoIn.setiHmManualFlag("1");
						controlDtoIn.setiTerminalFlag("1");
						billAccControlService.procAccPrepareDataExt(controlDtoIn);
						
						//调用外部存储过程
						BillAccControlDto dtoIn = new BillAccControlDto();
						dtoIn.setiPaperNo(billOmLocate.getLocateNo());
						dtoIn.setiLocType("2");
						dtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
						dtoIn.setiIoFlag("I");
						dtoIn.setiPrepareDataExt(new BigDecimal(rowId));
						dtoIn.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dtoIn);
					}
					
				}
				
				
				//如果波茨不存在拣货单，还原回写预上预下数量
//				Map<String,Object> params = new HashMap<String, Object>();
//				params.put("locno", billOmLocate.getLocno());
//				params.put("locateNo", billOmLocate.getLocateNo());
//				List<BillOmOutstockDtl> listCheckDtls = billOmOutstockDtlMapper.selectByParams(null, params);
//				if(!CommonUtil.hasValue(listCheckDtls)){
//					//如果不存在拣货单，对应的预上预下数量要减掉
//					//减去的预上预下数量 = 已调度数量
//					List<BillOmOutstockDirect> listDirects = billOmOutstockDirectMapper.selectDispatchNoOutstockRealQty(billOmLocate);
//					if(CommonUtil.hasValue(listDirects)){
//						for (BillOmOutstockDirect d : listDirects) {
//							Item item = itemMapper.selectByCode(d.getItemNo(),null);//查询供应商
//							//扣减预下数量
//							BillAccControlDto controlDto = new BillAccControlDto();
//							controlDto.setiLocno(d.getLocno());
//							controlDto.setiOwnerNo(d.getOwnerNo());
//							controlDto.setiPaperNo(d.getLocateNo());
//							controlDto.setiPaperType(CNumPre.OM_LOCATE_PRE);
//							controlDto.setiIoFlag("O");
//							controlDto.setiCreator(d.getCreator());
//							controlDto.setiRowId(new BigDecimal(d.getDirectSerial()));
//							controlDto.setiCellNo(d.getsCellNo());
//							controlDto.setiCellId(new BigDecimal(d.getsCellId()));
//							controlDto.setiItemNo(d.getItemNo());
//							controlDto.setiSizeNo(d.getSizeNo());
//							controlDto.setiPackQty(d.getPackQty());
//							controlDto.setiSupplierNo(item.getSupplierNo());
//							controlDto.setiOutstockQty(new BigDecimal(0).subtract(d.getLocateQty()));
//							controlDto.setiItemType(d.getItemType());
//							controlDto.setiQuality(d.getQuality());
//							
//							/**默认值**/
//							controlDto.setiQty(new BigDecimal(0));
//							controlDto.setiInstockQty(new BigDecimal(0));
//							controlDto.setiStatus("0");
//							controlDto.setiFlag("0");
//							controlDto.setiHmManualFlag("1");
//							controlDto.setiTerminalFlag("1");
//							billAccControlService.procAccPrepareDataExt(controlDto);
//							
//							//调用外部存储过程
//							BillAccControlDto dto = new BillAccControlDto();
//							dto.setiPaperNo(d.getLocateNo());
//							dto.setiLocType("2");
//							dto.setiPaperType(CNumPre.OM_LOCATE_PRE);
//							dto.setiIoFlag("O");
//							dto.setiPrepareDataExt(new BigDecimal(d.getDirectSerial()));
//							dto.setiIsWeb(new BigDecimal(1));
//							billAccControlService.procAccApply(dto);
//							
//							//扣减预上数量
//							BillAccControlDto controlDtoIn = new BillAccControlDto();
//							controlDtoIn.setiLocno(d.getLocno());
//							controlDtoIn.setiOwnerNo(d.getOwnerNo());
//							controlDtoIn.setiPaperNo(d.getLocateNo());
//							controlDtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
//							controlDtoIn.setiIoFlag("I");
//							controlDtoIn.setiCreator(d.getCreator());
//							controlDtoIn.setiRowId(new BigDecimal(d.getDirectSerial()));
//							controlDtoIn.setiCellNo(d.getdCellNo());
//							controlDtoIn.setiCellId(new BigDecimal(d.getdCellId()));
//							controlDtoIn.setiItemNo(d.getItemNo());
//							controlDtoIn.setiSizeNo(d.getSizeNo());
//							controlDtoIn.setiPackQty(d.getPackQty());
//							controlDtoIn.setiSupplierNo(item.getSupplierNo());
//							controlDtoIn.setiInstockQty(new BigDecimal(0).subtract(d.getLocateQty()));
//							controlDtoIn.setiItemType(d.getItemType());
//							controlDtoIn.setiQuality(d.getQuality());
//							
//							/**默认值**/
//							controlDtoIn.setiQty(new BigDecimal(0));
//							controlDtoIn.setiOutstockQty(new BigDecimal(0));
//							controlDtoIn.setiStatus("0");
//							controlDtoIn.setiFlag("0");
//							controlDtoIn.setiHmManualFlag("1");
//							controlDtoIn.setiTerminalFlag("1");
//							billAccControlService.procAccPrepareDataExt(controlDtoIn);
//							
//							//调用外部存储过程
//							BillAccControlDto dtoIn = new BillAccControlDto();
//							dtoIn.setiPaperNo(d.getLocateNo());
//							dtoIn.setiLocType("2");
//							dtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
//							dtoIn.setiIoFlag("I");
//							dtoIn.setiPrepareDataExt(new BigDecimal(d.getDirectSerial()));
//							dtoIn.setiIsWeb(new BigDecimal(1));
//							billAccControlService.procAccApply(dtoIn);
//						}
//					}
//				}
			}
			
			flag.put("result", "success");
			flag.put("msg", "关闭成功!");
			return flag;
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public Map<String, String> recoveryLocateSend(List<BillOmLocate> lists) throws ServiceException {
		
		Map<String, String> flag = new HashMap<String, String>();
		try{
			String expStatus = "";
			//非空判断
			if(!CommonUtil.hasValue(lists)){
				flag.put("result", "fail");
				flag.put("msg", "参数非法!");
				return flag;
			}
			
			for (BillOmLocate billOmLocate : lists) {
				//非空验证
				if(StringUtils.isEmpty(billOmLocate.getLocno())||StringUtils.isEmpty(billOmLocate.getLocateNo())){
					flag.put("result", "fail");
					flag.put("msg", "参数非法!");
					return flag;
				}
				
				//检测是否存在以及做单的波次
				int checkCount = billOmLocateMapper.checkBillOmLocateRecovery(billOmLocate);
				if(checkCount > 0){
					flag.put("result", "fail");
					flag.put("msg", billOmLocate.getLocateNo()+"波次存在<已拣货>的拣货单或复核单,不能发单还原!");
					return flag;
				}
				
				//1.回滚发货通知单状态
				BillOmOutstockDirect billOmOutstockDirect = new BillOmOutstockDirect();
				billOmOutstockDirect.setLocno(billOmLocate.getLocno());
				billOmOutstockDirect.setLocateNo(billOmLocate.getLocateNo());
				List<BillOmOutstockDirect> listDirects = billOmOutstockDirectMapper.selectOutstockDirectExpNoGroupBy(billOmOutstockDirect);
				if(!CommonUtil.hasValue(listDirects)){
					flag.put("result", "fail");
					flag.put("msg", billOmLocate.getLocateNo() + "没有找到需要还原的数据,无需还原!");
					return flag;
				}
				
				expStatus = "10".equals(billOmLocate.getStatus())?"16":"17";//状态是建单说明没有调度完成
				for (BillOmOutstockDirect direct : listDirects) {
					BillOmExpKey billOmExpKey = new BillOmExpKey();
					billOmExpKey.setLocno(direct.getLocno());
					billOmExpKey.setExpNo(direct.getExpNo());
					billOmExpKey.setExpType(direct.getExpType());
					BillOmExp billOmExp = (BillOmExp)billOmExpMapper.selectByPrimaryKey(billOmExpKey);
					billOmExp.setStatus(expStatus);
					int eCount = billOmExpMapper.updateByPrimaryKey(billOmExp);
					if(eCount < 1){
						flag.put("result", "fail");
						flag.put("msg", "更新发货通知单状态失败!");
						return flag;
					}
					
					//删除日志已发单的日志信息
					BillStatusLog statusLog = new BillStatusLog();
					statusLog.setBillNo(billOmExp.getExpNo());
					statusLog.setStatus(STATUS18);
					billStatusLogMapper.deleteByPrimarayKeyForModel(statusLog);
				}
				
				//2.根据波次查询需要更新的拣货单明细
				BillOmOutstockDtl outstockDtl = new BillOmOutstockDtl();
				outstockDtl.setLocno(billOmLocate.getLocno());
				outstockDtl.setLocateNo(billOmLocate.getLocateNo());
				List<BillOmOutstockDtl> listOutstockDtls = billOmOutstockDtlMapper.selectOutstockDtlByLocateNoGroupBy(outstockDtl);
				if(!CommonUtil.hasValue(listOutstockDtls)){
					flag.put("result", "fail");
					flag.put("msg", "查询下架指示表数据失败(更改下架指示状态)!");
					return flag;
				}
				for (BillOmOutstockDtl billOmOutstockDtl : listOutstockDtls) {
					BillOmOutstockDirectKey directKey = new BillOmOutstockDirectKey();
					directKey.setLocno(billOmOutstockDtl.getLocno());
					directKey.setOperateDate(billOmOutstockDtl.getOperateDate());
					directKey.setDirectSerial(Long.valueOf(billOmOutstockDtl.getDivideId()));
					BillOmOutstockDirect direct = (BillOmOutstockDirect)billOmOutstockDirectMapper.selectByPrimaryKey(directKey);
					if(direct == null){
						flag.put("result", "fail");
						flag.put("msg", "更新查询下架指示对象失败!");
						return flag;
					}
					direct.setStatus(STATUS10);
					direct.setWorkQty(new BigDecimal(0));
					
					//更新下架指示表状态和分配数量
					int count = billOmOutstockDirectMapper.updateByPrimaryKeySelective(direct);
					if(count < 1){
						flag.put("result", "fail");
						flag.put("msg", "更新下架指示表失败!");
						return flag;
					}
				}
								
				//3.删除波次下的所有拣货单
				List<BillOmOutstockDtl> listOutstocks = billOmOutstockDtlMapper.selectOutstockNoByLocateNoGroupBy(outstockDtl);
				if(!CommonUtil.hasValue(listOutstocks)){
					flag.put("result", "fail");
					flag.put("msg", "查询拣货单明细失败!");
					return flag;
				}
				for (BillOmOutstockDtl billOmOutstockDtl : listOutstocks) {
					//删除拣货单明细
					int dOutstock = billOmOutstockDtlMapper.deleteByPrimarayKeyForModel(billOmOutstockDtl);
					if(dOutstock < 1){
						flag.put("result", "fail");
						flag.put("msg", "删除拣货单明细失败!");
						return flag;
					}
					//删除拣货单主表
					BillOmOutstock outstock = new BillOmOutstock();
					outstock.setLocno(billOmOutstockDtl.getLocno());
					outstock.setOutstockNo(billOmOutstockDtl.getOutstockNo());
					int cOutstock = billOmOutstockMapper.deleteByPrimarayKeyForModel(outstock);
					if(cOutstock < 1){
						flag.put("result", "fail");
						flag.put("msg", "删除拣货单失败!");
						return flag;
					}
				}
				
			}
			
			flag.put("result", "success");
			flag.put("msg", "发单还原成功!");
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(),e);
		}
		
		return flag;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void deleteOmLocate(List<BillOmLocate> lists,String loginName) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(lists)){
				throw new ServiceException("参数非法!");
			}
			for (BillOmLocate locate : lists) {
				
				//验证是否发单
				int locateNum = billOmOutstockDtlMapper.selectOmOutstockDtlNumByLocateNo(locate);
				if(locateNum > 0){
					throw new ServiceException(locate.getLocateNo()+"状态已改变,不能删除!");
				}
				
				//验证是否关闭
				BillOmLocateKey locateKey = new BillOmLocateKey();
				locateKey.setLocno(locate.getLocno());
				locateKey.setLocateNo(locate.getLocateNo());
				BillOmLocate billOmLocate = (BillOmLocate)billOmLocateMapper.selectByPrimaryKey(locateKey);
				if(billOmLocate == null){
					throw new ServiceException(locate.getLocateNo()+"查找波茨信息失败!");
				}
				if(STATUS91.equals(billOmLocate.getStatus())){
					throw new ServiceException(locate.getLocateNo()+"波茨已经关闭,不能删除!");
				}
				
				//1.扣减除锁定当前客户的库存外多余出来都数量，释放预下
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", locate.getLocno());
				params.put("locateNo", locate.getLocateNo());
				List<BillOmOutstockDirect> subtractList = billOmOutstockDirectMapper.selectOutstockDirectSubtractContent(params);
				if(CommonUtil.hasValue(subtractList)){
					for (BillOmOutstockDirect sod : subtractList) {
						//扣减预下数量
						BillAccControlDto controlDto = new BillAccControlDto();
						controlDto.setiLocno(sod.getLocno());
						controlDto.setiOwnerNo(sod.getOwnerNo());
						controlDto.setiPaperNo(sod.getLocateNo());
						controlDto.setiPaperType(CNumPre.OM_LOCATE_PRE);
						controlDto.setiIoFlag("O");
						controlDto.setiCreator(loginName);
						controlDto.setiRowId(new BigDecimal(sod.getDirectSerial()));
						controlDto.setiCellNo(sod.getsCellNo());
						controlDto.setiCellId(new BigDecimal(sod.getsCellId()));
						controlDto.setiItemNo(sod.getItemNo());
						controlDto.setiSizeNo(sod.getSizeNo());
						controlDto.setiPackQty(sod.getPackQty());
						controlDto.setiSupplierNo(sod.getSupplierNo());
						controlDto.setiOutstockQty(new BigDecimal(0).subtract(sod.getItemQty()));
						controlDto.setiItemType(sod.getItemType());
						controlDto.setiQuality(sod.getQuality());
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
						dto.setiPaperNo(sod.getLocateNo());
						dto.setiLocType("2");
						dto.setiPaperType(CNumPre.OM_LOCATE_PRE);
						dto.setiIoFlag("O");
						dto.setiPrepareDataExt(new BigDecimal(sod.getDirectSerial()));
						dto.setiIsWeb(new BigDecimal(1));
						billAccControlService.procAccApply(dto);
					}
				}
				
				//2.释放掉波茨的所有预上
				List<BillOmOutstockDirect> directsList = billOmOutstockDirectMapper.selectDispatchNoOutstockRealQty(locate);
				if(CommonUtil.hasValue(directsList)){
					for (BillOmOutstockDirect d : directsList) {
						//扣减预上数量
						BillAccControlDto controlDtoIn = new BillAccControlDto();
						controlDtoIn.setiLocno(d.getLocno());
						controlDtoIn.setiOwnerNo(d.getOwnerNo());
						controlDtoIn.setiPaperNo(d.getLocateNo());
						controlDtoIn.setiPaperType(CNumPre.OM_LOCATE_PRE);
						controlDtoIn.setiIoFlag("I");
						controlDtoIn.setiCreator(d.getCreator());
						controlDtoIn.setiRowId(new BigDecimal(d.getDirectSerial()));
						controlDtoIn.setiCellNo(d.getdCellNo());
						controlDtoIn.setiCellId(new BigDecimal(d.getdCellId()));
						controlDtoIn.setiItemNo(d.getItemNo());
						controlDtoIn.setiSizeNo(d.getSizeNo());
						controlDtoIn.setiPackQty(d.getPackQty());
						controlDtoIn.setiSupplierNo(d.getSupplierNo());
						controlDtoIn.setiInstockQty(new BigDecimal(0).subtract(d.getLocateQty()));
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
					}
				}
				
				//3.回滚库存锁定中对应的主档、明细状态,已调度数
				Map<String, BillConStorelock> mapStorelock = new HashMap<String, BillConStorelock>();
				for (BillOmOutstockDirect d : directsList) {
					Map<String, Object> storeLockparams = new HashMap<String, Object>();
					storeLockparams.put("locno", d.getLocno());
					storeLockparams.put("ownerNo", d.getOwnerNo());
					storeLockparams.put("storeNo", d.getStoreNo());
					storeLockparams.put("itemNo", d.getItemNo());
					storeLockparams.put("sizeNo", d.getSizeNo());
					storeLockparams.put("cellNo", d.getsCellNo());
					storeLockparams.put("cellId", d.getsCellId());
					List<BillConStorelockDtl> storeLockDtlList = billConStorelockDtlMapper.selectOutstockLeftStoreLock(storeLockparams);
					boolean result = false;
					BigDecimal numIn = d.getLocateQty();//写入数量
					BigDecimal losePackNum = d.getLocateQty();//剩余需要取出的数量
					if(CommonUtil.hasValue(storeLockDtlList)){
						for (BillConStorelockDtl bcd : storeLockDtlList) {
							//把需要更新状态都锁定单号放入MAP
							if(mapStorelock.get(bcd.getStorelockNo()) == null){
								BillConStorelock storelock = new BillConStorelock();
								storelock.setLocno(bcd.getLocno());
								storelock.setOwnerNo(bcd.getOwnerNo());
								storelock.setStorelockNo(bcd.getStorelockNo());
								mapStorelock.put(bcd.getStorelockNo(), storelock);
							}
							//数量分摊,一个客户会有多个锁定单据，可能储位、商品相同，需要循环来扣减
							if(losePackNum.compareTo(bcd.getRealQty())==1){
								numIn = bcd.getRealQty();
								losePackNum = losePackNum.subtract(bcd.getRealQty());
							}else{
								numIn = losePackNum;
								result = true;
							}
							
							//验证是否出现扣减调度数量为负
							if(bcd.getRealQty().intValue()-numIn.intValue() < 0){
								throw new ServiceException(bcd.getStorelockNo()+"出现扣减客户锁定调度数量为负!");
							}
							
							//更新调度数量
							BillConStorelockDtl upStoreLockDtl = new BillConStorelockDtl();
							upStoreLockDtl.setLocno(bcd.getLocno());
							upStoreLockDtl.setOwnerNo(bcd.getOwnerNo());
							upStoreLockDtl.setStorelockNo(bcd.getStorelockNo());
							upStoreLockDtl.setRowId(bcd.getRowId());
							upStoreLockDtl.setRealQty(bcd.getRealQty().subtract(numIn));
							upStoreLockDtl.setStatus(STATUS10);
							int count = billConStorelockDtlMapper.updateByPrimaryKeySelective(upStoreLockDtl);
							if(count < 1){
								throw new ServiceException(bcd.getStorelockNo()+"更新库存锁定数量失败!");
							}
							if(result){
								break;
							}
						}
					}
				}
				
				//4.更新对应库存冻结单都主档状态为已审核
				if(mapStorelock != null){
					for(Map.Entry<String, BillConStorelock> entry: mapStorelock.entrySet()) {
						BillConStorelock storelock = entry.getValue();
						if(storelock != null){
							billConStorelockMapper.updateStockStatus(storelock);
//							int count = billConStorelockMapper.updateStockStatus(storelock);
//							if(count < 1){
//								throw new ServiceException(storelock.getStorelockNo()+"更新库存锁定状态失败!");
//							}
						}
					}
				}
				
				//5.更改发货通知单状态
				List<BillOmLocateDtl> locateDtlList = billOmLocateDtlMapper.selectLocateExpNoGroupBy(params);
				if(CommonUtil.hasValue(locateDtlList)){
					for (BillOmLocateDtl bd : locateDtlList) {
						BillOmExp exp = new BillOmExp();
						exp.setLocno(bd.getLocno());
						exp.setExpNo(bd.getExpNo());
						exp.setStatus(STATUS10);
						int count = billOmExpMapper.updateOmExpStatusByExpNo(exp);
						if(count < 1){
							throw new ServiceException(bd.getExpNo()+"更新发货通知单主档状态失败!");
						}
						
						BillOmExpDtl expDtl = new BillOmExpDtl();
						expDtl.setLocno(bd.getLocno());
						expDtl.setExpNo(bd.getExpNo());
						expDtl.setLocateQty(new BigDecimal(0));
						expDtl.setStatus(STATUS10);
						int ecount = billOmExpDtlMapper.updateOmExpDtlStatusByExpNo(expDtl);
						if(ecount < 1){
							throw new ServiceException(bd.getExpNo()+"更新发货通知单明细状态失败!");
						}
					}
				}
				
				//6.删除下架指示表
				BillOmOutstockDirect outstockDirect = new BillOmOutstockDirect();
				outstockDirect.setLocno(locate.getLocno());
				outstockDirect.setLocateNo(locate.getLocateNo());
				int count = billOmOutstockDirectMapper.deleteOutstockDirectByLocateNo(outstockDirect);
				if(count < 1){
					throw new ServiceException(locate.getLocateNo()+"波茨删除拣货下架指示信息失败!");
				}
				
				//7.删除波茨信息
				billOmLocateDtlMapper.deleteLocateDtlByLocateNo(billOmLocate);
				int d2 = billOmLocateMapper.deleteByPrimarayKeyForModel(billOmLocate);
				if(d2 < 1){
					throw new ServiceException(locate.getLocateNo()+"删除波茨主档信息失败!");
				}
			}
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}
	
}