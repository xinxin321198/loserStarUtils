/**
 * author: loserStar
 * date: 2018年3月16日上午9:13:41
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author: loserStar
 * date: 2018年3月16日上午9:13:41
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
public class LoserStarHttpUtil {
	
	/**
	 * get请求
	 *
	 * @param urlStr
	 * @return
	 */
	public static String get(String urlStr){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn  =(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");   //设置本次请求的方式 ， 默认是GET方式， 参数要求都是大写字母
            conn.setConnectTimeout(5000);//设置连接超时
            conn.setDoInput(true);//是否打开输入流 ， 此方法默认为true
            conn.setDoOutput(true);//是否打开输出流， 此方法默认为false
            conn.connect();//表示连接
            int code = conn.getResponseCode();
            if (code==200) {
            	InputStream inputStream =  conn.getInputStream();//打开输入流
            	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            	String result = null;
            
            	while ((result =bufferedReader.readLine())!=null) {
            		stringBuffer.append(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
	

	/**
	 * post请求
	 *
	 * @param POST_URL
	 */
	public static String post(String POST_URL) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			URL url = new URL(POST_URL);
			// 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接 
			// (标识一个url所引用的远程对象连接)
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
			// 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
			connection.setDoOutput(true);
			// 设置连接输入流为true
			connection.setDoInput(true);
			// 设置请求方式为post
			connection.setRequestMethod("POST");
			// post请求缓存设为false
			connection.setUseCaches(false);
			// 设置该HttpURLConnection实例是否自动执行重定向
			connection.setInstanceFollowRedirects(true);
			// 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
			// application/x-javascript text/xml->xml数据
			// application/x-javascript->json对象
			// application/x-www-form-urlencoded->表单数据
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 建立连接
			// (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
			connection.connect();
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
			DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
			String parm = "";
			//URLEncoder.encode("32", "utf-8"); // URLEncoder.encode()方法  为字符串进行编码
			// 将参数输出到连接
			dataout.writeBytes(parm);
			// 输出完成后刷新并关闭流
			dataout.flush();
			dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
			if (200==connection.getResponseCode()) {
			  	InputStream inputStream =  connection.getInputStream();//打开输入流
            	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            	String result = null;
            
            	while ((result =bufferedReader.readLine())!=null) {
            		stringBuffer.append(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}

	
	
	
	
	public static void main(String[] args) {
			String urlStr = "http://www.cts-yn.com/";
//			System.out.println(get(urlStr));
			try {
				URL url = new URL(urlStr);
				InputStream inputStream = url.openStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				byte[] buf = new byte[1024];
				while ((-1!=bufferedInputStream.read(buf))) {
					System.out.println(new String(buf,"utf-8"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	}
}
