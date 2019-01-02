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
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.loserstar.utils.file.LoserStarFileUtil;


/**
 * author: loserStar
 * date: 2018年3月16日上午9:13:41
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarHttpUtil {
	/**
	 * 
	 * remarks:自己的x509证书信任管理器类,用于访问不受信任的https连接
	 */
	public static class MyX509TrustManager implements X509TrustManager{

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
	 * @param httpsConn
	 * @param requestHeaderMap
	 */
	private static void setRequestHeader(HttpURLConnection conn,Map<String, String> requestHeaderMap) {
		if (requestHeaderMap!=null) {
			Set<String> requestHeaderKeys = requestHeaderMap.keySet();
			for (String key : requestHeaderKeys) {
				conn.setRequestProperty(key, requestHeaderMap.get(key));
			}
		}
	}
	
	/**
	 * 创建一个http的请求连接对象（根据协议决定是创建http还是https,https是所有证书都信任的）
	 * @param urlStr
	 * @return
	 * @throws IOException
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private static HttpURLConnection createHttpUrlConnection(String urlStr,String method) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL url = new URL(urlStr);
		HttpURLConnection conn  = null;//可能是http可能是https
		if (url.getProtocol().equals("http")) {
			 conn  =(HttpURLConnection) url.openConnection();
		}else if(url.getProtocol().equals("https")) {
			//创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager ()};
			SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());

			//从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			//创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			HttpsURLConnection httpsConn = (HttpsURLConnection)url.openConnection();
			httpsConn.setSSLSocketFactory(ssf);
			conn = httpsConn;
		}
		conn.setRequestMethod(method);   //设置本次请求的方式 ， 默认是GET方式， 参数要求都是大写字母
		conn.setConnectTimeout(5000);//设置连接超时
		conn.setDoInput(true);//是否打开输入流 ， 此方法默认为true(post 请求是以流的方式隐式的传递参数)
		conn.setDoOutput(true);//是否打开输出流， 此方法默认为false
		// post请求缓存设为false
		conn.setUseCaches(false);
		// 设置该HttpURLConnection实例是否自动执行重定向
		conn.setInstanceFollowRedirects(true);
        return conn;
	}
	
	/**
	 * 工具内部使用的方法，从一个网络的inputStream流中，读取到字符串
	 * 并且根据请求头的Accept-Encoding判断是否是gzip格式，如果是gzip需特殊处理
	 * @param inputStream 远程输入流
	 * @param requestHeaderMap 请求头内容
	 * @return
	 */
	private static StringBuffer ReadStringByInputStream(InputStream inputStream,Map<String, String> requestHeaderMap) {
		StringBuffer stringBuffer = new StringBuffer();
		GZIPInputStream gZIPInputStream = null;
		InputStreamReader inputStreamReader = null;
		try {
				if (null!=requestHeaderMap&&requestHeaderMap.get("Accept-Encoding").contains("gzip")) {
					gZIPInputStream = new GZIPInputStream(inputStream);
					byte[] buf = LoserStarFileUtil.ReadByteByInputStream(gZIPInputStream);
					stringBuffer.append(new String(buf));
				}else {
				inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				//整行读,手动加上换行
				String result = null;
				while ((result =bufferedReader.readLine())!=null) {
					result+="\n";
					stringBuffer.append(result);
				}
				//单个字符读取
/*				int result = 0;
				bufferedReader.readLine();
				while ((result =bufferedReader.read())!=-1) {
					stringBuffer.append((char)result);
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return stringBuffer;
	}
	
	/**
	 * get请求
	 * @param urlStr url字符串
	 * @param requestHeaderMap 请求头的信息
	 * @return
	 */
	public static String get(String urlStr,Map<String, String> requestHeaderMap){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			HttpURLConnection conn = createHttpUrlConnection(urlStr,"GET");
			// 设置请求头里面的各个属性
			setRequestHeader(conn, requestHeaderMap);
            conn.connect();//表示连接
            int code = conn.getResponseCode();
            if (code==200) {
            	InputStream inputStream =  conn.getInputStream();//打开输入流
            	//根据请求头，判断处理方式（压缩格式的流需要使用GZIPInputStream特殊处理，否则乱码，而且并不是字符集的乱码）
            	stringBuffer.append(ReadStringByInputStream(inputStream,requestHeaderMap));
			}else {
				throw new Exception("请求失败:"+code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	
	/**
	 * post请求
	 * @param POST_URL 请求的url
	 * @param requestHeaderMap 请求头的信息		
	 * 		 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
			application/x-javascript text/xml->xml数据
			application/x-javascript->json对象
			application/x-www-form-urlencoded->表单数据
	 * @param parm  请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return
	 */
	public static String post(String urlStr,Map<String, String> requestHeaderMap,String parm) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			HttpURLConnection connection = createHttpUrlConnection(urlStr, "POST");
			// 设置请求头里面的各个属性
			setRequestHeader(connection, requestHeaderMap);
			// 建立连接
			// (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			connection.connect();
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
			DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
//			URLEncoder.encode("32", "utf-8"); // URLEncoder.encode()方法  为字符串进行编码
			// 将参数输出到连接
//			dataout.writeBytes(parm);//这好像会乱码
			dataout.write(parm.getBytes("UTF-8"));
			
			// 输出完成后刷新并关闭流
			dataout.flush();
			dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
			int code = connection.getResponseCode();
			if (code==200) {
	            	InputStream inputStream =  connection.getInputStream();//打开输入流
	            	//根据请求头，判断处理方式（压缩格式的流需要使用GZIPInputStream特殊处理，否则乱码，而且并不是字符集的乱码）
	            	stringBuffer.append(ReadStringByInputStream(inputStream,requestHeaderMap));
				}else {
					throw new Exception("请求失败:"+code);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	/**
	 * 下载一个远程的文件
	 * @param url 远程文件url
	 * @param localPath 本地存储路径（必须带文件名及后缀名）
	 */
	public static void downloadRemoteFile(String fileUrl,String localPath) {
		try {
			OutputStream outputStream = new FileOutputStream(new File(localPath));
			HttpURLConnection httpURLConnection = createHttpUrlConnection(fileUrl, "GET");
			InputStream inputStream = httpURLConnection.getInputStream();//打开URLConnection的输入流
			LoserStarFileUtil.WriteInputStreamToOutputStream(inputStream, outputStream);//把这个流的数据写到文件输出流中
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 示例
	 */
	public static void main(String[] args){
			
		//下载http文件
//		downloadRemoteFile("http://127.0.0.1:8080/EP/img/emule0.50a-Xtreme8.1.7z","c://test.7z");
		downloadRemoteFile("http://xiazai.xiazaiba.com/Soft/N/NetTraffic_1.49.0_XiaZaiBa.zip","c://NetTraffic.zip");
		
		//请求https不受信任的页面
		String url = "https://bpmtst:9443/BPMHelp/index.jsp";
		String result = get(url, null);
		System.out.println(result);
		
		//下载https不受信任的文件
		downloadRemoteFile("https://bpmtst:9443/BPMHelp/ver362.3/advanced/images/e_contents_view.gif", "c://e_contents_view.gif");
	}
}
