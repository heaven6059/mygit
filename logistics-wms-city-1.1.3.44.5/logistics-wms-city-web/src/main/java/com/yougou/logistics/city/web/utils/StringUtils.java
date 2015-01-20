package com.yougou.logistics.city.web.utils;

public class StringUtils {
	public static boolean isNumber(String str) {//判断小数，与判断整型的区别在与d后面的小数点（红色）
		   return str.matches("[\\d.]+");
		}
}
