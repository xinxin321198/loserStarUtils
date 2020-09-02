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
		/*       String ipString = request.getHeader("x-forwarded-for");
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
		}*/
        String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {  
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        } 
     
        return ip;
    }
    
    /**
     * 获取服务器名，localhost；
     * @return
     */
    public static String getServerName(final HttpServletRequest request) {
    	return request.getServerName();
    }

    /**
     *     获取服务器端口号，8080；
     * @param request
     * @return
     */
    public static int getServerPort(final HttpServletRequest request) {
    	return request.getServerPort();
    }
    /**
     * ：获取项目名，/Example；
     * @param request
     * @return
     */
    public static String getContextPath(final HttpServletRequest request) {
    	return request.getContextPath();
    }
    /**
     * 获取Servlet路径，/AServlet；
     * @return
     */
    public static String getServletPath(final HttpServletRequest request) {
    	return request.getServletPath();
    }
    /**
     * 获取参数部分，即问号后面的部分：username=zhangsan
     * @return
     */
    public static String getQueryString(final HttpServletRequest request) {
    	return request.getQueryString();
    }
    
    /**
     * 获取请求URI，等于项目名+Servlet路径：/Example/AServlet
     * @return
     */
    public static String getRequestURI(final HttpServletRequest request) {
    	return request.getRequestURI();
    }
	/**
	 * 获取请求URL，等于不包含参数的整个请求路径：http://localhost:8080/Example/AServlet 。
	 * @return
	 */
    public static  String getRequestURL(final HttpServletRequest request) {
    	return request.getRequestURL().toString();
    }
}
