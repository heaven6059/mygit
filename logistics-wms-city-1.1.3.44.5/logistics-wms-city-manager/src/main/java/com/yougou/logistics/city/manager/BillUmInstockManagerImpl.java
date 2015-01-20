package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.BillUmInstockStatusEnums;
import com.yougou.logistics.city.common.model.BillUmInstock;
import com.yougou.logistics.city.common.model.BillUmInstockDtl;
import com.yougou.logistics.city.common.model.ConContentMove;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.service.BillUmInstockDtlService;
import com.yougou.logistics.city.service.BillUmInstockService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ConContentMoveService;
import com.yougou.logistics.city.service.ProcCommonService;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2014-01-17 20:35:58
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
@Service("billUmInstockManager")
class BillUmInstockManagerImpl extends BaseCrudManagerImpl implements
	BillUmInstockManager {
    @Resource
    private BillUmInstockService billUmInstockService;
    @Resource
    private BillUmInstockDtlService billUmInstockDtlService;
    @Resource
    private ConContentMoveService conContentMoveService;
    @Resource
    private ConBoxService conBoxService;

    @Resource
    private ProcCommonService procCommonService;

    @Override
    public BaseCrudService init() {
	return billUmInstockService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public String check(String locno, String instockNo, String ownerNo, SystemUser user) throws ManagerException {
	try {
		Date dd = new Date();
		String oper = user.getLoginName();
	    int a = 0;

	    // 将改变主表状态放到前面，如果已经审核的单据，就不能审核，后面的记库存也不用操作了 luo.hl 2014-03-05
	    // 2改状态
	    BillUmInstock billUmInstock = new BillUmInstock();
	    billUmInstock.setLocno(locno);
	    billUmInstock.setOwnerNo(ownerNo);
	    billUmInstock.setInstockNo(instockNo);
	    billUmInstock.setSourceStatus(BillUmInstockStatusEnums.STATUS10
		    .getStatus());
	    billUmInstock.setStatus(BillUmInstockStatusEnums.STATUS13
		    .getStatus());
	    // 审核人，审核时间
	    billUmInstock.setAudittm(dd);
	    billUmInstock.setAuditor(oper);
	    billUmInstock.setAuditorName(user.getUsername());
	    a = billUmInstockService.modifyById(billUmInstock);
	    if (a < 1) {
	    	throw new ManagerException("更新退仓上架单状态异常");
	    }

	    // 0判断是否有明细的实际数量为null
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("locno", locno);
	    params.put("instockNo", instockNo);
	    params.put("ownerNo", ownerNo);
	    List<BillUmInstockDtl> list = billUmInstockDtlService.findByBiz(
		    null, params);
	    boolean isN = false;
	    if (list == null || list.size() == 0) {
	    	throw new ManagerException("不存在明细");
	    } else {
		for (BillUmInstockDtl dtl : list) {
		    if (dtl.getRealQty() == null || dtl.getRealCellNo() == null) {
		    	dtl.setRealCellNo(dtl.getDestCellNo());
		    	dtl.setRealQty(new BigDecimal(0));
		    	//throw new ManagerException("存在没有确认的明细");
		    }
		    dtl.setInstockDate(billUmInstock.getAudittm());
	    	dtl.setInstockWorker(billUmInstock.getAuditor());
	    	dtl.setInstockName(billUmInstock.getAuditorName());
	    	billUmInstockDtlService.modifyById(dtl);
	    	
		    if (!dtl.getRealCellNo().equals(dtl.getDestCellNo())) {
		    	isN = true;
		    }
		}
	    }
	    // 1记账
	    procCommonService.procAccApply(instockNo, "2", "UP", "O", 0);
	    procCommonService.procAccApply(instockNo, "2", "UP", "I", 0);

	    Map<String, Object> param1 = new HashMap<String, Object>();
	    param1.put("locno", locno);
	    param1.put("paperNo", instockNo);
	    param1.put("paperType", "UP");
	    param1.put("ioFlag", "I");
	    List<ConContentMove> conList = this.conContentMoveService
		    .selectConContentMoveBoxNo(param1);
	    if (conList != null) {
		for (ConContentMove move : conList) {
		    Map<String, Object> subMap = new HashMap<String, Object>();
		    subMap.put("locno", locno);
		    subMap.put("cellNo", move.getCellNo());
		    subMap.put("boxNo", move.getBoxNo());
		    this.conBoxService.updateCellNoByBoxNo(subMap);
		}
	    }
	    // 如果预上储位和实际储位不一致，需要调用移库的存储过程
	    if (isN) {
		// 加上移库的逻辑
		procCommonService.procAccApply(instockNo, "2", "MO", "O", 0);
		// ACC_APPLY(I_PAPER_NO,'2','MO','O','0','1');
	    }
	    // 查询三记账 根据 单号更新箱码储位
	    Map<String, Object> param2 = new HashMap<String, Object>();
	    param2.put("locno", locno);
	    param2.put("paperNo", instockNo);
	    param2.put("paperType", "MO");
	    param2.put("ioFlag", "O");
	    List<ConContentMove> conList2 = this.conContentMoveService
		    .selectConContentMoveBoxNo(param2);
	    if (conList != null) {
		for (ConContentMove move : conList2) {
		    Map<String, Object> subMap = new HashMap<String, Object>();
		    subMap.put("locno", locno);
		    subMap.put("cellNo", move.getCellNo());
		    subMap.put("boxNo", move.getBoxNo());
		    this.conBoxService.updateCellNoByBoxNo(subMap);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ManagerException(e.getMessage(),e);
	}

	return null;
    }

    @Override
    public Map<String, Object> selectSumQty(Map<String, Object> params,
            AuthorityParams authorityParams) throws ManagerException {
        try{
            return this.billUmInstockService.selectSumQty(params, authorityParams);
        } catch (ServiceException e) {
            throw new ManagerException(e.getMessage());
        }
    }
}