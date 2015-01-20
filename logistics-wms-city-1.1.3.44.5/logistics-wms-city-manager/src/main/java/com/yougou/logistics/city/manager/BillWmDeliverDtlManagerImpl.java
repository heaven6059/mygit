package com.yougou.logistics.city.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.CnLabelQueryDto;
import com.yougou.logistics.city.common.model.BillWmDeliver;
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtlKey;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConLabel;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.BillWmDeliverDtlService;
import com.yougou.logistics.city.service.BillWmDeliverService;
import com.yougou.logistics.city.service.BillWmRecheckDtlService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ConLabelDtlService;
import com.yougou.logistics.city.service.ConLabelService;
import com.yougou.logistics.city.service.OsLineBufferService;
import com.yougou.logistics.city.service.ProcCommonService;

import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请写出类的用途 
 * @author zuo.sw
 * @date  2013-10-16 10:44:50
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
@Service("billWmDeliverDtlManager")
class BillWmDeliverDtlManagerImpl extends BaseCrudManagerImpl implements BillWmDeliverDtlManager {
	
    @Log
    private Logger log;
	
    @Resource
    private BillWmDeliverDtlService billWmDeliverDtlService;
    
    @Resource
    private ConLabelDtlService  conLabelDtlService;
    
    @Resource
    private ConLabelService conLabelService;
    
    @Resource
    private OsLineBufferService  osLineBufferService;
    
    @Resource
    private ProcCommonService  procCommonService;
    
    @Resource
    private BillWmRecheckDtlService  billWmRecheckDtlService;
    
    @Resource
    private BillWmDeliverService billWmDeliverService;
    
    @Resource
    private ConBoxService conBoxService;
    
    private final static String STATUS8 = "8";
    
    private final static String STATUS2 = "2";

    @Override
    public BaseCrudService init() {
        return billWmDeliverDtlService;
    }

	@Override
	public List<ConLabelDtl> getLabelInfoDtlsList(List<BillWmDeliverDtl> lstBillWmDeliverDtl)
			throws ManagerException {
		List<ConLabelDtl>  lst = new ArrayList<ConLabelDtl>(0);
		try{
			for(BillWmDeliverDtl wmDeliverDtl : lstBillWmDeliverDtl){
				CnLabelQueryDto  dto = new  CnLabelQueryDto();
				dto.setLabelNo(wmDeliverDtl.getLabelNo());
				dto.setRecedeNo(wmDeliverDtl.getRecedeNo());
				dto.setRecheckNo(wmDeliverDtl.getRecheckNo());
				List<ConLabelDtl>  lstDtl = conLabelDtlService.selectItemInfoByLabelNo(dto);
				lst.addAll(lstDtl);
			}
			return lst;
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> boolean addWmDeliverDetail(String locno, String ownerNo,
			String supplierNo, String deliverNo,Map<CommonOperatorEnum, List<ModelType>> params, String LoginName,String userChName)
			throws ManagerException {
		try{
			BillWmDeliver billWmDeliver =new BillWmDeliver();
			billWmDeliver.setOwnerNo(ownerNo);
			billWmDeliver.setLocno(locno);
			billWmDeliver.setDeliverNo(deliverNo);
			billWmDeliver.setEditor(LoginName);
			billWmDeliver.setEditorName(userChName);
			billWmDeliver.setEdittm(new Date());
			billWmDeliverService.modifyById(billWmDeliver);
			
			
			List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);
			//List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);
			List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);
			
			//新增操作
			if(null!=addList && addList.size() > 0 ){
				
//				String cellNo = "";
//				OsLineBuffer  osLineBuffer  = new  OsLineBuffer();
//				osLineBuffer.setLocno(locno);
//				osLineBuffer.setSupplierNo(supplierNo);
//				List<OsLineBuffer>  lstOsLineBuffer = osLineBufferService.selectBufferBySupplierNo(osLineBuffer);
//				if(null!=lstOsLineBuffer && lstOsLineBuffer.size() >0){
//					OsLineBuffer vo = lstOsLineBuffer.get(0);
//					cellNo = vo.getCellNo();
//				}else{//如果获取不到储位，直接返回false;
//					log.error("=======================退厂确认保存明细时获取不到储位！");
//					return  false;
//				}
//				
//				//查询最大的Pid,作为主键
//				BillWmDeliverDtlKey  keyObj = new  BillWmDeliverDtlKey();
//				keyObj.setDeliverNo(deliverNo);
//				keyObj.setLocno(locno);
//				keyObj.setOwnerNo(ownerNo);
//				short pidNum = (short)billWmDeliverDtlService.selectMaxPid(keyObj);
//				
//				Set<String>  labelNoSet = new HashSet<String>(0);
//				
//				for(ModelType modelType : addList){
//					
//					if(modelType instanceof  BillWmDeliverDtl){
//						BillWmDeliverDtl vo = (BillWmDeliverDtl)modelType;
//						vo.setDeliverNo(deliverNo);
//						vo.setLocno(locno);
//						vo.setOwnerNo(ownerNo);
//						vo.setSupplierNo(supplierNo);
//						vo.setPoId(++pidNum);
//						vo.setCellNo(cellNo);
//						vo.setCellId(procCommonService.procGetCellId());
//						
//						BillWmRecheckDtl  billWmRecheckDtl = new  BillWmRecheckDtl();
//						billWmRecheckDtl.setLocno(locno);
//						billWmRecheckDtl.setRecedeNo(vo.getRecedeNo());
//						billWmRecheckDtl.setRecheckNo(vo.getRecheckNo());
//						billWmRecheckDtl.setContainerNo(vo.getContainerNo());
//						billWmRecheckDtl.setItemNo(vo.getItemNo());
//						billWmRecheckDtl.setSizeNo(vo.getSizeNo());
//						
//						Long itemId = billWmRecheckDtlService.getItemIdByRecheckNo(billWmRecheckDtl);
//						vo.setItemId(itemId);
//						
//						
//						//插入预到货通知单明细记录
//						int a = billWmDeliverDtlService.add(vo);
//						if(a < 1){
//							 throw new ManagerException("插入预到退厂配送单明细记录时未更新到记录！");
//						}
//						if(!labelNoSet.contains(vo.getBoxNo())){
//							labelNoSet.add(vo.getBoxNo());
//						}
//					}
//			    }
				
				
				Set<String>  labelNoSet = new HashSet<String>(0);
				for(ModelType modelType : addList){
					//转换成对象
					BillWmDeliverDtl vo = (BillWmDeliverDtl)modelType;
					String labelNo = vo.getBoxNo();
					String recedeNo = vo.getRecedeNo();
					
					BillWmDeliverDtl deliverDtlEntity = new BillWmDeliverDtl();
					deliverDtlEntity.setLocno(locno);
					deliverDtlEntity.setDeliverNo(deliverNo);
					deliverDtlEntity.setOwnerNo(ownerNo);
					deliverDtlEntity.setRecedeNo(recedeNo);
					deliverDtlEntity.setBoxNo(labelNo);
					//检查本装车单信息对否重复
					int isDeliverDtl = billWmDeliverDtlService.selectDeliverDtl(deliverDtlEntity);
					if(isDeliverDtl > 0) {
						throw new ManagerException(labelNo+"箱号重复！");
					} else {
						//查询复核单信息
						Map<String,Object> param = new HashMap<String, Object>();
						param.put("locno", locno);
						param.put("supplierNo", supplierNo);
						param.put("labelNo", labelNo);
						param.put("recedeNo", recedeNo);
						List<BillWmDeliverDtl> add = billWmDeliverDtlService.selectDeliverDtlByLabelNo(param);
						if(add != null && add.size() > 0) {
							//查询最大ID
							BillWmDeliverDtlKey keyObj = new BillWmDeliverDtlKey();
							keyObj.setLocno(locno);
							keyObj.setOwnerNo(ownerNo);
							keyObj.setDeliverNo(deliverNo);
							
							short num = (short)billWmDeliverDtlService.selectMaxPid(keyObj);
							for(int j = 0; j <add.size(); j++) {
								
								BillWmDeliverDtl deliverDtl = add.get(j);
								deliverDtl.setLocno(keyObj.getLocno());
								deliverDtl.setOwnerNo(keyObj.getOwnerNo());
								deliverDtl.setDeliverNo(keyObj.getDeliverNo());
								deliverDtl.setPoId(++num);
								deliverDtl.setSupplierNo(supplierNo);
								deliverDtl.setItemType("0");
								//deliverDtl.setCellId(procCommonService.procGetCellId());
								deliverDtl.setRecedeName(LoginName);
								deliverDtl.setRecedeChName(userChName);
								deliverDtl.setRecedeDate(new Date());
								deliverDtl.setCellNo(StringUtils.isEmpty(deliverDtl.getCellNo())?"N":deliverDtl.getCellNo());
								deliverDtl.setItemId(0L);
								int count = billWmDeliverDtlService.add(deliverDtl);
								if(count<1) {
									throw new ManagerException("新增时未更新记录！");
								}
								
								if(!labelNoSet.contains(vo.getBoxNo())){
									labelNoSet.add(vo.getBoxNo());
								}
							}
						}
					}
				}
				
				
				//新增时，更新标签号的状态
				if(null!=labelNoSet && labelNoSet.size() > 0 ){
					for(String labelNoStr: labelNoSet){
						
						//更改标签状态
						ConLabel  conLabel = new  ConLabel();
						conLabel.setStatus("A1");//18)	标签状态为A0 (待装车)
						conLabel.setLocno(locno);
						conLabel.setScanLabelNo(labelNoStr);
						int a = conLabelService.modifyStatusByLocnoAndLabelNo(conLabel);
						if(a < 1){
							throw new ManagerException("未更新到标签号【"+labelNoStr+"】的状态！");
						}
						
						ConLabelDtl  conLabelDtl = new  ConLabelDtl();
						conLabelDtl.setStatus("A1");//标签状态为6E（外复核完成）
						conLabelDtl.setLocno(locno);
						conLabelDtl.setScanLabelNo(labelNoStr);
						int c = conLabelDtlService.modifyLabelDtlStatusByLabelNo(conLabelDtl);
						if(c < 1){
							throw new ManagerException("未更新到标签明细【"+labelNoStr+"】的状态！");
						}
						
						//更改箱状态
						ConBox entityBox = new ConBox();
						entityBox.setLocno(locno);
						entityBox.setOwnerNo(ownerNo);
						entityBox.setBoxNo(labelNoStr);
						entityBox.setStatus(STATUS8);
						int cBox = conBoxService.modifyById(entityBox);
						if(cBox < 1){
							throw new ManagerException("未更新到箱号【"+labelNoStr+"】的状态！");
						}
						
					}
				}
			}
			
			//删除操作
			if(null!=delList && delList.size() > 0 ){
				Set<String>  labelNoDelSet = new HashSet<String>(0);
				for(ModelType modelType : delList){
					if(modelType instanceof  BillWmDeliverDtl){
						BillWmDeliverDtl vo = (BillWmDeliverDtl)modelType;
						if(!labelNoDelSet.contains(vo.getBoxNo())){
							 labelNoDelSet.add(vo.getBoxNo());
						}
					}
				}
				//删除时，更新标签号的状态
				if(null!=labelNoDelSet && labelNoDelSet.size() > 0 ){
					for(String labelNoStr: labelNoDelSet){
						
						ConLabel  conLabel = new  ConLabel();
						conLabel.setStatus("52");//标签状态为6E（外复核完成）
						conLabel.setLocno(locno);
						conLabel.setScanLabelNo(labelNoStr);
						int a = conLabelService.modifyStatusByLocnoAndLabelNo(conLabel);
						if(a < 1){
							throw new ManagerException("未更新到标签号【"+labelNoStr+"】的状态！");
						}
						
						ConLabelDtl  conLabelDtl = new  ConLabelDtl();
						conLabelDtl.setStatus("52");//标签状态为6E（外复核完成）
						conLabelDtl.setLocno(locno);
						conLabelDtl.setScanLabelNo(labelNoStr);
						int c = conLabelDtlService.modifyLabelDtlStatusByLabelNo(conLabelDtl);
						if(c < 1){
							throw new ManagerException("未更新到标签明细【"+labelNoStr+"】的状态！");
						}
						
						//更改箱状态
						ConBox entityBox = new ConBox();
						entityBox.setLocno(locno);
						entityBox.setOwnerNo(ownerNo);
						entityBox.setBoxNo(labelNoStr);
						entityBox.setStatus(STATUS2);
						int cBox = conBoxService.modifyById(entityBox);
						if(cBox < 1){
							throw new ManagerException("未更新到箱号【"+labelNoStr+"】的状态！");
						}
						
						BillWmDeliverDtl wmDeliverDtl = new BillWmDeliverDtl();
						wmDeliverDtl.setLocno(locno);
						wmDeliverDtl.setOwnerNo(ownerNo);
						wmDeliverDtl.setDeliverNo(deliverNo);
						wmDeliverDtl.setBoxNo(labelNoStr);
						int b = billWmDeliverDtlService.deleteById(wmDeliverDtl);
						if(b < 1){
							throw new ManagerException("删除标签号【"+labelNoStr+"】及其商品编码时未更新到数据！");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new  ManagerException(e.getMessage());
		}
		return true;
	}

	@Override
	public int countWmDeliverDtlByMainId(BillWmDeliverDtlKey vo)
			throws ManagerException {
		try{
			return billWmDeliverDtlService.countWmDeliverDtlByMainId(vo);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillWmDeliverDtl> findWmDeliverDtlByMainIdPage(SimplePage page,
			BillWmDeliverDtlKey vo) throws ManagerException {
		try{
			return billWmDeliverDtlService.findWmDeliverDtlByMainIdPage(page, vo);
		}catch(Exception e){
			throw new ManagerException(e);
		}
	}

	@Override
	public Map<String, Object> validateLabelNo(String noStrs, String deliverNo,
			String locno) throws ManagerException {
		String flag = "success";
		StringBuilder tipStr =  new StringBuilder(0);
		Map<String, Object>  objMap = new HashMap<String,Object>(0);
		try{
			if(StringUtils.isNotBlank(noStrs) && StringUtils.isNotBlank(deliverNo) && StringUtils.isNotBlank(locno)){
				String[] lableNoArr  =  noStrs.split(",");
				//新增时，更新标签号的状态
				if(null!=lableNoArr && lableNoArr.length > 0 ){
					for(String labelNo: lableNoArr){
						int n = billWmDeliverDtlService.selectCountLabelNo(deliverNo,labelNo);
						if(n > 0){
							tipStr.append("标签号:"+labelNo+"已存在；");
						}else{
							List<ConLabel> lstConLabel = conLabelService.getLabelStatus(labelNo,locno);
							if(null != lstConLabel && lstConLabel.size() > 0){
								ConLabel c = lstConLabel.get(0);
								if(!"61".equals(c.getStatus())){
									tipStr.append("标签号:"+labelNo+"已退厂；");
								}
							}else{
								tipStr.append("标签号:"+labelNo+"不存在；");
							}
						}
					}
				}
			}else{
				tipStr.append("未获取到数据！");
			}
		}catch(Exception e){
			throw new ManagerException(e);
		}
		
		if(tipStr.length() >0 ){
			flag = "warn";
		}
		objMap.put("flag", flag);
		objMap.put("returnMsg", tipStr.toString());
		
		return  objMap;
		
	}

	@Override
	public int findBillWmDeliverDtlGroupByCount(Map<String, Object> params,
			AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmDeliverDtlService.findBillWmDeliverDtlGroupByCount(params, authorityParams);
		} catch (Exception e) {
			throw new  ManagerException(e);
		}
	}

	@Override
	public List<BillWmDeliverDtl> findBillWmDeliverDtlGroupByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmDeliverDtlService.findBillWmDeliverDtlGroupByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new  ManagerException(e);
		}
	}

	@Override
	public int findBillWmDeliverDtlCount(Map<String, Object> params, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billWmDeliverDtlService.findBillWmDeliverDtlCount(params, authorityParams);
		} catch (Exception e) {
			throw new  ManagerException(e);
		}
	}

	@Override
	public List<BillWmDeliverDtl> findBillWmDeliverDtlByPage(SimplePage page, String orderByField, String orderBy,
			Map<String, Object> params, AuthorityParams authorityParams) throws ManagerException {
		try{
			return billWmDeliverDtlService.findBillWmDeliverDtlByPage(page, orderByField, orderBy, params, authorityParams);
		} catch (Exception e) {
			throw new  ManagerException(e);
		}
	}

	@Override
	public SumUtilMap<String, Object> selectSumQty(Map<String, Object> map, AuthorityParams authorityParams)
			throws ManagerException {
		try{
			return billWmDeliverDtlService.selectSumQty(map, authorityParams);
		} catch (Exception e) {
			throw new  ManagerException(e);
		}
	}
	
    
}