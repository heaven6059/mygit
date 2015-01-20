package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.yougou.logistics.city.common.model.BillSmOtherin;
import com.yougou.logistics.city.common.model.BillSmOtherinDtl;
import com.yougou.logistics.city.common.model.BillSmOtherinPrintDto;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.mapper.CmDefcellMapper;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillSmOtherinDtlService;
import com.yougou.logistics.city.service.BillSmOtherinService;
import com.yougou.logistics.city.service.SizeInfoService;

/*
 * 请写出类的用途 
 * @author yougoupublic
 * @date  Fri Feb 21 20:40:24 CST 2014
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
@Service("billSmOtherinManager")
class BillSmOtherinManagerImpl extends BaseCrudManagerImpl implements BillSmOtherinManager {
    @Resource
    private BillSmOtherinService billSmOtherinService;    
    @Resource
    private BillSmOtherinDtlService billSmOtherinDtlService;
    @Resource
    private BillAccControlService billAccControlService;
    
    @Resource
	private CmDefcellMapper cmDefcellMapper;
    
    @Resource
    private SizeInfoService sizeInfoService;

    @Override
    public BaseCrudService init() {
        return billSmOtherinService;
    }
    @Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int deleteBatch(String ids) throws ManagerException {
		int count = 0;
		if(StringUtils.isNotBlank(ids)){
			String[] idArr = ids.split(",");
			for(String str : idArr){
				String[] tmp = str.split("-");
				if(tmp.length==3){
					BillSmOtherin key = new BillSmOtherin();
					key.setLocno(tmp[0]);
					key.setOwnerNo(tmp[1]);
					key.setOtherinNo(tmp[2]);
					key.setUpdStatus("10");
					try {
						int result = billSmOtherinService.deleteById(key);
						if(result >0 ){
							billSmOtherinService.deleteDtlById(key);
						}else{
							throw new ManagerException("单据"+tmp[2]+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
						}
						count++;
					} catch (ServiceException e) {
						throw new ManagerException(e);
					}
				}
			}
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String, Object> checkBillSmOtherin(String ids, String auditor,String userName)
			throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			boolean flag = false;
			if(StringUtils.isNotBlank(ids)){
				String[] idArr = ids.split(",");
				for(String id : idArr){
					flag = false;
					String[] tmp = id.split("-");
					if(tmp.length==3){
						String locno = tmp[0];
						String ownerNo = tmp[1];
						String otherinNo = tmp[2];
						Map<String, Object> bill = new HashMap<String, Object>();
						bill.put("locno", locno);
						bill.put("ownerNo", ownerNo);
						bill.put("otherinNo", otherinNo);
						bill.put("status", "10");
						BillSmOtherin billSmOtherin = new BillSmOtherin();
						billSmOtherin.setLocno(locno);
						billSmOtherin.setOwnerNo(ownerNo);
						billSmOtherin.setOtherinNo(otherinNo);
						billSmOtherin.setStatus("10");
						//查询主表
						//List<BillSmOtherin> param = billSmOtherinService.findByBiz(billSmOtherin,bill);
						//if(param.size() > 0) {
							BillSmOtherinDtl billSmOtherinDtl = new BillSmOtherinDtl();
							billSmOtherinDtl.setLocno(locno);
							billSmOtherinDtl.setOwnerNo(ownerNo);
							billSmOtherinDtl.setOtherinNo(otherinNo);
							//查询明细
							List<BillSmOtherinDtl> query = billSmOtherinDtlService.findByBiz(billSmOtherinDtl,bill);
							String STAUTS0 = "0";
							for(BillSmOtherinDtl str: query) {
								String cellNo = str.getCellNo();
								Map<String, Object> parmMap = new HashMap<String, Object>();
								parmMap.put("locno", locno);
								parmMap.put("cellNo", cellNo);
								List<CmDefcell> list = this.cmDefcellMapper.selectByParams4Instock(parmMap);
								if (list == null || list.size() == 0) {
									throw new ManagerException("实际储位不存在！");
								}
								if (list.size() > 1) {
									throw new ManagerException(cellNo + "储位非法！");
								}
								CmDefcell cell = list.get(0);
								if (!STAUTS0.equals(cell.getCellStatus())) {
									throw new ManagerException("储位状态必须为可用！");
								}
								if (!STAUTS0.equals(cell.getCheckStatus())) {
									throw new ManagerException("储位必须是非盘点状态！");
								}
							}
							if(query.size() > 0) {								
								billSmOtherin.setStatus("13");
								billSmOtherin.setAuditor(auditor);
								billSmOtherin.setAuditorName(userName);
								billSmOtherin.setAudittm(new Date());
								billSmOtherin.setUpdStatus("10");
								int count = billSmOtherinService.modifyById(billSmOtherin);
								if(count != 1) {
									//throw new ManagerException("其它入库单状态更新失败！");
									throw new ManagerException("单据"+otherinNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
								} else {
									flag = true;
								}
								Map<String, Object> _map = new HashMap<String, Object>();
								/*LOCNO, OWNER_NO, OTHERIN_NO*/
								_map.put("locno", locno);
								_map.put("ownerNo", ownerNo);
								_map.put("otherinNo", otherinNo);
								_map.put("editor", auditor);
								_map.put("editorName", userName);
								_map.put("edittm", new Date());
								billSmOtherinDtlService.updateOperateRecord(_map);
								//调用统一记账方法
								//procCommonService.procAccApply(wasteNo, "2", "SW", "O", 0);
								BillAccControlDto controlDto = new BillAccControlDto();
								controlDto.setiPaperNo(otherinNo);
								controlDto.setiLocType("2");
								controlDto.setiPaperType(CNumPre.SM_OTHERIN_PRE);
								controlDto.setiIoFlag("I");
								controlDto.setiPrepareDataExt(new BigDecimal(0));
								controlDto.setiIsWeb(new BigDecimal(1));
								billAccControlService.procAccApply(controlDto);
								
							} else {
								throw new ManagerException("其它入库单不存在明细,不允许审核！");
							}
						/*} else {
							throw new ManagerException("当前单据状态不可编辑！");
						}*/
					}
				}
			}
			if(flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "审核成功");
			} else {
				throw new ManagerException("当前单据审核失败！");
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}
	@Override
	public Map<String, Object> findSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) {
		return billSmOtherinService.findSumQty(params, authorityParams);
	}
	@Override
	public Map<String, Object> print(String nos, String locno, SystemUser user) {
		Map<String, Object> obj = new HashMap<String, Object>();
		if(!CommonUtil.hasValue(nos) || !CommonUtil.hasValue(nos)){
			obj.put("status", "error");
			obj.put("msg", "缺少参数!");
			return obj;
		}
		List<Object> rows = new ArrayList<Object>();
		String [] noArr = nos.split(",");
		Map<String, List<String>> allSizeKind = new TreeMap<String, List<String>>();//缓存所有单据明细中所需要的尺码表 key=sizeKind+sysNo,value=sizeCodeList
		for(String otherinNo:noArr){
			
			Map<String, Object> page = joinDataByItem(otherinNo, locno, allSizeKind);
			if(CommonUtil.hasValue(page)){				
				rows.add(page);
			}
		}
		if(CommonUtil.hasValue(rows)){				
			obj.put("status", "success");
			obj.put("rows", rows);
		}else{			
			obj.put("status", "error");
			obj.put("msg", "没有需要打印的数据!");
		}
		
		return obj;
	}
	private Map<String, Object> joinDataByItem(String otherinNo, String locno,Map<String, List<String>> allSizeKind){
		Map<String, Object> queryDtlParams = new HashMap<String, Object>();//
		queryDtlParams.put("locno", locno);
		queryDtlParams.put("otherinNo", otherinNo);
		List<BillSmOtherinPrintDto> list = billSmOtherinDtlService.findPrintDtl4Size(queryDtlParams);
		Map<String, Integer> sizeQtyMap;
		Map<String, BillSmOtherinPrintDto> row = new HashMap<String, BillSmOtherinPrintDto>();
		Map<String, Object> sizeCodeQueryParams = new HashMap<String, Object>();
		if(CommonUtil.hasValue(list)){
			Map<String, Object> sizeMap = new TreeMap<String, Object>();//每个单据对应的size列表,key=sizeKind value=sizeCodeList
			int sizeMaxLength = 0;//所有尺码数组中最大的数组长度
			int total = 0;//每一张单据的总数量
			String creatorName = null;
			String auditorName = null;
			for(BillSmOtherinPrintDto dto:list){
				String cellNo = dto.getCellNo();
				String itemNo = dto.getItemNo();
				String key = cellNo+"_"+itemNo;
				BillSmOtherinPrintDto first;
				total = total + dto.getInstorageQty().intValue();
				if((first=row.get(key)) != null){
					first.getSizeQtyMap().put(dto.getSizeNo().substring(2), dto.getInstorageQty().intValue());
					first.setTotalQty(first.getTotalQty()+dto.getInstorageQty().intValue());
				}else{
					sizeQtyMap = new TreeMap<String, Integer>();
					sizeQtyMap.put(dto.getSizeNo().substring(2), dto.getInstorageQty().intValue());
					dto.setTotalQty(dto.getInstorageQty().intValue());
					dto.setSizeQtyMap(sizeQtyMap);
					dto.setSizeNo(null);
					dto.setInstorageQty(null);
					creatorName = dto.getCreatorName();
					auditorName = dto.getAuditorName();
					dto.setAuditorName(null);
					dto.setCreatorName(null);
					
					row.put(key, dto);
				}
				//获取size数组
				String sysNo = dto.getSysNo();
				String sizeKind = dto.getSizeKind();
				
				if(sizeMap.get(sizeKind) == null){
					List<String> sizeArr = null;
					if((sizeArr = allSizeKind.get(sizeKind+"_"+sysNo)) == null){
						sizeCodeQueryParams.put("sizeKind", sizeKind);
						sizeCodeQueryParams.put("sysNo", sysNo);
						sizeArr = sizeInfoService.findSizeCodeBySysAndKind(sizeCodeQueryParams);
						allSizeKind.put(sizeKind+"_"+sysNo,sizeArr);
					}
					if(sizeArr.size() > sizeMaxLength){
						sizeMaxLength = sizeArr.size();
					}
					sizeMap.put(sizeKind, sizeArr);
				}
			}
			/*test S*/
				//sizeMap.put("E", sizeMap.get("A"));
			/*test E*/
			Map<String, Object> page = new HashMap<String, Object>();
			page.put("list", row.values());
			page.put("otherinNo", otherinNo);
			page.put("creatorName", creatorName);
			page.put("auditorName", auditorName);
			page.put("total", total);
			page.put("sizeList", sizeMap);
			page.put("sizeMaxLength", sizeMaxLength);
			return page;
		}
		return null;
	}
}