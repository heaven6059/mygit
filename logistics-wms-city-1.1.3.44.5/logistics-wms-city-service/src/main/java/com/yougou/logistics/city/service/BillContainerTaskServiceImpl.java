package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillContainerTask;
import com.yougou.logistics.city.common.model.BillContainerTaskDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillContainerTaskDtlMapper;
import com.yougou.logistics.city.dal.database.BillContainerTaskMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author su.yq
 * @date  2014-10-21 11:01:28
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
@Service("billContainerTaskService")
class BillContainerTaskServiceImpl extends BaseCrudServiceImpl implements BillContainerTaskService {

	@Resource
	private BillContainerTaskMapper billContainerTaskMapper;

	@Resource
	private BillContainerTaskDtlMapper billContainerTaskDtlMapper;

	@Override
	public BaseCrudMapper init() {
		return billContainerTaskMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public int insertBillContainerTask(BillContainerTask containerTask, List<BillContainerTaskDtl> taskDtlList)
			throws ServiceException {
		int num = 0;
		try{
			int count = billContainerTaskMapper.insertSelective(containerTask);
			if(count > 0){
				if(CommonUtil.hasValue(taskDtlList)){
					int pageNum = 100;
					for(int idx=0;idx<taskDtlList.size();){
						idx += pageNum;
						if(idx > taskDtlList.size()){
							billContainerTaskDtlMapper.batchInsertDtl(taskDtlList.subList(idx-pageNum, taskDtlList.size()));
						}else{
							billContainerTaskDtlMapper.batchInsertDtl(taskDtlList.subList(idx-pageNum, idx));
						}
					}
				}
				num = count;
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return num;
	}
}