package com.yougou.logistics.city.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillStoreRule;
import com.yougou.logistics.city.service.BillStoreRuleService;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("billStoreRuleManager")
class BillStoreRuleManagerImpl extends BaseCrudManagerImpl implements BillStoreRuleManager {
    @Resource
    private BillStoreRuleService billStoreRuleService;

    @Override
    public BaseCrudService init() {
        return billStoreRuleService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public int deleteStoreRuleBatch(String locno, String keyStr, String tempStr) throws ManagerException {
		int count = 0;
		if (StringUtils.isNotBlank(keyStr)) {
			String[] keyStrs = keyStr.split(",");
			String[] tempStrs = tempStr.split(",");
			BillStoreRule rule = null;
			if(keyStrs.length == tempStrs.length) {
				for(int i = 0; i < keyStrs.length; i++) {
					String ruleNo = keyStrs[i];
					String tempNo = tempStrs[i];
					try {
						rule = new BillStoreRule();
						rule.setLocno(locno);
						rule.setRuleNo(ruleNo);
						rule.setTempNo(tempNo);
						count += billStoreRuleService.deleteById(rule);
					} catch (Exception e) {
						throw new ManagerException(e);
					}
				}
			} else {
				throw new ManagerException("删除失败");
			}
		}
		return count;
	}
    
}