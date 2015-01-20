package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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

import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmDeliver;
import com.yougou.logistics.city.common.model.BillOmDeliverDtl;
import com.yougou.logistics.city.common.model.BillOmDeliverDtlSizeDto;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.manager.BillOmDeliverDtlManager;
import com.yougou.logistics.city.manager.BillOmDeliverManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 
 * 装车单详情controller
 * 
 * @author qin.dy
 * @date 2013-10-12 下午3:29:00
 * @version 0.1.0    
 * @copyright yougou.com
 */
@Controller
@RequestMapping("/bill_om_deliver_dtl")
public class BillOmDeliverDtlController<ModelType> extends BaseCrudController<BillOmDeliverDtl> {

	@Log
	private Logger log;

	@Resource
	private BillOmDeliverDtlManager billOmDeliverDtlManager;

	
	@Resource
	private BillOmDeliverManager billOmDeliverManager;
	
	@Override
	public CrudInfo init() {
		return new CrudInfo("billomdeliverdtl/", billOmDeliverDtlManager);
	}

	@RequestMapping(value = "/get_biz")
	@ResponseBody
	@Override
	public List<BillOmDeliverDtl> getBiz(BillOmDeliverDtl modelType, HttpServletRequest req, Model model) {
		List<BillOmDeliverDtl> result = null;
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String, Object> params = builderParams(req, model);
			result = this.billOmDeliverDtlManager.boxDtlByParams(modelType, params, authorityParams);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 查看明细列表
	 */
	@RequestMapping(value = "/findDeliverDtlBoxByPage")
	@ResponseBody
	public Map<String, Object> findDeliverDtlBoxByPage(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmDeliverDtlManager.findDeliverDtlBoxCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.findDeliverDtlBoxByPage(page, sortColumn,sortOrder, params, authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 查看明细列表
	 */
	@RequestMapping(value = "/viewDtl")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) {
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
			int total = this.billOmDeliverDtlManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			obj.put("total", total);
			obj.put("rows", list);

			// 汇总
			List<Object> footerList = new ArrayList<Object>();
			Map<String, Object> footer = new HashMap<String, Object>();
			BigDecimal totalQty = new BigDecimal(0);
			for (BillOmDeliverDtl dtl : list) {
				totalQty = totalQty.add(dtl.getQty() == null ? new BigDecimal(0) : dtl.getQty());
			}

			footer.put("boxNo", "小计");
			footer.put("qty", totalQty);
			footerList.add(footer);

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmDeliverDtlManager.selectSumQty(params,authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("box_no", "合计");
			} else {
				sumFoot.put("boxNo", "合计");
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
	 * 选箱
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/boxSelectQuery")
	@ResponseBody
	public Map<String, Object> boxSelectQuery(HttpServletRequest req, Model model) {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.billOmDeliverDtlManager.boxSelectCount(params,authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.boxSelectQuery(page, sortColumn, sortOrder,
					params,authorityParams);
			obj.put("total", total);
			obj.put("rows", list);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 增删改
	 * @param billOmDeliverDtl
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveDtlInfo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveDtlInfo(BillOmDeliverDtl billOmDeliverDtl, HttpServletRequest req) {
		Map<String, Object> flag = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
			ObjectMapper mapper = null;
			String locno = req.getParameter("locno");
			String deliverNo = req.getParameter("deliverNo");
			String ownerNo = req.getParameter("ownerNo");
			String transFlag = "20";
			//新增的行
			String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
			//insertedList = URLDecoder.decode(insertedList, "UTF-8");
			if (insertedList != null && StringUtils.isNotBlank(insertedList)) {
				mapper = new ObjectMapper();
				List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
				});

				List<ModelType> oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.INSERTED, oList);
				//		    	if(list.size() > 0) {
				//		    		billOmDeliverDtlManager.addDtlInfo(list, locno, deliverNo, ownerNo);
				//		    	}
			}
			//删除的行
			String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
			//deletedList = URLDecoder.decode(deletedList, "UTF-8");
			if (deletedList != null && StringUtils.isNotBlank(deletedList)) {
				mapper = new ObjectMapper();
				List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
				});

				List<ModelType> oList = convertListWithTypeReference(mapper, list);
				params.put(CommonOperatorEnum.DELETED, oList);
			}
			//获取登陆用户
			HttpSession session = req.getSession();
			SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
			//插入、删除数据
			billOmDeliverDtlManager.addBillOmDeliverDtl(locno, deliverNo, ownerNo, params,
					user, transFlag, authorityParams);
			flag.put("success", "true");
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

	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper, List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<ModelType> tl = new ArrayList<ModelType>(list.size());
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		for (int i = 0; i < list.size(); i++) {
			ModelType type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}

	/**
	 * 客户
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/selectBoxStore")
	@ResponseBody
	public Map<String, Object> selectBoxStore(HttpServletRequest req, Model model) {
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
			int total = this.billOmDeliverDtlManager.selectBoxCount(params, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDeliverDtl> list = this.billOmDeliverDtlManager.selectBoxStore(page, sortColumn, sortOrder,
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
	
	@RequestMapping(value = "/printDetail4SizeHorizontal")
	@ResponseBody
	public Map<String, Object> printDetail4SizeHorizontal(HttpServletRequest req, HttpSession session, String keys,
			SizeInfo info, Model model) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {
			if (StringUtils.isEmpty(keys)) {
				result.put("result", ResultEnums.FAIL.getResultMsg());
				result.put("msg", "参数错误");
				return result;
			}
			String[] keysArray = keys.split(",");
			for (String str : keysArray) {
				String[] strs = str.split("\\|");
				String locno = strs[0];
				String deliverNo = strs[1];
				
				BillOmDeliver billOmDeliver = new BillOmDeliver();
				billOmDeliver.setLocno(locno);
				billOmDeliver.setDeliverNo(deliverNo);
				billOmDeliver = billOmDeliverManager.findById(billOmDeliver);
				List<BillOmDeliverDtlSizeDto> list = billOmDeliverDtlManager.findDtl4SizeHorizontal(deliverNo);
				if(CommonUtil.hasValue(list)){
					//同主单明细按storeNo分组
					Map<String, List<BillOmDeliverDtlSizeDto>> gMap = groupByStoreNo(list);
					for(Entry<String, List<BillOmDeliverDtlSizeDto>> gm:gMap.entrySet()){
						/*********************增加测试数据S*********************/
						/*BillSmWasteDtlSizeDto first = list.get(0);
						for(int idx=0;idx<200;idx++){
							BillSmWasteDtlSizeDto newDtl = new BillSmWasteDtlSizeDto();
							newDtl.setItemNo(first.getItemNo()+idx);
							newDtl.setSizeCode(first.getSizeCode());
							newDtl.setSizeKind(first.getSizeKind());
							newDtl.setSizeNo(first.getSizeNo());
							newDtl.setSysNo(first.getSysNo());
							newDtl.setWasteQty(first.getWasteQty());
							list.add(newDtl);
						}*/
						/*********************增加测试数据E*********************/
						Map<String, Object> main = new HashMap<String, Object>();
						Map<String, BillOmDeliverDtlSizeDto> itemRowDto = new HashMap<String, BillOmDeliverDtlSizeDto>();
						List<BillOmDeliverDtlSizeDto> rows = new ArrayList<BillOmDeliverDtlSizeDto>();
						List<List<String>> sizeList = new ArrayList<List<String>>();
						Map<String, BigDecimal> sizeCodeQtyMap = null;
						BillOmDeliverDtlSizeDto temp;
						BigDecimal total = new BigDecimal(0);
						
						Map<String, Map<String, String>> sizeHead = new TreeMap<String, Map<String,String>>();
						Map<String, String> sizeRow = null;
						int sizeColNum = 0;
						String storeName = "";
						for(BillOmDeliverDtlSizeDto d:gm.getValue()){
							String itemNo = d.getItemNo();
							storeName = d.getStoreName();
							if((temp = itemRowDto.get(itemNo)) != null){
								sizeCodeQtyMap = temp.getSizeCodeQtyMap();
								temp.setTotalQty(temp.getTotalQty().add(d.getQty()));
								if(sizeCodeQtyMap.get(d.getSizeCode()) != null){
									sizeCodeQtyMap.put(d.getSizeCode(), sizeCodeQtyMap.get(d.getSizeCode()).add(d.getQty()));
								}else{
									sizeCodeQtyMap.put(d.getSizeCode(), d.getQty());
								}
							}else{
								sizeCodeQtyMap = new TreeMap<String, BigDecimal>();
								sizeCodeQtyMap.put(d.getSizeCode(), d.getQty());
								d.setSizeCodeQtyMap(sizeCodeQtyMap);
								d.setTotalQty(d.getQty());
								itemRowDto.put(itemNo, d);
							}
							if((sizeRow = sizeHead.get(d.getSizeKind())) != null){
								sizeRow.put(d.getSizeCode(), d.getSizeCode());
							}else{
								sizeRow = new TreeMap<String, String>();
								sizeRow.put(d.getSizeCode(), d.getSizeCode());
								sizeHead.put(d.getSizeKind(), sizeRow);
							}
							total = total.add(d.getQty());
						}
						for(Entry<String, BillOmDeliverDtlSizeDto> m : itemRowDto.entrySet()){
							rows.add(m.getValue());
						}
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
						main.put("deliverNo", deliverNo);
						//main.put("storeNo", gm.getKey());
						//main.put("storeName", storeName);
						main.put("total", total);
						main.put("list", rows);
						main.put("sizeList", sizeList);
						main.put("sizeColNum", sizeColNum);
						resultList.add(main);
					}
					
				}
			}
			result.put("pages", resultList);
			result.put("result", ResultEnums.SUCCESS.getResultMsg());
			return result;
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.put("result", ResultEnums.FAIL.getResultMsg());
			result.put("msg", "系统异常请联系管理员");
			return result;
		}
	}
	
	private Map<String, List<BillOmDeliverDtlSizeDto>> groupByStoreNo(List<BillOmDeliverDtlSizeDto> list){
		Map<String, List<BillOmDeliverDtlSizeDto>> map = new HashMap<String, List<BillOmDeliverDtlSizeDto>>();
		if(CommonUtil.hasValue(list)){
			List<BillOmDeliverDtlSizeDto> temp = null;
			for(BillOmDeliverDtlSizeDto d:list){
				String sotreNo = d.getStoreNo();
				if((temp = map.get(sotreNo)) != null){
					temp.add(d);
				}else{
					temp = new ArrayList<BillOmDeliverDtlSizeDto>();
					temp.add(d);
					map.put(sotreNo, temp);
				}
			}
		}
		return map;
	}
}