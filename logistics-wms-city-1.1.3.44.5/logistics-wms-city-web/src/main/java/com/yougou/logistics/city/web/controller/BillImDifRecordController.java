package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillImDifRecord;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillImDifRecordManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.vo.CurrentUser;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 收货差异登记
 * @author chen.yl1
 * @date  2014-01-11 15:42:26
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
@Controller
@RequestMapping("/bill_im_dif_record")
@ModuleVerify("25070080")
public class BillImDifRecordController extends BaseCrudController<BillImDifRecord> {
	@Log
	private Logger log;
	@Resource
    private BillImDifRecordManager billImDifRecordManager;
    @Resource
    private ProcCommonManager procCommonManager;
    @Override
    public CrudInfo init() {
        return new CrudInfo("billimdifrecord/",billImDifRecordManager);
    }
    
    /**
   	 * 新增主表信息
   	 * @param billumreceipt
   	 * @param req
   	 * @return
   	 * @throws ManagerException
   	 */
   	@RequestMapping(value = "/saveMainInfo")
   	@ResponseBody
   	@OperationVerify(OperationVerifyEnum.ADD)
   	public String saveMainInfo(BillImDifRecord billImDifRecord,HttpServletRequest req)throws ManagerException {
   		String defRecordNo = null;
     		try {
     			defRecordNo = procCommonManager.procGetSheetNo(billImDifRecord.getLocno(), CNumPre.SM_WASTE_PRE);
     			billImDifRecord.setDefRecordNo(defRecordNo);
     			billImDifRecord.setStatus("10");
     			billImDifRecord.setCreatetm(new Date());
     			billImDifRecordManager.add(billImDifRecord);
     		} catch (Exception e) {
     			log.error("=======新增收货差异异常："+e.getMessage(),e);
     			return "{\"success\":\"" + false + "\"}";
     		}
     		return "{\"success\":\"" + true + "\",\"defRecordNo\":\"" + defRecordNo + "\"}";
   	}
   	
   	/**
   	 * 修改主表信息
   	 * @param billumreceipt
   	 * @param req
   	 * @return
   	 * @throws ManagerException
   	 */
   	@RequestMapping(value = "/editMainInfo")
   	@ResponseBody
   	@OperationVerify(OperationVerifyEnum.MODIFY)
   	public String editMainInfo(BillImDifRecord billImDifRecord,HttpServletRequest req)throws ManagerException {
   		String defRecordNo = null;
     		try {
     			defRecordNo = billImDifRecord.getDefRecordNo();
     			billImDifRecord.setEdittm(new Date());
     			billImDifRecordManager.modifyById(billImDifRecord);
     		} catch (Exception e) {
     			log.error("=======更细收货差异异常："+e.getMessage(),e);
     			return "{\"success\":\"" + false + "\"}";
     		}
     		return "{\"success\":\"" + true + "\",\"defRecordNo\":\"" + defRecordNo + "\"}";
   	}
   	
   	/**
	 * 删除主表和主表明细
	 * @param ids
	 * @return
	 * @throws ManagerException
	 */
	 @RequestMapping(value="/delete_records")
	 @ResponseBody
	 @OperationVerify(OperationVerifyEnum.REMOVE)
	 public String deleteRecords(String ids) throws ManagerException{
		 String m="";
	 	try {
	 		int count= billImDifRecordManager.deleteBatch(ids);
	 		if(count>0){
	 			m="success";
	 		}else{
	 			m="fail";
	 		}
	 	}catch (Exception e) {
	 		log.error("=======删除收货差异异常："+e.getMessage(),e);
	 		m="fail:"+e.getMessage();
	 	}
	 	return m;
	 }
	 
	 /**
	  * 审核
	  * @param ids
	  * @param req
	  * @return
	  * @throws JsonParseException
	  * @throws JsonMappingException
	  * @throws IOException
	  * @throws ManagerException
	  */
	 @RequestMapping(value = "/check")
	 @ResponseBody
	 @OperationVerify(OperationVerifyEnum.VERIFY)
	 public ResponseEntity<Map<String, Object>> checkBillSmWaste(String ids, HttpServletRequest req)
			 throws JsonParseException, JsonMappingException, IOException, ManagerException {
		 Map<String, Object> flag = new HashMap<String, Object>();
		 try {
			 CurrentUser currentUser = new CurrentUser(req);
			 flag = billImDifRecordManager.checkImDifRecord(ids,currentUser.getLoginName());
			 return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		 } catch (Exception e) {
			 flag.put("flag", "warn");
			 flag.put("msg", e.getMessage());
			 log.error("审核时异常："+e.getMessage(),e);
			 return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	   	}
	}
}