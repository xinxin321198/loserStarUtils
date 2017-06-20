package com.loserstar.utils.ftp;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;



/**
 * ftp上传、下载工具类
 *
 * @author liuyu
 *
 */
public class FtpUtil {

	/**
	 * ftp文件递归器，由recursionAllFile方法调用，采用深度优先的方式遍历所有文件
	 * @author liuyu
	 *
	 */
	public static abstract class FtpFileRecursioner{
		private boolean needNext = true;

		/**
		 * 访问一个文件
		 * @param f
		 */
		public abstract void visit(String path,FTPFile f);

		public boolean skipFilePath(String path){
			return false;
		}

		/**
		 * 停止递归
		 */
		public void stop(){
			needNext = false;
		};
	}

	private FTPClient ftpClient;

	/**
	 * 连接并登录至ftp服务器
	 *
	 * @param host
	 *            可以带端口号
	 * @param usr
	 *            ftp用户
	 * @param pwd
	 *            ftp密码
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @throws ProjectException
	 *             登录失败时抛出
	 */
	public void login(String host, String usr, String pwd) throws NumberFormatException, Exception {
		if(host.contains(":")) {
			String[] hostPart = host.split(":");
			login(hostPart[0], Integer.parseInt(hostPart[1]), usr, pwd);
		} else {
			login(host, null, usr, pwd);
		}
	}

	/**
	 * 连接并登录至ftp服务器
	 *
	 * @param ip
	 *            ip,不用带端口号
	 * @param port
	 *            端口号
	 * @param usr
	 *            ftp用户
	 * @param pwd
	 *            ftp密码
	 * @throws Exception 登录失败时抛出
	 */
	public void login(String ip, Integer port, String usr, String pwd) throws Exception {
		try {
			ftpClient = new FTPClient();
			if(port == null) {
				ftpClient.connect(ip);
			} else {
				ftpClient.connect(ip, port);
			}
			ftpClient.login(usr, pwd);
		} catch(Exception e) {
			throw new Exception("ftp登录失败", e);
		}
	}

	/**
	 * 上传
	 *
	 * @param fis
	 *            需上传的文件流
	 * @param dir
	 *            上传至ftp目录
	 * @param name
	 *            上传后的文件没
	 * @param encoding
	 *            编码格式，为空则使用默认值"GBK"
	 * @param fileType
	 *            文件类型，为空则使用默认值"FTPClient.BINARY_FILE_TYPE"
	 * @throws Exception 
	 */
	public boolean upload(InputStream fis, String dir, String name, String encoding, Integer fileType) throws Exception {
		if(null == encoding) {
			encoding = "GBK";
		}
		if(null == fileType) {
			fileType = FTPClient.BINARY_FILE_TYPE;
		}
		try {
			ftpClient.changeWorkingDirectory(new String(dir.getBytes(encoding), "iso-8859-1"));
			ftpClient.setControlEncoding(encoding);
			ftpClient.setFileType(fileType);
			return ftpClient.storeFile(new String(name.getBytes(encoding), "iso-8859-1"), fis);
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("FTP客户端出错", e);
		}
	}

	/**
	 * 下载(这个方法不能用，文件流都没关闭)
	 *
	 * @param fos
	 *            需要下载的文件流
	 * @param remoteFileName
	 *            ftp文件路径+文件名
	 * @param encoding
	 *            编码格式，为空则使用默认值"GBK"
	 * @param fileType
	 *            文件类型，为空则使用默认值"FTPClient.BINARY_FILE_TYPE"
	 * @throws Exception 
	 */
	@Deprecated
	public boolean download(OutputStream fos, String remoteFileName, String encoding, Integer fileType) throws Exception {
		if(null == encoding) {
			encoding = "GBK";
		}
		if(null == fileType) {
			fileType = FTPClient.BINARY_FILE_TYPE;
		}
		try {
			// 设置文件类型（二进制）
			ftpClient.setFileType(fileType);
			return ftpClient.retrieveFile(new String(remoteFileName.getBytes(encoding), "iso-8859-1"), fos);
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("FTP客户端出错", e);
		}
	}

	/**
	 * 
	 * author: loserStar
	 * date: 2017年5月22日下午5:22:27
	 * email:362527240@qq.com
	 * remarks: ftp下载文件
	 * @param localPath 本地文件名全路径（保证路径存在）
	 * @param remoteFileName 远程文件名
	 * @param remoteDir 远程目录
	 * @return
	 * @throws Exception
	 */
	public void download2(String localPath,String remoteFileName) throws Exception {
	        BufferedOutputStream outStream = null;  
	        boolean success = false;  
	        try {  
//	            this.ftpClient.changeWorkingDirectory(remoteDir);  //好像没必要用这句也行
	            outStream = new BufferedOutputStream(new FileOutputStream(  
	            		localPath));  
	            System.out.println(remoteFileName + "开始下载....远程文件路径为:"+remoteFileName);  
	            success = this.ftpClient.retrieveFile(remoteFileName, outStream);  
	            if (success == true) {  
	            	System.out.println(remoteFileName + "成功下载到本地：" +localPath);  
	            }  else{
	            	throw new Exception("ftp路径"+remoteFileName+"可能没有此文件");
	            }
	        } catch (Exception e) {  
	            throw e;
	        } finally {  
	            if (null != outStream) {  
	                try {  
	                    outStream.flush();  
	                    outStream.close();  
	                } catch (IOException e) {  
	                    throw e;
	                }  
	            }  
	        }  
	}

	public boolean fileExist(String fileName) {
		FTPFile[] f = null;
		try {
			f = ftpClient.listFiles(new String(fileName.getBytes("GBK"), "iso-8859-1"));
		} catch(Exception e) {

		}
		return null != f && f.length > 0;
	}

	public void close() throws Exception {
		if(null == ftpClient) {
			return;
		}
		try {
			ftpClient.disconnect();
		} catch(IOException e) {
			e.printStackTrace();
			throw new Exception("关闭FTP连接发生异常", e);
		}
	}

	/**
	 * 递归遍历指定目录下的所有文件
	 * @param startPath 起始目录，若为空，则从根目录"/"开始遍历
	 * @param recursioner
	 * @throws Exception 
	 */
	public void recursionAllFile(String startFile,FtpFileRecursioner recursioner) throws Exception{
		if(null==startFile){
			startFile = "/";
		}else{
			startFile+="/";
		}
		try {
			FTPFile[] fs = ftpClient.listFiles(startFile);
			for(FTPFile f:fs){
				if(!recursioner.needNext){
					return;
				}
				if(f.isFile()){
					recursioner.visit(startFile,f);
				}else if(f.isDirectory()){
					String directoryName = f.getName();
					int n = directoryName.length();
					int i = 0;
					for(;i<n;i++){
						if(directoryName.charAt(i)!='.'){
							break;
						}
					}
					if(i==n){
						continue;
					}
					String nextPath = startFile+directoryName;
					if(!recursioner.skipFilePath(nextPath)){
						recursionAllFile(nextPath, recursioner);
					}

				}
			}
		} catch(Exception e) {
			throw new Exception("遍历文件时产生异常", e);
		}
	}

	/**
	 * 得到ftpClient，进行精细操作
	 * @return
	 */
	public FTPClient getClient(){

		return ftpClient;
	}

	/**
	 * 列出Ftp服务器上的所有文件和目录
	 * @param remotePath
	 */
	public List<FTPFile> listRemoteAllFiles(String remotePath) {
		List<FTPFile> ftpFiles = new ArrayList<>();
		try {
			FTPFile[] files = ftpClient.listFiles(remotePath);
			for(int i = 0; i < files.length; i++) {
				if(files[i].isFile()) {
					ftpFiles.add(files[i]);
				} else if(files[i].isDirectory()) {
//					ftpFiles.addAll(listRemoteAllFiles(remotePath + files[i].getName() + "/"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ftpFiles;
	}
}
