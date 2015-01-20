package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillOmExpDtlDTO;
import com.yougou.logistics.city.common.model.BillOmExp;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.model.BillOmExpDtlKey;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.DateUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmExpDtlForPage;
import com.yougou.logistics.city.service.BillOmExpDtlService;
import com.yougou.logistics.city.service.BillOmExpService;

/**
 * 出库订单明细
 * 
 * @author zuo.sw
 * @date 2013-09-29 16:50:42
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
@Service("billOmExpDtlManager")
class BillOmExpDtlManagerImpl extends BaseCrudManagerImpl implements
	BillOmExpDtlManager {
    @Resource
    private BillOmExpDtlService billOmExpDtlService;
    
    @Resource
    private BillOmExpService billOmExpService;

    private final static String STATUS10 = "10";
    
    @Override
    public BaseCrudService init() {
	return billOmExpDtlService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public <ModelType> boolean addBillOmExpDtl(String locno, String ownerNo,
	    String expNo, String expDate,
	    Map<CommonOperatorEnum, List<ModelType>> params, String loginName)
	    throws ManagerException {
	try {
		
		Map<String, Object> paramsCehck = new HashMap<String, Object>();
		paramsCehck.put("locno", locno);
		paramsCehck.put("ownerNo", ownerNo);
		paramsCehck.put("expNo", expNo);
		List<BillOmExp> checkList=billOmExpService.findByBiz(null, paramsCehck);
		if(!CommonUtil.hasValue(checkList)||!(STATUS10).equals(checkList.get(0).getStatus())){
			throw new ManagerException("单据"+expNo+"已删除或状态已改变");
		}
//		BillOmExp exp = checkList.get(0);
//		if(!STATUS10.equals(exp.getStatus())){
//			throw new ManagerException("只能更新建单状态的发货通知单！");
//		}
		
	    List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);
	    List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);
	    List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);
	    // 新增操作
	    for (ModelType modelType : addList) {
		if (modelType instanceof BillOmExpDtl) {
		    // 转换成对象
		    BillOmExpDtl vo = (BillOmExpDtl) modelType;
		    vo.setExpNo(expNo);
		    vo.setLocno(locno);
		    vo.setOwnerNo(ownerNo);
		    vo.setExpDate(DateUtil.getdate(expDate));
		    vo.setPackQty(new BigDecimal(1));
		    int a = billOmExpDtlService.add(vo);
		    if (a < 1) {
			throw new ManagerException("插入出库订单明细记录时未更新到记录！");
		    }
		}
	    }

	    // 删除操作
	    for (ModelType modelType : delList) {
		if (modelType instanceof BillOmExpDtl) {
		    BillOmExpDtl vo = (BillOmExpDtl) modelType;

		    BillOmExpDtlKey billOmExpDtlKey = new BillOmExpDtlKey();
		    billOmExpDtlKey.setExpNo(expNo);
		    billOmExpDtlKey.setLocno(locno);
		    billOmExpDtlKey.setOwnerNo(ownerNo);
		    billOmExpDtlKey.setItemNo(vo.getItemNo());
		    billOmExpDtlKey.setSizeNo(vo.getSizeNo());
		    billOmExpDtlKey.setPackQty(vo.getPackQty());

		    int b = billOmExpDtlService
			    .deleteByPrimaryKey(billOmExpDtlKey);
		    if (b < 1) {
			throw new ManagerException("删除出库订单明细信息时未更新到记录！");
		    }
		}

	    }

	    // 修改操作
	    for (ModelType modelType : uptList) {
		if (modelType instanceof BillOmExpDtl) {
		    BillOmExpDtl vo = (BillOmExpDtl) modelType;
		    vo.setExpNo(expNo);
		    vo.setLocno(locno);
		    vo.setOwnerNo(ownerNo);
		    int b = billOmExpDtlService.modifyById(vo);
		    if (b < 1) {
			throw new ManagerException("修改出库订单明细信息时未更新到记录！");
		    }
		}

	    }

	} catch (Exception e) {
	    throw new ManagerException(e.getMessage());
	}
	return true;
    }

    @Override
    public Map<String, Object> selectItemNoByDetailPageCount(int pageNo,
	    int pageSize, String orderByField, String orderBy,
	    Map<String, Object> params) throws ManagerException {
	Map<String, Object> obj = new HashMap<String, Object>(0);
	try {
	    int total = billOmExpDtlService.selectItemNoByDetailCount(params);
	    SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
	    BillOmExpDtlForPage dtlPage = new BillOmExpDtlForPage();
	    dtlPage.setExpNo(String.valueOf(params.get("expNo")));
	    dtlPage.setLocno(String.valueOf(params.get("locno")));
	    dtlPage.setOwnerNo(String.valueOf(params.get("ownerNo")));
	    dtlPage.setStartRowNum(page.getStartRowNum());
	    dtlPage.setEndRowNum(page.getEndRowNum());
	    List<BillOmExpDtl> list = billOmExpDtlService
		    .selectItemNoByDetailPage(dtlPage);
	    obj.put("total", total);
	    obj.put("rows", list);
	    return obj;
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public int selectCountMx(BillOmExpDtlDTO dto,AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billOmExpDtlService.selectCountMx(dto, authorityParams);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOBExpNo(BillOmExpDtlDTO dto,AuthorityParams authorityParams)
	    throws ManagerException {
	try {
	    return billOmExpDtlService.queryBillOmExpDtlDTOBExpNo(dto, authorityParams);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmExpDtlDTO> queryBillOmExpDtlDTOGroupBy(SimplePage page,
	    BillOmExpDtlDTO dto,AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billOmExpDtlService.queryBillOmExpDtlDTOGroupBy(page, dto, authorityParams);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public int findBillOmExpDtlDispatchCount(
	    BillOmExpDispatchDtlDTO billOmExpDtl,
	    AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billOmExpDtlService.findBillOmExpDtlDispatchCount(
		    billOmExpDtl, authorityParams);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmExpDispatchDtlDTO> findBillOmExpDtlDispatchByPage(
	    SimplePage page, String orderByField, String orderBy,
	    BillOmExpDispatchDtlDTO billOmExpDtl,
	    AuthorityParams authorityParams) throws ManagerException {
	try {
	    return billOmExpDtlService.findBillOmExpDtlDispatchByPage(page,
		    orderByField, orderBy, billOmExpDtl, authorityParams);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

    @Override
    public List<BillOmExpDtl> selectStore(BillOmExpDtl billOmExpDtl)
	    throws ManagerException {
	try {
	    return billOmExpDtlService.selectStore(billOmExpDtl);
	} catch (Exception e) {
	    throw new ManagerException(e);
	}
    }

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
		    return billOmExpDtlService.selectSumQty(map,authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectDispatchSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
		    return billOmExpDtlService.selectDispatchSumQty(map,authorityParams);
		} catch (Exception e) {
		    throw new ManagerException(e);
		}
	}
	
}