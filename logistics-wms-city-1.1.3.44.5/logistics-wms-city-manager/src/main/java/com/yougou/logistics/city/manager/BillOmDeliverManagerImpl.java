package com.yougou.logistics.city.manager;

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

import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.enums.ContainerStatusEnums;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BmContainer;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConLabel;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.service.BillOmDeliverDtlService;
import com.yougou.logistics.city.service.BillOmDeliverService;
import com.yougou.logistics.city.service.BmContainerService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ConLabelDtlService;
import com.yougou.logistics.city.service.ConLabelService;

/**
 * 
 * 装车单manager实现
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:25:51
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Service("billOmDeliverManager")
class BillOmDeliverManagerImpl extends BaseCrudManagerImpl implements BillOmDeliverManager {
	
	@Resource
    private ConLabelService  conLabelService;
    
    @Resource
    private ConLabelDtlService conLabelDtlService;
	
    @Resource
    private BillOmDeliverService billOmDeliverService;
    
    @Resource
    private BillOmDeliverDtlService billOmDeliverDtlService;

    @Resource
    private ConBoxService conBoxService;
    
    @Resource
    private BmContainerService bmContainerService;
    
    private static final String  STATUS10 = "10";

    @Override
    public BaseCrudService init() {
        return billOmDeliverService;
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public int deleteBatch(String ids) throws ManagerException {
		//容器记账，锁定箱子  占用，status=1，optBillNo，optBillType都要传
		List<BmContainer> lstContainer = new  ArrayList<BmContainer>();
		Map<String,String> returnMap = new HashMap<String,String>();
		int count = 0;
		if(StringUtils.isNotBlank(ids)){
			String[] idArr = ids.split(",");
			for(String str : idArr){
				String[] tmp = str.split("-");
				if(tmp.length==3){
					try {
						String locno = tmp[0];
						String deliverNo = tmp[1];
						String ownerNo = tmp[2];
						
						BillOmDeliver key = new BillOmDeliver();
						key.setLocno(locno);
						key.setDeliverNo(deliverNo);
						key.setOwnerNo(ownerNo);
						BillOmDeliver selObj = billOmDeliverService.findById(key);
						if(null==selObj || !STATUS10.equals(selObj.getStatus())){
							throw new ManagerException("单据"+deliverNo+"不是新建状态，不可以删除！");
						}
						
						//查询单据下的所有标签号
						BillOmDeliverDtl deliverDtl = new BillOmDeliverDtl();
						deliverDtl.setLocno(locno);
						deliverDtl.setDeliverNo(deliverNo);
						deliverDtl.setOwnerNo(ownerNo);
						List<BillOmDeliverDtl> lstBillOmDeliverDtl = billOmDeliverDtlService.findBoxNoByDetail(deliverDtl);
						for (BillOmDeliverDtl b : lstBillOmDeliverDtl) {
							
							ConLabel  conLabel  = new  ConLabel();
							conLabel.setLocno(locno);
							conLabel.setScanLabelNo(b.getBoxNo());
							conLabel.setStatus("A0");
							//释放本单据已锁定的箱信息。更新标签状态为61（复核完成）
							int a = conLabelService.modifyStatusByLocnoAndLabelNo(conLabel);
							if(a < 1){
								throw new ManagerException("未更新到标签号【"+b.getBoxNo()+"】的状态！");
							}
							
							ConLabelDtl  conLabelDtl = new  ConLabelDtl();
							conLabelDtl.setStatus("A0");//标签状态为6E（外复核完成）
							conLabelDtl.setLocno(locno);
							conLabelDtl.setScanLabelNo(b.getBoxNo());
							int c = conLabelDtlService.modifyLabelDtlStatusByLabelNo(conLabelDtl);
							if(c < 1){
								throw new ManagerException("未更新到标签明细【"+b.getBoxNo()+"】的状态！");
							}
							
							ConBox conBox = new ConBox();
							conBox.setStatus("2");
							conBox.setLocno(locno);
							conBox.setOwnerNo(ownerNo);
							conBox.setBoxNo(b.getBoxNo());
							int b1 = conBoxService.modifyById(conBox);
							if(b1 < 1){
								throw new ManagerException("未更新到箱号【"+b.getBoxNo()+"】的状态！");
							}
							
							//添加解锁的容器
							String labelNo = b.getBoxNo();
							if(!returnMap.containsKey(labelNo)){
								BmContainer  bc = new BmContainer();
								bc.setLocno(locno);
								bc.setConNo(labelNo);
								bc.setStatus(ContainerStatusEnums.STATUS0.getContainerStatus());
								bc.setFalg("Y");
								lstContainer.add(bc);
								returnMap.put(labelNo, labelNo);
							}
						}
						
						//锁定容器号
						if (CommonUtil.hasValue(lstContainer)) {
							bmContainerService.batchUpdate(lstContainer);
							lstContainer.clear();
							returnMap.clear();
						}
						
						billOmDeliverService.deleteById(key);
						billOmDeliverService.deleteByDeliverDtl(key);
						count++;
						
					} catch (ServiceException e) {
						throw new ManagerException(e);
					}
				}
			}
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String, Object> checkBoxBillOmDeliver(String ids, SystemUser user)
			throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			String msg1 = "N";
			if(StringUtils.isNotBlank(ids)){
				Date now = new Date();
				BillOmDeliverDtl dtl = new BillOmDeliverDtl();
				dtl.setEditor(user.getLoginName());
				dtl.setEditorname(user.getUsername());
				dtl.setEdittm(now);
				String[] idArr = ids.split(",");
				for(String id : idArr){
					msg1 = "N";
					String msg2 = "审核失败!";
					String[] tmp = id.split("-");
					if(tmp.length==3){
						BillOmDeliver billOmDeliver = new BillOmDeliver();
						billOmDeliver.setLocno(tmp[0]);
						billOmDeliver.setOwnerNo(tmp[1]);
						billOmDeliver.setDeliverNo(tmp[2]);
						billOmDeliver.setSealNo("0");
						billOmDeliver.setIsDevice("0");
						billOmDeliver.setAuditor(user.getLoginName());
						
						BillOmDeliver singojb = billOmDeliverService.findById(billOmDeliver);
						if(null == singojb || !STATUS10.equals(singojb.getStatus())){
							throw new ManagerException("单据"+tmp[2]+"不为新建状态！");
						}
						
						if(StringUtils.isNotBlank(billOmDeliver.getLocno()) 
								&& StringUtils.isNotBlank(billOmDeliver.getOwnerNo()) 
								&& StringUtils.isNotBlank(billOmDeliver.getDeliverNo())
								&& StringUtils.isNotBlank(billOmDeliver.getSealNo()) 
								&& StringUtils.isNotBlank(billOmDeliver.getIsDevice())
								&& StringUtils.isNotBlank(billOmDeliver.getAuditor())){
							
							Map<String, String> map = new HashMap<String, String>();
				   			map.put("strLocNo", billOmDeliver.getLocno());
				   			map.put("strOwnerNo", billOmDeliver.getOwnerNo());
				   			map.put("strDeliverNo", billOmDeliver.getDeliverNo());
				   			map.put("strSealNo", billOmDeliver.getSealNo());
				   			map.put("strIsDevice", billOmDeliver.getIsDevice());
				   			map.put("strUserID", billOmDeliver.getAuditor());
				   			
							billOmDeliverService.checkBillOmDeliver(map);
							
							String stroutmsg = map.get("strResult");
				   			if(StringUtils.isNotBlank(stroutmsg)){
								String[] msgs = stroutmsg.split("\\|");
								if(msgs != null && msgs.length >= 2) {
									msg1 = msgs[0];
									msg2 = msgs[1];
								}
								if (!"Y".equals(msg1)) {
					   				throw new ManagerException(msg2);
					   			}
							} else {
								throw new ManagerException(msg2);
							}
				   			//审核时修改明细的editor、editorname、edittm
				   			dtl.setDeliverNo(billOmDeliver.getDeliverNo());
				   			dtl.setLocno(billOmDeliver.getLocno());
				   			dtl.setOwnerNo(billOmDeliver.getOwnerNo());
				   			billOmDeliverDtlService.modifyById(dtl);
				   			
						}else{
							msg2 = "参数非法！";
							throw new ManagerException(msg2);
						}
					}
				}
				if("Y".equals(msg1)) {
					mapObj.put("flag", "success");
	   				mapObj.put("msg", "审核成功！");
				} else {
					mapObj.put("flag", "warn");
	   				mapObj.put("msg", "无审核成功记录！");
				}
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ManagerException.class)
	public Map<String, Object> checkBillFlagOmDeliver(String ids, String auditor)
			throws ManagerException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		String msg1 = "N";
		String msg2 = "审核失败!";
		try {
			if(StringUtils.isNotBlank(ids)){
				String[] idArr = ids.split(",");
				for(String id : idArr){
					String[] tmp = id.split("-");
					if(tmp.length==3){
						BillOmDeliver billOmDeliver = new BillOmDeliver();
						billOmDeliver.setLocno(tmp[0]);
						billOmDeliver.setOwnerNo(tmp[1]);
						billOmDeliver.setDeliverNo(tmp[2]);
						billOmDeliver.setSealNo("0");
						billOmDeliver.setIsDevice("0");
						billOmDeliver.setAuditor(auditor);
						billOmDeliver.setAudittm(new Date());
						if(StringUtils.isNotBlank(billOmDeliver.getLocno()) 
								&& StringUtils.isNotBlank(billOmDeliver.getOwnerNo()) 
								&& StringUtils.isNotBlank(billOmDeliver.getDeliverNo())
								&& StringUtils.isNotBlank(billOmDeliver.getSealNo()) 
								&& StringUtils.isNotBlank(billOmDeliver.getIsDevice())
								&& StringUtils.isNotBlank(billOmDeliver.getAuditor())){
							
							Map<String, String> map = new HashMap<String, String>();
				   			map.put("strLocNo", billOmDeliver.getLocno());
				   			map.put("strOwnerNo", billOmDeliver.getOwnerNo());
				   			map.put("strDeliverNo", billOmDeliver.getDeliverNo());
				   			map.put("strSealNo", billOmDeliver.getSealNo());
				   			map.put("strIsDevice", billOmDeliver.getIsDevice());
				   			map.put("strUserID", billOmDeliver.getAuditor());
				   			
				   			//执行审核
							billOmDeliverService.checkBillOmDeliver(map);
							String stroutmsg = map.get("strResult");
				   			if(StringUtils.isNotBlank(stroutmsg)){
								String[] msgs = stroutmsg.split("\\|");
								if(msgs != null && msgs.length >= 2) {
									msg1 = msgs[0];
									msg2 = msgs[1];
								}
				   			
//////////////////////////////手动审核BEGIN////////////////////////////
//							billOmDeliver.setStatus("13");
//							int count = billOmDeliverService.modifyById(billOmDeliver);
//							if(count > 0) {
//								int two = billOmDeliverService.updateBillOmDeliverDtl(billOmDeliver);
//								if(two <= 0) {
//									msg2 = "审核派车单明细失败！";
//									throw new ManagerException(msg2);
//								}
//								msg1 = "Y";
//								msg2 = "审核成功！";
//////////////////////////////手动审核 END////////////////////////////
								if (!"Y".equals(msg1)) {
					   				throw new ManagerException(msg2);
					   			} else {
					   				if(checkFlag(billOmDeliver)) {
					   					mapObj.put("flag", "success");
						   				mapObj.put("msg", msg2);
						   				return mapObj;
					   				} else {
					   					msg2 = "派车单回填失败！";
					   					throw new ManagerException(msg2);
					   				}
					   			}
							} else {
								throw new ManagerException(msg2);
							}
						}else{
							msg2 = "参数非法！";
							throw new ManagerException(msg2);
						}
					}
				}
			}
			return mapObj;
		} catch (Exception e) {
			throw new ManagerException(e.getMessage());
		}
	}
	
	/**
	 * 回填派车单状态
	 * @param billOmDeliver
	 * @return
	 */
	private boolean checkFlag(BillOmDeliver billOmDeliver) throws ManagerException {
		boolean isCheck = false;
		
		List<BillOmDeliver> list;
		try {
			list = billOmDeliverService.selectLoadproposeDtl(billOmDeliver);
			if(list!= null && list.size() > 0) {
				int no = 0;
				for(BillOmDeliver bd : list) {
					String locno = bd.getLocno();
					String loadproposeNo = bd.getLoadproposeNo();
					String containerNo = bd.getContainerNo();
					String storeNo = bd.getStoreNo();
//					
					BillOmDeliver deliver = new BillOmDeliver();
					deliver.setLocno(locno);
					deliver.setLoadproposeNo(loadproposeNo);
					deliver.setStoreNo(storeNo);
					deliver.setContainerNo(containerNo);
					no += billOmDeliverService.updateLoadproposeDtl(deliver);
				}
				billOmDeliver = new BillOmDeliver();
				billOmDeliver.setLocno(list.get(0).getLocno());
				billOmDeliver.setLoadproposeNo(list.get(0).getLoadproposeNo());
				int count = billOmDeliverService.loadproposeDtlCount(billOmDeliver);
				if(count == 0) {
					billOmDeliverService.updateLoadpropose(billOmDeliver);
				}
				if(no > 0) {
					isCheck = true;
				}
			}
		} catch (ServiceException e) {
			isCheck = false;
			e.printStackTrace();
			throw new ManagerException("派车单回填异常");
		}
		
		return isCheck;
	}
	
	@Override
	public List<BillOmDeliver> findBillOmDeliverSum(Map<String,Object> params,AuthorityParams authorityParams) throws ManagerException {
		try {
			return billOmDeliverService.findBillOmDeliverSum(params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}

	@Override
	public List<BillOmDeliver> findPrintOmDeliverList(String sortColumn,
			String sortOrder, Map<String, Object> params,
			AuthorityParams authorityParams)
			throws ManagerException {
		try {
			return billOmDeliverService.findPrintOmDeliverList(sortColumn,sortOrder,params,authorityParams);
		} catch (ServiceException e) {
			throw new ManagerException(e);
		}
	}
}