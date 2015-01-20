package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AccContainerSku;
import com.yougou.logistics.city.common.vo.AccInventorySkuBookVo;
import com.yougou.logistics.city.dal.database.AccContainerSkuMapper;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-08-08 13:49:01
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
@Service("accContainerSkuService")
class AccContainerSkuServiceImpl extends BaseCrudServiceImpl implements AccContainerSkuService {
	private static Logger log = org.slf4j.LoggerFactory.getLogger("SKUCONSTOCK"); 
    @Resource
    private AccContainerSkuMapper accContainerSkuMapper;

    @Override
    public BaseCrudMapper init() {
        return accContainerSkuMapper;
    }

	@Override
	public void saveOrUpdateAccContainerSku(AccInventorySkuBookVo skuBookVo)
			throws ServiceException {
		log.info("saveOrUpdateAccContainerSku begin. conNo:"+skuBookVo.getConNo()+" qty:"+skuBookVo.getQty()+" ioFlag:"+skuBookVo.getIoFlag());
		AccContainerSku temp=new AccContainerSku();
    	temp.setLocno(skuBookVo.getLocno());
    	temp.setConNo(skuBookVo.getConNo());
		temp.setBarcode(skuBookVo.getBarcode());
		temp.setQuality(skuBookVo.getQuality());
		temp.setItemNo(skuBookVo.getItemNo());
		temp.setItemType(skuBookVo.getItemType());
		
		//为当前记录加锁
		accContainerSkuMapper.updateLockAccContainerSku(temp);
		
		AccContainerSku accContainerSku=accContainerSkuMapper.selectByPrimaryKey(temp);
		if(accContainerSku==null){
			temp.setSeqId(skuBookVo.getSeqId()!=null? skuBookVo.getSeqId():BigDecimal.valueOf(0));
			temp.setQty(skuBookVo.getQty());
			temp.setCreatetm(new Date());
			temp.setOwnerNo(StringUtils.isEmpty(skuBookVo.getOwnerNo())? "N":skuBookVo.getOwnerNo());
			temp.setSupplierNo(StringUtils.isEmpty(skuBookVo.getSupplierNo())? "N":skuBookVo.getSupplierNo());
			accContainerSkuMapper.insert(temp);
		}
		else{
			//入库出库计算
			if(skuBookVo.getIoFlag().equals("I")){
				accContainerSku.setQty(accContainerSku.getQty().add(skuBookVo.getQty()));
			}else{
				accContainerSku.setQty(accContainerSku.getQty().subtract(skuBookVo.getQty()));
				
				//不允许负库存出库
				if(accContainerSku.getQty().intValue()<0){
					log.error("["+skuBookVo.getItemNo()+"商品，"+skuBookVo.getBarcode()+"条码不允许负库存出库]");
					return;
				}
			}
			accContainerSku.setUpdatetm(new Date());
			accContainerSkuMapper.updateByPrimaryKeySelective(accContainerSku);
			//删除容器qty为0的记录
			if(accContainerSku.getQty().intValue()==0){
				int rows=accContainerSkuMapper.deleteByPrimarayKeyForModel(accContainerSku);
				log.info("delete con_no:"+accContainerSku.getConNo()+" rows:"+rows);
			}
		}
    	log.info("saveOrUpdateAccContainerSku end.");
	}
	
	public int findPlateCount(Map<String,Object> params)throws ServiceException{
		try {
			return accContainerSkuMapper.selectPlateCount(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<AccContainerSku> findPlateBypage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ServiceException{
		try {
			return accContainerSkuMapper.selectPlateByPage(page,orderByField,orderBy,params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	public List<AccContainerSku>  selectPlateSub(Map<String,Object> params)throws ServiceException{
		try {
			return accContainerSkuMapper.selectPlateSub(params);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}