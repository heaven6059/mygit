package com.yougou.logistics.city.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.CmDefware;
import com.yougou.logistics.city.dal.mapper.CmDefareaMapper;

/**
 * 
 * 库区service实现
 * 
 * @author qin.dy
 * @date 2013-9-26 上午10:10:09
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("cmDefareaService")
class CmDefareaServiceImpl extends BaseCrudServiceImpl implements CmDefareaService {
	@Resource
	private CmDefareaMapper cmDefareaMapper;

	@Override
	public BaseCrudMapper init() {
		return cmDefareaMapper;
	}

	@Override
	public List<?> findByWareAndArea(Map<String, Object> map) throws ServiceException {
		try {
			return cmDefareaMapper.selectByWareAndArea(map);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<CmDefarea> findCmDefareaIsHaveByWareNo(Map<String, Object> params, List<CmDefware> listCmDefwares)
			throws ServiceException {
		try {
			return cmDefareaMapper.selectCmDefareaIsHaveByWareNo(params, listCmDefwares);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<CmDefarea> findByStoreroom(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return cmDefareaMapper.findByStoreroom(params);
	}
	
}