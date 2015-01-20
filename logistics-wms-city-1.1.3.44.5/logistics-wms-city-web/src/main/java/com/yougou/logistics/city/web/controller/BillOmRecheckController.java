package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.BillOmDivideDtl;
import com.yougou.logistics.city.common.model.BillOmDivideKey;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmDivideManager;
import com.yougou.logistics.city.manager.BillOmRecheckManager;
import com.yougou.logistics.city.manager.ConLabelManager;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/**
 * 
 * 分货复核单controller
 * 
 * @author qin.dy
 * @date 2013-10-11 上午11:22:37
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_recheck")
@ModuleVerify("25040060")
public class BillOmRecheckController extends BaseCrudController<BillOmRecheck> {
	@Log
	private Logger log;
    @Resource
    private BillOmRecheckManager billOmRecheckManager;
    @Resource
    private ProcCommonManager procCommonManager;
    @Resource
    private ConLabelManager conLabelManager;
    @Resource
    private BillOmDivideManager billOmDivideManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billomrecheck/",billOmRecheckManager);
    }
    
    @RequestMapping(value = "/list4Source.json")
	@ResponseBody
	public Map<String, Object> queryList4Source(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map<String, Object> p = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(p);
			Object divideObj = params.get("divideType");
			if(divideObj != null){
				String divideType = divideObj.toString();
				if(!StringUtils.isEmpty(divideType)){
					params.put("divideTypeList", divideType.split(","));
				}
			}
			int total = this.billOmRecheckManager.findCount4Source(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmRecheck> list = this.billOmRecheckManager.findByPage4Source(page, null, null, params, authorityParams);
			
			obj.put("total", total);
			obj.put("rows", list);
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmRecheckManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmRecheck> list = this.billOmRecheckManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			Map<String, Object> footer = new HashMap<String, Object>();
			List<Object> footerList = new ArrayList<Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			BigDecimal totalRealQty = new BigDecimal(0);
			BigDecimal totalPackageQty = new BigDecimal(0);
			for (BillOmRecheck billOmRecheck : list) {
//				Map<String, Object> mapObj = new HashMap<String, Object>();
//				mapObj.put("locno", billOmRecheck.getLocno());
//				mapObj.put("storeNo", billOmRecheck.getStoreNo());
//				mapObj.put("divideNo", billOmRecheck.getDivideNo());
//				Map<String, Object> sumFoot = billOmRecheckManager.selectSumQty(mapObj, authorityParams);
//				BigDecimal itemQty = (BigDecimal) sumFoot.get("itemQty");
//				BigDecimal realQty = (BigDecimal)sumFoot.get("realQty");
				totalItemQty = totalItemQty.add(billOmRecheck.getItemQty());
				totalRealQty = totalRealQty.add(billOmRecheck.getRealQty());
				totalPackageQty = totalPackageQty.add(billOmRecheck.getPackageQty());
			}
			
			footer.put("recheckNo", "小计");
			footer.put("itemQty", totalItemQty);
			footer.put("realQty", totalRealQty);
			footer.put("packageQty", totalPackageQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmRecheckManager.selectRecheckSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("real_qty", 0);
					sumFoot.put("package_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("recheck_no", "合计");
			} else {
				sumFoot.put("recheckNo", "合计");
			}
			footerList.add(sumFoot);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
    @RequestMapping(value = "/check")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.VERIFY)
   	public ResponseEntity<Map<String, Object>> check(String ids,String checkUser,HttpServletRequest req){
    	Map<String,Object> flag=new HashMap<String,Object>();
   		try {
   			//CurrentUser currentUser=new CurrentUser(req);
   			HttpSession session = req.getSession();
   			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
   			billOmRecheckManager.check(ids,user,checkUser);
   			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   		} catch (ManagerException e) {
   			log.error(e.getMessage(), e);
   			flag.put("success", "false");
			flag.put("msg", e.getMessage());
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			flag.put("success", "false");
			flag.put("msg", e.getMessage());
   		}
   		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   	}
    
    @RequestMapping(value = "/deleteBillOmRecheck")
   	@ResponseBody    
   	@OperationVerify(OperationVerifyEnum.REMOVE)
   	public ResponseEntity<Map<String, Object>> deleteBillOmRecheck(String datas,HttpServletRequest req) {
    	Map<String,Object> flag=new HashMap<String,Object>();
   		try {
   			
   			datas = URLDecoder.decode(datas,"UTF-8");
   			ObjectMapper mapper = new ObjectMapper();
   			List<BillOmRecheck> listOmRechecks = new ArrayList<BillOmRecheck>();
   			if (StringUtils.isNotEmpty(datas)) {
   				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
   				listOmRechecks=convertListWithTypeReferenceR(mapper,list);
   			}
   			billOmRecheckManager.deleteBillOmRecheck(listOmRechecks);
   			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   		} catch (ManagerException e) {
   			log.error(e.getMessage(), e);
   			flag.put("success", "false");
			flag.put("msg", e.getMessage());
   		} catch (Exception e) {
   			log.error(e.getMessage(), e);
   			flag.put("success", "false");
			flag.put("msg", e.getMessage());
   		}
   		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
   	}
    
    
    @RequestMapping(value = "/divideSelectQuery")
	@ResponseBody    
	public  Map<String, Object> divideSelectQuery(HttpServletRequest req, Model model) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = this.billOmRecheckManager.selectDivideCollectCount(params, authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillOmDivide> list = this.billOmRecheckManager.selectDivideCollectByPage(page, sortColumn, sortOrder, params, authorityParams);
    		obj.put("total", total);
    		obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj; 
	}  
    
    @RequestMapping(value = "/selectStoreFromDivideDtl")
   	@ResponseBody    
   	public  List<Store> selectStoreFromDivideDtl(HttpServletRequest req, Model model) {
    	List<Store> result = null;
   		try {
   			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
   			Map<String, Object> params = builderParams(req, model);
   	   		result = billOmRecheckManager.selectStoreByParam(params, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
   		return result;
   	}
    
    
    @RequestMapping(value = "/queryRecheckBoxItem")
   	@ResponseBody    
   	public  Map<String, Object> queryRecheckBoxItem(HttpServletRequest req, Model model){
    	
//    	Map<String, Object> obj = new HashMap<String, Object>();
//    	List<Object> footerList = new ArrayList<Object>();
//    	Map<String, Object> params = builderParams(req, model);
//   		List<BillOmDivideDtl> result = billOmRecheckManager.queryRecheckBoxItem(params);
//   		BigDecimal totalItemQty = new BigDecimal(0);
//   		BigDecimal totalRealQty = new BigDecimal(0);
//   		int totalDiffQty = 0;
//   		for (BillOmDivideDtl b : result) {
//   			totalItemQty = totalItemQty.add(b.getItemQty()==null?new BigDecimal(0):b.getItemQty());
//   			totalRealQty = totalRealQty.add(b.getRealQty()==null?new BigDecimal(0):b.getRealQty());
//   			totalDiffQty += b.getDiffQty();
//		}
//   		
//   		Map<String, Object> footer = new HashMap<String, Object>();
//   		footer.put("itemNo", "汇总");
//   		footer.put("itemQty", totalItemQty);
//   		footer.put("realQty", totalRealQty);
//   		footer.put("diffQty", totalDiffQty);
//		footerList.add(footer);
//		
//		obj.put("rows", result);
//		obj.put("total", result.size());
//		obj.put("footer", footerList);
//   		return obj;
   		
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		Map<String, Object> params = builderParams(req, model);
    		int total = this.billOmRecheckManager.findRecheckBoxItemCount(params, authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillOmDivideDtl> list = this.billOmRecheckManager.findRecheckBoxItemByPage(page, sortColumn, sortOrder, params, authorityParams);
    		
    		
    		List<Object> footerList = new ArrayList<Object>();
    		BigDecimal totalItemQty = new BigDecimal(0);
    		BigDecimal totalRealQty = new BigDecimal(0);
    		BigDecimal totalPackageNoRecheckQty = new BigDecimal(0);
    		int totalDiffQty = 0;
    		for (BillOmDivideDtl b : list) {
    			totalItemQty = totalItemQty.add(b.getItemQty()==null?new BigDecimal(0):b.getItemQty());
    			totalRealQty = totalRealQty.add(b.getRealQty()==null?new BigDecimal(0):b.getRealQty());
    			totalDiffQty += b.getDiffQty();
    			totalPackageNoRecheckQty = totalPackageNoRecheckQty.add(b.getPackageNoRecheckQty() == null ? new BigDecimal(0) : b.getPackageNoRecheckQty());
    		}
    		
    		Map<String, Object> footer = new HashMap<String, Object>();
    		footer.put("ownerName", "小计");
    		footer.put("itemQty", totalItemQty);
    		footer.put("realQty", totalRealQty);
    		footer.put("diffQty", totalDiffQty);
    		footer.put("packageNoRecheckQty", totalPackageNoRecheckQty);
    		footerList.add(footer);

    		// 合计
    		Map<String, Object> sumFoot = new HashMap<String, Object>();
    		if (pageNo == 1) {
    			sumFoot = billOmRecheckManager.selectSumQty(params, authorityParams);
    			if (sumFoot == null) {
    				sumFoot = new SumUtilMap<String, Object>();
    				sumFoot.put("item_qty", 0);
    				sumFoot.put("real_qty", 0);
    				sumFoot.put("diff_qty", 0);
    			}
    			sumFoot.put("isselectsum", true);
    			sumFoot.put("owner_name", "合计");
    		} else {
    			sumFoot.put("ownerName", "合计");
    		}
    		footerList.add(sumFoot);
    	
    		obj.put("total", total);
    		obj.put("rows", list);
    		obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj; 
   	}
    
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody    
	@OperationVerify(OperationVerifyEnum.ADD)
	public Map<String, Object> saveMainInfo(HttpServletRequest req,BillOmRecheck billOmRecheck){
		String recheckNo = "";
		String msg = "保存失败!";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			BillOmDivideKey divideKey = new BillOmDivideKey();
			divideKey.setLocno(billOmRecheck.getLocno());
			divideKey.setDivideNo(billOmRecheck.getDivideNo());
			BillOmDivide billOmDivide = (BillOmDivide)billOmDivideManager.findById(divideKey);
			if(billOmDivide == null){
				msg = "分货单:"+billOmRecheck.getDivideNo()+"查询失败!";
				//return "{\"success\":\"" + false + "\",\"msg\":\"" + msg + "\"}";
				map.put("result", "fail");
				map.put("msg", msg);
				return map;
			}
			
			//验证是否存在相同建单状态分货单的复核单
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("locno", billOmRecheck.getLocno());
			params.put("divideNo", billOmDivide.getDivideNo());
			params.put("storeNo", billOmRecheck.getStoreNo());
			params.put("status", "10");
			List<BillOmRecheck> checkList=billOmRecheckManager.findByBiz(null, params);
			if(CommonUtil.hasValue(checkList)){
				msg = "分货单:"+billOmRecheck.getDivideNo()+"存在未审核的复核单,不能重复新建!";
				//return "{\"success\":\"" + false + "\",\"msg\":\"" + msg + "\"}";
				map.put("result", "fail");
				map.put("msg", msg);
				return map;
			}
			
			recheckNo = procCommonManager.procGetSheetNo(billOmRecheck.getLocno(), CNumPre.RECHECK_PRE);
			billOmRecheck.setRecheckNo(recheckNo);
			billOmRecheck.setStatus("10");//新建状态
			billOmRecheck.setCreatetm(new Date());
			billOmRecheck.setEdittm(new Date());
			billOmRecheck.setBusinessType(billOmDivide.getBusinessType());
			billOmRecheckManager.add(billOmRecheck);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			//return "{\"success\":\"" + false + "\",\"msg\":\"" + msg + "\"}";
			map.put("result", "fail");
			map.put("msg", msg);
			return map;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//return "{\"success\":\"" + false + "\",\"msg\":\"" + msg + "\"}";
			map.put("result", "fail");
			map.put("msg", msg);
			return map;
		}
		//return "{\"success\":\"" + true + "\",\"recheckNo\":\"" + recheckNo + "\"}";
		map.put("result", "success");
		map.put("recheckNo", recheckNo);
		map.put("msg", "保存成功");
		return map;
	}
	@RequestMapping(value = "/getLabelNo")
	@ResponseBody    
	public Map<String, String> getLabelNo(String locno,HttpServletRequest req){
		Map<String, String> result = null;
		try {
			CurrentUser currentUser=new CurrentUser(req);
			result = procCommonManager.procGetContainerNoBase(locno, "C", currentUser.getUserid().toString(), "F", "1", "1", "");
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
	@RequestMapping(value = "/validateBoxNo")
	@ResponseBody    
	public boolean validateBoxNo(String locno,String labelNo,HttpServletRequest req) {
		try {
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("labelNo", labelNo);
			params.put("locno", locno);
			int i = conLabelManager.findCount(params);
			if(i>0) {
				return true;
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
	
	@RequestMapping(value = "/packagebox")
	@ResponseBody    
	public ResponseEntity<Map<String, Object>> packagebox(HttpServletRequest req,String datas,String boxNo,String recheckNo,String locno,String divideNo,String storeNo,String recheckName) {
		Map<String,Object> flag=new HashMap<String,Object>();
		try {
			datas = URLDecoder.decode(datas,"UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			//分货单详细  刚复核的数据
			List<BillOmDivideDtl> dtlLst = new ArrayList<BillOmDivideDtl>();
			if (StringUtils.isNotEmpty(datas)) {
				List<Map> list = mapper.readValue(datas, new TypeReference<List<Map>>(){});
				dtlLst=convertListWithTypeReference(mapper,list);
			}
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			BillOmRecheck billOmRecheck = new BillOmRecheck();
			billOmRecheck.setLocno(locno);
			billOmRecheck.setRecheckNo(recheckNo);
			billOmRecheck.setDivideNo(divideNo);
			billOmRecheck.setStoreNo(storeNo);
			billOmRecheck.setCreator(user.getLoginName());
			billOmRecheck.setCreatorname(user.getUsername());
			billOmRecheck.setEditor(user.getLoginName());
			billOmRecheck.setEditorname(user.getUsername());
			billOmRecheckManager.packageBox(dtlLst,billOmRecheck,boxNo);
			
			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/packageBoxRf")
	@ResponseBody    
	public ResponseEntity<Map<String, Object>> packageboxRf(HttpServletRequest req,String datas,String boxNo,String recheckNo,String locno,String divideNo,String storeNo,String recheckName) {
		Map<String,Object> flag=new HashMap<String,Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			BillOmRecheck billOmRecheck = new BillOmRecheck();
			billOmRecheck.setLocno(locno);
			billOmRecheck.setRecheckNo(recheckNo);
			billOmRecheck.setDivideNo(divideNo);
			billOmRecheck.setStoreNo(storeNo);
			billOmRecheck.setCreator(user.getLoginName());
			billOmRecheckManager.packageBoxRf(billOmRecheck);
			flag.put("success", "true");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			flag.put("success", "false");
			flag.put("msg", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillOmDivideDtl> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillOmDivideDtl> tl=new ArrayList<BillOmDivideDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmDivideDtl type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillOmDivideDtl.class);
			tl.add(type);
		}
		return tl;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillOmRecheck> convertListWithTypeReferenceR(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillOmRecheck> tl=new ArrayList<BillOmRecheck>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmRecheck type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillOmRecheck.class);
			tl.add(type);
		}
		return tl;
	}
}