package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.annotation.OperationVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.enums.OperationVerifyEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.BeanUtilsCommon;
import com.yougou.logistics.base.common.utils.CustomDateEditorBase;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.common.HSSFExport;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.dto.BillCheckImRep;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillImCheckRepManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/bill_im_check_rep")
@ModuleVerify("25130020")
public class BillImCheckRepController {

	@Log
	private Logger log;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditorBase(dateFormat, false));
	}

	@Resource
	private BillImCheckRepManager billImCheckRepManager;
	
	@Resource
	private SizeInfoManager sizeInfoManager;

	@RequestMapping(value = "/forBillImCheckRep")
	public ModelAndView forBillImCheckRep() {
		return new ModelAndView("billimcheckrep/list");
	}

	// 获取尺码横排头部信息
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getBrandList(String preColNames, String endColNames, String sizeTypeFiledName, String sysNo,
			List<String> list) throws ManagerException {
		HashMap returnMap = new HashMap();

		LinkedList returnList = new LinkedList();
		ObjectMapper mapper = new ObjectMapper();
		List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
		List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();

		try {
			if (StringUtils.isNotEmpty(preColNames)) {
				preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}

			if (StringUtils.isNotEmpty(endColNames)) {
				endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
				});
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}

		// 尺码类型 A-ArrayList<SizeInfo>
		LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
		/*
		 * Map<String, Object> tempParams = new HashMap<String, Object>();
		 * tempParams.put("sysNo", sysNo); tempParams.put("preColNames",
		 * preColNames); tempParams.put("endColNames", endColNames);
		 * tempParams.put("sizeTypeFiledName", sizeTypeFiledName);
		 */

		// 查询尺码对应的尺码类别
		List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(list, sysNo,null);
		/*
		 * List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null,
		 * tempParams);
		 */
		if (sizeTypeList != null && sizeTypeList.size() > 0) {
			for (SizeInfo vo : sizeTypeList) {
				String sizeTypeName = vo.getSizeKind();
				if (sizeTypeMap.containsKey(sizeTypeName)) {
					ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
					listA.add(vo.getSizeName()); // ===========
					sizeTypeMap.put(sizeTypeName, listA);
				} else {
					ArrayList listA = new ArrayList();
					listA.add(vo.getSizeName());
					sizeTypeMap.put(sizeTypeName, listA);
				}
			}
		}

		int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
		if (sizeTypeMap != null) {
			java.util.Iterator it = sizeTypeMap.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				List tempList = (List) entry.getValue();
				if (maxSortCount < tempList.size()) {
					maxSortCount = tempList.size();
				}
			}
		}

		// ====开始处理====
		Editor defaultEditor = new Editor();
		defaultEditor.setType("validatebox");
		if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
			int typeSizeV = sizeTypeMap.size(); // 公共的类别数
			if (typeSizeV > 1) { // 多于一个尺码类别
				int rowspan = typeSizeV - 2; // 合并的行
				int preColNamesV = preColNamesList.size(); // 前
				int endColNamesV = endColNamesList.size(); // 后
				if (rowspan >= 0 && rowspan != -1) {
					// ①处理合并表头
					if (rowspan > 0) { // 大于2个的时候
						for (int i = 0; i < rowspan; i++) {
							LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
							// 1.前面合并
							if (i == 0) {
								JqueryDataGrid v = new JqueryDataGrid();
								v.setTitle("");
								v.setWidth(80);
								v.setEditor(defaultEditor);
								v.setRowspan(rowspan);
								v.setColspan(preColNamesV);
								v.setAlign("left");
								colList.add(v);
							}
							// 2.显示尺码
							int k = 0;
							for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
								if (k == i) {
									int diffCols = maxSortCount - (en.getValue().size());
									JqueryDataGrid v = new JqueryDataGrid();
									v.setTitle(en.getKey());
									v.setWidth(50);
									v.setEditor(defaultEditor);
									v.setAlign("left");
									colList.add(v);

									for (int p = 0; p < en.getValue().size(); p++) {
										JqueryDataGrid v1 = new JqueryDataGrid();
										v1.setTitle(en.getValue().get(p).toString());
										v1.setWidth(50);
										v1.setEditor(defaultEditor);
										v1.setAlign("left");
										colList.add(v1);
									}
									if (diffCols > 0) {
										for (int m = 1; m <= diffCols; m++) {
											JqueryDataGrid v1 = new JqueryDataGrid();
											v1.setTitle("");
											v1.setWidth(50);
											v1.setEditor(defaultEditor);
											v1.setAlign("left");
											colList.add(v1);
										}
									}

									break;
								}
								k = k + 1;
							}
							// 3.合并后头
							if (i == 0) {
								if (endColNamesV > 0) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setTitle("");
									v.setWidth(80);
									v.setEditor(defaultEditor);
									v.setRowspan(rowspan);
									v.setColspan(endColNamesV);
									v.setAlign("left");
									colList.add(v);
								}
							}
							returnList.add(colList);
						}
					}

					// ②处理业务表头
					for (int ii = 1; ii >= 0; ii--) {
						LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
						// 1.业务头
						if (ii == 1) {
							if (preColNamesList.size() > 0) {
								for (JqueryDataGrid col : preColNamesList) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setField(col.getField());
									v.setTitle(col.getTitle());
									v.setWidth(SysConstans.WIDTH_80);
									if (col.getWidth() != 0) {
										v.setWidth(col.getWidth());
									}

									v.setEditor(defaultEditor);
									if (col.getEditor() != null) {
										if (!CommonUtil.hasValue(col.getEditor().getType())) {
											col.getEditor().setType(defaultEditor.getType());
										}
										v.setEditor(col.getEditor());
									}

									v.setRowspan(2);
									v.setAlign("left");
									colList.add(v);
								}
							}
						}
						// 2.尺码
						int k2 = 0;
						java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
						while (it2.hasNext()) {
							java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
									.next();

							if ((sizeTypeMap.size() - 1 - ii) == k2) {

								int diffCols2 = maxSortCount - (entry.getValue().size());
								// <#-- 这里做判断的原因是为了防止重复写 field: ,上面是List循环的 -->
								JqueryDataGrid v = new JqueryDataGrid();
								v.setTitle(entry.getKey());
								v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
								v.setEditor(defaultEditor);
								v.setAlign("left");
								if (ii == 1) {
									v.setField(sizeTypeFiledName);
									v.setAlign("center");
								}
								colList.add(v);

								for (int p = 0; p < entry.getValue().size(); p++) {
									JqueryDataGrid v1 = new JqueryDataGrid();
									v1.setTitle(entry.getValue().get(p).toString());
									v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
									v1.setEditor(defaultEditor);
									v1.setAlign("left");
									if (ii == 1) {
										v1.setField("v" + (p + 1));
										v1.setAlign("center");
									}
									colList.add(v1);
								}

								if (diffCols2 > 0) {
									for (int m = 1; m <= diffCols2; m++) {
										JqueryDataGrid v1 = new JqueryDataGrid();
										v1.setTitle("");
										v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
										v1.setEditor(defaultEditor);
										v1.setAlign("left");
										if (ii == 1) {
											v1.setField("v" + (entry.getValue().size() + m));
											v1.setAlign("center");
										}
										colList.add(v1);
									}
								}

								break;
							}
							k2 = k2 + 1;
						}
						// 3.
						if (ii == 1) {
							if (endColNamesList.size() > 0) {
								for (JqueryDataGrid col : endColNamesList) {
									JqueryDataGrid v = new JqueryDataGrid();
									v.setField(col.getField());
									v.setTitle(col.getTitle());
									v.setWidth(SysConstans.WIDTH_80);
									if (col.getWidth() != 0) {
										v.setWidth(col.getWidth());
									}
									v.setEditor(defaultEditor);
									if (col.getEditor() != null) {
										if (!CommonUtil.hasValue(col.getEditor().getType())) {
											col.getEditor().setType(defaultEditor.getType());
										}
										v.setEditor(col.getEditor());
									}
									v.setRowspan(2);
									v.setAlign("left");
									colList.add(v);
								}
							}
						}

						returnList.add(colList);

					}
				}
			} else if (typeSizeV == 1) {// 只有一个尺码类别
				LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
				// 1.业务头 标题栏
				if (preColNamesList.size() > 0) {
					for (JqueryDataGrid col : preColNamesList) {
						JqueryDataGrid v = new JqueryDataGrid();
						v.setField(col.getField());
						v.setTitle(col.getTitle());
						v.setWidth(SysConstans.WIDTH_80);
						if (col.getWidth() != 0) {
							v.setWidth(col.getWidth());
						}

						v.setEditor(defaultEditor);
						if (col.getEditor() != null) {
							if (!CommonUtil.hasValue(col.getEditor().getType())) {
								col.getEditor().setType(defaultEditor.getType());
							}
							v.setEditor(col.getEditor());
						}

						v.setRowspan(1);
						v.setAlign("left");
						colList.add(v);
					}
				}
				// 尺码横排栏
				java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
				while (it2.hasNext()) {
					java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2.next();
					// 尺码类别
					JqueryDataGrid v = new JqueryDataGrid();
					v.setTitle(entry.getKey());
					v.setWidth(SysConstans.WIDTH_SIZETYPE_50);
					v.setEditor(defaultEditor);
					v.setAlign("left");
					v.setField(sizeTypeFiledName);
					v.setAlign("center");
					colList.add(v);

					for (int p = 0; p < entry.getValue().size(); p++) {
						JqueryDataGrid v1 = new JqueryDataGrid();
						v1.setTitle(entry.getValue().get(p).toString());
						v1.setWidth(SysConstans.WIDTH_SIZETYPE_50);
						v1.setEditor(defaultEditor);
						v1.setAlign("left");
						v1.setField("v" + (p + 1));
						v1.setAlign("center");
						colList.add(v1);
					}
				}
				// 合计信息
				if (endColNamesList.size() > 0) {
					for (JqueryDataGrid col : endColNamesList) {
						JqueryDataGrid vend = new JqueryDataGrid();
						vend.setField(col.getField());
						vend.setTitle(col.getTitle());
						vend.setWidth(SysConstans.WIDTH_80);
						if (col.getWidth() != 0) {
							vend.setWidth(col.getWidth());
						}
						vend.setEditor(defaultEditor);
						if (col.getEditor() != null) {
							if (!CommonUtil.hasValue(col.getEditor().getType())) {
								col.getEditor().setType(defaultEditor.getType());
							}
							vend.setEditor(col.getEditor());
						}
						vend.setRowspan(1);
						vend.setAlign("left");
						colList.add(vend);
					}
				}
				returnList.add(colList);
			}
		}

		// ====开始处理====

		returnMap.put("returnCols", returnList);
		returnMap.put("maxType", maxSortCount);
		returnMap.put("startType", preColNamesList.size() + 1);
		return returnMap;
	}

	/**
	 * 生成入库订单报表
	 * @param req
	 * @param model
	 * @param rep
	 * @return
	 * @throws ManagerException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/initHead")
	@ResponseBody
	public Map<String, Object> initHead(HttpServletRequest req, HttpSession session, Model model, BillCheckImRep rep)
			throws ManagerException {
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		rep.setLocNo(user.getLocNo());
		if(rep.getItemNo() != null) {
			rep.setItemNo(rep.getItemNo().toUpperCase());
		}
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		Map<String, Object> map = new HashMap<String, Object>();
		
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");
		String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
				.getParameter("sizeTypeFiledName");
		
		//大类一
		String cateOne = (String) req.getParameter("cateOne");
		String[] cateOneValues = null;
		if (StringUtils.isNotEmpty(cateOne)) {
			cateOneValues = cateOne.split(",");
		}
		rep.setCateOneValues(cateOneValues);
		
		//大类二
		String cateTwo = (String) req.getParameter("cateTwo");
		String[] cateTwoValues = null;
		if (StringUtils.isNotEmpty(cateTwo)) {
			cateTwoValues = cateTwo.split(",");
		}
		rep.setCateTwoValues(cateTwoValues);
		
		//大类三
		String cateThree = (String) req.getParameter("cateThree");
		String[] cateThreeValues = null;
		if (StringUtils.isNotEmpty(cateThree)) {
			cateThreeValues = cateThree.split(",");
		}
		rep.setCateThreeValues(cateThreeValues);
		
		//性别
		String season = (String)req.getParameter("seasonName");
		String [] seasonValues = null;
		if(StringUtils.isNotBlank(season)){
			seasonValues = season.split(",");
		}
		//季节
		String gender = (String)req.getParameter("genderName");
		String [] genderValues = null;
		if(StringUtils.isNotBlank(gender)){
			genderValues = gender.split(",");
		}
		rep.setSeasonValues(seasonValues);
		rep.setGenderValues(genderValues);
		
		List<String> sizeKindList = null;
			sizeKindList = billImCheckRepManager.selectAllDtlSizeKind(rep, authorityParams);
			if (sizeKindList == null || sizeKindList.size() == 0) {
				return map;
			} 
		Map header = getBrandList(preColNames, endColNames, sizeTypeFiledName, rep.getSysNo(), sizeKindList);
		map.put("header", header);
		return map;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/createBillCheckInRep")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> createBillCheckInRep(HttpServletRequest req, Model model,
			BillCheckImRep rep, HttpSession session) throws ManagerException {
		SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);
		rep.setLocNo(user.getLocNo());
		if(rep.getItemNo() != null) {
			rep.setItemNo(rep.getItemNo().toUpperCase());
		}
		String type = req.getParameter("businessType");
		
		//大类一
		String cateOne = (String) req.getParameter("cateOne");
		String[] cateOneValues = null;
		if (StringUtils.isNotEmpty(cateOne)) {
			cateOneValues = cateOne.split(",");
		}
		rep.setCateOneValues(cateOneValues);
		
		//大类二
		String cateTwo = (String) req.getParameter("cateTwo");
		String[] cateTwoValues = null;
		if (StringUtils.isNotEmpty(cateTwo)) {
			cateTwoValues = cateTwo.split(",");
		}
		rep.setCateTwoValues(cateTwoValues);
		
		//大类三
		String cateThree = (String) req.getParameter("cateThree");
		String[] cateThreeValues = null;
		if (StringUtils.isNotEmpty(cateThree)) {
			cateThreeValues = cateThree.split(",");
		}
		rep.setCateThreeValues(cateThreeValues);
		
		//性别
		String season = (String)req.getParameter("seasonName");
		String [] seasonValues = null;
		if(StringUtils.isNotBlank(season)){
			seasonValues = season.split(",");
		}
		//季节
		String gender = (String)req.getParameter("genderName");
		String [] genderValues = null;
		if(StringUtils.isNotBlank(gender)){
			genderValues = gender.split(",");
		}
		rep.setSeasonValues(seasonValues);
		rep.setGenderValues(genderValues);
		int total = 0;
		// 返回参数列表
		List<BillCheckImRep> billCheckImRepList = new ArrayList<BillCheckImRep>();
		//返回汇总列表
		List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
		//Map<String, Object> footerMap = new HashMap<String, Object>();
		Map<String, Object> footerMap = new HashMap<String, Object>();
		//footerMap.put("brandName", "小计");
		//footerList.add(footerMap);

		// 返回 Map集合
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			List<String> sizeKindList = billImCheckRepManager.selectAllDtlSizeKind(rep, authorityParams);
			if(sizeKindList.size() > 0) {
				//List<SizeInfo> list = billImCheckRepManager.getSizeCodeByGroup(rep, authorityParams);
				List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(sizeKindList, rep.getSysNo(),null);
				if(sizeTypeList.size() > 0) {
					Map<String, Integer> map = new HashMap<String, Integer>();
					Map<String, Integer> sizeNumGorupBySizeKind = new HashMap<String, Integer>();
					for (int j = 0; j < sizeTypeList.size(); j++) {
						String sizeKind = sizeTypeList.get(j).getSizeKind();
						if(sizeNumGorupBySizeKind.get(sizeKind) == null){
							sizeNumGorupBySizeKind.put(sizeKind,1);
						}else{
							sizeNumGorupBySizeKind.put(sizeKind,sizeNumGorupBySizeKind.get(sizeKind)+1);
						}
						map.put(sizeTypeList.get(j).getSizeCode(), sizeNumGorupBySizeKind.get(sizeTypeList.get(j).getSizeKind()));
					}
					int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
					int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
						.getParameter("rows"));
					rep.setPageNo(pageNo);
					rep.setPageSize(pageSize);
					rep.setLocNo(user.getLocNo());
					
					/** 统计总数 **/
					total = billImCheckRepManager.getCount(rep, authorityParams);
					/** 分组计算拿到订单编号和商品编号 **/

					List<BillCheckImRep> tempList = billImCheckRepManager.getBillImCheckByGroup(rep, authorityParams);
					
					SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
					for (int z = 0; z < tempList.size(); z++) {
						BigDecimal allCounts = new BigDecimal(0);
						BillCheckImRep model2 = new BillCheckImRep();
						
						model2=(BillCheckImRep) BeanUtils.cloneBean(rep);
						model2.setLocNo(user.getLocNo());
						model2.setItemNo(tempList.get(z).getItemNo());
						model2.setImportNo(tempList.get(z).getImportNo());
						model2.setCateNo(tempList.get(z).getCateNo());
						model2.setYears(tempList.get(z).getYears());
						model2.setSeason(tempList.get(z).getSeason());
						model2.setGender(tempList.get(z).getGender());
						Date date = tempList.get(z).getReciveDate();
						String d = dd.format(date);
						model2.setReciveDateStart(d);
						model2.setReciveDateEnd(d);

						BillCheckImRep dtl = tempList.get(z);
						SizeInfo sizeInfoParamInfo = new SizeInfo();
						List<BillCheckImRep> dtlList = null;
						if(dtl.getBusinessType() != null) {
							if(dtl.getBusinessType().equals("5")) {
								dtlList = billImCheckRepManager.getBillImCheckDtlUm(model2, authorityParams);
							} else if(dtl.getBusinessType().equals("6")) {
								dtlList = billImCheckRepManager.getBillImCheckDtlOtm(model2, authorityParams);
							} else if((dtl.getBusinessType().equals("0")) || (dtl.getBusinessType().equals("4"))) {
								dtlList = billImCheckRepManager.getBillImCheckDtlIm(model2, authorityParams);
							}
						}
						if(dtlList != null) {
//							Map<String, Object> mapParaMap = new HashMap<String, Object>();
//							mapParaMap.put("sysNo", dtl.getSysNo());
//							mapParaMap.put("sizeKind", dtl.getSizeKind());
//							List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
//							Map<String, BigDecimal> maps = new HashMap<String, BigDecimal>();
							for (int k = 0; k < dtlList.size(); k++) {
								BillCheckImRep temp = dtlList.get(k);
								BigDecimal a = new BigDecimal(0);
								if (null != temp.getCheckQty()) {
									a = temp.getCheckQty();
								}
								Object[] arg = new Object[] { a.toString() };
								String filedName = "setV" + map.get(temp.getSizeCode());
								CommonUtil.invokeMethod(dtl, filedName, arg);
								allCounts = allCounts.add(a);
								//FooterUtil.setFooterMap("v" + map.get(temp.getSizeCode()), a, footerMap);
								//FooterUtil.setFooterMap("allCount", a, footerMap);
							}
						}
						dtl.setAllCount(allCounts);
						billCheckImRepList.add(dtl);
					}
					if (pageNo == 1) {
						//footer1Map.putAll(footerMap);//1.将小计直接设值到汇总
						footerMap.put("brandName", "汇总");
						footerMap.put("isselectsum", true);
						footerList.add(footerMap);
						Map<String, Object> sumMap = billImCheckRepManager.selectSumQty(rep, authorityParams);
						footerMap.put("allCount", sumMap.get("checkqty"));						
					}
				}
			}
			obj.put("total", total);
			obj.put("rows", billCheckImRepList);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			new ManagerException(e);
		}
		return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
	}

	@RequestMapping(value = "/do_export4Detail")
	public void doExport(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info)
			throws ManagerException {
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map params = this.builderParams(req, model);
			
			// 设置查询参数
			BillCheckImRep rep = new BillCheckImRep();
			
			//大类一
			if (params.get("cateOne").toString() != null && params.get("cateOne").toString() != "") {
				String cateOne = params.get("cateOne").toString();
				String[] cateOneValues = null;
				if (StringUtils.isNotEmpty(cateOne)) {
					cateOneValues = cateOne.split(",");
				}
				rep.setCateOneValues(cateOneValues);
			}
			
			//大类二
			if (params.get("cateTwo").toString() != null && params.get("cateTwo").toString() != "") {
				String cateTwo = params.get("cateTwo").toString();
				String[] cateTwoValues = null;
				if (StringUtils.isNotEmpty(cateTwo)) {
					cateTwoValues = cateTwo.split(",");
				}
				rep.setCateTwoValues(cateTwoValues);
			}
			
			//大类三
			if (params.get("cateThree").toString() != null && params.get("cateThree").toString() != "") {
				String cateThree = params.get("cateThree").toString();
				String[] cateThreeValues = null;
				if (StringUtils.isNotEmpty(cateThree)) {
					cateThreeValues = cateThree.split(",");
				}
				rep.setCateThreeValues(cateThreeValues);
			}
			
			if (params.get("locno").toString() != null && params.get("locno").toString() != "") {
				rep.setLocNo(params.get("locno").toString());
			}
			if (params.get("sysNo").toString() != null && params.get("sysNo").toString() != "") {
				rep.setSysNo(params.get("sysNo").toString());
			}
			if (params.get("brandName").toString() != null && params.get("brandName").toString() != "") {
				rep.setBrandNo(params.get("brandName").toString());
			}
			if (params.get("reciveDateStart").toString() != null && params.get("reciveDateStart").toString() != "") {
				rep.setReciveDateStart(params.get("reciveDateStart").toString());
			}
			if (params.get("reciveDateEnd").toString() != null && params.get("reciveDateEnd").toString() != "") {
				rep.setReciveDateEnd(params.get("reciveDateEnd").toString());
			}
			if (params.get("businessType").toString() != null && params.get("businessType").toString() != "") {
				rep.setBusinessType(params.get("businessType").toString());
			}
			if (params.get("supplierNo").toString() != null && params.get("supplierNo").toString() != "") {
				rep.setSupplierNo(params.get("supplierNo").toString());
			}
			if (params.get("itemNo").toString() != null && params.get("itemNo").toString() != "") {
				rep.setItemNo(params.get("itemNo").toString().toUpperCase());
			}
			if (params.get("itemName").toString() != null && params.get("itemName").toString() != "") {
				rep.setItemName(params.get("itemName").toString());
			}
//			if (params.get("itemCate").toString() != null && params.get("itemCate").toString() != "") {
//				rep.setItemCate(params.get("itemCate").toString());
//			}
			if (params.get("importNo").toString() != null && params.get("importNo").toString() != "") {
				rep.setImportNo(params.get("importNo").toString());
			}
			
			if(rep != null) {
				List<String> sizeKindList = null;
				sizeKindList = billImCheckRepManager.selectAllDtlSizeKind(rep, authorityParams);
//				List<String> sizeKindList = billImCheckRepManager.selectItemSizeKind(params, authorityParams);
				if (sizeKindList == null || sizeKindList.size() == 0) {
					return;
				}
				String sysNo = rep.getSysNo();
				
				HashMap returnMap = new HashMap();

				LinkedList returnList = new LinkedList();

				String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
						.getParameter("preColNames");
				String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
						.getParameter("endColNames");

				String sizeTypeFiledName = StringUtils.isEmpty(req.getParameter("sizeTypeFiledName")) ? "" : req
						.getParameter("sizeTypeFiledName");

				ObjectMapper mapper = new ObjectMapper();
				List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
				List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();

				if (StringUtils.isNotEmpty(preColNames)) {
					preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
					});
				}

				if (StringUtils.isNotEmpty(endColNames)) {
					endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
					});
				}

				LinkedHashMap<String, ArrayList> sizeTypeMap = new LinkedHashMap<String, ArrayList>();
				List<SizeInfo> sizeTypeList = this.sizeInfoManager.selectSizeInfoBySizeNoList(sizeKindList, sysNo, null);

				if (sizeTypeList != null && sizeTypeList.size() > 0) {
					for (SizeInfo vo : sizeTypeList) {
						String sizeTypeName = vo.getSizeKind();
						if (sizeTypeMap.containsKey(sizeTypeName)) {
							ArrayList listA = (ArrayList) sizeTypeMap.get(sizeTypeName);
							listA.add(vo.getSizeName()); //===========
							sizeTypeMap.put(sizeTypeName, listA);
						} else {
							ArrayList listA = new ArrayList();
							listA.add(vo.getSizeName());
							sizeTypeMap.put(sizeTypeName, listA);
						}
					}
				}

				int maxSortCount = 1; // 最多的列有多少个 210 220的个数========================
				if (sizeTypeMap != null) {
					java.util.Iterator it = sizeTypeMap.entrySet().iterator();
					while (it.hasNext()) {
						java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
						List tempList = (List) entry.getValue();
						if (maxSortCount < tempList.size()) {
							maxSortCount = tempList.size();
						}
					}
				}

				String fileName = (String) params.get("fileName");

				String dataRow = StringUtils.isEmpty(req.getParameter("dataRow")) ? "" : req.getParameter("dataRow");
				List<Map> dataRowList = new ArrayList<Map>();
				if (StringUtils.isNotEmpty(dataRow)) {
					dataRowList = mapper.readValue(dataRow, new TypeReference<List<Map>>() {
					});
				}

				response.setContentType("application/vnd.ms-excel");

				String fileName2 = new String(fileName.getBytes("gb2312"), "iso-8859-1");
				//文件名
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName2 + ".xls");
				response.setHeader("Pragma", "no-cache");
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet1 = wb.createSheet();
				//HSSFSheet  sheet2=wb.createSheet();
				//wb.setSheetName(1,"魏海金",HSSFWorkbook.ENCODING_UTF_16);
				//sheet名字
				wb.setSheetName(0, fileName);
				sheet1.setDefaultRowHeightInPoints(20);
				sheet1.setDefaultColumnWidth((short) 18);
				//设置页脚
				HSSFFooter footer = sheet1.getFooter();
				footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
				//设置样式 表头
				HSSFCellStyle style1 = wb.createCellStyle();
				style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				HSSFFont font1 = wb.createFont();
				font1.setFontHeightInPoints((short) 13);
				font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style1.setFont(font1);
				//设置样式 表头
				HSSFCellStyle style2 = wb.createCellStyle();
				style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				style2.setWrapText(true);

				//设置样式 表头
				HSSFCellStyle style3 = wb.createCellStyle();
				style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				HSSFFont font3 = wb.createFont();
				font3.setFontHeightInPoints((short) 18);
				font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style3.setFont(font3);
				style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
				HSSFRow row0 = sheet1.createRow(0);
				row0.setHeightInPoints(35);

				//第一行 提示长
				HSSFCell cell0 = row0.createCell((short) 0);
				cell0.setCellValue(fileName.toString());
				cell0.setCellStyle(style3);

				//真正开始============================================================================
				if (sizeTypeMap != null && sizeTypeMap.size() > 0) {
					int typeSizeV = sizeTypeMap.size(); // 公共的类别数
					if (typeSizeV > 1) {
						int rowspan = typeSizeV - 2; // 合并的行========================
						int preColNamesV = preColNamesList.size(); // 前
						int endColNamesV = endColNamesList.size(); //后

						//合并
						Region rg1 = new Region(0, (short) 0, 0, (short) (maxSortCount + preColNamesV + endColNamesV));
						sheet1.addMergedRegion(rg1);

						if (rowspan >= 0 && rowspan != -1) {
							//①处理合并表头
							if (rowspan > 0) { // 大于2个的时候
								for (int i = 0; i < rowspan; i++) {
									int row = i + 1;
									HSSFRow row1 = sheet1.createRow(row);
									row1.setHeightInPoints(20);
									//1.前面合并
									if (i == 0) {
										HSSFCell cell1_0 = row1.createCell((short) 0);
										cell1_0.setCellValue("");
										Region rg11_cell1_0 = new Region(row, (short) 0, rowspan,
												(short) (preColNamesV - 1));
										sheet1.addMergedRegion(rg11_cell1_0);

									}

									//2.显示尺码
									int k = 0;
									for (Map.Entry<String, ArrayList> en : sizeTypeMap.entrySet()) {
										if (k == i) {
											int diffCols = maxSortCount - (en.getValue().size());
											sheet1.autoSizeColumn((short) (preColNamesV));
											HSSFCell cell1_A = row1.createCell((short) preColNamesV);
											cell1_A.setCellValue(en.getKey() + "   ");

											for (int p = 0; p < en.getValue().size(); p++) {
												sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
												HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
												cell1_AG.setCellValue(en.getValue().get(p).toString());
											}

											if (diffCols > 0) {
												for (int m = 1; m <= diffCols; m++) {
													HSSFCell cell1_AGG = row1.createCell((short) (preColNamesV
															+ en.getValue().size() + m));
													cell1_AGG.setCellValue("      ");
												}
											}

											break;
										}
										k = k + 1;
									}

									//3.合并后头
									if (i == 0) {
										if (endColNamesV > 0) {
											HSSFCell cell1_E = row1.createCell((short) (preColNamesV + maxSortCount + 1));
											cell1_E.setCellValue("");
											Region rg11_cell1_0 = new Region(row,
													(short) (preColNamesV + maxSortCount + 1), rowspan,
													(short) (preColNamesV + maxSortCount + endColNamesV));
											sheet1.addMergedRegion(rg11_cell1_0);
										}
									}
								}
							}

							int mm = 1;
							//②处理业务表头
							for (int ii = 1; ii >= 0; ii--) {
								HSSFRow row1 = sheet1.createRow(rowspan + mm);
								row1.setHeightInPoints(20);
								//1.业务头
								if (ii == 1) {
									if (preColNamesList.size() > 0) {
										int nn = 0;
										for (JqueryDataGrid col : preColNamesList) {
											HSSFCell cell1_0 = row1.createCell((short) nn);
											cell1_0.setCellValue(col.getTitle());
											Region rg11_cell1_0 = new Region(rowspan + mm, (short) (nn), rowspan + mm + 1,
													(short) (nn));
											sheet1.addMergedRegion(rg11_cell1_0);
											sheet1.autoSizeColumn((short) (nn));
											nn = nn + 1;
										}
									}
								}

								//2.尺码	
								int k2 = 0;
								java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
								while (it2.hasNext()) {
									java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
											.next();

									if ((sizeTypeMap.size() - 1 - ii) == k2) {
										int diffCols2 = maxSortCount - (entry.getValue().size());

										HSSFCell cell1_A = row1.createCell((short) preColNamesV);
										sheet1.autoSizeColumn((short) (preColNamesV));
										cell1_A.setCellValue(entry.getKey() + "   ");

										for (int p = 0; p < entry.getValue().size(); p++) {
											sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
											HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
											cell1_AG.setCellValue(entry.getValue().get(p).toString());
										}
										if (diffCols2 > 0) {
											for (int m = 1; m <= diffCols2; m++) {
												HSSFCell cell1_AGG = row1.createCell((short) (preColNamesV
														+ entry.getValue().size() + m));
												cell1_AGG.setCellValue("      ");
											}
										}
										break;
									}
									k2 = k2 + 1;
								}

								//3.
								if (ii == 1) {
									if (endColNamesList.size() > 0) {
										int nn = 0;
										for (JqueryDataGrid col : endColNamesList) {
											HSSFCell cell1_0 = row1
													.createCell((short) (preColNamesV + maxSortCount + 1 + nn));
											cell1_0.setCellValue(col.getTitle());
											Region rg11_cell1_0 = new Region(rowspan + mm, (short) (preColNamesV
													+ maxSortCount + 1 + nn), rowspan + mm + 1, (short) (preColNamesV
													+ maxSortCount + 1 + nn));
											sheet1.addMergedRegion(rg11_cell1_0);
											sheet1.autoSizeColumn((short) (preColNamesV + maxSortCount + 1 + nn));
											nn = nn + 1;
										}
									}
								}

								mm = mm + 1;
							}
						}
					} else if (typeSizeV == 1) {
						int preColNamesV = preColNamesList.size(); // 前
						int endColNamesV = endColNamesList.size(); //后
						// 1.业务头 标题栏
						int start = 0;
						HSSFRow row1 = sheet1.createRow(0);
						if (preColNamesList.size() > 0) {

							for (JqueryDataGrid col : preColNamesList) {
								HSSFCell cell1_0 = row1.createCell((short) start);
								cell1_0.setCellValue(col.getTitle());
								if (sizeTypeMap.size() > 1) {
									Region rg11_cell1_0 = new Region(0, (short) start, 1, (short) start);
									sheet1.addMergedRegion(rg11_cell1_0);
								}
								start++;
							}
						}
						// 尺码横排栏
						//尺码横排最大的单元格个数
						int sizeNoNum = 0;
						java.util.Iterator it2 = sizeTypeMap.entrySet().iterator();
						while (it2.hasNext()) {
							java.util.Map.Entry<String, ArrayList> entry = (java.util.Map.Entry<String, ArrayList>) it2
									.next();
							int diffCols2 = maxSortCount - (entry.getValue().size());

							HSSFCell cell1_A = row1.createCell((short) preColNamesV);
							sheet1.autoSizeColumn((short) (preColNamesV));
							cell1_A.setCellValue(entry.getKey() + "   ");

							for (int p = 0; p < entry.getValue().size(); p++) {
								sheet1.autoSizeColumn((short) (preColNamesV + p + 1));
								HSSFCell cell1_AG = row1.createCell((short) (preColNamesV + p + 1));
								cell1_AG.setCellValue(entry.getValue().get(p).toString());
							}
							if (diffCols2 > 0) {
								for (int m = 1; m <= diffCols2; m++) {
									HSSFCell cell1_AGG = row1
											.createCell((short) (preColNamesV + entry.getValue().size() + m));
									cell1_AGG.setCellValue("      ");
								}
							}

							if (entry.getValue().size() >= sizeNoNum) {
								sizeNoNum = entry.getValue().size();
							}
						}
						// 合计信息
						start = start + sizeNoNum + 1;
						if (endColNamesList.size() > 0) {
							row1.setHeightInPoints(20);
							for (JqueryDataGrid col : endColNamesList) {
								HSSFCell cell1_0 = row1.createCell((short) start);
								cell1_0.setCellValue(col.getTitle());
								if (sizeTypeMap.size() > 1) {
									Region rg11_cell1_0 = new Region(0, (short) start, 1, (short) start);
									sheet1.addMergedRegion(rg11_cell1_0);
								}
								start = start + 1;
							}
						}
					}
					if (dataRowList != null && dataRowList.size() > 0) {
						Map<String, Integer> sumMap = new HashMap<String, Integer>();
						int sumAllCount = 0;
						for (int v = 0; v < dataRowList.size(); v++) {
							Map map = dataRowList.get(v);
							sumAllCount = Integer.parseInt(map.get("allCount").toString()) + sumAllCount;
							int startRow = 0;
							if (sizeTypeMap.size() == 1) {
								startRow = typeSizeV;
							} else {
								startRow = typeSizeV + 1;
							}
							HSSFRow rowD = sheet1.createRow(startRow + v);
							rowD.setHeightInPoints(20);

							if (preColNamesList.size() > 0) {
								for (int m = 0; m < preColNamesList.size(); m++) {
									HSSFCell cell1_0 = rowD.createCell((short) m);
									JqueryDataGrid col = preColNamesList.get(m);
									String colV = map.get(col.getField()) != null ? String.valueOf(map.get(col.getField()))
											: "";
									cell1_0.setCellValue(colV);
								}
							}

							HSSFCell cell1_00 = rowD.createCell((short) preColNamesList.size());
							String colV = map.get(sizeTypeFiledName) != null ? String.valueOf(map.get(sizeTypeFiledName))
									: "";
							cell1_00.setCellValue(colV);

							for (int vv = 0; vv < maxSortCount; vv++) {
								HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + vv));
								String vX = "v" + (vv + 1);
								String colVVV = map.get(vX) != null ? String.valueOf(map.get(vX)) : "";
								cell1_000.setCellValue(colVVV);
								String sumVx = "S" + vX;
								if (sumMap.get(sumVx) == null) {
									if ("".equals(colVVV)) {
										sumMap.put(sumVx, 0);
									} else {
										sumMap.put(sumVx, Integer.parseInt(colVVV));
									}
								} else {
									if (!"".equals(colVVV)) {
										sumMap.put(sumVx, sumMap.get(sumVx) + Integer.parseInt(colVVV));
									}
								}
							}

							if (endColNamesList.size() > 0) {
								for (int m = 0; m < endColNamesList.size(); m++) {
									HSSFCell cell1_0000 = rowD.createCell((short) (preColNamesList.size() + 1
											+ maxSortCount + m));
									JqueryDataGrid coll = endColNamesList.get(m);
									String colVVVV = map.get(coll.getField()) != null ? String.valueOf(map.get(coll
											.getField())) : "0";
									cell1_0000.setCellValue(colVVVV);
								}
							}

						}
						//增加合计
						HSSFRow rowD = sheet1.createRow(sheet1.getLastRowNum() + 1);
						rowD.setHeightInPoints(20);
						HSSFCell cell1_0 = rowD.createCell(0);
						cell1_0.setCellValue("小计");
						for (int vv = 0; vv < maxSortCount; vv++) {
							HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + vv));
							String vX = "Sv" + (vv + 1);
							Integer colVVV = sumMap.get(vX) != null ? sumMap.get(vX) : 0;
							String stringValue = colVVV == 0 ? "" : String.valueOf(colVVV);
							cell1_000.setCellValue(stringValue);
						}
						HSSFCell cell1_000 = rowD.createCell((short) (preColNamesList.size() + 1 + maxSortCount));
						cell1_000.setCellValue(String.valueOf(sumAllCount));
					}
				}
				wb.write(response.getOutputStream());
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 导出excel
	 * @param req
	 * @param model
	 * @param response
	 * @throws ManagerException
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping(value = "/do_export2")
	@OperationVerify(OperationVerifyEnum.EXPORT)
	public void doExport2(HttpServletRequest req, Model model, HttpServletResponse response) throws ManagerException {

		Map<String, Object> params = builderParams(req, model);

		String exportColumns = (String) params.get("exportColumns");

		String fileName = (String) params.get("fileName");

		ObjectMapper mapper = new ObjectMapper();
		SystemUser user = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);
		if (StringUtils.isNotEmpty(exportColumns)) {
			try {
				AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
				exportColumns = exportColumns.replace("[", "");
				exportColumns = exportColumns.replace("]", "");
				exportColumns = "[" + exportColumns + "]";
				// 设置查询参数
				BillCheckImRep rep = new BillCheckImRep();
				if (params.get("sysNo").toString() != null && params.get("sysNo").toString() != "") {
					rep.setSysNo(params.get("sysNo").toString());
				}
				if (params.get("brandName").toString() != null && params.get("brandName").toString() != "") {
					rep.setBrandNo(params.get("brandName").toString());
				}
				if (params.get("itemNo").toString() != null && params.get("itemNo").toString() != "") {
					rep.setItemNo(params.get("itemNo").toString().toUpperCase());
				}
				if (params.get("itemName").toString() != null && params.get("itemName").toString() != "") {
					rep.setItemName(params.get("itemName").toString());
				}
				if (params.get("itemCate").toString() != null && params.get("itemCate").toString() != "") {
					rep.setItemCate(params.get("itemCate").toString());
				}
				if (params.get("supplierNo").toString() != null && params.get("supplierNo").toString() != "") {
					rep.setSupplierNo(params.get("supplierNo").toString());
				}
				if (params.get("reciveDateStart").toString() != null && params.get("reciveDateStart").toString() != "") {
					rep.setReciveDateStart(params.get("reciveDateStart").toString());
				}
				if (params.get("reciveDateEnd").toString() != null && params.get("reciveDateEnd").toString() != "") {
					rep.setReciveDateEnd(params.get("reciveDateEnd").toString());
				}
				if (params.get("importNo").toString() != null && params.get("importNo").toString() != "") {
					rep.setImportNo(params.get("importNo").toString());
				}
				if (params.get("businessType").toString() != null && params.get("businessType").toString() != "") {
					rep.setBusinessType(params.get("businessType").toString());
				}
				
				// 字段名列表
				List<Map> ColumnsList = mapper.readValue(exportColumns, new TypeReference<List<Map>>() {});
				// 返回参数列表
				List<BillCheckImRep> billCheckImRepList = new ArrayList<BillCheckImRep>();
				List<Map> listArrayList = new ArrayList<Map>();
				List<SizeInfo> list = billImCheckRepManager.getSizeCodeByGroup(rep, authorityParams);
				Map<String, Integer> map = new HashMap<String, Integer>();
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j) == null) {
						break;
					} else {
						map.put(list.get(j).getSizeCode(), j);
					}
				}
				int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req
						.getParameter("page"));
				int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10000 : Integer.parseInt(req
						.getParameter("rows"));
				rep.setPageNo(pageNo);
				rep.setPageSize(pageSize);
				rep.setLocNo(user.getLocNo());
				
					/** 统计总数 **/
					int total = billImCheckRepManager.getCount(rep, authorityParams);
					/** 分组计算拿到订单编号和商品编号 **/
					List<BillCheckImRep> tempList = billImCheckRepManager.getBillImCheckByGroup(rep, authorityParams);

					for (int z = 0; z < tempList.size(); z++) {
						BigDecimal allCounts = new BigDecimal(0);
						BillCheckImRep model2 = new BillCheckImRep();
						
						model2=(BillCheckImRep) BeanUtils.cloneBean(rep);
						model2.setLocNo(user.getLocNo());
						model2.setItemNo(tempList.get(z).getItemNo());
						model2.setImportNo(tempList.get(z).getImportNo());
						model2.setCateNo(tempList.get(z).getCateNo());
						model2.setYears(tempList.get(z).getYears());
						model2.setSeason(tempList.get(z).getSeason());
						model2.setGender(tempList.get(z).getGender());
						
						BillCheckImRep dtl = tempList.get(z);
						List<BillCheckImRep> dtlList = null;
						if(dtl.getBusinessType() != null) {
							if(dtl.getBusinessType().equals("5")) {
								dtlList = billImCheckRepManager.getBillImCheckDtlUm(model2, authorityParams);
							} else if(dtl.getBusinessType().equals("6")) {
								dtlList = billImCheckRepManager.getBillImCheckDtlOtm(model2, authorityParams);
							} else if((dtl.getBusinessType().equals("0")) || (dtl.getBusinessType().equals("4"))) {
								dtlList = billImCheckRepManager.getBillImCheckDtlIm(model2, authorityParams);
							}
						}
						Map<String, BigDecimal> maps = new HashMap<String, BigDecimal>();
						for (int k = 0; k < dtlList.size(); k++) {
							BillCheckImRep r = (BillCheckImRep) dtlList.get(k);
							String key = r.getImportNo() + "|" + r.getItemNo() + "|" + r.getSizeCode();
							BigDecimal qty = maps.get(key);
							if (qty == null) {
								maps.put(key, r.getCheckQty());
							} else {
								maps.put(key, qty.add(r.getCheckQty()));
							}
							if (dtlList.get(k) != null && dtlList.get(k).getSizeCode() != null) {
								int s = map.get(dtlList.get(k).getSizeCode());
								String filedName = "setV" + s;
								if (maps.get(key) != null) {
									Object[] arg = new Object[] { maps.get(key).toString() };
									CommonUtil.invokeMethod(dtl, filedName, arg);
									allCounts = allCounts.add(dtlList.get(k).getCheckQty());
								} else {
									Object[] arg = new Object[] { "0" };
									CommonUtil.invokeMethod(dtl, filedName, arg);
									allCounts = allCounts.add(new BigDecimal(0));
								}
							}

						}
						dtl.setAllCount(allCounts);
						billCheckImRepList.add(dtl);
					}
					for (BillCheckImRep vo : billCheckImRepList) {
						Map map2 = new HashMap();
						BeanUtilsCommon.object2MapWithoutNull(vo, map2);
						listArrayList.add(map2);
					}
				if(listArrayList.size() > 0) {
					HSSFExport.commonExportData(StringUtils.isNotEmpty(fileName) ? fileName : "导出信息", ColumnsList,
							listArrayList, response);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> builderParams(HttpServletRequest req, Model model) {
		Map<String, Object> params = req.getParameterMap();
		if (null != params && params.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Entry<String, Object> p : params.entrySet()) {
				if (null == p.getValue() || StringUtils.isEmpty(p.getValue().toString()))
					continue;
				// 只转换一个参数，多个参数不转换
				String values[] = (String[]) p.getValue();
				String match = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
				if (values[0].matches(match)) {
					try {
						p.setValue(sdf.parse(values[0]));
					} catch (ParseException e) {
						log.error(e.getMessage(), e);
					}
				} else if (p.getKey().equals("queryCondition") && model.asMap().containsKey("queryCondition")) {
					p.setValue(model.asMap().get("queryCondition"));
				} else {
					p.setValue(values[0]);
				}
			}
		}
		return params;
	}


	@RequestMapping(value = "/do_export3")
	public void doExport3(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info) throws IOException{
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");
		String fileName = StringUtils.isEmpty(req.getParameter("fileName")) ? "" : req
				.getParameter("fileName");

		ObjectMapper mapper = new ObjectMapper();
		List<JqueryDataGrid> preColNamesList = new ArrayList<JqueryDataGrid>();
		List<JqueryDataGrid> endColNamesList = new ArrayList<JqueryDataGrid>();
		if (StringUtils.isNotEmpty(preColNames)) {
			preColNamesList = mapper.readValue(preColNames, new TypeReference<List<JqueryDataGrid>>() {
			});
		}
		if (StringUtils.isNotEmpty(endColNames)) {
			endColNamesList = mapper.readValue(endColNames, new TypeReference<List<JqueryDataGrid>>() {
			});
		}
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map params = this.builderParams(req, model);
			// 设置查询参数
			BillCheckImRep rep = new BillCheckImRep();
			//大类一
			if (params.get("cateOne").toString() != null && params.get("cateOne").toString() != "") {
				String cateOne = params.get("cateOne").toString();
				String[] cateOneValues = null;
				if (StringUtils.isNotEmpty(cateOne)) {
					cateOneValues = cateOne.split(",");
				}
				rep.setCateOneValues(cateOneValues);
			}
			//大类二
			if (params.get("cateTwo").toString() != null && params.get("cateTwo").toString() != "") {
				String cateTwo = params.get("cateTwo").toString();
				String[] cateTwoValues = null;
				if (StringUtils.isNotEmpty(cateTwo)) {
					cateTwoValues = cateTwo.split(",");
				}
				rep.setCateTwoValues(cateTwoValues);
			}
			//大类三
			if (params.get("cateThree").toString() != null && params.get("cateThree").toString() != "") {
				String cateThree = params.get("cateThree").toString();
				String[] cateThreeValues = null;
				if (StringUtils.isNotEmpty(cateThree)) {
					cateThreeValues = cateThree.split(",");
				}
				rep.setCateThreeValues(cateThreeValues);
			}
			if (params.get("locno").toString() != null && params.get("locno").toString() != "") {
				rep.setLocNo(params.get("locno").toString());
			}
			if (params.get("sysNo").toString() != null && params.get("sysNo").toString() != "") {
				rep.setSysNo(params.get("sysNo").toString());
			}
			if (params.get("brandName").toString() != null && params.get("brandName").toString() != "") {
				rep.setBrandNo(params.get("brandName").toString());
			}
			if (params.get("reciveDateStart").toString() != null && params.get("reciveDateStart").toString() != "") {
				rep.setReciveDateStart(params.get("reciveDateStart").toString());
			}
			if (params.get("reciveDateEnd").toString() != null && params.get("reciveDateEnd").toString() != "") {
				rep.setReciveDateEnd(params.get("reciveDateEnd").toString());
			}
			if (params.get("businessType").toString() != null && params.get("businessType").toString() != "") {
				rep.setBusinessType(params.get("businessType").toString());
			}
			if (params.get("supplierNo").toString() != null && params.get("supplierNo").toString() != "") {
				rep.setSupplierNo(params.get("supplierNo").toString());
			}
			if (params.get("itemNo").toString() != null && params.get("itemNo").toString() != "") {
				rep.setItemNo(params.get("itemNo").toString().toUpperCase());
			}
			if (params.get("itemName").toString() != null && params.get("itemName").toString() != "") {
				rep.setItemName(params.get("itemName").toString());
			}
			if (params.get("importNo").toString() != null && params.get("importNo").toString() != "") {
				rep.setImportNo(params.get("importNo").toString());
			}
			if (params.get("years").toString() != null && params.get("years").toString() != "") {
				rep.setYearsName(params.get("years").toString());
			}
			String season = (String)req.getParameter("seasonName");
			String [] seasonValues = null;
			if(StringUtils.isNotBlank(season)){
				seasonValues = season.split(",");
			}
			//季节
			String gender = (String)req.getParameter("genderName");
			String [] genderValues = null;
			if(StringUtils.isNotBlank(gender)){
				genderValues = gender.split(",");
			}
			rep.setSeasonValues(seasonValues);
			rep.setGenderValues(genderValues);
			
			if(rep.getSysNo() != null) {
				Map<String,Object> obj=new HashMap<String,Object>();
				obj = billImCheckRepManager.findBillImCheckRepByPage(rep, authorityParams, true);
				
				List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(null, params);
				Map<String,List<String>> sizeTypeMap = new TreeMap<String, List<String>>();
				for(SizeInfo si : sizeTypeList){
					String sizeKind = si.getSizeKind();
					if(sizeTypeMap.get(sizeKind) == null){
						List<String> sizeCodeByKind = new ArrayList<String>();
						sizeCodeByKind.add(si.getSizeCode());
						sizeTypeMap.put(sizeKind, sizeCodeByKind);
					}else{
						sizeTypeMap.get(sizeKind).add(si.getSizeCode());
					}
				}
				List<BillCheckImRep> data = (List<BillCheckImRep>) obj.get("rows");
				HSSFWorkbook wb = ExcelUtils.getExcle4Size(preColNamesList, sizeTypeMap, endColNamesList, fileName, data, true);
				
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
				response.setHeader("Pragma", "no-cache");
				wb.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		}
	}
}

