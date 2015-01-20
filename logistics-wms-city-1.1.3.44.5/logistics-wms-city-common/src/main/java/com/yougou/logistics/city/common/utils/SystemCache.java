package com.yougou.logistics.city.common.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yougou.logistics.city.common.vo.LookupDtl;

/**
 * TODO: 增加描述
 * 
 * @author luo.hl
 * @date 2014-2-12 下午2:19:17
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class SystemCache {
	public static Map<String, List<LookupDtl>> lookupdMap = new LinkedHashMap<String, List<LookupDtl>>();

	public static String getLookUpName(String code, String value) {
		List<LookupDtl> lookupdtlList = lookupdMap.get(code);
		for (LookupDtl dtl : lookupdtlList) {
			if (dtl.getItemvalue().equals(value)) {
				return dtl.getItemname();
			}
		}
		return "";
	}
}
