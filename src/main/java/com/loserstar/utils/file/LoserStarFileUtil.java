/**
 * author: loserStar
 * date: 2018年3月13日下午2:24:54
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.util.UUID;


/**
 * author: loserStar
 * date: 2018年3月13日下午2:24:54
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarFileUtil {
	public static final boolean isLog = true;
	public static String sysLog(String string) {
		if (isLog) {
			System.out.println(string);
			return string;
		}
		else {
			return "未开启log开关";
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
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), charsetName);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = bufferedReader.readLine())!=null) {
				stringBuffer.append(line);
			}
			bufferedReader.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadReaderByFile(File, String) end");
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			InputStream inputStream = new FileInputStream(new File(objectFilePath));
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			@SuppressWarnings("unchecked")
			T resultObject = (T)objectInputStream.readObject();
			objectInputStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadObjectByFilePath(String, Class<T>) end");
			return resultObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 以字节的方式读取一个流中的内容（只读一次，传入读取的大小）
	 * @param inputStream 输入流
	 * @param length 读取的大小
	 * @return
	 */
	public static byte[] ReadByteByInputStream(InputStream inputStream,int length){
		try {
			//利用字节流读取文件
			byte[] buf = new byte[length];
			inputStream.read(buf);
			inputStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByInputStream(InputStream, int) end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 以字节的方式读取一个流中的内容（只读一次，大小以inputStream.available()的值来决定）
	 * @param inputStream 输入流
	 * @return
	 */
	public static byte[] ReadByteByInputStream(InputStream inputStream){
		try {
			byte[] buf = ReadByteByInputStream(inputStream, inputStream.available());
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.ReadByteByInputStream(InputStream) end");
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			
			OutputStream outputStream = new FileOutputStream(new File(objectFilePath));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			objectOutputStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteObjectToFilePath(String, Object) end");
		} catch (Exception e) {
			e.printStackTrace();
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
			writer.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteStringToWriter(String, Writer) end");
		} catch (Exception e) {
			e.printStackTrace();
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
			File file = new File(filePath);
			WriteBytesToFile(bytes, file, isAppend);
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
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file,isAppend);
			fileOutputStream.write(bytes);
			fileOutputStream.flush();
			fileOutputStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteBytesToFile(byte[], File, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写byte数组到某个outputstream中
	 * @param bytes  字节数组
	 * @param outputStream 输出流对象
	 */
	public static void WriteByteToOutputStream(byte[] bytes,OutputStream outputStream) {
		try {
			outputStream.write(bytes);
			outputStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteByteToOutputStream(byte[], OutputStream) end");
		} catch (Exception e) {
			e.printStackTrace();
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
			File file = new File(filePath);
			WriteInputStreamToFile(inputStream, file, isAppend);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteInputStreamToFilePath(InputStream, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
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
			byte[] buf = new byte[inputStream.available()];
			//利用字节流读取文件
			inputStream.read(buf);
			inputStream.close();
			WriteBytesToFile(buf,file, isAppend);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteInputStreamToFile(InputStream, File, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *写inputstream到某个outputstream中
	 * @param inputStream 输入流
	 * @param outputStream 输出流
	 */
	public static void WriteInputStreamToOutputStream(InputStream inputStream,OutputStream outputStream) {
		try {
			//在内存中开辟一个byte数组
			byte[] buf = new byte[inputStream.available()];
			//利用字节流读取文件
			inputStream.read(buf);
			inputStream.close();
			WriteByteToOutputStream(buf, outputStream);
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.WriteInputStreamToOutputStream(InputStream, OutputStream) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	

	

	/**
	 *  利用包装流PrintWriter包装Writer,输出字符串到文本文件中
	 *
	 * @param filePath 输出的文件路径
	 * @param printStr 输出的字符串
	 */
	public static void PrintWriterToFile(String filePath,String printStr,boolean isAppend){
		try {
			FileWriter fileWriter = new FileWriter(new File(filePath),isAppend);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(printStr);
			printWriter.flush();
			printWriter.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.PrintWriterToFile(String, String, boolean) end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  利用包装流printStream包装outPutStream输出字符串到文本文件中
	 * @param printStreamFilePath
	 * @param printStr
	 * @param isAppend
	 */
	public static void PrintStreamToFile(String printStreamFilePath,String printStr,boolean isAppend){
		try {
			OutputStream outputStream = new FileOutputStream(new File(printStreamFilePath),isAppend);
			PrintStream printStream = new PrintStream(outputStream);
			printStream.print(printStr);
			printStream.flush();
			printStream.close();
			sysLog("com.loserstar.utils.file.LoserStarFileUtil.PrintStreamToFile(String, String, boolean) end");
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
    
    public static byte[] conveterToByteArray(Object object) throws Exception {
    	byte[] data = null;
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    	if(object instanceof Double) {
    		dataOutputStream.writeDouble((double)object);
    	}else if (object instanceof Integer) {
			dataOutputStream.writeInt((int)object);
		} else if (object instanceof Long) {
			dataOutputStream.writeLong((long)object);
		}else if (object instanceof Boolean) {
			dataOutputStream.writeBoolean((boolean)object);
		}else if (object instanceof String) {
			dataOutputStream.writeUTF((String)object);
		}else if (object instanceof Character) {
			dataOutputStream.writeChar((char)object);
		}else if (object instanceof Float) {
			dataOutputStream.writeFloat((float)object);
		}else {
			throw new Exception("不能识别的数据类型！");
		}
    	dataOutputStream.flush();
    	data = byteArrayOutputStream.toByteArray();
    	dataOutputStream.close();
    	return data;
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
		
		double d = 123123.34234;
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
		}
		
	}
}
