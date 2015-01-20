package com.yougou.logistics.city.dal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.city.common.model.ConBox;

/**
 * 请写出类的用途
 * 
 * @author zuo.sw
 * @date 2013-09-25 21:07:33
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
public interface ConBoxMapper extends BaseCrudMapper {
    public void updateCellNoByBoxNo(@Param("params") Map<String, Object> map);
    
    public void deleteBox4Recheck(@Param("params") Map<String, Object> params) throws DaoException;
    
    public void updateBoxSumQty4Recheck(@Param("params") Map<String, Object> params)throws DaoException;

    public List<ConBox> selectBoxNumByPanNo(@Param("params")Map<String, Object> params,@Param("panList")List<String> panList)throws DaoException;
    
    public int findByPanNo(@Param("params") Map<String, Object> params)throws DaoException;
}