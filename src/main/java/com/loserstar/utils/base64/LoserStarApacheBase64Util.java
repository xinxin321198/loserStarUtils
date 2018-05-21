/**
 * author: loserStar
 * date: 2018年5月16日上午9:54:57
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.base64;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.codec.binary.Base64;

import com.loserstar.utils.file.LoserStarFileUtil;


/**
 * author: loserStar
 * date: 2018年5月16日上午9:54:57
 * remarks:
 */
public class LoserStarApacheBase64Util {
	
	/**
	 * base64编码
	 * @param buf
	 * @return
	 */
	public static String encode(byte[] buf) {
		return  Base64.encodeBase64String(buf);
	}
	
	/**
	 * base64解码
	 * @param s
	 * @return
	 */
	public static byte[] decode(String s) {
		return Base64.decodeBase64(s);
	}
	
	public static void main(String[] args) {
		File file = new File("c://base64test.png");
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			int length = fileInputStream.available();
			String fileBase64Str = Base64.encodeBase64String(LoserStarFileUtil.ReadByteByInputStream(fileInputStream));
			System.out.println(fileBase64Str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
