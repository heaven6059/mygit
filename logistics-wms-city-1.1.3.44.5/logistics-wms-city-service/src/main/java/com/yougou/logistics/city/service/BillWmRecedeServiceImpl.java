package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillWmOutstockDirect;
import com.yougou.logistics.city.common.model.BillWmRecede;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.mapper.BillWmRecedeMapper;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-11 13:57:00
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
@Service("billWmRecedeService")
class BillWmRecedeServiceImpl extends BaseCrudServiceImpl implements BillWmRecedeService {
	
	private final static String RESULTY = "Y";
	
    @Resource
    private BillWmRecedeMapper billWmRecedeMapper;

    @Override
    public BaseCrudMapper init() {
        return billWmRecedeMapper;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void procBillWmRecedeLocateQuery(List<BillWmOutstockDirect> listWmRecedes) throws ServiceException {
		String msg = "发生异常";
		try {
			if(!CommonUtil.hasValue(listWmRecedes)){
				throw new ServiceException("参数非法!");
			}
			
			for (BillWmOutstockDirect b : listWmRecedes) {
				Map<String, String> map = new HashMap<String, String>();
	   			map.put("v_locno", b.getLocno());
	   			map.put("v_owner_no", b.getOwnerNo());
	   			map.put("v_recede_no", b.getSourceNo());
	   			map.put("v_creator", b.getCreator());
	   			
	   			billWmRecedeMapper.procBillWmRecedeLocateQuery(map);
	   			if (!RESULTY.equals(map.get("stroutmsg"))) {
	   				String stroutmsg = map.get("stroutmsg");
	   				if(StringUtils.isNotBlank(stroutmsg)){
	   					String[] msgs = stroutmsg.split("\\|");
	   					msg = msgs[1];
	   				}
	   				throw new ServiceException(msg);
	   			}
			}
   			
		} catch (Exception e) {
			throw new ServiceException(msg);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBillWmRecedeJoinDirectCount(BillWmRecede billWmRecede, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billWmRecedeMapper.selectBillWmRecedeJoinDirectCount(billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecede> findBillWmRecedeJoinDirectByPage(SimplePage page, String orderByField, String orderBy,
			BillWmRecede billWmRecede, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billWmRecedeMapper.selectBillWmRecedeJoinDirectByPage(page, orderByField, orderBy, billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findBillWmRecedeGroupCount(BillWmRecede billWmRecede, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billWmRecedeMapper.selectBillWmRecedeGroupCount(billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillWmRecede> findBillWmRecedeGroupByPage(SimplePage page, String orderByField, String orderBy,
			BillWmRecede billWmRecede, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billWmRecedeMapper.selectBillWmRecedeGroupByPage(page, orderByField, orderBy, billWmRecede, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int updateRecedeStatus4Outstock(BillWmRecede billWmRecede) throws ServiceException {
		try{
			return billWmRecedeMapper.updateRecedeStatus4Outstock(billWmRecede);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}