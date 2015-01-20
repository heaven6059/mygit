package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.Lookupdtl;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.manager.LookupdtlManager;
import com.yougou.logistics.city.manager.common.CommonUtilManager;

@Controller
@RequestMapping("/lookupdtl")
public class LookupdtlController extends BaseCrudController<Lookupdtl> {
	
	@Log
	private Logger log;
	
    @Resource
    private LookupdtlManager lookupdtlManager;
    
    @Resource
	private CommonUtilManager commonUtilManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("lookupdtl/",lookupdtlManager);
    }
    
    @RequestMapping(value="/selectOutstockDirectExpType")
    @ResponseBody
	public  List<Lookupdtl> selectOutstockDirectExpType(String lookupcode,String locno) throws ManagerException{
	    List<Lookupdtl> listObj=new ArrayList<Lookupdtl>(0);
	    try{
	    	listObj = lookupdtlManager.selectOutstockDirectExpType(lookupcode,locno);
	    }catch(Exception e){
	    	log.error("=======查询拣货任务分派表的出库类型时异常："+e.getMessage(),e);
	    	throw new ManagerException(e);
	    }
		return listObj;
	}
    
    /**
     * 新增码表明细信息
     * @param req
     * @return
     * @throws ManagerException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @RequestMapping(value="/addLookupDtl")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> addLookupDtl(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException, ManagerException{
		ResponseEntity<Map<String, Boolean>> entity = this.save(req);
		try {
			LookupDtl l = new LookupDtl();
			List<LookupDtl> listDtls=commonUtilManager.queryLookupDtlsList(l);
			InitCacheController.reLoadMap(listDtls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return entity;
    }
    
    /**
     * 根据品牌查询码表明细信息
     * @param req
     * @return
     * @throws ManagerException
     */
    @RequestMapping(value="/selectLookupdtlBySysNo")
    @ResponseBody
    public List<Lookupdtl> selectLookupdtlBySysNo(String lookupcode,String sysNo) throws ManagerException{
    	List<Lookupdtl> list = new ArrayList<Lookupdtl>();
		try {
			list = this.lookupdtlManager.selectLookupdtlBySysNo(lookupcode,sysNo);
		} catch (Exception e) {
			log.error("======= 根据品牌查询码表明细信息时异常："+e.getMessage(),e);
	    	throw new ManagerException(e);
		}
		return list;
    }
}