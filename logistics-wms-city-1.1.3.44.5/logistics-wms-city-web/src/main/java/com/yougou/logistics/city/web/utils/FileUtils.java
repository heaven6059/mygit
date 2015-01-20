package com.yougou.logistics.city.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.yougou.logistics.base.service.log.Log;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-6-13 下午5:30:34
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class FileUtils {
	@Log
	private static Logger log;

	/**
	 * 获取导入模板
	 * @param req
	 * @param session
	 * @param response
	 * @throws Exception
	 */
	public static void downloadTemple(HttpSession session, HttpServletResponse response, String templeName) throws Exception {
		response.setContentType("application/vnd.ms-excel");
		//文件名
		response.setHeader("Content-Disposition", "attachment;filename="+templeName);
		response.setHeader("Pragma", "no-cache");
		String realPath = session.getServletContext().getRealPath("/");
		File file = new File(realPath + "template/" + templeName);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = response.getOutputStream();
			byte[] temp = new byte[1024 * 1024];
			int len = 0;
			while ((len = in.read(temp)) != -1) {
				out.write(temp);
			}
			out.flush();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (Exception e) {
					in = null;
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					out = null;
				}
			}
		}
	}

}
