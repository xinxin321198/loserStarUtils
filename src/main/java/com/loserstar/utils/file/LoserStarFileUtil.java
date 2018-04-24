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

/**
 * author: loserStar
 * date: 2018年3月13日下午2:24:54
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarFileUtil {
	/**
	 * 从某个文件中读取字符串
	 *
	 * @param filePath
	 * @return
	 */
	public static String ReaderForFile(String filePath){
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
	 * 基于字符流Writer类，写入字符串到某个文件中
	 *
	 * @param filePath
	 * @param string
	 */
	public static void WriterForFile(String filePath,String string,boolean isAppend){
		try {
			Writer writer = new FileWriter(new File(filePath),isAppend);
			writer.write(string);
			writer.flush();
			writer.close();
			System.out.println("WriterForFile end");
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
	 * 以字节的方式读取文件中的内容，一次读取文件所有(如果不是UT-8不能读中文)
	 *
	 * @param filePath
	 */
	public static String ReadByteForFile(String filePath){
		try {
			String encoding = "UTF-8";
			//拿到文件的大小
			File file = new File(filePath);
			long length = file.length();
			//在内存中开辟一个和文件大小一样的byte数组
			byte[] buf = new byte[(int)length];
			//利用字节流读取文件
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(buf);
			fileInputStream.close();
			System.out.println("readByteForFile end");
			return new String(buf, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 写一个inputStream流到某个文件中
	 * @param filePath
	 * @param inputStream
	 * @param isAppend
	 */
	public static void WriteInputStreamForFile(String filePath,InputStream inputStream,boolean isAppend) {
		try {
			 FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath),isAppend);
			//在内存中开辟一个byte数组
			byte[] buf = new byte[1024];
			//利用字节流读取文件
			int byteCount = 0;
			while ((byteCount = inputStream.read(buf)) != -1)
            {
               fileOutputStream.write(buf);
            }
			fileOutputStream.flush();
			fileOutputStream.close();
			inputStream.close();
			System.out.println("WriteInputStreamForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 写byte数组到某个文件中
	 *
	 * @param filePath
	 * @param data
	 */
	public static void WriteBytesForFile(String filePath,byte[] bytes,boolean isAppend){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath),isAppend);
			fileOutputStream.write(bytes);
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("writeBytesForFile end");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
