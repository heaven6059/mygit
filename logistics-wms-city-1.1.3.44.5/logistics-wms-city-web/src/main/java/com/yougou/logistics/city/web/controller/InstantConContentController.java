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
 * TODO: 即时库存查询
 * 
 * @author su.yq
 * @date 2014-1-24 下午12:09:08
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@Controller
@RequestMapping("/instant_concontent")
public class InstantConContentController extends BaseCrudController<ConContent> {
	
	@Log
	private Logger log;
	
    @Resource
    private ConContentManager conContentManager;
	
    @Resource
	private SizeInfoManager sizeInfoManager;
	@Override
    public CrudInfo init() {
        return new CrudInfo("instantConContent/",conContentManager);
    }
	
	/**
	 * 查询即时库存
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findInstantConContentList")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> findInstantConContentList(ConContentDto dto,String exportType, HttpServletRequest req,HttpServletResponse response) {
		Map<String,Object> obj=new HashMap<String,Object>();
    	//int total=0;
		//List<ConContentDto> listCC = new ArrayList<ConContentDto>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			//total = conContentManager.findInstantConContentCount(dto);
			//SimplePage page = new SimplePage(pageNo, pageSize, total);
			
			if(dto.getItemNo() != null) {
				dto.setItemNo(dto.getItemNo().toUpperCase());
			}
			if(dto.getBarcode() != null) {
				dto.setBarcode(dto.getBarcode().toUpperCase());
			}
			
			//大类一
			String cateOne = (String) req.getParameter("cateOne");
			String[] cateOneValues = null;
			if (StringUtils.isNotEmpty(cateOne)) {
				cateOneValues = cateOne.split(",");
			}
			dto.setCateOneValues(cateOneValues);
			
			//大类二
			String cateTwo = (String) req.getParameter("cateTwo");
			String[] cateTwoValues = null;
			if (StringUtils.isNotEmpty(cateTwo)) {
				cateTwoValues = cateTwo.split(",");
			}
			dto.setCateTwoValues(cateTwoValues);
			
			//大类三
			String cateThree = (String) req.getParameter("cateThree");
			String[] cateThreeValues = null;
			if (StringUtils.isNotEmpty(cateThree)) {
				cateThreeValues = cateThree.split(",");
			}
			dto.setCateThreeValues(cateThreeValues);
			
			dto.setPageNo(pageNo);
			dto.setPageSize(pageSize);
			
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
			dto.setSeasonValues(seasonValues);
			dto.setGenderValues(genderValues);
			obj = conContentManager.findInstantConContentByPage(dto, authorityParams, false);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ResponseEntity<Map<String,Object>>(obj,HttpStatus.OK);
	}
	
    /**
	 * 参数处理
	 * @param params
	 */
	private void paramsHandle(Map<String, Object> params) {
//		//品牌
//		String brandNo = (String) params.get("brandNo");
//		String[] brandNoValues = null;
//		if (StringUtils.isNotEmpty(brandNo)) {
//			brandNoValues = brandNo.split(",");
//		}
//		params.put("brandNoValues", brandNoValues);
		
		//大类一
		String cateOne = (String) params.get("cateOne");
		String[] cateOneValues = null;
		if (StringUtils.isNotEmpty(cateOne)) {
			cateOneValues = cateOne.split(",");
		}
		params.put("cateOneValues", cateOneValues);		
		
		//大类二
		String cateTwo = (String) params.get("cateTwo");
		String[] cateTwoValues = null;
		if (StringUtils.isNotEmpty(cateTwo)) {
			cateTwoValues = cateTwo.split(",");
		}
		params.put("cateTwoValues", cateTwoValues);		
		
		//大类三
		String cateThree = (String) params.get("cateThree");
		String[] cateThreeValues = null;
		if (StringUtils.isNotEmpty(cateThree)) {
			cateThreeValues = cateThree.split(",");
		}
		params.put("cateThreeValues", cateThreeValues);			
		
//
//		//品质
//		String quality = (String) params.get("quality");
//		String[] qualityValues = null;
//		if (StringUtils.isNotEmpty(quality)) {
//			qualityValues = quality.split(",");
//		}
//		params.put("qualityValues", qualityValues);
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
			//大类一
			String cateOne = (String) req.getParameter("cateOne");
			String[] cateOneValues = null;
			if (StringUtils.isNotEmpty(cateOne)) {
				cateOneValues = cateOne.split(",");
			}
			dto.setCateOneValues(cateOneValues);
			
			//大类二
			String cateTwo = (String) req.getParameter("cateTwo");
			String[] cateTwoValues = null;
			if (StringUtils.isNotEmpty(cateTwo)) {
				cateTwoValues = cateTwo.split(",");
			}
			dto.setCateTwoValues(cateTwoValues);
			
			//大类三
			String cateThree = (String) req.getParameter("cateThree");
			String[] cateThreeValues = null;
			if (StringUtils.isNotEmpty(cateThree)) {
				cateThreeValues = cateThree.split(",");
			}
			dto.setCateThreeValues(cateThreeValues);
			
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
			dto.setSeasonValues(seasonValues);
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
			obj = conContentManager.findInstantConContentByPage(dto, authorityParams, true);
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
