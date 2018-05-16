/**
 * author: loserStar
 * date: 2018年3月13日下午2:24:54
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.UUID;

/**
 * author: loserStar
 * date: 2018年3月13日下午2:24:54
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarFileUtil {
	/**
	 * 以字符流的方式从一个文件路径读取文件的字符(二进制读出来肯定是乱码，不用想，二进制文件要使用字节流的方式)
	 *
	 * @param filePath
	 * @return
	 */
	public static String ReadReaderForFile(String filePath){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			FileReader fileReader = new FileReader(new File(filePath));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine())!=null) {
				stringBuffer.append(line);
			}
			bufferedReader.close();
			System.out.println("ReaderForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 从某个文件中读取对象到内存中(泛型)
	 * @return 
	 */
	public static <T> T ReadObject(String objectFilePath,Class<T> class1){
		try {
			InputStream inputStream = new FileInputStream(new File(objectFilePath));
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			@SuppressWarnings("unchecked")
			T resultObject = (T)objectInputStream.readObject();
			objectInputStream.close();
			System.out.println("readObject end");
			return resultObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 以字节的方式读取一个流中的内容（只读一次，传入读取的大小）
	 * @param inputStream
	 * @param length 
	 * @return
	 */
	public static byte[] ReadByteForInputStream(InputStream inputStream,int length){
		try {
			//利用字节流读取文件
			byte[] buf = new byte[length];
			inputStream.read(buf);
			inputStream.close();
			System.out.println("ReadByteForInputStream end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 以字节的方式读取一个流中的内容（只读一次，大小以inputStream.available()的值来决定）
	 * @param inputStream
	 * @return
	 */
	public static byte[] ReadByteForInputStream(InputStream inputStream){
		try {
			byte[] buf = ReadByteForInputStream(inputStream, inputStream.available());
			System.out.println("ReadByteForInputStream end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 以字节的方式读取文件中的内容，一次读取文件所有
	 * @param filePath 文件路径
	 */
	public static byte[] ReadByteForFilePath(String filePath){
		try {
			File file = new File(filePath);
			byte[] buf = ReadByteForFile(file);
			System.out.println("ReadByteForFilePath end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 以字节的方式读取文件中的内容，一次读取文件所有
	 * @param file File对象
	 * @return
	 */
	public static byte[] ReadByteForFile(File file) {
		try {
			byte[] buf = ReadByteForInputStream(new FileInputStream(file));
			System.out.println("ReadByteForFile end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 写对象到文件中
	 */
	public static void WriteObject(String objectFilePath,Object object){
		try {
			
			OutputStream outputStream = new FileOutputStream(new File(objectFilePath));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			objectOutputStream.close();
			System.out.println("writeObject end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * 基于字符流Writer类，写入字符串到某个文件路径中
	 *
	 * @param filePath
	 * @param string
	 * @param isAppend 是否追加内容
	 */
	public static void WriteStringForFilePath(String string,String filePath,boolean isAppend){
		try {
			WriteStringForFile(string,new File(filePath),isAppend);
			System.out.println("WriteStringForFilePath end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  基于字符流Writer类，写入字符串到某个文件中
	 * @param string
	 * @param filePath
	 * @param isAppend 是否追加内容
	 */
	public static void WriteStringForFile(String string,File file,boolean isAppend){
		try {
			Writer writer = new FileWriter(file,isAppend);
			WriteStringForWriter(string, writer, isAppend);
			System.out.println("WriteStringForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 基于字符流Writer类，写入字符串到某个Writer中
	 * @param string
	 * @param writer
	 * @param isAppend
	 */
	public static void WriteStringForWriter(String string,Writer writer,boolean isAppend){
		try {
			writer.write(string);
			writer.flush();
			writer.close();
			System.out.println("WriteStringFoWriter end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写一个inputStream流到某个文件路径中
	 * @param filePath
	 * @param inputStream
	 * @param isAppend
	 */
	public static void WriteInputStreamForFilePath(InputStream inputStream,String filePath,boolean isAppend) {
		try {
			File file = new File(filePath);
			WriteInputStreamForFile(inputStream, file, isAppend);
			System.out.println("WriteInputStreamForFilePath end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 写一个inputStream流到某个文件中
	 * @param inputStream
	 * @param file
	 * @param isAppend
	 */
	public static void WriteInputStreamForFile(InputStream inputStream,File file,boolean isAppend) {
		try {
			byte[] buf = new byte[inputStream.available()];
			//利用字节流读取文件
			inputStream.read(buf);
			inputStream.close();
			WriteBytesForFile(buf,file, isAppend);
			System.out.println("WriteInputStreamForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写byte数组到某个文件路径中
	 *
	 * @param filePath
	 * @param data
	 */
	public static void WriteBytesForFilePath(byte[] bytes,String filePath,boolean isAppend){
		try {
			File file = new File(filePath);
			WriteBytesForFile(bytes, file, isAppend);
			System.out.println("WriteBytesForFilePath end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写byte数组到某个文件中
	 * @param bytes
	 * @param file
	 * @param isAppend
	 */
	public static void WriteBytesForFile(byte[] bytes,File file,boolean isAppend){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file,isAppend);
			fileOutputStream.write(bytes);
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("WriteBytesForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写byte数组到某个outputstream中
	 * @param buf
	 * @param outputStream
	 */
	public static void WriteByteForOutputStream(byte[] buf,OutputStream outputStream) {
		try {
			outputStream.write(buf);
			outputStream.close();
			System.out.println("WriterInputStreamToOutputStream end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *写inputstream到某个outputstream中
	 * @param inputStream
	 * @param outputStream
	 */
	public static void WriteInputStreamForOutputStream(InputStream inputStream,OutputStream outputStream) {
		try {
			//在内存中开辟一个byte数组
			byte[] buf = new byte[inputStream.available()];
			//利用字节流读取文件
			inputStream.read(buf);
			inputStream.close();
			WriteByteForOutputStream(buf, outputStream);
			System.out.println("WriterInputStreamToOutputStream end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 *  利用包装流PrintWriter包装Writer,输出字符串到文本文件中
	 *
	 * @param filePath
	 * @param printStr
	 */
	public static void PrintWriterForFile(String filePath,String printStr,boolean isAppend){
		try {
			FileWriter fileWriter = new FileWriter(new File(filePath),isAppend);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(printStr);
			printWriter.flush();
			printWriter.close();
			System.out.println("printWriterForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 利用包装流printStream包装outPutStream输出字符串到文本文件中
	 *
	 * @param data
	 */
	public static void PrintStreamForFile(String printStreamFilePath,String printStr,boolean isAppend){
		try {
			OutputStream outputStream = new FileOutputStream(new File(printStreamFilePath),isAppend);
			PrintStream printStream = new PrintStream(outputStream);
			printStream.print(printStr);
			printStream.flush();
			printStream.close();
			System.out.println("printStreamForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 遍历一个路径（深度优先）
	 *
	 * @param path
	 */
	public static void LoopPringFile(String path,StringBuffer outStr){
		File file = new File(path);
		if (!file.isDirectory()||!file.exists()) {
			return;
		}
		File[] files = file.listFiles();
		for (File file2 : files) {
			outStr.append("\r\n");
			outStr.append(file2.getPath());
			System.out.println(file2.getPath());
			LoopPringFile(file2.getAbsolutePath(),outStr);
		}
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
	 * 生成一个以uuid命名的新文件名，保留原后缀
	 * test.doc   ->    uuid.doc
	 * test    ->    uud
	 * @param sourceFilename
	 * @return
	 */
	public static String generateUUIDFileName(String sourceFileName){
//		long time = System.currentTimeMillis();
		String newFileName = UUID.randomUUID() + getFileNameSuffix(sourceFileName);// 构成新文件名。
		return newFileName;
	}
	
	/**
	 * 创建一个目录（此方法并不创建文件）
	 * 以路径的最后一个斜杠或反斜杠为判断依据，斜杠之前的都是目录
	 * 斜杠之后的认为是文件（无后缀名的文件）
	 * @param pathStr
	 * @return
	 */
	public static boolean createDir(String pathStr) {
		boolean flag = true;
		int i1 = pathStr.lastIndexOf("\\");
		int i2 = pathStr.lastIndexOf("/");
		int index = 0;
		if (i1>i2) {
			index = i1;
		}else{
			index = i2;
		}
		String dir = pathStr.substring(0, index+1);
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			flag = dirFile.mkdirs();
		}
		 return flag;
	}
}
