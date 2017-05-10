package com.loserstar.utils.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.nutz.http.Http;
import org.nutz.http.Response;

import jodd.io.FileUtil;
import jodd.io.StreamUtil;

public final class UrlUtil {

	public static String sendGet(String realURL) throws Exception {
		String result = "";
		BufferedReader in = null;
		URL url = new URL(realURL);
		URLConnection conn = url.openConnection();
		conn.connect();
		in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
		System.out.println("跨域请求的最终URL：" + sb.toString());
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

	/**
	 * 远程请求https的连接
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String httpsDoPost(String url, Map<String, String> params, String[][] headers) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ClientProtocolException,
			IOException, URISyntaxException {

		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

		CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		URIBuilder uriBuilder = new URIBuilder(url);
		for (String key : params.keySet()) {
			uriBuilder.setParameter(key, params.get(key));
		}

		HttpGet action = new HttpGet(uriBuilder.build());
		for (String[] header : headers) {
			action.setHeader(header[0], header[1]);
		}
		HttpResponse response = client.execute(action);
		byte[] bytes = StreamUtil.readBytes(response.getEntity().getContent());
		return new String(bytes);

	}

	/**
	 * 从一个http远程地址下载文件，保存到一个本地的地址
	 * @param remoteFilePath
	 * @param remoteFilePath
	 * @throws Exception
	 */
	public static void downloadFile(String remoteFilePath, String localFilePath) throws Exception {
		InputStream in = null;
		try {
			//生成目录
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
			
			System.out.println("正在下载文件：" + remoteFilePath + "--------------保存至本地：" + localFilePath);
			Response response = Http.get(remoteFilePath);
			in = response.getStream();
			FileUtil.writeStream(localFilePath, in);
//			com.dataagg.util.FileUtil.outPutFile(in, localFilePath);//这方法有问题
		} catch (Exception e) {
			throw e;
		} finally {
			StreamUtil.close(in);
		}
	}

	public static void main(String[] args) throws Exception {
		//测试用12301获取数据，https的方式
/*		String url = "https://opencomplain.12301e.com/process/prov/details";
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_type", "-1");
		params.put("start_day", "20160101");
		params.put("end_day", "20170428");
		params.put("start_page", "1");
		params.put("one_page_nums", "100");
*/
//		String[][] headers = { { "accept", "*/*" }, { "connection", "Keep-Alive" }, { "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)" }, { "Content-Type", "application/json" }, { "x-12301-key", "avbhvqlecaslnadhkcald38412dcaad" }, { "x-12301-version", "1.1" } };
/*		String returnStr = httpsDoPost(url, params, headers);
		System.out.println(returnStr);

		ObjectMapper jsonMapper = new ObjectMapper();
		VDocking12301Obj vDocking12301Obj = jsonMapper.readValue(returnStr, VDocking12301Obj.class);
		System.out.println(vDocking12301Obj);*/
	}

}
