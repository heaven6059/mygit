package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yougou.logistics.base.common.model.AuthorityParams;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.LookupDtl;
import com.yougou.logistics.city.manager.common.CommonUtilManager;
import com.yougou.logistics.city.service.AuthorityUserBrandService;
import com.yougou.logistics.city.web.utils.CommonJSUtils;
import com.yougou.logistics.city.web.utils.UserLoginUtil;
import com.yougou.logistics.uc.common.api.model.AuthorityUserBrand;

/**
 * 1.tomcat容器初始化后加载些数据,如字典加载到JS静态文件作缓存 
 * 
 * @author wugy
 * @date 2014-11-25
 * @copyRight yougou.com
 */
@Controller
@RequestMapping("/initjsCache")
public class InitJSCacheController implements InitializingBean {
	@Value("${db.schema}")
	private String schema;

	// 加载字典
	public Map<String, List<LookupDtl>> lookupdMap = com.yougou.logistics.city.common.utils.SystemCache.lookupdMap;
	// 加载表的字段 用户请求时某张表第一需加载,放到Map中,第二次再请求时直接从Map取
	// 但是这样存在一个问题,当数据库中表增加了一列的话,需从新加载
	// 暂时的思路 在界面上加载一个刷新的按钮,一点刷新的 就会刷新所有Map里面已存在的Key(表)
	@Resource
	private CommonUtilManager commonUtilManager;
	@Resource
	private AuthorityUserBrandService authorityUserBrandService;

	@Log
	private Logger log;

	/**
	 * 初始化
	 */
	public void init() {
		log.info(("initjsCache init() begin."));
		//JS静态字典
		initLookupdMapJsFile();
		log.info(("initjsCache init() end."));
	}


	/**
	 * 初始化加载字典数据
	 */
	private void reloadInitLookupdMap() {
		try {
			long starttime=System.currentTimeMillis();
			lookupdMap.clear();
			log.info("reloadInitLookupdMap() begin");
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

			log.info(("reloadInitLookupdMap() end LookupcodeSize：" + lookupdMap.size())+" usedtimes:"+(System.currentTimeMillis()-starttime)+"ms");
		} catch (Exception e) {
			log.info("reloadInitLookupdMap() error",e);
		}
	}
	
	
	/**
	 * 创建JS静态字典数据
	 */
	@RequestMapping(value = "/createInitLookupdMapJsFile")
	@ResponseBody
	public  Map<String, Object>  createInitLookupdMapJsFile(String lookupcode,HttpServletRequest req) {
		String res="true";
		Map<String, Object> obj = new HashMap<String, Object>();
		try {
			
			if(StringUtils.isNotEmpty(lookupcode)){
				//更新单个js文件
				ObjectMapper objectMapper =new ObjectMapper();
				CommonJSUtils.creatJSConfigFile("lookupcode_"+lookupcode+".js", objectMapper.writeValueAsString(lookupdMap.get(lookupcode)));
				objectMapper = null;
			}
			else{
				//全量更新
				initLookupdMapJsFile();
			}
		} catch (Exception e) {
			log.info(("createInitLookupdMapJsFile() error:"),e);
			res="false";
		}
		obj.put("res", res);
		return obj;
	}
	
	/**
	 * 初始化JS静态字典
	 */
	@SuppressWarnings("rawtypes")
	public void  initLookupdMapJsFile() {
		try {
			long starttime=System.currentTimeMillis();
			log.info(("initLookupdMapJsFile() begin"));
			//加载字典数据到map
			reloadInitLookupdMap();
			
			if (lookupdMap!=null) {
			    ObjectMapper objectMapper =new ObjectMapper();
			    Iterator iter = lookupdMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					CommonJSUtils.creatJSConfigFile("lookupcode_"+entry.getKey()+".js", objectMapper.writeValueAsString(entry.getValue()));
				}
			 objectMapper = null;
			}

			log.info(("initLookupdMapJsFile() end. listDtls.size：" + lookupdMap.size())+" usedtimes:"+(System.currentTimeMillis()-starttime)+"ms");
		} catch (Exception e) {
			log.info(("initLookupdMapJsFile() error:"),e);
		}
	}
	
	/**
	 * 获得用户拥有的品牌
	 * @param req
	 * @param lookupcode
	 * @return
	 */
	@RequestMapping(value = "/getLookupDtlsUserBrandList")
	@ResponseBody
	public List<AuthorityUserBrand> getLookupDtlsUserBrandList(HttpServletRequest req,String lookupcode) {
		AuthorityParams authorityParams = UserLoginUtil
				.getAuthorityParams(req);
		String userId = authorityParams.getUserId();
		String systemId = authorityParams.getSystemNoVerify();
		String areaSystemId = authorityParams.getAreaSystemNoVerify();
		List<AuthorityUserBrand> tempConfigDataList = null;
		try {
			tempConfigDataList = authorityUserBrandService.findByUserId(userId,Integer.valueOf(systemId),Integer.valueOf(areaSystemId));
			
		} catch (Exception e) {
			log.info(("getLookupDtlsUserBrandList() error:"),e);
		}
		return tempConfigDataList;
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

}
