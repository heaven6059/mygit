package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.dal.database.BillConConvertGoodsDtlMapper;
import com.yougou.logistics.city.dal.database.BillConConvertGoodsMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckMapper;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Tue Jul 15 14:35:55 CST 2014
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
@Service("billConConvertGoodsService")
class BillConConvertGoodsServiceImpl extends BaseCrudServiceImpl implements BillConConvertGoodsService {
	@Resource
	private BillConConvertGoodsMapper billConConvertGoodsMapper;
	@Resource
	private BillConConvertGoodsDtlMapper billConConvertGoodsDtlMapper;
	@Resource
	private BillUmCheckMapper billUmCheckMapper;
	@Resource
	private BillUmCheckDtlMapper billUmCheckDtlMapper;
	
	private final static String STATUS11 = "11";

	@Override
	public BaseCrudMapper init() {
		return billConConvertGoodsMapper;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void deleteConvertGoods(List<BillConConvertGoods> goodsList) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(goodsList)){
				throw new ServiceException("参数非法");
			}
			for (BillConConvertGoods g : goodsList) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", g.getLocno());
				params.put("ownerNo", g.getOwnerNo());
				params.put("convertGoodsNo", g.getConvertGoodsNo());
				List<BillConConvertGoodsDtl> dtlList = billConConvertGoodsDtlMapper.selectConvertGoodsDtlGroupByCheck(params);
				if(CommonUtil.hasValue(dtlList)){
					List<BillUmCheck> checkList = new ArrayList<BillUmCheck>();
					for (BillConConvertGoodsDtl goodsDtl : dtlList) {
						BillUmCheck check = new BillUmCheck();
						check.setLocno(goodsDtl.getLocno());
						check.setOwnerNo(goodsDtl.getOwnerNo());
						check.setCheckNo(goodsDtl.getCheckNo());
						checkList.add(check);
					}
					//params.put("status", STATUS11);
					billUmCheckMapper.updateRollbackCheckStatus4Convert(params, checkList);
					//删除明细
					BillConConvertGoodsDtl goodsDtl = new BillConConvertGoodsDtl();
					goodsDtl.setLocno(g.getLocno());
					goodsDtl.setOwnerNo(g.getOwnerNo());
					goodsDtl.setConvertGoodsNo(g.getConvertGoodsNo());
					billConConvertGoodsDtlMapper.deleteByPrimarayKeyForModel(goodsDtl);
					
					//回写明细数量
					Map<String, Object> paramsCheck = new HashMap<String, Object>();
					paramsCheck.put("locno", g.getLocno());
					paramsCheck.put("ownerNo", g.getOwnerNo());
					paramsCheck.put("updateType", "1");
					billUmCheckDtlMapper.updateConvertQty4Convert(paramsCheck, checkList);
				}
				//删除主档
				g.setUpdStatus("10");
				int count = billConConvertGoodsMapper.deleteByPrimarayKeyForModel(g);
				if(count <= 0){
					throw new ServiceException("单据"+g.getConvertGoodsNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
}