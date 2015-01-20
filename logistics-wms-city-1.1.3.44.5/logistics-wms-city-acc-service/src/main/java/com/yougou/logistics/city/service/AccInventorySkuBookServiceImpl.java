package com.yougou.logistics.city.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.city.common.model.AccInventorySku;
import com.yougou.logistics.city.common.model.AccInventorySkuBook;
import com.yougou.logistics.city.common.model.AccTask;
import com.yougou.logistics.city.common.model.AccTaskDtl;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.vo.AccCheckDtlVo;
import com.yougou.logistics.city.common.vo.AccInventoryConVo;
import com.yougou.logistics.city.common.vo.AccInventorySkuBookVo;
import com.yougou.logistics.city.common.vo.BillConAdjVo;
import com.yougou.logistics.city.dal.database.AccCheckDtlMapper;
import com.yougou.logistics.city.dal.database.AccInventorySkuBookMapper;
import com.yougou.logistics.city.dal.database.AccInventorySkuMapper;
import com.yougou.logistics.city.dal.database.AccTaskDtlMapper;
import com.yougou.logistics.city.dal.database.AccTaskMapper;
import com.yougou.logistics.city.dal.mapper.BillConAdjMapper;

/**
 * 库存记账类
 * @author wugy
 * @date  2014-07-11 15:24:23
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
@Service("accInventorySkuBookService")
class AccInventorySkuBookServiceImpl extends BaseCrudServiceImpl implements AccInventorySkuBookService {
	private static Logger log = org.slf4j.LoggerFactory.getLogger("SKUCONSTOCK"); 
    @Resource
    private AccInventorySkuBookMapper accInventorySkuBookMapper;
    @Resource
    private AccInventorySkuMapper accInventorySkuMapper;
    @Resource
    private AccTaskMapper accTaskMapper;
    @Resource
    private AccTaskDtlMapper accTaskDtlMapper;
    @Resource
    private AccCheckDtlMapper accCheckDtlMapper;
    @Resource
    private AccInventoryConService accInventoryConService;
    @Resource
    private AccContainerSkuService accContainerSkuService;
    @Resource
    private BillConAdjMapper billConAdjMapper;
    

    @Override
    public BaseCrudMapper init() {
        return accInventorySkuBookMapper;
    }
	
	/**
	 * 取绝对值
	 * @param n
	 * @return
	 */
	private BigDecimal Mathabs(BigDecimal n){
		return BigDecimal.valueOf(Math.abs(n.intValue()));
	};

	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void addSkuBookTran(AccInventorySkuBookVo skuBookVo) throws ServiceException {
		try {
			accountingForSku(skuBookVo);
			
			log.info("addSkuBookTran() "+skuBookVo.getBillNo());
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void accountingForSku(AccInventorySkuBookVo skuBookVo) throws ServiceException {
		log.info("accountingForSku() begin. conNo:"+skuBookVo.getConNo()+" cellNo:"+skuBookVo.getCellNo()+" cellId:"+skuBookVo.getCellId()
				+" itemNo:" + skuBookVo.getItemNo()+ " barcode:" + skuBookVo.getBarcode() + " qty:"+ skuBookVo.getQty() + " InstockQty:"
				+ skuBookVo.getInstockQty()+" OutstockQty:"+skuBookVo.getOutstockQty()+" ioFlag："+skuBookVo.getIoFlag());
		long begin=System.currentTimeMillis();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("locno", skuBookVo.getLocno());
		params.put("ownerNo", skuBookVo.getOwnerNo());
		params.put("itemNo", skuBookVo.getItemNo());
		params.put("itemType", skuBookVo.getItemType());
		params.put("barcode", skuBookVo.getBarcode());
		params.put("quality", skuBookVo.getQuality());
		params.put("cellNo", skuBookVo.getCellNo());
		if(skuBookVo.getCellId()>0){
			params.put("cellId", skuBookVo.getCellId());
		}
		//为当前sku库存总账加锁
		int lockrows=accInventorySkuMapper.updateLockAccInventorySku(params);
		AccInventorySku sku=new AccInventorySku();
		List<AccInventorySku>  list=accInventorySkuMapper.selectByParams(new AccInventorySku(), params);
		log.info("lockrows:"+lockrows+" listsize:"+list.size());
		if(list.size()>0){
			sku=list.get(0);
			if("I".equalsIgnoreCase(skuBookVo.getIoFlag())){
				//入库则增加
				sku.setQty(sku.getQty().add(skuBookVo.getQty()));
			}
			else{
				//出库则减少
				sku.setQty(sku.getQty().subtract(skuBookVo.getQty()));
				//不允许负库存出库
				if(sku.getQty().intValue()<0){
					log.error("["+skuBookVo.getItemNo()+"商品，"+skuBookVo.getBarcode()+"条码不允许负库存出库]");
					return;
				}
			}
			
			//预下、预上与出入库I/O无关，对传值（含正负）累加
			sku.setInstockQty(sku.getInstockQty().add(skuBookVo.getInstockQty()));
			sku.setOutstockQty(sku.getOutstockQty().add(skuBookVo.getOutstockQty()));
			sku.setEditor(skuBookVo.getCreator());
			sku.setEdittm(new Date());
			accInventorySkuMapper.updateAccInventorySkuStockQtyByPrimaryKey(sku);
			
			//QTY、预下、预上数量都为0的要删除
			if(sku.getQty().intValue()==0&&sku.getInstockQty().intValue()==0&&sku.getOutstockQty().intValue()==0){
				int rows=accInventorySkuMapper.deleteAllStockIs0(sku);
				log.info("deleteAllStockIs0() deleted rows:"+rows);
			}
		}
		else{
			sku.setLocno(skuBookVo.getLocno());
			sku.setOwnerNo(skuBookVo.getOwnerNo());
			sku.setBarcode(skuBookVo.getBarcode());
			sku.setQuality(skuBookVo.getQuality());
			sku.setSupplierNo(skuBookVo.getSupplierNo());
			sku.setItemNo(skuBookVo.getItemNo());
			sku.setItemType(skuBookVo.getItemType());
			sku.setQty(skuBookVo.getQty());//发生数量
			sku.setInstockQty(skuBookVo.getInstockQty());
			sku.setOutstockQty(skuBookVo.getOutstockQty());
			sku.setCellNo(skuBookVo.getCellNo());
			sku.setStatus("0");
			sku.setFlag("0");
			sku.setHmManualFlag(StringUtils.isEmpty(skuBookVo.getHmManualFlag())? "1":skuBookVo.getHmManualFlag());
			sku.setCreator(skuBookVo.getCreator());
			sku.setCreatetm(new Date());
			accInventorySkuMapper.insert(sku);
		}
		
		addSkuBook(sku,skuBookVo);//保存SKU三级账流水
		
		//记容器商品明细实时账
		if (skuBookVo.isNeedUpdateAccContainerSku() == true
				&& skuBookVo.getQty().intValue() != 0
				&& !StringUtils.isEmpty(skuBookVo.getConNo())
				&& !"N".equalsIgnoreCase(skuBookVo.getConNo())) {
			accContainerSkuService.saveOrUpdateAccContainerSku(skuBookVo);
		}
		log.info("accountingForSku() usedtime:"+(System.currentTimeMillis()-begin)+"ms");
	}
	
	/**
	 * 保存SKU三级账
	 * @param map
	 * @param skuBookVo
	 */
	private void addSkuBook(AccInventorySku sku,AccInventorySkuBookVo skuBookVo){
		AccInventorySkuBook skuBook=new AccInventorySkuBook();
		skuBook.setLocno(skuBookVo.getLocno());
		skuBook.setOwnerNo(skuBookVo.getOwnerNo());
		skuBook.setItemNo(skuBookVo.getItemNo());
		skuBook.setBarcode(skuBookVo.getBarcode());
		skuBook.setCellId(skuBookVo.getCellId());
		skuBook.setQuality(skuBookVo.getQuality());
		skuBook.setSupplierNo(skuBookVo.getSupplierNo());
		skuBook.setItemType(skuBookVo.getItemType());
		skuBook.setBillNo(skuBookVo.getBillNo());
		skuBook.setBillType(skuBookVo.getBillType());
		skuBook.setIoFlag(skuBookVo.getIoFlag());//进出标识(I=入库 O=出库)
		skuBook.setCellNo(skuBookVo.getCellNo());
		skuBook.setSeqId(BigDecimal.valueOf(1));
		skuBook.setCreator(skuBookVo.getCreator());
		skuBook.setConNo(StringUtils.isEmpty(skuBookVo.getConNo())? "N":skuBookVo.getConNo());
		skuBook.setCreatetm(new Date());
		skuBook.setCellId(sku.getCellId());
		skuBook.setTerminalFlag(StringUtils.isEmpty(skuBookVo.getTerminalFlag())? "1":skuBookVo.getTerminalFlag());
		skuBook.setStatusTrans("13");
		
		//数量、预上、预下分别记skubook账
		if(skuBookVo.getQty().intValue()!=0){
			skuBook.setDirection(skuBookVo.getQty().intValue()<0? -1L:1L);
			skuBook.setMoveQty(Mathabs(skuBookVo.getQty()));//取绝对值计数
			skuBook.setBalanceQty(sku.getQty());
			skuBook.setPreFlag("0");//财务类型(0=数量 1=预上 2=预下)
			accInventorySkuBookMapper.insert(skuBook);
		}
		if(skuBookVo.getInstockQty().intValue()!=0){
			skuBook.setDirection(skuBookVo.getInstockQty().intValue()<0? -1L:1L);
			skuBook.setMoveQty(Mathabs(skuBookVo.getInstockQty()));
			skuBook.setBalanceQty(sku.getInstockQty());
			skuBook.setPreFlag("1");
			accInventorySkuBookMapper.insert(skuBook);
		}
		if(skuBookVo.getOutstockQty().intValue()!=0){
			skuBook.setDirection(skuBookVo.getOutstockQty().intValue()<0? -1L:1L);
			skuBook.setMoveQty(Mathabs(skuBookVo.getOutstockQty()));
			skuBook.setBalanceQty(sku.getOutstockQty());
			skuBook.setPreFlag("2");
			accInventorySkuBookMapper.insert(skuBook);
		}
		
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public synchronized void accountForSkuConList(AccTask accTask,List<AccCheckDtlVo> list)
			throws ServiceException {
		log.info("accountForSkuConList() begin ");
		long begin=System.currentTimeMillis();
		String billType=accTask.getBillType();
		String ioFlag=accTask.getIoFlag();
		long direction=accTask.getIoFlag().equals("I")?1:-1;
		
		Map<String, AccInventoryConVo> conMap=new HashMap<String, AccInventoryConVo>();
		String key="";
		AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
		AccInventoryConVo tempcon = new AccInventoryConVo();
		for(AccCheckDtlVo  tmp:list){
			//sku库存记账
			skuBookVo=new AccInventorySkuBookVo();
			skuBookVo.setLocno(tmp.getLocno());
			skuBookVo.setOwnerNo(tmp.getOwnerNo());
			skuBookVo.setItemNo(tmp.getItemNo());
			skuBookVo.setBarcode(tmp.getBarcode());
			skuBookVo.setQuality(tmp.getQuality());
			skuBookVo.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
			skuBookVo.setItemType(tmp.getItemType());
			skuBookVo.setDirection(direction);//记账方向(1：增加库存，-1：减少库存)
			skuBookVo.setBillNo(tmp.getBillNo());
			skuBookVo.setBillType(billType);
			skuBookVo.setIoFlag(ioFlag);//进出标识(I=入库 O=出库)
			skuBookVo.setCellId(0L);
			skuBookVo.setCellNo(StringUtils.isEmpty(tmp.getCellNo())?"0":tmp.getCellNo());
			skuBookVo.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
			skuBookVo.setConNo(tmp.getConNo());
			skuBookVo.setQty(tmp.getQty());
			skuBookVo.setInstockQty(tmp.getInstockQty());
			skuBookVo.setOutstockQty(tmp.getOutstockQty());
			skuBookVo.setHmManualFlag(tmp.getHmManualFlag());
			accountingForSku(skuBookVo);
			
			//箱号不为空时，记容器库存账
			if(!StringUtils.isEmpty(tmp.getConNo())&&!"N".equalsIgnoreCase(tmp.getConNo())&&Math.abs(tmp.getQty().intValue())>0){
				tempcon = new AccInventoryConVo();
				tempcon.setCellNo(StringUtils.isEmpty(tmp.getCellNo())?"0":tmp.getCellNo());
				tempcon.setConNo(tmp.getConNo());
				tempcon.setLocno(tmp.getLocno());
				tempcon.setOwnerNo(tmp.getOwnerNo());
				tempcon.setItemType(tmp.getItemType());
				tempcon.setBillNo(tmp.getBillNo());
				tempcon.setBillType(billType);
				tempcon.setQuality(tmp.getQuality());
				tempcon.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
				tempcon.setMoveChildrenQty(BigDecimal.valueOf(0));
				tempcon.setMoveSkuQty(tmp.getQty());//发生数量
				tempcon.setSkuQty(BigDecimal.valueOf(0));
				tempcon.setChildrenQty(BigDecimal.valueOf(0));
				tempcon.setDirection((int)direction);//记账方向(1：增加库存，-1：减少库存)
				tempcon.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
				tempcon.setTerminalFlag(tmp.getTerminalFlag());
				
				//按箱及单据汇总后再记容器账
				key=tmp.getCellNo()+tmp.getConNo()+tmp.getLocno()+tmp.getOwnerNo()+tmp.getItemType()+tmp.getBillNo()+billType;
				if(conMap.get(key)!=null){
					AccInventoryConVo obj=conMap.get(key);
					obj.setMoveSkuQty(obj.getMoveSkuQty().add(tempcon.getMoveSkuQty()));
					conMap.put(key,obj);
				}else{
					conMap.put(key, tempcon);
				}
			}
		}
		
		AccInventoryConVo conVo = new AccInventoryConVo();
		
		Iterator iter = conMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			conVo = (AccInventoryConVo) entry.getValue();
			accInventoryConService.accontingForCon(conVo);
		}
		log.info("accountForSkuConList() end listsize:"+list.size()+" usedtim:"+(System.currentTimeMillis()-begin)+"ms");
	}
	
	/**
	 * 按单据查询的准备参数
	 * (处理CA类型的cellNo)
	 * @param params
	 * @return
	 */
	@Override
	public HashMap<String, Object> SelectPrepareAccDtlParams(HashMap<String, Object> params)  throws ServiceException {
		log.info("SelectPrepareAccDtlParams begin. params:"+params.toString());
		if("CA".equals(params.get("billType").toString())&&"I".equals(params.get("ioFlag").toString())){
			BillConAdj temp=new BillConAdj();
			temp.setAdjNo(params.get("billNo").toString());
			try {
				BillConAdj billConAdj=billConAdjMapper.selectOneByAdjNo(temp);
				if(billConAdj!=null){
					params.put("v_strFlag",billConAdj.getCellAdjFlag());
					
					BillConAdjVo bavTmp=new BillConAdjVo();
					bavTmp.setAdjNo(temp.getAdjNo());
					bavTmp.setLocNo(billConAdj.getLocno());
					bavTmp.setVsourceType(billConAdj.getSourceType());
					BillConAdjVo billConAdjVo=billConAdjMapper.selectBillConAdjVoBytype(bavTmp);
					if(billConAdjVo!=null){
						params.put("V_D_CELL_NO",billConAdjVo.getCellNo());
					}
					else{
						if("0".equals(billConAdj.getSourceType())){
							log.error("仓库["+billConAdj.getLocno()+"]库存调整区未设定!");
						}
						else if("1".equals(billConAdj.getSourceType())){
							log.error("仓库["+billConAdj.getLocno()+"]库存调整区目的储位未设定!");
						}
					}
				}
			} catch (DaoException e) {
				throw new ServiceException("SelectPrepareAccDtlParams exception:",e);
			}
		}
		log.info("SelectPrepareAccDtlParams end. params:"+params.toString());
		return params;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void accountForSkuConByBillTran(AccTask accTask) throws ServiceException {
		log.info("accountForSkuConByBillTran begin... billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag："+accTask.getIoFlag());
		try {
			accTask.setAccFlag("Y");
			accTask.setAccBeginTime(new Date());
			accTaskMapper.updateLockForAccTask(accTask);
			HashMap<String, Object> params=new HashMap<String, Object>();
			params.put("billNo", accTask.getBillNo());
			params.put("billType", accTask.getBillType());
			params.put("ioFlag", accTask.getIoFlag());
			params.put("locType", '2');
			List<AccCheckDtlVo> list=accCheckDtlMapper.selectAccCheckDtlByParams(SelectPrepareAccDtlParams(params));
			
			int splitMaxNum=10000; //分拆事务，sku大于该数拆
			if(list.size()>splitMaxNum){
				//拆分list记账
				splitAccCheckDtlVoListAccounting(accTask, list,splitMaxNum); 
			}
			else{
				//批量记账
				accountForSkuConList(accTask,list);
			}
			accTask.setAccEndTime(new Date());
			accTaskMapper.updateByPrimaryKeySelective(accTask); //更新记账状态
		} catch (Exception e) {
			throw new ServiceException("accountForSkuConByBillTran记账异常。billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag："+accTask.getIoFlag(),e);
		}
		log.info("accountForSkubatchAddSkuBookConByBillTran end...");
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void accountForSkuConByBillTranNoTask(AccTask accTask) throws ServiceException {
		log.info("accountForSkuConByBillTranNoTask begin... billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag："+accTask.getIoFlag());
		try {
			HashMap<String, Object> params=new HashMap<String, Object>();
			params.put("billNo", accTask.getBillNo());
			params.put("billType", accTask.getBillType());
			params.put("ioFlag", accTask.getIoFlag());
			params.put("locType", '2');
			if(accTask.getOutStockFlag()!=null){
				params.put("outStockFlag", accTask.getOutStockFlag());
			}
			
			List<AccCheckDtlVo> list=accCheckDtlMapper.selectAccCheckDtlByParams(SelectPrepareAccDtlParams(params));
			int splitMaxNum=10000; //分拆事务，sku大于该数拆
			if(list.size()>splitMaxNum){
				//拆分list记账
				splitAccCheckDtlVoListAccounting(accTask, list,splitMaxNum); 
			}
			else{
				//批量记账
				accountForSkuConList(accTask,list);
			}
		} catch (Exception e) {
			throw new ServiceException("accountForSkuConByBillTranNoTask记账异常。billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag："+accTask.getIoFlag(),e);
		}
		log.info("accountForSkuConByBillTranNoTask end...");
	}
	
	/**
	 * 拆分list记账,
	 * @param accTask
	 * @param list
	 * @throws ServiceException
	 */
	private void splitAccCheckDtlVoListAccounting(AccTask accTask,List<AccCheckDtlVo> list,int splitMaxNum)  throws ServiceException{
		int totalNum = list.size();// 总行数
		int totalPage = 0;// 总页数
		int pageSize = splitMaxNum;// 每页显示行数
		int yuNum=totalNum%pageSize; //余数
		log.info("splitAccCheckDtlVoListAccounting... splitMaxNum:"+splitMaxNum+" listsize:"+totalNum);
		if (yuNum==0) {
			totalPage = (totalNum - 1) / pageSize;
		} else {
			totalPage = (totalNum - 1) / pageSize+ 1;
		}
		
		int startRow=0,endRow=0;
		List<AccCheckDtlVo> subList = new ArrayList<AccCheckDtlVo>();
		for (int currentPage = 1; currentPage <= totalPage; currentPage++) {
			startRow = (currentPage - 1) * pageSize;// 开始显示的行
			endRow = currentPage * pageSize;// 结束显示的行
			if (yuNum!=0&&currentPage==totalPage) {
				endRow = totalNum;
			} 
			
			subList = new ArrayList<AccCheckDtlVo>();
			subList = list.subList(startRow, endRow);//拆分list进行记账
			accountForSkuConList(accTask,subList);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void accountForSkuConByDetailRowIdTran(AccTask accTask) throws ServiceException {
		log.info("accountForSkuConByDetailRowIdTran begin... billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag:"+accTask.getIoFlag());
		try {
			accTask.setAccFlag("Y");
			accTask.setAccBeginTime(new Date());
			accTaskMapper.updateLockForAccTask(accTask);
			
			//根据复合主键查出单个明细
			AccTaskDtl accTaskDtl=new AccTaskDtl();
			accTaskDtl.setBillNo(accTask.getBillNo());
			accTaskDtl.setBillType(accTask.getBillType());
			accTaskDtl.setIoFlag(accTask.getIoFlag());
			accTaskDtl.setRowId(accTask.getDetailRowid());
			AccTaskDtl tmp=accTaskDtlMapper.selectByPrimaryKey(accTaskDtl);
			if(tmp==null){
				log.error("无法查到明细错误。billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag:"+accTask.getIoFlag());
			}
			else{
				long direction=accTask.getIoFlag().equals("I")?1:-1;
				//sku库存记账
				AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
				skuBookVo.setLocno(tmp.getLocno());
				skuBookVo.setOwnerNo(tmp.getOwnerNo());
				skuBookVo.setItemNo(tmp.getItemNo());
				skuBookVo.setBarcode(tmp.getBarcode());
				skuBookVo.setQuality(tmp.getQuality());
				skuBookVo.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
				skuBookVo.setItemType(tmp.getItemType());
				skuBookVo.setDirection(direction);//记账方向(1：增加库存，-1：减少库存)
				skuBookVo.setBillNo(tmp.getBillNo());
				skuBookVo.setBillType(accTask.getBillType());
				skuBookVo.setIoFlag(accTask.getIoFlag());//进出标识(I=入库 O=出库)
				skuBookVo.setPreFlag("0");//财务类型(0=数量 1=预上 2=预下)
				skuBookVo.setCellId(0L);
				skuBookVo.setCellNo(StringUtils.isEmpty(tmp.getCellNo())?"0":tmp.getCellNo());
				skuBookVo.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
				skuBookVo.setConNo(tmp.getBoxNo());
				skuBookVo.setQty(tmp.getQty());
				skuBookVo.setInstockQty(tmp.getInstockQty());
				skuBookVo.setOutstockQty(tmp.getOutstockQty());
				skuBookVo.setHmManualFlag(tmp.getHmManualFlag());
				accountingForSku(skuBookVo);
				
				//箱号不为空时，记容器库存账
				if(!StringUtils.isEmpty(tmp.getBoxNo())&&!"N".equalsIgnoreCase(tmp.getBoxNo())&&Math.abs(tmp.getQty().intValue())>0){
					AccInventoryConVo tempcon = new AccInventoryConVo();
					tempcon.setCellNo(tmp.getCellNo());
					tempcon.setConNo(tmp.getBoxNo());
					tempcon.setLocno(tmp.getLocno());
					tempcon.setOwnerNo(tmp.getOwnerNo());
					tempcon.setItemType(tmp.getItemType());
					tempcon.setBillNo(tmp.getBillNo());
					tempcon.setBillType(accTask.getBillType());
					tempcon.setQuality(tmp.getQuality());
					tempcon.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
					tempcon.setMoveChildrenQty(BigDecimal.valueOf(0));
					tempcon.setMoveSkuQty(tmp.getQty());//发生数量
					tempcon.setSkuQty(BigDecimal.valueOf(0));
					tempcon.setChildrenQty(BigDecimal.valueOf(0));
					tempcon.setDirection((int)direction);//记账方向(1：增加库存，-1：减少库存)
					tempcon.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
					tempcon.setTerminalFlag(tmp.getTerminalFlag());
					accInventoryConService.accontingForCon(tempcon);
				}
			}
			
			accTask.setAccEndTime(new Date());
			accTaskMapper.updateByPrimaryKeySelective(accTask); //更新记账状态
		} catch (Exception e) {
			throw new ServiceException("accountForSkuConByDetailRowIdTran记账异常。 billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag:"+accTask.getIoFlag(),e);
		}
		log.info("accountForSkuConByDetailRowIdTran end. rowId:"+accTask.getDetailRowid());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void accountForSkuConByDetailTranNoTask(AccTask accTask,AccTaskDtl accTaskDtl)throws ServiceException {
		log.info("accountForSkuConByDetailTranNoTask begin... billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag:"+accTask.getIoFlag());
		try {
			if(accTaskDtl==null){
				log.error("明细不能为null。billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag:"+accTask.getIoFlag());
			}
			else{
				AccTaskDtl tmp=accTaskDtl;
				long direction=accTask.getIoFlag().equals("I")?1:-1;
				
				//sku库存记账
				AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
				skuBookVo.setLocno(tmp.getLocno());
				skuBookVo.setOwnerNo(tmp.getOwnerNo());
				skuBookVo.setItemNo(tmp.getItemNo());
				skuBookVo.setBarcode(tmp.getBarcode());
				skuBookVo.setQuality(tmp.getQuality());
				skuBookVo.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
				skuBookVo.setItemType(tmp.getItemType());
				skuBookVo.setDirection(direction);//记账方向(1：增加库存，-1：减少库存)
				skuBookVo.setBillNo(tmp.getBillNo());
				skuBookVo.setBillType(accTask.getBillType());
				skuBookVo.setIoFlag(accTask.getIoFlag());//进出标识(I=入库 O=出库)
				skuBookVo.setPreFlag("0");//财务类型(0=数量 1=预上 2=预下)
				skuBookVo.setCellId(0L);
				skuBookVo.setCellNo(StringUtils.isEmpty(tmp.getCellNo())?"0":tmp.getCellNo());
				skuBookVo.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
				skuBookVo.setConNo(tmp.getBoxNo());
				skuBookVo.setQty(tmp.getQty());
				skuBookVo.setInstockQty(tmp.getInstockQty());
				skuBookVo.setOutstockQty(tmp.getOutstockQty());
				skuBookVo.setTerminalFlag(tmp.getTerminalFlag());
				skuBookVo.setHmManualFlag(tmp.getHmManualFlag());
				accountingForSku(skuBookVo);
				
				//箱号不为空时，记容器库存账
				if(!StringUtils.isEmpty(tmp.getBoxNo())&&!"N".equalsIgnoreCase(tmp.getBoxNo())&&Math.abs(tmp.getQty().intValue())>0){
					AccInventoryConVo tempcon = new AccInventoryConVo();
					tempcon.setCellNo(tmp.getCellNo());
					tempcon.setConNo(tmp.getBoxNo());
					tempcon.setLocno(tmp.getLocno());
					tempcon.setOwnerNo(tmp.getOwnerNo());
					tempcon.setItemType(tmp.getItemType());
					tempcon.setBillNo(tmp.getBillNo());
					tempcon.setBillType(accTask.getBillType());
					tempcon.setQuality(tmp.getQuality());
					tempcon.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
					tempcon.setMoveChildrenQty(BigDecimal.valueOf(0));
					tempcon.setMoveSkuQty(tmp.getQty());//发生数量
					tempcon.setSkuQty(BigDecimal.valueOf(0));
					tempcon.setChildrenQty(BigDecimal.valueOf(0));
					tempcon.setDirection((int)direction);//记账方向(1：增加库存，-1：减少库存)
					tempcon.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
					tempcon.setTerminalFlag(tmp.getTerminalFlag());
					accInventoryConService.accontingForCon(tempcon);
				}
			}
			
			accTask.setAccEndTime(new Date());
			accTaskMapper.updateByPrimaryKeySelective(accTask); //更新记账状态
		} catch (Exception e) {
			throw new ServiceException("accountForSkuConByDetailTranNoTask记账异常。 billNo:"+ accTask.getBillNo()+" billType:"+accTask.getBillType()+" ioFlag:"+accTask.getIoFlag(),e);
		}
		log.info("accountForSkuConByDetailTranNoTask end. rowId:"+accTask.getDetailRowid());
	}


	@Override
	public int deleteAllStockIs0(
			AccInventorySku accInventorySku) throws ServiceException {
		return accInventorySkuMapper.deleteAllStockIs0(accInventorySku);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED, rollbackFor = ServiceException.class)
	public synchronized void updateCellNoConNoSkuStock(Map<String, Object> params) throws ServiceException {
		log.info("updateCellNoConNoSkuStock begin. params:"+params.toString());
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("billNo", params.get("billNo"));
			map.put("billType", params.get("billType"));
			map.put("ioFlag", "O");
			map.put("locType", '2');
			
			List<AccCheckDtlVo> list = accCheckDtlMapper.selectAccCheckDtlByParams(map);
			Map<String,Object> upmap=new HashMap<String,Object>();
			upmap.put("billNo", params.get("billNo"));
			upmap.put("billType",params.get("billType"));
			upmap.put("actType",params.get("actType"));
			if(list.size()>0){
				list=clearListWithNoEqConNo(list, params.get("conNo").toString());
				if(params.get("cellNo")!=null){
					//源储位减
					upmap.put("ioFlag","O");
					upmap.put("cellNo", params.get("cellNo"));
					upmap.put("conNo", params.get("conNo"));
					updateCellNoConNoSkuStockByList(upmap, list);
				}
				
				if(params.get("cellNo_d")!=null){
					//目标储位加
					upmap.put("ioFlag","I");
					upmap.put("cellNo", params.get("cellNo_d"));
					upmap.put("conNo", params.get("conNo_d"));
					updateCellNoConNoSkuStockByList(upmap, list);
				}
			}
			
			/*map.put("ioFlag", "I");
			list = accCheckDtlMapper.selectAccCheckDtlByParams(map);
			if(list.size()>0){
				if(params.get("cellNo_d")!=null){
					//目标储位加
					upmap.put("ioFlag","I");
					upmap.put("cellNo", params.get("cellNo_d"));
					upmap.put("conNo", params.get("conNo_d"));
					updateCellNoConNoSkuStockByList(upmap, list);
				}
			}*/
			log.info("updateCellNoConNoSkuStock end. listsize:"+list.size());
		} catch (DaoException e) {
			log.error("updateCellNoConNoSkuStock error:",e);
		}
	}
	
	/**
	 * 移除list中conNo不相同的对象
	 * @param list
	 * @param conNo
	 * @return
	 */
	private List<AccCheckDtlVo> clearListWithNoEqConNo(List<AccCheckDtlVo> list,String conNo){
		List<AccCheckDtlVo> retlist=new ArrayList<AccCheckDtlVo>();
		for(AccCheckDtlVo tmp:list){
			if(tmp.getConNo().equals(conNo)){
				retlist.add(tmp);
			}
		}
		return retlist;
	}
	
	/**
	 * 通过源list更新储位库存
	 * @param params
	 * @param list
	 * @throws ServiceException
	 */
	public void updateCellNoConNoSkuStockByList(Map<String, Object> params,List<AccCheckDtlVo> list) throws ServiceException {
		log.info("updateCellNoConNoSkuStockByList begin. params:"+params.toString()+" listsize:"+list.size()+"");
		String billNo=params.get("billNo").toString();
		String billType=params.get("billType").toString();
		String cellNo=params.get("cellNo").toString();
		String conNo=params.get("conNo").toString();
		String ioFlag=params.get("ioFlag").toString();
		String actType=params.get("actType").toString();
		long direction=ioFlag.equals("I")?1L:-1L;
		AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
		AccInventoryConVo tempcon = new AccInventoryConVo();
		for(AccCheckDtlVo tmp:list){
			//sku库存记账
			skuBookVo=new AccInventorySkuBookVo();
			skuBookVo.setLocno(tmp.getLocno());
			skuBookVo.setOwnerNo(tmp.getOwnerNo());
			skuBookVo.setItemNo(tmp.getItemNo());
			skuBookVo.setBarcode(tmp.getBarcode());
			skuBookVo.setQuality(tmp.getQuality());
			skuBookVo.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
			skuBookVo.setItemType(tmp.getItemType());
			skuBookVo.setDirection(direction);//记账方向(1：增加库存，-1：减少库存)
			skuBookVo.setBillNo(billNo);
			skuBookVo.setBillType(billType);
			skuBookVo.setIoFlag(ioFlag);//进出标识(I=入库 O=出库)
			skuBookVo.setPreFlag("0");//财务类型(0=数量 1=预上 2=预下)
			skuBookVo.setCellId(0L);
			skuBookVo.setCellNo(StringUtils.isEmpty(cellNo)?"0":cellNo);
			skuBookVo.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
			skuBookVo.setConNo(conNo);
			skuBookVo.setQty(tmp.getQty());
			skuBookVo.setInstockQty(BigDecimal.valueOf(tmp.getInstockQty().intValue()*direction));
			skuBookVo.setOutstockQty(BigDecimal.valueOf(tmp.getOutstockQty().intValue()*direction));
			skuBookVo.setHmManualFlag("");
			skuBookVo.setNeedUpdateAccContainerSku(false);//明细账已在accountForAccContainerSkuByBill()处理，此处不需再更新
			accountingForSku(skuBookVo);
			
			//箱号不为空时，记容器库存账(绑板操作容器的储位变更)
			if("2".equals(actType)&&!StringUtils.isEmpty(tmp.getConNo())&&!"N".equalsIgnoreCase(tmp.getConNo())&&Math.abs(tmp.getQty().intValue())>0){
				tempcon = new AccInventoryConVo();
				tempcon.setCellNo(cellNo);
				tempcon.setConNo(conNo);
				tempcon.setLocno(tmp.getLocno());
				tempcon.setOwnerNo(tmp.getOwnerNo());
				tempcon.setItemType(tmp.getItemType());
				tempcon.setBillNo(billNo);
				tempcon.setBillType(billType);
				tempcon.setQuality(tmp.getQuality());
				tempcon.setSupplierNo(StringUtils.isEmpty(tmp.getSupplierNo())? "N":tmp.getSupplierNo());
				tempcon.setMoveChildrenQty(BigDecimal.valueOf(0));
				tempcon.setMoveSkuQty(tmp.getQty());//发生数量
				tempcon.setSkuQty(BigDecimal.valueOf(0));
				tempcon.setChildrenQty(BigDecimal.valueOf(0));
				tempcon.setDirection((int)direction);//记账方向(1：增加库存，-1：减少库存)
				tempcon.setCreator(StringUtils.isEmpty(tmp.getCreator())? "N":tmp.getCreator());
				tempcon.setTerminalFlag(tmp.getTerminalFlag());
				accInventoryConService.accontingForCon(tempcon);
			}
		}
		log.info("updateCellNoConNoSkuStockByList end.");
	}
	
}