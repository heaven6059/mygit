package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.BillOmRecheckJoinDto;
import com.yougou.logistics.city.common.model.BillOmRecheck;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmRecheckJoinDtlManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 分货交接controller
 * 
 * @author luohl
 * @date 2013-10-11 上午11:22:37
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_recheck_join")
@ModuleVerify("25040030")
public class BillOmRecheckJoinController extends BaseCrudController<BillOmRecheck> {
	
	@Log
    private Logger log;
	
	@Resource
	private BillOmRecheckJoinDtlManager billOmRecheckJoinDtlManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billomrecheckjoin/", billOmRecheckJoinDtlManager);
	}

	@RequestMapping(value = "/findRecheckNo")
	@ResponseBody
	public Map<String, Object> findRecheckNo(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = billOmRecheckJoinDtlManager.findRecheckNoCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<?> list = billOmRecheckJoinDtlManager.findRecheckNo(page, params,authorityParams);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/findNoReCheck")
	@ResponseBody
	public Map<String, Object> findNoReCheck(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map<String, Object> p = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(p);
			Object divideObj = params.get("divideNoStr");
			if(divideObj != null){
				String divideNoStr = divideObj.toString();
				if(!StringUtils.isEmpty(divideNoStr)){
					params.put("divideNoList", divideNoStr.split(","));
				}
			}
			int total = billOmRecheckJoinDtlManager.findNoReCheckCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<?> list = billOmRecheckJoinDtlManager.findNoReCheck(params, page,authorityParams);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
			
			//汇总
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalItemQty = new BigDecimal(0);
			Iterator it = list.iterator();
			for(Object object:list){
				Map<String,Object> map = (Map<String,Object>)object;
				BigDecimal bigDecimal = (BigDecimal)map.get("REALQTYCOUNT");
				totalItemQty = totalItemQty.add(bigDecimal==null?new BigDecimal(0):bigDecimal);
			}
			
			footer.put("STATUS", "小计");
			footer.put("REALQTYCOUNT", totalItemQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			Map<String, Object> sumFoot2 = new HashMap<String, Object>();
			if (pageNo == 1) {
			    sumFoot = billOmRecheckJoinDtlManager.selectNoReCheckSumQty(params,authorityParams);
			    if (sumFoot == null) {
					sumFoot = new HashMap<String, Object>();
					sumFoot.put("REALQTYCOUNT", 0);
			    }
			    
			    sumFoot2.put("isselectsum", true);
			    sumFoot2.put("REALQTYCOUNT", sumFoot.get("realqtycount")==null?"0":sumFoot.get("realqtycount"));
			    sumFoot2.put("STATUS", "合计");
			    
			} else {
			    sumFoot2.put("STATUS", "合计");
			}
			footerList.add(sumFoot2);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/findReChecked")
	@ResponseBody
	public Map<String, Object> findReCheck(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			Map<String, Object> params = builderParams(req, model);
			int total = billOmRecheckJoinDtlManager.findReCheckedCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List<BillOmRecheckJoinDto> list = billOmRecheckJoinDtlManager.findReChecked(params, page,authorityParams);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
			
			//汇总
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalRealQty = new BigDecimal(0);
			for(BillOmRecheckJoinDto dto:list){
				totalRealQty = totalRealQty.add(new BigDecimal(dto.getRealQty()==null?"0":dto.getRealQty()));
			}
							
			footer.put("itemNo", "小计");
			footer.put("realQty", totalRealQty);
			footerList.add(footer);
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmRecheckJoinDtlManager.selectNoReCheckedSumQty(params,authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("real_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_No", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 审核交接单
	 * 1、更新箱号对应的状态
	 * 2、查询箱号对应的单号 的所有记录，然后统计状态为已交接的记录，对比得出是否要更新主表记录
	 * 
	 */
	@RequestMapping(value = "/sendReCheck")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.VERIFY)
	public ResponseEntity<Map<String, Object>> sendReCheck(String rowIdstr, Model model, HttpServletRequest req){
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			
			List<BillOmRecheck> recheckList = new ArrayList<BillOmRecheck>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				recheckList = convertListWithTypeReference(mapper, list);
			}
			
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			flag = billOmRecheckJoinDtlManager.sendReCheck(rowIdstr, user.getLocNo(),user.getLoginName(), recheckList);
			//billOmRecheckJoinDtlManager.queryReCheck(rowIdstr, user.getLocNo(),user.getLoginName());
			flag.put("flag", "success");
			flag.put("msg", "交接成功！");
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (ManagerException e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			flag.put("flag", "warn");
			flag.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillOmRecheck> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		List<BillOmRecheck> tl=new ArrayList<BillOmRecheck>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmRecheck type=mapper.readValue(mapper.writeValueAsString(list.get(i)),BillOmRecheck.class);
			tl.add(type);
		}
		return tl;
	}
}