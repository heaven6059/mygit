package com.yougou.logistics.city.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;

@SuppressWarnings("deprecation")
public class ExcelUtils {

	public final static Map<String, Class<?>[]> SET_PARAMS_MAP;
	
	public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static{
		SET_PARAMS_MAP = new HashMap<String, Class<?>[]>();
		SET_PARAMS_MAP.put("java.lang.String", new Class<?>[]{String.class});
		SET_PARAMS_MAP.put("int", new Class<?>[]{Integer.class});
		SET_PARAMS_MAP.put("java.lang.Integer", new Class<?>[]{Integer.class});
		SET_PARAMS_MAP.put("double", new Class<?>[]{Double.class});
		SET_PARAMS_MAP.put("java.lang.Double", new Class<?>[]{Double.class});
		SET_PARAMS_MAP.put("long", new Class<?>[]{Long.class});
		SET_PARAMS_MAP.put("java.lang.Long", new Class<?>[]{Long.class});
		SET_PARAMS_MAP.put("java.math.BigDecimal", new Class<?>[]{BigDecimal.class});
		SET_PARAMS_MAP.put("char", new Class<?>[]{Character.class});
		SET_PARAMS_MAP.put("java.lang.Character", new Class<?>[]{Character.class});
		SET_PARAMS_MAP.put("boolean", new Class<?>[]{Boolean.class});
		SET_PARAMS_MAP.put("java.lang.Boolean", new Class<?>[]{Boolean.class});
		SET_PARAMS_MAP.put("short", new Class<?>[]{Short.class});
		SET_PARAMS_MAP.put("java.lang.Short", new Class<?>[]{Short.class});
	}
	/**
	 * 返回类属性的类型Map,key为对应的set方法名
	 * 
	 * @param c
	 * @return
	 */
	public static <T> Map<String, String> getFieldType(Class<T> c) {
		Map<String, String> map = new HashMap<String, String>();
		Method[] ms = c.getMethods();
		String setter;
		for (Method m : ms) {
			setter = m.getName();
			if (setter.indexOf("set") == 0) {
				map.put(setter, m.getParameterTypes()[0].getName());
			}
		}
		return map;
	}

	
	/**
	 * 从导入的Excel获取数据
	 * @param request
	 * @param sheetIdx Excel工作簿序号,从0开始
	 * @param firstLineIdx 首行数据序列号 一般从1开始
	 * @param colNames 列名数组
	 * @param mustArray 对应列是否为必须
	 * @param mainKey 确定是否重复的键
	 * @param c Bean.class
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getData(HttpServletRequest request, int sheetIdx,
			int firstLineIdx, String[] colNames, boolean [] mustArray,String[] mainKey, Class<T> c) throws Exception{
		List<T> list = new ArrayList<T>();
		try {
			Map<String, String> mainKeyMap = new HashMap<String, String>();
			Map<String, String> mainKeyValueMap = new HashMap<String, String>();
			if(mainKey != null){
				for(String mk:mainKey){
					mainKeyMap.put(mk, mk);
				}
			} 
			int colNum = colNames.length;//
			InputStream in = null;
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile("reportValue");
			Workbook wb = null;
			in = multipartFile.getInputStream();
			wb = new HSSFWorkbook(in);
			Sheet sheet = wb.getSheetAt(sheetIdx);

			Map<String, String> fieldTypeMap = getFieldType(c);

			T t = null;
			String setter;
			String fieldTypeStr;
			String fieldName;
			String data;
			Class<?>[] filedType;
			Object[] params = null;
			Method m;
			
			//计算后面的空白行数量
			int blankEndCount=0;
			for (int idx = sheet.getLastRowNum(); idx >=firstLineIdx; idx--) {
				Row row = sheet.getRow(idx);
				if(row==null){
					blankEndCount++;
				}else{
					boolean isRowAllBlank=true;
					for (int colIdx = 0; colIdx < colNum; colIdx++) {
						data = GetValueTypeForXLSX(row.getCell(colIdx));
						if(!StringUtils.isBlank(data)){
							isRowAllBlank=false;
						}						
					}
					if(isRowAllBlank){
						blankEndCount++;
					}else{
						break;
					}
				}
			}			
			
			int len=sheet.getLastRowNum()-blankEndCount + 1;
			
			StringBuilder sb = new StringBuilder();
			for (int idx = firstLineIdx; idx < len; idx++) {
				Row row = sheet.getRow(idx);
				String mainKeyValue = "";
				if(row==null){
					sb.append("【 ").append(idx+1).append("行").append(" 】不能为空\\r\\n");
					continue;
				}
				
				t = c.newInstance();
				for (int colIdx = 0; colIdx < colNum; colIdx++) {
					
					data = GetValueTypeForXLSX(row.getCell(colIdx));
					fieldName = colNames[colIdx];
					if(mainKeyMap.get(fieldName) != null){
						mainKeyValue += data;
					}
					if(StringUtils.isBlank(fieldName)){
						continue;
					}
					
					setter = "set"
							+ String.valueOf(fieldName.charAt(0)).toUpperCase()
							+ fieldName.substring(1);
					fieldTypeStr = fieldTypeMap.get(setter);
					filedType = SET_PARAMS_MAP.get(fieldTypeStr);
					m = c.getMethod(setter, filedType);
					params = new Object[]{data};
					if(mustArray[colIdx] && StringUtils.isBlank(data)){
						sb.append("【 ").append(idx+1).append("*").append((char)('A'+colIdx)).append(" 】不能为空\\r\\n");
						continue;
					}
					if(fieldTypeStr.indexOf("int")>=0 || fieldTypeStr.indexOf("Integer")>=0){
						try {
							params = new Object[]{Integer.valueOf(StringUtils.isBlank(data)?"0":data)};
						} catch (NumberFormatException e) {
							sb.append("【 ").append(idx+1).append("*").append((char)('A'+colIdx)).append(" 】必须为数值\\r\\n");
							continue;
						}
					}else if(fieldTypeStr.indexOf("BigDecimal")>=0){
						try{
							params = new Object[]{new BigDecimal(StringUtils.isBlank(data)?"0":data)};
						} catch (NumberFormatException e) {
							sb.append("【 ").append(idx+1).append("*").append((char)('A'+colIdx)).append(" 】必须为数值\\r\\n");
							continue;
						}
					}else if(fieldTypeStr.indexOf("double")>=0 || fieldTypeStr.indexOf("Double")>=0){
						try{
							params = new Object[]{Double.valueOf(StringUtils.isBlank(data)?"0":data)};
						} catch (NumberFormatException e) {
							sb.append("【 ").append(idx+1).append("*").append((char)('A'+colIdx)).append(" 】必须为数值\\r\\n");
							continue;
						}
					}else if(fieldTypeStr.indexOf("long")>=0 || fieldTypeStr.indexOf("Long")>=0){
						try{
							params = new Object[]{Long.valueOf(StringUtils.isBlank(data)?"0":data)};
						} catch (NumberFormatException e) {
							sb.append("【 ").append(idx+1).append("*").append((char)('A'+colIdx)).append(" 】必须为数值\\r\\n");
							continue;
						}
					}else if(fieldTypeStr.indexOf("char")>=0 || fieldTypeStr.indexOf("Character")>=0){
						params = new Object[]{data.charAt(0)};
					}else if(fieldTypeStr.indexOf("boolean")>=0 || fieldTypeStr.indexOf("Boolean")>=0){
						params = new Object[]{Boolean.valueOf(data)};
					}else if(fieldTypeStr.indexOf("short")>=0 || fieldTypeStr.indexOf("Short")>=0){
						try{
							params = new Object[]{Short.valueOf(StringUtils.isBlank(data)?"0":data)};
						} catch (NumberFormatException e) {
							sb.append("【 ").append(idx+1).append("*").append((char)('A'+colIdx)).append(" 】必须为数值\\r\\n");
							continue;
						}
					}
					
					
					m.invoke(t, params);
					
				}
				if(!StringUtils.isBlank(mainKeyValue)){
					if(mainKeyValueMap.get(mainKeyValue) != null){
						sb.append("【 ").append(idx+1).append("行").append(" 】为重复数据\\r\\n");
						continue;
					}else{
						mainKeyValueMap.put(mainKeyValue, mainKeyValue);
					}
				}
				list.add(t);
			}
			if(sb.length() > 0){
				throw new Exception("<span style='color:red;'>数据异常</span><br><br><textarea rows='5' cols='40'>"+sb.toString()+"</textarea>");
				
			}
		} catch (IOException e) {
			throw new Exception(e.getMessage(),e);
		} catch (NumberFormatException e) {
			throw new Exception(e);
		} catch (NullPointerException e) {
			throw new Exception(e.getMessage(),e);
		} catch (InstantiationException e) {
			throw new Exception(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new Exception(e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			throw new Exception(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			throw new Exception(e.getMessage(),e);
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e);
		} 
		return list;
	}

	/**
	 * 将Double转成String,可能不含有小数和小数点
	 * @param value
	 * @return
	 */
	public static String doubleToString(Double value){
		String rs = "";
		if(value != null){
			Long a = value.longValue();
			double b = value.doubleValue() - a;
			if(b == 0){
				rs = a.toString();
			}else{
				rs = value.toString();
			}
		}
		return rs;
	}
	public static String GetValueTypeForXLSX(Cell cell) {
		if (cell == null)
			return null;
		Object obj;
		cell.setCellType(Cell.CELL_TYPE_STRING);
		obj = cell.getStringCellValue();
		return obj == null ? "" : (obj+"").trim();
	}
	
	/**
	 * 生成尺码横排的Excel
	 * @param preColNamesList 尺码前面的列
	 * @param sizeTypeMap 尺码列
	 * @param endColNamesList 尺码后面的列
	 * @param fileName 文件名
	 * @param data 数据
	 * @param needSum 是否要总计
	 * @return
	 */
	public static <T> HSSFWorkbook getExcle4Size(List<JqueryDataGrid> preColNamesList,
			Map<String,List<String>> sizeTypeMap,
			List<JqueryDataGrid> endColNamesList,
			String fileName,
			List<T> data,boolean needSum){
		
		List<String> getMethodNames = new ArrayList<String>();
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			//合计所在列的列序号
			//int countIdx = 0;
			Map<String, Integer> countIdxMap = new HashMap<String, Integer>();
			HSSFSheet sheet1 = wb.createSheet();
			//sheet名字
			wb.setSheetName(0, fileName);
			sheet1.setDefaultRowHeightInPoints(20);
			//sheet1.setDefaultColumnWidth((short) 18);
			
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
			
			
			int preColLen = preColNamesList == null?0:preColNamesList.size();
			int endColLen = endColNamesList == null?0:endColNamesList.size();
			int sizeColMaxLen = 0;
			int sizeRowLen = 0;
			if(sizeTypeMap != null){
				sizeRowLen = sizeTypeMap.size();
				for(Entry<String,List<String>> m:sizeTypeMap.entrySet()){
					if(sizeColMaxLen < m.getValue().size()){
						sizeColMaxLen = m.getValue().size();
					}
				}
			}
			//合并标题行
			Region rg1 = new Region(0, (short) 0, 0, (short) (preColLen + sizeColMaxLen + endColLen));
			sheet1.addMergedRegion(rg1);
			
			//填充尺码头
			int startSizeColIdx = 0;
			int startSizeRowIdx = 1;
			for(Entry<String,List<String>> m:sizeTypeMap.entrySet()){
				startSizeColIdx = preColLen;
				HSSFRow row = sheet1.createRow(startSizeRowIdx);
				HSSFCell cell = row.createCell((short)preColLen);
				cell.setCellValue(m.getKey());
				for(String title:m.getValue()){
					startSizeColIdx++;
					cell = row.createCell((short)startSizeColIdx);
					cell.setCellValue(title);
				}
				startSizeRowIdx++;
			}
		
			if(sizeRowLen > 1){//sizeKind大于等于2需要合并单元格
				if(sizeRowLen > 2){
					Region rg2 = new Region(1, (short) 0, sizeRowLen-2, (short) (preColLen-1));
					sheet1.addMergedRegion(rg2);
					rg2 = new Region(1, (short) (preColLen+sizeColMaxLen+1), sizeRowLen-2, (short) (preColLen + sizeColMaxLen + endColLen));
					sheet1.addMergedRegion(rg2);
				}
				HSSFRow row = sheet1.getRow(sizeRowLen-1);//不能用createRow
				for(int idx=0;idx<preColLen;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(preColNamesList.get(idx).getTitle());
					Region rg = new Region(sizeRowLen-1, (short) idx, sizeRowLen, (short) idx);
					sheet1.addMergedRegion(rg);
					getMethodNames.add("get"+(preColNamesList.get(idx).getField().charAt(0)+"").toUpperCase()+preColNamesList.get(idx).getField().substring(1));
				}
				getMethodNames.add("getSizeKind");
				for(int idx=0;idx<sizeColMaxLen;idx++){
					getMethodNames.add("getV"+(idx+1));
				}
				for(int idx=preColLen+sizeColMaxLen+1;idx<preColLen+sizeColMaxLen+endColLen+1;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle());
					Region rg = new Region(sizeRowLen-1, (short) idx, sizeRowLen, (short) idx);
					sheet1.addMergedRegion(rg);
					getMethodNames.add("get"+(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().charAt(0)+"").toUpperCase()+endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().substring(1));
					if("合计".equals(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle())||"总金额".equals(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle())){
						//countIdx = idx;
						countIdxMap.put(getMethodNames.get(getMethodNames.size()-1), idx);
					}
				}
			}else{
				HSSFRow row = sheet1.getRow(sizeRowLen);//不能用createRow
				for(int idx=0;idx<preColLen;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(preColNamesList.get(idx).getTitle());
					getMethodNames.add("get"+(preColNamesList.get(idx).getField().charAt(0)+"").toUpperCase()+preColNamesList.get(idx).getField().substring(1));
				}
				getMethodNames.add("getSizeKind");
				for(int idx=0;idx<sizeColMaxLen;idx++){
					getMethodNames.add("getV"+(idx+1));
				}
				for(int idx=preColLen+sizeColMaxLen+1;idx<preColLen+sizeColMaxLen+endColLen+1;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle());
					getMethodNames.add("get"+(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().charAt(0)+"").toUpperCase()+endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().substring(1));
					if("合计".equals(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle())||"总金额".equals(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle())){
						//countIdx = idx;
						countIdxMap.put(getMethodNames.get(getMethodNames.size()-1), idx);
					}
				}
			}
			//填充值
			int dataRowIdx = sizeRowLen+1;
			Object[] arg = new Object[] {};
			Object result = null;
			
			Map<String, BigDecimal> sumMap = new HashMap<String, BigDecimal>();
			
			
			
			for(T t:data){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				for(int idx=0;idx<getMethodNames.size();idx++){
					String getter = getMethodNames.get(idx);
					result = CommonUtil.invokeMethod(t, getter, arg);
					if(result == null){
						continue;
					}
					HSSFCell cell = row.createCell((short)idx);
					if(result instanceof Date){
						cell.setCellValue(format.format(((Date)result)));
					}else{
						cell.setCellValue(result.toString());
					}
					if(countIdxMap.get(getter) != null){//计算总计
						if(sumMap.get(getter) == null){
							sumMap.put(getter,new BigDecimal(result.toString()));
						}else{
							sumMap.put(getter,sumMap.get(getter).add(new BigDecimal(result.toString())));
						}
					}
				}
				dataRowIdx++;
			}
			if(needSum){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				HSSFCell cell = row.createCell(0);
				cell.setCellValue("总计");
				for(Entry<String, Integer> s : countIdxMap.entrySet()){
					BigDecimal value = sumMap.get(s.getKey());
					if(value != null){
						cell = row.createCell(s.getValue());
						cell.setCellValue(value.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
		
	}
	/**
	 * 生成表头跨行跨列的Excel
	 * @param fileName Excel标题名
	 * @param tabColMap 表头与合并行列坐标映射(坐标为rowFrom,colFrom,rowTo,colTo)
	 * @param data 数据集合
	 * @param needSum 是否需要总计
	 * @param sumColIdxs 需要总计的列序号(首列序号为0)
	 * @return
	 * @throws Exception
	 */
	public static <T> HSSFWorkbook get4XY(String fileName,Map<JqueryDataGrid, int[]> tabColMap,List<T> data,boolean needSum,int [] sumColIdxs) throws Exception{
		
		Map<String, Integer> getterIdxMap = new HashMap<String, Integer>();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet1 = wb.createSheet();
		//sheet名字
		wb.setSheetName(0, fileName);
		sheet1.setDefaultRowHeightInPoints(20);
		HSSFRow row0 = sheet1.createRow(0);
		row0.setHeightInPoints(35);

		//设置样式 表头
		HSSFCellStyle style1=wb.createCellStyle(); 
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font1=wb.createFont();
		font1.setFontHeightInPoints((short)11);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		//总计样式
		HSSFCellStyle style2=wb.createCellStyle(); 
		HSSFFont font2=wb.createFont();
		font2.setFontHeightInPoints((short)11);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style2.setFont(font2);
		//标题样式
		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 18);
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font3);
		style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		//第一行 提示长
		HSSFCell cell0 = row0.createCell((short) 0);
		cell0.setCellValue(fileName.toString());
		cell0.setCellStyle(style3);
		
		int maxColIdx = 0;//最大列序号(从0起)
		int firstRowIdx = 0;//首行数据序号(表头的下一行)
		
		for(Entry<JqueryDataGrid, int[]> m:tabColMap.entrySet()){
			int [] xy = m.getValue();
			HSSFRow row = sheet1.getRow(xy[0]+1);
			if(row == null){
				row = sheet1.createRow(xy[0]+1);
			}
			if(maxColIdx < xy[3]){
				maxColIdx = xy[3];
			}
			if(firstRowIdx < xy[2]){
				firstRowIdx = xy[2];
			}
			String getter = m.getKey().getField();
			if(!StringUtils.isBlank(getter)){
				getter = "get"+(getter.charAt(0)+"").toUpperCase()+getter.substring(1);
				getterIdxMap.put(getter, xy[3]);
			}
			HSSFCell cell = row.createCell((short) xy[1]);
			cell.setCellValue(m.getKey().getTitle());
			cell.setCellStyle(style1);
			Region rg = new Region(xy[0]+1, (short) xy[1], xy[2]+1, (short) xy[3]);
			sheet1.addMergedRegion(rg);
		}
		firstRowIdx += 2;
		//合并标题行
		Region rg = new Region(0, (short) 0, 0, (short) maxColIdx);
		sheet1.addMergedRegion(rg);
		//填充数据
		if(CommonUtil.hasValue(data)){
			int dataRowIdx = firstRowIdx;
			int colIdx = 0;
			String getter = null;
			Object result = null;
			Object [] arg = new Object[]{};
			Map<Integer, BigDecimal> sumMap = new HashMap<Integer, BigDecimal>();
			if(needSum && sumColIdxs != null){
				for(int sc:sumColIdxs){
					sumMap.put(sc, new BigDecimal(0));
				}
			}
			for(T t:data){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				for(Entry<String, Integer> m:getterIdxMap.entrySet()){
					colIdx = m.getValue();
					getter = m.getKey();
					result = CommonUtil.invokeMethod(t, getter, arg);
					if(result == null){
						continue;
					}
					if(needSum && sumMap.get(colIdx) != null){
						sumMap.put(colIdx, sumMap.get(colIdx).add(new BigDecimal(result.toString())));
					}
					HSSFCell cell = row.createCell((short)colIdx);
					cell.setCellValue(result.toString());
				}
				dataRowIdx++;
			}
			if(needSum){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				for(Entry<Integer, BigDecimal> m:sumMap.entrySet()){
					colIdx = m.getKey();
					HSSFCell cell = row.createCell((short)colIdx);
					cell.setCellValue(m.getValue().toString());
					cell.setCellStyle(style2);
				}
				HSSFCell cell = row.createCell((short)0);
				cell.setCellValue("总计");
				cell.setCellStyle(style2);
			}
		}
		return wb;
	}
	/**
	 * 生成表头跨行跨列的Excel
	 * @param fileName Excel标题名
	 * @param tabColMap 表头与合并行列坐标映射(坐标为rowFrom,colFrom,rowTo,colTo)
	 * @param data 数据集合
	 * @param needSum 是否需要总计
	 * @param sumColIdxs 需要总计的列序号(首列序号为0)
	 * @return
	 * @throws Exception
	 */
	/**
	 * 生成尺码横排的Excel
	 * @param preColNamesList 尺码前面的列
	 * @param sizeTypeMap 尺码列
	 * @param endColNamesList 尺码后面的列
	 * @param fileName 文件名
	 * @param data 数据
	 * @param needSum 是否要总计
	 * @return
	 */
	public static <T> HSSFWorkbook getSheet(List<JqueryDataGrid> preColNamesList,
			Map<String,List<String>> sizeTypeMap, List<JqueryDataGrid> endColNamesList,
			String fileName,List<T> data, boolean needSum,
			int sheetIdx, String sheetName, HSSFWorkbook wb){
		
		List<String> getMethodNames = new ArrayList<String>();
		if(wb == null) {
			wb = new HSSFWorkbook();
		}
		try {
			//合计所在列的列序号
			int countIdx = 0;
			HSSFSheet sheet1 = wb.createSheet();
			//sheet名字
			wb.setSheetName(sheetIdx, sheetName);
			sheet1.setDefaultRowHeightInPoints(20);
			//sheet1.setDefaultColumnWidth((short) 18);
			
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
			
			
			int preColLen = preColNamesList == null?0:preColNamesList.size();
			int endColLen = endColNamesList == null?0:endColNamesList.size();
			int sizeColMaxLen = 0;
			int sizeRowLen = 0;
			if(sizeTypeMap != null){
				sizeRowLen = sizeTypeMap.size();
				for(Entry<String,List<String>> m:sizeTypeMap.entrySet()){
					if(sizeColMaxLen < m.getValue().size()){
						sizeColMaxLen = m.getValue().size();
					}
				}
			}
			//合并标题行
			Region rg1 = new Region(0, (short) 0, 0, (short) (preColLen + sizeColMaxLen + endColLen));
			sheet1.addMergedRegion(rg1);
			
			//填充尺码头
			int startSizeColIdx = 0;
			int startSizeRowIdx = 1;
			for(Entry<String,List<String>> m:sizeTypeMap.entrySet()){
				startSizeColIdx = preColLen;
				HSSFRow row = sheet1.createRow(startSizeRowIdx);
				HSSFCell cell = row.createCell((short)preColLen);
				cell.setCellValue(m.getKey());
				for(String title:m.getValue()){
					startSizeColIdx++;
					cell = row.createCell((short)startSizeColIdx);
					cell.setCellValue(title);
				}
				startSizeRowIdx++;
			}
		
			if(sizeRowLen > 1){//sizeKind大于等于2需要合并单元格
				if(sizeRowLen > 2){
					Region rg2 = new Region(1, (short) 0, sizeRowLen-2, (short) (preColLen-1));
					sheet1.addMergedRegion(rg2);
					rg2 = new Region(1, (short) (preColLen+sizeColMaxLen+1), sizeRowLen-2, (short) (preColLen + sizeColMaxLen + endColLen));
					sheet1.addMergedRegion(rg2);
				}
				HSSFRow row = sheet1.getRow(sizeRowLen-1);//不能用createRow
				for(int idx=0;idx<preColLen;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(preColNamesList.get(idx).getTitle());
					Region rg = new Region(sizeRowLen-1, (short) idx, sizeRowLen, (short) idx);
					sheet1.addMergedRegion(rg);
					getMethodNames.add("get"+(preColNamesList.get(idx).getField().charAt(0)+"").toUpperCase()+preColNamesList.get(idx).getField().substring(1));
				}
				getMethodNames.add("getSizeKind");
				for(int idx=0;idx<sizeColMaxLen;idx++){
					getMethodNames.add("getV"+(idx+1));
				}
				for(int idx=preColLen+sizeColMaxLen+1;idx<preColLen+sizeColMaxLen+endColLen+1;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle());
					Region rg = new Region(sizeRowLen-1, (short) idx, sizeRowLen, (short) idx);
					sheet1.addMergedRegion(rg);
					getMethodNames.add("get"+(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().charAt(0)+"").toUpperCase()+endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().substring(1));
					if("合计".equals(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle())){
						countIdx = idx;
					}
				}
			}else{
				HSSFRow row = sheet1.getRow(sizeRowLen);//不能用createRow
				for(int idx=0;idx<preColLen;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(preColNamesList.get(idx).getTitle());
					getMethodNames.add("get"+(preColNamesList.get(idx).getField().charAt(0)+"").toUpperCase()+preColNamesList.get(idx).getField().substring(1));
				}
				getMethodNames.add("getSizeKind");
				for(int idx=0;idx<sizeColMaxLen;idx++){
					getMethodNames.add("getV"+(idx+1));
				}
				for(int idx=preColLen+sizeColMaxLen+1;idx<preColLen+sizeColMaxLen+endColLen+1;idx++){
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle());
					getMethodNames.add("get"+(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().charAt(0)+"").toUpperCase()+endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getField().substring(1));
					if("合计".equals(endColNamesList.get(idx-(preColLen+sizeColMaxLen+1)).getTitle())){
						countIdx = idx;
					}
				}
			}
			//填充值
			int dataRowIdx = sizeRowLen+1;
			Object[] arg = new Object[] {};
			Object result = null;
			
			Map<String, BigDecimal> sumMap = new HashMap<String, BigDecimal>();
			
			
			
			for(T t:data){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				for(int idx=0;idx<getMethodNames.size();idx++){
					String getter = getMethodNames.get(idx);
					result = CommonUtil.invokeMethod(t, getter, arg);
					if(result == null){
						continue;
					}
					HSSFCell cell = row.createCell((short)idx);
					cell.setCellValue(result.toString());
					if(idx>preColLen && idx<=(preColLen + sizeColMaxLen)){//计算合计从v1开始
						if(sumMap.get(getter) == null){
							sumMap.put(getter,new BigDecimal(result.toString()));
						}else{
							sumMap.put(getter,sumMap.get(getter).add(new BigDecimal(result.toString())));
						}
					}
				}
				dataRowIdx++;
			}
			if(needSum){
				HSSFRow row = sheet1.createRow(dataRowIdx);
				HSSFCell cell = row.createCell(0);
				cell.setCellValue("总计");
				BigDecimal sum = new BigDecimal(0);
				for(int idx=1;idx<getMethodNames.size();idx++){
					cell = row.createCell((short)idx);
					BigDecimal value = sumMap.get(getMethodNames.get(idx));
					if(value == null){
						continue;
					}
					sum = sum.add(value);
					cell.setCellValue(value.toString());
				}
				cell = row.createCell(countIdx);
				cell.setCellValue(sum.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
		
	}
	
	public static void main(String[] args) {
		
		
		
		
	}
	
	/**
	 * 导出明细第一行显示主表信息Excel
	 * @param titleName  Excel标题名
	 * @param heardData  第一行显示的主表 信息
	 * @param ColumnsList  表头列表
	 * @param data  数据计划
	 * @return
	 * @throws Exception
	 */
	public static <T> HSSFWorkbook getDtlExcel(String titleName,String heardData,List<JqueryDataGrid> ColumnsList,List<T> data) throws Exception{
		//创建excel  
         HSSFWorkbook wb = new HSSFWorkbook();
        //sheet 名字
        HSSFSheet sheet = wb.createSheet(titleName);  
        
        //标题样式
		HSSFCellStyle styleTitle = wb.createCellStyle();
		styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont fontTitle = wb.createFont();
		fontTitle.setFontHeightInPoints((short) 18);
		fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleTitle.setFont(fontTitle);
		styleTitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
       
        //创建第一行样式(显示主表信息)
        HSSFCellStyle styleHead = wb.createCellStyle();
        styleHead.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中  
        HSSFFont fontHead = wb.createFont();
        fontHead.setFontHeightInPoints((short)11);
        
        //创建表头样式  
        HSSFCellStyle styleTabHead = wb.createCellStyle();  
        styleTabHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中  
        styleTabHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中  
		HSSFFont fontTabHead=wb.createFont();
		fontTabHead.setFontHeightInPoints((short)11);
		fontTabHead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleTabHead.setFont(fontTabHead);
		
		//创建标题
		HSSFRow row0 = sheet.createRow((short) 0);
	    //创建标题的单元格
        HSSFCell cellTitle = row0.createCell(0);     
        cellTitle.setCellValue(titleName); // 表格的第一行第一列显示的数据      
        cellTitle.setCellStyle(styleTitle);   
        //合并单元格，参数依次为起始行，结束行，起始列，结束列  
	    sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) ColumnsList.size()-1));  
		//创建第一行(显示主表信息)
	    HSSFRow row1 = sheet.createRow((short)1);
	    HSSFCell cellHead = row1.createCell(0);
	    cellHead.setCellValue(heardData);
	    cellHead.setCellStyle(styleHead);
	    //合并单元格，参数依次为起始行，结束行，起始列，结束列  
	    sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 0, (short) ColumnsList.size()-1));
	    
	    //创建表头
	    HSSFRow rowTabHead = sheet.createRow((short) 2);
	    for(int i=0;i<ColumnsList.size();i++){
	    	HSSFCell cell = rowTabHead.createCell(i);
	    	cell.setCellType(HSSFCell.ENCODING_UTF_16);
	    	cell.setCellValue(ColumnsList.get(i).getTitle().toString());
	    	cell.setCellStyle(styleTabHead);
	    	
	    }
	    //填充数据
	    Object [] arg = new Object[]{};
	    Object result = null;
		for(int j=0;j<data.size();j++){
			HSSFRow row2=sheet.createRow((short)(j+3)); // 第四行开始填充数据 
			T cellData=data.get(j);
			for(int i=0;i<ColumnsList.size();i++){
				HSSFCell cell=row2.createCell((short)i);
				if(ColumnsList.get(i).getField() != null){
					String fieldString = String.valueOf(ColumnsList.get(i).getField());
					fieldString = "get" + fieldString.substring(0,1).toUpperCase() + fieldString.substring(1);
					result = CommonUtil.invokeMethod(cellData, fieldString, arg);
					if(result == null){
						continue;
					}
				}
				
				cell.setCellValue(result.toString());
				//cell.setCellStyle(style2);
			}
		}
		return wb;
	}
}
