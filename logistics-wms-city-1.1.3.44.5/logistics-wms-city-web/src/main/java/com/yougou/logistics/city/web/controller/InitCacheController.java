package com.yougou.logistics.city.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.constans.SysConstans;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.Column;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.Editor;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.BillImCheckDtlManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.manager.common.CommonUtilManager;
import com.yougou.logistics.city.service.AuthorityUserBrandService;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.city.web.vo.CurrentUser;
import com.yougou.logistics.uc.common.api.model.AuthorityUserBrand;

/**
 * 1.tomcat容器初始化后加载些数据,如字典加载到缓存 2. 处理些公用的链接
 * 
 * @author wei.hj weihaijin
 * @date 2013-07-20cc
 * @copyRight yougou.com
 */
@Controller
@RequestMapping("/initCache")
public class InitCacheController implements InitializingBean {
	@Value("${db.schema}")
	private String schema;

	// 加载字典
	public static Map<String, List<LookupDtl>> lookupdMap = com.yougou.logistics.city.common.utils.SystemCache.lookupdMap;
	// 加载表的字段 用户请求时某张表第一需加载,放到Map中,第二次再请求时直接从Map取
	// 但是这样存在一个问题,当数据库中表增加了一列的话,需从新加载
	// 暂时的思路 在界面上加载一个刷新的按钮,一点刷新的 就会刷新所有Map里面已存在的Key(表)
	public static Map<String, List<Column>> tableColumnMap = new HashMap<String, List<Column>>();
	// 条件关系
	public static List<Column> conditionRelationList = new LinkedList<Column>();
	// 条件表达式
	public static List<Column> conditionExpList = new LinkedList<Column>();

	@Resource
	private CommonUtilManager commonUtilManager;

	@Resource
	private SizeInfoManager sizeInfoManager;

	@Resource
	private BillImCheckDtlManager billImCheckDtlManager;

	@Resource
	private AuthorityUserBrandService authorityUserBrandService;
	
	private static final String SYS_NO = "SYS_NO";

	private static final String SYSTEMID = "21";

	@Log
	private Logger log;

	/**
	 * 初始化
	 */
	public void init() {
		log.info(("★★★★★★★★★★★★★★★★初始化开始★★★★★★★★★★★★★★★★"));
		// 1.字典
		reloadInitLookupdMap();
		//供应商

		//客户信息

		log.info(("★★★★★★★★★★★★★★★★初始化完成★★★★★★★★★★★★★★★★"));
	}

	// 页面获得表字段
	@RequestMapping(value = "/getTableColumnList")
	@ResponseBody
	public List<Column> getTableColumnList(String operatorFlag, String moduleFlag, HttpServletRequest req)
			throws ManagerException {
		List<Column> listObj = new LinkedList<Column>();
		if (CommonUtil.hasValue(operatorFlag) && operatorFlag.equals("1")) {
			listObj = conditionRelationList;
		} else if (CommonUtil.hasValue(operatorFlag) && operatorFlag.equals("2")) {
			if (CommonUtil.hasValue(moduleFlag)) {
				// tableColumnMap
				if (tableColumnMap.containsKey(moduleFlag)) { // 缓存里面有此表的记录
					listObj = tableColumnMap.get(moduleFlag);
				} else {// 没有的话先查数据库,返回，同时加载到Map中
					try {
						// listObj=
						// this.commonUtilManager.selectDataBaseMetaData(null,CommonUtil.hasValue(schema)?(schema.toUpperCase()):"DBUSRLMP",tableName.toUpperCase(),
						// new String[]{"TABLE"});
						Column vo = new Column();
						vo.setModuleFlag(moduleFlag);
						listObj = this.commonUtilManager.queryCommonQueryConfig(vo);
						tableColumnMap.put(moduleFlag, listObj); // 缓存起来
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		} else if (CommonUtil.hasValue(operatorFlag) && operatorFlag.equals("3")) {
			listObj = conditionExpList;
		}
		return listObj;
	}

	/**
	 * 初始化加载字典数据
	 */
	private void reloadInitLookupdMap() {
		try {
			lookupdMap.clear();
			log.info(("^^^^^^初始化字典开始①^^^^^^"));
			LookupDtl lookupDtl = new LookupDtl();
			List<LookupDtl> listDtls = commonUtilManager.queryLookupDtlsList(lookupDtl);
			if (CommonUtil.hasValue(listDtls)) {
				for (LookupDtl vo : listDtls) {
					if (lookupdMap.containsKey(vo.getLookupcode())) {
						List<LookupDtl> tempList = lookupdMap.get(vo.getLookupcode());
						tempList.add(vo);
						lookupdMap.put(vo.getLookupcode(), tempList);
					} else {
						List<LookupDtl> tempList = new LinkedList<LookupDtl>();
						tempList.add(vo);
						lookupdMap.put(vo.getLookupcode(), tempList);
					}
				}
			}

			log.info(("^^^^^^初始化字典完成①^^^^^^Lookupcode分类个数：" + lookupdMap.size()));
		} catch (Exception e) {
			log.info(("^^^^^^初始化字典异常^^^^^^"));
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 刷新缓存
	 * 
	 * @param list
	 */
	public static void reLoadMap(List<LookupDtl> list) {
		if (CommonUtil.hasValue(list)) {
			lookupdMap.clear();
			for (LookupDtl vo : list) {
				if (lookupdMap.containsKey(vo.getLookupcode())) {
					List<LookupDtl> tempList = lookupdMap.get(vo.getLookupcode());
					tempList.add(vo);
					lookupdMap.put(vo.getLookupcode(), tempList);
				} else {
					List<LookupDtl> tempList = new LinkedList<LookupDtl>();
					tempList.add(vo);
					lookupdMap.put(vo.getLookupcode(), tempList);
				}
			}
		}
	}

	// 处理公用查询条件的SQL拼装,返回处理好的SQL
	@RequestMapping(value = "/getDoWithValidSql")
	public ResponseEntity<Column> getDoWithValidSql(HttpServletRequest req) {
		String queryCondition = req.getParameter("queryCondition");
		HashMap<String, String> returnMap = CommonUtil.getConditionSQL(queryCondition);
		Column column = new Column();
		column.setQueryConditionSQL(returnMap.get("sql"));
		column.setErrorMsg(returnMap.get("errorMsg"));
		return new ResponseEntity<Column>(column, HttpStatus.OK);

	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 获得当前用户信息与当前系统时间
	 * 
	 * @param req
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/getCurrentUser")
	@ResponseBody
	public CurrentUser getCurrentUser(HttpServletRequest req) throws ManagerException {
		CurrentUser currentUser = new CurrentUser(req);
		return currentUser;
	}

	// 页面获得字典
	@RequestMapping(value = "/getLookupDtlsList")
	@ResponseBody
	public List<LookupDtl> getLookupDtlsList(HttpServletRequest req,String lookupcode) {
		List<LookupDtl> listObj = new ArrayList<LookupDtl>();
		if (CommonUtil.hasValue(lookupcode) && lookupdMap.size() > 0) {
			listObj = lookupdMap.get(lookupcode);
		}
		
		String isAll = req.getParameter("isAll");
		if (SYS_NO.equals(lookupcode)&&StringUtils.isEmpty(isAll)) {
			AuthorityParams authorityParams = UserLoginUtil
					.getAuthorityParams(req);
			String userId = authorityParams.getUserId();
			String systemId = authorityParams.getSystemNoVerify();
			String areaSystemId = authorityParams.getAreaSystemNoVerify();
			List<AuthorityUserBrand> tempConfigDataList = null;
			try {
				tempConfigDataList = authorityUserBrandService.findByUserId(userId,Integer.valueOf(systemId),Integer.valueOf(areaSystemId));
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			List<LookupDtl> sysNos = new ArrayList<LookupDtl>();
			int brandSize = tempConfigDataList.size();
			int idx = 0;
			for(LookupDtl dtl:listObj){
				idx = 0;
				for(;idx<brandSize;idx++){
					if(dtl.getItemvalue().equals(tempConfigDataList.get(idx).getSysNo())&&
							SYSTEMID.equals(dtl.getSystemid())){
						sysNos.add(dtl);
						break;
					}
				}
			}
			/*for (LookupDtl dtl : listObj) {
				if (SYSTEMID.equals(dtl.getSystemid())) {
					sysNos.add(dtl);
				}
			}*/
			
			return sysNos;
		}
		return listObj;
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

	@RequestMapping(value = "/getBrandList")
	@ResponseBody
	public ResponseEntity<HashMap> getBrandList(SizeInfo info, HttpServletRequest req, Model model,
			HttpServletResponse res) throws ManagerException {
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
		Map<String, Object> params = this.builderParams(req, model);
		List<SizeInfo> sizeTypeList = this.sizeInfoManager.findByBiz(info, params);
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
		}

		// ====开始处理====

		returnMap.put("returnCols", returnList);
		returnMap.put("maxType", maxSortCount);
		returnMap.put("startType", preColNamesList.size() + 1);
		return new ResponseEntity<HashMap>(returnMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/do_export")
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
					for (int v = 0; v < dataRowList.size(); v++) {
						Map map = dataRowList.get(v);

						HSSFRow rowD = sheet1.createRow(typeSizeV + 1 + v);
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
	 * 生成一行尺码
	 * @param info
	 * @param req
	 * @param model
	 * @param res
	 * @return
	 * @throws ManagerException
	 */

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getBrandList2")
	@ResponseBody
	public ResponseEntity<HashMap> getBrandList2(HttpServletRequest req, Model model, HttpServletResponse res)
			throws ManagerException {

		HashMap returnMap = new HashMap();
		LinkedList returnList = new LinkedList();
		String preColNames = StringUtils.isEmpty(req.getParameter("preColNames")) ? "" : req
				.getParameter("preColNames");
		String endColNames = StringUtils.isEmpty(req.getParameter("endColNames")) ? "" : req
				.getParameter("endColNames");
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
			throw new ManagerException(e);
		}

		Editor defaultEditor = new Editor();
		defaultEditor.setType("validatebox");
		List<SizeInfo> sizeList = billImCheckDtlManager.getSizeCodeByGroup();
		LinkedList<JqueryDataGrid> colList = new LinkedList<JqueryDataGrid>();
		if (sizeList != null && sizeList.size() > 0) {
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
					if (col.getField().equals("importNo") || col.getField().equals("reciveDate")) {
						v.setRowspan(1);
						v.setColspan(1);
						v.setAlign("center");
						colList.add(v);
					} else {
						v.setRowspan(1);
						v.setColspan(1);
						v.setAlign("left");
						colList.add(v);
					}
				}
			}

			for (int i = 0; i < sizeList.size(); i++) {
				if (sizeList.get(i) == null) {
					break;
				} else {
					JqueryDataGrid v = new JqueryDataGrid();
					v.setField("v" + i);
					v.setTitle(sizeList.get(i).getSizeCode());
					v.setWidth(40);
					v.setRowspan(1);
					v.setColspan(1);
					v.setAlign("right");
					colList.add(v);
				}

			}
			if (endColNamesList.size() > 0) {
				for (JqueryDataGrid col : endColNamesList) {
					JqueryDataGrid v = new JqueryDataGrid();
					v.setField(col.getField());
					v.setTitle(col.getTitle());
					v.setWidth(40);
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
					v.setColspan(1);
					v.setAlign("right");
					colList.add(v);
				}
			}
			returnList.add(colList);
			returnMap.put("returnCols", returnList);
		}

		return new ResponseEntity<HashMap>(returnMap, HttpStatus.OK);
	}
}
