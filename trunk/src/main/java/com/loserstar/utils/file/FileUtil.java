/**
 * author: loserStar
 * date: 2017年5月5日下午5:19:25
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.loserstar.utils.idgen.IdGen;

/**
 * author: loserStar
 * date: 2017年5月5日下午5:19:25
 * remarks:本系统的文件生成辅助类
 */
public class FileUtil {
	/**
	 * 生成一个以uuid命名的新文件名，保留原后缀
	 * test.doc   ->    uuid.doc
	 * test    ->    uud
	 * @param sourceFilename
	 * @return
	 */
	public static String generateFileName(String sourceFileName){
//		long time = System.currentTimeMillis();
		String newFileName = IdGen.uuid() + getFileNameSuffix(sourceFileName);// 构成新文件名。
		return newFileName;
	}
	
	/**
	 * 提取文件的文件名，不要后缀
	 * test.doc   ->    test
	 * test    ->    test
	 * @param sourceFileName
	 * @return
	 */
	public static String getFileNameNotSuffix(String sourceFileName){
		int i1 = sourceFileName.lastIndexOf("\\");
		int i2 = sourceFileName.lastIndexOf("/");
		int index = 0;
		if (i1>i2) {
			index = i1;
		}else{
			index = i2;
		}
		
		String oldFileName = sourceFileName.indexOf(".") != -1 ? sourceFileName.substring(index+1, sourceFileName.lastIndexOf(".")) : sourceFileName;
		return oldFileName;
	}
	
	/**
	 * 提取文件的文件名，带后缀
	 * test.doc   ->    test.doc
	 * test    ->    test
	 * @param sourceFileName
	 * @return
	 */
	public static String getFileNameWithSuffix(String sourceFileName){
		String fileName = getFileNameNotSuffix(sourceFileName)+getFileNameSuffix(sourceFileName);
		return fileName;
	}
	
	/**
	 * 提取文件的后缀，如果没有后缀返回空字符穿
	 * test.doc   ->    .doc
	 * test    ->    
	 * @param sourceFileName
	 * @return
	 */
	public static String getFileNameSuffix(String sourceFileName){
		String suffix = sourceFileName.indexOf(".") != -1 ? sourceFileName.substring(sourceFileName.lastIndexOf("."), sourceFileName.length()) : "";
		return suffix;
	}
	
	
	/**
	 * 根据一个输入流，传入文件的全路径，生成文件(这个方法的流有问题)
	 * @param inputStream
	 * @param filePath
	 * @throws IOException
	 */
	//XXX loserStar:这个方法的流有问题，不建议使用
	@Deprecated
	public static void outPutFile(InputStream inputStream,String filePath) throws IOException{
		int i1 = filePath.lastIndexOf("\\");
		int i2 = filePath.lastIndexOf("/");
		int index = 0;
		if (i1>i2) {
			index = i1;
		}else{
			index = i2;
		}
		String dir = filePath.substring(0, index+1);
		String fileName = filePath.substring(index+1, filePath.length());
		
		
		BufferedOutputStream bo = null;
		BufferedInputStream bi = null;
		
		try {
			
			if (dir.substring(dir.length()).equals("\\")&&dir.substring(dir.length()).equals("/")) {
				dir = dir+"/";
			}
			File dirFile = new File(dir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File newFile = new File(filePath);
			bo = new BufferedOutputStream(new FileOutputStream(newFile));
			bi = new BufferedInputStream(inputStream);
			int len = 1024;
			byte[] b = new byte[len];
			while ((len = bi.read(b)) != -1)
			{
				bo.write(b, 0, len);
			}
		} catch (Exception e) {
			throw e ;
		}finally{
			bo.flush();
			bi.close();
		}
	}
	
	
	/**
	 * 生成目录
	 * author: loserStar
	 * date: 2017年5月22日下午4:22:01
	 * email:362527240@qq.com
	 * remarks:
	 */
	public static void genDirectory(String localFilePath){
		int i1 = localFilePath.lastIndexOf("\\");
		int i2 = localFilePath.lastIndexOf("/");
		int index = 0;
		if (i1>i2) {
			index = i1;
		}else{
			index = i2;
		}
		String dir = localFilePath.substring(0, index+1);
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
	}
	
	 public static void main(String[] args) {
		 System.out.println(getFileNameNotSuffix("5bab31b8f06640f0a90052484dae49d0.jpg"));
		 File file = new File("e:\\wdtc\\html\\temp\\5bab31b8f06640f0a90052484dae49d0.jpg");
		 if (file.exists()) {
			file.deleteOnExit();;
		}else
		{
			System.out.println("文件不存在");
		}
	 }
}
