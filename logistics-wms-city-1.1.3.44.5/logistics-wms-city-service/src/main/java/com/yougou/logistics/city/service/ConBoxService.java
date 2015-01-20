package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.service.BaseCrudService;
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
public interface ConBoxService extends BaseCrudService {

    public void updateCellNoByBoxNo(Map<String, Object> map);
    
    public List<ConBox> selectBoxNumByPanNo(Map<String, Object> params,List<String> boxList)throws ServiceException;
}