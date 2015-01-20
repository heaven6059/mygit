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

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.enums.BillConvertStatusEnums;
import com.yougou.logistics.city.common.enums.BillSmOtherinStatusEnums;
import com.yougou.logistics.city.common.enums.BillSmOtherinTypeEnums;
import com.yougou.logistics.city.common.model.BillConConvert;
import com.yougou.logistics.city.common.model.BillConConvertDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillConConvertDtlService;
import com.yougou.logistics.city.service.BillConConvertService;
import com.yougou.logistics.city.service.BillOmRecheckDtlService;
import com.yougou.logistics.city.service.BillOmRecheckService;
import com.yougou.logistics.city.service.BillSmOtherinDtlService;
import com.yougou.logistics.city.service.BillSmOtherinService;
import com.yougou.logistics.city.service.ProcCommonService;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Thu Jun 05 10:15:19 CST 2014
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
@Service("billConConvertManager")
class BillConConvertManagerImpl extends BaseCrudManagerImpl implements BillConConvertManager {
    @Resource
    private BillConConvertService billConConvertService;

    @Resource
	private BillAccControlService accControlService;
    
    @Resource
    private BillConConvertDtlService billConConvertDtlService;
    
    @Resource
    private ProcCommonService procCommonService;
    
    @Resource
    private BillSmOtherinService billSmOtherinService;
    
    @Resource
    private BillSmOtherinDtlService billSmOtherinDtlService;
    
    @Resource
    private BillOmRecheckService billOmRecheckService;
    
    @Resource
    private BillOmRecheckDtlService billOmRecheckDtlService;
    
    private final static String STATUS10 = "10";
    
    private final static String SOURCETYPE2 = "2";
    
    private final static String RECHECKTYPE1 = "1";
    
    @Override
    public BaseCrudService init() {
        return billConConvertService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String removeMainAndDtl(String keyStr) throws ManagerException {
		try {
			int operand = 0;
			if(StringUtils.isBlank(keyStr)){
				throw new ManagerException("缺少删除记录的主键");
			}
			String [] keyStrArray = keyStr.split("\\|");
			String [] keyArray = null;
			String locno = null;
			String ownerNo = null;
			String convertNo = null;
			BillConConvert bcc = new BillConConvert();
			BillConConvertDtl bccd = new BillConConvertDtl();
			for(String key:keyStrArray){
				keyArray = key.split("_");
				locno = keyArray[0];
				ownerNo = keyArray[1];
				convertNo = keyArray[2];
				bcc.setLocno(locno);
				bcc.setOwnerNo(ownerNo);
				bcc.setConvertNo(convertNo);
				bcc.setUpdStatus("10");
				operand = billConConvertService.deleteById(bcc);
				if(operand <= 0){
					throw new ManagerException("单据"+convertNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
				bccd.setLocno(locno);
				bccd.setOwnerNo(ownerNo);
				bccd.setConvertNo(convertNo);
				billConConvertDtlService.deleteById(bccd);
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public String check(String keyStr, String operator,int type,String storeNoLoc,String auditorName) throws ManagerException {
		try {
			int operand = 0;
			if(StringUtils.isBlank(keyStr)){
				throw new ManagerException("缺少记录的主键");
			}
			if(StringUtils.isBlank(operator)){
				throw new ManagerException("缺少审核人");
			}
			String [] keyStrArray = keyStr.split("\\|");
			String [] keyArray = null;
			String locno = null;
			String ownerNo = null;
			String convertNo = null;
			String storeNo = null;
			BillConConvert bcc = new BillConConvert();
			
			Map<String, Object> params = new HashMap<String, Object>();
			List<BillConConvertDtl> dtlList = null;
			BillAccControlDto controlDto = new BillAccControlDto();
			Date now = new Date();
			for(String key:keyStrArray){
				keyArray = key.split("_");
				locno = keyArray[0];
				ownerNo = keyArray[1];
				convertNo = keyArray[2];
				storeNo = keyArray[3];
				//检验是否存在明细
				params.put("locno", locno);
				params.put("ownerNo", ownerNo);
				params.put("convertNo", convertNo);
				dtlList = billConConvertDtlService.findByBiz(null, params);
				if(!CommonUtil.hasValue(dtlList)){
					throw new ManagerException("单据【"+convertNo+"】缺少明细,不能审核");
				}
				
				bcc.setLocno(locno);
				bcc.setOwnerNo(ownerNo);
				bcc.setConvertNo(convertNo);
				bcc.setStatus(BillConvertStatusEnums.STATUS_11.getValue());
				bcc.setAuditor(operator);
				bcc.setAuditorName(auditorName);
				bcc.setAudittm(now);
				bcc.setUpdStatus("10");
				operand = billConConvertService.modifyById(bcc);
				if(operand <= 0){
					throw new ManagerException("单据"+convertNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
				//转货记账
				controlDto.setiPaperNo(convertNo);
				controlDto.setiLocType("2");
				controlDto.setiPaperType("CV");
				controlDto.setiIoFlag("O");
				controlDto.setiPrepareDataExt(new BigDecimal(0));
				controlDto.setiIsWeb(new BigDecimal(1));
				accControlService.procAccApply(controlDto);
				
				//生成转入仓库的其他入库单：生成转入仓库其他入库单，入库类型5-部门转货，单据状态默认为13-已确认
				String quality = dtlList.get(0).getQuality();//跨部门转货同一单所有明细的品质是相同的
				String itemType = dtlList.get(0).getItemType();//跨部门转货同一单所有明细的商品属性是相同的
				String otherinNo = procCommonService.procGetSheetNo(storeNo, CNumPre.SM_OTHERIN_PRE);
				BillSmOtherin billSmOtherin = new BillSmOtherin();
				billSmOtherin.setLocno(storeNo);
				billSmOtherin.setOwnerNo(ownerNo);
				billSmOtherin.setOtherinType(BillSmOtherinTypeEnums.VALUE_5.getValue());
				billSmOtherin.setOtherinNo(otherinNo);
				billSmOtherin.setStatus(BillSmOtherinStatusEnums.VALUE_13.getValue());
				billSmOtherin.setInstorageDate(now);
				billSmOtherin.setAreaQuality(quality);
				billSmOtherin.setItemType(itemType);
				billSmOtherin.setCreator(operator);
				billSmOtherin.setCreatorName(auditorName);
				billSmOtherin.setCreatetm(now);
				billSmOtherin.setEditor(operator);
				billSmOtherin.setEditorName(auditorName);
				billSmOtherin.setEdittm(now);
				billSmOtherin.setAuditor(operator);
				billSmOtherin.setAuditorName(auditorName);
				billSmOtherin.setAudittm(now);
				billSmOtherin.setPoNo(convertNo);//来源仓库的跨部门转货单号，作为目的仓库的其他入库单的单号
				operand = billSmOtherinService.add(billSmOtherin);
				if(operand <= 0){
					throw new ManagerException("生成其他入库单异常");
				}
				List<BillSmOtherinDtl> sodList = billConConvertDtlToBillSmOtherinDtl(dtlList, storeNo, ownerNo, otherinNo);
				
				//批量
				int pageNum = 100;
				for(int idx=0;idx<sodList.size();){
					idx += pageNum;
					if(idx > sodList.size()){
						billSmOtherinDtlService.batchInsertDtl(sodList.subList(idx-pageNum, sodList.size()));
					}else{
						billSmOtherinDtlService.batchInsertDtl(sodList.subList(idx-pageNum, idx));
					}
				}
				//其他入库记账
				controlDto = new BillAccControlDto();
				controlDto.setiPaperNo(otherinNo);
				controlDto.setiLocType("2");
				controlDto.setiPaperType(CNumPre.SM_OTHERIN_PRE);
				controlDto.setiIoFlag("I");
				controlDto.setiPrepareDataExt(new BigDecimal(0));
				controlDto.setiIsWeb(new BigDecimal(1));
				accControlService.procAccApply(controlDto);
				
				//生成转货复核单
				if(type > 0){
					String recheckNo = procCommonService.procGetSheetNo(billSmOtherin.getLocno(), CNumPre.RECHECK_PRE);
					int count = addConvertRecheck(billSmOtherin,storeNoLoc,recheckNo);
					if(count < 1){
						throw new ManagerException("生成转货复核单失败!");
					}
					
					//生成复核单明细
					Map<String, Object> otherinParams = new HashMap<String, Object>();
					otherinParams.put("locno", storeNo);
					otherinParams.put("ownerNo", ownerNo);
					otherinParams.put("otherinNo", otherinNo);
					otherinParams.put("recheckNo", recheckNo);
					otherinParams.put("recheckName", operator);
					int dcount = billOmRecheckDtlService.insertRecheckDtl4SmOtherin(otherinParams);
					if(dcount < 1){
						throw new ManagerException("新增复核单明细失败!");
					}
					//更新其他入库单明细复核数量=入库数量
					billSmOtherinDtlService.updateRecheckQty4Convert(otherinParams);
				}
			}
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 生成转货复核
	 * @param otherin
	 * @throws ServiceException 
	 */
	public int addConvertRecheck(BillSmOtherin otherin,String storeNoLoc,String recheckNo) throws ServiceException{
		//String recheckNo = procCommonService.procGetSheetNo(otherin.getLocno(), CNumPre.RECHECK_PRE);
		BillOmRecheck recheck = new BillOmRecheck();
		recheck.setLocno(otherin.getLocno());
		recheck.setRecheckNo(recheckNo);
		recheck.setStoreNo(storeNoLoc);
		recheck.setStatus(STATUS10);
		recheck.setCreator(otherin.getCreator());
		recheck.setCreatetm(new Date());
		recheck.setEditor(otherin.getCreator());
		recheck.setEdittm(new Date());
		recheck.setExpDate(CommonUtil.getCurrentDateYmd());
		recheck.setDivideNo(otherin.getOtherinNo());
		recheck.setSourceType(SOURCETYPE2);
		recheck.setRecheckType(RECHECKTYPE1);
		int count = billOmRecheckService.add(recheck);
		return count;
	}
	
	private List<BillSmOtherinDtl> billConConvertDtlToBillSmOtherinDtl(List<BillConConvertDtl> list,String locno,String ownerNo,String otherinNo){
		if(!CommonUtil.hasValue(list)){
			return null;
		}
		List<BillSmOtherinDtl> rs = new ArrayList<BillSmOtherinDtl>();
		BillSmOtherinDtl sod = null;
		short rowId = 1;
		for(BillConConvertDtl ccd:list){
			sod = new BillSmOtherinDtl();
			sod.setLocno(locno);
			sod.setOwnerNo(ownerNo);
			sod.setOtherinNo(otherinNo);
			sod.setRowId(rowId++);
			sod.setCellNo(ccd.getDestCellNo());
			sod.setItemNo(ccd.getItemNo());
			sod.setSizeNo(ccd.getSizeNo());
			sod.setInstorageQty(ccd.getRealyQty());
			sod.setBrandNO(ccd.getBrandNo());
			rs.add(sod);
		}
		return rs;
	}

	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billConConvertService.findSumQty(params, authorityParams);
	}
}