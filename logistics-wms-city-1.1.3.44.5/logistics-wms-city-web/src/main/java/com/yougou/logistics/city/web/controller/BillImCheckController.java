package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillImCheck;
import com.yougou.logistics.city.common.model.BillImCheckDtl;
import com.yougou.logistics.city.common.model.BillImReceipt;
import com.yougou.logistics.city.common.model.BillImReceiptDtl;
import com.yougou.logistics.city.common.model.Item;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CNumPre;
import com.yougou.logistics.city.manager.BillImCheckManager;
import com.yougou.logistics.city.manager.BillImReceiptDtlManager;
import com.yougou.logistics.city.manager.BillImReceiptManager;
import javax.servlet.http.HttpSession;
import com.yougou.logistics.city.manager.ProcCommonManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;

/**
 * 
 * 收货验收单controller
 * 
 * @author qin.dy
 * @date 2013-10-10 下午6:16:57
 * @version 0.1.0 
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_im_check")
@ModuleVerify("25070050")
public class BillImCheckController extends BaseCrudController<BillImCheck> {

	@Log
	private Logger log;
	@Resource
	private BillImCheckManager billImCheckManager;

	@Resource
	private BillImReceiptManager billImReceiptManager;

	@Resource
	private BillImReceiptDtlManager billImReceiptDtlManager;

	@Resource
	private ProcCommonManager procCommonManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billimcheck/", billImCheckManager);
	}

	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billImCheckManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillImCheck> list = this.billImCheckManager.findByPage(page, sortColumn, sortOrder, params, authorityParams, DataAccessRuleEnum.BRAND);
			
			
			// 返回小计列表
		    List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		    Map<String, Object> footerMap = new HashMap<String, Object>();
		    footerMap.put("checkNo", "小计");
		    footerList.add(footerMap);
			
			for (BillImCheck billImCheck : list) {
				//查询验收单明细做差异显示
//				BillImCheckDtl checkDtl = new BillImCheckDtl();
//				checkDtl.setLocno(billImCheck.getLocno());
//				checkDtl.setOwnerNo(billImCheck.getOwnerNo());
//				checkDtl.setCheckNo(billImCheck.getCheckNo());
//				int totalDiffQty = billImCheckDtlManager.selectCheckDtlDiffQty(checkDtl);
//				billImCheck.setTotalDiffQty(totalDiffQty);
				
				FooterUtil.setFooterMapByInt("totalPoQty", billImCheck.getTotalPoQty(), footerMap);
				FooterUtil.setFooterMapByInt("totalCheckQty", billImCheck.getTotalCheckQty(), footerMap);
				FooterUtil.setFooterMapByInt("totalDiffQty", billImCheck.getTotalDiffQty(), footerMap);
			}
//			  返回合计列表
		    Map<String, Object> sumFootMap = new HashMap<String, Object>();
		    if (pageNo == 1) {
				if(pageSize >= total){
					sumFootMap.putAll(footerMap);
				}else{
					Map<String, Object> sumFoot = billImCheckManager.findSumQty(params,authorityParams);
					if(sumFoot.get("TOTALPOQTY")!= null) {
						sumFootMap.put("totalPoQty", sumFoot.get("TOTALPOQTY"));
					} else {
						 sumFootMap.put("totalPoQty", 0);
					}
					if(sumFoot.get("TOTALCHECKQTY")!= null) {
						sumFootMap.put("totalCheckQty", sumFoot.get("TOTALCHECKQTY"));
					} else {
						sumFootMap.put("totalCheckQty", 0);
					}
					if(sumFoot.get("TOTALDIFFQTY")!= null) {
						sumFootMap.put("totalDiffQty", sumFoot.get("TOTALDIFFQTY"));
					} else {
						sumFootMap.put("totalDiffQty", 0);
					}
					
					sumFootMap.put("isselectsum", true);
				}
				sumFootMap.put("checkNo", "合计");
			    footerList.add(sumFootMap); 
			}
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
			obj.put("footer", footerList);
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/check")
	@OperationVerify(OperationVerifyEnum.VERIFY)
	@ResponseBody
	public Map<String, Object> check(BillImCheck check, HttpServletRequest req) throws JsonParseException,
			JsonMappingException, IOException, ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			//新增的行
			List<BillImCheck> checkList = new ArrayList<BillImCheck>();
			String dataList = StringUtils.isEmpty(req.getParameter("datas")) ? "" : req.getParameter("datas");
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(dataList)) {
				List<Map> list = mapper.readValue(dataList, new TypeReference<List<Map>>() {
				});
				checkList = convertListWithTypeReferenceCheck(mapper, list);
			}
			CurrentUser currentUser = new CurrentUser(req);
			billImCheckManager.check(checkList, currentUser.getLoginName(),currentUser.getUsername());
			obj.put("result", "success");
			return obj;
			//return billImCheckManager.check(checkList, currentUser.getLoginName());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "error");
			obj.put("msg", e.getMessage());
			return obj;
		}
	}
	@RequestMapping(value = "/directCheck")
	@ResponseBody
	public Object directCheck(HttpServletRequest req,String nos,String locno){
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		HttpSession session = req.getSession();
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			return billImCheckManager.directCheck(nos, locno, user, authorityParams);
		} catch (ManagerException e) {
			obj.put("status", "error");
			obj.put("msg", e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			obj.put("status", "error");
			obj.put("msg", "系统异常!");
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	@RequestMapping(value = "/saveMainInfo")
	@OperationVerify(OperationVerifyEnum.ADD)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveMainInfo(HttpServletRequest req, BillImCheck billImCheck) throws JsonParseException,
			JsonMappingException, IOException, ManagerException {
		String checkNo = "";
		Map<String, Object> result = new HashMap<String, Object>();
		
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		
		try {
			BillImReceipt billImReceipt = new BillImReceipt();
			billImReceipt.setReceiptNo(billImCheck.getsImportNo());
			billImReceipt.setLocno(billImCheck.getLocno());
			billImReceipt.setOwnerNo(billImCheck.getOwnerNo());
			BillImReceipt bir = billImReceiptManager.findById(billImReceipt);
			if(bir != null) {
				String receiptNo = bir.getReceiptNo();
				if(receiptNo != null) {
					Date checkEndDate = billImCheck.getCheckEndDate();
					Date recivedate = bir.getAudittm();
					int i = isDate(recivedate,checkEndDate);
					if(i == 1) {
						result.put("result", ResultEnums.FAIL.getResultMsg());
						result.put("msg", "验收日期不可小于收货单审核日期");
						return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
					}
					checkNo = procCommonManager.procGetSheetNo(billImCheck.getLocno(), CNumPre.IM_CHECK_NO_PRE);
					billImCheck.setCheckNo(checkNo);
					billImCheck.setCheckType("1");
					billImCheck.setStatus(BigDecimal.valueOf(10));
					Date date = new Date();
					billImCheck.setCreatetm(date);
                    billImCheck.setEdittm(date);
//					//新增的行
//					String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
//					insertedList = URLDecoder.decode(insertedList, "UTF-8");
//					ObjectMapper mapper = new ObjectMapper();
//					List<BillImCheckDtl> insertItemLst = new ArrayList<BillImCheckDtl>();
//					if (StringUtils.isNotEmpty(insertedList)) {
//						List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
//						});
//						insertItemLst = convertListWithTypeReference2(mapper, list);
//					}
//					//修改的行
//					String updatedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
//					updatedList = URLDecoder.decode(updatedList, "UTF-8");
//					List<BillImCheckDtl> updatedItemLst = new ArrayList<BillImCheckDtl>();
//					if (StringUtils.isNotEmpty(updatedList)) {
//						List<Map> list = mapper.readValue(updatedList, new TypeReference<List<Map>>() {
//						});
//						updatedItemLst = convertListWithTypeReference2(mapper, list);
//					}
//					//删除的行
//					String deletedList = StringUtils.isEmpty(req.getParameter("del")) ? "" : req.getParameter("del");
//					deletedList = URLDecoder.decode(deletedList, "UTF-8");
//					List<BillImCheckDtl> deletedItemLst = new ArrayList<BillImCheckDtl>();
//					if (StringUtils.isNotEmpty(deletedList)) {
//						List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
//						});
//						deletedItemLst = convertListWithTypeReference2(mapper, list);
//					}

					billImCheckManager.addMain(billImCheck,authorityParams);
					result.put("result", ResultEnums.SUCCESS.getResultMsg());
					result.put("checkNo", checkNo);
				} else {
					result.put("result", ResultEnums.FAIL.getResultMsg());
					result.put("msg", "收货单检查出错");
				}
				
			} else {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "收货单检查出错");
			}
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		}
	}

	private int isDate(Date dt1, Date dt2){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
			
			Date time1= sdf.parse(sdf.format(dt1));
			Date time2= sdf.parse(sdf.format(dt2));
//			System.out.println(time1);
//			System.out.println(time2);
			if (time1.equals(time2)) {
				return 0;
			} else if (time1.before(time2)) {
				return -1;
			} else {
				return 1;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 1;
	    }
	}

	@RequestMapping(value = "/editMainInfo")
	@OperationVerify(OperationVerifyEnum.MODIFY)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> editMainInfo(HttpServletRequest req, BillImCheck billImCheck) throws JsonParseException,
			JsonMappingException, IOException, ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			obj = billImCheckManager.editMainInfo(billImCheck, user.getLoginName());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			obj.put("result", "false");
			obj.put("msg", e.getMessage());
		}
	   return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	/*   @RequestMapping(value = "/save_detail")
	=======
	@Resource
	private BillImCheckManager billImCheckManager;
	
	@Resource
	private ProcCommonManager procCommonManager;
	
	@Override
	public CrudInfo init() {
	    return new CrudInfo("billimcheck/",billImCheckManager);
	}  
	@RequestMapping(value = "/check")
	@ResponseBody    
	public String check(String ids,HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		try {
			CurrentUser currentUser=new CurrentUser(req);
			billImCheckManager.check(ids,currentUser.getLoginName());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
		return "success";
	}
	@RequestMapping(value = "/saveMainInfo")
	@ResponseBody    
	public String saveMainInfo(HttpServletRequest req,BillImCheck billImCheck) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		String checkNo = "";
		try {
			checkNo = procCommonManager.procGetSheetNo(billImCheck.getLocno(), CNumPre.IM_CHECK_NO_PRE);
			billImCheck.setCheckNo(checkNo);
			billImCheck.setStatus(BigDecimal.valueOf(11));
			billImCheckManager.add(billImCheck);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "{\"success\":\"" + false + "\"}";
		}
		return "{\"success\":\"" + true + "\",\"checkNo\":\"" + checkNo + "\"}";
	}
	/*   @RequestMapping(value = "/save_detail")
	>>>>>>> .r852
	public ResponseEntity<BillUmUntread> saveDetail(BillImCheck billImCheck,HttpServletRequest req) throws ManagerException, JsonParseException, JsonMappingException, IOException {
		
		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
		insertedList =URLDecoder.decode(insertedList,"UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		List<Item> itemLst = new ArrayList<Item>();
		if (StringUtils.isNotEmpty(insertedList)) {
			List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
			itemLst=convertListWithTypeReference(mapper,list);
		}
		billImCheckManager.add(billImCheck, itemLst);
		return new ResponseEntity<BillUmUntread>(new BillUmUntread(), HttpStatus.OK);
	}*/
	@RequestMapping(value = "save_detail")
	@ResponseBody
	public Map<String, Object> save_detail(BillImCheck billImCheck, HttpServletRequest req) throws ManagerException,
			JsonParseException, JsonMappingException, IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//新增的行
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			insertedList = URLDecoder.decode(insertedList, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<BillImCheckDtl> insertItemLst = new ArrayList<BillImCheckDtl>();
			if (StringUtils.isNotEmpty(insertedList)) {
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});
				insertItemLst = convertListWithTypeReference2(mapper, list);
			}
			//修改的行
			String updatedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
			updatedList = URLDecoder.decode(updatedList, "UTF-8");
			List<BillImCheckDtl> updatedItemLst = new ArrayList<BillImCheckDtl>();
			if (StringUtils.isNotEmpty(updatedList)) {
				List<Map> list = mapper.readValue(updatedList, new TypeReference<List<Map>>() {
				});
				updatedItemLst = convertListWithTypeReference2(mapper, list);
			}
			//删除的行
			String deletedList = StringUtils.isEmpty(req.getParameter("del")) ? "" : req.getParameter("del");
			deletedList = URLDecoder.decode(deletedList, "UTF-8");
			List<BillImCheckDtl> deletedItemLst = new ArrayList<BillImCheckDtl>();
			if (StringUtils.isNotEmpty(deletedList)) {
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
				});
				deletedItemLst = convertListWithTypeReference2(mapper, list);
			}
			billImCheck.setEdittm(new Date());
			resultMap = billImCheckManager.update(billImCheck, insertItemLst, updatedItemLst, deletedItemLst);
			resultMap.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put("result", "fail");
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Item> convertListWithTypeReference(ObjectMapper mapper, List<Map> list) throws JsonParseException,
			JsonMappingException, JsonGenerationException, IOException {
		List<Item> tl = new ArrayList<Item>(list.size());
		for (int i = 0; i < list.size(); i++) {
			Item type = mapper.readValue(mapper.writeValueAsString(list.get(i)), Item.class);
			tl.add(type);
		}
		return tl;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillImCheckDtl> convertListWithTypeReference2(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<BillImCheckDtl> tl = new ArrayList<BillImCheckDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillImCheckDtl type = mapper.readValue(mapper.writeValueAsString(list.get(i)), BillImCheckDtl.class);
			tl.add(type);
		}
		return tl;
	}

	@RequestMapping(value = "/delete_records")
	@OperationVerify(OperationVerifyEnum.REMOVE)
	@ResponseBody
	public Map<String,Object> deleteRecords(String ids) throws ManagerException {
		Map<String, Object> gmap  = new HashMap<String, Object>();
		try {
			billImCheckManager.deleteBatch(ids);
			gmap.put("result", "success");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			gmap.put("result", "fail");
			gmap.put("msg", e.getMessage());
		}
		return gmap;
	}

	@RequestMapping(value = "/findNoCheckedReciptNo")
	@ResponseBody
	public Map<String, Object> findNoCheckedReciptNo(HttpServletRequest req, Model model) {
		Map obj = new HashMap();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map params = builderParams(req, model);
			int total = billImReceiptManager.findReciptNoCheckedCount(null, params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List list = billImReceiptManager.findReciptNoChecked(page, params, authorityParams);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/findCheckForDirect")
	@ResponseBody
	public Map<String, Object> findCheckForDirect(HttpServletRequest req, Model model) {
		Map obj = new HashMap();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map params = builderParams(req, model);
			int total = billImCheckManager.findCheckForDirectCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List list = billImCheckManager.selectByPageForDirect(page, params, authorityParams);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/selectAllDetailByReciptNo")
	@ResponseBody
	public Map<String, Object> selectAllDetailByReciptNo(HttpServletRequest req, BillImReceiptDtl dtl, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List<BillImReceiptDtl> list = billImReceiptDtlManager.findAllDetailByReciptNo(dtl, authorityParams);
			for (BillImReceiptDtl receiptDtl : list) {
				if (receiptDtl.getPoQty() != null && receiptDtl.getCheckQty() != null) {
					receiptDtl.setPoQty(new BigDecimal(receiptDtl.getPoQty().intValue()
							- receiptDtl.getCheckQty().intValue()));
				}
			}
			obj.put("list", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@RequestMapping(value = "/findItemNotInReceipt")
	@ResponseBody
	public Map<String, Object> findItemNotInReceipt(HttpServletRequest req, BillImReceiptDtl dtl, Item item, Model model) {
		Map obj = new HashMap();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));
			Map<String, Object> paramsAll = builderParams(req, model);
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(paramsAll);
			String itemNo = "";
			if(paramsAll.get("itemNo") != null) {
				itemNo = paramsAll.get("itemNo").toString().toUpperCase();
				params.put("itemNo", itemNo);
			}
			if(item.getItemNo() != null) {
				item.setItemNo(item.getItemNo().toUpperCase());
			}
			String barcode = "";
			if(paramsAll.get("barcode") != null) {
				barcode = paramsAll.get("barcode").toString().toUpperCase();
				params.put("barcode", barcode);
			}
			if(item.getBarcode() != null) {
				item.setBarcode(item.getBarcode().toUpperCase());
			}
			
			int total = billImReceiptDtlManager.findItemNotInReceiptCount(item, dtl, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, total);
			List list = billImReceiptDtlManager.findItemNotInReceipt(item, dtl, page, authorityParams);
			obj.put("total", Integer.valueOf(total));
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	@RequestMapping(value = "/findDtlByItemNoAndSizeNo")
	@ResponseBody
	public Map<String, Object> findDtlByItemNoAndSizeNo(HttpServletRequest req, BillImReceiptDtl dtl, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			obj.put("count", billImReceiptDtlManager.findDtlByItemNoAndSizeNo(dtl));
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * 转换成泛型列表
	 * @param mapper
	 * @param list
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillImCheck> convertListWithTypeReferenceCheck(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<BillImCheck> entityClass = (Class<BillImCheck>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<BillImCheck> tl = new ArrayList<BillImCheck>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillImCheck type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}
}