package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AccContainerSku;
import com.yougou.logistics.city.common.model.AccInventoryCon;
import com.yougou.logistics.city.common.model.AccInventoryConBook;
import com.yougou.logistics.city.common.vo.AccCheckDtlVo;
import com.yougou.logistics.city.common.vo.AccInventoryConVo;
import com.yougou.logistics.city.common.vo.AccInventorySkuBookVo;
import com.yougou.logistics.city.dal.database.AccCheckDtlMapper;
import com.yougou.logistics.city.dal.database.AccInventoryConBookMapper;
import com.yougou.logistics.city.dal.database.AccInventoryConMapper;

/**
 * 请写出类的用途 
 * @author wugy
 * @date  2014-07-10 17:27:03
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
@Service("accInventoryConService")
class AccInventoryConServiceImpl extends BaseCrudServiceImpl implements AccInventoryConService {
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger("SKUCONSTOCK"); 
	@Resource
    private AccInventoryConMapper accInventoryConMapper;
    @Resource
    private AccCheckDtlMapper accCheckDtlMapper;
    @Resource
    private AccContainerSkuService accContainerSkuService;
    @Override
    public BaseCrudMapper init() {
        return accInventoryConMapper;
    }
    
    @Resource
    private AccInventoryConBookMapper accInventoryConBookMapper;
    
    /**
     * 记录容器库存
     * @param accInventoryConVo
     */
    public synchronized void accontingForCon(AccInventoryConVo aconVo) throws ServiceException{
    	log.info("accontingForCon() begin cellNo:"+aconVo.getCellNo()+" ConNo:"+aconVo.getConNo()+" MoveSkuQty:"+aconVo.getMoveSkuQty()+" MoveChildrenQty:"+aconVo.getMoveChildrenQty()+" Direction:"+aconVo.getDirection());
    	long begin=System.currentTimeMillis();
		List<AccInventoryConBook> accInventoryConBookList = new ArrayList<AccInventoryConBook>();
		AccInventoryCon contemp = new AccInventoryCon();
		contemp.setLocno(aconVo.getLocno());
		contemp.setConNo(aconVo.getConNo());
		contemp.setCellNo(aconVo.getCellNo());
		
		//为当前容器加锁
		accInventoryConMapper.updatelockAccinverntoryCon(contemp);
		
		//查询同一储位的同一容器是否存在
		AccInventoryCon concur = accInventoryConMapper.selectAccInvertyroConExist(contemp);
		if(concur!=null){
			//记账方向(1：增加库存，-1：减少库存)
			if(aconVo.getDirection()==1){
				concur.setSkuQty(concur.getSkuQty().add(aconVo.getMoveSkuQty()));
				concur.setChildrenQty(concur.getChildrenQty().add(aconVo.getMoveChildrenQty()));
				concur.setEditor(aconVo.getCreator());
				
			}else{
				concur.setSkuQty(concur.getSkuQty().subtract(aconVo.getMoveSkuQty()));
				concur.setChildrenQty(concur.getChildrenQty().subtract(aconVo.getMoveChildrenQty()));
				concur.setEditor(aconVo.getCreator());
				if(concur.getSkuQty().intValue()<0){
					log.error("conNo:"+concur.getConNo()+" cellNo:"+concur.getCellNo()+"不允许负库存出库]");
					return;
				}
			}
			accInventoryConMapper.updateAccountConByPrimaryKey(concur);
			if(concur.getSkuQty().intValue()<=0&&concur.getChildrenQty().intValue()<=0){
				//如果skuQty及childrenQty数量为0则删掉该容器总账
				int rows=accInventoryConMapper.deleteAccinverntoryCon(concur);
				log.info("deleteAccinverntoryCon con_no:"+concur.getConNo()+" rows:"+rows);
				// 父级容器是否为空，如果拼箱和拆箱在板上面操作需要处理
				if(null!=aconVo.getUserType()&&aconVo.getUserType().equals("C")){
					handleParentCon(aconVo);
				}
			}
			
			//通过vo传已加减过的值
			aconVo.setSkuQty(concur.getSkuQty());
			aconVo.setChildrenQty(concur.getChildrenQty());
		}else{
			AccInventoryCon accInventoryCon = new AccInventoryCon();
			accInventoryCon.setCellNo(aconVo.getCellNo());
			accInventoryCon.setConNo(aconVo.getConNo());
			accInventoryCon.setLocno(aconVo.getLocno());
			accInventoryCon.setOwnerNo(aconVo.getOwnerNo());
			accInventoryCon.setItemType(aconVo.getItemType());
			accInventoryCon.setQuality(aconVo.getQuality());
			accInventoryCon.setSupplierNo(aconVo.getSupplierNo());
			accInventoryCon.setCreator(aconVo.getCreator());
			accInventoryCon.setCreatetm(new Date());
			
			if(aconVo.getDirection()==1){
				//只有入库I操作才会产生新容器
				accInventoryCon.setSkuQty(aconVo.getMoveSkuQty());
				accInventoryCon.setChildrenQty(aconVo.getMoveChildrenQty());
				accInventoryConMapper.insertAccInverntory(accInventoryCon);
			}
			else{
				//集货Ioflag=O操作时设置其结余为0
				accInventoryCon.setSkuQty(BigDecimal.valueOf(0));
				accInventoryCon.setChildrenQty(BigDecimal.valueOf(0));
			}
			
			//通过vo传值
			aconVo.setSkuQty(accInventoryCon.getSkuQty());
			aconVo.setChildrenQty(accInventoryCon.getChildrenQty());
			
		}
		appendAccInventoryConbook(accInventoryConBookList,aconVo);//拼装记录容器库存的流水

		accInventoryConBookMapper.insertAccInverntoryBookBat(accInventoryConBookList);
		log.info("  accontingForCon() end. ConNo:"+aconVo.getConNo()+" usedtime:"+(System.currentTimeMillis()-begin)+"ms");
    }
    
    /**
     * 拼装容器三级账
     * @param accInventoryConBookList
     * @param accInventoryConVo
     * @return
     */
    private List<AccInventoryConBook> appendAccInventoryConbook(List<AccInventoryConBook> accInventoryConBookList
    		,AccInventoryConVo accInventoryConVo){
    	AccInventoryConBook accInventoryConBook = new AccInventoryConBook();
    	accInventoryConBook.setConNo(accInventoryConVo.getConNo());
    	accInventoryConBook.setLocno(accInventoryConVo.getLocno());
    	accInventoryConBook.setCellNo(accInventoryConVo.getCellNo());
    	accInventoryConBook.setOwnerNo(accInventoryConVo.getOwnerNo());
    	accInventoryConBook.setItemType(accInventoryConVo.getItemType());
    	accInventoryConBook.setQuality(accInventoryConVo.getQuality());
    	accInventoryConBook.setSupplierNo(accInventoryConVo.getSupplierNo());
    	accInventoryConBook.setDirection((long)accInventoryConVo.getDirection());
    	accInventoryConBook.setMoveChildrenQty(accInventoryConVo.getMoveChildrenQty());
    	accInventoryConBook.setMoveSkuQty(accInventoryConVo.getMoveSkuQty());
    	accInventoryConBook.setBalanceSkuQty(accInventoryConVo.getSkuQty());
    	accInventoryConBook.setBalanceChildrenQty(accInventoryConVo.getChildrenQty());
    	accInventoryConBook.setBillNo(accInventoryConVo.getBillNo());
    	accInventoryConBook.setBillType(accInventoryConVo.getBillType());
    	accInventoryConBook.setIoFlag(accInventoryConVo.getDirection()==1?"I":"O");
    	accInventoryConBook.setCreator(accInventoryConVo.getCreator());
    	accInventoryConBook.setCreatedt(new Date());
    	accInventoryConBook.setCreatetm(new Date());
    	accInventoryConBook.setSeqId(BigDecimal.valueOf(0));
    	accInventoryConBook.setTerminalFlag(StringUtils.isEmpty(accInventoryConVo.getTerminalFlag())? "1":accInventoryConVo.getTerminalFlag());
    	accInventoryConBook.setStatusTrans("13");
    	accInventoryConBookList.add(accInventoryConBook);
    	return accInventoryConBookList;
    }
    

	@Override
    @Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void accontingForConByTran(AccInventoryConVo accInventoryConVo) throws ServiceException{
		if(accInventoryConVo.getMoveSkuQty().intValue()==0&&accInventoryConVo.getMoveChildrenQty().intValue()==0){
			log.error("accontingForConByTran()数量都0的未能记账。 ConNo:"+accInventoryConVo.getConNo()+" MoveSkuQty:"+accInventoryConVo.getMoveSkuQty()+" MoveChildrenQty:"+accInventoryConVo.getMoveChildrenQty());
		}
		else{
			//装箱操作
			if(accInventoryConVo.getMoveSkuQty().intValue()>0){
				accountForAccContainerSkuByBill(accInventoryConVo);
				if(accInventoryConVo.getSupplierNo().equals("N")){//查询供应商
					Map<String,Object> selectBoxMap=new HashMap<String,Object>();
					selectBoxMap.put("conNo", accInventoryConVo.getConNo());
					selectBoxMap.put("locno", accInventoryConVo.getLocno());
					List<AccContainerSku>  boxList=accContainerSkuService.findByBiz(null, selectBoxMap);
					if(null!=boxList && boxList.size()>0){
						accInventoryConVo.setSupplierNo(boxList.get(0).getSupplierNo());
					}
				}
			}
			accontingForCon(accInventoryConVo);
		}
	}

    /**
     * 按单据更新	容器商品明细实时账
     * @param accConVo
     * @throws ServiceException
     */
	public void accountForAccContainerSkuByBill(AccInventoryConVo accConVo) throws ServiceException {
		String ioFlag=accConVo.getDirection()==1? "I":"O";
		log.info("accountForAccContainerSkuByBill begin... billNo:"+ accConVo.getBillNo()+" billType:"+accConVo.getBillType()+" ioFlag："+ioFlag);
		try {
			HashMap<String, Object> params=new HashMap<String, Object>();
			params.put("billNo", accConVo.getBillNo());
			params.put("billType", accConVo.getBillType());
			params.put("ioFlag", ioFlag);
			params.put("locType", '2');
			
			List<AccCheckDtlVo> list=accCheckDtlMapper.selectAccCheckDtlByParams(params);
			AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
			for(AccCheckDtlVo  tmp:list){
				if(tmp.getConNo().equals(accConVo.getConNo())){
					skuBookVo=new AccInventorySkuBookVo();
					skuBookVo.setLocno(tmp.getLocno());
					skuBookVo.setOwnerNo(tmp.getOwnerNo());
					skuBookVo.setItemNo(tmp.getItemNo());
					skuBookVo.setBarcode(tmp.getBarcode());
					skuBookVo.setQuality(tmp.getQuality());
					skuBookVo.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
					skuBookVo.setItemType(tmp.getItemType());
					skuBookVo.setDirection((long)accConVo.getDirection());//记账方向(1：增加库存，-1：减少库存)
					skuBookVo.setBillNo(tmp.getBillNo());
					skuBookVo.setBillType(accConVo.getBillType());
					skuBookVo.setIoFlag(ioFlag);//进出标识(I=入库 O=出库)
					skuBookVo.setCellId(0L);
					skuBookVo.setCellNo(StringUtils.isEmpty(tmp.getCellNo())?"0":tmp.getCellNo());
					skuBookVo.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
					skuBookVo.setConNo(tmp.getConNo());
					skuBookVo.setQty(tmp.getQty());
					//skuBookVo.setInstockQty(tmp.getInstockQty());
					//skuBookVo.setOutstockQty(tmp.getOutstockQty());
					//skuBookVo.setHmManualFlag(tmp.getHmManualFlag());
					
					//记容器商品明细实时账
					if(skuBookVo.getQty().intValue()!=0&&!StringUtils.isEmpty(skuBookVo.getConNo())&&!"N".equalsIgnoreCase(skuBookVo.getConNo())){
						accContainerSkuService.saveOrUpdateAccContainerSku(skuBookVo);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException("accountForAccContainerSkuByBill记账异常。billNo:"+ accConVo.getBillNo()+" billType:"+accConVo.getBillType()+" ioFlag："+ioFlag,e);
		}
		log.info("accountForAccContainerSkuByBill end.");
	}
	/**
	 * 板上拆箱和拼箱处理子容器数量
	 */
	private void handleParentCon(AccInventoryConVo aconVo){
		String conNo = aconVo.getsContainerNo();
		log.info("parent_con_no:" + conNo );
		if(!StringUtils.isEmpty(conNo)){
			AccInventoryCon parentContemp = new AccInventoryCon();
			parentContemp.setLocno(aconVo.getLocno());
			parentContemp.setConNo(conNo);
			AccInventoryCon parentCon = accInventoryConMapper.selectAccInvertyroConExist(parentContemp);
			if (null != parentCon) {
				parentCon.setChildrenQty(parentCon.getChildrenQty().subtract(new BigDecimal(1)));
				parentCon.setEditor(aconVo.getCreator());
				if (parentCon.getChildrenQty().intValue() <= 0) {
					log.info("处理板子容器 parentConNo:" + parentCon.getConNo());
					int parentRows = accInventoryConMapper.deleteAccinverntoryCon(parentCon);
					log.info("handleParentCon parent_con_no:" + parentCon.getConNo() + " rows:"+ parentRows);
				} else {
					accInventoryConMapper.updateAccountConByPrimaryKey(parentCon);
				}
			} else {
				log.info("parent_con_no is null:" + conNo);
			}
		}
	}
}