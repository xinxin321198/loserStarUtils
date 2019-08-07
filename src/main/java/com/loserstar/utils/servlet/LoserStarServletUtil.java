/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * remarks:httpServlet工具类
 */
public class LoserStarServletUtil{
	

	/**
	 * 获取文件上传路径
	 * @return
	 */
	public static  String getFileLoadPath(HttpServletRequest request) {
		String filePath = getPropertiesPath(request)+"file-service.properties";
		Properties properties = new Properties(); 
		try {
			properties.load(new FileReader(filePath));
			String fileUploadDir = properties.getProperty("kaen.uploaddir","upload");
			return getRealPath(request)+fileUploadDir+File.separator;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 得到项目绝对路径（末尾带斜杠）
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request) {
//		String root = request.getServletContext().getRealPath(File.separator);//jdk1.7
		String root = request.getSession().getServletContext().getRealPath(File.separator);//jdk1.6
		 if (!root.endsWith(java.io.File.separator)) {
		        root = root + java.io.File.separator;
	        }
		return root;
	}
	/**
	 * 得到配置文件目录
	 * @return
	 */
	public static String getPropertiesPath(HttpServletRequest request) {
		return getRealPath(request)+"WEB-INF"+File.separator+"classes"+File.separator;
	}
	
	
	/**
	 * 获取访问用户的客户端IP（适用于公网与局域网）
	 * @param request
	 * @return
	 * @throws Exception
	 */
    public static  String getIpAddr(final HttpServletRequest request)
            throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (ipString==null||ipString.equals("") || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (ipString==null||ipString.equals("")  || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipString==null||ipString.equals("") || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }
     
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }
     
        return ipString;
    }
}
