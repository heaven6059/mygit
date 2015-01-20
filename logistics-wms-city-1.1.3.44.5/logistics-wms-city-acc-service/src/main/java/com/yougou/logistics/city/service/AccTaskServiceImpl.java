package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AccTask;
import com.yougou.logistics.city.common.vo.AccCheckDtlVo;
import com.yougou.logistics.city.dal.database.AccCheckDtlMapper;
import com.yougou.logistics.city.dal.database.AccTaskMapper;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-23 18:31:36
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
@Service("accTaskService")
class AccTaskServiceImpl extends BaseCrudServiceImpl implements AccTaskService {
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger("SKUCONSTOCK"); 
    @Resource
    private AccTaskMapper accTaskMapper;
    @Resource
    private AccInventorySkuBookService accInventorySkuBookService;
    @Resource
    private AccCheckDtlMapper accCheckDtlMapper;
    
    
    @Override
    public BaseCrudMapper init() {
        return accTaskMapper;
    }

	@Override
	public synchronized void executeAcctask() throws ServiceException {
		try {
			//查询未处理的任务(一条整单及全部明细)进行记账
			List<AccTask> list=accTaskMapper.selectLatestListByAccType(new HashMap<String,Object>());
			log.info("executeAcctask() tasklist size:"+list.size());
			StringBuffer liststr=new StringBuffer();
			for (AccTask tmp : list) {
				liststr.append(" SeqId:"+tmp.getSeqId()+" BillNo:"+tmp.getBillNo()+"\r\n");
				if (tmp.getAccType().equals("0")) {
					accInventorySkuBookService.accountForSkuConByBillTran(tmp);
				} else {
					accInventorySkuBookService
							.accountForSkuConByDetailRowIdTran(tmp);
				}
			}
			log.info("executeAcctask() end liststr:"+liststr.toString());
		} catch (Exception e) {
			log.error("executeAcctask()",e);
		}
	}
	

	@Override
	public void testqueryAccCheckDtlVoList(Map<String, Object> params)
			throws ServiceException {
		try {
			List<AccCheckDtlVo> list=accCheckDtlMapper.selectAccCheckDtlByParams(params);
			log.info("testqueryAccCheckDtlVoList end. size:"+list.size());
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
	}
}