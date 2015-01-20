package com.yougou.logistics.city.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.yougou.logistics.city.common.model.BillConConvertGoods;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtl;
import com.yougou.logistics.city.common.model.BillConConvertGoodsDtlSizeDto;
import com.yougou.logistics.city.common.model.BillConConvertGoodsKey;
import com.yougou.logistics.city.common.model.BillUmCheck;
import com.yougou.logistics.city.common.model.BillUmCheckDtl;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillConConvertGoodsDtlMapper;
import com.yougou.logistics.city.dal.database.BillConConvertGoodsMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillUmCheckMapper;

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
@Service("billConConvertGoodsDtlService")
class BillConConvertGoodsDtlServiceImpl extends BaseCrudServiceImpl implements BillConConvertGoodsDtlService {
	
    @Resource
    private BillConConvertGoodsDtlMapper billConConvertGoodsDtlMapper;
    
    @Resource
    private BillConConvertGoodsMapper billConConvertGoodsMapper;
    
    @Resource
    private BillUmCheckMapper billUmCheckMapper;
    
    @Resource
    private BillUmCheckDtlMapper billUmCheckDtlMapper;
    
    private final static String CONVERTTYPE0 = "0";
    
    private final static String CONVERTTYPE1 = "1";
    
    private final static String CONVERTTYPE2 = "2";
    
    private final static String CONVERTTYPE3 = "3";
    
    private final static String CONVERTTYPE5 = "5";
    
    private final static String STATUS30 = "30";
    private final static String STATUS10 = "10";
    @Override
    public BaseCrudMapper init() {
        return billConConvertGoodsDtlMapper;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void saveConvertGoodsDtl(BillConConvertGoods convertGoods,List<BillUmCheck> insertList,List<BillUmCheck> deleteList) throws ServiceException {
		try{
			BillConConvertGoodsKey key = new BillConConvertGoodsKey();
			key.setLocno(convertGoods.getLocno());
			key.setOwnerNo(convertGoods.getOwnerNo());
			key.setConvertGoodsNo(convertGoods.getConvertGoodsNo());
			BillConConvertGoods billConConvertGoods = (BillConConvertGoods) billConConvertGoodsMapper.selectByPrimaryKey(key);
			if(billConConvertGoods == null || !STATUS10.equals(billConConvertGoods.getStatus())){
				throw new ServiceException("单据"+convertGoods.getConvertGoodsNo()+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
			}
			//添加明细
			if(CommonUtil.hasValue(insertList)){
				BillUmCheck check = insertList.get(0);
				if(StringUtils.isEmpty(check.getLocno())||StringUtils.isEmpty(check.getOwnerNo())||StringUtils.isEmpty(check.getCheckNo())){
					throw new ServiceException(check.getCheckNo()+"参数非法!");
				}
				
				List<BillUmCheck> checkListValidate = billUmCheckMapper.selectCheckValidate(check, insertList);
				if(CommonUtil.hasValue(checkListValidate)){
					throw new ServiceException(checkListValidate.get(0).getCheckNo()+"不是审核状态,不能生成转货单!");
				}
				
				Map<String, Object> checkParams = new HashMap<String, Object>();
				checkParams.put("locno", convertGoods.getLocno());
				checkParams.put("ownerNo", convertGoods.getOwnerNo());
				List<BillUmCheckDtl> checkContentList = billUmCheckDtlMapper.selectCheckQtyJoinContent(checkParams, insertList);
				if(CommonUtil.hasValue(checkContentList)){
					BillUmCheckDtl ucd = checkContentList.get(0);
					StringBuffer sb = new StringBuffer();
					sb.append("商品：" + ucd.getItemNo());
					sb.append(",尺码：" + ucd.getSizeNo());
					sb.append(",退货暂存区库存不足,库存缺量：" + ucd.getCheckQty());
					throw new ServiceException(sb.toString());
				}
				
				String dCellNo = "";
				Map<String, Object> validateParams = new HashMap<String, Object>();
				validateParams.put("locno", convertGoods.getLocno());
				if(CONVERTTYPE0.equals(convertGoods.getConvertType())){
					dCellNo = billConConvertGoodsDtlMapper.selectDCellNo(validateParams);
					if(StringUtils.isEmpty(dCellNo)){
						throw new ServiceException("次品转货获取目的储位为空!");
					}
				}
				
				if(CONVERTTYPE5.equals(convertGoods.getConvertType())){
					validateParams.put("itemType", convertGoods.getdQuality());
					dCellNo = billConConvertGoodsDtlMapper.selectDCellNotoPropetyChange(validateParams);
					if(StringUtils.isEmpty(dCellNo)){
						throw new ServiceException("属性转货获取目的储位为空!");
					}
				}
				
				//验证是否有重复数据
				validateParams.put("ownerNo", convertGoods.getOwnerNo());
				validateParams.put("convertGoodsNo", convertGoods.getConvertGoodsNo());
				List<BillConConvertGoodsDtl> validateList = billConConvertGoodsDtlMapper.selectByParams(null, validateParams);
				if(CommonUtil.hasValue(validateList)){
					for (BillConConvertGoodsDtl v : validateList) {
						for (BillUmCheck c : insertList) {
							if(v.getCheckNo().equals(c.getCheckNo())){
								throw new ServiceException(c.getCheckNo()+"不能重复添加!");
							}
						}
					}
				}
				
				//循环添加明细
				Date date = new Date();
				long rowId = (long)billConConvertGoodsDtlMapper.selectMaxRowId(convertGoods);
				for (BillUmCheck c : insertList) {
					
					//获取来源目的储位
					String sCellNo = "";
					Map<String, Object> sCellParams = new HashMap<String, Object>();
					sCellParams.put("locno", convertGoods.getLocno());
					sCellParams.put("areaUsetype", "1");
					sCellParams.put("areaQuality", c.getQuality());
					sCellParams.put("areaAttribute", "1");
					sCellParams.put("attributeType", "6");
					sCellParams.put("itemType", c.getItemType());
					billConConvertGoodsDtlMapper.selectSCellNo(sCellParams);
					String strOutMsg = (String)sCellParams.get("strOutMsg");
					if (StringUtils.isNotBlank(strOutMsg)) {
						String msg = "";
						String[] msgs = strOutMsg.split("\\|");
						msg = msgs[1];
						throw new ServiceException(msg);
					}
					sCellNo = (String)sCellParams.get("cellNo");
					if(StringUtils.isEmpty(sCellNo)){
						throw new ServiceException("获取来源储位为空!");
					}
					
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("locno", c.getLocno());
					params.put("ownerNo", c.getOwnerNo());
					params.put("checkNo", c.getCheckNo());
					List<BillUmCheckDtl> checkDtlList = billUmCheckDtlMapper.selectCheckDtlByCheckNo(params);
					if(!CommonUtil.hasValue(checkDtlList)){
						throw new ServiceException(c.getCheckNo()+"验收明细数据为空!");
					}
					List<BillConConvertGoodsDtl> goodsList = new ArrayList<BillConConvertGoodsDtl>();
					for (BillUmCheckDtl umCheckDtl : checkDtlList) {
						BillConConvertGoodsDtl goodsDtl = new BillConConvertGoodsDtl();
						goodsDtl.setLocno(convertGoods.getLocno());
						goodsDtl.setOwnerNo(convertGoods.getOwnerNo());
						goodsDtl.setConvertGoodsNo(convertGoods.getConvertGoodsNo());
						goodsDtl.setCheckNo(umCheckDtl.getCheckNo());
						goodsDtl.setRowId(++rowId);
						goodsDtl.setItemNo(umCheckDtl.getItemNo());
						goodsDtl.setSizeNo(umCheckDtl.getSizeNo());
						goodsDtl.setItemQty(umCheckDtl.getItemQty());
						goodsDtl.setRealQty(umCheckDtl.getCheckQty());
						goodsDtl.setBoxNo(umCheckDtl.getBoxNo());
						goodsDtl.setBrandNo(umCheckDtl.getBrandNo());
						goodsDtl.setQuality(umCheckDtl.getQuality());
						goodsDtl.setItemType(umCheckDtl.getItemType());
						goodsDtl.setSourceNo(umCheckDtl.getUntreadNo());
						goodsDtl.setEdittm(date);
						goodsDtl.setsCellNo(sCellNo);
						goodsDtl.setdCellNo(dCellNo);
						goodsDtl.setRemark(c.getRemark());
						goodsList.add(goodsDtl);
					}
					billConConvertGoodsDtlMapper.insertBatchDtl(goodsList);
				}
				
				//更新状态
//				String convertType = STATUS30;
//				if(CONVERTTYPE0.equals(convertGoods.getConvertType())){
//					status = "30";
//				}
//				if(CONVERTTYPE1.equals(convertGoods.getConvertType())||CONVERTTYPE3.equals(convertGoods.getConvertType())){
//					status = "35";
//				}
//				if(CONVERTTYPE2.equals(convertGoods.getConvertType())){
//					status = "40";
//				}
				
//				if(StringUtils.isEmpty(status)){
//					throw new ServiceException(convertGoods.getConvertGoodsNo()+"转货类型为空!");
//				}
				
				//更新验收单主档状态
				Map<String, Object> paramsStatus = new HashMap<String, Object>();
				paramsStatus.put("locno", check.getLocno());
				paramsStatus.put("ownerNo", check.getOwnerNo());
				paramsStatus.put("status", STATUS30);
				paramsStatus.put("convertType", convertGoods.getConvertType());
				billUmCheckMapper.updateCheckStatus4Convert(paramsStatus, insertList);
				
				//更新验收单明细转货数量
				paramsStatus.put("updateType", "0");
				billUmCheckDtlMapper.updateConvertQty4Convert(paramsStatus, insertList);
			}
			
			//删除明细
			if(CommonUtil.hasValue(deleteList)){
				BillUmCheck check = deleteList.get(0);
				for (BillUmCheck c : deleteList) {
					BillConConvertGoodsDtl goodsDtl = new BillConConvertGoodsDtl();
					goodsDtl.setLocno(convertGoods.getLocno());
					goodsDtl.setOwnerNo(convertGoods.getOwnerNo());
					goodsDtl.setConvertGoodsNo(convertGoods.getConvertGoodsNo());
					goodsDtl.setCheckNo(c.getCheckNo());
					billConConvertGoodsDtlMapper.deleteByPrimarayKeyForModel(goodsDtl);
					
					//更新状态
					//String status = "11";
					Map<String, Object> paramsStatus = new HashMap<String, Object>();
					paramsStatus.put("locno", check.getLocno());
					paramsStatus.put("ownerNo", check.getOwnerNo());
					//paramsStatus.put("status", status);
					billUmCheckMapper.updateRollbackCheckStatus4Convert(paramsStatus, deleteList);
					
					//更新验收单明细转货数量为0
					paramsStatus.put("updateType", "1");
					billUmCheckDtlMapper.updateConvertQty4Convert(paramsStatus, deleteList);
				}
			}
			billConConvertGoodsMapper.updateByPrimaryKeySelective(convertGoods);
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheckByPage(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectConvertGoodsDtlGroupByCheckByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public int findConvertGoodsDtlGroupByCheckCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectConvertGoodsDtlGroupByCheckCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public List<BillConConvertGoodsDtl> findConvertGoodsDtlGroupByCheck(Map<String, Object> params)
			throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectConvertGoodsDtlGroupByCheck(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectGroupByCheckSumQty(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectGroupByCheckSumQty(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({ DataAccessRuleEnum.BRAND })
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectSumQty(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillConConvertGoodsDtl> findItemDtlByParams(Map<String, Object> params) throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectItemDtlByParams(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillConConvertGoodsDtl> findCheckContent4Convert(Map<String, Object> params) throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectCheckContent4Convert(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BillConConvertGoodsDtlSizeDto> findDtl4SizeHorizontal(
			Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.selectDtl4SizeHorizontal(params,authorityParams);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer batchUpdateBoxStatus4Container(Map<String, Object> params) throws ServiceException {
		try{
			return billConConvertGoodsDtlMapper.batchUpdateBoxStatus4Container(params);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
}