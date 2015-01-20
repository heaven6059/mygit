package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.util.CollectionUtils;
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
import com.yougou.logistics.city.common.dto.BillOmExpDispatchDtlDTO;
import com.yougou.logistics.city.common.dto.BillOmExpDtlDTO;
import com.yougou.logistics.city.common.enums.ResultEnums;
import com.yougou.logistics.city.common.model.BillOmExpDtl;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.model.SystemUser;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.utils.SumUtilMap;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillOmExpDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.FooterUtil;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * 出库订单明细
 * 
 * @author zuo.sw
 * @date 2013-09-29 16:50:42
 * @version 1.0.0
 * @param <ModelType>
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd All Rights
 *            Reserved.
 * 
 *            The software for the YouGou technology development, without the
 *            company's written consent, and any other individuals and
 *            organizations shall not be used, Copying, Modify or distribute the
 *            software.
 * 
 */
@Controller
@RequestMapping("/billomexpdtl")
public class BillOmExpDtlController<ModelType> extends BaseCrudController<BillOmExpDtl> {

	@Log
	private Logger log;

	@Resource
	private BillOmExpDtlManager billOmExpDtlManager;

	@Resource
	private SizeInfoManager sizeInfoManager;

	@Override
	public CrudInfo init() {
		return new CrudInfo("billomexpdtl/", billOmExpDtlManager);
	}

	@RequestMapping(value = "/dtllist.json")
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
			int total = billOmExpDtlManager.findCount(params, authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmExpDtl> list = billOmExpDtlManager.findByPage(page, sortColumn, sortOrder, params,
					authorityParams, DataAccessRuleEnum.BRAND);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillOmExpDtl temp : list) {
				this.setFooterMap("itemQty", temp.getItemQty(), footerMap);
				this.setFooterMap("scheduleQty", temp.getScheduleQty(), footerMap);
				this.setFooterMap("locateQty", temp.getLocateQty(), footerMap);
				this.setFooterMap("realQty", temp.getRealQty(), footerMap);
				this.setFooterMap("deliverQty", temp.getDeliverQty(), footerMap);
			}
			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				sumFoot = billOmExpDtlManager.selectSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("schedule_qty", 0);
					sumFoot.put("real_qty", 0);
					sumFoot.put("deliver_qty", 0);
					sumFoot.put("item_qty", 0);
					sumFoot.put("locate_qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_no", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 横码尺排
	 * @param expNo
	 * @param sysNo
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/queryBillOmExpDtlDTOlList")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> queryBillOmExpDtlDTOlList(String expNo, String sysNo,
			HttpServletRequest req, Model model) throws ManagerException {
		try {
			int total = 0;
			// 返回参数列表
			List<BillOmExpDtlDTO> returnDtoList = new ArrayList<BillOmExpDtlDTO>(0);
			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("storeName", "小计");
			footerList.add(footerMap);

			// 返回 Map集合
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", returnDtoList);
			obj.put("footer", footerList);

			ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(obj,
					HttpStatus.OK);
			if (StringUtils.isEmpty(expNo) || StringUtils.isEmpty(sysNo) || "0".equals(sysNo) || "N".equals(sysNo)
					|| "null".equals(sysNo)) {
				return responseEntity;
			}

			// 查询盘差单详情
			BillOmExpDtlDTO dtoParam = new BillOmExpDtlDTO();
			dtoParam.setExpNo(expNo);
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));

			total = billOmExpDtlManager.selectCountMx(dtoParam, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);

			List<BillOmExpDtlDTO> listTempGroup = billOmExpDtlManager.queryBillOmExpDtlDTOGroupBy(page, dtoParam,
					authorityParams);
			if (CollectionUtils.isEmpty(listTempGroup)) {
				return responseEntity;
			}

			for (BillOmExpDtlDTO gvo : listTempGroup) {

				if (CommonUtil.hasValue(gvo.getItemNo())) {
					dtoParam.setItemNo(gvo.getItemNo());
					dtoParam.setStoreNo(gvo.getStoreNo());
					dtoParam.setPoNo(gvo.getPoNo());
					List<BillOmExpDtlDTO> listTempMxList = billOmExpDtlManager.queryBillOmExpDtlDTOBExpNo(dtoParam,
							authorityParams);
					BigDecimal allCounts = new BigDecimal(0);
					if (CollectionUtils.isEmpty(listTempMxList)) {
						continue;
					}

					BillOmExpDtlDTO dto = listTempMxList.get(0);
					SizeInfo sizeInfoParamInfo = new SizeInfo();
					Map<String, Object> mapParaMap = new HashMap<String, Object>();
					mapParaMap.put("sysNo", dto.getSysNo());
					mapParaMap.put("sizeKind", dto.getSizeKind());
					List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
					for (BillOmExpDtlDTO temp : listTempMxList) {
						for (int i = 0; i < sizeInfoList.size(); i++) {
							SizeInfo sizeInfo = sizeInfoList.get(i);
							if (temp.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对
								Object[] arg = new Object[] { temp.getItemQty().toString() };
								String filedName = "setV" + (i + 1);
								CommonUtil.invokeMethod(dto, filedName, arg);
								allCounts = allCounts.add(temp.getItemQty());

								// //////////////////
								this.setFooterMap("v" + (i + 1), temp.getItemQty(), footerMap);
								this.setFooterMap("allCount", temp.getItemQty(), footerMap);
								// //////////////////
								break;
							}
						}
					}
					dto.setAllCount(allCounts);
					dto.setAllCost(new BigDecimal(0));
					returnDtoList.add(dto);
				}
			}
			if(pageNo == 1){
				Map<String, Object> SumFooterMap = new HashMap<String, Object>();
				if(total <= pageSize){
					SumFooterMap.putAll(footerMap);
				}else{
					page = new SimplePage(pageNo, total, (int) total);
					List<BillOmExpDtlDTO> ltg = billOmExpDtlManager.queryBillOmExpDtlDTOGroupBy(page, dtoParam,authorityParams);
					for (BillOmExpDtlDTO gvo : ltg) {

						if (CommonUtil.hasValue(gvo.getItemNo())) {
							dtoParam.setItemNo(gvo.getItemNo());
							dtoParam.setStoreNo(gvo.getStoreNo());
							dtoParam.setPoNo(gvo.getPoNo());
							List<BillOmExpDtlDTO> listTempMxList = billOmExpDtlManager.queryBillOmExpDtlDTOBExpNo(dtoParam,
									authorityParams);
							BigDecimal allCounts = new BigDecimal(0);
							if (CollectionUtils.isEmpty(listTempMxList)) {
								continue;
							}

							BillOmExpDtlDTO dto = listTempMxList.get(0);
							SizeInfo sizeInfoParamInfo = new SizeInfo();
							Map<String, Object> mapParaMap = new HashMap<String, Object>();
							mapParaMap.put("sysNo", dto.getSysNo());
							mapParaMap.put("sizeKind", dto.getSizeKind());
							List<SizeInfo> sizeInfoList = this.sizeInfoManager.findByBiz(sizeInfoParamInfo, mapParaMap);
							for (BillOmExpDtlDTO temp : listTempMxList) {
								for (int i = 0; i < sizeInfoList.size(); i++) {
									SizeInfo sizeInfo = sizeInfoList.get(i);
									if (temp.getSizeNo().equals(sizeInfo.getSizeNo())) { // 相对
										Object[] arg = new Object[] { temp.getItemQty().toString() };
										String filedName = "setV" + (i + 1);
										CommonUtil.invokeMethod(dto, filedName, arg);
										allCounts = allCounts.add(temp.getItemQty());

										// //////////////////
										this.setFooterMap("v" + (i + 1), temp.getItemQty(), SumFooterMap);
										this.setFooterMap("allCount", temp.getItemQty(), SumFooterMap);
										// //////////////////
										break;
									}
								}
							}
						}
					}
				}
				SumFooterMap.put("storeName", "总计");
				footerList.add(SumFooterMap);
			}
			obj.put("total", total);
			obj.put("rows", returnDtoList);
			obj.put("footer", footerList);
			return new ResponseEntity<Map<String, Object>>(obj, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ManagerException(e);
		}
	}

	@RequestMapping(value = "/do_export4Horizontal")
	public void doExport(HttpServletRequest req, Model model, HttpServletResponse response, SizeInfo info)
			throws ManagerException {
		try {
			HashMap returnMap = new HashMap();

			LinkedList returnList = new LinkedList();

			Map<String, Object> params = this.builderParams(req, model);
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
			List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(info, params);

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
								Region rg11_cell1_0 = new Region(row, (short) 0, rowspan, (short) (preColNamesV - 1));
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
									Region rg11_cell1_0 = new Region(row, (short) (preColNamesV + maxSortCount + 1),
											rowspan, (short) (preColNamesV + maxSortCount + endColNamesV));
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
									HSSFCell cell1_0 = row1.createCell((short) (preColNamesV + maxSortCount + 1 + nn));
									cell1_0.setCellValue(col.getTitle());
									Region rg11_cell1_0 = new Region(rowspan + mm, (short) (preColNamesV + maxSortCount
											+ 1 + nn), rowspan + mm + 1, (short) (preColNamesV + maxSortCount + 1 + nn));
									sheet1.addMergedRegion(rg11_cell1_0);
									sheet1.autoSizeColumn((short) (preColNamesV + maxSortCount + 1 + nn));
									nn = nn + 1;
								}
							}
						}

						mm = mm + 1;
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
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * 新增和删除出库订单明细
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/saveOmExpDtl")
	public ResponseEntity<Map<String, Object>> saveOmExpDtl(HttpServletRequest req)  throws JsonParseException, JsonMappingException, IOException, ManagerException {
		 Map<String, Object> flag = new HashMap<String, Object>();
		boolean isSuccess = false;
		try {
			String locno = req.getParameter("locno");
			String ownerNo = req.getParameter("ownerNo");
			String expNo = req.getParameter("expNo");
			String expDate = req.getParameter("expDate");
			if (StringUtils.isNotBlank(locno) && StringUtils.isNotBlank(ownerNo) && StringUtils.isNotBlank(expNo)
					&& StringUtils.isNotBlank(expDate)) {
				// 获取登陆用户
				HttpSession session = req.getSession();
				SystemUser user = (SystemUser) session.getAttribute(PublicContains.SESSION_USER);

				String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req
						.getParameter("deleted");
				String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req
						.getParameter("updated");
				String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req
						.getParameter("inserted");
				ObjectMapper mapper = new ObjectMapper();
				Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
				if (StringUtils.isNotBlank(deletedList)) {
					List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.DELETED, oList);
				}
				if (StringUtils.isNotBlank(upadtedList)) {
					List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.UPDATED, oList);
				}
				if (StringUtils.isNotBlank(insertedList)) {
					List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>() {
					});
					List<ModelType> oList = convertListWithTypeReference(mapper, list);
					params.put(CommonOperatorEnum.INSERTED, oList);
				}

				isSuccess = billOmExpDtlManager.addBillOmExpDtl(locno, ownerNo, expNo, expDate, params,
						user.getLoginName());
			}
			String result = ResultEnums.SUCCESS.getResultMsg();
			String msg = "保存成功";
			if(!isSuccess){
				result = ResultEnums.FAIL.getResultMsg();
				msg = "保存失败!";
			}
			flag.put("result", result);
			flag.put("msg", msg);
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		} catch (Exception e) {
			log.error("===========新增和删除出库订单明细时异常：" + e.getMessage(), e);
			flag.put("result", ResultEnums.FAIL.getResultMsg());
			flag.put("msg", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(flag, HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <ModelType> List<ModelType> convertListWithTypeReference(ObjectMapper mapper, List<Map> list)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		Class<ModelType> entityClass = (Class<ModelType>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		List<ModelType> tl = new ArrayList<ModelType>(list.size());
		for (int i = 0; i < list.size(); i++) {
			ModelType type = mapper.readValue(mapper.writeValueAsString(list.get(i)), entityClass);
			tl.add(type);
		}
		return tl;
	}

	/**
	 * 查询出库订单明细列表
	 */
	@RequestMapping(value = "/listItemNo")
	@ResponseBody
	public Map<String, Object> listItemNo(HttpServletRequest req, Model model) throws ManagerException {
		try {
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));

			Map<String, Object> params = builderParams(req, model);
			return billOmExpDtlManager.selectItemNoByDetailPageCount(pageNo, pageSize, sortColumn, sortOrder, params);
		} catch (Exception e) {
			log.error("===========查询出库订单明细列表（带分页）时异常：" + e.getMessage(), e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}

	private void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
		BigDecimal count = (BigDecimal) footerMap.get(key);
		if (count == null) {
			count = val;
		} else {
			count = count.add(val);
		}
		footerMap.put(key, count);
	}

	/**
	 * 查询单明细列表（带分页）
	 */
	@RequestMapping(value = "/listBillOmExpDtlDispatch.json")
	@ResponseBody
	public Map<String, Object> listBillOmExpDtlDispatch(HttpServletRequest req, BillOmExpDispatchDtlDTO billOmExpDtl)
			throws ManagerException {
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req
					.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req
					.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req
					.getParameter("order"));

			int total = billOmExpDtlManager.findBillOmExpDtlDispatchCount(billOmExpDtl, authorityParams);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmExpDispatchDtlDTO> list = billOmExpDtlManager.findBillOmExpDtlDispatchByPage(page, sortColumn,
					sortOrder, billOmExpDtl, authorityParams);

			// 返回汇总列表
			List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
			Map<String, Object> footerMap = new HashMap<String, Object>();
			footerMap.put("itemNo", "小计");
			footerList.add(footerMap);
			for (BillOmExpDispatchDtlDTO temp : list) {
				FooterUtil.setFooterMapByInt("itemQty", temp.getItemQty(), footerMap);
				FooterUtil.setFooterMapByInt("differenceQty", temp.getDifferenceQty(), footerMap);
				FooterUtil.setFooterMapByInt("noenoughQty", temp.getNoenoughQty(), footerMap);
				FooterUtil.setFooterMapByInt("usableQty", temp.getUsableQty(), footerMap);
			}

			// 合计
			Map<String, Object> sumFoot = new HashMap<String, Object>();
			if (pageNo == 1) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("locno", billOmExpDtl.getLocno());
				params.put("expNo", billOmExpDtl.getExpNo());
				sumFoot = billOmExpDtlManager.selectDispatchSumQty(params, authorityParams);
				if (sumFoot == null) {
					sumFoot = new SumUtilMap<String, Object>();
					sumFoot.put("item_qty", 0);
					sumFoot.put("difference_Qty", 0);
					sumFoot.put("noenough_Qty", 0);
					sumFoot.put("usable_Qty", 0);
				}
				sumFoot.put("isselectsum", true);
				sumFoot.put("item_no", "合计");
			} else {
				sumFoot.put("itemNo", "合计");
			}
			footerList.add(sumFoot);

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("footer", footerList);
			return obj;
		} catch (Exception e) {
			log.error("===========查询出库调度列表（带分页）时异常：" + e.getMessage(), e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}

	@RequestMapping(value = "/selectStore.json")
	@ResponseBody
	public Map<String, Object> selectStore(HttpServletRequest req, BillOmExpDtl billOmExpDtl) throws ManagerException {
		try {
			Map<String, Object> obj = new HashMap<String, Object>();
			List<BillOmExpDtl> list = this.billOmExpDtlManager.selectStore(billOmExpDtl);
			obj.put("rows", list);
			obj.put("total", list.size());
			return obj;
		} catch (Exception e) {
			log.error("===========查询出库调度列表（带分页）时异常：" + e.getMessage(), e);
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("success", false);
			return obj;
		}
	}
}