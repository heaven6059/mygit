package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillLocateRule;
import com.yougou.logistics.city.common.model.BillLocateRuleDtl;
import com.yougou.logistics.city.common.vo.BillLocateRuleQuery;
import com.yougou.logistics.city.manager.BillLocateRuleManager;

/*
 * 储位策略锁定表
 * @author su.yq
 * @date  Tue Nov 05 18:39:01 CST 2013
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
@SuppressWarnings("rawtypes")
@RequestMapping("/bill_locate_rule")
@ModuleVerify("25080010")
public class BillLocateRuleController extends
		BaseCrudController<BillLocateRule> {

	@Log
	private Logger log;

	@Resource
	private BillLocateRuleManager billLocateRuleManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billLocateRule/", billLocateRuleManager);
	}

	@RequestMapping(value = "/addBillLocateRule")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.ADD)
	public Map<String, Object> addBillLocateRule(String inserted,
			String deleted, int operStatus, BillLocateRule billLocateRule,
			HttpServletRequest req) throws ManagerException,
			JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<BillLocateRuleDtl> listInserteds = new ArrayList<BillLocateRuleDtl>();
		List<BillLocateRuleDtl> listdeleteds = new ArrayList<BillLocateRuleDtl>();
		try {

			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(inserted)) {
				List<Map> list = mapper.readValue(inserted,
						new TypeReference<List<Map>>() {
						});
				listInserteds = convertListWithTypeReference(mapper, list);
			}

			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted,
						new TypeReference<List<Map>>() {
						});
				listdeleteds = convertListWithTypeReference(mapper, list);
			}

			BillLocateRuleQuery query = new BillLocateRuleQuery();
			query.setOperStatus(0);
			query.setBillLocateRule(billLocateRule);
			query.setListInserteds(listInserteds);
			query.setListdeleteds(listdeleteds);

			int result = billLocateRuleManager.saveBillLocateRule(query);
			if (result > 0) {
				map.put("result", "true");
			} else {
				map.put("result", "false");
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", "false");
		}
		return map;
	}

	@RequestMapping(value = "/saveBillLocateRule")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.MODIFY)
	public Map<String, Object> saveBillLocateRule(String inserted,
			String deleted, int operStatus, BillLocateRule billLocateRule,
			HttpServletRequest req) throws ManagerException,
			JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<BillLocateRuleDtl> listInserteds = new ArrayList<BillLocateRuleDtl>();
		List<BillLocateRuleDtl> listdeleteds = new ArrayList<BillLocateRuleDtl>();
		try {

			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(inserted)) {
				List<Map> list = mapper.readValue(inserted,
						new TypeReference<List<Map>>() {
						});
				listInserteds = convertListWithTypeReference(mapper, list);
			}

			if (StringUtils.isNotEmpty(deleted)) {
				List<Map> list = mapper.readValue(deleted,
						new TypeReference<List<Map>>() {
						});
				listdeleteds = convertListWithTypeReference(mapper, list);
			}

			BillLocateRuleQuery query = new BillLocateRuleQuery();
			query.setOperStatus(1);
			query.setBillLocateRule(billLocateRule);
			query.setListInserteds(listInserteds);
			query.setListdeleteds(listdeleteds);

			int result = billLocateRuleManager.saveBillLocateRule(query);
			if (result > 0) {
				map.put("result", "true");
			} else {
				map.put("result", "false");
			}
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
			map.put("result", "false");
		}
		return map;
	}

	@RequestMapping(value = "/deleteLocateRule")
	@ResponseBody
	@OperationVerify(OperationVerifyEnum.REMOVE)
	public String deleteLocateRule(String keyStr, HttpServletRequest req) {
		try {
			List<BillLocateRule> listBillLocateRules = new ArrayList<BillLocateRule>();
			ObjectMapper mapper = new ObjectMapper();
			if (StringUtils.isNotEmpty(keyStr)) {
				List<Map> list = mapper.readValue(keyStr,
						new TypeReference<List<Map>>() {
						});
				listBillLocateRules = convertListWithTypeReferenceRule(mapper,
						list);
			}

			if (billLocateRuleManager.deleteBillLocateRule(listBillLocateRules) > 0) {
				return "success";
			} else {
				return "fail";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "fail";
		}
	}

	private List<BillLocateRuleDtl> convertListWithTypeReference(
			ObjectMapper mapper, List<Map> list) throws JsonParseException,
			JsonMappingException, JsonGenerationException, IOException {
		List<BillLocateRuleDtl> tl = new ArrayList<BillLocateRuleDtl>(
				list.size());
		for (int i = 0; i < list.size(); i++) {
			BillLocateRuleDtl type = mapper.readValue(
					mapper.writeValueAsString(list.get(i)),
					BillLocateRuleDtl.class);
			tl.add(type);
		}
		return tl;
	}

	private List<BillLocateRule> convertListWithTypeReferenceRule(
			ObjectMapper mapper, List<Map> list) throws JsonParseException,
			JsonMappingException, JsonGenerationException, IOException {
		List<BillLocateRule> tl = new ArrayList<BillLocateRule>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillLocateRule type = mapper.readValue(
					mapper.writeValueAsString(list.get(i)),
					BillLocateRule.class);
			tl.add(type);
		}
		return tl;
	}
}