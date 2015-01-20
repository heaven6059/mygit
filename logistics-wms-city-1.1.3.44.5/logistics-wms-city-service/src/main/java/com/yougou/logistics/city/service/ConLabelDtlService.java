package com.yougou.logistics.city.service;

import java.util.List;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.CnLabelQueryDto;
import com.yougou.logistics.city.common.model.ConLabelDtl;

/*
 * 请写出类的用途 
 * @author qin.dy
 * @date  Mon Sep 30 15:10:42 CST 2013
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
public interface ConLabelDtlService extends BaseCrudService {
	
	public  List<ConLabelDtl>  selectItemInfoByLabelNo(CnLabelQueryDto cnLabelQueryDto)throws ServiceException;
	
	/**
	 * 更新标签明细状态
	 * @param conLabelDtl
	 * @return
	 * @throws DaoException
	 */
	public int modifyLabelDtlStatusByLabelNo(ConLabelDtl conLabelDtl)throws ServiceException;
}