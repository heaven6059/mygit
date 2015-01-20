package com.yougou.logistics.city.common.tempdata;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步上传文件存储空间
 * 
 * @author jiang.ys
 * @date 2014-1-14 上午11:09:19
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class TempFile {

	private static Map<String, Map<String, Object>> TEMPFILE = new HashMap<String, Map<String, Object>>();
	
	public static void writeFile(String user,String fileName,byte [] buffer){
		Map<String, Object> userMap = TEMPFILE.get(user);
		if(userMap == null){
			userMap = new HashMap<String, Object>();
		}
		userMap.put(fileName, buffer);
		TEMPFILE.put(user, userMap);
	}
	public static byte[] readFile(String user,String fileName){
		Map<String, Object> userMap = TEMPFILE.get(user);
		if(userMap == null){
			return null;
		}else{
			byte[] b = (byte[]) userMap.get(fileName);
			return b;
		}
	}
	public static void removeByUser(String user){
		TEMPFILE.put(user, null);
	}
}
