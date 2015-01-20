package com.yougou.logistics.city.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.ContentStatusEnums;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.service.BillConAdjDtlService;
import com.yougou.logistics.city.service.BillConAdjService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ProcCommonService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
@Service("billConAdjManager")
class BillConAdjManagerImpl extends BaseCrudManagerImpl implements BillConAdjManager {
    @Resource
    private BillConAdjService billConAdjService;
	@Resource
    private BillConAdjDtlService billConAdjDtlService;
	@Resource
	CmDefcellService cmDefcellService;
	@Resource
	private ProcCommonService procCommonService;
	@Resource
	private BmContainerService bmContainerService;
    
    @Override
    public BaseCrudService init() {
        return billConAdjService;
    }
    
    @Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int deleteStockAdj(String ids,String editor) throws ManagerException {
    	int count = 0;
    	if(StringUtils.isNotBlank(ids)){
			String[] idArr = ids.split(",");
			for(String str : idArr){
				String[] tmp = str.split("-");
				if(tmp.length==3){
					try {
						String locno=tmp[0];
						String ownerNo=tmp[1];
						String adjNo=tmp[2];						
						
						//查询主档
						BillConAdj billParams=new BillConAdj();
						billParams.setAdjNo(adjNo);
						billParams.setLocno(locno);
						billParams.setOwnerNo(ownerNo);
						BillConAdj billConAdj=billConAdjService.findById(billParams);
						//整储位调整
						if(billConAdj == null){
							throw new ManagerException("单据"+adjNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
						}
						String cellAdjFlag=billConAdj.getCellAdjFlag();
						
						//如果是整储位调整，则需解冻储位,并调整储位的品质或属性
						if("1".equals(cellAdjFlag)){
							//查询明细中的储位
							Map<String,Object> dtlCellParams=new HashMap<String,Object>();
							dtlCellParams.put("locno", locno);
							dtlCellParams.put("adjNo", adjNo);
							dtlCellParams.put("ownerNo", ownerNo);
							List<String> cellList=billConAdjDtlService.findDtlCell(dtlCellParams);
							for(String cellNo:cellList){
								CmDefcell cdParams=new CmDefcell();
								cdParams.setLocno(locno);
								cdParams.setCellNo(cellNo);
								cdParams.setCellStatus("0");//解结			
								cmDefcellService.modifyById(cdParams);	
								//按储位解锁库存	
								procCommonService.UpdateContentStatus(locno, null, cellNo,null, null, "1", editor);
							}			
						}else{
							//查询库存调整明细在库存表中的ID，并解锁库存
							Map<String,Object> findConIdParams=new HashMap<String,Object>();
							findConIdParams.put("adjNo", adjNo);
							findConIdParams.put("locno", locno);
							findConIdParams.put("hmManualFlag", "0");
							List<String> conIdList=billConAdjDtlService.findConIdFromDtl(findConIdParams);
							for(String conId:conIdList){
								//按库存id解锁库存	
								procCommonService.UpdateContentStatus(locno, conId, null, null, null, "1", editor);
							}	
						}
						BillConAdj model = new BillConAdj();
						model.setLocno(locno);
						model.setOwnerNo(ownerNo);
						model.setAdjNo(adjNo);
						model.setUpdStatus("10");
						BillConAdjDtl key = new BillConAdjDtl();
						key.setLocno(locno);
						key.setOwnerNo(ownerNo);
						key.setAdjNo(adjNo);
						int result = billConAdjService.deleteById(model);
						if(result > 0 ){
							//解锁容器占用状态
							Map<String,Object> billAdjMap=new HashMap<String,Object>();
							billAdjMap.put("locno", locno);
							billAdjMap.put("adjNo", adjNo);
							billAdjMap.put("status", ContentStatusEnums.STATUS_0.getStatus());
							billAdjMap.put("optBillType", "");
							billAdjMap.put("optBillNo", "");
							billAdjMap.put("editor", editor);
							bmContainerService.batchStatusByBillConAdj(billAdjMap);
							billConAdjService.deleteStockAdjDetail(key);
							
						}else{
							throw new ManagerException("单据"+adjNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
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
	public Map<String, Object> selectSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try{
			return this.billConAdjService.selectSumQty(params,authorityParams);
		}catch (ServiceException e) {
			throw new ManagerException(e);
		}
		 
	}
}