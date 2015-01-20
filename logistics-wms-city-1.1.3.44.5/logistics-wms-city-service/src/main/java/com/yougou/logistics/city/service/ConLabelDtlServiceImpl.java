package com.yougou.logistics.city.service;

import java.util.List;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.CnLabelQueryDto;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.dal.mapper.ConLabelDtlMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

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
@Service("conLabelDtlService")
class ConLabelDtlServiceImpl extends BaseCrudServiceImpl implements ConLabelDtlService {
    @Resource
    private ConLabelDtlMapper conLabelDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return conLabelDtlMapper;
    }

	@Override
	public List<ConLabelDtl> selectItemInfoByLabelNo(
			CnLabelQueryDto cnLabelQueryDto) throws ServiceException {
		try{
			return conLabelDtlMapper.selectItemInfoByLabelNo(cnLabelQueryDto);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public int modifyLabelDtlStatusByLabelNo(ConLabelDtl conLabelDtl) throws ServiceException {
		try{
			return conLabelDtlMapper.modifyLabelDtlStatusByLabelNo(conLabelDtl);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	
	
}