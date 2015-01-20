package com.yougou.logistics.city.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import com.yougou.logistics.city.common.model.BillWmDeliver;
import com.yougou.logistics.city.common.model.BillWmDeliverDtl;
import com.yougou.logistics.city.common.model.BillWmDeliverDtlKey;
import com.yougou.logistics.city.common.model.BillWmDeliverKey;
import com.yougou.logistics.city.common.model.ConBox;
import com.yougou.logistics.city.common.model.ConLabel;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.service.BillWmDeliverDtlService;
import com.yougou.logistics.city.service.BillWmDeliverService;
import com.yougou.logistics.city.service.ConBoxService;
import com.yougou.logistics.city.service.ConLabelDtlService;
import com.yougou.logistics.city.service.ConLabelService;
import com.yougou.logistics.city.service.ProcCommonService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
@Service("billWmDeliverManager")
class BillWmDeliverManagerImpl extends BaseCrudManagerImpl implements BillWmDeliverManager {
	
    @Resource
    private BillWmDeliverService billWmDeliverService;
    
    @Resource
    private BillWmDeliverDtlService billWmDeliverDtlService;
    
    @Resource
    private ProcCommonService  procCommonService;
    
    @Resource
    private ConLabelService  conLabelService;
    
    @Resource
    private ConLabelDtlService conLabelDtlService;
    
    @Resource
    private ConBoxService conBoxService;

    private final static String STATUS2 = "2";
    
    @Override
    public BaseCrudService init() {
        return billWmDeliverService;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public boolean deleteWmDeliver(String nos) throws ManagerException {
		try{
			if(StringUtils.isNotBlank(nos)){
				String [] strs = nos.split(",");
				for(String obj : strs){
					String [] imImportArray = obj.split("#");
						if(imImportArray.length > 2){
							String deliverNo = imImportArray[0];
							String locno = imImportArray[1];
							String ownerNo = imImportArray[2];
							
							BillWmDeliverDtlKey billWmDeliverDtlKey = new  BillWmDeliverDtlKey();
							billWmDeliverDtlKey.setDeliverNo(deliverNo);
							billWmDeliverDtlKey.setLocno(locno);
							billWmDeliverDtlKey.setOwnerNo(ownerNo);
							
							//查询单据下的所有标签号
							List<BillWmDeliverDtl> lstBillWmDeliverDtl = billWmDeliverDtlService.selectBoxNoByDetail(billWmDeliverDtlKey);
							for(BillWmDeliverDtl vo : lstBillWmDeliverDtl){
								ConLabel  conLabel  = new  ConLabel();
								conLabel.setLocno(locno);
								conLabel.setScanLabelNo(vo.getBoxNo());
								conLabel.setStatus("52");
								//释放本单据已锁定的箱信息。更新标签状态为61（复核完成）
								int a = conLabelService.modifyStatusByLocnoAndLabelNo(conLabel);
								if(a < 1){
									throw new ManagerException("未更新到标签号【"+vo.getBoxNo()+"】的状态！");
								}
								
								ConLabelDtl  conLabelDtl = new  ConLabelDtl();
								conLabelDtl.setStatus("52");//标签状态为6E（外复核完成）
								conLabelDtl.setLocno(locno);
								conLabelDtl.setScanLabelNo(vo.getBoxNo());
								int c = conLabelDtlService.modifyLabelDtlStatusByLabelNo(conLabelDtl);
								if(c < 1){
									throw new ManagerException("未更新到标签明细【"+vo.getBoxNo()+"】的状态！");
								}
								
								//更改箱状态
								ConBox entityBox = new ConBox();
								entityBox.setLocno(locno);
								entityBox.setOwnerNo(ownerNo);
								entityBox.setBoxNo(vo.getBoxNo());
								entityBox.setStatus(STATUS2);
								int cBox = conBoxService.modifyById(entityBox);
								if(cBox < 1){
									throw new ManagerException("未更新到箱号【"+vo.getBoxNo()+"】的状态！");
								}
								
							}
							
							//删除明细
							BillWmDeliverDtl billWmDeliverDtl = new  BillWmDeliverDtl();
							billWmDeliverDtl.setDeliverNo(deliverNo);
							billWmDeliverDtl.setLocno(locno);
							billWmDeliverDtl.setOwnerNo(ownerNo);
							billWmDeliverDtlService.deleteById(billWmDeliverDtl);
							
							//删除主表
							BillWmDeliverKey billWmDeliverKey = new  BillWmDeliverKey();
							billWmDeliverKey.setDeliverNo(deliverNo);
							billWmDeliverKey.setLocno(locno);
							billWmDeliverKey.setOwnerNo(ownerNo);
							int  a  = billWmDeliverService.deleteById(billWmDeliverKey);
							if(a  < 1){
								throw new ManagerException("未删除到对应的信息！");
							}
						}
				}
			}
		}catch(Exception e){
			throw new ManagerException(e);
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object> addWmDeliver(BillWmDeliver billWmDeliver)
			throws ManagerException {
		Map<String,Object> obj=new HashMap<String,Object>();
		try{
			billWmDeliver.setStatus("10");//状态10-建单；13-完成；
			billWmDeliver.setCreatetm(new Date());
			billWmDeliver.setEdittm(new Date());
			//自定生成单号
			String deliverNo = procCommonService.procGetSheetNo(billWmDeliver.getLocno(),CNumPre.DELIVER_PRE);
			billWmDeliver.setDeliverNo(deliverNo);
			int a = billWmDeliverService.add(billWmDeliver);
			if(a < 1){
				 throw new ManagerException("新增时未更新到记录！");
			}
			obj.put("returnMsg", true);
			obj.put("deliverNo", deliverNo);
			obj.put("locno", billWmDeliver.getLocno());
		}catch (Exception e) {
            throw new ManagerException(e);
        }
		return obj;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Map<String, Object>  auditWmDeliver(String noStrs, String locno,String loginName,String userName) throws ManagerException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		boolean flag = false;
		String resultMsg = "";
		try{
			if (StringUtils.isNotBlank(noStrs)){
				String[] strs = noStrs.split(",");
				BillWmDeliver billWmDeliver =null;
				for(String obj : strs){
					String[] wmrecedeArray = obj.split("#");
					if(wmrecedeArray.length > 1){
						String ownerNo = wmrecedeArray[0];
						String deliverNo = wmrecedeArray[1];
						
						billWmDeliver =new BillWmDeliver();
						billWmDeliver.setOwnerNo(ownerNo);
						billWmDeliver.setLocno(locno);
						billWmDeliver.setDeliverNo(deliverNo);
						billWmDeliver.setEditor(loginName);
						billWmDeliver.setEditorName(userName);
						billWmDeliver.setEdittm(new Date());
						billWmDeliverService.modifyById(billWmDeliver);
						
						//调用存储过程..添加验收表、库存表
						Map<String, String> mapAudit = new HashMap<String, String>();
						mapAudit.put("I_locno", locno);
						mapAudit.put("I_owner_no", ownerNo);
						mapAudit.put("I_deliver_no", deliverNo);
						mapAudit.put("I_oper_user", loginName);
						billWmDeliverService.procWmDeliverAuditCar(mapAudit);
						String strOutMsg = mapAudit.get("stroutmsg");
						if (!"Y|".equals(strOutMsg)) {
							String[] msgs = strOutMsg.split("\\|");
							flag = false;
							resultMsg = msgs[1];
						} else {
							flag = true;
							resultMsg = "审核成功！";
						}

						Map<String, Object> _map = new HashMap<String, Object>();
						_map.put("recedeName", loginName);
						_map.put("recedeChName", userName);
						_map.put("recedeDate", new Date());
						_map.put("locno", locno);
						_map.put("ownerNo", ownerNo);
						_map.put("deliverNo", deliverNo);
						billWmDeliverDtlService.updateOperateRecord(_map);
					}
				}
			}
		}catch (Exception e) {
            throw new ManagerException(e);
        }
		if(flag) {
			objMap.put("flag", "true");
			objMap.put("resultMsg", resultMsg);
		} else {
			throw new ManagerException(resultMsg);
		}
		return objMap;
	}
	
}