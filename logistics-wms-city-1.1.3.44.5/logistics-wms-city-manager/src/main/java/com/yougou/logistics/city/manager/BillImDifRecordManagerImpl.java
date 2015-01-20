package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillImDifRecord;
import com.yougou.logistics.city.common.model.BillImDifRecordDtl;
import com.yougou.logistics.city.service.BillImDifRecordDtlService;
import com.yougou.logistics.city.service.BillImDifRecordService;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
@Service("billImDifRecordManager")
class BillImDifRecordManagerImpl extends BaseCrudManagerImpl implements BillImDifRecordManager {
    @Resource
    private BillImDifRecordService billImDifRecordService;
    @Resource
    private BillImDifRecordDtlService billImDifRecordDtlService;
    
    @Override
    public BaseCrudService init() {
        return billImDifRecordService;
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
					BillImDifRecord key = new BillImDifRecord();
					key.setLocno(tmp[0]);
					key.setOwnerNo(tmp[1]);
					key.setDefRecordNo(tmp[2]);
					try {
						billImDifRecordService.deleteById(key);
						billImDifRecordService.deleteDtlById(key);
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
	public Map<String, Object> checkImDifRecord(String ids, String auditor)
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
						String defRecordNo = tmp[2];
						Map<String, Object> bill = new HashMap<String, Object>();
						bill.put("locno", locno);
						bill.put("ownerNo", ownerNo);
						bill.put("defRecordNo", defRecordNo);
						bill.put("status", "10");
						BillImDifRecord billImDifRecord = new BillImDifRecord();
						billImDifRecord.setLocno(locno);
						billImDifRecord.setOwnerNo(ownerNo);
						billImDifRecord.setDefRecordNo(defRecordNo);
						billImDifRecord.setStatus("10");
						//查询主表
						List<BillImDifRecord> param = billImDifRecordService.findByBiz(billImDifRecord,bill);
						if(param.size() > 0) {
							BillImDifRecordDtl billImDifRecordDtl = new BillImDifRecordDtl();
							billImDifRecordDtl.setLocno(locno);
							billImDifRecordDtl.setOwnerNo(ownerNo);
							billImDifRecordDtl.setDefRecordNo(defRecordNo);
							//查询明细
							List<BillImDifRecordDtl> query = billImDifRecordDtlService.findByBiz(billImDifRecordDtl,bill);
							if(query.size() > 0) {
								for(BillImDifRecordDtl vo : query) {
									vo.setStatus("11");
									billImDifRecordDtlService.modifyById(vo);
								}
								billImDifRecord.setStatus("11");
								billImDifRecord.setAuditor(auditor);
								billImDifRecord.setAudittm(new Date());
								int count = billImDifRecordService.modifyById(billImDifRecord);
								if(count != 1) {
									throw new ManagerException("收货差异单审核失败！");
								} else {
									flag = true;
								}
							} else {
								throw new ManagerException("收货差异不存在明细,不允许审核！");
							}
						} else {
							throw new ManagerException("当前单据状态不可编辑！");
						}
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
}