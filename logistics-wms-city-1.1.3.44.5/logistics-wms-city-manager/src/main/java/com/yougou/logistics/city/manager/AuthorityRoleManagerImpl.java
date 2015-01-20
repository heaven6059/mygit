package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.service.AuthorityRoleService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Nov 04 12:15:10 CST 2013
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
@Service("authorityRoleManager")
class AuthorityRoleManagerImpl extends BaseCrudManagerImpl implements AuthorityRoleManager {
    @Resource
    private AuthorityRoleService authorityRoleService;

    @Override
    public BaseCrudService init() {
        return authorityRoleService;
    }
}