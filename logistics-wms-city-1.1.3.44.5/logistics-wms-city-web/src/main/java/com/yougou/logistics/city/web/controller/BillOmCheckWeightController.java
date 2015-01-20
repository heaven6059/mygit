package com.yougou.logistics.city.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmCheckWeight;
import com.yougou.logistics.city.common.model.ConLabel;
import com.yougou.logistics.city.common.model.ConLabelDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.ItemPack;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.manager.BillOmCheckWeightManager;
import com.yougou.logistics.city.manager.ConLabelDtlManager;
import com.yougou.logistics.city.manager.ConLabelManager;
import com.yougou.logistics.city.manager.ItemManager;
import com.yougou.logistics.city.manager.ItemPackManager;

/**
 * 
 * 称重controller
 * 
 * @author qin.dy
 * @date 2013-9-29 下午9:36:14
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_check_weight")
@ModuleVerify("25040050")
public class BillOmCheckWeightController extends BaseCrudController<BillOmCheckWeight> {
    @Resource
    private BillOmCheckWeightManager billOmCheckWeightManager;
	
    @Resource
    private ConLabelDtlManager conLabelDtlManager;
    
    @Resource
    private ConLabelManager conLabelManager;
    
    @Resource
    private ItemPackManager itemPackManager;
    
    @Resource
    private ItemManager itemManager;
    @Log
	private Logger log;
    @Override
    public CrudInfo init() {
        return new CrudInfo("billomcheckweight/",billOmCheckWeightManager);
    }
    @RequestMapping(value = "/get_dtl_by_labelno.json")
  	@ResponseBody
  	public  ConLabel queryList(HttpServletRequest req, ConLabel conLabel){
    	try {
    		String locno = req.getParameter("locnoAdd");
        	String labelNo = req.getParameter("labelAdd");
        	if(locno != null && labelNo != null) {
        		Map<String,Object> params = new HashMap<String, Object>();
              	params.put("labelNo", labelNo);
              	params.put("locno", locno);
              	
              	//查询总表信息
              	List<ConLabel> conLabelLst = conLabelManager.findByBiz(conLabel, params);
              	for(ConLabel label : conLabelLst){
              		params.clear();
              		params.put("labelNo", labelNo);
                  	params.put("locno", locno);
              		params.put("containerNo", label.getContainerNo());
              		//查询明细表信息
              		List<ConLabelDtl> dtlLst =  conLabelDtlManager.findByBiz(new ConLabelDtl(), params);
              		for(ConLabelDtl dtl : dtlLst){
              			Item primaryKey = new Item();
              			primaryKey.setItemNo(dtl.getItemNo());
              			//查询ITEM表信息
              			Item item = itemManager.findById(primaryKey);
              			dtl.setItem(item);
              			ItemPack itemPack = new ItemPack();
              			itemPack.setItemNo(dtl.getItemNo());
              			itemPack.setSizeNo(dtl.getSizeNo());
              			itemPack.setPackQty(dtl.getPackQty().shortValue());
              			//查询itemPack表信息
              			itemPack = itemPackManager.findById(itemPack);
              			BigDecimal weight = itemPack.getPackWeight();
          				if(weight == null) {
          					weight = new BigDecimal(0);
          				}
          				dtl.setLabelNo(labelNo);
          				dtl.setItemWeight(weight);
          				dtl.setItemName(dtl.getItem().getItemName());
              		}
              		label.setLableDtl(dtlLst);
              		conLabel = label;
              	}
        	}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
      	return conLabel;
  	}
    
    @RequestMapping(value = "/save_billomcheckweight")
    @OperationVerify(OperationVerifyEnum.ADD)
    public ResponseEntity<Map<String, Boolean>> save(HttpServletRequest req, BillOmCheckWeight billOmCheckWeight) {
    	try {
    		String locno = req.getParameter("locnoAdd");
        	String containerNo = req.getParameter("containerNoAdd");
        	String containerType = req.getParameter("containerTypeAdd");
        	String labelNo = req.getParameter("labelNoAdd");
        	if(locno!=null) {
        		locno.trim();
        	}
        	if(containerNo!=null) {
        		containerNo.trim();
        	}
        	if(containerType!=null) {
        		containerType=containerType.trim();
        	}
        	if(labelNo!=null) {
        		labelNo=labelNo.trim();
        	}
        	//获取数据
    		ConLabel conLabel = new ConLabel();
    		conLabel.setLocno(locno);
    		conLabel.setContainerNo(containerNo);
    		conLabel.setContainerType(containerType);
    		conLabel.setLabelNo(labelNo);
    		ConLabel con = conLabelManager.findById(conLabel);
    		String storeNo = "";
    		if(con != null) {
    			storeNo = con.getStoreNo();
    		}
    		//获取登陆用户
    		HttpSession session = req.getSession();
    		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
        	
        	//查询重量表信息
        	billOmCheckWeight.setLocno(locno);
        	billOmCheckWeight.setContainerNo(containerNo);
        	billOmCheckWeight.setLabelNo(labelNo);
        	BillOmCheckWeight check = billOmCheckWeightManager.findById(billOmCheckWeight);
    		//判断如果存在记录，更新数据；否则插入数据
        	if(check != null && check.getLocno().equals(locno)
        			&& check.getContainerNo().equals(containerNo) && check.getLabelNo().equals(labelNo)) {
        		billOmCheckWeight.setStoreNo(storeNo);
        		billOmCheckWeight.setOperateWorker(user.getLoginName());
        		billOmCheckWeight.setOperateDate(new Date());
        		billOmCheckWeightManager.modifyById(billOmCheckWeight);
        	} else {
        		BigDecimal realWeight = billOmCheckWeight.getRealWeight();
        		check = new BillOmCheckWeight();
        		check.setLocno(locno);
        		check.setContainerNo(containerNo);
        		check.setLabelNo(labelNo);
        		check.setStoreNo(storeNo);
        		check.setRealWeight(realWeight);
        		check.setOperateWorker(user.getLoginName());
        		check.setOperateDate(new Date());
        		billOmCheckWeightManager.add(check);
        	}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    	Map<String, Boolean> flag = new HashMap<String, Boolean>();
    	return new ResponseEntity<Map<String, Boolean>>(flag, HttpStatus.OK);
    }
}