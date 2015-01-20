package com.yougou.logistics.city.manager;

import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.service.ConContentService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Mon Oct 21 14:46:27 CST 2013
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
@Service("conContentManager")
class ConContentManagerImpl extends BaseCrudManagerImpl implements ConContentManager {
	
    @Resource
    private ConContentService conContentService;

    @Override
    public BaseCrudService init() {
        return conContentService;
    }

	@Override
	public int findCountMx(ConContentDto conContentDto, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.conContentService.findCountMx(conContentDto, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public List<ConContentDto> findConContentByPage(SimplePage page, ConContentDto conContentDto, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.conContentService.findConContentByPage(page, conContentDto, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String,Object> findInstantConContentByPage(ConContentDto cc, AuthorityParams authorityParams, boolean all) throws ManagerException {
		try {
			return this.conContentService.findInstantConContentByPage(cc, authorityParams, all);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int findInstantConContentCount(ConContentDto cc, AuthorityParams authorityParams) throws ManagerException {
		try {
			return this.conContentService.findInstantConContentCount(cc, authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> findDivideLocConContentByPage(ConContentDto ccd,
			AuthorityParams authorityParams, boolean all) throws ManagerException {
		try {
			return this.conContentService.findDivideLocConContentByPage(ccd, authorityParams, all);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
    
}