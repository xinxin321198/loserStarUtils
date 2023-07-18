package com.loserstar.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 
 * author: loserStar
 * date: 2023年5月25日下午3:34:19
 * remarks: 文件操作相关工具类
 */
public class LoserStarFileUtil {
	public static final boolean isLog = false;
	private static String sysLog(String string) {
		if (isLog) {
			System.out.println(string);
			return string;
		}
		else {
			return "未开启log开关";
		}
	}
	
	/**
	 * 关闭流
	 * @param closeable
	 */
	private static void close(Closeable closeable) {
		if (closeable!=null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 以字符流方式读取一个文件对象里的字符
	 * @param file 文件对象
	 * @param charsetName 字符集
	 * @return
	 */
	public static String ReadReaderByFile(File file,String charsetName){
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charsetName);
			bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = bufferedReader.readLine())!=null) {
				stringBuffer.append(line);
			}
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadReaderByFile(File, String) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(bufferedReader);
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 以字符流方式读取一个文件对象里的字符,并且自动判断文件编码并读取
	 * @param file 文件对象
	 * @return
	 */
	public static String ReadReaderByFile(File file){
		String charsetName = getFilecharset(file);//自动判断文件编码，自动设置读取时的文件编码，以免出现乱码
		String string = ReadReaderByFile(file, charsetName);
		sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadReaderByFile(File) end");
		return string;
	}
	/**
	 * 以字符流的方式从一个文件路径读取文件的字符,并且自动判断文件编码并读取(二进制文件读出来肯定是乱码，不用想，二进制文件要使用字节流的方式)
	 *
	 * @param filePath 文件路径
	 * @return
	 */
	public static String ReadReaderByFilePath(String filePath){
		File file = new File(filePath);
		String string = ReadReaderByFile(file);
		sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadReaderByFilePath(String)");
		return string;
	}
	/**
	 * 以字符流的方式从一个文件路径读取文件的字符,指定读取编码
	 * @param filePath 文件路径
	 * @param charsetName 编码名称
	 * @return
	 */
	public static String ReadReaderByFilePath(String filePath,String charsetName){
		File file = new File(filePath);
		String string = ReadReaderByFile(file,charsetName);
		sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadReaderByFilePath(String, String) end");
		return string;
	}
	
	/**
	 * 从某个文件中读取对象到内存中(泛型)
	 * @param objectFilePath 对方文件路径
	 * @param class1 对象模型的class
	 * @return
	 */
	public static <T> T ReadObjectByFilePath(String objectFilePath,Class<T> class1){
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			inputStream = new FileInputStream(new File(objectFilePath));
			objectInputStream = new ObjectInputStream(inputStream);
			@SuppressWarnings("unchecked")
			T resultObject = (T)objectInputStream.readObject();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadObjectByFilePath(String, Class<T>) end");
			return resultObject;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(objectInputStream);
			close(inputStream);
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 以字节的方式读取一个流中的内容
	 * @param inputStream 输入流
	 * @param length 每次读取的大小缓冲区，如果为0时直接返回空的字节数组，不去读取，因为读了也没地方存哈
	 * @return
	 */
	public static byte[] ReadByteByInputStream(InputStream inputStream,int length){
		if(0==length) {
			return  new byte[] {};
		}
		/*附上API：
	　　　　public int read(byte[] b) throws IOException　　　　
	　　从输入流中读取一定数量的字节，并将其存储在缓冲区数组 b 中。以整数形式返回实际读取的字节数。
	　　在输入数据可用、检测到文件末尾或者抛出异常前，此方法一直阻塞。
	　　如果 b 的长度为 0，则不读取任何字节并返回 0；否则，尝试读取至少一个字节。如果因为流位于文件末尾而没有可用的字节，则返回值 -1；否则，至少读取一个字节并将其存储在 b 中。
			注意这段话：将读取的第一个字节存储在元素 b[0] 中，下一个存储在 b[1] 中，依次类推。读取的字节数最多等于 b 的长度。设 k 为实际读取的字节数；这些字节将存储在 b[0] 到 b[k-1] 的元素中，不影响 b[k] 到 b[b.length-1] 的元素。 */
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byte[] buf = new byte[length];//每次读取的字节大小缓冲区（实际没太大用）
			byteArrayOutputStream = new ByteArrayOutputStream();//用字节数组流来存储多次读取后的内容
			int len = 0;
		
			while ((len = inputStream.read(buf))!=-1) {
				byteArrayOutputStream.write(buf,0,len);//此方法是读取到多长len长度的byte就写入多少，所以文件大小不会超出
//				System.out.println("此次读取到的byte大小："+len);
				/**
				 * 1.此地方不能使用byteArrayOutputStream.write(buf);方法
				 * 2.因为网络传输有延迟，每次使用read读取到的buf的大小都不一样，此方法每次循环都把buf整个length长度都输出到outputstream中，并且API上明确说明不会清空缓冲区，导致最终字节数超出原文件很多；
				 * 3.debug时候看似正常，原因是断点暂停的那段时间让网络延迟能完整的获取到buf最大长度的字节码，所以最终只会在输出最后的byte时候，文件末尾超出那么一点点字节
				 */
			}
			byteArrayOutputStream.flush();
			byte[] resultBuf = byteArrayOutputStream.toByteArray();//读完之后输出所有的字节
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByInputStream(InputStream, int) end");
			return resultBuf;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(byteArrayOutputStream);
			close(inputStream);
		}
		return null;
	}
	

	
	/**
	 * 以字节的方式读取一个流中的内容（每次读取的大小以inputStream.available()的值来决定，本地一次可以得到完整的流的大小，网络的是分批次传送，会读读多次）
	 * 网络读取时，因网络有延迟，如果流返回的内容是0字节则会一直等待直到字节出现
	 * @param inputStream 输入流
	 * @return
	 */
	public static byte[] ReadByteByInputStream(InputStream inputStream){
		try {
			//在进行网络操作时往往出错，因为你调用available()方法时，对发发送的数据可能还没有到达，你得到的count是0，所以等待到有数据出现
			int count = 0;
			while (count == 0) {
				count = inputStream.available(); //获取到用于每次读取的byte大小
			  } 
			byte[] buf = ReadByteByInputStream(inputStream, count);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByInputStream(InputStream) end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
		}
		return null;
	}
	
	/**
	 * 以字节的方式读取一个流中的内容（每次读取的大小以inputStream.available()的值来决定，本地一次可以得到完整的流的大小，网络的是分批次传送，会读读多次）
	 * 增加一个超时时间，如果经过10毫秒都没有读取到一个字节，则进入指定的millis超时等待，如果超过millis这个时间还每没有读取到字节，则放弃本次读取，不再一直等待
	 * @param inputStream
	 * @param millis
	 * @return
	 */
	public static byte[] ReadByteByInputStreamTimeout(InputStream inputStream,int millis){
		try {
			//在进行网络操作时往往出错，因为你调用available()方法时，对发发送的数据可能还没有到达，你得到的count是0，所以等待到有数据出现
			int count = 0;
			//如果20毫秒一个字节都没有，就进入设定好的等待时间继续等待，超时过期
			Thread.sleep(10);
			count = inputStream.available();
			if (0==count) {
				Thread.sleep(millis);
				count = inputStream.available();
			}
			byte[] buf = ReadByteByInputStream(inputStream, count);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByInputStream(InputStream,int) end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
		}
		return null;
	}
	
	/**
	 * 以字节的方式读取文件中的内容，一次读取文件所有(传入文件路径)
	 * @param filePath 文件路径
	 */
	public static byte[] ReadByteByFilePath(String filePath){
		try {
			File file = new File(filePath);
			byte[] buf = ReadByteByFile(file);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByFilePath(String) end");
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
	public static byte[] ReadByteByFile(File file) {
		try {
			byte[] buf = ReadByteByInputStream(new FileInputStream(file));
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByFile(File) end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  写对象到文件中
	 * @param objectFilePath 写入对象的文件路径
	 * @param object 对象
	 */
	public static void WriteObjectToFilePath(String objectFilePath,Object object){
		OutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			if (!createDir(objectFilePath)) {throw new Exception("目录创建失败："+getDir(objectFilePath));}
			outputStream = new FileOutputStream(new File(objectFilePath));
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteObjectToFilePath(String, Object) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(objectOutputStream);
			close(outputStream);
		}
	}
	

	
	/**
	 * 基于字符流Writer类，写入字符串到某个文件路径中
	 * @param string 要输出的字符串
	 * @param filePath 输出的文件路径
	 * @param isAppend 是否追加内容
	 * @param charsetName 写入的文件字符编码
	 */
	public static void WriteStringToFilePath(String string,String filePath,boolean isAppend,String charsetName){
		try {
			if (!createDir(filePath)) {throw new Exception("目录创建失败："+getDir(filePath));}
			WriteStringToFile(string,new File(filePath),isAppend,charsetName);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteStringToFilePath(String, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 基于字符流Writer类，写入字符串到某个文件路径中（文件编码使用Java默认编码）
	 * @param string 要输出的字符串
	 * @param filePath 输出的文件路径
	 * @param isAppend 是否追加内容
	 */
	public static void WriteStringToFilePath(String string,String filePath,boolean isAppend){
		try {
			if (!createDir(filePath)) {throw new Exception("目录创建失败："+getDir(filePath));}
			WriteStringToFile(string,new File(filePath),isAppend,null);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteStringToFilePath(String, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  基于字符流Writer类，写入字符串到某个文件中
	 * @param string 要输出的字符串
	 * @param file 要输出的文件对象
	 * @param isAppend 是否追加内容
	 * @param charsetName 指定写入的编码
	 */
	public static void WriteStringToFile(String string,File file,boolean isAppend,String charsetName){
		try {
			if (!createDir(file.getPath())) {throw new Exception("目录创建失败："+getDir(file.getPath()));}
			if (charsetName==null||charsetName.equals("")) {
				charsetName = Charset.defaultCharset().name();
			}
			WriteStringToWriter(string, new OutputStreamWriter(new FileOutputStream(file, isAppend),charsetName));
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteStringToFile(String, File, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  基于字符流Writer类，写入字符串到某个文件中(使用Java默认编码写入)
	 * @param string 要输出的字符串
	 * @param file 要输出的文件对象
	 * @param isAppend 是否追加内容
	 */
	public static void WriteStringToFile(String string,File file,boolean isAppend){
		try {
			if (!createDir(file.getPath())) {throw new Exception("目录创建失败："+getDir(file.getPath()));}
			WriteStringToFile(string,file,isAppend,null);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteStringToFile(String, File, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 基于字符流Writer类，写入字符串到某个Writer中(输出玩自动关闭)
	 * @param string 要输出的字符串
	 * @param writer 输入字符流对象
	 */
	public static void WriteStringToWriter(String string,Writer writer){
		try {
			writer.write(string);
			writer.flush();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteStringToWriter(String, Writer) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(writer);
		}
	}
	
	/**
	 * 写byte数组到某个文件路径中
	 * @param bytes 字节数组
	 * @param filePath 要写入的文件路径
	 * @param isAppend 是否追加内容
	 */
	public static void WriteBytesToFilePath(byte[] bytes,String filePath,boolean isAppend){
		try {
			if (!createDir(filePath)) {throw new Exception("目录创建失败："+getDir(filePath));}
			WriteBytesToFile(bytes, new File(filePath), isAppend);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteBytesToFilePath(byte[], String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写byte数组到某个文件中
	 * @param bytes 字节数组
	 * @param file 要写入的文件对象
	 * @param isAppend 是否追加内容
	 */
	public static void WriteBytesToFile(byte[] bytes,File file,boolean isAppend){
		FileOutputStream fileOutputStream = null;
		try {
			if (!createDir(file.getPath())) {throw new Exception("目录创建失败："+getDir(file.getPath()));}
			fileOutputStream = new FileOutputStream(file,isAppend);
			fileOutputStream.write(bytes);
			fileOutputStream.flush();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteBytesToFile(byte[], File, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(fileOutputStream);
		}
	}
	
	/**
	 * 写byte数组到某个outputstream中
	 * @param bytes  字节数组
	 * @param outputStream 输出流对象
	 */
	public static void WriteBytesToOutputStream(byte[] bytes,OutputStream outputStream) {
		try {
			outputStream.write(bytes);
			outputStream.flush();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteByteToOutputStream(byte[], OutputStream) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(outputStream);
		}
	}
	
	/**
	 * 写一个inputStream流到某个文件路径中
	 * @param inputStream 输入流
	 * @param filePath 要输出的文件路径
	 * @param isAppend 是否追加内容
	 */
	public static void WriteInputStreamToFilePath(InputStream inputStream,String filePath,boolean isAppend) {
		try {
			if (!createDir(filePath)) {throw new Exception("目录创建失败："+getDir(filePath));}
			WriteInputStreamToFile(inputStream, new File(filePath), isAppend);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteInputStreamToFilePath(InputStream, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
		}
	}
	/**
	 * 写一个inputStream流到某个文件中
	 * @param inputStream 输入流
	 * @param file 文件对象
	 * @param isAppend 是否追加内容
	 */
	public static void WriteInputStreamToFile(InputStream inputStream,File file,boolean isAppend) {
		try {
			if (!createDir(file.getPath())) {throw new Exception("目录创建失败："+getDir(file.getPath()));}
			byte[] buf = ReadByteByInputStream(inputStream);//从input流读取字节
			WriteBytesToFile(buf,file, isAppend);//把字节写入文件
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteInputStreamToFile(InputStream, File, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
		}
	}
	
	/**
	 *写inputstream到某个outputstream中
	 * @param inputStream 输入流
	 * @param outputStream 输出流
	 */
	public static void WriteInputStreamToOutputStream(InputStream inputStream,OutputStream outputStream) {
		try {
			byte[] buf = ReadByteByInputStream(inputStream);//从input流读取字节
			WriteBytesToOutputStream(buf, outputStream);//把字节写入outputStream
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteInputStreamToOutputStream(InputStream, OutputStream) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(inputStream);
			close(outputStream);
		}
	}
	

	

	

	/**
	 *  利用包装流PrintWriter包装Writer,输出字符串到文本文件中
	 *
	 * @param filePath 输出的文件路径
	 * @param printStr 输出的字符串
	 */
	public static void PrintWriterToFile(String filePath,String printStr,boolean isAppend){
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try {
			if (!createDir(filePath)) {throw new Exception("目录创建失败："+getDir(filePath));}
			fileWriter = new FileWriter(new File(filePath),isAppend);
			printWriter = new PrintWriter(fileWriter);
			printWriter.print(printStr);
			printWriter.flush();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.PrintWriterToFile(String, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(printWriter);
			close(fileWriter);
		}
	}
	
	/**
	 *  利用包装流printStream包装outPutStream输出字符串到文本文件中
	 * @param printStreamFilePath
	 * @param printStr
	 * @param isAppend
	 */
	public static void PrintStreamToFile(String printStreamFilePath,String printStr,boolean isAppend){
		OutputStream outputStream = null;
				PrintStream printStream = null;
		try {
			if (!createDir(printStreamFilePath)) {throw new Exception("目录创建失败："+getDir(printStreamFilePath));}
			outputStream = new FileOutputStream(new File(printStreamFilePath),isAppend);
			printStream = new PrintStream(outputStream);
			printStream.print(printStr);
			printStream.flush();
			printStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.PrintStreamToFile(String, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(printStream);
			close(outputStream);
		}
	}
	
	/**
	 * 把一堆文件路径下的文件以zip形式压缩进一个zipFile文件中
	 * @param filePathList
	 * @param zipFilePath
	 * @param reNameList
	 * @throws Exception
	 */
	public static void zipFilePathListToZipFilePath(List<String> filePathList,String zipFilePath,List<String> reNameList) throws Exception {
		List<File> fileList = new ArrayList<File>();
		for (String string : filePathList) {
			fileList.add(new File(string));
		}
		zipFileListToFile(fileList, new File(zipFilePath),reNameList);
	}
	
	/**
	 * 把一堆文件文件以zip形式压缩进一个zipFile文件中
	 * @param fileList 需要压缩的文件对象集合
	 * @param zipFile 压缩后生成的文件
	 * @param reNameList 重命名要压缩的文件，可以写成路径，压缩包里就是带路径的，开头不用加根目录/，否则斜杠也会认作是目录名称（如果传入，数量必须与fileList的数量一致。重名的文件的话不会覆盖）
	 * @throws Exception
	 */
	public static void zipFileListToFile(List<File> fileList,File zipFile,List<String> reNameList) throws Exception {
		if (!LoserStarFileUtil.createDir(zipFile.getPath())) {throw new Exception("目录创建失败："+LoserStarFileUtil.getDir(zipFile.getPath()));}
		zipFileListToOutputStream(fileList, new FileOutputStream(zipFile),reNameList);
	}
	
	/**
	 * 把一堆文件路径下的文件以zip形式压缩进一个outputstream中
	 * @param filePathList 需要压缩的文件路径集合
	 * @param out 输出流
	 * @param reNameList 重命名要压缩的文件，可以写成路径，压缩包里就是带路径的，开头不用加根目录/，否则斜杠也会认作是目录名称（如果传入，数量必须与fileList的数量一致。重名的文件的话不会覆盖）
	 * @throws Exception
	 */
	public static void zipFilePathListToOutputStream(List<String> filePathList,OutputStream out,List<String> reNameList) throws Exception {
		List<File> fileList = new ArrayList<File>();
		for (String string : filePathList) {
			fileList.add(new File(string));
		}
		zipFileListToOutputStream(fileList, out,reNameList);
	}
	/**
	 * 把一堆文件对象集合以zip形式压缩进一个outputstream中
	 * @param fileList 文件对象集合
	 * @param out 输出流
	 * @param reNameList 重命名要压缩的文件，可以写成路径，压缩包里就是带路径的，开头不用加根目录/，否则斜杠也会认作是目录名称（如果传入，数量必须与fileList的数量一致。重名的文件的话不会覆盖）
	 * @throws Exception
	 */
	public static void zipFileListToOutputStream(List<File> fileList,OutputStream out,List<String> reNameList) throws Exception {
		if (reNameList!=null&&reNameList.size()>0) {
			if (reNameList.size()!=fileList.size()) {
				throw new Exception("您打算压缩进去的文件进行重命名，但是文件数量和重命名数量不一致");
			}
		}
	    ZipOutputStream zos = null;
	    try {
	        //压缩文件
	        zos = new ZipOutputStream(out);
	        byte[] buf = new byte[1024];
	        for(int i=0;i<fileList.size();i++) {
	        	File file = fileList.get(i);
	            //在压缩包中添加文件夹
	        	if (reNameList!=null&&reNameList.size()>0) {
	        		zos.putNextEntry(new ZipEntry(reNameList.get(i)));
				}else {
					zos.putNextEntry(new ZipEntry(file.getName()));
				}
	            int len;
	            FileInputStream in = new FileInputStream(file);
	            while ((len = in.read(buf)) != -1){
	                zos.write(buf, 0, len);
	            }
	            zos.closeEntry();
	            close(in);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally {
	    	close(zos);
	    	close(out);
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
	 * 提取目录，以路径的最后一个斜杠或反斜杠为判断依据，斜杠之前的都是目录
	 * @param pathStr
	 * @return
	 */
	public static String getDir(String pathStr) {
		int i1 = pathStr.lastIndexOf("\\");
		int i2 = pathStr.lastIndexOf("/");
		int index = 0;
		if (i1>i2) {
			index = i1;
		}else{
			index = i2;
		}
		String dir = pathStr.substring(0, index+1);
		return dir;
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
	 * @return 目录存在或创建成功返回true，目录不存在并且创建失败范围false
	 */
	public static boolean createDir(String pathStr) {
		boolean flag = true;
		String dir = getDir(pathStr);
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			flag = dirFile.mkdirs();
		}
		 return flag;
	}
	
	/**
	 * 该文件对象是否是一个文件
	 * @param file
	 * @return
	 */
	public static boolean isFile(File file) {
		return file.isFile();
	}
	
	/**
	 * 该路径是否是一个文件
	 * @param filePath
	 * @return
	 */
	public static boolean isFile(String filePath) {
		File file = new File(filePath);
		return isFile(file);
	}
	
	/**
	 * 文件对象是否是一个目录
	 * @param file
	 * @return
	 */
	public static boolean isDirectory(File file) {
		return file.isDirectory();
	}
	
	/**
	 * 路径是否是一个目录
	 * @param filePath
	 * @return
	 */
	public static boolean isDirectory(String filePath) {
		File file = new File(filePath);
		return isDirectory(file);
	}
	
	/**
	 * 得到文件的编码字符集
	 * @param sourceFile
	 * @return
	 */
    private static  String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }
    
    /**
     * 把object对象转为字节数组
     * @param object
     * @return
     * @throws Exception
     */
    public static byte[] conveterToByteArray(Object object) throws Exception {
    	byte[] data = null;
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    	if(object instanceof Double) {
    		dataOutputStream.writeDouble((Double)object);
    	}else if (object instanceof Integer) {
			dataOutputStream.writeInt((Integer)object);
		} else if (object instanceof Long) {
			dataOutputStream.writeLong((Long)object);
		}else if (object instanceof Boolean) {
			dataOutputStream.writeBoolean((Boolean)object);
		}else if (object instanceof String) {
			dataOutputStream.writeUTF((String)object);
		}else if (object instanceof Character) {
			dataOutputStream.writeChar((Character)object);
		}else if (object instanceof Float) {
			dataOutputStream.writeFloat((Float)object);
		}else {
			throw new Exception("不能识别的数据类型！");
		}
    	dataOutputStream.flush();
    	data = byteArrayOutputStream.toByteArray();
    	dataOutputStream.close();
    	return data;
    }
    
    /**
     * 对附件的url根据系统编码重新编码
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeFileUrl(String url) throws UnsupportedEncodingException {
    	String os = System.getProperty("os.name");
		String sourceEncode = "utf-8";
		String targetEncode = System.getProperty("file.encoding");
		if (os.equalsIgnoreCase("AIX")) {
			sourceEncode = "GBK";
		}
		url = new String(url.getBytes(sourceEncode), targetEncode);
		return url;
    }
    
/*    public static String convert(byte[] data) throws IOException {
    	DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data));
    	char c = null;
    	dataInputStream.readChar();
    	dataInputStream.close();
    	return new string;
    }*/
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
//		File file = new File("c://下载地址.txt");
//		File outFile = new File("c://1.txt");
//		byte[] fileStrArray = ReadByteByFile(file);
//		String fileStr = ReadReaderByFile(file);
//		System.out.println(fileStr);
//		WriteStringToFile(fileStr,outFile,false,"utf8");
//		WriteInputStreamToOutputStream(new FileInputStream(file), new FileOutputStream(outFile));
//		System.out.println("end");
		
/*		double d = 123123.34234;
		String s = "loserStar";
		try {
			byte[] byte1 = conveterToByteArray( s);
			byte[] byte2 = s.getBytes();
			System.out.println(new String(byte1,"UTF-8"));
			System.out.println(new String(byte2));
//			System.out.println(convert(byte1));
//			System.out.println(convert(byte2));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
/*		String path = "c://testUpload/"+UUID.randomUUID().toString()+"/1.txt";
		File file = new File(path);
		String path2 = "c://testUpload/"+UUID.randomUUID().toString()+"/";
		File file2 = new File(path2);
//		WriteStringToFilePath(RandomUtils.getRandomCarNo()+RandomUtils.getRandomStartAddr(), path, false);
		WriteStringToFile(RandomUtils.getRandomCarNo()+RandomUtils.getRandomStartAddr(), file, false);
		WriteStringToFile(RandomUtils.getRandomCarNo()+RandomUtils.getRandomStartAddr(), file2, false);*/
/*		String path = "c://testUpload//0c125b27-8625-4dbd-8ced-ba786b4d90c0//";
		File file = new File(path);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getPath());
		System.out.println(file.getParent());
		System.out.println(file.getName());
		System.out.println(file.getParentFile());
		System.out.println("-----------------------分割线--------------------");
		String path2 = "c://testUpload//0c125b27-8625-4dbd-8ced-ba786b4d90c0//1.txt";
		File file2 = new File(path2);
		System.out.println(file2.getAbsolutePath());
		System.out.println(file2.getPath());
		System.out.println(file2.getParent());
		System.out.println(file2.getName());
		System.out.println(file2.getParentFile());
		
		
		System.out.println(getDir(file.getPath()));*/
		
	}
}
