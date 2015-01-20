package com.yougou.logistics.city.common.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

public class SumUtil {

	
	public static void setFooterMap(String key, BigDecimal val, Map<String, Object> footerMap) {
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
	
	public static void setSumMap(Map<String, Object> map,Map<String, Object> sumFoot){
		if(map == null || map.size() == 0 || sumFoot == null){
			return;
		}
		String key;
		Object value;
		for(Entry<String, Object> m:map.entrySet()){
			key = m.getKey();
			value = m.getValue();
			key = key.toLowerCase();
			int idx = key.indexOf("_");
			int len = key.length();
			while(idx >= 0 && idx < len-1){
				char c = key.charAt(idx+1);
				if(c >= 'a' && c <= 'z'){
					c = (char)(c - 32);
				}
				key = key.substring(0,idx) + c + ((idx+2)<len-1?key.substring(idx+2):"");
				idx = key.indexOf("_");
				len = key.length();
			}
			if(idx > 0){
				key = key.substring(0,idx);
			}
			sumFoot.put(key, value);
		}
	}
}
