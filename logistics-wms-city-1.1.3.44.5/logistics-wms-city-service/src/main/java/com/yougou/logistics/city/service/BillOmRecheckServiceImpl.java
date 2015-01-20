package com.yougou.logistics.city.service;

import java.math.BigDecimal;
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
import com.yougou.logistics.city.common.dto.BillOmOutstockDtlDto;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmOutstockDtl;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.BillOmRecheckDtl;
import com.yougou.logistics.city.common.model.BillOmRecheckKey;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.dal.database.BillOmDivideDtlMapper;
import com.yougou.logistics.city.dal.database.BillOmDivideMapper;
import com.yougou.logistics.city.dal.mapper.BillOmExpDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmExpMapper;
import com.yougou.logistics.city.dal.mapper.BillOmOutstockDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmRecheckDtlMapper;
import com.yougou.logistics.city.dal.mapper.BillOmRecheckMapper;
import com.yougou.logistics.city.dal.mapper.ConBoxDtlMapper;
import com.yougou.logistics.city.dal.mapper.ConBoxMapper;
import com.yougou.logistics.city.dal.mapper.ConLabelDtlMapper;
import com.yougou.logistics.city.dal.mapper.ConLabelMapper;
import com.yougou.logistics.city.dal.mapper.ProcCommonMapper;

/**
 * 
 * 分货复核单service实现
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:21:38
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmRecheckService")
class BillOmRecheckServiceImpl extends BaseCrudServiceImpl implements BillOmRecheckService {
    @Resource
    private BillOmRecheckMapper billOmRecheckMapper;
    @Resource
    private BillOmRecheckDtlMapper billOmRecheckDtlMapper;
    @Resource
    private ConLabelMapper conLabelMapper;
    @Resource
    private ConLabelDtlMapper conLabelDtlMapper;
    @Resource
    private BillOmDivideMapper billOmDivideMapper;
    @Resource
    private BillOmDivideDtlMapper billOmDivideDtlMapper;
    @Resource
    private BillOmExpMapper billOmExpMapper;
    @Resource
    private BillOmExpDtlMapper billOmExpDtlMapper;
    @Resource
    private ProcCommonMapper procCommonMapper;
    @Resource 
    private ConBoxMapper conBoxMapper;
    @Resource
    private ConBoxDtlMapper conBoxDtlMapper;
    @Resource
    private BillStatusLogService billStatusLogService;
    @Resource
    private BillOmOutstockDtlMapper billOmOutstockDtlMapper;
    @Resource
    private BillAccControlService billAccControlService;

    private final static String RESULTY = "Y|";
    
    private final static String STATUS10 = "10";
    
    private final static String STATUS14 = "14";
    
    private final static String STATUS40 = "40";
    
    private final static String STATUS45 = "45";
    
    private final static String SOURCETYPE0 = "0";
    
    @Override
    public BaseCrudMapper init() {
        return billOmRecheckMapper;
    }
    
    
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findRecheckBoxItemCount(Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDivideMapper.selectRecheckBoxItemCount(params, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmDivideDtl> findRecheckBoxItemByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDivideMapper.selectRecheckBoxItemByPage(page, orderByField, orderBy, params, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmDivideMapper.selectSumQty(map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void deleteBillOmRecheck(List<BillOmRecheck> listOmRechecks) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(listOmRechecks)){
				throw new ServiceException("参数不合法!");
			}
			for (BillOmRecheck b : listOmRechecks) {
				BillOmRecheck  obj = billOmRecheckMapper.selectByPrimaryKey(b);
				if(null==obj || !STATUS10.equals(obj.getStatus())){
					throw new ServiceException("单号"+b.getRecheckNo()+"不是新建状态，不可以删除！");
				}
				Map<String, String> map = new HashMap<String, String>();
	   			map.put("I_locno", b.getLocno());
	   			map.put("I_recheckNo", b.getRecheckNo());
	   			
	   			billOmRecheckMapper.procDeleteBillOmRecheck(map);
	   			if (!RESULTY.equals(map.get("strOutMsg"))) {
	   				String message = "";
	   				String msg = map.get("strOutMsg");
	   				if(StringUtils.isNotBlank(msg)){
	   					String[] msgs = msg.split("\\|");
	   					message = msgs[1];
	   				}
	   				throw new ServiceException(b.getRecheckNo()+message);
	   			}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void packageBoxRf(BillOmRecheck billOmRecheck) throws ServiceException {
		try{
			//查询客户流道号
			BillOmDivideDtl entityDtl = new BillOmDivideDtl();
			entityDtl.setLocno(billOmRecheck.getLocno());
			entityDtl.setDivideNo(billOmRecheck.getDivideNo());
			entityDtl.setStoreNo(billOmRecheck.getStoreNo());
			BillOmDivideDtl divideDtl = billOmDivideDtlMapper.selectDivideDtlSerialNo(entityDtl);
			if(divideDtl == null){
				throw new ServiceException("查询客户流道号为空");
			}
			
			//复核装箱
			Map<String, String> mapRecheck = new HashMap<String, String>();
			mapRecheck.put("I_locno", billOmRecheck.getLocno());
			mapRecheck.put("I_divideNo", billOmRecheck.getDivideNo());
			mapRecheck.put("I_serialNo", divideDtl.getSerialNo());
			mapRecheck.put("I_recheckNo", billOmRecheck.getRecheckNo());
			mapRecheck.put("creator", billOmRecheck.getCreator());
			billOmRecheckMapper.procRecheckComplete(mapRecheck);
			String strOutMsg = mapRecheck.get("strOutMsg");
			if (!"Y|".equals(strOutMsg)) {
				String[] msgs = strOutMsg.split("\\|");
				throw new ServiceException(msgs[1]);
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void packageBox(List<BillOmDivideDtl> dtlLst,BillOmRecheck billOmRecheck, String boxNo)
			throws ServiceException {
		
		try{
			Date now = new Date();
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("locno", billOmRecheck.getLocno());
			mapParams.put("recheckNo", billOmRecheck.getRecheckNo());
			mapParams.put("containerNo", "N");
			List<BillOmRecheckDtl> listCheckDtls = billOmRecheckDtlMapper.selectByParams(null, mapParams);
			if(CommonUtil.hasValue(listCheckDtls)){
				throw new ServiceException("RF正在进行封箱操作,不能封箱!");
			}
			
			//查询最大的ID累加
			BillOmRecheckDtl billOmRecheckDtl = new BillOmRecheckDtl();
			billOmRecheckDtl.setLocno(billOmRecheck.getLocno());
			billOmRecheckDtl.setRecheckNo(billOmRecheck.getRecheckNo());
			int rowId = billOmRecheckDtlMapper.selectMaxPid(billOmRecheckDtl);
			
			//箱号添加复核明细
			for(BillOmDivideDtl d : dtlLst){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("locno", billOmRecheck.getLocno());
				params.put("divideNo", billOmRecheck.getDivideNo());
				params.put("storeNo", billOmRecheck.getStoreNo());
				params.put("itemNo", d.getItemNo());
				params.put("sizeNo", d.getSizeNo());
				List<BillOmDivideDtl> listDtls = billOmDivideDtlMapper.selectBillOmDivideDtlIsComplete(params);
				
				boolean result = false;//是否结束当前循环
				BigDecimal packNumIn = d.getPackageNum();//装箱数量
				BigDecimal losePackNum = d.getPackageNum();//剩余装箱的数量
				if(CommonUtil.hasValue(listDtls)){
					for (BillOmDivideDtl odd : listDtls) {
						if(losePackNum.compareTo(odd.getItemQty().subtract(odd.getRealQty()))==1){
							packNumIn = odd.getItemQty().subtract(odd.getRealQty());
							losePackNum = losePackNum.subtract(odd.getItemQty().subtract(odd.getRealQty()));
						}else{
							packNumIn = losePackNum;
							result = true;
						}
						
						++rowId;
						BillOmRecheckDtl dtl = new BillOmRecheckDtl();
						dtl.setRowId(Long.valueOf(rowId));
						dtl.setLocno(billOmRecheck.getLocno());
						dtl.setRecheckNo(billOmRecheck.getRecheckNo());
						dtl.setContainerNo("N");
						dtl.setItemNo(odd.getItemNo());
						dtl.setItemQty(packNumIn);
						dtl.setAssignName(odd.getAssignName());
						dtl.setExpNo(odd.getExpNo());
						dtl.setExpType(odd.getExpType());
						dtl.setOwnerNo(odd.getOwnerNo());
						dtl.setPackQty(odd.getPackQty());
						dtl.setRealQty(packNumIn);
						dtl.setSizeNo(odd.getSizeNo());
						dtl.setStatus("10");
						dtl.setExpDate(now);
						dtl.setRecheckName(billOmRecheck.getEditor());
						dtl.setRechecknamech(billOmRecheck.getEditorname());
						dtl.setRecheckDate(now);
						dtl.setBoxNo(odd.getBoxNo());
						dtl.setBrandNo(odd.getBrandNo());
						int count = billOmRecheckDtlMapper.insertSelective(dtl);
						if(count < 1){
							throw new ServiceException("回写分货明细信息失败");
						}
						
						//更新回写分货明细表的已分货数量
						BillOmDivideDtl divideDtl = billOmDivideDtlMapper.selectByPrimaryKey(odd);
						if(divideDtl!=null){
							BigDecimal b1 = divideDtl.getRealQty()==null?new BigDecimal(0):divideDtl.getRealQty();
					        BigDecimal b2 = packNumIn;
					        int b3 = b1.add(b2).intValue();
					        int b4 = divideDtl.getItemQty().intValue();
							if(b3 >= b4){
								//不改差异调整的分货明细状态
								if(!STATUS14.equals(divideDtl.getStatus())){
									divideDtl.setStatus("13");
								}
							}
							divideDtl.setRealQty(b1.add(b2));
							int u = billOmDivideDtlMapper.updateByPrimaryKeySelective(divideDtl);
							if(u < 1){
								throw new ServiceException("更新分货明细信息失败");
							}
						}else{
							throw new ServiceException("没有查找到装箱的分货单");
						}
						
						if(result){
							break;
						}
						
					}
				}
			}
			
			//查询客户流道号
			BillOmDivideDtl entityDtl = new BillOmDivideDtl();
			entityDtl.setLocno(billOmRecheck.getLocno());
			entityDtl.setDivideNo(billOmRecheck.getDivideNo());
			entityDtl.setStoreNo(billOmRecheck.getStoreNo());
			BillOmDivideDtl divideDtl = billOmDivideDtlMapper.selectDivideDtlSerialNo(entityDtl);
			if(divideDtl == null){
				throw new ServiceException("查询客户流道号为空");
			}
			
			//复核装箱
			Map<String, String> mapRecheck = new HashMap<String, String>();
			mapRecheck.put("I_locno", billOmRecheck.getLocno());
			mapRecheck.put("I_divideNo", billOmRecheck.getDivideNo());
			mapRecheck.put("I_serialNo", divideDtl.getSerialNo());
			mapRecheck.put("I_recheckNo", billOmRecheck.getRecheckNo());
			mapRecheck.put("creator", billOmRecheck.getCreator());
			billOmRecheckMapper.procRecheckComplete(mapRecheck);
			String strOutMsg = mapRecheck.get("strOutMsg");
			if (!"Y|".equals(strOutMsg)) {
				String[] msgs = strOutMsg.split("\\|");
				throw new ServiceException(msgs[1]);
			}
			
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void check(String ids, SystemUser user, String checkUser) throws ServiceException {
		
		try {
			if(StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(checkUser)){
				Date now = new Date();
				BillOmRecheckDtl dtl = new BillOmRecheckDtl();
				dtl.setRecheckDate(now);
				dtl.setRecheckName(user.getLoginName());
				dtl.setRechecknamech(user.getUsername());
				String[] idArr = ids.split(",");
				for(String id : idArr){
					String[] tmp = id.split("-");
					if(tmp.length==2){
						
						BillOmRecheck recheck = new BillOmRecheck();
						recheck.setRecheckNo(tmp[0]);
						recheck.setLocno(tmp[1]);
						recheck = billOmRecheckMapper.selectByPrimaryKey(recheck);
						
						//查询客户流道号
						BillOmDivideDtl entityDtl = new BillOmDivideDtl();
						entityDtl.setLocno(recheck.getLocno());
						entityDtl.setDivideNo(recheck.getDivideNo());
						entityDtl.setStoreNo(recheck.getStoreNo());
						BillOmDivideDtl divideDtl = billOmDivideDtlMapper.selectDivideDtlSerialNo(entityDtl);
						if(divideDtl == null){
							throw new ServiceException("查询客户流道号为空");
						}
						
						//调用存储过程..添加验收表、库存表
						Map<String, String> mapRecheck = new HashMap<String, String>();
						mapRecheck.put("I_locno", recheck.getLocno());
						mapRecheck.put("I_divideNo", recheck.getDivideNo());
						mapRecheck.put("I_serialNo", divideDtl.getSerialNo());
						mapRecheck.put("I_recheckNo", recheck.getRecheckNo());
						mapRecheck.put("I_creator", user.getLoginName());
						billOmRecheckMapper.procOmRecheckAudit(mapRecheck);
						String strOutMsg = mapRecheck.get("O_msg");
						if (!"Y|".equals(strOutMsg)) {
							String[] msgs = strOutMsg.split("\\|");
							throw new ServiceException(msgs[1]);
						}
						//pc审核时将明细的实际复核人、实际复核人名称、实际时间更新为当前操作人、当前时间
						dtl.setLocno(tmp[1]);
						dtl.setRecheckNo(tmp[0]);
						billOmRecheckDtlMapper.updateByPrimaryKeySelective(dtl);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

//	@Override
//	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
//	public void check(BillOmRecheck billOmRecheck) throws ServiceException {
//		
//		try {
//			BillOmRecheck recheck = new BillOmRecheck();
//			recheck.setRecheckNo(billOmRecheck.getRecheckNo());
//			recheck.setLocno(billOmRecheck.getLocno());
//			recheck = billOmRecheckMapper.selectByPrimaryKey(recheck);
//			
//			//查询客户流道号
//			BillOmDivideDtl entityDtl = new BillOmDivideDtl();
//			entityDtl.setLocno(recheck.getLocno());
//			entityDtl.setDivideNo(recheck.getDivideNo());
//			entityDtl.setStoreNo(recheck.getStoreNo());
//			BillOmDivideDtl divideDtl = billOmDivideDtlMapper.selectDivideDtlSerialNo(entityDtl);
//			if(divideDtl == null){
//				throw new ServiceException("查询客户流道号为空");
//			}
//			
//			//调用存储过程..添加验收表、库存表
//			Map<String, String> mapRecheck = new HashMap<String, String>();
//			mapRecheck.put("I_locno", recheck.getLocno());
//			mapRecheck.put("I_divideNo", recheck.getDivideNo());
//			mapRecheck.put("I_serialNo", divideDtl.getSerialNo());
//			mapRecheck.put("I_recheckNo", recheck.getRecheckNo());
//			mapRecheck.put("I_creator", billOmRecheck.getCreator());
//			billOmRecheckMapper.procOmRecheckAudit(mapRecheck);
//			String strOutMsg = mapRecheck.get("O_msg");
//			if (!"Y|".equals(strOutMsg)) {
//				String[] msgs = strOutMsg.split("\\|");
//				throw new ServiceException(msgs[1]);
//			}
//		} catch (Exception e) {
//			throw new ServiceException(e.getMessage());
//		}
//	}
	
	
	public Map<String, String> getLabelInfo(String locno,String strpapertype,String strContainerMaterial){
		
		try{
			
			Map<String, String> mapResult = new HashMap<String, String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("strlocno", locno);
			map.put("strpapertype", strpapertype);
			map.put("strUserId", "admin");
			map.put("strGetType", "D");
			map.put("nGetNum", "1");
			map.put("strUseType", "1");
			map.put("strContainerMaterial", strContainerMaterial);
			procCommonMapper.procGetContainerNoBase(map);
			
			String msg = "";
			String labelNo = "";
			String containerNo = "";
			String flag = "";
			String strOutMsg = map.get("strOutMsg");
			if ("Y|".equals(strOutMsg)) {
				flag = strOutMsg;
				labelNo = map.get("strOutLabelNo");
				containerNo = map.get("strOutContainerNo");
			} else {
				if(StringUtils.isNotBlank(strOutMsg)){
					String[] msgs = strOutMsg.split("\\|");
					flag = msgs[0];;
					msg = msgs[1];
				}
			}
			
			mapResult.put("flag", flag);
			mapResult.put("msg", msg);
			mapResult.put("labelNo", labelNo);
			mapResult.put("containerNo", containerNo);
			mapResult.put("strOutMsg", strOutMsg);
			return mapResult;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void packageBoxOutstockRf(BillOmRecheck billOmRecheck) throws ServiceException {
		try{
			//4.封箱
			String msg = "";
			BillOmRecheckKey rKey = new BillOmRecheckKey();
			rKey.setLocno(billOmRecheck.getLocno());
			rKey.setRecheckNo(billOmRecheck.getRecheckNo());
			BillOmRecheck recheck = (BillOmRecheck)billOmRecheckMapper.selectByPrimaryKey(rKey);
			if(recheck == null){
				throw new ServiceException("查询复核单失败");
			}
			
			Map<String, String> map = new HashMap<String, String>();
			//根据来源类型调用封箱存储过程
			if(!SOURCETYPE0.equals(recheck.getSourceType())){
				map.put("I_locno", recheck.getLocno());
	   			map.put("I_recheckNo", recheck.getRecheckNo());
	   			map.put("I_boxNo", "N");
	   			map.put("I_creator", billOmRecheck.getCreator());
	   			billOmRecheckDtlMapper.procDirectrecheckSealBox(map);
			}else{
				map.put("I_locno", recheck.getLocno());
	   			map.put("I_locateNo", recheck.getDivideNo());
	   			map.put("I_serialNo", recheck.getStoreNo());
	   			map.put("I_recheckNo", recheck.getRecheckNo());
	   			map.put("I_boxNo", "N");
	   			map.put("I_creator", billOmRecheck.getCreator());
	   			billOmRecheckDtlMapper.procOmInsertLabel(map);
			}
   			if (!RESULTY.equals(map.get("O_msg"))) {
   				String stroutmsg = map.get("O_msg");
   				if(StringUtils.isNotBlank(stroutmsg)){
   					String[] msgs = stroutmsg.split("\\|");
   					msg = msgs[1];
   				}
   				throw new ServiceException(msg);
   			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void packageBoxOutstock(List<BillOmOutstockDtlDto> dtlLst, BillOmRecheck billOmRecheck, String boxNo)
			throws ServiceException {
		try{
			Date now = new Date();
			String editor = billOmRecheck.getEditor();
			String editorname = billOmRecheck.getEditorname();
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("locno", billOmRecheck.getLocno());
			mapParams.put("recheckNo", billOmRecheck.getRecheckNo());
			mapParams.put("containerNo", "N");
			List<BillOmRecheckDtl> listCheckDtls = billOmRecheckDtlMapper.selectByParams(null, mapParams);
			if(CommonUtil.hasValue(listCheckDtls)){
				throw new ServiceException("RF正在进行封箱操作,不能封箱!");
			}
			
			String serialNo = "";//流到号
			String storeNo = "";//客户编码
			
			BillOmRecheckDtl bDtl = new BillOmRecheckDtl();
			bDtl.setLocno(billOmRecheck.getLocno());
			bDtl.setRecheckNo(billOmRecheck.getRecheckNo());
			int rowId = billOmRecheckDtlMapper.selectMaxPid(bDtl);
			for(BillOmOutstockDtlDto d : dtlLst){
								
				//查询拣货单明细outstockDtl
				BillOmOutstockDtl entityOutstockDtl = new BillOmOutstockDtl();
				entityOutstockDtl.setLocno(billOmRecheck.getLocno());
				entityOutstockDtl.setLocateNo(billOmRecheck.getDivideNo());
				entityOutstockDtl.setItemNo(d.getItemNo());
				entityOutstockDtl.setSizeNo(d.getSizeNo());
				entityOutstockDtl.setSerialNo(d.getSerialNo());
				List<BillOmOutstockDtl> listOutstockDtl = billOmOutstockDtlMapper.selectOutstockDtlByLocateNo(entityOutstockDtl);
				
				if(!CommonUtil.hasValue(listOutstockDtl)){
					throw new ServiceException("查找拣货单明细失败");
				}
				
				BillOmOutstockDtl outstockDtl = listOutstockDtl.get(0);
				serialNo = outstockDtl.getSerialNo();
				storeNo = outstockDtl.getStoreNo();
				
				//复核单明细存在相同商品尺码累加数量
				BillOmRecheckDtl rd = new BillOmRecheckDtl();
				rd.setLocno(billOmRecheck.getLocno());
				rd.setOwnerNo(d.getOwnerNo());
				rd.setRecheckNo(billOmRecheck.getRecheckNo());
				rd.setBoxNo(boxNo);
				rd.setItemNo(d.getItemNo());
				rd.setSizeNo(d.getSizeNo());
				BillOmRecheckDtl rdEntity = billOmRecheckDtlMapper.selectRecheckDtlByItem(rd);
				if(rdEntity != null){
					rdEntity.setItemQty(d.getItemQty().add(d.getPackageNum()));
					rdEntity.setRealQty(d.getRealQty().add(d.getPackageNum()));
					int rdC = billOmOutstockDtlMapper.updateByPrimaryKeySelective(rdEntity);
					if(rdC < 1){
						throw new ServiceException("更新复核单明细数量失败");
					}
				}else{
					//1.新增复核明细表
					++rowId;
					BillOmRecheckDtl dtl = new BillOmRecheckDtl();
					dtl.setRowId(Long.valueOf(rowId));
					dtl.setLocno(billOmRecheck.getLocno());
					dtl.setRecheckNo(billOmRecheck.getRecheckNo());
					dtl.setContainerNo("N");
					dtl.setItemNo(d.getItemNo());
					dtl.setItemQty(d.getPackageNum());
					dtl.setAssignName(billOmRecheck.getCreator());
					dtl.setExpNo(outstockDtl.getExpNo());
					dtl.setExpType(outstockDtl.getExpType());
					dtl.setOwnerNo(outstockDtl.getOwnerNo());
					dtl.setPackQty(d.getPackQty()!=null?d.getPackQty():new BigDecimal(1));
					dtl.setRealQty(d.getPackageNum());
					dtl.setSizeNo(d.getSizeNo());
					dtl.setStatus("10");
					dtl.setExpDate(outstockDtl.getExpDate()!=null?outstockDtl.getExpDate():new Date());
					dtl.setRecheckName(billOmRecheck.getCreator());
					dtl.setRecheckDate(new Date());
					dtl.setBoxNo(d.getScanLabelNo());
					dtl.setBrandNo(outstockDtl.getBrandNo());
					dtl.setEdittm(now);
					dtl.setEditor(editor);
					dtl.setEditorname(editorname);
					int count = billOmRecheckDtlMapper.insertSelective(dtl);
					if(count < 1){
						throw new ServiceException("新增复核明细失败");
					}
				}
				
				
				//2.更新拣货明细数量
//				BillOmOutstockDtlKey outstockDtlKey = new BillOmOutstockDtlKey();
//				outstockDtlKey.setLocno(billOmRecheck.getLocno());
//				outstockDtlKey.setOwnerNo(d.getOwnerNo());
//				outstockDtlKey.setDivideId(d.getDivideId());
//				outstockDtlKey.setOutstockNo(d.getOutstockNo());
//				BillOmOutstockDtl outstockDtl=(BillOmOutstockDtl)billOmOutstockDtlMapper.selectByPrimaryKey(outstockDtlKey);
				
				int surplusNum = d.getPackageNum().intValue();
				BigDecimal recheckQtyIn = new BigDecimal(0);//实际写入的复核数量
				for (BillOmOutstockDtl bo : listOutstockDtl) {
					if(bo.getRealQty().intValue() > bo.getRecheckQty().intValue()){
						BigDecimal recheckQty = bo.getRecheckQty()==null?new BigDecimal(0):bo.getRecheckQty();
						if((recheckQty.intValue()+surplusNum) > bo.getRealQty().intValue()){
							surplusNum = (surplusNum + recheckQty.intValue()) - bo.getRealQty().intValue();
							recheckQtyIn = new BigDecimal(bo.getRealQty().intValue()-recheckQty.intValue());
						}else{
							recheckQtyIn = new BigDecimal(surplusNum);
							surplusNum = 0;
						}
						BillOmOutstockDtl uOutstockDtl = new BillOmOutstockDtl();
						uOutstockDtl.setLocno(bo.getLocno());
						uOutstockDtl.setOwnerNo(outstockDtl.getOwnerNo());
						uOutstockDtl.setDivideId(bo.getDivideId());
						uOutstockDtl.setOutstockNo(bo.getOutstockNo());
						uOutstockDtl.setRecheckQty(recheckQty.add(recheckQtyIn));
						int oCount = billOmOutstockDtlMapper.updateByPrimaryKeySelective(uOutstockDtl);
						if(oCount < 1){
							throw new ServiceException("更新拣货单明细复核数量失败");
						}
						if(surplusNum == 0){
							break;
						}
					}
				}
				
				//3.查询分货单号
				BillOmDivideDtl divideDtl = new BillOmDivideDtl();
				divideDtl.setLocno(billOmRecheck.getLocno());
				divideDtl.setSourceNo(billOmRecheck.getRecheckNo());
				List<BillOmDivideDtl> listDivideDtls=billOmDivideDtlMapper.selectBillOmDivideDtlBySourceNo(divideDtl);
				if(CommonUtil.hasValue(listDivideDtls)){
					BillOmDivideDtl entity = listDivideDtls.get(0);
					BigDecimal b1 = entity.getRealQty().add(d.getPackageNum());
					BigDecimal b2 = entity.getItemQty();
					String status = b1.intValue() >= b2.intValue()?"13":entity.getStatus();
					
					BillOmDivideDtl dtl2 = new BillOmDivideDtl();
					dtl2.setLocno(entity.getLocno());
					dtl2.setOwnerNo(entity.getOwnerNo());
					dtl2.setDivideNo(entity.getDivideNo());
					dtl2.setDivideId(Long.valueOf(d.getDivideId()));
					dtl2.setRealQty(b1);
					if("13".equals(status)){
						dtl2.setStatus(status);
					}
					billOmDivideDtlMapper.updateByPrimaryKeySelective(dtl2);
				}
			}
			
			//4.封箱
			String msg = "";
			BillOmRecheckKey rKey = new BillOmRecheckKey();
			rKey.setLocno(billOmRecheck.getLocno());
			rKey.setRecheckNo(billOmRecheck.getRecheckNo());
			BillOmRecheck recheck = (BillOmRecheck)billOmRecheckMapper.selectByPrimaryKey(rKey);
			if(recheck == null){
				throw new ServiceException("查询复核单失败");
			}
			
			Map<String, String> map = new HashMap<String, String>();
   			map.put("I_locno", recheck.getLocno());
   			map.put("I_locateNo", recheck.getDivideNo());
   			map.put("I_serialNo", storeNo);
   			map.put("I_recheckNo", billOmRecheck.getRecheckNo());
   			map.put("I_boxNo", "N");
   			map.put("I_creator", billOmRecheck.getCreator());
   			
   			billOmRecheckDtlMapper.procOmInsertLabel(map);
   			if (!RESULTY.equals(map.get("O_msg"))) {
   				String stroutmsg = map.get("O_msg");
   				if(StringUtils.isNotBlank(stroutmsg)){
   					String[] msgs = stroutmsg.split("\\|");
   					msg = msgs[1];
   				}
   				throw new ServiceException(msg);
   			}
			
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void checkOutStock(String ids, SystemUser user, String checkUser) throws ServiceException {
		try {
			if(StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(checkUser)){
				Date now = new Date();
				String[] idArr = ids.split(",");
				BillOmRecheckDtl dtl = new BillOmRecheckDtl();
				dtl.setEdittm(now);
				dtl.setEditor(user.getLoginName());
				dtl.setEditorname(user.getUsername());
				for(String id : idArr){
					String[] tmp = id.split("-");
					if(tmp.length==2){
						
						String msg = "发生异常";
						Map<String, String> map = new HashMap<String, String>();
			   			map.put("I_locno", tmp[1]);
			   			map.put("I_recheckNo", tmp[0]);
			   			map.put("I_creator", checkUser);
			   			
			   			billOmRecheckMapper.procOmOutStockRecheckAudit(map);
			   			if (!RESULTY.equals(map.get("O_msg"))) {
			   				String stroutmsg = map.get("O_msg");
			   				if(StringUtils.isNotBlank(stroutmsg)){
			   					String[] msgs = stroutmsg.split("\\|");
			   					msg = msgs[1];
			   				}
			   				throw new ServiceException(tmp[0]+msg);
			   			}
			   			dtl.setLocno(tmp[1]);
			   			dtl.setRecheckNo(tmp[0]);
			   			billOmRecheckDtlMapper.updateByPrimaryKeySelective(dtl);
					}
				}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void deleteBillOmOutStockRecheck(List<BillOmRecheck> listOmRechecks) throws ServiceException {
		try{
			if(!CommonUtil.hasValue(listOmRechecks)){
				throw new ServiceException("参数不合法!");
			}
			for (BillOmRecheck b : listOmRechecks) {
				Map<String, String> map = new HashMap<String, String>();
	   			map.put("I_locno", b.getLocno());
	   			map.put("I_recheckNo", b.getRecheckNo());
	   			BillOmRecheck temp=billOmRecheckMapper.selectByPrimaryKey(b);
	   			if(null!=temp){
					if (!temp.getStatus().equals("10")) {
						throw new ServiceException("已操作的复核单不能删除!");
					}
	   			}else{
	   				throw new ServiceException("已操作的复核单不能删除!");
	   			}
	   			billOmRecheckMapper.procOmOutStockRecheckDel(map);
	   			if (!RESULTY.equals(map.get("O_msg"))) {
	   				String message = "";
	   				String msg = map.get("O_msg");
	   				if(StringUtils.isNotBlank(msg)){
	   					String[] msgs = msg.split("\\|");
	   					message = msgs[1];
	   				}
	   				throw new ServiceException(b.getRecheckNo()+message);
	   			}
			}
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectRecheckSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmRecheckMapper.selectRecheckSumQty(map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectOutstockRecheckSumQty(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckMapper.selectOutstockRecheckSumQty(map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findOutstockRecheckCount(Map<String, Object> map, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billOmRecheckMapper.selectOutstockRecheckCount(map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillOmRecheck> findOutstockRecheckByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckMapper.selectOutstockRecheckByPage(page, orderByField, orderBy, map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public List<BillOmRecheck> selectRecheckByStoreAndCheckNo(Map<String, Object> map, List<Store> storeList)
			throws ServiceException {
		try{
			return billOmRecheckMapper.selectRecheckByStoreAndCheckNo(map, storeList);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public int findCount4Source(Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckMapper.selectCount4Source(map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public List<BillOmRecheck> findByPage4Source(SimplePage page,
			String orderByField, String orderBy, Map<String, Object> map,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billOmRecheckMapper.selectByPage4Source(page, orderByField, orderBy, map, authorityParams);
		}catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}