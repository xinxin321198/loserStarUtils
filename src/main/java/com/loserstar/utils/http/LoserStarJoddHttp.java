package com.loserstar.utils.http;

import java.util.HashMap;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * author: loserStar
 * date: 2018年3月30日下午2:27:22
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:基于jodd-http的自己封装的http工具类
 */
public class LoserStarJoddHttp {
	
	/**
	 * 内部使用生成httpResponse对象
	 *
	 * @param url
	 * @return
	 */
	public static HttpResponse buildResponse(String url){
		HttpResponse response = HttpRequest.get(url).send();
		return response;
	}
	
	
	/**
	 * 简单get请求获取body文本
	 *
	 * @param url
	 * @return
	 */
	public static String get(String url){
		return get(url, null);
	}
	
	
	/**
	 * get请求获取byte数组，原生字节流，用于下载文件
	 *
	 * @param url
	 * @return
	 */
	public static byte[] getForByte(String url){
		return buildResponse(url).bodyBytes();
	}
	
	/**
	 * post请求
	 *
	 * @param url
	 * @param name
	 * @param value
	 * @param parameters
	 * @return
	 */
	public static String post(String url,String name, Object value, Object... parameters){
		HttpRequest request = HttpRequest.post(url).form(name, value, parameters);
		HttpResponse response = request.send();
		return response.bodyText();
	}
	
	/**
	 * 指定返回数据编码的get请求
	 *
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String get(String url,String charset){
		HttpResponse response = buildResponse(url);
		response.charset((charset==null||charset.equals(""))?response.charset():charset);
		return response.bodyText();
	}
	
	public static void main(String[] args) {
//	    HttpResponse response = HttpRequest.get("http://www.cts-yn.com/plus/view.php?aid=118").send();
	    HttpResponse response = HttpRequest.get("http://www.cts-yn.com/plus/view.php").query("aid", new HashMap<>()).send();
//	    System.out.println(response.body());//通常是ISO-8859-1编码格式的基本报文内容。
//	    System.out.println(response.charset());//设置编码
//	    System.out.println(response.bodyText());//根据报文头"Content-Type"属性值的编码格式编译的报文。
//	   byte[] bodyByte = response.bodyBytes();//以字节数组形式返回的基本报文，例如下载一个要保存的文件。
	}
}
