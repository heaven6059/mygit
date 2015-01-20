package com.yougou.logistics.city.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.ConContentDto;
import com.yougou.logistics.city.common.model.ConContent;
import com.yougou.logistics.city.common.model.SizeInfo;
import com.yougou.logistics.city.common.vo.jqueryDataGrid.JqueryDataGrid;
import com.yougou.logistics.city.manager.ConContentManager;
import com.yougou.logistics.city.manager.SizeInfoManager;
import com.yougou.logistics.city.web.utils.ExcelUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

/**
 * TODO: 分仓库存明细
 * 
 * @author jiang.ys
 */
@Controller
@RequestMapping("/divide_loc_concontent")
public class DivideLocConContentController extends BaseCrudController<ConContent> {
	
	@Log
	private Logger log;
	
    @Resource
    private ConContentManager conContentManager;
	
    @Resource
	private SizeInfoManager sizeInfoManager;
    
	@Override
    public CrudInfo init() {
        return new CrudInfo("dividelocconcontent/",conContentManager);
    }
	
	
	@RequestMapping(value = "/cclist")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> ccList(ConContentDto dto, HttpServletRequest req,HttpServletResponse response) {
		Map<String,Object> obj=new HashMap<String,Object>();
    	
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			dto.setPageNo(pageNo);
			dto.setPageSize(pageSize);
			if(dto.getItemNo() != null) {
				dto.setItemNo(dto.getItemNo().toUpperCase());
			}
			if(dto.getBarcode() != null) {
				dto.setBarcode(dto.getBarcode().toUpperCase());
			}
			//性别
			String season = (String)req.getParameter("seasonName");
			String [] seasonValues = null;
			if(StringUtils.isNotBlank(season)){
				seasonValues = season.split(",");
			}
			dto.setSeasonValues(seasonValues);
			//季节
			String gender = (String)req.getParameter("genderName");
			String [] genderValues = null;
			if(StringUtils.isNotBlank(gender)){
				genderValues = gender.split(",");
			}
			dto.setGenderValues(genderValues);
			obj = conContentManager.findDivideLocConContentByPage(dto, authorityParams, false);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/doExport")
	public void doExport(ConContentDto dto, HttpServletRequest req,HttpServletResponse response) throws IOException{
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
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysNo", dto.getSysNo());
		try {
			if(dto.getItemNo() != null) {
				dto.setItemNo(dto.getItemNo().toUpperCase());
			}
			if(dto.getBarcode() != null) {
				dto.setBarcode(dto.getBarcode().toUpperCase());
			}
			//性别
			String season = (String)req.getParameter("seasonName");
			String [] seasonValues = null;
			if(StringUtils.isNotBlank(season)){
				seasonValues = season.split(",");
			}
			dto.setSeasonValues(seasonValues);
			//季节
			String gender = (String)req.getParameter("genderName");
			String [] genderValues = null;
			if(StringUtils.isNotBlank(gender)){
				genderValues = gender.split(",");
			}
			dto.setGenderValues(genderValues);
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
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			Map<String,Object> obj=new HashMap<String,Object>();
			obj = conContentManager.findDivideLocConContentByPage(dto, authorityParams, true);
			List<ConContentDto> data = (List<ConContentDto>) obj.get("rows");
			HSSFWorkbook wb = ExcelUtils.getExcle4Size(preColNamesList, sizeTypeMap, endColNamesList, fileName, data, true);
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1") + ".xls");
			response.setHeader("Pragma", "no-cache");
			wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (ManagerException e) {
			e.printStackTrace();
		}
	}
}
