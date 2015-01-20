package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.DataAccessAuth;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.model.BillConStorelock;
import com.yougou.logistics.city.common.model.BillConStorelockDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillConStoreLockQuery;
import com.yougou.logistics.city.dal.database.BillConStorelockDtlMapper;
import com.yougou.logistics.city.dal.database.BillConStorelockMapper;
import com.yougou.logistics.city.dal.database.ItemMapper;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*
 * 请写出类的用途 
 * @author su.yq
 * @date  Sat Mar 08 11:25:53 CST 2014
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
@Service("billConStorelockDtlService")
class BillConStorelockDtlServiceImpl extends BaseCrudServiceImpl implements BillConStorelockDtlService {
	
    @Resource
    private BillConStorelockDtlMapper billConStorelockDtlMapper;
    
    @Resource
    private BillConStorelockMapper billConStorelockMapper;
    
    @Resource
    private BillAccControlService billAccControlService;
    
    @Resource
    private ItemMapper itemMapper;
    
    private final static String STATUS10 = "10";

    @Override
    public BaseCrudMapper init() {
        return billConStorelockDtlMapper;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void saveStorelockDtl(BillConStoreLockQuery lockQuery) throws ServiceException {
		try {
			List<BillConStorelockDtl> addList = new ArrayList<BillConStorelockDtl>();
			List<BillConStorelockDtl> insertList = lockQuery.getInsertList();
			
            //验证单据状态是否为新状态
			BillConStorelock billConStorelock = new BillConStorelock();
			billConStorelock.setLocno(lockQuery.getLocno());
			billConStorelock.setOwnerNo(lockQuery.getOwnerNo());
			billConStorelock.setStorelockNo(lockQuery.getStorelockNo());
			billConStorelock = billConStorelockMapper.selectByPrimaryKey(billConStorelock);
			if(billConStorelock == null || !"10".equals(billConStorelock.getStatus())){
				throw new ServiceException("当前状态下不可编辑！");
			}
			//验证是否存在重复储位商品
			String expMsg = "";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", lockQuery.getLocno());
			params.put("ownerNo", lockQuery.getOwnerNo());
			params.put("storelockNo", lockQuery.getStorelockNo());
			List<BillConStorelockDtl> listDtls = billConStorelockDtlMapper.selectByParams(null, params);
			for (BillConStorelockDtl insert : insertList) {
				for (BillConStorelockDtl dtls : listDtls) {
					if (insert.getCellNo().equals(dtls.getCellNo())&&insert.getItemNo().equals(dtls.getItemNo()) 
							&& insert.getSizeNo().equals(dtls.getSizeNo())&& insert.getBarcode().equals(dtls.getBarcode())) {
						StringBuffer sb = new StringBuffer();
						sb.append("储位："+insert.getCellNo());
						sb.append("商品："+insert.getItemNo()+",");
						sb.append("尺码："+insert.getSizeNo()+",");
						sb.append("条码："+insert.getBarcode());
						expMsg = sb.toString();
						break;
					}
				}
			}
			
			//重复抛出异常
			if(StringUtils.isNotBlank(expMsg)){
				throw new ServiceException(expMsg+"已存在相同的库存商品!");
			}
			
			//查询最大的ROWID
			BillConStorelockDtl storelockDtl = new BillConStorelockDtl();
			storelockDtl.setLocno(lockQuery.getLocno());
			storelockDtl.setStorelockNo(lockQuery.getStorelockNo());
			int rowId = billConStorelockDtlMapper.selectMaxPid(storelockDtl);
			if(CommonUtil.hasValue(insertList)){
				for (BillConStorelockDtl dtl : insertList) {
					List<BillConStorelockDtl> listCons = billConStorelockDtlMapper.selectConContentByCellNo(dtl);
					if(!CommonUtil.hasValue(listCons)){
						throw new ServiceException("查询库存数据为空!");
					}
					boolean result = false;//是否结束当前循环
					BigDecimal numIn = dtl.getItemQty();//写入数量
					BigDecimal losePackNum = dtl.getItemQty();//剩余需要取出的数量
					for (BillConStorelockDtl bs : listCons) {
						if(losePackNum.compareTo(bs.getGoodContentQty())==1){
							numIn = bs.getGoodContentQty();
							losePackNum = losePackNum.subtract(bs.getGoodContentQty());
						}else{
							numIn = losePackNum;
							result = true;
						}
						
//						dtl.setLocno(lockQuery.getLocno());
//						dtl.setOwnerNo(lockQuery.getOwnerNo());
//						dtl.setStorelockNo(lockQuery.getStorelockNo());
//						dtl.setRowId(Long.valueOf(++rowId));
//						dtl.setCellNo(bs.getCellNo());
//						dtl.setCellId(bs.getCellId());
//						dtl.setItemNo(bs.getItemNo());
//						dtl.setSizeNo(bs.getSizeNo());
//						dtl.setBarcode(bs.getBarcode());
//						dtl.setPackQty(bs.getPackQty()==null?new BigDecimal(1):bs.getPackQty());
//						dtl.setItemQty(numIn);
//						dtl.setRealQty(new BigDecimal(0));
//						dtl.setStatus(STATUS10);
//						dtl.setCreator(lockQuery.getCreator());
//						dtl.setCreatetm(new Date());
						Date date = new Date();
						bs.setLocno(lockQuery.getLocno());
						bs.setOwnerNo(lockQuery.getOwnerNo());
						bs.setStorelockNo(lockQuery.getStorelockNo());
						bs.setRowId(Long.valueOf(++rowId));
						bs.setPackQty(bs.getPackQty()==null?new BigDecimal(1):bs.getPackQty());
						bs.setItemQty(numIn);
						bs.setRealQty(new BigDecimal(0));
						bs.setStatus(STATUS10);
						bs.setCreator(lockQuery.getCreator());
						bs.setCreatorName(lockQuery.getCreatorName());
						bs.setCreatetm(date);
						bs.setEditor(lockQuery.getCreator());
						bs.setEditorName(lockQuery.getEditorName());
						bs.setEdittm(date);
						bs.setBrandNo(bs.getBrandNo());
						addList.add(bs);
						if(result){
							break;
						}
					}
				}
				
				//批量新增
				billConStorelockDtlMapper.saveStorelockDtl(addList);
				
				//修改主单编辑人信息
				billConStorelock.setEdittm(new Date());
				billConStorelock.setEditor(lockQuery.getCreator());
				billConStorelock.setEditorName(lockQuery.getEditorName());
				billConStorelockMapper.updateByPrimaryKeySelective(billConStorelock);
				//循环调用记账外部存储过程
				for (BillConStorelockDtl bsd : addList) {
					//查询供应商
					Item item = itemMapper.selectByCode(bsd.getItemNo(),null);
					BillAccControlDto controlDto = new BillAccControlDto();
					controlDto.setiLocno(bsd.getLocno());
					controlDto.setiOwnerNo(bsd.getOwnerNo());
					controlDto.setiPaperNo(bsd.getStorelockNo());
					controlDto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					controlDto.setiIoFlag("I");
					controlDto.setiCreator(bsd.getCreator());
					controlDto.setiRowId(new BigDecimal(bsd.getRowId()));
					controlDto.setiCellNo(bsd.getCellNo());
					controlDto.setiCellId(new BigDecimal(bsd.getCellId()));
					controlDto.setiItemNo(bsd.getItemNo());
					controlDto.setiSizeNo(bsd.getSizeNo());
					controlDto.setiPackQty(bsd.getPackQty());
					controlDto.setiSupplierNo(item.getSupplierNo());
					controlDto.setiOutstockQty(bsd.getItemQty());
					/**默认值**/
					controlDto.setiItemType("0");
					controlDto.setiQuality("0");
					controlDto.setiQty(new BigDecimal(0));
					controlDto.setiInstockQty(new BigDecimal(0));
					controlDto.setiStatus("0");
					controlDto.setiFlag("0");
					controlDto.setiHmManualFlag("1");
					controlDto.setiTerminalFlag("1");
					billAccControlService.procAccPrepareDataExt(controlDto);
					
					
					//调用外部存储过程
					BillAccControlDto dto = new BillAccControlDto();
					dto.setiPaperNo(bsd.getStorelockNo());
					dto.setiLocType("2");
					dto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
					dto.setiIoFlag("I");
					dto.setiPrepareDataExt(new BigDecimal(bsd.getRowId()));
					dto.setiIsWeb(new BigDecimal(1));
					billAccControlService.procAccApply(dto);
				}
			}
					
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
    
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public void delStorelockDtl(BillConStoreLockQuery lockQuery) throws ServiceException {
		try{
			//验证单据状态是否为新状态
			BillConStorelock billConStorelock = new BillConStorelock();
			billConStorelock.setLocno(lockQuery.getLocno());
			billConStorelock.setOwnerNo(lockQuery.getOwnerNo());
			billConStorelock.setStorelockNo(lockQuery.getStorelockNo());
			billConStorelock = billConStorelockMapper.selectByPrimaryKey(billConStorelock);
			if(billConStorelock == null || !"10".equals(billConStorelock.getStatus())){
				throw new ServiceException("当前状态下不可编辑！");
			}
			
			List<BillConStorelockDtl> delList = lockQuery.getInsertList();
			if(CommonUtil.hasValue(delList)){
				for (BillConStorelockDtl dtl : delList) {
					//循环调用记账外部存储过程
					List<BillConStorelockDtl> listDtls = billConStorelockDtlMapper.selectStorelockDtlByCellNo(dtl);
					if(CommonUtil.hasValue(listDtls)){
						for (BillConStorelockDtl bsd : listDtls) {
							Item item = itemMapper.selectByCode(bsd.getItemNo(),null);//查询供应商
							BillAccControlDto controlDto = new BillAccControlDto();
							controlDto.setiLocno(bsd.getLocno());
							controlDto.setiOwnerNo(bsd.getOwnerNo());
							controlDto.setiPaperNo(bsd.getStorelockNo());
							controlDto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
							controlDto.setiIoFlag("O");
							controlDto.setiCreator(bsd.getCreator());
							controlDto.setiRowId(new BigDecimal(bsd.getRowId()));
							controlDto.setiCellNo(bsd.getCellNo());
							controlDto.setiCellId(new BigDecimal(bsd.getCellId()));
							controlDto.setiItemNo(bsd.getItemNo());
							controlDto.setiSizeNo(bsd.getSizeNo());
							controlDto.setiPackQty(bsd.getPackQty());
							controlDto.setiSupplierNo(item.getSupplierNo());
							controlDto.setiOutstockQty(new BigDecimal(0).subtract(bsd.getItemQty()));
							
							/**默认值**/
							controlDto.setiItemType("0");
							controlDto.setiQuality("0");
							controlDto.setiQty(new BigDecimal(0));
							controlDto.setiInstockQty(new BigDecimal(0));
							controlDto.setiStatus("0");
							controlDto.setiFlag("0");
							controlDto.setiHmManualFlag("1");
							controlDto.setiTerminalFlag("1");
							billAccControlService.procAccPrepareDataExt(controlDto);
							
							//调用外部存储过程
							BillAccControlDto dto = new BillAccControlDto();
							dto.setiPaperNo(bsd.getStorelockNo());
							dto.setiLocType("2");
							dto.setiPaperType(CNumPre.CON_STORELOCK_PRE);
							dto.setiIoFlag("O");
							dto.setiPrepareDataExt(new BigDecimal(bsd.getRowId()));
							dto.setiIsWeb(new BigDecimal(1));
							billAccControlService.procAccApply(dto);
						}
						
					}
				}
				//删除数据
				int deletecount  = billConStorelockDtlMapper.deleteModelByCellNo(delList);
				System.out.println(deletecount);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findConContentGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectConContentGroupByCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillConStorelockDtl> findConContentGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectConContentGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public int findStorelockGroupByCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectStorelockGroupByCount(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillConStorelockDtl> findStorelockGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectStorelockGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public List<BillConStorelockDtl> find4WmPlan(Map<String,Object> params) throws ServiceException {
		try{
			return billConStorelockDtlMapper.select4WmPlan(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillConStorelockDtl> findConContentGroup(Map<String, Object> params,
			AuthorityParams authorityParams) throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectConContentGroup(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	
	
	@Override
	@DataAccessAuth({DataAccessRuleEnum.BRAND})
	public List<BillConStorelockDtl> findStorelockGroup(Map<String, Object> params, AuthorityParams authorityParams)
			throws ServiceException {
		try{
			return billConStorelockDtlMapper.selectStorelockGroup(params, authorityParams);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
    
}