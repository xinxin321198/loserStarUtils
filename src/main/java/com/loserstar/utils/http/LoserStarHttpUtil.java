/**
 * author: loserStar
 * date: 2018年3月16日上午9:13:41
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.loserstar.utils.file.LoserStarFileUtil;

/**
 * 
 * author: loserStar 
 * date: 2019年1月29日下午8:33:28 
 * remarks:
 * 1.从input流读数据时候使用UTF-8，否则乱码
 * 2.本屌关闭流和连接的原则，最后吃完的洗碗，最后调用的负责关闭流或连接
 * 
 * GET:
1、创建远程连接
2、设置连接方式（get、post、put。。。）
3、设置连接超时时间
4、设置响应读取时间
5、发起请求
6、获取请求数据
7、关闭连接

POST:
1、创建远程连接
2、设置连接方式（get、post、put。。。）
3、设置连接超时时间
4、设置响应读取时间
5、当向远程服务器传送数据/写数据时，需要设置为true（setDoOutput）
6、当前向远程服务读取数据时，设置为true，该参数可有可无（setDoInput）
7、设置传入参数的格式:（setRequestProperty）
8、设置鉴权信息：Authorization:（setRequestProperty）
9、设置参数
10、发起请求
11、获取请求数据
12、关闭连接
参考文档：
java实现调用http请求的几种常见方式:https://blog.csdn.net/riemann_/article/details/90539829
关于Java中流关闭以及先后顺序等问题总结：https://blog.csdn.net/u012117723/article/details/119562031
关于OutputStream的flush()和close()方法：https://blog.csdn.net/lsy_cheer/article/details/107975868
 */
public class LoserStarHttpUtil {
	/**
	 * 
	 * remarks:自己的x509证书信任管理器类,用于访问不受信任的https连接
	 */
	public static class MyX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}

	/**
	 * 为请求对象中设置请求头信息
	 * 
	 * @param httpsConn
	 * @param requestHeaderMap
	 */
	private static void setRequestHeader(HttpURLConnection conn, Map<String, String> requestHeaderMap) {
		if (requestHeaderMap != null) {
			Set<String> requestHeaderKeys = requestHeaderMap.keySet();
			for (String key : requestHeaderKeys) {
				conn.setRequestProperty(key, requestHeaderMap.get(key));
			}
		}
	}

	/**
	 * 创建一个http的请求连接对象，可以设置超时时间（根据协议决定是创建http还是https,https是所有证书都信任的）
	 * @param urlStr url地址
	 * @param method 请求方法GET POST
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static HttpURLConnection createHttpUrlConnection(String urlStr, String method) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		return createHttpUrlConnection(urlStr, method, 5000);
	}

	/**
	 * 创建一个http的请求连接对象（根据协议决定是创建http还是https,https是所有证书都信任的）
	 * @param urlStr url地址
	 * @param method 请求方法GET POST
	 * @param timeout 自定义超时时间
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static HttpURLConnection createHttpUrlConnection(String urlStr, String method,int timeout) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		// lxx 20220516 加这句貌似才可以在1.7及以下jdk中使用调用https
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		URL url = new URL(urlStr);
		HttpURLConnection conn = null;// 可能是http可能是https
		if (url.getProtocol().equals("http")) {
			conn = (HttpURLConnection) url.openConnection();
		} else if (url.getProtocol().equals("https")) {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
			httpsConn.setSSLSocketFactory(ssf);
			conn = httpsConn;
		}
		conn.setRequestMethod(method); // 设置本次请求的方式 ， 默认是GET方式， 参数要求都是大写字母
		conn.setConnectTimeout(timeout);// 设置连接超时
		conn.setReadTimeout(timeout);//设置读取超时时间
		conn.setDoInput(true);// 是否打开输入流 ， 此方法默认为true(post 请求是以流的方式隐式的传递参数)
		conn.setDoOutput(true);// 是否打开输出流， 此方法默认为false
		// post请求缓存设为false
		conn.setUseCaches(false);
		// 设置该HttpURLConnection实例是否自动执行重定向
		conn.setInstanceFollowRedirects(true);
		return conn;
	}

	/**
	 * 工具内部使用的方法，从一个网络的inputStream流中，读取到字符串 并且根据请求头的Accept-Encoding判断是否是gzip格式，如果是gzip需特殊处理
	 * 
	 * @param inputStream
	 *            远程输入流
	 * @param requestHeaderMap
	 *            请求头内容
	 * @return
	 */
	private static StringBuffer ReadStringByInputStream(InputStream inputStream, Map<String, String> requestHeaderMap) {
		StringBuffer stringBuffer = new StringBuffer();
		GZIPInputStream gZIPInputStream = null;//远程连接响应本地的输入流的解压缩包装流
		InputStreamReader inputStreamReader = null;//远程连接响应本地的输入流
		BufferedReader bufferedReader = null;//远程连接响应本地的输入读取器
		try {
			if (null != requestHeaderMap && requestHeaderMap.get("Accept-Encoding") != null && requestHeaderMap.get("Accept-Encoding").contains("gzip")) {
				gZIPInputStream = new GZIPInputStream(inputStream);
				byte[] buf = LoserStarFileUtil.ReadByteByInputStream(gZIPInputStream);
				stringBuffer.append(new String(buf,"UTF-8"));
			} else {
				inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				bufferedReader = new BufferedReader(inputStreamReader);
				// 整行读,手动加上换行
				String result = null;
				while ((result = bufferedReader.readLine()) != null) {
					result += "\n";
					stringBuffer.append(result);
				}
				// 单个字符读取
				/*
				 * int result = 0; bufferedReader.readLine(); while ((result =bufferedReader.read())!=-1) { stringBuffer.append((char)result); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(inputStream!=null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null!=gZIPInputStream) {
				try {
					gZIPInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStreamReader!=null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedReader!=null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return stringBuffer;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createKeyValueParamString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuilder prestr = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr.append(key).append("=").append(value);
			} else {
				prestr.append(key).append("=").append(value).append("&");
			}
		}
		return prestr.toString();
	}

	/**
	 * get请求（自动生成Connection对象）（异常仅在控制台输出）
	 * 
	 * @param urlStr
	 *            url字符串
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @return 返回响应体字符串，有异常返回空字符串
	 */
	public static String get(String urlStr, Map<String, String> requestHeaderMap) {
		String resultStr = "";
		try {
			resultStr = getEx(urlStr, requestHeaderMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * get请求（自动生成Connection对象）（会抛异常）
	 * 
	 * @param urlStr
	 *            url字符串
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @return 返回响应体，响应码非200的话直接抛异常，异常字符串中包含响应码及响应体字符串
	 * @throws Exception
	 */
	public static String getEx(String urlStr, Map<String, String> requestHeaderMap) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		HttpURLConnection conn = createHttpUrlConnection(urlStr, "GET");
		stringBuffer = new StringBuffer(getEx(urlStr, requestHeaderMap, conn));
		return stringBuffer.toString();
	}

	/**
	 * get请求（可自定义Connection对象）（异常仅在控制台输出）
	 * 
	 * @param urlStr
	 *            url字符串
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @param conn
	 *            Connection对象
	 * @return 返回响应体字符串，有异常返回空字符串
	 */
	public static String get(String urlStr, Map<String, String> requestHeaderMap, HttpURLConnection conn) {
		String stringBuffer = "";
		try {
			stringBuffer = getEx(urlStr, requestHeaderMap, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	/**
	 * get请求（可自定义Connection对象）（会抛异常）
	 * 
	 * @param urlStr
	 *            url字符串
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @param conn
	 *            Connection对象
	 * @return 返回响应体，响应码非200的话直接抛异常，异常字符串中包含响应码及响应体字符串
	 * @throws Exception
	 */
	public static String getEx(String urlStr, Map<String, String> requestHeaderMap, HttpURLConnection conn) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		InputStream inputStream = null;//远程连接响应本地的输入流
		try {
			// 设置请求头里面的各个属性
			setRequestHeader(conn, requestHeaderMap);
			conn.connect();// 表示连接
			int code = conn.getResponseCode();
			if (code == 200) {
				inputStream = conn.getInputStream();// 打开输入流
				// 根据请求头，判断处理方式（压缩格式的流需要使用GZIPInputStream特殊处理，否则乱码，而且并不是字符集的乱码）
				stringBuffer.append(ReadStringByInputStream(inputStream, requestHeaderMap));
			} else {
				throw new Exception("请求失败:" + code + ";" + ReadStringByInputStream(conn.getErrorStream(), requestHeaderMap));
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (conn!=null) {
				conn.disconnect();
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * post请求（自动生成Connection对象）（异常仅在控制台输出）
	 * 
	 * @param urlStr
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @param parm
	 *            键值对参数
	 * @return 返回响应体字符串，有异常返回空字符串
	 */
	public static String post(String urlStr, Map<String, String> requestHeaderMap, Map<String, String> parm) {
		return post(urlStr, requestHeaderMap, createKeyValueParamString(parm));
	}

	/**
	 * post请求（自动生成Connection对象）（会抛异常）
	 * 
	 * @param urlStr
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @param parm
	 *            键值对参数
	 * @return 返回响应体，响应码非200的话直接抛异常，异常字符串中包含响应码及响应体字符串
	 * @throws Exception
	 */
	public static String postEx(String urlStr, Map<String, String> requestHeaderMap, Map<String, String> parm) throws Exception {
		return postEx(urlStr, requestHeaderMap, createKeyValueParamString(parm));
	}

	/**
	 * post请求 （自动生成Connection对象）（异常仅在控制台输出）
	 * 
	 * @param urlStr
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @param parm
	 *            键值对参数
	 * @param connection
	 *            自定义连接对象
	 * @return 返回响应体字符串，有异常返回空字符串
	 */
	public static String post(String urlStr, Map<String, String> requestHeaderMap, Map<String, String> parm, HttpURLConnection connection) {
		return post(urlStr, requestHeaderMap, createKeyValueParamString(parm), connection);
	}

	/**
	 * post请求（可自定义Connection对象）（会抛异常）
	 * 
	 * @param urlStr
	 *            url字符串
	 * @param requestHeaderMap
	 *            请求头的信息
	 * @param parm
	 *            键值对参数
	 * @param connection
	 *            Connection对象
	 * @return 返回响应体，响应码非200的话直接抛异常，异常字符串中包含响应码及响应体字符串
	 * @throws Exception
	 */
	public static String postEx(String urlStr, Map<String, String> requestHeaderMap, Map<String, String> parm, HttpURLConnection connection) throws Exception {
		return postEx(urlStr, requestHeaderMap, createKeyValueParamString(parm), connection);
	}

	/**
	 * post请求（自动生成Connection对象）（异常仅在控制台输出）
	 * 
	 * @param POST_URL
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息 Content-Type参考：https://www.runoob.com/http/http-content-type.html https://www.cnblogs.com/wangyuxing/p/10037470.html
	 * 
	 *            application/x-www-form-urlencoded ：最常见的POST提交数据方式。 原生form默认的提交方式(可以使用enctype指定提交数据类型)。 jquery，zepto等默认post请求提交的方式。支持GET/POST等方法，所有数据变成键值对的形式 key1=value1&key2=value2 的形式，并且特殊字符需要转义成utf-8编号，如空格会变成 %20;
	 * 
	 *            multipart/form-data ： 使用表单上传文件时，必须指定表单的 enctype属性值为 multipart/form-data. 请求体被分割成多部分，每部分使用 --boundary分割；
	 * 
	 *            application/json： JSON数据格式，对于一些复制的数据对象，对象里面再嵌套数组的话，建议使用application/json传递比较好，开发那边也会要求使用application/json。因为他们那边不使用application/json的话，使用默认的application/x-www-form-urlencoded传递的话，开发那边先要解析成如上那样的， 然后再解析成json对象，如果对于比上面更复杂的json对象的话，那么他们那边是很解析的，所以直接json对象传递的话，对于他们来说更简单。
	 *            通过json的形式将数据发送给服务器。json的形式的优点是它可以传递结构复杂的数据形式，比如对象里面嵌套数组这样的形式等。
	 * 
	 *            application/octet-stream ： 二进制流数据（如常见的文件下载）
	 * @param parm
	 * @return 返回响应体字符串，有异常返回空字符串
	 */
	public static String post(String urlStr, Map<String, String> requestHeaderMap, String parm) {
		String stringBuffer = "";
		try {
			HttpURLConnection connection = createHttpUrlConnection(urlStr, "POST");
			stringBuffer = post(urlStr, requestHeaderMap, parm, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	/**
	 * post请求（自动生成Connection对象）（会抛异常）
	 * 
	 * @param POST_URL
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息 Content-Type参考：https://www.runoob.com/http/http-content-type.html https://www.cnblogs.com/wangyuxing/p/10037470.html
	 * 
	 *            application/x-www-form-urlencoded ：最常见的POST提交数据方式。 原生form默认的提交方式(可以使用enctype指定提交数据类型)。 jquery，zepto等默认post请求提交的方式。支持GET/POST等方法，所有数据变成键值对的形式 key1=value1&key2=value2 的形式，并且特殊字符需要转义成utf-8编号，如空格会变成 %20;
	 * 
	 *            multipart/form-data ： 使用表单上传文件时，必须指定表单的 enctype属性值为 multipart/form-data. 请求体被分割成多部分，每部分使用 --boundary分割；
	 * 
	 *            application/json： JSON数据格式，对于一些复制的数据对象，对象里面再嵌套数组的话，建议使用application/json传递比较好，开发那边也会要求使用application/json。因为他们那边不使用application/json的话，使用默认的application/x-www-form-urlencoded传递的话，开发那边先要解析成如上那样的， 然后再解析成json对象，如果对于比上面更复杂的json对象的话，那么他们那边是很解析的，所以直接json对象传递的话，对于他们来说更简单。
	 *            通过json的形式将数据发送给服务器。json的形式的优点是它可以传递结构复杂的数据形式，比如对象里面嵌套数组这样的形式等。
	 * 
	 *            application/octet-stream ： 二进制流数据（如常见的文件下载）
	 * @param parm
	 * @return 返回响应体，响应码非200的话直接抛异常，异常字符串中包含响应码及响应体字符串
	 * @throws Exception
	 */
	public static String postEx(String urlStr, Map<String, String> requestHeaderMap, String parm) throws Exception {
		HttpURLConnection connection = createHttpUrlConnection(urlStr, "POST");
		String stringBuffer = postEx(urlStr, requestHeaderMap, parm, connection);
		return stringBuffer;
	}

	/**
	 * （可自定义Connection对象）（异常仅在控制台输出）
	 * 
	 * @param POST_URL
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息 Content-Type参考：https://www.runoob.com/http/http-content-type.html https://www.cnblogs.com/wangyuxing/p/10037470.html
	 * 
	 *            application/x-www-form-urlencoded ：最常见的POST提交数据方式。 原生form默认的提交方式(可以使用enctype指定提交数据类型)。 jquery，zepto等默认post请求提交的方式。支持GET/POST等方法，所有数据变成键值对的形式 key1=value1&key2=value2 的形式，并且特殊字符需要转义成utf-8编号，如空格会变成 %20;
	 * 
	 *            multipart/form-data ： 使用表单上传文件时，必须指定表单的 enctype属性值为 multipart/form-data. 请求体被分割成多部分，每部分使用 --boundary分割；
	 * 
	 *            application/json： JSON数据格式，对于一些复制的数据对象，对象里面再嵌套数组的话，建议使用application/json传递比较好，开发那边也会要求使用application/json。因为他们那边不使用application/json的话，使用默认的application/x-www-form-urlencoded传递的话，开发那边先要解析成如上那样的， 然后再解析成json对象，如果对于比上面更复杂的json对象的话，那么他们那边是很解析的，所以直接json对象传递的话，对于他们来说更简单。
	 *            通过json的形式将数据发送给服务器。json的形式的优点是它可以传递结构复杂的数据形式，比如对象里面嵌套数组这样的形式等。
	 * 
	 *            application/octet-stream ： 二进制流数据（如常见的文件下载）
	 * @param parm
	 *            参数字符串
	 * @param connection
	 *            自定义的连接对象
	 * @return 返回响应体字符串，有异常返回空字符串
	 */
	public static String post(String urlStr, Map<String, String> requestHeaderMap, String parm, HttpURLConnection connection) {
		String stringBuffer = "";
		try {
			stringBuffer = postEx(urlStr, requestHeaderMap, parm, connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	/**
	 * post请求（可自定义Connection对象）（会抛异常）
	 * 
	 * @param POST_URL
	 *            请求的url
	 * @param requestHeaderMap
	 *            请求头的信息 Content-Type参考：https://www.runoob.com/http/http-content-type.html https://www.cnblogs.com/wangyuxing/p/10037470.html
	 * 
	 *            application/x-www-form-urlencoded ：最常见的POST提交数据方式。 原生form默认的提交方式(可以使用enctype指定提交数据类型)。 jquery，zepto等默认post请求提交的方式。支持GET/POST等方法，所有数据变成键值对的形式 key1=value1&key2=value2 的形式，并且特殊字符需要转义成utf-8编号，如空格会变成 %20;
	 * 
	 *            multipart/form-data ： 使用表单上传文件时，必须指定表单的 enctype属性值为 multipart/form-data. 请求体被分割成多部分，每部分使用 --boundary分割；
	 * 
	 *            application/json： JSON数据格式，对于一些复制的数据对象，对象里面再嵌套数组的话，建议使用application/json传递比较好，开发那边也会要求使用application/json。因为他们那边不使用application/json的话，使用默认的application/x-www-form-urlencoded传递的话，开发那边先要解析成如上那样的， 然后再解析成json对象，如果对于比上面更复杂的json对象的话，那么他们那边是很解析的，所以直接json对象传递的话，对于他们来说更简单。
	 *            通过json的形式将数据发送给服务器。json的形式的优点是它可以传递结构复杂的数据形式，比如对象里面嵌套数组这样的形式等。
	 * 
	 *            application/octet-stream ： 二进制流数据（如常见的文件下载）
	 * @param parm
	 *            参数字符串
	 * @param connection
	 *            自定义的连接对象
	 * @return 返回响应体，响应码非200的话直接抛异常，异常字符串中包含响应码及响应体字符串
	 */
	public static String postEx(String urlStr, Map<String, String> requestHeaderMap, String parm, HttpURLConnection connection) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		DataOutputStream dataout = null;//本地输出到远程连接的输出流
		InputStream inputStream = null;//远程连接响应本地的输入流
		try {
			if (requestHeaderMap != null) {
				if (requestHeaderMap.get("Content-Type") == null || requestHeaderMap.get("Content-Type").equals("")) {
					requestHeaderMap.put("Content-Type", "application/x-www-form-urlencoded");
				}
			}
			// 设置请求头里面的各个属性
			setRequestHeader(connection, requestHeaderMap);
			// 建立连接
			// (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			connection.connect();
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
			dataout = new DataOutputStream(connection.getOutputStream());
			// URLEncoder.encode("32", "utf-8"); // URLEncoder.encode()方法  为字符串进行编码
			// 将参数输出到连接
			// dataout.writeBytes(parm);//这好像会乱码
			dataout.write(parm.getBytes("UTF-8"));
			
			// 输出完成后刷新并关闭流
			dataout.flush();
			int code = connection.getResponseCode();
			if (code == 200) {
				inputStream = connection.getInputStream();// 打开输入流
				// 根据请求头，判断处理方式（压缩格式的流需要使用GZIPInputStream特殊处理，否则乱码，而且并不是字符集的乱码）
				stringBuffer.append(ReadStringByInputStream(inputStream, requestHeaderMap));
			} else {
				throw new Exception("请求失败:" + code + "  " + ReadStringByInputStream(connection.getErrorStream(), requestHeaderMap));
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if (null!=dataout) {
				try {
					dataout.close(); //关闭本地发送给远程连接的输出流 重要且易忽略步骤 (关闭流,切记!)
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null!=connection) {
				connection.disconnect();//关闭连接
			}
		}
		return stringBuffer.toString();
	}

	/**
	 * 下载一个远程文件输出到某个本地路径下，每次下载前会等待10毫秒，如果10毫秒一个字节都没接收到，则会进入指定的超时间进行等待（默认为5秒，可通过自定义httpURLConnection的setReadTimeout设置）
	 * 默认为GET请求，如需要其它类型请求，需自定义httpUrlConnection对象
	 * 默认没有请求头，如需要请求头，需调用自定义方法
	 * @param fileUrl 下载文件的地址
	 * @param localPath 下载后的文件保存路径
	 */
	public static void downloadRemoteFile(String fileUrl, String localPath) {
		downloadRemoteFile(fileUrl, localPath, null);
	}

	/**
	 * 下载一个远程文件输出到某个本地路径下，每次下载前会等待10毫秒，如果10毫秒一个字节都没接收到，则会进入指定的超时间进行等待（默认为5秒，可通过自定义httpURLConnection的setReadTimeout设置）
	 * 默认为GET请求，如需要其它类型请求，需自定义httpUrlConnection对象
	 * @param fileUrl 下载文件的地址
	 * @param localPath 下载后的文件保存路径
	 * @param requestHeaderMap 可自定义的请求头
	 */
	public static void downloadRemoteFile(String fileUrl, String localPath, Map<String, String> requestHeaderMap) {
		try {
			HttpURLConnection httpURLConnection = createHttpUrlConnection(fileUrl, "GET");
			downloadRemoteFile(fileUrl, localPath, requestHeaderMap, httpURLConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载一个远程文件输出到某个本地路径下，每次下载前会等待10毫秒，如果10毫秒一个字节都没接收到，则会进入指定的超时间进行等待（默认为5秒，可通过自定义httpURLConnection的setReadTimeout设置）
	 * 默认为GET请求，如需要其它类型请求，需自定义httpUrlConnection对象
	 * 默认没有请求头，如需要请求头，需调用自定义方法
	 * @param fileUrl 下载文件的地址
	 * @param localPath 下载后的文件保存路径
	 * @param requestHeaderMap 可自定义的请求头
	 * @param httpURLConnection 可自定义的连接对象,可通过createHttpUrlConnection方法创建
	 */
	public static void downloadRemoteFile(String fileUrl, String localPath, Map<String, String> requestHeaderMap, HttpURLConnection httpURLConnection) {
		try {
			File file = new File(localPath);
			if (!file.exists()) {
				if (!LoserStarFileUtil.createDir(localPath)) {
					throw new Exception("目录创建失败：" + LoserStarFileUtil.getDir(localPath));
				}
			}
			OutputStream outputStream = new FileOutputStream(new File(localPath));
			downloadRemoteFileToOutputStream(fileUrl, outputStream, requestHeaderMap, httpURLConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载一个远程文件输出到某个outputstream输出流中，每次下载前会等待10毫秒，如果10毫秒一个字节都没接收到，则会进入指定的超时间进行等待（默认为5秒，可通过自定义httpURLConnection的setReadTimeout设置）
	 * 默认为GET请求，如需要其它类型请求，需自定义httpUrlConnection对象
	 * 默认没有请求头，如需要请求头，需调用自定义方法
	 * @param fileUrl 下载文件的地址
	 * @param outputStream 输出流对象
	 */
	public static void downloadRemoteFileToOutputStream(String fileUrl, OutputStream outputStream) {
		downloadRemoteFileToOutputStream(fileUrl, outputStream, null);
	}

	/**
	 * 下载一个远程文件输出到某个outputstream输出流中，每次下载前会等待10毫秒，如果10毫秒一个字节都没接收到，则会进入指定的超时间进行等待（默认为5秒，可通过自定义httpURLConnection的setReadTimeout设置）
	 * 默认为GET请求，如需其它类型请求，需自定义httpUrlConnection对象
	 * @param fileUrl 下载文件的地址
	 * @param outputStream 输出流对象
	 * @param requestHeaderMap 可自定义的请求头
	 */
	public static void downloadRemoteFileToOutputStream(String fileUrl, OutputStream outputStream, Map<String, String> requestHeaderMap) {
		try {
			HttpURLConnection httpURLConnection = createHttpUrlConnection(fileUrl, "GET");
			downloadRemoteFileToOutputStream(fileUrl, outputStream, requestHeaderMap, httpURLConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载一个远程文件输出到某个outputstream输出流中，每次下载前会等待10毫秒，如果10毫秒一个字节都没接收到，则会进入指定的超时间进行等待（默认为5秒，可通过自定义httpURLConnection的setReadTimeout设置）
	 * @param fileUrl 下载文件的地址
	 * @param outputStream 输出流对象
	 * @param requestHeaderMap 可自定义的请求头
	 * @param httpURLConnection 可自定义的连接对象,可通过createHttpUrlConnection方法创建
	 */
	public static void downloadRemoteFileToOutputStream(String fileUrl, OutputStream outputStream, Map<String, String> requestHeaderMap, HttpURLConnection httpURLConnection) {
		InputStream inputStream = null;//远程连接响应本地的输入流
		try {
			setRequestHeader(httpURLConnection, requestHeaderMap);
			inputStream = httpURLConnection.getInputStream();// 打开URLConnection的输入流
			byte[] buf = LoserStarFileUtil.ReadByteByInputStreamTimeout(inputStream, httpURLConnection.getReadTimeout());
			LoserStarFileUtil.WriteBytesToOutputStream(buf, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (httpURLConnection!=null) {
				httpURLConnection.disconnect();
			}
		}
	}

	/**
	 * 上传附件
	 * 
	 * @param uploadUrl
	 *            上传的地址
	 * @param localPath
	 *            附件路径
	 * @param requestHeaderMap
	 *            请求头
	 * @param paraMap
	 *            额外参数
	 */
	public static String uploadRemoteFile(String uploadUrl, String localPath, Map<String, String> requestHeaderMap, Map<String, String> paraMap) {
		String returnStr = "";
		try {
			returnStr = uploadRemoteFile(uploadUrl, localPath, requestHeaderMap, paraMap, createHttpUrlConnection(uploadUrl, "POST"));
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStr;
	}

	/**
	 * 上传附件
	 * 
	 * @param uploadUrl
	 *            上传的地址
	 * @param localPath
	 *            附件路径
	 * @param requestHeaderMap
	 *            请求头
	 * @param paraMap
	 *            额外参数
	 * @param conn
	 *            自定义的连接对象
	 * @return
	 */
	public static String uploadRemoteFile(String uploadUrl, String localPath, Map<String, String> requestHeaderMap, Map<String, String> paraMap, HttpURLConnection conn) {
		String resultStr = "";
		String urlStr = uploadUrl;
		File file = new File(localPath);
		String BOUNDARY = UUID.randomUUID().toString().replaceAll("-", ""); // 边界标识 随机生成
		String PREFIX = "--";
		String LINE_END = "\r\n";// 行结束标记
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		DataOutputStream dos = null;//本地输出到远程连接的输出流
		InputStream input = null;//远程连接响应本地的输入流
		try {
			if (conn == null) {
				throw new Exception("HttpURLConnection对象为null");
			}
			conn = createHttpUrlConnection(urlStr, "POST");
			setRequestHeader(conn, requestHeaderMap);
			conn.setRequestProperty("Charset", "utf-8"); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			// 建立连接
			// (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			conn.connect();
			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				dos = new DataOutputStream(conn.getOutputStream());

				if (paraMap != null) {
					for (Map.Entry<String, String> entry : paraMap.entrySet()) {
						/**
						 * 额外的参数
						 */
						StringBuffer fileIdBuffer = new StringBuffer();
						fileIdBuffer.append(PREFIX + BOUNDARY + LINE_END);// 数据分隔线
						fileIdBuffer.append("Content-Disposition: form-data;name=\"" + entry.getKey() + "\"");
						fileIdBuffer.append(LINE_END);
						fileIdBuffer.append(LINE_END);
						fileIdBuffer.append(entry.getValue());
						fileIdBuffer.append(LINE_END);
						dos.write(fileIdBuffer.toString().getBytes());
					}
				}

				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX + BOUNDARY + LINE_END);// 数据分隔线
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件,不传也是可以的 filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/ctet-stream" + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024 * 1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);// 数据分隔线，结束线
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if (res == 200) {

				}
				System.out.println("response code:" + res);
				System.out.println("request success");
				input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}

				resultStr = sb1.toString();
				resultStr = new String(resultStr.getBytes("iso8859-1"), "utf-8");
				System.out.println("result : " + resultStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (dos!=null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (input!=null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn!=null) {
				conn.disconnect();//用完关闭连接
			}
		}
		return resultStr;
	}

	/**
	 * 示例
	 */
	public static void main(String[] args) {

		// 下载http文件
		// downloadRemoteFile("http://127.0.0.1:8080/EP/img/emule0.50a-Xtreme8.1.7z","c://test.7z");
		// downloadRemoteFile("http://xiazai.xiazaiba.com/Soft/N/NetTraffic_1.49.0_XiaZaiBa.zip","c://NetTraffic.zip");

		// 请求https不受信任的页面
		// String url = "https://bpmtst:9443/BPMHelp/index.jsp";
		// String result = get(url, null);
		// System.out.println(result);

		// 下载https不受信任的文件
		// downloadRemoteFile("https://bpmtst:9443/BPMHelp/ver362.3/advanced/images/e_contents_view.gif", "c://e_contents_view.gif");
		// LoserStarHttpUtil.downloadRemoteFile("https://xiazai.xiazaiba.com/Soft/P/P2PSearcher_6.4.8.exe", "c://P2PSearcher_6.4.8.exe");
		// System.out.println("1111111111111111");

		// 上传附件
		/*
		 * multipart/form-data post 方法提交表单，后台获取不到数据 这个和servlet容器有关系，比如tomcat等。
		 * 
		 * 1.get方式 get方式提交的话，表单项都保存在http header中，格式是 http://localhost:8080/hello.do?name1=value1&name2=value2这样的字符串。server端通过request.getParameter是可以取到值的。
		 * 
		 * 2.post方式（enctype为缺省的application/x-www-form-urlencoded） 表单数据都保存在http的正文部分，格式类似于下面这样：用request.getParameter是可以取到数据的
		 * 
		 * name1=value1&name2=value2
		 * 
		 * 3.post方式（enctype为multipart/form-data，多用于文件上传)
		 * 表单数据都保存在http的正文部分，各个表单项之间用boundary隔开。格式类似于下面这样：用request.getParameter是取不到数据的，这时需要通过request.getInputStream来取数据，不过取到的是个InputStream，所以无法直接获取指定的表单项（需要自己对取到的流进行解析，才能得到表单项以及上传的文件内容等信息）。这种需求属于比较共通的功能，所以有很多开源的组件可以直接利用。比如：apache的fileupload组件,smartupload等。通过这些开源的upload组件提供的API，
		 * 就可以直接从request中取得指定的表单项了。 jfinal的话需要照如下方式获取参数： MultipartRequest mRequest = new MultipartRequest(getRequest()); fileId = mRequest.getParameter("fileId");
		 */
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("fileId", "loserStarFileId" + UUID.randomUUID());
		String url = "http://127.0.0.1:8080/ExtWebService/contractPayFile/fileUpload.do";
		String resultString = uploadRemoteFile(url, "D:\\printDiskGroup.txt", null, paraMap);
		System.out.println(resultString);
	}
}
