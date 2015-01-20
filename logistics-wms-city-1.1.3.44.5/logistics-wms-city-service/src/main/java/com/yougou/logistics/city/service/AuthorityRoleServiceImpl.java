package com.yougou.logistics.city.service;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AuthorityRole;
import com.yougou.logistics.city.dal.database.AuthorityRoleMapper;
/**
 * 权限业务逻辑处理
 * @author wei.hj
 *
 */

@Service("authorityRoleService")
public class AuthorityRoleServiceImpl extends BaseCrudServiceImpl  implements AuthorityRoleService {
	
	
	@Resource
	private AuthorityRoleMapper  authorityRoleMapper;
	
	@Override
	public BaseCrudMapper init() {
		return authorityRoleMapper;
	}

	@Override
	public int removeRole(Long roleId) {
	   return authorityRoleMapper.removeSelective(roleId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public int addRole(AuthorityRole record) throws ServiceException {
		try {
			return this.authorityRoleMapper.insertSelective(record);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public AuthorityRole queryRoleById(Long roleId) {
		AuthorityRole cur=this.authorityRoleMapper.selectByPrimaryKey(roleId);

		return cur; 
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public int updateRole(AuthorityRole record) {
		return this.authorityRoleMapper.updateSelective(record);
	}
	
	@Override
	public List<AuthorityRole> queryRoleList(AuthorityRole role){
		return this.authorityRoleMapper.queryRoleList(role);
	}
	
	@Override
	public int removeAuthorityRoleMenu(Long roleId){
		return this.authorityRoleMapper.removeAuthorityRoleMenu(roleId);
	}

	@Override
    public int insertAuthorityRoleMenu( Long roleId, Long menuId){
    	return this.authorityRoleMapper.insertAuthorityRoleMenu(roleId, menuId);
    }
	@Override
    public List<AuthorityRole> queryMyRoleByUserId(Long userId){
    	return this.authorityRoleMapper.queryMyRoleByUserId(userId);
    }
	@Override
    public List<AuthorityRole> queryNoMyRoleByUserId(Long userId){
		return this.authorityRoleMapper.queryNoMyRoleByUserId(userId);
    }

	
	

	
	
      
}
