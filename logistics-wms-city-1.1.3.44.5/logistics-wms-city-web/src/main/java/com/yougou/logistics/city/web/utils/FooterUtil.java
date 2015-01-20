package com.yougou.logistics.city.web.utils;

import java.math.BigDecimal;
import java.util.Map;

public class FooterUtil {
	public static void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap)
			throws Exception {
		BigDecimal count = null;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (BigDecimal) footerMap.get(key);
			if (null != val) {
				count = count.add(val);
			}
		}
		footerMap.put(key, count);
	}
	
	public static void setFooterMapByInt(String key, int val, Map<String, Object> footerMap)
			throws Exception {
		int count = 0;
		if (null == footerMap.get(key)) {
			count = val;
		} else {
			count = (Integer) footerMap.get(key);
			count += val;
		}
		footerMap.put(key, count);
	}
}
