package com.yougou.logistics.city.web.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 公共js文件加载
 * 
 */
public class CommonJSUtils {
    private static Logger logger = Logger.getLogger(CommonJSUtils.class);
	
	/**
     * 取出资源文件的路径
     * @param obj
     * @return
     * @throws Exception
     */
	public static String getResourceFilePath() throws Exception {
    	String path=CommonJSUtils.class.getClassLoader().getResource("")+"";
    	path=path.replace("file:/", "");
		if(!"/".equals(path.substring(0, 1))){
			path="/"+path;
		}
		//logger.info("path:"+path);
		return path;
    }

    /**
     * 加载公共字典js文件到项目
     */
	public static void creatJSConfigFile(String fileName, String content) {
    	logger.info("creatJSConfigFile() start.");
        try {
        	String targetpath=getResourceFilePath().replace("/WEB-INF/classes", "")+"resources/js/lookupdtl";
        	String targetFile=targetpath+"/"+fileName;
        	createJSpathDirectory(targetpath);
        	writeFile(targetFile, content,false);
        	logger.info("creatJSConfigFile() targetFile:"+targetFile);
        } catch (Exception e) {
            logger.error("creatJSConfigFile() error:",e);
        }
        logger.info("creatJSConfigFile() end. ");
    }
	
	/**
	 * 文件夹不存在则创建  
	 * @param targetpath
	 */
	private static void createJSpathDirectory(String targetpath){
		File file =new File(targetpath);    
		if  (!file .exists()  && !file .isDirectory()){
		    file .mkdir();    
		    logger.info("createJSpathDirectory targetpath:"+targetpath);  
		}
	}
	
	/*public static void main(String[] args) {
		createJSpathDirectory(null);
	}*/
	
	/**
	 * 写文件
	 * @param fileName
	 * @param content
	 * @param append true表示以追加形式写文件
	 */
	public static void writeFile(String fileName, String content,boolean append ) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName, append);
			writer.write(content);
		} catch (IOException e) {
			logger.error("writeFile() error:",e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}

			} catch (IOException e) {
				logger.error("writeFile() error:",e);
			}

		}
	}
    
}
