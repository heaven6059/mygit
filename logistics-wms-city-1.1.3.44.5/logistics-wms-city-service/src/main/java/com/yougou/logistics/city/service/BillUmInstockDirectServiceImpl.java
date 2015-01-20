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
import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillUmInstockDirect;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.mapper.BillUmInstockDirectMapper;
import com.yougou.logistics.city.dal.mapper.ProcCommonMapper;

/*
 * 请写出类的用途 
 * @author luo.hl
 * @date  Mon Nov 18 12:08:45 CST 2013
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
@Service("billUmInstockDirectService")
class BillUmInstockDirectServiceImpl extends BaseCrudServiceImpl implements BillUmInstockDirectService {
	@Resource
	private BillUmInstockDirectMapper billUmInstockDirectMapper;
	
	@Resource
	private ProcCommonMapper procCommonMapper;
	
	private final static String RESULTY = "Y";

	@Override
	public BaseCrudMapper init() {
		return billUmInstockDirectMapper;
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try {
			return billUmInstockDirectMapper.selectSumQty(params, authorityParams);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void akeySendOrder(BillUmInstockDirect instockDirect) throws ServiceException {
		try {
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("locno", instockDirect.getLocno());
			mapParams.put("itemType", instockDirect.getItemType());
			mapParams.put("quality", instockDirect.getQuality());
			mapParams.put("status", "10");
			List<BillUmInstockDirect> listDirects=billUmInstockDirectMapper.selectByParams(null, mapParams);
			if(!CommonUtil.hasValue(listDirects)){
				throw new ServiceException("该品质和属性没有可以发单的商品!");
			}
			String ownerNo = "";
			String strRowIdList = "";
			for (BillUmInstockDirect b : listDirects) {
				strRowIdList += b.getRowId()+",";
				ownerNo = b.getOwnerNo();
			}
			if(StringUtils.isNotBlank(strRowIdList)){
				strRowIdList=strRowIdList.substring(0, strRowIdList.length()-1);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("strLocno", instockDirect.getLocno());
			map.put("strOwnerNo", ownerNo);
			map.put("strSender", instockDirect.getSendUser());
			map.put("strRowIdList", strRowIdList);
			map.put("strWorkerNo", instockDirect.getWorkUser());
			procCommonMapper.sendOrder(map);
			if (!RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
				throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}