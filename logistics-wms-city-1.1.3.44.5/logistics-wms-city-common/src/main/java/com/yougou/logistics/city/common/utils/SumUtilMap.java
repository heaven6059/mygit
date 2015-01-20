package com.yougou.logistics.city.common.utils;

import java.util.HashMap;

public class SumUtilMap<K, V> extends HashMap<K, V> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public V get(Object key) {
	return super.get(key);
    }

    @Override
    public V put(K key, V value) {
	String curKey = replaceUnderlineAndfirst2Upper(String.valueOf(key)
		.toLowerCase(), "_", "");
	return super.put((K) curKey, value);
    }

    private String replaceUnderlineAndfirst2Upper(String srcStr, String org,
	    String ob) {
	String newString = "";
	int first = 0;
	while (srcStr.indexOf(org) != -1) {
	    first = srcStr.indexOf(org);
	    if (first != srcStr.length()) {
		newString = newString + srcStr.substring(0, first) + ob;
		srcStr = srcStr
			.substring(first + org.length(), srcStr.length());
		srcStr = firstCharacterToUpper(srcStr);
	    }
	}
	newString = newString + srcStr;
	return newString;
    }

    private String firstCharacterToUpper(String srcStr) {
	return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }
}
