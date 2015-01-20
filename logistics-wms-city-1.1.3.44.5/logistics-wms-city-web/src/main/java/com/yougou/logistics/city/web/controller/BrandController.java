package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.enums.DataAccessRuleEnum;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.dto.baseinfo.BrandDTO;
import com.yougou.logistics.city.common.model.BillOmDivide;
import com.yougou.logistics.city.common.model.Brand;
import com.yougou.logistics.city.manager.BrandManager;
import com.yougou.logistics.city.web.utils.UserLoginUtil;

@Controller
@RequestMapping("/brand")
@ModuleVerify("25030020")
public class BrandController extends BaseCrudController<Brand> {
    @Resource
    private BrandManager brandManager;
    @Log
	private Logger log;
    @Override
    public CrudInfo init() {
        return new CrudInfo("brand/",brandManager);
    }
    
      
    /**
	 * 获取树数据
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value="/queryBrand")
	@ResponseBody
	public  List<BrandDTO> queryBrand(HttpServletRequest req, String id) throws ManagerException {
		List<BrandDTO> obj = null;
		
		try {
//			result = new ModelAndView("jsonView");
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		
			obj = brandManager.findBrand(authorityParams);
			//ArrayList<String> data=new ArrayList<String>();
			String str=new String();
			
//			StringBuffer str=new StringBuffer();
//			for(int i=0;i<result.size();i++){
//				str.append("<option value="+"'"+i+"'>"+result.get(i).getBrandName()+"→"+result.get(i).getBrandNo());
//				str.append("</option>");
//			}
//			for(BrandDTO brandDTO:result){
//				str=brandDTO.getBrandName()+'→'+brandDTO.getBrandNo();
//				obj.add(str);
//				
//			}
			
			
		} catch (Exception e) {
			
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
	
	
    
    @RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException {
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
			int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
			int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
			String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
			String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
			Map<String, Object> params = builderParams(req, model);
			int total = this.brandManager.findCount(params,authorityParams, DataAccessRuleEnum.BRAND);
			SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
			List<BillOmDivide> list = this.brandManager.findByPage(page, sortColumn, sortOrder, params,authorityParams, DataAccessRuleEnum.BRAND);
			
			obj.put("total", total);
			obj.put("rows", list);
			obj.put("result", "success");
		} catch (Exception e) {
			obj.put("rows", "");
			obj.put("result", "fail");
			obj.put("msg", e.getCause().getMessage());
			log.error(e.getMessage(), e);
		}
		return obj;
	}
    
	/**
	 * 获取树数据
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value="/queryBrandTree")
	@ResponseBody
	public ModelAndView queryBrandTree(HttpServletRequest req, String id) throws ManagerException {
		ModelAndView result = null;
		try {
			result = new ModelAndView("jsonView");
			AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		
			List<BrandDTO> tempList = brandManager.queryBrandListByParentNo(id,authorityParams);
			result.addObject(tempList);
		} catch (ManagerException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}
    
	@RequestMapping(value = "/get_bizDy")
	@ResponseBody
	public List<Brand>  get_bizDy(HttpServletRequest req,Model model)throws ManagerException{
		Map<String,Object> params=builderParams(req, model);
		AuthorityParams authorityParams = UserLoginUtil.getAuthorityParams(req);
		List<Brand> list = this.brandManager.findMyselfByParams(params, authorityParams);
		return list;
	}

	
}