/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * remarks:
 */
public class LoserStarServletBase extends HttpServlet{

protected HttpServletRequest request;
protected HttpServletResponse response;

	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.request = req;
		this.response = resp;
		super.service(req, resp);
	}

	/**
	 * 获取文件上传路径
	 * @return
	 */
	protected String getFileLoadPath() {
		return LoserStarServletUtil.getFileLoadPath(request);
	}
	
	/**
	 * 得到项目绝对路径（末尾带斜杠）
	 * @return
	 */
	protected String getRealPath() {
		return LoserStarServletUtil.getRealPath(request);
	}
	/**
	 * 得到配置文件目录
	 * @return
	 */
	protected String getPropertiesPath() {
		return LoserStarServletUtil.getPropertiesPath(request);
	}
	
	
	

	/**
	 * 上传文件，返回新的文件名
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileUploadException 
	 */
	protected String uploadFile( ) throws IOException, FileUploadException {
		return LoserStarServleFileUtil.uploadFile(request, response);
	}
	
	/**
	 * 上传文件到某个绝对路径
	 * @param uploadDir 系统的绝对路径
	 * @param file 文件对象
	 * @return
	 * @throws IOException
	 */
	protected String uploadFile(String uploadDir,FileItem file) throws IOException {
		return LoserStarServleFileUtil.uploadFile(uploadDir, file);
	}
	/**
	 * 下载配置文件配置的路径下的某个文件
	 * @param fileUrl 文件的uuid文件名（带后缀）
	 * @param downName 下载时的名称
	 * @throws Exception 
	 */
	protected void downloadPropertiesDirFile(String fileUrl,String downName) throws Exception {
		LoserStarServleFileUtil.downloadPropertiesDirFile(fileUrl, downName,request,response);
	}
	
	/**
	 * 下载文件，指定一个文件的绝对路径以及下载时显示的文件名
	 * @param downloadFilePath 文件的绝对路径
	 * @param downName 下载时的名称,为null时默认取真实的该文件名称
	 * @throws Exception
	 */
	protected void downloadFile(String downloadFilePath,String downName) throws Exception {
		LoserStarServleFileUtil.downloadFile(downloadFilePath, downName, response);
	}
	
	/**
	 * 获取访问用户的客户端IP（适用于公网与局域网）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected  String getIpAddr(final HttpServletRequest request)
            throws Exception {
        return LoserStarServletUtil.getIpAddr(request);
    }
}
