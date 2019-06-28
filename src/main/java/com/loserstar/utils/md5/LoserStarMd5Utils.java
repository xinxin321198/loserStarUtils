/**
 * author: loserStar
 * date: 2019年3月27日上午10:33:30
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.md5;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * author: loserStar 
 * date: 2019年3月27日上午10:33:30 
 * remarks: 基于org.apache.commons.codec.digest.DigestUtils 此方法加密出的串是32位小写
 */
public class LoserStarMd5Utils {
	/**
	 * MD5加密方法
	 * 
	 * @param text 明文
	 * @param key 密钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String md5(String text) throws Exception {
		// 加密后的字符串
		String encodeStr = DigestUtils.md5Hex(text);
		System.out.println("MD5加密前的字符串为:" + text);
		System.out.println("MD5加密后的字符串为:" + encodeStr);
		return encodeStr;
	}

	public static void main(String[] args) {
		try {
			md5("admin");
		} catch (Exception e) {
		}
	}
}
