package com.yougou.logistics.city.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityDtlTableBean;
import com.yougou.logistics.city.dal.mapper.BillAuthorityMapper;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-4-11 下午3:19:21
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("billAuthorityService")
public class BillAuthorityServiceImpl extends BaseCrudServiceImpl implements BillAuthorityService {
	 @Resource
	 private BillAuthorityMapper billAuthorityMapper;
	@Override
	public BaseCrudMapper init() {
		return null;
	}
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public boolean isHasFullBillAuthority(String tableAndColumn,String billNo, AuthorityParams authorityParams)
			throws ServiceException {
		String[] temp=tableAndColumn.split("\\|");
		if(temp.length!=2){
			throw new ServiceException("tableAndColumn参数有误。");
		}
		String tableName=temp[0];
		String billNoClumn=temp[1];
		
		AuthorityDtlTableBean bean=new AuthorityDtlTableBean();
		bean.setTableName(tableName);
		bean.setBillNoClumn(billNoClumn);
		bean.setBillNo(billNo);
		
		int count=0;
		//当系统已开启权限校验时，需判断单据和用户的权限
		if(authorityParams!=null && authorityParams.isVerification()){
			count=billAuthorityMapper.selectHasFullBillAuthority(bean, authorityParams);
		}
		if(count==0){
			return true;
		}else{
			return false;
		}
		
	}
}
