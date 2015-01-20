package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.InitpowerInterceptors;
import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillOmOutstockDirect;
import com.yougou.logistics.city.common.model.CmDefarea;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.BillOmOutstockDirectForQuery;
import com.yougou.logistics.city.manager.BillOmOutstockDirectManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 拣货任务分派
 * @author zuo.sw
 * @date  2013-10-09 11:09:10
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
@RequestMapping("/outstockdirect")
@ModuleVerify("25080030")
public class BillOmOutstockDirectController extends BaseCrudController<BillOmOutstockDirect> {

	@Log
	private Logger log;

	@Resource
	private BillOmOutstockDirectManager billOmOutstockDirectManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("outstockdirect/", billOmOutstockDirectManager);
	}

	/**
	 * 移库发单
	 * @return
	 */
	@RequestMapping(value = "/toListPlanSend")
	@InitpowerInterceptors
	public String toListPlanSend() {
		return "outstockdirect/listPlanSend";
	}

	/**
	 * 根据出库单类型查询波次号和批次列表
	 * @param expType
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryLocateNoByExpType")
	@ResponseBody
	public List<BillOmOutstockDirect> queryLocateNoByExpType(HttpServletRequest req,String expType, String locno){
		List<BillOmOutstockDirect> lstObjs = new ArrayList<BillOmOutstockDirect>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			lstObjs = billOmOutstockDirectManager.queryLocateNoByExpType(expType, locno, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return lstObjs;
	}

	/**
     * 根据出库单类型,波次号，出库单号，订单号查询波次号和批次列表
     * @param expType
     * @return
     * @throws ManagerException
     */
	@RequestMapping(value = "/queryLocateNoByMore")
	@ResponseBody
	public  List<BillOmOutstockDirect> queryLocateNoByMore(HttpServletRequest req,String expType,String locno,String locateNo1,String expNo,String poNo,String brandNo,String sysNo){
		List<BillOmOutstockDirect> lstObjs =  new  ArrayList<BillOmOutstockDirect>(0);
		try{
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
			lstObjs = billOmOutstockDirectManager.queryLocateNoByMore(expType, user.getLocNo(), locateNo1, expNo, poNo, brandNo, sysNo, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return  lstObjs;
	}
	
	
	
	
	/**
	 *  根据出库单类型，波次号，批次查询作业类型
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryOperateTypeByParam")
	@ResponseBody
	public List<BillOmOutstockDirect> queryOperateTypeByParam(HttpServletRequest req,BillOmOutstockDirectForQuery vo) throws ManagerException {
		List<BillOmOutstockDirect> lstObjs = new ArrayList<BillOmOutstockDirect>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			lstObjs = billOmOutstockDirectManager.queryOperateTypeByParam(vo, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return lstObjs;
	}

	/**
	 * 根据出库单类型，波次号，批次，作业类型查询库区信息
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryAreaInfoByParam")
	@ResponseBody
	public List<BillOmOutstockDirect> queryAreaInfoByParam(HttpServletRequest req,BillOmOutstockDirectForQuery vo) throws ManagerException {
		List<BillOmOutstockDirect> lstObjs = new ArrayList<BillOmOutstockDirect>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			lstObjs = billOmOutstockDirectManager.queryAreaInfoByParam(vo, authorityParams, sortColumn, sortOrder);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return lstObjs;
	}

	/**
	 * 根据出库单类型，波次号，批次，作业类型查询客户信息
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryStoreInfoByParam")
	@ResponseBody
	public List<BillOmOutstockDirect> queryStoreInfoByParam(HttpServletRequest req,BillOmOutstockDirectForQuery vo) throws ManagerException {
		List<BillOmOutstockDirect> lstObjs = new ArrayList<BillOmOutstockDirect>(0);
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			lstObjs = billOmOutstockDirectManager.queryStoreInfoByParam(vo, authorityParams, sortColumn, sortOrder);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return lstObjs;
	}

	/**
	 * 根据出库单类型，波次号，批次,作业类型 ，客户查询拣货的商品信息
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryOutstockDirectByStore")
	@ResponseBody
	public  Map<String, Object> queryOutstockDirectByStore(HttpServletRequest req,BillOmOutstockDirectForQuery vo) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//List<BillOmOutstockDirect> result = billOmOutstockDirectManager.queryOutstockDirectByStore(vo);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			int total = this.billOmOutstockDirectManager.findOutstockDirectByStoreCount(vo, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDirect> result = this.billOmOutstockDirectManager.findOutstockDirectByStoreByPage(page, sortColumn,sortOrder, vo, authorityParams);
			
			//返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("sCellNo", "小计");
			footerList.add(footerMap);
			
			if(CommonUtil.hasValue(result)){
				for (BillOmOutstockDirect temp : result) {
					try {
						FooterUtil.setFooterMap("itemQty", temp.getItemQty()==null?new BigDecimal(0):temp.getItemQty(), footerMap);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}else{
				try {
					FooterUtil.setFooterMap("itemQty", new BigDecimal(0), footerMap);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			
			
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmOutstockDirectManager.selectOutstockDirectByStoreSumQty(vo, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("s_cell_no", "合计");
			} else {
				sumFoot.put("sCellNo", "合计");
			}
			footerList.add(sumFoot);
			
			obj.put("total", total);
			obj.put("rows", result);
			obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
   	}
//	public List<BillOmOutstockDirect> queryOutstockDirectByStore(BillOmOutstockDirectForQuery vo) throws ManagerException {
//		List<BillOmOutstockDirect> lstObjs = new ArrayList<BillOmOutstockDirect>(0);
//		try {
//			lstObjs = billOmOutstockDirectManager.queryOutstockDirectByStore(vo);
//		} catch (Exception e) {
//			log.error("=========== 根据出库单类型，波次号，批次,作业类型 ，客户查询拣货的商品信息时异常：" + e.getMessage(), e);
//		}
//		return lstObjs;
//	}

	/**
	 * 根据出库单类型，波次号，批次,作业类型 ，库区  查询拣货的商品信息
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryOutstockDirectByArea")
	@ResponseBody
	public  Map<String, Object> queryOutstockDirectByArea(HttpServletRequest req,BillOmOutstockDirectForQuery vo) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	try {
    		//List<BillOmOutstockDirect> result = billOmOutstockDirectManager.queryOutstockDirectByArea(vo);
    		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
    		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
    		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
    		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
    		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
    		int total = this.billOmOutstockDirectManager.findOutstockDirectByAreaCount(vo, authorityParams);
    		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
    		List<BillOmOutstockDirect> result = this.billOmOutstockDirectManager.findOutstockDirectByAreaByPage(page, sortColumn,sortOrder, vo, authorityParams);
       		
       		//返回汇总列表
       		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
       		Map<String, Object> footerMap = new HashMap<String, Object>();
       		footerMap.put("sCellNo", "汇总");
       		footerList.add(footerMap);
       		if(CommonUtil.hasValue(result)){
       			for (BillOmOutstockDirect temp : result) {
       	   			try {
       	   				FooterUtil.setFooterMap("itemQty", temp.getItemQty()==null?new BigDecimal(0):temp.getItemQty(), footerMap);
       	   			} catch (Exception e) {
       	   				log.error(e.getMessage(), e);
       	   			}
       	   		}
       		}else{
    			try {
    				FooterUtil.setFooterMap("itemQty", new BigDecimal(0), footerMap);
    			} catch (Exception e) {
    				log.error(e.getMessage(), e);
    			}
       		}
       		
    		
       		// 合计
       		Map<String, Object> sumFoot = new HashMap<String, Object>();
       		if (pageNo == 1) {
       			sumFoot = billOmOutstockDirectManager.selectOutstockDirectByAreaSumQty(vo, authorityParams);
       			if (sumFoot == null) {
       				sumFoot = new SumUtilMap<String, Object>();
       				sumFoot.put("item_qty", 0);
       			}
       			sumFoot.put("isselectsum", true);
       			sumFoot.put("s_cell_no", "合计");
       		} else {
       			sumFoot.put("sCellNo", "合计");
       		}
       		footerList.add(sumFoot);
       		
       		obj.put("total", total);
    		obj.put("rows", result);
    		obj.put("footer", footerList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
   		return obj;
   	}
//	public List<BillOmOutstockDirect> queryOutstockDirectByArea(BillOmOutstockDirectForQuery vo)
//			throws ManagerException {
//		List<BillOmOutstockDirect> lstObjs = new ArrayList<BillOmOutstockDirect>(0);
//		try {
//			lstObjs = billOmOutstockDirectManager.queryOutstockDirectByArea(vo);
//		} catch (Exception e) {
//			log.error("=========== 根据出库单类型，波次号，批次,作业类型 ，库区  查询拣货的商品信息时异常：" + e.getMessage(), e);
//		}
//		return lstObjs;
//	}

	/**
	 * 根据角色ID查询对应下的所有用户信息
	 * @param roleId
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/getUserListByRoleId")
	@ResponseBody
	public List<SystemUser> getUserListByRoleId(Integer roleId, String locno) {
		List<SystemUser> lstObjs = new ArrayList<SystemUser>(0);
		try {
			SystemUser user = new SystemUser();
			user.setLocNo(locno);
			user.setRoleId(roleId);
			lstObjs = billOmOutstockDirectManager.getUserListByRoleId(user);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return lstObjs;
	}

	/**
	 * 拣货任务分派发单
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/procOmOutStockDirect")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public String procOmOutStockDirect(BillOmOutstockDirectForQuery vo, HttpServletRequest req) {
		String resultMsg = "";
		try {
			List<BillOmOutstockDirect> dlist = new ArrayList<BillOmOutstockDirect>();
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>() {
				});
				dlist = convertListWithTypeReference(mapper, list);
			}
			vo.setDlist(dlist);
			
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			vo.setCreator(user.getLoginName());
			billOmOutstockDirectManager.procOmOutStockDirect(vo);
			resultMsg = "true";
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			resultMsg = e.getMessage();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMsg = e.getMessage();
		}
		return resultMsg;
	}

	/**
	 * 移库计划发单
	 * @param vo
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/procOmPlanOutStockDirect")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public String procOmPlanOutStockDirect(BillOmOutstockDirectForQuery vo, HttpServletRequest req) {
		String resultMsg = "";
		try {
			List<BillOmOutstockDirect> dlist = new ArrayList<BillOmOutstockDirect>();
			String datasList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(datasList)) {
				List<Map> list = mapper.readValue(datasList, new TypeReference<List<Map>>() {
				});
				dlist = convertListWithTypeReference(mapper, list);
			}
			vo.setDlist(dlist);
			billOmOutstockDirectManager.procOmPlanOutStockDirect(vo);
			resultMsg = "true";
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			resultMsg = e.getMessage();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMsg = e.getMessage();
		}
		return resultMsg;
	}

	/**
	 * 查询移库发单
	 * @param req
	 * @param billOmOutstockDirect
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/findOutstockDirectByPage.json")
	@ResponseBody
	public Map<String, Object> findConContentByPage(HttpServletRequest req, BillOmOutstockDirect billOmOutstockDirect) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			int total = billOmOutstockDirectManager.findOutstockDirectCount(billOmOutstockDirect, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmOutstockDirect> list = billOmOutstockDirectManager.findOutstockDirectByPage(page,
					billOmOutstockDirect, authorityParams);
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillOmOutstockDirect temp : list) {
				this.setFooterMap("itemTotalQty", new BigDecimal(temp.getItemTotalQty()), footerMap);
			}
			// 合计
						Map<String, Object> sumFoot1 = new HashMap<String, Object>();
						if (pageNo == 1) {
							sumFoot1 = billOmOutstockDirectManager.selectSumQty(billOmOutstockDirect, authorityParams);
							if (sumFoot1 != null) {
								Map<String, Object> sumFoot2 = new HashMap<String, Object>();
								sumFoot2.put("itemTotalQty", sumFoot1.get("itemtotalqty"));
								sumFoot2.put("isselectsum", true);
								sumFoot1 = sumFoot2;
							}
						}
						sumFoot1.put("itemNo", "合计");
						footerList.add(sumFoot1);
						
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

	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}
	
	@RequestMapping(value = "/findHmPlanCmDefareaByPage")
	@ResponseBody
	public Map<String, Object> findHmPlanCmDefareaByPage(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmOutstockDirectManager.findHmPlanCmDefareaCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<CmDefarea> list = this.billOmOutstockDirectManager.findHmPlanCmDefareaByPage(page, sortColumn, sortOrder,
					params, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillOmOutstockDirect> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillOmOutstockDirect> tl = new ArrayList<BillOmOutstockDirect>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillOmOutstockDirect type = mapper.readValue(mapper.writeValueAsString(list.get(i)),
					BillOmOutstockDirect.class);
			tl.add(type);
		}
		return tl;
	}

}