package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.api.dto.AccConInfoDto;
import com.yougou.logistics.city.common.api.dto.AccInventoryConDto;
import com.yougou.logistics.city.common.api.dto.AccTaskDto;
import com.yougou.logistics.city.common.model.AccTask;
import com.yougou.logistics.city.common.vo.AccInventoryConVo;
import com.yougou.logistics.city.service.AccInventoryConService;
import com.yougou.logistics.city.service.AccInventorySkuBookService;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午2:44:58
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Service("accInventoryConSkuServiceApi")
public class AccInventoryConSkuServiceApiImpl implements AccInventoryConSkuServiceApi {
	private static Logger log = org.slf4j.LoggerFactory.getLogger("SKUCONSTOCK"); 
	@Resource
    private AccInventoryConService accInventoryConService;
	@Resource
    private AccInventorySkuBookService accInventorySkuBookService;

	@Override
	public String accontingForCon(AccInventoryConDto accInventoryConDto) throws ServiceException {
		log.info("accontingForCon begin. params:"+accInventoryConDto.toString());
		String ret="succ";
		try {
			AccInventoryConVo tmp = new AccInventoryConVo();
			PropertyUtils.copyProperties(tmp,accInventoryConDto);
			tmp.setOwnerNo(StringUtils.isEmpty(accInventoryConDto.getOwnerNo())? "BL":accInventoryConDto.getOwnerNo());
	    	tmp.setItemType(StringUtils.isEmpty(accInventoryConDto.getOwnerNo())? "0":accInventoryConDto.getItemType());
	    	tmp.setQuality(StringUtils.isEmpty(accInventoryConDto.getQuality())? "0":accInventoryConDto.getQuality());
	    	tmp.setSupplierNo(StringUtils.isEmpty(accInventoryConDto.getSupplierNo())? "N":accInventoryConDto.getSupplierNo());
	    	tmp.setBillNo(StringUtils.isEmpty(accInventoryConDto.getBillNo())? "N":accInventoryConDto.getBillNo());
	    	tmp.setBillType(StringUtils.isEmpty(accInventoryConDto.getBillType())? "N":accInventoryConDto.getBillType());
	    	tmp.setSkuQty(accInventoryConDto.getSkuQty()==null? BigDecimal.valueOf(0):accInventoryConDto.getSkuQty());
	    	tmp.setMoveSkuQty(accInventoryConDto.getMoveSkuQty()==null? BigDecimal.valueOf(0):accInventoryConDto.getMoveSkuQty());
	    	tmp.setChildrenQty(accInventoryConDto.getChildrenQty()==null? BigDecimal.valueOf(0):accInventoryConDto.getChildrenQty());
	    	tmp.setMoveChildrenQty(accInventoryConDto.getMoveChildrenQty()==null? BigDecimal.valueOf(0):accInventoryConDto.getMoveChildrenQty());
			accInventoryConService.accontingForConByTran(tmp);
			
			//装箱、拼箱、拆箱业务更新储位的sku库存
			if(!StringUtils.isEmpty(accInventoryConDto.getDestCellNo())&&!StringUtils.isEmpty(accInventoryConDto.getDestConNo())){
				if(accInventoryConDto.getDirection()==-1){
					Map<String, Object> params=new HashMap<String, Object>();
					params.put("billNo",accInventoryConDto.getBillNo());
					params.put("billType",accInventoryConDto.getBillType());
					params.put("direction",accInventoryConDto.getDirection());
					params.put("cellNo",accInventoryConDto.getCellNo());
					params.put("conNo",accInventoryConDto.getConNo());
					params.put("cellNo_d",accInventoryConDto.getDestCellNo());//目标储位
					params.put("conNo_d",accInventoryConDto.getDestConNo());//目标箱
					params.put("actType","1");//箱操作类型
					accInventorySkuBookService.updateCellNoConNoSkuStock(params);
				}
			}
			else if(accInventoryConDto.getAccConInfoList()!=null&&accInventoryConDto.getAccConInfoList().size()>0){
				//绑板、拼板、解板业务 更新储位的sku库存
				accountForPlateStock(accInventoryConDto);
			}
		} catch (Exception e) {
			ret="failed";
			log.error("accontingForCon error:",e);
			throw new ServiceException(e);
		}
		log.info("accontingForCon end. ret:"+ret);
		return ret; 
	}
	
	/**
	 * 绑板解板业务 更新储位的sku库存
	 * @param accInventoryConDto
	 * @throws ServiceException 
	 */
	private void accountForPlateStock(AccInventoryConDto accInventoryConDto) throws ServiceException{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("billNo", accInventoryConDto.getBillNo());
		params.put("billType", accInventoryConDto.getBillType());
		params.put("actType","2");//板操作类型
		List<AccConInfoDto> list=accInventoryConDto.getAccConInfoList();
		StringBuffer liststr=new StringBuffer();
		for(AccConInfoDto tmpcon:list){
			liststr.append(" conNo:"+tmpcon.getConNo()+" cellNo:"+tmpcon.getCellNo()+"\r\n");
			//绑板1，解板-1(箱号不变，储位变更)
			if(!tmpcon.getCellNo().equals(accInventoryConDto.getCellNo())){
				if(accInventoryConDto.getDirection()==1){
					params.put("direction",accInventoryConDto.getDirection());
					params.put("cellNo",tmpcon.getCellNo());
					params.put("conNo",tmpcon.getConNo());
					params.put("cellNo_d",accInventoryConDto.getCellNo());//目标
					params.put("conNo_d",tmpcon.getConNo());//目标箱
					accInventorySkuBookService.updateCellNoConNoSkuStock(params);
				}
			}
		}
		log.info("getAccConInfoList:"+liststr.toString());
	}
	
	@Override
	public String accountForSkuConByBillTran(AccTaskDto accTaskDto) throws ServiceException {
		log.info("accountForSkuConByBillTran begin. billNo:"+accTaskDto.getBillNo());
		String ret="succ";
		try {
			AccTask tmp = new AccTask();
			PropertyUtils.copyProperties(tmp,accTaskDto);
			accInventorySkuBookService.accountForSkuConByBillTranNoTask(tmp);
		} catch (Exception e) {
			ret="failed";
			log.error("accountForSkuConByBillTran error:",e);
		}
		log.info("accountForSkuConByBillTran end. ret:"+ret);
		return ret; 
	}
	
}
