package com.yougou.logistics.city.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.dto.BillAccControlDto;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.enums.ContainerTypeEnums;
import com.yougou.logistics.city.common.enums.ContentStatusEnums;
import com.yougou.logistics.city.common.enums.ItemTypeEnums;
import com.yougou.logistics.city.common.enums.QualityEnums;
import com.yougou.logistics.city.common.model.AccContainerSku;
import com.yougou.logistics.city.common.model.AccInventoryCon;
import com.yougou.logistics.city.common.model.BillConAdj;
import com.yougou.logistics.city.common.model.BillConAdjDtl;
import com.yougou.logistics.city.common.model.BillConAdjDtlSizeDto;
import com.yougou.logistics.city.common.model.BillConAdjKey;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.CmDefcell;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.ConContentMove;
import com.yougou.logistics.city.common.model.Lookupdtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.service.AccContainerSkuService;
import com.yougou.logistics.city.service.AccInventoryConService;
import com.yougou.logistics.city.service.BillAccControlService;
import com.yougou.logistics.city.service.BillConAdjDtlService;
import com.yougou.logistics.city.service.BillConAdjService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.CmDefcellService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ConContentMoveService;
import com.yougou.logistics.city.service.ConContentService;
import com.yougou.logistics.city.service.ProcCommonService;

/**
 * 请写出类的用途 
 * @author chen.yl1
 * @date  2014-01-15 17:53:08
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
@Service("billConAdjDtlManager")
class BillConAdjDtlManagerImpl extends BaseCrudManagerImpl implements BillConAdjDtlManager {
	@Resource
	CmDefcellService cmDefcellService;
	
	@Resource
	private ProcCommonService procCommonService;
	
	@Resource
	private ConContentService conContentService;
	
	@Resource
	private BillConAdjService billConAdjService;
	
	@Resource
    private BillConAdjDtlService billConAdjDtlService;
    
	@Resource
	private BillAccControlService billAccControlService;
	
	@Resource
	private LookupdtlManager lookupdtlManager;
	
	@Resource
	private BmContainerService bmContainerService;
	@Resource
	private AccInventoryConService accInventoryConService;
	@Resource
	private AccContainerSkuService accContainerSkuService;
	@Resource
	private ConBoxService conBoxService;

	@Log
	private Logger log;
	@Resource
	private ConContentMoveService conContentMoveService;
	
    @Override
    public BaseCrudService init() {
        return billConAdjDtlService;
    }
    
    private final static String ILOCTYPE = "2";
    
	@Override
	public List<BillConAdjDtl> selectItem(SimplePage page, String orderByField,
			String orderBy, Map<String, Object> params,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billConAdjDtlService.selectItem(page, orderByField, orderBy, params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	public int selectItemCount(Map<String, Object> params,AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return this.billConAdjDtlService.selectItemCount(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> examineAdj(String ids, String auditor, String userName,AuthorityParams authorityParams)
			throws ManagerException {
		Map<String, Object> reslut = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			boolean flag = false;
			if(StringUtils.isNotBlank(ids)){
				String[] idArr = ids.split(",");
				for(String id : idArr){
					flag = false;
					String[] tmp = id.split("-");
					if(tmp.length==3){
						String locno = tmp[0];
						String ownerNo = tmp[1];
						String adjNo = tmp[2];
						
						//查询主档
						BillConAdj billParams=new BillConAdj();
						billParams.setAdjNo(adjNo);
						billParams.setLocno(locno);
						billParams.setOwnerNo(ownerNo);
						BillConAdj billConAdj=billConAdjService.findById(billParams);
						
						//查询明细中的储位
						Map<String,Object> dtlCellParams=new HashMap<String,Object>();
						dtlCellParams.put("locno", locno);
						dtlCellParams.put("adjNo", adjNo);
						dtlCellParams.put("ownerNo", ownerNo);
						List<String> cellList=billConAdjDtlService.findDtlCell(dtlCellParams);
			
						//整储位调整
						String cellAdjFlag=billConAdj.getCellAdjFlag();
						//调整类型 0---品质转换；1--属性转换
						String adjType=billConAdj.getAdjType();
						//调整后类型  ADJ_TYPE 0-写品质；1-写属性
						String dItemType=billConAdj.getdItemType();
						
						//如果是整储位调整，则需解冻储位,并调整储位的品质或属性
						if("1".equals(cellAdjFlag)){
							for(String cellNo:cellList){
								CmDefcell cdParams=new CmDefcell();
								cdParams.setLocno(locno);
								cdParams.setCellNo(cellNo);
								cdParams.setCellStatus("0");//解冻
								if(adjType.equals("0")){
									cdParams.setAreaQuality(dItemType);
								}else if(adjType.equals("1")){
									cdParams.setItemType(dItemType);
								}					
								cmDefcellService.modifyById(cdParams);					
							}			
						}
						//记账存储过程
						BillAccControlDto controlDto = new BillAccControlDto();
						controlDto.setiPaperNo(adjNo);
						controlDto.setiLocType(ILOCTYPE);
						controlDto.setiPaperType("CA");
						controlDto.setiIoFlag("O");
						controlDto.setiPrepareDataExt(new BigDecimal(0));
						controlDto.setiIsWeb(new BigDecimal(1));
						try {
							billAccControlService.procAccApply(controlDto);
						} catch (ServiceException es) {
							log.error(es.getMessage(), es);
							throw new ManagerException(es.getMessage());
						}
						//查询库存调整明细在库存表中的ID，并解锁库存
						Map<String,Object> findConIdParams=new HashMap<String,Object>();
						findConIdParams.put("adjNo", adjNo);
						findConIdParams.put("locno", locno);
						findConIdParams.put("hmManualFlag", "0");
						List<String> conIdList=billConAdjDtlService.findConIdFromDtl(findConIdParams);
						for(String conId:conIdList){
							procCommonService.UpdateContentStatus(locno, conId, null,null, null, "1", auditor);
						}
						//审核更改状态
						BillConAdj billAdj = new BillConAdj();
						billAdj.setAdjNo(adjNo);
						billAdj.setLocno(locno);
						billAdj.setOwnerNo(ownerNo);
						billAdj.setStatus("13");
						billAdj.setAuditor(auditor);
						billAdj.setAuditorName(userName);
						billAdj.setAudittm(new Date());
						billAdj.setUpdStatus("10");
						int i = billConAdjService.modifyById(billAdj);
						if(i != 1) {
							throw new ManagerException("单据"+adjNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
						} else {
							flag = true;
						}
						Map<String, Object> _map = new HashMap<String, Object>();
						_map.put("locno", locno);
						_map.put("adjNo", adjNo);
						_map.put("ownerNo", ownerNo);
						_map.put("editor", auditor);
						_map.put("editorName", userName);
						_map.put("edittm", new Date());
						billConAdjDtlService.updateOperateRecord(_map);
						//解锁容器占用状态
						Map<String,Object> billAdjMap=new HashMap<String,Object>();
						billAdjMap.put("locno", locno);
						billAdjMap.put("adjNo", adjNo);
						billAdjMap.put("status", ContentStatusEnums.STATUS_0.getStatus());
						billAdjMap.put("optBillType", "");
						billAdjMap.put("optBillNo", "");
						billAdjMap.put("editor", userName);
						bmContainerService.batchStatusByBillConAdj(billAdjMap);
						//更新con_box
						Map<String,Object> paramsMap=new HashMap<String,Object>();
						paramsMap.put("paperNo", adjNo);
						paramsMap.put("locno", locno);
						paramsMap.put("ownerNo", ownerNo);
						paramsMap.put("ioFlag", "I");
						paramsMap.put("preFlag", "0");
						SimplePage page = new SimplePage(1, 1,1);
						List<ConContentMove> conMoveList=conContentMoveService.findByPage(page, null, null, paramsMap, null, null);
						String dcellNo="";//目的储位
						if(null!=conMoveList && conMoveList.size()>0){
							ConContentMove conContent=conMoveList.get(0);
							dcellNo=conContent.getCellNo();
						}
						List<String> conList=billConAdjDtlService.findDtlCon(dtlCellParams);
						if(null!=conList && conList.size()>0){
							for(String conNo:conList){
								Map<String,Object> map=new HashMap<String,Object>();
								String []conNos=conNo.split(";");
								map.put("locno", locno);
								map.put("boxNo", conNos[0]);
								if(adjType.equals("0")){
									map.put("quality", dItemType);
								}else if(adjType.equals("1")){
									map.put("itemType", dItemType);
								}
								map.put("cellNo", dcellNo);
								if(conNos.length==1){
									map.put("panNoFlag", "true");//解绑
								}
								conBoxService.updateCellNoByBoxNo(map);
							}
						}
					}
				}
				if(flag) {
					reslut.put("flag", "true");
					reslut.put("msg", "审核成功");
				} else {
					throw new ManagerException("当前单据审核失败！");
				}
			} else {
				throw new ManagerException("当前单据状态不可审核！");
			}
			return reslut;
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public <ModelType> Map<String, Object> addDtlsave(BillConAdj billConAdj,
			Map<CommonOperatorEnum, List<ModelType>> params,String editor)
			throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		boolean flag = false;
		Date currentDate=new Date();
		try {
			/***********************************/
			String locno = billConAdj.getLocno();
			String ownerNo = billConAdj.getOwnerNo();
			String adjNo = billConAdj.getAdjNo();
			Map<String, Object> bill = new HashMap<String, Object>();
			bill.put("locNo", locno);
			bill.put("ownerNo", ownerNo);
			bill.put("adjNo", adjNo);
			bill.put("status", "10");
			List<BillConAdj> param = billConAdjService.findByBiz(billConAdj,bill);
			if(param != null && param.size() > 0) {
				List<ModelType> addList = params.get(CommonOperatorEnum.INSERTED);//新增
				List<ModelType> delList = params.get(CommonOperatorEnum.DELETED);//删除
				List<ModelType> uptList = params.get(CommonOperatorEnum.UPDATED);//更新
				
				//删除操作
				if (CommonUtil.hasValue(delList)) {
					List<BmContainer> bmList=new ArrayList<BmContainer>();
					flag = false;
					for(ModelType modelType : delList){
						if(modelType instanceof  BillConAdjDtl){
							BillConAdjDtl vo = (BillConAdjDtl) modelType;
							String adjNoDtl = adjNo;
							String locnoDtl = locno;
							String ownerNoDtl = ownerNo;
							String cellNoDtl = vo.getCellNo();
							String itemNoDtl = vo.getItemNo();
							String sizeNoDtl = vo.getSizeNo();
							String qualityDtl = vo.getQuality();
							String itemTypeDtl = vo.getItemType();
							String conNo=vo.getLabelNo();
							
							//获取库存的明细信息
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("locno", locnoDtl);
							queryParam.put("ownerNo", ownerNoDtl);
							queryParam.put("adjNo", adjNoDtl);
							queryParam.put("cellNo", cellNoDtl);
							queryParam.put("itemNo", itemNoDtl);
							queryParam.put("sizeNo", sizeNoDtl);
							queryParam.put("quality", qualityDtl);
							queryParam.put("itemType", itemTypeDtl);
							List<BillConAdjDtl> queryq = billConAdjDtlService.findByBiz(vo,queryParam);
							if(null!=queryq && queryq.size()>1){
								log.info("此商品包含整箱和零散，不解锁"+adjNoDtl+itemNoDtl);
							}else{
								List<String> conIdList=null;
								conIdList=billConAdjDtlService.findConIdFromDtl(queryParam);
								for(String conId:conIdList){
									//解锁库存 手工移库标识,1：可手工移库
									procCommonService.UpdateContentStatus(locno, conId, null,null, null,"1", editor);
								}
							}
							if(StringUtils.isNotEmpty(conNo)){
								Map<String, Object> conQueryParam = new HashMap<String, Object>();
								List<String> conIdList=null;
								conQueryParam.put("locno", locnoDtl);
								conQueryParam.put("ownerNo", ownerNoDtl);
								conQueryParam.put("adjNo", adjNoDtl);
								conQueryParam.put("cellNo", cellNoDtl);
								conQueryParam.put("labelNo", conNo);
								conIdList=billConAdjDtlService.findConIdFromDtl(conQueryParam);//解锁该容器商品
								for(String conId:conIdList){
									//解锁库存 手工移库标识,1：可手工移库
									procCommonService.UpdateContentStatus(locno, conId, null,null, null,"1", editor);
								}
								queryParam.put("labelNo", conNo);
							}else{
								queryParam.put("labelNo", "N");
							}
							List<BillConAdjDtl> query = billConAdjDtlService.findByBiz(vo,queryParam);
							if(query.size()>=1) {
								if(StringUtils.isNotEmpty(conNo)){//如果箱号不为空
									//删除单明细整箱或整板记录
									Map<String, Object> tempQueryParam = new HashMap<String, Object>();
									tempQueryParam.put("locno", locnoDtl);
									tempQueryParam.put("ownerNo", ownerNoDtl);
									tempQueryParam.put("adjNo", adjNoDtl);
									tempQueryParam.put("labelNo", conNo);
									billConAdjDtlService.deleteByConNo(tempQueryParam);
								}
								Short rowId = query.get(0).getRowId();
								BillConAdjDtl delParamerKey = new BillConAdjDtl();
								delParamerKey.setLocno(locno);
								delParamerKey.setOwnerNo(ownerNo);
								delParamerKey.setAdjNo(adjNo);
								delParamerKey.setRowId(rowId);
								delParamerKey.setCellNo(cellNoDtl);
								delParamerKey.setItemNo(itemNoDtl);
								delParamerKey.setSizeNo(sizeNoDtl);
								if(StringUtils.isNotEmpty(conNo)){
									delParamerKey.setLabelNo(conNo);
								}else{
									delParamerKey.setLabelNo("N");
								}
								billConAdjDtlService.deleteById(delParamerKey);
								
								if(StringUtils.isNotEmpty(conNo)){//如果箱号不为空
									//解锁箱号
									BmContainer bm=new BmContainer();
									bm.setConNo(conNo);
									bm.setLocno(locnoDtl);
									bm.setEditor(editor);
									bm.setEdittm(currentDate);
									bm.setStatus(ContentStatusEnums.STATUS_0.getStatus());
									bm.setFalg("true");
									bmList.add(bm);
									String panNo=query.get(0).getPanNo();//整板操作
									if(StringUtils.isNotEmpty(panNo)){
										BmContainer bm1=new BmContainer();
										bm1.setConNo(panNo);
										bm1.setLocno(locnoDtl);
										bm1.setEditor(editor);
										bm1.setEdittm(currentDate);
										bm1.setStatus(ContentStatusEnums.STATUS_0.getStatus());
										bm1.setFalg("true");
										bmList.add(bm1);
										//根据板查询箱记录
										Map<String,Object> panNoMap=new HashMap<String,Object>();
										panNoMap.put("locno", locno);
										panNoMap.put("panNo", panNo);
										List<ConBox> boxList=conBoxService.findByBiz(null,panNoMap);
										if(null!=boxList && boxList.size()>0){
											for(ConBox conbox:boxList){
											BmContainer bm2=new BmContainer();
											bm2.setConNo(conbox.getBoxNo());
											bm2.setLocno(locnoDtl);
											bm2.setEditor(editor);
											bm2.setEdittm(currentDate);
											bm2.setStatus(ContentStatusEnums.STATUS_0.getStatus());
											bm2.setFalg("true");
											bmList.add(bm2);
											}
										}
									}
								}
								
								
							} else {
//								throw new ManagerException("找不到可删除的库存调整信息！");
							}
						}
					}
					if(bmList.size()>0){
						bmContainerService.batchUpdate(bmList);
					}
					flag = true;
				}
				//新增操作
				if (CommonUtil.hasValue(addList)) {
					flag = false;
					//查询最大的Pid,作为主键 
					BillConAdjDtl keyObj = new BillConAdjDtl();
					keyObj.setAdjNo(adjNo);
					keyObj.setLocno(locno);
					keyObj.setOwnerNo(ownerNo);
					short pidNum = (short) billConAdjDtlService.selectMaxRowId(keyObj);

					for (ModelType modelType : addList) {
						if (modelType instanceof BillConAdjDtl) {
							BillConAdjDtl vo = (BillConAdjDtl) modelType;
							String adjNoDtl = adjNo;
							String locnoDtl = locno;
							String ownerNoDtl = ownerNo;
							String cellNoDtl = vo.getCellNo();
							String itemNoDtl = vo.getItemNo();
							String sizeNoDtl = vo.getSizeNo();
							String qualityDtl = vo.getQuality();
							String itemTypeDtl = vo.getItemType();
							BigDecimal adjQty = vo.getAdjQty();
							if(adjQty.doubleValue() <= 0) {
								throw new ManagerException("数量不允许为0！");
							}
							
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("locNo", locnoDtl);
							queryParam.put("ownerNo", ownerNoDtl);
							queryParam.put("cellNo", cellNoDtl);
							queryParam.put("itemNo", itemNoDtl);
							queryParam.put("sizeNo", sizeNoDtl);
							queryParam.put("quality", qualityDtl);
							queryParam.put("itemType", itemTypeDtl);
							queryParam.put("adjNo", adjNoDtl);
							//su.yq注释
//							List<BillConAdjDtl> query = billConAdjDtlService.findByBiz(vo,queryParam);
//							if(query.size() > 0) {
//								for(BillConAdjDtl sm : query) {
//									String no = sm.getAdjNo();
//									
//									Map<String, Object> bi = new HashMap<String, Object>();
//									bi.put("locNo", locno);
//									bi.put("ownerNo", ownerNo);
//									bi.put("adjNo", no);
//									bi.put("status", "10");
//									List<BillConAdj> pa = billConAdjService.findByBiz(billConAdj,bi);
//									if(pa.size() > 0) {
//										throw new ManagerException("表单：" + no + "中存在商品尺码重复，请核对！"+sm.getItemNo()+"-"+sm.getSizeNo());
//									}
//								}
//							}
//							query = billConAdjDtlService.findByBiz(vo,queryParam);
							
							
//							if(query.size() > 0) {
//								throw new ManagerException("库存调整数据重复，请核对！");
//							} else {
								queryParam.put("hmManualFlag", "1");
								//获取库存的明细信息
								List<BillConAdjDtl> conContent = billConAdjDtlService.selectContentParams(vo,queryParam);
								if(conContent.size()<=0) {
									throw new ManagerException("商品："+vo.getItemNo()+",尺码："+vo.getSizeNo()+"无可调整库存数据,或商品数据重复!");
								}
								//循环插入箱号明细
								for (BillConAdjDtl dtl : conContent) {
									BigDecimal conQty = dtl.getConQty();
									if(conQty.compareTo(adjQty)>=0) {
										vo.setLocno(locnoDtl);
										vo.setOwnerNo(ownerNoDtl);
										vo.setAdjNo(adjNoDtl);
										vo.setRowId(++pidNum);
										vo.setEditor(billConAdj.getEditor());
										vo.setEditorName(billConAdj.getEditorName());
										vo.setEdittm(currentDate);
										int a = 0;
										try {
											a = billConAdjDtlService.add(vo);
										} catch (Exception e) {
											throw new ManagerException("添加库存调整明细失败！",e);
										}
										if (a < 1) {
											throw new ManagerException("添加库存调整信息失败！,e");
										}
										//锁定库存 手工移库标识,0:不允许手工移库
										procCommonService.UpdateContentStatus(locno, dtl.getConId(), null,null, null,"0", editor);
										
									} else {
										throw new ManagerException("调整数量不允许大于库存数据！");
									}
								}	
							//}
						}
					}
					
					//su.yq验证重复商品
					Map<String, Object> queryParam = new HashMap<String, Object>();
					queryParam.put("locno", locno);
					queryParam.put("ownerNo", ownerNo);
					queryParam.put("adjNo", adjNo);
					List<BillConAdjDtl> checkList = billConAdjDtlService.findCheckRepeatData(queryParam);
					if(CommonUtil.hasValue(checkList)){
						BillConAdjDtl adjDtl = checkList.get(0);
						throw new ManagerException("表单：" + adjNo + "中商品："+adjDtl.getItemNo()+",尺码："+adjDtl.getSizeNo()+"存在商品尺码重复,请核对！");
					}
					flag = true;
				}
				//更新操作
				if (CommonUtil.hasValue(uptList)) {
					flag = false;
					for(ModelType modelType : uptList){
						if(modelType instanceof  BillConAdjDtl){
							BillConAdjDtl vo = (BillConAdjDtl) modelType;
							String adjNoDtl = adjNo;
							String locnoDtl = locno;
							String ownerNoDtl = ownerNo;
							String cellNoDtl = vo.getCellNo();
							String itemNoDtl = vo.getItemNo();
							String sizeNoDtl = vo.getSizeNo();
							String qualityDtl = vo.getQuality();
							String itemTypeDtl = vo.getItemType();
							BigDecimal adjQty = vo.getAdjQty();
							if(adjQty.doubleValue() <= 0) {
								throw new ManagerException("数量不允许为0！");
							}
							
							//获取库存的明细信息
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("locNo", locnoDtl);
							queryParam.put("ownerNo", ownerNoDtl);
							queryParam.put("cellNo", cellNoDtl);
							queryParam.put("itemNo", itemNoDtl);
							queryParam.put("sizeNo", sizeNoDtl);
							queryParam.put("quality", qualityDtl);
							queryParam.put("itemType", itemTypeDtl);
							queryParam.put("adjNo", adjNoDtl);
							queryParam.put("hmManualFlag", "0");
							String conNo=vo.getLabelNo();
							if(StringUtils.isNotEmpty(conNo)){
								queryParam.put("labelNo",vo.getLabelNo());
							}else{
								queryParam.put("labelNo","N");
							}
							
							
							List<BillConAdjDtl> query = billConAdjDtlService.findByBiz(vo,queryParam);
							if(query.size()==1) {
								//获取库存的明细信息
								List<BillConAdjDtl> conContent = billConAdjDtlService.selectContentParams(vo,queryParam);
								if(conContent.size()<=0) {
									throw new ManagerException("无可调整库存数据！");
								}
								//循环插入箱号明细
								for (BillConAdjDtl dtl : conContent) {
									BigDecimal conQty = dtl.getConQty();
									if(conQty.compareTo(adjQty)>=0) {
										Short rowId = query.get(0).getRowId();
										BillConAdjDtl delParamerKey = new BillConAdjDtl();
										delParamerKey.setLocno(locno);
										delParamerKey.setOwnerNo(ownerNo);
										delParamerKey.setAdjNo(adjNo);
										delParamerKey.setRowId(rowId);
										delParamerKey.setEditor(billConAdj.getEditor());
										delParamerKey.setEditorName(billConAdj.getEditorName());
										delParamerKey.setEdittm(currentDate);
										delParamerKey.setAdjQty(adjQty);
										int a = billConAdjDtlService.modifyById(delParamerKey);
										if(a < 1){
											throw new ManagerException("更新库存调整信息失败！");
										}
									} else {
										throw new ManagerException("调整数量不允许大于库存数据！");
									}
								}
							} else {
								throw new ManagerException("找不到可更新的库存调整信息！");
							}
						}
					}
					flag = true;
				}
			} else {
				throw new ManagerException("当前单据状态不可编辑！");
			}
			if(flag) {
				mapObj.put("flag", "true");
				mapObj.put("msg", "保存成功");
			} else {
				throw new ManagerException("当前单据保存失败！");
			}
			return mapObj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e.getMessage());
		}
		
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> addDtlByCell(List<String> list,Map<String,String> map) throws ManagerException {	
		try {
			String adjNo=map.get("adjNo");
			String locno=map.get("locNo");
			String ownerNo=map.get("ownerNo");
			String editor=map.get("editor");
			
			//查看主档是否可以修改
			BillConAdjKey billConAdjKey=new BillConAdjKey();
			billConAdjKey.setAdjNo(adjNo);
			billConAdjKey.setLocno(locno);
			billConAdjKey.setOwnerNo(ownerNo);
			BillConAdj billConAdj=(BillConAdj) billConAdjService.findById(billConAdjKey);
			if(billConAdj==null || !billConAdj.getStatus().equals("10")){
				throw new ManagerException("库存调整单不存在或状态已改变！");
			}
			
			
			//查询明细的最大rowId
			BillConAdjDtl dtlkeyObj = new BillConAdjDtl();
			dtlkeyObj.setAdjNo(adjNo);
			dtlkeyObj.setLocno(locno);
			dtlkeyObj.setOwnerNo(ownerNo);
			short pidNum = (short) billConAdjDtlService.selectMaxRowId(dtlkeyObj);
			
			//库存查询条件
			Map<String,Object> contentParams=new HashMap<String,Object>();
			contentParams.put("locno", locno);
			contentParams.put("ownerNo", ownerNo);
			
			//调整类型 0---品质转换；1--属性转换
			String adjType=billConAdj.getAdjType();
			//调整前类型  ADJ_TYPE 0-写品质；1-写属性
			String sItemType=billConAdj.getsItemType();

			if(adjType.equals("0")){
				contentParams.put("quality", sItemType);
			}else if(adjType.equals("1")){
				contentParams.put("itemType", sItemType);
			}
			
			//库存调整明细查询条件
			Map<String,Object> dtlParams=new HashMap<String,Object>();
			dtlParams.put("locno", locno);
			dtlParams.put("ownerNo", ownerNo);
			dtlParams.put("adjNo", adjNo);			
			
			for(String cellNo : list){
				contentParams.put("cellNo", cellNo);
				List<ConContentDto> conContentDtoList=conContentService.findViewByParams(contentParams);//零散商品
				List<ConContentDto> boxConContentDtoList=conContentService.selectConBoxViewByParams(contentParams);//箱库存商品
				if((!(null!=conContentDtoList&&conContentDtoList.size()!=0))){
					if((!(null!=boxConContentDtoList&&boxConContentDtoList.size()!=0))){
						throw new ManagerException("储位["+cellNo+"]未找到对应的商品，添加失败。");
					}
				}
				
				
				//冻结储位
//				CmDefcell cdParams=new CmDefcell();
//				cdParams.setLocno(locno);
//				cdParams.setCellNo(cellNo);
//				cdParams.setCellStatus("2");//冻结
//				cmDefcellService.modifyById(cdParams);
				
				//锁库存	
				procCommonService.UpdateContentStatus(locno, null, cellNo, null, null,"0", editor);

				dtlParams.put("cellNo", cellNo);
				int dtlHadCount=billConAdjDtlService.findCount(dtlParams);
				if(dtlHadCount>0){
					throw new ManagerException("储位["+cellNo+"]重复，添加失败。");
				}
								
				for(ConContentDto content:conContentDtoList){
					BillConAdjDtl addDtl =new BillConAdjDtl();
					addDtl.setAdjNo(adjNo);
					addDtl.setRowId(++pidNum);
					addDtl.setLocno(locno);
					addDtl.setOwnerNo(ownerNo);
					addDtl.setItemNo(content.getItemNo());
					addDtl.setSizeNo(content.getSizeNo());
					addDtl.setPackQty(content.getPackQty());
					addDtl.setQuality(content.getQuality());
					addDtl.setCellNo(content.getCellNo());
					addDtl.setAdjQty(content.getQty());
					addDtl.setItemType(content.getItemType());
					addDtl.setBarcode(content.getBarcode());
					addDtl.setBrandNo(content.getBrandNo());
					
					int addCount = 0;
					addCount = billConAdjDtlService.add(addDtl);					
					if (addCount < 1) {
						throw new ManagerException("添加库存调整信息失败！");
					}
				}
				Date currenDate=new Date();
				List<BmContainer> bmList=new ArrayList<BmContainer>();
				for(ConContentDto content:boxConContentDtoList){
					BillConAdjDtl addDtl =new BillConAdjDtl();
					addDtl.setAdjNo(adjNo);
					addDtl.setRowId(++pidNum);
					addDtl.setLocno(locno);
					addDtl.setOwnerNo(ownerNo);
					addDtl.setItemNo(content.getItemNo());
					addDtl.setSizeNo(content.getSizeNo());
					addDtl.setPackQty(content.getPackQty());
					addDtl.setQuality(content.getQuality());
					addDtl.setCellNo(content.getCellNo());
					addDtl.setAdjQty(content.getQty());
					addDtl.setItemType(content.getItemType());
					addDtl.setBarcode(content.getBarcode());
					addDtl.setBrandNo(content.getBrandNo());
					addDtl.setLabelNo(content.getLabelNo());
					BmContainer bm=new BmContainer();
					bm.setConNo(content.getLabelNo());
					bm.setStatus("1");
					bm.setOptBillNo(adjNo);
					bm.setOptBillType(ContainerTypeEnums.CA.getOptBillType());
					bm.setEditor(editor);
					bm.setEdittm(currenDate);
					bm.setLocno(locno);
					bmList.add(bm);
					int addCount = 0;
					addCount = billConAdjDtlService.add(addDtl);					
					if (addCount < 1) {
						throw new ManagerException("添加库存调整信息失败！");
					}
				}
				if(bmList.size()>0){
					bmContainerService.batchUpdate(bmList);
				}
			}			
		} catch (ServiceException e) {
			log.error(e.getMessage(),e);
			throw new ManagerException(e.getMessage());
		}				
		return null;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> addDtlByConNo(List<String> list,Map<String,String> map) throws ManagerException {	
		try {
			String adjNo=map.get("adjNo");
			String locno=map.get("locNo");
			String ownerNo=map.get("ownerNo");
			String editor=map.get("editor");
			String editorName=map.get("editorName");
			Date date = new Date();
			
			//查看主档是否可以修改
			BillConAdjKey billConAdjKey=new BillConAdjKey();
			billConAdjKey.setAdjNo(adjNo);
			billConAdjKey.setLocno(locno);
			billConAdjKey.setOwnerNo(ownerNo);
			BillConAdj billConAdj=(BillConAdj) billConAdjService.findById(billConAdjKey);
			if(billConAdj==null || !billConAdj.getStatus().equals("10")){
				throw new ManagerException("库存调整单不存在或状态已改变！");
			}
			//查询明细的最大rowId
			BillConAdjDtl dtlkeyObj = new BillConAdjDtl();
			dtlkeyObj.setAdjNo(adjNo);
			dtlkeyObj.setLocno(locno);
			dtlkeyObj.setOwnerNo(ownerNo);
			short pidNum = (short) billConAdjDtlService.selectMaxRowId(dtlkeyObj);
			
			//库存查询条件
			Map<String,Object> contentParams=new HashMap<String,Object>();
			contentParams.put("locno", locno);
			contentParams.put("ownerNo", ownerNo);
			
			//调整类型 0---品质转换；1--属性转换
			String adjType=billConAdj.getAdjType();
			//调整前类型  ADJ_TYPE 0-写品质；1-写属性
			String sItemType=billConAdj.getsItemType();

			if(adjType.equals("0")){
				contentParams.put("quality", sItemType);
			}else if(adjType.equals("1")){
				contentParams.put("itemType", sItemType);
			}
		    List<String> panList=new ArrayList<String>();//所有父容器
		    Map<String,String> subConMap=new HashMap<String,String>();//所有子容器
			for(String conNo : list){
				String []conNos=conNo.split(":");
				String conType=conNos[1];
				if(!conType.equals("C")){//查询板明细
					panList.add(conNos[0]);
				}
			}
			if(panList.size()>0){
				for(String panNo:panList){//查询子容器
					Map<String,Object> panParams=new HashMap<String,Object>();
					panParams.put("panNo", panNo);
					panParams.put("locno", locno);
					List<ConBox> conBoxList=conBoxService.findByBiz(null, panParams);
					for(ConBox boxNo:conBoxList){
						subConMap.put(boxNo.getBoxNo(),boxNo.getBoxNo());
					}
				}
			}
			
			//判断容器状态和容器库存
			Date currentDate=new Date();
			List<BmContainer> bmList=new ArrayList<BmContainer>();
			for(String conNo : list){
				String []conNos=conNo.split(":");
				conNo=conNos[0];
				String conType=conNos[1];
				if(conType.equals("C")){
					String tempConNo=subConMap.get(conNo);
					if(null!=tempConNo&&tempConNo.equals(conNo)){//选择的容器有包含关系
						log.info("托盘子容器包含选择的箱号");
						continue;
					}
				}
				contentParams.put("conNo", conNo);//
				BmContainer tmpBm=(BmContainer) bmContainerService.findById(contentParams);
				if(!(null!=tmpBm && tmpBm.getStatus().equals("0"))){
					throw new ManagerException("容器["+conNo+"]不存在或被占用，添加失败。");
				}
				AccInventoryCon tempAcc=(AccInventoryCon) accInventoryConService.findById(contentParams);
				if(!(null!=tempAcc && tempAcc.getStatus().equals("2"))){
					throw new ManagerException("容器["+conNo+"]没有库存或库存状态是非入库不允许调整库存，添加失败。");
				}
				//查询容器明细
				List<AccContainerSku> conSkuList=null;
				SimplePage page = new SimplePage(1, 10000,10000);
				if(tmpBm.getType().equals("C")){
					conSkuList =accContainerSkuService.findByPage(page,null,null, contentParams);
	    		}else{
	    			contentParams.put("conAdj", "true");//库存调整标示
	    			conSkuList =accContainerSkuService.findPlateBypage(page,null,null,contentParams);
	    		}
				if(!(null!=conSkuList && conSkuList.size()>0)){
					throw new ManagerException("容器["+conNo+"]没有查到明细，添加失败。");
				}
				for(AccContainerSku content:conSkuList){
					//查询是否有重复
					Map<String,Object> checkDoubleMap=new HashMap<String,Object>();
					checkDoubleMap.put("locno", locno);
					checkDoubleMap.put("adjNo", adjNo);
					checkDoubleMap.put("quality", content.getQuality());
					checkDoubleMap.put("itemType", content.getItemType());
					checkDoubleMap.put("sizeNo", content.getSizeNo());
					checkDoubleMap.put("itemNo", content.getItemNo());
					checkDoubleMap.put("cellNo", tempAcc.getCellNo());
					checkDoubleMap.put("labelNo",content.getConNo());
					List<BillConAdjDtl> billconadjList=billConAdjDtlService.findByBiz(null, checkDoubleMap);
					if(null!=billconadjList && billconadjList.size()>0){
						throw new ManagerException("表单：" + adjNo + "中商品："+content.getItemNo()+",尺码："+content.getSizeNo()+"存在商品尺码重复,请核对！");
					}
					BillConAdjDtl addDtl =new BillConAdjDtl();
					addDtl.setAdjNo(adjNo);
					addDtl.setRowId(++pidNum);
					addDtl.setLocno(locno);
					addDtl.setOwnerNo(ownerNo);
					addDtl.setItemNo(content.getItemNo());
					addDtl.setSizeNo(content.getSizeNo());
					//addDtl.setPackQty(content.getPackQty());
					addDtl.setQuality(content.getQuality());
					addDtl.setCellNo(tempAcc.getCellNo());
					addDtl.setAdjQty(content.getQty());
					addDtl.setItemType(content.getItemType());
					addDtl.setBarcode(content.getBarcode());
					addDtl.setBrandNo(content.getBrandNo());
					addDtl.setEditor(editor);
					addDtl.setEditorName(editorName);
					addDtl.setEdittm(date);
					if(!tmpBm.getType().equals("C")){//整板操作
						contentParams.put("conNo", content.getConNo());//判断子容器状态
						BmContainer tmpBm1=(BmContainer) bmContainerService.findById(contentParams);
						if(!(null!=tmpBm1 && tmpBm1.getStatus().equals("0"))){
							throw new ManagerException("子容器["+content.getConNo()+"]不存在或被占用，添加失败。");
						}
						AccInventoryCon tempAcc1=(AccInventoryCon) accInventoryConService.findById(contentParams);
						if(!(null!=tempAcc1 && tempAcc1.getStatus().equals("2"))){
							throw new ManagerException("子容器["+content.getConNo()+"]没有库存或库存状态是非入库不允许调整库存，添加失败。");
						}
						addDtl.setPanNo(conNo);
						BmContainer bm=new BmContainer();
						bm.setConNo(content.getConNo());
						bm.setStatus("1");
						bm.setOptBillNo(adjNo);
						bm.setOptBillType(ContainerTypeEnums.CA.getOptBillType());
						bm.setEditor(editor);
						bm.setEdittm(currentDate);
						bm.setLocno(locno);
						bmList.add(bm);
					}
					addDtl.setLabelNo(content.getConNo());
					int addCount = 0;
					addCount = billConAdjDtlService.add(addDtl);					
					if (addCount < 1) {
						throw new ManagerException("添加库存调整信息失败！");
					}
					//锁定库存
					Map<String,Object> conContentMap=new HashMap<String,Object>();
					conContentMap.put("barcode", content.getBarcode());
					conContentMap.put("cellNo", tempAcc.getCellNo());
					conContentMap.put("ownerNo",ownerNo);
					conContentMap.put("locno",locno);
					List<ConContent> tempList=conContentService.findByBiz(null, conContentMap);
					if (null!=tempList && tempList.size()>0) {
						procCommonService.UpdateContentStatus(locno, tempList.get(0).getCellId().toString(), null,null, null,"0", editor);
					}else{
						throw new ManagerException("储位"+tempAcc.getCellNo()+",商品编码"+content.getItemNo()+",条码"+content.getBarcode()+"找不到商品储位库存！");
					}
				}
				BmContainer bm=new BmContainer();
				bm.setConNo(conNo);
				bm.setStatus("1");
				bm.setOptBillNo(adjNo);
				bm.setOptBillType(ContainerTypeEnums.CA.getOptBillType());
				bm.setEditor(editor);
				bm.setEdittm(currentDate);
				bm.setLocno(locno);
				bmList.add(bm);
			}
			bmContainerService.batchUpdate(bmList);//锁定容器
		} catch (ServiceException e) {
			log.error(e.getMessage(),e);
			throw new ManagerException(e.getMessage());
		}				
		return null;
	}
	@Override
	public SumUtilMap<String, Object> findSumQty(Map<String, Object> map,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billConAdjDtlService.selectSumQty(map,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public String importDtl(List<BillConAdjDtl> list, String adjNo,
			String ownerNo, SystemUser user) throws ManagerException {
		StringBuilder sb = new StringBuilder();
		try {
			//所有储位集合,用于更新库存标示
			Map<String, String> allCell = new HashMap<String, String>();
			int pageSize = 500;//批量insert的数据量
			String locno = user.getLocNo();
			Map<String, String> map = new HashMap<String, String>();
			Map<String, BillConAdjDtl> exist = getExist(adjNo);
			Short rowId = 1;
			if(exist.size() != 0){
				BillConAdjDtl d = new BillConAdjDtl();
				d.setLocno(locno);
				d.setAdjNo(adjNo);
				d.setOwnerNo(ownerNo);
				rowId = (short) billConAdjDtlService.selectMaxRowId(d);
			}
			String key = "";
			Map<String, Object> params = new HashMap<String, Object>();
			List<ConContentDto> ccList = null;
			BigDecimal total = null;
			params.put("locno", locno);
			//明细中只能保存调整前的属性或品质S -JYS
			BillConAdj adj = new BillConAdj();
			adj.setLocno(user.getLocNo());
			adj.setAdjNo(adjNo);
			adj.setOwnerNo(ownerNo);
			adj = billConAdjService.findById(adj);
			if(adj == null){
				throw new ManagerException("主档信息已经不存在,不能导入!");
			}else if(!"10".equals(adj.getStatus())){
				throw new ManagerException("单据状态已经改变,不能导入!");
			}
			String adjType = adj.getAdjType();
			String sItemType = adj.getsItemType();
			//明细中只能保存调整前的属性或品质E
			String itemType = "";
			String quality = "";
			int idx = 1;
			for(BillConAdjDtl dtl:list){
				idx++;
				//dtl.setItemNo(dtl.getSysNo()+dtl.getItemNo());//导入的数据中商品编码、尺码都包含了品牌库
				//dtl.setSizeNo(dtl.getSysNo()+dtl.getSizeNo());
				//********************判断商品属性和品质是否合法S***********************
				quality = QualityEnums.getValueByDesc(dtl.getQuality());
				if(StringUtils.isBlank(quality)){
					sb.append("第").append(idx).append("行【品质】非法\\r\\n");
					continue;
				}else{
					dtl.setQuality(quality);
				}
				itemType = ItemTypeEnums.getValueByDesc(dtl.getItemType());
				if(StringUtils.isBlank(itemType)){
					sb.append("第").append(idx).append("行【属性】非法\\r\\n");
					continue;
				}else{
					dtl.setItemType(itemType);
				}
				
				if("0".equals(adjType)){//0:品质
					if(!quality.equals(sItemType)){
						sb.append("第").append(idx).append("行【品质】与主档【调整前】不合\\r\\n");
						continue;
					}
				}else{//1:属性
					if(!itemType.equals(sItemType)){
						sb.append("第").append(idx).append("行【属性】与主档【调整前】不合\\r\\n");
						continue;
					}
				}
				//********************判断商品属性和品质是否合法E***********************
				key = dtl.getItemNo()+"_"+dtl.getSizeNo()+"_"+dtl.getItemType()+"_"+dtl.getQuality()+"_"+dtl.getCellNo();
				//********************Excel重复S***********************
				if(map.get(key) == null){
					map.put(key, "【第"+idx+"行】");
				}else{
					sb.append(map.get(key)).append("与【第"+idx+"行】数据重复\\r\\n");
					continue;
				}
				//********************Excel重复E***********************
				//********************Excel与明细表是否存在重复S***********************
				if(exist.size() > 0 && exist.get(key) != null){
					sb.append("第").append(idx).append("行数据在明细表中已经存在\\r\\n");
					continue;
				}
				//********************Excel与明细表是否存在重复E***********************
				
				//********************库存是否存在、是否有足够的库存S***********************
				params.put("itemNo", dtl.getItemNo());
				params.put("sizeNo", dtl.getSizeNo());
				params.put("cellNo", dtl.getCellNo());
				params.put("itemType", itemType);
				params.put("quality", quality);
				params.put("status", "0");
				params.put("hmManualFlag", "1");
				params.put("flag", "0");
				ccList = conContentService.findViewByParams(params);
				if(!CommonUtil.hasValue(ccList)){
					sb.append("第").append(idx).append("行数据不存在或库存不可用\\r\\n");
					continue;
				}else{
					total = new BigDecimal(0);
					for(ConContent cc : ccList){
						total = total.add(cc.getQty().subtract(cc.getOutstockQty()));
					}
					if(total.intValue() < dtl.getAdjQty().intValue()){
						sb.append("第").append(idx).append("行【调整数量】不能大于").append(total.intValue()).append("\\r\\n");
						continue;
					}
					dtl.setBrandNo(ccList.get(0).getBrandNo());
					String cellIdStr=ccList.get(0).getCellId().toString();
					allCell.put(cellIdStr,cellIdStr);
				}
				//********************库存是否存在、是否有足够的库存E***********************
				dtl.setLocno(locno);
				dtl.setAdjNo(adjNo);
				dtl.setOwnerNo(ownerNo);
				dtl.setRowId(++rowId);
			}
			if(sb.length() > 0){
				throw new ManagerException("<span style='color:red;'>非法数据异常:以下行数为Excel真实行数</span><br><br><textarea rows='5' cols='40'>"+sb.toString()+"</textarea>");
			}
			//********************批量保存S***********************
			for(int i=0;i<list.size();){
				i += pageSize;
				if(i>list.size()){
					billConAdjDtlService.batchInsertDtl(list.subList(i-pageSize, list.size()));
				}else{
					billConAdjDtlService.batchInsertDtl(list.subList(i-pageSize, i));
				}
				
			}
			//********************批量保存E***********************
			//********************(锁库存)修改库存标示hmManualFlag为"0"S***********************
			for(Entry<String, String> c:allCell.entrySet()){
//				procCommonService.UpdateContentStatus(locno, null, c.getKey(),null, null,"0", user.getLoginName());
				//锁定库存 手工移库标识,0:不允许手工移库
				procCommonService.UpdateContentStatus(locno, c.getKey(), null,null, null,"0",  user.getLoginName());
			}
			//********************(锁库存)修改库存标示hmManualFlag为"0"E***********************
			
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ManagerException(e.getMessage(), e);
		}
		return null;
	}

	
public Map<String, BillConAdjDtl> getExist(String adjNo){
		
		Map<String, BillConAdjDtl> map = new HashMap<String, BillConAdjDtl>();
		String key = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("adjNo", adjNo);
		try {
			List<BillConAdjDtl> list = billConAdjDtlService.findByBiz(null, params);
			for(BillConAdjDtl dtl:list){
				key = dtl.getItemNo()+"_"+dtl.getSizeNo()+"_"+dtl.getItemType()+"_"+dtl.getQuality()+"_"+dtl.getCellNo();
				map.put(key, dtl);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return map;
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
    public void batchInsertDtl4ConvertGoods(List<BillConAdjDtl> list)
            throws ManagerException {
        try {
            billConAdjDtlService.batchInsertDtl4ConvertGoods(list);
        } catch (Exception e) {
            throw new ManagerException(e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> findAllDtl(String keys,
            AuthorityParams authorityParams) throws ManagerException {
       // List<BillConAdjDtlSizeDto> list = null;
    	List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    	
        try {
            //list = billConAdjDtlService.findAllDtl(params, authorityParams);
        	//查询码表信息
            Map<String, Object> adjTypeParams = new HashMap<String, Object>();
            String queryCondition = "and LOOKUPCODE in('ADJ_TYPE','AREA_QUALITY','ITEM_TYPE')";
            adjTypeParams.put("queryCondition", queryCondition);
            List<Lookupdtl> adjlookupdtl =  this.lookupdtlManager.findByBiz(null, adjTypeParams);
            String[] keysArray = keys.split(",");
            for (String str : keysArray) {
                String[] strs = str.split("\\|");
                String locno = strs[0];
                String adjNo = strs[1];
                String ownerNo = strs[2];
                
                BillConAdj billConAdj = new BillConAdj();
                billConAdj.setLocno(locno);
                billConAdj.setAdjNo(adjNo);
                billConAdj.setOwnerNo(ownerNo);
                billConAdj = billConAdjService.findById(billConAdj);
                //调整类型和调整结果匹配码表名称
                String code = "0".equals(billConAdj.getAdjType())?"AREA_QUALITY":"ITEM_TYPE";
                String sItemName = "";
                String dItemName = "";
                String adjType = "";
                for (Lookupdtl lookupdtl : adjlookupdtl) {
                    String lookupcode = lookupdtl.getLookupcode();
                    String itmeval = lookupdtl.getItemval();
                    // 调整属性类型
                    if ("ADJ_TYPE".equals(lookupcode) &&  itmeval.equals(billConAdj.getAdjType())) {
                        adjType = lookupdtl.getItemname();
                    }
                    if(code.equals(lookupcode) && itmeval.equals(billConAdj.getsItemType())){
                        sItemName = lookupdtl.getItemname();
                    }
                    if(code.equals(lookupcode) && itmeval.equals(billConAdj.getdItemType())){
                        dItemName = lookupdtl.getItemname();
                    }
                }
                //查询明细信息
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("locno", locno);
                params.put("adjNo", adjNo);
                params.put("ownerNo", ownerNo);
                List<BillConAdjDtlSizeDto> list = billConAdjDtlService.findAllDtl(params, authorityParams);
                if(!CommonUtil.hasValue(list)){
                	throw new ManagerException("没有明细！");
                }
                //将明细结果按品牌区分
                Map<String,List<BillConAdjDtlSizeDto>> tempBrandMap = new HashMap<String,List<BillConAdjDtlSizeDto>>();
                List<BillConAdjDtlSizeDto> tempList = null;
                for(BillConAdjDtlSizeDto dtl:list){
                    String brandNo = dtl.getBrandNo();
                    if((tempList = tempBrandMap.get(brandNo)) != null){
                        tempList.add(dtl);
                    }else{
                        tempList = new ArrayList<BillConAdjDtlSizeDto>();
                        tempList.add(dtl);
                        tempBrandMap.put(brandNo, tempList);
                    }
                }
                for(Entry<String, List<BillConAdjDtlSizeDto>> l : tempBrandMap.entrySet()){
              
                    if(CommonUtil.hasValue(l.getValue())){
                       //
                        Map<String, BillConAdjDtlSizeDto> itemRowDto = new HashMap<String, BillConAdjDtlSizeDto>();
                        //各商品各尺码对应的商品数量
                        Map<String, BigDecimal> sizeCodeQtyMap = null;
                        BillConAdjDtlSizeDto temp;
                        //表头显示的总商品合计
                        BigDecimal total = new BigDecimal(0);
                        //尺码表头
                        Map<String, Map<String, String>> sizeHead = new TreeMap<String, Map<String,String>>();
                        //同一类型的尺码
                        Map<String, String> sizeRow = null;
                        
                        for(BillConAdjDtlSizeDto d:l.getValue()){
                            String itemNo = d.getItemNo();
                            String itemName = d.getItemName();
                            if((temp = itemRowDto.get(itemNo+itemName)) != null){
                                sizeCodeQtyMap = temp.getSizeCodeQtyMap();
                                temp.setTotalQty(temp.getTotalQty().add(d.getAdjQty()));
                                if(sizeCodeQtyMap.get(d.getSizeCode()) != null){
                                    sizeCodeQtyMap.put(d.getSizeCode(), sizeCodeQtyMap.get(d.getSizeCode()).add(d.getAdjQty()));
                                }else{
                                    sizeCodeQtyMap.put(d.getSizeCode(), d.getAdjQty());
                                }
                            }else{
                                sizeCodeQtyMap = new TreeMap<String, BigDecimal>();
                                sizeCodeQtyMap.put(d.getSizeCode(), d.getAdjQty());
                                d.setSizeCodeQtyMap(sizeCodeQtyMap);
                                d.setTotalQty(d.getAdjQty());
                                itemRowDto.put(itemNo+itemName, d);
                            }
                            if((sizeRow = sizeHead.get(d.getSizeKind())) != null){
                                sizeRow.put(d.getSizeCode(), d.getSizeCode());
                            }else{
                                sizeRow = new TreeMap<String, String>();
                                sizeRow.put(d.getSizeCode(), d.getSizeCode());
                                sizeHead.put(d.getSizeKind(), sizeRow);
                            }
                            total = total.add(d.getAdjQty());
                        }
                        
                        List<BillConAdjDtlSizeDto> rows = new ArrayList<BillConAdjDtlSizeDto>();
                        List<List<String>> sizeList = new ArrayList<List<String>>();
                        for(Entry<String, BillConAdjDtlSizeDto> m : itemRowDto.entrySet()){
                            rows.add(m.getValue());
                        }
                        int sizeColNum = 0;
                        List<String> sizeSingleRow = null;
                        for(Entry<String, Map<String, String>> m : sizeHead.entrySet()){
                            sizeSingleRow = new ArrayList<String>();
                            sizeSingleRow.add(m.getKey());
                            for(Entry<String, String> s:m.getValue().entrySet()){
                                sizeSingleRow.add(s.getValue());
                            }
                            if(sizeColNum < sizeSingleRow.size()){
                                sizeColNum = sizeSingleRow.size();
                            }
                            sizeList.add(sizeSingleRow);
                        }
                        Map<String, Object> main = new HashMap<String, Object>();
                        main.put("adjNo", adjNo);
                        main.put("remark", billConAdj.getRemark()==null?"":billConAdj.getRemark());
                        main.put("adjType", adjType);
                        main.put("adjResult",sItemName+"->"+dItemName);
                        main.put("total", total);
                        main.put("list", rows);
                        main.put("sizeList", sizeList);
                        main.put("sizeColNum", sizeColNum);
                        resultList.add(main);
                    }
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}