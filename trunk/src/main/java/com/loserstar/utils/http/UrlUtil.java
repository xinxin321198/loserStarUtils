package com.loserstar.utils.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class UrlUtil {

	public static String sendGet(String realURL)throws Exception{  
        String result = "";    
        BufferedReader in = null;  
        URL url = new URL(realURL);  
        URLConnection conn = url.openConnection();  
        conn.connect();  
        in = new BufferedReader(    
                new InputStreamReader(conn.getInputStream()));    
        String line;    
        while ((line = in.readLine()) != null) {    
            result += line;    
        }  
        return result;  
    }  
	
	/**
	 * @Title: getDataFromURL
	 * @Description: 根据URL跨域获取输出结果，支持http
	 * @param strURL
	 *            要访问的URL地址
	 * @param param
	 *            参数
	 * @return 结果字符串
	 * @throws Exception
	 */
	public static String getDataFromURL(String strURL, Map<String, String> param) throws Exception {
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		final StringBuilder sb = new StringBuilder(param.size() << 4); // 4次方
		final Set<String> keys = param.keySet();
		for (final String key : keys) {
			final String value = param.get(key);
			sb.append(key); // 不能包含特殊字符
			sb.append('=');
			sb.append(value);
			sb.append('&');
		}
		// 将最后的 '&' 去掉
		sb.deleteCharAt(sb.length() - 1);
		writer.write(sb.toString());
		writer.flush();
		writer.close();

		InputStreamReader reder = new InputStreamReader(conn.getInputStream(), "utf-8");
		BufferedReader breader = new BufferedReader(reder);
//		 BufferedWriter w = new BufferedWriter(new FileWriter("d:/1.txt"));
		String content = "";
		String result = "";
		while ((content = breader.readLine()) != null) {
			result += content;
		}
//		w.write(result);
		return result;
	}
 

	public static void main(String[] args) throws Exception {
		try{
			String url = "http://feedback.api.juhe.cn/ISBN?sub=9787213076824&key=8c7a6ba75675f83f5003244ef712def9";
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "1");
			map.put("name", "昆明");
//			String msg = getDataFromURL(url, map);
			String msg  = sendGet(url);
			System.out.println(msg);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}